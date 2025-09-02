import java.io.*;//Importing classes for file handling
import java.util.*;//Importing classes for scanner and arraylist

public class Task3  {
    private static final int MAX_STUDENTS = 100;//Maximum numbers of student
    private static String[] studentIDs= new String[MAX_STUDENTS];//Array to store studentsIds
    private static String[] studentNames = new String[MAX_STUDENTS];//Array to the store the students name
    private static int[][] moduleMarks = new int[MAX_STUDENTS][3]; // Stores marks for 3 modules per student
    private static int studentCount = 0;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int choice = 0;
        do {
            //Display the menu options

            System.out.println("\nStudent Management System");
            System.out.println("1. Check available seats");//Check available seats
            System.out.println("2. Register student (with ID)");//Register new students in their student's ID
            System.out.println("3. Delete student");//Delete a student in their student ID
            System.out.println("4. Find student (with student ID)");//Found a student in their student ID
            System.out.println("5. Store student details into a file");//Store the all details of the all students in to file
            System.out.println("6. Load student details from the file to the system");//Load students from the file to system
            System.out.println("7. View the list of students based on their names");//View the list of students stored by their name
            System.out.println("8. Additional controls");//Additional controls menu
            System.out.println("9. Exit");//Exit the student management system
            System.out.print("Enter your choice: ");//Input the user to enter choice

            try {
                choice = scanner.nextInt();//Read user choice
                scanner.nextLine(); // Consume newline

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
                        additionalControls(scanner);//Additional controls menu
                        break;
                    case 9:
                        System.out.println("Exiting system.");//Exit system
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");//Invalid choice handling
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number between 1 and 9.");//Invalid input massage
                scanner.next(); // Clear invalid input
            }
        } while (choice != 9);//Continue until user chooses to exit

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
        // Initialize module marks array for the new student
        moduleMarks[studentCount] = new int[3];
        studentCount++;
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
                    moduleMarks[j] = moduleMarks[j + 1];
                }
                studentIDs[--studentCount] = null;
                studentNames[studentCount] = null;
                moduleMarks[studentCount] = null;
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
            if (studentIDs[i].equals(studentID)) {
                System.out.println("Student found: " + studentNames[i]);
                return;
            }
        }
        System.out.println("Student not found.");//Student not found message
    }

    private static void storeStudentDetails() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("students.txt"))) {//Write details to file
            for (int i = 0; i < studentCount; i++) {
                writer.print(studentIDs[i] + "," + studentNames[i]);//Write each student details
                for (int j = 0; j < 3; j++) {
                    writer.print("," + moduleMarks[i][j]);
                }
                writer.println();
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
                if (details.length >= 5) { // Check for minimum expected format
                    studentIDs[studentCount] = details[0];//Store student ID
                    studentNames[studentCount] = details[1];//Store student name
                    for (int i = 0; i < 3; i++) {
                        moduleMarks[studentCount][i] = Integer.parseInt(details[2 + i]);
                    }
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

        sortStudentsByAverage(); // Sort students by average marks (descending)
        System.out.println("List of students:");//Display list of students
        for (int i = 0; i < studentCount; i++) {
            Student student = new Student(studentIDs[i], studentNames[i]);// Create student object
            student.setModuleMarks(moduleMarks[i]);//set module marks
            student.calculateGrade();//Calculate grade
            System.out.println("Student ID: " + student.getStudentID());
            System.out.println("Student Name: " + student.getStudentName());
            System.out.println("Module 1 marks: " + student.getModuleMarks()[0]);
            System.out.println("Module 2 marks: " + student.getModuleMarks()[1]);
            System.out.println("Module 3 marks: " + student.getModuleMarks()[2]);
            System.out.println("Total marks: " + calculateTotalMarks(student.getModuleMarks()));
            System.out.println("Average marks: " + calculateAverageMarks(student.getModuleMarks()));
            System.out.println("Grade: " + student.getModuleGrade());
            System.out.println();
        }
    }

    private static void additionalControls(Scanner scanner) {
        System.out.println("\nAdditional Controls");//Additional controls menu
        System.out.println("a. Add student name");//option to add student name
        System.out.println("b. Enter module marks");//option to add module marks
        System.out.println("c. Generate summary of the system");
        System.out.println("d. Generate complete report with student details");
        System.out.print("Enter your choice: ");
        String choice = scanner.nextLine();

        switch (choice.toLowerCase()) {
            case "a":
                addStudentName(scanner);
                break;
            case "b":
                enterModuleMarks(scanner);
                break;
            case "c":
                generateSystemSummary();
                break;
            case "d":
                generateCompleteReport();
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    private static void addStudentName(Scanner scanner) {
        System.out.print("Enter student ID: ");//Input student ID
        String studentID = scanner.nextLine();
        System.out.print("Enter student name: ");//Input Student name
        String studentName = scanner.nextLine();

        for (int i = 0; i < studentCount; i++) {//Find student by ID
            if (studentIDs[i].equals(studentID)) {
                studentNames[i] = studentName;
                System.out.println("Student name updated successfully.");//Update student name
                return;
            }
        }
        System.out.println("Student not found.");//Student not found message
    }

    private static void enterModuleMarks(Scanner scanner) {
        System.out.print("Enter student ID: ");//Input student ID
        String studentID = scanner.nextLine();
        Student student = null;//Initialize student object

        for (int i = 0; i < studentCount; i++) {//Find student by ID
            if (studentIDs[i].equals(studentID)) {
                student = new Student(studentIDs[i], studentNames[i]);//Create new student object
                break;
            }
        }

        if (student == null) {
            System.out.println("Student not found.");//Check student not found
            return;
        }

        int[] marks = new int[3];//Array to store masks
        for (int i = 0; i < 3; i++) {
            System.out.print("Enter mark for module " + (i + 1) + ": ");//Input for module marks
            marks[i] = scanner.nextInt();
        }

        moduleMarks[findStudentIndex(studentID)] = marks;
        student.setModuleMarks(marks);//Set module marks
        student.calculateGrade();//Calculate grade
        System.out.println("Marks and grade entered successfully.");
    }

    private static void generateSystemSummary() {
        int totalRegistrations = studentCount;
        int[] passedCounts = new int[3];

        for (int i = 0; i < studentCount; i++) {
            for (int j = 0; j < 3; j++) {
                if (moduleMarks[i][j] > 40) {
                    passedCounts[j]++;
                }
            }
        }

        System.out.println("\nSystem Summary:");
        System.out.println("Total student registrations: " + totalRegistrations);
        System.out.println("Module 1 - Students scored more than 40 marks: " + passedCounts[0]);
        System.out.println("Module 2 - Students scored more than 40 marks: " + passedCounts[1]);
        System.out.println("Module 3 - Students scored more than 40 marks: " + passedCounts[2]);
    }

    private static void generateCompleteReport() {//Generate complete report
        if (studentCount == 0) {
            System.out.println("No students registered.");
            return;
        }

        sortStudentsByAverage(); // Sort students by average marks (descending)
        System.out.println("\nComplete Report:");
        for (int i = 0; i < studentCount; i++) {
            Student student = new Student(studentIDs[i], studentNames[i]);
            student.setModuleMarks(moduleMarks[i]);
            student.calculateGrade();
            int totalMarks = calculateTotalMarks(student.getModuleMarks());
            int averageMarks = calculateAverageMarks(student.getModuleMarks());
            System.out.println("Student ID: " + student.getStudentID());
            System.out.println("Student Name: " + student.getStudentName());
            System.out.println("Module 1 marks: " + student.getModuleMarks()[0]);
            System.out.println("Module 2 marks: " + student.getModuleMarks()[1]);
            System.out.println("Module 3 marks: " + student.getModuleMarks()[2]);
            System.out.println("Total marks: " + totalMarks);
            System.out.println("Average marks: " + averageMarks);
            System.out.println("Grade: " + student.getModuleGrade());
            System.out.println();
        }
    }

    private static int calculateTotalMarks(int[] marks) {//Calculate total marks
        int total = 0;
        for (int mark : marks) {
            total += mark;
        }
        return total;
    }

    private static int calculateAverageMarks(int[] marks) {//Calculate average marks
        if (marks.length == 0) return 0;
        int total = 0;
        for (int mark : marks) {
            total += mark;
        }
        return total / marks.length;
    }

    private static void sortStudentsByAverage() {
        // Bubble sort based on average marks (descending)
        for (int i = 0; i < studentCount - 1; i++) {
            for (int j = 0; j < studentCount - i - 1; j++) {
                int[] marks1 = moduleMarks[j];
                int[] marks2 = moduleMarks[j + 1];
                if (calculateAverageMarks(marks1) < calculateAverageMarks(marks2)) {
                    // Swap students IDs
                    String tempID = studentIDs[j];
                    studentIDs[j] = studentIDs[j + 1];
                    studentIDs[j + 1] = tempID;

                    //Swap students names
                    String tempName = studentNames[j];
                    studentNames[j] = studentNames[j + 1];
                    studentNames[j + 1] = tempName;

                    //Swap student module marks
                    int[] tempMarks = moduleMarks[j];
                    moduleMarks[j] = moduleMarks[j + 1];
                    moduleMarks[j + 1] = tempMarks;
                }
            }
        }
    }

    private static boolean isValidStudentID(String studentID) {
        return studentID.matches("^w\\d{7}$");//Validate student ID format
    }

    private static boolean isValidStudentName(String studentName) {
        String[] parts = studentName.split(" ");
        for (String part : parts) {
            if (!part.matches("^[a-zA-Z]{3,}$")) {//Validate each part of the name
                return false;
            }
        }
        return true;
    }

    private static int findStudentIndex(String studentID) {
        for (int i = 0; i < studentCount; i++) {
            if (studentIDs[i].equals(studentID)) {
                return i;
            }
        }
        return -1; // Not found
    }

    public static class Student {
        private String studentID;
        private String studentName;
        private int[] moduleMarks;
        private String moduleGrade;

        public Student(String studentID, String studentName) {
            this.studentID = studentID;
            this.studentName = studentName;
            this.moduleMarks = new int[3];
        }

        public void setModuleMarks(int[] moduleMarks) {
            this.moduleMarks = moduleMarks;
        }

        public void calculateGrade() {//Calculate grade
            int total = 0;
            for (int mark : moduleMarks) {
                total += mark;
            }
            int average = total / moduleMarks.length;//Calculate average marks

            if (average >= 80) {
                moduleGrade = "Distinction";
            } else if (average >= 70) {
                moduleGrade = "Merit";
            } else if (average >= 40) {
                moduleGrade = "Pass";
            } else {
                moduleGrade = "Fail";
            }
        }

        public String getStudentID() {
            return studentID;//Get student ID
        }

        public String getStudentName() {
            return studentName;//Get student name
        }

        public int[] getModuleMarks() {
            return moduleMarks;//Get module marks
        }

        public String getModuleGrade() {
            return moduleGrade;//Get module grade
        }
    }
}
