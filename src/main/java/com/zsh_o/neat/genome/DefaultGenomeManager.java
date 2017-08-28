package com.zsh_o.neat.genome;

import com.sun.xml.internal.ws.api.streaming.XMLStreamReaderFactory;
import com.zsh_o.neat.Config;
import com.zsh_o.neat.activation.ActivationFunctions;
import com.zsh_o.neat.activation.IActivate;
import com.zsh_o.neat.aggregation.AggregationFunctions;
import com.zsh_o.neat.aggregation.IAggregate;
import com.zsh_o.neat.gene.DefaultConnectionGene;
import com.zsh_o.neat.gene.DefaultNodeGene;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by zsh96 on 2017/8/22.
 */
public class DefaultGenomeManager {
    Config config;

    public int[] input_keys;
    public int[] output_keys;

    public DefaultGenomeManager(Config config) {
        this.config = config;
        input_keys=new int[config.defalutGenomeConfig.num_inputs];
        output_keys=new int[config.defalutGenomeConfig.num_outputs];
        for(int i=0;i<config.defalutGenomeConfig.num_inputs;i++){
            input_keys[i]=-i-1;
        }
        for(int i=0;i<this.config.defalutGenomeConfig.num_outputs;i++){
            output_keys[i]=i;
        }
        max_node_number=this.config.defalutGenomeConfig.num_outputs+this.config.defalutGenomeConfig.num_hidden-1;


    }

    int max_node_number;
    public int getNewNodeId(){
        return max_node_number+1;
    }
    public DefaultNodeGene create_node(){
        max_node_number++;
        DefaultNodeGene node=new DefaultNodeGene(max_node_number,config);
        return node;
    }
    public DefaultConnectionGene create_connection(int in,int out){
        return new DefaultConnectionGene(in,out,config);
    }

    public static boolean creates_cycle(ArrayList<DefaultConnectionGene> connections,int in,int out){
        if(in==out)
            return true;
        HashSet<Integer> visited=new HashSet<>();
        visited.add(out);
        while(true){
            int num_added=0;
            for(DefaultConnectionGene gene:connections){
                int a=gene.in;
                int b= gene.out;

                if(visited.contains(a)&&!visited.contains(b)){
                    if(b==in){
                        return true;
                    }
                    visited.add(b);
                    num_added+=1;
                }
            }
            if(num_added==0){
                return false;
            }
        }

    }
}
