package ma.projet.test;

import java.util.Date;
import java.util.List;

import ma.projet.classes.Categorie;
import ma.projet.classes.Commande;
import ma.projet.classes.LigneCommande;
import ma.projet.classes.Produit;
import ma.projet.service.CategorieService;
import ma.projet.service.CommandeService;
import ma.projet.service.LigneCommandeService;
import ma.projet.service.ProduitService;
import ma.projet.util.Entete;
import ma.projet.util.Format;
import ma.projet.util.HibernateUtil;
import ma.projet.util.TableConsole;

/**
 * Programme de test de l'exercice 1 : gestion du stock d'un magasin de produits
 * informatiques.
 */
public final class TestStock {

    private static final int ETAPES = 6;

    private TestStock() {
    }

    public static void main(String[] args) {
        Entete.ouvrir("EXERCICE 1", "Gestion du stock d'un magasin de produits informatiques",
                "Magasin \"Atlas Informatique\" - Casablanca");

        try {
            DonneesStock.charger();

            ProduitService produits = new ProduitService();
            CategorieService categories = new CategorieService();
            CommandeService commandes = new CommandeService();
            LigneCommandeService lignes = new LigneCommandeService();

            produitsParCategorie(produits, categories);
            produitsCommandesEntreDates(produits);
            produitsDuneCommande(produits, commandes);
            produitsPrixSuperieurA(produits);
            controleCrud(categories, produits);
            synthese(categories, produits, commandes, lignes);
        } finally {
            HibernateUtil.fermer();
        }
    }

    private static void produitsParCategorie(ProduitService produits, CategorieService categories) {
        Entete.etape(1, ETAPES, "Produits d'une categorie (requete nommee Produit.parCategorie)");

        Categorie imprimantes = categories.parNom("Imprimantes");
        List<Produit> liste = produits.produitsParCategorie(imprimantes);
        Entete.commentaire("Categorie : " + imprimantes.getNom() + "  (" + liste.size() + " produits)");
        System.out.println();

        TableConsole table = new TableConsole(
                new String[]{"Reference", "Prix (DH)", "Categorie"},
                new boolean[]{false, true, false});
        for (Produit produit : liste) {
            table.ligne(produit.getReference(), Format.montant(produit.getPrix()),
                    produit.getCategorie().getNom());
        }
        table.afficher();
    }

    private static void produitsCommandesEntreDates(ProduitService produits) {
        Entete.etape(2, ETAPES, "Produits commandes entre deux dates");

        Date debut = Format.date(1, 1, 2026);
        Date fin = Format.date(31, 12, 2026);
        List<LigneCommande> liste = produits.produitsCommandesEntre(debut, fin);
        Entete.commentaire("Periode : du " + Format.dateCourte(debut) + " au " + Format.dateCourte(fin)
                + "  (" + liste.size() + " lignes)");
        System.out.println();

        TableConsole table = new TableConsole(
                new String[]{"Commande", "Date", "Reference", "Prix (DH)", "Quantite", "Total (DH)"},
                new boolean[]{true, false, false, true, true, true});
        for (LigneCommande ligne : liste) {
            table.ligne(ligne.getCommande().getId(), Format.dateCourte(ligne.getCommande().getDate()),
                    ligne.getProduit().getReference(), Format.montant(ligne.getProduit().getPrix()),
                    ligne.getQuantite(), Format.montant(ligne.getTotal()));
        }
        table.afficher();
    }

    private static void produitsDuneCommande(ProduitService produits, CommandeService commandes) {
        Entete.etape(3, ETAPES, "Produits d'une commande donnee");

        Commande commande = commandes.findById(3);
        List<LigneCommande> liste = produits.produitsDuneCommande(commande);
        Entete.commentaire("Commande : " + commande.getId()
                + "    Date : " + Format.dateLongue(commande.getDate()));
        Entete.commentaire("Liste des produits :");
        System.out.println();

        TableConsole table = new TableConsole(
                new String[]{"Reference", "Prix (DH)", "Quantite"},
                new boolean[]{false, true, true});
        for (LigneCommande ligne : liste) {
            table.ligne(ligne.getProduit().getReference(), Format.montant(ligne.getProduit().getPrix()),
                    ligne.getQuantite());
        }
        table.afficher();
    }

    private static void produitsPrixSuperieurA(ProduitService produits) {
        Entete.etape(4, ETAPES, "Produits dont le prix depasse 100 DH (requete nommee Produit.prixSuperieurA)");

        List<Produit> liste = produits.produitsPrixSuperieurA(100);
        Entete.commentaire(liste.size() + " produits au-dessus du seuil de 100 DH");
        System.out.println();

        TableConsole table = new TableConsole(
                new String[]{"Reference", "Prix (DH)", "Categorie"},
                new boolean[]{false, true, false});
        for (Produit produit : liste) {
            table.ligne(produit.getReference(), Format.montant(produit.getPrix()),
                    produit.getCategorie().getNom());
        }
        table.afficher();
    }

    private static void controleCrud(CategorieService categories, ProduitService produits) {
        Entete.etape(5, ETAPES, "Controle des operations de l'interface IDao");
        Entete.commentaire("Produit CTRL-00 cree, modifie (99,5 -> 150 DH) puis supprime");
        System.out.println();
        Categorie temporaire = new Categorie("Categorie de controle");
        categories.create(temporaire);

        Produit produit = new Produit("CTRL-00", 99.5, temporaire);
        produits.create(produit);

        produit.setPrix(150);
        produits.update(produit);
        Produit relu = produits.findById(produit.getId());
        boolean modification = relu != null && relu.getPrix() == 150;

        produits.delete(relu);
        boolean suppressionProduit = produits.findById(produit.getId()) == null;

        categories.delete(temporaire);
        boolean suppressionCategorie = categories.findById(temporaire.getId()) == null;

        System.out.println("  create / update / findById");
        Entete.verification("creation, modification et relecture du produit ...", modification);
        Entete.verification("suppression du produit ...", suppressionProduit);
        Entete.verification("suppression de la categorie ...", suppressionCategorie);
        Entete.commentaire("Chaque operation a ete validee, la base est restauree ("
                + produits.findAll().size() + " produits)");
    }

    private static void synthese(CategorieService categories, ProduitService produits,
            CommandeService commandes, LigneCommandeService lignes) {
        Entete.etape(6, ETAPES, "Synthese du magasin");

        int articlesVendus = 0;
        double chiffreAffaires = 0;
        for (LigneCommande ligne : lignes.findAll()) {
            articlesVendus += ligne.getQuantite();
            chiffreAffaires += ligne.getTotal();
        }

        TableConsole table = new TableConsole(
                new String[]{"Indicateur", "Valeur"},
                new boolean[]{false, true});
        table.ligne("Categories enregistrees", categories.findAll().size());
        table.ligne("Produits au catalogue", produits.findAll().size());
        table.ligne("Commandes passees", commandes.findAll().size());
        table.ligne("Articles vendus", articlesVendus);
        table.ligne("Chiffre d'affaires", Format.montant(chiffreAffaires) + " DH");
        table.afficher();

        Entete.fermer("Exercice 1 termine - " + ETAPES + " etapes validees");
    }
}
