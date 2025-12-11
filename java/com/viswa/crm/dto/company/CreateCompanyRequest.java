package com.viswa.crm.dto.company;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.UniqueElements;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class CreateCompanyRequest {

    @NotBlank(message = "Company name is required")
    @Size(max = 100)
    @UniqueElements
    private String name;

    @Email
    @UniqueElements
    private String email;

    @Size(max = 20)
    private String phone;

    @Size(max = 255)
    private String address;
}
