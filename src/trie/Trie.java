package trie;

import java.util.ArrayList;
import java.util.Collections;

/**
 * The 'Trie' tree containing its nodes. It's a tree that can have many
 * sequences of characters and relatively high number of branches branches.
 * 
 * @author Moayad Yaghi
 * @version 1.0
 */
public class Trie {
    private String name;
    private Node root;

    /**
     * Creates a new trie named {@code name}.
     * 
     * @param name The name of the created trie.
     */
    public Trie(String name) {
        this.name = name;
        root = new Node();
    }

    /**
     * Performs the 'add' method from the class {@code Node} on the {@code root}.
     * 
     * @param word The passed student' name.
     * @param points The passed points.
     * @return {@code true} if the process went normally, {@code false} otherwise.
     */
    public boolean add(String word, int points) {
        return root.add(word, points);
    }

    /**
     * Performs the 'modify' method from the class {@code Node} on the {@code root}.
     * 
     * @param word The passed student's name.
     * @param points The passed points.
     * @return {@code true} if the process went normally, {@code false} otherwise.
     */
    public boolean modify(String word, int points) {
        return root.modify(word, points);
    }

    /**
     * Performs the 'delete' method from the class {@code Node} on the {@code root}.
     * 
     * @param word The passed student's name.
     */
    public void delete(String word) {
        root.delete(word);
    }

    /**
     * Performs the 'credits' method from the class {@code Node} on the
     * {@code root}.
     * 
     * @param word The passed student's name.
     * @return The points of the student whose name is passed.
     */
    public int credits(String word) {
        return root.credits(word);
    }

    /**
     * Performs the 'print' method from the class {@code Node} on the {@code root}.
     * '#' is the symbol that the root has.
     * 
     * @return The students' names and points in a way which is documented for the
     *         method {@code print} in the class {@code Node}.
     */
    public String print() {
        return "#" + "[" + root.print() + "]";
    }

    /**
     * Computes the average value of all points of students in a particular course
     * of study.
     * <p>
     * It does it by getting the points of students by using 'collectPoints' method
     * from the class {@code Node} on the {@code root} and then divides the sum of
     * all points by the number of students.
     * 
     * @return The average value of all students' points.
     */
    public int average() {
        ArrayList<Integer> creditList = new ArrayList<Integer>();
        root.collectPoints(creditList);
        int n = creditList.size();
        int average = 0;
        for (int i = 0; i < n; i++) {
            average += creditList.get(i);
        }
        return average / n;
    }

    /**
     * Computes the median (central value) of all students' scores of a particular
     * course.
     * <p>
     * It does it by getting the points of students by using 'collectPoints' method
     * from the class {@code Node} on the {@code root} then it sorts them and
     * afterwards it computes the median. If the number of students is
     * <ul>
     * <li>odd: it simply returns the value in the middle.
     * <li>even: it calculates the average of the two middle values.
     * </ul>
     * 
     * @return The median (central value) of all students' points.
     */
    public int median() {
        ArrayList<Integer> creditList = new ArrayList<Integer>();
        root.collectPoints(creditList);
        Collections.sort(creditList);
        int n = creditList.size();
        int length = n - 1;
        int median = 0;
        if (n % 2 == 0) {
            median = (creditList.get(length / 2) + creditList.get((length / 2) + 1)) / 2;
        } else {
            median = creditList.get((length + 1) / 2);
        }
        return median;
    }

    /**
     * Performs the 'has' method from the class {@code Node} on the {@code root}.
     * 
     * @param studentName The passed student's name.
     * @return {@code true} if the searched student is existed, {@code false} otherwise.
     */
    public boolean has(String studentName) {
        return root.has(studentName);
    }

    /**
     * Checks if a course of study has at least one student.
     * 
     * @return {@code true} if the course contains at least one student, {@code false} otherwise.
     */
    public boolean hasStudents() {
        return root.hasChildren();
    }

    /**
     * @return The course name.
     */
    public String getCourseName() {
        return name;
    }
}