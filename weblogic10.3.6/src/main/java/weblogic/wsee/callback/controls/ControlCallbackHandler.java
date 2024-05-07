package weblogic.wsee.callback.controls;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import javax.xml.namespace.QName;
import javax.xml.rpc.JAXRPCException;
import javax.xml.rpc.handler.GenericHandler;
import javax.xml.rpc.handler.HandlerInfo;
import javax.xml.rpc.handler.MessageContext;
import org.w3c.dom.Element;
import weblogic.wsee.deploy.WsdlAddressInfo;
import weblogic.wsee.jws.container.Container;
import weblogic.wsee.jws.container.ContainerFactory;
import weblogic.wsee.message.WlMessageContext;
import weblogic.wsee.util.ControlAPIUtil;
import weblogic.wsee.ws.WsPort;
import weblogic.wsee.ws.WsRegistry;
import weblogic.wsee.wsdl.WsdlPort;

public class ControlCallbackHandler extends GenericHandler {
   private static final String CONTEXT_KEY = "weblogic.controls.jws.ControlContainerContext";
   private static final String DATA_CLASS = "com.bea.control.SerializedControlData";
   String contextPath = null;

   public QName[] getHeaders() {
      return new QName[0];
   }

   public void init(HandlerInfo var1) {
      Map var2 = var1.getHandlerConfig();
      if (var2 != null) {
         this.contextPath = (String)var2.get("control.callback.context.path");
      }

   }

   public boolean handleRequest(MessageContext var1) {
      WlMessageContext var2 = WlMessageContext.narrow(var1);
      if (this.contextPath != null) {
         var2.setProperty("control.callback.context.path", this.contextPath);
      }

      ControlCallbackData var3 = (ControlCallbackData)var2.getProperty("weblogic.wsee.callback.controls.ControlCallbackData");
      if (var3 != null) {
         Class var4 = null;
         Method var5 = null;
         Object var6 = null;
         Method var7 = null;
         Method var8 = null;
         Class var9 = null;
         Constructor var10 = null;
         Constructor var11 = null;

         Class[] var13;
         try {
            ClassLoader var12 = Thread.currentThread().getContextClassLoader();
            var4 = var12.loadClass("weblogic.controls.jws.JwsControlContainerContext");
            var13 = new Class[]{WlMessageContext.class};
            var5 = var4.getMethod("beginContext", var13);
            var13 = new Class[]{var12.loadClass("org.apache.beehive.controls.api.context.ControlHandle"), var12.loadClass("org.apache.beehive.controls.api.events.EventRef"), Object[].class};
            var7 = var4.getMethod("dispatchEvent", var13);
            var13 = new Class[0];
            var8 = var4.getMethod("endContext", var13);
            var9 = var12.loadClass("weblogic.controls.container.ResourceUnavailableException");
            var10 = var9.getConstructor(String.class);
            var11 = var9.getConstructor(String.class, Throwable.class);
         } catch (ClassNotFoundException var49) {
            var2.setProperty("weblogic.wsee.local.invoke.throwable", var49);
            return false;
         } catch (NoSuchMethodException var50) {
            var2.setProperty("weblogic.wsee.local.invoke.throwable", var50);
            return false;
         }

         Serializable var55 = null;
         var13 = null;

         try {
            WsPort var14 = WsRegistry.instance().lookup(var3.getServiceUri());
            if (var14 == null) {
               throw new JAXRPCException("No port found for " + var3.getServiceUri());
            }

            if (var2.getProperty("weblogic.wsee.connection.end_point_address") == null) {
               WsdlPort var15 = var14.getWsdlPort();
               if (var15 != null) {
                  WsdlAddressInfo.PortAddress var16 = var14.getWsdlPort().getPortAddress();
                  if (var16 != null) {
                     String var17 = var14.getWsdlPort().getPortAddress().getProtocol() + "://" + var14.getWsdlPort().getPortAddress().getHost() + ":" + var14.getWsdlPort().getPortAddress().getListenPort() + var14.getWsdlPort().getPortAddress().getServiceuri();
                     var2.setProperty("weblogic.wsee.connection.end_point_address", var17);
                  }
               }
            }

            Container var57 = ContainerFactory.getContainer(var2, var14.getEndpoint().getJwsClass());
            var55 = var57.getProperty("weblogic.controls.jws.ControlContainerContext");
            if (var55 == null) {
               Throwable var59 = (Throwable)var10.newInstance("Unable to obtain ControlContainerContext");
               throw new InvocationTargetException(var59);
            }

            Object var56 = (MessageContext)var2.getProperty("weblogic.wsee.local.transport.prior.context");
            if (var56 == null) {
               var56 = var2;
            }

            Element[] var58 = (Element[])((Element[])var2.getProperty("control.callback.input.headers"));
            if (var58 != null && var58.length > 0) {
               ControlAPIUtil.setInputHeaders((MessageContext)var56, var58);
            }

            ControlAPIUtil.setURI(var2, var3.getServiceUri());
            Object[] var60 = new Object[]{var2};
            var5.invoke(var55, var60);
            Object[] var18 = var3.getArgs();
            if (var18 != null) {
               for(int var19 = 0; var19 < var18.length; ++var19) {
                  Object var20 = var18[var19];
                  if (var20 != null) {
                     Class var21 = var20.getClass();
                     if (var21.getName().equals("com.bea.control.SerializedControlData")) {
                        Throwable var23;
                        try {
                           ClassLoader var22 = Thread.currentThread().getContextClassLoader();
                           Class var63 = var22.loadClass("com.bea.control.SerializedControlData");
                           Method var24 = var63.getMethod("getData", ClassLoader.class);
                           var18[var19] = var24.invoke(var20, var22);
                        } catch (ClassNotFoundException var47) {
                           var23 = (Throwable)var11.newInstance("Unable to unmarshall callback data.", var47);
                           throw new InvocationTargetException(var23);
                        } catch (NoSuchMethodException var48) {
                           var23 = (Throwable)var11.newInstance("Unable to unmarshall callback data.", var48);
                           throw new InvocationTargetException(var23);
                        }
                     }
                  }
               }
            }

            var60 = new Object[]{var3.getControlHandle(), var3.getEventRef(), var18};
            var6 = var7.invoke(var55, var60);
            HashMap var61 = new HashMap();
            if (var6 != null) {
               var61.put("control.callback.return.value", var6);
            }

            Element[] var62 = ControlAPIUtil.getOutputHeaders((MessageContext)var56);
            if (var62 != null && var62.length > 0) {
               var61.put("control.callback.output.headers", var62);
            }

            if (var61.size() > 0) {
               var2.setProperty("weblogic.wsee.local.invoke.response", var61);
            }
         } catch (InvocationTargetException var51) {
            var2.setProperty("weblogic.wsee.local.invoke.throwable", var51);
         } catch (IllegalAccessException var52) {
            var2.setProperty("weblogic.wsee.local.invoke.throwable", var52);
         } catch (Exception var53) {
            var2.setProperty("weblogic.wsee.local.invoke.throwable", var53);
         } finally {
            if (var55 != null) {
               try {
                  var8.invoke(var55);
               } catch (InvocationTargetException var45) {
               } catch (IllegalAccessException var46) {
               }
            }

         }

         return false;
      } else {
         return true;
      }
   }

   public boolean handleResponse(MessageContext var1) {
      ControlCallbackData var2 = (ControlCallbackData)var1.getProperty("weblogic.wsee.callback.controls.ControlCallbackData");
      return var2 == null;
   }
}
