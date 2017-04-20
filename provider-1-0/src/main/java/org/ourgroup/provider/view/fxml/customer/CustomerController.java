package org.ourgroup.provider.view.fxml.customer;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.ourgroup.provider.controller.Controller;
import org.ourgroup.provider.model.ModelOperations;
import org.ourgroup.provider.model.instance.Instance;
import org.ourgroup.provider.model.instance.InstanceStatus;
import org.ourgroup.provider.model.order.Order;
import org.ourgroup.provider.model.order.OrderStatus;
import org.ourgroup.provider.model.person.Customer;
import org.ourgroup.provider.model.templates.ITemplate;
import org.ourgroup.provider.view.fxml.login.LoginController;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;

public class CustomerController implements Observer {

    private Customer customer;
    private Stage customerStage;
    private ITemplate choiceTemplate = null;
    private ModelOperations mO = ModelOperations.getModelOperations();

    @FXML
    private VBox vboxYourInstances;

    @FXML
    private VBox vboxAllInstances;

    @FXML
    private VBox vboxYourOrders;

    @FXML
    private Label exit;

    @FXML
    private ScrollPane yourOrdersScroll;

    @FXML
    private ScrollPane allInstancesScroll;

    @FXML
    private ScrollPane yourInstancesScroll;

    @FXML
    private Label balance;

    @FXML
    private Label surname;

    @FXML
    private Label name;

    @FXML
    private Pane mainPane;

    public CustomerController(Customer customer) throws IOException {
        this.customer = customer;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("customer.fxml"));
        fxmlLoader.setController(this);
        Scene scene = new Scene((Parent) fxmlLoader.load());

        balance.setText(String.valueOf(this.customer.getBalance()));
        name.setText(this.customer.getName());
        surname.setText(this.customer.getSurname());

        customerStage = new Stage();
        customerStage.setScene(scene);
        customerStage.setMinWidth(780);
        customerStage.setMinHeight(600);

        mO.addObserver(this);

