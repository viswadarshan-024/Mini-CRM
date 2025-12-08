package com.viswa.crm.model;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RequestLog {

    private Long id;

    private User user;

    private String endpoint;
    private String method;
    private int statusCode;
    private String ipAddress;

    private LocalDateTime requestTime;
}
