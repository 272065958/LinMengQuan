package net.kamfat.omengo.bean;

/**
 * Created by cjx on 2016/7/29.
 */
public class TreeBean {
    public String id;
    public String name;
    public int hasChild;
    public boolean isChild(){
        return hasChild == 0;
    }
}
