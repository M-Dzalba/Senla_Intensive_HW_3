package ru.dzalba.service.impl;

import ru.dzalba.dao.DatabaseInterface;
import ru.dzalba.annotations.Autowire;
import ru.dzalba.annotations.Component;
import ru.dzalba.service.ServiceInterface;

@Component
public class ServiceImpl implements ServiceInterface {

//    private DatabaseInterface database;
//
//    @Autowire
//    public void setDatabase(DatabaseInterface database) {
//        this.database = database;
//    }
//
//    @Override
//    public String execute() {
//        if (database != null) {
//          return   database.execute();
//        } else {
//            System.out.println("No database dependency.");
//        }
//    }
private DatabaseInterface database;

    @Autowire
    public void setDatabase(DatabaseInterface database) {
        this.database = database;
    }

    @Override
    public String execute() {
        return "";
    }

    public void performAction() {
        System.out.println(database.execute());
    }

}