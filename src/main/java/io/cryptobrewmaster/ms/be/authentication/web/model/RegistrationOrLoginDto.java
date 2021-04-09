package io.cryptobrewmaster.ms.be.authentication.web.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationOrLoginDto {
    @NotBlank
    private String wallet;
}
