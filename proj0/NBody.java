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

		
		/* Using two loops to update with time to show animation*/
		/*Create a time variable to loop until T*/
		for(double t=0.0; t<=T; t=t+dt){
			
			/*Calculate net x_force and y_force for each body in the array.*/
			double[] xForces = new double[bodies.length];
			double[] yForces = new double[bodies.length];
			for(int i=0; i<bodies.length; i++){
				Body b = bodies[i];
				xForces[i] = b.calcNetForceExertedByX(bodies);
				yForces[i] = b.calcNetForceExertedByY(bodies);
			}
			/*Update the body paras after all net X/Y_forces are calculated.*/
			for(int i=0; i<bodies.length; i++){
				Body b = bodies[i];
				b.update(dt, xForces[i], yForces[i]);
			}

			/*Drawing the background*/
			StdDraw.enableDoubleBuffering();
			StdDraw.setScale(-radius, radius);
			StdDraw.clear();

			String imageToDraw = "images/starfield.jpg";
			StdDraw.picture(0, 0, imageToDraw);
			/*Drawing all the bodys.*/
			for(Body b : bodies){
				b.draw();
			}
			StdDraw.show();
			StdDraw.pause(20);

		}
		StdOut.printf("%d\n", bodies.length);
		StdOut.printf("%.2e\n", radius);
		for (int i = 0; i < bodies.length; i++) {
		    StdOut.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n",
		                  bodies[i].xxPos, bodies[i].yyPos, bodies[i].xxVel,
		                  bodies[i].yyVel, bodies[i].mass, bodies[i].imgFileName);   
		}
		



	}



} 




















