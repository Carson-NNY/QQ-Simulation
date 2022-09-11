package utils;
/**
 The role of the tool class: 
Handle all kinds of user input, and can get the user's console input according to the needs of the programmer.
 */

import java.util.*;
/**
 */
public class Utility {
    //Static properties.
    private static Scanner scanner = new Scanner(System.in);


    /**
     * Function: read a menu option entered by the keyboard, range: 1- 5
     * @return 1——5
     */
    public static char readMenuSelection() {
        char c;
        for (; ; ) {
            String str = readKeyBoard(1, false);//A string containing one character
            c = str.charAt(0);//Convert a string to a character char type
            if (c != '1' && c != '2' &&
                    c != '3' && c != '4' && c != '5') {
                System.out.print("Wrong selection, please re-enter：");
            } else break;
        }
        return c;
    }

    /**
     * Function: read a character entered by the keyboard
     * @return One character
     */
    public static char readChar() {
        String str = readKeyBoard(1, false);//It's a character.
        return str.charAt(0);
    }
    /**
     * Function: read a character entered by the keyboard, and return the specified default value if you press enter directly; otherwise, 
     return the entered character
     * @param defaultValue  Specified default value
     * @return Default value or entered character
     */

    public static char readChar(char defaultValue) {
        String str = readKeyBoard(1, true);//Either an empty string or a character
        return (str.length() == 0) ? defaultValue : str.charAt(0);
    }

    /**
     * Function: read the integer of keyboard input, the length is less than 2 bits
     * @return integer
     */
    public static int readInt() {
        int n;
        for (; ; ) {
            String str = readKeyBoard(10, false);//An integer with a length < = 10 
            try {
                n = Integer.parseInt(str);//Convert a string to an integer
                break;
            } catch (NumberFormatException e) {
                System.out.print("Number input error, please re-enter:");
            }
        }
        return n;
    }
    /**
     * Function: read the integer or default value entered by the keyboard, if enter directly, return the default value,
     * otherwise return the entered integer
     * @param defaultValue Specified default value
     * @return Integer or default value
     */
    public static int readInt(int defaultValue) {
        int n;
        for (; ; ) {
            String str = readKeyBoard(10, true);
            if (str.equals("")) {
                return defaultValue;
            }

            //异常处理...
            try {
                n = Integer.parseInt(str);
                break;
            } catch (NumberFormatException e) {
                System.out.print("Number input error, please re-enter：");
            }
        }
        return n;
    }

    /**
     *Function: read the specified length string entered by the keyboard
     * @param limit  Restricted length
     * @return A string of specified length
     */

    public static String readString(int limit) {
        return readKeyBoard(limit, false);
    }

    /**
     * Function: read the specified length string or default value entered by the keyboard, if press the enter directly, return the 
     * default value, otherwise return the string
     * @param limit Restricted length
     * @param defaultValue Specified default value
     * @return  A string of specified length
     */

    public static String readString(int limit, String defaultValue) {
        String str = readKeyBoard(limit, true);
        return str.equals("")? defaultValue : str;
    }


    /**
     * Function: read the confirmation option of keyboard input, Y or N
     * Encapsulate small functions into a method.
     * @return Y or N
     */
    public static char readConfirmSelection() {
        System.out.println("Please make sure whether (Y / N): please choose carefully.");
        char c;
        for (; ; ) {// UNLimited cycle
            //Here, you will receive characters and convert them to uppercase letters 
            //y => Y n=>N
            String str = readKeyBoard(1, false).toUpperCase();
            c = str.charAt(0);
            if (c == 'Y' || c == 'N') {
                break;
            } else {
                System.out.print("Selection error, please re-enter:");
            }
        }
        return c;
    }

    /**
     * Function: read a string
     * @param limit Length of read
     * @param blankReturn If true, an empty string can be read.
     * 					  If false means that the empty string cannot be read.
     *
     *	If the input is empty, or if you enter a length greater than limit, you will be prompted to re-enter.
     * @return
     */
    private static String readKeyBoard(int limit, boolean blankReturn) {

        String line = "";

        //scanner.hasNextLine() To see whether there is any next line.
        while (scanner.hasNextLine()) {
            line = scanner.nextLine();

            //if line.length=0, That is, the user does not enter anything and press enter directly.
            if (line.length() == 0) {
                if (blankReturn) return line;//If blankReturn=true, you can return an empty string
                else continue; //If blankReturn=false does not accept empty strings, you must enter content
            }

            //If the content entered by the user is greater than limit, the user is prompted to rewrite the input.
            //If the content of the user such as > 0 and  < = limit, accept it
            if (line.length() < 1 || line.length() > limit) {
                System.out.print("enter a length (cannot be greater than" + limit + "）Error, please re-enter：");
                continue;
            }
            break;
        }

        return line;
    }
}

