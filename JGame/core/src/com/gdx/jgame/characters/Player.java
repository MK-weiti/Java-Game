package com.gdx.jgame.characters;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Player implements CharacterAdapter{
	private SpriteBatch m_batch;
	private Texture m_img;
	
	private float m_xpos, m_ypos;	// point in the bottom left of the character
	private int m_playerWidth, m_playerHeight;
	
	public Player(float startPosX, float startPosY){
		setPosition(startPosX, startPosY);
	}
	
	public Player(){}
	
	public void setPosition(float startPosX, float startPosY) {
		m_xpos=startPosX;
		m_ypos=startPosY;
	}
	
	@Override
	public void createApperance(String image) {
		m_batch = new SpriteBatch();
		m_img = new Texture(image);
		
		m_playerWidth = m_img.getWidth();
		m_playerHeight = m_img.getHeight();
	}
	
	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void render() {
		m_batch.begin();
		m_batch.draw(m_img, m_xpos, m_ypos, m_playerWidth, m_playerHeight);
		m_batch.end();
	}
	
	@Override
	public void disposeApperance() {
		m_batch.dispose();
		m_img.dispose();
	}
	
	@Override
	public float getPosX(){
		return m_xpos;
	}
	
	@Override
	public float getPosY() {
		return m_ypos;
	}
	
	@Override
	public void move(float deltaX, float deltaY) {
		m_xpos+=deltaX;
		m_ypos+=deltaY;
	}
	
	@Override
	public int getWidth() {
		return m_playerWidth;
	}
	
	@Override
	public int getHeight() {
		return m_playerHeight;
	}
	
}
