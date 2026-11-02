package ma.projet.service;

import java.util.List;

import ma.projet.classes.Employe;
import ma.projet.classes.Projet;
import ma.projet.classes.Tache;
import ma.projet.dao.SocleDao;

/**
 * Operations de gestion des projets.
 */
public class ProjetService extends SocleDao<Projet> {

    public ProjetService() {
        super(Projet.class);
    }

    /** Taches planifiees d'un projet, avec leurs dates previsionnelles. */
    public List<Tache> tachesPlanifiees(final Projet projet) {
        return lire(session -> session
                .createNamedQuery("Tache.planifieesDuProjet", Tache.class)
                .setParameter("projet", projet)
                .getResultList());
    }

    /** Taches reellement terminees d'un projet, avec leurs dates reelles. */
    public List<Tache> tachesRealisees(final Projet projet) {
        return lire(session -> session
                .createNamedQuery("Tache.realiseesDuProjet", Tache.class)
                .setParameter("projet", projet)
                .getResultList());
    }

    public Projet parNom(final String nom) {
        return lire(session -> session
                .createQuery("from Projet p where p.nom = :nom", Projet.class)
                .setParameter("nom", nom)
                .uniqueResult());
    }
}
