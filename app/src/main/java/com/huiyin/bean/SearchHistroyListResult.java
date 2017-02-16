package com.huiyin.bean;

import java.util.ArrayList;
import java.util.List;

public class SearchHistroyListResult {
	public ArrayList<Commodity> resultList=new ArrayList<Commodity>();
    public class Commodity{
    	public String SUMCOM;//总数量
    	public String CATEGORY_NAME;//属性名称
    	public String ID;//属性id
    	public String NUM;//数量
    }
}
