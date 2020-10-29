package comp557.a1;

import javax.swing.JTextField;

import javax.vecmath.Vector3d;

import comp557.a1.GeometryNode.Shape;
import mintools.parameters.BooleanParameter;

/*
 * Student Name: Alexandra Livadas
 * Student ID: 260709168
 */
public class CharacterMaker {

	static public String name = "Alexandra Livadas, 260709168";
	
	// Objective 8: change default of load from file to true once you start working with xml
	static BooleanParameter loadFromFile = new BooleanParameter( "Load from file (otherwise by procedure)", true );
	static JTextField baseFileName = new JTextField("data/a1data/character");
	
	/**
	 * Creates a character, either procedurally, or by loading from an xml file
	 * @return root node
	 */
	static public GraphNode create() {
		
		if ( loadFromFile.getValue() ) {
			// Objectives 6: create your character in the character.xml file 
			return CharacterFromXML.load( baseFileName.getText() + ".xml");
		} else {
			// Objective 3,4,5,6: test DAG nodes by creating a small DAG in the CharacterMaker.create() method 
						
			// Use this for testing, but ultimately it will be more interesting
			// to create your character with an xml description (see example).
			
			// Here we just return null, which will not be very interesting, so write
			// some code to create a test or partial character and return the root node.

			FreeJoint root = new FreeJoint("root");
			
			RotaryJoint neckRot = new RotaryJoint("Neck Rotation");
			neckRot.setPosition(new Vector3d(0, 0.3, 0));
			neckRot.setAxis(new Vector3d(0, 1, 0));
			neckRot.setMinMax(-90, 90);
			SphericalJoint headSphere = new SphericalJoint("Head Rotation");
			headSphere.setPosition(new Vector3d(0,0.4,0));
			headSphere.setMinMax(new Vector3d(-90, 0, -90), new Vector3d(90, 90, 90));

		
			SphericalJoint leftShoulder = new SphericalJoint("leftShoulder");
			leftShoulder.setPosition(new Vector3d(0.6,0.3,0));
			leftShoulder.setMinMax(new Vector3d(-180, -90, -180), new Vector3d(180, 90, 180));
			SphericalJoint rightShoulder = new SphericalJoint("rightShoulder");
			rightShoulder.setPosition(new Vector3d(-0.6,0.3,0));
			rightShoulder.setMinMax(new Vector3d(-180, -90, -180), new Vector3d(180, 90, 180));
			SphericalJoint leftElbow = new SphericalJoint("leftElbow");
			leftElbow.setPosition(new Vector3d(0,-1,0.1));
			leftElbow.setMinMax(new Vector3d(-180, 0, -180), new Vector3d(0, 0, 180));
			SphericalJoint rightElbow = new SphericalJoint("rightElbow");
			rightElbow.setPosition(new Vector3d(0,-1,0.1));
			rightElbow.setMinMax(new Vector3d(-180, 0, -180), new Vector3d(0, 0, 180));

			RotaryJoint hipBone = new RotaryJoint("hipBone");
			hipBone.setAxis(new Vector3d(0,1,0));
			hipBone.setPosition(new Vector3d(0,-0.63,0));
			hipBone.setMinMax(-90, 90);
			
			SphericalJoint upperLeftLegBone = new SphericalJoint("leftUpperLegBone");
			upperLeftLegBone.setPosition(new Vector3d(0.3, -1.7, 0.1));
			upperLeftLegBone.setMinMax(new Vector3d(-90, -90, -90), new Vector3d(0, 90, 90));
			SphericalJoint upperRightLegBone = new SphericalJoint("rightUpperLegBone");
			upperRightLegBone.setPosition(new Vector3d(-0.3, -1.7, 0.1));
			upperRightLegBone.setMinMax(new Vector3d(-90, -90, -90), new Vector3d(90, 90, 90));		
			SphericalJoint leftKneeBone = new SphericalJoint("Left Knee");
			leftKneeBone.setPosition(new Vector3d(0, -1, 0.1));
			leftKneeBone.setMinMax(new Vector3d(0, -90, -90), new Vector3d(90, 90, 90));
			SphericalJoint rightKneeBone = new SphericalJoint("Right Knee");
			rightKneeBone.setPosition(new Vector3d(0, -1 , 0.1));
			rightKneeBone.setMinMax(new Vector3d(0, -90, -90), new Vector3d(90, 90, 90));
			SphericalJoint leftFootBone = new SphericalJoint("Left Foot");
			leftFootBone.setPosition(new Vector3d(0, -0.7, 1));
			leftFootBone.setMinMax(new Vector3d(-10, -20, -20), new Vector3d(10, 20, 20));
			SphericalJoint rightFootBone = new SphericalJoint("Right Foot");
			rightFootBone.setPosition(new Vector3d(0, -0.7 , 1));
			rightFootBone.setMinMax(new Vector3d(-10, -20, -20), new Vector3d(10, 20, 20));

			float[] red = {0.89f, 0.21f, 0.22f};
			float[] green = {0.0f, 0.93f, 0.0f};
			float[] blue = {0.0f, 0.5f, 1.0f};
			
			GeometryNode torso = new GeometryNode("torso", Shape.CUBE);
			torso.setCentre(new Vector3d(0,0,0));
			torso.setColor(red);
			torso.setScale(new Vector3d(0.7,1,0.6));
			GeometryNode neck = new GeometryNode("neck", Shape.CUBE);
			neck.setCentre(new Vector3d(0,0.3,0));
			neck.setScale(new Vector3d(0.2,0.2,0.2));
			neck.setColor(green);
			GeometryNode head = new GeometryNode("head", Shape.SPHERE);
			head.setCentre(new Vector3d(0,0.4,0));
			head.setScale(new Vector3d(1.3,1.3,1.3));
			head.setColor(blue);
			GeometryNode leftUpperArm = new GeometryNode("leftUpperArm", Shape.CUBE);
			leftUpperArm.setCentre(new Vector3d(0.1,-0.1,0));
			leftUpperArm.setScale(new Vector3d(0.3,0.6,0.2));
			leftUpperArm.setColor(green);
			GeometryNode rightUpperArm = new GeometryNode("rightUpperArm", Shape.CUBE);
			rightUpperArm.setCentre(new Vector3d(-0.1,-0.1,0));
			rightUpperArm.setScale(new Vector3d(0.3,0.6,0.2));
			rightUpperArm.setColor(green);
			GeometryNode rightLowerArm = new GeometryNode("rightLowerArm", Shape.CUBE);
			rightLowerArm.setCentre(new Vector3d(0,0,0));
			rightLowerArm.setScale(new Vector3d(1,1,1));
			rightLowerArm.setColor(blue);
			GeometryNode leftLowerArm = new GeometryNode("leftLowerArm", Shape.CUBE);
			leftLowerArm.setCentre(new Vector3d(0,0,0));
			leftLowerArm.setScale(new Vector3d(1,1,1));
			leftLowerArm.setColor(blue);

			GeometryNode hip = new GeometryNode("hip", Shape.CUBE);
			hip.setCentre(new Vector3d(0,0,0));
			hip.setScale(new Vector3d(1, 0.3, 0.6));
			hip.setColor(red);
			GeometryNode leftUpperLeg = new GeometryNode("leftUpperLeg", Shape.CUBE);
			leftUpperLeg.setCentre(new Vector3d(0.1,-0.1,0));
			leftUpperLeg.setScale(new Vector3d(0.4,2.5,0.4));
			leftUpperLeg.setColor(green);
			GeometryNode rightUpperLeg = new GeometryNode("rightUpperLeg", Shape.CUBE);
			rightUpperLeg.setCentre(new Vector3d(-0.1,-0.1,0));
			rightUpperLeg.setScale(new Vector3d(0.4,2.5,0.4));
			rightUpperLeg.setColor(green);
			GeometryNode leftLowerLeg = new GeometryNode("leftLowerLeg", Shape.CUBE);
			leftLowerLeg.setCentre(new Vector3d(0,0,0));
			leftLowerLeg.setScale(new Vector3d(1,1,1));
			leftLowerLeg.setColor(blue);
			GeometryNode rightLowerLeg = new GeometryNode("rightLowerLeg", Shape.CUBE);
			rightLowerLeg.setCentre(new Vector3d(0,0,0));
			rightLowerLeg.setScale(new Vector3d(1,1,1));
			rightLowerLeg.setColor(blue);
			GeometryNode leftFoot = new GeometryNode("leftFoot", Shape.CUBE);
			leftFoot.setCentre(new Vector3d(0,0,0));
			leftFoot.setScale(new Vector3d(1,0.3,5));
			leftFoot.setColor(green);
			GeometryNode rightFoot = new GeometryNode("rightFoot", Shape.CUBE);
			rightFoot.setCentre(new Vector3d(0,0,0));
			rightFoot.setScale(new Vector3d(1,0.3,5));
			rightFoot.setColor(green);

			root.add(torso);
			torso.add(neckRot);
			neckRot.add(neck);
			neck.add(headSphere);
			headSphere.add(head);
			
			torso.add(leftShoulder);
			leftShoulder.add(leftUpperArm);
			leftUpperArm.add(leftElbow);
			leftElbow.add(leftLowerArm);
			torso.add(rightShoulder);
			rightShoulder.add(rightUpperArm);
			rightUpperArm.add(rightElbow);
			rightElbow.add(rightLowerArm);
			
			torso.add(hipBone);
			hipBone.add(hip);
			
			hip.add(upperLeftLegBone);
			upperLeftLegBone.add(leftUpperLeg);
			leftUpperLeg.add(leftKneeBone);
			leftKneeBone.add(leftLowerLeg);
			leftLowerLeg.add(leftFootBone);
			leftFootBone.add(leftFoot);
			
			hip.add(upperRightLegBone);
			upperRightLegBone.add(rightUpperLeg);
			rightUpperLeg.add(rightKneeBone);
			rightKneeBone.add(rightLowerLeg);
			rightLowerLeg.add(rightFootBone);
			rightFootBone.add(rightFoot);
			return root;
		
		}
	}
}
