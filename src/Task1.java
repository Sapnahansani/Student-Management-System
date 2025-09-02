import java.io.*;//Importing classes for file handling
import java.util.*;//Importing classes for scanner and arraylist

public class Task1 {
    private static final int MAX_STUDENTS = 100;//Maximum numbers of students
    private static String[] studentIDs = new String[MAX_STUDENTS];//Array to store studentsIds
    private static String[] studentNames = new String[MAX_STUDENTS];//Array to the store the Student Name
    private static int studentCount = 0;//Counter for the register students

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int choice = 0;
        do {
            //Display the menu options

            System.out.println("\nStudent Management System");
            System.out.println("1. Check available seats");//check available seats
            System.out.println("2. Register student (with ID)");//Register new students in their student's ID
            System.out.println("3. Delete student");//Delete a student in their student ID
            System.out.println("4. Find student (with student ID)");//Found a student in their student ID
            System.out.println("5. Store student details into a file");//Store the all details of all students in to a file
            System.out.println("6. Load student details from the file to the system");//Load students from the file to system
            System.out.println("7. View the list of students based on their names");//View the list of students stored by their names
            System.out.println("8. Exit");//Exit the student management system
            System.out.print("Enter your choice: ");//Input the user to enter choice

            try {
                choice = scanner.nextInt();//Read user choice
                scanner.nextLine(); //Consume newline

                switch (choice) {
                    case 1:
                        checkAvailableSeats();//Check available seats
                        break;
                    case 2:
                        registerStudent(scanner);//Register new students
                        break;
                    case 3:
                        deleteStudent(scanner);//Delete a student by ID
                        break;
                    case 4:
                        findStudent(scanner);//Find a student by ID
                        break;
                    case 5:
                        storeStudentDetails();//Store student details to a file
                        break;
                    case 6:
                        loadStudentDetails();//Load student details from a file
                        break;
                    case 7:
                        viewStudents();//View list of students sort by names
                        break;
                    case 8:
                        System.out.println("Exiting system.");//Exit system
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");//Invalid choice handling
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number between 1 and 8.");//Invalid input massage
                scanner.next(); // Clear invalid input
            }
        } while (choice != 8);//Continue until user chooses to exit

        scanner.close();//close the scanner
    }

    private static void checkAvailableSeats() {
        int availableSeats = MAX_STUDENTS - studentCount;//Calculate available seats
        System.out.println("Available seats: " + availableSeats);//Display available seats
    }

    private static void registerStudent(Scanner scanner) {
        if (studentCount >= MAX_STUDENTS) {// Check if maximum students reached
            System.out.println("No more seats available.");
            return;
        }

        System.out.print("Enter student ID: ");
        String studentID = scanner.nextLine();//Read student ID
        if (!isValidStudentID(studentID)) {//Validate student ID
            System.out.println("Invalid student ID. It must be 'w' followed by 7 digits (e.g., w1234567).");
            return;
        }

        for (int i = 0; i < studentCount; i++) {//Check if student ID already exists
            if (studentIDs[i].equals(studentID)) {
                System.out.println("Student ID already exists.");
                return;
            }
        }

        System.out.print("Enter student name: ");
        String studentName = scanner.nextLine();//Read student name
        if (!isValidStudentName(studentName)) {//Validate student name
            System.out.println("Invalid student name. Each part must be at least 3 characters long and contain only alphabetic characters.");
            return;
        }

        studentIDs[studentCount] = studentID;//Store student ID
        studentNames[studentCount] = studentName;//Store student name
        studentCount++;//Increment student count
        System.out.println("Student registered successfully.");
    }

    private static void deleteStudent(Scanner scanner) {
        System.out.print("Enter student ID to delete: ");
        String studentID = scanner.nextLine();//Read student ID to delete

        for (int i = 0; i < studentCount; i++) {
            if (studentIDs[i].equals(studentID)) {
                // Shift students to fill the gap
                for (int j = i; j < studentCount - 1; j++) {
                    studentIDs[j] = studentIDs[j + 1];
                    studentNames[j] = studentNames[j + 1];
                }
                studentIDs[--studentCount] = null;
                studentNames[studentCount] = null;
                System.out.println("Student deleted successfully.");
                return;
            }
        }
        System.out.println("Student not found.");
    }

    private static void findStudent(Scanner scanner) {
        System.out.print("Enter student ID to find: ");
        String studentID = scanner.nextLine();//Read student ID to find

        for (int i = 0; i < studentCount; i++) {
            if (studentIDs[i].equals(studentID)) {//Find student by ID
                System.out.println("Student found: " + studentNames[i]);
                return;
            }
        }
        System.out.println("Student not found.");//Student not found message
    }

    private static void storeStudentDetails() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("students.txt"))) {//Write details to file
            for (int i = 0; i < studentCount; i++) {
                writer.println(studentIDs[i] + "," + studentNames[i]);//Write ech student details
            }
            System.out.println("Student details stored successfully.");//Confirmation message
        } catch (IOException e) {
            System.out.println("Error storing student details.");//Error message
        }
    }

    private static void loadStudentDetails() {
        try (Scanner fileScanner = new Scanner(new File("students.txt"))) {//Read student details to file
            studentCount = 0;
            while (fileScanner.hasNextLine()) {
                String[] details = fileScanner.nextLine().split(",");
                if (details.length == 2) {
                    studentIDs[studentCount] = details[0];//Store student ID
                    studentNames[studentCount] = details[1];//Store student name
                    studentCount++;//Increment student count
                }
            }
            System.out.println("Student details loaded successfully.");//Confirmation message
        } catch (FileNotFoundException e) {
            System.out.println("Error loading student details.");//Error message
        }
    }

    private static void viewStudents() {
        if (studentCount == 0) {//Check if there are no students
            System.out.println("No students registered.");
            return;
        }

        sortStudentsByName();
        System.out.println("List of students:");//Display list of students
        for (int i = 0; i < studentCount; i++) {
            System.out.println(studentIDs[i] + " - " + studentNames[i]);//Display each student details
        }
    }

    private static void sortStudentsByName() {
        for (int i = 0; i < studentCount - 1; i++) {
            for (int j = i + 1; j < studentCount; j++) {
                if (studentNames[i].compareTo(studentNames[j]) > 0) {//Compare student name
                    String tempID = studentIDs[i];
                    String tempName = studentNames[i];
                    studentIDs[i] = studentIDs[j];
                    studentNames[i] = studentNames[j];
                    studentIDs[j] = tempID;
                    studentNames[j] = tempName;
                }
            }
        }
    }

    private static boolean isValidStudentID(String studentID) {
        return studentID.matches("^w\\d{7}$");//Validate student ID format
    }

    private static boolean isValidStudentName(String studentName) {
        String[] parts = studentName.split(" ");//split name in to parts
        for (String part : parts) {
            if (!part.matches("^[a-zA-Z]{3,}$")) {//Validate ech part of the name
                return false;
            }
        }
        return true;
    }
}

