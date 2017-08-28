package com.zsh_o.neat.aggregation;

/**
 * Created by zsh96 on 2017/8/22.
 */
public abstract class IAggregate {
    String name;
    public abstract double aggregate(double[] inputs);

    public IAggregate(String name) {
        this.name = name;
    }
}
