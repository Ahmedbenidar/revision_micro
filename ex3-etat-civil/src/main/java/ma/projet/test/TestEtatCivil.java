package ma.projet.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ma.projet.beans.Femme;
import ma.projet.beans.Homme;
import ma.projet.beans.Mariage;
import ma.projet.service.FemmeService;
import ma.projet.service.HommeService;
import ma.projet.service.MariageService;
import ma.projet.util.Entete;
import ma.projet.util.Format;
import ma.projet.util.HibernateUtil;
import ma.projet.util.TableConsole;

/**
 * Programme de test de l'exercice 3 : gestion de l'etat civil d'une province.
 */
public final class TestEtatCivil {

    private static final int ETAPES = 9;

    private TestEtatCivil() {
    }

    public static void main(String[] args) {
        Entete.ouvrir("EXERCICE 3", "Gestion des citoyens et des relations matrimoniales",
                "Province de Taroudant - etat civil de la commune de Souss");

        try {
            DonneesEtatCivil.charger();

            HommeService hommes = new HommeService();
            FemmeService femmes = new FemmeService();
            MariageService mariages = new MariageService();

            listeDesCitoyennes(femmes);
            femmeLaPlusAgee(femmes);
            epousesEntreDeuxDates(hommes);
            nombreEnfantsEntreDeuxDates(femmes);
            femmesMarieesDeuxFoisOuPlus(femmes);
            hommesMariesAQuatreFemmes(femmes);
            detailDesMariages(hommes, mariages);
            controleCrud(femmes, mariages);
            synthese(hommes, femmes, mariages);
        } finally {
            HibernateUtil.fermer();
        }
    }

    private static void listeDesCitoyennes(FemmeService femmes) {
        Entete.etape(1, ETAPES, "Liste des femmes enregistrees");

        List<Femme> liste = femmes.findAll();
        Entete.commentaire(liste.size() + " femmes recensees dans la province");
        System.out.println();

        TableConsole table = new TableConsole(
                new String[]{"Num", "Nom", "Prenom", "Naissance", "Age"},
                new boolean[]{true, false, false, false, true});
        for (Femme femme : liste) {
            table.ligne(femme.getId(), femme.getNom(), femme.getPrenom(),
                    Format.dateCourte(femme.getDateNaissance()),
                    Format.age(femme.getDateNaissance()) + " ans");
        }
        table.afficher();
    }

    private static void femmeLaPlusAgee(FemmeService femmes) {
        Entete.etape(2, ETAPES, "Femme la plus agee (requete nommee Femme.laPlusAgee)");

        Femme femme = femmes.laPlusAgee();
        System.out.println();
        TableConsole table = new TableConsole(
                new String[]{"Num", "Nom", "Prenom", "Naissance", "Age"},
                new boolean[]{true, false, false, false, true});
        table.ligne(femme.getId(), femme.getNom(), femme.getPrenom(),
                Format.dateLongue(femme.getDateNaissance()),
                Format.age(femme.getDateNaissance()) + " ans");
        table.afficher();
    }

    private static void epousesEntreDeuxDates(HommeService hommes) {
        Entete.etape(3, ETAPES, "Epouses d'un homme entre deux dates");

        Homme homme = hommes.findById(1);
        Date debut = Format.date(1, 1, 1990);
        Date fin = Format.date(31, 12, 2000);
        List<Mariage> liste = hommes.epousesEntreDeuxDates(homme, debut, fin);
        Entete.commentaire(homme.getNomComplet() + " - mariages commences entre le "
                + Format.dateCourte(debut) + " et le " + Format.dateCourte(fin));
        System.out.println();

        TableConsole table = new TableConsole(
                new String[]{"Num", "Epouse", "Date du mariage", "Nbr enfants", "Etat"},
                new boolean[]{true, false, false, true, false});
        for (Mariage mariage : liste) {
            table.ligne(mariage.getFemme().getId(), mariage.getFemme().getNomComplet(),
                    Format.dateCourte(mariage.getDateDebut()), mariage.getNombreEnfants(),
                    mariage.isEnCours() ? "en cours" : "echoue");
        }
        table.afficher();
    }

    private static void nombreEnfantsEntreDeuxDates(FemmeService femmes) {
        Entete.etape(4, ETAPES, "Nombre d'enfants d'une femme entre deux dates (requete native nommee)");

        Femme femme = femmes.findById(4);
        Date debut = Format.date(1, 1, 1994);
        Date fin = Format.date(31, 12, 2001);
        int total = femmes.nombreEnfantsEntreDeuxDates(femme, debut, fin);
        Entete.commentaire("Femme : " + femme.getNomComplet());
        Entete.commentaire("Periode : du " + Format.dateCourte(debut) + " au " + Format.dateCourte(fin));
        System.out.println();
        Entete.commentaire("Total des enfants : " + total);
    }

