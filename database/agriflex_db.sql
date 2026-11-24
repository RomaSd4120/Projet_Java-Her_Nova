
CREATE DATABASE IF NOT EXISTS agriflex_db
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE agriflex_db;

-- 1. Table des Utilisateurs (Authentification et Rôles)
CREATE TABLE IF NOT EXISTS utilisateur (
    id_utilisateur INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(50) NOT NULL,
    prenom VARCHAR(50) NOT NULL,
    cin VARCHAR(30) UNIQUE AFTER prenom,
    telephone VARCHAR(20) UNIQUE NOT NULL,
    mot_de_passe_hashe VARCHAR(255) NOT NULL,
    role VARCHAR(30) NOT NULL,
    INDEX idx_utilisateur_role (role)
);

-- 2. Table des Régions (Matching géographique et statistiques)
CREATE TABLE IF NOT EXISTS region (
    id_region INT AUTO_INCREMENT PRIMARY KEY,
    nom_region VARCHAR(100) NOT NULL UNIQUE
);

-- 3. Table des Travailleurs (Profils candidats)
CREATE TABLE IF NOT EXISTS travailleur (
    id_candidat INT AUTO_INCREMENT PRIMARY KEY,
    id_utilisateur INT NOT NULL,
    id_region INT,
    competences TEXT,
    experience TEXT,
    disponibilite VARCHAR(50) DEFAULT 'DISPONIBLE',
    mobilite BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (id_utilisateur) REFERENCES utilisateur(id_utilisateur) ON DELETE CASCADE,
    FOREIGN KEY (id_region) REFERENCES region(id_region) ON DELETE SET NULL,
    INDEX idx_travailleur_region_dispo (id_region, disponibilite),
    INDEX idx_travailleur_disponibilite (disponibilite)
);

-- 4. Table des Entreprises Agricoles
CREATE TABLE IF NOT EXISTS entreprise_agricole (
    id_entreprise INT AUTO_INCREMENT PRIMARY KEY,
    raison_sociale VARCHAR(150) NOT NULL,
    contact VARCHAR(100),
    telephone VARCHAR(20),
    region VARCHAR(100),
    sites TEXT,
    statut VARCHAR(30) DEFAULT 'ACTIF',
    INDEX idx_entreprise_statut (statut),
    INDEX idx_entreprise_region (region)
);

-- 5. Table des Demandes de Personnel / Missions
CREATE TABLE IF NOT EXISTS demande_personnel (
    id_mission INT AUTO_INCREMENT PRIMARY KEY,
    id_entreprise INT NOT NULL,
    poste VARCHAR(100) NOT NULL,
    description TEXT,
    nombre_travailleurs INT NOT NULL,
    lieu VARCHAR(100),
    date_debut DATE NOT NULL,
    date_fin DATE NOT NULL,
    competence_requise VARCHAR(255),
    statut VARCHAR(30) DEFAULT 'OUVERTE',
    FOREIGN KEY (id_entreprise) REFERENCES entreprise_agricole(id_entreprise) ON DELETE CASCADE,
    INDEX idx_mission_statut_dates (statut, date_debut, date_fin),
    INDEX idx_mission_lieu (lieu)
);

-- 6. Table des Affectations (Lien entre Candidats et Missions)
CREATE TABLE IF NOT EXISTS affectation (
    id_affectation INT AUTO_INCREMENT PRIMARY KEY,
    id_candidat INT NOT NULL,
    id_mission INT NOT NULL,
    date_proposition DATE,
    statut VARCHAR(30) DEFAULT 'PROPOSEE',
    FOREIGN KEY (id_candidat) REFERENCES travailleur(id_candidat) ON DELETE CASCADE,
    FOREIGN KEY (id_mission) REFERENCES demande_personnel(id_mission) ON DELETE CASCADE,
    INDEX idx_affectation_candidat_statut (id_candidat, statut),
    INDEX idx_affectation_mission (id_mission)
);

