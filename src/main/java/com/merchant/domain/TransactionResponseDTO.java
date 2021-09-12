package com.merchant.domain;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class TransactionResponseDTO {
    private String uuid;
    private String description;
    private TransactionStatus status;
    private String amount;
    private String currency;
    private LocalDateTime dateCreated;
    private String merchantUuid;
    private String merchantName;

}
