package com.merchant.service;

import com.merchant.domain.Transaction;
import com.merchant.exception.TransactionNotFoundException;
import com.merchant.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Transactional
@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;

    public List<Transaction> findAll(Map<String, String> filterParams) {
        TransactionSpecification spec = new TransactionSpecification((filterParams));
        return transactionRepository.findAll(spec);
    }

    public Transaction findByUuid(String uuid) {
        return transactionRepository.findByUuid(uuid).orElseThrow(() -> new TransactionNotFoundException(uuid));
    }

    public void saveTransaction(Transaction transaction) {
        transaction.setDateCreated(LocalDateTime.now(ZoneId.of("UTC")));
        transactionRepository.save(transaction);
    }

    //maybe in the real world this should be a soft delete?
    public void deleteTransaction(String uuid) {
        Optional<Transaction> optionalTransaction = transactionRepository.findByUuid(uuid);
        Transaction transaction = optionalTransaction.orElseThrow(() -> new TransactionNotFoundException(uuid));
        transactionRepository.delete(transaction);
    }

    public void updateTransaction(Transaction newTransaction, String uuid) {
        Transaction transaction = transactionRepository.findByUuid(uuid).orElseThrow(() -> new TransactionNotFoundException(uuid));
        if (StringUtils.isNotBlank(newTransaction.getMerchantUuid())) {
            transaction.setMerchantUuid(newTransaction.getMerchantUuid());
        }
        if (StringUtils.isNotBlank(newTransaction.getMerchantName())) {
            transaction.setMerchantName(newTransaction.getMerchantName());
        }
        if (newTransaction.getStatus() != null) {
            transaction.setStatus(newTransaction.getStatus());
        }
        if (StringUtils.isNotBlank(newTransaction.getAmountAsString())) {
            transaction.setAmountAsString(newTransaction.getAmountAsString());
        }
        if (StringUtils.isNotBlank(newTransaction.getCurrency())) {
            transaction.setCurrency(newTransaction.getCurrency());
        }
        transaction.setDescription(newTransaction.getDescription());
        transactionRepository.save(transaction);
    }
}
