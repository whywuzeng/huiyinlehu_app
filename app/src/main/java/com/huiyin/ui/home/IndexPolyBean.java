package com.huiyin.ui.home;

import java.io.Serializable;
import java.util.ArrayList;

import com.huiyin.bean.BaseBean;

public class IndexPolyBean extends BaseBean {
    private static final long serialVersionUID = 1L;
    
    public ArrayList<Polys> polyList;
    
    public class Polys implements Serializable {
        private static final long serialVersionUID = 1L;
        public ArrayList<Poly> polyList;
        /** 分类聚合类别1的名称*/
        public String POLY_TYPE_NAME;
        /**分类聚合类别1的图片*/
        public String POLY_TYPE_IMG;
        /**分类聚合类别1的id*/
        public int POLY_TYPE_ID;
    }

    public class Poly implements Serializable {
        private static final long serialVersionUID = 1L;
        /**分类聚合图片*/
        public String POLY_IMG;
        /**分类聚合的id*/
        public int ID;
        /**分类聚合所属类别id*/
        public int POLY_TYPE_ID;
        /**布局字段*/
        public int LAYOUT;
    }
    
}
