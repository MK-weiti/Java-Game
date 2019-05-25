package com.gdx.jgame.jBox2D.mapObjects;

import java.io.Serializable;

import com.gdx.jgame.ObjectsID;
import com.gdx.jgame.jBox2D.BodyData;
import com.gdx.jgame.jBox2D.FixturePolData;
// TODO
public class PolygonObjectDef implements Serializable, ObjectsID{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1098250249849099559L;
	
	private static long m_numberOfObjects = 0;
	public final long ID = m_numberOfObjects;
	
	BodyData bodyData;
	FixturePolData fixturePolData;
	
	
	@Override
	public long numberOfObjects() {
		return m_numberOfObjects;
	}
}
