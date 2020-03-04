package com.gdx.interpreter;

import java.util.*;

/**
 * It is recommended to hash the phrases that you want to find in file.
 *
 */
public class CommandInterpreter {
	
	private String command = null;
	private int actPos;
	private int beginIndex;
	private int maxBlacklistDistance;
	private int maxPhraseDistance;
	private BucketVector<String> container;
	private SortedSet<String> blacklist;
	
	public CommandInterpreter(){
		container = new BucketVector<String>();
		blacklist = new TreeSet<String>();
		maxBlacklistDistance = -1;
		maxPhraseDistance = -1;
	}
	
	/**
	 * Set command and reset pointers.
	 * @param command
	 */
	public void setCommand(String command) {
		this.command = new String(command);
		actPos = 0;
		beginIndex = 0;
	}
	
	/**
	 * Set how long does the analyzed string must be before 
	 * it is too long to be analyzed by the blacklist.
	 * <br>It is the best to set this as the longest string in blacklist.
	 * @param value - size of the string
	 */
	public void farReachingBlacklist(int value) {
		maxBlacklistDistance = value;
	}
	
	/**
	 * Set how long does the analyzed string must be before 
	 * it is too long to be analyzed by the phrase list.
	 * <br>It is the best to set this as the longest string in phrase list.
	 * @param value - size of the string
	 */
	public void farReachingPhrase(int value) {
		maxPhraseDistance = value;
	}
	
	/**
	 * Set strings that will be used as delimiter between words.
	 * The new strings will be added to the existing class object.
	 * @param strings
	 */
	public void setStringsToBlacklist(String ...strings) {		
		
		for (String string : strings) {
			
			if(blacklist.contains(string))  continue;
			else {
				blacklist.add(string);
			}
		}
	}
	
	/**
	 * Add bucket with name that will be used to store strings.
	 * @param name
	 */
	public void addBucket(String name) {
		container.add(name);
	}
	
	/**
	 * Check if bucket with this name exist.
	 * @param name
	 * @return True if exist. <br>False otherwise.
	 */
	public boolean bucketExist(String name) {
		return container.bucketExist(name);
	}
	
	/**
	 * Add phrase that will count as special string.
	 * <br> Note <br>
	 * Phrases can repeat. If so, it will hit at performance.
	 * @param bucket - name of the bucket
	 * @param strings - phrases to be added
	 * @return True if the bucket does not exist.
	 * <br>False if the operation was successful.
	 */
	public boolean addPhrase(String bucket, String ...strings) {
		/*
		 * This way it is easier to catch errors.
		 * Bucket must be defined before.
		 */
		if(!container.bucketExist(bucket)) return true;
		
		container.add(bucket, strings);
		
		return false;
	}
	
	/**
	 * Change name of the existing bucket.
	 * @return True if the operation was not successful. There was no bucket with this name.
	 * <br>False if the operation was successful.
	 */
	public boolean changeBucketName(String oldName, String newName) {
		return container.changeName(oldName, newName);
	}
	
	/**
	 * 
	 * @return True if the next strings are on blacklist or command pointer reached his end.
	 */
	public boolean isEnd() {
		blacklistPhrase(beginIndex);
		
		return actPos == command.length();
	}
	
	/**
	 * Find phrase that is defined in Interpreter class.
	 * @param baskets - where do you want to check for phrases.
	 * @return The phrase that was found or null if exceeded max phrase distance.
	 */
	public String getPhrase(String... baskets) {
		
		beginIndex = actPos; // forget previous function call
		int tmpBeginIndex = beginIndex;
		
		if(checkConditions()) return null;
		
		tmpBeginIndex = blacklistPhrase(tmpBeginIndex);
		
		return findPhrase(tmpBeginIndex, baskets);
	}
	
	/**
	 * Find phrase that is defined in Interpreter class.
	 * <br></br>
	 * This method do not change the pointer of the command.
	 * @param baskets - where do you want to check for phrases.
	 * @return The phrase that was found or null if exceeded max phrase distance.
	 */
	public String getPhraseNoTrace(String... baskets) {
		int tmpActPos = actPos;
		beginIndex = actPos; // forget previous function call
		int tmpBeginIndex = beginIndex;
		
		if(checkConditions()) return null;
		
		tmpBeginIndex = blacklistPhrase(tmpBeginIndex);
		
		String ret = findPhrase(tmpBeginIndex, baskets);
		actPos = tmpActPos; // forget this function call
		return ret;
	}
	
