package weblogic.wsee.connection.transport.jms;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Enumeration;
import java.util.Iterator;
import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.TextMessage;
import javax.xml.soap.MimeHeader;
import javax.xml.soap.MimeHeaders;
import weblogic.wsee.connection.ConnectionException;
import weblogic.wsee.connection.transport.ServerTransport;
import weblogic.wsee.util.Verbose;

public class JmsServerQueueTransport implements ServerTransport {
   private static final boolean verbose = Verbose.isVerbose(JmsServerQueueTransport.class);
   private static final String mimeHdrPrefix = "_wls_mimehdr";
   private Message message;
   private QueueConnectionFactory factory;
   private QueueConnection connection;
   private QueueSession session;
   private JmsMessageData messageData;
   private QueueSender sender;
   private String wsUrl;
   private boolean isWLW81Message;
   private boolean isNullReplyto;
   private String userName;
   private String passwd;

   public JmsServerQueueTransport(String var1, Message var2, QueueConnectionFactory var3, String var4, String var5) {
      this.message = var2;
      this.factory = var3;
      this.wsUrl = var1;
      this.userName = var4;
      this.passwd = var5;

      try {
         this.isWLW81Message = Boolean.valueOf(var2.getBooleanProperty("IsWLW81Message"));
      } catch (JMSException var7) {
      }

   }

   public boolean isUserInRole(String var1) {
      throw new Error("NYI");
   }

   public Principal getUserPrincipal() {
      throw new Error("NYI");
   }

   public boolean isReliable() {
      return true;
   }

   public String getName() {
      return "JMSServerTransport";
   }

   public String getServiceURI() {
      return this.wsUrl;
   }

   public String getEndpointAddress() {
      return this.wsUrl;
   }

   public boolean isBytesMessage() {
      return this.message instanceof BytesMessage;
   }

   public OutputStream send(MimeHeaders var1) throws IOException {
      try {
         Queue var2 = (Queue)this.message.getJMSReplyTo();
         if (var2 == null) {
            if (this.isWLW81Message) {
               this.isNullReplyto = true;
               return new JmsMessageData();
            } else {
               throw new IOException("Failed to find queue");
            }
         } else {
            if (this.userName != null) {
               this.connection = this.factory.createQueueConnection(this.userName, this.passwd);
            } else {
               this.connection = this.factory.createQueueConnection();
            }

            this.session = this.connection.createQueueSession(false, 1);
            String var3 = this.message.getJMSCorrelationID();
            if (var3 == null) {
               var3 = this.message.getJMSMessageID();
            }

            if (this.message instanceof TextMessage) {
               this.message = this.session.createTextMessage();
            }

            if (this.message instanceof BytesMessage) {
               this.message = this.session.createBytesMessage();
            }

            this.message.setJMSCorrelationID(var3);

            MimeHeader var5;
            String var6;
            for(Iterator var4 = var1.getAllHeaders(); var4.hasNext(); this.message.setStringProperty("_wls_mimehdr" + var6, var5.getValue())) {
               var5 = (MimeHeader)var4.next();
               var6 = var5.getName().replaceAll("-", "_");
               if (verbose) {
                  Verbose.say("write mime header " + var6 + ": " + var5.getValue());
               }
            }

            this.sender = this.session.createSender(var2);
            this.messageData = new JmsMessageData();
            return this.messageData;
         }
      } catch (JMSException var7) {
         throw new IOException("Failed to send message:" + var7);
      }
   }

   public void completeSend() throws JMSException {
      if (this.isWLW81Message && this.isNullReplyto) {
         if (verbose) {
            Verbose.log((Object)"Request is sent by a WLW 8.1 client, we don't send response back");
         }

      } else {
         try {
            byte[] var1 = this.messageData.toByteArray();
            if (verbose) {
               System.out.println("\n ***SENDING FOLLOWING MESSAGE AT SERVER SIDE *** \n");
               System.out.print(new String(var1));
               System.out.println("\n **** END SENDING MESSAGE ***");
            }

            if (this.message instanceof TextMessage) {
               try {
                  ((TextMessage)this.message).setText(new String(var1, "UTF-8"));
               } catch (UnsupportedEncodingException var7) {
               }
            } else if (this.message instanceof BytesMessage) {
               ((BytesMessage)this.message).writeBytes(var1);
            }

            this.sender.send(this.message);
         } finally {
            if (this.sender != null) {
               this.sender.close();
            }

            if (this.session != null) {
               this.session.close();
            }

            if (this.connection != null) {
               this.connection.close();
            }

         }

         if (verbose) {
            Verbose.log((Object)("Message send ok for service " + this.wsUrl));
         }

      }
   }

   public OutputStream sendGeneralFault(MimeHeaders var1) throws IOException {
      return this.send(var1);
   }

   public OutputStream sendAuthorizationFault(MimeHeaders var1) throws IOException {
      return this.send(var1);
   }

   public OutputStream sendAuthorizationRequiredFault(MimeHeaders var1) throws IOException {
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
      try {
         this.readMimeHeaders(this.message, var1);
         if (this.message instanceof TextMessage) {
            String var7 = ((TextMessage)this.message).getText();
            if (verbose) {
               System.out.println("\n ***RECEIVED FOLLOWING MESSAGE AT SERVER SIDE *****\n");
               System.out.print(var7);
               System.out.println("\n ***END RECEIVING MESSAGE ***\n");
            }

            ByteArrayInputStream var8 = null;

            try {
               var8 = new ByteArrayInputStream(var7.getBytes("UTF-8"));
            } catch (UnsupportedEncodingException var5) {
            }

            return var8;
         } else if (this.message instanceof BytesMessage) {
            byte[] var2 = new byte[(int)((BytesMessage)this.message).getBodyLength()];
            ((BytesMessage)this.message).readBytes(var2);
            if (verbose) {
               System.out.println("\n ***RECEIVED FOLLOWING MESSAGE AT SERVER SIDE *****\n");
               System.out.print(new String(var2));
               System.out.println("\n ***END RECEIVING MESSAGE ***\n");
            }

            return new ByteArrayInputStream(var2);
         } else {
            throw new IOException("Unsupported JMS message type '" + this.message.getClass() + "'");
         }
      } catch (JMSException var6) {
         IOException var3 = new IOException("Failed to get text");
         var3.initCause(var6);
         throw var3;
      }
   }

   public void confirmOneway() throws IOException {
   }

   class JmsMessageData extends ByteArrayOutputStream {
      public void close() throws IOException {
         if (JmsServerQueueTransport.verbose) {
            Verbose.log((Object)"here");
         }

         super.close();

         try {
            JmsServerQueueTransport.this.completeSend();
         } catch (JMSException var2) {
            throw new ConnectionException("Failed to send jms message", var2);
         }
      }
   }
}
