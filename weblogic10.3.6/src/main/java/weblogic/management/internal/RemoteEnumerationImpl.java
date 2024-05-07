package weblogic.management.internal;

import java.rmi.RemoteException;
import java.rmi.server.Unreferenced;
import weblogic.management.configuration.RemoteEnumeration;

public class RemoteEnumerationImpl implements RemoteEnumeration, Unreferenced {
   private static boolean debug = false;
   private BatchedEnumeration batchedEnumeration;
   private boolean done = false;

   public RemoteEnumerationImpl(BatchedEnumeration var1) throws RemoteException {
      this.batchedEnumeration = var1;
      if (debug) {
         this.trace("constructor");
      }

   }

   public Object[] getNextBatch() {
      if (debug) {
         this.trace("getNextBatch");
      }

      if (this.done) {
         return null;
      } else {
         Object[] var1 = this.batchedEnumeration.getNextBatch();
         if (var1 == null) {
            this.close();
         }

         return var1;
      }
   }

   public void close() {
      if (debug) {
         this.trace("close");
      }

      if (!this.done) {
         this.done = true;
         this.batchedEnumeration.close();
      }
   }

   public void unreferenced() {
      if (debug) {
         this.trace("unreferenced");
      }

      this.close();
   }

   public void finalize() {
      if (debug) {
         this.trace("finalize");
      }

   }

   private void trace(String var1) {
      System.out.println("RemoteEnumerationImpl done=" + this.done + " " + var1);
   }
}
