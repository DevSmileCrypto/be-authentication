package io.cryptobrewmaster.ms.be.authentication.communication.account.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountDto {
    @NotBlank
    private String id;
    @NotBlank
    private String publicId;
    @NotBlank
    private String nickname;
    @NotBlank
    private String wallet;
    @NotNull
    private boolean initialized;
}
