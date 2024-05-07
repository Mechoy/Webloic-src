package weblogic.wsee.reliability;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.xml.datatype.Duration;
import javax.xml.rpc.JAXRPCException;
import javax.xml.rpc.Stub;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import weblogic.wsee.jaxrpc.StubImpl;
import weblogic.wsee.util.Verbose;

public class WsrmUtils {
   public static boolean isSequenceInitialized(Stub var0) {
      Map var1 = getInvokePropertiesFromStub(var0);
      return WsrmProtocolUtils.isSequenceInitialized(var1);
   }

   public static String waitForSequenceInitialization(Stub var0, long var1, long var3) {
      Map var5 = getInvokePropertiesFromStub(var0);
      return WsrmProtocolUtils.waitForSequenceInitialization(var5, var1, var3);
   }

   public static void sendEmptyLastMessage(Stub var0) {
      Map var1 = getInvokePropertiesFromStub(var0);
      String var2 = (String)var0._getProperty("javax.xml.rpc.service.endpoint.address");
      WsrmProtocolUtils.sendEmptyLastMessage(var1, var2);
   }

   private static Map getInvokePropertiesFromStub(Stub var0) {
      if (var0 == null) {
         throw new JAXRPCException("Stub is null");
      } else {
         Map var1 = (Map)var0._getProperty("weblogic.wsee.invoke_properties");
         if (var1 == null) {
            throw new JAXRPCException("No invoke properties found on the stub.");
         } else {
            return var1;
         }
      }
   }

   public static void closeSequence(Stub var0) {
      Map var1 = getInvokePropertiesFromStub(var0);
      WsrmProtocolUtils.closeSequence(var1);
   }

   public static void terminateSequence(Stub var0) {
      Map var1 = getInvokePropertiesFromStub(var0);
      WsrmProtocolUtils.terminateSequence(var1);
   }

   public static void reset(Stub var0) {
      Map var1 = (Map)var0._getProperty("weblogic.wsee.invoke_properties");
      WsrmProtocolUtils.reset(var1);
   }

   public static String getSequenceId(Stub var0) {
      Map var1 = getInvokePropertiesFromStub(var0);
      return WsrmProtocolUtils.getSequenceId(var1);
   }

   public static void setExpires(Stub var0, Duration var1) {
      Map var2 = getPropertiesFromStub(var0);
      WsrmProtocolUtils.setExpires(var2, var1);
      setPropertiesOntoStub(var2, var0);
   }

   private static Map getPropertiesFromStub(Stub var0) {
      if (var0 == null) {
         throw new JAXRPCException("Stub is null");
      } else {
         HashMap var1 = new HashMap();
         Iterator var2 = var0._getPropertyNames();

         while(var2.hasNext()) {
            String var3 = (String)var2.next();
            Object var4 = var0._getProperty(var3);
            if (var4 != null) {
               var1.put(var3, var4);
            }
         }

         return var1;
      }
   }

   private static void setPropertiesOntoStub(Map var0, Stub var1) {
      if (var1 == null) {
         throw new JAXRPCException("Stub is null");
      } else {
         Iterator var2 = var1._getPropertyNames();

         while(var2.hasNext()) {
            String var3 = (String)var2.next();
            if (var0.containsKey(var3)) {
               Object var4 = var0.get(var3);
               var1._setProperty(var3, var4);
            } else if (var1 instanceof StubImpl) {
               ((StubImpl)var1)._removeProperty(var3);
            }
         }

      }
   }

   public static Duration getExpires(Stub var0) {
      return WsrmProtocolUtils.getExpires(getPropertiesFromStub(var0));
   }

   public static void setOfferExpires(Stub var0, Duration var1) {
      Map var2 = getPropertiesFromStub(var0);
      WsrmProtocolUtils.setOfferExpires(var2, var1);
      setPropertiesOntoStub(var2, var0);
   }

   public static Duration getOfferExpires(Stub var0) {
      return WsrmProtocolUtils.getOfferExpires(getPropertiesFromStub(var0));
   }

   public static void setAnonymousAck(Stub var0) {
      Map var1 = getPropertiesFromStub(var0);
      WsrmProtocolUtils.setAnonymousAck(var1);
      setPropertiesOntoStub(var1, var0);
   }

   public static boolean isAnonymousAck(Stub var0) {
      return WsrmProtocolUtils.isAnonymousAck(getPropertiesFromStub(var0));
   }

   public static void setLastMessage(Stub var0) {
      Map var1 = getPropertiesFromStub(var0);
      WsrmProtocolUtils.setLastMessage(var1);
      setPropertiesOntoStub(var1, var0);
   }

   public static boolean isLastMessage(Stub var0) {
      return WsrmProtocolUtils.isLastMessage(getPropertiesFromStub(var0));
   }

   public static void setFinalMessage(Stub var0) {
      Map var1 = getPropertiesFromStub(var0);
      WsrmProtocolUtils.setFinalMessage(var1);
      setPropertiesOntoStub(var1, var0);
   }

   public static boolean isFinalMessage(Stub var0) {
      return WsrmProtocolUtils.isFinalMessage(getPropertiesFromStub(var0));
   }

   public static void printSoapMsg(SOAPMessage var0) {
      Verbose.getOut().println("\n-------------------------------\n");

      try {
         var0.writeTo(Verbose.getOut());
      } catch (IOException var2) {
         Verbose.logException(var2);
      } catch (SOAPException var3) {
         Verbose.logException(var3);
      }

      Verbose.getOut().println("\n\n-------------------------------\n\n");
   }
}
