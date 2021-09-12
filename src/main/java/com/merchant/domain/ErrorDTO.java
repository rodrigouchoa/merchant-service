package com.merchant.domain;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ErrorDTO {
    private String httpErrorCode;
    private String errorMessage;
}
