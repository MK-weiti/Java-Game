package com.gdx.jgame;

import java.util.Vector;

public class CommandInterpreter {
	private String m_command = null;
	private int m_actPos;
	private int beginIndex;
	private Vector<Character> m_constraints;
	private static final String strue = "true", sfalse = "false";
	
	public CommandInterpreter() {
		m_constraints = new Vector<Character>(' ');
	}
	
	public void setConstraintsChar(Character ...characters) {
		for (Character character : characters) {
			if(!m_constraints.contains(character)) m_constraints.add(character);
		}
	}
	
	public String getNext() {
		boolean found = true;
		beginIndex = m_actPos;
		
		if(m_command == null) return null;
		if(m_actPos == m_command.length()) return null;
		
		found = ignoreConstraints(found);
		
		for (; m_actPos < m_command.length(); ++m_actPos) {
			for (Character character : m_constraints) {
				if(m_command.charAt(m_actPos) == character) {
					found = true;
					break;
				}
			}
			if(found) {
				return m_command.substring(beginIndex, m_actPos);
			}
		}
		
		if(beginIndex == m_actPos) return null;
		return m_command.substring(beginIndex, m_actPos);
	}

	private boolean ignoreConstraints(boolean found) {
		for (; m_actPos < m_command.length(); ++m_actPos) {
			found = true;
			for (Character character : m_constraints) {
				if(m_command.charAt(m_actPos) == character) {
					found = false;
					break;
				}
			}
			if(found) {
				beginIndex = m_actPos;
				found = false;
				break;
			}
		}
		return found;
	}
	
	public void reset() {
		m_actPos = 0;
		m_command = null;
		m_constraints.clear();
		m_constraints.add(' ');
	}
	
	public String getIncrementedString() {
		if(m_actPos == m_command.length()) return null;
		++m_actPos;
		return m_command.substring(beginIndex, m_actPos);
	}
	
	public Character getNextChar() {
		if(m_actPos == m_command.length()) return null;
		beginIndex = m_actPos;
		++m_actPos;
		return m_command.charAt(beginIndex);
	}
	
	public boolean isEnd() {
		boolean found = true;
		found = ignoreConstraints(found);
		
		if(m_command != null && m_actPos == m_command.length()) return true;
		return false;
	}
	
	public void setCommand(String command) {
		m_command = new String(command);
		m_actPos = 0;
		beginIndex = 0;
	}
	
	public String getCommand() {
		return new String(m_command);
	}

	public String True() {
		return strue;
	}

	public String False() {
		return sfalse;
	}
	
	
}
