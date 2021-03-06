package master;

import storage.Storage;
import task.Task;
import worker.Worker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Master  {
    static String deadLockHandler;
    static String Algo;
    static int i=0;
    public static int num_data;
    public static void main(String[] args) throws IOException {
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
        Algo=Algorithm;
        String ddd=arg(args);
        deadLockHandler=ddd;
        final_Args.add(deadLockHandler);

        int StoragePort=Integer.parseInt(arg(args));
        final_Args.add(String.valueOf(StoragePort));
        String storageData=arg(args);

        final_Args.add(storageData);
        num_data=storageData.split(" ").length;
        int TaskNum=Integer.parseInt(arg(args));
        final_Args.add(String.valueOf(TaskNum));
        List<String> Tasks=new ArrayList<String>();
        List<Task> ttt=new ArrayList<>();

        for (int i = 0; i < TaskNum; i++) {
            String t=arg(args);
            Tasks.add(t);
            final_Args.add(t);
            ttt.add(new Task(i,t));
        }



        // run master.Master Server
        try{
            MasterServer masterServer = new MasterServer(masterPort,ttt);
            masterServer.run();
            System.out.println("MasterServer run and initialize");
        }catch (Exception e) {
            System.out.println("error running main server");
        }

        //launch storage
        ProcessBuilder p=RunMaster(Storage.class,arguments,final_Args);

        Process s=p.start();

        //launch workers
        for (int j = 0; j < numWorker; j++) {
            RunMaster(Worker.class,arguments,final_Args).start();
            System.out.println("worker"+String.valueOf(j)+"started!" );
        }




    }

    public static ProcessBuilder RunMaster(Class classs,List<String> jvmArgs,List<String> args){
        String javaHome = jvmArgs.get(0);
        String classpath = jvmArgs.get(2);
        String className = classs.getName();
        List<String> command = new ArrayList<>();
        command.add(javaHome);
        command.add("-cp");
        command.add(classpath);
        command.add(className);
        command.addAll(args);
        return new ProcessBuilder(command);

    }

    public static String arg(String[] t) throws IOException {
        String s=t[i];
        i+=1;

        return s;
    }
}
