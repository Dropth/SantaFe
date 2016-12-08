import java.util.ArrayList;

/**
 * Created by Florian Alline on 21/04/2016.
 */
public class Node{
    private String instruction;
    private ArrayList<Node> fils;
    private Node pere;

    public Node(String instruction){
        this.instruction=instruction;
        fils = new ArrayList<Node>();
    }

    public void addFils(Node son){
        fils.add(son);
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public ArrayList<Node> getFils() {
        return fils;
    }

    public void setFils(ArrayList<Node> fils) {
        this.fils = fils;
    }

    public Node getPere() {
        return pere;
    }

    public void setPere(Node pere) {
        this.pere = pere;
    }
}
