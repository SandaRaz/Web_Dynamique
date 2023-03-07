package controller;

import connect.ConnectionDB;
import table.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Vector;

@WebServlet(name = "MyController", value = "*.MyController")
public class MyController extends MereController {
    @CtrlAnnotation(name = "f12",)
    public void qwertg() throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        Connection c = ConnectionDB.makaConPsql();
        String nom = request.getParameter("nom");
        String prenom = request.getParameter("prenom");
        HttpSession session = request.getSession();
        Vector<String> param = Dentiste.getDentisteParameter(c, request.getParameter("nom"), request.getParameter("prenom"), request.getParameter("date"), request.getParameter("email"), request.getParameter("adresse"), request.getParameter("sexe"), request.getParameter("telephone"));
        out.println(param);
        session.setAttribute("insc1", param);
        redirect("inscription-docteur2.jsp");
        try {
            c.close();
        } catch (Exception e) {
        }
    }

    @CtrlAnnotation(name = "f2")
    public void f2() throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        Connection c = ConnectionDB.makaConPsql();
        HttpSession session = request.getSession();
        String[] allSpec = request.getParameterValues("specialite");
        String niveau = request.getParameter("niveauEtude");
        Vector<String> param = SpecialiterDentiste.getParam(allSpec, niveau);
        session.setAttribute("insc2", param);
        redirect("inscription-docteur3.jsp");
        try {
            c.close();
        } catch (Exception e) {
        }


    }

    @CtrlAnnotation(name = "f3")
    public void f3() throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        Connection c = ConnectionDB.makaConPsql();
        HttpSession session = request.getSession();
        String[] allSpec = request.getParameterValues("specialite");
        String niveau = request.getParameter("niveauEtude");
        Vector<String> param = SpecialiterDentiste.getParam(allSpec, niveau);
        session.setAttribute("insc2", param);
        redirect("inscription-docteur3.jsp");
        try {
            c.close();
        } catch (Exception e) {
        }


    }

    @CtrlAnnotation(name = "add-montant")
    public void addMontant() throws Exception {

        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();

        Connection c = ConnectionDB.makaConPsql();
        c.setAutoCommit(false);

        int id = (int) session.getAttribute("idPatient");
        double montant = Double.parseDouble(String.valueOf(request.getParameter("montant")));
        int idRdv = Integer.parseInt(request.getParameter("idRdv"));

        RendezVous rendezVous = new RendezVous(idRdv, c);
        Facture facture = new Facture(id, idRdv, montant, "");


        try {
            facture.payerFacture(c);
            int idCaisse = Dentiste.getCaisseId(c);
            new Caisse(idCaisse).updateCaisse(c, montant);
            Mouvement.InsertInto(c, idCaisse, montant, 0);

            c.commit();
        } catch (Exception e) {
            c.rollback();
            e.printStackTrace();
        } finally {
            c.close();
        }
        response.sendRedirect("detailsRdv.jsp?idRdv=" + idRdv);
    }

    @CtrlAnnotation(name = "ReinitialiseTraitement")
    public void ReinitialiseTraitement() throws ServletException, IOException {
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession();
        int idRdv = Integer.parseInt(request.getParameter("idRdv"));
        session.removeAttribute("idTraitement");
        session.removeAttribute("idMateriel");

        redirect("TraitementDocteur.jsp?idRdv=" + idRdv);
    }

    @CtrlAnnotation(name = "AjoutAdmin")
    public void AjoutAdmin() throws ServletException, IOException {
        PrintWriter out = response.getWriter();

        Connection cnx = ConnectionDB.makaConPsql();
        try {
            cnx.setAutoCommit(false);

            switch (Integer.parseInt(request.getParameter("option"))) {
                case 1: // specialiter
                    String newspecialiter = request.getParameter("newspecialiter");
                    boolean exist = false;
                    for (Specialiter spec : Specialiter.getAll(cnx)) {
                        if (spec.getNom().equalsIgnoreCase(newspecialiter)) {
                            exist = true;
                            break;
                        }
                    }
                    if (!exist && newspecialiter != null && newspecialiter != "") {
                        Specialiter.InsertInto(cnx, newspecialiter);
                    }

                    break;
                case 2: // materiel
                    String materiel = request.getParameter("newmateriel");
                    double prixmateriel = 0;
                    prixmateriel = Double.parseDouble(request.getParameter("prixmateriel"));

                    boolean exist2 = false;
                    for (Materiel m : Materiel.getAll(cnx)) {
                        if (m.getMateriel().equalsIgnoreCase(materiel)) {
                            exist2 = true;
                            break;
                        }
                    }
                    if (!exist2 && materiel != null && materiel != "") {
                        Materiel.InsertInto(cnx, materiel, prixmateriel);
                        Mouvement.InsertInto(cnx, Dentiste.getCaisseId(cnx), 0, prixmateriel);
                        Dentiste.getCaisse(cnx).updateCaisse(cnx, -prixmateriel);
                    }

                    break;
                case 3: // type de traitement
                    String typeTraitement = request.getParameter("newTypeTraitement");
                    boolean exist3 = false;
                    for (TypeTraitement tt : TypeTraitement.getAll(cnx)) {
                        if (tt.getType().equalsIgnoreCase(typeTraitement)) {
                            exist3 = true;
                            break;
                        }
                    }
                    if (!exist3 && typeTraitement != null && typeTraitement != "") {
                        TypeTraitement.InsertInto(cnx, typeTraitement);
                    }

                    break;
                case 4: // traitement
                    int idTypeTrait = Integer.parseInt(request.getParameter("idTypeTraitement"));
                    String newtraitement = request.getParameter("newTraitement");
                    double prixTraitement = Double.parseDouble(request.getParameter("prixTraitement"));
                    int etatTraitement = Integer.parseInt(request.getParameter("etatTraitement"));
                    double margeTraitement = Double.parseDouble(request.getParameter("margeTraitement"));

                    if (newtraitement != "" && newtraitement != null) {
                        Traitement.InsertInto(cnx, idTypeTrait, newtraitement, prixTraitement, etatTraitement, margeTraitement);
                    }

                    break;

            }

            cnx.commit();
        } catch (Exception e) {
            try {
                cnx.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                cnx.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        redirect("Admin.jsp");
    }
}
