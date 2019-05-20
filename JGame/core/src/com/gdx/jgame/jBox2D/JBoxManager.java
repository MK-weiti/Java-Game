package com.gdx.jgame.jBox2D;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Disposable;

// IMPORTANT
// all libraries in jBox2d are loaded when World object is created

public class JBoxManager implements Disposable{
	public float timeStep = 1/60f;
	public int velocityIterations = 6;
	public int positionIterations = 2;
	private static final Vector2 gravitationForce = new Vector2(0, 0);
	private final boolean m_debugMode;
	private boolean m_showLayout;
	
	public World world;
	public Box2DDebugRenderer debugRenderer;
	
	Body body;
	Fixture fixture;
	
	public JBoxManager(MapLayers layers, OrthographicCamera camera, boolean debugMode, boolean showLayout) {
		// IMPORTANT
		// all libraries in jBox2d are loaded when World object is created
		world = new World(gravitationForce, true);
		m_debugMode = debugMode;
		m_showLayout = showLayout;
		if(m_debugMode) {
			debugRenderer = new Box2DDebugRenderer();
		}
		
		new LayerHitboxCreator(layers.get(LayersNames.obstacles), world);
		new LayerHitboxCreator(layers.get(LayersNames.dynamicObjects), world);
	}
	
	public void render(OrthographicCamera camera) {
		if(m_debugMode && m_showLayout) {
			debugRenderer.render(world, camera.combined);
		}
		world.step(timeStep, velocityIterations, positionIterations);
	}
	
	@Override
	public void dispose() {
		if(m_debugMode) {
			debugRenderer.dispose();
		}
		world.dispose();
	}

	public boolean isM_showLayout() {
		return m_showLayout;
	}

	public void setShowLayout(boolean m_showLayout) {
		this.m_showLayout = m_showLayout;
	}
}
