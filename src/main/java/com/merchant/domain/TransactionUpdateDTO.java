package com.merchant.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransactionUpdateDTO {
    private String description;
    private TransactionStatus status;
    private String amount;
    private String currency;
    private String merchantUuid;
    private String merchantName;
}
