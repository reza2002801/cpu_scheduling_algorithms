import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class main {
    public static void main(String[] args) throws IOException {
        List<String> final_Args=new ArrayList<>();
        Logger.log("main Server started");

        Scanner scanner = new Scanner(System.in);
        String argNum_temp=scanner.nextLine();
        final_Args.add(argNum_temp);
        int argNum=Integer.parseInt(argNum_temp);
        List<String> arguments = new ArrayList<>();
        for (int i = 0; i < argNum; i++) {
            String t=scanner.nextLine();
            arguments.add(t);
            final_Args.add(t);
        }
        int masterPort=Integer.parseInt(scanner.nextLine());
        final_Args.add(String.valueOf(masterPort));
        int numWorker=Integer.parseInt(scanner.nextLine());
        final_Args.add(String.valueOf(numWorker));
        String Algorithm=scanner.nextLine();
        final_Args.add(Algorithm);
        if(Algorithm.equals("RR")){
            final_Args.add(scanner.nextLine());
        }

        String deadLockHandler=scanner.nextLine();
        final_Args.add(deadLockHandler);
        int StoragePort=Integer.parseInt(scanner.nextLine());
        final_Args.add(String.valueOf(StoragePort));
        String storageData=scanner.nextLine();
        final_Args.add(storageData);
        int TaskNum=Integer.parseInt(scanner.nextLine());
        final_Args.add(String.valueOf(TaskNum));
        List<String> Tasks=new ArrayList<String>();
        for (int i = 0; i < TaskNum; i++) {
            String t=scanner.nextLine();
            Tasks.add(t);
            final_Args.add(t);
        }
        ProcessBuilder p=RunMaster(Master.class,arguments,final_Args);
        Logger.log("Master launched!!!");
        Process s=p.start();
        Scanner in=new Scanner(s.getInputStream());
        PrintStream out=new PrintStream(s.getOutputStream());
//        while (true) {
//            System.out.println(in.nextLine());
//        }


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
}
