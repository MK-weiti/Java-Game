package com.gdx.jgame;

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
import com.gdx.jgame.world.MapManager;

public class Hud implements Disposable{
	
	public Stage stage;
	private Viewport m_viewport;
	
	Label temporary;
	
	public Hud(SpriteBatch batch) {
		Table table = new Table();
		
		m_viewport = new FitViewport(MapManager.V_WIDTH, MapManager.V_HEIGHT,  new OrthographicCamera());
		stage = new Stage(m_viewport, batch);
		
		table.bottom();
		table.setFillParent(true);
		
		temporary = new Label("Temporary caption", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
		table.add().expandX();
		table.add(temporary).expandX().padTop(10);
		
		stage.addActor(table);
	}
	
	public void update(SpriteBatch batch) {
		// TODO
	}
	
	public void resize(int width, int height) {
		m_viewport.update(width, height);
	}

	@Override
	public void dispose() {
		stage.dispose();
	}
}
