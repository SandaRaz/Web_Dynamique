package test;

import etu2079.framework.ModelView;
import etu2079.framework.annotation.Url;

public class Emp {

    @Url(name = "emp-all")
    public ModelView getAll(){
        System.out.println("Select * from employe");
        return new ModelView("allEmp.jsp");
    }
}
