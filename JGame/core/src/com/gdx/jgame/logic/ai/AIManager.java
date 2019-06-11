package com.gdx.jgame.logic.ai;

import java.io.Serializable;
import java.util.TreeMap;

import com.gdx.jgame.IDAdapter;
import com.gdx.jgame.IDAdapterSerializable;
import com.gdx.jgame.gameObjects.MovingObject;

public class AIManager implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 97896323903573628L;
	
	private TreeMap<Integer, IDAdapterSerializable> map;
	
	public AIManager() {
		if(map == null) {
			map = new TreeMap<Integer, IDAdapterSerializable>();
		}
		
	}
	
	public FollowerEntity createFollowerEntity(MovingObject behaviorOwner, MovingObject targer) {
		FollowerEntity tmp = new FollowerEntity(behaviorOwner, targer);
		map.put(tmp.ID, tmp);
		return tmp;
	}
	
	public void save() {
		for(IDAdapterSerializable object : map.values()) {
			object.save();
		}
	}
	
	public void load(TreeMap<Integer, IDAdapter> restoreOwner) {
		TreeMap<Integer, IDAdapterSerializable> tmp = new TreeMap<Integer, IDAdapterSerializable>();
		for(IDAdapterSerializable object : map.values()) {
			object.load(restoreOwner);
			tmp.put(object.ID, object);
			
		}
		map = tmp;
	}
	
}
