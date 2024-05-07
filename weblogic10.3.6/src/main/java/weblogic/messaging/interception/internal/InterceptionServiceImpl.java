package weblogic.messaging.interception.internal;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import weblogic.messaging.interception.MIExceptionLogger;
import weblogic.messaging.interception.exceptions.InterceptionServiceException;
import weblogic.messaging.interception.interfaces.AssociationHandle;
import weblogic.messaging.interception.interfaces.AssociationListener;
import weblogic.messaging.interception.interfaces.InterceptionPointHandle;
import weblogic.messaging.interception.interfaces.InterceptionPointNameDescriptionListener;
import weblogic.messaging.interception.interfaces.InterceptionPointNameDescriptor;
import weblogic.messaging.interception.interfaces.InterceptionService;
import weblogic.messaging.interception.interfaces.Processor;
import weblogic.messaging.interception.interfaces.ProcessorHandle;

public class InterceptionServiceImpl implements InterceptionService {
   private HashMap interceptionPointTypeMap = new HashMap(0);
   private HashMap interceptionPointTypeListenersMap = new HashMap(0);
   private HashMap processorTypeMap = new HashMap(0);
   private static InterceptionServiceImpl singleton = null;

   private InterceptionServiceImpl() {
   }

   public static synchronized InterceptionService getInterceptionService() {
      if (singleton == null) {
         singleton = new InterceptionServiceImpl();
      }

      return singleton;
   }

   public AssociationHandle addAssociation(String var1, String[] var2, String var3, String var4, boolean var5, int var6) throws InterceptionServiceException {
      if (var1 == null) {
         throw new InterceptionServiceException(MIExceptionLogger.logAddAssociationInputErrorLoggable("InterceptionPointType cannot be null").getMessage());
      } else if (var2 == null) {
         throw new InterceptionServiceException(MIExceptionLogger.logAddAssociationInputErrorLoggable("InterceptionPointName cannot be null").getMessage());
      } else if (var3 == null) {
         throw new InterceptionServiceException(MIExceptionLogger.logAddAssociationInputErrorLoggable("ProcessorType cannot be null").getMessage());
      } else if (var4 == null) {
         throw new InterceptionServiceException(MIExceptionLogger.logAddAssociationInputErrorLoggable("ProcessorName cannot be null").getMessage());
      } else {
         String[] var7 = copyIPName(var2);
         InterceptionPointTypeWrapper var8;
         ProcessorTypeWrapper var9;
         synchronized(this) {
            var8 = (InterceptionPointTypeWrapper)this.interceptionPointTypeMap.get(var1);
            var9 = (ProcessorTypeWrapper)this.processorTypeMap.get(var3);
            if (var8 == null) {
               throw new InterceptionServiceException(MIExceptionLogger.logAddAssociationUnknownInterceptionPointTypeErrorLoggable("Unknown InterceptionPointType").getMessage());
            }

            if (var9 == null) {
               var9 = new ProcessorTypeWrapper(var3);
               this.processorTypeMap.put(var3, var9);
            }
         }

         var8.validate(var7);
         InterceptionPoint var10 = var8.findOrCreateInterceptionPoint(var7);
         ProcessorWrapper var11 = var9.findOrCreateProcessorWrapper(var4);
         AssociationListener var12 = var8.getAssociationListener();
         if (var12 != null) {
            var12.onAddAssociation(var1, copyIPName(var2), var3, var4, var5, var6);
         }

         return AssociationManager.createAssociation(var10, var11, var5, var6);
      }
   }

   public AssociationHandle addAssociation(String var1, String[] var2, String var3, String var4, boolean var5) throws InterceptionServiceException {
      return this.addAssociation(var1, var2, var3, var4, var5, Integer.MAX_VALUE);
   }

