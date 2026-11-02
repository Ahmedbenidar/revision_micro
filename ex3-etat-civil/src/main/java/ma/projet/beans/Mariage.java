package ma.projet.beans;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Union entre un homme et une femme.
 * <p>
 * Une date de fin nulle signifie que le mariage est toujours en cours.
 */
@Entity
@Table(name = "mariage")
@NamedQueries({
        @NamedQuery(
                name = "Mariage.epousesEntreDeuxDates",
                query = "select m from Mariage m "
                        + "join fetch m.femme "
                        + "where m.homme = :homme and m.dateDebut between :debut and :fin "
                        + "order by m.dateDebut"),
        @NamedQuery(
                name = "Mariage.parHomme",
                query = "select m from Mariage m "
                        + "join fetch m.femme "
                        + "where m.homme = :homme order by m.dateDebut"),
        @NamedQuery(
                name = "Mariage.femmesMarieesAuMoinsDeuxFois",
                query = "select m.femme from Mariage m "
                        + "group by m.femme having count(m) >= 2 "
                        + "order by m.femme.nom, m.femme.prenom")
})
@NamedNativeQuery(
        name = "Mariage.nombreEnfantsFemmeEntreDeuxDates",
        query = "select coalesce(sum(m.nbr_enfants), 0) from mariage m "
                + "where m.femme_id = :femme and m.date_debut between :debut and :fin")
public class Mariage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Temporal(TemporalType.DATE)
    @Column(name = "date_debut", nullable = false)
    private Date dateDebut;

    @Temporal(TemporalType.DATE)
    @Column(name = "date_fin")
    private Date dateFin;

    @Column(name = "nbr_enfants", nullable = false)
    private int nombreEnfants;

    @ManyToOne(optional = false)
    @JoinColumn(name = "homme_id", nullable = false)
    private Homme homme;

    @ManyToOne(optional = false)
    @JoinColumn(name = "femme_id", nullable = false)
    private Femme femme;

    public Mariage() {
    }

    public Mariage(Homme homme, Femme femme, Date dateDebut, int nombreEnfants) {
        this.homme = homme;
        this.femme = femme;
        this.dateDebut = dateDebut;
        this.nombreEnfants = nombreEnfants;
    }

    public Mariage(Homme homme, Femme femme, Date dateDebut, Date dateFin, int nombreEnfants) {
        this(homme, femme, dateDebut, nombreEnfants);
        this.dateFin = dateFin;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public int getNombreEnfants() {
        return nombreEnfants;
    }

    public void setNombreEnfants(int nombreEnfants) {
        this.nombreEnfants = nombreEnfants;
    }

    public Homme getHomme() {
        return homme;
    }

    public void setHomme(Homme homme) {
        this.homme = homme;
    }

    public Femme getFemme() {
        return femme;
    }

    public void setFemme(Femme femme) {
        this.femme = femme;
    }

    public boolean isEnCours() {
        return dateFin == null;
    }

    @Override
    public String toString() {
        return homme.getNomComplet() + " / " + femme.getNomComplet();
    }
}
