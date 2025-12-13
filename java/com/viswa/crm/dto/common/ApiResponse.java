package com.viswa.crm.dto.common;

import com.viswa.crm.dto.activity.ActivityResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ApiResponse<T> {

    private boolean success;
    private String message;
    private T data;
}
