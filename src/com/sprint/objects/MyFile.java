package com.sprint.objects;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;

public class MyFile {
	private String path;
	private String destination;
	private HttpServletRequest request;
	
	
	public MyFile(String path, String destination, HttpServletRequest request) {
		super();
		this.path = path;
		this.destination = destination;
		this.request = request;
	}
	public HttpServletRequest getRequest() {
		return request;
	}
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	
	public void write () throws IOException, ServletException {
		Part filePart = this.getRequest().getPart("file"); 
        String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString(); 
        File file = new File(this.destination, fileName);
		try (InputStream fileContent = filePart.getInputStream();
			FileOutputStream outputStream = new FileOutputStream(file)) {
	        byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fileContent.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
		}
	}
}
