package org.ourgroup.provider.view.jsp;

import org.ourgroup.provider.dao.DataAccessObject;
import org.ourgroup.provider.model.ModelOperations;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;
import java.io.IOException;

public class ProviderServlet extends HttpServlet {


    DataAccessObject dao = DataAccessObject.getDAO();
    ModelOperations mO = ModelOperations.getModelOperations();


    {
        try {
            mO.demarshallModel();
        } catch (JAXBException e) {

        }
    }


    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);

    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            mO.marshallModel();
        } catch (JAXBException e) {
            //Can be anything
        }


        try {
            mO.getCustomerById(Long.parseLong(request.getParameter("personId")));
            getServletContext().getRequestDispatcher("/customer.jsp").forward(request, response);
        } catch (RuntimeException e) {
            try {
                mO.getEmployeeById(Long.parseLong(request.getParameter("personId")));
                getServletContext().getRequestDispatcher("/employee.jsp").forward(request, response);
            } catch (RuntimeException e1) {
                getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
            }
        }
    }
}
