package io.cryptobrewmaster.ms.be.authentication.service.authentication.keychain.strategy;

import io.cryptobrewmaster.ms.be.authentication.communication.account.dto.AccountDto;
import io.cryptobrewmaster.ms.be.authentication.communication.account.service.AccountCommunicationService;
import io.cryptobrewmaster.ms.be.authentication.db.model.AccountAuthentication;
import io.cryptobrewmaster.ms.be.authentication.db.repository.AccountAuthenticationRepository;
import io.cryptobrewmaster.ms.be.authentication.properties.hive.HiveKeychainProperties;
import io.cryptobrewmaster.ms.be.authentication.web.model.AuthenticationTokenPairDto;
import io.cryptobrewmaster.ms.be.authentication.web.model.RegistrationOrLoginDto;
import io.cryptobrewmaster.ms.be.library.exception.InnerServiceException;
import io.cryptobrewmaster.ms.be.library.exception.ParametersAbsentOrInvalidException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.PumpStreamHandler;

import java.io.ByteArrayOutputStream;

@RequiredArgsConstructor
public abstract class BaseHiveKeychainAuthenticationStrategy implements HiveKeychainAuthenticationStrategy {

    protected final AccountCommunicationService accountCommunicationService;

    protected final AccountAuthenticationRepository accountAuthenticationRepository;

    protected final HiveKeychainProperties hiveKeychainProperties;

    abstract AuthenticationTokenPairDto login(AccountDto accountDto, AccountAuthentication accountAuthentication);

    abstract AuthenticationTokenPairDto registration(AccountDto accountDto);

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
        var accountDto = accountCommunicationService.createOrGet(registrationOrLoginDto.getWallet());

        return accountAuthenticationRepository.findByAccountId(accountDto.getId())
                .map(accountAuthentication -> login(accountDto, accountAuthentication))
                .orElseGet(() -> registration(accountDto));
    }


    protected boolean verifySignature(String signature, String message, String publicKey) {
        try {
            var validator = hiveKeychainProperties.getValidator();

            var outputStream = new ByteArrayOutputStream();
            var commandLine = new CommandLine(validator.getNodePath())
                    .addArgument(validator.getFilePath())
                    .addArgument(validator.getMethod())
                    .addArgument(signature)
                    .addArgument(message)
                    .addArgument(publicKey);

            var exec = new DefaultExecutor();
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
