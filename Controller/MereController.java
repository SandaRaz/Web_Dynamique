package controller;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

@WebServlet(name = "MereController", value = "*.MereController")
public class MereController extends HttpServlet {

    public RequestDispatcher dispat;
    public HttpServletRequest request;
    public HttpServletResponse response;

    protected void processResquest() throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        this.test(this, request.getRequestURI());

    }

    public void redirect(String indexFile) throws ServletException, IOException {
        dispat = request.getRequestDispatcher(indexFile);
        dispat.forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.request = request;
        this.response = response;
        processResquest();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.request = request;
        this.response = response;
        processResquest();
    }

    public String urlUtile(String URL) {
        return URL.split("/")[2];
    }

    public String getController(String URL) {
        return urlUtile(URL).split("\\.")[1];
    }

    public String getMethodName(String URL) {
        return urlUtile(URL).split("." + getController(URL))[0];
    }

    public void test(Object cls, String URL) {
        Method[] methods = cls.getClass().getDeclaredMethods();
        for (Method M : methods) {
            Annotation annotation = M.getAnnotation(CtrlAnnotation.class);
            CtrlAnnotation ctrl = (CtrlAnnotation) annotation;
            if (ctrl != null && ctrl.name().equals(getMethodName(URL))) {
                try {
                    M.invoke(cls);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                break;
            }
        }
    }
}
