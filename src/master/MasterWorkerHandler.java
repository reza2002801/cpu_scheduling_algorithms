package master;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class MasterWorkerHandler {
    private int id;
    private Socket clientSocket;
    private DataInputStream dis;
    private DataOutputStream dos;

    private int w;
    public MasterWorkerHandler(int id, Socket clientSocket, DataInputStream dis, DataOutputStream dos) throws IOException {
        this.w=0;
        this.id = id;
        this.clientSocket = clientSocket;
        this.dis = dis;
        this.dos = dos;

        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Reciever();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
    public void Reciever() throws IOException, ClassNotFoundException {
        while (true) {
            String result=dis.readUTF();

        }
    }

    public int getW() {
        return w;
    }

    public void setW(int w) {
        this.w = w;
    }

    private void sendRequest(String request) throws IOException {
        dos.writeUTF(request);
    }

    private String listenForResponse() throws IOException {
        return dis.readUTF();
    }



    public void sendWork() throws IOException {
        dos.writeUTF("work");


    }
}
