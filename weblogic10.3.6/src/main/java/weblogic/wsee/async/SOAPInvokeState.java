package weblogic.wsee.async;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;
import javax.xml.rpc.JAXRPCException;
import javax.xml.rpc.handler.soap.SOAPMessageContext;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeader;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import weblogic.logging.NonCatalogLogger;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.SecurityServiceManager;
import weblogic.wsee.message.WlMessageContext;
import weblogic.wsee.server.EncryptionUtil;
import weblogic.wsee.util.WLMessageFactory;
import weblogic.xml.saaj.MessageFactoryImpl;
import weblogic.xml.saaj.SOAPMessageImpl;

public class SOAPInvokeState implements AsyncSOAPInvokeState {
   private static final long serialVersionUID = 7337837335502099161L;
   private static final int HASSUBJECT = 1;
   private static final int HASSERVICEURI = 2;
   private SOAPMessage msg;
   private AuthenticatedSubject subject;
   private Map msgCtxProperties;
   private boolean isSoap12;
   private boolean isMtom;
   private String charset;
   private String serviceURI;
   private static HashSet<String> excludedProperties = new HashSet(Arrays.asList("weblogic.wsee.wsrm.sequence.expiration", "weblogic.wsee.wsrm.offer.sequence.expiration", "weblogic.wsee.wsrm.SequenceExpiration", "weblogic.wsee.jws.container", "weblogic.wsee.ejb.target", "weblogic.wsee.handler.jaxrpcHandlerChain"));
   public static final String USER_DEFINED_EXCLUDED_PROPERTIES = "weblogic.wsee.exclude.properties";

   private static void init() {
      String var0 = System.getProperty("weblogic.wsee.exclude.properties");
      if (var0 != null) {
         StringTokenizer var1 = new StringTokenizer(var0, ",");

         while(var1.hasMoreTokens()) {
            String var2 = var1.nextToken().trim();
            excludedProperties.add(var2);
         }

      }
   }

   public SOAPInvokeState() {
      this.msg = null;
      this.subject = null;
      this.msgCtxProperties = new HashMap();
      this.isSoap12 = false;
      this.isMtom = false;
      this.charset = "utf-8";
   }

   public SOAPInvokeState(SOAPMessage var1) {
      this(var1, true);
   }

   public SOAPInvokeState(SOAPMessage var1, boolean var2) {
      this.msg = null;
      this.subject = null;
      this.msgCtxProperties = new HashMap();
      this.isSoap12 = false;
      this.isMtom = false;
      this.charset = "utf-8";
      this.msg = var1;
      this.isMtom = ((SOAPMessageImpl)var1).getIsMTOMmessage();
      if (((SOAPMessageImpl)var1).getProperty("javax.xml.soap.character-set-encoding") != null) {
         this.charset = (String)((SOAPMessageImpl)var1).getProperty("javax.xml.soap.character-set-encoding");
      } else {
         this.charset = "utf-8";
      }

      if (var2) {
         this.msg = this.getClonedSOAPMessage();
      }

   }

   public SOAPInvokeState(SOAPMessageContext var1) {
      this(var1, true);
   }

   public SOAPInvokeState(SOAPMessageContext var1, boolean var2) {
      this.msg = null;
      this.subject = null;
      this.msgCtxProperties = new HashMap();
      this.isSoap12 = false;
      this.isMtom = false;
      this.charset = "utf-8";
      if (AsyncUtil.isSoap12(var1)) {
         this.isSoap12 = true;
      }

      this.msg = var1.getMessage();
      this.isMtom = ((SOAPMessageImpl)this.msg).getIsMTOMmessage();
      if (((SOAPMessageImpl)this.msg).getProperty("javax.xml.soap.character-set-encoding") != null) {
         this.charset = (String)((SOAPMessageImpl)this.msg).getProperty("javax.xml.soap.character-set-encoding");
      } else {
         this.charset = "utf-8";
      }

      if (var2) {
         this.msg = this.getClonedSOAPMessage();
      }

      WlMessageContext var3 = WlMessageContext.narrow(var1);

      try {
         this.serviceURI = var3.getDispatcher().getConnection().getTransport().getServiceURI();
      } catch (NullPointerException var6) {
         this.serviceURI = null;
      }

      Iterator var4 = var1.getPropertyNames();

      while(var4.hasNext()) {
         String var5 = (String)var4.next();
         this.msgCtxProperties.put(var5, var1.getProperty(var5));
      }

   }

