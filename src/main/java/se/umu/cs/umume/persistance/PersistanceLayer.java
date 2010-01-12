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
    
    public static void addDatabaseInfo(List<PersonBean> persons) {
        try {
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager
                    .getConnection("jdbc:sqlite://Users/Jonny/Universitet/SOA/apache-tomcat-6.0.20/webapps/UmuMeREST/WEB-INF/classes/thebrain.rsd");
            Statement stat = conn.createStatement();
            for (PersonBean person : persons) {
                ResultSet rs = stat.executeQuery("select * from Persons");
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
}
