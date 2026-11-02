package ma.projet.service;

import java.util.List;

import ma.projet.classes.Employe;
import ma.projet.classes.Projet;
import ma.projet.dao.SocleDao;

/**
 * Operations de gestion des employes du bureau d'etudes.
 */
public class EmployeService extends SocleDao<Employe> {

    public EmployeService() {
        super(Employe.class);
    }

    /** Projets dont l'employe assure la responsabilite. */
    public List<Projet> projetsGeresParLEmploye(final Employe employe) {
        return lire(session -> session
                .createNamedQuery("Projet.parResponsable", Projet.class)
                .setParameter("employe", employe)
                .getResultList());
    }
}
