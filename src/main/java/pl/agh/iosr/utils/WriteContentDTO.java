package pl.agh.iosr.utils;

/**
 * Created by Murzynas on 2016-11-05.
 */
public class WriteContentDTO {
    private String fileName;
    private String text;

    public WriteContentDTO() {}

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
