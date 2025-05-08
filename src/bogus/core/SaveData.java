package bogus.core;

public class SaveData {
    public byte[] data;

    public void overwrite(String data){
        byte[] newData = new byte[data.length()];
        for(int i = 0; i < data.length(); i++){
            newData[i] = (byte)data.charAt(i);
        }
        this.data = newData;
    }

    public SaveData(int size){
        this.data = new byte[size];
    }

    public SaveData(){
        this(0);
    }

    public String toString(){
        String out = "";
        for(byte b : data){
            out += (char)b;
        }
        return out;
    }
}
