package me.extain.game.gameObject.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class Account {

    public int id;
    public String username;
    public Player player;


    private ArrayList<Character> characters;

    private Character selectedChar;

    public Account(int id, String username) {
        this.id = id;
        this.username = username;
        characters = new ArrayList<>();
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public Player getPlayer() {
        return player;
    }

    public String getUsername() {
        return username;
    }

    public ArrayList<Character> getCharacters() {
        return characters;
    }

    public void setCharacters(ArrayList<Character> characters) {
        this.characters = characters;
    }

    public Character getSelectedChar() {
        return selectedChar;
    }

    public void setSelectedChar(Character character) {
        this.selectedChar = character;
    }

    public void addCharacter(Character character) {
        this.characters.add(character);
    }

    public void removeCharacter(Character character) {
        this.characters.remove(character);
    }
}
