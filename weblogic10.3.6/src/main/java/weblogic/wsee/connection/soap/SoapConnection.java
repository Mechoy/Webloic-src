package weblogic.wsee.connection.soap;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import javax.mail.internet.ContentType;
import javax.mail.internet.ParseException;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.rpc.handler.soap.SOAPMessageContext;
import javax.xml.soap.MimeHeader;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import org.w3c.dom.Node;
import weblogic.application.ApplicationAccess;
import weblogic.application.ApplicationContextInternal;
import weblogic.kernel.KernelStatus;
import weblogic.utils.StringUtils;
import weblogic.utils.classloaders.GenericClassLoader;
import weblogic.wsee.addressing.ActionHeader;
import weblogic.wsee.addressing.EndpointReference;
import weblogic.wsee.addressing.FromHeader;
import weblogic.wsee.addressing.ReplyToHeader;
import weblogic.wsee.addressing.ToHeader;
import weblogic.wsee.connection.Connection;
import weblogic.wsee.connection.transport.Transport;
import weblogic.wsee.handler.HandlerIterator;
import weblogic.wsee.message.WlMessageContext;
import weblogic.wsee.message.soap.SoapMessageContext;
import weblogic.wsee.util.IOUtil;
import weblogic.wsee.util.ToStringWriter;
import weblogic.wsee.util.Verbose;
import weblogic.xml.saaj.util.HeaderUtils;

public abstract class SoapConnection implements Connection {
   private Transport transport;
   private static final boolean verbose = Verbose.isVerbose(SoapConnection.class);
   protected static final boolean soapMessageLogVerbose = Verbose.isVerbose(SoapConnection.class.getName() + "MessageLog");
   protected static final boolean soapMessageFileVerbose = Verbose.isVerbose(SoapConnection.class.getName() + "Message");
   protected static final boolean soapMessageVerbose;
   protected boolean isMTOMmessage = false;
   private boolean isStreamAttachments = false;
   protected InputStream input = null;

   public void send(MessageContext var1) throws IOException {
      SOAPMessage var2 = ((SOAPMessageContext)var1).getMessage();
      WlMessageContext var3 = (WlMessageContext)var1;
      MimeHeaders var4 = var2.getMimeHeaders();
      this.addCustomHeaders(var3, var4);
      dumpSoapMsg((SoapMessageContext)var3, true);
      OutputStream var5 = this.transport.send(var4);
      IOException var6 = null;

      try {
         var2.writeTo(var5);
      } catch (SOAPException var13) {
         if (verbose) {
            Verbose.logException(var13);
         }

         IOException var8 = new IOException("Could not write SOAPMessage" + var13.toString());
         var8.initCause(var13);
         var6 = var8;
         throw var8;
      } catch (IOException var14) {
         var6 = var14;
         throw var14;
      } finally {
         this.safeClose(var5, var6);
         this.cleanUpCachedInputStream();
      }

      if (verbose) {
         Verbose.log((Object)"Message send ok");
      }

   }

   protected void safeClose(Closeable var1, IOException var2) throws IOException {
      try {
         var1.close();
      } catch (IOException var4) {
         if (var2 == null) {
            throw var4;
         }

         if (verbose) {
            Verbose.logException(var4);
         }
      }

   }

   public static void dumpSoapMsg(SoapMessageContext var0, boolean var1) {
      dumpSoapMsg(var0, var1, soapMessageVerbose, soapMessageFileVerbose, soapMessageLogVerbose, Verbose.getOut());
   }

   public static String dumpSoapMsgToString(SoapMessageContext var0, boolean var1) {
      ByteArrayOutputStream var2 = new ByteArrayOutputStream();
      dumpSoapMsg(var0, var1, true, false, true, var2);
      return var2.toString();
   }

