package ru.ylab.task;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import ru.ylab.task.mapper.HistoryMapper;
import ru.ylab.task.mapper.TransactionMapper;

@SpringBootApplication
@EnableAspectJAutoProxy
public class TaskApplication {
    private Info buildInfo() {
        return new Info()
                .title("Wallet API")
                .version("0.1");
    }

    @Bean
    public OpenAPI customOpenApi() {
        return new OpenAPI()
                .info(buildInfo());
    }

    @Bean
    public HistoryMapper historyMapper() {
        return HistoryMapper.INSTANCE;
    }

    @Bean
    public TransactionMapper transactionMapper() {
        return TransactionMapper.INSTANCE;
    }

    public static void main(String[] args) {
        SpringApplication.run(TaskApplication.class, args);
    }

}
