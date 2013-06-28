package com.badrabbit.psw.managers;

import android.view.MotionEvent;

import com.badrabbit.psw.graphics.SpriteBatch;
import com.badrabbit.psw.objects.SplashView;

public class GameManager {
	
	// Think of game as view
	public static Game currentGame = new SplashView();
	
	public static void switchGame(Game newGame) {
		currentGame.exit();
		currentGame = newGame;
		newGame.init();
	}
	
	public static void update(long dTime) {
		if (currentGame != null)
			currentGame.update(dTime);
	}
	
	public static void draw(SpriteBatch spriteBatch) {
		if (currentGame != null)
			currentGame.draw(spriteBatch);
	}
	
	public static void tapEvent() {
		
	}

	public static void init() {
		if (currentGame != null)
			currentGame.init();
	}

	public static void onTouchEvent(MotionEvent event) {
		if (currentGame != null)
			currentGame.onTouchEvent(event);
	}
}
