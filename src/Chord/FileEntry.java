package Chord;

import java.io.File;
import java.io.Serializable;

public class FileEntry implements Serializable{

    File file;
    String fileData;


    public FileEntry(File file, String fileData) {
        this.file = file;
        this.fileData = fileData;
    }


    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getFileData() {
        return fileData;
    }

    public void setFileData(String fileData) {
        this.fileData = fileData;
    }


    public String toString(){
        return " " + file.getName() + "\n" + fileData;
    }
}
