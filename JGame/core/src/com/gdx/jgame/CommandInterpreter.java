package com.gdx.jgame;

import java.util.Vector;

public class CommandInterpreter {
	private String m_command = null;
	private int m_actPos;
	private int beginIndex;
	private Vector<Character> m_constraints;
	private Vector<String> m_stringConstraints;
	private Vector<String> m_constantExpression;
	
	private static final String strue = "true", sfalse = "false";
	
	public CommandInterpreter() {
		m_constraints = new Vector<Character>(' ');
		m_stringConstraints = new Vector<String>();
		m_constantExpression = new Vector<String>();
	}
	
	/**
	 * Set characters that will be used as delimiter between words.
	 * The new characters will be added to the existing class object.
	 * @param characters - chars wrapped as objects
	 */
	public void setConstraintsChar(Character ...characters) {
		boolean br;
		
		for (Character character : characters) {
			br = false;
			
			for(Character existingCharacter : m_constraints) {
				if(existingCharacter.equals(character)) {
					br = true;
					break;
				}
			}
			if(br) continue;
			m_constraints.add(character);
		}
	}
	
	/**
	 * Set strings that will be used as delimiter between words.
	 * The new strings will be added to the existing class object.
	 * @param strings
	 */
	public void setConstraintsString(String ...strings) {
		boolean br;
		
		for (String string : strings) {
			br = false;
			
			for(String existingString : m_stringConstraints) {
				if(existingString.equals(string)) {
					br = true;
					break;
				}
			}
			if(br) continue;
			m_stringConstraints.add(string);
		}
	}
	
	/**
	 * Set strings that will be used as delimiter between words.
	 * The new strings will be added to the existing class object.
	 * @param strings
	 */
	public void setConstantExpression(String ...strings) {
		boolean br;
		
		for (String string : strings) {
			br = false;
			
			for(String existingString : m_constantExpression) {
				if(existingString.equals(string)) {
					br = true;
					break;
				}
			}
			if(br) continue;
			m_constantExpression.add(string);
		}
	}
	
	/**
	 * 
	 * @return String that is a next command
	 */
	public String getNext() {
		return getNextPrivate();
	}
	
	public String getNextNoTrace() {
		String tmp = getNextPrivate();
		m_actPos = beginIndex;		
		return tmp;
	}

	private String getNextPrivate() {
		boolean found = true;
		beginIndex = m_actPos;
		
		if(m_command == null) return null;
		if(m_actPos == m_command.length()) return null;
		
		found = ignoreConstraints(found);
		
		for (; m_actPos < m_command.length(); ++m_actPos) {
			found = findConstraint(found);
			
			if(found) {
				return m_command.substring(beginIndex, m_actPos);
			}
		}
		
		if(beginIndex == m_actPos) return null;
		return m_command.substring(beginIndex, m_actPos);
	}
	
	/**
	 * 
	 * @return code of the constant expression.
	 */
	public int findConstantExpression() {
		int index = -1;
		beginIndex = m_actPos;
		
		if(m_command == null) return -1;
		if(m_actPos == m_command.length()) return -1;
		
		for (; m_actPos < m_command.length(); ++m_actPos) {
			for(int i = 0; i < m_constantExpression.size(); ++i) {
				String tmp = m_command.substring(beginIndex, m_actPos);
				
				if(tmp.contains(m_constantExpression.get(i))) {
					index = i;
					break;
				}
			}
		}
		return index;
	}

	private boolean findConstraint(boolean found) {
		for (Character character : m_constraints) {
			if(m_command.charAt(m_actPos) == character) {
				found = true;
				break;
			}
		}
		
		if(!found) 
			for(String string : m_stringConstraints) {
				String tmp = m_command.substring(beginIndex, m_actPos);
				if(tmp.contains(string)) {
					found = true;
					break;
				}
			}
		return found;
	}

	private boolean ignoreConstraints(boolean found) {
		for (; m_actPos < m_command.length(); ++m_actPos) {
			
			found = true;
			found = !findConstraint(found);
			
			if(found) {
				beginIndex = m_actPos;
				found = false;
				break;
			}
		}
		return found;
	}
	
	
	/**
	 * Deleting things can change codes.
	 * @param string - string for which you want to find the code.
	 * @return Code of the string.
	 */
	public int getCode(String string) {
		for(int i = 0; i < m_stringConstraints.size(); ++i) {
			if(m_stringConstraints.get(i).equals(string)) return i;
		}
		return -1;
	}
	
	/**
	 * Deleting things can change codes.
	 * @param character - character for which you want to find the code.
	 * @return Code of the character.
	 */
	public int getCode(Character character) {
		for(int i = 0; i < m_constraints.size(); ++i) {
			if(m_constraints.get(i).equals(character)) return i;
		}
		return -1;
	}
	
	/**
	 * Delete all constraints and commands.
	 * Sets pointer at the beginning.
	 */
	public void reset() {
		m_actPos = 0;
		m_command = null;
		m_constraints.clear();
		m_stringConstraints.clear();
		m_constraints.add(' ');
	}
	
	public String getIncrementedString(int number) {
		if(m_actPos == m_command.length() || m_actPos + number >= m_command.length()) return null;
		m_actPos += number;
		return m_command.substring(beginIndex, m_actPos);
	}
	
	/**
	 * 
	 * @return Return next char from command.
	 */
	public Character getNextChar() {
		if(m_actPos == m_command.length()) return null;
		beginIndex = m_actPos;
		++m_actPos;
		return m_command.charAt(beginIndex);
	}
	
	
	/**
	 * Check if pointer is at the end of the command.
	 * 
	 * @return true if it reached the end of the command.
	 */
	public boolean isEnd() {
		boolean found = true;
		found = ignoreConstraints(found);
		
		if(m_command != null && m_actPos == m_command.length()) return true;
		return false;
	}
	
	/**
	 * @param command - that will be processed. Sets pointer at the beginning.
	 */
	public void setCommand(String command) {
		m_command = new String(command);
		m_actPos = 0;
		beginIndex = 0;
	}
	
	/**
	 * @return return current command.
	 */
	public String getCommand() {
		return new String(m_command);
	}

	/**
	 * 
	 * @return String "true".
	 */
	public String True() {
		return strue;
	}

	/**
	 * 
	 * @return String "false".
	 */
	public String False() {
		return sfalse;
	}
	
	
}
