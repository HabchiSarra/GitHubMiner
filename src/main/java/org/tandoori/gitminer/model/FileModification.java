package org.tandoori.gitminer.model;
/**
 * Created by sarra on 15/02/17.
 */
public class FileModification {

    private int additions;
    private int deletions;
    private String patch;
    private String fileName;
    private FileStatus status;
    private String sha;

    public FileModification(int additions, int deletions, String fileName, FileStatus status, String sha) {
        this.additions = additions;
        this.deletions = deletions;
        this.sha =sha;
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

    public FileStatus getStatus() {
        return status;
    }

    public void setStatus(FileStatus status) {
        this.status = status;
    }

    public String getSha() {
        return sha;
    }

    public void setSha(String sha) {
        this.sha = sha;
    }
}
