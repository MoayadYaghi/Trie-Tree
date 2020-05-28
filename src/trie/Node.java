package trie;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * The node of a Trie.
 * 
 * @author Moayad Yaghi
 * @version 1.0
 */
class Node {
    private HashMap<Character, Node> children;
    private int content;

    /**
     * Creates a new sub-node for the node and sets its content to -1.
     */
    public Node() {
        children = new HashMap<Character, Node>();
        content = -1;
    }

    /**
     * Adds a student and their points to a particular course of study.
     * <p>
     * It does it by traversing throw the actual nodes ,if there are any, and
     * compares the prefixes of the given student's name with the letters on the
     * nodes. Once it finds that there is no node for the letter it's now comparing
     * it creates nodes for the actual letter and for the rest suffixes of the
     * student's name if there are any. Lastly it changes the content of the last
     * node by making it equals the given points.
     * 
     * @param studentName The input student's name.
     * @param points The input points for a student.
     * @return {@code true} if the process went normally, {@code false} otherwise.
     */
    boolean add(String studentName, int points) {
        if (studentName.length() > 0) {
            if (children.keySet().contains(studentName.charAt(0))) {
                children.get(studentName.charAt(0)).add(studentName.substring(1, studentName.length()), points);
            } else {
                Node newNode = new Node();
                newNode.add(studentName.substring(1, studentName.length()), points);
                children.put(studentName.charAt(0), newNode);
            }
        } else {
            this.content = points;
        }
        return true;
    }

    /**
     * Changes the {@code content} value of the last node to the input points by
     * traversing throw the nodes until reaching the last node.
     * 
     * @param studentName The input student name whose points are going to change.
     * @param points The new points of the given student.
     * @return {@code true} if the process went normally, {@code false} otherwise.
     */
    boolean modify(String studentName, int points) {
        if (studentName.length() > 0) {
            if (children.keySet().contains(studentName.charAt(0))) {
                return children.get(studentName.charAt(0))
                        .modify(studentName.substring(1, studentName.length()), points);
            } else {
                return false;
            }
        } else {
            this.content = points;
        }
        return true;
    }

    /**
     * Deletes a student from a particular course of study.
     * <p>
     * It does it by traversing throw the nodes and to delete a given student name.
     * It looks if some nodes have no more sub-nodes after deleting the given
     * student's name (That means there is no other student has the same letters in
     * their name) then it deletes those nodes because they would be useless.
     * 
     * @param studentName The input student's name who is going to be removed.
     */
    void delete(String studentName) {
        if (studentName.length() > 0) {
            if (children.keySet().contains(studentName.charAt(0))) {
                children.get(studentName.charAt(0)).delete(studentName.substring(1, studentName.length()));
                if (!children.get(studentName.charAt(0)).hasChildren()) {
                    children.remove(studentName.charAt(0));
                }
                return;
            }
        }
        this.children.clear();
    }

    /**
     * Gets the points of a student.
     * <p>
     * It does it by traversing throw the nodes to get the content of the last node
     * which is the points of the student whose name consists of the letters on the
     * traversed nodes.
     * 
     * @param studentName The given student name.
     * @return The points of a student.
     */
    int credits(String studentName) {
        if (studentName.length() > 0) {
            if (children.keySet().contains(studentName.charAt(0))) {
                children.get(studentName.charAt(0)).credits(studentName.substring(1, studentName.length()));
                content = children.get(studentName.charAt(0)).content;
            }
        }
        return content;
    }

    /**
     * Prints the the summary of all students' points in a course of study.
     * <p>
     * It does it by doing the following: It traverses by recursion throw the nodes
     * from the root to the edges until it has traversed all the nodes. While
     * traversing it returns the characters of the nodes between square brackets
     * until it reaches the edge then it also returns the {@code content} ,which is
     * the points of a student, of a the last nodes between parentheses.
     * <p>
     * If a node has child nodes, it traverses them according to the alphabetical
     * order (from 'a' to 'z') of their associated character to add them in that
     * order in between the square brackets.
     * <p>
     * Example: Given two names with their points {@code uabmn}(1),
     * {@code uabxy}(2).
     * <p>
     * The output would be: {@code [u[a[b[m[n(1)]x[y(2)]]]]]}.
     * 
     * @return The summary of the students with their points in a particular course.
     */
    String print() {
        if (hasChildren()) {
            Set<Character> keys = children.keySet();
            String summary = "";

            for (char letter = 'a'; letter <= 'z'; letter++) {
                if (keys.contains(letter)) {
                    if (children.get(letter).hasChildren()) {
                        summary += letter + "[" + children.get(letter).print() + "]";
                    } else {
                        summary += letter + children.get(letter).print();
                    }
                }
            }
            return summary;
        }
        return "(" + content + ")";
    }

    /**
     * Collects all the students' points and add the into a given list.
     * <p>
     * It does it by traversing by recursion throw the nodes one by one from the
     * root to the edges to collect the points of a students from {@code content} of
     * the edges.
     * 
     * @param creditList The input array list of integers that is going to have the points
     *            of students.
     */
    void collectPoints(ArrayList<Integer> creditList) {
        if (hasChildren()) {
            Set<Character> keys = children.keySet();

            for (char letter = 'a'; letter <= 'z'; letter++) {
                if (keys.contains(letter)) {
                    children.get(letter).collectPoints(creditList);
                }
            }
        }
        if (content != -1  && !hasChildren()) {
            creditList.add(content);
        }
    }

    /**
     * Checks if a student is existed or not.
     * <p>
     * It does it by traversing throw the nodes to search for a given student's name
     * if it's existed by comparing the letters of the given name with the letters
     * on the nodes.
     * 
     * @param studentName The entered student name.
     * @return {@code true} if the searched student is existed, {@code false} otherwise.
     */
    boolean has(String studentName) {
        if (studentName.length() > 0) {
            if (children.keySet().contains(studentName.charAt(0))) {
                return children.get(studentName.charAt(0)).has(studentName.substring(1, studentName.length()));
            } else {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if the this node has sub-nodes.
     * 
     * @return {@code true} if the node has sub-nodes, {@code false} otherwise.
     */
    boolean hasChildren() {
        boolean hasCh = !children.isEmpty();
        return hasCh;
    }
}