   public void removeAssociation(AssociationHandle var1) throws InterceptionServiceException {
      AssociationListener var2 = null;
      if (var1 == null) {
         throw new InterceptionServiceException(MIExceptionLogger.logRemoveAssociationInputErrorLoggable("AssociationHandle cannot be null").getMessage());
      } else {
         Association var3 = (Association)var1;
         AssociationManager.removeAssociation(var3);
         synchronized(this) {
            InterceptionPointTypeWrapper var5 = (InterceptionPointTypeWrapper)this.interceptionPointTypeMap.get(var3.getIPType());
            var2 = var5.getAssociationListener();
         }

         if (var2 != null) {
            var2.onRemoveAssociation(var3.getIPType(), var3.getIPName(), var3.getPType(), var3.getPName());
         }
      }
   }

   public void registerInterceptionPointNameDescription(String var1, InterceptionPointNameDescriptor[] var2, AssociationListener var3) throws InterceptionServiceException {
      if (var1 == null) {
         throw new InterceptionServiceException(MIExceptionLogger.logRegisterInterceptionPointNameDescriptionInputErrorLoggable("InterceptionPointType cannot be null").getMessage());
      } else if (var2 == null) {
         throw new InterceptionServiceException(MIExceptionLogger.logRegisterInterceptionPointNameDescriptionInputErrorLoggable("Descriptor cannot be null").getMessage());
      } else {
         Iterator var4 = null;
         synchronized(this) {
            InterceptionPointTypeWrapper var6 = (InterceptionPointTypeWrapper)this.interceptionPointTypeMap.get(var1);
            if (var6 != null) {
               throw new InterceptionServiceException(MIExceptionLogger.logRegisterInterceptionPointNameDescriptionInputErrorLoggable("InterceptionPointType has been registered").getMessage());
            }

            InterceptionPointNameDescriptor[] var7 = copyIPND(var2);
            var6 = new InterceptionPointTypeWrapper(var1, var7, var3);
            this.interceptionPointTypeMap.put(var1, var6);
            var4 = this.removeListeners(var1);
         }

         this.notifyListeners(var4);
      }
   }

   public synchronized InterceptionPointNameDescriptor[] getInterceptionPointNameDescription(String var1) {
      if (var1 == null) {
         return null;
      } else {
         InterceptionPointTypeWrapper var2 = (InterceptionPointTypeWrapper)this.interceptionPointTypeMap.get(var1);
         return var2 == null ? null : copyIPND(var2.getIPND());
      }
   }

   public InterceptionPointHandle registerInterceptionPoint(String var1, String[] var2) throws InterceptionServiceException {
      if (var1 == null) {
         throw new InterceptionServiceException(MIExceptionLogger.logRegisterInterceptionPointInputErrorLoggable("InterceptionPointType cannot be null").getMessage());
      } else if (var2 == null) {
         throw new InterceptionServiceException(MIExceptionLogger.logRegisterInterceptionPointInputErrorLoggable("InterceptionPointName cannot be null").getMessage());
      } else {
         String[] var3 = copyIPName(var2);
         InterceptionPointTypeWrapper var4;
         synchronized(this) {
            var4 = (InterceptionPointTypeWrapper)this.interceptionPointTypeMap.get(var1);
            if (var4 == null) {
               throw new InterceptionServiceException(MIExceptionLogger.logRegisterInterceptionPointUnknownInterceptionPointTypeErrorLoggable("Unknown InterceptionPointType").getMessage());
            }
         }

         var4.validate(var3);
         InterceptionPoint var5 = var4.findOrCreateInterceptionPoint(var3);
         return var5.createHandle();
      }
   }

   public void unRegisterInterceptionPoint(InterceptionPointHandle var1) throws InterceptionServiceException {
      if (var1 == null) {
         throw new InterceptionServiceException(MIExceptionLogger.logUnRegisterInterceptionPointInputErrorLoggable("InterceptionPointHandle cannot be null").getMessage());
      } else {
         ((InterceptionPointHandleImpl)var1).unregister();
      }
   }

