import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import static java.util.Objects.hash;

/**
 * Classe des utilisateur.
 * Permet de mettre en place Observeur/Observable
 */
public class User implements Observer {

    public Coordinate getPosition() {
        return position;
    }

    private Coordinate position;
    private ArrayList<User> neighbours;
    private String name;
    private static double distanceMax = 5;

    public String getName() {
        return name;
    }

    /**
     * Change la distance max pour la détection des voisins
     *
     * @param distanceMax
     */
    public static void setDistanceMax(double distanceMax) {
        User.distanceMax = distanceMax;
    }

    public ArrayList<User> getNeighbours() {
        return neighbours;
    }

    @Override
    public String toString(){
        return ""+position.toString();
    }


    public User(String name, Coordinate p) {
        this.name = name;
        neighbours = new ArrayList<User>();
        position = p;

    }

    //Appelé lorsque l'observable (Graph) notifie ses abonnés
    public void update(Observable observable, Object o) {
        User n = (User) o;
        if (this.distance(n) < distanceMax && !n.equals(this)) {
            if (!neighbours.contains(n))
                neighbours.add(n);

        } else {
            neighbours.remove(n);
        }

    }


    public void computeNeighboursAtfirst(ArrayList<User> map) {
        neighbours = new ArrayList<>();
        for (User n : map) {
            if (this.distance(n) < distanceMax && !this.equals(n)) {
                neighbours.add(n);
            }
        }
    }

    public User nearestNode(ArrayList<User> users) {
        User res = null;
        double distanceMini = 1000;
        for (User n : users) {
            if (n.distance(this) < distanceMini) {
                distanceMini = n.distance(this);
                res = n;
            }
        }
        return res;
    }


    public double distance(User n) {
        return n.position.distance(position);
    }

    /**
     * point voisins du point "but"
     * @param but
     * @param map
     * @return
     */
    public ArrayList<User> usersAround(User but, ArrayList<User> map){
        double dist = distance(but);
        ArrayList<User> l = new ArrayList<>();
        for (User u : map){
            if(u.distance(but)<dist){
                l.add(u);
            }
        }
        return  l;
    }

    /**
     * Point non-voisin du point en question
     * @param but
     * @param map
     * @return
     */
    public ArrayList<User> usersNOTAround(User but, ArrayList<User> map){
        double dist = distance(but);
        ArrayList<User> l = new ArrayList<>();
        for (User u : map){
            if(u.distance(but)>dist){
                l.add(u);
            }
        }
        return  l;
    }

    /**
     * Déplace le point sur lequel la méthode est appelé au milieu des deux points passés en param
     * @param inCircle
     * @param outCircle
     */
    public void moveBetween(User inCircle, User outCircle) {
        double x= (inCircle.getPosition().getX() + outCircle.getPosition().getX())/2;
        double y= (inCircle.getPosition().getY() + outCircle.getPosition().getY())/2;

        position.set(x,y);
    }

    @Override public boolean equals(Object other) {
        boolean result = false;
        if (other instanceof User) {
            User that = (User) other;
            result = (this.getPosition().equals(that.getPosition()));
        }
        return result;
    }

    @Override
    public int hashCode()
    {
        return hash(name);
    }
}
