package com.gdx.jgame.jBox2D.contactListeners;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.gdx.jgame.gameObjects.UserData;

public abstract class BasicListener implements MyContactListener{
	protected UserData dataA, dataB;
	
	@Override
	public void beginContact(Contact contact, boolean isFirstFixture) {
		setObjects(contact, isFirstFixture);
	}
	
	@Override
	public void endContact(Contact contact, boolean isFirstFixture) {
		//TODO
	}
	
	@Override
	public void preSolve(Contact contact, Manifold oldManifold, boolean isFirstFixture) {
		//TODO
	}
	
	@Override
	public void postSolve(Contact contact, ContactImpulse impulse, boolean isFirstFixture) {
		//TODO
	}
	
	private void setObjects(Contact contact, boolean isFirstFixture) {
		if(isFirstFixture) {
			dataA = (UserData) contact.getFixtureA().getBody().getUserData();
			dataB = (UserData) contact.getFixtureB().getBody().getUserData();
		}
		else {
			dataB = (UserData) contact.getFixtureA().getBody().getUserData();
			dataA = (UserData) contact.getFixtureB().getBody().getUserData();
		}
	}
}
