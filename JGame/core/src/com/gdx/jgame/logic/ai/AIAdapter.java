package com.gdx.jgame.logic.ai;

import java.io.Serializable;
import java.util.TreeMap;

import com.gdx.jgame.ObjectsID;

public abstract class AIAdapter implements ObjectsID<AIAdapter>, Comparable<AIAdapter>, Serializable{

	private static int m_numberOfObjects = 0;
	public final int ID = m_numberOfObjects;
	
	public AIAdapter() {
		++m_numberOfObjects;
	}
	
	@Override
	public int numberOfObjects() {
		return m_numberOfObjects;
	}

	@Override
	public boolean equalsID(AIAdapter object) {
		return ID == object.ID;
	}

	@Override
	public int compareTo(AIAdapter object) {
		if(this.ID == object.ID) return 0;
		else if(this.ID < object.ID) return -1;
		else return 1;
	}
	
	public abstract void load(TreeMap<Integer, Object> restoreOwner);
	
	public abstract void save();
	
}
