package io.cryptobrewmaster.ms.be.authentication.util;

import io.cryptobrewmaster.ms.be.library.constants.GatewayType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class OAuthUtil {

    public static String generateState(GatewayType type) {
        var typeState = Optional.ofNullable(type)
                .map(GatewayType::name)
                .map(s -> String.format("type:%s", s))
                .orElse("");
        return Stream.of(typeState)
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.collectingAndThen(
                        Collectors.joining(","),
                        s -> Base64.getEncoder().encodeToString(s.getBytes(StandardCharsets.UTF_8))
                ));
    }

    public static Map<String, String> parseState(String state) {
        if (Objects.isNull(state)) {
            return new HashMap<>();
        }
        byte[] decode = Base64.getDecoder().decode(state);
        var decodedState = new String(decode, StandardCharsets.UTF_8);
        return Stream.of(decodedState.split(","))
                .map(String::strip)
                .map(s -> s.split(":"))
                .collect(Collectors.toMap(strings -> strings[0], strings -> strings[1]));
    }

}
