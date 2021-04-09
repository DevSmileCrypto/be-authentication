package io.cryptobrewmaster.ms.be.authentication.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;

import java.time.Clock;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UidGenerator {

    private static final int DEFAULT_SIZE = 24;

    public static String generate(Clock utcClock) {
        return generate(utcClock, DEFAULT_SIZE);
    }

    public static String generate(Clock utcClock, Integer size) {
        String uuid = UUID.randomUUID().toString();
        List<String> strings = List.of(uuid, String.valueOf(utcClock.millis()));
        uuid = DigestUtils.sha256Hex(String.join(", ", strings));

        if (size >= 64) {
            return uuid;
        }
        return RandomStringUtils.random(size, uuid);
    }

    public static String generate(Clock utcClock, Object... args) {
        return generate(utcClock, DEFAULT_SIZE, args);
    }

    public static String generate(Clock utcClock, Integer size, Object... args) {
        var randomUuid = UUID.randomUUID().toString();
        String uuid = Stream.of(randomUuid, args, String.valueOf(utcClock.millis()))
                .map(String::valueOf)
                .collect(Collectors.collectingAndThen(
                        Collectors.toList(),
                        strings -> DigestUtils.sha256Hex(String.join(", ", strings))
                ));

        if (size >= 64) {
            return uuid;
        }
        return RandomStringUtils.random(size, uuid);
    }

}
