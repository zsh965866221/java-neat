package com.zsh_o.neat.species;

import com.zsh_o.neat.genome.DefaultGenome;

import java.util.ArrayList;

/**
 * Created by zsh96 on 2017/8/24.
 */
public class Species {
    public int key;
    public int created;
    public int last_improved;
    public DefaultGenome representative;
    public ArrayList<DefaultGenome> members;
    public double fitness;
    public double adjusted_fitness;
    public ArrayList<Double> fitness_history=new ArrayList<>();

    public void update(DefaultGenome representative,ArrayList<DefaultGenome> members){
        this.representative=representative;
        this.members=members;
    }

    public Species(int key, int generation) {
        this.key = key;
        this.created = generation;
    }
    public ArrayList<Double> get_fitnesses(){
        ArrayList<Double> fitnesses=new ArrayList<>();
        for(DefaultGenome genome:members){
            fitnesses.add(genome.fitness);
        }
        return fitnesses;
    }
}
