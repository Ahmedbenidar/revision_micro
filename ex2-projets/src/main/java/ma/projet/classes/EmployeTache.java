package ma.projet.classes;

import java.util.Date;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

/**
 * Affectation d'un employe a une tache, avec les dates reellement passees.
 */
@Entity
@Table(name = "employe_tache",
        uniqueConstraints = @UniqueConstraint(columnNames = {"employe_id", "tache_id"}))
@NamedQueries({
        @NamedQuery(
                name = "EmployeTache.tachesRealiseesDeLEmploye",
                query = "select et from EmployeTache et "
                        + "join fetch et.tache t "
                        + "join fetch t.projet "
                        + "where et.employe = :employe and t.dateFinReelle is not null "
                        + "order by t.dateDebutReelle"),
        @NamedQuery(
                name = "EmployeTache.affectationsDeLEmploye",
                query = "select et from EmployeTache et "
                        + "join fetch et.tache "
                        + "where et.employe = :employe order by et.tache.nom")
})
public class EmployeTache {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Temporal(TemporalType.DATE)
    @Column(name = "date_debut_reelle")
    private Date dateDebutReelle;

    @Temporal(TemporalType.DATE)
    @Column(name = "date_fin_reelle")
    private Date dateFinReelle;

    @ManyToOne(optional = false)
    @JoinColumn(name = "employe_id", nullable = false)
    private Employe employe;

    @ManyToOne(optional = false)
    @JoinColumn(name = "tache_id", nullable = false)
    private Tache tache;

    public EmployeTache() {
    }

    public EmployeTache(Employe employe, Tache tache, Date dateDebutReelle, Date dateFinReelle) {
        this.employe = employe;
        this.tache = tache;
        this.dateDebutReelle = dateDebutReelle;
        this.dateFinReelle = dateFinReelle;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public Employe getEmploye() {
        return employe;
    }

    public void setEmploye(Employe employe) {
        this.employe = employe;
    }

    public Tache getTache() {
        return tache;
    }

    public void setTache(Tache tache) {
        this.tache = tache;
    }

    @Override
    public String toString() {
        return employe.getNomComplet() + " -> " + tache.getNom();
    }
}
