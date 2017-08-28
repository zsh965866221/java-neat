package com.zsh_o.neat;

import io.vavr.Tuple;
import io.vavr.Tuple2;

import java.util.*;

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
    public static <T,K> Iterable<Tuple2<T, K>> zip(Collection<T> c1, Collection<K> c2){
        return new Iterable<Tuple2<T, K>>() {
            @Override
            public Iterator<Tuple2<T, K>> iterator() {
                return new T2Iterator<>(c1,c2);
            }
        };
    }
    static class T2Iterator<T,K> implements Iterator<Tuple2<T,K>>{
        Iterator<T> iterator1;
        Iterator<K> iterator2;
        public boolean hasNext() {
            return iterator1.hasNext()&&iterator2.hasNext();
        }

        public Tuple2<T,K> next() {
            return Tuple.of(iterator1.next(),iterator2.next());
        }

        public T2Iterator(Collection<T> c1,Collection<K> c2) {
            iterator1=c1.iterator();
            iterator2=c2.iterator();
        }
        public void update(Collection<T> c1,Collection<K> c2) {
            iterator1=c1.iterator();
            iterator2=c2.iterator();
        }
    }
}
