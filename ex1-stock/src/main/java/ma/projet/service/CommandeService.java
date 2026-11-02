package ma.projet.service;

import java.util.List;

import ma.projet.classes.Commande;
import ma.projet.dao.SocleDao;

/**
 * Operations de gestion des commandes.
 */
public class CommandeService extends SocleDao<Commande> {

    public CommandeService() {
        super(Commande.class);
    }

    /** Les commandes sont toujours presentees dans l'ordre chronologique. */
    @Override
    public List<Commande> findAll() {
        return lire(session -> session
                .createQuery("from Commande c order by c.date", Commande.class)
                .getResultList());
    }
}
