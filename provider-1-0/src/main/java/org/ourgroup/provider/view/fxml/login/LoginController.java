package org.ourgroup.provider.view.fxml.login;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.ourgroup.provider.view.fxml.customer.CustomerController;
import org.ourgroup.provider.view.fxml.employee.EmployeeController;
import org.ourgroup.provider.model.ModelOperations;
import org.ourgroup.provider.model.person.Customer;
import org.ourgroup.provider.model.person.Employee;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoginController {

    private Stage loginStage;

    @FXML
    private Label messageId;

    @FXML
    private Label balance;

    @FXML
    private Label surname;

    @FXML
    private Label name;

    @FXML
    private ScrollPane allInstancesScroll;

    @FXML
    private ScrollPane yourInstancesScroll;

    @FXML
    private ScrollPane yourordersScroll;

    @FXML
    private Button btnSignIn;

    @FXML
    private TextField textField;

    @FXML
    private void textFieldMousePressed(MouseEvent event) {
        messageId.setVisible(false);
    }

    @FXML
    private void panelKeyPressed(Event event) { messageId.setVisible(false); }

    @FXML
    private void textFieldAction(ActionEvent event) throws IOException {
        enter(event);
    }

    @FXML
    private void textFieldKeyTyped(Event event) {
        messageId.setVisible(false);
    }

    @FXML
    private void btnSignInClicks(ActionEvent event) throws InterruptedException, IOException {
        enter(event);
    }

    public LoginController() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("login.fxml"));
        fxmlLoader.setController(this);
        loginStage = new Stage();

        Scene scene = new Scene(fxmlLoader.load());

        loginStage.setScene(scene);
        loginStage.setMinHeight(400);
        loginStage.setMinWidth(600);
        loginStage.show();
    }

    private void enter(ActionEvent event) throws IOException {
        ModelOperations mo = ModelOperations.getModelOperations();
        try {
            mo.demarshallModel();
        } catch (JAXBException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            
            Customer customer = mo.getCustomerById(Long.parseLong(textField.getText()));
            CustomerController cc = new CustomerController(customer);
            loginStage.close();
        } catch (RuntimeException runEx) {
            try {
                Employee employee = mo.getEmployeeById(Long.parseLong(textField.getText()));
                EmployeeController ec = new EmployeeController(employee);
                loginStage.close();
            } catch (RuntimeException runEx1) {
                messageId.setVisible(true);
                //runEx.printStackTrace();
            }
        }
    }
}
