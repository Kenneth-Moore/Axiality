package geometry;

public class HalfspaceN {

	public final VectorN direction;
	private double translate;
	
	public HalfspaceN(final VectorN dir, final double trans) {
		direction = VectorN.normal(dir);
		translate = trans;
	}
	
	public static HalfspaceN create(final double[] dir, final double t) {
		return new HalfspaceN(VectorN.create(dir), t);
	}
	
	public static HalfspaceN create(VectorN dir, final double t) {
		return new HalfspaceN(dir, t);
	}
	

	public static HalfspaceN fromVerts(final VectorN v1, final VectorN v2, final VectorN v3) {
		
		VectorN dir = VectorN.normal(VectorN.cross(v2.sub(v1), v3.sub(v1)));
		
		double t = v1.dot(dir);
		
		// if t is negative, that means our orientation was reversed. so, the vector is:
		if (t < 0) {
			dir = dir.scale(-1);
			t = t * -1;
		}
		
		return new HalfspaceN(dir, t);
	}
	
	
	public boolean contains(final VectorN v) {
		// this method just checks if a vector is within one halfspace.
		// we can do that just by checking if the dot product
		// is larger than the norm of the halfspace vect.
		
		return direction.dot(v) <= translate;
	}
	
	public void setTrans(final double newtrans) {
		translate = newtrans;
	}
	
	public double getTrans() {
		return translate;
	}
	
	// Sometimes we want the vector nearest the origin in the halfspace,
	// this method gives that one.
	public VectorN getRep() {
		return direction.scale(translate);
	}
	
	public VectorN getDir() {
		return direction;
	}
	
	public static HalfspaceN fromRep(final VectorN v) {
		return new HalfspaceN(v, v.norm());
	}
	
	public boolean approxequals(final HalfspaceN other) {
		
		//System.out.println(this.direction.dot(other.direction));
		if (this.direction.dot(other.direction) < 1-(ConvexSet.thresh/10)) return false;
		
		// the directions are the same. So, if the translates have the same sign, return true!
		return this.translate * other.translate > 0;
	}
	
    @Override
    public boolean equals(final Object obj) {
        final HalfspaceN other = (HalfspaceN) obj;
                
        return (translate == other.translate) && direction.equals(other.direction);
    }
    
    @Override
    public String toString() {
    	return "[ " + direction.toString() + " " + translate + " ]";
    }
}
