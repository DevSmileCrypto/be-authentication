package io.cryptobrewmaster.ms.be.authentication;

import io.cryptobrewmaster.ms.be.authentication.properties.hive.HiveKeychainProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BeAuthenticationApplication implements CommandLineRunner {

    @Value("${gateway.uri}")
    public String host;

    @Autowired
    private HiveKeychainProperties hiveKeychainProperties;

    public static void main(String[] args) {
        SpringApplication.run(BeAuthenticationApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
//        var signature = "1f15ebdab3f40edb2e796bde8242229ff2205196d5f95d5bdb461783deb72147fd4f4ae3f74083109939411704efa0dffc58e79a1c20d7f4fab25979aee21dec7f";
//        var message = "1617978904710";
//        var publicKey = "STM7varmMrtJWu1yqzr5vrauFnYB7pGncRnkKY72j2L4Y6tuWuPZ5";
//
//        HiveKeychainProperties.Validator validator = hiveKeychainProperties.getValidator();
//
//        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//        CommandLine commandLine = new CommandLine(validator.getNodePath())
//                .addArgument(validator.getFilePath())
//                .addArgument(validator.getMethod())
//                .addArgument(signature)
//                .addArgument(message)
//                .addArgument(publicKey);
//
//        System.out.println(commandLine);
//
//        DefaultExecutor exec = new DefaultExecutor();
//        exec.setStreamHandler(new PumpStreamHandler(outputStream));
//        exec.setWatchdog(new ExecuteWatchdog(validator.getTimeoutInMs()));
//        exec.execute(commandLine);
//        System.out.println(outputStream.toString().strip());
//        boolean b = Boolean.parseBoolean(outputStream.toString().strip());
//        System.out.println(b);

    }

}
