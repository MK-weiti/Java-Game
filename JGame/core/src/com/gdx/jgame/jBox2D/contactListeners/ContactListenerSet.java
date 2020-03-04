package com.gdx.jgame.jBox2D.contactListeners;

import java.util.HashMap;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.gdx.jgame.JGame;
import com.gdx.jgame.gameObjects.ObjectTypes;
import com.gdx.jgame.gameObjects.UserData;
import com.gdx.jgame.jBox2D.BodiesToDestroy;

public class ContactListenerSet implements ContactListener{
	private HashMap<ObjectTypes, MyContactListener> map;
	
	private PlayerListener m_player;
	private MissileListener m_normalBullet;
	private EnemiesListener m_enemy;
	
	public ContactListenerSet(JGame game, BodiesToDestroy destroy){
		map = new HashMap<ObjectTypes, MyContactListener>();
		setupListeners(game, destroy);
	}

	@Override
	public void beginContact(Contact contact) {
		MyContactListener listener;
		ObjectTypes tmp;
		
		UserData data = (UserData) contact.getFixtureA().getBody().getUserData();
		if(data != null && (tmp = data.getClassType()) != null && (listener = map.get(tmp)) != null) 
			listener.beginContact(contact, true);		
		
		data = (UserData) contact.getFixtureB().getBody().getUserData();
		if(data != null && (tmp = data.getClassType()) != null && (listener = map.get(tmp)) != null)
			listener.beginContact(contact, false);
	}

	@Override
	public void endContact(Contact contact) {
		MyContactListener listener;
		ObjectTypes tmp;
		
		UserData data = (UserData) contact.getFixtureA().getBody().getUserData();
		if(data != null && (tmp = data.getClassType()) != null && (listener = map.get(tmp)) != null) 
			listener.endContact(contact, true);		
		
		data = (UserData) contact.getFixtureB().getBody().getUserData();
		if(data != null && (tmp = data.getClassType()) != null && (listener = map.get(tmp)) != null)
			listener.endContact(contact, false);
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		MyContactListener listener = null;
		ObjectTypes tmp;
		
		UserData data = (UserData) contact.getFixtureA().getBody().getUserData();
		if(data != null && (tmp = data.getClassType()) != null && (listener = map.get(tmp)) != null) 
			listener.preSolve(contact, oldManifold, true);
		
		data = (UserData) contact.getFixtureB().getBody().getUserData();
		if(data != null && (tmp = data.getClassType()) != null && (listener = map.get(tmp)) != null)
			listener.preSolve(contact, oldManifold, false);
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		MyContactListener listener;
		ObjectTypes tmp;
		
		UserData data = (UserData) contact.getFixtureA().getBody().getUserData();
		if(data != null && (tmp = data.getClassType()) != null && (listener = map.get(tmp)) != null) 
			listener.postSolve(contact, impulse, true);
		
		data = (UserData) contact.getFixtureB().getBody().getUserData();
		if(data != null && (tmp = data.getClassType()) != null && (listener = map.get(tmp)) != null)
			listener.postSolve(contact, impulse, false);
	}
	
	public void addListener(ObjectTypes type, MyContactListener listener) {
		map.put(type, listener);
	}
	
	public void removeListener(ObjectTypes type) {
		map.remove(type);
	}
	
	private void setupListeners(JGame game, BodiesToDestroy destroy) {
		m_player = new PlayerListener(game, destroy);
		m_normalBullet = new MissileListener(destroy);
		m_enemy = new EnemiesListener(destroy);
		
		map.put(ObjectTypes.Player, m_player);
		map.put(ObjectTypes.Missile, m_normalBullet);
		map.put(ObjectTypes.Enemy, m_enemy);
	}
}
