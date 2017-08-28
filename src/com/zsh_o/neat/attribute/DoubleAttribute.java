package com.zsh_o.neat.attribute;

import com.zsh_o.neat.Config;
import com.zsh_o.neat.Utils;


/**
 * Created by zsh96 on 2017/8/22.
 */
public class DoubleAttribute extends BaseAttribute {
    Config.DoubleConfig config;
    double value;
    public DoubleAttribute(String name,Config.DoubleConfig config) {
        super(name);
        this.config=config;
        init();
    }

    @Override
    public void init() {
        if(config.init_type.equals("gaussian")){
            value= clamp(Utils.gauss(config.init_mean,config.init_stdev));
        }else if(config.init_type.equals("uniform")){
            double min_value=Math.max(config.min_value,config.init_mean-2*config.init_stdev);
            double max_value=Math.min(config.max_value,config.init_mean+2*config.init_stdev);
            value=Utils.uniform(min_value,max_value);
        }
    }

    @Override
    public void mutate() {
        double r=Utils.RANDOM.nextDouble();
        if(r<config.mutate_rate){
            value=clamp((double)value+Utils.gauss(0.0,config.mutate_power));
        }else if(r<config.replace_rate+config.mutate_rate){
            init();
        }
    }

    public double clamp(double value){
        return Math.max(Math.min(value,config.max_value),config.min_value);
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        DoubleAttribute t= (DoubleAttribute) super.clone();
        double v=value;
        t.value=v;
        return t;
    }

    @Override
    public String toString() {
        return "DoubleAttribute{" +
                "name='" + name + '\'' +
                ", value=" + value +
                '}';
    }
}
