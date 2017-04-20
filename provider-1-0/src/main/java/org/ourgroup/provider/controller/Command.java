/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ourgroup.provider.controller;

import org.ourgroup.provider.model.ModelOperations;
import org.ourgroup.provider.model.instance.Instance;
import org.ourgroup.provider.model.order.OrderAction;
import org.ourgroup.provider.model.person.Customer;
import org.ourgroup.provider.model.person.Employee;
import org.ourgroup.provider.model.templates.ITemplate;
import org.ourgroup.provider.model.templates.Internet100;
import org.ourgroup.provider.model.templates.Internet50;
import org.ourgroup.provider.model.templates.InternetVIP;

/**
 * @author Никита
 */
public enum Command {
    //command with "employee_" executes only from emloyees ("employee_" can be reduced to the "emp_", I think")
    //command without this one executes only from customers
    //
    Command1("registration") {
        @Override
        public void execute(String[] params) {
            //params must be look as "<command> <name> <surname>"
            //String[] params = s.split(" ");
            if (params.length != 3)
                throw new RuntimeException("Invalid params!");
            //creating customer
            Customer customer = new Customer(params[1], params[2]);
            //and sending this one to the org.ourgroup.provider.model
            super.modelOperations.addCustomer(customer);

        }
    },
    Command2("employee_registration") {
        @Override
        public void execute(String[] params) {
            //params must be look as "<command> <name> <surname>"

            if (params.length != 3)
                throw new RuntimeException("Invalid params!");
            //creating employee
            Employee employee = new Employee(params[1], params[2]);
            //and sending this one to the org.ourgroup.provider.model
            super.modelOperations.addEmployee(employee);

        }
    },
    Command3("connect") {
        @Override
        public void execute(String[] params) {
            //params must be look as "<command> <templateName> <Id>"

            if (params.length != 3)
                throw new RuntimeException("Invalid params!");

            long customerId;

            try {
                customerId = Long.parseLong(params[2]);
            } catch (NumberFormatException e) {
                throw new RuntimeException("Invalid params!");
            }

            ITemplate t;
            switch (params[1]) {
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
                    throw new RuntimeException("Invalid params!");

            }
            //sending Order with "connect"-action to the org.ourgroup.provider.model
            super.modelOperations.createOrder(customerId, OrderAction.CONNECT, t, null);
        }
    },
    Command4("change_status") {
        @Override
        public void execute(String[] params) {
            //params must be look as "<command> <status> <instanceId> <Id>"

            if (params.length != 4)
                throw new RuntimeException("Invalid params!");

            long customerId;
            long instanceId;
            try {
                customerId = Long.parseLong(params[3]);
                instanceId = Long.parseLong(params[2]);
            } catch (NumberFormatException e) {
                throw new RuntimeException("Invalid params!");
            }
            OrderAction action;
            switch (params[1]) {
                case "restart":
                    action = OrderAction.RESTART;
                    break;
                case "suspend":
                    action = OrderAction.SUSPEND;
                    break;
                case "disconnect":
                    action = OrderAction.STOP;
                    break;
                default:
                    throw new RuntimeException("Invalid params!");

            }
            Instance ins = super.modelOperations.getInstanceById(instanceId);
            //sending Order with appropriate action to the org.ourgroup.provider.model
            super.modelOperations.createOrder(customerId, action, null, ins);
        }

    },
    Command5("change_temp") {
        @Override
        public void execute(String[] params) {
            //params must be look as "<command> <templateName> <instanceId> <Id>"

            if (params.length != 4)
                throw new RuntimeException("Invalid params!");

            long customerId;
            long instanceId;
            try {
                customerId = Long.parseLong(params[3]);
                instanceId = Long.parseLong(params[2]);
            } catch (NumberFormatException e) {
                throw new RuntimeException("Invalid params!");
            }
            ITemplate t;
            switch (params[1]) {
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
                    throw new RuntimeException("Invalid params!");
            }
            Instance ins = super.modelOperations.getInstanceById(instanceId);
            //sending Order with "CHANGE_TEMPLATE" action to the org.ourgroup.provider.model
            super.modelOperations.createOrder(customerId, OrderAction.CHANGE_TEMPLATE, t, ins);
        }
    },
    Command6("employee_start_order") {
        @Override
        public void execute(String[] params) {
            //params must be look as "<command> <orderid> <employeeid>"

            if (params.length != 3)
                throw new RuntimeException("Invalid params!");
            long employeeId;
            long orderId;
            try {
                employeeId = Long.parseLong(params[2]);
                orderId = Long.parseLong(params[1]);
            } catch (NumberFormatException e) {
                throw new RuntimeException("Invalid params!");
            }
            super.modelOperations.startOrder(orderId, employeeId);
        }

    }, // пользуемся ли мы им? а то можно
    Command7("employee_create_ins") {
        @Override
        public void execute(String[] params) {
            //params must be look as "<command> <oderid> <employeeid>

            if (params.length != 3)
                throw new RuntimeException("Invalid params!");
            long employeeId;
            long orderId;
            try {
                employeeId = Long.parseLong(params[2]);
                orderId = Long.parseLong(params[1]);
            } catch (NumberFormatException e) {
                throw new RuntimeException("Invalid params!");
            }
            super.modelOperations.createInstance(orderId, employeeId);
        }

    },
    Command8("employee_set_ins_status") {
        @Override
        public void execute(String[] params) {
            //params must be look as "<command> <oderid> <employeeid>

            if (params.length != 3)
                throw new RuntimeException("Invalid params!");
            long employeeId;
            long orderId;
            try {
                employeeId = Long.parseLong(params[2]);
                orderId = Long.parseLong(params[1]);
            } catch (NumberFormatException e) {
                throw new RuntimeException("Invalid params!");
            }
            super.modelOperations.setInstanceStatus(orderId, employeeId);
        }

    },
    Command9("employee_set_ins_temp") {
        @Override
        public void execute(String[] params) {
            //params must be look as "<command> <oderid> <employeeid>

            if (params.length != 3)
                throw new RuntimeException("Invalid params!");
            long employeeId;
            long orderId;
            try {
                employeeId = Long.parseLong(params[2]);
                orderId = Long.parseLong(params[1]);
            } catch (NumberFormatException e) {
                throw new RuntimeException("Invalid params!");
            }
            super.modelOperations.setInstanceTemplate(orderId, employeeId);
        }

    },
    Command10("employee_complete_order") {
        @Override
        public void execute(String[] params) {
            //params must be look as "<command> <orderid> <employeeid>

            if (params.length != 3)
                throw new RuntimeException("Invalid params!");
            long employeeId;
            long orderId;
            try {
                employeeId = Long.parseLong(params[2]);
                orderId = Long.parseLong(params[1]);
            } catch (NumberFormatException e) {
                throw new RuntimeException("Invalid params!");
            }
            super.modelOperations.completeOrder(orderId, employeeId);
        }

    };

    protected ModelOperations modelOperations = ModelOperations.getModelOperations();
    private String command;

    public String getCommand() {
        return command;
    }

    Command(String command) {
        this.command = command;
    }

    public static Command parseCommand(String s) {
        for (Command c : values()) {
            if (c.getCommand().equals(s))
                return c;
        }
        throw new RuntimeException("Invalid command name!");
    }

    public abstract void execute(String[] s);
}
