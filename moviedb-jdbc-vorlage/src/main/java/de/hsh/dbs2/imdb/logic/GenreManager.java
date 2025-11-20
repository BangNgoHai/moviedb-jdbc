package de.hsh.dbs2.imdb.logic;

import java.sql.*;
import java.util.*;
import de.hsh.dbs2.imdb.util.DBConnection;

public class GenreManager {
    public List<String> getGenres() throws Exception {
        List<String> genres = new ArrayList<>();
        Connection conn = DBConnection.getConnection(); // Sử dụng DBConnection của thầy
        
        String sql = "SELECT Genre FROM Genre ORDER BY Genre";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                genres.add(rs.getString("Genre"));
            }
        }
        // KHÔNG đóng connection ở đây!
        return genres;
    }
}