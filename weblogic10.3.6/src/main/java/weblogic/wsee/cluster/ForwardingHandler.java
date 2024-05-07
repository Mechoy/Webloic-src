package weblogic.wsee.cluster;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.security.AccessController;
import javax.xml.namespace.QName;
import javax.xml.rpc.JAXRPCException;
import javax.xml.rpc.handler.GenericHandler;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.rpc.handler.soap.SOAPMessageContext;
import weblogic.application.utils.ApplicationVersionUtils;
import weblogic.protocol.LocalServerIdentity;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.wsee.addressing.AddressingProvider;
import weblogic.wsee.addressing.AddressingProviderFactory;
import weblogic.wsee.addressing.FaultToHeader;
import weblogic.wsee.addressing.FromHeader;
import weblogic.wsee.addressing.MessageIdHeader;
import weblogic.wsee.addressing.ReplyToHeader;
import weblogic.wsee.async.SOAPInvokeState;
import weblogic.wsee.connection.soap.SoapConnection;
import weblogic.wsee.conversation.ContinueHeader;
import weblogic.wsee.message.MsgHeaders;
import weblogic.wsee.message.WlMessageContext;
import weblogic.wsee.message.soap.SoapMessageContext;
import weblogic.wsee.reliability.headers.SequenceHeader;
import weblogic.wsee.security.wssc.sct.SCTHelperAllVersion;
import weblogic.wsee.util.AddressingUtil;
import weblogic.wsee.util.PathServiceUtil;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.ws.dispatch.Dispatcher;

public class ForwardingHandler extends GenericHandler {
   private static final boolean verbose = Verbose.isVerbose(ForwardingHandler.class);
   public static final String CLUSTER_ROUTED = "weblogic.wsee.cluster.routed";
   public static final String VERSION_FORWARDED = "weblogic.wsee.cluster.forwarded_version";
   private QName[] HEADERS = new QName[0];

   public QName[] getHeaders() {
      return this.HEADERS;
   }

   public boolean handleRequest(MessageContext var1) {
      assert var1 != null;

      if (!(var1 instanceof SOAPMessageContext)) {
         return true;
      } else {
         WlMessageContext var2 = WlMessageContext.narrow(var1);
         if ("true".equalsIgnoreCase((String)var1.getProperty("weblogic.wsee.jaxws.framework.jaxrpc.SOAPMessageContext.JAX_WS_RUNTIME"))) {
            return true;
         } else {
            if (verbose) {
               Verbose.log((Object)"[ForwardingHandler.handleRequest()] called");
            }

            ServiceIdentityHeader var3 = (ServiceIdentityHeader)var2.getHeaders().getHeader(ServiceIdentityHeader.TYPE);
            String var4 = null;
            Throwable var6 = null;
            String var7 = null;
            String var5;
            MsgHeaders var8;
            ContinueHeader var9;
            if (var3 == null) {
               if (verbose) {
                  Verbose.log((Object)"ServiceIdentityHeader is NULL.");
               }

               var8 = var2.getHeaders();
               var9 = (ContinueHeader)var8.getHeader(ContinueHeader.TYPE);
               String var10;
               if (var9 != null) {
                  if (verbose) {
                     Verbose.log((Object)"ContinueHeader is NOT NULL");
                  }

                  var4 = var9.getServerName();
                  if (var4 == null) {
                     try {
                        var4 = PathServiceUtil.getServerNameFromPathService(var9.getConversationId());
                     } catch (Throwable var12) {
                        var6 = var12;
                        var7 = var9.getConversationId();
                     }
                  }

                  if (verbose) {
                     Verbose.log((Object)("serverName='" + var4 + "'"));
                  }

                  if (var2.getProperty("weblogic.wsee.version.appversion.id") == null) {
                     var10 = var9.getAppVersionId();
                     if (var10 != null) {
                        var2.setProperty("weblogic.wsee.version.appversion.id", var10);
                     }
                  }
               } else {
                  if (verbose) {
                     Verbose.log((Object)"continueHeader is NULL");
                  }

                  if (var2.getProperty("weblogic.wsee.version.appversion.id") == null) {
                     var10 = ApplicationVersionUtils.getCurrentVersionId();
                     if (var10 != null) {
                        var2.setProperty("weblogic.wsee.cluster.forwarded_version", var10);
                     }
                  }
               }

               if (!"true".equalsIgnoreCase((String)var1.getProperty("weblogic.wsee.jaxws.framework.jaxrpc.SOAPMessageContext.JAX_WS_RUNTIME"))) {
                  SequenceHeader var16 = (SequenceHeader)var2.getHeaders().getHeader(SequenceHeader.TYPE);
                  if (var16 != null && var4 == null) {
                     var4 = var16.getSequenceId();
                     var4 = var4.substring(5, var4.indexOf(58, 6));
                  }

                  if (verbose && var4 != null) {
                     Verbose.log((Object)("serverName='" + var4 + "'"));
                  }
               }

               if (var4 == null) {
                  if (verbose) {
                     Verbose.log((Object)"serverName is NULL get from SCToken using SCTHelperAllVersion.getCredentialIdentifier");
                  }

                  var10 = SCTHelperAllVersion.getCredentialIdentifier((SOAPMessageContext)var1);
                  if (var10 != null) {
                     int var11 = var10.indexOf("uuid:");
                     if (var11 == 0) {
                        var11 = var10.indexOf(58, 6);
                        if (var11 >= 6) {
                           var4 = var10.substring(5, var11);
                           if (verbose) {
                              Verbose.log((Object)("Routing to " + var4 + " using SCT id"));
                           }
                        }
                     } else if (verbose) {
                        Verbose.log((Object)("credId='" + var10 + "' has no 'uuid'"));
                     }
                  }
               }

               if (var4 == null && var6 == null) {
                  return true;
               }

               if (var4 == null && var6 != null) {
                  throw new JAXRPCException("Failed to determine the route for conversation " + var7, var6);
               }

               var5 = "weblogic.wsee.conversation.msg.cluster.service";
            } else {
               var5 = var3.getServiceName();
               if (verbose) {
                  Verbose.log((Object)("ServiceIdentityHeader is NOT NULL.  got serviceName from ServiceIdentityHeader='" + var5 + "'"));
               }

               if (!var5.equals("weblogic.wsee.conversation.msg.cluster.service")) {
                  return true;
               }

               var4 = var3.getServerName();
               if (verbose) {
                  Verbose.log((Object)(" from Header, got serverName='" + var4 + "'"));
               }

               if (var2.getProperty("weblogic.wsee.version.appversion.id") == null) {
                  var8 = var2.getHeaders();
                  var9 = (ContinueHeader)var8.getHeader(ContinueHeader.TYPE);
                  if (var9 != null && var9.getAppVersionId() != null) {
                     var2.setProperty("weblogic.wsee.version.appversion.id", var9.getAppVersionId());
                  }
               }
            }

            String var13 = LocalServerIdentity.getIdentity().getServerName();
            if (verbose) {
               Verbose.log((Object)("from LocalServerIdentity.getIdentity() got serverName='" + var13 + "'"));
            }

            if (var13.equalsIgnoreCase(var4)) {
               return true;
            } else {
               if (verbose) {
                  MsgHeaders var14 = var2.getHeaders();
                  MessageIdHeader var17 = (MessageIdHeader)var14.getHeader(MessageIdHeader.TYPE);
                  String var18 = null;
                  if (var17 != null) {
                     var18 = var17.getMessageId();
                  }

                  Verbose.log((Object)("!! Forwarding msg id " + var18 + " from server " + var13 + " to server " + var4));
                  SoapConnection.dumpSoapMsg((SoapMessageContext)var2, true);
               }

               Dispatcher var15 = var2.getDispatcher();
               boolean var19 = this.confirmOneway(var15, var2);
               this.route(var4, var5, var2, var19);
               return false;
            }
         }
      }
   }

