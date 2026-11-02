package ma.projet.util;

import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * Remise a zero des tables avant le chargement du jeu de donnees, afin que les
 * identifiants affiches par les programmes de test restent identiques d'une
 * execution a l'autre.
 */
public final class MaintenanceBase {

    private MaintenanceBase() {
    }

    public static void viderEtReinitialiser(String... tables) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.createNativeQuery("SET FOREIGN_KEY_CHECKS = 0").executeUpdate();
            for (String table : tables) {
                session.createNativeQuery("DELETE FROM " + table).executeUpdate();
                session.createNativeQuery("ALTER TABLE " + table + " AUTO_INCREMENT = 1").executeUpdate();
            }
            session.createNativeQuery("SET FOREIGN_KEY_CHECKS = 1").executeUpdate();
            transaction.commit();
        } catch (RuntimeException e) {
            transaction.rollback();
            throw e;
        } finally {
            session.close();
        }
    }
}
