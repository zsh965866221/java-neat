package com.zsh_o.test;

import com.zsh_o.neat.Utils;
import io.vavr.Tuple2;

import java.util.ArrayList;

/**
 * Created by zsh96 on 2017/8/28.
 */
public class TestZip {
    public static void main(String[] args){
        ArrayList<String> list1=new ArrayList<>();
        ArrayList<Integer> list2=new ArrayList<>();

        list1.add("1");list1.add("2");list1.add("3");
        list2.add(1);list2.add(2);list2.add(3);

        for(Tuple2<String,Integer> t: Utils.zip(list1,list2)){
            System.out.println(t._1()+" "+t._2());
        }
    }
}
