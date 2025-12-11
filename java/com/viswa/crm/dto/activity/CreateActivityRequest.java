package com.viswa.crm.dto.activity;

import com.viswa.crm.model.ActivityType;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter

@Data
public class CreateActivityRequest {

    @NotNull(message = "Deal is required")
    private Long dealId;

    @NotNull(message = "Activity type is required")
    private ActivityType type;

    @NotBlank(message = "Description is required")
    private String description;

    @FutureOrPresent(message = "Due date cannot be in the past")
    private LocalDate dueDate;
}
