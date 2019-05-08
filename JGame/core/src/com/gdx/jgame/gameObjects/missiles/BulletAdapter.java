package com.gdx.jgame.gameObjects.missiles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;
import com.gdx.jgame.gameObjects.*;

public abstract class BulletAdapter extends MovingObjectAdapter{
	
	public BulletAdapter(Texture texture, World world, Vector2 position, float scale, float maxVelocity, 
			float acceleration, BodyType bodyType) {
		super(texture, world, position, scale, maxVelocity, acceleration, bodyType);
		defaultSprite.setPosition(position.x, position.y);
	}
	
	public BulletAdapter(Texture texture, World world, float posX, float posY, float scale, float maxVelocity, 
			float acceleration, BodyType bodyType) {
		super(texture, world, new Vector2(posX, posY), scale, maxVelocity, acceleration, bodyType);
		defaultSprite.setPosition(posX, posY);
	}	
	
	public void location() {
		// TODO
	}
	
}
