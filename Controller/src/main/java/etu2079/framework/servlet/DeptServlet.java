package etu2079.framework.servlet;

import etu2079.framework.MethAnnotation;

public class DeptServlet {
    @MethAnnotation(name = "add")
    public void AddDepartement(){
        System.out.println("Ajouter un nouveau departement");
    }

    @MethAnnotation(name = "all")
    public void AllDepartement(){
        System.out.println("Get All Departement");
    }
}
