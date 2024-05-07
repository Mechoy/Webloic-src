package weblogic.messaging.interception.internal;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import weblogic.messaging.interception.exceptions.InterceptionServiceException;
import weblogic.messaging.interception.interfaces.Processor;
import weblogic.messaging.interception.interfaces.ProcessorHandle;

public class ProcessorWrapper {
   private String type = null;
   private String name = null;
   private Processor p = null;
   private HashMap associationsMap = new HashMap(0);
   private long registrationTime = 0L;
   private ProcessorHandle pHandle = null;
   private boolean forcedShutdown = false;
   private ProcessorTypeWrapper ptw = null;

   ProcessorWrapper(String var1, String var2, ProcessorTypeWrapper var3) {
      this.type = var1;
      this.name = var2;
      this.ptw = var3;
   }

   public String getType() {
      return this.type;
   }

   public String getName() {
      return this.name;
   }

   synchronized void setProcessorHandle(ProcessorHandle var1) {
      this.pHandle = var1;
   }

   synchronized ProcessorHandle getProcessorHandle() {
      return this.pHandle;
   }

   synchronized long getRegistrationTime() {
      return this.registrationTime;
   }

   synchronized Processor getProcessor() {
      return this.p;
   }

   InterceptionServiceException addProcessor(Processor var1) {
      InterceptionServiceException var2 = null;
      int var3;
      synchronized(this) {
         this.p = var1;
         this.registrationTime = System.currentTimeMillis();
         var3 = this.associationsMap.size();
         this.forcedShutdown = false;
      }

      if (var3 > 0) {
         var2 = updateState(var1, true);
      }

      if (var2 != null) {
         synchronized(this) {
            if (var1 == this.p) {
               this.p = null;
               this.registrationTime = 0L;
               this.forcedShutdown = true;
            }
         }
      }

      return var2;
   }

   InterceptionServiceException removeProcessor(ProcessorHandle var1) {
      Processor var3 = null;
      synchronized(this) {
         if (var1 != null && this.pHandle != var1) {
            return new InterceptionServiceException("Processor has been removed");
         }

         var3 = this.getProcessor();
      }

      if (var3 == null) {
         return new InterceptionServiceException("Processor has been removed");
      } else {
         InterceptionServiceException var2 = this.removeProcessor(var3, false);
         return var2;
      }
   }

   InterceptionServiceException removeProcessor(Processor var1, boolean var2) {
      InterceptionServiceException var3 = null;
      ProcessorHandle var4 = null;
      synchronized(this) {
         if (var1 == this.p) {
            this.p = null;
            this.registrationTime = 0L;
            var4 = this.pHandle;
            this.pHandle = null;
            this.forcedShutdown = var2;
         }
      }

      if (var4 != null) {
         ((ProcessorHandleImpl)var4).removeProcessorWrapper();
      }

      var3 = shutdownProcessor(var1);
      return var3;
   }

   private static InterceptionServiceException shutdownProcessor(Processor var0) {
      Object var1 = null;

      try {
         var0.onShutdown();
      } catch (RuntimeException var3) {
         var1 = var3;
      } catch (Error var4) {
         var1 = var4;
      }

      if (var1 == null) {
         return null;
      } else {
         return var1 instanceof Error ? new InterceptionServiceException("Processor throws illegal error", (Throwable)var1) : new InterceptionServiceException("Processor throws illegal runtime exception", (Throwable)var1);
      }
   }

   private static InterceptionServiceException updateState(Processor var0, boolean var1) {
      InterceptionServiceException var2 = null;
      Object var3 = null;

      try {
         var0.associationStateChange(var1);
      } catch (RuntimeException var5) {
         var3 = var5;
         var2 = shutdownProcessor(var0);
      } catch (Error var6) {
         var3 = var6;
         var2 = shutdownProcessor(var0);
      }

      if (var2 != null) {
         return var2;
      } else if (var3 != null) {
         return var3 instanceof Error ? new InterceptionServiceException("Processor throws illegal error", (Throwable)var3) : new InterceptionServiceException("Processor throws illegal runtime exception", (Throwable)var3);
      } else {
         return null;
      }
   }

   void addAssociation(Association var1) throws InterceptionServiceException {
      int var2;
      Processor var3;
      synchronized(this) {
         this.associationsMap.put(var1.getInternalName(), var1);
         var2 = this.associationsMap.size();
         var3 = this.p;
      }

      if (var2 == 1 && var3 != null) {
         InterceptionServiceException var4 = updateState(var3, true);
         if (var4 != null) {
            synchronized(this) {
               if (var3 == this.p) {
                  this.p = null;
                  this.registrationTime = 0L;
                  this.forcedShutdown = true;
               }
            }

            throw var4;
         }
      }

   }

   void removeProcessorWrapperIfNotUsed() {
      synchronized(this.ptw) {
         synchronized(this) {
            if (this.pHandle != null || this.p != null || this.associationsMap.size() > 0) {
               return;
            }

            this.ptw.removeProcessorWrapper(this.name);
         }

      }
   }

   void removeAssociation(Association var1) throws InterceptionServiceException {
      Processor var2;
      int var3;
      synchronized(this) {
         var2 = this.p;
         this.associationsMap.remove(var1.getInternalName());
         var3 = this.associationsMap.size();
      }

      if (var3 == 0 && var2 != null) {
         InterceptionServiceException var4 = updateState(var2, false);
         if (var4 != null) {
            synchronized(this) {
               if (var2 == this.p) {
                  this.p = null;
                  this.registrationTime = 0L;
                  this.forcedShutdown = true;
               }
            }

            throw var4;
         }
      }

   }

   public synchronized Iterator getAssociationInfos() {
      HashMap var1 = null;
      synchronized(this) {
         var1 = (HashMap)this.associationsMap.clone();
      }

      Object[] var2 = var1.values().toArray();
      LinkedList var3 = new LinkedList();

      for(int var4 = 0; var4 < var2.length; ++var4) {
         var3.add(((Association)var2[var4]).getInfoInternal());
      }

      return var3.listIterator();
   }

   synchronized boolean forcedShutdown() {
      return this.forcedShutdown;
   }
}
