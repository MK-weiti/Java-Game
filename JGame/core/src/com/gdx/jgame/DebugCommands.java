package com.gdx.jgame;

import java.util.Scanner;
import java.util.concurrent.Semaphore;

import org.apache.commons.lang3.*;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.gdx.jgame.gameObjects.Methods;
import com.gdx.jgame.gameObjects.characters.CharacterPolygonDef;

public class DebugCommands extends Thread{
	private JGame m_jgame;
	private Semaphore m_sem;
	
	private Scanner m_scan;
	private String m_command;
	private CommandInterpreter m_interpr;
	
	public DebugCommands(JGame game) {
		super();
		m_jgame = game;
		m_sem = game.m_sem;
		m_interpr = new CommandInterpreter();
	}
	
	@Override
	public void run() {
		m_scan = new Scanner(System.in);
		
		/*
		 * if you pause the game then it will storage the commands
		 * and after the resume of the game it will do them
		 */
		while(true) {
			if(m_scan.hasNextLine() && m_jgame.gameState == JGame.GAME_STATE.RUN) {
				mainLoop();
			}
			
		}
	}
	
	private void mainLoop() {
		m_command = m_scan.nextLine();
		m_interpr.setCommand(m_command);
		m_interpr.setConstraintsChar(' ', '/', '\\', '=');
		
		if(m_command.isEmpty()) return;
		
		String firstCommand = m_interpr.getNext();
		if(firstCommand == null) return;
		
		if(isStringCreateEnemy(firstCommand)) {
			m_sem.acquireUninterruptibly();
			if(stringCreateEnemy()) {
				System.out.println("Operation failed.");
			}
			m_sem.release();
		}
		else {
			System.out.println("Unknown command.");
		}
		
			
	}
	
	
	private boolean isStringCreateEnemy(String firstCommand) {
		String commandName = "create-enemy";
		
		if(firstCommand.equals(commandName)) {
			return true;
		}
		return false;
	}
	
	private boolean stringCreateEnemy() {
		
		// texture id, scale, other (for example active=true position(500,200) friction=0.2)
		// create-enemy texture=honeybee.png scale=0.05 position=500 250 groupName=test_group
		
		String textureName = "texture", textureRes = null;
		String scaleName = "scale", scaleRes = null;
		float scale = 0;
		String positionName = "position", positionRes;
		Vector2 pos = new Vector2();
		String groupName = "groupName", groupRes = null;
		String result;
		
		int required = 0;
		
		while ((result = m_interpr.getNext()) != null){
			if(result.contentEquals(textureName)) {
				if(m_interpr.getNextChar() != '=') return true;
				textureRes = m_interpr.getNext();
				++required;
			}
			else
			if(result.contentEquals(scaleName)) {
				if(m_interpr.getNextChar() != '=') return true;
				scaleRes = m_interpr.getNext();
				scale = Float.parseFloat(scaleRes);
				++required;
			}
			else
			if(result.contentEquals(positionName)) {
				if(m_interpr.getNextChar() != '=') return true;
				positionRes = m_interpr.getNext();
				pos.x = Float.parseFloat(positionRes);
				positionRes = m_interpr.getNext();
				pos.y = Float.parseFloat(positionRes);
				++required;
			}
			else
			if(result.contentEquals(groupName)) {
				if(m_interpr.getNextChar() != '=') return true;
				groupRes = m_interpr.getNext();
				++required;
			}
		}
		
		if(required != 4) return true;
		
		Texture texture = m_jgame.m_charactersTextures.get(textureRes);
		
		CharacterPolygonDef charDef = new CharacterPolygonDef(m_jgame.m_jBox.world, m_jgame.m_charactersTextures, 
				textureRes, scale, groupRes, Methods.setVerticesToTexture(texture, scale), 
				CharacterPolygonDef.CharType.Enemy);
		
		charDef.bodyDef.type = BodyType.DynamicBody;
		charDef.bodyDef.active = true;
		charDef.bodyDef.allowSleep = false;
		charDef.bodyDef.fixedRotation = false;
		charDef.fixtureDef.friction = 0.2f;
		charDef.textureScale = scale;
		charDef.setPosition(pos.x, pos.y);
		
		m_jgame.m_characters.addEnemies(charDef);
		
		return false;
	}
	
}
