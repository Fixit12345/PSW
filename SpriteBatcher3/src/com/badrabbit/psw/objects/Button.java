package com.badrabbit.psw.objects;

import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.Window;

import com.badrabbit.psw.graphics.GameSurfaceRenderer;
import com.badrabbit.psw.graphics.SpriteBatch;
import com.badrabbit.psw.graphics.SpriteResource;
import com.badrabbit.psw.graphics.TextureManager;
import com.badrabbit.psw.graphics.TextureResource;
import com.example.spritebatcher3.R;

public class Button extends GameObject
{
	TextureResource texture;
	SpriteResource sprite;
	
	float X, Y;
	float buttonSizeX, buttonSizeY;
	float lBorder, rBorder, tBorder, bBorder;
	
	int frame = 0;
	long frameTime = 0;

	public Button(float X, float Y)
	{
		
		this.buttonSizeX = 64;
		this.buttonSizeY = 64;
		this.X = X;
		this.Y = Y;
		/*
		 *Why doesn't this work?
		this.lBorder = (GameSurfaceRenderer.width/2.0f) + (-buttonSizeX/2.0f);
		this.rBorder = (GameSurfaceRenderer.width/2.0f) + (buttonSizeX/2.0f);
		*/
	}
	
	@Override
	public void create() {
		texture = TextureManager.loadTexture(R.drawable.perspective_button_stop);
	}
	
	@Override
	public void update(long dTime) {
		frameTime += dTime;	
		if (frameTime > 1000)
		{
			frame++;
			frameTime = 0;
		}
	}

	@Override
	public void draw(SpriteBatch spriteBatch) {
		spriteBatch.begin(texture, SpriteBatch.BLEND_ALPHA);
		spriteBatch.draw(X, Y, buttonSizeX, buttonSizeY, 0.0f, 0.0f, 1.0f, 1.0f, 0f, (byte) 127, (byte) 127, (byte) 127, (byte) 127);
		spriteBatch.end();
	}
		 
	@Override
	public void tap(MotionEvent event){
		
	
		
		
		
		float lBorder = (GameSurfaceRenderer.width/2.0f) + (-buttonSizeX/2.0f);
		float rBorder = (GameSurfaceRenderer.width/2.0f) + (buttonSizeX/2.0f);
		
		float tBorder= (GameSurfaceRenderer.height/2.0f) + (-buttonSizeY/2.0f);
		float bBorder = (GameSurfaceRenderer.height/2.0f) + (buttonSizeY/2.0f);

		System.out.println("Tapped. \n");
		System.out.format("Remember that left border is %f and right border is %f /n", lBorder, rBorder);
		System.out.format("Remember that top border is %f and bottom border is %f /n", tBorder, bBorder);
		System.out.format("Remember that center width is %f and center height is %f /n", (GameSurfaceRenderer.width/2.0f), (GameSurfaceRenderer.height/2.0f));
		
		
		System.out.format("The screen has been tapped at %f, %f.", event.getRawX(), event.getRawY());
		
		
		
		
		if( (lBorder < event.getRawX()) && (event.getRawX() < rBorder)   ){
			
			System.out.println("The width is good.");
			
		}
		
		if( (tBorder < event.getRawY()) && (event.getRawY() < bBorder) ){
			
			System.out.println("The height is good.");
			
		}
		
		
		
		
		/*
		float tapX = event.getRawX() + GameSurfaceRenderer.width / 2.0f;
		float tapY = event.getRawY() + GameSurfaceRenderer.height / 2.0f;
		*/
		
		//System.out.println("Tapped./n");
		
		
		
		
		
		
	}
}