-- 7. Table des Notifications
CREATE TABLE IF NOT EXISTS notification (
    id_notification INT AUTO_INCREMENT PRIMARY KEY,
    id_candidat INT NOT NULL,
    id_mission INT,
    canal VARCHAR(30) DEFAULT 'SMS',
    message TEXT NOT NULL,
    date_envoi DATETIME DEFAULT CURRENT_TIMESTAMP,
    statut_envoi VARCHAR(30) DEFAULT 'ENVOYE',
    FOREIGN KEY (id_candidat) REFERENCES travailleur(id_candidat) ON DELETE CASCADE,
    FOREIGN KEY (id_mission) REFERENCES demande_personnel(id_mission) ON DELETE SET NULL,
    INDEX idx_notification_candidat_statut (id_candidat, statut_envoi),
    INDEX idx_notification_date_envoi (date_envoi)
);

-- 8. Table des Codes OTP (Vérification à usage unique par téléphone)
CREATE TABLE IF NOT EXISTS code_otp (
    id_otp INT AUTO_INCREMENT PRIMARY KEY,
    telephone VARCHAR(20) NOT NULL,
    code VARCHAR(10) NOT NULL,
    date_expiration DATETIME NOT NULL,
    est_utilise BOOLEAN DEFAULT FALSE,
    INDEX idx_otp_telephone_utilise_exp (telephone, est_utilise, date_expiration)
);

-- 9. Table des Compétences détaillées par travailleur
CREATE TABLE IF NOT EXISTS competence (
    id_competence INT AUTO_INCREMENT PRIMARY KEY,
    nom_competence VARCHAR(100) NOT NULL,
    description TEXT,
    id_travailleur INT NOT NULL,
    FOREIGN KEY (id_travailleur) REFERENCES travailleur(id_candidat) ON DELETE CASCADE,
    INDEX idx_competence_nom (nom_competence)
);

-- 10. Table des Disponibilités saisonnières des travailleurs
CREATE TABLE IF NOT EXISTS disponibilite (
    id_disponibilite INT AUTO_INCREMENT PRIMARY KEY,
    id_travailleur INT NOT NULL,
    date_debut DATE NOT NULL,
    date_fin DATE NOT NULL,
    statut VARCHAR(30) DEFAULT 'DISPONIBLE',
    FOREIGN KEY (id_travailleur) REFERENCES travailleur(id_candidat) ON DELETE CASCADE,
    INDEX idx_disponibilite_travailleur_dates (id_travailleur, date_debut, date_fin),
    INDEX idx_disponibilite_statut (statut)
);


-- 1. Vérifier la présence des 10 tables
SHOW TABLES;

-- 2. Vérifier les index sur les tables principales
SHOW INDEX FROM travailleur;
SHOW INDEX FROM demande_personnel;
SHOW INDEX FROM code_otp;

-- 3. Vérifier l'interrogation des statistiques régionales (RegionDAO.getStatistiquesParRegion)
INSERT IGNORE INTO region (nom_region) VALUES ('Souss-Massa');

SELECT r.id_region, r.nom_region,
       COUNT(DISTINCT t.id_candidat) AS total_travailleurs,
       COUNT(DISTINCT CASE WHEN UPPER(TRIM(t.disponibilite)) = 'DISPONIBLE' THEN t.id_candidat END) AS disponibles,
       COUNT(DISTINCT CASE WHEN UPPER(TRIM(d.statut)) = 'OUVERTE' THEN d.id_mission END) AS missions_ouvertes
FROM region r
LEFT JOIN travailleur t ON t.id_region = r.id_region
LEFT JOIN demande_personnel d ON LOWER(TRIM(d.lieu)) LIKE CONCAT('%', LOWER(TRIM(r.nom_region)), '%')
GROUP BY r.id_region, r.nom_region
ORDER BY r.nom_region;