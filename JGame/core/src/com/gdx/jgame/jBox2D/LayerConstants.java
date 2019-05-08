package com.gdx.jgame.jBox2D;

/*
 * In Tiled Map Editor custom properties names are always 
 * String type. These string types can hold value of
 * some type, for example boolean.
 */

public final class LayerConstants {
	public static final String bodyType = "BodyType";
	
	public final class BodyType{
		public static final String dynamicBody = "dynamic";
		public static final String staticBody = "static";
		public static final String kinematicBody = "kinematic";
	}
	
	public final class CustomProperties{
		public static final String strongBlocked = "strongBlocked";
		public static final String blocked = "blocked";
	}
}