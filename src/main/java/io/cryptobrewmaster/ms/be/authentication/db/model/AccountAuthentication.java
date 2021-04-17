package io.cryptobrewmaster.ms.be.authentication.db.model;

import io.cryptobrewmaster.ms.be.authentication.model.jwt.JwtTokenPair;
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
import java.util.List;

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

    @Field(UID_FIELD)
    @Indexed(unique = true)
    private String uid;
    private static final String UID_FIELD = "uid";

    @Field(ACCESS_TOKEN_FIELD)
    private String accessToken;
    private static final String ACCESS_TOKEN_FIELD = "accessToken";

    @Field(EXPIRATION_ACCESS_TOKEN_DATE_FIELD)
    private Long expirationAccessTokenDate;
    private static final String EXPIRATION_ACCESS_TOKEN_DATE_FIELD = "expirationAccessTokenDate";

    @Field(REFRESH_TOKEN_FIELD)
    private String refreshToken;
    private static final String REFRESH_TOKEN_FIELD = "refreshToken";

    @Field(EXPIRATION_REFRESH_TOKEN_DATE_FIELD)
    private Long expirationRefreshTokenDate;
    private static final String EXPIRATION_REFRESH_TOKEN_DATE_FIELD = "expirationRefreshTokenDate";

    @Field(ROLES_FIELD)
    private List<Role> roles;
    private static final String ROLES_FIELD = "roles";

    @Field(CREATED_DATE_FIELD)
    private Long createdDate;
    private static final String CREATED_DATE_FIELD = "createdDate";

    @Field(LAST_MODIFIED_DATE_FIELD)
    private Long lastModifiedDate;
    private static final String LAST_MODIFIED_DATE_FIELD = "lastModifiedDate";

    public void updateTokenPair(JwtTokenPair jwtTokenPair) {
        setAccessToken(jwtTokenPair.getAccessToken());
        setExpirationAccessTokenDate(jwtTokenPair.getExpirationAccessTokenDate());
        setRefreshToken(jwtTokenPair.getRefreshToken());
        setExpirationRefreshTokenDate(jwtTokenPair.getExpirationRefreshTokenDate());
    }

    public void clearTokenPair() {
        setAccessToken(null);
        setExpirationAccessTokenDate(null);
        setRefreshToken(null);
        setExpirationRefreshTokenDate(null);
    }

    public static AccountAuthentication of(String uid, JwtTokenPair jwtTokenPair,
                                           List<Role> roles, Clock utcClock) {
        long now = utcClock.millis();
        return new AccountAuthentication(
                null, uid, jwtTokenPair.getAccessToken(), jwtTokenPair.getExpirationAccessTokenDate(),
                jwtTokenPair.getRefreshToken(), jwtTokenPair.getExpirationRefreshTokenDate(), roles,
                now, now
        );
    }

}
