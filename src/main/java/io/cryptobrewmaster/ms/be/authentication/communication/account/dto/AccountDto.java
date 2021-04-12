package io.cryptobrewmaster.ms.be.authentication.communication.account.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountDto {
    @NotBlank
    private String accountId;
    @NotBlank
    private String publicAccountId;
    @NotBlank
    private String nickname;
    @NotBlank
    private String wallet;
}
