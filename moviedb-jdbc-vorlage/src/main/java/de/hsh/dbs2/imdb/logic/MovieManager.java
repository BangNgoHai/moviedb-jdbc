package de.hsh.dbs2.imdb.logic;

import java.sql.*;
import java.util.*;
import com.mycompany.app.Movie;
import com.mycompany.app.MovieFactory;
import de.hsh.dbs2.imdb.util.DBConnection;
import de.hsh.dbs2.imdb.logic.dto.MovieDTO;
import de.hsh.dbs2.imdb.logic.dto.CharacterDTO;

public class MovieManager {

    public List<MovieDTO> getMovieList(String search) throws Exception {
        List<MovieDTO> movies = new ArrayList<>();
        
        for (Movie movie : MovieFactory.findByTitle(search)) {
            MovieDTO dto = createMovieDTO(movie);
            movies.add(dto);
        }
        return movies;
    }

    public void insertUpdateMovie(MovieDTO movieDTO) throws Exception {
        Connection conn = DBConnection.getConnection(); // Sử dụng DBConnection của thầy
        try {
            // Transaction được quản lý bởi DBConnection static
            if (movieDTO.getId() == null) {
                Movie movie = new Movie();
                movie.setTitle(movieDTO.getTitle());
                movie.setYear(movieDTO.getYear());
                movie.setType(movieDTO.getType());
                movie.insert();
                movieDTO.setId((int)movie.getMovieId());
            } else {
                Movie movie = MovieFactory.findById(movieDTO.getId());
                movie.setTitle(movieDTO.getTitle());
                movie.setYear(movieDTO.getYear());
                movie.setType(movieDTO.getType());
                movie.update();
            }
            
            saveMovieRelations(movieDTO, conn);
            
        } catch (Exception e) {
            // Rollback sẽ được xử lý bởi DBConnection
            throw e;
        }
        // KHÔNG đóng connection ở đây!
    }

    public void deleteMovie(int movieId) throws Exception {
        Connection conn = DBConnection.getConnection(); // Sử dụng DBConnection của thầy
        
        // Delete relations
        deleteMovieRelations(movieId, conn);
        
        // Delete movie
        Movie movie = MovieFactory.findById(movieId);
        if (movie != null) movie.delete();
        
        // KHÔNG đóng connection ở đây!
    }

    public MovieDTO getMovie(int movieId) throws Exception {
        Movie movie = MovieFactory.findById(movieId);
        if (movie == null) throw new Exception("Movie not found: " + movieId);
        return createMovieDTO(movie);
    }

    // Các helper methods giữ nguyên, nhưng sử dụng DBConnection.getConnection()
    private MovieDTO createMovieDTO(Movie movie) throws Exception {
        MovieDTO dto = new MovieDTO();
        dto.setId((int)movie.getMovieId());
        dto.setTitle(movie.getTitle());
        dto.setYear(movie.getYear());
        dto.setType(movie.getType());
        
        // Load genres và characters
        dto.setGenres(getMovieGenres(movie.getMovieId()));
        for (CharacterDTO character : getMovieCharacters(movie.getMovieId())) {
            dto.addCharacter(character);
        }
        
        return dto;
    }

    private Set<String> getMovieGenres(long movieId) throws Exception {
        Set<String> genres = new HashSet<>();
        Connection conn = DBConnection.getConnection();
        String sql = "SELECT g.Genre FROM Genre g JOIN MovieGenre mg ON g.GenreID = mg.GenreID WHERE mg.MovieID = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, movieId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    genres.add(rs.getString("Genre"));
                }
            }
        }
        return genres;
    }

    private List<CharacterDTO> getMovieCharacters(long movieId) throws Exception {
        List<CharacterDTO> characters = new ArrayList<>();
        Connection conn = DBConnection.getConnection();
        String sql = "SELECT mc.Character, mc.Alias, p.Name as Player " +
                    "FROM MovieCharacter mc JOIN Person p ON mc.PlayerID = p.PersonID " +
                    "WHERE mc.MovieID = ? ORDER BY mc.Position";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
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
        deleteMovieRelations(movieDTO.getId(), conn);
        
        // Save genres
        String genreSql = "INSERT INTO MovieGenre (MovieID, GenreID) SELECT ?, GenreID FROM Genre WHERE Genre = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(genreSql)) {
            for (String genre : movieDTO.getGenres()) {
                pstmt.setInt(1, movieDTO.getId());
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
                pstmt.setInt(1, movieDTO.getId());
                pstmt.setString(2, charDTO.getCharacter());
                pstmt.setString(3, charDTO.getAlias());
                pstmt.setInt(4, position++);
                pstmt.setString(5, charDTO.getPlayer());
                pstmt.executeUpdate();
            }
        }
    }

    private void deleteMovieRelations(int movieId, Connection conn) throws Exception {
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM MovieCharacter WHERE MovieID = " + movieId);
            stmt.executeUpdate("DELETE FROM MovieGenre WHERE MovieID = " + movieId);
        }
    }
}