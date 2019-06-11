package com.gdx.jgame;

import java.io.Serializable;
import java.util.Map;

public abstract class IDAdapterSerializable extends IDAdapter implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5187143972311526566L;
	
	// use only when there is some reference to another object
	public abstract void load(Map<Integer, IDAdapter> restoreOwner);
	
	public abstract void save();
}
