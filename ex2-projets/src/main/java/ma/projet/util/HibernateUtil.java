package ma.projet.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.LogManager;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import ma.projet.classes.Employe;
import ma.projet.classes.EmployeTache;
import ma.projet.classes.Projet;
import ma.projet.classes.Tache;

/**
 * Fabrique de sessions Hibernate construite a partir de application.properties.
 */
public final class HibernateUtil {

    /** Entites declarees dans le fichier de configuration (une seule liste a maintenir). */
    private static final Class<?>[] ENTITES = {
            Projet.class,
            Tache.class,
            Employe.class,
            EmployeTache.class
    };

    private static final SessionFactory FABRIQUE = construire();

    private HibernateUtil() {
    }

    private static SessionFactory construire() {
        reduireLeBruitDeHibernate();

        StandardServiceRegistry registre = new StandardServiceRegistryBuilder()
                .applySettings(lireReglages())
                .build();

        MetadataSources sources = new MetadataSources(registre);
        for (Class<?> entite : ENTITES) {
            sources.addAnnotatedClass(entite);
        }
        return sources.buildMetadata().buildSessionFactory();
    }

    /** Applique logging.properties pour n'afficher que les messages utiles. */
    private static void reduireLeBruitDeHibernate() {
        try (InputStream flux = HibernateUtil.class.getClassLoader()
                .getResourceAsStream("logging.properties")) {
            if (flux != null) {
                LogManager.getLogManager().readConfiguration(flux);
            }
        } catch (IOException ignore) {
            // la configuration par defaut reste appliquee
        }
    }

    private static Properties lireReglages() {
        Properties fichier = new Properties();
        try (InputStream flux = HibernateUtil.class.getClassLoader()
                .getResourceAsStream("application.properties")) {
            if (flux == null) {
                throw new IllegalStateException("application.properties introuvable dans le classpath");
            }
            fichier.load(flux);
        } catch (IOException e) {
            throw new IllegalStateException("Lecture de application.properties impossible", e);
        }

        Properties reglages = new Properties();
        reglages.put("hibernate.connection.driver_class", fichier.getProperty("db.driver"));
        reglages.put("hibernate.connection.url", fichier.getProperty("db.url"));
        reglages.put("hibernate.connection.username", fichier.getProperty("db.user"));
        reglages.put("hibernate.connection.password", fichier.getProperty("db.password", ""));
        reglages.put("hibernate.dialect", fichier.getProperty("hibernate.dialect"));
        reglages.put("hibernate.show_sql", fichier.getProperty("hibernate.show_sql", "false"));
        reglages.put("hibernate.format_sql", fichier.getProperty("hibernate.format_sql", "false"));
        reglages.put("hibernate.hbm2ddl.auto", fichier.getProperty("hibernate.hbm2ddl.auto", "update"));
        return reglages;
    }

    public static SessionFactory getSessionFactory() {
        return FABRIQUE;
    }

    /**
     * Ferme la fabrique et arrete le thread de nettoyage du pilote MySQL, sans
     * quoi la JVM reste bloquee apres la fin du programme de test.
     */
    public static void fermer() {
        FABRIQUE.close();
        try {
            Class<?> nettoyage = Class.forName("com.mysql.jdbc.AbandonedConnectionCleanupThread");
            nettoyage.getMethod("shutdown").invoke(null);
        } catch (Exception ignore) {
            // ce pilote ne lance pas de thread de nettoyage : rien a faire
        }
    }
}
