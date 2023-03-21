package etu2079.framework.servlet;

import etu2079.framework.Mapping;

import etu2079.framework.MethAnnotation;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@WebServlet(name = "FrontServlet", value = "/FrontServlet")
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
        String[] urlsplitted = URL.split("/");
        //System.out.println("Target >>> "+urlsplitted[urlsplitted.length-1]);
        return urlsplitted[urlsplitted.length-1];
    }

    private static void findAnnotedMethod(){

    }

    public List<File> packagesFiles(String[] packages){
        List<File> files = new ArrayList<>();

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        for(String pkg : packages){
            files.add(new File(Objects.requireNonNull(classLoader.getResource(pkg.replace('.', '/'))).getFile()));
        }
        return files;
    }

    private void fillMappingUrls() throws ClassNotFoundException {
        this.setMappingUrls(new HashMap<String, Mapping>());

        String[] packages = new String[1];
        packages[0] = "etu2079.framework.servlet";
        List<File> packageDir = this.packagesFiles(packages);

        for(int i=0 ; i < packages.length ; i++){
            if(packageDir.get(i).exists()){
                String[] files = packageDir.get(i).list();
                assert files != null;
                for(String file : files){
                    if(file.endsWith(".class")){
                        String className = file.substring(0, file.length()-6 /* exemple: test.class >> enlever .class */);
                        Class<?> clazz = Class.forName(packages[i] + '.' + className);
                        for(Method method : clazz.getDeclaredMethods()){
                            if(method.isAnnotationPresent(MethAnnotation.class)){
                                MethAnnotation methannot = (MethAnnotation) method.getAnnotation(MethAnnotation.class);
                                this.getMappingUrls().put(methannot.name(), new Mapping(className, method.getName()));
                                System.out.println("Class: "+className);
                                System.out.println("Method: "+methannot.name()+" \n");
                            }
                        }
                    }
                }
            }
        }
        System.out.println("Mapping length: "+this.getMappingUrls().size());
    }
}
