package com.gdx.jgame.jBox2D.contactListeners;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.gdx.jgame.JGame;
import com.gdx.jgame.gameObjects.characters.Player;
import com.gdx.jgame.jBox2D.BodiesToDestroy;
import com.gdx.jgame.jBox2D.mapObjects.*;

public class PlayerListener implements ContactListener{
	private JGame m_jgame;
	private int m_fieldBoostId;
	private int m_fieldMaxVelocityId;
	private BodiesToDestroy m_destroy;
	
	public PlayerListener(JGame jgame, BodiesToDestroy destroy) {
		m_jgame = jgame;
		m_destroy = destroy;
	}
	
	@Override
	public void beginContact(Contact contact) {
		Object dataA = contact.getFixtureA().getBody().getUserData();
		Object dataB = contact.getFixtureB().getBody().getUserData();
		Object object = null;
		Player player = null;		
		
		if(dataA instanceof Player) {
			object = dataB;
			player = (Player) dataA;
		}
		else if(dataB instanceof Player) {
			object = dataA;
			player = (Player) dataB;
		}
		
		if(object == null || player == null) return;
		
		m_fieldBoostId = player.generateAccelerationId();
		m_fieldMaxVelocityId = player.generateMaxVelocityId();
		
		if(object instanceof Field) {
			
			if(object instanceof FieldGameState) {
				if(((FieldGameState) object).win){
					m_jgame.setGameLevelState(JGame.GAME_LEVEL_STATE.WIN);
				}
				else
				if(((FieldGameState) object).lose){
					m_jgame.setGameLevelState(JGame.GAME_LEVEL_STATE.LOSE);
				}
			}
			if(object instanceof FieldVelocityChange) {
				player.addRatioAcceleration(m_fieldBoostId, ((FieldVelocityChange) object).speedChange);
				player.addRatioMaxVelocity(m_fieldMaxVelocityId, ((FieldVelocityChange) object).maxSpeedChange);
			}
		}
		else {
			player.updateObject(object);
			if(player.isRemovable()) m_destroy.add(player.getBody());
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
