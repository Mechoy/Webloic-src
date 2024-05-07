package weblogic.wsee.mc.utils;

import com.sun.xml.ws.api.SOAPVersion;
import com.sun.xml.ws.api.addressing.AddressingVersion;
import com.sun.xml.ws.api.message.Message;
import com.sun.xml.ws.api.message.Messages;
import com.sun.xml.ws.message.StringHeader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import weblogic.protocol.LocalServerIdentity;
import weblogic.wsee.WseeMCLogger;
import weblogic.wsee.mc.faults.McFaultMsg;
import weblogic.wsee.util.Guid;

public class McProtocolUtils {
   private static final Logger LOGGER = Logger.getLogger(McProtocolUtils.class.getName());
   private static final String UTF8 = "UTF-8";

   public static McConstants.SOAPVersion getSOAPVersionFromName(Name var0) {
      McConstants.SOAPVersion[] var1 = McConstants.SOAPVersion.values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         McConstants.SOAPVersion var4 = var1[var3];
         if (var4.getNamespaceUri().equals(var0.getURI())) {
            return var4;
         }
      }

      return McConstants.SOAPVersion.SOAP_11;
   }

   public static McConstants.FaultCode getSOAPFaultCodeFromName(Name var0) {
      McConstants.SOAPVersion var1 = getSOAPVersionFromName(var0);
      McConstants.FaultCode[] var2 = McConstants.FaultCode.values();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         McConstants.FaultCode var5 = var2[var4];
         if (var5.getCodeLocalName(var1).equals(var0.getLocalName())) {
            return var5;
         }
      }

      return null;
   }

   public static McConstants.SOAPVersion getSOAPVersionFromNamespaceUri(String var0) {
      McConstants.SOAPVersion[] var1 = McConstants.SOAPVersion.values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         McConstants.SOAPVersion var4 = var1[var3];
         if (var4.getNamespaceUri().equals(var0)) {
            return var4;
         }
      }

      return McConstants.SOAPVersion.SOAP_11;
   }

   public static String generateUUID() {
      String var0 = Guid.generateGuid();
      String var1 = LocalServerIdentity.getIdentity().getServerName();
      if (var1 == null) {
         var1 = "client";
      }

      return "uuid:" + var1 + ":" + var0.substring(5, var0.length());
   }

   public static Message createMessageFromFaultMessage(McFaultMsg var0, AddressingVersion var1, SOAPVersion var2) throws SOAPException {
      String var3 = var2 == SOAPVersion.SOAP_12 ? "SOAP 1.2 Protocol" : "SOAP 1.1 Protocol";
      MessageFactory var4 = MessageFactory.newInstance(var3);
      SOAPMessage var5 = var4.createMessage();
      var0.write(var5);
      Message var6 = Messages.create(var5);
      String var7 = McConstants.Action.FAULT.getActionURI(var0.getMcVersion());
      StringHeader var8 = new StringHeader(var1.actionTag, var7);
      var6.getHeaders().add(var8);
      return var6;
   }

   public static String encodeId(String var0) {
      try {
         return URLEncoder.encode(var0, "UTF-8");
      } catch (UnsupportedEncodingException var2) {
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(Level.FINE, "Encoding of id failed: " + var2.toString(), var2);
         }

         WseeMCLogger.logUnexpectedException(var2.toString(), var2);
         return var0;
      }
   }

   public static String decodeId(String var0) {
      try {
         return URLDecoder.decode(var0, "UTF-8");
      } catch (UnsupportedEncodingException var2) {
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(Level.FINE, "Decode of id failed: " + var2.toString(), var2);
         }

         WseeMCLogger.logUnexpectedException(var2.toString(), var2);
         return var0;
      }
   }
}
