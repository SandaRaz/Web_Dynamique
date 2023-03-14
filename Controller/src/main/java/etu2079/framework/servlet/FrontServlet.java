package etu2079.framework.servlet;

import etu2079.framework.Mapping;

import etu2079.framework.MethAnnotation;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.HashMap;

public class FrontServlet extends HttpServlet {
    RequestDispatcher dispat;
    HttpServletRequest request;
    HttpServletResponse response;
    private HashMap<String, Mapping> MappingUrls;

    @Override
    public void init() throws ServletException {
        try {
            this.fillMappingUrls();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public RequestDispatcher getDispat() {
        return this.dispat;
    }
    public void setDispat(RequestDispatcher dispat) {
        this.dispat = dispat;
    }
    public HttpServletRequest getRequest() {
        return this.request;
    }
    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }
    public HttpServletResponse getResponse() {
        return this.response;
    }
    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    public HashMap<String, Mapping> getMappingUrls() {
        return MappingUrls;
    }

    public void setMappingUrls(HashMap<String, Mapping> mappingUrls) {
        MappingUrls = mappingUrls;
    }

    public void redirect(String indexOfFile) throws ServletException, IOException {
        this.setDispat(this.request.getRequestDispatcher(indexOfFile));
        this.getDispat().forward(this.getRequest(), this.getResponse());
    }

    protected void processRequest() throws IOException {
        PrintWriter out = this.getResponse().getWriter();
        String incommingURL = String.valueOf(this.getRequest().getRequestURL());
        String target = this.getTarget(incommingURL);
        System.out.println("URL target >> "+target);
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.setRequest(req);
        this.setResponse(resp);

        this.processRequest();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.setRequest(req);
        this.setResponse(resp);

        this.processRequest();
    }

    private String removeHttpProtocoleStr(String URL){
        return URL.split("//")[1];
    }

    private String getTarget(String URL){
        URL = removeHttpProtocoleStr(URL);

        if(URL.toLowerCase().contains("_war")){
            System.out.println("'war' artifact detected");
            return URL.split("/")[2];
        }else{
            return URL.split("/")[1];
        }
    }

    private static void findAnnotedMethod(){

    }

    private void fillMappingUrls() throws ClassNotFoundException {
        this.setMappingUrls(new HashMap<String, Mapping>());

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String myPackage = "etu2079.framework.servlet";
        String path = myPackage.replace('.', '/');
        URL resource = classLoader.getResource(path);

        assert resource != null;
        File directory = new File(resource.getFile());
        if(directory.exists()){
            String[] files = directory.list();
            assert files != null;
            for(String file : files){
                if(file.endsWith(".class")){
                    String className = file.substring(0, file.length()-6 /* exemple: test.class >> enlever .class */);
                    Class<?> clazz = Class.forName(myPackage + '.' + className);
                    for(Method method : clazz.getDeclaredMethods()){
                        if(method.isAnnotationPresent(MethAnnotation.class)){
                            MethAnnotation methannot = (MethAnnotation) method.getAnnotation(MethAnnotation.class);
                            this.getMappingUrls().put(methannot.name()+'-'+className , new Mapping(className, methannot.name()));
                            System.out.println("Class: "+className);
                            System.out.println("Method: "+methannot.name()+" \n");
                        }
                    }
                }
            }
        }
    }
}
