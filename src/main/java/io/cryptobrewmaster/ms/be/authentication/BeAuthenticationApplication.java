package io.cryptobrewmaster.ms.be.authentication;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.ByteArrayOutputStream;

@SpringBootApplication
public class BeAuthenticationApplication implements CommandLineRunner {

    @Value("${gateway.uri}")
    public String host;

    public static void main(String[] args) {
        SpringApplication.run(BeAuthenticationApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DefaultExecutor exec = new DefaultExecutor();
        exec.setStreamHandler(new PumpStreamHandler(outputStream));
        exec.execute(CommandLine.parse("echo Hello"));
        System.out.println(outputStream.toString());
    }
}
