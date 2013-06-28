package com.badrabbit.psw.objects;

import android.view.MotionEvent;

import com.badrabbit.psw.graphics.SpriteBatch;


public abstract class GameObject {

	public abstract void create();
	public abstract void update(long dTime);
	public abstract void draw(SpriteBatch spriteBatch);
	public abstract void tap(MotionEvent event);
}
