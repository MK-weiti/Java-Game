package com.gdx.jgame.gameObjects;

import com.gdx.jgame.jBox2D.mapObjects.FieldGameState;

public final class UserData {
	private Object object;
	private ObjectTypes classType;
	private String name;
	private FieldGameState fieldGameState;
	
	public UserData() {
		object = null;
		name = null;
		classType = null;
	}
	
	public UserData(Object referenceToObject) {
		object = referenceToObject;
		name = null;
		classType = null;
	}
	
	public Object getObject() {
		return object;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setClassType(ObjectTypes type) {
		this.classType = type;
	}
	
	public ObjectTypes getClassType() {
		return classType;
	}

	public FieldGameState getFieldGameState() {
		return fieldGameState;
	}

	public void setFieldGameState(FieldGameState fieldGameState) {
		this.fieldGameState = fieldGameState;
	}
	
}
