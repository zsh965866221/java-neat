package com.zsh_o.neat;

import com.zsh_o.neat.genome.DefaultGenome;
import com.zsh_o.neat.species.DefaultSpeciesManager;
import com.zsh_o.neat.stagnation.DefaultStagnation;
import com.zsh_o.test.Xor_Test;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by zsh96 on 2017/8/25.
 */
public class Population {
    Config config;
    DefaultStagnation stagnation;
    DefaultReproduction reproduction;
    DefaultSpeciesManager speciesManager;
    int generation;
    ArrayList<DefaultGenome> population;

    DefaultGenome best_genome=null;

    public Population(Config config) {
        this.config = config;
        if(config.populationConfig.init_stated){
            population=config.populationConfig.population;
            speciesManager=config.populationConfig.speciesManager;
            generation=config.populationConfig.generation;
        }else{
            speciesManager=config.speciesManager;
            reproduction=config.reproduction;
            stagnation=new DefaultStagnation(config);
            population=reproduction.create_new(config.populationConfig.pop_size);
            speciesManager.speciate(population,generation);
        }

    }

    public DefaultGenome run(FitnessFunc fitnessFunc) throws CloneNotSupportedException {
        return run(fitnessFunc,-1);
    }

    public DefaultGenome run(FitnessFunc fitnessFunc,int n) throws CloneNotSupportedException {
        int k=0;
        while(n==-1 || k<n){
            k+=1;
            fitnessFunc.calculate(population,config);
            DefaultGenome best=null;
            for(DefaultGenome g:population){
                if(best==null||g.fitness>best.fitness){
                    best=g;
                }
            }

            if(best_genome==null||best.fitness>best_genome.fitness){
                best_genome=best;
            }
            if(!config.populationConfig.no_fitness_termination){
                double fv=fitness_criterion(Utils.adjust(population,ss->ss.fitness));
                if(fv>config.populationConfig.fitness_threshold){
                    break;
                }
            }

            population=reproduction.reproduce(speciesManager,config.populationConfig.pop_size,generation);
            if(speciesManager.species.isEmpty()){
                if(config.populationConfig.reset_on_extinction){
                    population=reproduction.create_new(config.populationConfig.pop_size);
                }else{
                    return null;
                }
            }
            speciesManager.speciate(population,generation);
            generation+=1;


            System.out.println("generation: "+n);
            System.out.println("fitness: "+best.fitness);
            FeedForward net=new FeedForward(best,config);
            for(int i = 0; i< Xor_Test.xor_inputs.length; i++){
                double[] output=net.activate(Xor_Test.xor_inputs[i]);
                System.out.println(Xor_Test.xor_inputs[i][0]+" , "+ Xor_Test.xor_inputs[i][1]+" = "+ Xor_Test.xor_outputs[i][0]+" , "+output[0]);
            }
            System.out.println("------------------------------------------------------------------------------------------------------------");
        }
        return best_genome;
    }

    public double fitness_criterion(ArrayList<Double> list){
        switch (config.populationConfig.fitness_criterion){
            case "max":
                return list.stream().max(Comparator.comparing(ss->ss)).get();
            case "min":
                return list.stream().min(Comparator.comparing(ss->ss)).get();
            case "mean":
                return Utils.mean(list,ss->ss);
            default:
                return Utils.mean(list,ss->ss);
        }
    }
}
