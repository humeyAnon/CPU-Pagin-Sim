
// Process class - Stores all information about the processes
//                 Holds an arrayList of all the pages it needs to process before its finished
//                 NOTE - Most methods dont have comments as I feel they are self explanatory

import java.util.ArrayList;

public class Process {

    private ArrayList<Integer> pagesNeeded = new ArrayList<>();
    private int pID, frameMemory, time, numberPageFaults, processTime, currentPageRunning, timeRunning;
    private boolean finished;
    private String output = "";
    private String pageFaultTimes = "{";
    private int pointer = 0;

    public Process(int pID) {
        this.pID = pID;
        this.time = 0;
        this.finished = false;
        this.numberPageFaults = 0;
        this.processTime = 0;
        this.timeRunning = 0;
    }

    // GETTERS & SETTERS
    public int getPointer() {
        return pointer;
    }

    public void setPointer(int pointer) {
        this.pointer = pointer;
    }

    public void increasePointer(){
        this.pointer++;
    }

    public String processOutput() {

        pageFaultTimes = pageFaultTimes.substring(0, pageFaultTimes.lastIndexOf(","));

        output = output.concat(pID + "\tProcess" + pID + ".txt\t" + processTime + "\t\t" + numberPageFaults + "\t\t" + pageFaultTimes.concat("}"));

        return output;
    }

    public void increaseTimeRunning() {
        this.timeRunning++;
    }

    public int getTimeRunning() {
        return timeRunning;
    }

    public void resetRunningTime(){
        this.timeRunning = 0;
    }

    public void addPage(int pageNo) {
        pagesNeeded.add(pageNo);
    }

    public void setFrameMemory(int frameMemory) {
        this.frameMemory = frameMemory;
    }

    public int getFrameMemory() {
        return frameMemory;
    }

    public boolean hasPagesLeft() {
        if(pagesNeeded.isEmpty()){
            return false;
        }
        return true;
    }

    public Page getNextPage2(){

        return new Page(pagesNeeded.get(0));
    }

    public void increasePageFaults(int time){
        this.numberPageFaults++;
        pageFaultTimes = pageFaultTimes.concat(time + ", ");
    }

    public void deletePage(){
        pagesNeeded.remove(0);
    }

    public void increaseProcessTime(int time) {
        this.processTime = time;
    }

    public int getTime() {
        return processTime;
    }
}
