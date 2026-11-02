package ma.projet.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Mise en forme des dates et des montants utilisee par les programmes de test.
 * Les libelles sont volontairement sans accents pour rester lisibles quelle que
 * soit l'encodage de la console.
 */
public final class Format {

    private static final String[] MOIS = {
            "Janvier", "Fevrier", "Mars", "Avril", "Mai", "Juin",
            "Juillet", "Aout", "Septembre", "Octobre", "Novembre", "Decembre"
    };

    private Format() {
    }

    /** "14 Mars 2013" */
    public static String dateLongue(Date date) {
        if (date == null) {
            return "-";
        }
        Calendar calendrier = calendrier(date);
        return jour(calendrier) + " " + MOIS[calendrier.get(Calendar.MONTH)]
                + " " + calendrier.get(Calendar.YEAR);
    }

    /** "14/03/2013" */
    public static String dateCourte(Date date) {
        return date == null ? "-" : new SimpleDateFormat("dd/MM/yyyy").format(date);
    }

    /** "02/11/2026 11:15" */
    public static String horodatage(Date date) {
        return new SimpleDateFormat("dd/MM/yyyy HH:mm").format(date);
    }

    public static String montant(double valeur) {
        if (valeur == Math.rint(valeur)) {
            return String.valueOf((long) valeur);
        }
        return String.format("%.2f", valeur).replace('.', ',');
    }

    /** Age en annees revolues a la date du jour. */
    public static int age(Date naissance) {
        if (naissance == null) {
            return 0;
        }
        Calendar debut = calendrier(naissance);
        Calendar aujourdhui = new GregorianCalendar();
        int annees = aujourdhui.get(Calendar.YEAR) - debut.get(Calendar.YEAR);
        if (aujourdhui.get(Calendar.DAY_OF_YEAR) < debut.get(Calendar.DAY_OF_YEAR)) {
            annees--;
        }
        return annees;
    }

    /** Construit une date a partir du jour, du mois (1 a 12) et de l'annee. */
    public static Date date(int jour, int mois, int annee) {
        Calendar calendrier = new GregorianCalendar();
        calendrier.clear();
        calendrier.set(annee, mois - 1, jour);
        return calendrier.getTime();
    }

    private static Calendar calendrier(Date date) {
        Calendar calendrier = new GregorianCalendar();
        calendrier.setTime(date);
        return calendrier;
    }

    private static String jour(Calendar calendrier) {
        int valeur = calendrier.get(Calendar.DAY_OF_MONTH);
        return valeur < 10 ? "0" + valeur : String.valueOf(valeur);
    }
}
