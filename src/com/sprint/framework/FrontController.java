package com.sprint.framework;

import java.io.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(name = "frontController", value = "/")
public class FrontController extends HttpServlet {
    public void processRequest (HttpServletRequest request,HttpServletResponse response)throws IOException{
        PrintWriter out = response.getWriter();
        String url = request.getRequestURI();
        out.println("you are in :"+ url );
    }
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        processRequest(request, response);
    }
}