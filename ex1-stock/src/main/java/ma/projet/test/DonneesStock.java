package ma.projet.test;

import ma.projet.classes.Categorie;
import ma.projet.classes.Commande;
import ma.projet.classes.LigneCommande;
import ma.projet.classes.Produit;
import ma.projet.service.CategorieService;
import ma.projet.service.CommandeService;
import ma.projet.service.LigneCommandeService;
import ma.projet.service.ProduitService;
import ma.projet.util.Format;
import ma.projet.util.MaintenanceBase;

/**
 * Jeu de donnees personnel du magasin "Atlas Informatique".
 * Les tables sont videes puis remplies a chaque execution : les identifiants
 * affiches dans le compte rendu restent donc stables.
 */
public final class DonneesStock {

    private DonneesStock() {
    }

    public static void charger() {
        MaintenanceBase.viderEtReinitialiser("ligne_commande", "commande", "produit", "categorie");

        CategorieService categories = new CategorieService();
        ProduitService produits = new ProduitService();
        CommandeService commandes = new CommandeService();
        LigneCommandeService lignes = new LigneCommandeService();

        Categorie portables = new Categorie("Ordinateurs portables");
        Categorie imprimantes = new Categorie("Imprimantes");
        Categorie reseau = new Categorie("Reseau et connectique");
        categories.create(portables);
        categories.create(imprimantes);
        categories.create(reseau);

        Produit lpA54 = new Produit("LP-A54", 8900, portables);
        Produit lpB17 = new Produit("LP-B17", 12400, portables);
        Produit imJet200 = new Produit("IM-JET200", 1250, imprimantes);
        Produit imLas450 = new Produit("IM-LAS450", 3100, imprimantes);
        Produit sw8P = new Produit("SW-8P", 640, reseau);
        Produit rtAx3000 = new Produit("RT-AX3000", 1890, reseau);
        produits.create(lpA54);
        produits.create(lpB17);
        produits.create(imJet200);
        produits.create(imLas450);
        produits.create(sw8P);
        produits.create(rtAx3000);

        Commande premiere = new Commande(Format.date(12, 9, 2025));
        Commande deuxieme = new Commande(Format.date(3, 11, 2025));
        Commande troisieme = new Commande(Format.date(18, 2, 2026));
        Commande quatrieme = new Commande(Format.date(7, 5, 2026));
        commandes.create(premiere);
        commandes.create(deuxieme);
        commandes.create(troisieme);
        commandes.create(quatrieme);

        lignes.create(new LigneCommande(lpA54, premiere, 3));
        lignes.create(new LigneCommande(imJet200, premiere, 2));

        lignes.create(new LigneCommande(sw8P, deuxieme, 10));
        lignes.create(new LigneCommande(rtAx3000, deuxieme, 2));

        lignes.create(new LigneCommande(lpB17, troisieme, 2));
        lignes.create(new LigneCommande(imLas450, troisieme, 1));
        lignes.create(new LigneCommande(rtAx3000, troisieme, 1));

        lignes.create(new LigneCommande(lpA54, quatrieme, 6));
        lignes.create(new LigneCommande(imJet200, quatrieme, 4));
        lignes.create(new LigneCommande(sw8P, quatrieme, 12));
    }
}
