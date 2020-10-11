public class NBody{
	/** Given a file name as a String, it should return a double 
	corresponding to the radius of the universe in that file.*/
	public static double readRadius(String file){
		In in = new In(file);
		int number = in.readInt();
		double radius = in.readDouble();
		return radius;

	}

	public static Body[] readBodies(String file){
		In in = new In(file);
		int count = in.readInt();
		double radius = in.readDouble();
		Body[] list = new Body[count];

		for(int i=0; i<count; i++){
			double xxpos = in.readDouble();
			double yypos = in.readDouble();
			double xxvel = in.readDouble();
			double yyvel = in.readDouble();
			double mass = in.readDouble();
			String img = in.readString();
			list[i] = new Body(xxpos, yypos, xxvel, yyvel, mass, img);
		}
		return list;

	}


	public static void main(String[] args){
		double T=Double.parseDouble(args[0]); 
		double dt=Double.parseDouble(args[1]); 
		String filename = args[2];

		/*collecting all needed input*/
		Body[] bodies = readBodies(filename);
		double radius = readRadius(filename); 

		/*Drawing the background*/
		StdDraw.enableDoubleBuffering();
		StdDraw.setScale(-radius, radius);
		StdDraw.clear();

		String imageToDraw = "images/starfield.jpg";
		StdDraw.picture(0, 0, imageToDraw);
		StdDraw.show();


	}



} 




















