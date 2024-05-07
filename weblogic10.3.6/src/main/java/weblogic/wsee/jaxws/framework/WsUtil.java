package weblogic.wsee.jaxws.framework;

import com.sun.istack.NotNull;
import com.sun.istack.Nullable;
import com.sun.xml.ws.api.SOAPVersion;
import com.sun.xml.ws.api.addressing.AddressingVersion;
import com.sun.xml.ws.api.addressing.WSEndpointReference;
import com.sun.xml.ws.api.message.Message;
import com.sun.xml.ws.api.message.Messages;
import com.sun.xml.ws.api.streaming.XMLStreamReaderFactory;
import com.sun.xml.ws.api.streaming.XMLStreamWriterFactory;
import com.sun.xml.ws.client.ClientTransportAccessException;
import com.sun.xml.ws.message.StringHeader;
import com.sun.xml.ws.util.DOMUtil;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import javax.net.ssl.SSLHandshakeException;
import javax.xml.namespace.QName;
import javax.xml.soap.Detail;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPMessage;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.ws.soap.SOAPFaultException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import weblogic.protocol.LocalServerIdentity;
import weblogic.store.PersistentStoreManager;
import weblogic.wsee.addressing.EndpointReference;
import weblogic.wsee.util.Guid;

public class WsUtil {
   public static final String ROUTABLE_UUID_FORMAT_ID = "WLS1";

   public static boolean isPermanentSendFailure(Throwable var0) {
      List var1 = findNestedThrowableClasses(var0);
      if (!var1.contains(ClientTransportAccessException.class) && !var1.contains(SSLHandshakeException.class)) {
         if (var1.contains(SOAPFaultException.class)) {
            SOAPFaultException var2 = (SOAPFaultException)getNestedThrowable(var0, SOAPFaultException.class);

            try {
               String var3 = var2.getFault().getFaultCode();
               if (var3 != null) {
                  int var4 = var3.indexOf(":");
                  String var5 = var3.substring(var4 + 1);
                  if (var5.equals("Client.Access")) {
                     return true;
                  }
               }
            } catch (Exception var6) {
            }
         }

         return false;
      } else {
         return true;
      }
   }

   public static boolean hasRootCause(Throwable var0, Class<? extends Throwable> var1) {
      List var2 = findNestedThrowableClasses(var0);
      Iterator var3 = var2.iterator();

      Class var4;
      do {
         if (!var3.hasNext()) {
            return false;
         }

         var4 = (Class)var3.next();
      } while(!var1.isAssignableFrom(var4));

      return true;
   }

   public static List<Class<? extends Throwable>> findNestedThrowableClasses(Throwable var0) {
      ArrayList var1;
      for(var1 = new ArrayList(); var0 != null; var0 = var0.getCause()) {
         var1.add(var0.getClass());
      }

      return var1;
   }

   public static <T extends Throwable> T getNestedThrowable(Throwable var0, Class<T> var1) {
      while(var0 != null) {
         if (var1.isAssignableFrom(var0.getClass())) {
            return var0;
         }

         var0 = var0.getCause();
      }

      return null;
   }

   public static EndpointReference createWseeEPRFromWsEPR(WSEndpointReference var0) {
      EndpointReference var1 = new EndpointReference();
      DOMResult var2 = new DOMResult();
      var0.toSpec().writeTo(var2);
      var1.read(((Document)var2.getNode()).getDocumentElement());
      return var1;
   }

   public static WSEndpointReference createWsEPRFromWseeEPR(EndpointReference var0) {
      try {
         Element var1 = DOMUtil.createDom().createElementNS(var0.getNamespaceURI(), "EndpointReference");
         var0.write(var1);
         DOMSource var2 = new DOMSource(var1);
         javax.xml.ws.EndpointReference var3 = javax.xml.ws.EndpointReference.readFrom(var2);
         return new WSEndpointReference(var3);
      } catch (Exception var4) {
         throw new IllegalStateException(var4.toString(), var4);
      }
   }

   public static Message createMessageFromThrowable(Throwable var0, AddressingVersion var1, SOAPVersion var2) throws RuntimeException {
      try {
         String var3 = var2 == SOAPVersion.SOAP_12 ? "SOAP 1.2 Protocol" : "SOAP 1.1 Protocol";
         MessageFactory var4 = MessageFactory.newInstance(var3);
         SOAPMessage var5 = var4.createMessage();
         QName var6 = var2.faultCodeServer;
         String var7 = var0.toString();
         SOAPFault var8 = var5.getSOAPBody().addFault(var6, var7);
         Detail var9 = var8.addDetail();
         ByteArrayOutputStream var10 = new ByteArrayOutputStream();
         PrintWriter var11 = new PrintWriter(var10, true);
         var0.printStackTrace(var11);
         String var12 = var10.toString();
         var9.addTextNode(var12);
         Message var13 = Messages.create(var5);
         addSOAPFaultActionHeader(var1, var13);
         return var13;
      } catch (SOAPException var14) {
         throw new RuntimeException(var14.toString(), var14);
      }
   }

