package weblogic.jms.common;

import java.rmi.RemoteException;
import weblogic.rmi.extensions.RemoteHelper;
import weblogic.utils.NestedThrowable;

public final class JMSFailover {
   private final DistributedDestinationImpl[] dests;
   private final Object firstFailure;
   private final int size;
   private int current;

   public JMSFailover(DistributedDestinationImpl[] var1, Object var2) {
      this.dests = var1;
      this.size = var1.length;
      this.current = 0;
      this.firstFailure = var2;
   }

   public static boolean isRecoverableFailure(Throwable var0) {
      Throwable var1 = var0;

      while(var0 != null) {
         if (var0 instanceof RuntimeException || var0 instanceof Error) {
            return false;
         }

         if (var0 instanceof RemoteException) {
            return RemoteHelper.isRecoverablePreInvokeFailure((RemoteException)var0);
         }

         if (var0.getCause() != null) {
            var0 = var0.getCause();
         } else {
            if (!(var0 instanceof NestedThrowable)) {
               if (!(var0 instanceof javax.jms.JMSException)) {
                  return false;
               }

               if (JMSDebug.JMSMessagePath.isDebugEnabled()) {
                  JMSDebug.JMSMessagePath.debug("failover permitted for ", var1);
               } else if (JMSDebug.JMSFrontEnd.isDebugEnabled()) {
                  JMSDebug.JMSFrontEnd.debug("failover permitted for ", var1);
               }

               return true;
            }

            var0 = ((NestedThrowable)var0).getNested();
         }
      }

      return false;
   }

   public DistributedDestinationImpl failover(DistributedDestinationImpl var1, Throwable var2) {
      if (!isRecoverableFailure(var2)) {
         return null;
      } else if (this.current < this.size) {
         return this.dests[this.current] == this.firstFailure && ++this.current >= this.size ? null : this.dests[this.current++];
      } else {
         return null;
      }
   }
}
