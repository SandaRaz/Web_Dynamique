package test;

import etu2079.framework.ModelView;
import etu2079.framework.annotation.Auth;
import etu2079.framework.annotation.Url;
import etu2079.framework.annotation.Param;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Emp {
    public String id;
    public String nom;
    public double salaire;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public double getSalaire() {
        return salaire;
    }

    public void setSalaire(double salaire) {
        this.salaire = salaire;
    }

    public Emp(){

    }

    public Emp(String id, String nom, double salaire){
        this.id = id;
        this.nom = nom;
        this.salaire = salaire;
    }

    @Url("emp-all")
    public ModelView getAll(){
        System.out.println("Select * from employe");

        List<Emp> emps = new ArrayList<>();
        emps.add(new Emp("1","Sanda",1000));
        emps.add(new Emp("2","TsySanda",2000));
        emps.add(new Emp("3","MbolaTsySanda",3000));


        ModelView mv = new ModelView();
        mv.setView("allEmp.jsp");
        mv.addItem("lst", emps);

        return mv;
    }

    @Auth @Url("emp-save")
    public ModelView addEmp(@Param("ddn") Date ddn){

        System.out.println("Ajout d'employe:");
        System.out.println("    Nom: "+this.nom+" Date de Naissance: "+ddn+" salaire: "+this.salaire);

        return new ModelView("index.jsp");
    }
}
