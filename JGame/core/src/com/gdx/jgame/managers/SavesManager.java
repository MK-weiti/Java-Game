package com.gdx.jgame.managers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.management.RuntimeErrorException;

import com.badlogic.gdx.Gdx;
import com.gdx.jgame.GameState;
import com.gdx.jgame.JGame;

public class SavesManager {
	private final String pathToSaves = "saves/";
	private GameState m_tmpGameState = null;
	
	public SavesManager() {
		setSaveDir();
	}
	
	private void setSaveDir() {
		File file = new File(Gdx.files.getLocalStoragePath() + pathToSaves);
		if(!(file.exists())) {
			if(!file.mkdir())
				try {
					throw new FileNotFoundException("Cannot create directory.");
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}
	
	public void save(JGame game, String name) {
		GameState state;
		state = new GameState(game);
		state.save();
		
		try {
			ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream
					(Gdx.files.getLocalStoragePath() + pathToSaves + name));
			outputStream.writeObject(state);
			outputStream.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void prepareData(String name) throws FileNotFoundException, IOException, ClassNotFoundException {
		ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream
				(Gdx.files.getLocalStoragePath() + pathToSaves + name));
		m_tmpGameState = (GameState) inputStream.readObject();
		inputStream.close();
	}
	
	public void loadData(JGame game) {
		if(m_tmpGameState != null) {
			m_tmpGameState.load(game);
			m_tmpGameState = null;
		}
		else {
			throw new RuntimeErrorException(new Error("Data for load was not prepared"));
		}
	}
	
	public void load(JGame game, String name) {
		try {
			ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream
					(Gdx.files.getLocalStoragePath() + pathToSaves + name));
			GameState state;
			state = (GameState) inputStream.readObject();
			inputStream.close();
			state.load(game);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean isDataLoaded() {
		if(m_tmpGameState == null) return false;
		return true;
	}
	
	public String mapName() {
		if(m_tmpGameState == null) return null;
		return m_tmpGameState.getMapName();
	}
}
