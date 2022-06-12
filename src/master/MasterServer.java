package master;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MasterServer implements Runnable {


    private ServerSocket serverSocket;
    private int port;
    private Socket clientSocket;
    private List<MasterWorkerHandler> MasterWorkerHandler;

    public MasterServer(int port) throws IOException {
        this.port = port;
        this.MasterWorkerHandler = new ArrayList<>();
        establishServer();
    }
    @Override
    public void run() {
//        Thread thread=new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while (true){
//                        // do master.Master Work
//                }
//            }
//        });
//        thread.start();
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


    }
    private void establishServer() throws IOException {
        serverSocket = new ServerSocket(port);
    }
    private void listenForNewConnection() throws IOException {
//        System.out.println(port);
        while (true) {
            try {

                clientSocket = this.serverSocket.accept();

                DataInputStream dis = new DataInputStream(clientSocket.getInputStream());
                DataOutputStream dos = new DataOutputStream(clientSocket.getOutputStream());
                int id = createUid();
                MasterWorkerHandler workerHandler = new MasterWorkerHandler(id, clientSocket, dis, dos);
                MasterWorkerHandler.add(workerHandler);
                System.out.println("worker connected");

            } catch (IOException e) {
                System.out.println("error conncting worker");
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
