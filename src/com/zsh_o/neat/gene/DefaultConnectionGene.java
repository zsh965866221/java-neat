package com.zsh_o.neat.gene;

import com.zsh_o.neat.Config;
import com.zsh_o.neat.attribute.BoolAttribute;
import com.zsh_o.neat.attribute.DoubleAttribute;

/**
 * Created by zsh96 on 2017/8/22.
 */
public class DefaultConnectionGene extends BaseGene {
    public int in;
    public int out;
    public DoubleAttribute weight;
    public BoolAttribute enabled;
    public DefaultConnectionGene(int in,int out,Config config) {
        super(config.connectionConfig);
        this.in=in;
        this.out=out;
        this.weight=new DoubleAttribute("weight",config.weightConfig);
        this.enabled=new BoolAttribute("enabled",config.enabledConfig);
        addAttribute(weight);
        addAttribute(enabled);
    }

    @Override
    public double distance(BaseGene other) {
        DefaultConnectionGene o= (DefaultConnectionGene) other;

        double s_weight=weight.getValue();
        boolean s_enabled=enabled.getValue();

        double o_weight=o.weight.getValue();
        boolean o_enabled=o.enabled.getValue();

        double d=Math.abs(s_weight-o_weight);
        if(s_enabled!=o_enabled){
            d+=1;
        }
        return d;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        DefaultConnectionGene clone= (DefaultConnectionGene) super.clone();
        clone.enabled= (BoolAttribute) enabled.clone();
        clone.weight= (DoubleAttribute) weight.clone();
        return clone;
    }

    @Override
    public String toString() {
        return "DefaultConnectionGene{" +
                "in=" + in +
                ", out=" + out +
                ", weight=" + weight +
                ", enabled=" + enabled +
                '}';
    }
}
