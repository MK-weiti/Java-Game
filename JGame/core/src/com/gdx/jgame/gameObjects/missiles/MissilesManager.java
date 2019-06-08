package com.gdx.jgame.gameObjects.missiles;

import java.util.Collection;
import java.util.HashMap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MissilesManager {
	private HashMap<MissileAdapter, MissileAdapter> missiles;
	
	public MissilesManager(){
		missiles = new HashMap<MissileAdapter, MissileAdapter>();
	}
	
	public void remove(MissileAdapter missile) {
		if(missile.isRemovable()) {
			missiles.remove(missile);
			missile.deleteObject();
		}
	}
	
	public void add(MissileAdapter missile) {
		missiles.put(missile, missile);
	}
	
	public void render(SpriteBatch batch) {
		
		for(MissileAdapter bullet : missiles.keySet()) {
			bullet.render(batch);
		}
	}
	
	Collection<MissileAdapter> getMissiles() {
		return missiles.values();
	}
}
