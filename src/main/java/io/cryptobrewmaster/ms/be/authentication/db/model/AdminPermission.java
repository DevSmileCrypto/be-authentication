package io.cryptobrewmaster.ms.be.authentication.db.model;

import io.cryptobrewmaster.ms.be.library.constants.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = AdminPermission.COLLECTION_NAME)
public class AdminPermission {

    public static final String COLLECTION_NAME = "adminPermission";

    @Id
    @Field(ID_FIELD)
    private ObjectId id;
    private static final String ID_FIELD = "_id";

    @Field(WALLET_FIELD)
    @Indexed(unique = true)
    private String wallet;
    private static final String WALLET_FIELD = "wallet";

    @Field(ROLES_FIELD)
    private Set<Role> roles;
    private static final String ROLES_FIELD = "roles";

    @Field(ALLOWED_FIELD)
    private boolean allowed;
    private static final String ALLOWED_FIELD = "allowed";

}
