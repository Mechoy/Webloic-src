package weblogic.wsee.async;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.UndeclaredThrowableException;
import java.rmi.RemoteException;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import javax.xml.namespace.QName;
import javax.xml.rpc.JAXRPCException;
import javax.xml.rpc.Stub;
import javax.xml.rpc.handler.GenericHandler;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.rpc.handler.soap.SOAPMessageContext;
import javax.xml.rpc.soap.SOAPFaultException;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPMessage;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.store.PersistentStoreException;
import weblogic.wsee.addressing.MessageIdHeader;
import weblogic.wsee.conversation.ContinueHeader;
import weblogic.wsee.conversation.ConversationPhase;
import weblogic.wsee.handler.HandlerIterator;
import weblogic.wsee.handler.InvocationException;
import weblogic.wsee.jaxrpc.StubImpl;
import weblogic.wsee.message.MsgHeaders;
import weblogic.wsee.message.WlMessageContext;
import weblogic.wsee.message.soap.SoapMessageContext;
import weblogic.wsee.reliability.headers.SequenceHeader;
import weblogic.wsee.server.WsStorage;
import weblogic.wsee.server.WsStorageFactory;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.ws.WsPort;
import weblogic.wsee.ws.WsRegistry;
import weblogic.wsee.ws.WsSkel;
import weblogic.wsee.ws.dispatch.client.ClientDispatcher;
import weblogic.xml.crypto.wss.WSSecurityContext;

public class AsyncResponseHandler extends GenericHandler {
   private static final boolean verbose = Verbose.isVerbose(AsyncResponseHandler.class);
   public static final String APP_NAME_PROPERTY = "weblogic.wsee.async.appname";
   public static final String APP_VERSION_PROPERTY = "weblogic.wsee.async.appversion";
   public static final String ASYNC_FAULT_PROPERTY = "weblogic.wsee.async.fault";
   public static final String ASYNC_RESULT_PROPERTY = "weblogic.wsee.async.result";

   public QName[] getHeaders() {
      return null;
   }

   public boolean handleRequest(MessageContext var1) {
      if (var1 == null) {
         return true;
      } else if (!(var1 instanceof SOAPMessageContext)) {
         return true;
      } else {
         if (verbose) {
            Verbose.log((Object)"AsyncResponseHandler.handleRequest");
         }

         String var2 = (String)var1.getProperty("weblogic.wsee.addressing.RelatesTo");
         if (var2 == null) {
            return false;
         } else {
            String var3 = "Unknown";
            String var4 = "";
            if (verbose) {
               MsgHeaders var5 = ((SoapMessageContext)var1).getHeaders();
               MessageIdHeader var6 = (MessageIdHeader)var5.getHeader(MessageIdHeader.TYPE);
               var3 = var6 != null ? var6.getMessageId() : "Unknown";
               SequenceHeader var7 = (SequenceHeader)var5.getHeader(SequenceHeader.TYPE);
               if (var7 != null) {
                  var4 = " on reliable sequence " + var7.getSequenceId() + " and seq num " + var7.getMessageNumber();
               } else {
                  String var8 = (String)var1.getProperty("weblogic.wsee.reliability.RequestMessageSeqNumber");
                  String var9 = (String)var1.getProperty("weblogic.wsee.reliability.RequestMessageSeqID");
                  if (var9 != null) {
                     var4 = " on reliable sequence " + var9 + " and seq num " + var8;
                  }
               }

               Verbose.log((Object)("Processing async response message " + var3 + " related to request msg " + var2 + var4));
               StringBuffer var11 = HandlerIterator.getHandlerHistory(var1);
               if (var11 != null) {
                  Verbose.log((Object)("Async response message " + var3 + " related to request msg " + var2 + var4 + " has this handler history: " + var11));
               }
            }

            try {
               this.handleRequestInternal(var1, var2, var3, var4);
               if (verbose) {
                  Verbose.log((Object)("Successfully processed async response message " + var3 + " related to request msg " + var2 + var4));
               }
            } catch (Throwable var10) {
               if (verbose) {
                  Verbose.log((Object)("Failed to process async response message " + var3 + " related to request msg " + var2 + var4 + " error was: " + var10.toString()));
                  Verbose.logException(var10);
               }
            }

            return true;
         }
      }
   }

