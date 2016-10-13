package perso.youri;


import javax.vecmath.Vector2d;
import javax.vecmath.Vector3d;

import org.graphstream.graph.Node;

public class Point3D {
	private double x;
	private double y;
	private double z;
	
	public Point3D(Node node){
		Object[] locationAttribute = node.getAttribute("xyz");
		affect((Double) locationAttribute[0], (Double) locationAttribute[1], (Double) locationAttribute[2]);
	}

	public Point3D(double x, double y, double z){
		affect(x, y, z);
	}

	private final void affect(double x, double y, double z){
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public double getX(){
		return x;
	}
	
	public double getY(){
		return y;
	}
	
	public double getZ(){
		return z;
	}
	
	public String toString(){
		return "("+this.x+" ; "+this.y+" ; "+this.z+")";
	}
	
	public double distance(Point3D c1,boolean troisD){
		if(troisD){
			return Math.sqrt( Math.pow(c1.getX()-this.getX(),2)+Math.pow(c1.getY()-this.getY(),2)+Math.pow(c1.getZ()-this.getZ(),2));
		}else{
			return Math.sqrt( Math.pow(c1.getX()-this.getX(),2)+Math.pow(c1.getY()-this.getY(),2));
		}
		
	}
	
	public boolean verificationDistance(Point3D pB, Point3D pC, double margeErreur, double ratio,boolean troisD){
		if(Math.abs(this.distance(pB, troisD)*ratio-pB.distance(pC,troisD))< seuilErreur(this.distance(pB,troisD), margeErreur)){
			return true;
		}
		return false;	
	}
	
	public static double seuilErreur(double nb, double margeErreur){
		return nb*margeErreur;
	}

	public Vector2d getVector2DTo(Point3D other) {
		return new Vector2d(other.getX()-this.getX(), other.getY()-this.getY());
	}
	
	public Vector3d getVector3DTo(Point3D other) {
		return new Vector3d(other.getX()-this.getX(), other.getY()-this.getY(), other.getZ()-this.getZ());
	}
}

