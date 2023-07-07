package etu2079.framework.servlet;

import com.google.gson.Gson;
import etu2079.framework.Mapping;
import etu2079.framework.ModelView;
import etu2079.framework.annotation.*;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import jakarta.servlet.http.HttpSession;
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
import java.lang.reflect.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@WebServlet(name = "FrontServlet", value = "/")
public class FrontServlet extends HttpServlet {
    private HashMap<String, Mapping> MappingUrls;
    private HashMap<Class<?>, Object> MappingSingletons;
    private String loginSessionName;
    private String profilSessionName;

    public HashMap<String, Mapping> getMappingUrls() {
        return MappingUrls;
    }
    public void setMappingUrls(HashMap<String, Mapping> mappingUrls) {
        MappingUrls = mappingUrls;
    }

    public HashMap<Class<?>, Object> getMappingSingletons() {
        return MappingSingletons;
    }
    public void setMappingSingletons(HashMap<Class<?>, Object> mappingSingletons) {
        MappingSingletons = mappingSingletons;
    }

    public String getLoginSessionName() {
        return loginSessionName;
    }
    public void setLoginSessionName(String loginSessionName) {
        this.loginSessionName = loginSessionName;
    }

    public String getProfilSessionName() {
        return profilSessionName;
    }
    public void setProfilSessionName(String profilSessionName) {
        this.profilSessionName = profilSessionName;
    }

