import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * Classe permettant l'affichage dans une fenetre de la grille et des points
 */
public class Ui extends JFrame implements Observer {
    private JFrame f;
    private Dessin p;
    private static Ui instance=null;

    public static Ui Instance(){
        if (instance == null){
            instance = new Ui();
            return instance;
        }else{
            return instance;
        }
    }
    public Ui() {
        f = new JFrame("Serious Game");
        p = new Dessin();
        p.setBackground(Color.white);
        f.add(p);

        f.setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }

    public void update(Observable observable, Object o) {
        p.addAndrefresh((User) o);
    }

    public void printTrail(ArrayList<User> l){
        p.addAndrefresh(l);
    }

    public void setDepFin(User dep, User fin) {
        p.setDepFin(dep,fin);
    }
    public void selection(User dep) {
        p.selection(dep);
    }
}
