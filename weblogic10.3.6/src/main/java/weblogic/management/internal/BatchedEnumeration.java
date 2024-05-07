package weblogic.management.internal;

import java.util.Enumeration;
import java.util.Vector;
import weblogic.security.acl.ClosableEnumeration;

public final class BatchedEnumeration {
   private static boolean debug = false;
   private Enumeration enumeration;
   private int batchSize;
   private ElementHandler handler;
   private boolean done = false;

   public BatchedEnumeration(Enumeration var1, int var2, ElementHandler var3) {
      this.enumeration = var1;
      this.batchSize = var2;
      this.handler = var3;
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
         Vector var1 = new Vector();

         for(int var2 = 0; var2 < this.batchSize || this.batchSize <= 0; ++var2) {
            if (this.enumeration == null || !this.enumeration.hasMoreElements()) {
               this.closeEnumeration();
               break;
            }

            var1.add(this.handler.handle(this.enumeration.nextElement()));
         }

         if (var1.size() <= 0) {
            return null;
         } else {
            Object[] var3 = new Object[var1.size()];
            var1.copyInto(var3);
            return var3;
         }
      }
   }

   public boolean hasMoreElements() {
      if (debug) {
         this.trace("hasMoreElements");
      }

      return this.done ? false : this.enumeration.hasMoreElements();
   }

   public void close() {
      if (debug) {
         this.trace("close");
      }

      this.closeEnumeration();
   }

   protected void finalize() {
      if (debug) {
         this.trace("finalize");
      }

      this.close();
   }

   private void trace(String var1) {
      System.out.println("BatchedEnumeration done=" + this.done + ", batchSize=" + this.batchSize + " " + var1);
   }

   private void closeEnumeration() {
      if (debug) {
         this.trace("closeEnumeration");
      }

      if (!this.done) {
         this.done = true;
         if (this.enumeration instanceof ClosableEnumeration) {
            ((ClosableEnumeration)this.enumeration).close();
         }

      }
   }

   interface ElementHandler {
      Object handle(Object var1);
   }
}
