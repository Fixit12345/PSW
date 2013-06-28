package com.badrabbit.psw.graphics;

import java.util.LinkedList;

public class TextureResource {

	public int resourceId;
	public int glID;
	
	public LinkedList<SpriteResource> sprites = new LinkedList<SpriteResource>();
	
	public SpriteResource getSprite(String s) 
	{
		for (SpriteResource r : sprites)
			if (r.name.contentEquals(s))
				return r;
		
		return null;
	}
}
