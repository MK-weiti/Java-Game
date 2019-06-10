package com.gdx.jgame.jBox2D.mapObjects;

import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Disposable;
import com.gdx.jgame.jBox2D.LayerHitboxCreator;
import com.gdx.jgame.managers.MapManager;

public class PolygonObject implements Disposable{
	private World m_world;
	private Vector2[] m_vertices;
	
	public BodyDef bodyDef;
	public FixtureDef fixtureDef;
	public PolygonShape shape;
	public BodyType bodyType = null;
	
	private boolean m_created = false;
	
	public PolygonObject(World world, BodyType bodyType){
		m_world = world;
		bodyDef = new BodyDef();
		fixtureDef = new FixtureDef();
		shape = new PolygonShape();
		
		// initialize body physics
		fixtureDef.density = 1f;
		fixtureDef.friction = 0.5f;
		this.bodyType = bodyType;
		}
	
	public Body createRectangle(RectangleMapObject rectangleObject) {
		if(m_created) return null;
		
		Rectangle rectangle = rectangleObject.getRectangle();
		m_vertices = new Vector2[4];
		
		bodyDef.position.set(rectangle.x / MapManager.PIXELS_PER_METER, 
				rectangle.y / MapManager.PIXELS_PER_METER);
		
		float width = rectangle.getWidth() / MapManager.PIXELS_PER_METER;
		float height = rectangle.getHeight() / MapManager.PIXELS_PER_METER;
		
		m_vertices[0] = new Vector2(0, 0);
		m_vertices[1] = new Vector2(width, 0);
		m_vertices[2] = new Vector2(width, height);
		m_vertices[3] = new Vector2(0, height);
		
		shape.set(m_vertices);
		fixtureDef.shape = shape;
		
		Body body;
		
		if((body = LayerHitboxCreator.setProperties(m_world, rectangleObject.getProperties(), bodyDef, fixtureDef)) == null) return null;
		m_created = true;		
		
		return body;
	}

	public Body createPolygon(PolygonMapObject polygonObject) {
		if(m_created) return null;
		
		Polygon polygon = polygonObject.getPolygon();
		PolygonShape shape = new PolygonShape();
		float[] bodyVertices = polygon.getVertices();
		m_vertices = new Vector2[bodyVertices.length/2];
		
		bodyDef.type = bodyType;
		
		bodyDef.position.set(polygon.getX() / MapManager.PIXELS_PER_METER, 
				polygon.getY() / MapManager.PIXELS_PER_METER);
		
		for (int i = 0, j = 0; i < bodyVertices.length; i+=2, ++j) {
			m_vertices[j] = new Vector2(bodyVertices[i] / MapManager.PIXELS_PER_METER, 
					bodyVertices[i+1] / MapManager.PIXELS_PER_METER);
		}
		
		shape.set(m_vertices);
		fixtureDef.shape = shape;
		
		Body body;
		
		if((body = LayerHitboxCreator.setProperties(m_world, polygonObject.getProperties(), bodyDef, fixtureDef)) == null) return null;
		
		m_created = true;
		return body;
	}
	
	// TODO
	@Deprecated
	public Body createEllipse(EllipseMapObject ellipseObject){
		if(m_created) return null;
		
		Ellipse ellipse = ellipseObject.getEllipse();
		
		CircleShape circleShape;
		BodyDef bodyDef = new BodyDef();
		FixtureDef fixtureDef = new FixtureDef();

		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set((ellipse.x + ellipse.width/2)/ MapManager.PIXELS_PER_METER, 
				(ellipse.y + ellipse.height/2) / MapManager.PIXELS_PER_METER);	
		
		circleShape = new CircleShape();
		circleShape.setRadius((ellipse.height / 2) / MapManager.PIXELS_PER_METER);
		
		fixtureDef.shape = circleShape;
		fixtureDef.density = 0.5f; 
		fixtureDef.friction = 0.4f;
		fixtureDef.restitution = 0.6f;
		fixtureDef.filter.categoryBits = 1;
		
		shape.set(m_vertices);
		fixtureDef.shape = shape;
		
		Body body;
		
		if((body = LayerHitboxCreator.setProperties(m_world, ellipseObject.getProperties(), bodyDef, fixtureDef)) == null) return null;
		
		m_created = true;
		return body;
	}

	@Override
	public void dispose() {
		shape.dispose();
	}

	boolean isCreated() {
		return m_created;
	}
	
	
}
