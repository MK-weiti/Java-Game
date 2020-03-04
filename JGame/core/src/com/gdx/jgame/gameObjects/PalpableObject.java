package com.gdx.jgame.gameObjects;

import java.awt.IllegalComponentStateException;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.gdx.jgame.Camera;
import com.gdx.jgame.IDAdapter;
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

public abstract class PalpableObject extends IDAdapter{
	
	private static int m_numberOfObjects = 0;
	public transient final int ID = m_numberOfObjects;
	
	private transient Sprite defaultSprite;
	
	private transient World world;
	private transient Body body;	// get access to give forces to body
	protected transient Fixture mainFixture;
	private float scale;
	private String texturePath;
	private transient Vector2 lastPosition; // to reconstruct destroyed body
	private boolean isBodyDestroyed = false;
	private boolean isDeleted = false;
	protected boolean isDestructible = true;
	
	protected UserData userData;

	public PalpableObject(PalpableObjectPolygonDef objectDef) {
		if(objectDef.textureScale == 0) throw new IllegalComponentStateException("No scale.");
		if(objectDef.texturePath == null) throw new IllegalComponentStateException("No texture path.");
		if(objectDef.fixturePolData.shapeVertices == null) throw new IllegalComponentStateException("No vertices.");
		
		++m_numberOfObjects;
		world = objectDef.world;
		scale = objectDef.textureScale;
		
		texturePath = objectDef.texturePath;		
		body = world.createBody(objectDef.bodyDef);
		lastPosition = new Vector2(objectDef.bodyDef.position);
		
		PolygonShape shape = new PolygonShape();
		shape.set(objectDef.fixturePolData.shapeVertices);
		objectDef.fixtureDef.shape = shape;
		mainFixture = body.createFixture(objectDef.fixtureDef); 
		
		userData = new UserData(this);
		body.setUserData(userData);
		
		setTexture(objectDef.texture);
		shape.dispose();
	}	

	private void setTexture(Texture defaultTexture) {
		defaultSprite = new Sprite(defaultTexture);		
		defaultSprite.setBounds(0, 0, defaultSprite.getWidth() * scale / MapManager.PIXELS_PER_METER, 
				defaultSprite.getHeight() * scale / MapManager.PIXELS_PER_METER);
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
		scale = txScale;
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
	
	public float angleRadOnScreen(Camera camera, Vector2 position, float shift) {		
		return getVectorToMouse(camera, position).angleRad() + shift;
	}
	
	public float angleToTarget(Vector2 target, float shift) {
		return getVectorToTarget(target).angleRad() + shift;
	}
	
	public float getBodyAngle(float shift) {
		return body.getAngle() + shift;
	}
	
	public Vector2 getVectorToMouse(Camera camera, Vector2 position) {
		Vector2 tmp = new Vector2(position);
		
		tmp.x = getPositionOnScreen(camera).x - tmp.x;
		tmp.y = tmp.y - getPositionOnScreen(camera).y;
		
		return tmp;
	}
	
	public Vector2 getVectorToTarget(Vector2 target) {
		Vector2 tmp = new Vector2(target);
		
		tmp.x = body.getPosition().x - tmp.x;
		tmp.y = body.getPosition().y - tmp.y;
		
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
		return scale;
	}
	
	public String getTexturePath() {
		return texturePath;
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
	
	public boolean isDeleted() {
		return isDeleted;
	}
	
	public void setIsDeleted(boolean bool) {
		isDeleted = bool;
	}
	
	protected void setDestructible(boolean bool) {
		isDestructible = bool;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (isBodyDestroyed ? 1231 : 1237);
		result = prime * result + (isDestructible ? 1231 : 1237);
		result = prime * result + Float.floatToIntBits(scale);
		result = prime * result + ((texturePath == null) ? 0 : texturePath.hashCode());
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
		if (Float.floatToIntBits(scale) != Float.floatToIntBits(other.scale))
			return false;
		if (texturePath == null) {
			if (other.texturePath != null)
				return false;
		} else if (!texturePath.equals(other.texturePath))
			return false;
		return true;
	}
	
}
