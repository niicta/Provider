/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ourgroup.provider.model;

import org.ourgroup.provider.dao.DataAccessObject;
import org.ourgroup.provider.model.instance.Instance;
import org.ourgroup.provider.model.instance.InstanceStatus;
import org.ourgroup.provider.model.order.Order;
import org.ourgroup.provider.model.order.OrderAction;
import org.ourgroup.provider.model.order.OrderStatus;
import org.ourgroup.provider.model.person.Customer;
import org.ourgroup.provider.model.person.Employee;
import org.ourgroup.provider.model.templates.ITemplate;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Никита
 */
public class ModelOperations extends Observable {

    private Model model;
    private IdCounter idCounter;
    private String path;
    //Singleton realization
    //nikitakrasn

    private static ModelOperations instance = new ModelOperations();

    private ModelOperations() {
        super();
        this.model = new Model();
        idCounter = IdCounter.getCounter();
        idCounter.setCountOfInstances(0);
        idCounter.setCountOfOrders(0);
        idCounter.setCountOfUsers(0);
        path = "configs1.xml";
    }

    public static ModelOperations getModelOperations() {
        return instance;
    }

    public void setPath(String path){
        this.path = path;
    }

    public void setModel(Model model) {
        this.model = model;
        //nikitakrasn
        //пересчет полей счетчика для работы с моделью
        idCounter.setCountOfInstances(model.getInstances().size());
        idCounter.setCountOfOrders(model.getOrders().size());
        long size = model.getCustomers().size() + model.getEmployees().size();
        idCounter.setCountOfUsers(size);
        setChanged();
        notifyObservers();
    }

    public Customer getCustomerById(long customerId) {
        return model.getCustomerById(customerId);
    }

