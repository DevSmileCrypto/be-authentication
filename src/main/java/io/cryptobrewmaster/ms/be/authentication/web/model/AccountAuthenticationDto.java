package io.cryptobrewmaster.ms.be.authentication.web.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.cryptobrewmaster.ms.be.authentication.db.model.AccountAuthentication;
import io.cryptobrewmaster.ms.be.library.constants.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountAuthenticationDto {
    @NotNull
    private boolean valid;
    @NotBlank
    private String uid;
    @NotNull @NotEmpty
    private List<Role> roles;

    public static AccountAuthenticationDto of(boolean valid, AccountAuthentication accountAuthentication) {
        return new AccountAuthenticationDto(
                valid, accountAuthentication.getUid(), accountAuthentication.getRoles()
        );
    }

    public static AccountAuthenticationDto of(boolean valid) {
        return new AccountAuthenticationDto(valid, "", List.of());
    }
}
