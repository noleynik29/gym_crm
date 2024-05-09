package com.gym.demo.controller;

import com.gym.demo.model.ClientsPayments;
import com.gym.demo.service.ClientService;
import com.gym.demo.service.ClientsPaymentsService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.RedirectView;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Optional;

@Controller
@RestController
public class ClientsPaymentsController implements WebMvcConfigurer {

    private final ClientService clientService;

    private final ClientsPaymentsService clientsPaymentsService;

    public ClientsPaymentsController(ClientService clientService, ClientsPaymentsService clientsPaymentsService) {
        this.clientService = clientService;
        this.clientsPaymentsService = clientsPaymentsService;
    }

    @GetMapping("/clientsPayments")
    public ModelAndView getClientsPayments() {
        var modelAndView = new ModelAndView("clientsPayments");
        List<String> clientsNames = clientService.findAllNames();
        modelAndView.addObject("clientsNames", clientsNames);
        Optional<ClientsPayments> clientsPayments = clientsPaymentsService.getLatestClientsPayments();
        if (clientsPayments.isEmpty()) {
            modelAndView.addObject("noDataMessage", "Список посещений не найден");
        } else {
            modelAndView.addObject("clientsPayments", clientsPayments.get());
        }
        return modelAndView;
    }

    @PostMapping("/clientsPayments")
    public RedirectView createClientsPayments(@Valid @ModelAttribute("clientsPayments") ClientsPayments clientsPayments, Model model) {
        clientsPaymentsService.saveClientsPayments(clientsPayments);
        model.addAttribute("clientsPayments", clientsPayments);
        return new RedirectView("/clientsPayments");
    }

    @GetMapping("/clientsPayments/edit/{id}")
    public ModelAndView getClientsPaymentsByIdToChange(@PathVariable Long id) {
        var modelAndView = new ModelAndView("clientsPayments_edit");
        Optional<ClientsPayments> clientsPayments = clientsPaymentsService.getClientsPaymentsById(id);
        if (clientsPayments.isPresent()) {
            modelAndView.addObject("clientsPayments", clientsPayments.get());
        } else {
            modelAndView.setStatus(HttpStatus.NOT_FOUND);
        }
        return modelAndView;
    }

    @GetMapping("/clientsPayments/{id}")
    public ModelAndView getClientsPaymentsById(@PathVariable Long id) {
        var modelAndView = new ModelAndView("clientsPayments_see");
        Optional<ClientsPayments> clientsPayments = clientsPaymentsService.getClientsPaymentsById(id);
        if (clientsPayments.isPresent()) {
            modelAndView.addObject("clientsPayments", clientsPayments.get());
        } else {
            modelAndView.setStatus(HttpStatus.NOT_FOUND);
        }
        return modelAndView;
    }

    @PostMapping("/clientsPayments/{id}")
    public RedirectView updateClientsPayments(@Valid @ModelAttribute("clientsPayments") ClientsPayments clientsPayments, Model model) {
        clientsPaymentsService.updateClientsPayments(clientsPayments);
        model.addAttribute("clientsPayments", clientsPayments);
        return new RedirectView("/clientsPayments");
    }

    @GetMapping("/clientsPayments/delete/{clientsPaymentsId}")
    public RedirectView deleteClientsPayments(@PathVariable Long clientsPaymentsId) {
        clientsPaymentsService.deleteClientsPayments(clientsPaymentsId);
        return new RedirectView("/clientsPayments");
    }

    @GetMapping("/clientsPayments/allClientsPayments")
    public ModelAndView getAllClientsPayments() {
        var modelAndView = new ModelAndView("allClientsPayments");
        List<ClientsPayments> clientsPaymentsList = clientsPaymentsService.getAllClientsPayments();
        modelAndView.addObject("allClientsPayments", clientsPaymentsList);
        return modelAndView;
    }

    @GetMapping("/clientsPayments/export/{clientsPaymentsId}")
    public void exportClientsPayments(@PathVariable Long clientsPaymentsId, HttpServletResponse response) throws IOException {
        ClientsPayments clientsPayments = clientsPaymentsService.getClientsPaymentsById(clientsPaymentsId).get();
        Workbook workbook = clientsPaymentsService.createExcel(clientsPayments);

        response.setContentType("application/vnd.ms-excel");
        String filename = "Феникс посещение  " + clientsPayments.getName();
        String encodedFilename = URLEncoder.encode(filename, "UTF-8").replace("+", "%20");
        response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + encodedFilename + ".xlsx");

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        outputStream.close();
        workbook.close();
    }
}