	/**
	 * Find any word that is not in blacklist.
	 * To catch a phrase it must starts with the string from blacklist.
	 * @param baskets - where do you want to check for phrases.
	 * @return Phrase or the word that is not on any list.
	 */
	public String getNext(String... baskets) {
		
		beginIndex = actPos; // forget previous function call
		int tmpBeginIndex = beginIndex;
		
		if(checkConditions()) return null;
		
		tmpBeginIndex = blacklistPhrase(tmpBeginIndex);
		// now it can be a phrase from the list or new, unknown string.
		
		return findAnyTrace(tmpBeginIndex, baskets);
	}
	
	/**
	 * Find any word that is not in blacklist.
	 * To catch a phrase it must starts with the string from blacklist.
	 * <br> This method do not change the pointer of the command.
	 * @param baskets - where do you want to check for phrases.
	 * @return Phrase or the word that is not on any list.
	 */
	public String getNextNoTrace(String... baskets) {
		int tmpActPos = actPos;
		beginIndex = actPos; // forget previous function call
		int tmpBeginIndex = beginIndex;
		
		if(checkConditions()) return null;
		
		tmpBeginIndex = blacklistPhrase(tmpBeginIndex);
		// now it can be a phrase from the list or new, unknown string.
		
		String ret = findAnyTrace(tmpBeginIndex, baskets);
		actPos = tmpActPos; // forget this function call
		return ret;
	}

	/**
	 * @param tmpBeginIndex - where to starts to search
	 * @param baskets - where do you want to check for phrases. 
	 * @return String that is on phrase list or unknown string from command.
	 * It ignore the the strings from blacklist at the beginning.
	 */
	private String findAnyTrace(int tmpBeginIndex, String... baskets) {
		int searchAreaBlacklistStart = tmpBeginIndex;
		String ret = null;
		boolean beginConditionBlacklist = false;
		boolean beginConditionPhrase = false;
		
		for (; actPos < command.length(); ++actPos) {
			// it can be also a phrase
			String potentialPhrase = command.substring(tmpBeginIndex, actPos);
			
			// we must find where it ends
			String potentialBlacklist = command.substring(searchAreaBlacklistStart, actPos);
			
			if(blacklist.contains(potentialBlacklist)) {
				actPos = searchAreaBlacklistStart;
				ret = command.substring(tmpBeginIndex, actPos);
				break;
			}
			else if(beginConditionBlacklist) {
				++searchAreaBlacklistStart;
			}
			else if(maxBlacklistDistance < actPos - searchAreaBlacklistStart) {
				beginConditionBlacklist = true;
				++searchAreaBlacklistStart;
			}
			
			if(!beginConditionPhrase && container.find(potentialPhrase, baskets)) {
				actPos = tmpBeginIndex;
				ret = potentialPhrase;
				break;
			}
			else if(maxPhraseDistance < actPos - tmpBeginIndex) {
				beginConditionPhrase = true;
			}
		}
		
		// if it is the end of the command
		if(ret == null) ret = command.substring(tmpBeginIndex, actPos);
		
		return ret;
	}

	/**
	 * Find the phrase that is on phrases list.
	 * <br></br>
	 * It is assumed that some phrase starts with the same character 
	 * as the first character of the tmpBeginIndex in command.
	 * @param tmpBeginIndex - where to starts to search
	 * @param baskets - where do you want to check for phrases. 
	 * @return The found phrase or null if not found any matching phrase.
	 */
	private String findPhrase(int tmpBeginIndex, String... baskets) {
		int length = 0;
		
		// find phrase		
		for (; actPos < command.length(); ++actPos) {
			String potentialPhrase = command.substring(tmpBeginIndex, actPos);
			if(container.find(potentialPhrase, baskets)) return potentialPhrase;			
			else if(maxPhraseDistance < ++length) return null;			
		}
		
		return null;
	}
	/**
	 * Ignore all phrases that are on blacklist.
	 * Set the actPos pointer to the first character that may be useful.
	 * <br></br>
	 * It is assumed that some phrase starts with the same character 
	 * as the first character of the tmpBeginIndex in command.
	 * @param tmpBeginIndex - where to starts to search
	 * @return The index of the end of the phrase.
	 */
	private int blacklistPhrase(int tmpBeginIndex) {
		int blacklistValue = 0;
		
		// ignore characters at the beginning
		for (; actPos < command.length(); ++actPos) {
			String potentialPhrase = command.substring(tmpBeginIndex, actPos);
			if(blacklist.contains(potentialPhrase)) tmpBeginIndex = actPos;
			else if(maxBlacklistDistance < ++blacklistValue) break;
		}
		actPos = tmpBeginIndex;	// get back
		return tmpBeginIndex;
	}

	private boolean checkConditions() {
		if(command == null || actPos == command.length() || maxBlacklistDistance == -1 || maxPhraseDistance == -1) return true;
		return false;
	}
}
