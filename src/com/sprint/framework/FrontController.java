package com.sprint.framework;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import com.sprint.exception.ConvertException;
import com.sprint.exception.ReturnException;
import com.sprint.exception.SprintException;
import com.sprint.helper.FrontHelper;
import com.sprint.objects.Mapping;
import com.sprint.objects.ModelView;
import com.sprint.utils.FrontUtil;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;

public class FrontController extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private HashMap<String , Mapping > allMapping;
    private Mapping mapping;
    private boolean init=false;
    
	public boolean isInit() {
		return init;
	}

	public void setInit(boolean init) {
		this.init = init;
	}

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
    
    public void initialize ()throws ServletException {
		FrontHelper helper = new FrontHelper(this);
		helper.initPackage();
    }
    
    public void getData(  HttpServletRequest req , HttpServletResponse res , String verb) throws ReturnException,
		    ServletException, IOException, ClassNotFoundException, InstantiationException,
		    IllegalAccessException, IllegalArgumentException, InvocationTargetException, 
		    NoSuchMethodException, SecurityException, SprintException, ConvertException, ParseException 
    {
        PrintWriter out = res.getWriter();       
        if(this.getMapping()!=null) {
            Object obj = this.getMapping().excecute(req , verb);
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
                throw new ReturnException("ERROR 400: Type de retour non trouve. Le type peut être java.lang.String ou com.objects.ModelView.");
            }
        }else{
           throw new ServletException("ERROR 404: URL NON TROUVÉE OU N'EXISTE PAS.");
        }
    }
    public void initFrontController(HttpServletRequest req) throws ServletException  {
    	if(!isInit()) {
    		this.setInit(true);
    		this.initialize();
    	}
    	FrontHelper helper = new FrontHelper(this);
    	helper.initMapping(req);    		
    }
    public void processRequest(HttpServletRequest request, HttpServletResponse response , String verb) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ReturnException, ServletException, IOException, SprintException, ConvertException, ParseException  {
    	this.initFrontController(request);
        this.getData(request , response , verb);
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter out = resp.getWriter();
        resp.setContentType("text/html");
        try {
            this.processRequest(req, resp,"Get");
        }catch (Exception e){
        	e.printStackTrace();
        	out.println(FrontUtil.generateErrorPage(e.getMessage())) ;
        }
    }
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    	 PrintWriter out = resp.getWriter();
         resp.setContentType("text/html");
        try {
            this.processRequest(req, resp,"Post");
        }catch (Exception e){
        	e.printStackTrace();
        	out.println(FrontUtil.generateErrorPage(e.getMessage())) ;
        }
    }
}