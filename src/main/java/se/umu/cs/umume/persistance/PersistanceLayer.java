package se.umu.cs.umume.persistance;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.logging.Logger;

import se.umu.cs.umume.PersonBean;

public class PersistanceLayer {

    private static Logger logger = Logger.getLogger("persistanceLayerLogger");
    private static String databasePath = "jdbc:sqlite://Users/Jonny/Universitet/SOA/apache-tomcat-6.0.20/webapps/UmuMeREST/WEB-INF/classes/thebrain.rsd";

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
