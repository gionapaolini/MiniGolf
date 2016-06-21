package GameEngine.AI.genetic;

import org.lwjgl.util.vector.Vector3f;

public class FitnessCalc {
	
	
	Vector3f hole;

	
	public FitnessCalc(Vector3f hole){
		this.hole = hole;
	
	}


    /* Public methods */

    // Calculate inidividuals fittness by comparing it to our candidate solution
    float getFitness(Individual individual) {
    	float x = Math.abs(individual.endPosition.x - hole.x);
    	float y = Math.abs(individual.endPosition.y - hole.y);
    	float z = Math.abs(individual.endPosition.z - hole.z);
    	return x+y+z;
    }
    
    
}