   public void registerProcessorType(String var1, String var2) throws InterceptionServiceException {
      if (var1 == null) {
         throw new InterceptionServiceException(MIExceptionLogger.logRegisterProcessorTypeInputErrorLoggable("ProcessorType cannot be null").getMessage());
      } else if (var2 == null) {
         throw new InterceptionServiceException(MIExceptionLogger.logRegisterProcessorTypeInputErrorLoggable("ProcessorFactory cannot be null").getMessage());
      } else {
         synchronized(this.processorTypeMap) {
            ProcessorTypeWrapper var3 = (ProcessorTypeWrapper)this.processorTypeMap.get(var1);
            if (var3 != null && var3.getFactory() != null) {
               throw new InterceptionServiceException(MIExceptionLogger.logRegisterProcessorTypeInputErrorLoggable("ProcessorType has been registered").getMessage());
            } else {
               if (var3 == null) {
                  var3 = new ProcessorTypeWrapper(var1, var2);
                  this.processorTypeMap.put(var1, var3);
               } else {
                  var3.setFactoryName(var2);
               }

            }
         }
      }
   }

   public ProcessorHandle addProcessor(String var1, String var2, String var3) throws InterceptionServiceException {
      if (var1 == null) {
         throw new InterceptionServiceException(MIExceptionLogger.logAddProcessorInputErrorLoggable("ProcessorType cannot be null").getMessage());
      } else if (var2 == null) {
         throw new InterceptionServiceException(MIExceptionLogger.logAddProcessorInputErrorLoggable("ProcessorName cannot be null").getMessage());
      } else {
         ProcessorTypeWrapper var4;
         synchronized(this) {
            var4 = (ProcessorTypeWrapper)this.processorTypeMap.get(var1);
            if (var4 == null) {
               throw new InterceptionServiceException(MIExceptionLogger.logAddProcessorUnknownProcessorTypeErrorLoggable("Unknown ProcessorType").getMessage());
            }
         }

         ProcessorWrapper var5 = var4.findOrCreateProcessorWrapper(var2);
         InterceptionServiceException var6 = null;
         synchronized(var5) {
            if (var5.getProcessor() != null) {
               throw new InterceptionServiceException(MIExceptionLogger.logAddProcessorInputErrorLoggable("Processor exists").getMessage());
            }

            Processor var8 = null;

            try {
               var8 = var4.getFactory().create(var2, var3);
            } catch (RuntimeException var11) {
               throw new InterceptionServiceException(MIExceptionLogger.logAddProcessorInputErrorLoggable("Failed to create processor").getMessage(), var11);
            } catch (Error var12) {
               throw new InterceptionServiceException(MIExceptionLogger.logAddProcessorInputErrorLoggable("Failed to create processor").getMessage(), var12);
            }

            var6 = var5.addProcessor(var8);
         }

         if (var6 != null) {
            throw var6;
         } else {
            return new ProcessorHandleImpl(var5);
         }
      }
   }

   public void removeProcessor(ProcessorHandle var1) throws InterceptionServiceException {
      if (var1 == null) {
         throw new InterceptionServiceException(MIExceptionLogger.logRemoveProcessorInputErrorLoggable("ProcessorHandle cannot be null").getMessage());
      } else {
         ProcessorWrapper var2 = ((ProcessorHandleImpl)var1).getProcessorWrapper();
         if (var2 == null) {
            throw new InterceptionServiceException(MIExceptionLogger.logRemoveProcessorInputErrorLoggable("Processor has been removed").getMessage());
         } else {
            InterceptionServiceException var3 = var2.removeProcessor(var1);
            var2.removeProcessorWrapperIfNotUsed();
            if (var3 != null) {
               throw var3;
            }
         }
      }
   }

