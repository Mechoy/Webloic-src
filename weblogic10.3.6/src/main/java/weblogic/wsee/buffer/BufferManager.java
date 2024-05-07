package weblogic.wsee.buffer;

import java.io.Serializable;
import java.security.PrivilegedExceptionAction;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.xml.rpc.JAXRPCException;
import weblogic.jms.common.JMSServerUtilities;
import weblogic.jms.common.MessageImpl;
import weblogic.jms.extensions.WLMessage;
import weblogic.jms.extensions.WLMessageProducer;
import weblogic.wsee.server.ServerUtil;
import weblogic.wsee.util.Verbose;

public class BufferManager {
   private static final boolean verbose = Verbose.isVerbose(BufferManager.class);
   public static final String RETRY_DELAY = "BEARetryDelay";
   private static final String QUEUE_SESSION_POOL_SIZE_PROP = "weblogic.wsee.buffer.QueueSessionPoolSize";
   private static final BufferManager theManager = new BufferManager();
   private final Map listeners = new HashMap();
   private final Map<String, ConcurrentHashMap<String, Long>> retryDelays = new ConcurrentHashMap();
   private final Map errorListeners = new HashMap();
   private final Map<String, ServerUtil.QueueInfo> queues = new HashMap();
   private boolean initialized;
   private QueueConnectionFactory factory;
   private QueueConnection connection;
   private Vector sessions;
   private Vector transactedSessions;
   private Context ctx;
   private int numWaiters = 0;

   public static BufferManager instance() {
      return theManager;
   }

   private synchronized QueueSession getSession(boolean var1) {
      Vector var2;
      if (var1) {
         var2 = this.transactedSessions;
      } else {
         var2 = this.sessions;
      }

      boolean var3 = false;

      while(var2.isEmpty()) {
         if (var2.isEmpty()) {
            if (!var3) {
               ++this.numWaiters;
               var3 = true;
            }

            if (verbose) {
               Verbose.log((Object)("BufferManager ran out of sessions. Waiting as one of " + this.numWaiters + " waiting threads"));
            }

            try {
               this.wait(30000L);
            } catch (InterruptedException var5) {
            }
         }
      }

      if (var3) {
         --this.numWaiters;
         if (verbose) {
            Verbose.log((Object)("BufferManager now has an available session. Waking up to use newly available session. Available sessions=" + var2.size() + " numWaiters=" + this.numWaiters));
         }
      }

      QueueSession var4 = (QueueSession)var2.remove(0);
      return var4;
   }

   private synchronized void putSession(QueueSession var1, boolean var2) {
      Vector var3;
      if (var2) {
         var3 = this.transactedSessions;
      } else {
         var3 = this.sessions;
      }

      if (var1 != null) {
         var3.add(var1);
         if (verbose && this.numWaiters > 0) {
            Verbose.log((Object)("BufferManager put back one session. Now have " + var3.size() + " sessions and " + this.numWaiters + " threads waiting for a session. Notifying all threads"));
         }

         this.notifyAll();
      }
   }

   public void bufferMessage(String var1, Serializable var2, int var3, long var4) {
      this.initialize();
      if (verbose) {
         Verbose.log((Object)("Buffering message for " + var1));
         Verbose.log((Object)("(retryCount = " + var3 + ", retryDelay = " + var4 + ")"));
      }

      QueueSession var6 = null;
      QueueSender var7 = null;

      try {
         var6 = this.getSession(true);
         var7 = this.getQueueSender(var6, var1);
         ObjectMessage var8 = var6.createObjectMessage(var2);
         var8.setStringProperty("ASYNC_URI", var1);
         var8.setLongProperty("BEARetryDelay", var4);
         ((WLMessageProducer)var7).setRedeliveryLimit(var3);
         this.sendMessage(var1, var7, var8);
         var6.commit();
      } catch (JMSException var15) {
         throw new JAXRPCException("Could not enqueue buffered message: " + var15, var15);
      } finally {
         if (var7 != null) {
            try {
               var7.close();
            } catch (Exception var16) {
               if (verbose) {
                  Verbose.logException(var16);
               } else {
                  var16.printStackTrace();
               }
            }
         }

         this.putSession(var6, true);
      }

   }

