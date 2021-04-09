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
public class HiveSignerAccountData {
    @NotBlank
    @JsonProperty("user")
    private String user;
    @NotBlank
    @JsonProperty("_id")
    private String id;
    @NotBlank
    @JsonProperty("name")
    private String name;
    @NotBlank
    @JsonProperty("account")
    private Account account;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Account {
        @NotBlank
        @JsonProperty("id")
        private Integer id;
        @NotBlank
        @JsonProperty("name")
        private String name;
    }
}
