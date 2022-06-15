package task;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Task implements Serializable {

    private int id;
    private List<Integer> miniTasks;
    private int result;
    private int timeNeed;
    private int step;

    public int getStep() {
        return step;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Integer> getMiniTasks() {
        return miniTasks;
    }

    public void setMiniTasks(List<Integer> miniTasks) {
        this.miniTasks = miniTasks;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public int getTimeNeed() {
        return timeNeed;
    }

    public void setTimeNeed(int timeNeed) {
        this.timeNeed = timeNeed;
    }

    public void setStep(int step) {
        this.step = step;
    }
    public void updateVal(int a){
        setResult(getResult()+a);
    }

    public Task(int id, String s) {
        this.id = id;
        this.miniTasks=new ArrayList<>();
        List<String> temp= Arrays.asList(s.split(" "));
        this.result=0;
        this.timeNeed=0;
        for (int i = 0; i < temp.size(); i++) {
            this.miniTasks.add(Integer.valueOf(temp.get(i)));
            if(i%2==0){
                this.timeNeed+=Integer.valueOf(temp.get(i));
            }
        }
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", miniTasks=" + miniTasks +
                ", result=" + result +
                ", timeNeed=" + timeNeed +
                ", step=" + step +
                '}';
    }

    public void updateTask(){
        setStep(getStep()+1);
    }

    public static void main(String[] args) {
        master.Master.num_data=4;
        Task t=new Task(1,"0 1 100 2 100 3");
        System.out.println(t.toString());
    }
}
