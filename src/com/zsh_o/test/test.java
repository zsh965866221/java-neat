package com.zsh_o.test;

import com.zsh_o.neat.Config;
import com.zsh_o.neat.FeedForward;
import com.zsh_o.neat.FitnessFunc;
import com.zsh_o.neat.Population;
import com.zsh_o.neat.gene.DefaultConnectionGene;
import com.zsh_o.neat.genome.DefaultGenome;

import java.util.ArrayList;

/**
 * Created by zsh96 on 2017/8/24.
 */
public class test {
    public static double[][] xor_inputs={{0,0},{0,1},{1,0},{1,1}};
    public static double[][] xor_outputs={{0},{1},{1},{0}};
    public static void main(String[] args) throws CloneNotSupportedException {
        Config config=new Config();
        Population population= new Population(config);
        DefaultGenome winner= population.run(new FitnessFunc() {
            @Override
            public void calculate(ArrayList<DefaultGenome> genomes, Config config) {
                for(DefaultGenome genome:genomes){
                    genome.fitness=4;
                    FeedForward net=new FeedForward(genome,config);
                    double sum=0;
                    for(int i=0;i<xor_inputs.length;i++){
                        double[] output=net.activate(xor_inputs[i]);
                        genome.fitness-=(Math.pow(output[0]-xor_outputs[i][0],2));
                    }
                }
            }
        }, 100);

        System.out.println("----------------------------------------------------------");
        for(DefaultConnectionGene gene:winner.connections){
            if(!gene.enabled.getValue())
                continue;
            System.out.println(gene.in +"\t -> \t"+gene.out);
        }
        System.out.println(winner.fitness);

        FeedForward net=new FeedForward(winner,config);
        for(int i=0;i<xor_inputs.length;i++){
            double[] output=net.activate(xor_inputs[i]);
            System.out.println(xor_inputs[i][0]+" , "+xor_inputs[i][1]+" = "+xor_outputs[i][0]+" , "+output[0]);
        }

        System.out.println("over");
    }
}
