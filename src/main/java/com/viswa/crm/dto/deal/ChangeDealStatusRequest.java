package com.viswa.crm.dto.deal;

import com.viswa.crm.model.DealStatus;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ChangeDealStatusRequest {

    @NotNull
    private DealStatus status;
}
