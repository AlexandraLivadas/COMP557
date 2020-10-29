package comp557.a2;

import java.awt.Component;

import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.vecmath.AxisAngle4d;
import javax.vecmath.Matrix3d;
import javax.vecmath.Matrix4d;
import javax.vecmath.Vector2d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector4d;

import com.jogamp.opengl.GLAutoDrawable;

import mintools.parameters.DoubleParameter;
import mintools.swing.VerticalFlowPanel;

/** 
 * Left Mouse Drag Arcball
 * @author kry
 * 
 * Student: Alexandra Livadas
 * ID: 260709168
 */
public class ArcBall {
		
	private DoubleParameter fit = new DoubleParameter( "Fit", 1, 0.5, 2 );
	private DoubleParameter gain = new DoubleParameter( "Gain", 1, 0.5, 2, true );
	
	/** The accumulated rotation of the arcball */
	Matrix4d R = new Matrix4d();
	
	private Vector3d prevPos = new Vector3d();
	private Vector3d currPos = new Vector3d();
	private AxisAngle4d quat = new AxisAngle4d();
	//private Vector4d quatVector = new Vector4d();
	private Matrix4d Rprev = new Matrix4d();
	private Vector2d center = new Vector2d();
	private double radius = 1.0;

	public ArcBall() {
		R.setIdentity();
		Rprev.setIdentity();
	}
	
	
	/** 
	 * Convert the x y position of the mouse event to a vector for your arcball computations 
	 * @param e
	 * @param v
	 */
	public void setVecFromMouseEvent( MouseEvent e, Vector3d v ) {
		Component c = e.getComponent();
		Dimension dim = c.getSize();
		double width = dim.getWidth();
		double height = dim.getHeight();
		int mousex = e.getX();
		int mousey = e.getY();
		
		// TODO: Objective 1: finish arcball vector helper function
		// Get radius and center
		Vector2d wh = new Vector2d(width, height);
		if (width > height) {
			radius = height/fit.getValue();
		}
		else {
			radius = width/fit.getValue();
		}
		center.x = width / 2.0;
		center.y = height / 2.0;
		// Taken from paper by Ken Shoemake
		v.x = (mousex - center.x) / radius;
		v.y = -1*((mousey - center.y) / radius); //INvert y
		double r = v.x*v.x + v.y*v.y;
		if (r > 1.0) {
			double s = 1.0 / Math.sqrt(r);
			v.x = s*v.x;
			v.y = s*v.y;
			v.z = 0.0;
		}
		else {
			v.z = Math.sqrt(1.0 - r);
		}

	}
	
	public void attach( Component c ) {
		c.addMouseMotionListener( new MouseMotionListener() {			
			@Override
			public void mouseMoved( MouseEvent e ) {}
			@Override
			public void mouseDragged( MouseEvent e ) {				
				if ( (e.getModifiersEx() & MouseEvent.BUTTON1_DOWN_MASK) != 0 ) {
					// TODO: Objective 1: Finish arcball rotation update on mouse drag when button 1 down!
					setVecFromMouseEvent(e, currPos);
					double angle = Math.acos(prevPos.dot(currPos)); 
					Vector3d cross = new Vector3d();
					cross.cross(prevPos, currPos); //Cross product of prev and curr positions = camera axis
					quat.set(cross, angle*gain.getValue()); //Set quaternion to axis and angle*gain
					prevPos.set(currPos);
					Rprev.set(quat);
					R.mul(Rprev);
				}
			}
		});
		c.addMouseListener( new MouseListener() {
			@Override
			public void mouseReleased( MouseEvent e) {}
			@Override
			public void mousePressed( MouseEvent e) {
				// TODO: Objective 1: arcball interaction starts when mouse is clicked
				setVecFromMouseEvent(e, prevPos);
				quat.set(new double[]{0.0, 0.0, 0.0, 0.0}); //Set initial value?
			}
			@Override
			public void mouseExited(MouseEvent e) {}
			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mouseClicked(MouseEvent e) {}
		});
	}
	
	public JPanel getControls() {
		VerticalFlowPanel vfp = new VerticalFlowPanel();
		vfp.setBorder( new TitledBorder("ArcBall Controls"));
		vfp.add( fit.getControls() );
		vfp.add( gain.getControls() );
		return vfp.getPanel();
	}
		
}
