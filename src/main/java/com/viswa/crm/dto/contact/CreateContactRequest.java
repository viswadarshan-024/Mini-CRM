package com.viswa.crm.dto.contact;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateContactRequest {

    @NotNull(message = "Company is required")
    private Long companyId;

    @NotBlank(message = "Contact name is required")
    @Size(max = 100)
    private String name;

    @Email
    private String email;

    @Size(max = 20)
    private String phone;

    @Size(max = 100)
    private String jobTitle;
}
