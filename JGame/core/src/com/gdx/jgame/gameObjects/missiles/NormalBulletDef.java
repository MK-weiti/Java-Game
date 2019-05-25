package com.gdx.jgame.gameObjects.missiles;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.gdx.jgame.gameObjects.PalpableObjectPolygonDef;
import com.gdx.jgame.managers.TextureManager;

public class NormalBulletDef extends PalpableObjectPolygonDef{

	public NormalBulletDef(World world, TextureManager txManager, String texPath, float texScale, Vector2[] vertices) {
		super(world, txManager.get(texPath), texPath, texScale, vertices);
		
	}
	
	public NormalBulletDef(NormalBulletDef definition) {
		super(definition);
		// TODO Auto-generated constructor stub
	}

}
