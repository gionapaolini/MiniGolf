package GameEngine.AI.genetic;

import java.util.Random;

import org.lwjgl.util.vector.Vector3f;

public class Individual {
	
	Random r  = new Random();
    private Vector3f genes; //initial
    public Vector3f endPosition;
    public Vector3f hole;
    // Cache
    private float fitness = 100;

    // Create a random individual
    public Individual(Vector3f hole) {
            genes.x = r.nextFloat() * 10;
            genes.y = 0;
            genes.z = r.nextFloat() * 10;
            this.hole = hole;
    }
    
    public Individual(Individual ind) {
    	float n;
    	if(r.nextFloat()>0.5f)
    		n=1;
    	else
    		n=-1;
        genes.x = ind.genes.x + r.nextFloat() * n/10;
        genes.y = 0;
        genes.z = ind.genes.z + r.nextFloat() * n/10;
        ind.hole = hole;
}

    /* Getters and setters */
    // Use this if you want to create individuals with different gene lengths
 
    
    public float getGene(char dimension) {
    	
    	if(dimension == 'x' ||dimension == 'y' || dimension == 'z'){
    		switch (dimension){
    			case 'x': return genes.x;
    			case 'y': return genes.y;
    			case 'z': return genes.z;
    		}
    	}
		return 0;
    	
    }

    public void setGene(char dimension, float value) {
    	if(dimension == 'x' ||dimension == 'y' || dimension == 'z'){
    		switch (dimension){
    			case 'x': genes.x = value;
    			case 'y': genes.y = value;
    			case 'z': genes.z = value;
    		}
        fitness = 100;
    	}
    }

    /* Public methods */

    public float getFitness() {
        if (fitness == 100) {
            //fitness = FitnessCalc.getFitness(this);
        }
        return fitness;
    }

    @Override
    public String toString() {
        return genes.toString();
    }

}
