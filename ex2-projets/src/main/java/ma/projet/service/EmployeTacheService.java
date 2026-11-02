package ma.projet.service;

import java.util.ArrayList;
import java.util.List;

import ma.projet.classes.Employe;
import ma.projet.classes.EmployeTache;
import ma.projet.classes.Tache;
import ma.projet.dao.SocleDao;

/**
 * Operations de gestion des affectations employe / tache.
 */
public class EmployeTacheService extends SocleDao<EmployeTache> {

    public EmployeTacheService() {
        super(EmployeTache.class);
    }

    /** Taches effectivement terminees par un employe. */
    public List<Tache> tachesRealiseesParLEmploye(final Employe employe) {
        List<EmployeTache> affectations = lire(session -> session
                .createNamedQuery("EmployeTache.tachesRealiseesDeLEmploye", EmployeTache.class)
                .setParameter("employe", employe)
                .getResultList());

        List<Tache> taches = new ArrayList<Tache>();
        for (EmployeTache affectation : affectations) {
            taches.add(affectation.getTache());
        }
        return taches;
    }

    /** Toutes les affectations d'un employe, terminees ou non. */
    public List<EmployeTache> affectationsDeLEmploye(final Employe employe) {
        return lire(session -> session
                .createNamedQuery("EmployeTache.affectationsDeLEmploye", EmployeTache.class)
                .setParameter("employe", employe)
                .getResultList());
    }
}
