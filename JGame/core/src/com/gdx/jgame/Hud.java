package com.gdx.jgame;

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
import com.gdx.jgame.world.MapManager;

public class Hud implements Disposable{
	
	private Stage m_stage;
	private Viewport m_viewport;
	private Player m_player;
	
	Label temporary;
	
	public Hud(SpriteBatch batch, Player player) {
		m_player = player;
		Table table = new Table();
		
		m_viewport = new FitViewport(MapManager.V_WIDTH, MapManager.V_HEIGHT,  new OrthographicCamera());
		m_stage = new Stage(m_viewport, batch);
		
		table.bottom();
		table.setFillParent(true);
		
		temporary = new Label(Integer.toString((m_player.getHealth()/m_player.getMaxHealth()) * 100) + "%", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
		table.add().expandX();
		table.add(temporary).expandX().padTop(10);
		
		m_stage.addActor(table);
	}
	
	public void update(SpriteBatch batch) {
		// TODO
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
	
	
}