   public void removeProcessor(String var1, String var2) throws InterceptionServiceException {
      if (var1 == null) {
         throw new InterceptionServiceException(MIExceptionLogger.logRemoveProcessorInputErrorLoggable("ProcessorType cannot be null").getMessage());
      } else if (var2 == null) {
         throw new InterceptionServiceException(MIExceptionLogger.logRemoveProcessorInputErrorLoggable("ProcessorName cannot be null").getMessage());
      } else {
         ProcessorTypeWrapper var3;
         synchronized(this) {
            var3 = (ProcessorTypeWrapper)this.processorTypeMap.get(var1);
            if (var3 == null) {
               throw new InterceptionServiceException(MIExceptionLogger.logRemoveProcessorInputErrorLoggable("Unknown ProcessorType").getMessage());
            }
         }

         ProcessorWrapper var4 = var3.findProcessorWrapper(var2);
         if (var4 == null) {
            throw new InterceptionServiceException(MIExceptionLogger.logRemoveProcessorInputErrorLoggable("Processor not found").getMessage());
         } else {
            InterceptionServiceException var5 = var4.removeProcessor((ProcessorHandle)null);
            var4.removeProcessorWrapperIfNotUsed();
            if (var5 != null) {
               throw var5;
            }
         }
      }
   }

   public Iterator getAssociationHandles() {
      return AssociationManager.getAssociations();
   }

   public AssociationHandle getAssociationHandle(String var1, String[] var2) throws InterceptionServiceException {
      if (var1 == null) {
         throw new InterceptionServiceException(MIExceptionLogger.logGetAssociationHandleInputErrorLoggable("InterceptionPointType cannot be null").getMessage());
      } else if (var2 == null) {
         throw new InterceptionServiceException(MIExceptionLogger.logGetAssociationHandleInputErrorLoggable("InterceptionPointName cannot be null").getMessage());
      } else {
         String[] var3 = copyIPName(var2);
         InterceptionPointTypeWrapper var4;
         synchronized(this) {
            var4 = (InterceptionPointTypeWrapper)this.interceptionPointTypeMap.get(var1);
            if (var4 == null) {
               throw new InterceptionServiceException(MIExceptionLogger.logGetAssociationHandleInputErrorLoggable("Unknown InterceptionPointType").getMessage());
            }
         }

         var4.validate(var3);
         InterceptionPoint var5 = var4.findInterceptionPoint(var3);
         return var5 == null ? null : var5.getAssociation();
      }
   }

   public Iterator getProcessorHandles(String var1) throws InterceptionServiceException {
      if (var1 == null) {
         throw new InterceptionServiceException(MIExceptionLogger.logGetProcessorHandlesInputErrorLoggable("ProcessorType cannot be null").getMessage());
      } else {
         ProcessorTypeWrapper var2;
         synchronized(this) {
            var2 = (ProcessorTypeWrapper)this.processorTypeMap.get(var1);
            if (var2 == null) {
               throw new InterceptionServiceException(MIExceptionLogger.logGetProcessorHandlesInputErrorLoggable("Unknown ProcessorType").getMessage());
            }
         }

         return var2.getProcessors();
      }
   }

   public ProcessorHandle getProcessorHandle(String var1, String var2) throws InterceptionServiceException {
      if (var1 == null) {
         throw new InterceptionServiceException(MIExceptionLogger.logGetProcessorHandleInputErrorLoggable("ProcessorType cannot be null").getMessage());
      } else if (var2 == null) {
         throw new InterceptionServiceException(MIExceptionLogger.logGetProcessorHandleInputErrorLoggable("ProcessorName cannot be null").getMessage());
      } else {
         ProcessorTypeWrapper var3;
         synchronized(this) {
            var3 = (ProcessorTypeWrapper)this.processorTypeMap.get(var1);
            if (var3 == null) {
               throw new InterceptionServiceException(MIExceptionLogger.logGetProcessorHandleInputErrorLoggable("Unknown ProcessorType").getMessage());
            }
         }

         ProcessorWrapper var4 = var3.findProcessorWrapper(var2);
         return var4 == null ? null : var4.getProcessorHandle();
      }
   }

