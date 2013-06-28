package com.badrabbit.psw.graphics;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import android.opengl.GLES20;

import com.badrabbit.psw.managers.ContextManager;
import com.badrabbit.psw.managers.Logic;
import com.example.spritebatcher3.R;

public class SpriteBatchShader {

    //shader handles
	public int mPositionHandle;
	public int mColorHandle;
	public int mTexCoordHandle;
	public int mTextureHandle;

	public final int mBytesPerFloat = 4;
	public int mBytesPerShort = 2;
	
	public final int mItemsPerVertex = 5;
	public final int mStrideBytes = mItemsPerVertex * mBytesPerFloat;
	
	private final int mPositionOffset = 0;
	private  final int mPositionDataSize = 2;

	private  final int mColorOffset = 2;
	private  final int mColorDataSize = 4;

	private  final int mTexCoordOffset = 3;
	private  final int mTexCoordDataSize = 2;

	
	public int buildProgram()
	{
		int vertexShaderHandle = SpriteBatchShader.loadShader(R.raw.spritebatchvertex, GLES20.GL_VERTEX_SHADER);
		int fragmentShaderHandle = SpriteBatchShader.loadShader(R.raw.spritebatchfragment, GLES20.GL_FRAGMENT_SHADER);
		
		int programHandle = GLES20.glCreateProgram();
		 
		if (programHandle != 0)
		{
		    // Bind the vertex shader to the program.
		    GLES20.glAttachShader(programHandle, vertexShaderHandle);
		 
			// Bind the fragment shader to the program.
		    GLES20.glAttachShader(programHandle, fragmentShaderHandle);
		 
		    // Bind attributes
		    GLES20.glBindAttribLocation(programHandle, 0, "a_Position");
		    GLES20.glBindAttribLocation(programHandle, 1, "a_Color");
		    GLES20.glBindAttribLocation(programHandle, 2, "a_TexCoord");
		    
		    // Link the two shaders together into a program.
		    GLES20.glLinkProgram(programHandle);
		 
		    // Get the link status.
		    final int[] linkStatus = new int[1];
		    GLES20.glGetProgramiv(programHandle, GLES20.GL_LINK_STATUS, linkStatus, 0);
		 
		    // If the link failed, delete the program.
		    if (linkStatus[0] == 0)
		    {
		        GLES20.glDeleteProgram(programHandle);
		        programHandle = 0;
		    }
		}
		
		if (programHandle == 0)
		{
		    throw new RuntimeException("Error creating program.");
		}

	    mTextureHandle = GLES20.glGetUniformLocation(programHandle, "u_Texture");
	    mPositionHandle = GLES20.glGetAttribLocation(programHandle, "a_Position");
	    mColorHandle = GLES20.glGetAttribLocation(programHandle, "a_Color");
	    mTexCoordHandle = GLES20.glGetAttribLocation(programHandle, "a_TexCoord");

	    GLES20.glUseProgram(programHandle);
		return programHandle;
	}
	
	public void draw(final FloatBuffer vertexBuffer, final ShortBuffer indexBuffer, final int bufferPosition) 
	{
	    // Pass in the position information
	    vertexBuffer.position(mPositionOffset);
	    GLES20.glVertexAttribPointer(mPositionHandle, mPositionDataSize, GLES20.GL_FLOAT, false,
	    		mStrideBytes, vertexBuffer);
	 
	    GLES20.glEnableVertexAttribArray(mPositionHandle);

	    // Pass in the color information
	    vertexBuffer.position(mColorOffset);
	    GLES20.glVertexAttribPointer(mColorHandle, mColorDataSize, GLES20.GL_BYTE, false,
	    		mStrideBytes, vertexBuffer);
	 
	    GLES20.glEnableVertexAttribArray(mColorHandle);
	    
	    // Pass in the normal information
	    vertexBuffer.position(mTexCoordOffset);
	    GLES20.glVertexAttribPointer(mTexCoordHandle, mTexCoordDataSize, GLES20.GL_FLOAT, false,
	    		mStrideBytes, vertexBuffer);
	 
	    GLES20.glEnableVertexAttribArray(mTexCoordHandle);
	    

        GLES20.glDrawElements(GLES20.GL_TRIANGLES, bufferPosition * 6,
                              GLES20.GL_UNSIGNED_SHORT, indexBuffer);
	}
	
	public static int loadShader(int resourceId, int shaderType)
	{
		int shaderHandle = 0;
		shaderHandle = GLES20.glCreateShader(shaderType);
		 
		final String shaderCode = Logic.readRawFile(resourceId);
		
		if (shaderHandle != 0)
		{
		    // Pass in the shader source.
		    GLES20.glShaderSource(shaderHandle, shaderCode);
		 
		    // Compile the shader.
		    GLES20.glCompileShader(shaderHandle);
		 
		    // Get the compilation status.
		    final int[] compileStatus = new int[1];
		    GLES20.glGetShaderiv(shaderHandle, GLES20.GL_COMPILE_STATUS, compileStatus, 0);
		 
		    // If the compilation failed, delete the shader.
		    if (compileStatus[0] == 0)
		    {
		        GLES20.glDeleteShader(shaderHandle);
		        shaderHandle = 0;
		    }
		}
		if (shaderHandle == 0)
		{
		    throw new RuntimeException("Error creating shaders");
		}
		return shaderHandle;
	}
}
