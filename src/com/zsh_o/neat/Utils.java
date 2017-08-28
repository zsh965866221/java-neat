package com.zsh_o.neat;

import com.zsh_o.neat.genome.DefaultGenome;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Created by zsh96 on 2017/8/22.
 */
public class Utils {
    public static Random RANDOM=new Random(System.currentTimeMillis());
    public static double gauss(double mean,double std){
        return RANDOM.nextGaussian()*std+mean;
    }
    public static double uniform(double min,double max){
        return RANDOM.nextDouble()*(max-min)+min;
    }
    public static <T>T choice(ArrayList<T> list){
        return list.get(RANDOM.nextInt(list.size()));
    }
    public static <T> double mean(ArrayList<T> list,IS<T> is){
        double sum=0;
        for(T t:list){
            sum+=is.action(t);
        }
        return sum/list.size();
    }
    public static interface IS<T>{
        double action(T t);
    }
    public static <T1,T2> ArrayList<T1> adjust(ArrayList<T2> list,AIS<T1,T2> ais){
        ArrayList<T1> rlist=new ArrayList<>();
        for(T2 t2:list){
            rlist.add(ais.action(t2));
        }
        return rlist;
    }
    public static interface AIS<T1,T2>{
        T1 action(T2 t2);
    }

    public static double sum_double(ArrayList<Double> list){
        double s=0;
        for(double i:list){
            s+=i;
        }
        return s;
    }
    public static int sum_int(ArrayList<Integer> list){
        int s=0;
        for(int i:list){
            s+=i;
        }
        return s;
    }

    public static interface BIS<T>{
        boolean act(T t);
    }
    public static <T> T getValue(ArrayList<T> list,BIS<T> bis){
        for(T t:list){
            if(bis.act(t))
                return t;
        }
        return null;
    }
    public static <T> boolean item_in(int[] list, int item){
        for(int i=0;i<list.length;i++){
            if(item==list[i])
                return true;
        }
        return false;
    }
}
