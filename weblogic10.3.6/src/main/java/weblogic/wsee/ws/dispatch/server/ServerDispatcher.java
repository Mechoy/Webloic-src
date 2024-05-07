package weblogic.wsee.ws.dispatch.server;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.xml.rpc.handler.soap.SOAPMessageContext;
import javax.xml.rpc.soap.SOAPFaultException;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPFault;
import weblogic.diagnostics.instrumentation.DelegatingMonitor;
import weblogic.diagnostics.instrumentation.DiagnosticAction;
import weblogic.diagnostics.instrumentation.DiagnosticActionState;
import weblogic.diagnostics.instrumentation.InstrumentationSupport;
import weblogic.diagnostics.instrumentation.JoinPoint;
import weblogic.wsee.codec.CodecException;
import weblogic.wsee.codec.CodecFactory;
import weblogic.wsee.handler.HandlerIterator;
import weblogic.wsee.handler.InvocationException;
import weblogic.wsee.jws.RetryException;
import weblogic.wsee.jws.container.ServerResponsePathDispatcher;
import weblogic.wsee.message.WlMessageContext;
import weblogic.wsee.monitoring.OperationStats;
import weblogic.wsee.util.ToStringWriter;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.ws.WsException;
import weblogic.wsee.ws.WsMethod;
import weblogic.wsee.ws.dispatch.DispatcherImpl;

public class ServerDispatcher extends DispatcherImpl {
   public static final String ABORT_REQUEST_ON_FAULT = "weblogic.wsee.ws.dispatch.server.AbortRequestOnFault";
   private static final boolean verbose;
   static final long serialVersionUID = 6505747173496609555L;
   public static final String _WLDF$INST_VERSION = "9.0.0";
   // $FF: synthetic field
   static Class _WLDF$INST_FLD_class = Class.forName("weblogic.wsee.ws.dispatch.server.ServerDispatcher");
   public static final DelegatingMonitor _WLDF$INST_FLD_Webservices_JAXRPC_Diagnostic_Dispatch_Action_Around_Low;
   public static final JoinPoint _WLDF$INST_JPFLD_0;

   public ServerDispatcher() {
      this.setInParams(new LinkedHashMap());
      this.setOutParams(new LinkedHashMap());
   }

   public ServerDispatcher(WlMessageContext var1) {
      this();
      this.setContext(var1);
   }

