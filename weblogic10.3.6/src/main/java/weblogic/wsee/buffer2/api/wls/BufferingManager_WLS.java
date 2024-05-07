package weblogic.wsee.buffer2.api.wls;

import java.lang.reflect.Method;
import java.security.PrivilegedExceptionAction;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import weblogic.ejb.spi.DynamicEJBModule;
import weblogic.jms.common.MessageImpl;
import weblogic.jms.extensions.WLMessage;
import weblogic.jms.extensions.WLMessageProducer;
import weblogic.wsee.buffer2.api.common.BufferingDispatch;
import weblogic.wsee.buffer2.api.common.BufferingFeature;
import weblogic.wsee.buffer2.api.common.BufferingManager;
import weblogic.wsee.buffer2.exception.BufferingException;
import weblogic.wsee.buffer2.internal.common.JmsSessionPool;

public class BufferingManager_WLS extends BufferingManager {
   private static UnitOfOrderQueueFinder _unitOfOrderQueueFinder;

   public static void setUnitOfOrderQueueFinder(UnitOfOrderQueueFinder var0) {
      _unitOfOrderQueueFinder = var0;
   }

   public BufferingManager_WLS() throws BufferingException {
   }

   public BufferingDispatch newBufferingDispatch() {
      return new BufferingDispatch_WLS();
   }

   public void bufferMessagePlatform(Message var1, QueueSender var2, String var3, long var4, int var6, String var7, long var8) throws BufferingException {
      try {
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("** WLS bufferMessagePlatform(): targetURI='" + var3 + "', " + "setting Retry Delay='" + var4 + ", " + "redeliveryLimit='" + var6 + ", " + "unitOfOrder='" + var7 + "'.");
         }

         var1.setLongProperty("WLSRetryDelay", var4);
         ((WLMessageProducer)var2).setRedeliveryLimit(var6);
         ((WLMessageProducer)var2).setUnitOfOrder(var7);
         BufferingFeature.BufferingFeatureUsers var10 = BufferingFeature.getBufferingFeatureUser(var3);
         if (var10.equals(BufferingFeature.BufferingFeatureUsers.WSRM)) {
            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.fine("** WLS bufferMessagePlatform():  setting message properties for feature WS-RM:  SAFSequenceName='" + var7 + "', " + "SAFSeqNumber='" + var8 + "', " + "SAFNeedReorder='" + true + "'");
            }

            ((WLMessage)var1).setSAFSequenceName(var7);
            ((WLMessage)var1).setSAFSeqNumber(var8);
            ((MessageImpl)var1).setSAFNeedReorder(true);
         }

      } catch (Exception var11) {
         var11.printStackTrace();
         if (var11.getCause() != null) {
            var11.getCause().printStackTrace();
         }

         throw new BufferingException("Could not enqueue buffered message: " + var11, var11);
      }
   }

   protected final JmsSessionPool getNonTransactedSessionPoolPlatform() throws BufferingException {
      try {
         Class var2 = Class.forName("weblogic.jms.common.JMSServerUtilities");
         Method var3 = var2.getMethod("getConnectionFactory");
         if (var3 != null) {
            Object var4 = var3.invoke((Object)null);
            QueueConnectionFactory var5 = (QueueConnectionFactory)var4;
            JmsSessionPool var1 = new JmsSessionPool(var5, false, 1);
            return var1;
         } else {
            throw new BufferingException("Could not load method weblogic.jms.common.JMSServerUtilities.getXAConnectionFactory1");
         }
      } catch (Exception var6) {
         if (var6 instanceof BufferingException) {
            throw (BufferingException)var6;
         } else {
            throw new BufferingException(var6);
         }
      }
   }

   protected final QueueSender getQueueSenderPlatform(final QueueSession var1, String var2, String var3) throws JMSException {
      final Queue var5 = null;
      if (var3 != null && _unitOfOrderQueueFinder != null) {
         String var6 = _unitOfOrderQueueFinder.findQueueJndiNameForUnitOfOrder(var2, var3);
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Calculated UnitOfOrder-specific Queue JNDI: " + var6 + " for UnitOfOrder: " + var3 + " from Distributed Queue JNDI: " + var2);
         }

         if (var6 != null) {
            try {
               var5 = (Queue)this.ctx.lookup(var6);
            } catch (Exception var11) {
               JMSException var8 = new JMSException(var11.toString(), var11.toString());
               var8.setLinkedException(var11);
               throw var8;
            }
         }
      }

      if (var5 == null) {
         try {
            var5 = (Queue)this.ctx.lookup(var2);
         } catch (Exception var10) {
            JMSException var7 = new JMSException(var10.toString(), var10.toString());
            var7.setLinkedException(var10);
            throw var7;
         }
      }

      PrivilegedExceptionAction var13 = new PrivilegedExceptionAction() {
         public Object run() throws JMSException {
            return var1.createSender(var5);
         }
      };

      try {
         QueueSender var4 = (QueueSender)var13.run();
         return var4;
      } catch (Exception var12) {
         if (var12 instanceof JMSException) {
            throw (JMSException)var12;
         } else {
            JMSException var9 = new JMSException(var12.toString());
            var9.setLinkedException(var12);
            throw var9;
         }
      }
   }

   protected final void sendMessagePlatform(String var1, final QueueSender var2, final Message var3) throws JMSException {
      PrivilegedExceptionAction var4 = new PrivilegedExceptionAction() {
         public Object run() throws JMSException {
            var2.send(var3);
            return null;
         }
      };

      try {
         var4.run();
      } catch (JMSException var7) {
         throw var7;
      } catch (Exception var8) {
         JMSException var6 = new JMSException(var8.toString());
         var6.setLinkedException(var8);
         throw var6;
      }
   }

   public void cleanupDynamicMDBs(Map<String, Object> var1) {
      if (var1 != null) {
         Iterator var2 = var1.values().iterator();

         while(var2.hasNext()) {
            Object var3 = var2.next();
            DynamicEJBModule var4 = (DynamicEJBModule)var3;
            var4.undeployDynamicEJB();
         }
      }

   }

   public interface UnitOfOrderQueueFinder {
      String findQueueJndiNameForUnitOfOrder(String var1, String var2);
   }
}
