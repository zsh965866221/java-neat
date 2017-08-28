package com.zsh_o.neat.genome;

import com.zsh_o.neat.Config;
import com.zsh_o.neat.Utils;
import com.zsh_o.neat.gene.DefaultConnectionGene;
import com.zsh_o.neat.gene.DefaultNodeGene;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Created by zsh96 on 2017/8/23.
 */
public class DefaultGenome {
    Config config;
    public ArrayList<DefaultNodeGene> nodes =new ArrayList<>();
    public ArrayList<DefaultConnectionGene> connections=new ArrayList<>();
    DefaultGenomeManager manager;
    public int key;

    public double fitness=-1e20;

    public DefaultGenome(int key,DefaultGenomeManager manager) {
        this.manager = manager;
        this.config=manager.config;
        this.key=key;

        for(int i=0;i<manager.output_keys.length;i++){
            setNode(new DefaultNodeGene(manager.output_keys[i],config));
        }

        if(config.defalutGenomeConfig.initial_connection.equals("full_forward")){
            connect_full_forward();
        }

    }

    public DefaultConnectionGene getConnection(int in, int out){
        for (DefaultConnectionGene gene:
                connections) {
            if(gene.in==in&&gene.out==out)
                return gene;
        }
        return null;
    }
    public void setConnection(DefaultConnectionGene gene){
        boolean existed=false;
        for(int i = 0; i< connections.size(); i++){
            DefaultConnectionGene g= connections.get(i);
            if(g.in==gene.in&&g.out==gene.out){
                connections.set(i,gene);
                existed=true;
            }
        }
        if(!existed){
            connections.add(gene);
        }
    }
    public DefaultNodeGene getNode(int key){
        for(int i=0;i<nodes.size();i++){
            DefaultNodeGene n=nodes.get(i);
            if(n.key==key)
                return n;
        }
        return null;
    }
    public void delNode(int key) {
        int index = -1;
        for (int i = 0; i < nodes.size(); i++) {
            DefaultNodeGene n = nodes.get(i);
            if (n.key == key)
                index=i;
        }
        if(index>=0){
            nodes.remove(index);
        }
    }
    public void delConnection(int in,int out){
        int index=-1;
        for(int i=0;i<connections.size();i++){
            DefaultConnectionGene c=connections.get(i);
            if(c.in==in&&c.out==out)
                index=i;
        }
        if(index>=0){
            connections.remove(index);
        }
    }
    public void setNode(DefaultNodeGene gene){
        boolean existed=false;
        for(int i=0;i<nodes.size();i++){
            DefaultNodeGene n=nodes.get(i);
            if(n.key==gene.key){
                nodes.set(i,gene);
                existed=true;
            }
        }
        if(!existed){
            nodes.add(gene);
        }
    }

    public void connect_full_forward(){
        for(int i=0;i<manager.input_keys.length;i++){
            for(int j=0;j<manager.output_keys.length;j++){
                setConnection(manager.create_connection(manager.input_keys[i],manager.output_keys[j]));
            }
        }
    }

    public void crossover(DefaultGenome genome1,DefaultGenome genome2) throws CloneNotSupportedException {
        DefaultGenome parent1,parent2;
        if(genome1.fitness>genome2.fitness){
            parent1=genome1;
            parent2=genome2;
        }else{
            parent1=genome2;
            parent2=genome1;
        }

        for (DefaultConnectionGene cg1 :
                parent1.connections) {
            DefaultConnectionGene cg2=parent2.getConnection(cg1.in,cg1.out);
            if(cg2==null){
                this.setConnection((DefaultConnectionGene) cg1.clone());
            }else{
                this.setConnection((DefaultConnectionGene) cg1.crossover(cg2));
            }
        }

        for (DefaultNodeGene ng1 :
                parent1.nodes) {
            DefaultNodeGene ng2=parent2.getNode(ng1.key);
            if(ng2==null){
                this.setNode((DefaultNodeGene) ng1.clone());
            }else{
                this.setNode((DefaultNodeGene) ng1.crossover(ng2));
            }
        }
    }

    public void mutate(){
        if(config.defalutGenomeConfig.single_structural_mutation){
            double div=Math.max(1,(config.defalutGenomeConfig.node_add_prob+config.defalutGenomeConfig.node_delete_prob+
                                config.defalutGenomeConfig.conn_add_prob+config.defalutGenomeConfig.conn_delete_prob));
            double r= Utils.RANDOM.nextDouble();
            if(r<config.defalutGenomeConfig.node_add_prob/div){
                mutate_add_node();
            }else if(r<((config.defalutGenomeConfig.node_add_prob+config.defalutGenomeConfig.node_delete_prob)/div)){
                mutate_delete_node();
            }else if(r<(config.defalutGenomeConfig.node_add_prob+config.defalutGenomeConfig.node_delete_prob+
                        config.defalutGenomeConfig.conn_add_prob)/div){
                mutate_add_connection();
            }else if(r<(config.defalutGenomeConfig.node_add_prob+config.defalutGenomeConfig.node_delete_prob+
                        config.defalutGenomeConfig.conn_add_prob+config.defalutGenomeConfig.conn_delete_prob)){
                mutate_delete_connection();
            }
        }else{
            if(Utils.RANDOM.nextDouble()<config.defalutGenomeConfig.node_add_prob){
                mutate_add_node();
            }
            if(Utils.RANDOM.nextDouble()<config.defalutGenomeConfig.node_delete_prob){
                if(nodes.size()>config.defalutGenomeConfig.num_outputs){
                    mutate_delete_node();
                }
            }
            if(Utils.RANDOM.nextDouble()<config.defalutGenomeConfig.conn_add_prob){
                mutate_add_connection();
            }
            if(Utils.RANDOM.nextDouble()<config.defalutGenomeConfig.conn_delete_prob){
                mutate_delete_connection();
            }
        }

        for(DefaultConnectionGene gene:connections){
            gene.mutate();
        }
        for(DefaultNodeGene gene:nodes){
            gene.mutate();
        }
    }

