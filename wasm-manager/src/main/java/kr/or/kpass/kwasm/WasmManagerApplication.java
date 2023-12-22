package kr.or.kpass.kwasm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
@EnableDiscoveryClient
@SpringBootApplication
public class WasmManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(WasmManagerApplication.class, args);
    }

}
