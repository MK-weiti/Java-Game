package com.gdx.jgame;

public abstract class IDAdapter implements ObjectsID<IDAdapter>, Comparable<IDAdapter>{

	private static int m_numberOfObjects = 0;
	public final int ID = m_numberOfObjects;
	
	public IDAdapter() {
		++m_numberOfObjects;
	}
	
	@Override
	public int numberOfObjects() {
		return m_numberOfObjects;
	}

	@Override
	public boolean equalsID(IDAdapter object) {
		return ID == object.ID;
	}

	@Override
	public int compareTo(IDAdapter object) {
		if(this.ID == object.ID) return 0;
		else if(this.ID < object.ID) return -1;
		else return 1;
	}
	
}
