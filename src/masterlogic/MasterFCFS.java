package masterlogic;

import master.Master;
import master.MasterServer;
import master.MasterWorkerHandler;
import task.Task;

import java.io.IOException;

public class MasterFCFS {
    static String deadLockHandler;

    public static void handleTasks(String deadLockHandler) throws IOException {
        deadLockHandler=deadLockHandler;
        if(deadLockHandler.equals("NONE")){

            sendWork();
        }

    }
    private static void sendWork() throws IOException {


        while(!MasterServer.works.isEmpty()){
            while(!canSend());
            System.out.println();
            send(MasterServer.works.get(0));
            System.out.println(MasterServer.works.get(0)+" sentt");
            MasterServer.works.remove(0);
        }
    }
    private static boolean canSend(){
        for (int i = 0; i < MasterServer.MasterWorkerHandlers.size(); i++) {
            if(!MasterServer.MasterWorkerHandlers.get(i).isFull()){

                return true;
            }

        }
//        System.out.println("No");
        return false;
    }
    private static void send(Task t) throws IOException {
        for (int i = 0; i < MasterServer.MasterWorkerHandlers.size(); i++) {
            if(!MasterServer.MasterWorkerHandlers.get(i).isFull()){
                MasterServer.MasterWorkerHandlers.get(i).sendWork(t);
                return;
            }
        }
    }
}
