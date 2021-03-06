package com.gdx.jgame.jBox2D.contactListeners;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.gdx.jgame.gameObjects.characters.PlainCharacter;
import com.gdx.jgame.jBox2D.BodiesToDestroy;

public class EnemiesListener extends BasicListener{
	private BodiesToDestroy m_destroy;

	public EnemiesListener(BodiesToDestroy destroy){
		m_destroy = destroy;
	}
	
	@Override
	public void beginContact(Contact contact, boolean isFirstFixture) {
		super.beginContact(contact, isFirstFixture);
		
		Object object = null;
		PlainCharacter enemy = null;	
		
		object = dataB.getObject();
		enemy = (PlainCharacter) dataA.getObject();
		
		if(object == null || enemy == null) return;
		
		enemy.updateObject(object);
		if(enemy.isRemovable() && !enemy.isDeleted()) {
			m_destroy.add(enemy.getBody());	
			enemy.setIsDeleted(true);
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
