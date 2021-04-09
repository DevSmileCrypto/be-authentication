package io.cryptobrewmaster.ms.be.authentication.web.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RegistrationOrLoginDto {
    @NotBlank
    private String wallet;
    @NotBlank
    private String signature;
    @NotBlank
    private String message;
    @NotBlank
    private String publicKey;
}
