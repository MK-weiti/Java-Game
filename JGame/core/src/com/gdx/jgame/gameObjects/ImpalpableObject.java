package com.gdx.jgame.gameObjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/*
 * Doesn`t react with any objects.
 * Only as a texture with potencial animations.
 */

public abstract class ImpalpableObject{
	
protected Texture m_texture;
	
	public ImpalpableObject(Texture texture) {
		m_texture = texture;
	}
	
	public void setTexture(Texture texture) {
		if(m_texture != null)
		{
			m_texture.dispose();
		}
		m_texture = texture;
	}
	
	public void render(SpriteBatch batch, float posX, float posY) {
		batch.draw(m_texture, posX, posY);
	}
	
	public void dispose() {
		m_texture.dispose();
	}
}
