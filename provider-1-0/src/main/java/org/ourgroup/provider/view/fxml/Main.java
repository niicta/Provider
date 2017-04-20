package org.ourgroup.provider.view.fxml;

import javafx.application.Application;
import javafx.stage.Stage;
import org.ourgroup.provider.view.fxml.login.LoginController;
import org.ourgroup.provider.model.ModelOperations;

/**
 * Created by friend_RU.
 */

public class Main extends Application {

    private Stage  primaryStage;

    public static ModelOperations modelOperations = ModelOperations.getModelOperations();

    public static void main(String[] args) {
        launch();
    }

    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        initialize();


        LoginController lc = new LoginController();
    }

    private void initialize() {

// Don't delete this part! It can be useful!
        
//        Customer c = new Customer("Roman", "Khrapko");
//        c.setBalance(25000);
//        modelOperations.addCustomer(c);
//        modelOperations.addEmployee(new Employee("Romka", "Hrop"));
//        modelOperations.createOrder(0, OrderAction.CONNECT, new Internet50(), null);
//        modelOperations.createOrder(0, OrderAction.CONNECT, new Internet50(), null);
//        modelOperations.createOrder(0, OrderAction.CONNECT, new Internet50(), null);
//        modelOperations.getOrderByID(1).setEmployeeId(1);
//        modelOperations.createOrder(0, OrderAction.CONNECT, new Internet50(), null);
//        modelOperations.createOrder(0, OrderAction.CONNECT, new Internet50(), null);
//        modelOperations.createOrder(0, OrderAction.CONNECT, new Internet50(), null);
//        modelOperations.createOrder(0, OrderAction.CONNECT, new Internet50(), null);
//        modelOperations.createOrder(0, OrderAction.CONNECT, new Internet50(), null);
//        modelOperations.createOrder(0, OrderAction.CONNECT, new Internet50(), null);
//        modelOperations.createOrder(0, OrderAction.CONNECT, new Internet50(), null);
//        modelOperations.createOrder(0, OrderAction.CONNECT, new Internet50(), null);

    }
}
