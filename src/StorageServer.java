import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StorageServer implements Runnable {

    private ServerSocket serverSocket;
    private int port;
    private Socket clientSocket;
    private List<StorageWorkerHandler> StorageWorkerHandler;

    public StorageServer(int port) throws IOException {
        this.port = port;
        this.StorageWorkerHandler = new ArrayList<>();
    }
    @Override
    public void run() {
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    // do Master Work
                }
            }
        });
        thread.start();
        try{
            listenForNewConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private void establishServer() throws IOException {
        serverSocket = new ServerSocket(port);
    }
    private void listenForNewConnection() throws IOException {
        while (true) {
            try {
                clientSocket = this.serverSocket.accept();
                DataInputStream dis = new DataInputStream(clientSocket.getInputStream());
                DataOutputStream dos = new DataOutputStream(clientSocket.getOutputStream());
                int id = createUid();
                StorageWorkerHandler storageWorkerHandler = new StorageWorkerHandler(id, clientSocket, dis, dos);
                StorageWorkerHandler.add(storageWorkerHandler);

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