import java.lang.Math; // import the math package to do the math calculation.




public class Body {
	public double xxPos;
	public double yyPos;
	public double xxVel;
	public double yyVel;
	public double mass;
	public String imgFileName;


	/** Two Constructors for Body class. */
	public Body(double xP, double yP, double xV,
              double yV, double m, String img){
		xxPos = xP;
		yyPos = yP;
		xxVel = xV;
		yyVel = yV;
		mass = m;
		imgFileName = img;
	}

	public Body(Body b) {
		xxPos = b.xxPos;
		yyPos = b.yyPos;
		xxVel = b.xxVel;
		yyVel = b.yyVel;
		mass = b.mass;
		imgFileName = b.imgFileName;
	}


	/** Methods */

	/** This method calculated the distance between this body and the given body. */
	public double calcDistance(Body a){
		double r_square = Math.pow(a.xxPos-this.xxPos,2)
							+Math.pow(a.yyPos-this.yyPos,2);
		return Math.sqrt(r_square);
	}


	/** This method calculated the force exerted on this body by the given body. */
	public double calcForceExertedBy(Body a){
		double G = 6.67 * Math.pow(10, -11);
		double r = this.calcDistance(a);
		double force = G * this.mass * a.mass / Math.pow(r,2);
		return force;
	}

	/** This method calculated the force exerted on this body 
	by the given body in the X direction.
	The sign could be both. */
	public double calcForceExertedByX(Body a){
		double force = this.calcForceExertedBy(a);
		double r = this.calcDistance(a);
		double dx = a.xxPos - this.xxPos;
		double force_x = force * dx / r;
		return force_x;
	}
	
	/** This method calculated the force exerted on this body 
	by the given body in the Y direction.
	The sign could be both.*/
	public double calcForceExertedByY(Body a){
		double force = this.calcForceExertedBy(a);
		double r = this.calcDistance(a);
		double dy = a.yyPos - this.yyPos;
		double force_y = force * dy / r;
		return force_y;
	}

	
	/** This method calculated the net X force exerted by all bodies 
	in the give body array upon the current Body. */
	public double calcNetForceExertedByX(Body[] body_array){
		double force = 0;
		for (Body b : body_array){
			if(!this.equals(b)){
				force += this.calcForceExertedByX(b);
			}
		}
		return force;
	}

	/** This method calculated the net X force exerted by all bodies 
	in the give body array upon the current Body. */
	public double calcNetForceExertedByY(Body[] body_array){
		double force = 0;
		for (Body b : body_array){
			if(!this.equals(b)){
				force += this.calcForceExertedByY(b);
			}
		}
		return force;
	}


	/** This method calculates the updated position in dt time 
	with x-force fX and y-force fY.*/
	public void update(double dt, double fX, double fY){
		double aX = fX / this.mass;
		double aY = fY / this.mass;
		this.xxVel += dt * aX;
		this.yyVel += dt * aY;
		this.xxPos += dt * this.xxVel;
		this.yyPos += dt * this.yyVel; 
	}

}









































