package marksmanagement;

import java.util.LinkedList;
import edu.kit.informatik.Terminal;
import trie.Trie;

/**
 * This class controls the flow of the game.
 *
 * @author Moayad Yaghi
 * @version 1.0
 */
public class MarksManagement {
    private boolean isRunning = true;
    private String input;
    private String courseTitle;
    private String studentName;
    private int points;
    private LinkedList<Trie> courses;

    /**
     * Creates a new marks manager and setting the attributes to trivial initial
     * values.
     */
    public MarksManagement() {
        this.courseTitle = "";
        this.studentName = "";
        this.points = 0;
        this.courses = new LinkedList<Trie>();
    }

    /**
     * Runs the program to receive inputs and output responses as long as the
     * program is running appropriately.
     */
    public void run() {
        while (isRunning) {
            String[] args = new String[0];
            input = Terminal.readLine();
            if (!emptyCommand()) {
                String[] rawCmd = input.split(" ");
                String rawArgs;
                String command = rawCmd[0];
                if (rawCmd.length > 1) {
                    rawArgs = rawCmd[1];
                    args = rawArgs.split(";");
                }
                if (validateInput(command, args)) {
                    switch (command) {
                        case "create":
                            create();
                            break;
                        case "reset":
                            reset();
                            break;
                        case "add":
                            add();
                            break;
                        case "modify":
                            modify();
                            break;
                        case "delete":
                            delete();
                            break;
                        case "credits":
                            credits();
                            break;
                        case "print":
                            print();
                            break;
                        case "average":
                            average();
                            break;
                        case "median":
                            median();
                            break;
                        case "quit":
                            isRunning = false;
                            break;
                        default:
                            Terminal.printError("invalid command: ".concat(input));
                            break;
                    }
                }
            }
        }
    }

    /**
     * Creates a new course of study after checking if it's not already existed. An
     * already existed course can't be recreated.
     */
    private void create() {
        if (!courseExists(courseTitle)) {
            Trie newTrie = new Trie(courseTitle);
            courses.add(newTrie);
            Terminal.printLine("OK");
        } else {
            Terminal.printError("the course '" + courseTitle + "' already exist.");
        }
    }

    /**
     * Clears all the course's data except for its name after checking if it's
     * already existed.
     */
    private void reset() {
        if (availableCourse()) {
            courses.remove(this.getCourse(courseTitle));
            create();
        }
    }

    /**
     * Performs the 'add' command from the class {@code Trie} to add a student and
     * their points to a particular course of study.
     * It performs the add command after checking if the wanted course exists and if
     * the added student or their points are the same. An 'add' command can't be
     * used for re-adding a student or their points.
     */
    private void add() {
        if (availableCourse()) {
            if (!this.getCourse(courseTitle).has(studentName)) {
                if (this.getCourse(courseTitle).credits(studentName) != points) {
                    if (this.getCourse(courseTitle).add(studentName, points)) {
                        Terminal.printLine("OK");
                    } else { // should never happen
                        Terminal.printError("unknown error occured.");
                    }
                } else {
                    Terminal.printError("you can't change the points of a student using add command,"
                            + " please use modify command to do that.");
                }
            } else {
                Terminal.printError(
                        "The student '" + studentName + "' already exists, you can't add an already existed student.");
            }
        }
    }

    /**
     * Performs the 'modify' command from the class {@code Trie} to change a
     * student's points in a particular course of study.
     * It performs that after checking if the course and the student exist and
     * whether the added points are the same as the previous ones.
     */
    private void modify() {
        if (availableCourse() && availableStudent()) {
            if (this.getCourse(courseTitle).credits(studentName) != points) {
                if (this.getCourse(courseTitle).modify(studentName, points)) {
                    Terminal.printLine("OK");
                } else { // should never happen
                    Terminal.printError("unknown error occured.");
                }
            } else {
                Terminal.printError("the points of the student are still the same, please choose different points.");
            }
        }
    }

    /**
     * Performs the 'delete' command from the class {@code Trie} to remove a student
     * from a particular course of study.
     * It performs that after checking if the course and the student exist.
     */
    private void delete() {
        for (Trie course : courses) {
            if (course.getCourseName().equals(courseTitle)) {
                if (course.has(studentName)) {
                    course.delete(studentName);
                    Terminal.printLine("OK");
                } else {
                    Terminal.printError("the searched student is not existed.");
                    break;
                }
            } else {
                Terminal.printError("the searched course is not existed.");
                break;
            }
        }
    }

