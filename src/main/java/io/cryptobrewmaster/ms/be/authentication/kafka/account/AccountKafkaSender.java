package io.cryptobrewmaster.ms.be.authentication.kafka.account;

import io.cryptobrewmaster.ms.be.authentication.communication.account.dto.AccountDto;
import io.cryptobrewmaster.ms.be.authentication.configuration.kafka.properties.KafkaProperties;
import io.cryptobrewmaster.ms.be.library.kafka.dto.account.KafkaAccount;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class AccountKafkaSender {

    private final KafkaTemplate<String, KafkaAccount> accountKafkaTemplate;

    private final KafkaProperties kafkaProperties;

    public void init(AccountDto accountDto) {
        var kafkaAccount = new KafkaAccount(
                accountDto.getId(), accountDto.getWallet(), accountDto.getNickname()
        );

        log.info("Send message to core ms for init account: {}", kafkaAccount);

        accountKafkaTemplate.send(
                kafkaProperties.getTopic().getAccountInit(),
                kafkaAccount.getId(),
                kafkaAccount
        );
    }

}