   public SOAPMessage getSOAPMessage() {
      return this.msg;
   }

   public SOAPMessage getClonedSOAPMessage() {
      try {
         ByteArrayOutputStream var2 = new ByteArrayOutputStream();
         ObjectOutputStream var3 = new ObjectOutputStream(var2);
         ((Externalizable)this.msg).writeExternal(var3);
         var3.flush();
         byte[] var4 = var2.toByteArray();
         ByteArrayInputStream var5 = new ByteArrayInputStream(var4);
         ObjectInputStream var6 = new ObjectInputStream(var5);
         MessageFactory var7 = WLMessageFactory.getInstance().getMessageFactory(this.isSoap12);
         SOAPMessage var1;
         if (this.isMtom) {
            var1 = ((MessageFactoryImpl)var7).createMessage(false, true);
         } else {
            var1 = var7.createMessage();
         }

         MimeHeaders var8 = var1.getMimeHeaders();
         var8.removeAllHeaders();
         Iterator var9 = this.msg.getMimeHeaders().getAllHeaders();

         while(var9.hasNext()) {
            MimeHeader var10 = (MimeHeader)var9.next();
            var8.addHeader(var10.getName(), var10.getValue());
         }

         ((Externalizable)var1).readExternal(var6);
         if (this.charset != null) {
            var1.setProperty("javax.xml.soap.character-set-encoding", this.charset);
         }

         return var1;
      } catch (IOException var11) {
         throw new JAXRPCException(var11);
      } catch (ClassNotFoundException var12) {
         throw new JAXRPCException(var12);
      } catch (SOAPException var13) {
         throw new JAXRPCException(var13);
      }
   }

   public boolean isSoap12() {
      return this.isSoap12;
   }

   public Map getMessageContextProperties() {
      return this.msgCtxProperties;
   }

   public void setSubject(AuthenticatedSubject var1) {
      this.subject = var1;
   }

   public AuthenticatedSubject getSubject(AuthenticatedSubject var1) {
      if (!SecurityServiceManager.isKernelIdentity(var1)) {
         throw new SecurityException("Unauthorized access to SOAPInvokeState.getSubject()");
      } else {
         return this.subject;
      }
   }

   public long getPayloadSize() {
      if (this.msg == null) {
         return 0L;
      } else {
         ByteArrayOutputStream var1 = new ByteArrayOutputStream();

         try {
            this.msg.writeTo(var1);
         } catch (SOAPException var3) {
            return 0L;
         } catch (IOException var4) {
            return 0L;
         }

         return (long)var1.toByteArray().length;
      }
   }

