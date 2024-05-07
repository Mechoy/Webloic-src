package weblogic.wsee.security.wst.framework.async;

import com.sun.xml.ws.api.WSBinding;
import com.sun.xml.ws.api.WSService;
import com.sun.xml.ws.api.addressing.WSEndpointReference;
import com.sun.xml.ws.developer.MemberSubmissionAddressingFeature;
import com.sun.xml.ws.developer.MemberSubmissionEndpointReference;
import java.net.MalformedURLException;
import java.security.AccessController;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.soap.MimeHeaders;
import javax.xml.ws.Endpoint;
import javax.xml.ws.EndpointReference;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.wsaddressing.W3CEndpointReference;
import org.w3c.dom.Element;
import weblogic.jws.jaxws.client.async.AsyncClientTransportFeature;
import weblogic.kernel.KernelStatus;
import weblogic.management.configuration.SSLMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.utils.StringUtils;
import weblogic.wsee.mc.api.McFeature;
import weblogic.wsee.message.WlMessageContext;
import weblogic.wsee.util.EndpointAddressUtil;
import weblogic.wsee.wsdl.WsdlPort;

public class AsyncTrustClientHelper {
   public static boolean isAsyncTrustRequired(MessageContext var0) {
      String var1 = (String)var0.getProperty("weblogic.wsee.security.wst.enforceAsyncTrustExchange");
      String var2 = (String)var0.getProperty("weblogic.wsee.security.wst.originalTargetEndpointAddress");
      String var3 = (String)var0.getProperty("javax.xml.rpc.service.endpoint.address");
      if (var2 != null && var3 != null) {
         int var4 = var2.indexOf(58);
         if (var4 > -1) {
            var2 = var2.substring(var4);
         }

         var4 = var3.indexOf(58);
         if (var4 > -1) {
            var3 = var3.substring(var4);
         }

         if (!var2.equals(var3) && !"true".equalsIgnoreCase(var1)) {
            return false;
         }
      }

      return isJAXWS(var0) && isAsyncClientTransportFeatureEnabled(var0);
   }

   public static boolean isAsyncClientTransportFeatureEnabled(MessageContext var0) {
      return (WebServiceFeature)var0.getProperty("weblogic.wsee.jaxws.framework.jaxrpc.SOAPMessageContext.ASYNC_CLIENT_FEATURE") instanceof AsyncClientTransportFeature;
   }

   public static boolean isMcFeatureEnabled(MessageContext var0) {
      return (WebServiceFeature)var0.getProperty("weblogic.wsee.jaxws.framework.jaxrpc.SOAPMessageContext.ASYNC_CLIENT_FEATURE") instanceof McFeature;
   }

   public static WebServiceFeature getAsyncWebServiceFeature(MessageContext var0) {
      return (WebServiceFeature)var0.getProperty("weblogic.wsee.jaxws.framework.jaxrpc.SOAPMessageContext.ASYNC_CLIENT_FEATURE");
   }

   public static boolean isJAXWS(MessageContext var0) {
      return "true".equalsIgnoreCase((String)var0.getProperty("weblogic.wsee.jaxws.framework.jaxrpc.SOAPMessageContext.JAX_WS_RUNTIME"));
   }

   public static WsdlPort getWsdlPort(MessageContext var0) {
      try {
         WlMessageContext var1 = WlMessageContext.narrow(var0);
         return var1.getDispatcher().getWsdlPort();
      } catch (IllegalArgumentException var2) {
         return null;
      }
   }

   public static WSService getWSService(MessageContext var0) {
      return (WSService)var0.getProperty("weblogic.wsee.jaxws.framework.jaxrpc.SOAPMessageContext.SERVICE");
   }

   public static String getBindingID(String var0, String var1) {
      if ("http".equals(var0) || "https".equals(var0)) {
         if ("SOAP11".equals(var1)) {
            return "http://schemas.xmlsoap.org/wsdl/soap/http";
         }

         if ("SOAP12".equals(var0)) {
            return "http://www.w3.org/2003/05/soap/bindings/HTTP/";
         }
      }

      return null;
   }

