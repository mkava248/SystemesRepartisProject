import java.util.ArrayList;

/**
 * Classe pour représenter un utilisateur/point
 */
public class Node {

    private Coordinate pos;
    private Node father;
    private User user;
    private double distanceToHere;
    private int deep;

    public Node getFather() {
        return father;
    }


    public double heuristic(Coordinate but) {
        return distanceToHere + pos.distance(but);
    }

    public Coordinate getPos() {
        return pos;
    }

    public User getUser() {
        return user;
    }

    public Node(User user, Node father) {
        this.father = father;
        this.pos = user.getPosition();
        this.user = user;
        if (father == null) {
            distanceToHere = 0;
            deep = 0;
        }
        else{
            distanceToHere = father.distanceToHere + father.getPos().distance(pos);
            deep = father.deep +1;
        }

    }


    public ArrayList<Node> nextNode() {
        boolean b = true;
        ArrayList<Node> list = new ArrayList<>();
        ArrayList<User> toAvoid = new ArrayList<>();
        for (User u :this.toTrail())
            toAvoid.add(u);
        for (User u : user.getNeighbours()) {
            b = true;
            for (User avoid : toAvoid) {
                if ((!avoid.equals(u)) && b) {
                    list.add(new Node(u, this));
                    b = false;
                }

            }
        }

        return list;
    }

    public ArrayList<User> toTrail() {

        ArrayList<User> result = new ArrayList<>();
        Node finish = this;
        if (finish!=null) {
            while(finish !=null){
                result.add(finish.getUser());
                finish = finish.getFather();
            }
            return result;
        }
        return null;
    }

    @Override
    public String toString(){
        return ""+pos.toString();
    }

    public int deep() {
        return deep;
    }

    public boolean isIn(ArrayList<Node> openList) {
        for(Node u :openList){
            if(user.equals(u.getUser())){
                return true;
            }
        }
        return false;
    }
}