    @Override
    public void init(){
        try {
            String[] packages = this.readConfig();
            this.fillMappingUrls(packages);
            this.fillMappingSingletons(packages);

            ServletContext context = getServletContext();
            this.setLoginSessionName(context.getInitParameter("LoginSessionName"));
            this.setProfilSessionName(context.getInitParameter("LoginProfilName"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void redirect(String indexOfFile, HttpServletRequest req, HttpServletResponse rep) throws ServletException, IOException {
        RequestDispatcher dispatcher = req.getRequestDispatcher(indexOfFile);
        dispatcher.forward(req, rep);
    }

// -------------------- FONCTION POUR ModelView ------------------------------
    public void fillingAttribute(ModelView modelView,HttpServletRequest req, HttpServletResponse rep){
        Set<String> mvKey = modelView.getData().keySet();
        for(String key : mvKey){
            req.setAttribute(key, modelView.getData().get(key));
        }
    }
    public void fillingSession(ModelView modelView, HttpServletRequest req, HttpServletResponse rep){
        HttpSession session = req.getSession();
        Set<String> mvSessionName = modelView.getSession().keySet();
        for(String key : mvSessionName){
            session.setAttribute(key, modelView.getSession().get(key));
            System.out.println("Placer le session: "+key);
        }
    }
// ----------------------------------------------------------------------------

    public Object conversion(String parametre){
        Object value = parametre.trim();
        try{
            value = Integer.parseInt(parametre.trim());
        }catch (NumberFormatException e1){
            try{
                value = Double.parseDouble(parametre.trim());
            }catch (NumberFormatException e2) {
                try{
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    value = sdf.parse(parametre.trim());
                } catch (ParseException ignored) {
                }
            }
        }
        return value;
    }

    public Object myCast(String parametre, Class<?> type){
        Object value = parametre.trim();
        if(type.equals(int.class) || type.equals(Integer.class)){
            value = Integer.parseInt(parametre.trim());
        }
        if(type.equals(double.class) || type.equals(Double.class)){
            value = Double.parseDouble(parametre.trim());
        }
        if(type.equals(Date.class)){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                value = sdf.parse(parametre.trim());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return value;
    }

// ----------------------------- FONCTION POUR Singleton ---------------------------------
    public void ResetObjectFieldsValue(Object obj) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Field[] fields = obj.getClass().getFields();
        Method tempMethod = null;
        for(Field field : fields){
            tempMethod = obj.getClass().getMethod("set" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1));
            Object invokeArg = "null";
            tempMethod.invoke(obj, null);
        }
    }
// ----------------------------------------------------------------------------------------

    protected void processRequest(HttpServletRequest req, HttpServletResponse rep) throws Exception {
        PrintWriter out = rep.getWriter();
        String incomingURL = String.valueOf(req.getRequestURL());
        String target = this.getTarget(incomingURL);

        Mapping mapping = this.getMappingUrls().get(target);
        if(mapping != null){
            Class<?> clazz = Class.forName(mapping.getClassName());
            Constructor<?> constructor = clazz.getConstructor();
            
            /* Creation d'une nouvelle instance d'objet */
            Object clone = null;

            // si c'est un singleton
            if(clazz.isAnnotationPresent(Scope.class) && clazz.getAnnotation(Scope.class).value().equalsIgnoreCase("singleton")){
                // si ce singleton n'a pas encore été instancié
                if(this.getMappingSingletons().get(clazz) == null){
                    clone = constructor.newInstance();
                    this.getMappingSingletons().put(clazz, clone);
                }else{
                    clone = this.getMappingSingletons().get(clazz);
                    // mettre tous les attributs de l'instance a 0
                    this.ResetObjectFieldsValue(clone);
                }
                System.out.println(clazz.getSimpleName()+" est un Singleton");
            }else{
                clone = constructor.newInstance();
            }
            /* ---------------------------------------- */
            
            Method method = mapping.getMethod();

            Parameter[] parameters = method.getParameters();
            String[] argumentsNamesList = this.getArgumentNames(method);    // liste de nom des parametres
            Object[] args = new Object[argumentsNamesList.length];

            Enumeration<String> parameterNames = req.getParameterNames();   // Maka ny nom parametres en entrer rehetra dispo ao @ getParameter() 
            while(parameterNames.hasMoreElements()){    // si != 0 ny taille (Misy parametres hoavy)
                String paramName = parameterNames.nextElement();
                Field paramField = null;
                try{
                    paramField = clone.getClass().getField(paramName.toLowerCase());    // si le name du formulaire est un field de la classe Cible
                }catch (NoSuchFieldException ignored){
                    System.out.println("L'attribut "+paramName+" n'est pas présent dans la class '"+clazz.getSimpleName()+"'");
                }

        // -------------- en utilisant les setters -----------------
                if(paramField != null){    // si le field du formulaire ou du link existe
                    String[] paramValues = req.getParameterValues(paramName);
                    Object paramValue = null;
                    if(paramValues.length == 1){
                        paramValue = myCast(paramValues[0], paramField.getType());
                    }else{
                        Object[] objectValue = new Object[paramValues.length];
                        for(int i=0;i<paramValues.length;i++){
                            objectValue[i] = this.conversion(paramValues[i]);
                        }
                        paramValue = objectValue;
                    }
                    Method setters = clone.getClass().getMethod("set"+paramName.substring(0, 1).toUpperCase()+paramName.substring(1), paramField.getType()); // settena ao @ class cible tous les parametres
                    //System.out.println("Setters: "+setters.getName()+" avec value: "+paramValue);
                    setters.invoke(clone, paramValue);
                }
        // ------- en utilisant les arguments d'une fonction --------
                String[] paramValues = req.getParameterValues(paramName);
                int pIndice = this.getArgumentIndice(argumentsNamesList ,paramName);
                if(pIndice != -1){
                    Object paramValue = myCast(paramValues[0], parameters[pIndice].getType());
                    args[pIndice] = paramValue;
                }
            }

            this.checkMethod(method, req.getSession());
            Object result = method.invoke(clone, args);
            Gson gson = new Gson();
            ServletContext context = getServletContext();
            // Traitement resultats --------------------------------
            if(result instanceof ModelView){
                ModelView modelView = (ModelView) result;
                this.fillingAttribute(modelView, req, rep);
                this.fillingSession(modelView, req, rep);
                this.redirect(modelView.getView(), req, rep);

                if(modelView.isJson()){
                    String json = gson.toJson(modelView);
                    rep.setContentType("application/json");
                    rep.setCharacterEncoding("UTF-8");
                    rep.getWriter().write(json);
                    System.out.println("-------------- JSON >>> "+json);
                    out.print("JSON >>> "+json);
                }else{
                    this.encodingJson(method, modelView, gson, rep);
                }

                if(modelView.isInvalidateSession()){
                    Enumeration<String> attributeNames = context.getAttributeNames();

                    System.out.println(" --------------- KILL ALL SESSION -------------");
                    // Parcourez les attributs de session
                    while (attributeNames.hasMoreElements()) {
                        String attributeName = attributeNames.nextElement();
                        if (attributeName.startsWith("javax.servlet.http.HttpSession") || attributeName.startsWith("jakarta.servlet.http.HttpSession")) {
                            HttpSession tempSession = (HttpSession) context.getAttribute(attributeName);
                            tempSession.invalidate();
                            System.out.println("Session \""+attributeName+"\" removed >>> "+tempSession);
                        }
                    }
                }
                System.out.println(" --------------- KILL ALL SPECIFIED SESSION -------------");
                List<String> removesessions = modelView.getRemoveSession();
                for(String sessionName : removesessions){
                    HttpSession tempSession = (HttpSession) context.getAttribute(sessionName);
                    tempSession.invalidate();
                    System.out.println("Session \""+sessionName+"\" removed >>> "+tempSession);
                }
            }else{
                this.encodingJson(method, result, gson, rep);
            }
            // -----------------------------------------------------
        }else{
            //System.out.println("Url inconnu");
            throw new Exception("Url inconnu");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp){
        try {
            this.processRequest(req, resp);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp){
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

    // prendre la liste de tous les arguments annote d'une fonction
    public String[] getArgumentNames(Method method){
        Parameter[] params = method.getParameters();
        String[] argsNames = new String[params.length];
        int it = 0;
        for(Parameter p : params){
            if(p.isAnnotationPresent(Param.class)){
                Param pname = (Param) p.getAnnotation(Param.class);
                argsNames[it] = pname.value();
            }else{
                argsNames[it] = null;
            }
            it++;
        }
        return argsNames;
    }

    // recuperer la position d'un arguments dans sa fonction
    public int getArgumentIndice(String[] argsNames, String name){
        int ind = -1;
        for(int i = 0; i<argsNames.length ; i++){
            if(argsNames[i] != null && argsNames[i].equals(name)){
                ind = i;
                break;
            }
        }
        return ind;
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

// ---------------------- FONCTION Pour Remplir les attributs HashMap MAPPING -----------------------
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
                                    this.getMappingUrls().put(url.value(), new Mapping(clazz.getName(), method));
                                }
                            }
                        }
                    }
                }
            }
        }
        //System.out.println("Mapping length: "+this.getMappingUrls().size());
    }

    public void fillMappingSingletons(String[] packages) throws Exception {
        this.setMappingSingletons(new HashMap<>());

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
                            if(clazz.isAnnotationPresent(Scope.class)){
                                Scope scope = clazz.getAnnotation(Scope.class);
                                if(scope.value().equalsIgnoreCase("singleton")){
                                    this.getMappingSingletons().put(clazz, null);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
// --------------------------------------------------------------------------------------------------

    // Verifier si une methode est annoté Auth()
    public void checkMethod(Method method, HttpSession session) throws Exception {
        Class<?> clazz = method.getClass();
        if(clazz.isAnnotationPresent(Auth.class)){
            Auth auth = (Auth) clazz.getAnnotation(Auth.class);

            // si c'est Auth("Profil")
            if(!auth.value().isEmpty() && auth.value().equals(this.getProfilSessionName())){
                if(session.getAttribute(this.getProfilSessionName()) == null){
                    throw new Exception("Profil d'Administrateur requise");
                }
            }else{
                if(session.getAttribute(this.getLoginSessionName()) == null){
                    throw new Exception("Authentification necessaire requise");
                }
            }
        }
    }

    public void encodingJson(Method method, Object result, Gson gson, HttpServletResponse rep) throws IOException {
        if(method.getClass().isAnnotationPresent(RestAPI.class)){
            String json = gson.toJson(result);
            rep.setContentType("application/json");
            rep.setCharacterEncoding("UTF-8");
            rep.getWriter().write(json);
            PrintWriter out = rep.getWriter();
            System.out.println("-------------- JSON PAR ANNOTATION >>> "+json);
            out.print("JSON PAR ANNOTATION >>> "+json);
        }
    }
}

