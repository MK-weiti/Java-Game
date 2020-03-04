package com.gdx.interpreter;

import java.util.*;

public class BucketVector<T> {
	
	private class MyVector{
		private Vector<T> vector;
		private String name;		
		
		public MyVector(String name){
			init(name);
		}
		
		public MyVector(String name, T value){
			init(name);
			add(value);
		}
		
		public MyVector(String name, T values[]){
			init(name);
			add(values);
		}
		
		private void init(String name) {
			vector = new Vector<T>();
			this.name = new String(name);
		}
		
		public Collection<T> getVector(){
			return vector;
		}
		
		public void add(T value) {
			vector.add(value);
		}
		
		public void add(T values[]) {
			for(T it : vector) {
				vector.add(it);
			}
		}
		
		public boolean find(T value) {
			for(T it : vector) {
				if(it.equals(value)) return true;
			}
			return false;
		}
		
		public void changeName(String newName) {
			name = new String(newName);
		}
		
		public String getName() {
			return name;
		}
	}
	
	private HashMap<Integer, MyVector> priority;
	
	public BucketVector(){
		priority = new HashMap<Integer, MyVector>();
	}
	
	/**
	 * This method do not guarantee the uniqueness of the elements in the bucket. 
	 * @param bucket - name of the bucket
	 * @param valueToPut
	 */
	public void add(String bucket, T valueToPut) {
		int hash;
		BucketVector<T>.MyVector index = priority.get(hash = bucket.hashCode());	

		if(index == null) {
			priority.put(hash, new MyVector(bucket, valueToPut));
		}
		else {
			priority.get(hash).add(valueToPut);
		}
	}
	
	/**
	 * This method do not guarantee the uniqueness of the elements in the bucket. 
	 * @param bucket - name of the bucket
	 * @param valuesToPut
	 */
	public void add(String bucket, T valuesToPut[]) {
		int hash;
		BucketVector<T>.MyVector index = priority.get(hash = bucket.hashCode());	

		if(index == null) {
			priority.put(hash, new MyVector(bucket, valuesToPut));
		}
		else {
			priority.get(hash).add(valuesToPut);
		}
	}
	
	/**
	 * Add a single bucket without values.
	 * @param bucket
	 * @return True if the bucket already exist.
	 * <br> False otherwise.
	 */
	public boolean add(String bucket) {
		int hash;
		BucketVector<T>.MyVector index = priority.get(hash = bucket.hashCode());	

		if(index == null) {
			priority.put(hash, new MyVector(bucket));
			return false;
		}
		return true;
	}
	
	/**
	 * Check if bucket with this name exist.
	 * @param bucket
	 * @return True if exist. <br>False otherwise.
	 */
	public boolean bucketExist(String bucket) {
		BucketVector<T>.MyVector index = priority.get(bucket.hashCode());
		if(index == null) return false;
		return true;
	}
	
	/**
	 * @param bucket
	 * @return Collection from this bucket.
	 */
	public Collection<T> getCollection(String bucket) {
		int hash;
		if(priority.get(hash = bucket.hashCode()) == null) return null;
		
		return priority.get(hash).getVector();
	}
	
	/**
	 * @param expression - expression that you want to find in this collection
	 * @param baskets - baskets where you want to check the expression. It allows for 
	 * easier error checking.
	 * @return True if expression was found in these baskets. Otherwise false.
	 */
	public boolean find(T expression, String... baskets) {
		for(String bucket : baskets) {
			if(priority.get(bucket.hashCode()).find(expression)) return true;
		}
		
		return false;
	}
	
	/**
	 * Change name of the existing bucket.
	 * @param oldName
	 * @param newName
	 * @return True if the operation was not successful. There was no bucket with this name.
	 * <br>False if the operation was successful.
	 */
	public boolean changeName(String oldName, String newName) {
		BucketVector<T>.MyVector tmp;
		int hashOld;
		if((tmp = priority.get(hashOld = oldName.hashCode())) == null) return true;
		tmp.changeName(newName);
		
		priority.remove(hashOld);
		priority.put(newName.hashCode(), tmp);
		
		return false;
	}
	
	/**
	 * @return Collection of names of all buckets.
	 */
	public Collection<String> getNames(){
		Collection<String> ret = new ArrayList<String>();
		
		for(BucketVector<T>.MyVector vector : priority.values()) {
			ret.add(vector.getName());
		}
		
		return ret;
	}
}
