package com.gdx.jgame.gameObjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.gdx.jgame.Camera;
import com.gdx.jgame.ObjectsID;
import com.gdx.jgame.jBox2D.FixturePolData;
import com.gdx.jgame.managers.MapManager;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

/*
 * React with other PalpableObject.
 * Create as body with fixture.
 */

public abstract class PalpableObject implements ObjectsID<PalpableObject>, Comparable<PalpableObject>{
	
	private static int m_numberOfObjects = 0;
	public final int ID = m_numberOfObjects;
	
	private Sprite defaultSprite;
	
	private World world;
	private Body body;	// get access to give forces to body
	protected Fixture mainFixture;
	private float m_scale;
	private String m_texturePath;
	private Vector2 lastPosition;
	private boolean isBodyDestroyed = false;
	protected boolean isDestructible = true;

	public PalpableObject(PalpableObjectPolygonDef objectDef) {
		++m_numberOfObjects;
		world = objectDef.world;
		m_scale = objectDef.textureScale;
		m_texturePath = objectDef.texturePath;
		body = world.createBody(objectDef.bodyDef);
		lastPosition = new Vector2(objectDef.bodyDef.position);
		objectDef.setObjectInGameID(ID);
		
		PolygonShape shape = new PolygonShape();
		shape.set(objectDef.fixturePolData.shapeVertices);
		objectDef.fixtureDef.shape = shape;
		mainFixture = body.createFixture(objectDef.fixtureDef); 
		body.setUserData(this);
		
		setTexture(objectDef.texture);
		shape.dispose();
	}	

	private void setTexture(Texture defaultTexture) {
		defaultSprite = new Sprite(defaultTexture);		
		defaultSprite.setBounds(0, 0, defaultSprite.getWidth() * m_scale / MapManager.PIXELS_PER_METER, 
				defaultSprite.getHeight() * m_scale / MapManager.PIXELS_PER_METER);
		defaultSprite.setOriginCenter();
	}
	
	private void changeMainFixture(Vector2[] vertices) {
		FixturePolData def = new FixturePolData(mainFixture);
		PolygonShape shape = new PolygonShape();
		FixtureDef fdef = new FixtureDef();
		body.destroyFixture(mainFixture);
		
		shape.set(vertices);
		def.restore(fdef);
		fdef.shape = shape;
		
		mainFixture = body.createFixture(fdef);
		
		shape.dispose();
	}
	
	public void changeMainFixture(Texture texture, Vector2[] vertices, float txScale) {
		m_scale = txScale;
		changeMainFixture(vertices);
		setTexture(texture);
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
		if(!isBodyDestroyed) {
			defaultSprite.setRotation(body.getAngle()*(float)((double)180/Math.PI));
			defaultSprite.setPosition(body.getPosition().x - defaultSprite.getWidth()/2, 
					body.getPosition().y - defaultSprite.getHeight() / 2);
			defaultSprite.draw(batch);
		}
		else {
			defaultSprite.draw(batch);
		}
		
	}
	
	public void rotate(float angle) {
		body.setTransform(body.getWorldCenter(), angle);
	}
	
	public Vector2 getPosition() {
		return body.getPosition();
	}
	
	public Vector2 getPositionOnScreen(Camera camera) {
		if(camera.getFollower() != this) {
			throw new IllegalArgumentException("Camera do not follow this object");
		}
		return camera.getPositionOnScreen();
	}
	
	public float rawAngleRadOnScreen(Camera camera, Vector2 position) {
		return getVectorToMouse(camera, position).angleRad();
	}
	
	public float angleRadOnScreen(Camera camera, Vector2 position) {		
		return getVectorToMouse(camera, position).angleRad() - ((float) Math.PI/2);
	}
	
	public Vector2 getVectorToMouse(Camera camera, Vector2 position) {
		Vector2 tmp = new Vector2(position);
		
		tmp.x = tmp.x - getPositionOnScreen(camera).x;
		tmp.y = tmp.y - getPositionOnScreen(camera).y;
		
		return tmp;
	}

	public World getWorld() {
		return world;
	}
	
	public Body getBody() {
		return body;
	}
	
	public Sprite getDefaultSprite() {
		return defaultSprite;
	}

	public float getScale() {
		return m_scale;
	}
	
	public String getTexturePath() {
		return m_texturePath;
	}
	
	protected void destoryBody() {
		if(!isBodyDestroyed) {
			lastPosition.set(body.getPosition());
			isBodyDestroyed = true;
			world.destroyBody(body);
		}
	}
	
	public Vector2 getLastPosition() {
		if(isBodyDestroyed) {
			return new Vector2(lastPosition);
		}
		else {
			lastPosition.set(body.getPosition());
			return new Vector2(lastPosition);
		}
	}

	public boolean isBodyDestroyed() {
		return isBodyDestroyed;
	}

	protected boolean isDestructible() {
		return isDestructible;
	}

	@Override
	public int numberOfObjects() {
		return m_numberOfObjects;
	}

	// only for optimization of Map
	@Override
	public int compareTo(PalpableObject object) {
		if(this.ID == object.ID) return 0;
		else if(this.ID < object.ID) return -1;
		else return 1;
	}

	@Override
	public boolean equalsID(PalpableObject object) {
		if(ID == object.ID) return true;
		return false;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (isBodyDestroyed ? 1231 : 1237);
		result = prime * result + (isDestructible ? 1231 : 1237);
		result = prime * result + ((lastPosition == null) ? 0 : lastPosition.hashCode());
		result = prime * result + Float.floatToIntBits(m_scale);
		result = prime * result + ((m_texturePath == null) ? 0 : m_texturePath.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PalpableObject other = (PalpableObject) obj;
		if (isBodyDestroyed != other.isBodyDestroyed)
			return false;
		if (isDestructible != other.isDestructible)
			return false;
		if (lastPosition == null) {
			if (other.lastPosition != null)
				return false;
		} else if (!lastPosition.equals(other.lastPosition))
			return false;
		if (Float.floatToIntBits(m_scale) != Float.floatToIntBits(other.m_scale))
			return false;
		if (m_texturePath == null) {
			if (other.m_texturePath != null)
				return false;
		} else if (!m_texturePath.equals(other.m_texturePath))
			return false;
		return true;
	}
	
}
