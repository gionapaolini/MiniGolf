package GameEngine.AI.genetic;

import java.util.Arrays;
import java.util.Random;

import org.lwjgl.util.vector.Vector3f;

public class Population {

    Individual[] individuals;
    float[] scores;
    Random r = new Random();
    Vector3f hole;
    
    public Population(int size , Vector3f hole){
    	individuals = new Individual[size];
    	scores = new float[size];
    	this.hole = hole;
    }
    
    public void createIndividuals(){
    	for(Individual ind: individuals){
    		ind = new Individual(hole);
    	}
    	for(int i= 0; i<scores.length; i++){
    		scores[i] = individuals[i].getFitness();
    	}
    }
    
    public void createIndividuals(Individual[] inds){	
    	for(Individual ind: individuals){
    		int rand = r.nextInt(inds.length);
    		ind = new Individual(inds[rand]);
    	}
    	for(int i= 0; i<scores.length; i++){
    		scores[i] = individuals[i].getFitness();
    	}
    }
    
    public Individual[] getFittest(){
    	Individual[] fit = new Individual[3];
    	Arrays.sort(scores);
    	float first = scores[0];
    	float second = scores[1] ;
    	float third = scores[2];
    	for(Individual ind: individuals){
    		if(ind.getFitness()== first)
    			fit[0]= ind;
    		if(ind.getFitness()== second)
    			fit[1]= ind;
    		if(ind.getFitness()== third)
    			fit[2]= ind;
    	}
    	return fit;
    }

}
