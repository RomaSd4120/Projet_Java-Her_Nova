# 🌾 AgriFlex - Plateforme de Recrutement & Rotation de Main-d'œuvre Agricole
> **Projet de Conception UI/UX & Architecture Système — Groupe KIBA**

---

## 📌 Présentation du Projet
**AgriFlex** est une solution digitale binationale (Espace Candidat & Back-Office KIBA) conçue pour structurer, accélérer et optimiser la gestion de la main-d'œuvre agricole non qualifiée au Maroc. La plateforme répond aux enjeux de pénurie saisonnière et de gestion des rotations d'ouvriers entre les grandes régions agricoles (Souss-Massa, Gharb, Oriental, etc.).

* 📘 **Cahier des Charges :** [AgriFlex_Cahier de charge.pdf](./AgriFlex_Cahier%20de%20charge.pdf)

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

* 🏗️ **Exigences & Architecture :** [AgriFlex_Exigences_Architecture.pdf](./AgriFlex_Exigences_Architecture.pdf)

---

## 🎨 Prototypes & Maquettes UI (Figma)

Les maquettes ont été conçues selon une charte graphique professionnelle dédiée au secteur agricole :
* **Palette de couleurs :** Vert Agriculture (`#2E7D32`), Slate Foncé (`#1E293B`), Fond Neutre (`#F8FAFC`).
* **Standardisation :** Format Desktop 1440x1024 pour l'ensemble des écrans.

### 🖥️ Aperçu des Écrans Principaux :
1. **Frame 1 — Page d'Accueil (Landing Page) :** Présentation générale de la plateforme, opportunités agricoles et bouton d'accès rapide.
2. **Frame 2 — Connexion & OTP Candidat :** Interface d'accès rapide par téléphone.
3. **Frame 3 — Espace Candidat :** Interface de disponibilité et propositions reçues.
4. **Frame 4 — Dashboard KIBA Back-Office :** Vue d'ensemble opérationnelle et statistiques régionales.
5. **Frame 5 — Matching & Création de Mission :** Module d'affectation intelligente et alerte SMS/WhatsApp.
6. **Frame 6 — Planning & Vue Saisonnière :** Calendrier de rotation des équipes par culture.

## 🎨 Prototype & UI Design (Figma)

- **🔗 Lien du Prototype Interactif :** [Cliquez ici pour tester la maquette Figma]
- https://www.figma.com/design/gRzqx04jBctT5In5F3SNiH/Sans-titre?node-id=0-1&t=9wIYUNYjMCLGak5F-1
* 🎨 **Charte & Cohérence UI/UX :** [AgriFlex_Cohérence et UXUI.pdf](./AgriFlex_Coh%C3%A9rence%20et%20UXUI.pdf)


---

## 📂 Structure des Livrables de l'Itération 1

```text
├── 📄 Cahier_des_Charges_AgriFlex.pdf     # Périmètre du projet et spécifications
├── 📄 Exigences_Fonctionnelles.pdf         # Exigences fonctionnelles et non-fonctionnelles
├── 📐 Diagrammes_UML/
│   ├── Diagramme_Cas_Utilisation.png       # Interactions Utilisateurs - Système
│   └── Diagramme_de_Classes.png            # Modélisation des entités et relations
├── 🎨 Maquettes_UI_Figma/
│   ├── Frame1_Accueil.png
│   ├── Frame2_Connexion.png
│   ├── Frame3_Espace_Candidat.png
│   ├── Frame4_Dashboard_KIBA.png
│   ├── Frame5_Matching_Alertes.png
│   └── Frame6_Planning_Saisonnier.png
└── 📝 README.md
```

---

# Projet_Java-Her_Nova

**Lien du repository GitHub :** [Projet Java-Her_Nova](https://github.com/RomaSd4120/Projet_Java-Her_Nova)
