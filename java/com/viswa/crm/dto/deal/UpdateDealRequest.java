package com.viswa.crm.dto.deal;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class UpdateDealRequest {

    @Size(max = 150)
    private String title;

    @DecimalMin("0.01")
    private BigDecimal amount;

    private Long assignedUserId;
    private Long contactId;
}
