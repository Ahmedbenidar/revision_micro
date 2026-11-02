package ma.projet.util;

import java.util.Date;

/**
 * Bandeau, titres de sections et pied de page des programmes de test.
 * Le nom de l'auteur est le repere personnel du rendu.
 */
public final class Entete {

    public static final String AUTEUR = "Ahmed BEN IDAR";

    private static final String TRAIT =
            "==========================================================================================";
    private static final String TRAIT_FIN =
            "------------------------------------------------------------------------------------------";

    private Entete() {
    }

    public static void ouvrir(String exercice, String theme, String contexte) {
        System.out.println();
        System.out.println(TRAIT);
        System.out.println("  " + exercice + "   " + theme);
        System.out.println("  " + contexte);
        System.out.println(TRAIT_FIN);
        System.out.println("  Programme de test realise par " + AUTEUR
                + "        " + Format.horodatage(new Date()));
        System.out.println(TRAIT);
    }

    public static void etape(int numero, int total, String libelle) {
        System.out.println();
        System.out.println("[" + numero + "/" + total + "] " + libelle);
        System.out.println(TRAIT_FIN);
    }

    public static void commentaire(String texte) {
        System.out.println("  > " + texte);
    }

    /** Ligne de controle alignee : "  delete du produit ............... OK". */
    public static void verification(String libelle, boolean reussie) {
        StringBuilder ligne = new StringBuilder("  ").append(libelle);
        while (ligne.length() < 44) {
            ligne.append('.');
        }
        System.out.println(ligne.append(' ').append(reussie ? "OK" : "ECHEC").toString());
    }

    public static void fermer(String texte) {
        System.out.println();
        System.out.println(TRAIT);
        System.out.println("  " + texte);
        System.out.println(TRAIT);
        System.out.println();
    }
}
