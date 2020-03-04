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
	
	public final class ObstaclesProperties{
		public static final String maskBits = "MaskBits";
		public final class MaskBits{
			public static final String worldBorder = "worldBorder";
			public static final String simpleObstacle = "simpleObstacle";
		}		
	}
	
	public final class Logic{
		public static final String changeGameState = "ChangeGameState";
		public final class ChangeGameState{
			public static final String win = "win";
			public static final String lose = "lose";
		}
		
		
	}
}