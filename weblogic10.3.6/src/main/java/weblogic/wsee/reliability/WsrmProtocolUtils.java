package weblogic.wsee.reliability;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.xml.datatype.Duration;
import javax.xml.rpc.JAXRPCException;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.rpc.handler.soap.SOAPMessageContext;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import weblogic.messaging.saf.SAFConversationInfo;
import weblogic.messaging.saf.SAFException;
import weblogic.wsee.addressing.ActionHeader;
import weblogic.wsee.addressing.AddressingProvider;
import weblogic.wsee.addressing.AddressingProviderFactory;
import weblogic.wsee.addressing.EndpointReference;
import weblogic.wsee.message.MsgHeader;
import weblogic.wsee.message.MsgHeaders;
import weblogic.wsee.message.soap.SoapMessageContext;
import weblogic.wsee.policy.framework.NormalizedExpression;
import weblogic.wsee.reliability.faults.IllegalRMVersionFaultException;
import weblogic.wsee.reliability.headers.WsrmHeader;
import weblogic.wsee.reliability.policy.ReliabilityPolicyAssertionsFactory;
import weblogic.wsee.util.FaultUtil;
import weblogic.wsee.util.Guid;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.util.WLMessageFactory;

public class WsrmProtocolUtils {
   private static final boolean verbose = Verbose.isVerbose(WsrmProtocolUtils.class);

   public static boolean isSequenceInitialized(Map var0) {
      return isSequenceInitialized(var0, false);
   }

   public static boolean isSequenceInitialized(Map var0, boolean var1) {
      String var2 = getSequenceIdFromInvokeProperties(var0);
      SAFConversationInfo var3 = WsrmSAFManager.getConversationInfo(true, var2, var1);
      if (var3 == null) {
         return false;
      } else if (var3.isDynamic()) {
         return var3.getDynamicConversationName() != null;
      } else {
         return true;
      }
   }

   private static String getSequenceIdFromInvokeProperties(Map var0) {
      if (var0 == null) {
         throw new JAXRPCException("Null invoke properties found.");
      } else {
         String var1 = (String)var0.get("weblogic.wsee.sequenceid");
         if (var1 != null && !var1.equals("PendingSeqId")) {
            return var1;
         } else {
            throw new JAXRPCException("No reliable sequence ID found in invoke properties.");
         }
      }
   }

   private static SAFConversationInfo getConversationInfo(String var0) {
      return WsrmSAFManager.getConversationInfo(true, var0, false);
   }

   public static String waitForSequenceInitialization(Map var0, long var1, long var3) {
      if (isSequenceInitialized(var0, true)) {
         return getSequenceId(var0);
      } else {
         for(int var5 = 0; (long)var5 < var3; ++var5) {
            try {
               if (verbose) {
                  Verbose.say("# Waiting for sequence initialization (" + (var5 + 1) + " of " + var3 + ")...");
               }

               Thread.sleep(var1);
               if (isSequenceInitialized(var0, true)) {
                  if (verbose) {
                     Verbose.say("# Sequence initialized: " + getSequenceId(var0));
                  }

                  return getSequenceId(var0);
               }
            } catch (InterruptedException var7) {
            }
         }

         return null;
      }
   }

   /** @deprecated */
   public static void sendEmptyLastMessage(Map var0, String var1) {
      EndpointReference var2 = new EndpointReference(var1);
      sendEmptyLastMessage(var0, var2);
   }

