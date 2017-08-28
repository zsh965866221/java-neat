package com.zsh_o.neat;

import com.zsh_o.neat.genome.DefaultGenome;
import com.zsh_o.neat.species.DefaultSpeciesManager;
import com.zsh_o.neat.species.Species;
import com.zsh_o.neat.stagnation.DefaultStagnation;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.stream.Collector;

/**
 * Created by zsh96 on 2017/8/24.
 */
public class DefaultReproduction {
    Config config;
    int max_genome_num=0;
    DefaultStagnation stagnation;
    HashMap<Integer,Ancestor> ancestors=new HashMap<>();

    public DefaultReproduction(Config config, DefaultStagnation stagnation) {
        this.config = config;
        this.stagnation = stagnation;
    }
    public ArrayList<DefaultGenome> create_new(int num_genomes){
        ArrayList<DefaultGenome> new_genomes=new ArrayList<>();
        for(int i=0;i<num_genomes;i++){
            int key=getNewGenomeId();
            DefaultGenome g=new DefaultGenome(key,config.genomeManager);
            new_genomes.add(g);
        }
        return new_genomes;
    }

    public ArrayList<Integer> compute_spawn(ArrayList<Double> adjusted_fitness,ArrayList<Integer> previous_sizes,int pop_size,int min_species_size){
        double af_sum=Utils.sum_double(adjusted_fitness);
        ArrayList<Integer> spawn_amounts=new ArrayList<>();
        for(int i=0;i<adjusted_fitness.size();i++){
            double af=adjusted_fitness.get(i);
            int ps=previous_sizes.get(i);

            double s;
            if(af_sum>0){
                s=Math.max(min_species_size,af/af_sum*pop_size);
            }else{
                s=min_species_size;
            }
            double d=(s-ps)*0.5;
            int c= (int) Math.round(d);
            int spawn=ps;
            if(Math.abs(c)>0){
                spawn+=c;
            }else if(d>0){
                spawn+=1;
            }else if(d<0){
                spawn-=1;
            }
            spawn_amounts.add(spawn);
        }
        int total_spawn= Utils.sum_int(spawn_amounts);
        double norm=pop_size/total_spawn;
        ArrayList<Integer> result=new ArrayList<>();
        for(int k:spawn_amounts){
            result.add(Math.max(min_species_size,(int)Math.round(k*norm)));
        }
        return result;
    }

    public ArrayList<DefaultGenome> reproduce(DefaultSpeciesManager speciesManager,int pop_size,int generation) throws CloneNotSupportedException {
        ArrayList<Double> all_fitnesses=new ArrayList<>();
        ArrayList<Species> remaining_species=new ArrayList<>();

        ArrayList<DefaultGenome> new_population=new ArrayList<>();

        ArrayList<DefaultStagnation.StagnationState> stagnationStates= stagnation.update(speciesManager,generation);
        for(DefaultStagnation.StagnationState state:stagnationStates){
            int stag_sid=state.sid;
            Species stag_s=state.s;
            boolean stagnant=state.is_stagnant;

            if(!stagnant){
                for(DefaultGenome genome:stag_s.members){
                    all_fitnesses.add(genome.fitness);
                }
                remaining_species.add(stag_s);
            }
        }
        if(remaining_species.isEmpty()){
            speciesManager.species.clear();
            new_population.clear();
            return new_population;
        }

        double min_fitness=all_fitnesses.stream().min(Comparator.comparing(ss->ss)).get();
        double max_fitness=all_fitnesses.stream().max(Comparator.comparing(ss->ss)).get();

        double fitness_range=Math.max(1.0,max_fitness-min_fitness);
        for(Species afs:remaining_species){
            double msf=Utils.mean(afs.members,ss->ss.fitness);
            double af=(msf-min_fitness)/fitness_range;
            afs.adjusted_fitness=af;
        }
        ArrayList<Double> adjusted_fitnesses=Utils.adjust(remaining_species,ss->ss.adjusted_fitness);
        double avg_adjusted_fitness=Utils.mean(adjusted_fitnesses,ss->ss);

        ArrayList<Integer> previous_sizes=Utils.adjust(remaining_species,ss->ss.members.size());
        int min_species_size=Math.max(config.reproductionConfig.min_species_size,config.reproductionConfig.elitism);
        ArrayList<Integer> spawn_amounts=compute_spawn(adjusted_fitnesses,previous_sizes,pop_size,min_species_size);

        speciesManager.species.clear();

        for(int i=0;i<spawn_amounts.size();i++){
            int spawn=spawn_amounts.get(i);
            Species s=remaining_species.get(i);

            spawn=Math.max(spawn,config.reproductionConfig.elitism);

            assert spawn>0;

            ArrayList<DefaultGenome> old_members= (ArrayList<DefaultGenome>) s.members.clone();
            s.members.clear();
            speciesManager.species.add(s);

            old_members.sort(Comparator.comparing(ss->-ss.fitness));
            if(config.reproductionConfig.elitism>0){
                for(int j=0;j<config.reproductionConfig.elitism;j++){
                    if(j>old_members.size()-1){
                        break;
                    }
                    DefaultGenome m=old_members.get(j);
                    new_population.add(m);
                    spawn-=1;
                }
            }
            if(spawn<=0){
                continue;
            }
            int repro_cutoff= (int) Math.ceil(config.reproductionConfig.survival_threshold*old_members.size());
            repro_cutoff=Math.max(repro_cutoff,2);
            if(old_members.size()>2){
                old_members=new ArrayList<>(old_members.subList(0,repro_cutoff));
            }

            while(spawn>0){
                spawn-=1;
                DefaultGenome parent1= Utils.choice(old_members);
                DefaultGenome parent2=Utils.choice(old_members);
                int gid=getNewGenomeId();
                DefaultGenome child= new DefaultGenome(gid,config.genomeManager);
                child.crossover(parent1,parent2);
                child.mutate();
                new_population.add(child);;
                ancestors.put(gid,new Ancestor(parent1.key,parent2.key));
            }

        }
        return new_population;
    }



    public int getNewGenomeId(){
        max_genome_num++;
        return max_genome_num;
    }

    public static class Ancestor{
        int parent1;
        int parent2;

        public Ancestor(int parent1, int parent2) {
            this.parent1 = parent1;
            this.parent2 = parent2;
        }
    }
}
