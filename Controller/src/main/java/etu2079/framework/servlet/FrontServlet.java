package etu2079.framework.servlet;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

public class FrontServlet extends HttpServlet {
    RequestDispatcher dispat;
    HttpServletRequest request;
    HttpServletResponse response;
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
}
