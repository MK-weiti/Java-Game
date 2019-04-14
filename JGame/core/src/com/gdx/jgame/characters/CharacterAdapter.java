package com.gdx.jgame.characters;

public interface CharacterAdapter {
	public void createApperance(String image);
	
	public void render();
	
	public void disposeApperance();
	
	public void resize(int width, int height);
	
	public void setPosition(float startPosX, float startPosY);
	
	public void move(float deltaX, float deltaY);
	
	public float getPosX();
	
	public float getPosY();
	
	public int getWidth();
	
	public int getHeight();
}
