package com.zsh_o.neat.gene;

import com.zsh_o.neat.Config;
import com.zsh_o.neat.Utils;
import com.zsh_o.neat.attribute.BaseAttribute;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by zsh96 on 2017/8/22.
 */
public abstract class BaseGene implements Cloneable {
    ArrayList<BaseAttribute> attributes=new ArrayList<>();
    Config.GeneConfig config;

    public BaseGene(Config.GeneConfig config) {
        this.config=config;
    }

    public void mutate(){
        for (BaseAttribute a:
             attributes) {
            a.mutate();
        }
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        BaseGene clone= (BaseGene) super.clone();
        ArrayList<BaseAttribute> tba=new ArrayList<>();
        for(BaseAttribute b:attributes){
            tba.add((BaseAttribute) b.clone());
        }
        clone.attributes=tba;

//        clone.attributes= (ArrayList<BaseAttribute>) this.attributes.clone();
        return clone;
    }

    public BaseGene crossover(BaseGene gene2) throws CloneNotSupportedException {

        BaseGene new_gene = (BaseGene) this.clone();
        for (BaseAttribute a :
                attributes) {
            if (Utils.RANDOM.nextDouble() > 0.5) {
                String name=a.name;
                new_gene.setAttribute(name,gene2.getAttribute(name));
            }
        }
        return new_gene;
    }
    public BaseAttribute getAttribute(String name){
        for (BaseAttribute a:attributes) {
            if(a.name.equals(name)){
                return a;
            }
        }
        return null;
    }
    public void setAttribute(String name,BaseAttribute attribute){
        for(int i=0;i<attributes.size();i++){
            if(attributes.get(i).name.equals(name)){
                attributes.set(i,attribute);
            }
        }
    }
    public void addAttribute(BaseAttribute attribute){
        attributes.add(attribute);
    }

    public abstract double distance(BaseGene other);
}
