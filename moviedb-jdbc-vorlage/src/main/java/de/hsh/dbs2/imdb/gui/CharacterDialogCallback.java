package de.hsh.dbs2.imdb.gui;

import java.awt.Window;
import java.util.List;

import javax.swing.JOptionPane;

import de.hsh.dbs2.imdb.logic.PersonManager;
import de.hsh.dbs2.imdb.persistence.DoesNotExistException;

public class CharacterDialogCallback {

	private final PersonManager pm = new PersonManager();
	private Window owner;
	
	public void setOwner(Window owner) {
		this.owner = owner;
	}
	
	public List<String> getPersonList(String text) {
		try {
            return pm.getPersonList(text);
		} catch (Exception e) {
			new ShowErrorDialog(owner, "Fehler beim der Personensuche", e);
			return null;
		}
	}
	
	public int getPerson(String text) {
		try {
            return pm.getPerson(text);
		} catch (DoesNotExistException e) {
			JOptionPane.showMessageDialog(owner, "Eine Person mit dem Namen " + text + " wurde nicht gefunden"); 
			return -1;
		} catch (Exception e) {
			new ShowErrorDialog(owner, "Fehler bei der Personensuche", e);
			return -1;
		}
	}
}
