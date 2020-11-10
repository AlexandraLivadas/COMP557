package mintools.viewer.geom;

import com.jogamp.opengl.GLAutoDrawable;

import mintools.viewer.ShadowPipeline;

public class Cube {
	public static void draw( GLAutoDrawable drawable, ShadowPipeline pipeline ) {
		// silly way to do it... and might get some tearing if we are not careful?
		pipeline.push();
		pipeline.scale(drawable,.5,.5,.5);
		pipeline.translate(drawable,0,0,1);
		Quad.draw(drawable, pipeline);
		pipeline.pop(drawable);
		pipeline.push();
		pipeline.rotate(drawable,Math.PI/2,0,1,0);
		pipeline.scale(drawable,.5,.5,.5);
		pipeline.translate(drawable,0,0,1);
		Quad.draw(drawable, pipeline);
		pipeline.pop(drawable);
		pipeline.push();
		pipeline.rotate(drawable,Math.PI,0,1,0);
		pipeline.scale(drawable,.5,.5,.5);
		pipeline.translate(drawable,0,0,1);
		Quad.draw(drawable, pipeline);
		pipeline.pop(drawable);
		pipeline.push();
		pipeline.rotate(drawable,-Math.PI/2,0,1,0);
		pipeline.scale(drawable,.5,.5,.5);
		pipeline.translate(drawable,0,0,1);
		Quad.draw(drawable, pipeline);
		pipeline.pop(drawable);
		pipeline.push();
		pipeline.rotate(drawable,Math.PI/2,1,0,0);
		pipeline.scale(drawable,.5,.5,.5);
		pipeline.translate(drawable,0,0,1);
		Quad.draw(drawable, pipeline);
		pipeline.pop(drawable);
		pipeline.push();
		pipeline.rotate(drawable,-Math.PI/2,1,0,0);
		pipeline.scale(drawable,.5,.5,.5);
		pipeline.translate(drawable,0,0,1);
		Quad.draw(drawable, pipeline);
		pipeline.pop(drawable);
	}
}
