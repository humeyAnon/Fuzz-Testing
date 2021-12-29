/*

			   Creates 50 input files with random strings made up of random characters
			   Finds unique exception crashes and prints them to the outputfile.txt in the root folder
			   To run - Just run this file with the KWIC class in the same folder

*/


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static RandomString stringGenerator = new RandomString();
    public static final int AMOUNT_OF_LINES = 100, NUMBER_OF_FILES = 50;
    public static List<String> exceptionList = new ArrayList<>();
    public static int filesTested = 0;

    public static void main(String[] args) throws IOException {

        // Getting inputFile path - Check if it exists, if so run testing program
        // Else create a file with random length strings and run program
        List<File> files = new ArrayList<>();

        for(int i = 0; i < NUMBER_OF_FILES; i++) {
            files.add(new File(System.getProperty("user.dir") + "\\inputFile" + i + ".txt"));
        }
        File outputFile = new File(System.getProperty("user.dir") + "\\outputFile.txt");

        System.out.println("Creating test input file and output file then running test program.");

        outputFile.createNewFile();
        for(int i = 0; i <= NUMBER_OF_FILES; i++ ) {
            createInputFile(i);
        }

        // Running all test files through program
        while (filesTested != NUMBER_OF_FILES) {
            runProgram();
        }

        // Appending the output file with the total unique exceptions
        try {
            String uniqueExceptions = "Total unique exceptions: " + exceptionList.size();
            Files.write(Paths.get("outputFile.txt"), uniqueExceptions.getBytes(), StandardOpenOption.APPEND);
        }
        catch(IOException e){
            System.exit(0);
        }
    }

    // Recursive run, that when it hits an exception re-runs main
    // Until the inputFile is empty or no more exceptions are hit
    public static void runProgram() throws IOException {

            int existFlag = 0;

            // Run the process with process builder, open streams, keep track of how many lines are being processed
            Runtime run = Runtime.getRuntime();
            String[] runArgs = {
                    "cmd.exe",
                    "/c",
                    "java -cp . KWIC inputFile" + getFilesTested() + ".txt"
            };

            Process p = run.exec(runArgs);

            BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedReader e = new BufferedReader(new InputStreamReader(p.getErrorStream()));

            String inputLine = "";
            StringBuilder inputFileLine = new StringBuilder();
            StringBuilder exceptionLine = new StringBuilder();

            while((inputLine = r.readLine()) != null) {

                inputFileLine.append(inputLine).append("\n");

            }

            // Catch the KWIC programs exception & append to the exceptionLine
            while((inputLine = e.readLine()) != null) {

                exceptionLine.append(inputLine).append("\n");

            }

            // Check if there was any exception found in the file
            if(!exceptionLine.toString().equals("")) {
                outerLoop:
                for(String s : exceptionList) {
                    // split strings by lines to compare exceptions
                    String[] oldException = s.split("\\n");
                    String[] newException = exceptionLine.toString().split("\\n");

                    // Checking what line caused the exception in KWIC program
                    // If matches break out of the whole checking loop - Could be done more efficiently
                    for(String s1 : oldException) {

                        for(String s2 : newException) {

                            if(s1.equals(s2)) {

                                existFlag = 1;
                                break outerLoop;

                            }
                        }
                    }
                }
            }

        // Check the exist flag, 0 = unique exception, 1 = non-unique
        // Write the test number, input sb and error sb to the output file
        // Only append to the output file on a unique exception
        if(existFlag == 0 && !exceptionLine.toString().equals("")) {

            exceptionList.add(exceptionLine.toString());

            try {
                FileWriter fw = new FileWriter("outputFile.txt",true);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter pw = new PrintWriter(bw);

                pw.write(testTemplate(inputFileLine.toString(), exceptionLine.toString()));

                pw.close();
            }
            catch (Exception err) {
                System.out.println(err.toString());
            }
        }

        // Increase the files tested by 1
        increaseFilesTested();

        System.out.println("Finished testing file: " + getFilesTested());
    }

    public static String testTemplate(String inputLine, String exceptionLine) {

        return  "On test file: inputFile" + getFilesTested() + ".txt\n" +
                "Input up to exception: \n" +
                "------------------------------- \n" +
                inputLine + "\n" +
                "------------------------------- \n" +
                "Caught exception: \n"+
                "------------------------------- \n" +
                exceptionLine + "\n" +
                "------------------------------- \n";
    }

    public static void createInputFile(int fileNumber) {

        try {
            // Creating a writer to output the file
            PrintWriter writer = new PrintWriter("inputFile" + fileNumber + ".txt");

            for(int i = 1; i < AMOUNT_OF_LINES; i++) {
                writer.println(stringGenerator.generateRandomString());
            }

            writer.close();

        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static int getFilesTested() {
        return filesTested;
    }

    public static void increaseFilesTested() {
        filesTested++;
    }

}
