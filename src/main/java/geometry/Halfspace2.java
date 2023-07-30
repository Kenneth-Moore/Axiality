package geometry;

public class Halfspace2 {

	final Vector2 direction;
	final double translate;
	
	public Halfspace2(final Vector2 dir, final double trans) {
		direction = Vector2.normal(dir);
		translate = trans;
	}
	
	public static Halfspace2 create(final double x, final double y, final double t) {
		return new Halfspace2(Vector2.create(x, y), t);
	}
	
	public boolean contains(final Vector2 v) {
		// this method just checks if a vector is within one halfspace.
		// we can do that just by checking if the dot product
		// is larger than the norm of the halfspace vect.
		
		return Vector2.dot(v, direction) <= translate;
	}
	
	// these next two methods return the x and y coords of the nearest 
	// point to the origin on the halfspace
	public double getX() {
		return direction.x*translate;
	}
	
	public double getY() {
		return direction.y*translate;
	}
	
	public double gettrans() {
		return translate;
	}
	
	// Sometimes we want the vector nearest the origin in the halfspace,
	// this method gives that one.
	public Vector2 getRep() {
		return direction.scale(translate);
	}
	
	public static Halfspace2 fromRep(final Vector2 v) {
		return new Halfspace2(v, v.norm());
	}
	
	public boolean approxequals(final Halfspace2 other) {
		return Math.abs(this.direction.x - other.direction.x) <= 10*ConvexSet.thresh && 
			   Math.abs(this.direction.y - other.direction.y) <= 10*ConvexSet.thresh &&
			   Math.abs(this.translate   - other.translate  ) <= 10*ConvexSet.thresh;
	}
	
    @Override
    public boolean equals(final Object obj) {
        final Halfspace2 other = (Halfspace2) obj;
        return this.direction.x == other.direction.x && 
        	   this.direction.y == other.direction.y &&
        	   this.translate == other.translate;
    }
    
    @Override
    public String toString() {
    	return "[ " + direction.toString() + " " + translate + " ]";
    }
}
