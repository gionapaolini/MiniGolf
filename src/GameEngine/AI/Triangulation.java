package GameEngine.AI;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import org.lwjgl.util.vector.Vector3f;


public class Triangulation {
	
	Ray direct; 
	Vector3f wall, holeToWall;
	Vector3f hole, ball;
	
	public Triangulation(Vector3f ball,Vector3f hole, float x,float z){
		direct = new Ray(ball, hole);
		this.hole = hole;
		this.ball = ball;
		System.out.println("ball: " + ball);
		
		//quadrant I
		if(ball.x >= 0 && ball.z >= 0){
			if(x-ball.x > z-ball.z){
				wall= new Vector3f(x,0,ball.z);
				holeToWall = new Vector3f(x,0,hole.z);
				System.out.println(wall.toString()+ "Wall");
			}else {
				wall= new Vector3f(ball.x,0,z);
				holeToWall = new Vector3f(hole.x,0,z);
				System.out.println(wall.toString()+ "Wall");
			}
			System.out.println("Q1");
		}
		//quadrant II
		else if(ball.x <0 && ball.z >= 0){
			if(x-ball.x > z-ball.z){
				wall= new Vector3f(-x,0,ball.z);
				holeToWall = new Vector3f(-x,0,hole.z);
				System.out.println(wall.toString()+ "Wall");
			}else {
				wall= new Vector3f(ball.x,0,z);
				holeToWall = new Vector3f(hole.x,0,z);
				System.out.println(wall.toString()+ "Wall");
			}
			System.out.println("Q2");
		}
		//quadrant III
		else if (ball.x <0 && ball.z < 0){
			if(x-ball.x > z-ball.z){
				wall= new Vector3f(-x,0,ball.z);
				holeToWall = new Vector3f(-x,0,hole.z);
				System.out.println(wall.toString() + "Wall");
			}else {
				wall= new Vector3f(ball.x,0,-z);
				holeToWall = new Vector3f(hole.x,0,-z);
				System.out.println(wall.toString()+ "Wall");
			}
			System.out.println("Q3");
		}
		
		//quadrant IV
		else if(ball.x >= 0 && ball.z < 0){
			if(Math.abs(x-ball.x) > Math.abs(z-ball.z)){
				wall= new Vector3f(x,0,ball.z);
				holeToWall = new Vector3f(x,0,hole.z);
				System.out.println(wall.toString()+ "Wall");
			}else {
				wall= new Vector3f(ball.x,0,-z);
				holeToWall = new Vector3f(hole.x,0,-z);
				System.out.println(wall.toString()+ "Wall");
			}
			System.out.println("Q4");
		}
		else wall = hole;
	}
	
	public Vector3f reflectionPoint(){
		Ray a = new Ray(hole,wall);
		Ray b = new Ray(ball, holeToWall);
		Point2D.Float inter = getIntersectionPoint(a,b);
		System.out.println("intesection: " + inter.toString());
		Vector3f refl;
		if(wall.x == holeToWall.x){
			refl =new Vector3f(wall.x,0,inter.y);
			System.out.println("Bounce x");
			}
		else{
			refl = new Vector3f(inter.x, 0, wall.z);
			System.out.println("Bounce z");
			}
		
		System.out.println("reflection point: " + refl.toString());
		return refl;
	}
	
	public Vector3f shot(){
		Ray r = new Ray(ball, reflectionPoint());
		Vector3f vel = r.shot();
		//vel.normalise();
		//vel.x -= -1;
		//vel.z -= -1;
		System.out.println("Vel:" + vel.toString());
		return vel;
	}
	
	public Point2D.Float getIntersectionPoint(Line2D.Float line1, Line2D.Float line2) {
	    if (! line1.intersectsLine(line2) ) return null;
	      double px = line1.getX1(),
	            py = line1.getY1(),
	            rx = line1.getX2()-px,
	            ry = line1.getY2()-py;
	      double qx = line2.getX1(),
	            qy = line2.getY1(),
	            sx = line2.getX2()-qx,
	            sy = line2.getY2()-qy;

	      double det = sx*ry - sy*rx;
	      if (det == 0) {
	        return null;
	      } else {
	        double z = (sx*(qy-py)+sy*(px-qx))/det;
	        if (z==0 ||  z==1) return null;  // intersection at end point!
	        return new Point2D.Float(
	          (float)(px+z*rx), (float)(py+z*ry));
	      }
	 }

}

