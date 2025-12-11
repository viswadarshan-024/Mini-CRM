package com.viswa.crm.dto.deal;

import com.viswa.crm.model.DealStatus;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class ChangeDealStatusRequest {

    @NotNull
    private DealStatus status;
}
