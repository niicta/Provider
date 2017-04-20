package org.ourgroup.provider.view.console;

import org.ourgroup.provider.controller.Controller;
import org.ourgroup.provider.model.ModelOperations;
import org.ourgroup.provider.model.instance.Instance;
import org.ourgroup.provider.model.order.Order;
import org.ourgroup.provider.model.order.OrderAction;
import org.ourgroup.provider.model.person.Customer;
import org.ourgroup.provider.model.person.Employee;
import org.ourgroup.provider.model.templates.ITemplate;

import javax.xml.bind.JAXBException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by friend_RU.
 */

public class View {
    static ModelOperations modelOperations = ModelOperations.getModelOperations();
    Employee employee;
    Customer customer;
    long personId;
    String personData;
    String temp;
    Controller controller = new Controller();
    Scanner in = new Scanner(System.in);


//    {
//       modelOperations.addCustomer(new Customer("Roman", "Khrapko"));
//       modelOperations.addEmployee(new Employee("Roman", "Khrapko"));
//    }
    

    public Customer getCustomer() {
        return customer;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }


    public void logInById() {
        try{
            modelOperations.demarshallModel();
        }
        catch(JAXBException ex){
            ex.printStackTrace();
        }
        do {
            System.out.print("\n\n\n\nEnter your Id: ");
            personId = in.nextLong();
            // "-1" - Exit
            if (personId < 0) {
                
                System.exit(-1);
            }

            try {
                customer = modelOperations.getCustomerById(personId);
                personData = "Client: " + customer.getName() + " " + customer.getSurname() + ". Balance "
                        + customer.getBalance() + " rub";
                break;
            } catch (RuntimeException runEx) {
                try {
                    employee = modelOperations.getEmployeeById(personId);
                    personData = "Employee: " + employee.getName() + " " + employee.getSurname();
                    break;
                } catch (RuntimeException runEx1) {
                    System.out.println("\n\n\n\n\n\nWrong Id. Try again.\n");
                }
            }
        } while (true);
    }

