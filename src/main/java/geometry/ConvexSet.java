package geometry;

import java.util.ArrayList;

public class ConvexSet {
	
	public static final double thresh = 0.00000000000001;

	// we store vectors which determine our convex set. These
	// sets always contain the origin so we don't need to consider
	// sign of the vectors really
	private final ArrayList<Halfspace2> halfspaces;
	
	//we'll also keep the vertices, but you only find these after 
	// computing the volume
	private final ArrayList<Vector2> vertices;

	public ConvexSet(final ArrayList<Halfspace2> halfs) {
		
		// destroy duplicates
		halfspaces = new ArrayList<>();
		
		for (Halfspace2 half : halfs) {
			boolean add = true;
			for (Halfspace2 check : halfspaces) {
				if (half.approxequals(check)) {
					add = false;
					break;
				}
			}
			
			if (add) halfspaces.add(half);
		}
		
		vertices = new ArrayList<>();
		//System.out.println(halfspaces.size());
		
	}
	
	public void addhalf(final Halfspace2 half) {
		halfspaces.add(half);
	}
	
	public double vol() {
		
		vertices.clear();
		
		// compute the volume using Kenny's Triangulation (TM) 
		// this will at the same time, parse any unused halfspaces?
		
		if (!isfeasible()) return -1; 
		
		// for each, find the two unique intersection points with
		// other halfspaces which are positive in all halfspaces.
		// there may be 0 of these, in which case this halfspace is not
		// needed for the convex set. So we parse it.
		
		double volume = 0;
		
		for (Halfspace2 space : halfspaces) {
			// find the boundary segment on this space.
			
			final ArrayList<Vector2> intervalends = new ArrayList<>();
			
			Vector2 rep = space.getRep();
			final double x1 = rep.x;
			final double y1 = rep.y;
			
			for (Halfspace2 cut : halfspaces) {
				//System.out.println(" space: " + space + " cut: " + cut);
				if (cut.equals(space)) continue;
				
				// note, if we want to have a lot of halfspaces
				// eventually, it will be better to start the indices
				// at the middle and move outwards.
				
				// The intersection of the two is at v+t v^perp, where
				// t is some polynomial of the coefs.
				
				// t = (x_2^2 - x_1 x_2 + y_2 (y_2 - y_1))/(x_2 y_1 - x_1 y_2) 
				
				final double x2 = cut.getX();
				final double y2 = cut.getY();
				
				// if the lines are parallel (or nearly) we do this:
				if (Math.abs((x2*y1) - (x1*y2)) <= thresh) {
					continue;
				}
				
				final double t =  ((x2*x2) - (x1*x2) + (y2*(y2 - y1)))/((x2*y1) - (x1*y2)) ;
				
				Vector2 meeting = rep.add((rep.perp()).scale(t));
				
				boolean intervalend = true;
				
				for (Halfspace2 checker : halfspaces) {
					
					if (checker.equals(space) || checker.equals(cut)) continue;
					
					//System.out.println("Checker: " + checker + " space: " + space + " cut: " + cut);
					
					if (!checker.contains(meeting)) {
						// if the meeting is not inside the halfspace, this 
						// is not an endpoint of the interval.
						intervalend = false;
						break;
					}
				}
				if (intervalend) {
					// later, we can just exit once there are two interval ends
					// since it's not possible to have more. But for now, let's
					// keep going to help debug
					
					boolean addit = true;
					
					for (Vector2 end : intervalends) {
						if (Math.abs(end.x-meeting.x) + Math.abs(end.y-meeting.y) <= thresh) {
							//System.out.println("nearly parallel");
							addit = false;
							break;
						}
					}
					if (addit) intervalends.add(meeting);
				}
			}
			
			
			if (intervalends.size() < 2) {
				// the set is probably unbounded
				continue;
			}
			else if (intervalends.size() > 2) {
				
				final Vector2 end1 = intervalends.get(0);
				Vector2 end2 = intervalends.get(0);
				
				for (int i = 1; i < intervalends.size(); i++) {
					if (!end1.approxequals(intervalends.get(i))) {
						end2 = intervalends.get(i);
						break;
					}
				}
				
				if (end1.approxequals(end2)) {
					System.out.println("Something is wrong: too many interval ends");
					for (Vector2 end : intervalends) System.out.println(end);
					return -1;
				}
				
				//otherwise, we can still use this interval!
				
				
			}
			// There are two interval ends, we can compute the area of this triangle.
			// It will be b*h/2, here b is the distance between the ends, h is 
			// the length of the halfpace vector.
			
			final double base = intervalends.get(0).dist(intervalends.get(1));
			
			volume += (base * space.translate);
			
			vertices.addAll(intervalends);
			
		}
		
		//System.out.println(minvoladded);
		
		return volume / 2.0;
	}
	
	
	// make sure to list the vertices in clockwise order, and that they are in convex 
	// position!
	public static ConvexSet fromVertices(final ArrayList<Vector2> verts) {
		
		// TODO
		
		return new ConvexSet(new ArrayList<>());
	}
	
	
	
