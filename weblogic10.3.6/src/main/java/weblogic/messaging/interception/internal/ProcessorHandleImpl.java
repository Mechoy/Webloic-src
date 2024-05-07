package weblogic.messaging.interception.internal;

import java.util.Iterator;
import weblogic.messaging.interception.MIExceptionLogger;
import weblogic.messaging.interception.exceptions.InterceptionServiceException;
import weblogic.messaging.interception.interfaces.ProcessorHandle;

public class ProcessorHandleImpl implements ProcessorHandle {
   private ProcessorWrapper pw = null;

   ProcessorHandleImpl(ProcessorWrapper var1) {
      this.pw = var1;
      var1.setProcessorHandle(this);
   }

   synchronized ProcessorWrapper getProcessorWrapper() {
      return this.pw;
   }

   synchronized void removeProcessorWrapper() {
      this.pw = null;
   }

   void checkRemoved() throws InterceptionServiceException {
      if (this.pw == null) {
         throw new InterceptionServiceException(MIExceptionLogger.logRemoveProcessorAlreadyRemoveErrorLoggable("Processor has been removed").getMessage());
      }
   }

   public Iterator getAssociationInfos() throws InterceptionServiceException {
      ProcessorWrapper var1;
      synchronized(this) {
         this.checkRemoved();
         var1 = this.pw;
      }

      return var1.getAssociationInfos();
   }

   public String getType() throws InterceptionServiceException {
      ProcessorWrapper var1;
      synchronized(this) {
         this.checkRemoved();
         var1 = this.pw;
      }

      return var1.getType();
   }

   public String getName() throws InterceptionServiceException {
      ProcessorWrapper var1;
      synchronized(this) {
         this.checkRemoved();
         var1 = this.pw;
      }

      return var1.getName();
   }
}