   private void handleRequestInternal(MessageContext var1, String var2, String var3, String var4) {
      if (var1.getProperty("weblogic.wsee.oneway.confirmed") == null) {
         try {
            ((WlMessageContext)var1).getDispatcher().getConnection().getTransport().confirmOneway();
         } catch (IOException var14) {
            throw new InvocationException("Failed to confirm oneway", var14);
         }

         var1.setProperty("weblogic.wsee.oneway.confirmed", "true");
      }

      WsStorage var5 = WsStorageFactory.getStorage("weblogic.wsee.async.store", new AsyncInvokeStateObjectHandler());

      AsyncInvokeState var6;
      try {
         var6 = (AsyncInvokeState)var5.persistentGet(var2);
         if (var6 == null) {
            throw new JAXRPCException("Cannot retrieve request information for message " + var2);
         }
      } catch (PersistentStoreException var17) {
         if (verbose) {
            Verbose.logException(var17);
         }

         throw new JAXRPCException(var17);
      }

      synchronized(var6) {
         var6 = (AsyncInvokeState)var5.get(var2);
         if (var6 == null) {
            throw new JAXRPCException("Cannot retrieve request information for message " + var2);
         } else {
            if (var6.getState() == AsyncInvokeState.STATE.PENDING_RESPONSE) {
               var6.setState(AsyncInvokeState.STATE.COMPLETE);

               try {
                  var5.persistentPut(var2, var6);
               } catch (Exception var13) {
                  throw new JAXRPCException("Error while trying to clear the 'pending reliable request' flag from AsyncInvokeState: " + var13.toString(), var13);
               }
            }

            AuthenticatedSubject var8 = var6.getSubject();
            AuthorizedInvoke var9 = new AuthorizedInvoke(var6, var1, var2, var3, var4);
            if (var8 != null) {
               try {
                  SecurityServiceManager.runAs((AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction()), var8, var9);
               } catch (PrivilegedActionException var15) {
                  if (var15.getException() instanceof IOException) {
                     throw new JAXRPCException(var15.getException());
                  }

                  throw new UndeclaredThrowableException(var15.getException());
               }
            } else {
               try {
                  var9.run();
               } catch (Exception var12) {
                  throw new JAXRPCException(var12);
               }
            }

            ContinueHeader var10 = (ContinueHeader)((WlMessageContext)var1).getHeaders().getHeader(ContinueHeader.TYPE);
            if (var10 != null) {
               var1.setProperty("weblogic.wsee.conversation.ConversationId", var10.getConversationId());
               var1.setProperty("weblogic.wsee.conversation.IsServerAssigned", new Boolean(var10.isServerAssigned()));
               var1.setProperty("weblogic.wsee.conversation.ConversationPhase", ConversationPhase.CONTINUE);
               var1.setProperty("weblogic.wsee.version.appversion.id", var10.getAppVersionId());
            }

            this.cleanState(var6, var5, var2);
         }
      }
   }

   private void cleanState(AsyncInvokeState var1, WsStorage var2, String var3) {
      boolean var4 = true;
      if (var1.getMessageContext() != null && var1.getMessageContext().getProperty("weblogic.wsee.enable.rm") != null) {
         var4 = false;
      }

      if (var4) {
         try {
            var2.persistentRemove(var3);
         } catch (PersistentStoreException var6) {
            if (verbose) {
               Verbose.logException(var6);
            }

            throw new JAXRPCException(var6);
         }
      }

   }

   private WsPort getPort(String var1) {
      String var2 = WsRegistry.getURL(var1);
      String var3 = WsRegistry.getVersion(var1);
      return WsRegistry.instance().lookup(var2, var3);
   }

   private class AuthorizedInvoke implements PrivilegedExceptionAction {
      private AsyncInvokeState ais = null;
      private MessageContext mc;
      String relatesTo;
      String requestMsgIdMsg;
      String rmSeqIdMsg;

      AuthorizedInvoke(AsyncInvokeState var2, MessageContext var3, String var4, String var5, String var6) {
         this.ais = var2;
         this.mc = var3;
         this.relatesTo = var4;
         this.requestMsgIdMsg = var5;
         this.rmSeqIdMsg = var6;
      }

