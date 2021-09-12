package com.merchant.domain;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class TransactionSaveDTO {
    private String description;

    @NotNull
    private TransactionStatus status;

    @NotBlank
    private String amount;

    @NotBlank
    private String currency;

    @NotBlank
    private String merchantUuid;

    @NotBlank
    private String merchantName;
}
