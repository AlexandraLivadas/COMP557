package comp557.a1;
 		  	  				   
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Scanner;

import javax.vecmath.Tuple3d;
import javax.vecmath.Vector3d;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import comp557.a1.GeometryNode.Shape;

/**
 * Loads an articulated character hierarchy from an XML file. 
 * Student Name: Alexandra Livadas
 * Student ID: 260709168
 */
public class CharacterFromXML {

	public static GraphNode load( String filename ) {
		try {
			InputStream inputStream = new FileInputStream(new File(filename));
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(inputStream);
			return createScene( null, document.getDocumentElement() ); // we don't check the name of the document elemnet
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Failed to load simulation input file.", e);
		}
	}
	
	/**
	 * Load a subtree from a XML node.
	 * Returns the root on the call where the parent is null, but otherwise
	 * all children are added as they are created and all other deeper recursive
	 * calls will return null.
	 */
	public static GraphNode createScene( GraphNode parent, Node dataNode ) {
        NodeList nodeList = dataNode.getChildNodes();
        for ( int i = 0; i < nodeList.getLength(); i++ ) {
            Node n = nodeList.item(i);
            // skip all text, just process the ELEMENT_NODEs
            if ( n.getNodeType() != Node.ELEMENT_NODE ) continue;
            String nodeName = n.getNodeName();
            GraphNode node = null;
            if ( nodeName.equalsIgnoreCase( "node" ) ) {
            	node = CharacterFromXML.createJoint( n );
            } else if ( nodeName.equalsIgnoreCase( "geom" ) ) {        		
        		node = CharacterFromXML.createGeom( n ) ;            
            } else {
            	System.err.println("Unknown node " + nodeName );
            	continue;
            }
            if ( node == null ) continue;
            // recurse to load any children of this node
            createScene( node, n );
            if ( parent == null ) {
            	// if no parent, we can only have one root... ignore other nodes at root level
            	return node;
            } else {
            	parent.add( node );
            }
        }
        return null;
	}
	
	/**​‌​​​‌‌​​​‌‌​​​‌​​‌‌‌​​‌
	 * Create a joint
	 * 
	 * Objective 8: XML, Adapt commented code in createJoint() to create your joint nodes when loading from xml
	 */
	public static GraphNode createJoint( Node dataNode ) {
		String type = dataNode.getAttributes().getNamedItem("type").getNodeValue();
		String name = dataNode.getAttributes().getNamedItem("name").getNodeValue();
		Tuple3d t;
		Tuple3d tMin;
		Tuple3d tMax;
		if ( type.equals("free") ) {
			FreeJoint joint = new FreeJoint( name );
			return joint;
		} else if ( type.equals("spherical") ) {
			// position is optional (ignored if missing) but should probably be a required attribute!​‌​​​‌‌​​​‌‌​​​‌​​‌‌‌​​‌
			// Could add optional attributes for limits (to all joints)

			SphericalJoint joint = new SphericalJoint( name );
			if ( (t=getTuple3dAttr(dataNode,"position")) != null ) joint.setPosition( t );
			tMin = getTuple3dAttr(dataNode,"min");
			tMax=getTuple3dAttr(dataNode,"max");
			if ( tMin != null && tMax != null) {
				joint.setMinMax(tMin, tMax);
			}
			else { //If min/max not specified, set to -180 to 180. 
				tMin = new Vector3d(-180, -180, 180);
				tMax = new Vector3d(180, 180, 180);
				joint.setMinMax(tMin, tMax);
			}
			return joint;
			
			
		} else if ( type.equals("rotary") ) {
			// position and axis are required... passing null to set methods
			// likely to cause an execption (perhaps OK)
			
			RotaryJoint joint = new RotaryJoint( name );
			joint.setPosition( getTuple3dAttr(dataNode,"position") );
			joint.setAxis( getTuple3dAttr(dataNode,"axis") );
			joint.setMinMax(getIntAttr(dataNode, "min"), getIntAttr(dataNode, "max"));
			return joint;
			
		} else {
			System.err.println("Unknown type " + type );
		}
		return null;
	}

	/**
	 * Creates a geometry DAG node 
	 * 
	 * TODO: Objective 5: Adapt commented code in greatGeom to create your geometry nodes when loading from xml
	 */
	public static GraphNode createGeom( Node dataNode ) {
		String type = dataNode.getAttributes().getNamedItem("type").getNodeValue();
		String name = dataNode.getAttributes().getNamedItem("name").getNodeValue();
		Tuple3d t;
		float[] f;
		if ( type.equals("box" ) ) {
			GeometryNode geom = new GeometryNode(name, Shape.CUBE);
			if ( (t=getTuple3dAttr(dataNode,"center")) != null ) geom.setCentre( t );
			if ( (t=getTuple3dAttr(dataNode,"scale")) != null ) geom.setScale( t );
			if ( (f=getFloatAttr(dataNode,"color")).length > 0) geom.setColor( f );
			return geom;
		} else if ( type.equals( "sphere" )) {
			GeometryNode geom = new GeometryNode( name , Shape.SPHERE);				
			if ( (t=getTuple3dAttr(dataNode,"center")) != null ) geom.setCentre( t );
			if ( (t=getTuple3dAttr(dataNode,"scale")) != null ) geom.setScale( t );
			if ( (f=getFloatAttr(dataNode,"color")).length > 0) geom.setColor( f );
			return geom;	
		} else {
			System.err.println("unknown type " + type );
		}
		return null;		
	}
	
	/**
	 * Loads tuple3d attributes of the given name from the given node.
	 * @param dataNode
	 * @param attrName
	 * @return null if attribute not present
	 */
	public static Tuple3d getTuple3dAttr( Node dataNode, String attrName ) {
		Node attr = dataNode.getAttributes().getNamedItem( attrName);
		Vector3d tuple = null;
		if ( attr != null ) {
			Scanner s = new Scanner( attr.getNodeValue() );
			tuple = new Vector3d( s.nextDouble(), s.nextDouble(), s.nextDouble() );			
			s.close();
		}
		return tuple;
	}
	
	/**
	 * Loads int attributes of the given name from the given node.
	 * Used to get min/max for RotaryJoint
	 * @param dataNode
	 * @param attrName
	 * @return null if attribute not present
	 */
	public static int getIntAttr( Node dataNode, String attrName ) {
		Node attr = dataNode.getAttributes().getNamedItem( attrName);
		int i = 0;
		if ( attr != null ) {
			Scanner s = new Scanner( attr.getNodeValue() );
			i = s.nextInt();		
			s.close();
		}
		return i;
	}
	
	/**
	 * Loads float attributes of the given name from the given node.
	 * Used to get color values for GeometryNodes.
	 * @param dataNode
	 * @param attrName
	 * @return null if attribute not present
	 */
	public static float[] getFloatAttr( Node dataNode, String attrName ) {
		Node attr = dataNode.getAttributes().getNamedItem( attrName);
		float[] f = new float[3];
		if ( attr != null ) {
			Scanner s = new Scanner( attr.getNodeValue() );
			f[0] = s.nextFloat();	
			f[1] = s.nextFloat();		
			f[2] = s.nextFloat();		
			s.close();
		}
		return f;
	}

}