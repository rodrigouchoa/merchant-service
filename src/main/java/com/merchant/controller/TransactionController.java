package com.merchant.controller;

import com.merchant.domain.Transaction;
import com.merchant.domain.TransactionResponseDTO;
import com.merchant.domain.TransactionSaveDTO;
import com.merchant.domain.TransactionUpdateDTO;
import com.merchant.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toUnmodifiableList;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @GetMapping
    public List<TransactionResponseDTO> findAll(@RequestParam Map<String, String> params) {
        List<Transaction> transactions = transactionService.findAll(params);
        List<TransactionResponseDTO> dtos = transactions.stream().map(this::convertToDTO).collect(toUnmodifiableList());
        return dtos;
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<TransactionResponseDTO> findByUuid(@PathVariable String uuid) {
        Transaction transaction = transactionService.findByUuid(uuid);
        TransactionResponseDTO dto = convertToDTO(transaction);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{uuid}")
    public void delete(@PathVariable String uuid) {
        transactionService.deleteTransaction(uuid);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> save(@Valid @RequestBody TransactionSaveDTO dto) {
        Transaction transaction = convertSaveToEntity(dto);
        transactionService.saveTransaction(transaction);
        return ResponseEntity.status(HttpStatus.CREATED)
                .header(HttpHeaders.LOCATION, "/v1/transactions/" + transaction.getUuid()).build();
    }

    @PutMapping(value = "/{uuid}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void update(@RequestBody TransactionUpdateDTO dto, @PathVariable String uuid) {
        Transaction newTransaction = convertUpdateToEntity(dto);
        transactionService.updateTransaction(newTransaction, uuid);
    }

    private TransactionResponseDTO convertToDTO(Transaction transaction) {
        return TransactionResponseDTO.builder()
                .uuid(transaction.getUuid())
                .amount(transaction.getAmountAsString())
                .currency(transaction.getCurrency())
                .description(transaction.getDescription())
                .dateCreated(transaction.getDateCreated())
                .status(transaction.getStatus())
                .merchantName(transaction.getMerchantName())
                .merchantUuid(transaction.getMerchantUuid())
                .build();
    }

    private Transaction convertSaveToEntity(TransactionSaveDTO dto) {
        Transaction t = new Transaction();
        t.setCurrency(dto.getCurrency());
        t.setDescription(dto.getDescription());
        t.setStatus(dto.getStatus());
        t.setAmountAsString(dto.getAmount());
        t.setMerchantUuid(dto.getMerchantUuid());
        t.setMerchantName(dto.getMerchantName());
        return t;
    }

    private Transaction convertUpdateToEntity(TransactionUpdateDTO dto) {
        Transaction t = new Transaction();
        t.setDescription(dto.getDescription());
        t.setStatus(dto.getStatus());
        if (StringUtils.isNotBlank(dto.getAmount())) {
            t.setAmountAsString(dto.getAmount());
        }
        t.setCurrency(dto.getCurrency());
        t.setMerchantUuid(dto.getMerchantUuid());
        t.setMerchantName(dto.getMerchantName());
        return t;
    }
}
