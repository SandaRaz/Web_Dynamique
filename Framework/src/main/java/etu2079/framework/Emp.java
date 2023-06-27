package etu2079.framework;

import etu2079.framework.annotation.Auth;
import etu2079.framework.annotation.Scope;

@Scope("Singleton")
public class Emp {
    String id;
    String nom;

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

    public void Login(){

    }

    @Auth
    public void makeComment(){
        System.out.println("*********************** Manao Commentaire *************************");
    }
}
