package com.zsh_o.neat;

import com.zsh_o.neat.genome.DefaultGenome;

import java.util.ArrayList;

/**
 * Created by zsh96 on 2017/8/25.
 */
public interface FitnessFunc {
    void calculate(ArrayList<DefaultGenome> genomes,Config config);
}
