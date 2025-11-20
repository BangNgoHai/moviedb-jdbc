package de.hsh.dbs2.imdb.logic;

import java.sql.*;
import java.util.*;
import de.hsh.dbs2.imdb.util.DBConnection;

public class PersonManager {
    public List<String> getPersonList(String name) throws Exception {
        List<String> persons = new ArrayList<>();
        Connection conn = DBConnection.getConnection(); // Sử dụng DBConnection của thầy
        
        String sql = "SELECT Name FROM Person WHERE LOWER(Name) LIKE LOWER(?) ORDER BY Name";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + name + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    persons.add(rs.getString("Name"));
                }
            }
        }
        // KHÔNG đóng connection ở đây!
        return persons;
    }

    public int getPerson(String name) throws Exception {
        Connection conn = DBConnection.getConnection(); // Sử dụng DBConnection của thầy
        
        String sql = "SELECT PersonID FROM Person WHERE Name = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("PersonID");
                }
            }
        }
        // KHÔNG đóng connection ở đây!
        throw new Exception("Person not found: " + name);
    }
}