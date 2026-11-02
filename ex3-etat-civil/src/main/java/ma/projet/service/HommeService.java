package ma.projet.service;

import java.util.Date;
import java.util.List;

import ma.projet.beans.Homme;
import ma.projet.beans.Mariage;
import ma.projet.dao.SocleDao;

/**
 * Operations de gestion des citoyens.
 */
public class HommeService extends SocleDao<Homme> {

    public HommeService() {
        super(Homme.class);
    }

    /** Epouses d'un homme dont le mariage a commence entre deux dates. */
    public List<Mariage> epousesEntreDeuxDates(final Homme homme, final Date debut, final Date fin) {
        return lire(session -> session
                .createNamedQuery("Mariage.epousesEntreDeuxDates", Mariage.class)
                .setParameter("homme", homme)
                .setParameter("debut", debut)
                .setParameter("fin", fin)
                .getResultList());
    }
}
