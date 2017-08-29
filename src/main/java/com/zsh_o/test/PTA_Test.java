package com.zsh_o.test;

import com.zsh_o.neat.Config;
import com.zsh_o.neat.FeedForward;
import com.zsh_o.neat.FitnessFunc;
import com.zsh_o.neat.Population;
import com.zsh_o.neat.gene.DefaultConnectionGene;
import com.zsh_o.neat.genome.DefaultGenome;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by zsh96 on 2017/8/29.
 */
public class PTA_Test {
    public static double[][] train_in;
    public static double[][] train_out;
    public static double[][] test_in;
    public static double[][] test_out;
    public double[][] read(String path,int row,int col,String sep) throws IOException {
        InputStreamReader reader=new InputStreamReader(new FileInputStream(path));
        BufferedReader br=new BufferedReader(reader);
        double[][] data=new double[row][col];
        String line="";
        for(int i=0;i<row;i++){
            line=br.readLine();
            String[] cs=line.split(sep);
            for(int j=0;j<col;j++){
                data[i][j]=Double.valueOf(cs[j]);
            }
        }
        return data;
    }

    public static void main(String[] args) throws IOException, CloneNotSupportedException {
        PTA_Test test=new PTA_Test();
        train_in=test.read("E:\\workstation\\github\\java-neat\\src\\main\\resources\\train_pta_in.txt",172,17," ");
        train_out=test.read("E:\\workstation\\github\\java-neat\\src\\main\\resources\\train_pta_out.txt",172,1," ");
        test_in=test.read("E:\\workstation\\github\\java-neat\\src\\main\\resources\\test_pta_in.txt",86,17," ");
        test_out=test.read("E:\\workstation\\github\\java-neat\\src\\main\\resources\\test_pta_out.txt",86,1," ");

        Config config=new Config();
        Population population= new Population(config);
        DefaultGenome winner= population.run(new FitnessFunc() {
            @Override
            public void calculate(ArrayList<DefaultGenome> genomes, Config config) {
                for(DefaultGenome genome:genomes){
                    genome.fitness=10;
                    FeedForward net=new FeedForward(genome,config);
                    double sum=0;
                    for(int i=0;i<train_in.length;i++){
                        double[] output=net.activate(train_in[i]);
                        sum+=(Math.pow(output[0]-train_out[i][0],2));
                    }
                    genome.fitness-=(sum);
                }
            }
        }, 300);

        System.out.println("----------------------------------------------------------");
        for(DefaultConnectionGene gene:winner.connections){
            if(!gene.enabled.getValue())
                continue;
            System.out.println(gene.in +"\t -> \t"+gene.out);
        }
        System.out.println(winner.fitness);

        FeedForward net=new FeedForward(winner,config);
        System.out.println("final species size: "+config.speciesManager.species.size());
        System.out.println("over");

    }
}
