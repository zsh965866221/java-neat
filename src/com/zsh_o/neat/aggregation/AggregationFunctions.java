package com.zsh_o.neat.aggregation;

import java.util.ArrayList;

/**
 * Created by zsh96 on 2017/8/22.
 */
public class AggregationFunctions {
    ArrayList<IAggregate> aggregates=new ArrayList<>();
    public void add(IAggregate[] fs){
        for(int i=0;i<fs.length;i++){
            aggregates.add(fs[i]);
        }
    }
    public void add(IAggregate f){
        aggregates.add(f);
    }

    public ArrayList<IAggregate> getActivates() {
        return aggregates;
    }

    public class Sum extends IAggregate{
        public Sum(String name) {
            super(name);
        }

        @Override
        public double aggregate(double[] inputs) {
            double sum=0;
            for(int i=0;i<inputs.length;i++){
                sum+=inputs[i];
            }
            return sum;
        }
    }
    public class Product extends IAggregate{

        public Product(String name) {
            super(name);
        }

        @Override
        public double aggregate(double[] inputs) {
            double product=0;
            for(int i=0;i<inputs.length;i++){
                product*=inputs[i];
            }
            return product;
        }
    }

    public AggregationFunctions() {
        add(new Sum("sum"));
        add(new Product("product"));
    }
    private static AggregationFunctions INSTANCE;
    public static AggregationFunctions instance(){
        if(INSTANCE==null){
            INSTANCE=new AggregationFunctions();
        }
        return INSTANCE;
    }
}
