package org.ourgroup.provider;

import org.ourgroup.provider.view.console.View;

/**
 * Created by friend_RU.
 */

public class Start {

    public static void main(String[] args) {
        View view = new View();

        while (true) {
            view.logInById();
            if (view.getCustomer() != null) {
                view.customerView();
            } else if (view.getEmployee() != null) {
                view.employeeView();
            } else throw new RuntimeException("ID not found. Try again or enter \"-1\" for exit.");
        }
    }
}
