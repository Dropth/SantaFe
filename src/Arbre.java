import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Florian Alline on 21/04/2016.
 */
public class Arbre {

    private Node racine;
    private double fitness;
    
    public Arbre(Node racine){
    	
        this.racine=racine;
    }

    public double getFitness() { return fitness; }

    public void setFitness(double fitness) { this.fitness = fitness; }
    
    public Node getRacine(){ return racine; }
    
    public String toString() { return getRepresentationArbre(); }
    
    public String getRepresentationArbre() {
    	
        String res ="";
        if(racine==null) return null;
        else{
        	res+=racine.getInstruction()+" ";
            for(Node son : racine.getFils()){
            	res+=getRepresentationSousArbre(son);
            }
        }
        return res;
    }
    
    private int getTailleArbre(){
    	
        int res=1;
        
        for(Node fil : racine.getFils())
        	res+=getTailleSousArbre(fil);
        
        return res;
    }

    private int getTailleSousArbre(Node n){
    	
        int res=1;
        
        for(Node fil : n.getFils())
        	res+=getTailleSousArbre(fil);
        
        return res;
    }

    private void getNodeList(Node n,ArrayList<Node> al){
    	
        al.add(n);
        for(Node fil : n.getFils())
            getNodeList(fil,al);
        
    }

    public void recombinaison(Arbre r){
    	
        ArrayList<Node> list1 = new ArrayList<>();
        ArrayList<Node> list2 = new ArrayList<>();
        
        this.getNodeList(racine,list1);
        this.getNodeList(r.racine,list2);
        
        int taille1 = this.getTailleArbre();
        int taille2 = r.getTailleArbre();
        
        Random ra = new Random();
        
        int ra1=0;
        int ra2=0;
        
        while(ra1==0) ra1 = ra.nextInt(taille1);
        while(ra2==0) ra2 = ra.nextInt(taille2);
        
        Node coupe1 = list1.get(ra1);
        Node coupe2 = list2.get(ra2);
        
        Node pere1=coupe1.getPere();
        Node pere2=coupe2.getPere();
        
        int place1 = pere1.getFils().indexOf(coupe1);
        int place2 = pere2.getFils().indexOf(coupe2);
        
        pere1.getFils().remove(coupe1);
        pere2.getFils().remove(coupe2);
        pere1.getFils().add(place1,coupe2);
        pere2.getFils().add(place2,coupe1);
        
        coupe2.setPere(pere1);
        coupe1.setPere(pere2);
    }

    private String getRepresentationSousArbre(Node n) {
    	
        String res="";
        
        if(n.getFils().size()>0){
        	
        	res+=n.getInstruction()+" ";
        	
            for(Node son : n.getFils())
            	res+=getRepresentationSousArbre(son);
            
        }
        else
        	res+=n.getInstruction()+" ";
        
        return res;
    }
    
    private String parcourirNoeud(Node n){
    	
        String ret=n.getInstruction();
        boolean ok=false;
        
        for(Node filsNode : n.getFils()){
        	
            ok=true;
            ret+=parcourirNoeud(filsNode);
        }
        
        if(ok)ret+=")";
        return ret;
    }

    public static Arbre genererArbre(ArrayList<String> nonTerminaux, ArrayList<String> terminaux, int profondeurMax){
    	
        Random r = new Random();
        int alea = r.nextInt(nonTerminaux.size());
        Node n = new Node(nonTerminaux.get(alea));
        Arbre a = new Arbre(n);
        
        int nbIte = nonTerminaux.get(alea).equals("if-sensor")?2:4;
        
        for(int i=0;i<nbIte;i++)
            genererArbre(nonTerminaux,terminaux,profondeurMax,1,n);
        
        return a;
    }

    private static void genererArbre(ArrayList<String> nonTerminaux, ArrayList<String> terminaux, int profondeurMax,int profondeurActuelle, Node n) {
    	
        Random r = new Random();
        
        if(profondeurActuelle<profondeurMax){
        	
            int dico = r.nextInt(2);
            
            if(dico==0){
            	
                int alea = r.nextInt(nonTerminaux.size());
                Node fil = new Node(nonTerminaux.get(alea));
                n.addFils(fil);
                fil.setPere(n);
                int nbIte = nonTerminaux.get(alea).equals("if-sensor")?2:4;
                
                for(int i=0;i<nbIte;i++)
                    genererArbre(nonTerminaux,terminaux,profondeurMax,profondeurActuelle+1,fil);
                
            }
            else{
            	
                int alea = r.nextInt(terminaux.size());
                Node fil = new Node(terminaux.get(alea));
                n.addFils(fil);
                fil.setPere(n);
            }
        }
        else{
        	
            int alea = r.nextInt(terminaux.size());
            Node fil = new Node(terminaux.get(alea));
            n.addFils(fil);
            fil.setPere(n);
        }
    }
}
