package com.mycompany.app;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import de.hsh.dbs2.imdb.util.DBConnection; 

public class MovieGenre {

    private long movieId;
    private long genreId;

    // Getter und Setter
    public long getMovieId() {
        return movieId;
    }

    public void setMovieId(long movieId) {
        this.movieId = movieId;
    }

    public long getGenreId() {
        return genreId;
    }

    public void setGenreId(long genreId) {
        this.genreId = genreId;
    }

    // Insert-Methode
    public void insert() throws SQLException {
        Connection conn = DBConnection.getConnection(); 

        // Keine Sequenz nötig - Primärschlüssel ist zusammengesetzt aus Fremdschlüsseln
        String sql = "INSERT INTO MovieGenre (MovieID, GenreID) VALUES (?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, this.movieId);
            pstmt.setLong(2, this.genreId);
            pstmt.executeUpdate();
        }
    }
}