   private void route(String var1, String var2, WlMessageContext var3, boolean var4) {
      SOAPMessageContext var5 = (SOAPMessageContext)var3;

      try {
         ClusterDispatcherRemote var6 = ClusterRoutingUtil.getClusterDispatcher(var1, var2);
         if (var6 != null) {
            SOAPInvokeState var7 = new SOAPInvokeState(var5);
            var7.setSubject(ClusterUtil.getSubject((AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction())));
            String var8 = null;
            if (verbose) {
               MsgHeaders var9 = var3.getHeaders();
               MessageIdHeader var10 = (MessageIdHeader)var9.getHeader(MessageIdHeader.TYPE);
               if (var10 != null) {
                  var8 = var10.getMessageId();
               }

               Verbose.say("!! Invoking ClusterDispatcherRemote for msg " + var8);
            }

            Serializable var14 = var6.dispatch(var2, var7);
            if (verbose) {
               Verbose.say("!! Back from ClusterDispatcherRemote on msg " + var8 + " with ret: " + var14);
            }

            if (var14 != null) {
               if (!(var14 instanceof SerializableSOAPMessage)) {
                  throw new JAXRPCException("Unknown return type for cluster routing");
               }

               SerializableSOAPMessage var13 = (SerializableSOAPMessage)var14;
               var5.setMessage(var13.getSOAPMessage());
               var3.setProperty("weblogic.wsee.reply.anonymous", "true");
            } else {
               var3.setProperty("weblogic.wsee.cluster.routed", "true");
               if (!var4) {
                  AddressingUtil.confirmOneway(var3);
                  var4 = true;
               }
            }
         }

      } catch (ClusterServiceException var11) {
         if (verbose) {
            Verbose.logException(var11);
         }

         throw new JAXRPCException(var11);
      } catch (RemoteException var12) {
         if (verbose) {
            Verbose.logException(var12);
         }

         throw new JAXRPCException(var12);
      }
   }

   private boolean confirmOneway(Dispatcher var1, WlMessageContext var2) {
      boolean var3 = false;
      if (var1.getOperation() != null && (var1.getOperation().getType() == 1 || var1.getOperation().getType() == 3)) {
         AddressingUtil.confirmOneway(var2);
         var3 = true;
      }

      if (!var3 && this.checkAsyncReqRes(var2)) {
         AddressingUtil.confirmOneway(var2);
         var3 = true;
      }

      return var3;
   }

   private boolean checkAsyncReqRes(WlMessageContext var1) {
      boolean var2 = false;
      boolean var3 = false;
      ReplyToHeader var4 = (ReplyToHeader)var1.getHeaders().getHeader(ReplyToHeader.TYPE);
      AddressingProvider var5 = AddressingProviderFactory.getInstance().getAddressingProvider((MessageContext)var1);
      if (var4 != null) {
         if (!var5.isAnonymousReferenceURI(var4.getReference().getAddress())) {
            var2 = true;
         }
      } else {
         FromHeader var6 = (FromHeader)var1.getHeaders().getHeader(FromHeader.TYPE);
         if (var6 != null && !var5.isAnonymousReferenceURI(var6.getReference().getAddress())) {
            var2 = true;
         }
      }

      FaultToHeader var7 = (FaultToHeader)var1.getHeaders().getHeader(FaultToHeader.TYPE);
      if (var7 != null) {
         if (!var5.isAnonymousReferenceURI(var7.getReference().getAddress())) {
            var3 = true;
         }
      } else {
         var3 = var2;
      }

      return var2 && var3;
   }
}
