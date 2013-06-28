package com.badrabbit.psw.managers;

import java.util.LinkedList;

import android.view.MotionEvent;

import com.badrabbit.psw.graphics.SpriteBatch;
import com.badrabbit.psw.objects.GameObject;
import com.badrabbit.psw.objects.Button;
import com.badrabbit.psw.objects.StrobingTank;


// Each Game is a View
public class Game {

 	public LinkedList<GameObject> children = new LinkedList<GameObject>();
 	
	public void init() {
		
	}
	
	public void update(long dTime) {
		for (GameObject o : children)
			o.update(dTime);
	}
	
	public void draw(SpriteBatch spriteBatch) {
		for (GameObject o : children)
			o.draw(spriteBatch);
	}

	public void exit() {
		
	}
	
	public void addChild(GameObject o) {
		children.addLast(o);
		o.create();
	}

	public void onTouchEvent(MotionEvent event) {
		for (GameObject o : children)
			o.tap(event);
	}
}
