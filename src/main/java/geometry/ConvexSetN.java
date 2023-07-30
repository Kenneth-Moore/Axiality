package geometry;

import java.util.ArrayList;

public class ConvexSetN {
	
	// we store vectors which determine our convex set. These
	// sets always contain the origin so we don't need to consider
	// sign of the vectors really
	private final ArrayList<HalfspaceN> halfspaces;
	
	public boolean degen;
	
	public ConvexSetN(final ArrayList<HalfspaceN> halfs) {
		//System.out.println("+");

		// destroy duplicates
		halfspaces = new ArrayList<>();
		degen = false;
		
		for (HalfspaceN half : halfs) {
			
			boolean add = true;
			for (HalfspaceN check : halfspaces) {
				
				// System.out.println(half + " approxeq " + check + "? " + half.approxequals(check));
				
				if (half.approxequals(check)) {
					
					degen = Math.abs( half.getTrans() -  check.getTrans()) < ConvexSet.thresh;
					
					// they approx equals in direction, but we still want to add if 
					// this one has the smaller translate
					check.setTrans(Math.min(half.getTrans(), check.getTrans()));
					
					add = false;
					break;
				} 
			}
			
			if (add) halfspaces.add(half);
		}
		
		//System.out.println("-");
	}
	
	/*
	public ConvexSetN intersect(final ConvexSetN C) {
		@SuppressWarnings("unchecked")
		final ArrayList<HalfspaceN> spacesCopy = (ArrayList<HalfspaceN>) halfspaces.clone();
		spacesCopy.addAll(C.getPlanes());
		
		return new ConvexSetN(spacesCopy);
	}
	*/
	
	public double vol(final boolean print) {
		
		if (print) System.out.println("------------");
		
		double volume = 0;
		
		for (int i = 0; i< halfspaces.size(); i++) {
			// new problem. find the vol of each face??
			
			final double t = halfspaces.get(i).getTrans();
			final ArrayList<Halfspace2> spaces = new ArrayList<>();
			
			// rotate and find all
			for (int j = 0; j < halfspaces.size(); j++) {
				if (i==j) continue;
				
				if (Math.abs(halfspaces.get(i).getDir().dot(halfspaces.get(j).getDir())) > 1-(ConvexSet.thresh/100)) {
					// nearly parallel!
					continue;
				}
				
				final VectorN rotated = VectorN.rotate(halfspaces.get(i).getDir(), halfspaces.get(j).getDir());
				final HalfspaceN rotatoe = new HalfspaceN(rotated, halfspaces.get(j).getTrans());
				
				final double[] zedvectcoords = {0,0, halfspaces.get(i).getTrans()};
				final VectorN zedvect = VectorN.create(zedvectcoords);
				
				final Vector2 planar = Vector2.create(rotated.get(0), rotated.get(1));
				
				final double newt = ((halfspaces.get(j).getTrans() 
						- (t * (rotated.get(2)))) / planar.norm());
				
				// If rotated contains the origin in its new 2D home, we wish for newt to be positive.
				// otherwise, negative.
				
				final boolean powersign = rotatoe.contains(zedvect);
				
				
				if (print) {
					System.out.println("- newt: " + newt + " powersign: " + powersign);
					
				
					System.out.println("i: " + i + " j: " + j + " newt " + newt + " planar: " + planar 
							+ " MO: " + (t * (rotated.get(2)) + ", " + halfspaces.get(j).getTrans() ));
				}
				spaces.add(Halfspace2.create(planar.x, planar.y, newt));
			}
			
			ConvexSet convi = new ConvexSet(spaces);
			
			if (print) System.out.println("\n face vol: " + convi.vol() 
					+ " i=" + i + " sp: " + halfspaces.get(i).getDir() + " tr: " + halfspaces.get(i).getTrans());
			// System.out.println( t * convi.vol() / 3);
			
			convi.getvertices();
			
			final double vol = convi.vol();
			
			volume += t * vol / 3;
		}

		// System.out.println(volume);
		
		return volume;
	}
	
	
	public boolean isfeasible() {
		// check whether this region is even bounded. This will be true
		// iff all angles are covered by the halfspaces.
		
		
		return true;
	}
	
