package worker;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Worker extends Thread{
    static int i=0;
    private int masterPort;
    private int storagePort;
    private Socket mastersocket;
    private Socket storageSocket;
    public static DataInputStream mdis;
    public static DataOutputStream mdos;
    public static DataInputStream sdis;
    public static DataOutputStream sdos;
    public static String Algorithms;
    public static String DeadLockMode;
    static List<String> works;
    public Worker(int masterPort, int storagePort) {
        this.masterPort = masterPort;
        this.storagePort = storagePort;
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        workerLogger.log(" worker.Worker started");
        List<String> final_Args=new ArrayList<>();
        String argNum_temp=arg(args);
        final_Args.add(argNum_temp);
        int argNum=Integer.parseInt(argNum_temp);
        List<String> arguments = new ArrayList<>();
        for (int i = 0; i < argNum; i++) {
            String t=arg(args);
            arguments.add(t);
            final_Args.add(t);
        }
        int masterPort=Integer.parseInt(arg(args));
        final_Args.add(String.valueOf(masterPort));
        int numWorker=Integer.parseInt(arg(args));
        final_Args.add(String.valueOf(numWorker));
        String Algorithm=arg(args);
        final_Args.add(Algorithm);
        if(Algorithm.equals("RR")){
            String s=arg(args);
            final_Args.add(s);
            int qTime=Integer.parseInt(s);

        }
        String deadLockHandler=arg(args);
        final_Args.add(deadLockHandler);

        int StoragePort=Integer.parseInt(arg(args));
        final_Args.add(String.valueOf(StoragePort));
        String storageData=arg(args);

        final_Args.add(storageData);
        int TaskNum=Integer.parseInt(arg(args));
        final_Args.add(String.valueOf(TaskNum));
        List<String> Tasks=new ArrayList<String>();

        for (int i = 0; i < TaskNum; i++) {
            String t=arg(args);
            Tasks.add(t);
            final_Args.add(t);
        }
        DeadLockMode=deadLockHandler;
        Algorithms=Algorithm;
        //start worker
        Worker worker=new Worker(masterPort,StoragePort);
        worker.start();
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
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (Algorithms.equals("FCFS") && DeadLockMode.equals("NONE")) {
                        FCFS_NONE();
                    }
                }
            }
        });
//        thread.start();
    }
    private void FCFS_NONE(){
        while (!works.isEmpty()) {
            List<String> list = Arrays.asList(works.get(0).split(" "));
            System.out.println(list);
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

    public static String arg(String[] t) throws IOException {
        String s=t[i];
        i+=1;

        return s;
    }
}
