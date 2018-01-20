package Test;

import Chord.FileEntry;

public class Message {

    FileEntry requestedFile;
    String msg;

    public Message(FileEntry requestedFile, String msg){
        this.requestedFile = requestedFile;
        this.msg = msg;
    }

    public FileEntry getRequestedFile() {
        return requestedFile;
    }

    public void setRequestedFile(FileEntry requestedFile) {
        this.requestedFile = requestedFile;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