   public void registerInterceptionPointNameDescriptionListener(InterceptionPointNameDescriptionListener var1) throws InterceptionServiceException {
      if (var1 == null) {
         throw new InterceptionServiceException(MIExceptionLogger.logRegisterInterceptionPointNameDescriptionListenerInputErrorLoggable("Listener cannot be null").getMessage());
      } else if (var1.getType() == null) {
         throw new InterceptionServiceException(MIExceptionLogger.logRegisterInterceptionPointNameDescriptionListenerInputErrorLoggable("Listener.getType() cannot be null").getMessage());
      } else {
         Object var2 = null;
         synchronized(this) {
            var2 = this.interceptionPointTypeMap.get(var1.getType());
         }

         if (var2 != null) {
            this.notifyListener(var1);
         } else {
            this.addListener(var1);
         }

      }
   }

   public static String[] copyIPName(String[] var0) {
      String[] var1 = new String[var0.length];

      for(int var2 = 0; var2 < var0.length; ++var2) {
         var1[var2] = var0[var2];
      }

      return var1;
   }

   public static InterceptionPointNameDescriptor[] copyIPND(InterceptionPointNameDescriptor[] var0) {
      InterceptionPointNameDescriptor[] var1 = new InterceptionPointNameDescriptor[var0.length];

      for(int var2 = 0; var2 < var0.length; ++var2) {
         var1[var2] = var0[var2];
      }

      return var1;
   }

   public int getIPTMapSize(String var1) {
      if (var1 == null) {
         return this.interceptionPointTypeMap.keySet().size();
      } else {
         Iterator var2 = this.interceptionPointTypeMap.keySet().iterator();
         int var3 = 0;

         while(var2.hasNext()) {
            String var4 = (String)var2.next();
            if (var1.equals(var4)) {
               ++var3;
            }
         }

         return var3;
      }
   }

   public int getPTMapSize() {
      return this.processorTypeMap.keySet().size();
   }

   public int getIPsSize(String var1) {
      if (this.getIPTMapSize((String)null) == 0) {
         return 0;
      } else {
         Iterator var2 = null;
         synchronized(this) {
            var2 = ((HashMap)this.interceptionPointTypeMap.clone()).values().iterator();
         }

         int var3 = 0;

         while(true) {
            InterceptionPointTypeWrapper var4;
            do {
               if (!var2.hasNext()) {
                  return var3;
               }

               var4 = (InterceptionPointTypeWrapper)var2.next();
            } while(var1 != null && var1.equals(var4.getName()));

            var3 += var4.getIPsSize();
         }
      }
   }

   public int getAssociationsSize(String var1) {
      return AssociationManager.getAssociationsSize(var1);
   }

   public int getProcessorsSize() {
      if (this.getPTMapSize() == 0) {
         return 0;
      } else {
         Iterator var1 = null;
         synchronized(this) {
            var1 = ((HashMap)this.processorTypeMap.clone()).values().iterator();
         }

         int var2;
         ProcessorTypeWrapper var3;
         for(var2 = 0; var1.hasNext(); var2 += var3.getProcessorsSize()) {
            var3 = (ProcessorTypeWrapper)var1.next();
         }

         return var2;
      }
   }

   private void notifyListener(InterceptionPointNameDescriptionListener var1) {
      var1.onRegister();
   }

   private void notifyListeners(Iterator var1) {
      InterceptionPointNameDescriptionListener var2 = null;

      while(var1.hasNext()) {
         var2 = (InterceptionPointNameDescriptionListener)var1.next();
         this.notifyListener(var2);
      }

   }

   private synchronized void addListener(InterceptionPointNameDescriptionListener var1) {
      Object var2 = (List)this.interceptionPointTypeListenersMap.get(var1.getType());
      if (var2 == null) {
         var2 = new LinkedList();
         this.interceptionPointTypeListenersMap.put(var1.getType(), var2);
      }

      ((List)var2).add(var1);
   }

   private synchronized Iterator removeListeners(String var1) {
      List var2 = (List)this.interceptionPointTypeListenersMap.get(var1);
      if (var2 == null) {
         return (new LinkedList()).listIterator();
      } else {
         this.interceptionPointTypeListenersMap.remove(var1);
         return var2.listIterator();
      }
   }
}
