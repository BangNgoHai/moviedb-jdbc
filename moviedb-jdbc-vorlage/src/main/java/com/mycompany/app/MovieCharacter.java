package com.mycompany.app;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import de.hsh.dbs2.imdb.util.DBConnection; 

public class MovieCharacter {

    private long movCharID;
    private long movieId;
    private long playerId;
    private String character;
    private String alias;
    private Integer position;

    //Getter und Setter

    public long getMovCharID() {
        return movCharID;
    }

    public void setMovCharID(long movCharID) {
        this.movCharID = movCharID;
    }

    public long getMovieId() {
        return movieId;
    }

    public void setMovieId(long movieId) {
        this.movieId = movieId;
    }

    public long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    //Insert-Methode
    public void insert() throws SQLException {
        Connection conn = DBConnection.getConnection(); 

        //Keine Sequenz nötig - Primärschlüssel ist zusammengesetzt
        String sql = "INSERT INTO MovieCharacter (moveCharID, MovieID,  PlayerID, Character, Alias, Position) "
                + "VALUES (?, ?, ?, ?, ?, ? )";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, this.movCharID);
            pstmt.setLong(2, this.movieId);
            pstmt.setLong(3, this.playerId);
            pstmt.setString(4, this.character);


            if (this.alias != null) {
                pstmt.setString(5, this.alias);
            } else {
                pstmt.setNull(5, Types.VARCHAR);
            }

            if (this.position != null) {
                pstmt.setInt(6, this.position);
            } else {
                pstmt.setNull(6, Types.INTEGER);
            }

            pstmt.executeUpdate();
        }
    }
}