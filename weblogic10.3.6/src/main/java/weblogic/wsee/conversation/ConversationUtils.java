package weblogic.wsee.conversation;

import java.util.Iterator;
import java.util.Map;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.rpc.JAXRPCException;
import javax.xml.rpc.Stub;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import weblogic.store.PersistentStoreException;
import weblogic.wsee.addressing.EndpointReference;
import weblogic.wsee.cluster.ServiceIdentityHeader;
import weblogic.wsee.conversation.wsdl.ConversationWsdlPhase;
import weblogic.wsee.jaxrpc.WLStub;
import weblogic.wsee.message.MsgHeaders;
import weblogic.wsee.message.WlMessageContext;
import weblogic.wsee.server.WsStorage;
import weblogic.wsee.server.WsStorageFactory;
import weblogic.wsee.util.ControlAPIUtil;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.ws.WsPort;
import weblogic.wsee.wsdl.WsdlBinding;
import weblogic.wsee.wsdl.WsdlBindingOperation;

public class ConversationUtils {
   private static final boolean verbose = Verbose.isVerbose(ConversationUtils.class);
   public static final String SERVER_NAME = "serverName";
   public static final String CONVERSATION_ID = "conversationId";

   public static String getConversationId(Stub var0) throws ConversationIdNotYetAvailableException {
      if (var0 == null) {
         throw new JAXRPCException("Stub is null");
      } else {
         String var1 = (String)var0._getProperty("weblogic.wsee.conversation.ConversationId");
         if (var1 != null) {
            return var1;
         } else {
            Map var2 = (Map)var0._getProperty("weblogic.wsee.invoke_properties");
            if (var2 == null) {
               return null;
            } else {
               var1 = (String)var2.get("weblogic.wsee.conversation.ConversationId");
               if (var1 != null) {
                  return var1;
               } else if (var2.get("weblogic.wsee.conversation.started") == null) {
                  return null;
               } else {
                  String var3 = (String)var2.get("weblogic.wsee.conversation.correlation.id");
                  if (var3 != null) {
                     WsStorage var4 = WsStorageFactory.getStorage("weblogic.wsee.conversation.store", new ConversationInvokeStateObjectHandler());
                     ConversationInvokeState var5 = null;

                     try {
                        var5 = (ConversationInvokeState)var4.persistentGet(var3);
                        if (var5 == null) {
                           throw new JAXRPCException("Internal error: cannot get conversation state.");
                        }
                     } catch (PersistentStoreException var8) {
                        throw new JAXRPCException("Internal error: error getting conversation state", var8);
                     }

                     EndpointReference var6 = var5.getEpr();
                     if (var6 != null) {
                        ContinueHeader var7 = (ContinueHeader)var6.getReferenceParameters().getHeader(ContinueHeader.TYPE);
                        if (var7 == null) {
                           var7 = (ContinueHeader)var6.getReferenceProperties().getHeader(ContinueHeader.TYPE);
                        }

                        if (var7 != null) {
                           return var7.getConversationId();
                        }
                     }
                  }

                  throw new ConversationIdNotYetAvailableException("Conversation ID assigned by server has not yet reached the client.  Please wait and invoke this method later.");
               }
            }
         }
      }
   }

   public static void setConversationId(Stub var0, String var1) {
      if (var0 == null) {
         throw new JAXRPCException("Stub is null");
      } else {
         var0._setProperty("weblogic.wsee.conversation.ConversationId", var1);
      }
   }

   public static void setConversationVersionOne(Stub var0) {
      if (var0 == null) {
         throw new JAXRPCException("Stub is null");
      } else {
         var0._setProperty("weblogic.wsee.conversation.ConversationVersion", WLStub.CONVERSATION_VERSION_ONE);
      }
   }

   public static void setConversationVersionTwo(Stub var0) {
      if (var0 == null) {
         throw new JAXRPCException("Stub is null");
      } else {
         var0._setProperty("weblogic.wsee.conversation.ConversationVersion", WLStub.CONVERSATION_VERSION_TWO);
      }
   }