   private void sendMessage(String var1, final QueueSender var2, final Message var3) throws JMSException {
      if (var1 != null) {
         var1.hashCode();
      }

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

   public void bufferMessageWithServiceURI(String var1, String var2, Serializable var3, int var4, long var5) {
      this.initialize();
      if (verbose) {
         Verbose.log((Object)("Buffering message for " + var1));
         Verbose.log((Object)("(retryCount = " + var4 + ", retryDelay = " + var5 + ")"));
      }

      QueueSession var7 = null;
      QueueSender var8 = null;

      try {
         var7 = this.getSession(true);
         var8 = this.getQueueSender(var7, var1);
         ObjectMessage var9 = var7.createObjectMessage(var3);
         var9.setStringProperty("ASYNC_URI", var1);
         var9.setLongProperty("BEARetryDelay", var5);
         var9.setStringProperty("SERV_URI", var2);
         ((WLMessageProducer)var8).setRedeliveryLimit(var4);
         this.sendMessage(var1, var8, var9);
         var7.commit();
      } catch (JMSException var16) {
         throw new JAXRPCException("Could not enqueue buffered message: " + var16, var16);
      } finally {
         if (var8 != null) {
            try {
               var8.close();
            } catch (Exception var17) {
               if (verbose) {
                  Verbose.logException(var17);
               } else {
                  var17.printStackTrace();
               }
            }
         }

         this.putSession(var7, true);
      }

   }

   public void bufferMessageUOO(String var1, Serializable var2, int var3, long var4, String var6, long var7) {
      this.initialize();
      if (verbose) {
         Verbose.log((Object)("Buffering message for " + var1));
         Verbose.log((Object)("(retryCount = " + var3 + ", retryDelay = " + var4 + ")"));
      }

      QueueSession var9 = null;
      QueueSender var10 = null;

      try {
         var9 = this.getSession(false);
         var10 = this.getQueueSender(var9, var1);
         ObjectMessage var11 = var9.createObjectMessage(var2);
         var11.setStringProperty("ASYNC_URI", var1);
         var11.setLongProperty("BEARetryDelay", var4);
         ((WLMessageProducer)var10).setRedeliveryLimit(var3);
         ((WLMessageProducer)var10).setUnitOfOrder(var6);
         ((WLMessage)var11).setSAFSequenceName(var6);
         ((WLMessage)var11).setSAFSeqNumber(var7);
         ((MessageImpl)var11).setSAFNeedReorder(true);
         this.sendMessage(var1, var10, var11);
      } catch (JMSException var19) {
         var19.printStackTrace();
         if (var19.getCause() != null) {
            var19.getCause().printStackTrace();
         }

         throw new JAXRPCException("Could not enqueue buffered message: " + var19, var19);
      } finally {
         if (var10 != null) {
            try {
               var10.close();
            } catch (Exception var18) {
               if (verbose) {
                  Verbose.logException(var18);
               } else {
                  var18.printStackTrace();
               }
            }
         }

         this.putSession(var9, false);
      }

   }

   public void dispatchBufferedMessage(String var1, Message var2) throws Exception {
      if (verbose) {
         Verbose.log((Object)("Dispatching buffered message to " + var1));
      }

      MessageListener var3 = this.getMessageListener(var1);
      if (var3 == null) {
         throw new JAXRPCException("Message listener for " + var1 + " not available");
      } else {
         var3.onMessage(var2);
      }
   }

   public void dispatchErrorMessage(String var1, Message var2) throws Exception {
      if (verbose) {
         Verbose.log((Object)("Dispatching error message to " + var1));
      }

      MessageListener var3 = this.getErrorListener(var1);
      if (var3 == null) {
         throw new JAXRPCException("Error listener for " + var1 + " not available");
      } else {
         var3.onMessage(var2);
      }
   }

   public synchronized void addErrorListener(String var1, MessageListener var2) {
      if (verbose) {
         Verbose.log((Object)("Adding error listener for " + var1));
      }

      this.errorListeners.put(var1, var2);
   }

   public synchronized void addMessageListener(String var1, MessageListener var2) {
      if (verbose) {
         Verbose.log((Object)("Adding message listener for " + var1));
      }

      this.listeners.put(var1, var2);
   }

   public synchronized MessageListener getMessageListener(String var1) {
      return (MessageListener)this.listeners.get(var1);
   }

   public synchronized void removeMessageListener(String var1) {
      this.listeners.remove(var1);
   }

   public synchronized MessageListener getErrorListener(String var1) {
      return (MessageListener)this.errorListeners.get(var1);
   }

   public synchronized void removeErrorListener(String var1) {
      this.errorListeners.remove(var1);
   }

   public void putRetryDelay(String var1, String var2, long var3) {
      ConcurrentHashMap var5 = (ConcurrentHashMap)this.retryDelays.get(var1);
      if (var5 == null) {
         var5 = new ConcurrentHashMap();
         this.retryDelays.put(var1, var5);
      }

      var5.put(var2, var3);
   }

   public long getRetryDelay(String var1, String var2) {
      ConcurrentHashMap var3 = (ConcurrentHashMap)this.retryDelays.get(var1);
      if (var3 == null) {
         return -1L;
      } else {
         Long var4 = (Long)var3.get(var2);
         return var4 == null ? -1L : var4;
      }
   }

   public void removeRetryDelay(String var1) {
      this.retryDelays.remove(var1);
   }

   public synchronized void setTargetQueue(String var1, ServerUtil.QueueInfo var2) {
      if (verbose) {
         Verbose.log((Object)("Setting " + var2.getQueueName() + " as target queue for " + var1));
      }

      this.queues.put(var1, var2);
   }

   public synchronized ServerUtil.QueueInfo getTargetQueue(String var1) {
      ServerUtil.QueueInfo var2 = (ServerUtil.QueueInfo)this.queues.get(var1);
      if (var2 == null) {
         var2 = ServerUtil.getMessagingQueueInfo();
      }

      return var2;
   }

   private QueueSender getQueueSender(final QueueSession var1, String var2) {
      try {
         if (verbose) {
            Verbose.log((Object)("Looking up queue for " + var2));
         }

         ServerUtil.QueueInfo var3 = this.getTargetQueue(var2);
         if (var3 == null) {
            throw new JAXRPCException("Could not find buffer queue for " + var2);
         } else {
            if (verbose) {
               Verbose.log((Object)("queue JNDI " + var3.getQueueName()));
            }

            final Queue var4 = (Queue)this.ctx.lookup(var3.getQueueName());
            PrivilegedExceptionAction var6 = new PrivilegedExceptionAction() {
               public Object run() throws JMSException {
                  return var1.createSender(var4);
               }
            };

            QueueSender var5;
            try {
               var5 = (QueueSender)var6.run();
            } catch (Exception var9) {
               if (var9 instanceof JMSException) {
                  throw (JMSException)var9;
               }

               JMSException var8 = new JMSException(var9.toString());
               var8.setLinkedException(var9);
               throw var8;
            }

            return var5;
         }
      } catch (NamingException var10) {
         var10.printStackTrace();
         throw new JAXRPCException("Could not find buffer queue for " + var2, var10);
      } catch (JMSException var11) {
         throw new JAXRPCException("Could not create sender for buffer queue", var11);
      }
   }

   private synchronized void initialize() {
      if (!this.initialized) {
         if (verbose) {
            Verbose.log((Object)"Initializing");
         }

         try {
            this.sessions = this.initializeSessions(false);
            this.transactedSessions = this.initializeSessions(true);
            this.connection.start();
         } catch (JMSException var2) {
            throw new JAXRPCException("Could not create JMS resources: " + var2);
         }

         this.initialized = true;
      }

   }

   private Vector initializeSessions(boolean var1) {
      Vector var2 = new Vector();
      if (var1) {
         this.factory = (QueueConnectionFactory)JMSServerUtilities.getXAConnectionFactory();
      } else {
         this.factory = (QueueConnectionFactory)JMSServerUtilities.getXAConnectionFactory1();
      }

      try {
         this.ctx = new InitialContext();
         this.connection = this.factory.createQueueConnection();
         int var3 = 10;
         String var4 = System.getProperty("weblogic.wsee.buffer.QueueSessionPoolSize");
         int var5;
         if (var4 != null) {
            var4 = var4.trim();
            if (var4.length() != 0) {
               try {
                  var5 = Integer.parseInt(var4);
                  if (var5 < 2) {
                     var5 = 2;
                  } else if (var5 > 50) {
                     var5 = 50;
                  }

                  var3 = var5;
               } catch (NumberFormatException var6) {
               }
            }
         }

         if (verbose) {
            Verbose.log((Object)("BufferManager initializing " + var3 + " " + (var1 ? "transacted" : "non-transacted") + " sessions"));
         }

         for(var5 = 0; var5 < var3; ++var5) {
            var2.add(this.connection.createQueueSession(var1, 1));
         }

         return var2;
      } catch (NamingException var7) {
         throw new JAXRPCException("Could not get buffering queue: " + var7);
      } catch (JMSException var8) {
         throw new JAXRPCException("Could not create JMS resources: " + var8);
      }
   }

   private BufferManager() {
   }
}
