package com.zsh_o.neat;

import com.zsh_o.neat.activation.ActivationFunctions;
import com.zsh_o.neat.activation.IActivate;
import com.zsh_o.neat.aggregation.AggregationFunctions;
import com.zsh_o.neat.aggregation.IAggregate;
import com.zsh_o.neat.genome.DefaultGenome;
import com.zsh_o.neat.genome.DefaultGenomeManager;
import com.zsh_o.neat.species.DefaultSpeciesManager;
import com.zsh_o.neat.stagnation.DefaultStagnation;

import java.util.ArrayList;

/**
 * Created by zsh96 on 2017/8/22.
 */
public class Config {
    public DoubleConfig biasConfig=new DoubleConfig(0,1,0.1,0.7,1,30,-30,"gaussian");
    public DoubleConfig responseConfig=new DoubleConfig(1,0,0,0,0,30,-30,"gaussian");
    public ListConfig<IActivate> activationConfig=new ListConfig(0,false,0, ActivationFunctions.instance().getActivates());
    public ListConfig<IAggregate> aggregationConfig=new ListConfig(0,false,0, AggregationFunctions.instance().getActivates());
    public GeneConfig nodeConfig=new GeneConfig(0.5);
    public GeneConfig connectionConfig=new GeneConfig(0.5);
    public DoubleConfig weightConfig=new DoubleConfig(0,1,0.05,0.8,1,30,-30,"gaussian");
    public BoolConfig enabledConfig=new BoolConfig(true,0.001,0,0,false);
    public GenomeConfig defalutGenomeConfig=new GenomeConfig(2,1,0,1, 0.5,0.5,0.3,0.2,false,"full_forward",true);
    public StagnationConfig defaultStagnationConfig=new StagnationConfig("mean",5,2);
    public SpeciesConfig defaultSpeciesConfig=new SpeciesConfig(3);
    public ReproductionConfig reproductionConfig=new ReproductionConfig(2,0.2,2);
    public PopulationConfig populationConfig=new PopulationConfig("max",true,150,false,3.9);

    public DefaultStagnation stagnation=new DefaultStagnation(this);
    public DefaultGenomeManager genomeManager=new DefaultGenomeManager(this);
    public DefaultSpeciesManager speciesManager=new DefaultSpeciesManager(this);
    public DefaultReproduction reproduction=new DefaultReproduction(this,stagnation);

    public static class BoolConfig {
        public boolean default_value;
        public double mutate_rate;
        public double rate_to_true_add;
        public double rate_to_false_add;
        public boolean init_random;

        public BoolConfig(boolean default_value, double mutate_rate, double rate_to_true_add, double rate_to_false_add, boolean init_random) {
            this.default_value = default_value;
            this.mutate_rate = mutate_rate;
            this.rate_to_true_add = rate_to_true_add;
            this.rate_to_false_add = rate_to_false_add;
            this.init_random = init_random;
        }
    }
    public static class DoubleConfig {
        public double init_mean;
        public double init_stdev;
        public double replace_rate;
        public double mutate_rate;
        public double mutate_power;
        public double max_value;
        public double min_value;
        public String init_type="gaussian";

        public DoubleConfig(double init_mean, double init_stdev, double replace_rate, double mutate_rate, double mutate_power, double max_value, double min_value, String init_type) {
            this.init_mean = init_mean;
            this.init_stdev = init_stdev;
            this.replace_rate = replace_rate;
            this.mutate_rate = mutate_rate;
            this.mutate_power = mutate_power;
            this.max_value = max_value;
            this.min_value = min_value;
            this.init_type = init_type;
        }
    }
    public static class ListConfig<T> {
        public int default_index=0;
        public boolean init_random;
        public double mutate_rate;
        public ArrayList<T> list=new ArrayList();