   public static void dumpSoapMsg(SoapMessageContext var0, boolean var1, boolean var2, boolean var3, boolean var4, OutputStream var5) {
      if (var2) {
         boolean var6 = false;
         String var7;
         String var8;
         String var9;
         if (var1) {
            var7 = ">>>";
            var8 = "sent-";
            var9 = "SENDING";
         } else {
            var7 = "<<<";
            var8 = "recvd-";
            var9 = "RECEIVING";
            Boolean var10 = (Boolean)var0.getProperty("weblogic.wsee.stream_attachments");
            if (var10 != null && var10) {
               var6 = true;
            }
         }

         Verbose.say(var7 + "#########################");

         try {
            String var19 = getAction(var0);
            String var11 = getCurrentParty(var0, var1);
            String var12 = var1 ? " To: " + getTo(var0) : "";
            if (var19 != null) {
               String var13 = sanitizeUriForFilename(var19);
               var13 = var8 + var13;
               if (var11 != null) {
                  var11 = sanitizeUriForFilename(var11);
                  var13 = var11 + "-" + var13;
               }

               Verbose.say(var7 + "### " + var9 + " Action: " + var19 + " Current party: " + var11 + var12);
               StringBuffer var14 = HandlerIterator.getHandlerHistory(var0);
               if (var14.length() > 0) {
                  Verbose.say(var7 + "### Handler history: " + var14);
               }

               if (!var6) {
                  if (var3) {
                     writeSoapMsgToFile(var0.getMessage(), new File("." + File.separatorChar + "soapMessages"), var13);
                  }

                  if (var4) {
                     try {
                        var0.getMessage().writeTo(Verbose.getOut());
                        Verbose.getOut().println();
                     } catch (IOException var16) {
                        Verbose.logException(var16);
                     } catch (SOAPException var17) {
                        Verbose.logException(var17);
                     }
                  }
               } else {
                  Verbose.say("Ignore the received attachment(s) message dump.");
               }
            }
         } catch (Exception var18) {
            Verbose.logException(var18);
         }

         Verbose.say(var7 + "#########################");
      }
   }

   protected static void printSoapMsg(SOAPMessageContext var0, boolean var1) {
      SOAPMessage var2 = var0.getMessage();
      Verbose.getOut().println("\n\n--------- " + (var1 ? "SENDING" : "RECEIVING") + " ----------------------\n");
      if (var0 instanceof SoapMessageContext) {
         SoapMessageContext var3 = (SoapMessageContext)var0;
         String var4 = "Unknown";
         String var5 = "Unknown";
         ActionHeader var6 = (ActionHeader)var3.getHeaders().getHeader(ActionHeader.TYPE);
         if (var6 != null) {
            var4 = var6.getActionURI();
         }

         Verbose.say("SOAP Action: " + var4);
         if (var1) {
            ToHeader var7 = (ToHeader)var3.getHeaders().getHeader(ToHeader.TYPE);
            if (var7 != null) {
               var5 = var7.getAddress();
            }

            Verbose.say("To:          " + var5);
         } else {
            FromHeader var10 = (FromHeader)var3.getHeaders().getHeader(FromHeader.TYPE);
            if (var10 != null) {
               var5 = var10.getReference().getAddress();
            }

            Verbose.say("From:        " + var5);
         }
      }

      try {
         var2.writeTo(Verbose.getOut());
      } catch (IOException var8) {
         Verbose.logException(var8);
      } catch (SOAPException var9) {
         Verbose.logException(var9);
      }

      Verbose.getOut().println("\n\n-------------------------------\n\n\n");
   }

   public static String getCurrentParty(SoapMessageContext var0, boolean var1) {
      String var2 = null;
      EndpointReference var3 = (EndpointReference)var0.getProperty("weblogic.wsee.addressing.ServerEndpoint");
      if (var3 != null) {
         var2 = var3.getAddress();
      }

      if (var2 != null) {
         return var2;
      } else {
         var2 = (String)var0.getProperty("weblogic.wsee.service_name");
         if (var2 != null) {
            return var2;
         } else if (!KernelStatus.isServer()) {
            return "client";
         } else {
            ApplicationContextInternal var4 = ApplicationAccess.getApplicationAccess().getCurrentApplicationContext();
            if (var4 != null) {
               GenericClassLoader var5 = var4.getAppClassLoader();
               if (var5.getAnnotation() != null) {
                  var2 = var5.getAnnotation().toString();
               } else {
                  var2 = var4.getApplicationId();
               }
            } else if (var1) {
               var2 = getFrom(var0);
               if (var2 == null) {
                  var2 = getReplyTo(var0);
               }
            } else {
               var2 = getTo(var0);
            }

            if (var2 == null) {
               var2 = "Unknown";
            }

            return var2;
         }
      }
   }

