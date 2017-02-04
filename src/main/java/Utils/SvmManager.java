package Utils;

import Objects.TravelRecord;
import libsvm.svm;
import libsvm.svm_model;
import libsvm.svm_node;
import libsvm.svm_parameter;

import java.io.*;

/**
 * Created by thibault on 20/12/2016.
 */
public class SvmManager {

    public static String training_path = "src/ressources/training";
    public static String model_path = "src/ressources/model";
    public static BufferedWriter output;

    public SvmManager(){
        try {
            output = new BufferedWriter(new FileWriter(training_path, true));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
    write record to file
        1 1:0.583333 2:-1 3:0.333333 4:-0.603774 ...
     */

    public void open(){
        try {
            output = new BufferedWriter(new FileWriter(training_path, true));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void write(TravelRecord records){
        System.out.println("writing records " + records);
        try {
            output.write(records+"\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void train(){
        try {
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        svm_parameter param = new svm_parameter();
        svm_train t = new svm_train();
        String argv[] = {"-s","0","-c","5","-t","2","-g","0.5","-e","0.1",training_path,model_path};
        try {
            t.run(argv);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int predict(TravelRecord record) throws IOException {
        svm_model model = svm.svm_load_model(model_path);
        svm_node[] nodes = record.getNodes();
        int res = (int) svm.svm_predict(model,nodes);
        return res;
    }

    public static void clear(){
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(training_path);
            writer.print("");
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void close(){
        try {
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
