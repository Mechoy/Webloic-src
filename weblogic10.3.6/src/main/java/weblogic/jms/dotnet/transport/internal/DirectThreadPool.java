package weblogic.jms.dotnet.transport.internal;

import weblogic.jms.dotnet.transport.TransportExecutable;
import weblogic.jms.dotnet.transport.TransportThreadPool;

class DirectThreadPool implements TransportThreadPool {
   public void schedule(TransportExecutable var1) {
      var1.execute();
   }
}
