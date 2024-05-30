package org.Modules;

import java.sql.*;

public class MySqlConn {
    Statement stmt=null;
    Connection conn=null;
    public ResultSet rs=null;
    public MySqlConn() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String connectionURL = "jdbc:mysql://127.0.0.1/NeonCity?"
                    + "characterEncoding=latin1&" + "user=root&password=ThoryMia898";
            conn = DriverManager.getConnection(connectionURL);
        }catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        }catch (ClassNotFoundException e) {
            System.out.println("ClassNotFoundException: " + e.getMessage());
        }
    }


    public void consult(String query){
        try {
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            if (stmt.execute(query)) {
                rs = stmt.getResultSet();
                rs.first();
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Error Code: " + e.getErrorCode());
        }

    }
    public void closeRsStmt(){
        if(rs !=null){
            try{
                rs.close();
            }catch(SQLException sqlEx){

            }
            rs=null;
        }
        if(stmt != null){
            try{
                stmt.close();
            }catch(SQLException sqlEx){

            }
            stmt = null;
        }
    }
    public int uptade (String query){
        int rModified = 0;
        try {
            stmt = conn.createStatement();
            rModified = stmt.executeUpdate(query);
        }catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Error Code: " + e.getErrorCode());
        }
        return rModified;
    }
}
