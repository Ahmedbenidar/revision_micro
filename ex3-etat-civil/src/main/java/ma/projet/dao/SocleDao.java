package ma.projet.dao;

import java.io.Serializable;
import java.util.List;
import java.util.function.Function;

import org.hibernate.Session;
import org.hibernate.Transaction;

import ma.projet.util.HibernateUtil;

/**
 * Implementation generique de {@link IDao}.
 *
 * Les sous-classes n'ecrivent plus le code d'ouverture de session, de
 * transaction et de fermeture : elles se contentent de decrire le travail a
 * realiser dans {@link #lire(Function)} ou {@link #ecrire(Function)}.
 *
 * @param <T> entite geree
 */
public abstract class SocleDao<T> implements IDao<T> {

    private final Class<T> entite;

    protected SocleDao(Class<T> entite) {
        this.entite = entite;
    }

    protected Class<T> entite() {
        return entite;
    }

    /** Execution en lecture seule (pas de transaction explicite). */
    protected <R> R lire(Function<Session, R> travail) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            return travail.apply(session);
        } finally {
            session.close();
        }
    }

    /** Execution dans une transaction : commit si tout va bien, rollback sinon. */
    protected <R> R ecrire(Function<Session, R> travail) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            R resultat = travail.apply(session);
            transaction.commit();
            return resultat;
        } catch (RuntimeException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public boolean create(final T value) {
        return ecrire(session -> {
            session.save(value);
            return Boolean.TRUE;
        });
    }

    @Override
    public boolean update(final T value) {
        return ecrire(session -> {
            session.update(value);
            return Boolean.TRUE;
        });
    }

    @Override
    public boolean delete(final T value) {
        return ecrire(session -> {
            session.delete(value);
            return Boolean.TRUE;
        });
    }

    @Override
    public T findById(final Serializable id) {
        return lire(session -> session.get(entite, id));
    }

    @Override
    public List<T> findAll() {
        return lire(session -> session
                .createQuery("from " + entite.getSimpleName(), entite)
                .getResultList());
    }
}
