package com.zsh_o.neat.species;

import com.zsh_o.neat.Config;
import com.zsh_o.neat.genome.DefaultGenome;

import java.util.HashMap;

/**
 * Created by zsh96 on 2017/8/24.
 */
public class GenomeDistanceCache {
    Config config;
    public int hits;
    public int misses;

    HashMap<String,Double> distances=new HashMap<>();

    public GenomeDistanceCache(Config config) {
        this.config = config;
    }

    public double getDistance(DefaultGenome genome1,DefaultGenome genome2){
        int g1=genome1.key;
        int g2=genome2.key;
        double d=get(g1,g2);
        if(d<0){
            d=genome1.distance(genome2);
            set(g1,g2,d);
            set(g2,g1,d);
            misses+=1;
        }else{
            hits+=1;
        }
        return d;
    }
    private double get(int g1,int g2){
        String k=g1+" "+g2;
        if(distances.containsKey(k)){
            return distances.get(k);
        }else{
            return -1;
        }
    }
    private void set(int g1,int g2,double d){
        distances.put(g1+" "+g2,d);
    }
}
