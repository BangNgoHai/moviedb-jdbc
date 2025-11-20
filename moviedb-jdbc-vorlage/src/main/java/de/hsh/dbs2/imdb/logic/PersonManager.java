package de.hsh.dbs2.imdb.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import de.hsh.dbs2.imdb.util.DBConnection;

public class PersonManager {
	/**
     * Liefert eine Liste aller Personen, deren Name den Suchstring enthaelt.
     *
     * @param name Suchstring
     * @return Liste mit passenden Personennamen, die in der Datenbank
     * eingetragen sind.
     * @throws Exception Beschreibt evtl. aufgetretenen Fehler
     */
    public List<String> getPersonList(String name) throws Exception {
        List<String> personNames = new ArrayList<>();
        Connection conn = DBConnection.getConnection(); 
        
        String sql = "SELECT Name FROM Person WHERE Name ILIKE ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + name + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    personNames.add(rs.getString("Name"));
                }
            }
        }catch (SQLException e){
			throw new Exception("Error fetching persons", e);
		};
        return personNames;
    }

	/**
     * Liefert die ID einer Person, deren Name genau name ist. Wenn die Person
     * nicht existiert, wird eine Exception geworfen.
     *
     * @param name Exakter Name der Person
     * @return ID der Person
     * @throws Exception Beschreibt evtl. aufgetretenen Fehler
     */

    public int getPerson(String name) throws Exception {
        Connection conn = DBConnection.getConnection(); 
        
        String sql = "SELECT PersonID FROM Person WHERE Name = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("PersonID");
                }
            }
        }catch (SQLException e){
			throw new Exception("Error fetching person ID", e);
		};

        throw new Exception("Person not found: " + name);
    }
}