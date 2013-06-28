package com.badrabbit.psw.graphics;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import android.opengl.GLES20;

public class SpriteBatch {

	public final static int BLEND_NONE = 0;
	public final static int BLEND_ALPHA = 1;
	public final static int BLEND_ADDITIVE = 2;
	
	final static int spriteCount = 64;
	static boolean blendingEnabled = false;
	static int lastBlendingMode = -1;
	
	//Vertices
	private final FloatBuffer vertexBuffer;
	private final ShortBuffer indexBuffer;
	float[] vertexArray;
	
	private boolean active = false;
	private int bufferPosition = 0;

	private float width;
	private float height;
	
	private TextureResource lastTexture;
	
	SpriteBatchShader myShader = new SpriteBatchShader();

	public SpriteBatch() {

		myShader.buildProgram();
		
		vertexBuffer = ByteBuffer.allocateDirect(spriteCount * 4 * myShader.mBytesPerFloat * myShader.mItemsPerVertex)
	    .order(ByteOrder.nativeOrder()).asFloatBuffer();
		vertexArray = new float[myShader.mItemsPerVertex * 4];
		
		short[] indecies = new short[spriteCount * 6];
		
		for(int i = 0; i < spriteCount; i++)
		{
			indecies[i * 6 + 0] = (short) (i * 4 + 0);
			indecies[i * 6 + 1] = (short) (i * 4 + 1);
			indecies[i * 6 + 2] = (short) (i * 4 + 2);
			
			indecies[i * 6 + 3] = (short) (i * 4 + 0);
			indecies[i * 6 + 4] = (short) (i * 4 + 2);
			indecies[i * 6 + 5] = (short) (i * 4 + 3);
		}
		indexBuffer = ByteBuffer.allocateDirect(spriteCount * myShader.mBytesPerShort * 6)
				.order(ByteOrder.nativeOrder()).asShortBuffer();
		indexBuffer.put(indecies).position(0);
		
	}
	
	public void onSurfaceChanged(int width, int height) {
		this.width = width / 2.0f;
		this.height = height / 2.0f;
	}
	
	public void begin(TextureResource texture, int blendingMode)
	{
		assert !active : "cannot make consecutive calls to begin() without an interviening end()";
		if (lastTexture != texture)
		{
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture.glID);	
			lastTexture = texture;
		}
		if (blendingMode != lastBlendingMode) 
		{
			activateBlendMode(blendingMode);
		}
		active = true;
	}

	private void activateBlendMode(int blendingMode) {
		lastBlendingMode = blendingMode;
		switch (blendingMode)
		{
			case 1:
				GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
				enableBlending();
				break;
			case 2:
				GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE);
				enableBlending();
			break;
			default:
				if (blendingEnabled)
				{
					GLES20.glDisable(GLES20.GL_BLEND);
					blendingEnabled = false;
				}
				break;
		}
	}
	
	void enableBlending() 
	{
		if (!blendingEnabled)
		{
			GLES20.glEnable(GLES20.GL_BLEND);
			blendingEnabled = true;
		}
	}

	public void draw(
			float centerX, float centerY, 
			float sizeX, float sizeY, 
			SpriteResource sprite, int frame,
			float rotation,
			byte R, byte G, byte B, byte A) {
		int id = frame % sprite.frames.length;
		FrameResource f = sprite.frames[id];
		draw(centerX, centerY, sizeX, sizeY, f.x, f.y, f.x + f.width, f.y + f.height, rotation, R, G, B, A);
	}

	public void draw(
			float centerX, float centerY, 
			float sizeX, float sizeY, 
			float texMinX, float texMinY, float texMaxX, float texMaxY,
			float rotation,
			byte R, byte G, byte B, byte A) {

		final int b = 0;
		
		if (rotation != 0) {
			
			final float cos = (float) Math.cos(rotation);
			final float sin = (float) Math.sin(rotation);

			//upper left
			vertexArray[b] = (-sizeX / 2 * cos + sizeY / 2 * sin + centerX) / width;
			vertexArray[b + 1] = (-sizeX / 2 * sin - sizeY / 2 * cos + centerY) / -height;
			
			//upper right
			vertexArray[b + 5] = (sizeX / 2 * cos + sizeY / 2 * sin + centerX) / width;
			vertexArray[b + 6] = (sizeX / 2 * sin - sizeY / 2 * cos + centerY) / -height;
			
			//lower right
			vertexArray[b + 10] = (sizeX / 2 * cos - sizeY / 2 * sin + centerX) / width;
			vertexArray[b + 11] = (sizeX / 2 * sin + sizeY / 2 * cos + centerY) / -height;
			
			//lower left
			vertexArray[b + 15] = (-sizeX / 2 * cos - sizeY / 2 * sin + centerX) / width;
			vertexArray[b + 16] = (-sizeX / 2 * sin + sizeY / 2 * cos + centerY) / -height;
		} else {
			//upper left
			vertexArray[b] = (centerX - sizeX / 2.0f) / width;
			vertexArray[b + 1] = (centerY - sizeY / 2.0f) / -height;
			
			//upper right
			vertexArray[b + 5] = (centerX + sizeX / 2.0f) / width;
			vertexArray[b + 6] = (centerY - sizeY / 2.0f) / -height;
			
			//lower right
			vertexArray[b + 10] = (centerX + sizeX / 2.0f) / width;
			vertexArray[b + 11] = (centerY + sizeY / 2.0f) / -height;
			
			//lower left
			vertexArray[b + 15] = (centerX - sizeX / 2.0f) / width;
			vertexArray[b + 16] = (centerY + sizeY / 2.0f) / -height;
		}
		
		float color = bitsToFloat(R, G, B, A);
		
		//upper left
		vertexArray[b + 2] = color;

		vertexArray[b + 3] = texMinX;
		vertexArray[b + 4] = texMinY;
		
		//upper right
		vertexArray[b + 7] = color;

		vertexArray[b + 8] = texMaxX;
		vertexArray[b + 9] = texMinY;
		
		//lower right
		vertexArray[b + 12] = color;

		vertexArray[b + 13] = texMaxX;
		vertexArray[b + 14] = texMaxY;

		//lower left
		vertexArray[b + 17] = color;

		vertexArray[b + 18] = texMinX;
		vertexArray[b + 19] = texMaxY;
		
		vertexBuffer.position(bufferPosition * myShader.mItemsPerVertex * 4);
		vertexBuffer.put(vertexArray);
		
		bufferPosition++;
		if (bufferPosition == spriteCount)
			flush();
	}
	
	public static float bitsToFloat(byte a, byte b, byte c, byte d)
	{
		int asInt = (a & 0xFF) 
	            | ((b & 0xFF) << 8) 
	            | ((c & 0xFF) << 16) 
	            | ((d & 0xFF) << 24);

		return Float.intBitsToFloat(asInt);
	}
	
	public void end()
	{
		assert active : "cannot call end() before begin()";
		flush();
		active = false;
	}
	
	private void flush()
	{
		myShader.draw(vertexBuffer, indexBuffer, bufferPosition);
		bufferPosition = 0;
		vertexBuffer.position(0);
	}
	
	public static void Reset() 
	{
		lastBlendingMode = -1;
		blendingEnabled = false;
	}
}
