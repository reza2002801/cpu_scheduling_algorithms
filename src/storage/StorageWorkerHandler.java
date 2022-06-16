package storage;

import storageLogic.LockManagerFCFS;
import storageLogic.LockManagerSJF;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;

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
                if(Storage.Alg.equals("FCFS")){
                    try {
                        sL.log("in Reciever from  SWH");
                        RecieverFCFS();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                else if(Storage.Alg.equals("SJF")){
                    try {
                        RecieverSJF();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
                else if(Storage.Alg.equals("RR")){

                }

            }
        });
        thread.start();
    }
    public void RecieverFCFS() throws IOException, ClassNotFoundException {
        while (true) {
            String result=dis.readUTF();
            sL.log("in Reciever from  SWH"+result);

            if(result.equals("LockReq")){
                int id=Integer.parseInt(dis.readUTF());
                int index=Integer.parseInt(dis.readUTF());
                sL.log(String.valueOf(id)+" "+String.valueOf(index));
                StorageServer.lockManagerFCFS.handleReq(index,id,this);
            }
            else if(result.equals("WorkDone")){
                int id=Integer.parseInt(dis.readUTF());
                StorageServer.lockManagerFCFS.workDone(id);
            }
        }
    }
    public void RecieverSJF() throws IOException, ClassNotFoundException {
        while (true) {
            String result=dis.readUTF();
            sL.log("in Reciever from  SWH"+result);

            if(result.equals("LockReq")){
                int id=Integer.parseInt(dis.readUTF());
                int index=Integer.parseInt(dis.readUTF());
                sL.log(String.valueOf(id)+" "+String.valueOf(index));
                StorageServer.lockManagerSJF.handleReq(index,id,this);
            }
            else if(result.equals("WorkDone")){
                int id=Integer.parseInt(dis.readUTF());
                StorageServer.lockManagerSJF.workDone(id);
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
