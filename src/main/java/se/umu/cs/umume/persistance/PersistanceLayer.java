package se.umu.cs.umume.persistance;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.umu.cs.umume.Person;

public class PersistanceLayer {

    private static Logger logger = LoggerFactory.getLogger(PersistanceLayer.class);
    private static String databasePath = "jdbc:sqlite:/" + PersistanceLayer.class.getResource("/thebrain.rsd").getFile();

    //    public static void testURI() {
    //        logger.info("DB: " + databasePath);
    //        logger.info("DB: " + PersistanceLayer.class.getResource("/thebrain.rsd"));
    //        logger.info("DB: " + PersistanceLayer.class.getResource("/thebrain.rsd").getPath());
    //        logger.info("DB: " + PersistanceLayer.class.getResource("/thebrain.rsd").getFile());
    //    }

    public static void addDatabaseInfo(List<Person> persons) {
        try {
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection(databasePath);
            Statement stat = conn.createStatement();
            
            for (Person person : persons) {
                ResultSet rs = stat
                .executeQuery("select * from Persons where UserName = \""
                        + person.getUid() + "\"");
                while (rs.next()) {
                    person.setDescription(rs.getString("Description"));
                    person.setTwitterName(rs.getString("TwitterName"));
                    person.setLongitude(rs.getDouble("Longitude"));
                    person.setLatitude(rs.getDouble("Latitude"));
                    logger.info("From db (desc) {}: ", person.getDescription());
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

    public static void updateInfo(Person person) {
        try {
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection(databasePath);
            Statement stat = conn.createStatement();
            ResultSet rs = stat
            .executeQuery("select * from Persons where UserName = '"
                    + person.getUid() + "'");
            if (rs.next()) {
                /*
                String sql = "UPDATE Persons SET "
                    + "TwitterName = '"
                    + person.getTwitterName() + "', "
                    + "Longitude = "
                    + person.getLongitude() + ", "
                    + "Latitude = "
                    + person.getLatitude() + ", "
                    + "Description = '"
                    + person.getDescription() + "'"
                    + " WHERE UserName = '" + person.getUid() + "'";
                logger.info("Running query: {}", sql);
                conn.createStatement().executeUpdate(sql);
                */
                
                String sql = "UPDATE Persons SET "
                    + "TwitterName = ?, "
                    + "Longitude = ?, "
                    + "Latitude = ?,"
                    + "Description = ?"
                    + " WHERE UserName = '" + person.getUid() + "'";
                
                PreparedStatement prepStmt = conn.prepareStatement(sql);
                prepStmt.setString(1, person.getTwitterName());
                prepStmt.setDouble(2, person.getLongitude());
                prepStmt.setDouble(3, person.getLatitude());
                prepStmt.setString(4, person.getDescription());

                logger.info("Running query: {}", sql);
                
                prepStmt.executeUpdate();
                
            } else {
                /*
                String sql = "INSERT INTO Persons " 
                    + "(UserName, TwitterName, Longitude, Latitude, Description) VALUES ("
                    + "'"+ person.getUid() + "',"  
                    + "'"+ person.getTwitterName() + "',"
                    + "'"+ person.getLongitude() + "'," 
                    + "'"+ person.getLatitude() + "'," 
                    + "'" + person.getDescription() + "')";
                */
                String sql = "INSERT INTO Persons " 
                    + "(UserName, TwitterName, Longitude, Latitude, Description) VALUES "
                    + "(?,?,?,?,?)";
                logger.info("Running query: {}", sql);
                
                PreparedStatement prepStmt = conn.prepareStatement(sql);
                prepStmt.setString(1, person.getUid());
                prepStmt.setString(2, person.getTwitterName());
                prepStmt.setDouble(3, person.getLongitude());
                prepStmt.setDouble(4, person.getLatitude());
                prepStmt.setString(5, person.getDescription());

                
                prepStmt.executeUpdate();
                //stat.executeUpdate(sql);
            }
            rs.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        // TODO: Close connection and resultset
    }
}
