package ma.projet.test;

import java.util.List;

import ma.projet.classes.Employe;
import ma.projet.classes.Projet;
import ma.projet.classes.Tache;
import ma.projet.service.EmployeService;
import ma.projet.service.EmployeTacheService;
import ma.projet.service.ProjetService;
import ma.projet.service.TacheService;
import ma.projet.util.Entete;
import ma.projet.util.Format;
import ma.projet.util.HibernateUtil;
import ma.projet.util.TableConsole;

/**
 * Programme de test de l'exercice 2 : suivi des projets d'un bureau d'etudes.
 */
public final class TestProjets {

    private static final int ETAPES = 6;

    private TestProjets() {
    }

    public static void main(String[] args) {
        Entete.ouvrir("EXERCICE 2", "Suivi des projets et du temps passe par les employes",
                "Bureau d'etudes \"Souss Digital\" - Agadir");

        try {
            DonneesProjets.charger();

            EmployeService employes = new EmployeService();
            ProjetService projets = new ProjetService();
            TacheService taches = new TacheService();
            EmployeTacheService affectations = new EmployeTacheService();

            projetsGeres(employes);
            tachesPlanifiees(projets);
            tachesRealisees(projets);
            tachesRealiseesParEmploye(affectations, employes);
            controleCrud(projets, taches, employes);
            synthese(employes, projets, taches, affectations);
        } finally {
            HibernateUtil.fermer();
        }
    }

    private static void projetsGeres(EmployeService employes) {
        Entete.etape(1, ETAPES, "Projets geres par un employe (requete nommee Projet.parResponsable)");

        Employe responsable = employes.findById(1);
        List<Projet> liste = employes.projetsGeresParLEmploye(responsable);
        Entete.commentaire("Employe : " + responsable.getNomComplet() + " - " + responsable.getPoste());
        Entete.commentaire(liste.size() + " projet(s) sous sa responsabilite");
        System.out.println();

        TableConsole table = new TableConsole(
                new String[]{"Num", "Projet", "Date debut", "Responsable"},
                new boolean[]{true, false, false, false});
        for (Projet projet : liste) {
            table.ligne(projet.getId(), projet.getNom(), Format.dateLongue(projet.getDateDebut()),
                    projet.getResponsable().getNomComplet());
        }
        table.afficher();
    }

    private static void tachesPlanifiees(ProjetService projets) {
        Entete.etape(2, ETAPES, "Taches planifiees d'un projet");

        Projet projet = projets.findById(1);
        List<Tache> liste = projets.tachesPlanifiees(projet);
        Entete.commentaire("Projet : " + projet.getId() + "    Nom : " + projet.getNom()
                + "    Date debut : " + Format.dateLongue(projet.getDateDebut()));
        Entete.commentaire("Liste des taches planifiees :");
        System.out.println();

        TableConsole table = new TableConsole(
                new String[]{"Num", "Tache", "Debut prevu", "Fin prevue", "Etat"},
                new boolean[]{true, false, false, false, false});
        for (Tache tache : liste) {
            table.ligne(tache.getId(), tache.getNom(),
                    Format.dateCourte(tache.getDateDebutPrevue()),
                    Format.dateCourte(tache.getDateFinPrevue()),
                    tache.isRealisee() ? "terminee" : "en cours");
        }
        table.afficher();
    }

    private static void tachesRealisees(ProjetService projets) {
        Entete.etape(3, ETAPES, "Taches realisees avec les dates reelles");

        Projet projet = projets.findById(2);
        List<Tache> liste = projets.tachesRealisees(projet);
        Entete.commentaire("Projet : " + projet.getId() + "    Nom : " + projet.getNom()
                + "    Date debut : " + Format.dateLongue(projet.getDateDebut()));
        Entete.commentaire("Liste des taches :");
        System.out.println();

        TableConsole table = new TableConsole(
                new String[]{"Num", "Nom", "Date debut reelle", "Date fin reelle"},
                new boolean[]{true, false, false, false});
        for (Tache tache : liste) {
            table.ligne(tache.getId(), tache.getNom(),
                    Format.dateCourte(tache.getDateDebutReelle()),
                    Format.dateCourte(tache.getDateFinReelle()));
        }
        table.afficher();
    }

    private static void tachesRealiseesParEmploye(EmployeTacheService affectations, EmployeService employes) {
        Entete.etape(4, ETAPES, "Taches realisees par un employe");

        Employe employe = employes.findById(2);
        List<Tache> liste = affectations.tachesRealiseesParLEmploye(employe);
        Entete.commentaire("Employe : " + employe.getNomComplet() + " - " + employe.getPoste());
        System.out.println();

        TableConsole table = new TableConsole(
                new String[]{"Num", "Tache", "Projet", "Fin reelle"},
                new boolean[]{true, false, false, false});
        for (Tache tache : liste) {
            table.ligne(tache.getId(), tache.getNom(), tache.getProjet().getNom(),
                    Format.dateCourte(tache.getDateFinReelle()));
        }
        table.afficher();
    }

    private static void controleCrud(ProjetService projets, TacheService taches, EmployeService employes) {
        Entete.etape(5, ETAPES, "Controle des operations de l'interface IDao");
        Entete.commentaire("Projet \"Projet de controle\" cree, renomme puis supprime avec sa tache");
        System.out.println();

        Employe responsable = employes.findById(1);
        Projet temporaire = new Projet("Projet de controle", Format.date(1, 6, 2025), responsable);
        projets.create(temporaire);

        Tache tache = new Tache("Tache de controle", Format.date(1, 6, 2025),
                Format.date(30, 6, 2025), temporaire);
        taches.create(tache);

        temporaire.setNom("Projet de controle renomme");
        projets.update(temporaire);
        Projet relu = projets.findById(temporaire.getId());
        boolean modification = relu != null && "Projet de controle renomme".equals(relu.getNom());

        taches.delete(taches.findById(tache.getId()));
        boolean suppressionTache = taches.findById(tache.getId()) == null;

        projets.delete(relu);
        boolean suppressionProjet = projets.findById(temporaire.getId()) == null;

        Entete.verification("creation, modification et relecture du projet ...", modification);
        Entete.verification("suppression de la tache ...", suppressionTache);
        Entete.verification("suppression du projet ...", suppressionProjet);
        Entete.commentaire("Base restauree : " + projets.findAll().size() + " projets, "
                + taches.findAll().size() + " taches");
    }

    private static void synthese(EmployeService employes, ProjetService projets,
            TacheService taches, EmployeTacheService affectations) {
        Entete.etape(6, ETAPES, "Synthese du bureau d'etudes");

        List<Tache> toutes = taches.findAll();
        int terminees = 0;
        for (Tache tache : toutes) {
            if (tache.isRealisee()) {
                terminees++;
            }
        }
        int taux = toutes.isEmpty() ? 0 : (100 * terminees) / toutes.size();

        TableConsole table = new TableConsole(
                new String[]{"Indicateur", "Valeur"},
                new boolean[]{false, true});
        table.ligne("Employes", employes.findAll().size());
        table.ligne("Projets suivis", projets.findAll().size());
        table.ligne("Taches planifiees", toutes.size());
        table.ligne("Taches terminees", terminees);
        table.ligne("Affectations enregistrees", affectations.findAll().size());
        table.ligne("Taux d'avancement", taux + " %");
        table.afficher();

        Entete.fermer("Exercice 2 termine - " + ETAPES + " etapes validees");
    }
}
