package com.zsh_o.neat;

import com.zsh_o.neat.gene.DefaultConnectionGene;
import com.zsh_o.neat.gene.DefaultNodeGene;
import com.zsh_o.neat.genome.DefaultGenome;

import java.util.*;

/**
 * Created by zsh96 on 2017/8/25.
 */
public class FeedForward {
    DefaultGenome genome;
    Config config;
    HashMap<Integer,Double> values=new HashMap<>();

    int[] input_keys;
    int[] output_keys;
    ArrayList<Integer> temp_inputs=new ArrayList<>();
    public FeedForward(DefaultGenome genome, Config config) {
        this.genome = genome;
        this.config = config;
        input_keys=config.genomeManager.input_keys;
        output_keys=config.genomeManager.output_keys;
    }
    public double[] activate(double[] inputs){
        values.clear();
        for(int i=0;i<input_keys.length;i++){
            values.put(input_keys[i],inputs[i]);
        }

        HashSet<Integer> remained_nodes=new HashSet<>();
        for(DefaultNodeGene gene:genome.nodes){
            remained_nodes.add(gene.key);
        }
//        while(!remained_nodes.isEmpty()){
            Iterator<Integer> iterator=remained_nodes.iterator();
            while(iterator.hasNext()){
                int key=iterator.next();
                boolean no_existed=true;
                temp_inputs.clear();
                for(DefaultConnectionGene gene: genome.connections){
                    if(!gene.enabled.getValue()){
                        continue;
                    }
                    if(gene.out==key){
                        temp_inputs.add(gene.in);
                        if(remained_nodes.contains(gene.in)){
                            no_existed=false;
                            break;
                        }
                    }
                }

                if(no_existed){
                    DefaultNodeGene node=genome.getNode(key);
                    double[] tti=new double[temp_inputs.size()];
                    for(int i=0;i<tti.length;i++){
                        int in=temp_inputs.get(i);
                        tti[i]=values.get(temp_inputs.get(i))*genome.getConnection(in,key).weight.getValue();
                    }
                    values.put(key,node.activation.getValue().activate(node.bias.getValue()+node.response.getValue()*node.aggregation.getValue().aggregate(tti)));
                    remained_nodes.remove(key);
                    iterator=remained_nodes.iterator();
                }

            }
//        }
        double[] outputs=new double[output_keys.length];
        for(int i=0;i<outputs.length;i++){
            outputs[i]=values.get(output_keys[i]);
        }
        return outputs;
    }
}
