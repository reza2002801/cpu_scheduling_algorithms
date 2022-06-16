package master;

import Serializer.serializer;
import task.Task;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class MasterWorkerHandler {
    private int id;
    private Socket clientSocket;
    private DataInputStream dis;
    private DataOutputStream dos;
    private boolean isFull;
    private int w;
    public MasterWorkerHandler(int id, Socket clientSocket, DataInputStream dis, DataOutputStream dos) throws IOException {
        this.w=0;
        this.id = id;
        this.clientSocket = clientSocket;
        this.dis = dis;
        this.dos = dos;
        this.isFull=false;
        System.out.println("worker done");

        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Reciever();
                    } catch (IOException e) {
                        e.printStackTrace();
                        break;
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                        break;
                    }
                }
            }
        });
        thread.start();
    }

    public boolean isFull() {
        return isFull;
    }

    public void setFull(boolean full) {
        isFull = full;
    }

    public void Reciever() throws IOException, ClassNotFoundException {
        System.out.println("sx");
        String result=dis.readUTF();
        System.out.println("sxas");
            if(Master.Algo.equals("FCFS")){
                if(result.equals("workRes")){
                    Task t= (Task) serializer.fromString(dis.readUTF());
                    System.out.println("task "+String.valueOf(t.getId())+" executed successfully with result "+String.valueOf(t.getResult()));
                    this.isFull=false;
                }
            }
            else if(Master.Algo.equals("SJF")){
                if(result.equals("workRes")){
                    Task t= (Task) serializer.fromString(dis.readUTF());
                    System.out.println("task "+String.valueOf(t.getId())+" executed successfully with result "+String.valueOf(t.getResult()));
                    this.isFull=false;
                }
            }
            else if(Master.Algo.equals("RR")){

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



    public void sendWork(Task t) throws IOException {
        dos.writeUTF("work");
        dos.writeUTF(serializer.toString(t));
        setFull(true);
    }
}
