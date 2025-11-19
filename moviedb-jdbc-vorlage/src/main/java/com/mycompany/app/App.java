package com.mycompany.app;

import java.sql.SQLException;
import java.io.IOException;

public class App {

    //Test-Methode aus der Aufgabenstellung
    public static void testInsert() throws SQLException {
        boolean ok = false;
        try {
            Person person = new Person();
            person.setName("Karl Tester");
            person.insert();

            Movie movie = new Movie();
            movie.setTitle("Die tolle Komoedie");
            movie.setYear(2012);
            movie.setType("C");
            movie.insert();

            MovieCharacter chr = new MovieCharacter();
            chr.setMovieId(movie.getMovieId());
            chr.setPlayerId(person.getPersonId());
            chr.setCharacter("Hauptrolle");
            chr.setAlias(null);
            chr.setPosition(1);
            chr.insert();

            Genre genre = new Genre();
            genre.setGenre("Unklar");
            genre.insert();

            MovieGenre movieGenre = new MovieGenre();
            movieGenre.setGenreId(genre.getGenreId());
            movieGenre.setMovieId(movie.getMovieId());
            movieGenre.insert();

            DbConnection.getConnection().commit();
            System.out.println("Transaction committed successfully!");
        } catch (Exception e) {
            System.err.println("Error in transaction: " + e.getMessage());
            if (DbConnection.getConnection() != null) {
                DbConnection.getConnection().rollback();
            }
            throw e;
        }
    }

    public static void main(String[] args) {
        try {
            // Öffne die Datenbankverbindung ZUERST
            DbConnection.open();
            
            System.out.println("================== Test Insert beginnt ==================");
            testInsert();
            System.out.println("\nALLE TESTS ERFOLGREICH ABGESCHLOSSEN!\n");
            System.out.println("================== Test Insert endet ==================");

        } catch (SQLException e) {
            System.err.println("SQL Fehler: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("IO Fehler: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Schließe die Verbindung immer
            try {
                if (DbConnection.getConnection() != null && !DbConnection.getConnection().isClosed()) {
                    DbConnection.getConnection().close();
                    System.out.println("Database connection closed.");
                }
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }
}