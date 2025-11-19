package de.hsh.dbs2.imdb.gui;

import java.awt.Window;
import java.util.ArrayList;
import java.util.List;

import de.hsh.dbs2.imdb.logic.GenreManager;
import de.hsh.dbs2.imdb.logic.MovieManager;
import de.hsh.dbs2.imdb.logic.dto.MovieDTO;

public class SearchMovieDialogCallback {
	
	private final MovieManager mm = new MovieManager();
	private final GenreManager gm = new GenreManager();
	private Window owner;

	public void setOwner(Window owner) {
		this.owner = owner;
	}
	
	public List<MovieDTO> runSearch(String search) {
		List<MovieDTO> movies;
		try {
			movies = mm.getMovieList(search);
		} catch (Exception e) {
			new ShowErrorDialog(owner, "Fehler beim der Suche", e);
			movies = new ArrayList<>();
		}
		return movies;
	}

	public void addMovie() {
		movieDialog(null);
	}

	public void editMovie(int movieId) {
		movieDialog(movieId);
	}

	public void deleteMovie(int movieId) {
		try {
			mm.deleteMovie(movieId);
		} catch (Exception e) {
			new ShowErrorDialog(owner, "Fehler beim Löschen des Films", e);
		}
	}

	private void movieDialog(Integer movieId) {

		MovieDTO movie;
		if (movieId == null)
			movie = new MovieDTO();
		else {
			try {
				movie = mm.getMovie(movieId);
			} catch (Exception e) {
				new ShowErrorDialog(owner, "Fehler beim Laden des Films", e);
				return;
			}
		}
		
		List<String> allGenres;
		try {
			allGenres = gm.getGenres();
		} catch (Exception e) {
			new ShowErrorDialog(owner, "Fehler beim Laden der Genres", e);
			return;
		}

		MovieDialog movieDialog = new MovieDialog(owner, new MovieDialogCallback(), allGenres);
		movieDialog.setMovie(movie);
		
		boolean finished = false;
		while (!finished) {
			movieDialog.setVisible(true);
			finished = true;

			if (movieDialog.getOutcome().equals("ok")) {
				try {
					mm.insertUpdateMovie(movie);
				} catch (Exception e) {
					new ShowErrorDialog(owner, "Fehler beim Speichern des Films", e);
					finished = false;  // Movie Dialog offen halten
				}
			}
		}
	}
}
