package ru.dzalba.service.impl;

import ru.dzalba.dao.DatabaseInterface;
import ru.dzalba.annotations.Autowire;
import ru.dzalba.annotations.Component;
import ru.dzalba.service.ServiceInterface;

@Component
public class ServiceImpl implements ServiceInterface {

    private final DatabaseInterface database;

    @Autowire
    public ServiceImpl(DatabaseInterface database) {
        this.database = database;
    }

    @Override
    public String execute() {
        if (database != null) {
          return   database.execute();
        } else {
            return "No database dependency.";
        }
    }

    public void performAction() {
        System.out.println(database.execute());
    }

}