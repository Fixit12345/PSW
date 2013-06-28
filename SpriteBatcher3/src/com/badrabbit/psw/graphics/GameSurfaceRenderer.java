package com.badrabbit.psw.graphics;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.os.SystemClock;

import com.badrabbit.psw.managers.Game;
import com.badrabbit.psw.managers.GameManager;


public class GameSurfaceRenderer implements Renderer {

	public SpriteBatch spriteBatch;
	
	public static int width;
	public static int height;
	
	long previousTime = 0;
	
    @Override
    public void onDrawFrame(GL10 glUnused)
    {
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
        long time = SystemClock.uptimeMillis();
        long dTime = time - previousTime;
        if (dTime > 0)
        {
        	GameManager.update(dTime);
        	previousTime = time;
        }
        GameManager.draw(spriteBatch);
    }
    
	@Override
	public void onSurfaceChanged(GL10 arg0, int width, int height) {
		GLES20.glViewport(0, 0, width, height);

		spriteBatch.onSurfaceChanged(width, height);
		GameSurfaceRenderer.width = width;
		GameSurfaceRenderer.height = height;
	}
	
	@Override
	public void onSurfaceCreated(GL10 arg0, EGLConfig arg1) {

		GLES20.glClearColor(1, 1, 1, 1);
		spriteBatch = new SpriteBatch();
		GameManager.init();
	}
}