   public static Integer getConversationVersion(Stub var0) {
      if (var0 == null) {
         throw new JAXRPCException("Stub is null");
      } else {
         Integer var1 = (Integer)var0._getProperty("weblogic.wsee.conversation.ConversationVersion");
         if (var1 == null) {
            var1 = WLStub.CONVERSATION_VERSION_TWO;
         }

         return var1;
      }
   }

   public static void setConversationMethodBlockTimeout(Stub var0, int var1) {
      if (var0 == null) {
         throw new JAXRPCException("Stub is null");
      } else {
         var0._setProperty("weblogic.wsee.conversation.method.block.timeout", new Integer(var1));
      }
   }

   public static int getConversationMethodBlockTimeout(Stub var0) {
      if (var0 == null) {
         throw new JAXRPCException("Stub is null");
      } else {
         Integer var1 = (Integer)var0._getProperty("weblogic.wsee.conversation.method.block.timeout");
         return var1 == null ? 120 : var1;
      }
   }

   public static boolean isConversational(WsPort var0) {
      boolean var1 = isConversational(var0.getWsdlPort().getBinding());
      if (var1 && verbose) {
         Verbose.log((Object)(var0.getWsdlPort().getName() + " is conversational"));
      }

      return var1;
   }

   public static boolean isConversational(WsdlBinding var0) {
      Iterator var1 = var0.getOperations().values().iterator();

      WsdlBindingOperation var2;
      do {
         if (!var1.hasNext()) {
            return false;
         }

         var2 = (WsdlBindingOperation)var1.next();
      } while(ConversationWsdlPhase.narrow(var2) == null);

      return true;
   }

   public static String getConversationAppVersion(WlMessageContext var0) {
      String var1 = (String)var0.getProperty("weblogic.wsee.version.appversion.id");
      if (var1 == null) {
         MsgHeaders var2 = var0.getHeaders();
         if (var2 != null) {
            ContinueHeader var3 = (ContinueHeader)var2.getHeader(ContinueHeader.TYPE);
            if (var3 != null) {
               var1 = var3.getAppVersionId();
            }
         }
      }

      return var1;
   }

   public static void renewStub(Stub var0) {
      Map var1 = (Map)var0._getProperty("weblogic.wsee.invoke_properties");

      assert var1 != null;

      synchronized(var1) {
         var1.remove("weblogic.wsee.conversation.ConversationId");
         var1.remove("weblogic.wsee.conversation.started");
         var1.remove("weblogic.wsee.conversation.key");
         var1.remove("weblogic.wsee.conversation.epr.set");
         var1.remove("weblogic.wsee.conversation.correlation.id");
         var1.remove("weblogic.wsee.addressing.Target");
      }
   }

   public static void continueConversation(Stub var0, Map var1) {
      String var2 = (String)var1.get("conversationId");
      if (var2 == null) {
         throw new JAXRPCException("No conversation id specified");
      } else {
         String var3 = (String)var1.get("serverName");
         if (var3 == null) {
            throw new JAXRPCException("No server name specified");
         } else {
            var0._setProperty("weblogic.wsee.complex", "true");
            var0._setProperty("weblogic.wsee.conversation.ConversationId", var2);
            var0._setProperty("weblogic.wsee.conversation.started", Boolean.TRUE);
            ServiceIdentityHeader var4 = new ServiceIdentityHeader();
            var4.setServerName(var3);
            var4.setServiceName("weblogic.wsee.conversation.msg.cluster.service");
            ContinueHeader var5 = new ContinueHeader(var2, var3, (String)null);
            Document var6 = null;

            try {
               var6 = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            } catch (ParserConfigurationException var10) {
               throw new JAXRPCException(var10);
            }

            Element var7 = var6.createElementNS(ContinueHeader.NAME.getNamespaceURI(), ContinueHeader.NAME.getPrefix() + ":" + ContinueHeader.NAME.getLocalPart());
            var5.write(var7);
            Element var8 = var6.createElementNS(ServiceIdentityHeader.NAME.getNamespaceURI(), ServiceIdentityHeader.NAME.getPrefix() + ":" + ServiceIdentityHeader.NAME.getLocalPart());
            var4.write(var8);
            Element[] var9 = new Element[]{var7, var8};
            ControlAPIUtil.setOutputHeaders(var0, var9);
         }
      }
   }
}
