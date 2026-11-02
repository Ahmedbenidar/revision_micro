package ma.projet.test;

import ma.projet.beans.Femme;
import ma.projet.beans.Homme;
import ma.projet.beans.Mariage;
import ma.projet.service.FemmeService;
import ma.projet.service.HommeService;
import ma.projet.service.MariageService;
import ma.projet.util.Format;
import ma.projet.util.MaintenanceBase;

/**
 * Jeu de donnees personnel de la province : 5 hommes, 10 femmes et 13 unions.
 * Les tables sont videes puis remplies a chaque execution pour que les
 * identifiants du compte rendu restent stables.
 */
public final class DonneesEtatCivil {

    private DonneesEtatCivil() {
    }

    public static void charger() {
        MaintenanceBase.viderEtReinitialiser("mariage", "homme", "femme");

        HommeService hommes = new HommeService();
        FemmeService femmes = new FemmeService();
        MariageService mariages = new MariageService();

        Homme said = new Homme("SAFI", "SAID", Format.date(12, 3, 1968));
        Homme karim = new Homme("BENNANI", "KARIM", Format.date(5, 7, 1975));
        Homme youssef = new Homme("EL AMRANI", "YOUSSEF", Format.date(23, 11, 1980));
        Homme rachid = new Homme("OUAZZANI", "RACHID", Format.date(14, 2, 1985));
        Homme mehdi = new Homme("TAZI", "MEHDI", Format.date(30, 6, 1990));
        hommes.create(said);
        hommes.create(karim);
        hommes.create(youssef);
        hommes.create(rachid);
        hommes.create(mehdi);

        Femme salima = new Femme("RAMI", "SALIMA", Format.date(18, 4, 1970));
        Femme amal = new Femme("ALI", "AMAL", Format.date(7, 9, 1972));
        Femme wafa = new Femme("ALAOUI", "WAFA", Format.date(25, 12, 1974));
        Femme karima = new Femme("ALAMI", "KARIMA", Format.date(2, 2, 1965));
        Femme nadia = new Femme("BERRADA", "NADIA", Format.date(11, 6, 1978));
        Femme souad = new Femme("CHAKIR", "SOUAD", Format.date(30, 8, 1981));
        Femme hanane = new Femme("IDRISSI", "HANANE", Format.date(16, 3, 1983));
        Femme leila = new Femme("FASSI", "LEILA", Format.date(9, 11, 1986));
        Femme imane = new Femme("ZIANI", "IMANE", Format.date(22, 5, 1988));
        Femme ghita = new Femme("MARZOUK", "GHITA", Format.date(14, 7, 1992));
        Femme[] toutesLesFemmes = {salima, amal, wafa, karima, nadia,
                souad, hanane, leila, imane, ghita};
        for (Femme femme : toutesLesFemmes) {
            femmes.create(femme);
        }

        mariages.create(new Mariage(said, karima,
                Format.date(3, 9, 1989), Format.date(3, 9, 1990), 0));
        mariages.create(new Mariage(said, salima, Format.date(3, 9, 1990), 4));
        mariages.create(new Mariage(said, amal, Format.date(3, 9, 1995), 2));
        mariages.create(new Mariage(said, wafa, Format.date(4, 11, 2000), 3));

        mariages.create(new Mariage(karim, karima,
                Format.date(12, 1, 1994), Format.date(30, 6, 2001), 2));
        mariages.create(new Mariage(karim, souad,
                Format.date(20, 1, 2002), Format.date(15, 3, 2004), 1));
        mariages.create(new Mariage(karim, nadia, Format.date(12, 6, 2003), 2));

        mariages.create(new Mariage(youssef, nadia,
                Format.date(15, 3, 2000), Format.date(12, 12, 2001), 0));
        mariages.create(new Mariage(youssef, souad,
                Format.date(2, 4, 2005), Format.date(20, 8, 2014), 2));
        mariages.create(new Mariage(youssef, hanane, Format.date(6, 12, 2015), 1));

        mariages.create(new Mariage(rachid, leila,
                Format.date(14, 2, 2009), Format.date(1, 1, 2018), 0));
        mariages.create(new Mariage(rachid, imane, Format.date(22, 7, 2019), 2));

        mariages.create(new Mariage(mehdi, ghita, Format.date(30, 6, 2016), 1));
    }
}
