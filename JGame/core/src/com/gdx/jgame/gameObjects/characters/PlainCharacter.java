package com.gdx.jgame.gameObjects.characters;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.gdx.jgame.gameObjects.MovingObjectAdapter;

public abstract class PlainCharacter extends MovingObjectAdapter{
	
	public PlainCharacter(Texture defaultTexture, World world, Vector2 position, float scale, 
			float maxVelocity, float acceleration){
		super(defaultTexture, world, position, scale, maxVelocity, acceleration, BodyType.DynamicBody);
		
		defaultSprite.setPosition(position.x, position.y);
	}
	
	public PlainCharacter(Texture defaultTexture, World world, float posX, 
			float posY, float scale, float maxVelocity, float acceleration){
		super(defaultTexture, world, new Vector2(posX, posY), scale, maxVelocity, acceleration, 
				BodyType.DynamicBody);
		
		defaultSprite.setPosition(posX, posY);
	}
}