   private static String getTo(SoapMessageContext var0) {
      ToHeader var1 = (ToHeader)var0.getHeaders().getHeader(ToHeader.TYPE);
      if (var1 != null) {
         return var1.getAddress();
      } else {
         String var2 = (String)var0.getProperty("weblogic.wsee.addressing.To");
         return var2 != null ? var2 : null;
      }
   }

   private static String getFrom(SoapMessageContext var0) {
      FromHeader var1 = (FromHeader)var0.getHeaders().getHeader(FromHeader.TYPE);
      if (var1 != null) {
         return var1.getReference().getAddress();
      } else {
         EndpointReference var2 = (EndpointReference)var0.getProperty("weblogic.wsee.addressing.From");
         return var2 != null ? var2.getAddress() : null;
      }
   }

   private static String getReplyTo(SoapMessageContext var0) {
      ReplyToHeader var1 = (ReplyToHeader)var0.getHeaders().getHeader(ReplyToHeader.TYPE);
      if (var1 != null) {
         return var1.getReference().getAddress();
      } else {
         EndpointReference var2 = (EndpointReference)var0.getProperty("weblogic.wsee.addressing.From");
         return var2 != null ? var2.getAddress() : null;
      }
   }

   public static String getAction(SoapMessageContext var0) {
      String var1 = null;
      ActionHeader var2 = (ActionHeader)var0.getHeaders().getHeader(ActionHeader.TYPE);
      if (var2 != null) {
         String var3 = var2.getActionURI();
         if (var3 != null) {
            var1 = sanitizeUriForFilename(var3);
         }
      }

      if (var1 == null) {
         SOAPMessage var9 = var0.getMessage();

         try {
            SOAPBody var4 = var9.getSOAPBody();
            if (var4 != null) {
               String var5 = null;
               Iterator var6 = var4.getChildElements();
               if (var6.hasNext()) {
                  Node var7 = (Node)var6.next();
                  var5 = var7.getNodeName();
               }

               if (var5 == null) {
                  var5 = "UnknownBodyElem";
               }

               var1 = var5;
            }
         } catch (SOAPException var8) {
            var8.printStackTrace();
         }
      }

      return var1;
   }

   private static String sanitizeUriForFilename(String var0) {
      if (var0 == null) {
         return null;
      } else {
         if (var0.startsWith("http://")) {
            var0 = var0.substring("http://".length());
         }

         int var1 = var0.length();
         StringBuffer var2 = new StringBuffer();

         for(int var3 = 0; var3 < var1; ++var3) {
            char var4 = var0.charAt(var3);
            if (!Character.isJavaIdentifierStart(var4) && !Character.isJavaIdentifierPart(var4)) {
               var2.append("_");
            } else {
               var2.append(var4);
            }
         }

         return var2.toString();
      }
   }

   public static void writeSoapMsgToFile(SOAPMessage var0, File var1, String var2) {
      var1 = var1.getAbsoluteFile();
      var1.mkdirs();
      FileOutputStream var3 = null;
      int var4 = 2;

      File var5;
      for(var5 = computeMessageFilename(var1, var2, -1); var5.exists(); var5 = computeMessageFilename(var1, var2, var4++)) {
      }

      try {
         var3 = new FileOutputStream(var5);
         var0.writeTo(var3);
      } catch (Exception var15) {
         Verbose.logException(var15);
      } finally {
         if (var3 != null) {
            try {
               var3.close();
            } catch (Exception var14) {
               var14.printStackTrace();
            }
         }

      }

   }

