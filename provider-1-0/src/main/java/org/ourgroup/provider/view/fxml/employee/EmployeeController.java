package org.ourgroup.provider.view.fxml.employee;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.ourgroup.provider.view.fxml.login.LoginController;
import org.ourgroup.provider.controller.Controller;
import org.ourgroup.provider.model.ModelOperations;
import org.ourgroup.provider.model.order.Order;
import org.ourgroup.provider.model.order.OrderStatus;
import org.ourgroup.provider.model.person.Employee;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

public class EmployeeController implements Observer {

    private Employee employee;
    private Stage employeeStage;
    private ModelOperations mO = ModelOperations.getModelOperations();
    private int currentPage = 0; // 0 - All orders, 1 - Not started orders, 2 - Complete orders

    @FXML
    private Label name;

    @FXML
    private Label surname;

    @FXML
    private Label exit;

    @FXML
    private VBox vboxYourOrders;

    @FXML
    private ScrollPane yourOrdersScroll;

    @FXML
    private Label allOrdersLabel;

    @FXML
    private VBox vboxAllOrders;

    @FXML
    private ScrollPane allOrdersScroll;

    public EmployeeController(Employee employee) throws IOException {
        this.employee = employee;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("employee.fxml"));
        fxmlLoader.setController(this);
        Scene scene = new Scene(fxmlLoader.load());

        name.setText(this.employee.getName());
        surname.setText(this.employee.getSurname());

        employeeStage = new Stage();
        employeeStage.setScene(scene);
        employeeStage.setMinWidth(780);
        employeeStage.setMinHeight(600);

        update(mO, null);

        mO.addObserver(this);

