package com.gdx.jgame.jBox2D.contactListeners;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.gdx.jgame.JGame;
import com.gdx.jgame.jBox2D.BodiesToDestroy;

public class ContactListenerSet implements ContactListener{
	private PlayerListener m_player;
	private MissleListener m_normalBullet;
	private EnemiesListener m_enemy;
	
	public ContactListenerSet(JGame game, BodiesToDestroy destroy){
		m_player = new PlayerListener(game, destroy);
		m_normalBullet = new MissleListener(destroy);
		m_enemy = new EnemiesListener(destroy);
	}

	@Override
	public void beginContact(Contact contact) {
		m_player.beginContact(contact);
		m_normalBullet.beginContact(contact);
		m_enemy.beginContact(contact);
	}

	@Override
	public void endContact(Contact contact) {
		m_player.endContact(contact);
		m_normalBullet.endContact(contact);
		m_enemy.endContact(contact);
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		m_player.preSolve(contact, oldManifold);
		m_normalBullet.preSolve(contact, oldManifold);
		m_enemy.preSolve(contact, oldManifold);
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		m_player.postSolve(contact, impulse);
		m_normalBullet.postSolve(contact, impulse);
		m_enemy.postSolve(contact, impulse);
	}
}
