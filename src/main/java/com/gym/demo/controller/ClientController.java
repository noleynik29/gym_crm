package com.gym.demo.controller;

import com.gym.demo.model.Client;
import com.gym.demo.model.MedicalData;
import com.gym.demo.model.TrainingProgram;
import com.gym.demo.service.ClientService;
import com.gym.demo.service.MedicalDataService;
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
    private final MedicalDataService medicalDataService;

    private final TrainingProgramService trainingProgramService;

    @Autowired
    public ClientController(ClientService clientService, MedicalDataService medicalDataService, TrainingProgramService trainingProgramService) {
        this.clientService = clientService;
        this.medicalDataService = medicalDataService;
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

    @GetMapping("/clients/{id}/medical_data")
    public ModelAndView getClientMedicalDataById(@PathVariable Long id) {
        var modelAndView = new ModelAndView("medical_data");
        Optional<Client> client = clientService.getClientById(id);
        Optional<MedicalData> medicalData = medicalDataService.getLatestMedicalData(id);
        if (client.isPresent()) {
            modelAndView.addObject("client", client.get());
            if (medicalData.isPresent()){
                modelAndView.addObject("medicalData", medicalData.get());
            } else {
                modelAndView.addObject("noDataMessage", "Медицинские данные по данному клиенту не найдены");
            }
        } else {
            modelAndView.setStatus(HttpStatus.NOT_FOUND);
        }
        return modelAndView;
    }

    @PostMapping("/clients/{id}/medical_data")
    public RedirectView createClientMedicalData(@Valid @ModelAttribute("medical_data") @PathVariable Long id, MedicalData medicalData, Model model) {
        medicalDataService.saveMedicalData(id, medicalData);
        clientService.getClientById(id).ifPresent(client -> model.addAttribute("client", client));
        model.addAttribute("medicalData", medicalData);
        return new RedirectView("/clients/" + id + "/medical_data");
    }

    @GetMapping("/clients/{clientId}/medical_data/delete/{medicalDataId}")
    public RedirectView deleteClientMedicalData(@PathVariable Long clientId, @PathVariable Long medicalDataId) {
        medicalDataService.deleteMedicalData(medicalDataId);
        return new RedirectView("/clients/" + clientId + "/medical_data");
    }

    @GetMapping("/clients/{clientId}/medical_data/allMedicalData/delete/{medicalDataId}")
    public RedirectView deleteClientMedicalDataFromAllMedicalDataPage(@PathVariable Long clientId, @PathVariable Long medicalDataId) {
        medicalDataService.deleteMedicalData(medicalDataId);
        return new RedirectView("/clients/" + clientId + "/medical_data/allMedicalData");
    }

    @GetMapping("clients/{clientId}/medical_data/allMedicalData")
    public ModelAndView getAllMedicalDataByClientId(@PathVariable Long clientId) {
        var modelAndView = new ModelAndView("allMedicalData");
        List<MedicalData> medicalDataList = medicalDataService.getAllMedicalDataByClientId(clientId);
        clientService.getClientById(clientId).ifPresent(client -> modelAndView.addObject("client", client));
        modelAndView.addObject("allMedicalData", medicalDataList);
        return modelAndView;
    }

    @GetMapping("/clients/{id}/trainingProgram")
    public ModelAndView getClientTrainingProgramById(@PathVariable Long id) {
        var modelAndView = new ModelAndView("trainingProgram");
        Optional<Client> client = clientService.getClientById(id);
        Optional<TrainingProgram> trainingProgram = trainingProgramService.getLatestTrainingProgram(id);
        if (client.isPresent()) {
            modelAndView.addObject("client", client.get());
            if (trainingProgram.isPresent()){
                modelAndView.addObject("trainingProgram", trainingProgram.get());
            } else {
                modelAndView.addObject("noDataMessage", "Программ тренировок по данному клиенту не найдено");
            }
        } else {
            modelAndView.setStatus(HttpStatus.NOT_FOUND);
        }
        return modelAndView;
    }

    @PostMapping("/clients/{id}/trainingProgram")
    public RedirectView createClientTrainingProgram(@Valid @ModelAttribute("trainingProgram") @PathVariable Long id, TrainingProgram trainingProgram, Model model) {
        trainingProgramService.saveTrainingProgram(id, trainingProgram);
        clientService.getClientById(id).ifPresent(client -> model.addAttribute("client", client));
        model.addAttribute("trainingProgram", trainingProgram);
        return new RedirectView("/clients/" + id + "/trainingProgram");
    }

    @GetMapping("/clients/{clientId}/trainingProgram/delete/{trainingProgramId}")
    public RedirectView deleteClientTrainingProgram(@PathVariable Long clientId, @PathVariable Long trainingProgramId) {
        trainingProgramService.deleteTrainingProgram(trainingProgramId);
        return new RedirectView("/clients/" + clientId + "/trainingProgram");
    }

    @GetMapping("/clients/{clientId}/trainingProgram/allTrainingProgram/delete/{trainingProgramId}")
    public RedirectView deleteClientTrainingProgramFromAllMedicalDataPage(@PathVariable Long clientId, @PathVariable Long trainingProgramId) {
        trainingProgramService.deleteTrainingProgram(trainingProgramId);
        return new RedirectView("/clients/" + clientId + "/trainingProgram/allTrainingProgram");
    }

    @GetMapping("clients/{clientId}/trainingProgram/allTrainingProgram")
    public ModelAndView getAllTrainingProgramByClientId(@PathVariable Long clientId) {
        var modelAndView = new ModelAndView("allTrainingProgram");
        List<TrainingProgram> trainingProgramList = trainingProgramService.getAllTrainingProgramByClientId(clientId);
        clientService.getClientById(clientId).ifPresent(client -> modelAndView.addObject("client", client));
        modelAndView.addObject("allTrainingProgram", trainingProgramList);
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
        return new RedirectView("success");
    }

    @GetMapping("/clients/delete/{id}")
    public RedirectView deleteClient(@PathVariable Long id) {
        clientService.deleteClient(id);
        return new RedirectView("/");
    }
}