package com.gdx.jgame;

import java.awt.IllegalComponentStateException;
import java.io.Serializable;
import java.util.Map;

public class SaveReference<T extends IDAdapter> implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3329893085592199538L;
	
	private transient T owner = null;
	private int objectOwnerHashCode = -1;
	private int ID = -1;
	
	public SaveReference() {} //default
	
	public SaveReference(T object) {
		owner = object;
		this.ID = object.ID;
		save();
	}
	
	public SaveReference(SaveReference<T> saveReference){
		this.owner = saveReference.owner;
		this.objectOwnerHashCode = saveReference.objectOwnerHashCode;
		this.ID = saveReference.ID;
	}
	
	public void load(Map<Integer, IDAdapter> restoreOwner) {
		if(objectOwnerHashCode == -1) throw new IllegalComponentStateException("No owner hashCode.");
		owner = (T) restoreOwner.get(ID);
		
		if(owner != null) {
			this.ID = owner.ID;
		}
			
	}
	
	public void save() {
		if(owner == null) throw new IllegalComponentStateException("No owner.");
		this.objectOwnerHashCode = owner.hashCode();
		
		if(owner != null) {
			this.ID = owner.ID;
		}
	}

	
	public int getOwnerID() {
		return ID;
	}
	
	public T getOwner() {
		return owner;
	}
	
	public boolean isNull() {
		return owner == null;
	}
	
	public void setOwner(T owner) {
		this.owner = owner;
		this.ID = owner.ID;
	}
	
	public int getSavedHashCode() {
		return objectOwnerHashCode;
	}
	
	public void setAndSave(T owner) {
		setOwner(owner);
		save();
		this.ID = owner.ID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ID;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SaveReference<?> other = (SaveReference<?>) obj;
		if (ID != other.ID)
			return false;
		return true;
	}
	
}
