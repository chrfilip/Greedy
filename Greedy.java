/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/*Class for detecting a seed set S.*/
/**
 *
 * @authors 
 * Xristoforos Filippou - chrfilip@inf.uth.gr 
 * Euaggelos Kanoulas - evkanoul@inf.uth.gr
 */


import java.util.ArrayList;

public class Greedy {
	
	final private int n;          //diamension of the nxn matrix.
	private int[][] graph;  //matrix with the edges information.
	final private int k;          //max iterations.
        
        ArrayList<Element> elements = new ArrayList<>();    //contains id and degree of a node.
    	ArrayList<Integer> seeds = new ArrayList<>();       //set of the influential nodes.
        
        //initialization of the instance varables of the class.
        
	public Greedy(int[][] graph,int k){
		
            this.k=k;
            this.n=graph.length;
            this.graph=new int[n][n];
		
            for(int i=0; i<n; i++){
                System.arraycopy(graph[i], 0, this.graph[i], 0, this.n);
            }
            
	}
        
        //the use of this class is for storing the info of each node.
        
        public class Element{
            
            int infected;
            int influence;
            final private int id;
        
            public Element(int id,int influ,int inf){
                this.id=id;
                this.influence=influ;
                this.infected=inf;
            }
        
        }
        
        
        //funtion for computing the influence of the nodes.
        private void computeInfluence(){
            
            int tmp;
            for(int i=0; i<n; i++){
                tmp=0;
                for(int j=0; j<n; j++){
                    if(graph[i][j]==1 && i!=j && elements.get(j).infected!=1){
                        tmp = tmp +1;
                    }
                
                }
                elements.get(i).influence=tmp;
            }
      
            
        }
        
        //function where we compute and take the node with the max influence.
        private int computeMax(){
            
            int position=0;
            int maxinfl=elements.get(0).influence;
        
            for(int i=1; i<elements.size(); i++){
                if(maxinfl<elements.get(i).influence){
                    maxinfl=elements.get(i).influence;
                    position=i;
                }
            }
            return position;
        }
        
        //function where we remove all connections between a node and every othe node.
        private void removeEdges(int position){
            
            for(int i=0; i<this.n; i++){
            
                graph[position][i]=0;
                graph[i][position]=0;
            } 
          
        }
        
        //function for infecting nodes. setting to 1 if the node is infected.
        private void infectNodes(int p){
            for(int j=0; j<n; j++){
                if(graph[p][j]==1){
                    elements.get(j).infected=1;
                }
            }
        }
        
        /*function for checking if there is at least one node with influence
        equal or bigger than one. if true then the detection continues.
        if false then the detection ends. This is helpful for when the user 
        enters a large k and the algorithm finds less than k seeds needed.*/
        private boolean checkK(){
            
            boolean flag=false;
            int i=0;
            while(!(flag) &&  i<elements.size()){
                if(elements.get(i).influence>=1){
                    flag=true;
                }
                i++;
            }
            return flag;
        }
        
        
        //function for detecting the seed set S.    
        private void detection(){
    	
            int position;
        
            for(int i=0; i<n; i++){
                Element element = new Element(i,0,0);
                elements.add(element);
            }
            //the first seed is the node with the biggest degree.
            computeInfluence();

            while(seeds.size()<k && checkK()){    //bigK() if the 
                
                position=computeMax();      //computing the node with the max influence.
                seeds.add(elements.get(position).id);   //adding the node to the seed S.
                infectNodes(position);      //infecting the directly connected nodes.
                removeEdges(position);      //removing edges equals to decreasing influence.
                computeInfluence();     //computing the new influence for each node
   
            }
        
    	//return seeds;
        }
        
        //the function to be called from the main application.
        public ArrayList<Integer> getSeeds(){
            detection();
            return seeds;
        }
    
}