package com.gdx.jgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.gdx.jgame.ScreenManager.SCREEN_STATE;

public abstract class Menu implements Screen{
	Stage m_stage;
	Skin m_skin;
	Viewport m_viewport;
	public ScreenManager m_screenManager;
	RecordManager m_recordManager;
	
	public Menu(RecordManager recordManager, ScreenManager screenManager, String pathToSkin) {
		m_recordManager = recordManager;
		m_screenManager = screenManager;
		m_viewport = new FitViewport(1920f, 1080f);
		m_stage = new Stage(m_viewport);
		m_stage = new Stage();
		m_skin = new Skin(Gdx.files.internal(pathToSkin));		
	}
	
	public void setScreen(ScreenManager.SCREEN_STATE screenName) {
		m_screenManager.setScreen(screenName);
	}
	
	@Override
	public void resize(int width, int height) {
		m_viewport.update(width, height);
	}

	@Override
	public void dispose() {
		m_skin.dispose();
		m_stage.dispose();
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(m_stage);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		m_viewport.apply();
		m_stage.act();
		m_stage.draw();
	}
}
