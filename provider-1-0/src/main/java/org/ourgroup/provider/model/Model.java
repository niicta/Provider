package org.ourgroup.provider.model;

import org.ourgroup.provider.model.instance.Instance;
import org.ourgroup.provider.model.order.Order;
import org.ourgroup.provider.model.person.Customer;
import org.ourgroup.provider.model.person.Employee;
import org.ourgroup.provider.model.templates.ITemplate;
import org.ourgroup.provider.model.templates.Internet100;
import org.ourgroup.provider.model.templates.Internet50;
import org.ourgroup.provider.model.templates.InternetVIP;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;

/**
 * Created by friend_RU.
 */

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "org/ourgroup/provider/model")
public class Model implements IModel {
    @XmlElement(name = "order")
    @XmlElementWrapper(name = "orders")
    private ArrayList<Order> orders = new ArrayList<Order>();
    @XmlElement(name = "instance")
    @XmlElementWrapper(name = "instances")
    private ArrayList<Instance> instances = new ArrayList<Instance>();
    @XmlElement(name = "employee")
    @XmlElementWrapper(name = "employees")
    private ArrayList<Employee> employees = new ArrayList<Employee>();
    @XmlElement(name = "customer")
    @XmlElementWrapper(name = "customers")
    private ArrayList<Customer> customers = new ArrayList<Customer>();
    
    @XmlElements({
        @XmlElement(name = "internet50", type = Internet50.class),
        @XmlElement(name = "internet100",type = Internet100.class),
        @XmlElement(name = "internetVIP",type = InternetVIP.class)
    })
    @XmlElementWrapper(name = "templates")
    private ArrayList<ITemplate> templates = new ArrayList<ITemplate>();

    {
        templates.add(new Internet50());
        templates.add(new Internet100());
        templates.add(new InternetVIP());
    }

    public Model() {
        this.orders = new ArrayList<Order>();
        this.instances = new ArrayList<Instance>();
        this.employees = new ArrayList<Employee>();
        this.customers = new ArrayList<Customer>();
        
        // Don't delete this part! It can be useful!
//        instances.add(new Instance(new InternetVIP(), 0));
//        instances.add(new Instance(new InternetVIP(), 0));
//        instances.add(new Instance(new InternetVIP(), 0));
//        instances.add(new Instance(new InternetVIP(), 0));
//        instances.add(new Instance(new InternetVIP(), 0));
//        instances.add(new Instance(new InternetVIP(), 0));
//        instances.add(new Instance(new InternetVIP(), 0));
//        instances.add(new Instance(new InternetVIP(), 0));
//        instances.add(new Instance(new InternetVIP(), 0));
//        instances.add(new Instance(new InternetVIP(), 0));
//        instances.add(new Instance(new InternetVIP(), 0));
//        instances.add(new Instance(new InternetVIP(), 0));
//        instances.add(new Instance(new InternetVIP(), 0));
//        instances.add(new Instance(new InternetVIP(), 0));
//        instances.add(new Instance(new InternetVIP(), 0));
//        instances.add(new Instance(new InternetVIP(), 0));
//        instances.add(new Instance(new InternetVIP(), 0));
//        instances.add(new Instance(new InternetVIP(), 0));
//        instances.add(new Instance(new InternetVIP(), 0));
//        instances.add(new Instance(new InternetVIP(), 0));
//        instances.add(new Instance(new InternetVIP(), 0));
//        instances.add(new Instance(new InternetVIP(), 0));
//        instances.add(new Instance(new InternetVIP(), 0));
//        instances.add(new Instance(new InternetVIP(), 0));
//        instances.add(new Instance(new InternetVIP(), 0));
//        instances.add(new Instance(new InternetVIP(), 0));
//        instances.add(new Instance(new InternetVIP(), 0));
//        instances.add(new Instance(new InternetVIP(), 0));
//        instances.add(new Instance(new InternetVIP(), 0));
//        instances.add(new Instance(new InternetVIP(), 0));
//        instances.add(new Instance(new InternetVIP(), 0));
//        instances.add(new Instance(new InternetVIP(), 0));
//        instances.add(new Instance(new InternetVIP(), 0));
//        instances.add(new Instance(new InternetVIP(), 0));
//        instances.add(new Instance(new InternetVIP(), 0));
//        instances.add(new Instance(new InternetVIP(), 0));
//        instances.add(new Instance(new InternetVIP(), 0));
//        instances.add(new Instance(new InternetVIP(), 0));
//        instances.add(new Instance(new InternetVIP(), 0));
//        instances.add(new Instance(new InternetVIP(), 0));
    }

