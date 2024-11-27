package com.sprint.helper;


import java.util.HashMap;


import com.sprint.framework.FrontController;

import com.sprint.objects.Mapping;
import com.sprint.utils.FrontUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;

public class FrontHelper {
	FrontController frontcontroller;
	
	public FrontHelper() {}
	public FrontHelper(FrontController frontconroller) {
		this.frontcontroller=frontconroller;
	}
	
	public FrontController getFrontcontroller() {
		return frontcontroller;
	}

	public void setFrontcontroller(FrontController frontcontroller) {
		this.frontcontroller = frontcontroller;
	}
	
	 public void initPackage() throws ServletException{
        String packagePath=getFrontcontroller().getInitParameter("packageControllers");
        if(packagePath.isEmpty()){
            throw new ServletException("ERROR 500 :Missing package in the 'webapps/WEB-INF/web.xml',in the init param->'packageControllers'");
        }
        else{
            Class<?>[] controllers= FrontUtil.getListControllers(packagePath);
            HashMap<String ,  Mapping> allMapping= FrontUtil.getAllMapping(controllers);
            getFrontcontroller().setAllMapping(allMapping);
        }
    }

    public void initMapping(HttpServletRequest req){
        if(!getFrontcontroller().getAllMapping().isEmpty()){
            String url = req.getRequestURI();
            url=FrontUtil.getMetaUrl(url);
            Mapping map = FrontUtil.getMapping(url , getFrontcontroller().getAllMapping());
            getFrontcontroller().setMapping(map);
        }
    }

    
}
