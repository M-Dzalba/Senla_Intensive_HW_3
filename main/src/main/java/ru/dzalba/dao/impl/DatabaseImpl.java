package ru.dzalba.dao.impl;


import ru.dzalba.annotations.Autowire;
import ru.dzalba.annotations.Component;
import ru.dzalba.dao.DatabaseInterface;
import ru.dzalba.utils.ParametersHolder;

@Component
public class DatabaseImpl implements DatabaseInterface {
//    @Override
//    public void execute() {
//        System.out.println("Database operation executed.");
//    }
    private ParametersHolder parametersHolder;

    @Autowire
    public void setParametersHolder(ParametersHolder parametersHolder) {
        this.parametersHolder = parametersHolder;
    }

    public String execute() {
        return parametersHolder.getSomeText();
    }
}
