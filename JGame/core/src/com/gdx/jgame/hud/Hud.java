package com.gdx.jgame.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.gdx.jgame.gameObjects.characters.Player;
import com.gdx.jgame.managers.MapManager;

public class Hud implements Disposable{
	
	Stage m_stage;
	Table tableTop, tableBottom;
	private Viewport m_viewport;
	private Player m_player;
	private final boolean m_debugMode;
	private boolean m_layoutLines;
	
	Label health;
	Label fps, deltaTime;
	Label gameMapState;
	Label ammo;
	
	public Hud(SpriteBatch batch, Player player, boolean debugMode, boolean showLayoutLines) {	
		m_layoutLines = showLayoutLines;
		m_debugMode = debugMode;
		m_player = player;
		tableTop = new Table();
		tableBottom = new Table();
		
		m_viewport = new FitViewport(MapManager.V_WIDTH, MapManager.V_HEIGHT,  new OrthographicCamera());		
		m_stage = new Stage(m_viewport, batch);
		
		tableTop.top();
		tableBottom.bottom();
		tableTop.setFillParent(true);
		tableBottom.setFillParent(true);
		
		health = new Label(null, new Label.LabelStyle(new BitmapFont(), Color.WHITE));
		ammo = new Label(null, new Label.LabelStyle(new BitmapFont(), Color.WHITE));
		if(m_debugMode) {
			fps = new Label(null, new Label.LabelStyle(new BitmapFont(), Color.WHITE));
			deltaTime = new Label(null, new Label.LabelStyle(new BitmapFont(), Color.WHITE));
		}
		
		
		tableTop.add().expandX();
		tableTop.add(health).expandX().padTop(10);
		tableBottom.add().expandX();
		tableBottom.add(ammo).padRight(70).padBottom(10);
		if(m_debugMode) {
			tableTop.add(fps).expandX().padTop(10);
			tableTop.add(deltaTime).expandX().padTop(10);
		}
		
		m_stage.addActor(tableTop);
		m_stage.addActor(tableBottom);
	}
	
	public void update(SpriteBatch batch) {
		m_viewport.apply();
		
		drawLabels();
		if(m_debugMode && m_layoutLines) {
			m_stage.setDebugAll(true);
			drawDebugLabels();
		}
		
		batch.setProjectionMatrix(m_stage.getCamera().combined);
		m_stage.draw();
	}

	void drawLabels() {
		float currentHealth = m_player.getHealthFloat()/m_player.getMaxHealthFloat() * 100;
		health.setText("Health: " + String.format("%.0f", currentHealth) + "%");
		ammo.setText("Ammo: " + m_player.getCurrentAmmoBouncingBullet());
	}
	
	private float raw = 0f;
	void drawDebugLabels() {
		fps.setText("FPS: " + Gdx.graphics.getFramesPerSecond());
		if(raw < Gdx.graphics.getRawDeltaTime()) raw = Gdx.graphics.getRawDeltaTime();
		deltaTime.setText("max raw delta time: " + raw);
	}
	
	public void resize(int width, int height) {
		m_viewport.update(width, height);
	}

	@Override
	public void dispose() {
		m_stage.dispose();
	}
	
	public Stage getStage() {
		return m_stage;
	}	
	
	Viewport getM_viewport() {
		return m_viewport;
	}

	public boolean isLayoutLines() {
		return m_layoutLines;
	}

	public void setLayoutLines(boolean m_layoutLines) {
		this.m_layoutLines = m_layoutLines;
	}
	
	
	
}
