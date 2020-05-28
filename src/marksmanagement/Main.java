package marksmanagement;

/**
 * This is the entry point class for the program containing the main method.
 *
 * @author Moayad Yaghi
 * @version 1.0
 */
public class Main {
    
    /**
     * The main method of the program.
     *
     * @param args The arguments that are passed to the program at launch as array.
     */
    public static void main(String[] args) {
        MarksManagement mgmt = new MarksManagement();
        
        mgmt.run();
    }
}