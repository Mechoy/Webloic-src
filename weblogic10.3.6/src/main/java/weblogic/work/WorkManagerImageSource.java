package weblogic.work;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import weblogic.diagnostics.image.ImageSource;
import weblogic.diagnostics.image.ImageSourceCreationException;

public class WorkManagerImageSource implements ImageSource {
   private static final WorkManagerImageSource THE_ONE = new WorkManagerImageSource();
   final ArrayList workManagers = new ArrayList();

   private WorkManagerImageSource() {
   }

   static WorkManagerImageSource getInstance() {
      return THE_ONE;
   }

   synchronized void register(ServerWorkManagerImpl var1) {
      this.workManagers.add(var1);
   }

   synchronized void deregister(ServerWorkManagerImpl var1) {
      this.workManagers.remove(var1);
   }

   public synchronized void createDiagnosticImage(OutputStream var1) throws ImageSourceCreationException {
      RequestManager var2 = RequestManager.getInstance();
      PrintWriter var3 = new PrintWriter(var1);
      var3.println("Total thread count  : " + var2.getExecuteThreadCount());
      var3.println("Idle thread count   : " + var2.getIdleThreadCount());
      var3.println("Standby thread count: " + var2.getStandbyCount());
      var3.println("Queue depth         : " + var2.getQueueDepth());
      var3.println("Queue departures    : " + var2.getQueueDepartures());
      var3.println("Mean throughput     : " + var2.getThroughput());
      var3.println("Total requests      : " + var2.getTotalRequestsCount());
      Iterator var4 = this.workManagers.iterator();

      while(var4.hasNext()) {
         ServerWorkManagerImpl var5 = (ServerWorkManagerImpl)var4.next();
         var5.dumpInformation(var3);
      }

      var3.flush();
   }

   public void timeoutImageCreation() {
   }
}
