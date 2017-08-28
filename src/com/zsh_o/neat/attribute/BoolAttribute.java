package com.zsh_o.neat.attribute;

import com.zsh_o.neat.Config;
import com.zsh_o.neat.Utils;

/**
 * Created by zsh96 on 2017/8/22.
 */
public class BoolAttribute extends BaseAttribute {
    Config.BoolConfig config;
    boolean value;

    public BoolAttribute(String name, Config.BoolConfig config) {
        super(name);
        this.config=config;
        init();
    }

    @Override
    public void init() {
        if(config.init_random){
            value= Utils.RANDOM.nextBoolean();
        }else{
            value=config.default_value;
        }
    }

    @Override
    public void mutate() {
        double mutate_rate=config.mutate_rate;
        if(value){
            mutate_rate+=config.rate_to_false_add;
        }else{
            mutate_rate+=config.rate_to_true_add;
        }
        if(mutate_rate>0){
            if(Utils.RANDOM.nextDouble()<mutate_rate){
                value=Utils.RANDOM.nextDouble()<0.5;
            }
        }
    }

    public boolean getValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        BoolAttribute t= (BoolAttribute) super.clone();
        boolean v=value;
        t.value=v;
        return t;
    }

    @Override
    public String toString() {
        return "BoolAttribute{" +
                "name='" + name + '\'' +
                ", value=" + value +
                '}';
    }
}