    /**
     * Performs the 'credits' command from the class {@code Trie} to get a student's
     * points from a particular course of study and then prints it.
     * It performs that after checking if the course and the student exist.
     */
    private void credits() {
        if (availableCourse() && availableStudent()) {
            Terminal.printLine(this.getCourse(courseTitle).credits(studentName));
        }
    }

    /**
     * Performs the 'print' command from the class {@code Trie} to print students'
     * names and their points in a particular course of study after checking that
     * wanted course exists and has a student at least.
     */
    private void print() {
        if (availableCourse()) {
            if (this.getCourse(courseTitle).hasStudents()) {
                Terminal.printLine(this.getCourse(courseTitle).print());
            } else {
                Terminal.printLine("#");
            }
        }
    }

    /**
     * Performs the 'average' command from the class {@code Trie} to get the average
     * value of students' points in a particular course of study then prints it
     * after checking that wanted course exists and has a student at least.
     */
    private void average() {
        if (availableCourse() && hasStudents()) {
            Terminal.printLine(this.getCourse(courseTitle).average());
        }
    }

    /**
     * Performs the 'median' command from the class {@code Trie} to get the median
     * value (central value) of students' points in a particular course of study
     * then prints it after checking that wanted course exists and has a student at
     * least.
     */
    private void median() {
        if (availableCourse() && hasStudents()) {
            Terminal.printLine(this.getCourse(courseTitle).median());
        }
    }

    /**
     * Checks if the input command was an empty string (space at least or enter
     * without any explicit command).
     * 
     * @return {@code true} if there is no command, {@code false} otherwise.
     */
    private boolean emptyCommand() {
        if (input.matches("(\\s*)")) {
            Terminal.printError("your input is invalid, please input one of the valid commands.");
            return true;
        }
        return false;
    }

    /**
     * Checks if a course of study has students at all.
     * 
     * @return {@code true} if the course has at least one student, {@code false} otherwise.
     */
    private boolean hasStudents() {
        if (!this.getCourse(courseTitle).hasStudents()) {
            Terminal.printError("there are no students in the wanted course.");
            return false;
        }
        return true;
    }

    /**
     * Checks if the a course of study exists.
     * 
     * @return {@code true} if the course exists, {@code false} otherwise.
     */
    private boolean availableCourse() {
        if (!courseExists(courseTitle)) {
            Terminal.printError("the course '" + courseTitle + "' does not exist.");
            return false;
        }
        return true;
    }

    /**
     * Checks if a course of study has a student named {@code studentName}.
     * 
     * @return {@code true} if student exists, {@code false} otherwise.
     */
    private boolean availableStudent() {
        if (!this.getCourse(courseTitle).has(studentName)) {
            Terminal.printError("the searched student is not existed.");
            return false;
        }
        return true;
    }

