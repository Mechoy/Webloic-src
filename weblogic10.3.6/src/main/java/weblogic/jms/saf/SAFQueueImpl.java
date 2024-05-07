package weblogic.jms.saf;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import javax.jms.JMSException;
import javax.jms.ServerSessionPool;
import weblogic.jms.JMSLogger;
import weblogic.jms.backend.BEBrowser;
import weblogic.jms.backend.BEConnectionConsumerImpl;
import weblogic.jms.backend.BEConsumerCreateRequest;
import weblogic.jms.backend.BEConsumerImpl;
import weblogic.jms.backend.BEDestinationSecurityImpl;
import weblogic.jms.backend.BEQueueImpl;
import weblogic.jms.backend.BESession;
import weblogic.jms.backend.BESessionImpl;
import weblogic.jms.common.EntityName;
import weblogic.jms.common.JMSDebug;
import weblogic.jms.common.JMSID;
import weblogic.jms.common.MessageImpl;
import weblogic.jms.extensions.WLMessage;
import weblogic.jms.module.JMSBeanHelper;
import weblogic.kernel.AuditableThreadLocal;
import weblogic.kernel.AuditableThreadLocalFactory;
import weblogic.management.runtime.SAFRemoteEndpointRuntimeMBean;
import weblogic.messaging.kernel.KernelException;
import weblogic.messaging.kernel.Queue;
import weblogic.messaging.kernel.RedirectionListener;
import weblogic.messaging.kernel.SendOptions;
import weblogic.messaging.kernel.Sequence;

public final class SAFQueueImpl extends BEQueueImpl {
   private String errorHandlingName;
   private ErrorHandler errorHandler;
   private boolean updatedErrorHandler;
   private SAFAgentAdmin safAgent;
   private SAFRemoteEndpointRuntimeMBean safRuntimeMBean;
   private long timeToLiveDefault = -1L;
   private int nonPersistentQos = 1;
   private int persistentQos = 2;
   private static transient AuditableThreadLocal consumersAllowedThreadLocal = AuditableThreadLocalFactory.createThreadLocal();
   private String refreshedSequenceName;

   public SAFQueueImpl(SAFAgentAdmin var1, String var2, boolean var3, EntityName var4, String var5) throws JMSException {
      super(var1.getBackEnd(), var2, var3, new BEDestinationSecurityImpl(var4, var5));
      this.safAgent = var1;
      this.moduleName = var4.getFullyQualifiedModuleName();
      this.refreshedSequenceName = this.refreshSequenceName(this.fullyQualifiedDestinationName);
      if (JMSDebug.JMSSAF.isDebugEnabled()) {
         JMSDebug.JMSSAF.debug("Refreshed SAF sequence name is " + this.refreshedSequenceName);
      }

   }

   static void allowCreateConsumer() {
      consumersAllowedThreadLocal.set(Boolean.TRUE);
   }

   static void disallowCreateConsumer() {
      consumersAllowedThreadLocal.set(Boolean.FALSE);
   }

   protected BEConsumerImpl createConsumer(BESessionImpl var1, boolean var2, BEConsumerCreateRequest var3) throws JMSException {
      this.checkConsumption("consumer");
      return super.createConsumer(var1, var2, var3);
   }

   protected BEConnectionConsumerImpl createConnectionConsumer(JMSID var1, ServerSessionPool var2, String var3, String var4, String var5, boolean var6, int var7, long var8, boolean var10, boolean var11) throws JMSException {
      this.checkConsumption("connection consumer");
      return super.createConnectionConsumer(var1, var2, var3, var4, var5, var6, var7, var8, var10, var11);
   }

   public BEBrowser createBrowser(BESession var1, String var2) throws JMSException {
      this.checkConsumption("browser");
      return super.createBrowser(var1, var2);
   }

   private void checkConsumption(String var1) throws JMSException {
      if (consumersAllowedThreadLocal.get() != Boolean.TRUE) {
         throw new JMSException("Cannot create " + var1 + " on imported " + "(store-and-forward) destination because it is not allowed to " + "receive messages from an imported destination");
      }
   }

   public void setTimeToLiveDefault(long var1) {
      this.timeToLiveDefault = var1;
   }

   protected int getSequenceMode(WLMessage var1) {
      int var2 = ((MessageImpl)var1).getJMSDeliveryMode();
      if (var2 == 1 && this.nonPersistentQos != 2) {
         return 0;
      } else if (var2 == 2 && this.persistentQos != 2) {
         return 0;
      } else {
         boolean var3 = this.checkForwarded(var1);
         boolean var4 = var1.getSAFSeqNumber() != 0L;
         return var4 && var3 ? super.getSequenceMode(var4, var3) : 1;
      }
   }

