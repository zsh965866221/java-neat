package com.zsh_o.neat.attribute;

/**
 * Created by zsh96 on 2017/8/22.
 */
public abstract class BaseAttribute<T> implements Cloneable {
    public String name;

    public BaseAttribute(String name) {
        this.name = name;
    }
    public abstract void init();
    public abstract void mutate();

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
