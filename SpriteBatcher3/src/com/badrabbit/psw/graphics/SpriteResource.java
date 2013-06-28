package com.badrabbit.psw.graphics;

public class SpriteResource {

	public String name;
	FrameResource[] frames;
	
	public SpriteResource(FrameResource[] frames, String name)
	{
		this.name = name;
		this.frames = frames;
	}
	
	public SpriteResource(FrameResource frame, String name)
	{
		this.name = name;
		frames = new FrameResource[1];
		frames[0] = frame;
	}
}
