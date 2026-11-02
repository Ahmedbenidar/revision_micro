package ma.projet.service;

import ma.projet.classes.Categorie;
import ma.projet.dao.SocleDao;

/**
 * Operations de gestion des categories.
 */
public class CategorieService extends SocleDao<Categorie> {

    public CategorieService() {
        super(Categorie.class);
    }

    public Categorie parNom(final String nom) {
        return lire(session -> session
                .createQuery("from Categorie c where c.nom = :nom", Categorie.class)
                .setParameter("nom", nom)
                .uniqueResult());
    }
}
