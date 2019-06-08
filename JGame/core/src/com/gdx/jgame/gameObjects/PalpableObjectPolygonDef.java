package com.gdx.jgame.gameObjects;

import java.io.Serializable;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.gdx.jgame.ObjectsID;
import com.gdx.jgame.jBox2D.BodyData;
import com.gdx.jgame.jBox2D.FixturePolData;
import com.gdx.jgame.managers.MapManager;
import com.gdx.jgame.managers.TextureManager;

public class PalpableObjectPolygonDef implements Serializable, ObjectsID, Comparable<PalpableObjectPolygonDef>{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7170239912473329317L;
	
	private static long m_numberOfObjects = 0;
	public final long ID = m_numberOfObjects;
	
	BodyData bodyData;
	FixturePolData fixturePolData;
	
	public String texturePath;
	public float textureScale;
	
	public transient Texture texture = null;
	public transient World world = null;
	public transient FixtureDef fixtureDef;
	public transient BodyDef bodyDef;
	
	public PalpableObjectPolygonDef(World world, Texture texture, String texPath, float texScale, Vector2[] vertices) {
		++m_numberOfObjects;
		bodyDef = new BodyDef();
		fixturePolData = new FixturePolData();
		fixtureDef = new FixtureDef();
		bodyData = new BodyData();
		
		this.world = world;
		this.texture = texture;
		texturePath = texPath;
		textureScale = texScale;
		
		bodyDef.type =  BodyType.StaticBody;
		bodyDef.fixedRotation = true;
		
		// only to initialize body`s full physics
		fixtureDef.density = 1f;	// 16 * 16 * density
		fixtureDef.friction = 0.5f;
		
		fixturePolData.shapeVertices = vertices;
	}
	
	public PalpableObjectPolygonDef(PalpableObject palpableObject) {
		++m_numberOfObjects;
		
		bodyDef = new BodyDef();
		fixturePolData = new FixturePolData(palpableObject.getBody().getFixtureList().first());
		fixtureDef = new FixtureDef();
		bodyData = new BodyData(palpableObject.getBody());
		
		this.world = palpableObject.getWorld();
		this.texture = palpableObject.getDefaultSprite().getTexture();
		texturePath = palpableObject.getTexturePath();
		textureScale = palpableObject.getScale();
		
		restoreFixBody();
	}

	public PalpableObjectPolygonDef(PalpableObjectPolygonDef definition) {
		++m_numberOfObjects;
		bodyData = new BodyData(definition.bodyData);
		fixturePolData = new FixturePolData(definition.fixturePolData);
		bodyDef = new BodyDef();
		fixtureDef = new FixtureDef();
		
		texturePath = new String(definition.texturePath);
		textureScale = definition.textureScale;
		
		texture = definition.texture;
		world = definition.world;
		
		restoreFixBody();
	}
	
	public void setPosition(float posX, float posY) {
		bodyDef.position.set(posX / MapManager.PIXELS_PER_METER, 
				posY / MapManager.PIXELS_PER_METER);
	}
	
	public void setPosition(Vector2 pos) {
		bodyDef.position.set(pos.x / MapManager.PIXELS_PER_METER, 
				pos.y / MapManager.PIXELS_PER_METER);
	}
	
	public void setPositionInGame(Vector2 pos) {
		bodyDef.position.set(pos.x, pos.y);
	}
	
	public Vector2 getPosition() {
		return new Vector2(bodyDef.position);
	}
	
	public void synchronize() {
		bodyData.synchronize(bodyDef);
		fixturePolData.synchronize(fixtureDef);
	}
	
	private void restoreFixture() {
		fixturePolData.restore(fixtureDef);
	}
	
	public void restoreBody() {
		bodyData.restore(bodyDef);
	}
	
	public void restoreFixBody() {
		restoreFixture();
		restoreBody();
	}
	
	public long numberOfObjects() {
		return m_numberOfObjects;
	}
	
	public void restore(TextureManager txMan, World world) {
		texture = txMan.get(texturePath);
		this.world = world;
		fixtureDef = new FixtureDef();
		bodyDef = new BodyDef();
		restoreFixBody();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (ID ^ (ID >>> 32));
		result = prime * result + ((bodyData == null) ? 0 : bodyData.hashCode());
		result = prime * result + ((fixturePolData == null) ? 0 : fixturePolData.hashCode());
		result = prime * result + ((texturePath == null) ? 0 : texturePath.hashCode());
		result = prime * result + Float.floatToIntBits(textureScale);
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
		PalpableObjectPolygonDef other = (PalpableObjectPolygonDef) obj;
		if (ID != other.ID)
			return false;
		if (bodyData == null) {
			if (other.bodyData != null)
				return false;
		} else if (!bodyData.equals(other.bodyData))
			return false;
		if (fixturePolData == null) {
			if (other.fixturePolData != null)
				return false;
		} else if (!fixturePolData.equals(other.fixturePolData))
			return false;
		if (texturePath == null) {
			if (other.texturePath != null)
				return false;
		} else if (!texturePath.equals(other.texturePath))
			return false;
		if (Float.floatToIntBits(textureScale) != Float.floatToIntBits(other.textureScale))
			return false;
		return true;
	}

	@Override
	public int compareTo(PalpableObjectPolygonDef object) {
		if(this.numberOfObjects() == object.numberOfObjects()) return 0;
		else if(this.numberOfObjects() < object.numberOfObjects()) return -1;
		else return 1;
	}
	
	
}
