package com.badrabbit.psw.graphics;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.w3c.dom.Element;
import org.xmlpull.v1.XmlPullParser;

import android.content.res.XmlResourceParser;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import com.badrabbit.psw.managers.ContextManager;
import com.badrabbit.psw.managers.Logic;

public class TextureManager {

	static final String KEY_SPRITE = "sprite";
	static final String KEY_NAME = "n";
	static final String KEY_X = "x";
	static final String KEY_Y = "y";
	static final String KEY_WIDTH = "w";
	static final String KEY_HEIGHT = "h";
	
	static final int frameStringLength = 5;
	
	public static boolean useGL = true;
	
	static boolean resourcesReleased = false;
	static LinkedList<TextureResource> resources = new LinkedList<TextureResource>();
	static int resourceCount = 0;
	
	public static TextureResource loadTexture(final int textureId)
	{
		assert !resourcesReleased : 
			"TextureManager has currently released resources\ncall reloadResources() before calling loadTexture()";
	
		TextureResource r = new TextureResource();
		r.resourceId = textureId;
		
		if (useGL) 
		    r.glID = genGLTexture(textureId);
	 
		resources.addLast(r);
		resourceCount++;
	    return r;
	}
	
	public static TextureResource loadTexture(final int textureId, final int xmlId)
	{
		TextureResource r = loadTexture(textureId);

		LinkedList<FrameResource> frames = new LinkedList<FrameResource>();
		int event;   
        XmlResourceParser parser = ContextManager.currentContext.getResources().getXml(com.example.spritebatcher3.R.xml.test);  
  
        try 
        {
	        event = parser.getEventType();  
	  
	        int sizeX = 0;
	        int sizeY = 0;
	        while (event != XmlPullParser.END_DOCUMENT)   
	        {  
	            event = parser.getEventType();  
	            if (event == XmlPullParser.START_TAG) {
	            	
	            	if (parser.getName().contentEquals("TextureAtlas"))   
		            {  
		            	sizeX = parser.getAttributeIntValue(null, "width", 0);
		            	sizeY = parser.getAttributeIntValue(null, "height", 0);
		            }   
	            	
	            	if (parser.getName().contentEquals("sprite"))   
		            {  
		            	frames.addLast(new FrameResource(parser.getAttributeValue(null, "n"),
		            			parser.getAttributeIntValue(null, "x", 0) / (float)sizeX, parser.getAttributeIntValue(null, "y", 0) / (float)sizeY,
		            			parser.getAttributeIntValue(null, "w", 0) / (float)sizeX, parser.getAttributeIntValue(null, "h", 0) / (float)sizeY));
		            }   
	            }
	            parser.next();  
	        }  
        }
        catch(Exception e) 
        {
        	e.printStackTrace();
        }

        parser.close();  
		 
		for (FrameResource fi : frames)
		{
			int dotPos = 5;
			for (int i = 0; i < fi.name.length(); i++)
				if (fi.name.charAt(fi.name.length() - i - 1) == '.')
				{
					dotPos = i + 2;
					break;
				}
			
			if (fi.name.length() > dotPos + frameStringLength)
			{
				String frameSub = fi.name.substring(fi.name.length() - (dotPos + frameStringLength), 
						fi.name.length() - dotPos);
				frameSub += frameSub;
			}
			if (fi.name.length() > dotPos + frameStringLength && fi.name.substring(fi.name.length() - (dotPos + frameStringLength), 
					fi.name.length() - dotPos).equalsIgnoreCase("FRAME"))
			{
				if (fi.name.charAt(fi.name.length() - dotPos) == '1')
				{
					FrameResource[] subFrames = new FrameResource[1];
					int framesLength = 1;
					String subName = fi.name.substring(0, fi.name.length() - (dotPos + frameStringLength));
					
					for (FrameResource fj : frames)
						if (fi != fj && fj.name.substring(0, fj.name.length() - (dotPos + frameStringLength)).equals(subName))
						{
							int frameIndex = Integer.parseInt(fj.name.substring(fj.name.length() - dotPos, fj.name.length() - dotPos + 1));
							if (frameIndex > framesLength)
							{
								FrameResource[] newSubFrames = new FrameResource[frameIndex];
								for (int k = 0; k < framesLength; k++)
									newSubFrames[k] = subFrames[k];
								framesLength = frameIndex;
								subFrames = newSubFrames;
							}
							subFrames[frameIndex - 1] = fj;
			 			}
					
					subFrames[0] = fi;
					r.sprites.add(new SpriteResource(subFrames, subName));
				}
			}
			else
				r.sprites.add(new SpriteResource(fi, fi.name));
		}

		return r;
	}

	static int genGLTexture(final int resourceId)
	{
		final int[] textureHandle = new int[1];
		 
	    GLES20.glGenTextures(1, textureHandle, 0);
	 
	    if (textureHandle[0] != 0)
	    {
	        final BitmapFactory.Options options = new BitmapFactory.Options();
	        options.inScaled = false;   // No pre-scaling
	 
	        // Read in the resource
	        final Bitmap bitmap = BitmapFactory.decodeResource(ContextManager.currentContext.getResources(), resourceId, options);
	 
	        // Bind to the texture in OpenGL
	        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0]);
	 
	        // Set filtering
	        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
	        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
	 
	        // Load the bitmap into the bound texture.
	        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
	 
	        // Recycle the bitmap, since its data has been loaded into OpenGL.
	        bitmap.recycle();
	    }
	 
	    if (textureHandle[0] == 0)
	    {
	        throw new RuntimeException("Error loading texture.");
	    }
	    return textureHandle[0];
	}
	
	public static void releaseResources() {
		
		if (!resourcesReleased)
		{
			if (useGL) 
			{
				int[] textureHandles = new int[resourceCount];
				int i = 0;
				
				for (TextureResource r : resources)
				{
					textureHandles[i] = r.glID;
					i++;
				}
				GLES20.glDeleteTextures(resourceCount, textureHandles, 0);
			}
			
			resourcesReleased = true;
		}
	}
	
	public static void reloadResources() {
		if (resourcesReleased)
		{
			if (useGL)
			{
				for (TextureResource r : resources)
					r.glID = genGLTexture(r.resourceId);
			}
			resourcesReleased = false;
		}
	}
}
