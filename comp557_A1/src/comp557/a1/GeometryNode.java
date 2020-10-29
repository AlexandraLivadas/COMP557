package comp557.a1;

import javax.vecmath.Tuple3d;
import javax.vecmath.Vector3d;

import com.jogamp.opengl.GL4;

import com.jogamp.opengl.GLAutoDrawable;

import comp557.a1.geom.Cube;
import comp557.a1.geom.Sphere;
import mintools.parameters.DoubleParameter;

/*
 * Student Name: Alexandra Livadas
 * Student ID: 260709168
 */
public class GeometryNode extends GraphNode{
	
	enum Shape{CUBE, SPHERE};
	private Shape shape;
	DoubleParameter x;
	DoubleParameter y;
	DoubleParameter z;
	Tuple3d translation = null;
	Tuple3d scale = null;
	float[] color;
	
	public GeometryNode(String name, Shape shape) {
		super(name);
		this.shape = shape;
		//this.translation.set(new double[] {0.0, 0.0, 0.0});
		this.translation = new Vector3d(0.0, 0.0, 0.0);
		this.scale = new Vector3d(0, 1, 0);
		this.color = new float[] {0.0f, 0.0f, 0.0f};
	}
	
	public void setCentre(Tuple3d translation) {
		this.translation = translation;
	}
	
	public void setScale(Tuple3d scale) {
		this.scale = scale;
	}
	
	public void setColor(float[] color) {
		this.color = color;
	}
	
	@Override
	public void display(GLAutoDrawable drawable, BasicPipeline pipeline) {
		GL4 gl = drawable.getGL().getGL4();
		
		pipeline.push();
		// First, apply basic transformations, then draw shapes
		pipeline.translate(translation.x, translation.y, translation.z);
		pipeline.scale(scale.x, scale.y, scale.z);
		
		gl.glUniform3f( pipeline.kdID, color[0], color[1], color[2] );
		gl.glUniform3f(pipeline.ksID, color[0], color[1], color[2] );
		
		if (shape.equals(GeometryNode.Shape.CUBE)) {
			Cube.draw(drawable, pipeline);
		}
		else if (shape.equals(Shape.SPHERE)) {
			Sphere.draw(drawable, pipeline);
		}

		pipeline.setModelingMatrixUniform(gl);

		super.display(drawable, pipeline);
		pipeline.pop();
	}

}