    public void customerView() {
        while (true) {
            System.out.println("\n\n\n\n" + personData + "\n"
                    + "1 - My services\n"
                    + "2 - View templates of services\n"
                    + "3 - Connect new service\n"
                    + "4 - Exit");
            switch (enterInt()) {
                case 1: {
                    myServices();
                    break;
                }
                case 2: {
                    templatesOfService();
                    break;
                }
                case 3: {
                    newService();
                    break;
                }
                case 4: {
                    try{
                    modelOperations.marshallModel();
                }
                catch(JAXBException ex){
                    ex.printStackTrace();
                }
                    customer = null;
                    return;
                }
                default: {
                    System.out.println("\n\n\n\nInvalid command\n");
                    break;
                }
            }
        }
    }
    public void myServices() {
        while (true) {
            int k = 1;
            ArrayList<Instance> instances = modelOperations.getInstances(customer.getId());

            for (int i = 0; i < instances.size(); i++) {
                System.out.println(k + ") " + instances.get(i).getTemplate().toString()
                        + "; Id" + instances.get(i).getId()
                        + "; Date of start: " + instances.get(i).getConnectionDate()
                        + "; Service status: " + instances.get(i).getStatus());
                k++;
            }
            if (k == 1) {
                System.out.println("\n\n\n\nNo connection services\n"
                        + "Enter any symbol for return to main menu.");
                in.next();
                return;
            }
            System.out.println(personData + "\n"
                    + "1 - Change status of service\n"
                    + "2 - Change service\n"
                    + "3 - Back");
            switch (enterInt()) {
                case 1: {
                    changeStatusOfService();
                    break;
                }
                case 2: {
                    changeService();
                    break;
                }
                case 3: {
                    return;
                }
                default: {
                    System.out.println("\n\n\n\nInvalid command\n");
                    break;
                }
            }
        }
    }
    public void newService() {
        while (true) {
            System.out.println("\n\n\n\nChoice new service: ");
            for (int i = 1; i <= modelOperations.getTemplates().size(); i++) {
                System.out.println(i + ") " + modelOperations.getTemplates().get(i - 1).toString());
            }
            ITemplate template = modelOperations.getTemplates().get(enterInt() - 1);

            modelOperations.createOrder(customer.getId(), OrderAction.CONNECT, template, null);
            System.out.println("Request sent to review");
            return;
        }
    }
    public void changeStatusOfService() {
        while (true) {
            System.out.println("\n\n\n\nChoice service: ");
            for (int i = 1; i <= modelOperations.getInstances(customer.getId()).size(); i++) {
                System.out.println(i + ") " + modelOperations.getInstances(customer.getId()).get(i - 1).toStrings());
            }
            Instance instance = modelOperations.getInstances(customer.getId()).get(enterInt() - 1);

            System.out.println("Choice action: \n"
                    + "1 - Suspend service\n"
                    + "2 - Restart service\n"
                    + "3 - Stop service\n"
                    + "4 - Back");
            modelOperations.createOrder(customer.getId(), OrderAction.values()[enterInt()],
                    instance.getTemplate(), instance);
            System.out.println("Request sent to review");
            return;
        }
    }
    public void changeService() {
        while (true) {
            System.out.println("\n\n\n\nChoice service: ");
            for (int i = 1; i <= modelOperations.getInstances(customer.getId()).size(); i++) {
                System.out.println(i + ") " + modelOperations.getInstances(customer.getId()).get(i - 1).toStrings());
            }
            Instance instance = modelOperations.getInstances(customer.getId()).get(enterInt() - 1);

            System.out.println("\n\n\n\nChoice new service: ");
            for (int i = 1; i <= modelOperations.getTemplates().size(); i++) {
                System.out.println(i + ") " + modelOperations.getTemplates().get(i - 1).toString());
            }
            ITemplate template = modelOperations.getTemplates().get(enterInt() - 1);

            modelOperations.createOrder(customer.getId(), OrderAction.CHANGE_TEMPLATE, template, instance);
            System.out.println("Request sent to review");
            return;
        }
    }
    public void templatesOfService() {
        System.out.println("\n\n\n\n");
            for (ITemplate t : modelOperations.getTemplates()) {
                System.out.println(t.toString());
            }
            System.out.println("\nEnter any symbol for return to main menu.");
            in.next();
    }
    
    
    public void employeeView() {
        while (true) {
            System.out.println(personData);
            temp = in.nextLine();
                while (true) {
                    try{
                    System.out.println("insert command");                    
                    temp = in.nextLine();
                    String [] command = temp.split(" ");
                    switch (command[0]) {
                        case "help": {
                            try{
                              commandHelp(command[1]);
                              break;
                            }
                            catch(ArrayIndexOutOfBoundsException e){
                               help(); 
                               break;
                            }
                        }
                        case "see_not_started_orders": {
                            seeNotStartedOrders();
                            break;
                        }
                        case "see_all_orders": {
                            seeAllOrders();
                            break;
                        }
                        case "see_all_instances": {
                            seeAllInstances();
                            break;
                        }
                        case "exit": {
                            try{
                    modelOperations.marshallModel();
                }
                catch(JAXBException ex){
                    ex.printStackTrace();
                }
                            employee = null;
                            return;
                        }
                        default: {
                            controller.parseCommand(temp);
                            break;
                        }
                    }
                    }
                    catch(RuntimeException ex){
                        System.out.println(ex.getMessage());
                    }
                }
            }
        
    }
    public void seeAllInstances() {                // May be need \n... \n...
        ArrayList<Instance> instances = modelOperations.getInstances();
        for (int i = 0; i < instances.size(); i++) {
            System.out.println(instances.get(i).getTemplate().toString()
                    + "; Id" + instances.get(i).getId()
                    + "; Дата начала: " + instances.get(i).getConnectionDate()
                    + "; Статус услуги: " + instances.get(i).getStatus());
        }
    }
    public void seeNotStartedOrders() {
        ArrayList<Order> orders = modelOperations.getNotStartedOrders();
        for (int i = 0; i < orders.size(); i++) {
            if (orders.get(i).getInstance() == null) {
                System.out.println("Id:" + orders.get(i).getId() + "; customerId: "
                        + orders.get(i).getCustomerId() +  
                        (orders.get(i).getEmployeeId() == -1? "": "; employeeId: " + orders.get(i).getEmployeeId()) 
                        + "; action: " + orders.get(i).getAction().name()
                        + "; service:" + orders.get(i).getTemplate().getName() + "; instanceId: not found");
            } else {
                System.out.println("Id:" + orders.get(i).getId() + "; customerId: "
                        + orders.get(i).getCustomerId() +  
                        (orders.get(i).getEmployeeId() == -1? "": "; employeeId: " + orders.get(i).getEmployeeId()) 
                        + "; action: " + orders.get(i).getAction().name()
                        + "; service:" + orders.get(i).getTemplate().getName() + "; instanceId: " + orders.get(i).getInstance().getId());
            }
        }

    }
    public void seeAllOrders() {
        ArrayList<Order> orders = modelOperations.getOrders();
        for (int i = 0; i < orders.size(); i++) {
            if (orders.get(i).getInstance() == null) {
                System.out.println("Id:" + orders.get(i).getId() + "; customerId: " 
                        + orders.get(i).getCustomerId() + 
                        (orders.get(i).getEmployeeId() == -1? "": "; employeeId: " + orders.get(i).getEmployeeId()) 
                        + "; action: " + orders.get(i).getAction()
                        + "; service:" + orders.get(i).getTemplate().getName() + "; instanceId: notfound; status: "
                        + orders.get(i).getStatus().name());
            } else {
                System.out.println("Id:" + orders.get(i).getId() + "; customerId: "
                        + orders.get(i).getCustomerId() + 
                        (orders.get(i).getEmployeeId() == -1? "": "; employeeId: " + orders.get(i).getEmployeeId()) 
                        + "; action: " + orders.get(i).getAction()
                        + "; service:" + orders.get(i).getTemplate().getName() + "; instanceId: "
                        + orders.get(i).getInstance().getId() + "; status" + orders.get(i).getStatus().name());
            }
        }

    }
    public static void help() {     // Need extended help
        System.out.println("see_not_started_orders \nsee_all_orders\nsee_all_instances\nexit");
        System.out.println("employee_start_order <orderid> <employeeid>");
        System.out.println("employee_create_ins <orderid> <employeeid>");
        System.out.println("employee_set_ins_status <orderid> <employeeid>");
        System.out.println("employee_set_ins_temp <orderid> <employeeid>");
        System.out.println("employee_finish_order <orderid> <employeeid>");
    }

