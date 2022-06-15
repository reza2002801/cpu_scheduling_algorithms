package storage;

import java.io.IOException;
import java.util.*;

public class LockManager {
    static HashMap<Integer,StorageWorkerHandler> map=new HashMap<Integer, StorageWorkerHandler>();
    int[] values;
    int[] lockId;
    public List<List<Integer>> requests=new ArrayList<>();

    public LockManager(int[] values, int[] lockId) {
        for (int i = 0; i < values.length; i++) {
            List<Integer> temp=new ArrayList<>();
            this.requests.add(temp);
        }
        this.values = values;
        this.lockId = lockId;
    }
    public void handleReq(int index, int id, StorageWorkerHandler storageWorkerHandler) throws IOException {
        if(lockId[index]==-1){
            sL.log("p1");
            lockId[index]=id;
//            map.put(id,storageWorkerHandler);
            storageWorkerHandler.sendResponse("reqAcc");
            storageWorkerHandler.sendResponse(String.valueOf(values[index]));

        }else if(lockId[index]==id){
            sL.log("p2");
//            map.put(id,storageWorkerHandler);
            storageWorkerHandler.sendResponse("reqAcc");
            storageWorkerHandler.sendResponse(String.valueOf(values[index]));

            //none
        }else{
            sL.log("p3");
            if(!requests.get(index).contains(id)) {
                sL.log("p4");
                map.put(id,storageWorkerHandler);
                requests.get(index).add(id);
                sL.log(Collections.singletonList(this.map).toString());
                sL.log(this.requests.toString());
            }
        }
    }
    public void workDone(int id) throws IOException {
        sL.log(Arrays.toString(this.lockId));
        sL.log(Collections.singletonList(this.map).toString());
        sL.log(this.requests.toString());
        for (int i = 0; i < lockId.length; i++) {
            if(lockId[i]==id){
                if(requests.get(i).isEmpty()){
                    lockId[i]=-1;
                }
                else {
                    lockId[i]=requests.get(i).get(0);
                    requests.get(i).remove(0);
                    map.get(lockId[i]).sendResponse("reqAcc");
                    map.get(lockId[i]).sendResponse(String.valueOf(values[i]));
                    map.remove(lockId[i]);
                }
            }
        }
        sL.log(Arrays.toString(lockId));

    }
//    public void removeReq{
//        for (int i = 0; i < requests.size(); i++) {
//            for (int j = 0; j < requests.get(i).size(); j++) {
//                if()
//            }
//
//        }
//    }


}
