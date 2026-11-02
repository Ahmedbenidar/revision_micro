package ma.projet.service;

import java.util.List;

import ma.projet.beans.Homme;
import ma.projet.beans.Mariage;
import ma.projet.dao.SocleDao;

/**
 * Operations de gestion des mariages.
 */
public class MariageService extends SocleDao<Mariage> {

    public MariageService() {
        super(Mariage.class);
    }

    /** Tous les mariages d'un homme, du plus ancien au plus recent. */
    public List<Mariage> mariagesDunHomme(final Homme homme) {
        return lire(session -> session
                .createNamedQuery("Mariage.parHomme", Mariage.class)
                .setParameter("homme", homme)
                .getResultList());
    }
}