        public ListConfig(int default_index, boolean init_random, double mutate_rate, ArrayList<T> list) {
            this.default_index = default_index;
            this.init_random = init_random;
            this.mutate_rate = mutate_rate;
            this.list = list;
        }
    }
    public static class GeneConfig {
        public double compatibility_weight_coefficient;

        public GeneConfig(double compatibility_weight_coefficient) {
            this.compatibility_weight_coefficient = compatibility_weight_coefficient;
        }
    }

    public static class GenomeConfig {
        public int num_inputs;
        public int num_outputs;
        public int num_hidden;
        public double compatibility_disjoint_coefficient;
        public double conn_add_prob;
        public double conn_delete_prob;
        public double node_add_prob;
        public double node_delete_prob;
        public boolean single_structural_mutation=false;
        public String initial_connection="full_forward";
        public boolean feed_forward=true;

        public GenomeConfig(int num_inputs, int num_outputs, int num_hidden, double compatibility_disjoint_coefficient, double conn_add_prob, double conn_delete_prob, double node_add_prob, double node_delete_prob, boolean single_structural_mutation, String initial_connection, boolean feed_forward) {
            this.num_inputs = num_inputs;
            this.num_outputs = num_outputs;
            this.num_hidden = num_hidden;
            this.compatibility_disjoint_coefficient = compatibility_disjoint_coefficient;
            this.conn_add_prob = conn_add_prob;
            this.conn_delete_prob = conn_delete_prob;
            this.node_add_prob = node_add_prob;
            this.node_delete_prob = node_delete_prob;
            this.single_structural_mutation = single_structural_mutation;
            this.initial_connection = initial_connection;
            this.feed_forward = feed_forward;
        }
    }
    public static class StagnationConfig{
        public String species_fitness_func="mean";
        public int max_stagnation=15;
        public int species_elitism=0;

        public StagnationConfig(String species_fitness_func, int max_stagnation, int species_elitism) {
            this.species_fitness_func = species_fitness_func;
            this.max_stagnation = max_stagnation;
            this.species_elitism = species_elitism;
        }
    }
    public static class SpeciesConfig{
        public double compatibility_threshold;
        public SpeciesConfig(double compatibility_threshold) {
            this.compatibility_threshold = compatibility_threshold;
        }
    }
    public static class ReproductionConfig{
        public int elitism=0;
        public double survival_threshold=0.2;
        public int min_species_size=2;

        public ReproductionConfig(int elitism, double survival_threshold, int min_species_size) {
            this.elitism = elitism;
            this.survival_threshold = survival_threshold;
            this.min_species_size = min_species_size;
        }
    }
    public static class PopulationConfig{
        public ArrayList<DefaultGenome> population;
        public DefaultSpeciesManager speciesManager;
        public int generation;

        public String fitness_criterion;
        public boolean no_fitness_termination;

        public boolean init_stated=false;

        int pop_size;
        boolean reset_on_extinction;
        double fitness_threshold;

        public PopulationConfig(ArrayList<DefaultGenome> population, DefaultSpeciesManager speciesManager, int generation, String fitness_criterion, boolean no_fitness_termination, int pop_size, boolean reset_on_extinction, double fitness_threshold) {
            this.population = population;
            this.speciesManager = speciesManager;
            this.generation = generation;
            this.fitness_criterion = fitness_criterion;
            this.no_fitness_termination = no_fitness_termination;
            this.pop_size = pop_size;
            this.reset_on_extinction = reset_on_extinction;
            this.fitness_threshold = fitness_threshold;
            this.init_stated=true;
        }

        public PopulationConfig(String fitness_criterion, boolean no_fitness_termination, int pop_size, boolean reset_on_extinction, double fitness_threshold) {
            this.fitness_criterion = fitness_criterion;
            this.no_fitness_termination = no_fitness_termination;
            this.pop_size = pop_size;
            this.reset_on_extinction = reset_on_extinction;
            this.fitness_threshold = fitness_threshold;
        }
    }
}
