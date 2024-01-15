package com.gym.demo.controller;

import com.gym.demo.model.Client;
import com.gym.demo.model.TrainingProgram;
import com.gym.demo.service.ClientService;
import com.gym.demo.service.TrainingProgramService;
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
public class TrainingProgramController implements WebMvcConfigurer {
    private final ClientService clientService;

    private final TrainingProgramService trainingProgramService;

    public TrainingProgramController(ClientService clientService, TrainingProgramService trainingProgramService) {
        this.clientService = clientService;
        this.trainingProgramService = trainingProgramService;
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
}
