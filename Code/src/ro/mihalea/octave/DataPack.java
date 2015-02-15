package ro.mihalea.octave;

/**
 * Created by Mircea on 29-Sep-14.
 */
public class DataPack {
    public String folder;
    public String email;
    public String host;
    public int port;
    public int encryption; //0 = disabled ; 1 = ssl ; 2 = tls
    public long refreshRate;
    public long size;

    public DataPack(String folder, String email, String host, int port, int encryption, long refreshRate, long size) {
        this.folder = folder;
        this.email = email;
        this.host = host;
        this.port = port;
        this.encryption = encryption;
        this.refreshRate = refreshRate;
        this.size = size;
    }
}
