package com.gdx.jgame.jBox2D.mapObjects;

public class FieldGameState extends Field{
	public boolean win;
	public boolean lose;
	
	public FieldGameState(boolean win, boolean lose) {
		if(win && lose) throw new IllegalArgumentException("Field cannot be set to win and lose the game simultaneously");
		this.win = win;
		this.lose = lose;
	}
}
