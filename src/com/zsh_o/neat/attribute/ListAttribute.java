package com.zsh_o.neat.attribute;

import com.zsh_o.neat.Config;
import com.zsh_o.neat.Utils;

/**
 * Created by zsh96 on 2017/8/22.
 */
public class ListAttribute<T> extends BaseAttribute {
    Config.ListConfig<T> config;
    T value;
    public ListAttribute(String name, Config.ListConfig bean) {
        super(name);
        this.config=bean;
        init();
    }

    @Override
    public void init() {
        if(config.init_random){
            value = Utils.choice(config.list);
        }else{
            value= config.list.get(config.default_index);
        }
    }

    @Override
    public void mutate() {
        if(config.mutate_rate>0){
            if(Utils.RANDOM.nextDouble()<config.mutate_rate){
                value=Utils.choice(config.list);
            }
        }
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        ListAttribute<T> clone= (ListAttribute<T>) super.clone();
        clone.value=value;
        return clone;
    }

    @Override
    public String toString() {
        return "ListAttribute{" +
                "name='" + name + '\'' +
                ", value=" + value +
                '}';
    }
}
