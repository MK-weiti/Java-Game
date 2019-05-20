package com.gdx.jgame.gameObjects;

import java.io.Serializable;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.gdx.jgame.ObjectsID;
import com.gdx.jgame.TextureManager;
import com.gdx.jgame.jBox2D.BodyData;
import com.gdx.jgame.jBox2D.FixtureData;
import com.gdx.jgame.world.MapManager;

public class PalpableObjectPolygonDef implements Serializable, ObjectsID{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8695790437291106846L;
	private static long m_numberOfObjects = 0;
	public final long ID;
	
	BodyData bodyData;
	FixtureData fixtureData;
	
	public String texturePath;
	public float textureScale;
	
	public transient Texture texture = null;
	public transient World world = null;
	public transient FixtureDef fixtureDef;
	public transient BodyDef bodyDef;
	
	public PalpableObjectPolygonDef(World world, Texture texture, String texPath, float texScale, Vector2[] vertices) {
		ID = m_numberOfObjects;
		++m_numberOfObjects;
		bodyDef = new BodyDef();
		fixtureData = new FixtureData();
		fixtureDef = new FixtureDef();
		bodyData = new BodyData();
		
		this.world = world;
		this.texture = texture;
		texturePath = texPath;
		textureScale = texScale;
		
		bodyDef.type =  BodyType.StaticBody;
		bodyDef.fixedRotation = true;
		
		// only to initialize body`s full physics
		fixtureDef.density = 1f;	// 16 x 16 * density
		fixtureDef.friction = 0.5f;
		
		fixtureData.shapeVertices = vertices;
		
		// default
		textureScale = 1f;
	}

	public PalpableObjectPolygonDef(PalpableObjectPolygonDef definition) {
		ID = m_numberOfObjects;
		++m_numberOfObjects;
		bodyData = new BodyData(definition.bodyData);
		fixtureData = new FixtureData(definition.fixtureData);
		bodyDef = new BodyDef();
		fixtureDef = new FixtureDef();
		restoreFixture();
		restoreBody();
		bodyDef.position.set(definition.bodyDef.position);
		
		
		texturePath = new String(definition.texturePath);
		textureScale = definition.textureScale;
		
		texture = definition.texture;
		world = definition.world;
	}
	
	public void setPosition(float posX, float posY) {
		bodyDef.position.set(posX / MapManager.PIXELS_PER_METER, 
				posY / MapManager.PIXELS_PER_METER);
	}
	
	public void synchronize() {
		bodyData.synchronize(bodyDef);
		fixtureData.synchronize(fixtureDef);
	}
	
	private void restoreFixture() {
		fixtureData.restore(fixtureDef);
	}
	
	public void restoreBody() {
		bodyData.restore(bodyDef);
	}
	
	public long numberOfObjects() {
		return m_numberOfObjects;
	}
	
	public void restoreObject(TextureManager txMan, World world) {
		texture = txMan.get(texturePath);
		this.world = world;
		fixtureDef = new FixtureDef();
		bodyDef = new BodyDef();
		restoreFixture();
		restoreBody();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (ID ^ (ID >>> 32));
		result = prime * result + ((bodyData == null) ? 0 : bodyData.hashCode());
		result = prime * result + ((fixtureData == null) ? 0 : fixtureData.hashCode());
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
		if (fixtureData == null) {
			if (other.fixtureData != null)
				return false;
		} else if (!fixtureData.equals(other.fixtureData))
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
	
	
}
