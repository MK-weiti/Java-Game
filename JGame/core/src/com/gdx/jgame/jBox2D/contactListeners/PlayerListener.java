package com.gdx.jgame.jBox2D.contactListeners;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.gdx.jgame.JGame;
import com.gdx.jgame.gameObjects.characters.Player;
import com.gdx.jgame.jBox2D.BodiesToDestroy;
import com.gdx.jgame.jBox2D.mapObjects.*;

public class PlayerListener extends BasicListener{
	private JGame m_jgame;
	private int m_fieldBoostId;
	private int m_fieldMaxVelocityId;
	private BodiesToDestroy m_destroy;
	
	public PlayerListener(JGame jgame, BodiesToDestroy destroy) {
		m_jgame = jgame;
		m_destroy = destroy;
	}
	
	@Override
	public void beginContact(Contact contact, boolean isFirstFixture) {
		super.beginContact(contact, isFirstFixture);
		
		Object object = dataB.getObject();
		Player player = (Player) dataA.getObject();	
		Object field = dataB.getFieldGameState();
		
		if(player == null) return;

		m_fieldBoostId = player.generateAccelerationId();
		m_fieldMaxVelocityId = player.generateMaxVelocityId();
		
		if(field != null) {
			
			if(field instanceof FieldGameState) {
				if(((FieldGameState) field).win){
					m_jgame.setGameLevelState(JGame.GAME_LEVEL_STATE.WIN);
				}
				else
				if(((FieldGameState) field).lose){
					m_jgame.setGameLevelState(JGame.GAME_LEVEL_STATE.LOSE);
				}
			}
			if(field instanceof FieldVelocityChange) {
				player.addRatioAcceleration(m_fieldBoostId, ((FieldVelocityChange) object).speedChange);
				player.addRatioMaxLinearSpeed(m_fieldMaxVelocityId, ((FieldVelocityChange) object).maxSpeedChange);
			}
		}
		else if(object != null){
			player.updateObject(object);
			if(player.isRemovable() && !player.isDeleted()) {
				m_destroy.add(player.getBody());
				player.setIsDeleted(true);
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
