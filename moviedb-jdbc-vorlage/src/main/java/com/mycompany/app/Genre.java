package com.mycompany.app;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import de.hsh.dbs2.imdb.util.DBConnection; // 

public class Genre {

    private long genreId;
    private String genre;

    //Getter und Setter
    public long getGenreId() {
        return genreId;
    }

    public void setGenreId(long genreId) {
        this.genreId = genreId;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    //Insert-Methode
    public void insert() throws SQLException {
        Connection conn = DBConnection.getConnection(); 

        //Neue ID aus Sequenz holen
        String seqSql = "SELECT nextval('genre_seq')";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(seqSql)) {
            if (rs.next()) {
                this.genreId = rs.getLong(1);
            }
        }

        //Insert durchführen
        String sql = "INSERT INTO Genre (GenreID, Genre) VALUES (?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, this.genreId);
            pstmt.setString(2, this.genre);
            pstmt.executeUpdate();
        }
    }
}