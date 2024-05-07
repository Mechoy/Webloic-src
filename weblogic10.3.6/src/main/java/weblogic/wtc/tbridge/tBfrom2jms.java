package weblogic.wtc.tbridge;

import java.util.Hashtable;
import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageEOFException;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSession;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public final class tBfrom2jms implements MessageListener {
   public static final String JNDI_FACTORY = "weblogic.jndi.WLInitialContextFactory";
   public static final String JMS_FACTORY = "weblogic.jms.ConnectionFactory";
   public static final String QUEUE = "weblogic.jms.Tux2JmsQueue";
   private QueueConnectionFactory qconFactory;
   private QueueConnection qcon;
   private QueueSession qsession;
   private QueueReceiver qreceiver;
   private Queue queue;
   private boolean quit = false;
   private static String myqueue = null;

   public void onMessage(Message var1) {
      try {
         if (var1 instanceof TextMessage) {
            String var2 = ((TextMessage)var1).getText();
            System.out.println("TextMessage:" + var2);
            if (var2.equalsIgnoreCase("quit") || var2.equalsIgnoreCase("<FML32><STRING>tiuq</STRING></FML32>")) {
               synchronized(this) {
                  this.quit = true;
                  this.notifyAll();
               }
            }
         } else if (var1 instanceof BytesMessage) {
            byte[] var3 = new byte[1000];
            int var8 = 0;

            while(true) {
               try {
                  var3[var8] = ((BytesMessage)var1).readByte();
               } catch (MessageEOFException var6) {
                  System.out.println("BytesMessage: " + var8 + " bytes.");

                  for(var8 = 0; var8 < 10; ++var8) {
                     System.out.println("  CArray[" + var8 + "] = " + var3[var8]);
                     if (var3[var8] == 0) {
                        return;
                     }
                  }

                  return;
               }

               ++var8;
            }
         } else {
            System.out.println("Unsupported message type.");
         }
      } catch (JMSException var7) {
         var7.printStackTrace();
      }

   }

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

      String var3 = "";
      this.qreceiver = this.qsession.createReceiver(this.queue, var3);
      this.qreceiver.setMessageListener(this);
      this.qcon.start();
   }

   public void close() throws JMSException {
      this.qreceiver.close();
      this.qsession.close();
      this.qcon.close();
   }

   public static void main(String[] var0) throws Exception {
      if (var0.length >= 1 && var0.length <= 2) {
         tBfrom2jms var1 = new tBfrom2jms();
         InitialContext var2;
         if (var0.length == 1) {
            var2 = getInitialContext(var0[0]);
            var1.init(var2, "weblogic.jms.Tux2JmsQueue");
            myqueue = "weblogic.jms.Tux2JmsQueue";
         } else {
            var2 = getInitialContext(var0[1]);
            var1.init(var2, var0[0]);
            myqueue = var0[0];
         }

         System.out.println("tBfrom2jms ready to recieve messages from: " + myqueue);
         synchronized(var1) {
            while(!var1.quit) {
               try {
                  var1.wait();
               } catch (InterruptedException var5) {
               }
            }
         }

         System.out.println("exiting...quit received.");
         Thread.sleep(2000L);
         var1.close();
      } else {
         System.out.println("Usage: java tBfrom2jms [queue] WebLogicURL");
      }
   }

   private static InitialContext getInitialContext(String var0) throws NamingException {
      Hashtable var1 = new Hashtable();
      var1.put("java.naming.factory.initial", "weblogic.jndi.WLInitialContextFactory");
      var1.put("java.naming.provider.url", var0);
      return new InitialContext(var1);
   }
}
