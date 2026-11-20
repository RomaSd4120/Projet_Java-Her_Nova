-- Création de la base de données

CREATE DATABASE IF NOT EXISTS agriflex_db
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE agriflex_db;

-- 1. Table des Utilisateurs (Authentification et Rôles)
CREATE TABLE IF NOT EXISTS utilisateur (
    id_utilisateur INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(50) NOT NULL,
    prenom VARCHAR(50) NOT NULL,
    cin VARCHAR(30) UNIQUE,
    telephone VARCHAR(20) UNIQUE NOT NULL,
    mot_de_passe_hashe VARCHAR(255) NOT NULL,
    role VARCHAR(30) NOT NULL
);

-- 2. Table des Régions (Matching géographique)
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
    FOREIGN KEY (id_region) REFERENCES region(id_region) ON DELETE SET NULL
);

-- 4. Table des Entreprises Agricoles
CREATE TABLE IF NOT EXISTS entreprise_agricole (
    id_entreprise INT AUTO_INCREMENT PRIMARY KEY,
    raison_sociale VARCHAR(150) NOT NULL,
    contact VARCHAR(100),
    telephone VARCHAR(20),
    region VARCHAR(100),
    sites TEXT,
    statut VARCHAR(30) DEFAULT 'ACTIF'
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
    FOREIGN KEY (id_entreprise) REFERENCES entreprise_agricole(id_entreprise) ON DELETE CASCADE
);

-- 6. Table des Affectations (Lien entre Candidats et Missions)
CREATE TABLE IF NOT EXISTS affectation (
    id_affectation INT AUTO_INCREMENT PRIMARY KEY,
    id_candidat INT NOT NULL,
    id_mission INT NOT NULL,
    date_proposition DATE,
    statut VARCHAR(30) DEFAULT 'PROPOSEE',
    FOREIGN KEY (id_candidat) REFERENCES travailleur(id_candidat) ON DELETE CASCADE,
    FOREIGN KEY (id_mission) REFERENCES demande_personnel(id_mission) ON DELETE CASCADE
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
    FOREIGN KEY (id_mission) REFERENCES demande_personnel(id_mission) ON DELETE SET NULL
);

-- 8. Table des Codes OTP (Vérification à usage unique par téléphone)
CREATE TABLE IF NOT EXISTS code_otp (
    id_otp INT AUTO_INCREMENT PRIMARY KEY,
    telephone VARCHAR(20) NOT NULL,
    code VARCHAR(10) NOT NULL,
    date_expiration DATETIME NOT NULL,
    est_utilise BOOLEAN DEFAULT FALSE
);

-- 9. Table des Compétences détaillées par travailleur
CREATE TABLE IF NOT EXISTS competence (
    id_competence INT AUTO_INCREMENT PRIMARY KEY,
    nom_competence VARCHAR(100) NOT NULL,
    description TEXT,
    id_travailleur INT NOT NULL,
    FOREIGN KEY (id_travailleur) REFERENCES travailleur(id_candidat) ON DELETE CASCADE
);

-- 10. Table des Disponibilités saisonnières des travailleurs
CREATE TABLE IF NOT EXISTS disponibilite (
    id_disponibilite INT AUTO_INCREMENT PRIMARY KEY,
    id_travailleur INT NOT NULL,
    date_debut DATE NOT NULL,
    date_fin DATE NOT NULL,
    statut VARCHAR(30) DEFAULT 'DISPONIBLE',
    FOREIGN KEY (id_travailleur) REFERENCES travailleur(id_candidat) ON DELETE CASCADE
);