   public String getServiceURI() {
      return this.serviceURI;
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      String var2 = var1.readUTF();
      boolean var3 = false;
      boolean var4 = false;
      if ("9.2".equals(var2)) {
         var3 = true;
      } else if ("9.5".equals(var2)) {
         var4 = true;
      } else if (!"10.3".equals(var2)) {
         throw new IOException("Wrong version, expected: 10.3 actual: " + var2);
      }

      this.isSoap12 = var1.readBoolean();
      if (!var3 && !var4) {
         this.isMtom = var1.readBoolean();
         this.charset = var1.readUTF();
      }

      try {
         MessageFactory var5 = WLMessageFactory.getInstance().getMessageFactory(this.isSoap12);
         if (this.isMtom) {
            this.msg = ((MessageFactoryImpl)var5).createMessage(false, true);
         } else {
            this.msg = var5.createMessage();
         }
      } catch (SOAPException var14) {
         throw new IOException(var14.getMessage());
      }

      int var7;
      int var15;
      if (!var3) {
         var15 = var1.readInt();
         MimeHeaders var6 = this.msg.getMimeHeaders();
         var6.removeAllHeaders();

         for(var7 = 0; var7 < var15; ++var7) {
            var6.addHeader(var1.readUTF(), var1.readUTF());
         }
      }

      ((Externalizable)this.msg).readExternal(var1);
      var15 = var1.readInt();
      int var16;
      if ((var15 & 1) != 0) {
         try {
            var16 = var1.readInt();
            byte[] var17 = new byte[var16];
            var1.readFully(var17);
            var17 = EncryptionUtil.decrypt(var17);
            ByteArrayInputStream var8 = new ByteArrayInputStream(var17);
            ObjectInputStream var9 = new ObjectInputStream(var8);
            this.subject = (AuthenticatedSubject)var9.readObject();
         } catch (Exception var13) {
            (new NonCatalogLogger("WebServices")).warning("Couldn't completely read SOAPInvokeState object", var13);
         }
      } else {
         this.subject = null;
      }

      var16 = var1.readInt();

      for(var7 = 0; var7 < var16; ++var7) {
         String var18 = var1.readUTF();
         Object var19;
         if (var18.equals("javax.xml.rpc.security.auth.password")) {
            int var10 = var1.readInt();
            if (var10 > 0) {
               byte[] var11 = new byte[var10];
               var1.readFully(var11);
               byte[] var12 = EncryptionUtil.decrypt(var11);
               var19 = new String(var12);
            } else {
               var19 = null;
            }
         } else {
            var19 = (Serializable)var1.readObject();
         }

         this.msgCtxProperties.put(var18, var19);
      }

      if ((var15 & 2) != 0) {
         this.serviceURI = var1.readUTF();
      }

   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1.writeUTF("10.3");
      var1.writeBoolean(this.isSoap12);
      var1.writeBoolean(this.isMtom);
      var1.writeUTF(this.charset);
      int var3;
      if (this.msg != null) {
         Iterator var2 = this.msg.getMimeHeaders().getAllHeaders();

         for(var3 = 0; var2.hasNext(); ++var3) {
            var2.next();
         }

         var1.writeInt(var3);
         var2 = this.msg.getMimeHeaders().getAllHeaders();

         while(var2.hasNext()) {
            MimeHeader var4 = (MimeHeader)var2.next();
            var1.writeUTF(var4.getName());
            var1.writeUTF(var4.getValue());
         }

         ((Externalizable)this.msg).writeExternal(var1);
      }

      int var11 = 0;
      if (this.subject != null) {
         var11 |= 1;
      }

      if (this.serviceURI != null) {
         var11 |= 2;
      }

      var1.writeInt(var11);
      if (this.subject != null) {
         ByteArrayOutputStream var12 = new ByteArrayOutputStream();
         ObjectOutputStream var13 = new ObjectOutputStream(var12);
         var13.writeObject(this.subject);
         var13.flush();
         byte[] var5 = var12.toByteArray();
         var5 = EncryptionUtil.encrypt(var5);
         var1.writeInt(var5.length);
         var1.write(var5);
      }

      var3 = 0;
      Iterator var14 = this.msgCtxProperties.keySet().iterator();

      String var15;
      while(var14.hasNext()) {
         var15 = (String)var14.next();
         if (!excludedProperties.contains(var15)) {
            ++var3;
         }
      }

      var1.writeInt(var3);
      var14 = this.msgCtxProperties.keySet().iterator();

      while(var14.hasNext()) {
         var15 = (String)var14.next();
         if (!excludedProperties.contains(var15)) {
            Object var6 = this.msgCtxProperties.get(var15);
            if (var6 != null && !(var6 instanceof Serializable)) {
               throw new IOException("The property " + var15 + " is not serializable");
            }

            Serializable var7 = (Serializable)var6;
            if (var7 != null) {
               synchronized(var7) {
                  this.writeContextValue(var15, var7, var1);
               }
            } else {
               this.writeNullContextValue(var15, var1);
            }
         }
      }

      if (this.serviceURI != null) {
         var1.writeUTF(this.serviceURI);
      }

   }

   private void writeContextValue(String var1, Serializable var2, ObjectOutput var3) throws IOException {
      var3.writeUTF(var1);
      if (var1.equals("javax.xml.rpc.security.auth.password")) {
         byte[] var4 = ((String)var2).getBytes();
         byte[] var5 = EncryptionUtil.encrypt(var4);
         var3.writeInt(var5.length);
         var3.write(var5);
      } else {
         var3.writeObject(var2);
      }

   }

   private void writeNullContextValue(String var1, ObjectOutput var2) throws IOException {
      var2.writeUTF(var1);
      if (var1.equals("javax.xml.rpc.security.auth.password")) {
         var2.writeInt(0);
      } else {
         var2.writeObject((Object)null);
      }

   }

   private void writeObject(ObjectOutputStream var1) throws IOException {
      this.writeExternal(var1);
   }

   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      this.readExternal(var1);
   }

   static {
      init();
   }
}