   /** @deprecated */
   public static void sendEmptyLastMessage(Map var0, EndpointReference var1) {
      String var2 = getSequenceIdFromInvokeProperties(var0);

      boolean var3;
      try {
         SAFConversationInfo var4 = getConversationInfo(var2);
         WsrmSequenceContext var5 = (WsrmSequenceContext)var4.getContext();
         var3 = var5.isSoap12();
      } catch (JAXRPCException var18) {
         throw var18;
      } catch (Exception var19) {
         throw new JAXRPCException(var19);
      }

      SoapMessageContext var21 = new SoapMessageContext(var3);
      MessageFactory var22 = WLMessageFactory.getInstance().getMessageFactory(var21.isSoap12());

      SOAPMessage var6;
      try {
         var6 = var22.createMessage();
      } catch (SOAPException var17) {
         throw new JAXRPCException(var17);
      }

      var21.setMessage(var6);
      synchronized(var0) {
         Iterator var8 = var0.entrySet().iterator();

         while(var8.hasNext()) {
            Map.Entry var9 = (Map.Entry)var8.next();

            assert var9.getKey() instanceof String;

            var21.setProperty((String)var9.getKey(), var9.getValue());
         }
      }

      SAFConversationInfo var7 = getConversationInfo(var2);
      WsrmSequenceContext var23 = (WsrmSequenceContext)var7.getContext();
      if (var23.getRmVersion() != WsrmConstants.RMVersion.RM_10) {
         throw new IllegalStateException("Cannot send empty last message on sequence that isn't using WS-RM version 1.0");
      } else {
         MsgHeaders var24 = var21.getHeaders();
         Iterator var10 = var1.getReferenceProperties().listHeaders();

         while(var10.hasNext()) {
            var24.addHeader((MsgHeader)var10.next());
         }

         var10 = var1.getReferenceParameters().listHeaders();

         while(var10.hasNext()) {
            var24.addHeader((MsgHeader)var10.next());
         }

         var21.setProperty("weblogic.wsee.lastmessage", "true");
         AddressingProvider var11 = AddressingProviderFactory.getInstance().getAddressingProvider((MessageContext)var21);
         var21.getHeaders().addHeader(var11.createToHeader(var1.getAddress()));
         var21.getHeaders().addHeader(var11.createActionHeader(WsrmConstants.Action.LAST_MESSAGE.getActionURI(var23.getRmVersion())));
         var21.getHeaders().addHeader(var11.createMessageIdHeader(Guid.generateGuid()));
         WsrmSAFManagerFactory.getWsrmSAFSendingManager().storeAndForward(var21);
         if (var21.hasFault()) {
            if (var21.getFault() != null) {
               Throwable var25 = var21.getFault();
               if (var25 instanceof RuntimeException) {
                  throw (RuntimeException)var25;
               }

               throw new RuntimeException(var25.toString(), var25);
            }

            SOAPMessage var12 = var21.getMessage();

            try {
               if (var12 != null) {
                  SOAPBody var13 = var12.getSOAPBody();
                  if (var13 != null && var13.hasFault()) {
                     String var14 = var13.getFault().getFaultCode();
                     String var15 = var13.getFault().getFaultString();
                     FaultUtil.throwSOAPFaultException(var14, var15, new Exception(var15));
                  }
               }
            } catch (SOAPException var16) {
               throw new JAXRPCException("Failed to call hasFault", var16);
            }
         }

      }
   }

   public static void closeSequence(Map var0) {
      String var1 = getSequenceIdFromInvokeProperties(var0);

      try {
         WsrmSequenceSender.closeSequence(var1);
      } catch (JAXRPCException var3) {
         throw var3;
      } catch (Throwable var4) {
         throw new JAXRPCException(var4);
      }
   }

   public static void terminateSequence(Map var0) {
      String var1 = getSequenceIdFromInvokeProperties(var0);

      try {
         SAFConversationInfo var2 = getConversationInfo(var1);
         WsrmSequenceSender.terminateSequence(var2, false);
      } catch (JAXRPCException var3) {
         throw var3;
      } catch (SAFException var4) {
         throw new JAXRPCException(var4);
      } catch (Throwable var5) {
         throw new JAXRPCException(var5);
      }
   }

   public static void reset(Map var0) {
      if (var0 != null && var0.containsKey("weblogic.wsee.sequenceid")) {
         var0.remove("weblogic.wsee.sequenceid");
      }

   }

   public static String getSequenceId(Map var0) {
      String var1 = getSequenceIdFromInvokeProperties(var0);

      try {
         SAFConversationInfo var2 = getConversationInfo(var1);
         return var2.isDynamic() ? var2.getDynamicConversationName() : var2.getConversationName();
      } catch (JAXRPCException var3) {
         throw var3;
      } catch (Throwable var4) {
         throw new JAXRPCException(var4);
      }
   }

   public static void setExpires(Map var0, Duration var1) {
      if (var0 == null) {
         throw new JAXRPCException("Properties param is null");
      } else {
         var0.put("weblogic.wsee.wsrm.sequence.expiration", var1);
      }
   }

   public static Duration getExpires(Map var0) {
      if (var0 == null) {
         throw new JAXRPCException("Properties param is null");
      } else {
         return (Duration)var0.get("weblogic.wsee.wsrm.sequence.expiration");
      }
   }

   public static void setOfferExpires(Map var0, Duration var1) {
      if (var0 == null) {
         throw new JAXRPCException("Properties param is null");
      } else {
         var0.put("weblogic.wsee.wsrm.offer.sequence.expiration", var1);
      }
   }

   public static Duration getOfferExpires(Map var0) {
      if (var0 == null) {
         throw new JAXRPCException("Properties param is null");
      } else {
         return (Duration)var0.get("weblogic.wsee.wsrm.offer.sequence.expiration");
      }
   }

