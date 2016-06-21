package GameEngine.AI.genetic;

import java.util.Arrays;

import org.lwjgl.util.vector.Vector3f;

public class Genetic {
	
	Population[] populations;
	Individual[] individuals;
	float[] scores;
	
	public Genetic(int generations, int popSize, Vector3f hole){
		populations = new Population[generations];
		individuals = new Individual[generations];
		scores = new float[generations];
		
		for(Population pop : populations){
			pop = new Population(popSize, hole);
		}
		populations[0].createIndividuals();
		for(int i = 1; i<generations; i++ ){
			populations[i].createIndividuals(populations[i-1].getFittest());
		}
		
		for(int i = 0; i<generations; i++){
			individuals[i]= populations[i].getFittest()[0];
		}
		
		for(int i = 0; i<generations; i++){
			scores[i]= individuals[i].getFitness();
		}	
	}
	
	public Individual getFittest(){
		Arrays.sort(scores);
		float best = scores[0];
		Individual master = individuals[0];
		for (Individual ind : individuals){
			if(ind.getFitness() == best)
				master = ind;
		}
		return master;
	}
	

	

	

}
