package com.gdx.jgame.jBox2D;

import java.io.Serializable;
import java.util.Arrays;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class FixtureData implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7225754191969239883L;
	
	public float friction = 0.2f;
	public float restitution = 0;
	public float density = 0;
	public boolean isSensor = false;
	
	public short categoryBits = 0x0001;
	public short maskBits = -1;
	public short groupIndex = 0;
	
	public Vector2[] shapeVertices;
	
	public FixtureData(FixtureDef fixtureDef) {
		friction = fixtureDef.friction;
		restitution = fixtureDef.restitution;
		density = fixtureDef.density;
		isSensor = fixtureDef.isSensor;
		categoryBits = fixtureDef.filter.categoryBits;
		maskBits = fixtureDef.filter.maskBits;
		groupIndex = fixtureDef.filter.groupIndex;
	}
	
	public FixtureData(FixtureData fixtureData) {
		friction = fixtureData.friction;
		restitution = fixtureData.restitution;
		density = fixtureData.density;
		isSensor = fixtureData.isSensor;
		categoryBits = fixtureData.categoryBits;
		maskBits = fixtureData.maskBits;
		groupIndex = fixtureData.groupIndex;
		
		shapeVertices = new Vector2[fixtureData.shapeVertices.length];
		for (int i = 0; i < fixtureData.shapeVertices.length; ++i) {
			shapeVertices[i] = new Vector2(fixtureData.shapeVertices[i]);
		}
	}

	public FixtureData() {} // default
	
	public void synchronize(FixtureDef fixtureDef) {
		friction = fixtureDef.friction;
		restitution = fixtureDef.restitution;
		density = fixtureDef.density;
		isSensor = fixtureDef.isSensor;
		categoryBits = fixtureDef.filter.categoryBits;
		maskBits = fixtureDef.filter.maskBits;
		groupIndex = fixtureDef.filter.groupIndex;
	}
	
	public void setVertices(Vector2[] vertices) {
		shapeVertices = new Vector2[vertices.length];
		for (int i = 0; i < vertices.length; ++i) {
			shapeVertices[i] = new Vector2(vertices[i]);
		}
	}
	
	public void restore(FixtureDef fixtureDef) {
		fixtureDef.filter.categoryBits = categoryBits;
		fixtureDef.filter.maskBits = maskBits;
		fixtureDef.filter.groupIndex = groupIndex;
		fixtureDef.density = density;
		fixtureDef.friction = friction;
		fixtureDef.isSensor = isSensor;
		fixtureDef.restitution = restitution;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + categoryBits;
		result = prime * result + Float.floatToIntBits(density);
		result = prime * result + Float.floatToIntBits(friction);
		result = prime * result + groupIndex;
		result = prime * result + (isSensor ? 1231 : 1237);
		result = prime * result + maskBits;
		result = prime * result + Float.floatToIntBits(restitution);
		result = prime * result + Arrays.hashCode(shapeVertices);
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
		FixtureData other = (FixtureData) obj;
		if (categoryBits != other.categoryBits)
			return false;
		if (Float.floatToIntBits(density) != Float.floatToIntBits(other.density))
			return false;
		if (Float.floatToIntBits(friction) != Float.floatToIntBits(other.friction))
			return false;
		if (groupIndex != other.groupIndex)
			return false;
		if (isSensor != other.isSensor)
			return false;
		if (maskBits != other.maskBits)
			return false;
		if (Float.floatToIntBits(restitution) != Float.floatToIntBits(other.restitution))
			return false;
		if (!Arrays.equals(shapeVertices, other.shapeVertices))
			return false;
		return true;
	}}