    /**
     * Checks if the course is already existed or not.
     * 
     * @param title The name of the course to be checked.
     * @return {@code true} if the searched course is existed, {@code false} otherwise.
     */
    private boolean courseExists(String title) {
        for (Trie course : courses) {
            if (course.getCourseName().equals(title)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks the validity of the arguments number. Each command has a particular
     * number of arguments.
     * 
     * <ul>
     * <li>'quit' command has no arguments.
     * <li>'create', 'print', 'average', 'reset' and 'median' commands have only one
     * argument for each.
     * <li>'credits' and 'delete' commands have two commands for each.
     * <li>'add' and 'modify' commands have three commands for each.
     * </ul>
     * 
     * @param command The entered command.
     * @param args The arguments of the entered command.
     * @return {@code true} if the input command has valid number of arguments, {@code false} otherwise.
     */
    private boolean validArgumentsNumber(String command, String[] args) {
        if (command.equals("quit")) {
            if (args.length != 0) {
                Terminal.printError("quit command does not require any arguments.");
                return false;
            }
        } else if (command.equals("create") || command.equals("print") || command.equals("average")
                || command.equals("reset") || command.equals("median")) {
            if (args.length != 1) {
                Terminal.printError(command + " command requires one argument.");
                return false;
            }
        } else if (command.equals("credits") || command.equals("delete")) {
            if (args.length != 2) {
                Terminal.printError(command + " command requires two arguments in particular.");
                return false;
            }
        } else if (command.equals("add") || command.equals("modify")) {
            if (args.length != 3) {
                Terminal.printError(command + " command requires three arguments in particular.");
                return false;
            }
        }
        return true;
    }

    /**
     * Checks the validity of the entered course title. A title of a course is valid
     * only if it's consisted of Latin small letters.
     * 
     * @param courseTitle The course title to be checked.
     * @return {@code true} if the input course title is valid, {@code false} otherwise.
     */
    private boolean validCourseTitle(String title) {
        if (title.matches("[a-z]+")) {
            courseTitle = title;
            return true;
        } else {
            Terminal.printError("invalid course title: ".concat(title));
            courseTitle = "";
            return false;
        }
    }

    /**
     * Checks the validity of the entered student name. A name of a student is valid
     * only if it's consisted of five letters. All the letters must be in small
     * letter form. And the first letter must be 'u'.
     *
     * @param studentName The student name to be checked.
     * @return {@code true} if the input student name is valid, {@code false} otherwise.
     */
    private boolean validStudentName(String studentName) {
        if (studentName.matches("u{1}[a-z]{4}")) {
            this.studentName = studentName;
            return true;
        } else {
            Terminal.printError("invalid student name: ".concat(studentName));
            this.studentName = "";
            return false;
        }
    }

    /**
     * Checks the validity of the entered points. Points are only then valid when
     * they are positive integers. And points must consist of at least one positive
     * integer.
     *
     * @param pointsAsString The input points to be checked.
     * @return {@code true} if the points are valid, {@code false} otherwise.
     */
    private boolean validPoints(String pointsAsString) {
        if (!pointsAsString.matches("([0-9]+)")) {
            Terminal.printError("points must be a non-negative integer: ".concat(pointsAsString));
            this.points = -1;
            return false;
        } else {
            try {
                int tmp = Integer.parseInt(pointsAsString);
                if (tmp < 0) {
                    Terminal.printError("points must be a non-negative integer: ".concat(pointsAsString));

                    this.points = -1;
                    return false;
                } else {
                    this.points = tmp;
                }
            } catch (NumberFormatException ex) {
                Terminal.printError("points must be a non-negative integer: ".concat(pointsAsString));
                this.points = -1;
                return false;
            }
        }
        return true;
    }

    /**
     * Checks completely the validity of the input commands using other methods from
     * this class.
     * 
     * @param command The entered command in whole as a string.
     * @param args The arguments of the input command.
     * @return {@code true} if the commands and their arguments are valid, {@code false} otherwise.
     */
    private boolean validateInput(String command, String[] args) {
        boolean validationPass = true;
        switch (command) {
            case "create":
                if (!validArgumentsNumber(command, args) || !validCourseTitle(args[0])) {
                    validationPass = false;
                }
                break;
            case "reset":
                if (!validArgumentsNumber(command, args) || !validCourseTitle(args[0])) {
                    validationPass = false;
                }
                break;
            case "add":
                if (!validArgumentsNumber(command, args) || !validCourseTitle(args[0]) || !validStudentName(args[1])
                        || !validPoints(args[2])) {
                    validationPass = false;
                }
                break;
            case "modify":
                if (!validArgumentsNumber(command, args) || !validCourseTitle(args[0]) || !validStudentName(args[1])
                        || !this.validPoints(args[2])) {
                    validationPass = false;
                }
                break;
            case "delete":
                if (!validArgumentsNumber(command, args) || !validCourseTitle(args[0]) || !validStudentName(args[1])) {
                    validationPass = false;
                }
                break;
            case "credits":
                if (!validArgumentsNumber(command, args) || !validCourseTitle(args[0]) || !validStudentName(args[1])) {
                    validationPass = false;
                }
                break;
            case "print":
                if (!validArgumentsNumber(command, args) || !validCourseTitle(args[0])) {
                    validationPass = false;
                }
                break;
            case "average":
                if (!validArgumentsNumber(command, args) || !validCourseTitle(args[0])) {
                    validationPass = false;
                }
                break;
            case "median":
                if (!validArgumentsNumber(command, args) || !validCourseTitle(args[0])) {
                    validationPass = false;
                }
                break;
            case "quit":
                if (!validArgumentsNumber(command, args)) {
                    validationPass = false;
                }
                break;
            default:
                Terminal.printError("invalid command: ".concat(input));
                validationPass = false;
                break;
        }
        return validationPass;
    }

    /**
     * @param title The name of the course to be gotten.
     * 
     * @return The wanted course.
     */
    private Trie getCourse(String title) {
        for (Trie course : courses) {
            if (course.getCourseName().equals(title)) {
                return course;
            }
        }
        return null;
    }
}