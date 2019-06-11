package com.gdx.jgame;

import java.util.Scanner;
import java.util.concurrent.Semaphore;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.gdx.jgame.gameObjects.DefaultDef;
import com.gdx.jgame.gameObjects.characters.BasicEnemyDef;
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
		m_sem = game.getRenderSemaphore();
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
		
		if(isCreateEnemy(firstCommand)) {
			executeCreateEnemy();
		}
		else if(isSetDebugLines(firstCommand)) {
			executeSetDebugLines();
		}
		else {
			System.out.println("Unknown command.");
		}
		
			
	}

	private void executeSetDebugLines() {
		m_sem.acquireUninterruptibly();
		if(setDebugLines()) {
			System.out.println("Set debug lines failed.");
		}
		m_sem.release();
	}

	private void executeCreateEnemy() {
		m_sem.acquireUninterruptibly();
		try {
			if(createEnemy()) {
				System.out.println("Create enemy failed.");
			}
		}
		catch(IllegalArgumentException e) {
			System.out.println("Create enemy failed.");
		}
		
		m_sem.release();
	}
	
	private boolean isCreateEnemy(String firstCommand) {
		String commandName = "create-enemy";
		
		if(firstCommand.equals(commandName)) {
			return true;
		}
		return false;
	}
	
	private boolean isSetDebugLines(String firstCommand) {
		String commandName = "debug-lines";
		
		if(firstCommand.equals(commandName)) {
			return true;
		}
		return false;
	}
	
	private boolean setDebugLines() {
		// commands example
		// debug-lines jbox=true 
		// debug-lines jbox=false hud=true menu=true
		
		String tmp;
		String lines = "hud", jbox = "jbox", menu = "menu";
		boolean changeLines = false, changeJbox = false, changeMenu = false;
		boolean setLines = false, setJbox = false, setMenu = false;
		
		while(!m_interpr.isEnd()) {
			tmp = m_interpr.getNext();
			
			if(tmp.contentEquals(lines)) { 
				if(m_interpr.getNextChar() != '=') return true;
				
				tmp = m_interpr.getNext();
				if(tmp.contentEquals(m_interpr.True())) {
					changeLines = true;
					setLines = true;
				}
				else if(tmp.contentEquals(m_interpr.False())) {
					changeLines = true;
					setLines = false;
				}
				else return true;
			}
			else if(tmp.contentEquals(jbox)) {
				if(m_interpr.getNextChar() != '=') return true;
				
				tmp = m_interpr.getNext();
				if(tmp.contentEquals(m_interpr.True())) {
					changeJbox = true;
					setJbox = true;
				}
				else if(tmp.contentEquals(m_interpr.False())) {
					changeJbox = true;
					setJbox = false;
				}
				else return true;
			}
			else if(tmp.contentEquals(menu)) {
				if(m_interpr.getNextChar() != '=') return true;
				
				tmp = m_interpr.getNext();
				if(tmp.contentEquals(m_interpr.True())) {
					changeMenu = true;
					setMenu = true;
				}
				else if(tmp.contentEquals(m_interpr.False())) {
					changeMenu = true;
					setMenu = false;
				}
				else return true;
			}
			else return true;
		}
		
		if(changeLines) m_jgame.getHud().setLayoutLines(setLines);
		if(changeJbox) m_jgame.getJBox().setShowLayout(setJbox);
		if(changeMenu) m_jgame.getPauseMenu().setLayoutLines(setMenu);
		return false;
	}
	
	private boolean createEnemy() {
		
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
		
		Texture texture = m_jgame.getCharactersTextures().get(textureRes);
		
		BasicEnemyDef charDef = new BasicEnemyDef(m_jgame.getJBox().getWorld(), 
				m_jgame.getCharactersTextures(), m_jgame.getBulletsTextures(), m_jgame.getMisslesManager(), 
				textureRes, scale, groupRes, Utils.setVerticesToTexture(texture, scale));
		
		DefaultDef.defBasicEnemy(charDef);
		
		charDef.bodyDef.type = BodyType.DynamicBody;
		charDef.bodyDef.active = true;
		charDef.bodyDef.allowSleep = false;
		charDef.bodyDef.fixedRotation = false;
		charDef.fixtureDef.friction = 0.2f;
		charDef.textureScale = scale;
		charDef.setPosition(pos.x, pos.y);
		
		m_jgame.getCharacters().addEnemies(charDef);
		
		return false;
	}
	
}
