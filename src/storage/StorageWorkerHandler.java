package storage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class StorageWorkerHandler {
    private int id;
    private Socket clientSocket;
    private DataInputStream dis;
    private DataOutputStream dos;

    private int w;
    public StorageWorkerHandler(int id, Socket clientSocket, DataInputStream dis, DataOutputStream dos) throws IOException {
        sL.log("sdc");
        this.w=0;
        this.id = id;
        this.clientSocket = clientSocket;
        this.dis = dis;
        this.dos = dos;

        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    sL.log("in Reciever from  SWH");
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
            sL.log("in Reciever from  SWH"+result);

            if(result.equals("LockReq")){
                int id=Integer.parseInt(dis.readUTF());
                int index=Integer.parseInt(dis.readUTF());
                sL.log(String.valueOf(id)+" "+String.valueOf(index));
                StorageServer.lockManager.handleReq(index,id,this);
            }
            else if(result.equals("WorkDone")){
                int id=Integer.parseInt(dis.readUTF());
                StorageServer.lockManager.workDone(id);
            }
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



    public void sendResponse(String s) throws IOException {
        dos.writeUTF(s);
    }
}
