package com.huiyin.ui.home;

import java.io.Serializable;
import java.util.ArrayList;

import com.huiyin.bean.BaseBean;
import com.huiyin.ui.home.module.Fast;

public class IndexFirstBean extends BaseBean {
    private static final long serialVersionUID = 1L;
    
    public ArrayList<Banner> bannerList;
    public ArrayList<Fast> fastList;
    public ArrayList<Chartposition> chartpositionList;
    public Prommotion prommotionLayout;
    
    
    public class Banner implements Serializable{
        private static final long serialVersionUID = 1L;
        /**banner的图片对应的url*/
        public String URL;
        public String NUM;
        /**banner的图片*/
        public String IMG;
    }

//    public class Fast{
//        /**快捷服务的名字*/
//        public String FAST_NAME;
//        /**快捷服务的id*/
//        public int ID;
//        /**快捷服务的图片*/
//        public String FAST_IMG;
//    }

    public class Prommotion implements Serializable{
        private static final long serialVersionUID = 1L;
        /**图片区域1*/
        public String IMG1;
        /**图片区域2*/
        public String IMG2;
        /**图片区域3*/
        public String IMG3;
        /**图片区域1对应的url*/
        public String IMG1URL;
        /**图片区域2对应的url*/
        public String IMG2URL;
        /**图片区域3对应的url*/
        public String IMG3URL;
        /**布局对应的id，1表示布局1，2表示布局2*/        
        public int LAYOUTID;
        
        public String KEY1;
        public String KEY2;
        public String KEY3;
        public String KEY4;
        public String KEY5;
        public String KEY6;
        public String KEY7;
        public String KEY8;
        public String KEY9;
        public String KEY10;
        public String KEY11;
        public String KEY12;
        
        public String KEY1URL;
        public String KEY2URL;
        public String KEY3URL;
        public String KEY4URL;
        public String KEY5URL;
        public String KEY6URL;
        public String KEY7URL;
        public String KEY8URL;
        public String KEY9URL;
        public String KEY10URL;
        public String KEY11URL;
        public String KEY12URL;
    }
    
    public class Chartposition implements Serializable{
        private static final long serialVersionUID = 1L;
        public String URL;
        public String NUM;
        public String IMG;
    }
    
}
