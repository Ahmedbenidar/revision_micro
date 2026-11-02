package ma.projet.service;

import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import ma.projet.beans.Femme;
import ma.projet.beans.Mariage;
import ma.projet.dao.SocleDao;

/**
 * Operations de gestion des citoyennes.
 */
public class FemmeService extends SocleDao<Femme> {

    public FemmeService() {
        super(Femme.class);
    }

    /**
     * Nombre total d'enfants nes d'une femme entre deux dates, calcule par une
     * requete native nommee (Mariage.nombreEnfantsFemmeEntreDeuxDates).
     */
    public int nombreEnfantsEntreDeuxDates(final Femme femme, final Date debut, final Date fin) {
        return lire(session -> {
            Object total = session
                    .getNamedNativeQuery("Mariage.nombreEnfantsFemmeEntreDeuxDates")
                    .setParameter("femme", femme.getId())
                    .setParameter("debut", new java.sql.Date(debut.getTime()))
                    .setParameter("fin", new java.sql.Date(fin.getTime()))
                    .uniqueResult();
            return total == null ? 0 : ((Number) total).intValue();
        });
    }

    /** Femmes mariees au moins deux fois (requete nommee). */
    public List<Femme> femmesMarieesAuMoinsDeuxFois() {
        return lire(session -> session
                .createNamedQuery("Mariage.femmesMarieesAuMoinsDeuxFois", Femme.class)
                .getResultList());
    }

    /** Femme dont la date de naissance est la plus ancienne (requete nommee). */
    public Femme laPlusAgee() {
        return lire(session -> session
                .createNamedQuery("Femme.laPlusAgee", Femme.class)
                .setMaxResults(1)
                .uniqueResult());
    }

    /**
     * Nombre d'hommes ayant contracte au moins quatre mariages commences entre
     * deux dates. Cet indicateur est construit avec l'API Criteria.
     */
    public long nombreHommesMariesAQuatreFemmesEntreDeuxDates(final Date debut, final Date fin) {
        return lire(session -> {
            CriteriaBuilder constructeur = session.getCriteriaBuilder();
            CriteriaQuery<Long> requete = constructeur.createQuery(Long.class);

            Subquery<Integer> identifiants = requete.subquery(Integer.class);
            Root<Mariage> mariageFiltre = identifiants.from(Mariage.class);
            Path<Integer> identifiantHomme = mariageFiltre.get("homme").get("id");
            identifiants.select(identifiantHomme);
            identifiants.where(constructeur.between(mariageFiltre.get("dateDebut"), debut, fin));
            identifiants.groupBy(mariageFiltre.get("homme"));
            identifiants.having(constructeur.ge(constructeur.count(mariageFiltre), 4L));

            Root<Mariage> mariage = requete.from(Mariage.class);
            requete.select(constructeur.countDistinct(mariage.get("homme")));
            requete.where(mariage.get("homme").get("id").in(identifiants));

            return session.createQuery(requete).getSingleResult();
        });
    }
}
