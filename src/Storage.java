import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Storage {
    static int i=0;
    public static void main(String[] args) throws IOException, InterruptedException {
        Logger.log(" Storage started");
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
        // run StorageServer
        StorageServer storageServer=new StorageServer(StoragePort,Algorithm);
        storageServer.run();
    }

    public static String arg(String[] t) throws IOException {
        String s=t[i];
        i+=1;

        return s;
    }
}
