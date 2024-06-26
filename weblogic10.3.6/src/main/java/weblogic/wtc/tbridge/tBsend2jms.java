package weblogic.wtc.tbridge;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Hashtable;
import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public final class tBsend2jms {
   public static final String JNDI_FACTORY = "weblogic.jndi.WLInitialContextFactory";
   public static final String JMS_FACTORY = "weblogic.jms.ConnectionFactory";
   public static final String QUEUE = "weblogic.jms.Jms2TuxQueue";
   private QueueConnectionFactory qconFactory;
   private QueueConnection qcon;
   private QueueSession qsession;
   private QueueSender qsender;
   private Queue queue;
   private TextMessage textmsg;
   private BytesMessage bytesmsg;
   private static byte[] canofdata = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 0};

   public void init(Context var1, String var2) throws NamingException, JMSException {
      this.qconFactory = (QueueConnectionFactory)var1.lookup("weblogic.jms.ConnectionFactory");
      this.qcon = this.qconFactory.createQueueConnection();
      this.qsession = this.qcon.createQueueSession(false, 1);

      try {
         this.queue = (Queue)var1.lookup(var2);
      } catch (NamingException var4) {
         this.queue = this.qsession.createQueue(var2);
         var1.bind(var2, this.queue);
      }

      this.qsender = this.qsession.createSender(this.queue);
      this.textmsg = this.qsession.createTextMessage();
      this.bytesmsg = this.qsession.createBytesMessage();
      this.qcon.start();
   }

   public void close() throws JMSException {
      this.qsender.close();
      this.qsession.close();
      this.qcon.close();
   }

   public static void main(String[] var0) throws Exception {
      if (var0.length >= 1 && var0.length <= 2) {
         tBsend2jms var1 = new tBsend2jms();
         InitialContext var2;
         if (var0.length == 1) {
            var2 = getInitialContext(var0[0]);
            var1.init(var2, "weblogic.jms.Jms2TuxQueue");
         } else {
            var2 = getInitialContext(var0[1]);
            var1.init(var2, var0[0]);
         }

         BufferedReader var5 = new BufferedReader(new InputStreamReader(System.in));
         String var3 = null;
         boolean var4 = false;

         do {
            System.out.println("Enter message (\"quit\" to quit): ");
            var3 = var5.readLine();
            if (var3 != null && var3.trim().length() != 0) {
               var4 = var3.equalsIgnoreCase("quit") || var3.trim().equalsIgnoreCase("<XML><STRING>quit</STRING></XML>");
               if (var3.equalsIgnoreCase("bytes")) {
                  var1.sendbytes(canofdata);
               } else {
                  if (var3.equalsIgnoreCase("null")) {
                     var3 = null;
                  }

                  var1.send(var3);
               }
            }
         } while(!var4);

         var1.close();
      } else {
         System.out.println("Usage: tBsend2jms [queue] WebLogicURL");
      }
   }

   private static InitialContext getInitialContext(String var0) throws NamingException {
      Hashtable var1 = new Hashtable();
      var1.put("java.naming.factory.initial", "weblogic.jndi.WLInitialContextFactory");
      var1.put("java.naming.provider.url", var0);
      return new InitialContext(var1);
   }

   public void send(String var1) throws JMSException {
      try {
         this.textmsg.setText(var1);
         this.qsender.send(this.textmsg);
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   public void sendbytes(byte[] var1) throws JMSException {
      try {
         this.bytesmsg.clearBody();
         this.bytesmsg.writeBytes(var1);
         this.qsender.send(this.bytesmsg);
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }
}
