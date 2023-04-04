package etu2079.framework.servlet;

import etu2079.framework.Mapping;
import etu2079.framework.annotation.Url;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@WebServlet(name = "FrontServlet", value = "/")
public class FrontServlet extends HttpServlet {
    private HashMap<String, Mapping> MappingUrls;

    public HashMap<String, Mapping> getMappingUrls() {
        return MappingUrls;
    }
    public void setMappingUrls(HashMap<String, Mapping> mappingUrls) {
        MappingUrls = mappingUrls;
    }


    @Override
    public void init() throws ServletException {
        try {
            String[] packages = this.readConfig();
            this.fillMappingUrls(packages);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void redirect(String indexOfFile, HttpServletRequest req, HttpServletResponse rep) throws ServletException, IOException {
        RequestDispatcher dispatcher = req.getRequestDispatcher(indexOfFile);
        dispatcher.forward(req, rep);
    }
    protected void processRequest(HttpServletRequest req, HttpServletResponse rep) throws Exception {
        PrintWriter out = rep.getWriter();
        String incomingURL = String.valueOf(req.getRequestURL());
        String target = this.getTarget(incomingURL);

        //System.out.println("Traitement URL");

        Mapping mapping = this.getMappingUrls().get(target);
        if(mapping != null){
            //System.out.println("Class: "+mapping.getClassName()+" Method: "+mapping.getMethod());
            Class<?> clazz = Class.forName(mapping.getClassName());
            Constructor<?> constructor = clazz.getConstructor();
            Object clone = constructor.newInstance();
            Method method = clazz.getMethod(mapping.getMethod());

            String desti = (String) method.invoke(clone);
            this.redirect(desti, req, rep);
        }else{
            //System.out.println("Url inconnu");
            throw new Exception("Url inconnu");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            this.processRequest(req, resp);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            this.processRequest(req, resp);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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

    public String[] readConfig() throws IOException, SAXException, ParserConfigurationException {
        String path = getServletContext().getRealPath("/WEB-INF/myconfig.xml");
        File inputFile = new File(path);
        //System.out.println("Absolute path: "+inputFile.getAbsolutePath());
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(inputFile);
        doc.getDocumentElement().normalize();

        NodeList nodeList = doc.getElementsByTagName("myclasspath");
        List<String> packages = new ArrayList<>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node nNode = nodeList.item(i);

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) nNode;
                packages.add(element.getElementsByTagName("package").item(0).getTextContent());
            }
        }

        String[] list = new String[packages.size()];
        int it = 0;
        for(String s : packages){
            list[it] = s;
            it++;
        }
        return list;
    }

    public List<File> packagesFiles(String[] packages) throws Exception {  // String[] to List<String>
        List<File> files = new ArrayList<>();

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        for(String pkg : packages){
            try{
                files.add(new File(Objects.requireNonNull(classLoader.getResource(pkg.replace('.', '/'))).getFile()));
            }catch (Exception e){
                throw new Exception("Votre package n'existe pas ("+pkg+")");
            }
        }
        return files;
    }

    private void fillMappingUrls(String[] packages) throws Exception {
        this.setMappingUrls(new HashMap<String, Mapping>());

        List<File> packageDir = this.packagesFiles(packages);
        for(int i=0 ; i < packageDir.size() ; i++){
            if(packageDir.get(i).exists()){
                File[] files = packageDir.get(i).listFiles();
                assert files != null;
                for(File f : files){
                    String file = f.getName();
                    if(f.isDirectory()){
                        String[] newpackage = new String[1];
                        newpackage[0] = packages[i]+"."+file;
                        this.fillMappingUrls(newpackage);
                    }else{
                        if(file.endsWith(".class")){
                            String className = file.substring(0, file.length()-6 /* exemple: test.class >> enlever .class */);
                            Class<?> clazz = Class.forName(packages[i] + '.' + className);
                            System.out.println(clazz.getName());
                            for(Method method : clazz.getDeclaredMethods()){
                                if(method.isAnnotationPresent(Url.class)){
                                    Url url = (Url) method.getAnnotation(Url.class);
                                    this.getMappingUrls().put(url.name(), new Mapping(clazz.getName(), method.getName()));
                                }
                            }
                        }
                    }
                }
            }
        }
        //System.out.println("Mapping length: "+this.getMappingUrls().size());
    }
}
