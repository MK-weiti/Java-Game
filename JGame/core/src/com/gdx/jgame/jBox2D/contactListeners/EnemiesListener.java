package com.gdx.jgame.jBox2D.contactListeners;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.gdx.jgame.gameObjects.characters.BasicEnemy;
import com.gdx.jgame.gameObjects.characters.PlainCharacter;
import com.gdx.jgame.gameObjects.missiles.NormalBullet;
import com.gdx.jgame.jBox2D.BodiesToDestroy;

public class EnemiesListener implements ContactListener{
	private BodiesToDestroy m_destroy;

	public EnemiesListener(BodiesToDestroy destroy){
		m_destroy = destroy;
	}
	
	@Override
	public void beginContact(Contact contact) {
		Object dataA = contact.getFixtureA().getBody().getUserData();
		Object dataB = contact.getFixtureB().getBody().getUserData();
		Object object = null;
		PlainCharacter enemy = null;	
		
		if(dataA instanceof PlainCharacter) {
			object = dataB;
			enemy = (PlainCharacter) dataA;
		}
		else if(dataB instanceof PlainCharacter) {
			object = dataA;
			enemy = (PlainCharacter) dataB;
		}
		
		if(object == null || enemy == null) return;
		
		enemy.updateObject(object);
		if(enemy.isRemovable()) m_destroy.add(enemy.getBody());	
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
