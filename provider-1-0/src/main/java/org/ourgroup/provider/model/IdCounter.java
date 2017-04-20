/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ourgroup.provider.model;

/**
 * @author Никита
 */
public class IdCounter {
    private static IdCounter counter = new IdCounter();

    private long countOfOrders;
    private long countOfInstances;
    private long countOfUsers;

    private IdCounter() {
        this.countOfOrders = 0;
        this.countOfInstances = 0;
        this.countOfUsers = 0;
    }

    public static IdCounter getCounter() {
        return counter;
    }

    public long getCountOfOrders() {
        return countOfOrders++;
    }

    public long getCountOfInstances() {
        return countOfInstances++;
    }

    public long getCountOfUsers() {
        return countOfUsers++;
    }


    //Далее - методы для установки значений после загрузки объекта из файла (Пока не продумано использование)
    public void setCountOfOrders(long countOfOrders) {
        this.countOfOrders = countOfOrders;
    }

    public void setCountOfInstances(long countOfInstances) {
        this.countOfInstances = countOfInstances;
    }


    public void setCountOfUsers(long countOfUsers) {
        this.countOfUsers = countOfUsers;
    }


}
