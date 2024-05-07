package weblogic.wsee.conversation;

import java.util.Map;
import javax.xml.namespace.QName;
import javax.xml.rpc.JAXRPCException;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.rpc.handler.soap.SOAPMessageContext;
import weblogic.store.PersistentStoreException;
import weblogic.wsee.addressing.EndpointReference;
import weblogic.wsee.handler.WLHandler;
import weblogic.wsee.message.FreeStandingMsgHeaders;
import weblogic.wsee.message.WlMessageContext;
import weblogic.wsee.server.WsStorage;
import weblogic.wsee.server.WsStorageFactory;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.wsdl.WsdlBindingOperation;

public class ConversationGetIdHandler extends ConversationHandler implements WLHandler {
   static final int DEFAULT_WAITID_TIMEOUT = 120;
   private static final boolean verbose = Verbose.isVerbose(ConversationGetIdHandler.class);

   public boolean handleRequest(MessageContext var1) {
      if (!(var1 instanceof SOAPMessageContext)) {
         return true;
      } else {
         WlMessageContext var2 = WlMessageContext.narrow(var1);
         WsdlBindingOperation var3 = var2.getDispatcher().getBindingOperation();
         QName var4 = var3.getName();
         ConversationPhase var5 = this.getConversationPhase(var2);
         if (var5 != null && var5 != ConversationPhase.NONE) {
            if (var5 != ConversationPhase.START && var1.getProperty("weblogic.wsee.conversation.started") == null) {
               throw new JAXRPCException("Operation " + var4 + " with phase " + var5 + " cannot be invoked without calling" + " start operation first");
            } else {
               var1.setProperty("weblogic.wsee.conversation.ConversationPhase", var5);
               var1.setProperty("weblogic.wsee.complex", "true");
               int var6 = this.getConversationMajorVersion(var2);
               if (var6 != 1 && var6 != 2) {
                  throw new JAXRPCException("Unsupported conversation version " + var6 + " on method " + var4);
               } else {
                  var1.setProperty("weblogic.wsee.conversation.ConversationVersion", new Integer(var6));
                  String var7 = (String)var1.getProperty("weblogic.wsee.conversation.ConversationId");
                  if (var7 != null) {
                     return true;
                  } else if (var6 != 1 && var5 != ConversationPhase.START) {
                     Map var8 = (Map)var1.getProperty("weblogic.wsee.invoke_properties");

                     assert var8 != null;

                     String var9 = (String)var8.get("weblogic.wsee.conversation.correlation.id");
                     if (var9 != null) {
                        WsStorage var10 = WsStorageFactory.getStorage("weblogic.wsee.conversation.store", new ConversationInvokeStateObjectHandler());

                        ConversationInvokeState var11;
                        try {
                           var11 = (ConversationInvokeState)var10.persistentGet(var9);
                           if (var11 == null) {
                              throw new JAXRPCException("Cannot retrieve conversation information for correlation " + var9);
                           }
                        } catch (PersistentStoreException var18) {
                           if (verbose) {
                              Verbose.logException(var18);
                           }

                           throw new JAXRPCException(var18);
                        }

                        EndpointReference var12 = var11.getEpr();
                        if (var12 == null) {
                           boolean var13 = var1.getProperty("weblogic.wsee.enable.rm") != null;
                           if (var13) {
                              var1.setProperty("weblogic.wsee.conversation.waitid", "true");
                              return true;
                           }

                           Integer var14 = (Integer)var1.getProperty("weblogic.wsee.conversation.method.block.timeout");
                           int var15 = var14 == null ? 120 : var14;
                           if (verbose) {
                              Verbose.log((Object)("Waiting for conversation EPR to come back on method " + var4 + " (" + var15 + " seconds) ........"));
                           }

                           var12 = var11.getEpr(var15);
                           if (var12 == null) {
                              throw new JAXRPCException("Did not receive server assigned conversation id for method " + var4 + " in " + var15 + " seconds");
                           }
                        }

                        if (verbose) {
                           Verbose.log((Object)"Successfully got conversation EPR ........");
                        }

                        EndpointReference var19 = (EndpointReference)var1.getProperty("weblogic.wsee.addressing.Target");
                        if (var19 != null) {
                           if (!var19.getAddress().equals(var12.getAddress())) {
                              throw new JAXRPCException("The address in the new conversational EPR does not match the existing address for method " + var4 + ", new: " + var12.getAddress() + " old: " + var19.getAddress());
                           }

                           ((FreeStandingMsgHeaders)var19.getReferenceProperties()).merge((FreeStandingMsgHeaders)var12.getReferenceProperties());
                           ((FreeStandingMsgHeaders)var19.getReferenceParameters()).merge((FreeStandingMsgHeaders)var12.getReferenceParameters());
                           var8.put("weblogic.wsee.addressing.Target", var19);
                        } else {
                           var8.put("weblogic.wsee.addressing.Target", var12);
                           var1.setProperty("weblogic.wsee.addressing.Target", var12);
                        }

                        var8.remove("weblogic.wsee.conversation.correlation.id");
                        EndpointReference var20 = (EndpointReference)var1.getProperty("weblogic.wsee.addressing.Target");
                        ContinueHeader var21 = (ContinueHeader)var20.getReferenceParameters().getHeader(ContinueHeader.TYPE);
                        if (var21 == null) {
                           var21 = (ContinueHeader)var20.getReferenceProperties().getHeader(ContinueHeader.TYPE);
                        }

                        if (var21 != null) {
                           var7 = var21.getConversationId();
                           var8.put("weblogic.wsee.conversation.ConversationId", var7);
                           var1.setProperty("weblogic.wsee.conversation.ConversationId", var7);
                        }

                        try {
                           if (verbose) {
                              Verbose.log((Object)("Explicitly removing ConversationInvokeState RM=true under correlation ID " + var9 + "."));
                           }

                           var10.persistentRemove(var9);
                        } catch (PersistentStoreException var17) {
                           if (verbose) {
                              Verbose.logException(var17);
                           }

                           throw new JAXRPCException(var17);
                        }
                     }

                     return true;
                  } else {
                     return true;
                  }
               }
            }
         } else {
            return true;
         }
      }
   }

   public boolean handleResponse(MessageContext var1) {
      ConversationPhase var2 = (ConversationPhase)var1.getProperty("weblogic.wsee.conversation.ConversationPhase");
      if (var2 == ConversationPhase.FINISH) {
         Map var3 = (Map)var1.getProperty("weblogic.wsee.invoke_properties");

         assert var3 != null;

         Integer var4 = (Integer)var1.getProperty("weblogic.wsee.conversation.ConversationVersion");
         if (var4 == null) {
            return true;
         }

         synchronized(var3) {
            var3.remove("weblogic.wsee.conversation.ConversationId");
            var3.remove("weblogic.wsee.conversation.started");
            if (var4 == 2) {
               var3.remove("weblogic.wsee.addressing.Target");
            }
         }
      }

      return true;
   }

   public boolean handleClosure(MessageContext var1) {
      return this.handleResponse(var1);
   }
}
