package weblogic.wsee.connection.transport.jms;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.security.AccessController;
import java.util.Hashtable;
import javax.jms.BytesMessage;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.TemporaryQueue;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import weblogic.jms.common.LostServerException;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.utils.encoders.BASE64Encoder;
import weblogic.wsee.connection.transport.TransportUtil;
import weblogic.wsee.util.Verbose;

public class JmsQueueConnection {
   private QueueConnectionFactory factory;
   private QueueConnection connection;
   private QueueSession session;
   private QueueSender sender;
   private TemporaryQueue responseQueue;
   private QueueReceiver receiver;
   private boolean alive = true;
   private static final boolean verbose = Verbose.isVerbose(JmsQueueConnection.class);
   private JmsQueueConnectionPoolInternal pool;
   private String subjectString;
   public static final String JNDI_FACTORY = "weblogic.jndi.WLInitialContextFactory";

   public JmsQueueConnection(JmsTransportInfo var1) throws NamingException, IOException, JMSException {
      Hashtable var2 = new Hashtable();
      var2.put("java.naming.factory.initial", "weblogic.jndi.WLInitialContextFactory");
      if (var1.getJndiURL() != null) {
         if (verbose) {
            Verbose.say("Setting Provider URL to Jms JNDI url " + var1.getJndiURL());
         }

         var2.put("java.naming.provider.url", var1.getJndiURL());
      } else {
         if (verbose) {
            Verbose.say("Setting Provider URL to default url t3://" + var1.getHost() + ":" + var1.getPort());
         }

         var2.put("java.naming.provider.url", "t3://" + var1.getHost() + ":" + var1.getPort());
      }

      String var3 = var1.getUsername();
      String var4 = var1.getPassword();
      if (verbose) {
         Verbose.say("Got username from the transportinfo: " + var3);
      }

      if (var3 != null) {
         if (verbose) {
            Verbose.say("Setting username to " + var3);
         }

         var2.put("java.naming.security.principal", var3);
         var2.put("java.naming.security.credentials", var4);
      }

      InitialContext var5 = new InitialContext(var2);
      boolean var6 = false;
      StringBuffer var7 = new StringBuffer();
      StringBuffer var8 = new StringBuffer();
      var6 = TransportUtil.getForeignCredentials(var1.getFactory(), var5, var7, var8);
      this.factory = (QueueConnectionFactory)var5.lookup(var1.getFactory());
      AuthenticatedSubject var9 = SecurityServiceManager.getCurrentSubject((AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction()));
      if (var9 != null && var3 != null) {
         this.subjectString = this.getSubjectString(var9);
      }

      if (verbose) {
         Verbose.say("Subject = " + var9);
      }

      if (var6) {
         this.connection = this.factory.createQueueConnection(var7.toString(), var8.toString());
      } else if (var3 != null) {
         this.connection = this.factory.createQueueConnection(var3, var4);
      } else {
         this.connection = this.factory.createQueueConnection();
      }

      this.session = this.connection.createQueueSession(false, 1);
      Queue var10 = (Queue)var5.lookup(var1.getQueue());
      this.sender = this.session.createSender(var10);
      if (!var1.isSendTo81()) {
         this.responseQueue = this.session.createTemporaryQueue();
         this.receiver = this.session.createReceiver(this.responseQueue);
      }

      this.connection.start();
      this.connection.setExceptionListener(new ExceptionListener() {
         public void onException(JMSException var1) {
            if (var1 instanceof LostServerException) {
               JmsQueueConnection.this.alive = false;
               if (JmsQueueConnection.verbose) {
                  Verbose.say("A LostServerException found. ");
               }
            }

         }
      });
      this.alive = true;
   }

   QueueReceiver getReceiver() {
      return this.receiver;
   }

   Queue getResponseQueue() {
      return this.responseQueue;
   }

   public QueueSender getSender() {
      return this.sender;
   }

   private String getSubjectString(AuthenticatedSubject var1) throws IOException {
      ByteArrayOutputStream var2 = new ByteArrayOutputStream();
      ObjectOutputStream var3 = new ObjectOutputStream(var2);
      var3.writeObject(var1);
      var3.flush();
      byte[] var4 = var2.toByteArray();
      return (new BASE64Encoder()).encodeBuffer(var4);
   }

   public Message getTextMessage() throws JMSException {
      TextMessage var1 = this.session.createTextMessage();
      if (this.subjectString != null) {
         var1.setStringProperty("WSEE_JMS_SUBJECT", this.subjectString);
      }

      return var1;
   }

   public Message getBytesMessage() throws JMSException {
      BytesMessage var1 = this.session.createBytesMessage();
      if (this.subjectString != null) {
         var1.setStringProperty("WSEE_JMS_SUBJECT", this.subjectString);
      }

      return var1;
   }

   void setPool(JmsQueueConnectionPoolInternal var1) {
      this.pool = var1;
   }

   void release() {
      if (verbose) {
         Verbose.say("releasing a connection");
      }

      JmsQueueConnectionPool.getInstance().release(this.pool, this);
   }

   public void close() throws JMSException {
      this.sender.close();
      this.session.close();
      this.connection.close();
      if (this.receiver != null) {
         this.receiver.close();
      }

      if (this.responseQueue != null) {
         this.responseQueue.delete();
      }

   }

   public boolean isAlive() {
      return this.alive;
   }

   public void closeIgoreException() {
      try {
         this.close();
      } catch (JMSException var2) {
      }

   }
}
