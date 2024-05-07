package weblogic.wsee.server.jms;

import java.io.ByteArrayInputStream;
import java.nio.charset.Charset;
import java.security.AccessController;
import java.security.PrivilegedExceptionAction;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSession;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.security.auth.login.LoginException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.rpc.JAXRPCException;
import org.w3c.dom.Document;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrincipalAuthenticator;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityService;
import weblogic.security.service.SecurityServiceManager;
import weblogic.security.service.SecurityService.ServiceType;
import weblogic.wsee.connection.transport.TransportUtil;
import weblogic.wsee.server.ServerUtil;
import weblogic.wsee.util.JmsUtil;
import weblogic.wsee.util.ServerSecurityHelper;
import weblogic.wsee.util.StringUtil;
import weblogic.wsee.util.Verbose;

public class JmsQueueListener extends TimerTask implements MessageListener {
   private static final boolean verbose = Verbose.isVerbose(JmsQueueListener.class);
   private QueueConnectionFactory factory;
   private QueueConnection connection;
   private QueueSession session;
   private QueueReceiver receiver;
   private QueueReceiver receiver81;
   private String wsUrl;
   private ServerUtil.QueueInfo queueInfo;
   private String factoryName;
   private Timer timer;
   private JmsWebservicesMessageDispatcher dispatcher = null;
   private static int TIME_TO_SLEEP = 60000;
   private static final String DELIMITER = "##";
   private static Set<String> queueMap = new HashSet();
   private static Map<String, Integer> countMap = new HashMap();
   private static boolean hasServices = false;
   private static Set asyncResponseListeners = new HashSet();
   private PrincipalAuthenticator _pa;
   private static final AuthenticatedSubject _kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   private static void addJmsListener(String var0, ServerUtil.QueueInfo var1, JmsQueueListener var2) {
      String var3 = var0 + "##" + var1.getQueueName();
      queueMap.add(var3);
      if (verbose) {
         Verbose.log((Object)("Added new jms queue listener to queue map for url " + var0 + " queue " + var1.getQueueName()));
      }

   }

   static void removeJmsListener(String var0, String var1) {
      String var2 = var0 + "##" + var1;
      synchronized(queueMap) {
         Integer var4 = (Integer)countMap.remove(var2);
         if (var4 != null) {
            if (var4 <= 1) {
               if (queueMap.remove(var2)) {
                  if (verbose) {
                     Verbose.log((Object)("Removed jms queue listener from map for url " + var0 + " queue " + var1));
                  }
               } else if (verbose) {
                  Verbose.log((Object)("No jms Queue listener present for url " + var0 + " queue " + var1));
               }
            } else {
               countMap.put(var2, new Integer(var4 - 1));
            }
         }

      }
   }

   static JmsQueueListener findOrCreateJmsListener(String var0, ServerUtil.QueueInfo var1, String var2) {
      String var3 = var0 + "##" + var1.getQueueName();
      boolean var5 = false;
      JmsQueueListener var4;
      boolean var6;
      synchronized(queueMap) {
         if (queueMap.contains(var3) && verbose) {
            Verbose.log((Object)("Jms Queue Listener for url " + var0 + " queue " + var1.getQueueName() + " already created"));
         }

         var4 = new JmsQueueListener(var0, var1, var2);
         addJmsListener(var0, var1, var4);
         Integer var8 = (Integer)countMap.get(var3);
         if (var8 != null) {
            countMap.put(var3, new Integer(var8 + 1));
         } else {
            countMap.put(var3, new Integer(1));
         }

         if (var0.indexOf("/_async/AsyncResponseService") == -1) {
            var5 = true;
            hasServices = true;
         } else if (hasServices) {
            var5 = true;
         } else {
            asyncResponseListeners.add(var4);
         }

         var6 = hasServices && asyncResponseListeners.size() != 0;
      }

      if (verbose) {
         Verbose.log((Object)("Created jms Queue listener  for url " + var0 + " queue " + var1.getQueueName()));
      }

      if (var5) {
         var4.connect();
      }

      if (var6) {
         connectAsyncResponseListeners();
      }

      return var4;
   }

   private static void connectAsyncResponseListeners() {
      Object[] var0;
      synchronized(queueMap) {
         var0 = asyncResponseListeners.toArray();
         asyncResponseListeners.clear();
      }

      for(int var1 = 0; var1 < var0.length; ++var1) {
         if (verbose) {
            Verbose.log((Object)("In connectAsyncResponseListeners. Connecting listener " + (var1 + 1) + "/" + var0.length + " = " + var0[var1]));
         }

         ((JmsQueueListener)var0[var1]).connect();
         if (verbose) {
            Verbose.log((Object)("In connectAsyncResponseListeners. Done connecting listener " + (var1 + 1) + "/" + var0.length + " = " + var0[var1]));
         }
      }

   }

