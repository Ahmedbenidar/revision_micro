-- Bases de donnees du lab (XAMPP / MariaDB, port 3306).
-- Les tables sont creees automatiquement par Hibernate (hibernate.hbm2ddl.auto=update),
-- ce script ne sert donc qu'a preparer les schemas.
--
-- Executer :  mysql -u root < sql/creation_bases.sql

CREATE DATABASE IF NOT EXISTS stock_atlas_info
    DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

CREATE DATABASE IF NOT EXISTS bureau_etudes_atlas
    DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

CREATE DATABASE IF NOT EXISTS etat_civil_souss
    DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
