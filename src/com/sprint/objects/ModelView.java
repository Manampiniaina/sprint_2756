package com.sprint.objects;

import java.util.HashMap;

public class ModelView {
    private String url;
    private HashMap<String ,  Object> data = new HashMap<String , Object>();

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public HashMap<String, Object> getData() {
        return data;
    }

    public void setData(HashMap<String, Object> data) {
        this.data = data;
    }

    public void add(String name ,  Object obj){
        HashMap<String,Object> newdata= this.getData();
        if(newdata==null){
           newdata = new HashMap<String , Object>();
        }
        newdata.put(name,obj);
        this.setData(newdata);
    }

}
