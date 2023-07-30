package geometry;

public class VectorN {

	final int dim;
	private final double[] values;
	
	private VectorN(double[] inputs) {
		
		values = inputs;
		dim = inputs.length;
		
		
	}
	
	public static VectorN create(double[] inputs) {
		return new VectorN(inputs);
	}
	
	public static VectorN create(double x, double y, double z) {
		double[] inputs = {x,y,z};
		return new VectorN(inputs);
	}
	
	public static VectorN Zero(int N) {
		final double[] in = new double[N];
		return new VectorN(in);
	}
	
	// this - v
    public VectorN sub(final VectorN v) {
    	
    	final double[] output = new double[dim];
    	
    	for (int i=0; i<dim; i++) output[i] = this.values[i] - v.vals()[i];
    	
        return VectorN.create(output);
    }

    // this + v
    public VectorN add(final VectorN v) {
    	final double[] output = new double[dim];
    	
    	for (int i=0; i<dim; i++) output[i] = this.values[i] + v.vals()[i];
    	
        return VectorN.create(output);
    }

    // scale * this
    public VectorN scale(final double scale) {
    	final double[] output = new double[dim];
    	
    	for (int i=0; i<dim; i++) output[i] = this.values[i] * scale;
    	
        return VectorN.create(output);
    }
    
    public double norm() {
    	double ans = 0;
    	
    	for (int i=0; i<dim; i++) ans += Math.pow(values[i], 2); 
    	
        return Math.sqrt(ans);
    }
    
    public double normN() {
    	double ans = 0;
    	
    	for (int i=0; i<dim; i++) {
    		//System.out.println("vlas " + i + " = " + values[i]);
    		ans += Math.pow(values[i], 2); 
    	}
    	
        return ans;
    }
    
    public double dist(final VectorN v) {
    	return (this.sub(v)).norm();
    }
    
    //project v onto this vector
    public VectorN proj(final VectorN v) {
    	
    	double scal = (this.dot(v))/(this.normN());
    	return this.scale(scal);
    }
    
    public VectorN planeproj(final HalfspaceN plane) {
    	// to project onto the plane, we project onto the normal vector, and 
    	// then subtract that from the vector. then, we translate it by the 
    	// same translation the plane has...
    	
    	final VectorN vprime = this.proj(plane.direction);
    	
    	final double scale = vprime.norm() - plane.getTrans();
    	
    	return this.sub(VectorN.normal(vprime).scale(scale));
    }
    
    public static VectorN normal(final VectorN v) {
    	return v.scale(1 / v.norm());
    }

    public double dot(final VectorN v) {
    	double output = 0;
    	
    	for (int i=0; i<dim; i++) output += values[i] * v.vals()[i];
    	
        return output;
    }

    public static double cross(final Vector2 v, final Vector2 w) {
        return v.x * w.y - v.y * w.x;
    }
    
    public double[] vals() {
    	return values;
    }
    
	public static VectorN cross(final VectorN vect0, final VectorN vect1) {
		double[] v0 = vect0.vals();
		double[] v1 = vect1.vals();

        double crossProduct[] = new double[3];

        crossProduct[0] = v0[1] * v1[2] - v0[2] * v1[1];
        crossProduct[1] = v0[2] * v1[0] - v0[0] * v1[2];
        crossProduct[2] = v0[0] * v1[1] - v0[1] * v1[0];
        
        return VectorN.create(crossProduct);
	}
    
    
    // for r3 use only!
    public VectorN rotateZ(final double theta) {
    	final double[] rotatoe = new double[3];
    	
    	rotatoe[0] = (Math.cos(theta) * this.values[0]) - (Math.sin(theta) * this.values[1]);
    	rotatoe[1] = (Math.sin(theta) * this.values[0]) + (Math.cos(theta) * this.values[1]);
    	rotatoe[2] = this.values[2];
    	return VectorN.create(rotatoe);
    }
    
    // for r3 use only!
    public VectorN rotateX(final double theta) {
    	final double[] rotatoe = new double[3];
    	
    	rotatoe[0] = this.values[0];
    	rotatoe[1] = (Math.cos(theta)*this.values[1] )  - (Math.sin(theta)*this.values[2]);
    	rotatoe[2] = ((Math.sin(theta)*this.values[1]) +(Math.cos(theta)*this.values[1]));
    	return VectorN.create(rotatoe);
    }
    
    //sends z to the z axis, and you get vect back, rotated!
    public static VectorN rotate(final VectorN z, final VectorN vect) {
    	final double[] valuez = {0,0,1};
    	
    	final VectorN d = VectorN.create(valuez);
    	
    	final double[] v = VectorN.cross(z, d).vals();
    	final double c = z.dot(d);
    	
    	if (c == -1) {
    		final double[] newz = {-vect.get(0), vect.get(1), -vect.get(2)};
    		return VectorN.create(newz);
    	}
    	
    	final double k = 1.0/(1.0+c);
    	
    	
    	double p = (v[0] * v[0] * k + c)    * vect.vals()[0] + (v[1] * v[0] * k - v[2]) * vect.vals()[1] + (v[2] * v[0] * k + v[1]) * vect.vals()[2];
    	double q = (v[0] * v[1] * k + v[2]) * vect.vals()[0] + (v[1] * v[1] * k + c)    * vect.vals()[1] + (v[2] * v[1] * k - v[0]) * vect.vals()[2];
    	double r = (v[0] * v[2] * k - v[1]) * vect.vals()[0] + (v[1] * v[2] * k + v[0]) * vect.vals()[1] + (v[2] * v[2] * k + c)    * vect.vals()[2];
    	
    	final double[] ans = {p, q, r};
    	
    	return VectorN.create(ans);
    }
    
    
    public static VectorN reflectorigin(final HalfspaceN plane, final VectorN vect) {
    	
		//System.out.println("reflect: " + reflect.getDir() + " rep: " + reflspaces.get(i).getRep());

    	final VectorN proj = plane.getDir().proj(vect);
    	final VectorN vprime = vect.sub(proj.scale(2));
    	// System.out.println("in reflectorig: proj: " + proj + " vprime: " + vprime);
    	return vprime;
    	
    }
    
    public double get(int i) {
    	try {
    		return values[i];
    	} catch (ArrayIndexOutOfBoundsException e) {
    		System.out.println("VectorN 'get' error: " + this + ", index: " +i);
    		return 0;
    	}
    }
    
    public static Vector2 fromangle(final double angle) {
    	return Vector2.create(Math.cos(angle), Math.sin(angle));
    }
    
    @Override
    public String toString() {
    	String strs = "(" + values[0];
    	
    	for (int i = 1; i < dim; i++) strs +=  ", " + values[i];
    	
        return strs + ")";
    }
    
}
