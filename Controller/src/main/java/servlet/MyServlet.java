package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

public class MyServlet extends HttpServlet {
    HttpServletRequest req;
    HttpServletResponse resp;
    protected void processRequest() throws IOException {
        PrintWriter out = this.resp.getWriter();

        String url = String.valueOf(this.req.getRequestURL());
        System.out.println(this.getTarget(url));
        String test = req.getParameter("test");
        System.out.print("Parameter: "+test);

        resp.sendRedirect("../index.jsp");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
        this.req = req;
        this.resp = resp;
        this.processRequest();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
        this.req = req;
        this.resp = resp;
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
}
