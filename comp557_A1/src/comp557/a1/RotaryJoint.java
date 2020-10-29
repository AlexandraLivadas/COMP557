package comp557.a1;

import javax.vecmath.Tuple3d;
import javax.vecmath.Vector3d;

import com.jogamp.opengl.GL4;

import com.jogamp.opengl.GLAutoDrawable;

import mintools.parameters.DoubleParameter;

/*
 * Student Name: Alexandra Livadas
 * Student ID: 260709168
 */
public class RotaryJoint extends GraphNode{
	
	DoubleParameter rotation;
	Tuple3d translation;
	Tuple3d axis;
	
	public RotaryJoint(String name) {
		super(name);
		this.translation = new Vector3d(0.0, 0.0, 0.0);
		this.axis = new Vector3d(0, 1, 0);
		this.rotation = new DoubleParameter("rotation", 0, -180, 180);
	}
	
	public void setPosition(Tuple3d translation) {
		this.translation.set(translation);
	}
	
	public void setAxis(Tuple3d axis) {
		this.axis.set(axis);
	}
	
	public void setMinMax(int min, int max) {
		this.rotation = new DoubleParameter("rotation", 0, min, max);
		dofs.add(this.rotation);
	}
	
	@Override
	public void display(GLAutoDrawable drawable, BasicPipeline pipeline) {
		GL4 gl = drawable.getGL().getGL4();

		pipeline.push();
		
		pipeline.translate(translation.x, translation.y, translation.z);
		pipeline.rotate((rotation.getValue()*Math.PI)/180, axis.x, axis.y, axis.z);
		pipeline.setModelingMatrixUniform(gl);

		super.display(drawable, pipeline);
		pipeline.pop();
	}

}
