
import master.Master;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class main {
    public static void main(String[] args) throws IOException {
        List<String> final_Args=new ArrayList<>();
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
        String[] arr = new String[final_Args.size()];
        for (int i = 0; i < final_Args.size(); i++)
            arr[i] = final_Args.get(i);
        Master.main(arr);
    }

}
