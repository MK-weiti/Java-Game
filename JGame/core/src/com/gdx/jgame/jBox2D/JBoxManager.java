package com.gdx.jgame.jBox2D;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Disposable;
import com.gdx.jgame.world.MapManager;

// IMPORTANT
// all libraries in jBox2d are loaded when World object is created

public class JBoxManager implements Disposable{
	public float timeStep = 1/60f;
	public int velocityIterations = 6;
	public int positionIterations = 2;
	private static final Vector2 gravitationForce = new Vector2(0, 0);
	
	public World world;
	public Box2DDebugRenderer debugRenderer;
	
	Body body;
	Fixture fixture;
	
	public JBoxManager(MapLayers layers, OrthographicCamera camera) {
		// IMPORTANT
		// all libraries in jBox2d are loaded when World object is created
		world = new World(gravitationForce, true);
		debugRenderer = new Box2DDebugRenderer();
		
		new LayerHitboxCreator(layers.get(LayersNames.obstacles), world);
		new LayerHitboxCreator(layers.get(LayersNames.dynamicObjects), world);
	}
	
	// used only in the method below
	private float accumulator = 0;
	private void doPhysicsStep(float deltaTime) {
	    // fixed time step
	    // max frame time to avoid spiral of death (on slow devices)
	    float frameTime = Math.min(deltaTime, 0.25f);
	    accumulator += frameTime;
	    while (accumulator >= timeStep) {
	        world.step(timeStep, velocityIterations, positionIterations);
	        accumulator -= timeStep;
	    }
	}
	
	public void render(OrthographicCamera camera) {
		debugRenderer.render(world, camera.combined);
		doPhysicsStep(Gdx.graphics.getDeltaTime());
	}
	
	@Override
	public void dispose() {
		debugRenderer.dispose();
		world.dispose();
	}
}
