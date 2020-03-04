package com.gdx.jgame.jBox2D.contactListeners;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.gdx.jgame.gameObjects.missiles.Missile;
import com.gdx.jgame.jBox2D.BodiesToDestroy;

public class MissileListener extends BasicListener{
	private BodiesToDestroy m_destroy;
	
	public MissileListener(BodiesToDestroy destroy){
		m_destroy = destroy;
	}

	@Override
	public void beginContact(Contact contact, boolean isFirstFixture) {
		super.beginContact(contact, isFirstFixture);
		
		Object object = null;
		Missile missile = null;	
		
		object = dataB.getObject();
		missile = (Missile) dataA.getObject();
		
		if(contact.getFixtureB().getBody().getFixtureList().first().isSensor()) return;
		
		if(missile != null) {
			missile.updateObject(object);
			if(missile.isRemovable() && !missile.isDeleted()) {
				m_destroy.add(missile.getBody());
				missile.setIsDeleted(true);
			}
		}
	}

	@Override
	public void endContact(Contact contact, boolean isFirstFixture) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold, boolean isFirstFixture) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse, boolean isFirstFixture) {
		// TODO Auto-generated method stub
		
	}

}
