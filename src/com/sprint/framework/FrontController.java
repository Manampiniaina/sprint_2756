package com.sprint.framework;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import com.sprint.objects.Mapping;
import com.sprint.utils.FrontUtil;
import jakarta.servlet.http.*;

public class FrontController extends HttpServlet {
    private HashMap<String , Mapping > allMapping;
    private Mapping mapping;

    public Mapping getMapping() {
        return mapping;
    }

    public void setMapping(Mapping mapping) {
        this.mapping = mapping;
    }

    public HashMap<String, Mapping> getAllMapping() {
        return allMapping;
    }

    public void setAllMapping(HashMap<String, Mapping> allMapping) {
        this.allMapping = allMapping;
    }

    public void init(){
        String packagePath=this.getInitParameter("packageControllers");
        Class<?>[] controllers= FrontUtil.getListControllers(packagePath);
        HashMap<String ,  Mapping> allMapping= FrontUtil.getAllMapping(controllers);
        setAllMapping(allMapping);
    }

    public void initMapping(HttpServletRequest req){
        if(!getAllMapping().isEmpty()){
            String url = req.getRequestURI();
            url=FrontUtil.getMetaUrl(url);
            Mapping map = FrontUtil.getMapping(url , this.getAllMapping());
            if(map!=null){
                setMapping(map);
            }
        }
    }

    public void processRequest(HttpServletRequest request, HttpServletResponse response)throws
            IOException,ClassNotFoundException ,
            NoSuchMethodException , InstantiationException ,
            IllegalAccessException , InvocationTargetException {
        PrintWriter out = response.getWriter();
        this.initMapping(request);
        if(this.getMapping()!=null){
           Object obj=this.getMapping().excecute();
           String output=(String)obj;
           out.println("output->" + output);
        }
        else{
            out.println("error 404 , url not found . ");
        }
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            this.processRequest(request, response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}