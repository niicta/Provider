package org.ourgroup.provider.model.person;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Created by friend_RU.
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "customer")
public class Customer {
    @XmlElement(name = "id")
    private long id;
    @XmlElement(name = "name")
    private String name;
    @XmlElement(name = "surname")
    private String surname;
    @XmlElement(name = "balance")
    private double balance;


    public Customer(String name, String surname) {
        balance = 0;
        this.name = name;
        this.surname = surname;
    }
    //данный конструктор необходим для загрузки класса из xml
    private Customer() {        
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void setId(long id) {
        this.id = id;
    }
}