    public void mutate_add_node(){
        DefaultConnectionGene conn_to_split=Utils.choice(connections);
        DefaultNodeGene ng= manager.create_node();
        setNode(ng);
        conn_to_split.enabled.setValue(false);
        add_connection(conn_to_split.in,ng.key,1.0,true);
        add_connection(ng.key,conn_to_split.out,conn_to_split.weight.getValue(),true);
    }
    public void add_connection(int in,int out,double weight,boolean enabled){
        DefaultConnectionGene cg=new DefaultConnectionGene(in,out,manager.config);
        cg.weight.setValue(weight);
        cg.enabled.setValue(enabled);
        setConnection(cg);
    }
    public void mutate_add_connection(){
        int out=nodes.get(Utils.RANDOM.nextInt(nodes.size())).key;
        ArrayList<Integer> ta=new ArrayList<>();
        for(int i=0;i<manager.input_keys.length;i++){
            ta.add(manager.input_keys[i]);
        }
        for(int i=manager.output_keys.length;i<nodes.size();i++){
            ta.add(nodes.get(i).key);
        }
        int in=Utils.choice(ta);

        if(getConnection(in,out)!=null){
            getConnection(in,out).enabled.setValue(true);
            return;
        }
        if(Utils.item_in(manager.output_keys,in)&& Utils.item_in(manager.output_keys,out)){
            return;
        }
        if(Utils.item_in(manager.output_keys,in)){
            return;
        }

        if(config.defalutGenomeConfig.feed_forward&&manager.creates_cycle(connections,in,out)){
            return;
        }

        setConnection(manager.create_connection(in,out));
    }

    public int mutate_delete_node(){
        if(nodes.size()-config.defalutGenomeConfig.num_outputs<=0)
            return -1;
        int del_key=Utils.RANDOM.nextInt(nodes.size()-config.defalutGenomeConfig.num_outputs)+config.defalutGenomeConfig.num_outputs;
        delNode(del_key);
        for(int i=0;i<nodes.size();i++){
            delConnection(nodes.get(i).key,del_key);
            delConnection(del_key,nodes.get(i).key);
        }
        return del_key;
    }
    public void mutate_delete_connection(){
        int del_index=Utils.RANDOM.nextInt(connections.size());
        connections.remove(del_index);
    }

    public int getEnabledConnectionsNum(){
        int num=0;
        for(DefaultConnectionGene gene:connections){
            if(gene.enabled.getValue()){
                num++;
            }
        }
        return num;
    }

    public double distance(DefaultGenome other){
        double node_distance=0;
        if(this.nodes.size()>0||other.nodes.size()>0){
            double disjoint_nodes=0;
            for(DefaultNodeGene n2:other.nodes){
                if(this.getNode(n2.key)==null){
                    disjoint_nodes+=1;
                }
            }
            for(DefaultNodeGene n1:this.nodes){
                DefaultNodeGene n2=other.getNode(n1.key);
                if(n2==null){
                    disjoint_nodes+=1;
                }else{
                    node_distance+=n1.distance(n2);
                }
            }
            int max_nodes=Math.max(this.nodes.size(),other.nodes.size());
            node_distance = (node_distance + (config.defalutGenomeConfig.compatibility_disjoint_coefficient *
                    disjoint_nodes))/max_nodes;
        }
        double connection_distance=0;
        if(this.connections.size()>0||other.connections.size()>0){
            double disjoint_connections=0;
            for(DefaultConnectionGene c2:other.connections){
                if(this.getConnection(c2.in,c2.out)==null){
                    disjoint_connections+=1;
                }
            }
            for(DefaultConnectionGene c1:this.connections){
                DefaultConnectionGene c2=other.getConnection(c1.in,c1.out);
                if(c2==null){
                    disjoint_connections+=1;
                }else{
                    node_distance+=c1.distance(c2);
                }
            }
            int max_conn=Math.max(this.connections.size(),other.connections.size());
            connection_distance = (connection_distance + (config.defalutGenomeConfig.compatibility_disjoint_coefficient *
                    disjoint_connections))/max_conn;
        }
        return node_distance+connection_distance;
    }

    @Override
    public String toString() {
        return "key=" + key+
                "fitness=" + fitness +
                "nodes=" + nodes +
                "connections=" + connections;
    }
}