   public void dispatch() throws WsException {
      boolean var21;
      boolean var10000 = var21 = _WLDF$INST_FLD_Webservices_JAXRPC_Diagnostic_Dispatch_Action_Around_Low.isEnabledAndNotDyeFiltered();
      DiagnosticAction[] var22 = null;
      DiagnosticActionState[] var23 = null;
      Object var20 = null;
      if (var10000) {
         JoinPoint var38 = _WLDF$INST_JPFLD_0;
         DelegatingMonitor var10001 = _WLDF$INST_FLD_Webservices_JAXRPC_Diagnostic_Dispatch_Action_Around_Low;
         DiagnosticAction[] var10002 = var22 = var10001.getActions();
         InstrumentationSupport.preProcess(var38, var10001, var10002, var23 = InstrumentationSupport.getActionStates(var10002));
      }

      try {
         long var1 = System.nanoTime();
         CodecFactory var3 = CodecFactory.instance();

         try {
            this.setCodec(var3.getCodec(this.getWsdlPort().getBinding()));
         } catch (CodecException var30) {
            throw new WsException("Unable to dispatch", var30);
         }

         if (this.getContext() == null) {
            this.setContext(WlMessageContext.narrow(this.getCodec().createContext()));
            this.getContext().setDispatcher(this);
         } else {
            this.getContext().setDispatcher(this);
            if (this.getContext().getProperty("weblogic.wsee.ws.server.OperationName") != null) {
               (new OperationLookupHandler()).handleRequest(this.getContext());
            }
         }

         this.setHandlerChain(new HandlerIterator(this.getWsPort().getInternalHandlerList()));
         long var4 = System.nanoTime();
         int var6 = 0;
         Integer var7 = (Integer)this.getContext().getProperty("weblogic.wsee.handler.index");
         if (var7 != null) {
            var6 = var7 + 1;
         }

         this.getHandlerChain().handleRequest(this.getContext(), var6);
         if (this.getContext().containsProperty("weblogic.wsee.ws.dispatch.server.AbortRequestOnFault") && this.getContext().hasFault()) {
            if (verbose) {
               Verbose.say("Request generated a fault, and we've got ABORT_REQUEST_ON_FAULT set on the context. Aborting this request, no response path will be dispatched");
            }

            Throwable var8 = this.getContext().getFault();
            if (var8 != null) {
               throw new WsException(var8.toString(), var8);
            }

            if (this.getContext() instanceof SOAPMessageContext) {
               SOAPMessageContext var9 = (SOAPMessageContext)this.getContext();
               SOAPFault var10 = null;

               try {
                  SOAPBody var11 = var9.getMessage().getSOAPBody();
                  if (var11.hasFault()) {
                     var10 = var11.getFault();
                  }
               } catch (Exception var29) {
               }

               if (var10 != null) {
                  SOAPFaultException var36 = new SOAPFaultException(var10.getFaultCodeAsQName(), var10.getFaultString(), var10.getFaultActor(), var10.getDetail());
                  var36.fillInStackTrace();
                  throw new WsException(var36.toString(), var36);
               }
            }
         }

         long var32 = System.nanoTime();
         if (this.getContext().getProperty("weblogic.wsee.cluster.routed") == null && this.getContext().getProperty("weblogic.wsee.util.VersionRedirectUtil.redirected") == null) {
            if (this.getOperation() == null) {
               if (this.getContext().getProperty("weblogic.wsee.oneway.confirmed") != null) {
                  this.getHandlerChain().handleClosure(this.getContext());
               } else if (this.getContext().getProperty("weblogic.jws.AsyncTransactionalInvoke") != null) {
                  ServerResponsePathDispatcher.saveContextForResponsePath(this.getContext());
               } else {
                  this.getHandlerChain().handleResponse(this.getContext());
               }
            } else if (this.getContext().getProperty("weblogic.jws.AsyncTransactionalInvoke") != null && !this.getContext().hasFault()) {
               ServerResponsePathDispatcher.saveContextForResponsePath(this.getContext());
            } else {
               this.callHandleResponse();
            }
         }

         WsMethod var33 = this.getWsMethod();
         if (var33 != null) {
            OperationStats var34 = var33.getStats();
            if (var34 != null) {
               var34.reportInvocation(var4 - var1, var32 - var4, System.nanoTime() - var32);
               if (this.getContext().hasFault()) {
                  Throwable var12 = this.getContext().getFault();
                  var34.reportResponseError(var12);
                  if (var12 instanceof RetryException) {
                     throw new WsException("RetryException", var12);
                  }
               }
            }
         }

         if (this.getContext().getFault() != null) {
            Throwable var35 = this.getContext().getFault();
            if (this.getOperation() != null) {
               switch (this.getOperation().getType()) {
                  case 1:
                     if (verbose) {
                        Verbose.logException(var35);
                     }
                     break;
                  default:
                     if (this.getContext() instanceof SOAPMessageContext) {
                        SOAPMessageContext var37 = (SOAPMessageContext)this.getContext();
                        SOAPBody var13 = null;

                        try {
                           var13 = var37.getMessage().getSOAPBody();
                        } catch (Exception var28) {
                        }

                        if ((var37.getMessage() == null || var13 == null || !var13.hasFault()) && verbose) {
                           Verbose.logException(var35);
                        }
                     }
               }
            } else if (verbose) {
               Verbose.logException(var35);
            }
         }
      } finally {
         if (var21) {
            InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_0, _WLDF$INST_FLD_Webservices_JAXRPC_Diagnostic_Dispatch_Action_Around_Low, var22, var23);
         }

      }

   }

   public void dispatchResponsePath() throws WsException {
      long var1 = System.nanoTime();
      CodecFactory var3 = CodecFactory.instance();

      try {
         this.setCodec(var3.getCodec(this.getWsdlPort().getBinding()));
      } catch (CodecException var13) {
         throw new WsException("Unable to dispatch", var13);
      }

      if (this.getContext() == null) {
         throw new WsException("No WLMessageContext in dispatchResponsePath. This context should have been saved during the request path, and used to construct the dispatcher for the response path");
      } else {
         this.getContext().setDispatcher(this);
         if (this.getContext().getProperty("weblogic.wsee.ws.server.OperationName") != null) {
            (new OperationLookupHandler()).handleRequest(this.getContext());
         }

         Integer var5 = (Integer)this.getContext().getProperty("weblogic.wsee.handler.index");
         int var4 = var5 != null ? var5 : 0;
         HandlerIterator var6 = new HandlerIterator(this.getWsPort().getInternalHandlerList());
         this.setHandlerChain(var6);
         var6.setIndex(var4);
         long var7 = System.nanoTime();
         long var9 = System.nanoTime();
         if (this.getContext().getProperty("weblogic.wsee.cluster.routed") == null && this.getContext().getProperty("weblogic.wsee.util.VersionRedirectUtil.redirected") == null) {
            if (this.getOperation() == null) {
               if (this.getContext().getProperty("weblogic.wsee.oneway.confirmed") != null) {
                  this.getHandlerChain().handleClosure(this.getContext());
               } else {
                  this.getHandlerChain().handleAsyncResponse(this.getContext());
               }
            } else {
               this.callHandleResponse();
            }
         }

         WsMethod var11 = this.getWsMethod();
         if (var11 != null) {
            OperationStats var12 = var11.getStats();
            if (var12 != null) {
               var12.reportInvocation(var7 - var1, var9 - var7, System.nanoTime() - var9);
               if (this.getContext().hasFault()) {
                  var12.reportResponseError(this.getContext().getFault());
               }
            }
         }

      }
   }

   private void callHandleResponse() {
      switch (this.getOperation().getType()) {
         case 0:
         case 2:
            this.getHandlerChain().handleResponse(this.getContext());
            break;
         case 1:
            if (this.getContext().getProperty("weblogic.wsee.reliable.oneway.reply") == null) {
               if (this.getContext().hasFault() && this.getContext().getProperty("weblogic.wsee.oneway.confirmed") == null) {
                  this.getHandlerChain().handleResponse(this.getContext());
                  return;
               }

               if (this.getContext().getProperty("weblogic.wsee.oneway.confirmed") == null) {
                  try {
                     this.getContext().getDispatcher().getConnection().getTransport().confirmOneway();
                  } catch (IOException var2) {
                     throw new InvocationException("Failed to confirm oneway", var2);
                  }

                  this.getContext().setProperty("weblogic.wsee.oneway.confirmed", "true");
               }
            }

            this.getHandlerChain().handleClosure(this.getContext());
         case 3:
      }

   }

   public String toString() {
      ToStringWriter var1 = new ToStringWriter();
      this.toString(var1);
      return var1.toString();
   }

   public void toString(ToStringWriter var1) {
      var1.start(this);
      var1.end();
   }

   static {
      _WLDF$INST_FLD_Webservices_JAXRPC_Diagnostic_Dispatch_Action_Around_Low = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "Webservices_JAXRPC_Diagnostic_Dispatch_Action_Around_Low");
      _WLDF$INST_JPFLD_0 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "ServerDispatcher.java", "weblogic.wsee.ws.dispatch.server.ServerDispatcher", "dispatch", "()V", 77, (Map)null, (boolean)0);
      verbose = Verbose.isVerbose(ServerDispatcher.class);
   }
}
