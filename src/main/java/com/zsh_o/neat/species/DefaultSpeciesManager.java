package com.zsh_o.neat.species;

import com.zsh_o.neat.Config;
import com.zsh_o.neat.Utils;
import com.zsh_o.neat.genome.DefaultGenome;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by zsh96 on 2017/8/24.
 */
public class DefaultSpeciesManager {
    Config config;
    int max_species_num=0;
    public ArrayList<Species> species=new ArrayList<>();
    HashMap<Integer,Integer> genome_to_species=new HashMap<>();

    public DefaultSpeciesManager(Config config) {
        this.config = config;
    }

    public void speciate(ArrayList<DefaultGenome> population, int generation){
        HashSet<Integer> unspeciated=new HashSet<Integer>(population.stream().map(s->s.key).collect(Collectors.toList()));
        HashMap<Integer,Integer> new_representatives=new HashMap<>();
        HashMap<Integer,ArrayList<Integer>> new_members=new HashMap<>();

        GenomeDistanceCache distanceCache= new GenomeDistanceCache(config);
        for(Species s:species){
            ArrayList<Candiate> candidates=new ArrayList<>();
            for(int gid:unspeciated){
                DefaultGenome g= Utils.getValue(population,ss->ss.key==gid);
                double d=distanceCache.getDistance(s.representative,g);
                candidates.add(new Candiate(d,g.key));
            }

            Candiate ct= candidates.stream().min(Comparator.comparing(cc->cc.d)).get();
            double ignored_rdist=ct.d;
            int new_rid=ct.g;
            new_representatives.put(s.key,new_rid);
            addMapList(new_members,s.key,new_rid);
            unspeciated.remove(new_rid);
        }
        Iterator<Integer> ui=unspeciated.iterator();
        while(ui.hasNext()){
            Integer gid=ui.next();
            DefaultGenome g= Utils.getValue(population,ss->ss.key==gid);
            ArrayList<Candiate> candidates=new ArrayList<>();
            Iterator iterator= new_representatives.entrySet().iterator();
            while(iterator.hasNext()){
                Map.Entry<Integer,Integer> entry= (Map.Entry) iterator.next();
                int sid=entry.getKey();
                int rid=entry.getValue();
                DefaultGenome rep=Utils.getValue(population,ss->ss.key==rid);
                double d=distanceCache.getDistance(rep,g);
                if(d<config.defaultSpeciesConfig.compatibility_threshold){
                    candidates.add(new Candiate(d,sid));
                }
            }

            //找出距离gid最近的每个种群的代表节点，把其加入到距离最近的种群中
            if(!candidates.isEmpty()){
                Candiate ct=candidates.stream().min(Comparator.comparing(cc->cc.d)).get();
                addMapList(new_members,ct.g,gid);
            }else{
                int sid=getNextSpeciesId();
                new_representatives.put(sid,gid);
                addMapList(new_members,sid,gid);
            }
        }
        unspeciated.clear();
        genome_to_species.clear();
        Iterator iterator= new_representatives.entrySet().iterator();
        while(iterator.hasNext()) {
            Map.Entry<Integer, Integer> entry = (Map.Entry) iterator.next();
            int sid = entry.getKey();
            int rid = entry.getValue();
            Species s=getSpecies(sid);
            if(s==null){
                s=new Species(sid,generation);
                species.add(s);
            }
            ArrayList<Integer> members=new_members.get(sid);
            for(int gid:members){
                genome_to_species.put(gid,sid);
            }
            ArrayList<DefaultGenome> member_dict=new ArrayList<>();
            for(int sk:members){
                member_dict.add(Utils.getValue(population,ss->ss.key==sk));
            }
            s.update(Utils.getValue(population,ss->ss.key==rid),member_dict);

        }

    }


    public void addMapList(HashMap<Integer, ArrayList<Integer>> map, int key, Integer value){
        if(!map.containsKey(key)){
            map.put(key,new ArrayList<>());
        }
        map.get(key).add(value);
    }
    public Species getSpecies(int key){
        for(Species s:species){
            if(s.key==key){
                return s;
            }
        }
        return null;
    }

    public int getNextSpeciesId(){
        max_species_num++;
        return max_species_num;
    }
    class Candiate{
        double d;
        int g;

        public Candiate(double d, int g) {
            this.d = d;
            this.g = g;
        }
    }

    public int get_species_id(int individual_id){
        return genome_to_species.get(individual_id);
    }
    public Species get_species(int individual_id){
        int sid=get_species_id(individual_id);
        return species.get(sid);
    }
}
