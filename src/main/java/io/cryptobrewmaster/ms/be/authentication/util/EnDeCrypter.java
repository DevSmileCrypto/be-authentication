package io.cryptobrewmaster.ms.be.authentication.util;

import io.cryptobrewmaster.ms.be.authentication.exception.InnerServiceException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

import static java.nio.charset.StandardCharsets.UTF_8;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EnDeCrypter {

    private static final String CRYPTO_METHODS = "AES/ECB/PKCS5Padding";

    private static SecretKey setKey(String secret) {
        try {
            byte[] key = secret.getBytes(UTF_8);
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            return new SecretKeySpec(key, "AES");
        } catch (NoSuchAlgorithmException e) {
            throw new InnerServiceException(
                    String.format("Error while set key to SecretKeySpec with secret = %s. Error = %s",
                            secret, e.getMessage())
            );
        }
    }

    public static String encrypt(String strToEncrypt, String secret) {
        try {
            SecretKey secretKey = setKey(secret);
            Cipher cipher = Cipher.getInstance(CRYPTO_METHODS);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes(UTF_8)));
        } catch (Exception e) {
            throw new InnerServiceException(
                    String.format("Error while encrypting with secret = %s, string for decrypt = %s. Error = %s",
                            secret, strToEncrypt, e.getMessage())
            );
        }
    }

    public static String decrypt(String strToDecrypt, String secret) {
        try {
            SecretKey secretKey = setKey(secret);
            Cipher cipher = Cipher.getInstance(CRYPTO_METHODS);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)), UTF_8);
        } catch (Exception e) {
            throw new InnerServiceException(
                    String.format("Error while decrypting with secret = %s, string for decrypt = %s. Error = %s",
                            secret, strToDecrypt, e.getMessage()));
        }
    }

}
