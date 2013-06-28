package com.badrabbit.psw.objects;

import android.view.MotionEvent;

import com.example.spritebatcher3.R;
import com.badrabbit.psw.graphics.SpriteBatch;
import com.badrabbit.psw.graphics.SpriteResource;
import com.badrabbit.psw.graphics.TextureManager;
import com.badrabbit.psw.graphics.TextureResource;

public class StrobingTank extends GameObject
{
	TextureResource texture;
	SpriteResource sprite;
	
	float X, Y;
	
	int frame = 0;
	long frameTime = 0;

	public StrobingTank(float X, float Y)
	{
		this.X = X;
		this.Y = Y;
	}
	
	@Override
	public void create() {
		texture = TextureManager.loadTexture(R.drawable.test, R.xml.test);
		sprite = texture.getSprite("Tank");
	}
	
	@Override
	public void update(long dTime) {
		frameTime += dTime;
		if (frameTime > 5000)
		{
			frame++;
			frameTime = 0;
		}
	}

	@Override
	public void draw(SpriteBatch spriteBatch) {
		spriteBatch.begin(texture, SpriteBatch.BLEND_ALPHA);
		spriteBatch.draw(X, Y, 64f, 64f, sprite, frame, 0f, (byte)127, (byte)127, (byte)127, (byte)127);
		spriteBatch.end();
	}

	@Override
	public void tap(MotionEvent event) {
		// TODO Auto-generated method stub
		
	}
	
}
