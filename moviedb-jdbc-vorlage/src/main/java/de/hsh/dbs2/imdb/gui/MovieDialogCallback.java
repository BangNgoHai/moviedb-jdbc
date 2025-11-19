package de.hsh.dbs2.imdb.gui;

import java.awt.Window;

import de.hsh.dbs2.imdb.logic.dto.CharacterDTO;

public class MovieDialogCallback {
	
	private Window owner;
	
	public void setOwner(Window owner) {
		this.owner = owner;
	}
	
	public CharacterDTO addCharacter() {
		CharacterDialogCallback callback = new CharacterDialogCallback();
		CharacterDialog cd = new CharacterDialog(owner, callback);
		cd.setVisible(true);
		if (cd.getOutcome().equals("ok")) {
			CharacterDTO mc = new CharacterDTO();
			mc.setCharacter(cd.getCharacter());
			mc.setAlias(cd.getAlias());
			mc.setPlayer(cd.getPlayer());
			return mc;
		} else
			return null;
	}

	public void editCharacter(CharacterDTO character) {
		CharacterDialogCallback callback = new CharacterDialogCallback();
		CharacterDialog cd = new CharacterDialog(owner, callback);
		cd.setCharacter(character.getCharacter());
		cd.setAlias(character.getAlias());
		cd.setPlayer(character.getPlayer());
		cd.start();
		if (cd.getOutcome().equals("ok")) {
			character.setCharacter(cd.getCharacter());
			character.setAlias(cd.getAlias());
			character.setPlayer(cd.getPlayer());
		}
	}

}
