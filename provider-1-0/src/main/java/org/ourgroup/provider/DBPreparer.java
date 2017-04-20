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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Краснопольский
 */
public class DBPreparer {
    public static void main(String[] args) {
        try {
            ModelOperations mo = ModelOperations.getModelOperations();
            mo.demarshallXmlModel();
            DataAccessObject dao = DataAccessObject.getDAO();
            dao.insertModelToDB();
        } catch (JAXBException ex) {
            Logger.getLogger(DBPreparer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(DBPreparer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
