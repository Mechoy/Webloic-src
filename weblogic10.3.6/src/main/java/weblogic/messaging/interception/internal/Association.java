package weblogic.messaging.interception.internal;

import javax.jms.Message;
import javax.jms.TextMessage;
import javax.xml.rpc.handler.MessageContext;
import weblogic.jms.common.JMSMessageContext;
import weblogic.jms.common.MessageImpl;
import weblogic.messaging.interception.MIExceptionLogger;
import weblogic.messaging.interception.exceptions.InterceptionException;
import weblogic.messaging.interception.exceptions.InterceptionServiceException;
import weblogic.messaging.interception.exceptions.MessageContextException;
import weblogic.messaging.interception.interfaces.AssociationHandle;
import weblogic.messaging.interception.interfaces.AssociationInfo;
import weblogic.messaging.interception.interfaces.CarrierCallBack;
import weblogic.messaging.interception.interfaces.Processor;

public class Association implements AssociationHandle {
   private static final int PROCESS = 1;
   private static final int PROCESSONLY = 2;
   private static final int PROCESSASYNC = 3;
   private static final int PROCESSONLYASYNC = 4;
   private InterceptionPoint ip = null;
   private ProcessorWrapper pw = null;
   private boolean activated = false;
   private boolean removed = false;
   private long totalMessagesCount = 0L;
   private long continueMessagesCount = 0L;
   private long inProgressMessagesCount = 0L;
   private int depth;
   private AssociationInfoImpl info = null;
   private String ipType = null;
   private String[] ipName = null;
   private String pType = null;
   private String pName = null;

   Association(InterceptionPoint var1, ProcessorWrapper var2, boolean var3, int var4) throws InterceptionServiceException {
      this.ip = var1;
      this.ipType = var1.getType();
      this.ipName = var1.getName();
      this.pw = var2;
      this.pType = var2.getType();
      this.pName = var2.getName();
      this.activated = var3;
      this.depth = var4;
      this.info = new AssociationInfoImpl(this);
   }

   String getInternalName() {
      return this.ip.getInternalName();
   }

   String getIPType() {
      return this.ipType;
   }

   String[] getIPName() {
      return this.ipName;
   }

   String getPType() {
      return this.pType;
   }

   String getPName() {
      return this.pName;
   }

   synchronized void remove() throws InterceptionServiceException {
      if (this.removed) {
         throw new InterceptionServiceException(MIExceptionLogger.logRemoveAssociationAlreadyRemoveErrorLoggable("Association has been removed").getMessage());
      } else {
         this.removed = true;
         this.pw.removeAssociation(this);
         this.ip.removeAssociation();
         this.ip = null;
         this.pw = null;
      }
   }

   private boolean processInternal(MessageContext var1, CarrierCallBack var2, int var3) throws InterceptionServiceException, InterceptionException, MessageContextException {
      Processor var4 = null;
      this.adjust(var1);
      synchronized(this) {
         if (this.removed) {
            return true;
         }

         ++this.totalMessagesCount;
         if (!this.activated) {
            ++this.continueMessagesCount;
            return true;
         }

         var4 = this.pw.getProcessor();
         if (var4 == null) {
            throw new InterceptionServiceException(MIExceptionLogger.logProcessProcessorNotFoundErrorLoggable("Processor not found").getMessage());
         }

         if (this.inProgressMessagesCount >= (long)this.depth) {
            throw new InterceptionServiceException(MIExceptionLogger.logProcessProcessorDepthExceededErrorLoggable("Processor has more intercepted message than " + this.depth + " outstanding").getMessage());
         }

         ++this.inProgressMessagesCount;
      }

      boolean var5 = false;
      boolean var6 = false;
      Object var7 = null;
      InterceptionServiceException var8 = null;

      try {
         if (var3 == 1) {
            var5 = var4.process(var1, this.info);
            var6 = true;
         } else {
            InterceptionCallBackImpl var9;
            if (var3 == 3) {
               var9 = new InterceptionCallBackImpl(var2, this, false, var1);
               var4.processAsync(var1, this.info, var9);
               var6 = true;
            } else if (var3 == 2) {
               var4.processOnly(var1, this.info);
               var6 = true;
            } else {
               var9 = new InterceptionCallBackImpl(var2, this, true, var1);
               var4.processOnlyAsync(var1, this.info, var9);
               var6 = true;
            }
         }
      } catch (RuntimeException var23) {
         var7 = var23;
         var8 = this.pw.removeProcessor(var4, true);
      } catch (Error var24) {
         var7 = var24;
         var8 = this.pw.removeProcessor(var4, true);
      } finally {
         synchronized(this) {
            if (!var6) {
               --this.inProgressMessagesCount;
            } else if (var3 == 1) {
               --this.inProgressMessagesCount;
               if (var5) {
                  ++this.continueMessagesCount;
               }
            } else if (var3 == 2) {
               --this.inProgressMessagesCount;
               ++this.continueMessagesCount;
            }
         }

         if (var7 != null) {
            this.pw.removeProcessorWrapperIfNotUsed();
            if (var8 != null) {
               throw var8;
            }

            if (var7 instanceof Error) {
               throw new InterceptionServiceException(MIExceptionLogger.logProcessIllegalErrorLoggable("Processor throws illegal error").getMessage(), (Throwable)var7);
            }

            throw new InterceptionServiceException(MIExceptionLogger.logProcessIllegalExceptionLoggable("Processor throws illegal runtime exception").getMessage(), (Throwable)var7);
         }

      }

      return var5;
   }

