package com.badrabbit.psw.managers;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.ConfigurationInfo;
import android.graphics.Rect;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.Window;

import com.badrabbit.psw.graphics.GameSurfaceRenderer;

public class MainActivity extends Activity {

	private GLSurfaceView mySurfaceView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// Test console output
		
		
		
		
		//call super inherited Activity method on create, pass the saved state
		super.onCreate(savedInstanceState);
		
		//Setting current context. What is ContextManager?
		ContextManager.currentContext = this;
		
		final ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
		final boolean supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000;
		
		if (true) {
			mySurfaceView = new GameSurfaceView(this);
			mySurfaceView.setEGLContextClientVersion(2);
			mySurfaceView.setRenderer(new GameSurfaceRenderer());
		} else {
			return;
		}
		
		displayConsoleOut();
		
		setContentView(mySurfaceView);
		
		
	}
	
	public void displayConsoleOut(){
		
		// get status bar height.
		Rect rectgle= new Rect();
		Window window= getWindow();
		window.getDecorView().getWindowVisibleDisplayFrame(rectgle);
		int StatusBarHeight= rectgle.top;
		System.out.format("Working? %d is the status bar height:", StatusBarHeight);
		System.out.println("Console outing");
	}

		
	
	
	

	@Override
	protected void onResume()
	{
	    super.onResume();
	    mySurfaceView.onResume();
	}
	 
	@Override
	protected void onPause()
	{
	    super.onPause();
	    mySurfaceView.onPause();
	}
}