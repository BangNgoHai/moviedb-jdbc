package de.hsh.dbs2.imdb.logic;

import java.sql.*;
import java.util.*;
import com.mycompany.app.DbConnection;
import com.mycompany.app.Movie;
import com.mycompany.app.MovieFactory;
import de.hsh.dbs2.imdb.logic.dto.MovieDTO;
import de.hsh.dbs2.imdb.logic.dto.CharacterDTO;

public class MovieManager {

    public List<MovieDTO> getMovieList(String search) throws Exception {
        List<MovieDTO> movies = new ArrayList<>();
        for (Movie movie : MovieFactory.findByTitle(search)) {
            MovieDTO dto = new MovieDTO();
            dto.setId((int) movie.getMovieId());  // Chuyển long sang int, chú ý có thể mất dữ liệu nếu ID quá lớn
            dto.setTitle(movie.getTitle());
            dto.setYear(movie.getYear());
            dto.setType(movie.getType());
            dto.setGenres(new HashSet<>(getMovieGenres(movie.getMovieId()))); // Chuyển List<String> thành Set<String>
            dto.getCharacters().addAll(getMovieCharacters(movie.getMovieId())); // Thêm tất cả characters
            movies.add(dto);
        }
        return movies;
    }

    public void insertUpdateMovie(MovieDTO movieDTO) throws Exception {
        Connection conn = DbConnection.getConnection();
        try {
            conn.setAutoCommit(false);
            
            // Save movie
            Movie movie;
            if (movieDTO.getId() == null) {
                movie = new Movie();
                movie.setTitle(movieDTO.getTitle());
                movie.setYear(movieDTO.getYear());
                movie.setType(movieDTO.getType());
                movie.insert();
                movieDTO.setId((int) movie.getMovieId()); // Chuyển long sang int
            } else {
                movie = MovieFactory.findById(movieDTO.getId());
                movie.setTitle(movieDTO.getTitle());
                movie.setYear(movieDTO.getYear());
                movie.setType(movieDTO.getType());
                movie.update();
            }
            
            // Save relations
            saveMovieRelations(movieDTO, conn);
            conn.commit();
            
        } catch (Exception e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }

    public void deleteMovie(int movieId) throws Exception {
        Connection conn = DbConnection.getConnection();
        try {
            conn.setAutoCommit(false);
            
            // Delete relations first
            executeUpdate("DELETE FROM MovieCharacter WHERE MovieID = " + movieId, conn);
            executeUpdate("DELETE FROM MovieGenre WHERE MovieID = " + movieId, conn);
            
            // Delete movie
            Movie movie = MovieFactory.findById(movieId);
            if (movie != null) movie.delete();
            
            conn.commit();
        } catch (Exception e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }

    public MovieDTO getMovie(int movieId) throws Exception {
        Movie movie = MovieFactory.findById(movieId);
        if (movie == null) throw new Exception("Movie not found: " + movieId);
        
        MovieDTO dto = new MovieDTO();
        dto.setId((int) movie.getMovieId());
        dto.setTitle(movie.getTitle());
        dto.setYear(movie.getYear());
        dto.setType(movie.getType());
        dto.setGenres(new HashSet<>(getMovieGenres(movieId)));
        dto.getCharacters().addAll(getMovieCharacters(movieId));
        return dto;
    }

    // Helper methods
    private List<String> getMovieGenres(long movieId) throws Exception {
        List<String> genres = new ArrayList<>();
        String sql = "SELECT g.Genre FROM Genre g JOIN MovieGenre mg ON g.GenreID = mg.GenreID WHERE mg.MovieID = ?";
        
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, movieId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) genres.add(rs.getString("Genre"));
            }
        }
        return genres;
    }

    private List<CharacterDTO> getMovieCharacters(long movieId) throws Exception {
        List<CharacterDTO> characters = new ArrayList<>();
        String sql = "SELECT mc.Character, mc.Alias, p.Name as Player " +
                    "FROM MovieCharacter mc JOIN Person p ON mc.PlayerID = p.PersonID " +
                    "WHERE mc.MovieID = ? ORDER BY mc.Position";
        
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, movieId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    CharacterDTO dto = new CharacterDTO();
                    dto.setCharacter(rs.getString("Character"));
                    dto.setAlias(rs.getString("Alias"));
                    dto.setPlayer(rs.getString("Player"));
                    characters.add(dto);
                }
            }
        }
        return characters;
    }

    private void saveMovieRelations(MovieDTO movieDTO, Connection conn) throws Exception {
        // Delete old relations
        executeUpdate("DELETE FROM MovieCharacter WHERE MovieID = " + movieDTO.getId(), conn);
        executeUpdate("DELETE FROM MovieGenre WHERE MovieID = " + movieDTO.getId(), conn);
        
        // Save genres
        String genreSql = "INSERT INTO MovieGenre (MovieID, GenreID) SELECT ?, GenreID FROM Genre WHERE Genre = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(genreSql)) {
            for (String genre : movieDTO.getGenres()) {
                pstmt.setLong(1, movieDTO.getId());
                pstmt.setString(2, genre);
                pstmt.executeUpdate();
            }
        }
        
        // Save characters
        String charSql = "INSERT INTO MovieCharacter (MovieID, PlayerID, Character, Alias, Position) " +
                        "SELECT ?, PersonID, ?, ?, ? FROM Person WHERE Name = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(charSql)) {
            int position = 1;
            for (CharacterDTO charDTO : movieDTO.getCharacters()) {
                pstmt.setLong(1, movieDTO.getId());
                pstmt.setString(2, charDTO.getCharacter());
                pstmt.setString(3, charDTO.getAlias());
                pstmt.setInt(4, position++);
                pstmt.setString(5, charDTO.getPlayer());
                pstmt.executeUpdate();
            }
        }
    }

    private void executeUpdate(String sql, Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
        }
    }
}