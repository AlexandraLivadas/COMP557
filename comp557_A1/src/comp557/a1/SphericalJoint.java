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
public class SphericalJoint extends GraphNode {

	Tuple3d translation = null;
	Tuple3d min;
	Tuple3d max;
	DoubleParameter rotx;
	DoubleParameter roty;
	DoubleParameter rotz;

	
	public SphericalJoint(String name) {
		super(name);
		this.translation = new Vector3d(0.0, 0.0, 0.0); 
	}
	
	public void setPosition(Tuple3d translation) {
		this.translation.set(translation);
	}
	
	public void setMinMax(Tuple3d min, Tuple3d max) {
		dofs.add(this.rotx = new DoubleParameter("angleX", 0, min.x, max.x));
		dofs.add(this.roty = new DoubleParameter("angleY", 0, min.y, max.y));
		dofs.add(this.rotz = new DoubleParameter("angleZ", 0, min.z, max.z));
	}
	
	@Override
	public void display(GLAutoDrawable drawable, BasicPipeline pipeline) {
		GL4 gl = drawable.getGL().getGL4();

		pipeline.push();
		
		pipeline.translate(translation.x, translation.y, translation.z);
		pipeline.rotate((rotx.getValue()*Math.PI)/180, 1, 0, 0);
		pipeline.rotate((roty.getValue()*Math.PI)/180, 0, 1, 0);
		pipeline.rotate((rotz.getValue()*Math.PI)/180, 0, 0, 1);
		pipeline.setModelingMatrixUniform(gl);


		super.display(drawable, pipeline);
		pipeline.pop();
	}
}
