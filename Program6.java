/*
  Federico Rubino
  frubino
  federico.rubino8@gmail.com
  Assignment#6
  Program6.java
  tested/working
*/

import java.lang.StringBuilder;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

/*
  Program that uses a basic version of the solitaire encryption method
  by Bruce Schneier to encrypt and decrypt any text.
*/
public class Program6{

    //function that takes info from a file and gives it to fillList
    public static void dealWithDeck(CirclyList list, String filename){
        try{
            Scanner fl = new Scanner(new File(filename));
            while(fl.hasNext()){
		list.insertBack(fl.nextInt());
            }
        } catch (FileNotFoundException e){
            System.err.println(filename+ ": File not found!");
	    System.exit(0);
        }
    }

    /*
      takes the text that needs to be encrypted or decrypted
      and gives it to the proper function
    */
    public static void takeInput(CirclyList list, String args0){
	Scanner in = new Scanner(System.in);
	String message;
	if(args0.equals("e")){
	    System.out.printf("Enter message to be encrypted (non-letters ignored)%n");
	    message = in.nextLine();
	    encrypt(bufferWithX(format(message)), list); 

	} else if(args0.equals("d")){
	    System.out.printf("Enter message to be decrypted:%n");
	    message = in.nextLine();
	    decrypt(format(message), list);
	}
	return;
    }

    // encrypts the message with the given deck
    public static void encrypt(String input, CirclyList list){
	StringBuilder encrypted = new StringBuilder();
	int keystroke = 0;
	int charInt = 0;
	char tempChar;
	for(int i = 0; i < input.length(); i++){
	    keystroke = list.solitaire();
	    //	    System.out.printf("keystroke value: %d %n", keystroke); // for testing purposes
	    charInt = input.charAt(i) + keystroke;
	    while(charInt > 90) { charInt = (charInt - 26);}
	    tempChar = (char) charInt;
	    encrypted.append(tempChar);
	}
	System.out.printf("The encrypted message is: %s%n", encrypted.toString().toUpperCase());
    }

    // decrypts the message with the given deck
    public static void decrypt(String input, CirclyList list){
	StringBuilder encrypted = new StringBuilder();
	int keystroke = 0;
	int charInt = 0;
	char tempChar;
	for(int i = 0; i < input.length(); i++){
	    keystroke = list.solitaire();
	    //	    System.out.printf("keystroke value: %d %n", keystroke); // for testing purposes
	    charInt = input.charAt(i) - keystroke;
	    while(charInt < 65) { charInt = (charInt + 26);}
	    tempChar = (char) charInt;
	    encrypted.append(tempChar);
	}
	System.out.printf("The decrypted message is: %s%n", encrypted.toString().toUpperCase());
    }

    //formats the input
    public static String format(String input){
        String transformedString = "";
        char c;
        int sLength = input.length( );
        for (int i = 0; i < sLength; i++){
            c = input.charAt(i);
            if (Character.isLetter(c)) {
                transformedString += c;
            }
        }
        return transformedString.toUpperCase();
    }

    //buffers the message with X's to be of length as a multiple of five
    public static String bufferWithX(String message){
	StringBuilder newString = new StringBuilder();
	newString.append(message);
	while(newString.length() % 5 != 0){
	    newString.append("X");
	}
	System.out.printf("Plaintext message is: %s%n", newString.toString());
	return newString.toString();
    }

    // takes in args and gives it to the dealWithDeck function
    public static void main(String args[]){
        CirclyList list = new CirclyList();
        if(args.length == 2){
            dealWithDeck(list, args[1]);
	    takeInput(list, args[0]);
        } else if(args.length == 1 && (args[0].equals("e") || args[0].equals("d"))){
	    Scanner input = new Scanner(System.in);
	    System.out.println("Please input the deck you want to use: ");
	    String deckInput = input.next();
	    dealWithDeck(list, deckInput);
	    takeInput(list, args[0]);
	} else {
	    System.err.println("Program needs a deck and encryption details (either e or d) to function");
	    System.exit(0);
        }
    }
}
