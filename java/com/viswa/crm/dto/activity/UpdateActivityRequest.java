package com.viswa.crm.dto.activity;

import com.viswa.crm.model.ActivityStatus;
import com.viswa.crm.model.ActivityType;
import javax.validation.constraints.FutureOrPresent;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter

@Data
public class UpdateActivityRequest {

    private ActivityType type;

    private ActivityStatus status;

    private String description;

    @FutureOrPresent
    private LocalDate dueDate;
}
