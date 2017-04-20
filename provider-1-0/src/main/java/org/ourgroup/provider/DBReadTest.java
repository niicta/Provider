/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ourgroup.provider;

import org.ourgroup.provider.dao.DataAccessObject;
import org.ourgroup.provider.model.ModelOperations;

import javax.xml.bind.JAXBException;
import java.sql.SQLException;

/**
 *
 * @author Краснопольский
 */
public class DBReadTest {
    public static void main(String[] args) throws SQLException, JAXBException {
        ModelOperations mo = ModelOperations.getModelOperations();
        DataAccessObject dao = DataAccessObject.getDAO();
        dao.readModelFromDB();
        mo.marshallModel();
    }
    
}
