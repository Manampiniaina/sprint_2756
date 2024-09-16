package com.sprint.framework;

import java.io.*;
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

    public void processRequest(HttpServletRequest request, HttpServletResponse response)throws IOException{
        PrintWriter out = response.getWriter();
        this.initMapping(request);
        if(this.getMapping()!=null){
            out.println("You are in : controller->" +this.getMapping().getControllerName() + " / method->" + this.getMapping().getMethodName());
        }
        else{
            out.println("error 404 , url not found . ");
        }
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        this.processRequest(request, response);
    }

}