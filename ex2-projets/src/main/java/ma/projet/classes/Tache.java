package ma.projet.classes;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Tache d'un projet, avec ses dates previsionnelles et reelles.
 */
@Entity
@Table(name = "tache")
@NamedQueries({
        @NamedQuery(
                name = "Tache.planifieesDuProjet",
                query = "select t from Tache t where t.projet = :projet "
                        + "order by t.dateDebutPrevue, t.nom"),
        @NamedQuery(
                name = "Tache.realiseesDuProjet",
                query = "select t from Tache t where t.projet = :projet "
                        + "and t.dateFinReelle is not null order by t.dateDebutReelle")
})
public class Tache {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "nom", nullable = false, length = 80)
    private String nom;

    @Temporal(TemporalType.DATE)
    @Column(name = "date_debut_prevue", nullable = false)
    private Date dateDebutPrevue;

    @Temporal(TemporalType.DATE)
    @Column(name = "date_fin_prevue", nullable = false)
    private Date dateFinPrevue;

    @Temporal(TemporalType.DATE)
    @Column(name = "date_debut_reelle")
    private Date dateDebutReelle;

    @Temporal(TemporalType.DATE)
    @Column(name = "date_fin_reelle")
    private Date dateFinReelle;

    @ManyToOne(optional = false)
    @JoinColumn(name = "projet_id", nullable = false)
    private Projet projet;

    @OneToMany(mappedBy = "tache")
    private List<EmployeTache> affectations = new ArrayList<EmployeTache>();

    public Tache() {
    }

    public Tache(String nom, Date dateDebutPrevue, Date dateFinPrevue, Projet projet) {
        this.nom = nom;
        this.dateDebutPrevue = dateDebutPrevue;
        this.dateFinPrevue = dateFinPrevue;
        this.projet = projet;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Date getDateDebutPrevue() {
        return dateDebutPrevue;
    }

    public void setDateDebutPrevue(Date dateDebutPrevue) {
        this.dateDebutPrevue = dateDebutPrevue;
    }

    public Date getDateFinPrevue() {
        return dateFinPrevue;
    }

    public void setDateFinPrevue(Date dateFinPrevue) {
        this.dateFinPrevue = dateFinPrevue;
    }

    public Date getDateDebutReelle() {
        return dateDebutReelle;
    }

    public void setDateDebutReelle(Date dateDebutReelle) {
        this.dateDebutReelle = dateDebutReelle;
    }

    public Date getDateFinReelle() {
        return dateFinReelle;
    }

    public void setDateFinReelle(Date dateFinReelle) {
        this.dateFinReelle = dateFinReelle;
    }

    public Projet getProjet() {
        return projet;
    }

    public void setProjet(Projet projet) {
        this.projet = projet;
    }

    public List<EmployeTache> getAffectations() {
        return affectations;
    }

    public void setAffectations(List<EmployeTache> affectations) {
        this.affectations = affectations;
    }

    public boolean isRealisee() {
        return dateFinReelle != null;
    }

    @Override
    public String toString() {
        return nom;
    }
}