   public static AsyncClientTransportFeature getAsyncClientTransportFeatureOnTrust(MessageContext var0, String var1) throws MalformedURLException {
      AsyncClientTransportFeature var2 = (AsyncClientTransportFeature)getAsyncWebServiceFeature(var0);
      Endpoint var3 = var2.getEndpoint();
      WSBinding var4 = (WSBinding)var3.getBinding();
      boolean var5 = var4.isFeatureEnabled(MemberSubmissionAddressingFeature.class);
      Class var6 = var5 ? MemberSubmissionEndpointReference.class : W3CEndpointReference.class;
      EndpointReference var7 = var3.getEndpointReference(var6, new Element[0]);
      String var8 = (new WSEndpointReference(var7)).getAddress();
      if (var8 != null && !var8.equals("") && var1 != null && !var1.equals("") && !EndpointAddressUtil.getProtocolFromEndpointAddress(var8).equals(var1) && var1.equals("https")) {
         if (!KernelStatus.isServer()) {
            throw new MalformedURLException("Policy on STS endpoint requires " + var1 + " transport, but the endpoint address url of async callback service specified on client side doesn't comply with it :" + var8);
         } else {
            AuthenticatedSubject var9 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
            ServerMBean var10 = ManagementService.getRuntimeAccess(var9).getServer();
            String var11 = var10.getListenAddress();
            SSLMBean var12 = var10.getSSL();
            int var13 = var12.getListenPort();
            String var14 = var8.substring(var8.indexOf(47, var8.indexOf("://") + 3));
            StringBuffer var15 = new StringBuffer();
            var15.append(var1).append("://").append(var11).append(":").append(var13).append(var14);
            var8 = var15.toString();
            String var16 = UUID.randomUUID().toString();
            if (!var8.endsWith("/") && !var8.endsWith("\\")) {
               var8 = var8 + "/TrustCallbackService-" + var16;
            } else {
               var8 = var8 + "TrustCallbackService-" + var16;
            }

            return new AsyncClientTransportFeature(var8);
         }
      } else {
         return var2;
      }
   }

   public static void setupCookies(MessageContext var0, MimeHeaders var1) {
      if (var0.getProperty("weblogic.wsee.transport.headers") != null) {
         var0.setProperty("weblogic.wsee.transport.headers", var1);
      }

      setSessionCookies(var0, var1);
   }

   private static void setSessionCookies(MessageContext var0, MimeHeaders var1) {
      Object var2 = var0.getProperty("javax.xml.rpc.session.maintain");
      if (var2 != null) {
         if (!(var2 instanceof Boolean)) {
            throw new IllegalArgumentException("Value of javax.xml.rpc.session.maintain must be java.lang.Boolean");
         }

         if ((Boolean)var2) {
            String[] var3 = var1.getHeader("Set-Cookie");
            if (var3 != null) {
               HashMap var4 = new HashMap();
               String[] var5 = var3;
               int var6 = var3.length;

               String var8;
               for(int var7 = 0; var7 < var6; ++var7) {
                  var8 = var5[var7];
                  addCookies(cleanupCookie(var8), var4);
               }

               boolean var10 = !var4.isEmpty();
               MimeHeaders var11 = mergeMimeHeaderCookies(var4, var0);
               if (!var4.isEmpty() || var10) {
                  Iterator var12 = var4.keySet().iterator();

                  while(var12.hasNext()) {
                     var8 = (String)var12.next();
                     String var9 = (String)var4.get(var8);
                     var11.addHeader("Cookie", var8 + "=" + var9);
                  }
               }
            }
         }
      }

   }

   private static void addCookies(String var0, Map<String, String> var1) {
      String[] var2 = StringUtils.splitCompletely(var0, ";");
      String[] var3 = var2;
      int var4 = var2.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         String var6 = var3[var5];
         int var7 = var6.indexOf("=");
         if (var7 != -1 && var7 < var6.length() - 1) {
            String var8 = var6.substring(0, var7).trim();
            String var9 = var6.substring(var7 + 1, var6.length()).trim();
            var1.put(var8, var9);
         }
      }

   }

   private static String cleanupCookie(String var0) {
      var0 = var0.trim();
      int var1 = var0.indexOf(59);
      if (var1 != -1) {
         var0 = var0.substring(0, var1);
      }

      return var0;
   }

   private static MimeHeaders mergeMimeHeaderCookies(Map<String, String> var0, MessageContext var1) {
      Object var2 = (Map)var1.getProperty("weblogic.wsee.invoke_properties");
      if (var2 == null) {
         var2 = new HashMap();
         var1.setProperty("weblogic.wsee.invoke_properties", var2);
      }

      MimeHeaders var3 = (MimeHeaders)((Map)var2).get("weblogic.wsee.transport.headers");
      if (var3 == null) {
         var3 = new MimeHeaders();
         ((Map)var2).put("weblogic.wsee.transport.headers", var3);
      } else {
         String[] var4 = var3.getHeader("Cookie");
         if (var4 != null) {
            String[] var5 = var4;
            int var6 = var4.length;

            for(int var7 = 0; var7 < var6; ++var7) {
               String var8 = var5[var7];
               addCookies(var8, var0);
            }

            var3.removeHeader("Cookie");
         }
      }

      return var3;
   }
}
