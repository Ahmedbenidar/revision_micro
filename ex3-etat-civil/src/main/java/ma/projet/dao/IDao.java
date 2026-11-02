package ma.projet.dao;

import java.io.Serializable;
import java.util.List;

/**
 * Contrat unique implemente par tous les services du projet.
 *
 * @param <T> entite prise en charge par le service
 */
public interface IDao<T> {

    boolean create(T entite);

    boolean update(T entite);

    boolean delete(T entite);

    T findById(Serializable id);

    List<T> findAll();
}
