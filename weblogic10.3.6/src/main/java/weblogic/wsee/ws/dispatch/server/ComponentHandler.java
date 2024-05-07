package weblogic.wsee.ws.dispatch.server;

import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.Map;
import javax.ejb.EJBException;
import javax.xml.namespace.QName;
import javax.xml.rpc.JAXRPCException;
import javax.xml.rpc.handler.GenericHandler;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.rpc.soap.SOAPFaultException;
import weblogic.wsee.addressing.EndpointReference;
import weblogic.wsee.buffer.BufferHelper;
import weblogic.wsee.component.Component;
import weblogic.wsee.component.ComponentException;
import weblogic.wsee.handler.InvocationException;
import weblogic.wsee.jws.RetryException;
import weblogic.wsee.jws.wlw.SoapFaultException;
import weblogic.wsee.message.WlMessageContext;
import weblogic.wsee.util.HolderUtil;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.ws.WsMethod;
import weblogic.wsee.ws.WsMethodImpl;
import weblogic.wsee.ws.WsParameterType;
import weblogic.wsee.ws.WsReturnType;
import weblogic.wsee.ws.WsSkel;
import weblogic.wsee.ws.dispatch.Dispatcher;
import weblogic.wsee.ws.dispatch.DispatcherImpl;

public class ComponentHandler extends GenericHandler {
   private static final boolean verbose = Verbose.isVerbose(ComponentHandler.class);
   public static final String APP_EXCEPTION = "weblogic.wsee.component.AppException";
   private static final boolean dumpException = Boolean.getBoolean("weblogic.wsee.component.exception");

   public boolean handleRequest(MessageContext var1) {
      WlMessageContext var2 = WlMessageContext.narrow(var1);
      Dispatcher var3 = var2.getDispatcher();
      String var4 = var3.getWsMethod().getOperationName().getLocalPart();
      Map var5 = var3.getInParams();
      Component var6 = ((WsSkel)var3.getWsPort().getEndpoint()).getComponent();

      try {
         Object[] var7 = ((WsMethodImpl)var3.getWsMethod()).getMethodArgs(var5);
         if (verbose) {
            Verbose.log((Object)("Going to invoke method: " + var4));
         }

         EndpointReference var13 = (EndpointReference)var1.getProperty("weblogic.wsee.addressing.ServerEndpoint");
         DispatcherImpl.ServiceContext.set(var13);
         DispatcherImpl.ServiceContext.propagatePropertiesToClient(var2);
         Object var14 = var6.invoke(var4, var7, var1);
         WsReturnType var15 = var3.getWsMethod().getReturnType();
         if (var15 != null) {
            var3.getOutParams().put(var15.getName(), var14);
         }

         this.putHolderValues(var3.getWsMethod(), var5, var3.getOutParams());
         return true;
      } catch (ComponentException var11) {
         Throwable var8 = var11.getCause();
         Throwable var9 = var8;
         if (var8 instanceof InvocationTargetException) {
            var9 = ((InvocationTargetException)var8).getTargetException();
         }

         if (var9 != null) {
            var2.setProperty("weblogic.wsee.component.AppException", var9);
            if (var9 instanceof RetryException && BufferHelper.isRuntimeRetryExceptionEnabled(var1)) {
               if (verbose) {
                  Verbose.say("Throw RetryException");
               }

               throw (RetryException)var9;
            }
         }

         if (var9 instanceof SoapFaultException) {
            var2.setProperty("weblogic.wsee.service_specific_exception", var9);
            return true;
         } else {
            if (var9 instanceof EJBException) {
               Exception var10 = ((EJBException)var9).getCausedByException();
               if (var10 instanceof SOAPFaultException) {
                  throw (SOAPFaultException)var10;
               }
            }

            if (var9 instanceof SOAPFaultException) {
               throw (SOAPFaultException)var9;
            } else {
               if (verbose || dumpException) {
                  Verbose.log("\n Error invoking " + var6 + ": " + var9.getClass().getName(), var9);
               }

               if (this.isAppDefinedException(var9)) {
                  var2.setProperty("weblogic.wsee.service_specific_exception", var9);
                  return true;
               } else {
                  throw new InvocationException("Failed to invoke end component " + var6 + ", operation=" + var4, var11);
               }
            }
         }
      } catch (Throwable var12) {
         throw new JAXRPCException("ComponentHandler request failed", var12);
      }
   }

   private boolean isAppDefinedException(Throwable var1) {
      if (var1 instanceof RuntimeException) {
         return false;
      } else if (var1 instanceof RemoteException) {
         return false;
      } else if (!(var1 instanceof Exception)) {
         return false;
      } else if (var1.getClass().getName().equals(Exception.class.getName())) {
         return false;
      } else {
         return !var1.getClass().getName().startsWith("java.sql");
      }
   }

   private void putHolderValues(WsMethod var1, Map var2, Map var3) {
      Iterator var4 = var1.getParameters();

      while(true) {
         WsParameterType var5;
         do {
            if (!var4.hasNext()) {
               return;
            }

            var5 = (WsParameterType)var4.next();
         } while(var5.getMode() != 1 && var5.getMode() != 2);

         Object var6 = var2.get(var5.getName());
         var3.put(var5.getName(), HolderUtil.getHolderValue(var6));
      }
   }

   public QName[] getHeaders() {
      return new QName[0];
   }
}
