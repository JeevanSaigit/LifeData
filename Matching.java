package lifedata;

import java.util.*;

public class Matching {
    public static Map<String,String> match(Map<String,List<String>> graph){
        Map<String,String> owner=new LinkedHashMap<String,String>();
        for(String task:graph.keySet()){
            for(String resource:graph.get(task)){
                if(!owner.containsValue(resource)){owner.put(task,resource);break;}
            }
        }
        return owner;
    }
}
