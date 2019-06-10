package com.gdx.jgame.logic.ai;

import java.io.Serializable;
import java.util.TreeMap;

import com.gdx.jgame.gameObjects.MovingObject;

public class AIManager implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 97896323903573628L;
	
	private TreeMap<Integer, AIAdapter> map;
	
	public AIManager() {
		map = new TreeMap<Integer, AIAdapter>();
	}
	
	public FollowerEntity createFollowerEntity(MovingObject behaviorOwner, MovingObject targer) {
		FollowerEntity tmp = new FollowerEntity(behaviorOwner, targer);
		map.put(tmp.ID, tmp);
		return tmp;
	}
	
	public void save() {
		for(AIAdapter object : map.values()) {
			object.save();
		}
	}
	
	public void load(TreeMap<Integer, Object> restoreOwner) {
		for(AIAdapter object : map.values()) {
			object.load(restoreOwner);
		}
	}
	
}
