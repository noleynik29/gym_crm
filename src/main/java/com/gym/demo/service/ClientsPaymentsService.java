package com.gym.demo.service;

import com.gym.demo.model.ClientsPayments;
import com.gym.demo.repository.ClientRepository;
import com.gym.demo.repository.ClientsPaymentsRepository;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ClientsPaymentsService {
    private final ClientsPaymentsRepository clientsPaymentsRepository;

    private final ClientRepository clientRepository;

    @Autowired
    public ClientsPaymentsService(ClientsPaymentsRepository clientsPaymentsRepository, ClientRepository clientRepository) {
        this.clientsPaymentsRepository = clientsPaymentsRepository;
        this.clientRepository = clientRepository;
    }

    @Transactional
    public ClientsPayments saveClientsPayments(ClientsPayments clientsPayments) {
        return clientsPaymentsRepository.save(clientsPayments);
    }

    @Transactional
    public Optional<ClientsPayments> getLatestClientsPayments() {
        return clientsPaymentsRepository.findFirstByOrderByDateOfCreation();
    }

    @Transactional
    public void deleteClientsPayments(Long id) {
        clientsPaymentsRepository.deleteById(id);
    }

    @Transactional
    public Optional<ClientsPayments> getClientsPaymentsById(Long id) {
        return clientsPaymentsRepository.findById(id);
    }

    @Transactional
    public List<ClientsPayments> getAllClientsPayments() {
        return clientsPaymentsRepository.findAll();
    }

    @Transactional
    public void updateClientsPayments(ClientsPayments clientsPayments) {
        if (clientsPaymentsRepository.existsById(clientsPayments.getId())) {
            clientsPaymentsRepository.save(clientsPayments);
        } else {
            throw new IllegalArgumentException("Client with ID " + clientsPayments.getId() + " does not exist.");
        }
    }

    public Workbook createExcel(ClientsPayments clientsPayments) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Феникс посещение " + clientsPayments.getName());

        // Create header row
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Посетители");
        headerRow.createCell(1).setCellValue("Нал");
        headerRow.createCell(2).setCellValue("Без/н");
        headerRow.createCell(3).setCellValue("Дата оплаты");

        // Populate data rows
        List<String> clientNames = clientsPayments.getClients();
        List<BigDecimal> cash_payments = clientsPayments.getCashPayments();
        List<BigDecimal> card_payments = clientsPayments.getCardPayments();
        List<Date> paymentDates = clientsPayments.getDates();
        SimpleDateFormat outputFormatter = new SimpleDateFormat("dd.MM.yyyy");

        int numRows = Math.min(clientNames.size(), Math.min(cash_payments.size(), paymentDates.size()));
        for (int i = 0; i < numRows; i++) {
            Row row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(clientNames.get(i));
            String cash_payment = (cash_payments.get(i) != null) ? String.valueOf(cash_payments.get(i)) : "";
            row.createCell(1).setCellValue(cash_payment);
            String card_payment = (card_payments.get(i) != null) ? String.valueOf(card_payments.get(i)) : "";
            row.createCell(2).setCellValue(card_payment);
            String formattedDate = (paymentDates.get(i) != null) ? outputFormatter.format(paymentDates.get(i)) : "";
            row.createCell(3).setCellValue(String.valueOf(formattedDate));
        }

        return workbook;
    }
}
