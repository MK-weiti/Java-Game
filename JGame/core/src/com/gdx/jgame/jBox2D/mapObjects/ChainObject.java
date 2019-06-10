package com.gdx.jgame.jBox2D.mapObjects;

import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.math.Polyline;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.gdx.jgame.jBox2D.LayerHitboxCreator;
import com.gdx.jgame.managers.MapManager;
import com.badlogic.gdx.physics.box2d.ChainShape;

public class ChainObject {
	private World m_world;
	private Vector2[] m_vertices;
	
	public BodyDef bodyDef;
	public FixtureDef fixtureDef;
	public ChainShape shape;
	public BodyType bodyType = null;
	
	private boolean m_created = false;
	
	ChainObject(World world, BodyType bodyType){
		m_world = world;
		bodyDef = new BodyDef();
		fixtureDef = new FixtureDef();
		shape = new ChainShape();
		
		// initialize body physics
		fixtureDef.density = 1f;
		fixtureDef.friction = 0.5f;
		this.bodyType = bodyType;
		}
	
	Body createPolyline(PolylineMapObject polylineObject) {
		if(m_created) return null;
		
		Polyline polyline = polylineObject.getPolyline();
		PolygonShape shape = new PolygonShape();
		float[] bodyVertices = polyline.getVertices();
		m_vertices = new Vector2[bodyVertices.length/2];
		
		bodyDef.type = bodyType;
		
		bodyDef.position.set(polyline.getX() / MapManager.PIXELS_PER_METER, 
				polyline.getY() / MapManager.PIXELS_PER_METER);
		
		for (int i = 0, j = 0; i < bodyVertices.length; i+=2, ++j) {
			m_vertices[j] = new Vector2(bodyVertices[i] / MapManager.PIXELS_PER_METER, 
					bodyVertices[i+1] / MapManager.PIXELS_PER_METER);
		}
		
		shape.set(m_vertices);
		fixtureDef.shape = shape;
		
		Body body;
		
		if((body = LayerHitboxCreator.setProperties(m_world, polylineObject.getProperties(), bodyDef, fixtureDef)) == null) return null;
		
		m_created = true;
		return body;
	}
}