   private JmsQueueListener(String var1, ServerUtil.QueueInfo var2, String var3) throws JAXRPCException {
      this.wsUrl = var1;
      this.queueInfo = var2;
      this.factoryName = var3;
   }

   private synchronized void connect() {
      if (verbose) {
         Verbose.log((Object)("Connecting to JMS queue " + this.queueInfo.getQueueName()));
      }

      boolean var1 = false;
      StringBuffer var2 = new StringBuffer();
      StringBuffer var3 = new StringBuffer();

      try {
         InitialContext var4 = new InitialContext();
         this.factory = (QueueConnectionFactory)var4.lookup(this.factoryName);
         var1 = TransportUtil.getForeignCredentials(this.factoryName, var4, var2, var3);
         if (var1) {
            this.connection = this.factory.createQueueConnection(var2.toString(), var3.toString());
         } else {
            this.connection = this.factory.createQueueConnection();
         }

         this.session = this.connection.createQueueSession(false, 1);
         final Queue var5 = (Queue)var4.lookup(this.queueInfo.getQueueName());
         final String var6 = JmsUtil.getJmsTransportSelector(this.wsUrl);
         final String var7 = JmsUtil.getWLW81Selector(this.wsUrl);
         if (verbose) {
            Verbose.log((Object)("Select for " + var6));
         }

         PrivilegedExceptionAction var8 = new PrivilegedExceptionAction() {
            public Object run() throws JMSException {
               JmsQueueListener.this.receiver = JmsQueueListener.this.session.createReceiver(var5, var6);
               JmsQueueListener.this.receiver81 = JmsQueueListener.this.session.createReceiver(var5, var7);
               return null;
            }
         };

         try {
            if (StringUtil.isEmpty(this.queueInfo.getMdbRunAsPrincipalName())) {
               var8.run();
            } else {
               AuthenticatedSubject var9 = this.authenticateAs(this.queueInfo.getMdbRunAsPrincipalName());
               SecurityServiceManager.runAs(_kernelId, var9, var8);
            }
         } catch (Exception var11) {
            if (var11 instanceof JMSException) {
               throw (JMSException)var11;
            }

            JMSException var10 = new JMSException(var11.toString());
            var10.setLinkedException(var11);
            throw var10;
         }

         this.receiver.setMessageListener(this);
         this.receiver81.setMessageListener(new WLW81Listener());
         this.connection.start();
         if (var1) {
            this.dispatcher = new JmsWebservicesMessageDispatcher(this.wsUrl, this.factory, var2.toString(), var3.toString());
         } else {
            this.dispatcher = new JmsWebservicesMessageDispatcher(this.wsUrl, this.factory);
         }

         if (verbose) {
            Verbose.log((Object)("connected to JMS queue " + this.queueInfo.getQueueName()));
         }

         if (this.timer != null) {
            this.timer.cancel();
            this.timer = null;
         }
      } catch (NamingException var12) {
         if (this.wsUrl.indexOf("_async/AsyncResponseService") != -1) {
            if (verbose) {
               Verbose.log((Object)("Warning: JMS queue '" + this.queueInfo.getQueueName() + "' is not found, as a result, Web Service async responses via " + "jms transport is not supported. If the target service uses " + "JMS transport, the responses will not be able to come back."));
            }
         } else {
            this.handleConnectionException(var12);
         }
      } catch (JMSException var13) {
         this.handleConnectionException(var13);
      }

   }

   private AuthenticatedSubject authenticateAs(String var1) {
      try {
         if (this._pa == null) {
            this._pa = (PrincipalAuthenticator)this.getService(ServiceType.AUTHENTICATION);
         }

         return this._pa.impersonateIdentity(var1);
      } catch (LoginException var3) {
         throw new SecurityException("User " + var1 + " is an invalid user");
      }
   }

   private SecurityService getService(SecurityService.ServiceType var1) {
      try {
         SecurityService var2 = SecurityServiceManager.getSecurityService(_kernelId, SecurityServiceManager.getDefaultRealmName(), var1);
         return var2;
      } catch (Exception var4) {
         throw new SecurityException("Unexpected exception: " + var4.toString(), var4);
      }
   }

