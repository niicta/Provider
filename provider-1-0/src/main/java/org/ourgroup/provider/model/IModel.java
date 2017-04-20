package org.ourgroup.provider.model;

import org.ourgroup.provider.model.instance.Instance;
import org.ourgroup.provider.model.order.Order;
import org.ourgroup.provider.model.person.Customer;
import org.ourgroup.provider.model.person.Employee;
import org.ourgroup.provider.model.templates.ITemplate;

import java.util.ArrayList;

/**
 * Created by friend_RU.
 */

public interface IModel {

    Order getOrderById(long orderId);

    Instance getInstanceById(long instanceId);

    Customer getCustomerById(long customerId);

    Employee getEmployeeById(long employeeId);

    void addOrder(Order order);

    void addInstance(Instance instance);

    void addCustomer(Customer customer);

    void addEmployee(Employee employee);

    ArrayList<Order> getOrders();

    ArrayList<Instance> getInstances();

    ArrayList<Employee> getEmployees();

    ArrayList<Customer> getCustomers();

    ArrayList<ITemplate> getTemplates();

}
