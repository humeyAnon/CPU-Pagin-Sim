
// Page class - Used to store information about the pages the processes use
//              Mainly used to help with the replacement algorithms - To store the time
//              and bit information - Also methods with no comments are self explanatory

public class Page {

    private int pageID, bit, timeLastUsed;

    // change bit to start at 1 - All pages start with a bit of 1 - If they are hit the bit then changes to 0 as they are given
    // a second chance - the first page to hit with a bit of 0 will be changed out
    public Page(int pageID) {
        this.pageID = pageID;
        this.bit = 1;
    }

    public int getBit() {
        return bit;
    }

    public int getPageID() {
        return pageID;
    }

    public int getTimeLastUsed() {
        return timeLastUsed;
    }

    public void setTimeLastUsed(int timeLastUsed) {
        this.timeLastUsed = timeLastUsed;
    }

    // Method to change the pages bit status - If it has had a second chance it changes to a 0
    public void hitBit() {

        if(bit == 1) {
            this.bit = 0;
        }
        else {
            this.bit = 1;
        }
    }
}
