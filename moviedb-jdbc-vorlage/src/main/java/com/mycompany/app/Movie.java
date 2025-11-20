package com.mycompany.app;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import de.hsh.dbs2.imdb.util.DBConnection; // ĐÃ SỬA IMPORT

public class Movie {

    private long movieId;
    private String title;
    private int year;
    private String type;

    //Getter und Setter
    public long getMovieId() {
        return movieId;
    }

    public void setMovieId(long movieId) {
        this.movieId = movieId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    //Insert-Methode
    public void insert() throws SQLException {
        Connection conn = DBConnection.getConnection(); // ĐÃ SỬA

        //Neue ID aus Sequenz holen
        String seqSql = "SELECT nextval('movie_seq')";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(seqSql)) {
            if (rs.next()) {
                this.movieId = rs.getLong(1);
            }
        }

        //Insert durchführen
        String sql = "INSERT INTO Movie (MovieID, Title, Year, Type) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, this.movieId);
            pstmt.setString(2, this.title);
            pstmt.setInt(3, this.year);
            pstmt.setString(4, this.type);
            pstmt.executeUpdate();
        }
    }

    //Update-Methode
    public void update() throws SQLException {
        Connection conn = DBConnection.getConnection(); // ĐÃ SỬA

        String sql = "UPDATE Movie SET Title = ?, Year = ?, Type = ? WHERE MovieID = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, this.title);
            pstmt.setInt(2, this.year);
            pstmt.setString(3, this.type);
            pstmt.setLong(4, this.movieId);
            pstmt.executeUpdate();
        }
    }

    //Delete-Methode
    public void delete() throws SQLException {
        Connection conn = DBConnection.getConnection(); // ĐÃ SỬA

        String sql = "DELETE FROM Movie WHERE MovieID = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, this.movieId);
            pstmt.executeUpdate();
        }
    }
}