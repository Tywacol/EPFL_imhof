package ch.epfl.imhof;

public class Vector3 {
    private final double x, y, z;
    
    public Vector3(double x, double y, double z) {
        this.x = x; this.y = y; this.z = z; 
    }
    
    public double norm() {
        return Math.sqrt(x*x + y*y + z*z);
    }
    
    public Vector3 normalized() { 
        return new Vector3(x/this.norm(), y/this.norm(), z/this.norm());
    }
    
    public double scalarProduct(Vector3 other) {
        return this.x*other.x + this.y*other.y + this.z*other.z;
    }
    
    // méthode pour tester HGTDigitalElevationModel
    public double y() { return y; }
    

}
