import java.util.Random;
import java.util.Scanner;

/**
 * Classe main
 */
public class Main {

    /**
     * Méthode principale
     * @param args
     */
    public static void main(String[] args) {
        int size = 50; //Taille du graphe
        User.setDistanceMax(5); //Distance de détection des voisins

        Graph graph = new Graph(size);
        Random r = new Random();

        //Nombre de points affichés dans la fenetre
        for (int i = 0; i < 80; i++) {

            User temp = new User(Integer.toString(i), new Coordinate(r.nextInt(size), r.nextInt(size)));
            graph.addNode(temp);
        }
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int i;
        do {
            System.out.println("Merci de renseigner le mode de jeu:\n1.Jouer contre l'IA\n2.Recherche automatique de trajet et déplacement de point\n");
            Scanner sc = new Scanner(System.in);
            i = Integer.parseInt(sc.nextLine());
        }while(i <1 || i>2);
        if (i == 1){

            //Ligne permettant de lancer le jeu Utilisateur contre Ordinateur.
            //Il faut la commenter si on veut lancer le mode automatique
            graph.game();
        }
        else{
            while (true) {

                //Ligne permettant de lancer le mode automatique de A*
                //Il faut la commenter si on veut lancer le mode jeu Utilisateur contre Ordinateur
                graph.sendMessage();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }


}
