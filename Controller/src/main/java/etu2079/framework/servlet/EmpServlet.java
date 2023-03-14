package etu2079.framework.servlet;

import etu2079.framework.MethAnnotation;


public class EmpServlet {

    @MethAnnotation(name = "add")
    public void AddEmploye(){
        System.out.println("Ajouter un employe");
    }

    @MethAnnotation(name = "all")
    public void AllEmploye(){
        System.out.println("Get All employe");
    }
}
