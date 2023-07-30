


public enum NodeExplanation {
	infocb,
	iters1,
	iters2,
	iters3,
	presets,
	outputbox,
	calcbutton,
	reflinebox,
	ratbox,
	plus,
	minus,
	printbtn,
	printmenu,
	loadbutton,
	R2search,
	R2longsearch,
	precisiondrag,
	graph,
	clickspace,
	Def
	;
	
	public static String syntax = "To load points, you must have on each line two numbers for the coordinates, "
			+ "separated by a comma, and have each vector separated by a newline. Any other symbols (such as "
			+ "brackets) are fine, the program will ignore them. You must have at least 3 vectors. An example "
			+ "of a proper load is:"
			+ "\n\n"
			+ "(3.3749532047534987, 46.087238864445794)\n"
			+ "(-2.922193755046752, -11.91503559672285)\n"
			+ "(2.04762663572186, -23.621444267800502)\n"
			+ "(199.79456384757057, -0.29513200787055305)\n";
	
	public static String info(NodeExplanation node) {
		
		switch(node) {
		
			case infocb:
				return "With the 'Info Mode' checkbox selected, you can click on anything (a button, textbox, or "
						+ "the graph or diagram) to get a detailed explanation of its purpose.\n\n"
						+ "This program computes the axiality of polygons, who can be controlled in the diagram "
						+ "to the right.";
				
			case iters1:
				return "iters1 controls the number of angles to try when calculating or searching. For the exact "
						+ "check on our shape, this number needs to be a little less than 50,000,000, but for other "
						+ "purposes you should have it set to much less than that. In conjunction with iters3, this "
						+ "number only needs to be around 1000 for a great deal of precision.";
				
			case iters2:
				return "iters2 controls the number of iterations in the translation selection. There is "
						+ "no fixed number that guarantees this check will work, as for a small error we need "
						+ "to see the search interval become very small. However, setting it to 80 will almost "
						+ "surely be enough, and one can increase it if needed. The program will display whether "
						+ "there were any unsuccessful translation searches after the calculation terminates.\n\n"
						+ "In general, 30 is enough for an accurate enough result. This advice may fail if your "
						+ "shape is fairly long and thin! You might need more iters2 in such cases.";
				
			case iters3:
				return "iters3 controls an auxiliary iteration which greatly speeds up calculations, "
						+ "however it is a non-convex optimization which makes it hard to guarantee error bounds "
						+ "while using it. For an exact check, set this to 0.\n\n"
						+ "For shape searching purposes, it is extremely useful. You need ~50 million in iters1 to do "
						+ "the precise check for example, but with only 400 iters1 and 30 iters3, you will very "
						+ "closely approximate the right value.";
				
			case presets:
				return "With Use Preset Iters selected, whenever you do anything I will automatically set the iters "
						+ "fields to values that will return "
						+ "an accurate enough result that doesn't take super long to compute. For example, Shape "
						+ "Search needs lower iters than Calculate, since Calculate is meant to be a more accurate "
						+ "check. Clicking and dragging in the picture window needs even less than that or it will "
						+ "feel very laggy. \n\n"
						+ "For more accurate searches, you should turn this off and raise the iters at your discretion. "
						+ "Each iters has a build it max, so you shouldn't be able to accidently set them too high and "
						+ "end up with a process that will take forever, except for the Calculate button. The max "
						+ "allowed iters on that one can take a few hours since we need that one for precision.";
				
			case outputbox:
				return "This large text area is a built in console. It will print some error messages if there are any, and "
						+ "other information.";
				
			case calcbutton:
				return "Calculate computes the best reflection line for the shape shown in the diagram to the right.\n\n"
						+ "With iters1=700, iters2=30, and iters3=30, this test should be reasonably precise. For the "
						+ "exact check, instead use iters1=50000000, iters2=80, and iters3=0, and be prepared to wait "
						+ "for up to 2 hours.";
				
			case reflinebox:
				return "The refline textbox will display the optimal reflection line, which is the dotted blue line "
						+ "in the diagram to the right.\n\n"
						+ "Note that the refline is given via a unit direction vector orthogonal to the line, and "
						+ "a quantity t that is the translation of the line along that unit vector's direction.";
				
			case ratbox:
				return "The rat textbox displays the calculated ratio of the area of the largest inscribed axially "
						+ "symmetric body to the area of the original convex body.";
				
			case plus:
				return "The plus button adds another halfplane to the diagram which can then be modified.";
				
			case minus:
				return "The minus button removes a halfplane from the diagram. If you have interacted with any of the "
						+ "halfplanes, it will default to remove the last one you touched.";
		
			case printbtn:
				return "The print button prints data into the built-in console, as well as your actual console. You "
						+ "can select what to print from the print menu next to this button.";
				
			case printmenu:
				return "The print menu has three options. You can print the control points for the set, which you "
						+ "will need if you would like to load your set later. You can print the vertices, which "
						+ "is helpful if you would like to investigate the set in other softwares. Finally, you can "
						+ "print the data from the graph below.";
				
			case loadbutton:
				return "You can copy and past a previously found convex set into the built in console. Pressing "
						+ "the load button will then replace the convex set in the diagram with that one. Note "
						+ "that you are loading using the control points (red), so do not enter the locations of "
						+ "vertices.\n\n" + syntax;
				
			case R2search:
				return "This runs our iterative search for shapes, starting from the polygon in the diagram. \n\n"
						+ "Even with Use Preset Iters selected, this process takes time! Thankfully there is a "
						+ "cancell button. Cancelling will merely revert back to the best shape found so far, so"
						+ "you don't lose any progress by doing this. \n\n"
						+ "You can run this process on our shape as well, but be aware that with much fewer iters "
						+ "used than are recommended for the complete check, it is somewhat likely that very tiny "
						+ "improvements are due to error, so you should check any improvements carefully.";
				
			case R2longsearch:
				return "This will fire the 'Search' button over and over until you decide to cancel it. ";
				
			case precisiondrag:
				return "With Precion Drag selected, you will be able to click and drag the red control points in the "
						+ "diagram much finer than usual, so you can modify your polygon with extreme care.";
				
			case graph:
				return "This graph displays the maximal symmetry ratio found in every direction tested. If you are "
						+ "testing more than 30,000 directions, it will not draw.\n\n"
						+ "It also shows the best symmetry found, which is the blue point in the graph.\n\n"
						+ "When you hover your mouse over the graph, it will show the best reflection line for that "
						+ "specific direction. Said best line should vary continuously as you move your mouse, but "
						+ "you'll find that if you draw a parallelogram this won't be the case! When you have "
						+ "parallel lines in your shape, there are potentially many different best lines for some "
						+ "directions. Rather than being 'unimodal', the function of symmetry per direction has a "
						+ "plateau at the top. Luckily, you can see that these directions are always worst than "
						+ "directions with a unique max.\n\n"
						+ "You can also click on the graph, to print the amount of symmetry from the displayed best"
						+ "line in that direction.";
				
			case clickspace:
				return "This diagram is a picture of the convex set which is tested by the program. The convex "
						+ "set is stored as an intersection of halfplanes. The halplanes are represented here by "
						+ "black lines with a red point on them. You can control the halfplanes by moving those red "
						+ "points. Note that each halfplane under your control contains the origin, which is the "
						+ "black point at the center of the diagram.";
				
			default:
				return "";
		}
		
		
		
	}
}

