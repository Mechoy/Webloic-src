package weblogic.wsee.conversation;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import javax.xml.rpc.JAXRPCException;
import javax.xml.soap.SOAPMessage;
import weblogic.wsee.addressing.MessageIdHeader;
import weblogic.wsee.addressing.ToHeader;
import weblogic.wsee.async.SOAPInvokeState;
import weblogic.wsee.cluster.ClusterService;
import weblogic.wsee.cluster.ClusterServiceException;
import weblogic.wsee.cluster.SerializableSOAPMessage;
import weblogic.wsee.connection.soap.SoapConnection;
import weblogic.wsee.message.MsgHeaderException;
import weblogic.wsee.message.soap.SoapMessageContext;
import weblogic.wsee.message.soap.SoapMsgHeaders;
import weblogic.wsee.server.rmi.RMISoapDispatcher;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.ws.WsPort;
import weblogic.wsee.ws.WsRegistry;

public class ConversationMsgClusterService implements ClusterService {
   private static final boolean verbose = Verbose.isVerbose(ConversationMsgClusterService.class);
   public static final String TARGET_URI = "weblogic.wsee.conversation.msg.cluster.service";

   public String getTargetURI() {
      return "weblogic.wsee.conversation.msg.cluster.service";
   }

   public Serializable dispatch(Serializable var1) throws ClusterServiceException {
      if (!(var1 instanceof SOAPInvokeState)) {
         throw new ClusterServiceException("Invalid object type: " + var1);
      } else if (var1 == null) {
         throw new ClusterServiceException("Null object");
      } else {
         SOAPMessage var2 = ((SOAPInvokeState)var1).getSOAPMessage();
         if (var2 == null) {
            throw new ClusterServiceException("Null SOAP message");
         } else {
            try {
               SoapMsgHeaders var3 = new SoapMsgHeaders(var2);
               SOAPInvokeState var4 = (SOAPInvokeState)var1;
               Map var5 = var4.getMessageContextProperties();
               String var6 = var4.getServiceURI();
               if (verbose) {
                  MessageIdHeader var7 = (MessageIdHeader)var3.getHeader(MessageIdHeader.TYPE);
                  String var8 = null;
                  if (var7 != null) {
                     var8 = var7.getMessageId();
                  }

                  String var9 = (String)var5.get("weblogic.wsee.conversation.ConversationId");
                  Verbose.log((Object)"------------------------------");
                  Verbose.log((Object)("Invoking conversation message cluster service for msg " + var8 + " and conversation " + var9));
                  Verbose.log((Object)"------------------------------");
                  boolean var10 = false;

                  try {
                     var10 = "http://www.w3.org/2003/05/soap-envelope".equals(var2.getSOAPPart().getEnvelope().getNamespaceURI());
                  } catch (Exception var15) {
                     var15.printStackTrace();
                  }

                  SoapMessageContext var11 = new SoapMessageContext(var10);
                  var11.setMessage(var2);
                  Iterator var12 = var5.keySet().iterator();

                  while(var12.hasNext()) {
                     Object var13 = var12.next();
                     Object var14 = var5.get(var13);
                     var11.setProperty((String)var13, var14);
                  }

                  SoapConnection.dumpSoapMsg(var11, false);
               }

               if (var6 == null) {
                  ToHeader var17 = (ToHeader)var3.getHeader(ToHeader.TYPE);
                  if (var17 == null) {
                     throw new ClusterServiceException("Cannot find the TO WS-Addressing header");
                  }

                  var6 = var17.getAddress();
                  var6 = var6.substring(var6.indexOf("/", var6.indexOf("//") + 2));
               }

               String var18 = WsRegistry.getVersion(var6);
               if (var18 == null) {
                  var18 = (String)var5.get("weblogic.wsee.cluster.forwarded_version");
                  if (var18 == null) {
                     var18 = (String)var5.get("weblogic.wsee.version.appversion.id");
                  }
               }

               WsPort var19 = WsRegistry.instance().lookup(var6, var18);
               if (var19 == null) {
                  throw new JAXRPCException("No port found for " + var6);
               } else {
                  RMISoapDispatcher var20 = new RMISoapDispatcher(var19);
                  SOAPMessage var21 = var20.dispatch((SOAPInvokeState)var1, var6);
                  return var21 != null ? new SerializableSOAPMessage(var21) : null;
               }
            } catch (MsgHeaderException var16) {
               if (verbose) {
                  Verbose.logException(var16);
               }

               throw new ClusterServiceException(var16);
            }
         }
      }
   }
}