   private static File computeMessageFilename(File var0, String var1, int var2) {
      File var3;
      if (var2 <= 0) {
         var3 = new File(var0, var1 + ".soap");
      } else {
         var3 = new File(var0, var1 + "_" + var2 + ".soap");
      }

      return var3;
   }

   public void receive(MessageContext var1) throws IOException {
      MimeHeaders var2 = new MimeHeaders();
      InputStream var3 = this.transport.receive(var2);
      String var4 = this.getContentType(var2);
      boolean var5 = this.isMtom(var4);
      if (var5) {
         var1.setProperty("weblogic.wsee.mtom_message_recvd", "true");
      } else {
         var1.removeProperty("weblogic.wsee.mtom_message_recvd");
      }

      if (var1.getProperty("weblogic.wsee.transport.headers") != null) {
         var1.setProperty("weblogic.wsee.transport.headers", var2);
      }

      this.setSessionCookies(var1, var2);
      IOException var6 = null;
      SoapMessageContext var7 = (SoapMessageContext)var1;

      try {
         if (var7.getProperty("weblogic.wsee.stream_attachments") != null) {
            this.isStreamAttachments = (Boolean)var7.getProperty("weblogic.wsee.stream_attachments");
         }

         SOAPMessage var8 = this.createSOAPMessage(var7, var2, var3, var5);
         var7.setMessage(var8);
      } catch (SOAPException var13) {
         IOException var9 = new IOException("Could not parse SOAP message. Remaining stream ... " + IOUtil.toString(var3));
         var9.initCause(var13);
         var6 = var9;
         throw var9;
      } finally {
         this.cleanUpInputStream(var3, var2, var6);
      }

      dumpSoapMsg(var7, false);
   }

   private void setSessionCookies(MessageContext var1, MimeHeaders var2) {
      Object var3 = var1.getProperty("javax.xml.rpc.session.maintain");
      if (var3 != null) {
         if (!(var3 instanceof Boolean)) {
            throw new IllegalArgumentException("Value of javax.xml.rpc.session.maintain must be java.lang.Boolean");
         }

         if ((Boolean)var3) {
            String[] var4 = var2.getHeader("Set-Cookie");
            if (var4 != null) {
               HashMap var5 = new HashMap();
               String[] var6 = var4;
               int var7 = var4.length;

               String var9;
               for(int var8 = 0; var8 < var7; ++var8) {
                  var9 = var6[var8];
                  this.addCookies(this.cleanupCookie(var9), var5);
               }

               boolean var11 = !var5.isEmpty();
               MimeHeaders var12 = this.mergeMimeHeaderCookies(var5, var1);
               if (!var5.isEmpty() || var11) {
                  Iterator var13 = var5.keySet().iterator();

                  while(var13.hasNext()) {
                     var9 = (String)var13.next();
                     String var10 = (String)var5.get(var9);
                     var12.addHeader("Cookie", var9 + "=" + var10);
                  }
               }
            }
         }
      }

   }

   private boolean isMtom(String var1) throws IOException {
      if (var1.indexOf("application/xop+xml") < 0) {
         return false;
      } else {
         ContentType var2;
         try {
            var2 = new ContentType(var1);
         } catch (ParseException var4) {
            throw new IOException(" could not parse MIME contentType '" + var1 + "'. " + var4.getMessage());
         }

         String var3 = var2.getParameter("type");
         return var3 != null && var3.toLowerCase(Locale.ENGLISH).equals("application/xop+xml");
      }
   }

