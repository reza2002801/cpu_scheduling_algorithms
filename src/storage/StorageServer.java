package storage;

import storageLogic.LockManagerFCFS;
import storageLogic.LockManagerRR;
import storageLogic.LockManagerSJF;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;



public class StorageServer implements Runnable {
    public static LockManagerFCFS lockManagerFCFS;
    public static LockManagerSJF lockManagerSJF;
    public static LockManagerRR lockManagerRR;
    private ServerSocket serverSocket;
    private int port;
    private Socket clientSocket;
    public volatile static List<StorageWorkerHandler> StorageWorkerHandler;
    private String Algorithm;
    public StorageServer(int port,String Algorithm) throws IOException {
        sL.log(String.valueOf(port));
        this.port = port;
        StorageWorkerHandler = new ArrayList<>();
        this.Algorithm = Algorithm;
        establishServer();
    }
    @Override
    public void run() {
        if(this.Algorithm.equals("FCFS")){
            int[] p=new int[Storage.StorageData.length];
            Arrays.fill(p,-1);
            LockManagerFCFS l=new LockManagerFCFS(Storage.StorageData,p);
            this.lockManagerFCFS =l;
            try {
                sL.log(l.toString());
                sL.log(Storage.StorageData.toString());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        else if(this.Algorithm.equals("SJF")){
            int[] p=new int[Storage.StorageData.length];
            Arrays.fill(p,-1);
            LockManagerSJF l=new LockManagerSJF(Storage.StorageData,p);
            this.lockManagerSJF =l;
            try {
                sL.log(l.toString());
                sL.log(Storage.StorageData.toString());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        else if(this.Algorithm.equals("RR")){

        }

        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    sL.log("start connction in storage");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                try{
                    listenForNewConnection();
                } catch (IOException e) {
                    try {
                        sL.log(e.getMessage());
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
    private void establishServer() throws IOException {
        sL.log(String.valueOf(port));
        try {
            serverSocket = new ServerSocket(port);
        }catch (Exception e){
            sL.log(e.getMessage());
        }
    }
    private void listenForNewConnection() throws IOException {
        while (true) {
            try {
                clientSocket = this.serverSocket.accept();
                DataInputStream dis = new DataInputStream(clientSocket.getInputStream());
                DataOutputStream dos = new DataOutputStream(clientSocket.getOutputStream());
                int id = createUid();
                try {
                    StorageWorkerHandler storageWorkerHandler = new StorageWorkerHandler(id, clientSocket, dis, dos);
                    StorageWorkerHandler.add(storageWorkerHandler);
                }
                catch (Exception e){
                    sL.log(e.getMessage());
                }

                sL.log("worker connected");
            } catch (IOException e) {
                clientSocket.close();
                e.printStackTrace();
            }
        }
    }
    private int createUid() {
        Random rand = new Random();
        int maxNumber = 9999;
        int minNumber = 1000;

        return rand.nextInt(maxNumber) + minNumber;
    }
}
