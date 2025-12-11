package com.viswa.crm.dto.deal;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class CreateDealRequest {

    @NotBlank(message = "Deal title is required")
    @Size(max = 150)
    private String title;

    @NotNull(message = "Deal amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
    private BigDecimal amount;

    @NotNull(message = "Assigned user is required")
    private Long assignedUserId;

    @NotNull(message = "Company is required")
    private Long companyId;

    private Long contactId;
}
