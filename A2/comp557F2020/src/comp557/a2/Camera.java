package comp557.a2;

import javax.swing.JPanel;

import javax.vecmath.Matrix4d;
import javax.vecmath.Vector3d;

import mintools.parameters.DoubleParameter;
import mintools.parameters.Parameter;
import mintools.parameters.ParameterListener;
import mintools.parameters.Vec3Parameter;
import mintools.swing.VerticalFlowPanel;

/**
 * Camera class to be used both for viewing the scene, but also to draw the scene from 
 * a point light.
 * 
 * Student: Alexandra Livadas
 * ID: 260709168
 */
public class Camera {

	Vec3Parameter position = new Vec3Parameter("position", 0, 0, 10 );
	Vec3Parameter lookat = new Vec3Parameter("look at", 0, 0, 0 );
	Vec3Parameter up = new Vec3Parameter("up", 0, 1, 0 );
	
    DoubleParameter near = new DoubleParameter( "near plane", 1, 0.1, 10 );    
    DoubleParameter far = new DoubleParameter( "far plane" , 40, 1, 100 );    
    DoubleParameter fovy = new DoubleParameter( "fovy degrees" , 27, 14, 67 );  
    
    double l = 0.0;
    double r = 0.0;
    double t = 0.0;
    double b = 0.0;
	 
    /** Viewing matrix to be used by the pipeline */
    Matrix4d V = new Matrix4d();
    /** Projection matrix to be used by the pipeline */
    Matrix4d P = new Matrix4d();
    
    public Camera() {
    	near.addParameterListener( new ParameterListener<Double>() {			
			@Override
			public void parameterChanged(Parameter<Double> parameter) {
				// Let's keep near and far from crossing!
				if ( near.getValue() >= far.getValue() ) {
					far.setValue( near.getValue() + 0.1 );
				}
			}
		});
    	far.addParameterListener( new ParameterListener<Double>() {
    		@Override
    		public void parameterChanged(Parameter<Double> parameter) {
				// Let's keep near and far from crossing!
    			if ( far.getValue() <= near.getValue() ) {
					near.setValue( far.getValue() - 0.1 );
				}
    		}
		});
    }
    
    /**
     * Update the projection and viewing matrices
     * We'll do this every time we draw, though we could choose to more efficiently do this only when parameters change.
     * @param width of display window (for aspect ratio)
     * @param height of display window (for aspect ratio)
     */
    public void updateMatrix( double width, double height ) {
    	
    	// TODO: Objective 2: Replace the default viewing matrix with one constructed from the parameters available in this class!
    	Vector3d e = new Vector3d(new double[] {position.x, position.y, position.z}); //eye vector
    	Vector3d a = new Vector3d(new double[] {lookat.x, lookat.y, lookat.z}); //lookAt vector
    	Vector3d u = new Vector3d(new double[] {up.x, up.y, up.z}); //up vector
    	
    	//Get "w" vector
    	Vector3d zaxis = new Vector3d();
    	e.sub(a);
    	zaxis.normalize(e);
    	//Get "u" vector
    	Vector3d xaxis = new Vector3d();
    	xaxis.cross(u, zaxis);
    	xaxis.normalize();
    	//Get "v" vector
    	Vector3d yaxis = new Vector3d();
    	yaxis.cross(zaxis,  xaxis);
    	
    	Matrix4d m1 = new Matrix4d();
    	m1.set( new double[] {
    			xaxis.x, yaxis.x, zaxis.x, position.x,
    			xaxis.y, yaxis.y, zaxis.y, position.y,
    			xaxis.z, yaxis.z, zaxis.z, position.z,
    			0, 		 0, 	  0, 	   1
    	});
    	m1.invert();
    	V.set(m1);

    	// TODO: Objective 3: Replace the default projection matrix with one constructed from the parameters available in this class!
        //t = tan(fov/2)*f
        //b = -t
        //r = t*a
        //l = b*a
        double aspectRatio = width/height;
        double n = near.getValue();
        double f = far.getValue();
        double angle = Math.toRadians(fovy.getValue()/2);
        t = Math.tan(angle)*n;
        b = -t;
        r = t * aspectRatio;
        l = -r;
        Matrix4d m4 = new Matrix4d();
        m4.set(new double[] {
        		2*n/(r-l), 0,         (r+l)/(r-l), 0,
        		0,         2*n/(t-b), (t+b)/(t-b), 0,
        		0,         0,         (f+n)/(n-f), 2*n*f/(n-f),
        		0,         0,          -1,         0
        });
        P.set(m4);
    }
    
    /**
     * @return controls for the camera
     */
    public JPanel getControls() {
        VerticalFlowPanel vfp = new VerticalFlowPanel();
        vfp.add( position );
        vfp.add( lookat );
        vfp.add( up );
        vfp.add( near.getControls() );
        vfp.add( far.getControls() );
        vfp.add( fovy.getControls() );
        return vfp.getPanel();
    }
	
}