    public Model(ArrayList<Order> orders, ArrayList<Instance> instances, ArrayList<Employee> employees, ArrayList<Customer> customers) {
        this.orders = orders;
        this.instances = instances;
        this.employees = employees;
        this.customers = customers;

    }

    public void setOrders(ArrayList<Order> orders) {
        this.orders = orders;
    }

    public void setInstances(ArrayList<Instance> instances) {
        this.instances = instances;
    }

    public void setEmployees(ArrayList<Employee> employees) {
        this.employees = employees;
    }

    public void setCustomers(ArrayList<Customer> customers) {
        this.customers = customers;
    }


//ILGO Comment below is broken, rewrite it in English or check it is saved correct in Russian.
//ILGO Комментарий ниже побился при сохранении, перепишите на английский или проверьте, что у вас правильно сохраняется на русском
// ��������� ��� ����������� ��������� �� ������ Id. (����� ���� ��� ������? ���� �� - �������� ����� � ���������)


    //ILGO add exception throwing in method declaration to make API users process possible exception raising (? tentative)
    //ILGO возможно, стоить добавить throws RuntimeException к описанию метода, чтобы заставить пользователей отлавливать ошибку. (обсуждаемо)
    @Override
    public Order getOrderById(long orderId) {
        for (Order order : orders) {
            if (order.getId() == orderId) return order;
        }
        throw new RuntimeException("Order not found");
    }

    @Override
    public Instance getInstanceById(long instanceId) {
        for (Instance instance : instances) {
            if (instance.getId() == instanceId) return instance;
        }
        throw new RuntimeException("Instance not found");
    }

    @Override
    public Customer getCustomerById(long customerId) {
        for (Customer customer : customers) {
            if (customer.getId() == customerId) return customer;
        }
        throw new RuntimeException("Customer not found");
    }

    @Override
    public Employee getEmployeeById(long employeeId) {
        for (Employee employee : employees) {
            if (employee.getId() == employeeId) return employee;
        }
        throw new RuntimeException("Employee not found");
    }

    //не допускаем появления одинаковых Order'ов(Никита)
    @Override
    public void addOrder(Order order) {
        if (!orders.contains(order))
            orders.add(order);
        else
            throw new RuntimeException("Order is already contains in org.ourgroup.provider.model");
    }

    //не допускаем появления одинаковых Instance'ов(Никита)
    @Override
    public void addInstance(Instance instance) {
        if (!instances.contains(instance))
            instances.add(instance);
        else
            throw new RuntimeException("Instance is already contains in org.ourgroup.provider.model");
    }

    //не допускаем появления одинаковых Customer'ов(Никита)
    @Override
    public void addCustomer(Customer customer) {
        if (!customers.contains(customer)) {
            customers.add(customer);
        } else
            throw new RuntimeException("Customer is already contains in org.ourgroup.provider.model");
    }

    //не допускаем появления одинаковых Employee'ов(Никита)
    @Override
    public void addEmployee(Employee employee) {
        if (!employees.contains(employee)) {

            employees.add(employee);
        } else
            throw new RuntimeException("Employee is already contains in org.ourgroup.provider.model");
    }

    @Override
    public ArrayList<Order> getOrders() {
        return orders;
    }

    @Override
    public ArrayList<Instance> getInstances() {
        return instances;
    }

    @Override
    public ArrayList<Employee> getEmployees() {
        return employees;
    }

    @Override
    public ArrayList<Customer> getCustomers() {
        return customers;
    }

    @Override
    public ArrayList<ITemplate> getTemplates() {
        return templates;
    }

}

