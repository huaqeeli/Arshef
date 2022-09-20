
package controllers;

import java.util.Comparator;
import modeles.IndexingModel;


public class SortByDate implements Comparator<IndexingModel>{

    @Override
    public int compare(IndexingModel o1, IndexingModel o2) {
        return o1.getCirculardate().compareTo(o2.getCirculardate());
    }
    
}
