package ma.projet.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Petit moteur de rendu de tableau ASCII pour les programmes de test.
 *
 * Exemple :
 * <pre>
 * new TableConsole("Reference", "Prix (DH)").ligne("ES12", 120).afficher();
 * </pre>
 */
public final class TableConsole {

    private final String[] entetes;
    private final boolean[] aligneADroite;
    private final List<String[]> lignes = new ArrayList<String[]>();

    public TableConsole(String... entetes) {
        this(entetes, null);
    }

    /**
     * @param entetes       libelles des colonnes
     * @param aligneADroite une valeur par colonne (true = alignement a droite,
     *                      utile pour les nombres) ; null = tout a gauche
     */
    public TableConsole(String[] entetes, boolean[] aligneADroite) {
        this.entetes = entetes;
        this.aligneADroite = aligneADroite != null ? aligneADroite : new boolean[entetes.length];
    }

    public TableConsole ligne(Object... cellules) {
        String[] valeurs = new String[entetes.length];
        for (int colonne = 0; colonne < entetes.length; colonne++) {
            valeurs[colonne] = colonne < cellules.length && cellules[colonne] != null
                    ? String.valueOf(cellules[colonne])
                    : "";
        }
        lignes.add(valeurs);
        return this;
    }

    public TableConsole lignes(List<String[]> valeurs) {
        for (String[] ligne : valeurs) {
            ligne((Object[]) ligne);
        }
        return this;
    }

    public void afficher() {
        int[] largeurs = largeurs();
        String separateur = separateur(largeurs);

        System.out.println("  " + separateur);
        System.out.println("  " + formater(entetes, largeurs, new boolean[entetes.length]));
        System.out.println("  " + separateur);
        for (String[] ligne : lignes) {
            System.out.println("  " + formater(ligne, largeurs, aligneADroite));
        }
        System.out.println("  " + separateur);

        if (lignes.isEmpty()) {
            System.out.println("  (aucun resultat)");
        }
    }

    private int[] largeurs() {
        int[] largeurs = new int[entetes.length];
        for (int colonne = 0; colonne < entetes.length; colonne++) {
            largeurs[colonne] = entetes[colonne].length();
        }
        for (String[] ligne : lignes) {
            for (int colonne = 0; colonne < ligne.length; colonne++) {
                largeurs[colonne] = Math.max(largeurs[colonne], ligne[colonne].length());
            }
        }
        return largeurs;
    }

    private String separateur(int[] largeurs) {
        StringBuilder trait = new StringBuilder("+");
        for (int largeur : largeurs) {
            trait.append(repet('=', largeur + 2)).append('+');
        }
        return trait.toString();
    }

    private String formater(String[] valeurs, int[] largeurs, boolean[] alignement) {
        StringBuilder ligne = new StringBuilder("|");
        for (int colonne = 0; colonne < valeurs.length; colonne++) {
            ligne.append(' ')
                    .append(caler(valeurs[colonne], largeurs[colonne], alignement[colonne]))
                    .append(" |");
        }
        return ligne.toString();
    }

    private String caler(String valeur, int largeur, boolean droite) {
        int manque = largeur - valeur.length();
        if (manque <= 0) {
            return valeur;
        }
        String espaces = repet(' ', manque);
        return droite ? espaces + valeur : valeur + espaces;
    }

    private String repet(char caractere, int fois) {
        StringBuilder texte = new StringBuilder(fois);
        for (int i = 0; i < fois; i++) {
            texte.append(caractere);
        }
        return texte.toString();
    }
}
