package worker;
import task.Task;
import workerLogic.WorkerFCFS;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
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
        workerLogger.log("constructed done");
    }

    public void start(){
        try {
            establishConnectionMaster(masterPort);
            establishConnectionStorage(storagePort);
        } catch (IOException e) {
            e.printStackTrace();
            try {
                workerLogger.log("failed to establish connection ");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }

        Thread storageSend = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if(Worker.Algorithms.equals("FCFS")){

                    }
                    else if(Worker.Algorithms.equals("SJF")){

                    }
                    else if(Worker.Algorithms.equals("RR")){

                    }
                }
            }
        });
        Thread storageRecieve = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                }
            }
        });
        Thread worker = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        WorkerFCFS.handleTasks(Worker.DeadLockMode);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
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
            workerLogger.log(t.toString());
            works.add(t);
            workerLogger.log(works.toString());
        }
    }

    private void establishConnectionMaster(int masterPort) throws IOException {
        try{
            this.mastersocket = new Socket(InetAddress.getLocalHost(), masterPort);
            mdis = new DataInputStream(mastersocket.getInputStream());
            mdos = new DataOutputStream(mastersocket.getOutputStream());
            workerLogger.log("success to stablish connction with master");
        }catch (Exception e){
            workerLogger.log("faild to stablish connction with master");
        }
    }
    private void establishConnectionStorage(int storagePort) throws IOException {
        try{
            this.storageSocket = new Socket(InetAddress.getLocalHost(), storagePort);
            sdis = new DataInputStream(storageSocket.getInputStream());
            sdos = new DataOutputStream(storageSocket.getOutputStream());
            workerLogger.log("success to stablish connction with storage");
        }catch (Exception e){
            workerLogger.log("faild to stablish connction with storage");
        }
    }
}
