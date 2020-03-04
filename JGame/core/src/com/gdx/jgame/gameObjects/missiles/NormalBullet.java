package com.gdx.jgame.gameObjects.missiles;

import com.gdx.jgame.IDAdapter;
import com.gdx.jgame.gameObjects.missiles.def.NormalBulletDef;

public class NormalBullet extends Missile{	
	
	public NormalBullet(NormalBulletDef missileDef, IDAdapter owner) {
		super(missileDef, owner);
	}

	@Override
	public void updateObject(Object object) {
		return;
	}

	@Override
	public boolean isRemovable() {
		return isDestructible();
	}
	
	@Override
	public void setRemovable(boolean bool) {
		setDestructible(bool);
	}
	
	@Override
	public void deleteObject() {
		destoryBody();
	}
	
}
