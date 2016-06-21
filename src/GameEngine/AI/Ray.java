package GameEngine.AI;

import java.awt.geom.Line2D;


import org.lwjgl.util.vector.Vector3f;

public class Ray  extends Line2D.Float{
	
	final float COEF = 1.5f;
	
	public Ray(Vector3f a, Vector3f b){
		x1 = a.x;
		y1 = a.z;
		x2 = b.x;
		y2 = b.z;
	}
	
	public Vector3f shot(){
		System.out.println("origin: " + x1 + ", 0, " + y1);
		System.out.println("target: " + x2 + ", 0, " + y2);
		float dx = (float) Math.abs(x1-x2);
		float dy = (float) Math.abs(y1-y2);
		System.out.println("dx: " + dx);
		System.out.println("dy: " + dy);
		float absX = (float) Math.sqrt(2 * COEF*dx);
		float absY = (float) Math.sqrt(2 * COEF*dy);
		System.out.println("absX: " + absX);
		System.out.println("absY: " + absY);
		
		Vector3f shot;
		if(x1<=x2 && y1<=y2)
			//shot = new Vector3f(absX,0,absY);
			shot = new Vector3f(dx,0,dy);
		else if(x1>x2 && y1<=y2)
			//shot =  new Vector3f(-absX,0,absY);
			shot = new Vector3f(-dx,0,dy);
		else if(x1<=x2 && y1>y2)
			//shot =  new Vector3f(absX,0,-absY);
			shot = new Vector3f(dx,0,-dy);
		else
			//shot =  new Vector3f(-absX,0,-absY);
			shot = new Vector3f(-dx,0,-dy);
			
		System.out.println("shot: " + shot);
		return shot;
	}

}
