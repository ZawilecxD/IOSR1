package pl.agh.iosr.utils;



import java.io.*;

/**
 * Created by Murzynas on 2016-11-05.
 */
public class ContentDTO implements Serializable{

    public enum OperationType{
        ADD(1), DELETE(0);

        int type;

        OperationType(int i){
            type = i;
        }


        @Override
        public String toString() {
            return type == 0 ? "DELETE" : "ADD";
        }
    }

    /*type:
    * 0 - usuniecie
    * 1 - dodanie
    * */
    private OperationType type;
    private String key;
    private String value = " ";

    public ContentDTO() {}

    public ContentDTO withKey(String key) {
        setKey(key);
        return this;
    }

    public ContentDTO withValue(String value) {
        setValue(value);
        return this;
    }

    public ContentDTO withType(OperationType type) {
        setType(type);
        return this;
    }

    public byte[] toByteArray() throws IOException{
        return (type.toString() + ";" + key + ";" + value).getBytes();
    }

    public static ContentDTO fromByteArray(byte[] bytes) throws IOException, ClassNotFoundException{
        String str = new String(bytes);
        String[] split = str.split(";");
        String type = split[0];
        String key = split[1];
        String value = split[2];
        return new ContentDTO().withType(type.equals("DELETE")? OperationType.DELETE : OperationType.ADD).withKey(key).withValue(value);

    }

    public OperationType getType() {
        return type;
    }

    public void setType(OperationType type) {
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ContentDTO that = (ContentDTO) o;

        if (type != that.type) return false;
        if (key != null ? !key.equals(that.key) : that.key != null) return false;
        return value != null ? value.equals(that.value) : that.value == null;

    }

    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + (key != null ? key.hashCode() : 0);
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return type.toString()+":"+key+":"+value;
    }
}



