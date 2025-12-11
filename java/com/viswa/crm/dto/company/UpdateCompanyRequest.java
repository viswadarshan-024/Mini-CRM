package com.viswa.crm.dto.company;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class UpdateCompanyRequest {

    @Size(max = 100)
    private String name;

    @Email
    private String email;

    @Size(max = 20)
    private String phone;

    @Size(max = 255)
    private String address;
}
