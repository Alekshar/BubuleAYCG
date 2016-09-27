package perso.gatien;

public class Point3D {
	private double x;
	private double y;
	private double z;
	
	public Point3D(double x, double y, double z){
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
	
	public double distance(Point3D c1){
		return Math.abs(Math.sqrt( Math.pow(c1.getX()-this.getX(),2)+Math.pow(c1.getY()-this.getY(),2)+Math.pow(c1.getZ()-this.getZ(),2)));
	}
}
