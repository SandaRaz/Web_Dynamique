package test;

import etu2079.framework.annotation.Url;

public class Emp {

    @Url(name = "emp-all")
    public String getAll(){
        System.out.println("Select * from employe");
        return "testPage.jsp";
    }
}