      public Object run() throws Exception {
         SoapMessageContext var1 = (SoapMessageContext)this.mc;
         Object var2 = null;
         Object var3 = null;
         ClientDispatcher var4 = this.ais.getDispatcher();
         WlMessageContext var5 = this.ais.getMessageContext();
         if (var5 == null) {
            throw new JAXRPCException("No message context saved");
         } else {
            boolean var6 = this.mc.getProperty("weblogic.wsee.rm.secure") != null;
            WSSecurityContext var7 = WSSecurityContext.getSecurityContext(this.mc);
            if (var7 != null) {
               var5.setProperty("weblogic.xml.crypto.wss.WSSecurityContext", var7);
            }

            String var8 = (String)var5.getProperty("weblogic.wsee.async.appversion");
            if (AsyncResponseHandler.verbose) {
               Verbose.log((Object)("Version from savedMc = " + var8));
            }

            String var9 = (String)var5.getProperty("weblogic.wsee.enclosing.jws.serviceuri");
            if (var8 != null) {
               if (AsyncResponseHandler.verbose) {
                  Verbose.log((Object)("adding version " + var8 + " to URI " + var9));
               }

               var9 = var9 + "#" + var8;
            } else if (AsyncResponseHandler.verbose) {
               Verbose.log((Object)"no version ");
            }

            boolean var10 = var5.containsProperty("weblogic.wsee.jaxrpc.SoapDispatchInitiatedOperation");
            if (var9 == null && !var10) {
               throw new JAXRPCException("No jws service uri found");
            } else {
               ClassLoader var11 = Thread.currentThread().getContextClassLoader();
               if (var9 != null) {
                  WsPort var12 = AsyncResponseHandler.this.getPort(var9);
                  if (var12 == null) {
                     throw new JAXRPCException("No port found for " + var9);
                  }

                  var11 = ((WsSkel)var12.getEndpoint()).getClassLoader();
               }

               Thread var75 = Thread.currentThread();
               ClassLoader var13 = var75.getContextClassLoader();
               String var14;
               if (var4 != null && !var6) {
                  if (AsyncResponseHandler.verbose) {
                     Verbose.log((Object)("Calculating async response using existing ClientDispatcher for message " + this.requestMsgIdMsg + " related to request msg " + this.relatesTo + this.rmSeqIdMsg));
                  }

                  try {
                     var75.setContextClassLoader(var11);
                     ((SoapMessageContext)var4.getContext()).setMessage(var1.getMessage());
                     var4.handleAsyncResponse(var1.getMessage());
                     if (var10) {
                        SOAPMessage var76 = ((SoapMessageContext)var4.getContext()).getMessage();
                        if (var76 instanceof SOAPMessage) {
                           try {
                              SOAPMessage var78 = (SOAPMessage)var76;
                              if (var78.getSOAPBody().hasFault()) {
                                 var3 = var78;
                              } else {
                                 var2 = var78;
                              }
                           } catch (Exception var69) {
                              if (AsyncResponseHandler.verbose) {
                                 Verbose.logException(var69);
                              } else {
                                 var69.printStackTrace();
                              }

                              var2 = var76;
                           }
                        } else {
                           var2 = var76;
                        }
                     } else {
                        var2 = var4.getWsMethod().getReturnType() == null ? null : var4.getOutParams().remove(var4.getWsMethod().getReturnType().getName());
                     }
                  } catch (JAXRPCException var70) {
                     var3 = new RemoteException(var70.getMessage(), var70.getLinkedCause());
                  } catch (SOAPFaultException var71) {
                     var3 = new RemoteException("SOAP Fault:" + var71 + "--- Detail:" + var71.getDetail(), var71);
                  } catch (RuntimeException var72) {
                     var3 = new RemoteException(var72.getMessage(), var72);
                  } catch (Throwable var73) {
                     var3 = var73;
                  } finally {
                     var75.setContextClassLoader(var13);
                  }
               } else {
                  if (AsyncResponseHandler.verbose) {
                     Verbose.log((Object)("Calculating async response using new ClientDispatcher/Stub for message " + this.requestMsgIdMsg + " related to request msg " + this.relatesTo + this.rmSeqIdMsg));
                  }

                  try {
                     var75.setContextClassLoader(var11);
                     var14 = (String)var5.getProperty("weblogic.wsee.service.class.name");
                     if (var14 == null) {
                        throw new JAXRPCException("No class name found");
                     }

                     String var15 = (String)var5.getProperty("weblogic.wsee.service.method.name");
                     if (var15 == null) {
                        throw new JAXRPCException("No method name found");
                     }

                     String var16 = (String)var5.getProperty("weblogic.wsee.operation.name");
                     if (var16 == null) {
                        throw new JAXRPCException("No operation name found");
                     }

                     Stub var17;
                     try {
                        Class var18 = Thread.currentThread().getContextClassLoader().loadClass(var14);

                        Object var19;
                        try {
                           Constructor var20 = var18.getConstructor();
                           var19 = var20.newInstance();
                        } catch (Throwable var64) {
                           String var21 = (String)var5.getProperty("weblogic.wsee.wsdl.location");
                           if (var21 == null) {
                              throw var64;
                           }

                           Constructor var22 = var18.getConstructor(String.class);
                           var19 = var22.newInstance(var21);
                        }

                        Method var80 = var18.getMethod(var15);
                        var17 = (Stub)var80.invoke(var19);
                     } catch (ClassNotFoundException var65) {
                        throw new JAXRPCException(var65);
                     } catch (NoSuchMethodException var66) {
                        throw new JAXRPCException(var66);
                     } catch (Throwable var67) {
                        throw new JAXRPCException(var67);
                     }

                     try {
                        ((SoapMessageContext)var5).setMessage(var1.getMessage());
                        var2 = ((StubImpl)var17)._asyncResponse(var16, var1.getMessage(), var5);
                     } catch (JAXRPCException var60) {
                        var3 = new RemoteException(var60.getMessage(), var60.getLinkedCause());
                     } catch (SOAPFaultException var61) {
                        var3 = new RemoteException("SOAP Fault:" + var61 + "--- Detail:" + var61.getDetail(), var61);
                     } catch (RuntimeException var62) {
                        var3 = new RemoteException(var62.getMessage(), var62);
                     } catch (Throwable var63) {
                        var3 = var63;
                     }
                  } finally {
                     var75.setContextClassLoader(var13);
                  }
               }

               if (AsyncResponseHandler.verbose) {
                  StringBuffer var77 = HandlerIterator.getHandlerHistory(var5);
                  if (var77 != null) {
                     Verbose.log((Object)("Async response message " + this.requestMsgIdMsg + " related to request msg " + this.relatesTo + this.rmSeqIdMsg + " has this *client* handler history: " + var77));
                  }
               }

               this.mc.setProperty("weblogic.wsee.async.invoke.state", this.ais);
               if (var3 != null) {
                  if (AsyncResponseHandler.verbose) {
                     var14 = var3.toString();
                     if (var3 instanceof SOAPMessage) {
                        try {
                           SOAPFault var79 = ((SOAPMessage)var3).getSOAPBody().getFault();
                           var14 = var79.getFaultString();
                        } catch (Exception var59) {
                           Verbose.logException(var59);
                        }
                     }

                     Verbose.log((Object)("Calculated FAULT async response for message " + this.requestMsgIdMsg + " related to request msg " + this.relatesTo + this.rmSeqIdMsg + ": " + var14));
                  }

                  this.mc.setProperty("weblogic.wsee.async.fault", var3);
               } else {
                  if (AsyncResponseHandler.verbose) {
                     Verbose.log((Object)("Calculated normal async response for message " + this.requestMsgIdMsg + " related to request msg " + this.relatesTo + this.rmSeqIdMsg + ": " + var2));
                  }

                  this.mc.setProperty("weblogic.wsee.async.result", var2);
               }

               if (AsyncUtil.isSoap12(this.mc)) {
                  this.mc.setProperty("weblogic.wsee.ws.server.OperationName", new QName("http://www.bea.com/async/AsyncResponseServiceSoap12", "onAsyncDelivery"));
               } else {
                  this.mc.setProperty("weblogic.wsee.ws.server.OperationName", new QName("http://www.bea.com/async/AsyncResponseService", "onAsyncDelivery"));
               }

               return null;
            }
         }
      }
   }
}
