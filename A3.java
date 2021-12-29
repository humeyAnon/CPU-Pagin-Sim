
// Main class - Reads the input files and creates the appropriate processes and pages
//              Creates the frameMemory hash maps and runs both simulations LRU first then clock replacement

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class A3 {

    public static int frameSize, qTime, count = 0;
    private static ArrayList<Process> processes = new ArrayList<>();
    private static ArrayList<Process> processes2 = new ArrayList<>();
    private static Simulation Simulation, Simulation2;
    private static FrameMemory memory, memory2;

    public static void main(String[] args) throws IOException {

        if (args.length < 1) {
            System.out.println("Wrong arguments provided");
            System.exit(0);
        }
        else {

            setUpSimulation(args);

            Simulation = new Simulation(processes, memory, qTime);
            Simulation.run(0);

            Simulation2 = new Simulation(processes2, memory2, qTime);
            Simulation2.run(1);

        }
    }

    public static void setUpSimulation(String[] args) throws IOException {

        frameSize = Integer.parseInt(args[0]);
        qTime = Integer.parseInt(args[1]);

        memory = new FrameMemory(frameSize);
        memory2 = new FrameMemory(frameSize);

        for(int i = 2; i < args.length; i++) {

            String processInput = readFile(args[i]);

            processes.add(new Process(i - 1));
            processes2.add(new Process(i - 1));

            makeProcess(processInput, count);
            count++;
        }

        for(Process p : processes) {
            p.setFrameMemory(frameSize / processes.size());
            memory.addProcess(p, p.getFrameMemory());
        }
        for(Process p : processes2) {
            p.setFrameMemory(frameSize / processes.size());
            memory2.addProcess(p, p.getFrameMemory());
        }
    }

    public static void makeProcess(String processInput, int index) {

        String regexPattern = "(?<PAGES>[\\d]+)";
        Pattern inputPattern = Pattern.compile(regexPattern);
        Matcher inputMatcher = inputPattern.matcher(processInput);

        while(inputMatcher.find()) {

            processes.get(index).addPage(Integer.parseInt(inputMatcher.group("PAGES")));
            processes2.get(index).addPage(Integer.parseInt(inputMatcher.group("PAGES")));

        }
    }

    static String readFile(String path) throws IOException {
            byte[] encoded = Files.readAllBytes(Paths.get(path));
            return new String(encoded, StandardCharsets.UTF_8);
        }
}
