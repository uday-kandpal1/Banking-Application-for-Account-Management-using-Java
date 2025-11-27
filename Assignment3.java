import java.util.InputMismatchException;
import java.util.Scanner;
public class ResultManager {
    private Student[] students;
    private int count;
    private Scanner scanner;

    public ResultManager(int capacity) {
        students = new Student[capacity];
        count = 0;
        scanner = new Scanner(System.in);
    }
    /**
     * Add a student by interacting with the user.
     * Declares throws InvalidMarksException to show checked-exception declaration.
     */
    public void addStudent() throws InvalidMarksException {
        try {
            System.out.print("Enter Roll Number: ");
            int roll = readInt();
            if (findIndexByRoll(roll) != -1) {
                System.out.println("Error: A student with roll number " + roll + " already exists. Aborting add.");
                return;
            }
            System.out.print("Enter Student Name: ");
            String name = readLine();
            int[] marks = new int[3];
            for (int i = 0; i < 3; i++) {
                System.out.print("Enter marks for subject " + (i + 1) + ": ");
                marks[i] = readInt();
            }
            Student s = new Student(roll, name, marks);
            s.validateMarks();
            if (count >= students.length) {
                System.out.println("Error: Student storage is full. Increase capacity.");
                return;
            }
            students[count++] = s;
            System.out.println("Student added successfully. Returning to main menu...");
        } catch (InputMismatchException ime) {
            System.out.println("Input error: expected a number. Returning to main menu...");
            scanner.nextLine(); // clear buffer
        }
    }
    public void showStudentDetails() {
        try {
            System.out.print("Enter Roll Number to search: ");
            int roll = readInt();
            int idx = findIndexByRoll(roll);
            if (idx == -1) {
                System.out.println("Student with roll number " + roll + " not found.");
            } else {
                students[idx].displayResult();
            }
            System.out.println("Search completed.");
        } catch (InputMismatchException ime) {
            System.out.println("Input error: invalid roll number. Returning to main menu...");
            scanner.nextLine();
        }
    }
    private int findIndexByRoll(int roll) {
        for (int i = 0; i < count; i++) {
            if (students[i].getRollNumber() == roll) return i;
        }
        return -1;
    }
    private int readInt() throws InputMismatchException {
        while (true) {
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) {
                System.out.print("Empty input — please enter a number: ");
                continue;
            }
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException nfe) {
                System.out.print("Invalid number format, please enter again: ");
            }
        }
    }
    private String readLine() {
        String line = scanner.nextLine();
        return line.trim();
    }
    public void mainMenu() {
        try {
            boolean running = true;
            while (running) {
                System.out.println("===== Student Result Management System =====");
                System.out.println("1. Add Student");
                System.out.println("2. Show Student Details");
                System.out.println("3. Exit");
                System.out.print("Enter your choice: ");
                int choice;
                try {
                    choice = readInt();
                } catch (InputMismatchException ime) {
                    System.out.println("Invalid choice. Returning to main menu...");
                    continue;
                }
                switch (choice) {
                    case 1:
                        try {
                            addStudent();
                        } catch (InvalidMarksException ime) {
                            // Catching custom checked exception thrown by addStudent / validateMarks
                            System.out.println("Error: " + ime.getMessage() + " Returning to main menu...");
                        }
                        break;
                    case 2:
                        showStudentDetails();
                        break;
                    case 3:
                        System.out.println("Exiting program. Thank you!");
                        running = false;
                        break;
                    default:
                        System.out.println("Invalid choice. Please select 1-3.");
                }
            }
        } finally {
            // Ensure resources are cleaned up and a closing message is shown
            if (scanner != null) {
                scanner.close();
            }
            System.out.println("Scanner closed. Application terminated.");
        }
    }
    public static void main(String[] args) {
        // create manager with capacity for e.g. 100 students
        ResultManager manager = new ResultManager(100);
        manager.mainMenu();
    }
}
class Student {
    private int rollNumber;
    private String studentName;
    private int[] marks; // length 3
    public Student(int rollNumber, String studentName, int[] marks) {
        this.rollNumber = rollNumber;
        this.studentName = studentName;
        this.marks = marks.clone();
    }
    public int getRollNumber() {
        return rollNumber;
    }
    public void validateMarks() throws InvalidMarksException {
        if (marks == null) {
            throw new InvalidMarksException("Marks array is null for roll " + rollNumber);
        }
        if (marks.length != 3) {
            throw new InvalidMarksException("Expected marks for 3 subjects for roll " + rollNumber);
        }
        for (int i = 0; i < marks.length; i++) {
            int m = marks[i];
            if (m < 0 || m > 100) {
                throw new InvalidMarksException("Invalid marks for subject " + (i + 1) + ": " + m);
            }
        }
    }
    public double calculateAverage() {
        double sum = 0;
        for (int m : marks) sum += m;
        return sum / marks.length;
    }
    public void displayResult() {
        System.out.println("Roll Number: " + rollNumber);
        System.out.println("Student Name: " + studentName);
        System.out.print("Marks: ");
        for (int m : marks) System.out.print(m + " ");
        System.out.println();
        double avg = calculateAverage();
        System.out.println("Average: " + avg);
        // simple pass/fail: pass if avg >= 40 and each subject >= 35 (example rule)
        boolean pass = avg >= 40;
        for (int m : marks) {
            if (m < 35) { pass = false; break; }
        }
        System.out.println("Result: " + (pass ? "Pass" : "Fail"));
    }
}
class InvalidMarksException extends Exception {
    public InvalidMarksException(String message) {
        super(message);
    }}
