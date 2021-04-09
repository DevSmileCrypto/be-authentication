package io.cryptobrewmaster.ms.be.authentication.web.model;

import io.cryptobrewmaster.ms.be.authentication.constant.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountAuthenticationDto {
    @NotBlank
    private String accountId;
    @NotNull
    private List<Role> roles;
}
