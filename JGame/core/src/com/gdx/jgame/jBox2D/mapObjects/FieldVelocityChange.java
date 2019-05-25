package com.gdx.jgame.jBox2D.mapObjects;

public class FieldVelocityChange {
	public float speedChange;
	public float maxSpeedChange;
	
	public FieldVelocityChange(float speedRatio, float maxSpeedRatio) {
		if(speedRatio < 0 || maxSpeedRatio < 0) throw new IllegalArgumentException("Ratio cannot be negative");
		this.speedChange = speedRatio;
		this.maxSpeedChange = maxSpeedRatio;
	}
}
