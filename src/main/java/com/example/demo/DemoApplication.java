package com.example.demo;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class DemoApplication {
    public static void main(String[] args) {
        String containerId = "dfe51dffc220";  // This is the container ID on which we have to perform operations
        try {

            //test
            String testCommand = "docker exec dfe51dffc220 echo Hello World";
            runCommand(testCommand);


            // Executing oscap from outside as it was the task
            String oscapCommand = "docker exec "+ containerId+" oscap oval eval --report report.html /com.ubuntu.noble.usn.oval.xml";
            runCommand(oscapCommand);

            System.out.println("oscap command executed successfully!");

            // Copying report from container to our local machine so that we can view
            String copyCommand = "docker cp " + containerId + ":/report.html /home/vibhor/Programs/Java/OSCAP2/demo";
            runCommand(copyCommand);

            System.out.println("Report copied successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String runCommand(String command) throws Exception {
        System.out.println("Executing: " + command);
        Process process = Runtime.getRuntime().exec(command);

        // Capture output and error streams
        StringBuilder output = new StringBuilder();
        StringBuilder errorOutput = new StringBuilder();

        try (BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
             BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {

            String line;
            while ((line = stdInput.readLine()) != null) {
                output.append(line).append("\n");
            }

            while ((line = stdError.readLine()) != null) {
                errorOutput.append(line).append("\n");
            }
        }

        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new RuntimeException("Command failed with exit code: " + exitCode + "\nError: " + errorOutput.toString().trim());
        }

        String result = output.toString().trim();
        System.out.println("Output: " + result);
        return result;
    }

}
