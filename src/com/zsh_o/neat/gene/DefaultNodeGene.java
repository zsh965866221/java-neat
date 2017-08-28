package com.zsh_o.neat.gene;

import com.zsh_o.neat.Config;
import com.zsh_o.neat.activation.IActivate;
import com.zsh_o.neat.aggregation.IAggregate;
import com.zsh_o.neat.attribute.DoubleAttribute;
import com.zsh_o.neat.attribute.ListAttribute;

/**
 * Created by zsh96 on 2017/8/22.
 */
public class DefaultNodeGene extends BaseGene {
    public int key;
    public DoubleAttribute bias;
    public DoubleAttribute response;
    public ListAttribute<IActivate> activation;
    public ListAttribute<IAggregate> aggregation;
    public DefaultNodeGene(int key, Config config) {
        super(config.nodeConfig);
        this.key=key;
        this.bias=new DoubleAttribute("bias",config.biasConfig);
        this.response=new DoubleAttribute("response",config.responseConfig);
        this.activation=new ListAttribute<IActivate>("activation",config.activationConfig);
        this.aggregation=new ListAttribute<IAggregate>("aggregation",config.aggregationConfig);
        addAttribute(bias);
        addAttribute(response);
        addAttribute(activation);
        addAttribute(aggregation);
    }

    @Override
    public double distance(BaseGene other) {
        DefaultNodeGene o=(DefaultNodeGene)other;
        double s_bias=bias.getValue();
        double s_response=response.getValue();
        IActivate s_activation= activation.getValue();
        IAggregate s_aggregate= aggregation.getValue();

        double o_bias= o.bias.getValue();
        double o_response=o.response.getValue();
        IActivate o_activation= o.activation.getValue();
        IAggregate o_aggregate= o.aggregation.getValue();

        double d=Math.abs(s_bias-o_bias)+Math.abs(s_response-o_response);
        if(!s_activation.equals(o_activation)){
            d+=1;
        }
        if(!s_aggregate.equals(o_aggregate)){
            d+=1;
        }
        return d*config.compatibility_weight_coefficient;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        DefaultNodeGene clone= (DefaultNodeGene) super.clone();
        clone.aggregation= (ListAttribute<IAggregate>) aggregation.clone();
        clone.response= (DoubleAttribute) response.clone();
        clone.activation= (ListAttribute<IActivate>) activation.clone();
        clone.bias= (DoubleAttribute) bias.clone();
        return clone;

    }

    @Override
    public String toString() {
        return "DefaultNodeGene{" +
                "key=" + key +
                ", bias=" + bias +
                ", response=" + response +
                ", activation=" + activation +
                ", aggregation=" + aggregation +
                '}';
    }
}
