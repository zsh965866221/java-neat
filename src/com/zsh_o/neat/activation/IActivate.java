package com.zsh_o.neat.activation;

/**
 * Created by zsh96 on 2017/8/22.
 */
public abstract class IActivate {
    String name;
    public abstract double activate(double x);

    public IActivate(String name) {
        this.name = name;
    }
}
