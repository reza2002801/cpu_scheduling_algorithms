package masterlogic;

import master.MasterServer;
import task.Task;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MasterSJF {
    static String deadLockHandler;

    public static void handleTasks(String deadLockHandler) throws IOException {
        deadLockHandler=deadLockHandler;
        MasterServer.works=sortByTimeNeed(MasterServer.works);

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
    public static List<Task> sortByTimeNeed(List<Task> tasks){
        Collections.sort(tasks, new Comparator<Task>() {
            @Override
            public int compare(Task t1, Task t2) {
                if ( t1.getTimeNeed() >= t2.getTimeNeed() )
                    return 1;
                else
                    return -1;

            }
        });
        return tasks;
    }
}