   boolean process(MessageContext var1) throws InterceptionServiceException, InterceptionException, MessageContextException {
      return this.processInternal(var1, (CarrierCallBack)null, 1);
   }

   void process(MessageContext var1, CarrierCallBack var2) throws InterceptionServiceException, InterceptionException, MessageContextException {
      this.processInternal(var1, var2, 3);
   }

   void processOnly(MessageContext var1) throws InterceptionServiceException, InterceptionException, MessageContextException {
      this.processInternal(var1, (CarrierCallBack)null, 2);
   }

   void processOnly(MessageContext var1, CarrierCallBack var2) throws InterceptionServiceException, InterceptionException, MessageContextException {
      this.processInternal(var1, var2, 4);
   }

   private void checkRemove() throws InterceptionServiceException {
      if (this.removed) {
         throw new InterceptionServiceException(MIExceptionLogger.logRemoveAssociationAlreadyRemoveErrorLoggable("Association has been removed").getMessage());
      }
   }

   String getInterceptionPointType() throws InterceptionServiceException {
      InterceptionPoint var1 = null;
      synchronized(this) {
         this.checkRemove();
         var1 = this.ip;
      }

      return var1.getType();
   }

   String[] getInterceptionPointName() throws InterceptionServiceException {
      InterceptionPoint var1 = null;
      synchronized(this) {
         this.checkRemove();
         var1 = this.ip;
      }

      return var1.getName();
   }

   String getProcessorType() throws InterceptionServiceException {
      ProcessorWrapper var1 = null;
      synchronized(this) {
         this.checkRemove();
         var1 = this.pw;
      }

      return var1.getType();
   }

   String getProcessorName() throws InterceptionServiceException {
      ProcessorWrapper var1 = null;
      synchronized(this) {
         this.checkRemove();
         var1 = this.pw;
      }

      return var1.getName();
   }

   public AssociationInfo getInfoInternal() {
      return this.info;
   }

   public synchronized AssociationInfo getAssociationInfo() throws InterceptionServiceException {
      this.checkRemove();
      return this.info;
   }

   synchronized ProcessorWrapper getProcessorWrapper() {
      return this.pw;
   }

   synchronized long getTotalMessagesCount() throws InterceptionServiceException {
      this.checkRemove();
      return this.totalMessagesCount;
   }

   synchronized long getContinueMessagesCount() throws InterceptionServiceException {
      this.checkRemove();
      return this.continueMessagesCount;
   }

   synchronized long getInProgressMessagesCount() throws InterceptionServiceException {
      this.checkRemove();
      return this.inProgressMessagesCount;
   }

   long getProcessorRegistrationTime() throws InterceptionServiceException {
      ProcessorWrapper var1 = null;
      synchronized(this) {
         this.checkRemove();
         var1 = this.pw;
      }

      return var1.getRegistrationTime();
   }

   boolean hasProcessor() throws InterceptionServiceException {
      ProcessorWrapper var1 = null;
      synchronized(this) {
         this.checkRemove();
         var1 = this.pw;
      }

      return var1.getProcessor() != null;
   }

   public synchronized void activate() throws InterceptionServiceException {
      this.checkRemove();
      this.activated = true;
   }

   public synchronized void deActivate() throws InterceptionServiceException {
      this.checkRemove();
      this.activated = false;
   }

   synchronized boolean isActivated() throws InterceptionServiceException {
      this.checkRemove();
      return this.activated;
   }

   boolean isProcessorShutdown() throws InterceptionServiceException {
      ProcessorWrapper var1 = null;
      synchronized(this) {
         this.checkRemove();
         var1 = this.pw;
      }

      return var1.forcedShutdown();
   }

   synchronized void updateAsyncMeessagesCount(boolean var1) {
      if (var1) {
         ++this.continueMessagesCount;
      }

      --this.inProgressMessagesCount;
   }

   private void adjust(MessageContext var1) {
      if (var1 instanceof JMSMessageContext) {
         JMSMessageContext var2 = (JMSMessageContext)var1;
         Message var3 = var2.getMessage();
         if (var3 instanceof TextMessage) {
            ((MessageImpl)var3).setBodyWritable();
            ((MessageImpl)var3).setPropertiesWritable(true);
         }
      }

   }
}
