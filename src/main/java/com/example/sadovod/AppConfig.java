package com.example.sadovod;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration //спринговый бин для работы с конфигами
public class AppConfig {

    @Value("${app.first}")//@Value- аннотация -автозаполнение ${} взять из конфига
    private boolean firstRun;

    public boolean getFirstRun() {// функция get
        return this.firstRun;
    }
}
