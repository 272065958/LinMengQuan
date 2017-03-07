package net.kamfat.omengo.bean;

/**
 * Created by cjx on 2016/7/29.
 */
public class TreeBean {
    public String _id;
    public String project_name;
    public String use_area;
    public int hasChild;
    public boolean isChild(){
        return hasChild == 0;
    }
}
