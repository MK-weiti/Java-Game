package com.gdx.jgame.desktop;

public class GameProperties {
	private static String M_TITLE = "My Game";
	private static int M_WIDTH = 1280;
	private static int M_HEIGHT = 900;
	private static int M_FOREGROUND_FPS = 60;
	private static int M_BACKGROUND_FPS = -1;
	
	public final static String TITLE = M_TITLE;
	public final static int WIDTH = M_WIDTH;
	public final static int HEIGHT = M_HEIGHT;
	public final static int FOREGROUND_FPS = M_FOREGROUND_FPS;
	public final static int BACKGROUND_FPS = M_BACKGROUND_FPS;
	
	
	
	void resize(int width, int height) {
		M_WIDTH = width;
		M_HEIGHT = height;
	}
	
	void changeTitle(String title) {
		M_TITLE = title;
	}
}
