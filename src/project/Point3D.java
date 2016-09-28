package project;

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
	
	public double getDistanceWith(Point3D c1){
		return Math.sqrt( Math.pow(c1.getX()-this.getX(),2)+Math.pow(c1.getY()-this.getY(),2)+Math.pow(c1.getZ()-this.getZ(),2));
	}
	
	/**
	 * Checks if distance between this and p1 is same as between p1 and p2
	 * @param toleratedRatio percentage in double : 10% -> 0.1
	 */
	public boolean checkDistance(Point3D p1, Point3D p2, double toleratedRatio){
		return this.checkDistance(p1, p2, toleratedRatio, 1);
	}
	
	/**
	 * Checks if distance between this and p1 is same order as between p1 and p2
	 * @param toleratedRatio percentage in double : 10% -> 0.1
	 * @param distanceRatio number of distance units between p1 and p2 (unit is distance between this an p1)
	 */
	public boolean checkDistance(Point3D p1, Point3D p2, double toleratedRatio, int distanceRatio){
		double distanceDiff = Math.abs(this.getDistanceWith(p1)*distanceRatio-p1.getDistanceWith(p2));
		double errorLimit = this.getDistanceWith(p1)*toleratedRatio;
		if(distanceDiff < errorLimit){
			return true;
		}
		return false;	
	}
	
	/**
	 * @return vector from this to other point 
	 */
	public Vector3d getVectorTo(Point3D other) {
		return new Vector3d(other.getX()-this.getX(), other.getY()-this.getY(), other.getZ()-this.getZ());
	}
	
	public static void main(String[] args) {
		Point3D p1 = new Point3D(0,0,0);
		Point3D p2 = new Point3D(1,0,0);
		Point3D p3 = new Point3D(2,0,0);
		Point3D p4 = new Point3D(4,0,0);
		Point3D p6 = new Point3D(1,1,1);
		Point3D p7 = new Point3D(5,2,3);
		
		System.out.println(p1.getDistanceWith(p2));
		System.out.println(p1.getDistanceWith(p4));
		System.out.println(p2.getDistanceWith(p6)); // => racine de 2
		
		System.out.println(p1.checkDistance(p2, p3, 0.1));
		System.out.println(p1.checkDistance(p2, p4, 0.1));
		System.out.println(p2.checkDistance(p3, p4, 0.1));
		System.out.println(p2.checkDistance(p3, p4, 0.1, 2));
		System.out.println(p1.checkDistance(p2, p3, 0.1, 2));
		
		System.out.println(ProcessUtils.getAngle(p2, p6, p7));
	}

}

