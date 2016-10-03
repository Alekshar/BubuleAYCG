package perso.gatien;

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
	
	public double distance(Point3D c1){
		return Math.sqrt( Math.pow(c1.getX()-this.getX(),2)+Math.pow(c1.getY()-this.getY(),2)/*+Math.pow(c1.getZ()-this.getZ(),2)*/);
	}
	
	public boolean verificationDistance(Point3D pB, Point3D pC, double margeErreur, double ratio){
		if(Math.abs(this.distance(pB)*ratio-pB.distance(pC))< seuilErreur(this.distance(pB), margeErreur)){
			return true;
		}
		return false;	
	}
	
	public static double seuilErreur(double nb, double margeErreur){
		return nb*margeErreur;
	}

	public Vector2d getVectorTo(Point3D other) {
		return new Vector2d(other.getX()-this.getX(), other.getY()-this.getY()/*, other.getZ()-this.getZ()*/);
	}
	
	public static void main(String[] args) {
		Point3D p1 = new Point3D(0,0,0);
		Point3D p2 = new Point3D(1,0,0);
		Point3D p3 = new Point3D(2,0,0);
		Point3D p4 = new Point3D(4,0,0);
		Point3D p5 = new Point3D(5,0,0);
		Point3D p6 = new Point3D(1,1,1);
		Point3D p7 = new Point3D(5,2,3);
		
		System.out.println(p1.distance(p2));
		System.out.println(p1.distance(p4));
		System.out.println(p2.distance(p6)); // => racine de 2
		
		System.out.println(p1.verificationDistance(p2, p3, 0.1,1));
		System.out.println(p1.verificationDistance(p2, p4, 0.1,1));
		System.out.println(p2.verificationDistance(p3, p4, 0.1,1));
		System.out.println(p2.verificationDistance(p3, p4, 0.1,2));
		System.out.println(p1.verificationDistance(p2, p3, 0.1,2));
		
		
		System.out.println(ProcessUtils.getAngle(p2, p6, p7));
	}

}

