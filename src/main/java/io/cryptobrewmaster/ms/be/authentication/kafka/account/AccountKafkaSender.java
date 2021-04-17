package io.cryptobrewmaster.ms.be.authentication.kafka.account;

import io.cryptobrewmaster.ms.be.authentication.communication.account.dto.AccountDto;
import io.cryptobrewmaster.ms.be.authentication.configuration.kafka.properties.KafkaProperties;
import io.cryptobrewmaster.ms.be.library.kafka.dto.account.AccountId;
import io.cryptobrewmaster.ms.be.library.kafka.dto.account.AccountKDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class AccountKafkaSender {

    private final KafkaTemplate<String, AccountKDto> accountKafkaTemplate;

    private final KafkaProperties kafkaProperties;

    public void init(AccountDto accountDto) {
        AccountId accountId = new AccountId(null, accountDto.getUid());
        AccountKDto accountKDto = new AccountKDto(
                accountId, accountDto.getWallet(), accountDto.getNickname()
        );

        log.info("Send message to core ms for init account: {}", accountKDto);

        accountKafkaTemplate.send(
                kafkaProperties.getTopic().getAccountInit(),
                accountId.getUid(),
                accountKDto
        );
    }

}
