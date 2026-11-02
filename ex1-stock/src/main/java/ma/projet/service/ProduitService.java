package ma.projet.service;

import java.util.Date;
import java.util.List;

import ma.projet.classes.Categorie;
import ma.projet.classes.Commande;
import ma.projet.classes.LigneCommande;
import ma.projet.classes.Produit;
import ma.projet.dao.SocleDao;

/**
 * Operations de gestion des produits, y compris les recherches demandees par
 * l'exercice.
 */
public class ProduitService extends SocleDao<Produit> {

    public ProduitService() {
        super(Produit.class);
    }

    public Produit parReference(final String reference) {
        return lire(session -> session
                .createQuery("from Produit p where p.reference = :reference", Produit.class)
                .setParameter("reference", reference)
                .uniqueResult());
    }

    /** Liste des produits d'une categorie (requete nommee Produit.parCategorie). */
    public List<Produit> produitsParCategorie(final Categorie categorie) {
        return lire(session -> session
                .createNamedQuery("Produit.parCategorie", Produit.class)
                .setParameter("categorie", categorie)
                .getResultList());
    }

    /** Lignes de commande dont la commande est passee entre deux dates. */
    public List<LigneCommande> produitsCommandesEntre(final Date debut, final Date fin) {
        return lire(session -> session
                .createNamedQuery("LigneCommande.parPeriode", LigneCommande.class)
                .setParameter("debut", debut)
                .setParameter("fin", fin)
                .getResultList());
    }

    /** Produits contenus dans une commande donnee. */
    public List<LigneCommande> produitsDuneCommande(final Commande commande) {
        return lire(session -> session
                .createNamedQuery("LigneCommande.parCommande", LigneCommande.class)
                .setParameter("commande", commande)
                .getResultList());
    }

    /** Produits plus chers qu'un seuil (requete nommee Produit.prixSuperieurA). */
    public List<Produit> produitsPrixSuperieurA(final double seuil) {
        return lire(session -> session
                .createNamedQuery("Produit.prixSuperieurA", Produit.class)
                .setParameter("seuil", seuil)
                .getResultList());
    }
}
