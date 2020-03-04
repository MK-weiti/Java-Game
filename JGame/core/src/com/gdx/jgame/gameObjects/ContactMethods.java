package com.gdx.jgame.gameObjects;

public interface ContactMethods {
	
	public void updateObject(Object object);
	
	public boolean isRemovable();
	
	public void setRemovable(boolean bool);
	
	public void deleteObject();
}
