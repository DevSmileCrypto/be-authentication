package io.cryptobrewmaster.ms.be.authentication.model.hive.signer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class HiveSignerTokenPair {
    @NotBlank
    @JsonProperty("access_token")
    private String accessToken;
    @NotBlank
    @JsonProperty("refresh_token")
    private String refreshToken;
    @NotBlank
    @JsonProperty("expires_in")
    private Long expiresIn;
    @NotBlank
    @JsonProperty("username")
    private String username;
}
