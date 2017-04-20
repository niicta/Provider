/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ourgroup.provider.dao;

import org.ourgroup.provider.model.Model;
import org.ourgroup.provider.model.ModelOperations;
import org.ourgroup.provider.model.instance.Instance;
import org.ourgroup.provider.model.instance.InstanceStatus;
import org.ourgroup.provider.model.order.Order;
import org.ourgroup.provider.model.order.OrderAction;
import org.ourgroup.provider.model.order.OrderStatus;
import org.ourgroup.provider.model.person.Customer;
import org.ourgroup.provider.model.person.Employee;
import org.ourgroup.provider.model.templates.ITemplate;
import org.ourgroup.provider.model.templates.Internet100;
import org.ourgroup.provider.model.templates.Internet50;
import org.ourgroup.provider.model.templates.InternetVIP;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * @author Краснопольский
 */
public class DataAccessObject {
    //для работы прописать свой юзернейм и пароль в субд
    private static String username = "admin";
    private static String password = "password";
    private static String url = "jdbc:derby://localhost:1527/providerdb;create=true";

    private static String orderTableName = "PROV_ORDERS";
    private static String instanceTableName = "PROV_INSTANCES";
    private static String userTableName = "PROV_USERS";


    private static final DataAccessObject dao = new DataAccessObject();

    private Connection connection;

