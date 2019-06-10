package com.gdx.jgame.jBox2D;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Disposable;
import com.gdx.jgame.JGame;
import com.gdx.jgame.jBox2D.contactListeners.ContactListenerSet;

// IMPORTANT
// all libraries in jBox2d are loaded when World object is created

public class JBoxObjects implements Disposable{
	private float timeStep = 1/60f;
	private int velocityIterations = 6;
	private int positionIterations = 2;
	private JGame m_jGame;
	private static final Vector2 gravitationForce = new Vector2(0f, 0f);
	private final boolean m_debugMode;
	private boolean m_showLayout;
	private ContactListenerSet m_ListenerSet;
	private BodiesToDestroy m_destroyBodies;
	
	private World world;
	private Box2DDebugRenderer debugRenderer;
	
	public JBoxObjects(JGame jGame, MapLayers layers, OrthographicCamera camera, boolean debugMode, boolean showLayout) {
		// IMPORTANT
		// all libraries in jBox2d are loaded when World object is created
		m_jGame = jGame;
		world = new World(gravitationForce, true);
		m_destroyBodies = new BodiesToDestroy(m_jGame);
		m_debugMode = debugMode;
		m_showLayout = showLayout;
		
		if(m_debugMode) {
			debugRenderer = new Box2DDebugRenderer();
		}
		
		if(layers.get(LayersNames.obstacles) != null)
			new LayerHitboxCreator(layers.get(LayersNames.obstacles), world);
		if(layers.get(LayersNames.logic) != null)
			new LayerHitboxCreator(layers.get(LayersNames.logic), world);
		
		m_ListenerSet = new ContactListenerSet(m_jGame, m_destroyBodies);
		world.setContactListener(m_ListenerSet);
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

	public boolean isShowLayout() {
		return m_showLayout;
	}

	public void setShowLayout(boolean m_showLayout) {
		this.m_showLayout = m_showLayout;
	}
	
	public World getWorld() {
		return world;
	}

	public void removeBodies() {
		m_destroyBodies.removeAll();
	}
}
