package de.hsh.dbs2.imdb.logic;

import java.sql.*;
import java.util.*;
import com.mycompany.app.DbConnection;

public class PersonManager {
    public List<String> getPersonList(String name) throws Exception {
        List<String> persons = new ArrayList<>();
        Connection conn = DbConnection.getConnection();
        String sql = "SELECT Name FROM Person WHERE LOWER(Name) LIKE LOWER(?) ORDER BY Name";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + name + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    persons.add(rs.getString("Name"));
                }
            }
        }
        return persons;
    }

    public int getPerson(String name) throws Exception {
        Connection conn = DbConnection.getConnection();
        String sql = "SELECT PersonID FROM Person WHERE Name = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("PersonID");
                }
            }
        }
        throw new Exception("Person not found: " + name);
    }
}