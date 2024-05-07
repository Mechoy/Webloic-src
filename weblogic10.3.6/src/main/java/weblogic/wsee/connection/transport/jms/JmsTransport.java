package weblogic.wsee.connection.transport.jms;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.Enumeration;
import java.util.Iterator;
import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.QueueReceiver;
import javax.jms.QueueSender;
import javax.jms.TextMessage;
import javax.naming.NamingException;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.xml.soap.MimeHeader;
import javax.xml.soap.MimeHeaders;
import weblogic.transaction.TxHelper;
import weblogic.wsee.connection.ConnectionException;
import weblogic.wsee.connection.ResponseListener;
import weblogic.wsee.connection.transport.ClientTransport;
import weblogic.wsee.connection.transport.TransportInfo;
import weblogic.wsee.util.Verbose;

public class JmsTransport implements ClientTransport {
   private static final boolean verbose = Verbose.isVerbose(JmsTransport.class);
   private static final String mimeHdrPrefix = "_wls_mimehdr";
   private JmsQueueConnection connection;
   private JmsTransportInfo info;
   private String uri;
   private Message message;
   private JmsMessageData messageData;
   private int readTimeout = -1;

   public void connect(String var1, TransportInfo var2) throws IOException {
      if (verbose) {
         Verbose.log((Object)("destination: " + var1));
      }

      if (verbose) {
         Verbose.log((Object)("transport info: " + var2));
      }

      this.uri = var1;
      if (var2 instanceof JmsTransportInfo) {
         this.info = (JmsTransportInfo)var2;
      } else {
         this.createTransportInfo();
      }

   }

   private void createTransportInfo() throws ConnectionException {
      try {
         this.info = new JmsTransportInfo(this.uri);
         Transaction var1 = TxHelper.getTransactionManager().getTransaction();
         if (var1 != null) {
            this.info.setTransactional(true);
         }

      } catch (URISyntaxException var2) {
         throw new ConnectionException("Failed to create JMS transport info", var2);
      } catch (SystemException var3) {
         throw new ConnectionException("Failed to create JMS transport info", var3);
      }
   }

   public void setResponseListener(ResponseListener var1) {
      throw new Error("NIY");
   }

   public boolean isBlocking() {
      return true;
   }

   public void setConnectionTimeout(int var1) {
      throw new Error("NIY");
   }

   public void setReadTimeout(int var1) {
      this.readTimeout = var1;
   }

   public String getName() {
      return "JMSTransport";
   }

   public String getServiceURI() {
      return this.uri;
   }

   public String getEndpointAddress() {
      return this.uri;
   }

   public OutputStream send(MimeHeaders var1) throws IOException {
      this.connection = null;

      try {
         String var2 = null;
         Iterator var3 = var1.getAllHeaders();

         MimeHeader var4;
         while(var3.hasNext()) {
            var4 = (MimeHeader)var3.next();
            if (var4.getName().equals("weblogic.wsee.transport.jms.url")) {
               if (verbose) {
                  Verbose.say("DEBUG---- Setting Provider URL in JmsTransportInfo to " + var4.getValue());
               }

               this.info.setJndiURL(var4.getValue());
            }

            if (var4.getName().equals("javax.xml.rpc.security.auth.username")) {
               if (verbose) {
                  Verbose.say("DEBUG---- Setting username/password in JmsTransportInfo to " + var4.getValue());
               }

               this.info.setUsername(var4.getValue());
            }

            if (var4.getName().equals("javax.xml.rpc.security.auth.password")) {
               this.info.setPassword(var4.getValue());
            }

            if (var4.getName().equals("weblogic.wsee.transport.jms.messagetype")) {
               if (verbose) {
                  Verbose.say("DEBUG---- MessageType = " + var4.getValue());
               }

               var2 = var4.getValue();
            }
         }

         this.connection = JmsQueueConnectionPool.getInstance().getConnection(this.info);
         if (var2 != null && !var2.equals("TextMessage")) {
            if (!var2.equals("BytesMessage")) {
               throw new ConnectionException("Unsupported JMS message type " + var2);
            }

            this.message = this.connection.getBytesMessage();
         } else {
            this.message = this.connection.getTextMessage();
         }

         if (verbose) {
            Verbose.log((Object)("URI : " + this.info.getServiceUri()));
         }

         if (verbose) {
            Verbose.log((Object)("Message : " + this.message));
         }

         var3 = var1.getAllHeaders();

         while(var3.hasNext()) {
            var4 = (MimeHeader)var3.next();
            String var5 = var4.getName().replaceAll("-", "_");
            if (!var5.equals("weblogic.wsee.transport.jms.url") && !var5.equals("javax.xml.rpc.security.auth.username") && !var5.equals("javax.xml.rpc.security.auth.password") && !var5.equals("weblogic.wsee.transport.jms.messagetype")) {
               if (verbose) {
                  Verbose.say("write mime header " + var5 + ": " + var4.getValue());
               }

               this.message.setStringProperty("_wls_mimehdr" + var5, var4.getValue());
            }
         }

         this.message.setStringProperty("URI", this.info.getServiceUri());
         this.message.setJMSReplyTo(this.connection.getResponseQueue());
         this.messageData = new JmsMessageData();
         return this.messageData;
      } catch (Throwable var6) {
         if (this.connection != null) {
            this.connection.release();
         }

         if (var6 instanceof NamingException) {
            throw new ConnectionException("Failed to lookup jms connection", var6);
         } else if (var6 instanceof JMSException) {
            throw new ConnectionException("JMS connection send faile", var6);
         } else {
            throw new IOException("Failed to send jms message", var6);
         }
      }
   }