        employeeStage.show();
    }

    @FXML
    private void exitMouseClicked(MouseEvent event) throws IOException, JAXBException {
        //update(mO, null); //Temple function
        mO.deleteObserver(this);
        mO.marshallModel();
        LoginController loginController = new LoginController();
        vboxYourOrders.getChildren().removeAll(vboxYourOrders.getChildren());
        employeeStage.close();
    }

    @FXML
    private void lblAllOrdersClicked(MouseEvent event) {
        ModalWindow modalWindow = new ModalWindow();
        modalWindow.newWindow();
    }

    private HBox getHBoxYourOrder(Order order) {
        HBox hBoxOrder = new HBox();
        hBoxOrder.setSpacing(5);

        Label lblAction = new Label();
        switch (order.getAction().toString()) {
            case ("CONNECT"): {
                lblAction.setText("Connecting");
                break;
            }
            case ("SUSPEND"): {
                lblAction.setText("Suspending");
                break;
            }
            case ("RESTART"): {
                lblAction.setText("Restarting");
                break;
            }
            case ("STOP"): {
                lblAction.setText("Stopping");
                break;
            }
            case ("CHANGE_TEMPLATE"): {
                lblAction.setText("Changing");
                break;
            }
            default: {
                lblAction.setText("Unknown action");
                break;
            }
        }

        hBoxOrder.getChildren().add(lblAction);
        try {
            Label lblTemplate = new Label("\"" + order.getTemplate().getName() + "\"");
            if (lblAction.getText().equals("Connecting")) {
                hBoxOrder.getChildren().add(lblTemplate);
            } else if (lblAction.getText().equals("Changing")) {
                Label lblInstanceTemplate = new Label("\"" + order.getInstance().getTemplate().getName() + "\"");
                Label lblOn = new Label("on");
                hBoxOrder.getChildren().addAll(lblInstanceTemplate, lblOn, lblTemplate);
            }
        } catch (NullPointerException e) {
            Label lblInstanceTemplate = new Label("\"" + order.getInstance().getTemplate().getName() + "\"");
            hBoxOrder.getChildren().add(lblInstanceTemplate);
        }

        Label lblStatusStr = new Label("status:");

        Label lblStatus = new Label();
        switch (order.getStatus().toString()) {
            case ("NOT_STARTED"): {
                lblStatus.setId("notStartedStatus");
                lblStatus.setText("Not started");
                break;
            }
            case ("IN_PROCESS"): {
                lblStatus.setId("processStatus");
                lblStatus.setText("In process");
                break;
            }
            case ("COMPLETE"): {
                lblStatus.setId("completeStatus");
                lblStatus.setText("Complete");
                break;
            }
        }
        hBoxOrder.getChildren().addAll(lblStatusStr, lblStatus);
        hBoxOrder.getChildren().addAll(getLabelComplete(order));

        return hBoxOrder;
    }

    private HBox getHBoxAllOrder(Order order) {
        HBox hBoxOrder = new HBox();
        hBoxOrder.setSpacing(5);

        Label lblAction = new Label();
        switch (order.getAction().toString()) {
            case ("CONNECT"): {
                lblAction.setText("Connecting");
                break;
            }
            case ("SUSPEND"): {
                lblAction.setText("Suspending");
                break;
            }
            case ("RESTART"): {
                lblAction.setText("Restarting");
                break;
            }
            case ("STOP"): {
                lblAction.setText("Stopping");
                break;
            }
            case ("CHANGE_TEMPLATE"): {
                lblAction.setText("Changing");
                break;
            }
            default: {
                lblAction.setText("Unknown action");
                break;
            }
        }

        hBoxOrder.getChildren().add(lblAction);
        try {
            Label lblTemplate = new Label("\"" + order.getTemplate().getName() + "\"");
            if (lblAction.getText().equals("Connecting")) {
                hBoxOrder.getChildren().add(lblTemplate);
            } else if (lblAction.getText().equals("Changing")) {
                Label lblInstanceTemplate = new Label("\"" + order.getInstance().getTemplate().getName() + "\"");
                Label lblOn = new Label("on");
                hBoxOrder.getChildren().addAll(lblInstanceTemplate, lblOn, lblTemplate);
            }
        } catch (NullPointerException e) {
            Label lblInstanceTemplate = new Label("\"" + order.getInstance().getTemplate().getName() + "\"");
            hBoxOrder.getChildren().add(lblInstanceTemplate);
        }

        Label lblStatusStr = new Label("status:");

        Label lblStatus = new Label();
        switch (order.getStatus().toString()) {
            case ("NOT_STARTED"): {
                lblStatus.setText("Not started");
                break;
            }
            case ("IN_PROCESS"): {
                lblStatus.setText("In process");
                break;
            }
            case ("COMPLETE"): {
                lblStatus.setText("Complete");
                break;
            }
        }
        hBoxOrder.getChildren().addAll(lblStatusStr, lblStatus);
        if (order.getStatus().equals(OrderStatus.NOT_STARTED)) {
            hBoxOrder.getChildren().add(getLblTake(order));
        }

        return hBoxOrder;
    }

    public void update(Observable o, Object arg) {
        cleanAll();
        ModelOperations mo = (ModelOperations) o;

        for (Order order : mo.getOrders()) {
            if ((order.getEmployeeId() == employee.getId()) && (!order.getStatus().equals(OrderStatus.COMPLETE))) {
                order.setStatus(OrderStatus.IN_PROCESS);
                vboxYourOrders.getChildren().add(getHBoxYourOrder(order));
            }
            switch (currentPage) {
                case 1: {
                    if (order.getStatus().equals(OrderStatus.NOT_STARTED)) {
                        vboxAllOrders.getChildren().add(getHBoxAllOrder(order));
                    }
                    break;
                }
                case 2: {
                    if (order.getStatus().equals(OrderStatus.COMPLETE)) {
                        vboxAllOrders.getChildren().add(getHBoxAllOrder(order));
                    }
                    break;
                }
                default: {
                    vboxAllOrders.getChildren().add(getHBoxAllOrder(order));
                }
            }
        }
    }

    private Label getLabelComplete(Order order) {
        Label lblComplete = new Label("Complete");
        lblComplete.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent arg0) {
                Controller.parseCommand("employee_complete_order " + order.getId() + " " + employee.getId());
                update(mO, null);
            }
        });
        return lblComplete;
    }

    private Label getLblTake(Order order) {
        Label lblTake = new Label("Take to process");
        lblTake.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent arg0) {
                Controller.parseCommand("employee_start_order " + order.getId() + " " + employee.getId());
                update(mO, null);
            }
        });
        return lblTake;
    }

    private void cleanAll() {
        vboxYourOrders.getChildren().removeAll(vboxYourOrders.getChildren());
        vboxAllOrders.getChildren().removeAll(vboxAllOrders.getChildren());
    }

    private class ModalWindow {

        public void newWindow() {
            Stage window = new Stage();

            window.initModality(Modality.APPLICATION_MODAL);

            VBox vBox = new VBox();
            vBox.setAlignment(Pos.CENTER);
            vBox.setSpacing(10);

            Scene scene = new Scene(vBox);
            window.setScene(scene);
            window.setTitle("Choice list of orders");

            Label lblAllOrders = new Label("All orders");
            Label lblNotStartedOrders = new Label("Not started orders");
            Label lblCompletedOrders = new Label("Completed orders");

            lblAllOrders.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent arg0) {
                    allOrdersLabel.setText("All orders");
                    currentPage = 0;
                    update(mO, null);
                    window.close();
                }
            });

            lblNotStartedOrders.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent arg0) {
                    allOrdersLabel.setText("Not started orders");
                    currentPage = 1;
                    update(mO, null);
                    window.close();
                }
            });

            lblCompletedOrders.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent arg0) {
                    allOrdersLabel.setText("Completed orders");
                    currentPage = 2;
                    update(mO, null);
                    window.close();
                }
            });

            vBox.getChildren().addAll(lblAllOrders, lblNotStartedOrders, lblCompletedOrders);

            window.showAndWait();
        }
    }
}