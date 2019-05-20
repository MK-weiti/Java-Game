package com.gdx.jgame.gameObjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.gdx.jgame.managers.MapManager;

public class Methods {
	public static Vector2[] setVerticesToTexture(Texture texture, float scale) {
		Vector2[] points = new Vector2[4];
		float tmpWidth = texture.getWidth() * scale / MapManager.PIXELS_PER_METER /2;
		float tmpHeight = texture.getHeight() * scale / MapManager.PIXELS_PER_METER / 2;
		
		points[0] = new Vector2(- tmpWidth, - tmpHeight);
		points[1] = new Vector2(tmpWidth, - tmpHeight);
		points[2] = new Vector2(tmpWidth, tmpHeight);
		points[3] = new Vector2(- tmpWidth, tmpHeight);
		
		return points;
	}
}
