package master;

import masterlogic.MasterFCFS;
import masterlogic.MasterSJF;
import task.Task;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static master.Master.deadLockHandler;

public class MasterServer implements Runnable {
    public volatile static List<Task> works;
    private ServerSocket serverSocket;
    private int port;
    private Socket clientSocket;
     public  volatile static List<MasterWorkerHandler> MasterWorkerHandlers;

    public MasterServer(int port,List<Task> tasks) throws IOException {
        works=tasks;
        this.port = port;
        establishServer();
    }

    @Override
    public void run() {
        MasterWorkerHandlers=new ArrayList<>();
        Thread connctionThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    listenForNewConnection();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        connctionThread.start();

        Thread senderWork=new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    if(Master.Algo.equals("FCFS")){
                        try {
                            MasterFCFS.handleTasks(deadLockHandler);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    else if(Master.Algo.equals("SJF")){
                        try {
                            MasterSJF.handleTasks(deadLockHandler);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    else if(Master.Algo.equals("RR")){

                    }

                }
            }
        });
        senderWork.start();

    }
    private void establishServer() throws IOException {
        serverSocket = new ServerSocket(port);
    }
    public void listenForNewConnection() throws IOException {
        while (true) {
            try {
                clientSocket = this.serverSocket.accept();
                DataInputStream dis = new DataInputStream(clientSocket.getInputStream());
                DataOutputStream dos = new DataOutputStream(clientSocket.getOutputStream());
                int id = createUid();
                MasterWorkerHandler workerHandler = new MasterWorkerHandler(id, clientSocket, dis, dos);
                MasterWorkerHandlers.add(workerHandler);

                System.out.println("worker connected");
            } catch (IOException e) {
                System.out.println("error conncting worker");
                clientSocket.close();
                e.printStackTrace();
            }
            System.out.println(MasterWorkerHandlers);
        }
    }
    private int createUid() {
        Random rand = new Random();
        int maxNumber = 9999;
        int minNumber = 1000;
        return rand.nextInt(maxNumber) + minNumber;
    }
}
