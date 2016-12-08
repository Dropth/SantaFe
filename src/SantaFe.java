import java.util.ArrayList;

/**
 * Created by Florian Alline on 16/05/2016.
 */

public class SantaFe {
	
	static final int NBITE=200;
	static final int NBGENERATION=300;
	static final int TAILLEPOP=300;
	
	
    static int[][] grille;
    static int[] coord;
    
    static int direction;
    static int score;
    static int but=0;
    
    public static void main(String[] args) {

        ArrayList<String> terminaux = new ArrayList<>();
        terminaux.add("if-sensor");
        terminaux.add("progn");
        
        ArrayList<String> nonTerminaux = new ArrayList<>();
        nonTerminaux.add("advance");
        nonTerminaux.add("turn-left");
        nonTerminaux.add("turn-right");

        int cpt=0;
        
        ArrayList<Arbre> alArbre = new ArrayList<>();
        ArrayList<Arbre> tmp;
        
        init();
        setBut();
        
        for (int i = 0; i < TAILLEPOP; i++) {
        	
            init();
            Arbre arb = Arbre.genererArbre(terminaux, nonTerminaux, 3);
            launch(arb);
            arb.setFitness(calculerFitnessAjustée(score));
            alArbre.add(arb);
        }
        
        tmp = getSortedList(alArbre);
        
        while(tmp.get(0).getFitness()!=1.0 && cpt<NBGENERATION) {
        	
        	alArbre = new ArrayList<>();
        	
            for(int i=0;i<tmp.size();i+=2){
            	
            	tmp.get(i).recombinaison(tmp.get(i+1));
            }
            
            for(Arbre arb : tmp){
            	
            	init();
                launch(arb);
                arb.setFitness(calculerFitnessAjustée(score));
                alArbre.add(arb);
            }
            
            tmp = getSortedList(alArbre);
            cpt++;
        }
        
        System.out.println("En " + cpt + " générations, nous avons obtenue une fitnesse de : " + tmp.get(0).getFitness() + " avec l'arbre suivant : \n\n" + tmp.get(0));
    }
    
    public static void init(){
    	
        grille = new int[][]{
                {1,1,0,0,0,0,0,0,0,2},
                {0,1,0,0,0,0,0,0,0,1},
                {0,1,0,0,0,0,0,0,1,0},
                {0,0,1,0,0,0,0,0,1,0},
                {0,0,0,0,0,0,0,1,0,0},
                {0,0,0,1,0,0,0,1,0,0},
                {0,0,0,1,0,0,0,0,0,0},
                {0,0,0,1,0,0,0,0,0,0},
                {0,0,0,0,0,1,1,0,0,0},
                {0,0,0,0,0,0,0,0,0,0}};
        coord = new int[]{0,9};
        direction=2;
        score=0;
    }

    public static void setBut(){
    	
        for(int i=0;i<grille.length;i++)
            for(int j=0;j<grille[0].length;j++)
                if(grille[i][j]==1) but++;
    }
    
    public static ArrayList<Arbre> getSortedList(ArrayList<Arbre> arbres){
    	
        ArrayList<Arbre> resultat = new ArrayList<>();
        Arbre tmp;
        
        int bound=arbres.size();
        
        for(int i=0;i<bound;i++){
        	
        	tmp=arbres.get(0);
            for(Arbre a : arbres){
            	
                if(a.getFitness()>tmp.getFitness()) tmp=a;
            }
            
            resultat.add(tmp);
            arbres.remove(tmp);
        }
        return resultat;
    }
    
    public static double calculerFitnessAjustée(int score){
    	
        double score2 = score*1.0;
        return 1/(1+but-score2);
    }
    
    public static void launch(Arbre a){
    	
        for(int i=0;i<NBITE;i++){
        	
        	exe(a.getRacine());
        }
    }

    private static void exe(Node n){
    	
        String instruction=n.getInstruction();
        
        switch(instruction){
        
            case "if-sensor":
                if(if_sensor()) exe(n.getFils().get(0));
                else exe(n.getFils().get(1));
                break;
            case "progn":
                for(Node fils : n.getFils()) exe(fils);
                break;
            case "advance" :
                avancerFourmis();
                break;
            case "turn-left":
                turnLeft();
                break;
            case "turn-right":
                turnRight();
                break;
        }
    }
    
    //Si cela est possible, on avance notre fourmis dans la bonne direction et on regarde s'il y a de la nourriture sur son chemin
    public static void avancerFourmis(){
    	
        if(isPossible()) {
        	
            if (direction == 0) coord[0]--;
            else if (direction == 1) coord[1]++;
            else if (direction == 2) coord[0]++;
            else coord[1]--;

            if(grille[coord[0]][coord[1]] == 1){
                grille[coord[0]][coord[1]]=0;
                score++;
            }
        }
    }

    public static boolean if_sensor(){
    	
        if(isPossible()){
        	
            if(direction==0 && grille[coord[0]-1][coord[1]] ==1) return true;
            else if(direction==1 && grille[coord[0]][coord[1]+1] ==1) return true;
            else if(direction==2 && grille[coord[0]+1][coord[1]] ==1) return true;
            else if(direction==3 && grille[coord[0]][coord[1]-1] ==1) return true;
            else return false;
        }
        return false;
    }

    public static boolean isPossible(){
    	
        if(direction==0 && coord[0] ==0) return false;
        else if (direction==1 && coord[1] == grille[0].length-1) return false;
        else if (direction==2 && coord[0] == grille.length-1) return false;
        else if (direction==3 && coord[1] == 0) return false;
        else return true;
    }

    public static void turnRight(){
    	
        if(direction==0) direction=1;
        else if(direction==1) direction=2;
        else if(direction==2) direction=3;
        else direction=0;
    }

    public static void turnLeft(){
    	
        if(direction==0) direction=3;
        else if(direction==1) direction=1;
        else if(direction==2) direction=2;
        else direction=3;
    }
}
