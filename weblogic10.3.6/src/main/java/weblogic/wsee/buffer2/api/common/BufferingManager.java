package weblogic.wsee.buffer2.api.common;

import com.sun.istack.NotNull;
import com.sun.xml.ws.api.pipe.ClientTubeAssemblerContext;
import com.sun.xml.ws.api.pipe.ServerTubeAssemblerContext;
import com.sun.xml.ws.api.server.WSEndpoint;
import java.io.Serializable;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import weblogic.wsee.buffer2.exception.BufferingException;
import weblogic.wsee.buffer2.internal.common.JmsSessionPool;
import weblogic.wsee.buffer2.utils.BufferingConstants;
import weblogic.wsee.jaxws.spi.WLSEndpoint;
import weblogic.wsee.jaxws.tubeline.TubelineSpliceFactory;

public abstract class BufferingManager {
   protected static final Logger LOGGER = Logger.getLogger(BufferingManager.class.getName());
   public static final String RETRY_DELAY = "WLSRetryDelay";
   private static volatile JmsSessionPool nonTransactedSessionPool;
   protected InitialContext ctx;
   private static WLSEndpointListener _wlsEndpointListener = new WLSEndpointListener();

   protected BufferingManager() throws BufferingException {
      try {
         this.ctx = new InitialContext();
      } catch (NamingException var2) {
         throw new BufferingException(var2);
      }
   }

   public void bufferMessage(String var1, Serializable var2, @NotNull BufferingConstants.MsgDirection var3, int var4, long var5, String var7, long var8) throws BufferingException {
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("Buffering message for " + var1);
         LOGGER.fine("(retryCount = " + var4 + ", retryDelay = " + var5 + ")");
      }

      QueueSession var10 = null;
      QueueSender var11 = null;

      try {
         var10 = this.getNonTransactedSession();
         var11 = this.getQueueSender(var10, var1, var7);
         ObjectMessage var12 = var10.createObjectMessage(var2);
         var12.setStringProperty(BufferingConstants.TARGET_URI, var1);
         var12.setStringProperty(BufferingConstants.DIRECTION, var3.toString());
         this.bufferMessagePlatform(var12, var11, var1, var5, var4, var7, var8);
         this.sendMessage(var1, var11, var12);
      } catch (Exception var23) {
         var23.printStackTrace();
         if (var23.getCause() != null) {
            var23.getCause().printStackTrace();
         }

         throw new BufferingException("Could not enqueue buffered message: " + var23, var23);
      } finally {
         if (var11 != null) {
            try {
               var11.close();
            } catch (Exception var22) {
               if (LOGGER.isLoggable(Level.FINE)) {
                  LOGGER.log(Level.FINE, var22.toString(), var22);
               } else {
                  var22.printStackTrace();
               }
            }
         }

         try {
            this.putNonTransactedSession(var10);
         } catch (BufferingException var21) {
         }

      }

   }

   public QueueSession getNonTransactedSession() throws BufferingException {
      return this.getNonTransactedSessionPool().take();
   }

   public void putNonTransactedSession(QueueSession var1) throws BufferingException {
      this.getNonTransactedSessionPool().recycle(var1);
   }

   protected JmsSessionPool getNonTransactedSessionPool() throws BufferingException {
      JmsSessionPool var1 = nonTransactedSessionPool;
      if (var1 == null) {
         Class var2 = BufferingFeature.class;
         synchronized(BufferingFeature.class) {
            var1 = nonTransactedSessionPool;
            if (var1 == null) {
               nonTransactedSessionPool = var1 = this.getNonTransactedSessionPoolPlatform();
            }
         }
      }

      return var1;
   }

   protected QueueSender getQueueSender(QueueSession var1, String var2, String var3) throws Exception {
      String var4 = BufferingFeature.getQueueJndiName(var2);
      this.ctx.lookup(var4);
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("Buffering message for " + var2);
         LOGGER.fine("put onto Queue ='" + var4 + "')");
      }

      return this.getQueueSenderPlatform(var1, var4, var3);
   }

   protected void sendMessage(String var1, QueueSender var2, Message var3) throws JMSException {
      this.sendMessagePlatform(var1, var2, var3);
   }

   public BufferingFeature newBufferingFeature(BufferingFeature.BufferingFeatureUsers var1, ServerTubeAssemblerContext var2, boolean var3, WSEndpoint var4, TubelineSpliceFactory.DispatchFactory var5) throws BufferingException {
      BufferingFeature var6 = BufferingFeature.newBufferingFeature(var1, var2);
      var6 = this.newBufferingFeature(var4, var5, var6);
      var6.setDirection(var3 ? BufferingConstants.MsgDirection.RESPONSE : BufferingConstants.MsgDirection.REQUEST);
      return var6;
   }

   public BufferingFeature newBufferingFeature(BufferingFeature.BufferingFeatureUsers var1, ClientTubeAssemblerContext var2, boolean var3, WLSEndpoint var4, TubelineSpliceFactory.DispatchFactory var5) throws BufferingException {
      BufferingFeature var6 = BufferingFeature.newBufferingFeature(var1, var2);
      var6 = this.newBufferingFeature(var4.getWSEndpoint(), var5, var6);
      var6.setDirection(var3 ? BufferingConstants.MsgDirection.RESPONSE : BufferingConstants.MsgDirection.REQUEST);
      var4.addListener(_wlsEndpointListener);
      return var6;
   }

   private BufferingFeature newBufferingFeature(WSEndpoint var1, TubelineSpliceFactory.DispatchFactory var2, BufferingFeature var3) {
      BufferingDispatch var4 = this.newBufferingDispatch();
      var4.setWSEndpoint(var1);
      var4.setDispatchFactory(var2);
      var3.setBufferDispatch(var4);
      return var3;
   }

   protected abstract void sendMessagePlatform(String var1, QueueSender var2, Message var3) throws JMSException;

   public abstract <T extends BufferingDispatch> T newBufferingDispatch();

   protected abstract JmsSessionPool getNonTransactedSessionPoolPlatform() throws BufferingException;

   protected abstract QueueSender getQueueSenderPlatform(QueueSession var1, String var2, String var3) throws JMSException;

   protected abstract void bufferMessagePlatform(Message var1, QueueSender var2, String var3, long var4, int var6, String var7, long var8) throws BufferingException;

   public abstract void cleanupDynamicMDBs(Map<String, Object> var1);

   private static class WLSEndpointListener implements WLSEndpoint.Listener {
      private WLSEndpointListener() {
      }

      public void endpointStopping(WLSEndpoint var1) {
         BufferingFeature.unRegisterBufferingFeature(var1.getWSEndpoint());
      }

      // $FF: synthetic method
      WLSEndpointListener(Object var1) {
         this();
      }
   }
}
