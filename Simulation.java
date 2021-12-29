
// Simulation class - Used to simulate both LRU and clock replacement
//                    algorithms

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Simulation {

    private ArrayList<Process> processes;
    private Process currentProcessRunning;
    private FrameMemory memory;
    private int clockTime, qTime, algoType;
    private Queue<Process> readyQ = new LinkedList<>();
    private ArrayDeque<Process> blockedQ = new ArrayDeque<>();
    private ArrayList<Process> finishedList = new ArrayList<>();
    private String LRUoutput = "LRU - Fixed:\n" +
                               "PID\tProcess Name\tTurnaround Time\t# Faults\tFault Times\n";
    private String ClockOutput = "Clock - Fixed:\n" +
                                 "PID\tProcess Name\tTurnaround Time\t# Faults\tFault Times\n";

    public Simulation(ArrayList<Process> processes, FrameMemory memory, int qTime) {

        this.processes = processes;
        this.memory = memory;
        this.clockTime = 0;
        this.qTime = qTime;
        readyQ.addAll(processes);
    }

    // On startup it gets all pages from VM and places them into the processes memory and adds the time it takes
    // Onto the system. Then runs while all processes are yet to finish, constantly grabbing from the readyQ and running the process
    // If readyQ is empty it ticks a timeunit and checks if any processes are ready to join the readyQ
    // Then concats the appropriate output string for each replacement algorithm - algoType 0 is LRU
    public void run(int algoType) {

        this.algoType = algoType;
        getPages();

        do {
            if(!readyQ.isEmpty()){
                runProcess();
            }
            else {

                clockTime++;
                blockedToReady();
            }
        } while(finishedList.size() != processes.size());

        if(algoType == 0){
            for(Process p : processes) {
                LRUoutput = LRUoutput.concat(p.processOutput() + "\n");
            }
            System.out.println(LRUoutput);
        }
        else {
            for(Process p : processes) {
                ClockOutput = ClockOutput.concat(p.processOutput() + "\n");
            }
            System.out.println(ClockOutput);
        }
    }

    // On start up all processes page fault and grab the appropriate pages from VM
    // Due to all processes faulting on start up, it fast forwards to when all processes have put pages in memory
    // Post startup - Takes the process sitting in the blockedQ and brings the processTime up to the ClockTime
    // If the process has pages to execute - Finds the index of an empty frame and gets the next page from VM
    public void getPages() {
        // Get initial pages for system
        if(clockTime == 0) {
            for (int i = 0; i < processes.size(); i++) {

                memory.getMemory2(readyQ.peek())[0] = readyQ.peek().getNextPage2();

                readyQ.peek().increasePageFaults(clockTime);

                // Making the process time when it can come back into the ReadyQ
                readyQ.peek().increaseProcessTime(clockTime + 6);

                Process temp = readyQ.remove();
                readyQ.add(temp);
            }
            clockTime += 6;
        }

        else {
            // Making process - For ease of reading
            Process p = blockedQ.peek();
            p.increaseProcessTime(clockTime);

            if(p.hasPagesLeft()) {
                // Still has pages to run
                // Put the next page into the frame

                int emptyPage = memory.getEmptyFrameIndex(p, algoType);
                memory.getMemory2(p)[emptyPage] = p.getNextPage2();

                p.increasePageFaults(clockTime);

                p.increaseProcessTime(clockTime + 6);
            }

            else {
                // Process has finished add to the finishedList
                finishedList.add(p);
                blockedQ.remove(p);

            }
        }
    }

    public void runProcess() {
        // Getting the process in readyQ
        currentProcessRunning = readyQ.remove();

        // Run page while the process has the page in its frame memory
        while(currentProcessRunning.hasPagesLeft() && memory.isPageInMemory2(currentProcessRunning.getNextPage2(), currentProcessRunning, clockTime) && currentProcessRunning.getTimeRunning() != qTime) {

            clockTime++;
            currentProcessRunning.increaseProcessTime(clockTime);

            // Deleting the page after executing
            currentProcessRunning.deletePage();

            // Increasing the process running time
            currentProcessRunning.increaseTimeRunning();

            // Need to check if a blockedP to re-join readyQ
            blockedToReady();
        }

        // Process has finished its QTime - Check if it has finished, if not re-add to readyQ
        if(currentProcessRunning.getTimeRunning() == qTime) {

            if(currentProcessRunning.hasPagesLeft()){
                readyQ.add(currentProcessRunning);
                currentProcessRunning.resetRunningTime();
            }
            else {
                finishedList.add(currentProcessRunning);
            }

        }
        else {
            // Has had page fault or has finished before its hit its time Q
            blockedQ.push(currentProcessRunning);
            currentProcessRunning.resetRunningTime();
            getPages();
        }
    }

    // Helper function to run constantly to check if a process is ready to re-join the readyQ
    public void blockedToReady() {
        for(Process p : blockedQ) {
            if(p.getTime() <= clockTime && !readyQ.contains(p) && p != currentProcessRunning) {
                readyQ.add(p);
                blockedQ.remove(p);
            }
        }
    }
}