   private String getContentType(MimeHeaders var1) throws IOException {
      String var2 = HeaderUtils.getContentType(var1);
      if (var2 == null) {
         throw new IOException("Null content type");
      } else {
         if (verbose) {
            Verbose.log((Object)("Content type = " + var2));
         }

         String var3 = var2.toLowerCase(Locale.ENGLISH);
         if (!var3.startsWith("text/xml") && !var3.startsWith("application/soap+xml") && !var2.toLowerCase(Locale.ENGLISH).startsWith("multipart/related")) {
            throw new IOException("Wrong content type '" + var2 + "'. It " + "must be '" + "application/soap+xml" + "', '" + var3.startsWith("text/xml") + "', multipart/related' or 'application/xop+xml'");
         } else {
            return var2;
         }
      }
   }

   protected void addCustomHeaders(WlMessageContext var1, MimeHeaders var2) {
      MimeHeaders var3 = (MimeHeaders)var1.getProperty("weblogic.wsee.transport.headers");
      if (var3 != null) {
         Iterator var4 = var3.getAllHeaders();

         while(var4.hasNext()) {
            MimeHeader var5 = (MimeHeader)var4.next();
            var2.addHeader(var5.getName(), var5.getValue());
         }
      }

   }

   public void setTransport(Transport var1) {
      this.transport = var1;
   }

   public Transport getTransport() {
      return this.transport;
   }

   public String toString() {
      ToStringWriter var1 = new ToStringWriter();
      this.toString(var1);
      return var1.toString();
   }

   public void toString(ToStringWriter var1) {
      var1.start(this);
      var1.writeField("transport", this.transport);
      var1.end();
   }

   private void addCookies(String var1, Map<String, String> var2) {
      String[] var3 = StringUtils.splitCompletely(var1, ";");
      String[] var4 = var3;
      int var5 = var3.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         String var7 = var4[var6];
         int var8 = var7.indexOf("=");
         if (var8 != -1 && var8 < var7.length() - 1) {
            String var9 = var7.substring(0, var8).trim();
            String var10 = var7.substring(var8 + 1, var7.length()).trim();
            var2.put(var9, var10);
         }
      }

   }

   private String cleanupCookie(String var1) {
      var1 = var1.trim();
      int var2 = var1.indexOf(59);
      if (var2 != -1) {
         var1 = var1.substring(0, var2);
      }

      return var1;
   }

   private MimeHeaders mergeMimeHeaderCookies(Map<String, String> var1, MessageContext var2) {
      Object var3 = (Map)var2.getProperty("weblogic.wsee.invoke_properties");
      if (var3 == null) {
         var3 = new HashMap();
         var2.setProperty("weblogic.wsee.invoke_properties", var3);
      }

      MimeHeaders var4 = (MimeHeaders)((Map)var3).get("weblogic.wsee.transport.headers");
      if (var4 == null) {
         var4 = new MimeHeaders();
         ((Map)var3).put("weblogic.wsee.transport.headers", var4);
      } else {
         String[] var5 = var4.getHeader("Cookie");
         if (var5 != null) {
            String[] var6 = var5;
            int var7 = var5.length;

            for(int var8 = 0; var8 < var7; ++var8) {
               String var9 = var6[var8];
               this.addCookies(var9, var1);
            }

            var4.removeHeader("Cookie");
         }
      }

      return var4;
   }

   abstract SOAPMessage createSOAPMessage(SoapMessageContext var1, MimeHeaders var2, InputStream var3, boolean var4) throws IOException, SOAPException;

   private void cleanUpInputStream(InputStream var1, MimeHeaders var2, IOException var3) throws IOException {
      if (!this.isStreamAttachments) {
         this.safeClose(var1, var3);
      } else {
         String[] var4 = var2.getHeader("Transfer-Encoding");
         if (var4 != null && var4.length == 1) {
            if (!"Chunked".equalsIgnoreCase(var4[0])) {
               var1.close();
               this.input = null;
            } else {
               this.input = var1;
            }
         }
      }

   }

   protected void cleanUpCachedInputStream() throws IOException {
      if (this.input != null) {
         try {
            this.input.read();
            this.input.close();
         } finally {
            this.input = null;
         }
      }

   }

   static {
      soapMessageVerbose = soapMessageFileVerbose || soapMessageLogVerbose;
   }
}
