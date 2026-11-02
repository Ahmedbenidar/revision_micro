package ma.projet.test;

import ma.projet.classes.Employe;
import ma.projet.classes.EmployeTache;
import ma.projet.classes.Projet;
import ma.projet.classes.Tache;
import ma.projet.service.EmployeService;
import ma.projet.service.EmployeTacheService;
import ma.projet.service.ProjetService;
import ma.projet.service.TacheService;
import ma.projet.util.Format;
import ma.projet.util.MaintenanceBase;

/**
 * Jeu de donnees personnel du bureau d'etudes "Souss Digital" (Agadir).
 * Les tables sont videes puis remplies a chaque execution pour que les
 * identifiants du compte rendu restent stables.
 */
public final class DonneesProjets {

    private DonneesProjets() {
    }

    public static void charger() {
        MaintenanceBase.viderEtReinitialiser("employe_tache", "tache", "projet", "employe");

        EmployeService employes = new EmployeService();
        ProjetService projets = new ProjetService();
        TacheService taches = new TacheService();
        EmployeTacheService affectations = new EmployeTacheService();

        Employe ahmed = new Employe("BEN IDAR", "Ahmed", "Chef de projet");
        Employe salma = new Employe("EL FASSI", "Salma", "Developpeuse Java");
        Employe yassine = new Employe("OUBAHA", "Yassine", "Analyste fonctionnel");
        Employe khadija = new Employe("NDAHBI", "Khadija", "Testeuse QA");
        Employe hamza = new Employe("BOUZID", "Hamza", "Developpeur front-end");
        employes.create(ahmed);
        employes.create(salma);
        employes.create(yassine);
        employes.create(khadija);
        employes.create(hamza);

        Projet elearning = new Projet("Refonte du portail e-learning",
                Format.date(14, 1, 2025), ahmed);
        Projet stock = new Projet("Application de gestion de stock",
                Format.date(3, 3, 2025), ahmed);
        Projet erp = new Projet("Migration de l'ERP",
                Format.date(15, 9, 2025), salma);
        projets.create(elearning);
        projets.create(stock);
        projets.create(erp);

        Tache analyseBesoins = new Tache("Analyse des besoins",
                Format.date(14, 1, 2025), Format.date(31, 1, 2025), elearning);
        Tache conceptionUx = new Tache("Conception UX",
                Format.date(1, 2, 2025), Format.date(28, 2, 2025), elearning);
        Tache developpementBack = new Tache("Developpement back-end",
                Format.date(1, 3, 2025), Format.date(30, 4, 2025), elearning);
        Tache recette = new Tache("Recette fonctionnelle",
                Format.date(1, 5, 2025), Format.date(31, 5, 2025), elearning);

        Tache cadrage = new Tache("Cadrage du besoin",
                Format.date(3, 3, 2025), Format.date(21, 3, 2025), stock);
        Tache modelisation = new Tache("Modelisation des donnees",
                Format.date(24, 3, 2025), Format.date(18, 4, 2025), stock);
        Tache ecrans = new Tache("Developpement des ecrans",
                Format.date(21, 4, 2025), Format.date(13, 6, 2025), stock);
        Tache miseEnProduction = new Tache("Mise en production",
                Format.date(16, 6, 2025), Format.date(30, 6, 2025), stock);

        Tache audit = new Tache("Audit de l'existant",
                Format.date(15, 9, 2025), Format.date(10, 10, 2025), erp);
        Tache connecteurs = new Tache("Developpement des connecteurs",
                Format.date(13, 10, 2025), Format.date(19, 12, 2025), erp);

        Tache[] toutesLesTaches = {analyseBesoins, conceptionUx, developpementBack, recette,
                cadrage, modelisation, ecrans, miseEnProduction, audit, connecteurs};
        for (Tache tache : toutesLesTaches) {
            taches.create(tache);
        }

        analyseBesoins.setDateDebutReelle(Format.date(15, 1, 2025));
        analyseBesoins.setDateFinReelle(Format.date(2, 2, 2025));
        conceptionUx.setDateDebutReelle(Format.date(3, 2, 2025));
        conceptionUx.setDateFinReelle(Format.date(25, 2, 2025));
        developpementBack.setDateDebutReelle(Format.date(1, 3, 2025));
        developpementBack.setDateFinReelle(Format.date(5, 5, 2025));
        cadrage.setDateDebutReelle(Format.date(4, 3, 2025));
        cadrage.setDateFinReelle(Format.date(20, 3, 2025));
        modelisation.setDateDebutReelle(Format.date(25, 3, 2025));
        modelisation.setDateFinReelle(Format.date(22, 4, 2025));
        ecrans.setDateDebutReelle(Format.date(23, 4, 2025));
        ecrans.setDateFinReelle(Format.date(18, 6, 2025));
        audit.setDateDebutReelle(Format.date(16, 9, 2025));
        audit.setDateFinReelle(Format.date(8, 10, 2025));
        for (Tache tache : toutesLesTaches) {
            taches.update(tache);
        }

        affectations.create(new EmployeTache(yassine, analyseBesoins,
                Format.date(15, 1, 2025), Format.date(2, 2, 2025)));
        affectations.create(new EmployeTache(hamza, conceptionUx,
                Format.date(3, 2, 2025), Format.date(25, 2, 2025)));
        affectations.create(new EmployeTache(salma, developpementBack,
                Format.date(1, 3, 2025), Format.date(5, 5, 2025)));
        affectations.create(new EmployeTache(khadija, recette, null, null));

        affectations.create(new EmployeTache(yassine, cadrage,
                Format.date(4, 3, 2025), Format.date(20, 3, 2025)));
        affectations.create(new EmployeTache(ahmed, modelisation,
                Format.date(25, 3, 2025), Format.date(22, 4, 2025)));
        affectations.create(new EmployeTache(salma, ecrans,
                Format.date(23, 4, 2025), Format.date(18, 6, 2025)));
        affectations.create(new EmployeTache(hamza, miseEnProduction, null, null));

        affectations.create(new EmployeTache(yassine, audit,
                Format.date(16, 9, 2025), Format.date(8, 10, 2025)));
        affectations.create(new EmployeTache(salma, connecteurs, null, null));
    }
}
