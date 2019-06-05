import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

/**
 * Classe permettant de connaitre les coordonnées d'un utilisateur dans la grille
 */
public class Coordinate {
    //Varaibles de coordonnées
    private double x;
    private double y;

    /**
     * Constructeur où les coordonnées sont initialisées
     * @param px (double)
     * @param py (double)
     */
    public Coordinate(double px, double py){
        x=px;
        y=py;
    }

    /**
     * Surcharge de la méthode equals pour savoir si une classe est égale à celle-ci
     * @param other (Object)
     * @return (boolean)
     */
    @Override
    public boolean equals(Object other) {
        boolean result = false;
        if (other instanceof Coordinate) {
            Coordinate that = (Coordinate) other;
            result = (this.getX() == that.getX() && this.getY() == that.getY());
        }
        return result;
    }

    /**
     * Hash identique pour deux points aux memes coordonnées
     * @return (int)
     */
    @Override
    public int hashCode()
    {
        return (int)(41 * (41 + getX()) + getY());
    }

    /**
     * Permet de savoir la distance entre deux points
     * @param c (Coordinate)
     * @return distance (double)
     */
    public double distance (Coordinate c){

        return sqrt(pow(x-c.getX(),2)+pow(y-c.getY(),2));

    }

    /**
     * Permet d'obtenir le x de la coordonnée
     * @return x (double)
     */
    public double getX() {
        return x;
    }

    /**
     * Permet d'obtenir le y de la coordonnée
     * @return y (double)
     */
    public double getY() {
        return y;
    }

    /**
     * Permet d'obtenir les coordonnées en String
     * @return (String)
     */
    @Override
    public String toString(){
        return ""+x +"-"+ y ;
    }

    /**
     * Pour changer le x et le y des coordonnées
     * @param x (double)
     * @param y (double)
     */
    public void set(double x, double y) {
        this.x = x;
        this.y = y;
    }
}
