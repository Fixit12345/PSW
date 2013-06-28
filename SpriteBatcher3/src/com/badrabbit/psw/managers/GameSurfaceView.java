package com.badrabbit.psw.managers;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

public class GameSurfaceView extends GLSurfaceView {

	public GameSurfaceView(Context context) {
		// calls GLSurfaceView constructor and passes context argument
		super(context);

		// calls GLSurfaceView method and passes numbers.
		super.setEGLConfigChooser(8 , 8, 8, 8, 16, 0);

		System.out.println("GameSurface--------------------------------0000000000000000000000000000000000000000");
	}

	
	//call this to make sprites do shit when you touch them
	
	// what type of event are we going to be passing into here?
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		GameManager.onTouchEvent(event);
		return super.onTouchEvent(event);
	}
}