    private static void femmesMarieesDeuxFoisOuPlus(FemmeService femmes) {
        Entete.etape(5, ETAPES, "Femmes mariees au moins deux fois (requete nommee)");

        List<Femme> liste = femmes.femmesMarieesAuMoinsDeuxFois();
        Entete.commentaire(liste.size() + " femmes concernees");
        System.out.println();

        TableConsole table = new TableConsole(
                new String[]{"Num", "Nom", "Prenom", "Naissance"},
                new boolean[]{true, false, false, false});
        for (Femme femme : liste) {
            table.ligne(femme.getId(), femme.getNom(), femme.getPrenom(),
                    Format.dateCourte(femme.getDateNaissance()));
        }
        table.afficher();
    }

    private static void hommesMariesAQuatreFemmes(FemmeService femmes) {
        Entete.etape(6, ETAPES, "Hommes maries a quatre femmes entre deux dates (API Criteria)");

        Date debut = Format.date(1, 1, 1989);
        Date fin = Format.date(31, 12, 2001);
        long total = femmes.nombreHommesMariesAQuatreFemmesEntreDeuxDates(debut, fin);
        System.out.println();

        TableConsole table = new TableConsole(
                new String[]{"Indicateur", "Valeur"},
                new boolean[]{false, true});
        table.ligne("Periode analysee", Format.dateCourte(debut) + " - " + Format.dateCourte(fin));
        table.ligne("Hommes ayant au moins 4 mariages", total);
        table.afficher();
    }

    private static void detailDesMariages(HommeService hommes, MariageService mariages) {
        Entete.etape(7, ETAPES, "Mariages d'un homme avec tous les details");

        Homme homme = hommes.findById(1);
        List<Mariage> liste = mariages.mariagesDunHomme(homme);
        List<String[]> enCours = new ArrayList<String[]>();
        List<String[]> echoues = new ArrayList<String[]>();
        for (Mariage mariage : liste) {
            if (mariage.isEnCours()) {
                enCours.add(new String[]{String.valueOf(mariage.getFemme().getId()),
                        mariage.getFemme().getNomComplet(),
                        Format.dateCourte(mariage.getDateDebut()),
                        String.valueOf(mariage.getNombreEnfants())});
            } else {
                echoues.add(new String[]{String.valueOf(mariage.getFemme().getId()),
                        mariage.getFemme().getNomComplet(),
                        Format.dateCourte(mariage.getDateDebut()),
                        Format.dateCourte(mariage.getDateFin()),
                        String.valueOf(mariage.getNombreEnfants())});
            }
        }

        Entete.commentaire("Nom : " + homme.getNomComplet());
        System.out.println();
        Entete.commentaire("Mariages en cours : " + enCours.size());
        new TableConsole(new String[]{"Num", "Femme", "Date debut", "Nbr enfants"},
                new boolean[]{true, false, false, true}).lignes(enCours).afficher();
        System.out.println();
        Entete.commentaire("Mariages echoues : " + echoues.size());
        new TableConsole(new String[]{"Num", "Femme", "Date debut", "Date fin", "Nbr enfants"},
                new boolean[]{true, false, false, false, true}).lignes(echoues).afficher();
    }

    private static void controleCrud(FemmeService femmes, MariageService mariages) {
        Entete.etape(8, ETAPES, "Controle des operations de l'interface IDao");
        Entete.commentaire("Femme et mariage de controle crees, modifies puis supprimes");
        System.out.println();

        Femme temporaire = new Femme("TEST", "Temporaire", Format.date(1, 1, 1990));
        femmes.create(temporaire);

        Homme homme = new HommeService().findById(5);
        Mariage mariage = new Mariage(homme, temporaire, Format.date(1, 3, 2020), 0);
        mariages.create(mariage);

        mariage.setNombreEnfants(2);
        mariages.update(mariage);
        Mariage relu = mariages.findById(mariage.getId());
        boolean modification = relu != null && relu.getNombreEnfants() == 2;

        mariages.delete(relu);
        boolean suppressionMariage = mariages.findById(mariage.getId()) == null;

        femmes.delete(temporaire);
        boolean suppressionFemme = femmes.findById(temporaire.getId()) == null;

        Entete.verification("creation, modification et relecture du mariage ...", modification);
        Entete.verification("suppression du mariage ...", suppressionMariage);
        Entete.verification("suppression de la femme ...", suppressionFemme);
        Entete.commentaire("Base restauree : " + femmes.findAll().size() + " femmes, "
                + mariages.findAll().size() + " mariages");
    }

    private static void synthese(HommeService hommes, FemmeService femmes, MariageService mariages) {
        Entete.etape(9, ETAPES, "Synthese de l'etat civil");

        List<Mariage> tous = mariages.findAll();
        int enCours = 0;
        int enfants = 0;
        for (Mariage mariage : tous) {
            if (mariage.isEnCours()) {
                enCours++;
            }
            enfants += mariage.getNombreEnfants();
        }

        TableConsole table = new TableConsole(
                new String[]{"Indicateur", "Valeur"},
                new boolean[]{false, true});
        table.ligne("Hommes recenses", hommes.findAll().size());
        table.ligne("Femmes recensees", femmes.findAll().size());
        table.ligne("Mariages enregistres", tous.size());
        table.ligne("Mariages en cours", enCours);
        table.ligne("Mariages echoues", tous.size() - enCours);
        table.ligne("Enfants declares", enfants);
        table.afficher();

        Entete.fermer("Exercice 3 termine - " + ETAPES + " etapes validees");
    }
}
