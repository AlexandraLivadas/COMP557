package mintools.viewer;

import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.vecmath.Matrix4d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector4d;

import com.jogamp.opengl.GLAutoDrawable;

import mintools.parameters.BooleanParameter;
import mintools.parameters.DoubleParameter;
import mintools.swing.VerticalFlowPanel;
import mintools.viewer.geom.FancyAxis;
import mintools.viewer.geom.QuadWithTexCoords;
import mintools.viewer.geom.WireCube;

public class PointLightCamera extends Camera {	

    public BooleanParameter debugLightFrustum = new BooleanParameter( "debug light frustum" , false );
    public DoubleParameter sigma = new DoubleParameter( "self shadowing offset", 0.0015, 0, 0.002 );
	
    public PointLightCamera() {
    	super();
    	position.set( new Vector3d(3, 3, 3) );
    	fovy.setDefaultValue(55.0);
    }
    
    public void getPositionInWorld( Vector4d pos ) {
    	pos.set( position.x, position.y, position.z, 1 );
    }
    
    Matrix4d Vinv = new Matrix4d();
    Matrix4d Pinv = new Matrix4d();
    
    public void draw( GLAutoDrawable drawable, ShadowPipeline pipeline ) {
    	if ( !debugLightFrustum.getValue() ) return;
		Vinv.invert( V ); // is rigid, could have a smarter inverse
		Pinv.invert( P );
		pipeline.push();
		pipeline.disableLighting(drawable);
		pipeline.multMatrix( drawable, Vinv );
		FancyAxis.draw(drawable, pipeline);

		pipeline.push();
		pipeline.multMatrix( drawable, Pinv );
		pipeline.setkd( drawable, 1, 1, 1 );
		WireCube.draw( drawable, pipeline );
		pipeline.pop(drawable);

		pipeline.debugLightTexture(drawable);
		pipeline.translate( drawable, 0, 0, -near.getValue() );
		pipeline.scale( drawable, r, r, 1 ); // assume it is square !
		
		QuadWithTexCoords.draw( drawable, pipeline );
		
		pipeline.pop(drawable);
    }
    
    /**
     * @return controls for the shadow mapped light
     */
    public JPanel getControls() {
        VerticalFlowPanel vfp = new VerticalFlowPanel();
        vfp.setBorder(new TitledBorder("Point Light Camera Controls"));
        vfp.add( super.getControls() );
        vfp.add( sigma.getControls() );
        vfp.add( debugLightFrustum.getControls() );        
        return vfp.getPanel();
    }
    
}
