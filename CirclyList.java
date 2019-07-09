/*
  Federico Rubino
  frubino
  federico.rubino8@gmail.com
  CirclyList.java
  Assignment#6 
  working/tested
*/
import java.lang.StringBuilder;

/*
  CirclyList
  class that imitates a circly doubly linked List, with all the appropriate functions
  uses class LinkNode to create a list of linked Nodes
*/
public class CirclyList{

    //nodes for the linkedList CirclyList
    private class LinkNode{
        private int data;
        private LinkNode next;
	private LinkNode prev;

        //constructor w/o parameter
        private LinkNode(){
            next = null;
        }

        //constructor w/ a value for data
        //not necessary but seems smoother
        private LinkNode(int data){
            next = null;
            this.data = data;
        }
    }

    private LinkNode front;
    private LinkNode back; 

    // constructs a new list
    public CirclyList(){
        front = new LinkNode();
	back = new LinkNode();
    }
    
    // checks if list is empty
    public boolean isEmpty(){
        return front.next == null; // will only be null if insert has never been called before
    }
    
    // inserts an integer into the circly doubly list
    public void insertBack(int num){
        LinkNode temp = new LinkNode(num);
	if(isEmpty()){
	    front = back = temp;
	    front.next = back;
	    front.prev = back;
	    back.next = front;
	    back.prev = front;
	    return;
	}
	temp.prev = back;
	temp.next = front;
	back.next = temp;
	front.prev = temp;
	back = temp;
    }

    //used by step 1 and 2 swaps adjacent Nodes    
    private void swapAdjacent(LinkNode previous, LinkNode node){
	boolean specialCase = (previous == back && node == front);
	boolean oneBeforeBack = (node == back);
	boolean frontJoker = (previous == front);
	node.prev = previous.prev; 
	previous.next = node.next; 
	node.next = previous;      
	previous.prev = node;      
	node.prev.next = node;     
	previous.next.prev = previous;
	if(specialCase){
	    back = node;
	    front = previous;
	} else if(oneBeforeBack){
	    back = previous;
	} else if(frontJoker){
	    front = node;
	}
	return;
    }
    
    //first step involving moving the 27 Joker back one
    private LinkNode joker27DownOne(){
	LinkNode temp = front;
	LinkNode tempPrev = front.prev;
	while(temp != back){
	    if(tempPrev.data == 27){
		swapAdjacent(tempPrev, temp);
		//System.out.printf("first step:   %s",toStringForward()); // test
		return tempPrev;
	    }
	    tempPrev = temp;
            temp = temp.next;
        }
	swapAdjacent(tempPrev, temp);
	//System.out.printf("first step:   %s",toStringForward()); // test
	return tempPrev;
    }
        
    //second step involving moving the 28 Joker back two
    private LinkNode joker28DownTwo(){
	LinkNode temp = front;
	LinkNode tempPrev = front.prev;
	LinkNode tempNext = front.next;
	while(temp != back){
	    if(tempPrev.data == 28){
		swapAdjacent(tempPrev, temp);
		swapAdjacent(tempPrev, tempNext);
		//System.out.printf("second step:  %s",toStringForward()); //test
		return tempPrev;
	    }
	    tempPrev = temp;
            temp = temp.next;
	    tempNext = temp.next;
        }
	swapAdjacent(tempPrev, temp);
	swapAdjacent(tempPrev, tempNext);
	//System.out.printf("second step:  %s",toStringForward()); //test
	return tempPrev;
    }

    //function that figures out which joker is first in the list    
    private boolean findFirstJoker(LinkNode joker1, LinkNode joker2){
	LinkNode temp = front;
	while(temp != back){
	    if(temp.data == joker1.data){
		return true;
	    } else if(temp.data == joker2.data){
		return false;
	    }
	    temp = temp.next;
	}	
	return temp.data == joker1.data;
    }

    //function used for step 3 and 4, cuts the deck into three
    private void cutDeck(LinkNode first, LinkNode second){
	boolean specialCase = first == front;
	boolean specialCase2 = first == front.next;
	LinkNode newBack = first.prev;
	if(second == back && !(specialCase)){
	    second.next = front;
	    front.prev = second;
	    front = first;
	    back = newBack;
	    return;
	}
	if(specialCase){ newBack = second;}
	LinkNode newFront = second.next;
	LinkNode tempFront = front;
	LinkNode tempBack = back;
	second.next = tempFront;
	tempFront.prev = second;
	first.prev = tempBack;
	tempBack.next = first;
	newFront.prev = newBack;
	newBack.next = newFront;
	front = newFront;
	back = newBack;
    }

    //third step, cutting the deck around the two jokers
    private void tripleCut(LinkNode joker1, LinkNode joker2){
	LinkNode temp = joker2;
	if(!(findFirstJoker(joker1, joker2))){
	    joker2 = joker1;
	    joker1 = temp;
	}
	cutDeck(joker1, joker2);
	//System.out.printf("cut the deck: %s",toStringForward()); // test
    }

    //step 4, the value on the last Node decides how many from the front get cut infront of it 
    private void moveFrontDown(){
	LinkNode cut = front;
	LinkNode tempBack = back;
	int backCardValue = back.data;
	if(back.data == 28 || back.data == 27){ return;}
	for(int i = 0; i < backCardValue; i++){
	    cut = cut.next;
	}
	cutDeck(cut, back.prev);
	back = front;
	front = front.next;
	//System.out.printf("step four:    %s",toStringForward()); //test
    }

    //fifth step gets us the keystroke
    private int findKey(){
	LinkNode keyStroke = front;
	int frontData = front.data;
	if(frontData > 26 ){frontData = 27;}
	for(int i = 0; i < frontData; i++){
	    keyStroke = keyStroke.next;
	}
	if(keyStroke.data > 26){ return -1;} // jokers are larger than 26 -> do nothing
	//System.out.printf("keystroke: %d%n",keyStroke.data); //test
	return keyStroke.data;
    }
    
    // function that returns one keystroke by calling on all the proper steps in order
    public int solitaire(){
	LinkNode step1 = joker27DownOne();
	LinkNode step2 = joker28DownTwo();
	tripleCut(step1, step2);
	moveFrontDown();
	int keyStroke = findKey();
	if(keyStroke == -1){ return solitaire();} //in the case that findKey() pointed at a joker
	return keyStroke;
    }

    /*
      returns a string with each integer in the list in order, starting from
      the first, with one entry per line.
    */
    public String toStringForward(){
	if(isEmpty()){ return "";}
        StringBuilder builtString = new StringBuilder();
        LinkNode temp = front;
	String formated = "";
        while(temp != back){
	    formated = String.format("%02d", temp.data);
            builtString.append(formated);
            builtString.append(" ");
            temp = temp.next;
        }
	formated = String.format("%02d", temp.data);
	builtString.append(formated);
	builtString.append("\n");
        return builtString.toString();
    }
    
    //prints the String backwards // function for testing purposes 
    public String toStringBackward(){
	if(isEmpty()){ return "";}
        StringBuilder builtString = new StringBuilder();
        LinkNode temp = back;
	String formated = "";
        while(temp != front){
	    formated = String.format("%02d", temp.data);
            builtString.append(formated);
            builtString.append(" ");
            temp = temp.prev;
        }
	formated = String.format("%02d", temp.data);
	builtString.append(formated);
	builtString.append("\n");
        return builtString.toString();
    }
}
