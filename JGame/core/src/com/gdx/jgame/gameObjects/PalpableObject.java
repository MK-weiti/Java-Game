package com.gdx.jgame.gameObjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.gdx.jgame.world.MapManager;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

/*
 * React with other PalpableObject.
 * Create as body with fixture.
 */

public class PalpableObject {
	public Sprite defaultSprite;
	
	public World world;
	public  Body body;	// get access to give forces to body
	protected Fixture fixture;	// to change later the properties of fixture
	private float m_scale;
	
	public PalpableObject(Texture defaultTexture, World world, Vector2 pos, float scale, BodyType bodyType) {	
		m_scale = scale;
		this.world = world;
		
		createBody(defaultTexture, world, pos, m_scale, bodyType);
	}

	private void createBody(Texture defaultTexture, World world, Vector2 pos, float scale, BodyType bodyType) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(pos.x / MapManager.PIXELS_PER_METER, 
				pos.y / MapManager.PIXELS_PER_METER);
		bodyDef.type = bodyType;
		bodyDef.fixedRotation = false;
		
		FixtureDef fixtureDef = new FixtureDef();
		PolygonShape shape = new PolygonShape();
		
		Vector2[] points = new Vector2[4];
		float tmpWidth = defaultTexture.getWidth() * scale / MapManager.PIXELS_PER_METER /2;
		float tmpHeight = defaultTexture.getHeight() * scale / MapManager.PIXELS_PER_METER / 2;
		
		points[0] = new Vector2(- tmpWidth, - tmpHeight);
		points[1] = new Vector2(tmpWidth, - tmpHeight);
		points[2] = new Vector2(tmpWidth, tmpHeight);
		points[3] = new Vector2(- tmpWidth, tmpHeight);
		
		shape.set(points);
		fixtureDef.shape = shape;
		fixtureDef.density = 1f;	// 16 x 16 * density
		fixtureDef.friction = 0.2f;
		
		body = world.createBody(bodyDef);
		fixture = body.createFixture(fixtureDef);
		
		shape.dispose();

		setTexture(defaultTexture);
	}
	

	private void setTexture(Texture defaultTexture) {
		defaultSprite = new Sprite(defaultTexture);		
		defaultSprite.setBounds(0, 0, defaultSprite.getWidth() * m_scale / MapManager.PIXELS_PER_METER, 
				defaultSprite.getHeight() * m_scale / MapManager.PIXELS_PER_METER);
		defaultSprite.setOriginCenter();
	}
	
	public static boolean overlaps(Polygon polygon, Rectangle rectangle) {
	    Polygon rPoly = new Polygon(new float[] { 0, 0, rectangle.width, 0, rectangle.width,
	            rectangle.height, 0, rectangle.height });
	    
	    rPoly.setPosition(rectangle.x, rectangle.y);
	    if (Intersector.overlapConvexPolygons(rPoly, polygon))
	        return true;
	    return false;
	}
	
	public void render(SpriteBatch batch) {
		defaultSprite.setRotation(body.getAngle()*(float)(180/Math.PI));
		defaultSprite.setPosition(fixture.getBody().getPosition().x - defaultSprite.getWidth()/2, 
				fixture.getBody().getPosition().y - defaultSprite.getHeight() / 2);
		defaultSprite.draw(batch);
	}
	
	public void setScale(float scale) {
		m_scale = scale;
		
		Sprite newSprite = new Sprite(defaultSprite.getTexture());		
		
		createBody(newSprite.getTexture(), world, body.getPosition(), m_scale, body.getType());
	}
	
	private void rotate() {
		// TODO
	}
}
