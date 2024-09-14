package com.sprint.framework;

import java.io.*;

import com.sprint.utils.FrontUtil;
import jakarta.servlet.http.*;

public class FrontController extends HttpServlet {
    private boolean scanController = false ;
    private Class<?>[] controllers;

    private boolean isScanController() {
        return scanController;
    }

    private Class<?>[] getControllers() {
        return controllers;
    }

    private void setControllers(Class<?>[] controllers) {
        this.controllers = controllers;
    }

    private void setScanController() {
        this.scanController = true;
    }

    private void initControllers(){
        if(!isScanController()){
            String packagePath=this.getInitParameter("packageControllers");
            Class<?>[] classes= FrontUtil.getListControllers(packagePath);
            setControllers(classes);
            setScanController();
        }

    }
    private void printControllers(HttpServletResponse response) throws IOException{
        PrintWriter out = response.getWriter();
        if(getControllers().length >0 ){
            out.println("list of controllers : ");
            for (int i = 0; i <getControllers().length ; i++) {
                out.println(getControllers()[i].getName());
            }
        }
        else{
            out.println("No controllers found");
        }
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response)throws IOException{
        initControllers();
        printControllers(response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        processRequest(request, response);
    }


}