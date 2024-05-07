package weblogic.messaging.interception.internal;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Iterator;
import weblogic.messaging.interception.MIExceptionLogger;
import weblogic.messaging.interception.exceptions.InterceptionServiceException;
import weblogic.messaging.interception.interfaces.ProcessorFactory;
import weblogic.messaging.interception.interfaces.ProcessorHandle;

class ProcessorTypeWrapper {
   private HashMap processorWrapperMap = new HashMap(0);
   private String name = null;
   private ProcessorFactory factory = null;

   ProcessorTypeWrapper(String var1, String var2) throws InterceptionServiceException {
      this.name = var1;
      this.factory = this.instantiateProcessorFactory(var2);
   }

   ProcessorTypeWrapper(String var1) {
      this.name = var1;
   }

   private ProcessorFactory instantiateProcessorFactory(String var1) throws InterceptionServiceException {
      try {
         Class var2 = Class.forName(var1);
         Constructor[] var3 = var2.getConstructors();
         Constructor var4 = null;

         for(int var5 = 0; var5 < var3.length; ++var5) {
            Class[] var6 = var3[var5].getParameterTypes();
            if (var6.length == 0) {
               var4 = var3[var5];
               break;
            }
         }

         if (var4 == null) {
            throw new InterceptionServiceException(MIExceptionLogger.logProcessorFactoryCreateErrorLoggable("ProcessorFactory requires no argument constructor").getMessage());
         } else {
            return (ProcessorFactory)var4.newInstance((Object[])null);
         }
      } catch (Exception var7) {
         if (var7 instanceof InterceptionServiceException) {
            throw (InterceptionServiceException)var7;
         } else {
            throw new InterceptionServiceException(MIExceptionLogger.logProcessorFactoryCreateUnknownErrorLoggable("Fail to construct ProcessorFactory").getMessage(), var7);
         }
      }
   }

   String getName() {
      return this.name;
   }

   ProcessorFactory getFactory() {
      return this.factory;
   }

   void setFactoryName(String var1) throws InterceptionServiceException {
      this.factory = this.instantiateProcessorFactory(var1);
   }

   synchronized ProcessorWrapper findOrCreateProcessorWrapper(String var1) {
      ProcessorWrapper var2 = (ProcessorWrapper)this.processorWrapperMap.get(var1);
      if (var2 == null) {
         var2 = new ProcessorWrapper(this.name, var1, this);
         this.processorWrapperMap.put(var1, var2);
      }

      return var2;
   }

   synchronized ProcessorWrapper findProcessorWrapper(String var1) {
      return (ProcessorWrapper)this.processorWrapperMap.get(var1);
   }

   void removeProcessorWrapper(String var1) {
      this.processorWrapperMap.remove(var1);
   }

   Iterator getProcessors() {
      HashMap var1 = null;
      HashMap var2 = new HashMap(0);
      synchronized(this) {
         var1 = (HashMap)this.processorWrapperMap.clone();
      }

      Object[] var3 = var1.values().toArray();

      for(int var4 = 0; var4 < var3.length; ++var4) {
         ProcessorWrapper var5 = (ProcessorWrapper)var3[var4];
         ProcessorHandle var6 = var5.getProcessorHandle();
         if (var6 != null) {
            var2.put(var5.getName(), var6);
         }
      }

      return var2.values().iterator();
   }

   public int getProcessorsSize() {
      return this.processorWrapperMap.keySet().size();
   }
}
