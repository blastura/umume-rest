package se.umu.cs.umume.persistance;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.umu.cs.umume.PersonBean;

public class PersistanceLayer {

    private static Logger logger = LoggerFactory.getLogger(PersistanceLayer.class);
    private static String databasePath = "jdbc:sqlite:/" + PersistanceLayer.class.getResource("/thebrain.rsd").getFile();
    
//    public static void testURI() {
//        logger.info("DB: " + databasePath);
//        logger.info("DB: " + PersistanceLayer.class.getResource("/thebrain.rsd"));
//        logger.info("DB: " + PersistanceLayer.class.getResource("/thebrain.rsd").getPath());
//        logger.info("DB: " + PersistanceLayer.class.getResource("/thebrain.rsd").getFile());
//    }
    
    public static void addDatabaseInfo(List<PersonBean> persons) {
        try {
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection(databasePath);
            Statement stat = conn.createStatement();
            for (PersonBean person : persons) {
                ResultSet rs = stat
                .executeQuery("select * from Persons where UserName = \""
                        + person.getUid() + "\"");
                while (rs.next()) {
                    person.setDescription(rs.getString("Description"));
                    person.setTwitterName(rs.getString("TwitterName"));
                }
                rs.close();
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void updateInfo(PersonBean person) {
        try {
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection(databasePath);
            Statement stat = conn.createStatement();
            ResultSet rs = stat
                    .executeQuery("select * from Persons where UserName = '"
                            + person.getUid() + "'");
            if (rs.next()) {
                ResultSet rs2 = stat
                        .executeQuery("UPDATE Persons SET TwitterName = '"
                                + person.getTwitterName()
                                + "' AND Description = '"
                                + person.getDescription()
                                + "' WHERE UserName = '" + person.getUid() + "'");
                rs2.close();
            } else {
                ResultSet rs2 = stat
                .executeQuery("INSERT INTO Persons " 
                        + "(TwitterName, Description) VALUES ("
                        + "'"+ person.getTwitterName() + "'"  
                        + "'" + person.getDescription() + "')"
                        + " WHERE UserName = '" + person.getUid() + "'");
                rs2.close();
            }
            rs.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
