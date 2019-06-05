import java.util.ArrayList;
import java.util.Observable;
import java.util.Random;

/**
 * Classe correspondant à la grille où les utilisateurs sont représenter par des points
 */
public class Graph extends Observable {
    private ArrayList<User> map; //Liste des utilisateurs/points
    private int size; //Taille de la grille
    private Ui ui;

    private User dep; //Point de départ du circuit
    private User fin; //Point d'arrivé du circuit

    /**
     * Permet d'obtenir la liste contenant tous les utilisateurs
     *
     * @return map (ArrayList<User>)
     */
    public ArrayList<User> getMap() {
        return map;
    }

    /**
     * Constructeur de la classe Graph
     * @param size (int) //Correspond au nombre de ligne et de colonne dans la grille
     *             (Exemple : 50 veut dire qu'il y a une grille de 50*50)
     */
    public Graph(int size) {
        map = new ArrayList<User>();
        this.size = size;
        this.ui = Ui.Instance();
        this.addObserver(ui);
    }

    /**
     * Permet de savoir le nombre de ligne et de colonne présentent dans la grille
     * @return size (int)
     */
    public int getSize(){
        return size;
    }

    /**
     * Ajoute un utilisateur dans la liste
     * @param n (User)
     */
    public void addNode(User n) {
        boolean b = true;
        //Partie de vérification si l'utilisateur est déjà dans la liste
        for (User n1 : map) {
            if (n1.getPosition().equals(n.getPosition()))
                b = false;
        }
        if (b) {
            n.computeNeighboursAtfirst(map); //Initialisation de ses voisins
            setChanged();
            notifyObservers(n);
            this.addObserver(n);
            map.add(n);
        }
    }

    /**
     * Cette fonction a pour but d'envoyer un message d'un point A à un point B
     * en executant l'algorithme A* et déplace les points si besoin

     */
    public void sendMessage() {
        setPoint();
        Search s = new Search(); // recherche A* : nouvelle instance
        ArrayList<User> l = s.execute(dep, fin); // execution de la recherche
        while (l == null) { // si pas de chemin possible
            Ui.Instance().selection(s.getNearestNode().getUser()); // on récupère le point avec la meilleure
            // heuristique (point le plus proche du point but)
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            movepoint(s.getNearestNode(),fin); // on déplace le point en question

            s = new Search(); // on réinstantie
            l = s.execute(dep, fin);// et on relance la recherche
            Ui.Instance().printTrail(l); // on affiche le chemin

        }
            System.out.println("Chemin trouvé" );
        Ui.Instance().printTrail(l);
    }

    /**
     * Méthode lançant le jeu utilisateur contre ordinateur
     */
    public void game(){
        setPoint();
        Search s = new Search();
        ArrayList<User> l = s.execute(dep, fin);
        int i = 0;
        while(l == null ){
            setPoint();
            l = s.execute(dep, fin);
            i++;
            if(i > 10000){
                System.out.println("Aucun chemin trouvé dans le graphe actuel après " + i + " itérations.");
                System.out.println("Veillez relancer le programme.");
                System.out.println("Si le problème persiste, veillez réduire la taille du graphe dans la classe principale");
                System.out.println("ou bien augmenter le nombre de point présent ou le périmètre de détection de voisin.");
                System.out.println("Merci.");
                System.exit(-1);
            }
        }
    }

    /**
     * Cette fonction choisi aléatoirement deux points, ayant une distance d'au moins
     * size/4 (pour éviter d'avoir des trajets court et triviaux
     */
    private void setPoint(){
        Random r = new Random();
        int user1 = r.nextInt(map.size());
        int user2 = r.nextInt(map.size());
        while (user1 == user2) //Evite d'avoir un user sender et receiver
            user2 = r.nextInt(map.size());

        double distance = map.get(user1).getPosition().distance(map.get(user2).getPosition());

        while(distance < size/4){
            user1 = r.nextInt(map.size());
            user2 = r.nextInt(map.size());
            while (user1 == user2) //Evite d'avoir un user sender et receiver
                user2 = r.nextInt(map.size());
            distance = map.get(user1).getPosition().distance(map.get(user2).getPosition());
        }
        dep = map.get(user1);
        fin = map.get(user2);
        //System.out.println(dep + "-->" + fin);
        Ui.Instance().setDepFin(dep, fin);
    }

    /**
     * déplace le point vers le but
     * @param tomove le point à déplacer
     * @param fin le point "but"
     */
    private void movepoint(Node tomove, User fin) {
        User userToMove = tomove.getUser(); // on récupère l'user lié au point

        ArrayList<User> possibility = userToMove.usersAround(fin,map); // on regarde les point voisins autour du point "but"
        ArrayList<User> outpossibility = userToMove.usersNOTAround(fin,map); // de même pour les non-voisins du point "but"
        User inCircle = userToMove.nearestNode(possibility); // on recherche le point le plus proche dans les voisins
        User outCircle = userToMove.nearestNode(outpossibility); // on recherche le point le plus proche dans les non-voisins

        if (tomove.getFather()==null){ // lorsque c'est le premier point du chemin
            userToMove.moveBetween(inCircle,userToMove); // on déplace le point entre le voisin le plus proche et et lui même
            //on notifie les observers
            setChanged();
            notifyObservers(userToMove);

            userToMove.computeNeighboursAtfirst(map);// on recalcule les voisins du point déplacé

        }else{
            userToMove.moveBetween(inCircle,outCircle);// on déplace le point entre le voisin le plus proche et son non-voisin le plus proche
            //on notifie les observers
            setChanged();
            notifyObservers(userToMove);

            userToMove.computeNeighboursAtfirst(map); // on recalcule les voisins du point déplacé
        }
    }


}
