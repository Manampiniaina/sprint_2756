package com.sprint.framework;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import com.sprint.objects.Mapping;
import com.sprint.objects.ModelView;
import com.sprint.utils.FrontUtil;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.*;

public class FrontController extends HttpServlet {
    private boolean init = false;
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

    public void initPackage() throws Exception{
        String packagePath=this.getInitParameter("packageControllers");
        if(packagePath.isEmpty()){
            throw new Exception("ERROR 1:Missing package in the 'webapps/WEB-INF/web.xml',in the init param->'packageControllers'");
        }
        else{
            Class<?>[] controllers= FrontUtil.getListControllers(packagePath);
            HashMap<String ,  Mapping> allMapping= FrontUtil.getAllMapping(controllers);
            setAllMapping(allMapping);
        }
    }

    public void initMapping(HttpServletRequest req){
        if(!getAllMapping().isEmpty()){
            String url = req.getRequestURI();
            url=FrontUtil.getMetaUrl(url);
            Mapping map = FrontUtil.getMapping(url , this.getAllMapping());
            setMapping(map);
        }
    }
    public void getData(  HttpServletRequest req , HttpServletResponse res) throws Exception {
        PrintWriter out = res.getWriter();
        if(this.getMapping()!=null) {
                Object obj = this.getMapping().excecute();
                if (obj.getClass().getName().equals("java.lang.String")) {
                    out.println( "data:"+(String)obj);
                }
                else if (obj.getClass().getName().equals("com.sprint.objects.ModelView")){
                    ModelView mv = (ModelView)obj;
                    for ( Map.Entry<String, Object> entry :   mv.getData().entrySet()) {
                        req.setAttribute(entry.getKey(), entry.getValue());
                    }

                    String urlNow=FrontUtil.getMetaUrl(req.getRequestURI());
                    String add=FrontUtil.getAddDispactcher(urlNow);

                    RequestDispatcher dispatcher = req.getRequestDispatcher( add + mv.getUrl());

                    dispatcher.forward(req, res);
                }
                else{
                    throw new Exception("ERROR 3: return type not found , the type may a java.lang.String or a com.objects.ModelView ");
                }

        }else{
           throw new Exception("ERROR 404 : page not found");
        }
    }
    public void processRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if(!this.init){
            this.initPackage();
            this.init=true;
        }
        this.initMapping(request);
        this.getData(request , response);
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter out = resp.getWriter();
        try {
            this.processRequest(req, resp);
        }catch (Exception e){
            out.println(e.getMessage());
        }
    }
}