   public static void setAnonymousAck(Map var0) {
      if (var0 == null) {
         throw new JAXRPCException("Properties param is null");
      } else {
         var0.put("weblogic.wsee.ackstoanon", "true");
      }
   }

   public static boolean isAnonymousAck(Map var0) {
      if (var0 == null) {
         throw new JAXRPCException("Properties param is null");
      } else {
         return var0.get("weblogic.wsee.ackstoanon") != null;
      }
   }

   public static void setLastMessage(Map var0) {
      if (var0 == null) {
         throw new JAXRPCException("Properties param is null");
      } else {
         var0.put("weblogic.wsee.lastmessage", "true");
      }
   }

   public static void setFinalMessage(Map var0) {
      if (var0 == null) {
         throw new JAXRPCException("properties param is null");
      } else {
         var0.put("weblogic.wsee.lastmessage", "true");
      }
   }

   public static boolean isLastMessage(Map var0) {
      if (var0 == null) {
         throw new JAXRPCException("Properties param is null");
      } else {
         return var0.get("weblogic.wsee.lastmessage") != null;
      }
   }

   public static boolean isFinalMessage(Map var0) {
      if (var0 == null) {
         throw new JAXRPCException("Properties param is null");
      } else {
         return var0.get("weblogic.wsee.lastmessage") != null;
      }
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

   public static WsrmConstants.RMVersion getActionVersion(WsrmConstants.Action var0, String var1) {
      WsrmConstants.RMVersion[] var2 = WsrmConstants.RMVersion.values();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         WsrmConstants.RMVersion var5 = var2[var4];
         String var6 = var0.getActionURI(var5);
         if (var6.equals(var1)) {
            return var5;
         }
      }

      return null;
   }

   public static void checkRMVersion(String var0, WsrmConstants.RMVersion var1, NormalizedExpression var2) throws IllegalRMVersionFaultException {
      List var3 = ReliabilityPolicyAssertionsFactory.getRMPolicyVersions(var2);
      if (!var3.contains(var1)) {
         throw new IllegalRMVersionFaultException(var0, var1, var3);
      }
   }

   public static WsrmConstants.SOAPVersion getSOAPVersionFromName(Name var0) {
      WsrmConstants.SOAPVersion[] var1 = WsrmConstants.SOAPVersion.values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         WsrmConstants.SOAPVersion var4 = var1[var3];
         if (var4.getNamespaceUri().equals(var0.getURI())) {
            return var4;
         }
      }

      return WsrmConstants.SOAPVersion.SOAP_11;
   }

   public static WsrmConstants.FaultCode getSOAPFaultCodeFromName(Name var0) {
      WsrmConstants.SOAPVersion var1 = getSOAPVersionFromName(var0);
      WsrmConstants.FaultCode[] var2 = WsrmConstants.FaultCode.values();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         WsrmConstants.FaultCode var5 = var2[var4];
         if (var5.getCodeLocalName(var1).equals(var0.getLocalName())) {
            return var5;
         }
      }

      return null;
   }

   public static WsrmConstants.SOAPVersion getSOAPVersionFromNamespaceUri(String var0) {
      WsrmConstants.SOAPVersion[] var1 = WsrmConstants.SOAPVersion.values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         WsrmConstants.SOAPVersion var4 = var1[var3];
         if (var4.getNamespaceUri().equals(var0)) {
            return var4;
         }
      }

      return WsrmConstants.SOAPVersion.SOAP_11;
   }

   public static WsrmConstants.RMVersion getRMVersionFromMessageContext(SOAPMessageContext var0) {
      WsrmConstants.RMVersion var1 = null;
      MsgHeaders var2 = ((SoapMessageContext)var0).getHeaders();
      ActionHeader var3 = null;
      Iterator var4 = var2.listHeaders();

      while(var4.hasNext()) {
         MsgHeader var5 = (MsgHeader)var4.next();
         if (var5 instanceof ActionHeader) {
            var3 = (ActionHeader)var5;
         }

         if (var5 instanceof WsrmHeader) {
            var1 = ((WsrmHeader)var5).getRmVersion();
            break;
         }
      }

      if (var1 == null && var3 != null) {
         WsrmConstants.Action[] var9 = WsrmConstants.Action.values();
         int var6 = var9.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            WsrmConstants.Action var8 = var9[var7];
            var1 = getActionVersion(var8, var3.getActionURI());
            if (var1 != null) {
               break;
            }
         }
      }

      if (var1 == null) {
         var1 = WsrmConstants.RMVersion.RM_10;
      }

      return var1;
   }
}