   public static void addSOAPFaultActionHeader(AddressingVersion var0, Message var1) {
      String var2 = getSOAPFaultAction(var0);
      StringHeader var3 = new StringHeader(var0.actionTag, var2);
      var1.getHeaders().add(var3);
   }

   public static String getSOAPFaultAction(AddressingVersion var0) {
      return var0.nsUri + "/fault";
   }

   public static Message createEmptyMessage(String var0, AddressingVersion var1, SOAPVersion var2) throws SOAPException {
      String var3 = var2 == SOAPVersion.SOAP_12 ? "SOAP 1.2 Protocol" : "SOAP 1.1 Protocol";
      MessageFactory var4 = MessageFactory.newInstance(var3);
      SOAPMessage var5 = var4.createMessage();
      Message var6 = Messages.create(var5);
      StringHeader var7 = new StringHeader(var1.actionTag, var0);
      var6.getHeaders().add(var7);
      return var6;
   }

   public static String generateUUID() {
      String var0 = Guid.generateGuid();
      String var1 = LocalServerIdentity.getIdentity().getServerName();
      if (var1 == null) {
         var1 = "client";
      }

      return "uuid:" + var1 + ":" + var0.substring(5, var0.length());
   }

   public static String generateRoutableUUID(String var0) {
      if (var0 == null) {
         throw new IllegalArgumentException("Null storeName parameter given when generating routable UUID");
      } else {
         if (var0.isEmpty()) {
            var0 = PersistentStoreManager.getManager().getDefaultStore().getName();
         }

         String var1 = Guid.generateGuid();
         return "uuid:WLS1:" + var0 + ":" + var1.substring(5, var1.length());
      }
   }

   public static String getStoreNameFromRoutableUUID(String var0) {
      ArrayList var1 = parseRoutableUUID(var0);
      return var1.size() >= 4 && "uuid".equals(var1.get(0)) && ((String)var1.get(1)).equals("WLS1") ? (String)var1.get(2) : null;
   }

   public static ArrayList<String> parseRoutableUUID(String var0) {
      StringTokenizer var1 = new StringTokenizer(var0, ":", true);
      ArrayList var2 = new ArrayList();
      boolean var3 = false;

      while(var1.hasMoreTokens()) {
         String var4 = var1.nextToken();
         if (var4.equals(":")) {
            if (var3) {
               var2.add("");
            }

            var3 = true;
         } else {
            var2.add(var4);
            var3 = false;
         }
      }

      return var2;
   }

   public static void serializeWSEndpointReference(@Nullable WSEndpointReference var0, @NotNull ObjectOutputStream var1) throws IOException {
      if (var0 == null) {
         var1.writeInt(0);
      } else {
         ByteArrayOutputStream var2 = new ByteArrayOutputStream();
         XMLStreamWriter var3 = XMLStreamWriterFactory.create(var2, "UTF-8");

         try {
            var0.writeTo("dummy", var3);
            var3.flush();
            var3.close();
            XMLStreamWriterFactory.recycle(var3);
         } catch (Exception var5) {
            throw new IOException(var5.toString(), var5);
         }

         byte[] var4 = var2.toByteArray();
         var1.writeInt(var4.length);
         if (var4.length > 0) {
            var1.write(var4);
         }

      }
   }

   @Nullable
   public static WSEndpointReference deserializeWSEndpointReference(@NotNull ObjectInputStream var0, @NotNull AddressingVersion var1) throws IOException {
      int var2 = var0.readInt();
      if (var2 > 0) {
         byte[] var3 = new byte[var2];
         var0.readFully(var3);
         ByteArrayInputStream var4 = new ByteArrayInputStream(var3);
         XMLStreamReader var5 = XMLStreamReaderFactory.create((String)null, var4, "UTF-8", false);

         try {
            return new WSEndpointReference(var5, var1);
         } catch (Exception var7) {
            throw new IOException(var7.toString(), var7);
         }
      } else {
         return null;
      }
   }

   public static String getOrSetMessageID(Message var0, AddressingVersion var1, SOAPVersion var2) {
      return getOrSetMessageID(var0, var1, var2, (String)null);
   }

   public static String getOrSetMessageID(Message var0, AddressingVersion var1, SOAPVersion var2, String var3) {
      String var4 = var0.getHeaders().getMessageID(var1, var2);
      if (var4 == null || var3 != null && !var3.equals(getStoreNameFromRoutableUUID(var4))) {
         if (var3 == null) {
            var4 = generateUUID();
         } else {
            var4 = generateRoutableUUID(var3);
         }

         StringHeader var5 = new StringHeader(var1.messageIDTag, var4);
         var0.getHeaders().addOrReplace(var5);
         var0.getHeaders().clearCachedMessageID();
         var0.clearCachedID();
      }

      return var4;
   }
}
