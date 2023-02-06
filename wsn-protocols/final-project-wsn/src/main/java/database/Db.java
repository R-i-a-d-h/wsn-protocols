package database;

import javafx.scene.control.Alert;

import java.sql.*;

;

/**
 * @author Riadh
 */
public class Db {
    private Connection con;
    private Statement st;
    private ResultSet rs;


    public Db() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/Simulationdb?autoReconnect=true&useSSL=false", "root", "1234");
            st = con.createStatement();


        } catch (Exception ex) {
            System.out.println("Error : " + ex);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("");
            alert.setContentText("Database Connection Time Out");
            alert.setTitle("Connection Error");
            alert.showAndWait();

        }


    }

    public Connection getCon() {
        return con;
    }


    public Statement getSt() {
        return st;
    }


    public ResultSet getRs() {
        return rs;
    }


    public void closeConnection() throws SQLException {
        st.close();
    }

}