        customerStage.show();
    }

    @FXML
    private void exitMouseClicked(MouseEvent event) throws IOException, JAXBException {
        mO.deleteObserver(this);
        LoginController loginController = new LoginController();
        mO.marshallModel();
        customerStage.close();
    }

    private HBox getHBoxYourInstance(Instance instance) {

        HBox hBoxInstance = new HBox();
        hBoxInstance.setSpacing(5);

        Label lblInstanceName = new Label("\"" + instance.getTemplate().getName() + "\"");
        Label lblStatus = new Label("Status:");
        Label lblInstanceStatus = new Label();

        switch (String.valueOf(instance.getStatus())) {
            case ("ACTIVE"): {
                lblInstanceStatus.setText("Active");
                break;
            }
            case ("UNACTIVE"): {
                lblInstanceStatus.setText("Not active");
                break;
            }
            case ("SUSPEND"): {
                lblInstanceStatus.setText("Suspended");
                break;
            }
        }

        hBoxInstance.getChildren().addAll(lblInstanceName, lblStatus, lblInstanceStatus);

        if (lblInstanceStatus.getText().equals("Active")) {
            Label lblTimeOfAction = new Label("Time of action:");
            Label lblInstanceTimeOfAction = new Label(getTimeOfAction(instance) +
                    (getTimeOfAction(instance) == 1 ? " day" : " days"));

            hBoxInstance.getChildren().addAll(//lblConnected, lblInstanceDateOfConnection,
                    lblTimeOfAction, lblInstanceTimeOfAction);
        }
        hBoxInstance.getChildren().addAll(getLblSuspendActivate(instance), getLblChange(instance), getLblStop(instance));

        return hBoxInstance;
    }

    private Label getLblStop(final Instance instance) {
        final Label lblStop = new Label("Stop");
        if (isFreeInstance(instance)) {
            lblStop.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent arg0) {
                    Controller.parseCommand("change_status disconnect " + instance.getId() + " " + customer.getId());
//                    mO.createOrder(customer.getId(), OrderAction.STOP, null, instance);
                    deactivateLabel(lblStop);
                }
            });
        } else {
            deactivateLabel(lblStop);
        }
        return lblStop;
    }

    private Label getLblSuspendActivate(final Instance instance) {
        final Label lblSuspendActivate = new Label((instance.getStatus() == InstanceStatus.ACTIVE) ? "Suspend" : "Activate");
        if (isFreeInstance(instance)) {
            lblSuspendActivate.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent arg0) {
                    if (instance.getStatus() == InstanceStatus.ACTIVE) {
                        Controller.parseCommand("change_status suspend " + instance.getId() + " " + customer.getId());
                        deactivateLabel(lblSuspendActivate);
                    } else if (instance.getStatus() == InstanceStatus.SUSPEND) {
                        Controller.parseCommand("change_status restart " + instance.getId() + " " + customer.getId());
                        deactivateLabel(lblSuspendActivate);
                    }
                }
            });
        } else {
            deactivateLabel(lblSuspendActivate);
        }
        return lblSuspendActivate;
    }

    private Label getLblChange(final Instance instance) {
        final Label lblChange = new Label("Change");
        if (isFreeInstance(instance)) {
            lblChange.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent arg0) {
                    ModalWindow modalWindow = new ModalWindow();
                    modalWindow.newWindow(instance);
                    try {
                        if (!choiceTemplate.equals(null)) {
                            choiceTemplate = null;
                            deactivateLabel(lblChange);
                        }
                    } catch (NullPointerException e) {
                    }
                }
            });
        } else {
            deactivateLabel(lblChange);
        }
        return lblChange;
    }

    private boolean isFreeInstance(Instance instance) { // Добавить проверку на актуальность ордера - чтобы он был не комплит, а то ордера завершены будут а инсты не доступны

        for (Order order : mO.getOrders(customer.getId())) {//(order.getAction() != OrderAction.CONNECT) ||
            if (!(order.getStatus().equals(OrderStatus.COMPLETE)))  {

                if (order.getInstance() != null && order.getInstance().getId() == (instance.getId())) {
                    return false;
                }
            }
        }
        return true;
    }

    private void deactivateLabel(Label label) {
        label.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent arg0) {
            }
        });//Предотвращаем повторное нажатие
        label.setOpacity(0.5);
    }

    private HBox getHBoxAllInstance(ITemplate template) {
        HBox hBoxTemplate = new HBox();
        hBoxTemplate.setSpacing(5);

        Label lblTemplateName = new Label("\"" + template.getName() + "\"");

        Label lblCost = new Label("Cost:");
        Label lblTemplateCost = new Label(String.valueOf(template.getCost()));

        Label lblLimit = new Label("Limit:");
        Label lblTemplateLimit = new Label(String.valueOf(template.getLimit()));

        Label lblSpeed = new Label("Speed:");
        Label lblTemplateSpeed = new Label(String.valueOf(template.getSpeed()));

        hBoxTemplate.getChildren().addAll(lblTemplateName, lblCost, lblTemplateCost, lblLimit, lblTemplateLimit,
                lblSpeed, lblTemplateSpeed, getLblConnect(template));

        return hBoxTemplate;
    }

    private Label getLblConnect(final ITemplate template) {
        final Label lblConnect = new Label("Connect");
        lblConnect.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent arg0) {
                try {
                    Controller.parseCommand("connect " + template.getName() + " " + customer.getId());
                    deactivateLabel(lblConnect);
                } catch (RuntimeException ex) {
                    ModalWindow window = new ModalWindow();
                    window.newWindow(ex);
                }
            }
        });
        return lblConnect;
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

        return hBoxOrder;
    }

    public int getTimeOfAction(Instance instance) {
        Date date = new Date();
        int delta = instance.getDelta();
        return 29 - (int) ((date.getTime() - instance.getConnectionDate().getTime()) / 1000 / 60 / 60 / 24) + delta;
    }

    public void update(Observable o, Object arg) {
        cleanAll();
        ModelOperations mo = (ModelOperations) o;

        balance.setText(Double.toString(mo.getCustomerById(customer.getId()).getBalance()));

        for (Instance instance : mo.getInstances(customer.getId())) {               //refactor
                vboxYourInstances.getChildren().add(getHBoxYourInstance(instance));
        }

        for (ITemplate template : mO.getTemplates()) {
            vboxAllInstances.getChildren().add(getHBoxAllInstance(template));
        }

        for (Order order : mo.getOrders(customer.getId())) {                        //refactor
            vboxYourOrders.getChildren().add(getHBoxYourOrder(order));
        }
    }

    private void cleanAll() {
        vboxYourInstances.getChildren().removeAll(vboxYourInstances.getChildren());
        vboxAllInstances.getChildren().removeAll(vboxAllInstances.getChildren());
        vboxYourOrders.getChildren().removeAll(vboxYourOrders.getChildren());
    }

    private class ModalWindow {

        public void newWindow(RuntimeException ex) {
            Stage window = new Stage();

            window.initModality(Modality.APPLICATION_MODAL);

            VBox box = new VBox();

            box.setPrefHeight(100);
            box.setMinWidth(300);

            Label message = new Label("   " + ex.getMessage() + "   ");
            ex.printStackTrace();
            message.setFont(new Font(24));

            Button btn = new Button("    OK    ");
            btn.setOnMouseClicked(event -> {
                window.close();
            });

            box.setAlignment(Pos.CENTER);
            box.setSpacing(15);
            Scene scene = new Scene(box);
            window.setScene(scene);
            window.setTitle("Something wrong");

            box.getChildren().add(message);
            box.getChildren().add(btn);
            box.setPrefWidth(message.getPrefWidth());

            window.showAndWait();
        }

        public void newWindow(Instance instance) {
            Stage window = new Stage();

            window.initModality(Modality.APPLICATION_MODAL);

            VBox vBox = new VBox();
            vBox.setAlignment(Pos.CENTER);
            vBox.setSpacing(10);

            Scene scene = new Scene(vBox);
            window.setScene(scene);
            window.setTitle("Choice new template");

            for (ITemplate template : mO.getTemplates()) {
                Label lblTemplate = new Label(template.getName());
                lblTemplate.setOnMouseClicked(event -> {
                    try {
                        Controller.parseCommand("change_temp " + template.getName() + " " + instance.getId() + " " + customer.getId());
                        //mO.createOrder(customer.getId(), OrderAction.CHANGE_TEMPLATE, template, instance);
                        choiceTemplate = template;
                    } catch (RuntimeException ex) {
                        newWindow(ex);
                    } finally {
                        window.close();
                    }
                });
                vBox.getChildren().add(lblTemplate);
            }
            window.showAndWait();
        }
    }
}
