package com.zsh_o.neat.stagnation;

import com.zsh_o.neat.Config;
import com.zsh_o.neat.Utils;
import com.zsh_o.neat.species.DefaultSpeciesManager;
import com.zsh_o.neat.species.Species;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by zsh96 on 2017/8/24.
 */
public class DefaultStagnation {
    Config config;

    public DefaultStagnation(Config config) {
        this.config = config;
    }


    public double species_fitness_func(ArrayList<Double> fitness_list){
        switch (config.defaultStagnationConfig.species_fitness_func){
            case "mean":
                return Utils.mean(fitness_list,ss->ss);
            case "max":
                return fitness_list.stream().max(Comparator.comparing(ss->ss)).get();
            case "min":
                return fitness_list.stream().min(Comparator.comparing(ss->ss)).get();
            default:
                return Utils.mean(fitness_list,ss->ss);
        }
    }

    public ArrayList<StagnationState> update(DefaultSpeciesManager speciesManager,int generation){
        ArrayList<Species> species_date=new ArrayList<>();
        for(Species s:speciesManager.species){
            double prev_fitness=-1e20;
            if(!s.fitness_history.isEmpty()){
                prev_fitness=s.fitness_history.stream().max(Comparator.comparing(ss->ss)).get();
            }
            s.fitness=species_fitness_func(s.get_fitnesses());
            s.fitness_history.add(s.fitness);
            if(s.fitness>prev_fitness){
                s.last_improved=generation;
            }
            species_date.add(s);
        }
        species_date.sort(Comparator.comparing(ss->ss.fitness));

        ArrayList<StagnationState> result=new ArrayList<>();
        ArrayList<Double> species_fitnesses=new ArrayList<>();
        int num_non_stagnant=species_date.size();

        for(int i=0;i<species_date.size();i++){
            Species s=species_date.get(i);
            int stagnant_time=generation-s.last_improved;
            boolean is_stagnant=false;
            if(num_non_stagnant>config.defaultStagnationConfig.species_elitism){
                is_stagnant=stagnant_time>=config.defaultStagnationConfig.max_stagnation;
            }
            if(species_date.size()-i<=config.defaultStagnationConfig.species_elitism){
                is_stagnant=false;
            }
            if(is_stagnant){
                num_non_stagnant-=1;
            }
            result.add(new StagnationState(s.key,s,is_stagnant));
            species_fitnesses.add(s.fitness);
        }
        return result;
    }


    public static class StagnationState{
        public int sid;
        public Species s;
        public boolean is_stagnant;

        public StagnationState(int sid, Species s, boolean is_stagnant) {
            this.sid = sid;
            this.s = s;
            this.is_stagnant = is_stagnant;
        }
    }
}