   private void handleConnectionException(Exception var1) {
      Verbose.logException(var1);
      if (this.timer == null) {
         this.timer = new Timer(true);
         this.timer.scheduleAtFixedRate(this, (long)TIME_TO_SLEEP, (long)TIME_TO_SLEEP);
      }

   }

   public synchronized void close() throws JMSException {
      if (this.dispatcher != null) {
         this.dispatcher.shutdown();
      }

      if (this.receiver != null) {
         this.receiver.close();
      }

      if (this.receiver81 != null) {
         this.receiver81.close();
      }

      if (this.session != null) {
         this.session.close();
      }

      if (this.connection != null) {
         this.connection.close();
      }

   }

   public void run() {
      this.connect();
   }

   public void onMessage(Message var1) {
      if (verbose) {
         Verbose.log((Object)("Got a JMS message for service " + this.wsUrl + " on queue " + this.queueInfo.getQueueName() + ": " + var1));
      }

      ServerSecurityHelper.assertAnonymousIdentity();
      if (!(var1 instanceof TextMessage) && !(var1 instanceof BytesMessage)) {
         throw new JAXRPCException("not text message or a bytes message" + var1);
      } else {
         try {
            if (var1.getJMSReplyTo() == null) {
               String var2 = var1.getStringProperty("URI");
               var1.clearProperties();
               var1.setStringProperty("URI", var2);
               var1.setStringProperty("_wls_mimehdrContent_Type", new String("text/xml"));
               var1.setBooleanProperty("IsWLW81Message", true);
            }
         } catch (JMSException var3) {
            throw new JAXRPCException("Failed to get properties from JMS text message:" + var3, var3);
         }

         this.processMessage(var1);
      }
   }

   private void processMessage(Message var1) {
      this.dispatcher.dispatchMessage(var1);
   }

   static void setHasServices() {
      synchronized(queueMap) {
         if (verbose) {
            Verbose.log((Object)("In setHasServices with hasServices=" + hasServices));
         }

         if (!hasServices) {
            if (verbose) {
               Verbose.log((Object)("In setHasServices with asyncResponseListeners.size()=" + asyncResponseListeners.size()));
            }

            hasServices = true;
            if (asyncResponseListeners.size() != 0) {
               connectAsyncResponseListeners();
            }

         }
      }
   }

   private class WLW81Listener implements MessageListener {
      private WLW81Listener() {
      }

      public void onMessage(Message var1) {
         if (JmsQueueListener.verbose) {
            Verbose.log((Object)("Got a 8.1 JMS message:" + var1));
         }

         ServerSecurityHelper.assertAnonymousIdentity();
         if (!(var1 instanceof TextMessage)) {
            throw new JAXRPCException("not text message" + var1);
         } else {
            this.convertMessage((TextMessage)var1);
            JmsQueueListener.this.processMessage(var1);
         }
      }

      private String getEncodingFromMessage(String var1) {
         try {
            int var2 = var1.indexOf("<?");
            int var3 = var1.indexOf("?>");
            if (var2 != -1 && var3 != -1 && var3 > var2) {
               String var4 = var1.substring(var2, var3 + 2) + "<test/>";
               System.out.println(var4);
               DocumentBuilderFactory var5 = DocumentBuilderFactory.newInstance();
               DocumentBuilder var6 = var5.newDocumentBuilder();
               Document var7 = var6.parse(new ByteArrayInputStream(var4.getBytes()));
               if (Charset.isSupported(var7.getXmlEncoding())) {
                  return var7.getXmlEncoding();
               }
            }
         } catch (Exception var8) {
            if (JmsQueueListener.verbose) {
               Verbose.logException(var8);
            }
         }

         return null;
      }

      private void convertMessage(TextMessage var1) {
         try {
            String var2 = var1.getStringProperty("URI");
            var1.clearProperties();
            var1.setStringProperty("URI", var2);
            String var3 = this.getEncodingFromMessage(var1.getText());
            if (var3 == null) {
               var1.setStringProperty("_wls_mimehdrContent_Type", new String("text/xml"));
            } else {
               var1.setStringProperty("_wls_mimehdrContent_Type", new String("text/xml; charset=\"" + var3 + "\""));
            }

            var1.setBooleanProperty("IsWLW81Message", true);
         } catch (JMSException var4) {
            throw new JAXRPCException("Failed to get properties from JMS text message:" + var4, var4);
         }
      }

      // $FF: synthetic method
      WLW81Listener(Object var2) {
         this();
      }
   }
}
