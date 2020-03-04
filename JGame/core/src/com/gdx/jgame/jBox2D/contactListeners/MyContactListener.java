package com.gdx.jgame.jBox2D.contactListeners;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Manifold;

public interface MyContactListener {
	public void beginContact(Contact contact, boolean isFirstFixture);
	
	public void endContact(Contact contact, boolean isFirstFixture);
	
	public void preSolve(Contact contact, Manifold oldManifold, boolean isFirstFixture);
	
	public void postSolve(Contact contact, ContactImpulse impulse, boolean isFirstFixture);
}
