package weblogic.jms.safclient.agent;

import java.util.List;
import javax.jms.JMSException;
import weblogic.jms.JMSLogger;
import weblogic.jms.common.JMSMessageExpirationHelper;
import weblogic.jms.common.MessageImpl;
import weblogic.jms.dispatcher.JMSDispatcherManager;
import weblogic.jms.safclient.agent.internal.ErrorHandler;
import weblogic.messaging.kernel.KernelException;
import weblogic.messaging.kernel.Queue;
import weblogic.messaging.kernel.RedirectionListener;
import weblogic.messaging.kernel.SendOptions;

public final class DestinationImpl extends weblogic.jms.common.DestinationImpl implements RedirectionListener {
   private static final String SERVER_PFX = "client.saf.server.";
   private static final String MODULE_PFX = "client.saf.module.";
   private static final long serialVersionUID = 6099783323740404732L;
   private transient Queue kernelQueue;
   private transient boolean loggingEnabled = true;
   private transient ErrorHandler errorHandler;
   private transient String sequenceName;
   private transient String nonPersistentQOS;

   public DestinationImpl(String var1, String var2, boolean var3) {
      super((byte)(var3 ? 1 : 2));
      String var4 = JMSDispatcherManager.generateDispatcherName();
      String var5 = "client.saf.server." + var4;
      String var6 = "client.saf.module." + var4;
      this.setName(var2);
      this.setApplicationName(var1);
      this.setServerName(var5);
      this.setModuleName(var6);
   }

   public String getCreateDestinationArgument() {
      return AgentManager.constructDestinationName(this.getApplicationName(), this.getName());
   }

   public String toString() {
      return AgentManager.constructDestinationName(this.getApplicationName(), this.getName());
   }

   public void setKernelQueue(Queue var1) {
      this.kernelQueue = var1;
   }

   public void setLoggingEnabled(boolean var1) {
      this.loggingEnabled = var1;
   }

   public Queue getKernelQueue() {
      return this.kernelQueue;
   }

   public void setErrorHandler(ErrorHandler var1) throws JMSException {
      this.errorHandler = var1;
      if (this.errorHandler != null && this.kernelQueue != null) {
         try {
            this.kernelQueue.setProperty("RedirectionListener", this);
            if (this.errorHandler.getPolicy() == 3) {
               this.kernelQueue.setProperty("IgnoreExpiration", new Boolean(true));
            }
         } catch (KernelException var3) {
            throw new weblogic.jms.common.JMSException(var3);
         }
      }

   }

   private static SendOptions createSendOptions(MessageImpl var0) {
      SendOptions var1 = new SendOptions();
      var1.setPersistent(var0.getAdjustedDeliveryMode() == 2);
      var1.setExpirationTime(var0.getExpirationTime());
      var1.setRedeliveryLimit(var0.getRedeliveryLimit());
      return var1;
   }

   private static void overrideMessageProperties(MessageImpl var0, boolean var1) {
      var0.setDeliveryTime(0L);
      var0._setJMSRedeliveryLimit(-1);
      if (var1) {
         var0._setJMSExpiration(0L);
      }

      var0.setSAFSequenceName((String)null);
      var0.setSAFSeqNumber(0L);
   }

   public void expirationTimeReached(RedirectionListener.Info var1, boolean var2) {
      if (this.errorHandler == null) {
         if (!var2 && this.loggingEnabled) {
            JMSLogger.logExpiredSAFMessageNoHeaderProperty("'" + var1.getMessage().getMessageID() + "'");
         }

      } else {
         MessageImpl var3 = (MessageImpl)var1.getMessage();
         int var4 = this.errorHandler.getPolicy();
         switch (var4) {
            case 0:
            case 3:
               return;
            case 1:
               StringBuffer var5 = new StringBuffer(256);
               List var6 = JMSMessageExpirationHelper.extractJMSHeaderAndProperty(this.errorHandler.getLogFormat(), var5);
               List var7 = JMSMessageExpirationHelper.convertStringToLinkedList(var5.toString());
               JMSMessageExpirationHelper.logExpiredSAFMessage(var3, var6, var7);
               return;
            case 2:
               DestinationImpl var8 = this.errorHandler.getErrorDestination();
               Queue var9 = var8.getKernelQueue();
               overrideMessageProperties(var3, true);
               var1.setSendOptions(createSendOptions(var3));
               var1.setRedirectDestination(var9);
               return;
            default:
               throw new AssertionError("Unknown policy: " + var4);
         }
      }
   }

   public void deliveryLimitReached(RedirectionListener.Info var1) {
      DestinationImpl var2 = this.errorHandler.getErrorDestination();
      if (var2 != null) {
         Queue var3 = var2.getKernelQueue();
         MessageImpl var4 = (MessageImpl)var1.getMessage();
         overrideMessageProperties(var4, false);
         var1.setSendOptions(createSendOptions(var4));
         var1.setRedirectDestination(var3);
      }
   }

   public void setSequenceName(String var1) {
      this.sequenceName = var1;
   }

   public String getSequenceName() {
      return this.sequenceName;
   }

   public void setNonPersistentQOS(String var1) {
      this.nonPersistentQOS = var1;
   }

   public String getNonPersistentQOS() {
      return this.nonPersistentQOS;
   }
}