	public boolean isfeasible() {
		// check whether this region is even bounded. This will be true
		// iff all angles are covered by the halfspaces.
		if (halfspaces.size() < 3) return false;
		
		// final double anglestart = halfspaces.get(0).direction;
		
		return true;
	}
	
	public boolean isinterior(final Vector2 v) {
		
		for (Halfspace2 space : halfspaces) {
			if ( !space.contains(v) ) return false;
		}
	
		return true;
	}
	
	public ArrayList<Vector2> getvertices() {
		return vertices;
	}
	
	public void refill(final ArrayList<Halfspace2> newhalfspaces) {
		halfspaces.clear();
		
		for (Halfspace2 half : newhalfspaces) {
			boolean add = true;
			for (Halfspace2 check : halfspaces) {
				if (half.approxequals(check)) {
					add = false;
					break;
				}
			}
			
			if (add) halfspaces.add(half);
		}
		
		vertices.clear();
		
	}
	
	public void replace(final int index, final Halfspace2 replacement) {
		if (index > halfspaces.size()) {
			System.out.println("Tried to replace with too high of index!");
			return;
		}
		halfspaces.remove(index);
		halfspaces.add(index, replacement);
	}
	
	public ConvexSet intersect(final ConvexSet C) {
		@SuppressWarnings("unchecked")
		final ArrayList<Halfspace2> spacesCopy = (ArrayList<Halfspace2>) halfspaces.clone();
		spacesCopy.addAll(C.getPlanes());
		
		return new ConvexSet(spacesCopy);
	}

	public ConvexSet reflect(final Halfspace2 reflect) {
		@SuppressWarnings("unchecked")
		final ArrayList<Halfspace2> spacesCopy = (ArrayList<Halfspace2>) halfspaces.clone();
		final ConvexSet refl = new ConvexSet(spacesCopy);
		final ArrayList<Halfspace2> reflspaces = refl.getPlanes();
		
		Vector2 doubletrans = reflect.getRep().scale(2);
		
		Vector2 refline = doubletrans.perp();
		
		for (int i = 0; i < reflspaces.size(); i++) {
			// need to reflect v in just direction first, then project v' + 2 dir onto it.
			
			final Vector2 vprime = Vector2.reflect(Vector2.Zero, refline, reflspaces.get(i).getRep());
			
			Vector2 vfinal = vprime.proj(vprime.add(doubletrans));
			
			final int sign = (int) Math.signum(Vector2.dot(vprime, vfinal));
			vfinal = vfinal.scale(sign);
			
			final Halfspace2 halvsie = Halfspace2.create(vfinal.x, vfinal.y, sign * vfinal.norm());
			
			refl.replace(i, halvsie);
		}
		
		return refl;
	}

	public ArrayList<Halfspace2> getPlanes() {
		return halfspaces;
	}
	
	// for drawing purposes: we get the rep vectors
	public ArrayList<Vector2> getReps() {
		final ArrayList<Vector2> reps = new ArrayList<>();
		
		for (Halfspace2 space: halfspaces) {
			reps.add(space.getRep());
		}
		
		return reps;
	}
	
	// Sourced from: http://www.sunshine2k.de/coding/java/Polygon/Convex/polygon.htm
	public static boolean checkConvexity(final ArrayList<Vector2> polygon) {
		if (polygon.size() < 3) return false;
		
		Vector2 p;
		Vector2 v;
		Vector2 u;
		double res = 0;
		for (int i = 0; i < polygon.size(); i++)
		{
			 p = polygon.get(i);
			 Vector2 tmp = polygon.get((i+1) % polygon.size());
			 v = Vector2.create(tmp.x - p.x, tmp.y - p.y);
			 u = polygon.get((i+2) % polygon.size());
		
			 if (i == 0) // in first loop direction is unknown, so save it in res
				 res = u.x * v.y - u.y * v.x + v.x * p.y - v.y * p.x;
			 else
			 {
				 double newres = u.x * v.y - u.y * v.x + v.x * p.y - v.y * p.x;
				 if ( (newres > 0 && res < 0) || (newres < 0 && res > 0) )
					 return false;
			 }
		}
		return true;
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
