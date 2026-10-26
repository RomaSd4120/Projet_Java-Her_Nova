# 🌾 AgriFlex - Plateforme de Recrutement & Rotation de Main-d'œuvre Agricole
> **Projet de Conception UI/UX & Architecture Système — Groupe KIBA**

---

## 📌 Présentation du Projet
**AgriFlex** est une solution digitale binationale (Espace Candidat & Back-Office KIBA) conçue pour structurer, accélérer et optimiser la gestion de la main-d'œuvre agricole non qualifiée au Maroc. La plateforme répond aux enjeux de pénurie saisonnière et de gestion des rotations d'ouvriers entre les grandes régions agricoles (Souss-Massa, Gharb, Oriental, etc.).

---

## 🎯 Architecture & Cas d'Utilisation

Le système repose sur deux piliers ergonomiques adaptés aux profils utilisateurs :

### 1. 📱 Espace Candidat (Ouvriers Agricoles)
Conçu pour une accessibilité maximale et une prise en main ultra-simple sans prérequis techniques :
* **Connexion sécurisée & simplifiée :** Authentification renforcée combinant le numéro de CINE (Carte d'Identité Nationale) et un numéro de téléphone avec vérification par code OTP (sans mot de passe complexe).
* **Sélection visuelle par Icônes :** Choix des compétences (Récolte de tomates 🍅, Cueillette des agrumes 🍊, Conduite de tracteur 🚜) via des cartes illustrées sans saisie textuelle obligatoire.
* **Profil Audio / Note Vocale (🎙️) :** Intégration d'un bouton d'enregistrement vocal permettant aux ouvriers non-lettrés de présenter leur parcours oralement.
* **Formulaire Hybride :** Champs texte classiques disponibles pour les candidats lettrés.
* **Gestion du statut :** Mise à jour instantanée de la disponibilité ("Disponible" / "Indisponible") et choix de la région actuelle.
* **Offres de mission :** Consultation et réponse en 1-clic ("Accepter" / "Refuser") aux propositions de travail.

### 2. 🖥️ Back-Office KIBA (Recruteurs & Gestionnaires)
Un tableau de bord puissant pour le pilotage opérationnel en temps réel :
* **Dashboard Analytics :** Suivi des KPIs (Ouvriers inscrits, disponibles, missions en cours, taux de réaffectation).
* **Cartographie Régionale :** Visualisation de la répartition géographique de la main-d'œuvre.
* **Matching Algorithmique :** Filtrage instantané des candidats selon les critères des exploitations (localisation, disponibilité, compétences).
* **Alertes Multi-canaux :** Déclenchement automatique des campagnes de recrutement par SMS, WhatsApp ou appel téléphonique.
* **Planning Saisonnier :** Vue Gantt des récoltes pour planifier les rotations d'ouvriers à l'échelle nationale.

---

## 🎨 Prototypes & Maquettes UI (Figma)

Les maquettes ont été conçues selon une charte graphique professionnelle dédiée au secteur agricole :
* **Palette de couleurs :** Vert Agriculture (`#2E7D32`), Slate Foncé (`#1E293B`), Fond Neutre (`#F8FAFC`).
* **Standardisation :** Format Desktop 1440x1024 pour l'ensemble des écrans.

### 🖥️ Aperçu des Écrans Principaux :
1. **Frame 1 — Connexion & OTP Candidat :** Interface d'accès rapide par téléphone.
2. **Frame 2 — Espace Candidat :** Interface de disponibilité et propositions reçues.
3. **Frame 3 — Dashboard KIBA Back-Office :** Vue d'ensemble opérationnelle et statistiques régionales.
4. **Frame 4 — Matching & Création de Mission :** Module d'affectation intelligente et alerte SMS/WhatsApp.
5. **Frame 5 — Planning & Vue Saisonnière :** Calendrier de rotation des équipes par culture.

🔗 **[Cliquez ici pour consulter le projet interactif sur Figma]**

---

## 📂 Structure des Livrables de l'Itération 1

```text
├── 📄 Cahier_des_Charges_AgriFlex.pdf     # Périmètre du projet et spécifications
├── 📄 Exigences_Fonctionnelles.pdf         # Exigences fonctionnelles et non-fonctionnelles
├── 📐 Diagrammes_UML/
│   ├── Diagramme_Cas_Utilisation.png       # Interactions Utilisateurs - Système
│   └── Diagramme_de_Classes.png            # Modélisation des entités et relations
├── 🎨 Maquettes_UI_Figma/
│   ├── Frame1_Connexion.png
│   ├── Frame2_Espace_Candidat.png
│   ├── Frame3_Dashboard_KIBA.png
│   ├── Frame4_Matching_Alertes.png
│   └── Frame5_Planning_Saisonnier.png
└── 📝 README.md                            # Synthèse et documentation du projet

# Projet_Java-Her_Nova

**Lien du repository GitHub : https://github.com/RomaSd4120/Projet_Java-Her_Nova
