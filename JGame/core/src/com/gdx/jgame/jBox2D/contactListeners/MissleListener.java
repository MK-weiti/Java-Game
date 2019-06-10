package com.gdx.jgame.jBox2D.contactListeners;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.gdx.jgame.gameObjects.missiles.Missile;
import com.gdx.jgame.jBox2D.BodiesToDestroy;

public class MissleListener implements ContactListener{
	private BodiesToDestroy m_destroy;
	
	public MissleListener(BodiesToDestroy destroy){
		m_destroy = destroy;
	}

	@Override
	public void beginContact(Contact contact) {
		Object dataA = contact.getFixtureA().getBody().getUserData();
		Object dataB = contact.getFixtureB().getBody().getUserData();
		Object object = null;
		Missile missile = null;	
		
		if(dataA instanceof Missile) {
			object = dataB;
			missile = (Missile) dataA;
			if(contact.getFixtureB().getBody().getFixtureList().first().isSensor()) return;
		}
		else if(dataB instanceof Missile) {
			object = dataA;
			missile = (Missile) dataB;
			if(contact.getFixtureA().getBody().getFixtureList().first().isSensor()) return;
		}
		
		if(missile != null) {
			missile.updateObject(object);
			if(missile.isRemovable()) m_destroy.add(missile.getBody());
		}
	}

	@Override
	public void endContact(Contact contact) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub
		
	}

}
