package worker;
import task.Task;
import workerLogic.WorkerFCFS;
import workerLogic.WorkerSJF;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


public class WorkerServer extends Thread {
    private int masterPort;
    private int storagePort;
    private Socket mastersocket;
    private Socket storageSocket;
    public static DataInputStream mdis;
    public static DataOutputStream mdos;
    public static DataInputStream sdis;
    public static DataOutputStream sdos;

    public volatile static List<Task> works;
    public WorkerServer(int masterPort, int storagePort) throws IOException {
        works=new ArrayList<>();
        this.masterPort = masterPort;
        this.storagePort = storagePort;
        wL.log("constructed done");
    }

    public void start(){
        try {
            establishConnectionMaster(masterPort);
            establishConnectionStorage(storagePort);
        } catch (IOException e) {
            e.printStackTrace();
            try {
                wL.log("failed to establish connection ");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        Thread worker = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if(Worker.Algorithms.equals("FCFS")){
                        try {
                            WorkerFCFS.handleTasks(Worker.DeadLockMode);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    else if(Worker.Algorithms.equals("SJF")){
                        try {
                            WorkerSJF.handleTasks(Worker.DeadLockMode);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    else if(Worker.Algorithms.equals("RR")){

                    }

                }
            }
        });
        worker.start();
        Thread masterRecieve = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if(Worker.Algorithms.equals("FCFS")){
                        try {
                            recieveFCFS();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        } catch (ClassNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    else if(Worker.Algorithms.equals("SJF")){
                        try {
                            recieveSJF();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        } catch (ClassNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    else if(Worker.Algorithms.equals("RR")){

                    }
                }
            }
        });
        masterRecieve.start();
    }

    private void recieveFCFS() throws IOException, ClassNotFoundException {
        String result=mdis.readUTF();
        if(result.equals("work")){
            Task t= (Task) Serializer.serializer.fromString(mdis.readUTF());
            wL.log(t.toString());
            works.add(t);
            wL.log(works.toString());
        }
    }
    private void recieveSJF() throws IOException, ClassNotFoundException {
        String result=mdis.readUTF();
        if(result.equals("work")){
            Task t= (Task) Serializer.serializer.fromString(mdis.readUTF());
            wL.log(t.toString());
            works.add(t);
            wL.log(works.toString());
        }
    }

    private void establishConnectionMaster(int masterPort) throws IOException {
        try{
            this.mastersocket = new Socket(InetAddress.getLocalHost(), masterPort);
            mdis = new DataInputStream(mastersocket.getInputStream());
            mdos = new DataOutputStream(mastersocket.getOutputStream());
            wL.log("success to stablish connction with master");
        }catch (Exception e){
            wL.log("faild to stablish connction with master");
        }
    }
    private void establishConnectionStorage(int storagePort) throws IOException {
        try{
            this.storageSocket = new Socket(InetAddress.getLocalHost(), storagePort);
            sdis = new DataInputStream(storageSocket.getInputStream());
            sdos = new DataOutputStream(storageSocket.getOutputStream());
            wL.log("success to stablish connction with storage");
        }catch (Exception e){
            wL.log("faild to stablish connction with storage");
        }
    }
}
