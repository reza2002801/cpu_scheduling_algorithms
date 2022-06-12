package worker;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class workerLogger {
    static String name="filename.txt";
    static synchronized void log(String l) throws IOException {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(" HH:mm:ss.nn");
        LocalDateTime now = LocalDateTime.now();
        try {
            List<String> lines = new ArrayList<String>();
            File myObj = new File(name);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                lines.add(myReader.nextLine());
            }
            myReader.close();
            l=dtf.format(now)+"  "+l;
            lines.add(l);
            FileWriter myWriter = new FileWriter(name);
            for (int i = 0; i < lines.size(); i++) {
                myWriter.write(lines.get(i)+"\n");
            }
            myWriter.close();
        } catch (Exception e) {
            FileWriter myWriter = new FileWriter(name);
            l=dtf.format(now)+"  "+l;
            myWriter.write(l);
            myWriter.close();
        }

    }

}
