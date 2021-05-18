package io.cryptobrewmaster.ms.be.authentication.db.model;

import io.cryptobrewmaster.ms.be.authentication.model.jwt.JwtTokenPair;
import io.cryptobrewmaster.ms.be.library.constants.GatewayType;
import io.cryptobrewmaster.ms.be.library.constants.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Clock;
import java.util.Map;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = AccountAuthentication.COLLECTION_NAME)
public class AccountAuthentication {

    public static final String COLLECTION_NAME = "accountAuthentication";

    @Id
    @Field(ID_FIELD)
    private ObjectId id;
    private static final String ID_FIELD = "_id";

    @Field(ACCOUNT_ID_FIELD)
    @Indexed(unique = true)
    private String accountId;
    private static final String ACCOUNT_ID_FIELD = "accountId";

    @Field(TOKEN_INFO_FIELD)
    private Map<GatewayType, TokenInfo> tokenInfo;
    private static final String TOKEN_INFO_FIELD = "tokenInfo";

    @Field(ROLES_FIELD)
    private Set<Role> roles;
    private static final String ROLES_FIELD = "roles";

    @Field(CREATED_DATE_FIELD)
    private Long createdDate;
    private static final String CREATED_DATE_FIELD = "createdDate";

    @Field(LAST_MODIFIED_DATE_FIELD)
    private Long lastModifiedDate;
    private static final String LAST_MODIFIED_DATE_FIELD = "lastModifiedDate";

    public TokenInfo getTokenInfo(GatewayType type) {
        return getTokenInfo().get(type);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TokenInfo {
        private String accessToken;
        private Long expirationAccessTokenDate;
        private String refreshToken;
        private Long expirationRefreshTokenDate;
    }

    public void updateTokenInfo(JwtTokenPair jwtTokenPair, GatewayType type) {
        getTokenInfo().put(type, new TokenInfo(
                jwtTokenPair.getAccessToken(), jwtTokenPair.getExpirationAccessTokenDate(),
                jwtTokenPair.getRefreshToken(), jwtTokenPair.getExpirationRefreshTokenDate()
        ));
    }

    public void clearTokenInfo(GatewayType type) {
        getTokenInfo().remove(type);
    }

    public static AccountAuthentication of(String accountId, GatewayType type, JwtTokenPair jwtTokenPair,
                                           Set<Role> roles, Clock utcClock) {
        var tokenInfoMap = Map.of(type, new TokenInfo(
                jwtTokenPair.getAccessToken(), jwtTokenPair.getExpirationAccessTokenDate(),
                jwtTokenPair.getRefreshToken(), jwtTokenPair.getExpirationRefreshTokenDate()
        ));

        long now = utcClock.millis();
        return new AccountAuthentication(null, accountId, tokenInfoMap, roles, now, now);
    }

}
