package ma.projet.service;

import java.util.List;

import ma.projet.classes.Projet;
import ma.projet.classes.Tache;
import ma.projet.dao.SocleDao;

/**
 * Operations de gestion des taches.
 */
public class TacheService extends SocleDao<Tache> {

    public TacheService() {
        super(Tache.class);
    }

    public List<Tache> parProjet(final Projet projet) {
        return lire(session -> session
                .createQuery("from Tache t where t.projet = :projet order by t.nom", Tache.class)
                .setParameter("projet", projet)
                .getResultList());
    }
}
