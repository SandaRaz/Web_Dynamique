package test;

import etu2079.framework.ModelView;
import etu2079.framework.ViewModel;
import etu2079.framework.annotation.Url;

import java.util.ArrayList;
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

    @Url(name = "emp-all")
    public ModelView getAll(){
        System.out.println("Select * from employe");

        List<Emp> emps = new ArrayList<>();
        emps.add(new Emp("1","AAA",1000));
        emps.add(new Emp("2","BBB",2000));
        emps.add(new Emp("3","CCC",3000));


        ModelView mv = new ModelView();
        mv.setView("allEmp.jsp");
        mv.addItem("lst", emps);

        return mv;
    }

    @Url(name = "emp-add")
    public ModelView addEmp(ViewModel viewModel){
        String nom = (String) viewModel.getData().get("nom");
        int age = (int) viewModel.getData().get("age");

        System.out.println("Ajout d'employe:");
        System.out.println("    Nom: "+nom+" age: "+age);

        return new ModelView("index.jsp");
    }
}
