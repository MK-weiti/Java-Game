package com.gdx.jgame.ui;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.gdx.jgame.JGame;
import com.gdx.jgame.managers.SavesManager;
import com.gdx.jgame.managers.ScreenManager;
import com.gdx.jgame.managers.ScreenManager.SCREEN_STATE;

public class PauseMenu extends Menu {
	private TextButton m_buttonNewGame, m_buttonExit, m_buttonResume, m_buttonSave, m_buttonLoad;
	
	private Table m_table;
	private boolean m_firstRun = true;
	private String m_saveName = "first.bin";
	private JGame m_game = null;
	private boolean m_layoutLines;
	
	private int m_tableWidth = 100;
	private int m_tableHeight = 640;
	
	public PauseMenu(SavesManager savesManager, ScreenManager screenManager, boolean showLayoutLines) {
		super(savesManager, screenManager, "buttons/glassy/skin/glassy-ui.json");
		m_layoutLines = showLayoutLines;
		
		m_table = new Table(m_skin);
		m_table.setBounds(Gdx.graphics.getWidth()/2 - m_tableWidth/2, 
				Gdx.graphics.getHeight()/2 - m_tableHeight/2, 
				m_tableWidth, m_tableHeight);
		
		newButtonNewGame();
		newButtonExit();
		newButtonResume();
		newButtonSave();
		newButtonLoad();
		
		m_table.add(m_buttonNewGame).width(100).height(m_tableHeight/5);//.padTop(20f).padBottom(20f);
		m_table.row();
		m_table.add(m_buttonResume).width(100).height(m_tableHeight/5);//.padTop(20f).padBottom(20f);
		m_table.row();
		m_table.add(m_buttonSave).width(100).height(m_tableHeight/5);//.padTop(20f).padBottom(20f);
		m_table.row();
		m_table.add(m_buttonLoad).width(100).height(m_tableHeight/5);//.padTop(20f).padBottom(20f);
		m_table.row();
		m_table.add(m_buttonExit).width(100).height(m_tableHeight/5);//.padTop(20f).padBottom(20f);
		
		m_stage.addActor(m_table);
	}
	
	public void connectWithGame(JGame game) {
		m_game = game;
	}

	@Override
	public void show() {
		super.show();
		m_table.setVisible(true);
		
		if(m_screenManager.isDebugMode() && m_layoutLines) {
			m_stage.setDebugAll(true);
		}
	}
	
	private void newButtonNewGame() {
		m_buttonNewGame = new TextButton("New Game", m_skin, "small");
		m_buttonNewGame.setTransform(true);
		m_buttonNewGame.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				if(m_screenManager.isDebugMode()) {
					Gdx.app.log("NewGame", "touch started at (" + x + ", " + y + ")");
				}
				return true;
		 	}
		 
		 	public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
		 		if(m_screenManager.isDebugMode()) {
		 			Gdx.app.log("NewGame", "touch done at (" + x + ", " + y + ")");
		 		}
	 			m_firstRun = false;
	 			m_screenManager.createNewGame();
		 	}
		} );
	}
	
	private void newButtonLoad() {
		m_buttonLoad = new TextButton("Load", m_skin, "small");
		m_buttonLoad.setTransform(true);
		m_buttonLoad.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				if(m_screenManager.isDebugMode()) {
					Gdx.app.log("Load game", "touch started at (" + x + ", " + y + ")");
				}
		 		
				return true;
		 	}
		 
		 	public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
		 		if(m_screenManager.isDebugMode()) {
		 			Gdx.app.log("Load game", "touch done at (" + x + ", " + y + ")");
		 		}
		 		
	 			try {
					m_recordManager.prepareData(m_saveName);
				} catch (FileNotFoundException e) {
					return;
					
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	 			m_firstRun = false;
	 			m_screenManager.loadGame();
		 	}
		});
	}

	private void newButtonSave() {
		m_buttonSave = new TextButton("Save", m_skin, "small");
		m_buttonSave.setTransform(true);
		m_buttonSave.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				if(m_screenManager.isDebugMode()) {
					Gdx.app.log("Save game", "touch started at (" + x + ", " + y + ")");
				}
				return true;
		 	}
		 
		 	public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
		 		if(m_screenManager.isDebugMode()) {
		 			Gdx.app.log("Save game", "touch done at (" + x + ", " + y + ")");
		 		}
		 		if(m_game != null) {
		 			m_recordManager.save(m_game, m_saveName);
		 		}
		 	}
		} );
	}

	private void newButtonResume() {
		m_buttonResume = new TextButton("Resume", m_skin, "small");
		m_buttonResume.setTransform(true);
		m_buttonResume.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				if(m_screenManager.isDebugMode()) {
					Gdx.app.log("Resume game", "touch started at (" + x + ", " + y + ")");
				}
				return true;
		 	}
		 
		 	public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
		 		if(m_screenManager.isDebugMode()) {
		 			Gdx.app.log("Resume game", "touch done at (" + x + ", " + y + ")");
		 		}
		 		if(!m_firstRun) {
		 			setScreen(SCREEN_STATE.PLAY);	
		 		}
		 	}
		} );
	}

	private void newButtonExit() {
		m_buttonExit = new TextButton("Exit", m_skin, "small");
		m_buttonExit.setTransform(true);
		m_buttonExit.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				if(m_screenManager.isDebugMode()) {
					Gdx.app.log("Exit", "touch started at (" + x + ", " + y + ")");
				}
				return true;
		 	}
		 
		 	public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
		 		if(m_screenManager.isDebugMode()) {
		 			Gdx.app.log("Exit", "touch done at (" + x + ", " + y + ")");
		 		}
		 		Gdx.app.exit();
		 	}
		} );
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		m_table.setVisible(false);		
	}

	public boolean isLayoutLines() {
		return m_layoutLines;
	}

	public void setLayoutLines(boolean m_layoutLines) {
		this.m_layoutLines = m_layoutLines;
	}
	
	
}