	public boolean isinterior(final VectorN v) {
		
		for (HalfspaceN space : halfspaces) {
			if ( !space.contains(v) ) return false;
		}
	
		return true;
	}
	
	public void refill(final ArrayList<HalfspaceN> newhalfspaces) {
		halfspaces.clear();
		
		for (HalfspaceN half : newhalfspaces) {
			
			boolean add = true;
			for (HalfspaceN check : halfspaces) {
				
				// System.out.println(half + " approxeq " + check + "? " + half.approxequals(check));
				
				if (half.approxequals(check)) {
					
					// they approx equals in direction, but we still want to add if 
					// this one has the smaller translate
					check.setTrans(Math.min(half.getTrans(), check.getTrans()));
					
					add = false;
					break;
				} 
			}
			
			if (add) halfspaces.add(half);
		}
	}
	
	public void replace(final int index, final HalfspaceN replacement) {
		if (index > halfspaces.size()) {
			System.out.println("Tried to replace with too high of index!");
			return;
		}
		halfspaces.remove(index);
		halfspaces.add(index, replacement);
	}
	
	public ConvexSetN intersect(final ConvexSetN C) {
		@SuppressWarnings("unchecked")
		final ArrayList<HalfspaceN> spacesCopy = (ArrayList<HalfspaceN>) halfspaces.clone();
		
		//System.out.println(spacesCopy.get(0).getDir());
		
		spacesCopy.addAll(C.getPlanes());
		
		return new ConvexSetN(spacesCopy);
	}

	public ConvexSetN reflect(final HalfspaceN reflect) {
		@SuppressWarnings("unchecked")
		final ArrayList<HalfspaceN> spacesCopy = (ArrayList<HalfspaceN>) halfspaces.clone();
		final ConvexSetN refl = new ConvexSetN(spacesCopy);
		final ArrayList<HalfspaceN> reflspaces = refl.getPlanes();
		
		VectorN doubletrans = reflect.getRep().scale(2);
		
		for (int i = 0; i < reflspaces.size(); i++) {
			
			//System.out.println("reflect: " + reflect.getDir() + " rep: " + reflspaces.get(i).getRep());
			
			final VectorN vprime = VectorN.reflectorigin(reflect, reflspaces.get(i).getRep());

			VectorN vfinal = vprime.proj(vprime.add(doubletrans));

			final int sign = (int) Math.signum(vprime.dot(vfinal));
			vfinal = vfinal.scale(sign);
			
			final HalfspaceN halvsie = HalfspaceN.create(vfinal, sign * vfinal.norm());
			
			//System.out.println(halvsie.getRep() + " vprime: " + vprime + " vfinal : " + vfinal);

			refl.replace(i, halvsie);
		}

		//System.out.println("refl: " + refl.getPlanes().get(0).getDir());
		return refl;
	}
	
	public ArrayList<HalfspaceN> getPlanes() {
		return halfspaces;
	}
	
	// for drawing purposes: we get the rep vectors
	public ArrayList<VectorN> getReps() {
		final ArrayList<VectorN> reps = new ArrayList<>();
		
		for (HalfspaceN space: halfspaces) {
			reps.add(space.getRep());
		}
		
		return reps;
	}
	
    @Override
    public String toString() {
    	if (halfspaces.size() < 2) return "(unbounded)";
    	
    	String ans = "{ " + halfspaces.get(0).toString() + ",\n";
    	
		for (int i = 1; i < halfspaces.size() - 1; i++) {
			
			ans += "  " + halfspaces.get(i).toString() + ",\n";
		}
		
		ans += "  " + halfspaces.get(halfspaces.size()-1).toString() + " }";
    	
        return ans;
    }
	
}
