package weblogic.management.configuration;

import java.io.Serializable;
import java.util.Iterator;
import java.util.NoSuchElementException;
import weblogic.utils.NestedRuntimeException;

/** @deprecated */
public final class RealmIterator implements Iterator, Serializable {
   private static final long serialVersionUID = 7069491944073400204L;
   private static boolean debug = false;
   private RemoteEnumeration helper;
   private ElementHandler handler;
   private boolean done = false;
   private Object[] elements = null;
   private int current = 0;

   public RealmIterator(ListResults var1, ElementHandler var2) {
      this.elements = var1.getFirstBatch();
      this.helper = var1.getRest();
      this.handler = var2;
      if (debug) {
         this.trace("constructor");
      }

   }

   public boolean hasNext() {
      if (debug) {
         this.trace("hasNext");
      }

      this.ensureBatch();
      return !this.done;
   }

   public Object next() {
      if (debug) {
         this.trace("next");
      }

      this.ensureBatch();
      if (this.done) {
         throw new NoSuchElementException("RealmIterator.getNextElement");
      } else {
         return this.handler.handle(this.elements[this.current++]);
      }
   }

   public void remove() {
      if (debug) {
         this.trace("remove");
      }

      throw new UnsupportedOperationException("RealmIterator.remove");
   }

   public void close() {
      if (debug) {
         this.trace("close");
      }

      if (!this.done) {
         this.done = true;
         if (this.helper != null) {
            try {
               this.helper.close();
               this.helper = null;
            } catch (Throwable var2) {
               this.helper = null;
               throw new NestedRuntimeException("RealmIterator.close", var2);
            }
         }
      }
   }

   public void finalize() {
      if (debug) {
         this.trace("finalize");
      }

   }

   private void trace(String var1) {
      System.out.print("RealmIterator done=" + this.done + ", current=" + this.current);
      if (this.elements == null) {
         System.out.print("no elements");
      } else {
         System.out.print("elements.length=" + this.elements.length);
      }

      System.out.println(" " + var1);
   }

   private void ensureBatch() {
      if (debug) {
         this.trace("ensureBatch");
      }

      if (!this.done) {
         if (this.elements == null || 0 > this.current || this.current >= this.elements.length) {
            try {
               this.current = 0;
               this.elements = null;
               if (this.helper != null) {
                  this.elements = this.helper.getNextBatch();
                  if (this.elements == null || this.elements.length == 0) {
                     this.close();
                  }
               } else {
                  this.close();
               }

            } catch (Throwable var2) {
               this.close();
               throw new NestedRuntimeException("RealmIterator.ensureBatch", var2);
            }
         }
      }
   }

   interface ElementHandler {
      Object handle(Object var1);
   }
}
