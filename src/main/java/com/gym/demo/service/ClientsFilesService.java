package com.gym.demo.service;

import com.gym.demo.model.Client;
import com.gym.demo.model.ClientsFiles;
import com.gym.demo.repository.ClientRepository;
import com.gym.demo.repository.ClientsFilesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.webjars.NotFoundException;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ClientsFilesService {

    @Autowired
    private final ClientRepository clientRepository;
    @Autowired
    private final ClientsFilesRepository clientsFilesRepository;
    @Autowired
    private final ResourceLoader resourceLoader;

    public ClientsFilesService(ClientRepository clientRepository, ClientsFilesRepository clientsFilesRepository, ResourceLoader resourceLoader) {
        this.clientRepository = clientRepository;
        this.clientsFilesRepository = clientsFilesRepository;
        this.resourceLoader = resourceLoader;
    }

    public List<ClientsFiles> getFilesByUserId(Long userId) {
        return clientsFilesRepository.findByClientId(userId);
    }

    public Optional<ClientsFiles> getFileById(Long fileId) { return clientsFilesRepository.findById(fileId); }

    public void attachFileToUser(Long userId, MultipartFile file) throws IOException {
        Client client = clientRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        String clientsName = client.getId() + " " + client.getName() + " " + client.getSurname();

        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        ClientsFiles clientsFile = new ClientsFiles();
        clientsFile.setFileName(fileName);
        clientsFile.setClientId(userId);

        String clientsFolderPath = System.getProperty("user.home") + "\\Desktop\\Феникс Файлы Клиентов\\";
        File clientsDirectory = new File(clientsFolderPath);
        if (!clientsDirectory.exists()){
            clientsDirectory.mkdir();
        }

        String folderPath = System.getProperty("user.home") + "\\Desktop\\Феникс Файлы Клиентов\\" + clientsName + "\\";
        File directory = new File(folderPath);
        if (!directory.exists()){
            directory.mkdir();
        }
        Path path = Paths.get(folderPath + fileName);
        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        clientsFilesRepository.save(clientsFile);
    }

    public void detachFileFromUser(Long userId, Long fileId) throws IOException {
        Client client = clientRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        String clientsName = client.getId() + " " + client.getName() + " " + client.getSurname();
        ClientsFiles clientsFile = clientsFilesRepository.findById(fileId)
                .orElseThrow(() -> new NotFoundException("File not found"));

        if (!clientsFile.getClientId().equals(userId)) {
            throw new AccessDeniedException("File does not belong to the specified user");
        }

        String folderPath = System.getProperty("user.home") + "\\Desktop\\Феникс Файлы Клиентов\\" + clientsName + "\\";
        Path filePath = Paths.get(folderPath + clientsFile.getFileName());
        Files.delete(filePath);

        clientsFilesRepository.deleteById(fileId);
    }

    public ResponseEntity<Resource> downloadFile(Long fileId, Long userId) {
        ClientsFiles clientsFile = clientsFilesRepository.findById(fileId)
                .orElseThrow(() -> new NotFoundException("File not found"));
        Client client = clientRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        String clientsName = client.getId() + " " + client.getName() + " " + client.getSurname();

        String folderPath = System.getProperty("user.home") + "\\Desktop\\Феникс Файлы Клиентов\\" + clientsName + "\\";
        String filePath = folderPath + clientsFile.getFileName();

        try {
            Resource resource = resourceLoader.getResource("file:" + filePath);

            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + clientsFile.getFileName() + "\"")
                        .body(resource);
            } else {
                throw new NotFoundException("File not found or not readable");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while trying to download the file", e);
        }
    }
}
