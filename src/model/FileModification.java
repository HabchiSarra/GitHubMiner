package model;

/**
 * Created by sarra on 15/02/17.
 */
public class FileModification {

    int additions;
    int deletions;
    String patch;
    String fileName;
    String status;  //TODO what are the types?

    public FileModification(int additions, int deletions, String patch, String fileName, String status) {
        this.additions = additions;
        this.deletions = deletions;
        this.patch = patch;
        this.fileName = fileName;
        this.status = status;
    }

    public int getAdditions() {
        return additions;
    }

    public void setAdditions(int additions) {
        this.additions = additions;
    }

    public int getDeletions() {
        return deletions;
    }

    public void setDeletions(int deletions) {
        this.deletions = deletions;
    }

    public String getPatch() {
        return patch;
    }

    public void setPatch(String patch) {
        this.patch = patch;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