   private void completeSend() throws JMSException {
      byte[] var1 = this.messageData.toByteArray();
      if (verbose) {
         System.out.println("\n *** SENDING FOLLOWING MESSAGE AT CALLING SIDE *** \n");
         System.out.print(new String(var1));
         System.out.println("\n *** END SENDING MESSAGE AT CALLING SIDE *** \n");
      }

      if (this.message instanceof TextMessage) {
         try {
            ((TextMessage)this.message).setText(new String(var1, "UTF-8"));
         } catch (UnsupportedEncodingException var3) {
         }
      } else {
         ((BytesMessage)this.message).writeBytes(var1);
      }

      QueueSender var2 = this.connection.getSender();
      var2.send(this.message);
      if (verbose) {
         Verbose.log((Object)("Message send ok for servcie " + this.uri));
      }

   }

   public OutputStream sendFault(MimeHeaders var1) throws IOException {
      return this.send(var1);
   }

   private void readMimeHeaders(Message var1, MimeHeaders var2) throws JMSException {
      Enumeration var4 = var1.getPropertyNames();

      while(var4.hasMoreElements()) {
         String var3 = (String)var4.nextElement();
         if (verbose) {
            Verbose.log((Object)("read mime header - jms key " + var3));
         }

         if (var3.startsWith("_wls_mimehdr")) {
            String var5 = var3.substring("_wls_mimehdr".length()).replaceAll("_", "-");
            String var6 = var1.getStringProperty(var3);
            if (var6 != null && var6.length() != 0) {
               if (verbose) {
                  Verbose.log((Object)("set mime header " + var5 + ":" + var6));
               }

               var2.addHeader(var5, var6);
            }
         }
      }

   }

   public InputStream receive(MimeHeaders var1) throws IOException {
      if (verbose) {
         Verbose.log((Object)"Receive response from temp queue");
      }

      QueueReceiver var2 = this.connection.getReceiver();
      if (var2 == null) {
         throw new IOException("No receiver found");
      } else {
         Message var3 = null;

         ByteArrayInputStream var6;
         try {
            if (this.readTimeout != -1) {
               var3 = var2.receive((long)this.readTimeout);
            } else {
               var3 = var2.receive();
            }

            if (var3 == null) {
               throw new IOException("Request timed out");
            }

            this.readMimeHeaders(var3, var1);
            ByteArrayInputStream var15;
            if (var3 instanceof TextMessage) {
               String var14 = ((TextMessage)var3).getText();
               if (verbose) {
                  System.out.println("\n *** RECEIVED FOLLOWING MESSAGE AT CALLING SIDE *** \n");
                  System.out.print(var14);
                  System.out.println("\n *** END MESSAGE **** \n");
               }

               var15 = null;

               try {
                  var15 = new ByteArrayInputStream(var14.getBytes("UTF-8"));
               } catch (UnsupportedEncodingException var11) {
               }

               var6 = var15;
               return var6;
            }

            if (!(var3 instanceof BytesMessage)) {
               throw new IOException("Got an unsuported JMS message type: " + var3);
            }

            byte[] var4 = new byte[(int)((BytesMessage)var3).getBodyLength()];
            ((BytesMessage)var3).readBytes(var4);
            if (verbose) {
               System.out.println("\n *** RECEIVED FOLLOWING MESSAGE AT CALLING SIDE *** \n");
               System.out.print(new String(var4));
               System.out.println("\n *** END MESSAGE **** \n");
            }

            var15 = new ByteArrayInputStream(var4);
            var6 = var15;
         } catch (JMSException var12) {
            IOException var5 = new IOException("Failed to receive message");
            var5.initCause(var12);
            throw var5;
         } finally {
            this.connection.release();
         }

         return var6;
      }
   }

   public void confirmOneway() throws IOException {
      if (verbose) {
         Verbose.log((Object)"confirmOneway is called");
      }

      this.connection.release();
   }

   class JmsMessageData extends ByteArrayOutputStream {
      public void close() throws IOException {
         if (JmsTransport.verbose) {
            Verbose.log((Object)"here");
         }

         super.close();

         try {
            JmsTransport.this.completeSend();
         } catch (JMSException var2) {
            throw new ConnectionException("Failed to send jms message", var2);
         }
      }
   }
}