    private int enterInt() {
        return in.nextInt();
    }
    public void commandHelp(String command){
        switch(command){
                        case "see_not_started_orders":{
                            System.out.println("Command see_not_started_orders allows you to see all orders that was not started");
                            break;
                        }
                        case "see_all_orders": {
                            System.out.println("Command see_all_orders allows you to see all your orders and all not started orders");
                            break;
                        }
                        case "see_all_instances": {
                            System.out.println("Command see_all_instances allows you to see all instances");
                            break;
                        }
                        case "employee_start_order":{
                           System.out.println("Command employee_start_order <orderId> <employeeId> allows you to start work with new order"); 
                           break;
                        }
                        case "employee_create_ins":{
                            System.out.println("Command employee_create_ins <orderId> <employeeId> allows you to create instance for new order");
                            break;
                        }
                        case "employee_set_ins_status":{
                            System.out.println("Command employee_set_ins_status <orderId> <employeeId> allows you to change status in order's instance");
                            break;
                        }
                        case "employee_set_ins_temp":{
                            System.out.println("Command employee_set_ins_temp <orderId> <employeeId> allows you to set or change template in instance");
                            break;
                        }
                        case "employee_finish_order":{
                            System.out.println("Command employee_finish_order <orderId> <employeeId> allows you to fnish the order");
                            break;
                        }
                        default:{
                            System.out.println("Command not found");
                            break;
                        }

                    }
    }
}



