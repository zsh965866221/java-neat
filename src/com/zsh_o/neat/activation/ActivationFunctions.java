package com.zsh_o.neat.activation;

import java.util.ArrayList;

/**
 * Created by zsh96 on 2017/8/22.
 */
public class ActivationFunctions {
    ArrayList<IActivate> activates=new ArrayList<>();
    public void add(IActivate f){
        activates.add(f);
    }
    public void add(IActivate[] fs){
        for(int i=0;i<fs.length;i++){
            activates.add(fs[i]);
        }
    }

    public ArrayList<IActivate> getActivates() {
        return activates;
    }

    public class Sigmoid extends IActivate{
        public Sigmoid(String name) {
            super(name);
        }

        @Override
        public double activate(double x) {
            double z=Math.max(-60,5*x);
            return 1/(1+Math.exp(-z));
        }
    }
    public class Tanh extends IActivate{

        public Tanh(String name) {
            super(name);
        }

        @Override
        public double activate(double x) {
            return Math.tanh(x);
        }
    }

    public ActivationFunctions() {
        add(new Sigmoid("sigmoid"));
//        add(new Tanh("tanh"));
    }
    private static ActivationFunctions INSTANCE;
    public static ActivationFunctions instance(){
        if(INSTANCE==null){
            INSTANCE=new ActivationFunctions();
        }
        return INSTANCE;
    }
}
