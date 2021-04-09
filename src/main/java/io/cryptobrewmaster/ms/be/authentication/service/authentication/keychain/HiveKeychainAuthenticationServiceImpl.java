package io.cryptobrewmaster.ms.be.authentication.service.authentication.keychain;

import io.cryptobrewmaster.ms.be.authentication.db.model.AccountAuthentication;
import io.cryptobrewmaster.ms.be.authentication.db.repository.AccountAuthenticationRepository;
import io.cryptobrewmaster.ms.be.authentication.integration.account.dto.AccountDto;
import io.cryptobrewmaster.ms.be.authentication.integration.account.service.AccountCommunicationService;
import io.cryptobrewmaster.ms.be.authentication.model.jwt.JwtTokenPair;
import io.cryptobrewmaster.ms.be.authentication.properties.hive.HiveKeychainProperties;
import io.cryptobrewmaster.ms.be.authentication.service.jwt.JwtService;
import io.cryptobrewmaster.ms.be.authentication.web.model.AuthenticationTokenPairDto;
import io.cryptobrewmaster.ms.be.authentication.web.model.RegistrationOrLoginDto;
import io.cryptobrewmaster.ms.be.library.constants.Role;
import io.cryptobrewmaster.ms.be.library.exception.InnerServiceException;
import io.cryptobrewmaster.ms.be.library.exception.ParametersAbsentOrInvalidException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.PumpStreamHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.Clock;
import java.util.List;

@RequiredArgsConstructor
@Service
public class HiveKeychainAuthenticationServiceImpl implements HiveKeychainAuthenticationService {

    private final AccountCommunicationService accountCommunicationService;

    private final JwtService jwtService;

    private final AccountAuthenticationRepository accountAuthenticationRepository;

    private final HiveKeychainProperties hiveKeychainProperties;

    @Value("${authentication.default.roles:USER}")
    private List<Role> defaultAuthenticationRoles;

    private final Clock utcClock;

    @Override
    public AuthenticationTokenPairDto registrationOrLogin(RegistrationOrLoginDto registrationOrLoginDto) {
        boolean isValid = verifySignature(registrationOrLoginDto.getSignature(), registrationOrLoginDto.getMessage(), registrationOrLoginDto.getPublicKey());
        if (!isValid) {
            throw new ParametersAbsentOrInvalidException(
                    String.format("Account with wallet = %s entered not valid hive keychain data. Signature = %s, " +
                                    "message = %s, public key = %s.", registrationOrLoginDto.getWallet(),
                            registrationOrLoginDto.getSignature(), registrationOrLoginDto.getMessage(),
                            registrationOrLoginDto.getPublicKey())
            );
        }
        AccountDto accountDto = accountCommunicationService.createOrGet(registrationOrLoginDto.getWallet());

        var accountAuthenticationOptional = accountAuthenticationRepository.findByAccountId(accountDto.getAccountId());
        if (accountAuthenticationOptional.isEmpty()) {
            JwtTokenPair jwtTokenPair = jwtService.generatePair(accountDto.getAccountId(), defaultAuthenticationRoles);

            AccountAuthentication accountAuthentication = AccountAuthentication.of(
                    accountDto.getAccountId(), jwtTokenPair, defaultAuthenticationRoles, utcClock
            );
            accountAuthenticationRepository.save(accountAuthentication);

            return AuthenticationTokenPairDto.of(jwtTokenPair);
        }

        AccountAuthentication accountAuthentication = accountAuthenticationOptional.get();
        JwtTokenPair jwtTokenPair = jwtService.generatePair(accountAuthentication);

        accountAuthentication.updateTokenPair(jwtTokenPair);
        accountAuthentication.setLastModifiedDate(utcClock.millis());
        accountAuthenticationRepository.save(accountAuthentication);

        return AuthenticationTokenPairDto.of(jwtTokenPair);
    }

    private boolean verifySignature(String signature, String message, String publicKey) {
        try {
            HiveKeychainProperties.Validator validator = hiveKeychainProperties.getValidator();

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            CommandLine commandLine = new CommandLine(validator.getNodePath())
                    .addArgument(validator.getFilePath())
                    .addArgument(validator.getMethod())
                    .addArgument(signature)
                    .addArgument(message)
                    .addArgument(publicKey);

            DefaultExecutor exec = new DefaultExecutor();
            exec.setStreamHandler(new PumpStreamHandler(outputStream));
            exec.setWatchdog(new ExecuteWatchdog(validator.getTimeoutInMs()));
            exec.execute(commandLine);
            return Boolean.parseBoolean(outputStream.toString().strip());
        } catch (Exception e) {
            throw new InnerServiceException(
                    String.format("Error while on verify hive keychain message signature. Signature = %s, message = %s, " +
                            "public key = %s. Error = %s.", signature, message, publicKey, e.getMessage())
            );
        }
    }

}
