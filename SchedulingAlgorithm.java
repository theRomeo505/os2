// Run() is called from Scheduling.main() and is where
// the scheduling algorithm written by the user resides.
// User modification should occur within the Run() function.

import java.util.Vector;
import java.io.*;

public class SchedulingAlgorithm {

  public static Results Run(int runtime, Vector processVector, Results result) {
    int i = 0;
    int comptime = 0;
    int currentProcess = 0;
    int previousProcess = 0;
    int size = processVector.size();
    ///// sort by cputime
    sProcess temp;
    for (int g=0;g<processVector.size();g++){
      for (int j=0;j<processVector.size();j++){
        if (((sProcess)processVector.elementAt(j)).cputime>((sProcess)processVector.elementAt(g)).cputime){
          temp=(sProcess)processVector.elementAt(g);
          processVector.set(g,((sProcess)processVector.elementAt(j)));
          processVector.set(j,temp);
        }
      }
    }
    /////
    int completed = 0;
    String resultsFile = "Summary-Processes";

    result.schedulingType = "preemptive";
    result.schedulingName = "shortest time first";
    try {
      //BufferedWriter out = new BufferedWriter(new FileWriter(resultsFile));
      //OutputStream out = new FileOutputStream(resultsFile);
      PrintStream out = new PrintStream(new FileOutputStream(resultsFile));
      sProcess process = (sProcess) processVector.elementAt(currentProcess);
      out.println("Process: " + process.cputime + " registered... (" + process.cputime + " " + process.ioblocking + " " + process.cpudone +  ")");
      while (comptime < runtime) {
        ////////// adding new process
          if (comptime==551){
            out.println("process added");
            //out.println(process.cpudone);
            sProcess newproc=new sProcess(100,50,0,0,0);
            int t=0;
            // finding place
            while (newproc.cputime>((sProcess)processVector.elementAt(t)).cputime-((sProcess)processVector.elementAt(t)).cpudone || t==size){
              t++;
            }
            //out.println(t+"k"+currentProcess);
            processVector.insertElementAt(newproc,t);
            size++;
////////////////////
            int predictNext=0;
            sProcess process1;
            //for (int j=0;j<size;j++){
             // sProcess p=(sProcess) processVector.elementAt(j);
             // out.println(p.cputime+" "+p.cpudone);
            //}
            for (i = size - 1; i >= 0; i--) {
              process1 = (sProcess) processVector.elementAt(i);
              if (process1.cpudone < process1.cputime ) {
                predictNext = i;
              }
            }
            /////////////
            //out.println(predictNext+" "+t);
            if (t==predictNext){//(t<=currentProcess){ // if new one should interapt proccess
              currentProcess++;
              out.println("Process: " + process.cputime + " stopped (" + process.cputime + " " + process.ioblocking + " " + process.cpudone +  ")");
              // swaping cur(prevoius already) ((sorting))
              while (currentProcess!=0 && ((sProcess)processVector.elementAt(currentProcess)).cputime-((sProcess)processVector.elementAt(currentProcess)).cpudone<((sProcess)processVector.elementAt((currentProcess-1))).cputime-((sProcess)processVector.elementAt(currentProcess-1)).cpudone){
                out.println("swaped");
                sProcess tmp=(sProcess)processVector.elementAt(currentProcess);
                processVector.set(currentProcess,(sProcess)processVector.elementAt(previousProcess));
                processVector.set(previousProcess,(sProcess)tmp);
                currentProcess--;
              }

              previousProcess = currentProcess;
              // new current
              for (i = size - 1; i >= 0; i--) {
                process = (sProcess) processVector.elementAt(i);
                if (process.cpudone < process.cputime && previousProcess != i) {
                  currentProcess = i;
                }
              }
              process = (sProcess) processVector.elementAt(currentProcess);
              out.println("Process: " + process.cputime + " registered... (" + process.cputime + " " + process.ioblocking + " " +  " " + process.cpudone + ")");
            }
            for (int j=0;j<size;j++){
              sProcess p=(sProcess) processVector.elementAt(j);
              out.println(p.cputime+" "+p.cpudone);
            }
          }
        //////////
        if (process.cpudone == process.cputime) {
          completed++;
          out.println("Process: " + process.cputime + " completed... (" + process.cputime + " " + process.ioblocking + " " + process.cpudone + " "  + ")");
          if (completed == size) {
            result.compuTime = comptime;
            out.close();
            return result;
          }
          ///// swaping to start
          if (currentProcess!=0 && ((sProcess)processVector.elementAt(currentProcess)).cputime-((sProcess)processVector.elementAt(currentProcess)).cpudone<((sProcess)processVector.elementAt((currentProcess-1))).cputime-((sProcess)processVector.elementAt(currentProcess-1)).cpudone){
            out.println("swaped");
            sProcess tmp=(sProcess)processVector.elementAt(currentProcess);
            processVector.set(currentProcess,(sProcess)processVector.elementAt(previousProcess));
            processVector.set(previousProcess,(sProcess)tmp);
          }
          ////
          for (i = size - 1; i >= 0; i--) {
            process = (sProcess) processVector.elementAt(i);
            if (process.cpudone < process.cputime) { 
              currentProcess = i;
            }
          }
          process = (sProcess) processVector.elementAt(currentProcess);
          out.println("Process: " + process.cputime + " registered... (" + process.cputime + " " + process.ioblocking + " " + process.cpudone + " " + ")");
        }      
        if (process.ioblocking == process.ionext) {
          out.println("Process: " + process.cputime + " I/O blocked... (" + process.cputime + " " + process.ioblocking + " " + process.cpudone + " "  + ")");
          process.numblocked++;
          process.ionext = 0;
          //// if needed swaping cur with prev (cur - 1) if remain time is less
          if (currentProcess!=0 && ((sProcess)processVector.elementAt(currentProcess)).cputime-((sProcess)processVector.elementAt(currentProcess)).cpudone<((sProcess)processVector.elementAt((currentProcess-1))).cputime-((sProcess)processVector.elementAt(currentProcess-1)).cpudone){
            out.println("swaped");
            sProcess tmp=(sProcess)processVector.elementAt(currentProcess);
            processVector.set(currentProcess,(sProcess)processVector.elementAt(previousProcess));
            processVector.set(previousProcess,(sProcess)tmp);
            previousProcess=currentProcess-1;
          }
          else
          previousProcess = currentProcess;
          ////
          for (i = size - 1; i >= 0; i--) {
            process = (sProcess) processVector.elementAt(i);
            if (process.cpudone < process.cputime && previousProcess != i) { 
              currentProcess = i;
            }
          }
          process = (sProcess) processVector.elementAt(currentProcess);
          out.println("Process: " + process.cputime + " registered... (" + process.cputime + " " + process.ioblocking + " " +  " " + process.cpudone + ")");
        }        
        process.cpudone++;       
        if (process.ioblocking > 0) {
          process.ionext++;
        }
        comptime++;
      }
      out.close();
    } catch (IOException e) { /* Handle exceptions */ }
    result.compuTime = comptime;
    return result;
  }
}
