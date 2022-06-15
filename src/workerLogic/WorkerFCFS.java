package workerLogic;

import Serializer.serializer;
import master.MasterServer;
import storage.Storage;
import storage.StorageServer;
import storage.StorageWorkerHandler;
import storage.sL;
import task.Task;
import worker.WorkerServer;
import worker.workerLogger;

import java.io.IOException;

import static worker.WorkerServer.*;

public class WorkerFCFS {
    static String deadLockHandler;

    public static void handleTasks(String deadLockHandler) throws IOException, InterruptedException {
        deadLockHandler=deadLockHandler;
        workerLogger.log("in handle Tasks");
        while(!works.isEmpty()) {
            Task t = null;
            if (deadLockHandler.equals("NONE")) {
                t = doWork(WorkerServer.works.get(0));
            }
            mdos.writeUTF("workRes");
            mdos.writeUTF(serializer.toString(t));
            sdos.writeUTF("WorkDone");
            sdos.writeUTF(String.valueOf(t.getId()));
            works.remove(0);
        }

    }

    private static Task doWork(Task t) throws InterruptedException, IOException {
        workerLogger.log("in doWork");
        for (int i = 0; i < t.getMiniTasks().size(); i++) {
            if(i%2==0){
                Sleep(t.getMiniTasks().get(i));
            }else{

                int f=requestForValue(t.getMiniTasks().get(i),t.getId());
                t.updateVal(f);
            }
            t.updateTask();
            workerLogger.log(t.toString());
        }
        return t;
    }

    private static int requestForValue(Integer integer,int id) throws IOException {
        sdos.writeUTF("LockReq");
        workerLogger.log("LOck requested");
        sdos.writeUTF(String.valueOf(id));
        sdos.writeUTF(String.valueOf(integer));
        String result = sdis.readUTF();
        sL.log(result);
        if(result.equals("reqAcc")){
            int res=Integer.parseInt(sdis.readUTF());
            return res;
        }
        return -1;

    }

    public static void Sleep(int st) throws InterruptedException {
        Thread.sleep(st);
    }

}
