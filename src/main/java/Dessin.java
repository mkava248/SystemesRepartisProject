import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Classe d'affichage et de gestion de clique lors du jeu Utilisateur contre Ordinateur
 */
public class Dessin extends JPanel {

    private ArrayList<User> users;
    private int pad = 20;
    private double maxX=1;
    private double maxY=1;
    private ArrayList<User> trail =null; //Liste de points faisant du trait du chemin fait par l'utilisateur
    private ArrayList<User> trail2 = null; //Liste de points faisant du trait du chemin fait par A*
    private User dep=null;
    private User fin=null;
    private User curSelection =null;

    private ArrayList<User> u = new ArrayList<>();

    /**
     * Constructeur
     */
    public Dessin() {
        super();
        users = new ArrayList<>();
        setBackground(Color.darkGray);
        //Évènement d'écoute quand on clique dans la fenetre
        this.addMouseListener(new MouseAdapter(){
            public void mousePressed(MouseEvent e){
                //Coordonnées pour savoir où on a cliquer
                int x = e.getX();
                int y = e.getY();
                getUser(x, y);
                //Cela veut dire que c'est la fin
                //Fait l'affichage du score dans la console
                if(curSelection.equals(fin)){
                    System.out.println("Fini");
                    int score = 0;
                    for(int i = 0; i < trail.size()-1; i++){
                        score += trail.get(i).getPosition().distance(trail.get(i+1).getPosition());
                    }
                    System.out.println("Vous avez un score final : " + score);
                    trail2 = trail;
                    Search s = new Search();
                    ArrayList<User> us = s.executeColor(dep,fin);
                    int scoreAsta = 0;
                    for(int i = 0; i < us.size()-1; i++){
                        scoreAsta += us.get(i).getPosition().distance(us.get(i+1).getPosition());
                    }
                    paintComponent(getGraphics());
                    System.out.println("Astar a un score de : " + scoreAsta);
                    if(scoreAsta < score) System.out.println("Astar a gagné");
                    if(scoreAsta > score) System.out.println("Felicitation vous avez gagné"); // Impossible
                    if(scoreAsta == score) System.out.println("Bravo, vous avez trouvé le meilleur chemin");
                    //Permet de faire afficher les résultats pendant 2 secondes dans la fenetre
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException ie) {
                        ie.printStackTrace();
                    }
                    System.exit(0);
                }
            }
        });
    }

    /**
     * Pour faire l'affichage de tous les composants dans la fenetre
     *
     * @param g (Graphic)
     */
    public void paintComponent(Graphics g) {
        g.setColor(Color.darkGray);
        g.fillRect (0, 0, getWidth(), getHeight());
        for (int i = 0; i< users.size(); i++) {
            drawnode(g, users.get(i),Color.white);
        }
        if(trail2 != null){
            for(int i = 0; i< trail2.size()-1; i++){
                drawTrail2(g, trail2.get(i), trail2.get(i+1));
            }
        }
        if(trail != null){
            for(int i = 0; i< trail.size()-1; i++){
                drawTrail(g, trail.get(i), trail.get(i+1));


            }
        }
        if(curSelection!=null){
            drawnode(g,curSelection,Color.red);
            for(User u :curSelection.getNeighbours()){
                drawnode(g,u,Color.yellow);

            }
        }
        if(dep!=null){
            drawnode(g,dep,Color.blue);
            drawnode(g,fin,Color.green);
        }
    }

    /**
     * Permet de faire l'écriture de trait en noir entre deux points
     * Utiliser pour le mode automatique et l'ecriture de la solution A*
     *
     * @param g (Graphic)
     * @param user (User) (Source)
     * @param user1 (User) (Destination)
     */
    private void drawTrail(Graphics g, User user, User user1) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(4));
        g2.drawLine((int) getPixel(user).getX()+4,(int) getPixel(user).getY()+4,
                (int) getPixel(user1).getX()+4,(int) getPixel(user1).getY()+4);

    }

    /**
     * Permet de faire l'écriture de trait en rouge entre deux points
     * Utiliser pour faire le tracer du chemin de l'utilisateur où il c'est trompé
     *
     * @param g (Graphic)
     * @param user (User) (Source)
     * @param user1 (User) (Destination)
     */
    private void drawTrail2(Graphics g, User user, User user1) {
        g.setColor(Color.red);
        g.drawLine((int) getPixel(user).getX()+4,(int) getPixel(user).getY()+4,
                (int) getPixel(user1).getX()+4,(int) getPixel(user1).getY()+4);
        g.setColor(Color.black);
    }

    /**
     * Va changer la couleur d'un point dans la couleur choisi
     * @param g (Graphic)
     * @param n (User)
     * @param color (Color)
     */
    private void drawnode(Graphics g, User n, Color color) {
        double posX = getPixel(n).getX();
        double posY = getPixel(n).getY();
        g.setColor(color);
        g.fillOval( (int)posX, (int )posY, 8, 8);
        g.setColor(Color.red);
        g.setColor(Color.black);

    }

    /**
     * Permet de savoir les coordonnées d'un point en particulier
     * @param n (User)
     * @return (Coordinate)
     */
    private Coordinate getPixel(User n ){
        double posX = (getSize().width - 4 * pad) * n.getPosition().getX() / (maxX);
        double posY = (getSize().height - 4 * pad) * n.getPosition().getY() / (maxY);
        return  new Coordinate(pad+ posX,pad +posY);

    }

    /**
     * Va choisir l'utilisateur le plus proche de la où on a cliquer dans les voisins du points sélectionné
     * @param x (int)
     * @param y (int)
     */
    private void getUser(int x, int y){
        double xUser = (x - pad) * maxX / (getSize().width - 4 * pad);
        double yUser = (y - pad) * maxY / (getSize().height - 4 * pad);

        double distance = 9999;
        User user =  new User("", new Coordinate(xUser, yUser));

        for (User u : users) {
            double tempDist = u.distance(new User("", new Coordinate(xUser, yUser)));
            if(tempDist < distance){
                distance = tempDist;
                user = u;
            }
        }
        //Appuie sur le dernier user
        if(trail != null && trail.get(trail.size()-1).equals(user)){
            ArrayList<User> temp = trail;
            trail = null;
            paintComponent(getGraphics());

            trail = temp;

            trail.remove(user);

            if(trail.size() == 0){
                trail.add(dep);
            }
            User u1 = trail.get(trail.size()-1);
            curSelection = u1;

            u.clear();
            for (User user1 : u1.getNeighbours()) {
                u.add(user1);
            }
        }
        //Appuie sur un autre user
        else{
            boolean b = false;
            User bon = new User("", new Coordinate(-1, -1));
            for (User u : u) {
                double tempDist = u.distance(user);
                if(tempDist < distance){
                    distance = tempDist;
                    bon = u;
                    b = true;
                }
            }
            if(b){
                curSelection = bon;

                u.clear();
                for (User user1 : curSelection.getNeighbours()) {
                    u.add(user1);
                }
                trail.add(curSelection);
            }
        }
        paintComponent(getGraphics());
    }

    /**
     * Ajout d'un nouvel utilisateur et refresh la fenetre
     * @param n
     */
    public void addAndrefresh(User n) {
        users.add(n);
        maxX = Collections.max(users, Comparator.comparing(a -> a.getPosition().getX())).getPosition().getX();
        maxY = Collections.max(users, Comparator.comparing(a -> a.getPosition().getY())).getPosition().getY();
        repaint();

    }

    /**
     * Ajout et affichage d'un trait entre deux points
     * @param l (ArrayList<User>) //Les deux points du trait
     */
    public void addAndrefresh(ArrayList<User> l) {
        trail = l;
        repaint();
    }

    /**
     * Initialisation du point de départ et d'arrivé
     *
     * @param pdep (User) //Départ
     * @param pfin (User) //Arrivé
     */
    public void setDepFin(User pdep, User pfin) {
        dep = pdep;
        fin= pfin;
        for (User user : dep.getNeighbours()) {
            u.add(user);
        }
        trail = new ArrayList<>();
        trail.add(dep);
        curSelection = dep;
        paintComponent(getGraphics());
    }

    /**
     * Permet de définir le point de départ
     *
     * @param pdep (User)
     */
    public void selection(User pdep) {
        curSelection = pdep;
    }
}
