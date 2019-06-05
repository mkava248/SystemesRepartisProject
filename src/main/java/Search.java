import java.util.ArrayList;

public class Search {
    private Coordinate but;
    private Node nearestNode;
    private double min = 1000;

    /**
     * recherche A* standard, à partir d'un point passé en paramètre
     *
     * @param node
     * @return null si pas de chemin, sinon le point d'arrivé ("but")
     */
    private Node search(Node node,boolean b) {
        ArrayList<Node> closedList = new ArrayList<>();
        ArrayList<Node> openList = new ArrayList<>();
        openList.add(node);

        while (!openList.isEmpty()) {
            Node curNode = selectNode(openList);
            if(b)
                Ui.Instance().printTrail(curNode.toTrail());

            openList.remove(curNode);
            closedList.add(curNode);
            if (curNode.getPos().equals(but)) {
                return curNode;
            }else if(curNode.getPos().distance(but)<min){
                nearestNode = curNode;
                min = curNode.getPos().distance(but);
            }else if (curNode.deep() > 80) {
                return null;
            }

            for (Node nNode : curNode.nextNode()) {

                if (!nNode.isIn(openList)) {
                    openList.add(nNode);
                } else if (nNode.heuristic(but) < curNode.heuristic(but)) {
                    curNode = nNode;
                }
            }
        }
        return null;
    }


    /**
     * Execute une recherche  A* du point u vers le point but, fait en plus l'affichage en couleur
     *
     * @param u point A
     * @param but point B
     * @return  le chemin pour aller de A vers B, null si pas de chemin possible
     */
    public ArrayList<User> executeColor(User u, User but) {
        min = 1000;
        nearestNode = null;
        this.but = but.getPosition();
        Node node = new Node(u, null);
        Node finish = search(node,true);
        if (finish != null)
            return finish.toTrail();
        else
            return null;
    }

    /**
     * Execute une recherche  A* du point u vers le point but
     *
     * @param u point A
     * @param but point B
     * @return  le chemin pour aller de A vers B, null si pas de chemin possible
     */
    public ArrayList<User> execute(User u, User but) {
        min = 1000;
        nearestNode = null;
        this.but = but.getPosition();
        Node node = new Node(u, null);
        Node finish = search(node,false);
        if (finish != null)
            return finish.toTrail();
        else
            return null;
    }

    /**
     * Sélectionne le point
     *
     * @param list (ArrayList<Node>)
     * @return node (Node)
     */
    private Node selectNode(ArrayList<Node> list) {
        Node curNode = list.get(0);

        for (int i = 1; i < list.size(); i++) {
            if (curNode.heuristic(but) > list.get(i).heuristic(but)) {
                curNode = list.get(i);
            }
        }
        return curNode;
    }

    /**
     * retourne le point le plus proche du node sur lequel on appelle la methode
     * @return
     */
    public Node getNearestNode() {
        return nearestNode;
    }
}