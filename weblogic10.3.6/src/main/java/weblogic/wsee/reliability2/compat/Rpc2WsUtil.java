package weblogic.wsee.reliability2.compat;

import com.sun.istack.NotNull;
import com.sun.xml.ws.api.SOAPVersion;
import com.sun.xml.ws.api.addressing.AddressingVersion;
import com.sun.xml.ws.api.message.Header;
import com.sun.xml.ws.api.message.Headers;
import com.sun.xml.ws.api.message.Message;
import com.sun.xml.ws.api.message.Messages;
import com.sun.xml.ws.api.streaming.XMLStreamReaderFactory;
import com.sun.xml.ws.api.streaming.XMLStreamWriterFactory;
import com.sun.xml.ws.message.StringHeader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import weblogic.wsee.reliability.WsrmConstants;
import weblogic.wsee.reliability.faults.WsrmFaultMsg;
import weblogic.wsee.reliability.handshake.WsrmHandshakeMsg;
import weblogic.wsee.reliability.headers.WsrmHeader;
import weblogic.wsee.reliability2.headers.WsrmHeaderFactory;

public class Rpc2WsUtil {
   public static Message createMessageFromFaultMessage(WsrmFaultMsg var0, AddressingVersion var1, SOAPVersion var2) throws SOAPException {
      String var3 = var2 == SOAPVersion.SOAP_12 ? "SOAP 1.2 Protocol" : "SOAP 1.1 Protocol";
      MessageFactory var4 = MessageFactory.newInstance(var3);
      SOAPMessage var5 = var4.createMessage();
      var0.write(var5);
      Message var6 = Messages.create(var5);
      String var7 = var0.getRmVersion().getNamespaceUri() + "/fault";
      StringHeader var8 = new StringHeader(var1.actionTag, var7);
      var6.getHeaders().add(var8);
      return var6;
   }

   public static String getSOAPActionFromHandshakeMessage(WsrmHandshakeMsg var0) {
      String var1 = WsrmConstants.Action.valueOfElementName(var0.getElementName()).getActionURI(var0.getRmVersion());
      return var1;
   }

   public static Message createMessageFromHandshakeMessage(WsrmHandshakeMsg var0, AddressingVersion var1, SOAPVersion var2) throws SOAPException {
      String var3 = var2 == SOAPVersion.SOAP_12 ? "SOAP 1.2 Protocol" : "SOAP 1.1 Protocol";
      MessageFactory var4 = MessageFactory.newInstance(var3);
      SOAPMessage var5 = var4.createMessage();
      var0.writeMsg(var5);
      Message var6 = Messages.create(var5);
      String var7 = getSOAPActionFromHandshakeMessage(var0);
      SOAPActionHeader var8 = new SOAPActionHeader(var1.actionTag, var7, var2);
      var8.setMustUnderstand(true);
      var6.getHeaders().addOrReplace(var8);
      return var6;
   }

   public static void serializeHeaderList(@NotNull List<Header> var0, @NotNull ObjectOutputStream var1) throws IOException {
      try {
         ArrayList var2 = new ArrayList(var0.size());
         ArrayList var3 = new ArrayList(var0.size());
         Iterator var4 = var0.iterator();

         while(var4.hasNext()) {
            Header var5 = (Header)var4.next();
            ByteArrayOutputStream var6 = new ByteArrayOutputStream();
            var2.add(new QName(var5.getNamespaceURI(), var5.getLocalPart()));
            XMLStreamWriter var7 = XMLStreamWriterFactory.create(var6, "UTF-8");
            var5.writeTo(var7);
            var7.flush();
            var7.close();
            XMLStreamWriterFactory.recycle(var7);
            byte[] var8 = var6.toByteArray();
            var3.add(var8);
         }

         var1.writeObject(var2);
         var4 = var3.iterator();

         while(var4.hasNext()) {
            byte[] var10 = (byte[])var4.next();
            var1.writeInt(var10.length);
            var1.write(var10);
         }

      } catch (Exception var9) {
         throw new IOException(var9.toString(), var9);
      }
   }

   @NotNull
   public static List<Header> deserializeHeaderList(@NotNull ObjectInputStream var0, @NotNull SOAPVersion var1) throws IOException, ClassNotFoundException {
      List var2 = (List)var0.readObject();
      ArrayList var3 = new ArrayList(var2.size());
      Iterator var4 = var2.iterator();

      while(var4.hasNext()) {
         QName var5 = (QName)var4.next();
         int var6 = var0.readInt();
         byte[] var7 = new byte[var6];
         var0.readFully(var7);
         ByteArrayInputStream var8 = new ByteArrayInputStream(var7);
         XMLStreamReader var9 = XMLStreamReaderFactory.create((String)null, var8, "UTF-8", false);
         Object var10 = WsrmHeaderFactory.getInstance().createHeader(var5);

         try {
            if (var10 == null) {
               var10 = Headers.create(var1, var9);
            } else {
               ((WsrmHeader)var10).read(var9);
            }

            var9.close();
            XMLStreamReaderFactory.recycle(var9);
         } catch (XMLStreamException var12) {
            throw new IOException(var12.toString(), var12);
         }

         var8.close();
         var3.add(var10);
      }

      return var3;
   }
}
