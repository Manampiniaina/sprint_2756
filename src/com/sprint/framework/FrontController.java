package com.sprint.framework;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import com.sprint.helper.FrontHelper;
import com.sprint.objects.Mapping;
import com.sprint.objects.ModelView;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.*;

public class FrontController extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private HashMap<String , Mapping > allMapping;
    private Mapping mapping;
	public Mapping getMapping() {
        return mapping;
    }

    public void setMapping(Mapping mapping) {
        this.mapping = mapping;
    }

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public HashMap<String, Mapping> getAllMapping() {
        return allMapping;
    }

    public void setAllMapping(HashMap<String, Mapping> allMapping) {
        this.allMapping = allMapping;
    }
    public void init() {
		try {
			FrontHelper helper = new FrontHelper(this);
			helper.initPackage();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
    }
    public void getData(  HttpServletRequest req , HttpServletResponse res) throws Exception {
        PrintWriter out = res.getWriter();       
        if(this.getMapping()!=null) {
                Object obj = this.getMapping().excecute(req);
                
                if (obj.getClass().getName().equals("java.lang.String")) {
                    out.println( (String)obj);
                }
                else if (obj.getClass().getName().equals("com.sprint.objects.ModelView")){
                    ModelView mv = (ModelView)obj;
                    for ( Map.Entry<String, Object> entry :   mv.getData().entrySet()) {
                        req.setAttribute(entry.getKey(), entry.getValue());
                    }
                    RequestDispatcher dispatcher = req.getRequestDispatcher( req.getContextPath() +"/../"+mv.getUrl());
                    dispatcher.forward(req, res);
                }
                else{
                    throw new Exception("ERROR 3: return type not found , the type may a java.lang.String or a com.objects.ModelView ");
                }
        }else{
           throw new Exception("ERROR 404: URL/PAGE NOT FOUND OR DOESN'T EXIST ");
        }
    }
    public void initFrontController(HttpServletRequest req) throws Exception {
   	 	FrontHelper helper = new FrontHelper(this);
    	helper.initMapping(req);
    	
    }
    public void processRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	this.initFrontController(request);
        this.getData(request , response);
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter out = resp.getWriter();
        try {
            this.processRequest(req, resp);
        }catch (Exception e){
        	e.printStackTrace();
            out.println(e.getMessage());
        }
    }
}