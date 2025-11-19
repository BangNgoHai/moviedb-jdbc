package com.mycompany.app;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Person {
    private long personId;
    private String name;

    //Getter und Setter
    public long getPersonId() {
        return personId;
    }

    public void setPersonId(long personId) {
        this.personId = personId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    //Insert-Methode
    public void insert() throws SQLException {
        Connection conn = DbConnection.getConnection();

        //Neue ID aus Sequenz holen
        String seqSql = "SELECT nextval('person_seq')";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(seqSql)) {
            if (rs.next()) {
                this.personId = rs.getLong(1);
            }
        }

        //Insert durchführen
        String sql = "INSERT INTO Person (PersonID, Name) VALUES (?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, this.personId);
            pstmt.setString(2, this.name);
            pstmt.executeUpdate();
            
        }
    }  
}
