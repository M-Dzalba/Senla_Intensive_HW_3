package ru.dzalba.utils;

import ru.dzalba.annotations.Component;
import ru.dzalba.annotations.Value;

@Component
public class ParametersHolder {

    @Value("my.param.db")
    private String someText;

    public String getSomeText() {
        return someText;
    }
}
