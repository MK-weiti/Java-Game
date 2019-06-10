package com.gdx.jgame.jBox2D.mapObjects;

import java.io.Serializable;

import com.gdx.jgame.ObjectsID;
import com.gdx.jgame.jBox2D.BodyData;
import com.gdx.jgame.jBox2D.FixturePolData;
// TODO
public class PolygonObjectDef implements Serializable, ObjectsID<PolygonObjectDef>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1098250249849099559L;
	
	private static int m_numberOfObjects = 0;
	public final int ID = m_numberOfObjects;
	
	BodyData bodyData;
	FixturePolData fixturePolData;
	
	
	@Override
	public int numberOfObjects() {
		return m_numberOfObjects;
	}
	
	@Override
	public boolean equalsID(PolygonObjectDef object) {
		if(ID == object.ID) return true;
		return false;
	}

	public static void resetNumberOfObjects() {
		m_numberOfObjects = 0;
	}
}