    private DataAccessObject() {
        try {
            Locale.setDefault(Locale.ENGLISH);
            Class.forName("org.apache.derby.jdbc.ClientDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static DataAccessObject getDAO() {
        return dao;
    }

    private boolean checkExistance(String tableName) throws SQLException {
        connection = DriverManager.getConnection(url /*, username, password*/);
        Statement statement = connection.createStatement();
        String sql = "select tablename from SYS.SYSTABLES where tablename = '" + tableName + "'";
        ResultSet set = statement.executeQuery(sql);
        while (set.next()) {
            if (set.getString("tablename").equals(tableName)) {
                connection.close();
                return true;
            }
        }
        set.close();
        statement.close();
        connection.close();
        return false;
    }

    public void insertCustomer(Customer c) throws SQLException {

        Statement statement = null;
        if (!checkExistance(userTableName)) {
            connection = DriverManager.getConnection(url, username, password);
            statement = connection.createStatement();
            String sql = "CREATE TABLE " + userTableName + "\n(" +
                    "Id INTEGER NOT NULL, " +
                    "NAME VARCHAR(20) NOT NULL, " +
                    "SURNAME VARCHAR(20) NOT NULL, " +
                    "TYPE VARCHAR(20) NOT NULL, " +
                    "BALANCE VARCHAR(20), " +
                    "PRIMARY KEY (Id))";
            statement.executeUpdate(sql);

        }
        if (statement == null) {
            connection = DriverManager.getConnection(url, username, password);
            statement = connection.createStatement();
        }
        String sql = "INSERT INTO " + userTableName + " " +
                "VALUES (" + c.getId() + ", '" + c.getName() + "', '" + c.getSurname() + "', 'CUSTOMER', '" + c.getBalance() + "')";
        System.out.println(sql);
        statement.executeUpdate(sql);
        statement.close();
        connection.close();
    }

    public void insertEmployee(Employee e) throws SQLException {
        Statement statement = null;
        if (!checkExistance(userTableName)) {
            connection = DriverManager.getConnection(url, username, password);
            statement = connection.createStatement();
            String sql = "CREATE TABLE  " + userTableName + "\n" +
                    "(Id INTEGER NOT NULL, " +
                    "NAME VARCHAR(20) NOT NULL ," +
                    "SURNAME VARCHAR(20) NOT NULL ," +
                    "TYPE VARCHAR(20) NOT NULL ," +
                    "BALANCE VARCHAR(20) , " +  // Employee's Balance?!
                    "PRIMARY KEY (Id))";
            statement.executeUpdate(sql);
        }
        if (statement == null) {
            connection = DriverManager.getConnection(url, username, password);
            statement = connection.createStatement();
        }
        String sql = "INSERT INTO " + userTableName + " (Id, NAME, SURNAME, TYPE) " +
                "VALUES (" + e.getId() + ", '" + e.getName() + "', '" + e.getSurname() + "', 'EMPLOYEE' " + ")";
        System.out.println(sql);
        statement.executeUpdate(sql);
        statement.close();
        connection.close();
    }

    public List<Object> getUsers() throws SQLException {

        if (!checkExistance(userTableName))
            return null;
        connection = DriverManager.getConnection(url, username, password);
        Statement statement = connection.createStatement();
        List<Object> users = new ArrayList<>(0);
        String sql = "select * from " + userTableName + " order by Id";
        System.out.println(sql);
        ResultSet set = statement.executeQuery(sql);
        while (set.next()) {
            long id = Long.parseLong(set.getString("Id"));
            String name = set.getString("NAME");
            String surname = set.getString("SURNAME");
            System.out.println(id + " " + name + " " + surname);
            switch (set.getString("TYPE")) {
                case "CUSTOMER":
                    double balance = Double.parseDouble(set.getString("BALANCE"));
                    Customer c = new Customer(name, surname);
                    c.setId(id);
                    c.setBalance(balance);
                    users.add(c);
                    break;
                case "EMPLOYEE":
                    Employee e = new Employee(name, surname);
                    e.setId(id);
                    users.add(e);
                default:
                    users.add(null);
            }
        }
        set.close();
        statement.close();
        connection.close();

        return users;
    }

    public void insertOrder(Order o) throws SQLException {
        Statement statement = null;
        if (!checkExistance(orderTableName)) {
            connection = DriverManager.getConnection(url, username, password);
            statement = connection.createStatement();
            String sql = "CREATE TABLE  " + orderTableName + "\n" +
                    "(	Id INTEGER NOT NULL, " +
                    "CUSTOMER_Id INTEGER NOT NULL ," +
                    "EMPLOYEE_Id INTEGER ," +
                    "TEMPLATE VARCHAR(20) ," +
                    "INSTANCE_Id INTEGER , " +
                    "COMPLETE_DATE DATE, " +
                    "REQUEST_DATE DATE NOT NULL , " +
                    "ORDER_STATUS VARCHAR(20) NOT NULL , " +
                    "ORDER_ACTION VARCHAR(20) NOT NULL , " +
                    "PRIMARY KEY (Id), " +
                    "FOREIGN KEY (EMPLOYEE_Id) REFERENCES  " + userTableName + "(Id), " +
                    "FOREIGN KEY (CUSTOMER_Id) REFERENCES " + userTableName + "(Id), " +
                    "FOREIGN KEY (INSTANCE_Id) REFERENCES " + instanceTableName + "(Id)) ";
            ;
            statement.executeUpdate(sql);

        }
        if (statement == null) {
            connection = DriverManager.getConnection(url, username, password);
            statement = connection.createStatement();
        }
        StringBuilder sql = new StringBuilder("INSERT INTO " + orderTableName + " (Id, CUSTOMER_Id");
        StringBuilder values = new StringBuilder("VALUES (" + o.getId() + ", " + o.getCustomerId());
        if (o.getEmployeeId() != -1) {
            sql.append(", EMPLOYEE_Id");
            values.append(", ").append(o.getEmployeeId());
        }
        if (o.getTemplate() != null) {
            sql.append(", TEMPLATE");
            values.append(", '").append(o.getTemplate().getName()).append("'");
        }
        if (o.getInstance() != null) {
            sql.append(", INSTANCE_Id");
            values.append(", ").append(o.getInstance().getId());
        }
        if (o.getCompleteDate() != null) {
            sql.append(", COMPLETE_DATE");
            values.append(", DATE ('").append(new java.sql.Date(o.getCompleteDate().getTime())).append("')");
        }
        sql.append(", REQUEST_DATE, ORDER_STATUS, ORDER_ACTION) ");
        values.append(", DATE ('").append(new java.sql.Date(o.getRequestDate().getTime()))
                .append("'), '").append(o.getStatus().name())
                .append("', '").append(o.getAction().name())
                .append("')");
        sql.append(values);
        System.out.println(sql.toString());
        statement.executeUpdate(sql.toString());
        statement.close();
        connection.close();
    }

    public List<Order> getOrders() throws SQLException {
        if (!checkExistance(orderTableName))
            return null;
        ArrayList<Order> orders = new ArrayList<>(0);
        connection = DriverManager.getConnection(url, username, password);
        Statement statement = connection.createStatement();
        String sql = "select * from " + orderTableName + " order by Id";
        ResultSet set = statement.executeQuery(sql);
        System.out.println(sql);
        while (set.next()) {
            long id = Long.parseLong(set.getString("Id"));
            long employeeId = set.getString("EMPLOYEE_Id") == null ? -1 : Long.parseLong(set.getString("EMPLOYEE_Id"));
            long customerId = Long.parseLong(set.getString("CUSTOMER_Id"));
            ITemplate t;
            String temp;
            if ((temp = set.getString("TEMPLATE")) == null)
                t = null;
            else {
                switch (temp) {
                    case "Internet50":
                        t = new Internet50();
                        break;
                    case "Internet100":
                        t = new Internet100();
                        break;
                    case "InternetVIP":
                        t = new InternetVIP();
                        break;
                    default:
                        t = null;
                        break;
                }
            }

            Instance instance;

            if (set.getString("INSTANCE_Id") == null)
                instance = null;
            else
                instance = ModelOperations.getModelOperations().getInstanceById(Long.parseLong(set.getString("INSTANCE_Id")));

            Date completeDate = set.getString("COMPLETE_DATE") == null ? null : set.getDate("COMPLETE_DATE");
            Date requestDate = set.getDate("REQUEST_DATE");
            OrderStatus status = OrderStatus.valueOf(set.getString("ORDER_STATUS"));
            OrderAction action = OrderAction.valueOf(set.getString("ORDER_ACTION"));

            Order order = new Order();
            order.setAction(action);
            order.setCompleteDate(completeDate);
            order.setCustomerId(customerId);
            order.setEmployeeId(employeeId);
            order.setId(id);
            order.setInstance(instance);
            order.setRequestDate(requestDate);
            order.setStatus(status);
            order.setTemplate(t);
            System.out.println(id + " " + customerId + " " + employeeId + " " + temp + " " + (instance == null ? "null" : instance.getId()) + " " + (completeDate == null ? "null" : completeDate.toString()) + " " + requestDate.toString() + " " + status.name() + " " + action.name());
            orders.add(order);
        }
        set.close();
        statement.close();
        connection.close();
        return orders;
    }

    public void insertInstance(Instance i) throws SQLException {
        Statement statement = null;
        if (!checkExistance(instanceTableName)) {
            connection = DriverManager.getConnection(url, username, password);
            statement = connection.createStatement();
            String sql = "CREATE TABLE  " + instanceTableName + "\n" +
                    "(	Id INTEGER NOT NULL, " +
                    "TEMPLATE VARCHAR(20) NOT NULL ," +
                    "CUSTOMER_Id INTEGER NOT NULL ," +
                    "CONNECTION_DATE DATE NOT NULL , " +
                    "INSTANCE_STATUS VARCHAR(20) NOT NULL , " +
                    "SUSPENDION_DATE DATE, " +
                    "RESUME_DATE DATE, " +
                    "DELTA INTEGER NOT NULL , " +
                    "PRIMARY KEY (Id), " +
                    "FOREIGN KEY (CUSTOMER_Id) REFERENCES " + userTableName + "(Id)) ";
            statement.executeUpdate(sql);
        }
        if (statement == null) {
            connection = DriverManager.getConnection(url, username, password);
            statement = connection.createStatement();
        }

        StringBuilder sql = new StringBuilder("INSERT INTO " + instanceTableName + " (Id, TEMPLATE, CUSTOMER_Id, CONNECTION_DATE, INSTANCE_STATUS");
        StringBuilder values = new StringBuilder("VALUES (" + i.getId() + ", '" + i.getTemplate().getName() + "', " + i.getCustomerId() + ", DATE ('" + new java.sql.Date(i.getConnectionDate().getTime()) + "'), '" + i.getStatus().name() + "'");
        if (i.getSuspendionDate() != null) {
            sql.append(", SUSPENDION_DATE");
            values.append(", DATE ('").append(new java.sql.Date(i.getSuspendionDate().getTime())).append("')");
        }
        if (i.getResumeDate() != null) {
            sql.append(", RESUME_DATE");
            values.append(", DATE ('").append(new java.sql.Date(i.getResumeDate().getTime())).append("')");
        }
        sql.append(", DELTA) ");
        values.append(", ").append(i.getDelta())
                .append(")");
        sql.append(values);
        System.out.println(sql.toString());
        statement.executeUpdate(sql.toString());
        statement.close();
        connection.close();
    }

    public List<Instance> getInstances() throws SQLException {
        if (!checkExistance(instanceTableName))
            return null;
        ArrayList<Instance> instances = new ArrayList<>(0);
        connection = DriverManager.getConnection(url, username, password);
        Statement statement = connection.createStatement();
        String sql = "select * from " + instanceTableName + " order by Id";
        ResultSet set = statement.executeQuery(sql);
        System.out.println(sql);
        while (set.next()) {
            long id = Long.parseLong(set.getString("Id"));
            ITemplate t;
            String temp;
            if ((temp = set.getString("TEMPLATE")) == null)
                t = null;
            else {
                switch (temp) {
                    case "Internet50":
                        t = new Internet50();
                        break;
                    case "Internet100":
                        t = new Internet100();
                        break;
                    case "InternetVIP":
                        t = new InternetVIP();
                        break;
                    default:
                        t = null;
                        break;
                }
            }
            long customerId = Long.parseLong(set.getString("CUSTOMER_Id"));
            Date connectionDate = set.getDate("CONNECTION_DATE");
            InstanceStatus status = InstanceStatus.valueOf(set.getString("INSTANCE_STATUS"));
            Date suspendionDate = set.getString("SUSPENDION_DATE") == null ? null : set.getDate("SUSPENDION_DATE");
            Date resumeDate = set.getString("RESUME_DATE") == null ? null : set.getDate("RESUME_DATE");
            int delta = Integer.parseInt(set.getString("DELTA"));

            Instance ins = new Instance(t, customerId);
            ins.setConnectionDate(connectionDate);
            ins.setCustomerId(customerId);
            ins.setDelta(delta);
            ins.setId(id);
            ins.setResumeDate(resumeDate);
            ins.setStatus(status);
            ins.setSuspendionDate(suspendionDate);
            ins.setTemplate(t);
            System.out.println(id + " " + (t == null ? "null" : t.getName()) + " " + customerId + " " + (suspendionDate == null ? "null" : suspendionDate.toString()) + " " + delta + " " + (resumeDate == null ? "null" : resumeDate.toString()) + " " + status.toString() + " ");
            instances.add(ins);
        }
        set.close();
        statement.close();
        connection.close();
        return instances;
    }

    public void updateOrder(Order order) throws SQLException {
        if (!checkExistance(orderTableName)) {
            return;
        }

        connection = DriverManager.getConnection(url, username, password);
        Statement statement = connection.createStatement();
        String sql = "update " + orderTableName + " set" +


                " EMPLOYEE_Id = " + order.getEmployeeId() + "," +
                (order.getTemplate() == null ? "" : " TEMPLATE = '" + order.getTemplate().getName() + "',") +
                (order.getInstance() == null ? "" : " INSTANCE_Id = " + order.getInstance().getId() + ",") +
                (order.getCompleteDate() == null ? "" : " COMPLETE_DATE = DATE ('" + new java.sql.Date(order.getCompleteDate().getTime()) + "'),") +
                " REQUEST_DATE = DATE ('" + new java.sql.Date(order.getRequestDate().getTime()) + "')," +
                " ORDER_STATUS = '" + order.getStatus().name() + "'," +
                " ORDER_ACTION = '" + order.getAction().name() + "'" +
                " WHERE Id = " + order.getId();
        System.out.println(sql);
        statement.executeUpdate(sql);

        statement.close();
        connection.close();
    }

    public void updateCustomer(Customer customer) throws SQLException {
        if (!checkExistance(userTableName)) {
            return;
        }

        connection = DriverManager.getConnection(url, username, password);
        Statement statement = connection.createStatement();
        String sql = "update " + userTableName + " set" +
                " BALANCE = " + customer.getBalance() +
                " WHERE Id = " + customer.getBalance();
        statement.executeUpdate(sql);
        System.out.println(sql);
        statement.close();
        connection.close();
    }

    public void updateInstance(Instance ins) throws SQLException {
        if (!checkExistance(instanceTableName)) {
            return;
        }

        connection = DriverManager.getConnection(url, username, password);
        Statement statement = connection.createStatement();
        String sql = "update " + instanceTableName + " set" +
                " TEMPLATE = '" + ins.getTemplate().getName() + "'," +
                " CONNECTION_DATE = DATE ('" + new java.sql.Date(ins.getConnectionDate().getTime()) + "')," +
                " INSTANCE_STATUS = '" + ins.getStatus().name() + "'," +
                (ins.getSuspendionDate() == null ? "" : " SUSPENDION_DATE = DATE ('" + new java.sql.Date(ins.getSuspendionDate().getTime()) + "'),") +
                (ins.getResumeDate() == null ? "" : " RESUME_DATE = DATE ('" + new java.sql.Date(ins.getResumeDate().getTime()) + "'),") +
                " DELTA = " + ins.getDelta() +
                " WHERE Id = " + ins.getId();
        statement.executeUpdate(sql);
        System.out.println(sql);
        statement.close();
        connection.close();
    }

    public void readModelFromDB() throws SQLException {
        ModelOperations mo = ModelOperations.getModelOperations();
        Model m = new Model();


        List<Object> users = getUsers();
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i) instanceof Employee) {
                m.addEmployee((Employee) users.get(i));
            } else if (users.get(i) instanceof Customer) {
                m.addCustomer((Customer) users.get(i));
            }
        }
        mo.setModel(m);
        List<Instance> instances = getInstances();
        for (int i = 0; i < instances.size(); i++) {
            if (instances.get(i) != null) {
                mo.addInstance(instances.get(i));
            }
        }


        List<Order> orders = getOrders();
        for (int i = 0; i < orders.size(); i++) {
            if (orders.get(i) != null) {
                mo.addOrder(orders.get(i));
            }
        }

    }

    public void insertModelToDB() throws SQLException {
        List<Employee> emps = ModelOperations.getModelOperations().getEmployees();
        List<Customer> custs = ModelOperations.getModelOperations().getCustomers();
        List<Instance> insts = ModelOperations.getModelOperations().getInstances();
        List<Order> ords = ModelOperations.getModelOperations().getOrders();
        long count = emps.size() + custs.size();
        for (int i = 0; i < count; i++) {
            try {
                insertEmployee(ModelOperations.getModelOperations().getEmployeeById(i));
            } catch (RuntimeException e) {
                insertCustomer(ModelOperations.getModelOperations().getCustomerById(i));
            }


        }

        for (int i = 0; i < insts.size(); i++) {
            insertInstance(ModelOperations.getModelOperations().getInstanceById(i));
        }

        for (int i = 0; i < ords.size(); i++) {
            insertOrder(ModelOperations.getModelOperations().getOrderById(i));
        }
    }


}
