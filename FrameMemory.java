
// FrameMemory class - Has a hashmap to give each process its own frame memory with the pages
//                     it can currently access, implements the LRU and Clock replacement - Clock replacement uses the changePointer method
//                     Hashmap may have been a bit overkill but I used it as practice as I haven't used them before :)

import java.util.HashMap;

public class FrameMemory {

    private HashMap<Process, Page[]> pages = new HashMap<>();

    public FrameMemory(int frameSize) {
    }

    // Adds to hashmap - gives the process the frames it has access too
    public void addProcess(Process p, int memoryFrame) {
        Page[] test1 = new Page[memoryFrame];
        pages.put(p, test1);
    }

    // Returns the processes frame memory array
    public Page[] getMemory2(Process p) {
        return pages.get(p);
    }

    // Checks if the indicated page is in the processes frame memory
    // If it is hit the bit on the process used for clock replacement algorithm
    // and return true, else return false
    public boolean isPageInMemory2(Page p, Process process, int time) {
        for (Page page : pages.get(process)) {
            // If pageID == pID, use the Page object
            if (page != null && page.getPageID() == p.getPageID()) {
                page.setTimeLastUsed(time + 1);
                if (page.getBit() == 1) {
                    page.hitBit();
                }
                return true;
            }
        }
        return false;
    }

    // Finds the processes empty frame index
    public int getEmptyFrameIndex(Process p, int algoType) {
        int count = 0;

        for (Page page : getMemory2(p)) {

            if (page == null) {
                return count;
            }
            count++;
        }

        // Process has no spare memory - Need to do LRU/Clock to replace
        // Use algotype to do either LRU or clock
        if (algoType == 0) {
            int lowestTime = Integer.MAX_VALUE;
            count = 0;
            int index = 0;

            for (Page page : getMemory2(p)) {

                if (page.getTimeLastUsed() < lowestTime) {

                    lowestTime = page.getTimeLastUsed();
                    index = count;

                }
                count++;

            }

            return index;
        }
        return changePointer(p);
    }

    // Used for the clock replacement algorithm, iterates through the processes frames starting
    // from the pointer position - if the bit of the process hits 0 - replace that page with the new one
    // and change the pointer position. Else if the process gets hit again change the bit noting its had a second
    // chance
    public int changePointer(Process p) {

        // Start from the frame the pointer is at
        for (int i = p.getPointer(); i < getMemory2(p).length; i++) {

            if(pages.get(p)[i].getBit() == 0) {
                // Hit the frame to replace the page with
                // increment pointer and send back index

                if(p.getPointer() == getMemory2(p).length) {
                    p.setPointer(0);
                }
                else {
                    p.increasePointer();
                }
                return i;
            }

            pages.get(p)[i].hitBit();

            if(p.getPointer() == getMemory2(p).length) {
                p.setPointer(0);
            }

        }
        return changePointer(p);
    }
}