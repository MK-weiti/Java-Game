package com.gdx.jgame.gameObjects.missiles;

import java.util.Collection;
import java.util.HashMap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MissilesManager {
	private HashMap<Integer, Missile> missiles;
	
	public MissilesManager(){
		missiles = new HashMap<Integer, Missile>();
	}
	
	public void remove(Missile missile) {
		if(missile.isRemovable()) {
			missiles.remove(missile.ID);
			missile.deleteObject();
		}
	}
	
	public void add(Missile missile) {
		if(missiles.put(missile.ID, missile) != null) throw new IllegalArgumentException("The missile is already in manager.");
	}
	
	public void render(SpriteBatch batch) {
		for(Missile bullet : missiles.values()) {
			bullet.render(batch);
		}
	}
	
	Collection<Missile> getMissiles() {
		return missiles.values();
	}
}
