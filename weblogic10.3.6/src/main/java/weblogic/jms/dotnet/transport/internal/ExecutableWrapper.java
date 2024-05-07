package weblogic.jms.dotnet.transport.internal;

import weblogic.jms.dotnet.transport.TransportExecutable;

class ExecutableWrapper {
   ExecutableWrapper next;
   private final TransportExecutable task;

   ExecutableWrapper(TransportExecutable var1) {
      this.task = var1;
   }

   public TransportExecutable getTask() {
      return this.task;
   }
}
