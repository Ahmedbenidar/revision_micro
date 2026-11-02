package ma.projet.classes;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * Detail d'une commande : un produit et la quantite demandee.
 */
@Entity
@Table(name = "ligne_commande",
        uniqueConstraints = @UniqueConstraint(columnNames = {"commande_id", "produit_id"}))
@NamedQueries({
        @NamedQuery(
                name = "LigneCommande.parPeriode",
                query = "select lc from LigneCommande lc "
                        + "join fetch lc.produit "
                        + "join fetch lc.commande "
                        + "where lc.commande.date between :debut and :fin "
                        + "order by lc.commande.date, lc.produit.reference"),
        @NamedQuery(
                name = "LigneCommande.parCommande",
                query = "select lc from LigneCommande lc "
                        + "join fetch lc.produit "
                        + "where lc.commande = :commande "
                        + "order by lc.produit.reference")
})
public class LigneCommande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "quantite", nullable = false)
    private int quantite;

    @ManyToOne(optional = false)
    @JoinColumn(name = "produit_id", nullable = false)
    private Produit produit;

    @ManyToOne(optional = false)
    @JoinColumn(name = "commande_id", nullable = false)
    private Commande commande;

    public LigneCommande() {
    }

    public LigneCommande(Produit produit, Commande commande, int quantite) {
        this.produit = produit;
        this.commande = commande;
        this.quantite = quantite;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public Produit getProduit() {
        return produit;
    }

    public void setProduit(Produit produit) {
        this.produit = produit;
    }

    public Commande getCommande() {
        return commande;
    }

    public void setCommande(Commande commande) {
        this.commande = commande;
    }

    public double getTotal() {
        return produit.getPrix() * quantite;
    }

    @Override
    public String toString() {
        return produit.getReference() + " x " + quantite;
    }
}
