package ma.projet.service;

import java.util.List;

import ma.projet.classes.Commande;
import ma.projet.classes.LigneCommande;
import ma.projet.dao.SocleDao;

/**
 * Operations de gestion des lignes de commande.
 */
public class LigneCommandeService extends SocleDao<LigneCommande> {

    public LigneCommandeService() {
        super(LigneCommande.class);
    }

    public List<LigneCommande> parCommande(final Commande commande) {
        return lire(session -> session
                .createQuery("from LigneCommande lc where lc.commande = :commande", LigneCommande.class)
                .setParameter("commande", commande)
                .getResultList());
    }
}
