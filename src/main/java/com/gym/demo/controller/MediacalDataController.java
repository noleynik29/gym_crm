package com.gym.demo.controller;

import com.gym.demo.model.Client;
import com.gym.demo.model.MedicalData;
import com.gym.demo.service.ClientService;
import com.gym.demo.service.MedicalDataService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.RedirectView;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RestController
public class MediacalDataController implements WebMvcConfigurer {

    private final ClientService clientService;
    private final MedicalDataService medicalDataService;

    public MediacalDataController(ClientService clientService, MedicalDataService medicalDataService) {
        this.clientService = clientService;
        this.medicalDataService = medicalDataService;
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
}
