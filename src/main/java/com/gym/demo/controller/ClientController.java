package com.gym.demo.controller;

import com.gym.demo.model.Client;
import com.gym.demo.service.ClientService;
import com.gym.demo.service.TrainingProgramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.RedirectView;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RestController
public class ClientController implements WebMvcConfigurer {

    private final ClientService clientService;


    private final TrainingProgramService trainingProgramService;

    @Autowired
    public ClientController(ClientService clientService, TrainingProgramService trainingProgramService) {
        this.clientService = clientService;
        this.trainingProgramService = trainingProgramService;
    }

    @GetMapping("/")
    public ModelAndView getAll(@RequestParam(required = false) String keyword,
                         @RequestParam(defaultValue = "1") int page,
                         @RequestParam(defaultValue = "10") int size) {
        ModelAndView modelAndView = new ModelAndView("clients");
        try {
            List<Client> clients = new ArrayList<Client>();

            Pageable pageable = PageRequest.of(page - 1, size, Sort.by("name"));
            Page<Client> pageClients;
            if (keyword == null) {
                pageClients = clientService.findAllPageable(pageable);
            } else {
                pageClients = clientService.findContainingIgnoreCase(keyword, pageable);
                modelAndView.addObject("keyword", keyword);
            }

            clients = pageClients.getContent();

            modelAndView.addObject("clients", clients);
            modelAndView.addObject("currentPage", pageClients.getNumber() + 1);
            modelAndView.addObject("totalItems", pageClients.getTotalElements());
            modelAndView.addObject("totalPages", pageClients.getTotalPages());
            modelAndView.addObject("pageSize", size);
        } catch (Exception e) {
            modelAndView.addObject("message", e.getMessage());
        }

        return modelAndView;
    }

    @GetMapping("/clients/{id}")
    public ModelAndView getClientById(@PathVariable Long id) {
        var modelAndView = new ModelAndView("client");
        Optional<Client> client = clientService.getClientById(id);
        if (client.isPresent()) {
            modelAndView.addObject("client", client.get());
        } else {
            modelAndView.setStatus(HttpStatus.NOT_FOUND);
        }
        return modelAndView;
    }

    @GetMapping("/{id}")
    public ModelAndView getClientByIdToChange(@PathVariable Long id) {
        var modelAndView = new ModelAndView("edit");
        Optional<Client> client = clientService.getClientById(id);
        if (client.isPresent()) {
            modelAndView.addObject("client", client.get());
        } else {
            modelAndView.setStatus(HttpStatus.NOT_FOUND);
        }
        return modelAndView;
    }

    @PostMapping("/clients/{id}")
    public RedirectView updateClient(@Valid @ModelAttribute("client") Client client, Model model) {
        clientService.updateClient(client);
        model.addAttribute("client", client);
        return new RedirectView("/success-edit");
    }

    @PostMapping("/clients")
    public RedirectView createClient(@Valid @ModelAttribute("client") Client client, Model model) {
        clientService.saveClient(client);
        model.addAttribute("client", client);
        return new RedirectView("/success");
    }

    @GetMapping("/clients/delete/{id}")
    public RedirectView deleteClient(@PathVariable Long id) {
        clientService.deleteClient(id);
        return new RedirectView("/");
    }
}