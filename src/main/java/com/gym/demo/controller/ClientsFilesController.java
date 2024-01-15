package com.gym.demo.controller;

import com.gym.demo.model.Client;
import com.gym.demo.model.ClientsFiles;
import com.gym.demo.service.ClientService;
import com.gym.demo.service.ClientsFilesService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.RedirectView;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
@RestController
public class ClientsFilesController implements WebMvcConfigurer {

    private final ClientService clientService;
    private final ClientsFilesService clientsFilesService;

    public ClientsFilesController(ClientService clientService, ClientsFilesService clientsFilesService) {
        this.clientService = clientService;
        this.clientsFilesService = clientsFilesService;
    }

    @GetMapping("/clients/{id}/clients_files")
    public ModelAndView getClientsFilesById(@PathVariable Long id) {
        var modelAndView = new ModelAndView("clients_files");
        Optional<Client> client = clientService.getClientById(id);
        List<ClientsFiles> clientsFiles = clientsFilesService.getFilesByUserId(id);
        if (client.isPresent()) {
            modelAndView.addObject("client", client.get());
            if (!clientsFiles.isEmpty()){
                modelAndView.addObject("clients_files", clientsFiles);
            } else {
                modelAndView.addObject("noDataMessage", "Файлы данного клиента не найдены");
            }
        } else {
            modelAndView.setStatus(HttpStatus.NOT_FOUND);
        }
        return modelAndView;
    }

    @PostMapping("/clients/{id}/clients_files")
    public RedirectView saveClientsFile(@Valid @ModelAttribute("clients_files") @PathVariable Long id, @RequestParam("file") MultipartFile file, Model model) throws IOException {
        clientsFilesService.attachFileToUser(id, file);
        clientService.getClientById(id).ifPresent(client -> model.addAttribute("client", client));
        model.addAttribute("clients_files", file);
        return new RedirectView("/clients/" + id + "/clients_files");
    }

    @GetMapping("/clients/{clientId}/clients_files/{fileId}")
    public ResponseEntity<Resource> downloadClientFile(@PathVariable Long clientId, @PathVariable Long fileId) {
        return clientsFilesService.downloadFile(fileId, clientId);
    }

    @GetMapping("/clients/{clientId}/clients_files/delete/{fileId}")
    public RedirectView deleteClientFile(@PathVariable Long clientId, @PathVariable Long fileId) throws IOException {
        clientsFilesService.detachFileFromUser(clientId, fileId);
        return new RedirectView("/clients/" + clientId + "/clients_files");
    }
}