    public void addCustomer(Customer customer) {
        //nikitakrasn
        //тут и ниже - при добавлении сущности в систему присваеваем ей id в данной системе
        customer.setId(idCounter.getCountOfUsers());
        model.addCustomer(customer);
        try {
            DataAccessObject.getDAO().insertCustomer(customer);
        } catch (SQLException ex) {
            Logger.getLogger(ModelOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        setChanged();
        notifyObservers();
    }

    public Employee getEmployeeById(long employeeId) {
        return model.getEmployeeById(employeeId);
    }

    public void addEmployee(Employee employee) {
        employee.setId(idCounter.getCountOfUsers());
        model.addEmployee(employee);
        try {
            DataAccessObject.getDAO().insertEmployee(employee);
        } catch (SQLException ex) {
            Logger.getLogger(ModelOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        setChanged();
        notifyObservers();
    }
    
    public void addInstance(Instance instance){
        instance.setId(idCounter.getCountOfInstances());
        model.addInstance(instance);
       
        setChanged();
        notifyObservers();
    }
    
    public void addOrder(Order order){
        order.setId(idCounter.getCountOfOrders());
        model.addOrder(order);
        
        setChanged();
        notifyObservers();
    }

    public Instance getInstanceById(long instanceId) {
        return model.getInstanceById(instanceId);
    }

    public Order getOrderById(long orderId) {
        return model.getOrderById(orderId);
    }

    public ArrayList<Order> getOrders() {
        return model.getOrders();
    }

    // почему бы не возвращать ArrayList,  как в самой модели? Аня
    public ArrayList<Instance> getInstances() {
        return model.getInstances();
    }

    public ArrayList<Employee> getEmployees() {
        return model.getEmployees();
    }

    public ArrayList<Customer> getCustomers() {
        return model.getCustomers();
    }

    public ArrayList<ITemplate> getTemplates() {
        return model.getTemplates();
    }

    public void createOrder(long customerId, OrderAction action, ITemplate template, Instance instance) {
        //nikitakrasn
        //Принудительный выброс исключения, если такого кастомера нет
        Customer c = getCustomerById(customerId);
        if (action == OrderAction.CHANGE_TEMPLATE || action == OrderAction.CONNECT) {
            if (c.getBalance() < template.getCost()) {
                throw new RuntimeException("Balance is too low");
            }
            else{
                c.setBalance(c.getBalance() - template.getCost());
            }
        }
        Order order = new Order(customerId, template, instance, action);
        order.setId(idCounter.getCountOfOrders());
        model.addOrder(order);
        try {
            DataAccessObject.getDAO().insertOrder(order);
        } catch (SQLException ex) {
            Logger.getLogger(ModelOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        setChanged();
        notifyObservers();
    }

    public ArrayList<Order> getOrders(long customerId) {
        //nikitakrasn
        //Принудительный выброс исключения, если такого кастомера нет
        model.getCustomerById(customerId);
        ArrayList<Order> a = new ArrayList<Order>();
        for (Order or : model.getOrders()) {
            if (or.getCustomerId() == customerId)
                a.add(or);
        }
        return a;
    }

    public ArrayList<Instance> getInstances(long customerId) {
        //nikitakrasn
        //Принудительный выброс исключения, если такого кастомера нет
        model.getCustomerById(customerId);
        ArrayList<Instance> a = new ArrayList<Instance>();
        for (Instance in : model.getInstances()) {
            if (in.getCustomerId() == customerId)
                a.add(in);
        }
        return a;
    }

    public ArrayList<Order> getNotStartedOrders() {
        ArrayList<Order> orders = new ArrayList<Order>();
        for (Order or : model.getOrders()) {
            if (or.getStatus() == OrderStatus.NOT_STARTED)
                orders.add(or);
        }
        return orders;
    }

    public void startOrder(long orderId, long employeeId) {
        //nikitakrasn
        //Принудительный выброс исключения, если такого эмплоя нет
        model.getEmployeeById(employeeId);
        Order or = model.getOrderById(orderId);
        if(or.getStatus() != OrderStatus.NOT_STARTED) throw new RuntimeException("It's impossible to start this order");
        or.setStatus(OrderStatus.IN_PROCESS);
        or.setEmployeeId(employeeId);
        try {
            DataAccessObject.getDAO().updateOrder(or);
        } catch (SQLException ex) {
            Logger.getLogger(ModelOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        setChanged();
        notifyObservers();
    }

    public void createInstance(long orderId, long employeeId) {
        Order or = model.getOrderById(orderId);
        if(or.getAction() != OrderAction.CONNECT) throw new RuntimeException("Order action is not \"CONNECT\" !");
        if (or.getEmployeeId() != employeeId || or.getStatus() != OrderStatus.IN_PROCESS) throw new RuntimeException("This employee can't service this order");
        Instance ins = new Instance(or.getTemplate(), or.getCustomerId());
        ins.setId(idCounter.getCountOfInstances());
        model.addInstance(ins);
        try {
            DataAccessObject.getDAO().insertInstance(ins);
        } catch (SQLException ex) {
            Logger.getLogger(ModelOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        setChanged();
        notifyObservers();
    }

    public void setInstanceStatus(long orderId, long employeeId) {
        Order or = model.getOrderById(orderId);
         if(or.getAction() != OrderAction.RESTART && or.getAction() != OrderAction.STOP && or.getAction() != OrderAction.SUSPEND)
             throw new RuntimeException("Order action is not \"CONNECT/RESTART/RESUME\"  !");
        if (or.getEmployeeId() != employeeId) throw new RuntimeException("This employee can't service this order");
        Instance ins = getInstanceById(or.getInstance().getId());
        switch (or.getAction()) {
            case STOP:
                ins.setStatus(InstanceStatus.UNACTIVE);
                break;
            case SUSPEND:
                ins.setStatus(InstanceStatus.SUSPEND);
                break;
            case RESTART:
                ins.setStatus(InstanceStatus.ACTIVE);
                break;
        }
        try {
            DataAccessObject.getDAO().updateInstance(ins);
        } catch (SQLException ex) {
            Logger.getLogger(ModelOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        setChanged();
        notifyObservers();
    }

    public void setInstanceTemplate(long orderId, long employeeId) {
        Order or = model.getOrderById(orderId);
        if(or.getAction() != OrderAction.CHANGE_TEMPLATE) throw new RuntimeException("Order action is not \"CHANGE_TEMPLATE\" !");
        if (or.getEmployeeId() != employeeId) throw new RuntimeException("This employee can't service this order");
        Instance ins;
        ins = getInstanceById(or.getInstance().getId());
        ins.setTemplate(or.getTemplate());
        try {
            DataAccessObject.getDAO().updateInstance(ins);
        } catch (SQLException ex) {
            Logger.getLogger(ModelOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        setChanged();
        notifyObservers();
    }
/*
    public void finishOrder(long orderId, long employeeId) {
        Order or = model.getOrderById(orderId);
        if (or.getEmployeeId() != employeeId) throw new RuntimeException("This employee can't service this order");
        if(or.getStatus() != OrderStatus.IN_PROCESS) throw new RuntimeException("It's impossible to finish this order");
        or.setStatus(OrderStatus.COMPLETE);
        try {
            DataAccessObject.getDAO().updateOrder(or);
        } catch (SQLException ex) {
            Logger.getLogger(ModelOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        setChanged();
        notifyObservers();
    } // can be deleted*/

    public ArrayList<Order> getEmployeeOrders(long employeeId) {
        //nikitakrasn
        //Принудительный выброс исключения, если такого эмплоя нет
        model.getEmployeeById(employeeId);
        ArrayList<Order> orders = new ArrayList<Order>();
        for (Order or : model.getOrders()) {
            if (or.getEmployeeId() == employeeId)
                orders.add(or);
        }
        return orders;
    }

    public ArrayList<Order> getEmployeesOrders(long employeeId, OrderStatus status) {
        //nikitakrasn
        //Принудительный выброс исключения, если такого кастомера нет
        model.getEmployeeById(employeeId);
        ArrayList<Order> orders = new ArrayList<Order>();
        for (Order or : model.getOrders()) {
            if (or.getEmployeeId() == employeeId && or.getStatus() == status)
                orders.add(or);
        }
        return orders;
    }

    public void refillBalance(Customer c, int money){
        c.setBalance(c.getBalance() + money);
        try {
            DataAccessObject.getDAO().updateCustomer(c);
        } catch (SQLException ex) {
            Logger.getLogger(ModelOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        setChanged();
        notifyObservers();
    }
    
    public void marshallModel() throws JAXBException{
//       
//        File file = new File(path);
//        JAXBContext context = JAXBContext.newInstance(org.ourgroup.provider.model.getClass());
//        Marshaller marshaller = context.createMarshaller();
//        marshaller.marshal(org.ourgroup.provider.model, file);
    }

    public void demarshallModel() throws JAXBException{
//        
//      
        try {
            DataAccessObject.getDAO().readModelFromDB();
        setChanged();
        notifyObservers();
        } catch (SQLException ex) {
            Logger.getLogger(ModelOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Observable methods

    public synchronized void addObserver(Observer o) {
        super.addObserver(o);
        setChanged();
        notifyObservers();
    }

    public synchronized void deleteObserver(Observer o) {
        super.deleteObserver(o);
        super.setChanged();
        notifyObservers();
    }

    public void notifyObservers() {
        try {
            marshallModel();
        } catch (JAXBException ex) {
            Logger.getLogger(ModelOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally{
            super.notifyObservers();
        }
    }

    public void completeOrder(long orderId, long employeeId) {
        Order order = getOrderById(orderId);
        switch (order.getAction())
        {
            case CHANGE_TEMPLATE: setInstanceTemplate(orderId, employeeId); break;
            case CONNECT: createInstance(orderId, employeeId); break;
            case RESTART:
            case STOP:
            case SUSPEND: setInstanceStatus(orderId, employeeId); break;
            default: break;
        }
        order.setStatus(OrderStatus.COMPLETE);
        try {
            DataAccessObject.getDAO().updateOrder(order);
        } catch (SQLException ex) {
            Logger.getLogger(ModelOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void demarshallXmlModel() throws JAXBException {
          File file = new File(path); // path = "configs1.xml"
        if(!file.exists()) return;
        JAXBContext context = JAXBContext.newInstance(model.getClass());
        Unmarshaller unmarshaller = context.createUnmarshaller();
        Model m = (Model)unmarshaller.unmarshal(file);
        setModel(m);
        setChanged();
        notifyObservers();
    }
}