   protected String getSequenceName(WLMessage var1) {
      String var2 = var1.getSAFSequenceName();
      return var2 != null ? var2 : this.refreshedSequenceName;
   }

   private String versionedName(String var1) {
      SimpleDateFormat var2 = new SimpleDateFormat("yyyyMMddHHmmssSSSS");
      return var1 + var2.format(new Date(this.getCreationTime()));
   }

   public SendOptions createSendOptions(long var1, Sequence var3, MessageImpl var4) {
      SendOptions var5 = this.createSendOptionsInternal(var1, var3, var4);
      if (this.timeToLiveDefault != -1L) {
         if (this.timeToLiveDefault == 0L) {
            var5.setExpirationTime(0L);
         } else {
            long var6 = System.currentTimeMillis() + this.timeToLiveDefault;
            var5.setExpirationTime(var6);
         }
      }

      return var5;
   }

   public void expirationTimeReached(RedirectionListener.Info var1, boolean var2) {
      if (JMSDebug.JMSSAF.isDebugEnabled()) {
         JMSDebug.JMSSAF.debug("SAFQueueImpl's expirationTimeReached() is called");
      }

      this.errorHandler = this.getErrorHandler();
      if (this.errorHandler == null) {
         if (JMSDebug.JMSSAF.isDebugEnabled()) {
            JMSDebug.JMSSAF.debug("Error Handler == null, alreadyReported " + var2 + " loggingEnabled " + this.safAgent.isLoggingEnabled());
         }

         if (!var2 && this.safAgent.isLoggingEnabled()) {
            JMSLogger.logExpiredSAFMessageNoHeaderProperty("'" + var1.getMessage().getMessageID() + "'");
         }

         ((SAFRemoteEndpointRuntimeMBeanImpl)this.safRuntimeMBean).updateFailedMessagesCount(1L);
      } else {
         int var3 = this.errorHandler.getPolicyAsInt();
         if (var3 != 4) {
            ((SAFRemoteEndpointRuntimeMBeanImpl)this.safRuntimeMBean).updateFailedMessagesCount(1L);

            try {
               this.errorHandler.handleFailure(var1, this.getBackEnd().getName(), (MessageImpl)var1.getMessage());
            } catch (KernelException var5) {
            } catch (JMSException var6) {
            }
         }

      }
   }

   void initializeErrorHandling(String var1) {
      this.errorHandlingName = var1;
      this.errorHandler = this.getErrorHandler();
      if (this.errorHandler != null) {
         Queue var2 = this.getKernelQueue();

         try {
            if (this.errorHandler.getPolicyAsInt() == 4) {
               var2.setProperty("IgnoreExpiration", new Boolean(true));
            }
         } catch (KernelException var4) {
         }

      }
   }

   ErrorHandler getErrorHandler() {
      if (JMSDebug.JMSModule.isDebugEnabled()) {
         JMSDebug.JMSModule.debug("Name = " + this.name + " ModuleName = " + this.moduleName + " ErrorHandlingName " + this.errorHandlingName);
      }

      if (this.errorHandlingName == null) {
         return null;
      } else {
         synchronized(this) {
            if (!this.updatedErrorHandler) {
               this.errorHandler = JMSSAFManager.manager.getErrorHandler(JMSBeanHelper.getDecoratedName(this.moduleName, this.errorHandlingName));
               this.updatedErrorHandler = true;
            }
         }

         if (JMSDebug.JMSModule.isDebugEnabled()) {
            JMSDebug.JMSModule.debug("ErrorHandler= " + (this.errorHandler == null ? "null" : this.errorHandler.toString()));
         }

         return this.errorHandler;
      }
   }

   synchronized void setSAFErrorHandlingName(String var1) {
      this.errorHandlingName = var1;
      this.updatedErrorHandler = false;
   }

   synchronized void setSAFRuntimeMBean(SAFRemoteEndpointRuntimeMBean var1) {
      this.safRuntimeMBean = var1;
   }

   void setNonPersistentQos(int var1) {
      this.nonPersistentQos = var1;
   }

   void setPersistentQos(int var1) {
      this.persistentQos = var1;
   }

   protected boolean clientSendResumeNeedsNewThread() {
      return true;
   }

   private String refreshSequenceName(String var1) {
      String var2 = null;
      Queue var3 = this.getKernelQueue();
      Collection var4 = var3.getSequences();
      Iterator var5 = var4.iterator();
      String var6 = var1;

      while(var5.hasNext()) {
         Sequence var7 = (Sequence)var5.next();
         if (var7.getName().contains(var1) && var7.getName().compareTo(var6) >= 0) {
            var6 = var7.getName();
            var2 = var6;
         }
      }

      if (var2 == null || this.isNewlyCreated()) {
         var2 = this.versionedName(var1);
      }

      return var2;
   }
}
