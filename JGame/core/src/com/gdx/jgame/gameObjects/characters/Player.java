package com.gdx.jgame.gameObjects.characters;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class Player extends PlainCharacter{
	
	public Player(Texture defaultTexture, World world, Vector2 position, float scale, 
			float maxVelocity, float speed){
		super(defaultTexture, world, position, scale, maxVelocity, speed);
		constructor();
	}
	
	public Player(Texture defaultTexture, World world, float posX, 
			float posY, float scale, float maxVelocity, float speed){
		super(defaultTexture, world, posX, posY, scale, maxVelocity, speed);
		constructor();
	}	
	
	private void constructor() {
	}
	
}
