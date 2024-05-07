package weblogic.wsee.server.jms;

import java.util.Iterator;
import java.util.Map;
import weblogic.jws.WLJmsTransport;
import weblogic.wsee.server.ServerUtil;
import weblogic.wsee.util.JmsUtil;
import weblogic.wsee.util.PathUtil;
import weblogic.wsee.util.StringUtil;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.ws.WsPort;
import weblogic.wsee.ws.WsService;
import weblogic.wsee.ws.init.WsDeploymentContext;
import weblogic.wsee.ws.init.WsDeploymentException;
import weblogic.wsee.ws.init.WsDeploymentListener;
import weblogic.wsee.wsdl.WsdlUtils;
import weblogic.wsee.wsdl.soap11.SoapAddress;

public class JmsDeploymentListener implements WsDeploymentListener {
   private static final boolean verbose = Verbose.isVerbose(JmsDeploymentListener.class);
   public static final String CALLBACK_QUEUE_TOKEN = "@weblogic.wsee.CallbackQueue@";

   public void process(WsDeploymentContext var1) throws WsDeploymentException {
      WsService var2 = var1.getWsService();
      if (verbose) {
         Verbose.log((Object)("Looking for JMS transport in service: " + var2.getWsdlService().getName()));
      }

      Iterator var3 = var2.getPorts();

      while(var3.hasNext()) {
         WsPort var4 = (WsPort)var3.next();
         Class var5 = var4.getEndpoint().getJwsClass();
         if (verbose) {
            Verbose.log((Object)("Looking for JMS transport in port: " + var4.getWsdlPort().getName() + ", class=" + var5));
         }

         if (var5 != null) {
            boolean var6 = false;
            SoapAddress var7 = WsdlUtils.getSoapAddress(var4.getWsdlPort());
            if (verbose) {
               Verbose.log((Object)("Calculated SoapAddress: " + var7));
            }

            if (var7 != null) {
               String var8 = var7.getLocation();
               int var9;
               if ((var9 = var8.indexOf("://")) > -1) {
                  String var10 = var8.substring(0, var9);
                  if (var10.equalsIgnoreCase("jms")) {
                     jmsListenerDeploymentHelper(var8, true);
                     var6 = true;
                  }
               }
            }

            if (!var6) {
               WLJmsTransport var11 = (WLJmsTransport)var5.getAnnotation(WLJmsTransport.class);
               if (var11 != null) {
                  jmsListenerDeploymentHelper(var11, true, var1.getContextPath());
                  var6 = true;
               }
            }

            if (!var6) {
               JmsQueueListener.setHasServices();
            }
         }
      }

   }

   public static void removeJmsListener(WsPort var0) throws WsDeploymentException {
      Class var1 = var0.getEndpoint().getJwsClass();
      if (var1 != null) {
         boolean var2 = false;
         SoapAddress var3 = WsdlUtils.getSoapAddress(var0.getWsdlPort());
         if (var3 != null) {
            String var4 = var3.getLocation();
            int var5;
            if ((var5 = var4.indexOf("://")) > -1) {
               String var6 = var4.substring(0, var5);
               if (var6.equalsIgnoreCase("jms")) {
                  jmsListenerDeploymentHelper(var4, false);
                  var2 = true;
               }
            }
         }

         if (!var2) {
            WLJmsTransport var7 = (WLJmsTransport)var1.getAnnotation(WLJmsTransport.class);
            if (var7 != null) {
               jmsListenerDeploymentHelper(var7, false, var0.getPortComponent().getServiceEndpointAddress().getWebserviceContextpath());
            }
         }
      }

   }

   private static void jmsListenerDeploymentHelper(WLJmsTransport var0, boolean var1, String var2) {
      if (verbose) {
         Verbose.log((Object)"Adding JMS transport");
      }

      String var3 = getUrl(var0, var2);
      ServerUtil.QueueInfo var4 = getQueueInfo(var0.queue());
      String var5 = getFactory(var0.connectionFactory());
      if (var1) {
         JmsQueueListener.findOrCreateJmsListener(var3, var4, var5);
      } else {
         JmsQueueListener.removeJmsListener(var3, var4.getQueueName());
      }

   }

   private static void jmsListenerDeploymentHelper(String var0, boolean var1) {
      if (verbose) {
         Verbose.log((Object)("Adding JMS transport for url:" + var0));
      }

      int var3 = var0.indexOf("://");
      String var2 = var0.substring(var3 + 3, var0.length());
      var2 = var2.substring(var2.indexOf(47) + 1, var2.length());
      int var4 = var2.lastIndexOf("/");
      int var5 = var2.lastIndexOf(63);
      Object var8 = null;
      String var6;
      String var7;
      if (var4 > -1) {
         var6 = var2.substring(0, var4);
         if (var5 > -1) {
            var7 = var2.substring(var4 + 1, var5);
         } else {
            var7 = var2.substring(var4 + 1, var2.length());
         }
      } else {
         var6 = "/";
         var7 = var2;
      }

      Map var10 = JmsUtil.asMap(var2.substring(var5 + 1, var2.length()));
      if (verbose) {
         Verbose.log((Object)(" Queue = " + var10.get("URI") + " factory = " + var10.get("FACTORY")));
      }

      ServerUtil.QueueInfo var11 = getQueueInfo((String)var10.get("URI"));
      String var9 = getFactory((String)var10.get("FACTORY"));
      var2 = "/" + var6 + "/" + var7;
      if (var1) {
         if (verbose) {
            Verbose.log((Object)("Creating listener for JMS queue:" + var8 + " url = " + var2));
         }

         JmsQueueListener.findOrCreateJmsListener(var2, var11, var9);
      }

   }

   private static ServerUtil.QueueInfo getQueueInfo(String var0) {
      ServerUtil.QueueInfo var1 = ServerUtil.getBufferQueueInfo(var0);
      return var1;
   }

   private static String getFactory(String var0) {
      String var1 = var0;
      if (StringUtil.isEmpty(var0)) {
         var1 = ServerUtil.getJmsConnectionFactory();
      }

      return var1;
   }

   private static String getUrl(WLJmsTransport var0, String var1) {
      String var2 = "";
      String var3 = var0.contextPath();
      if (!StringUtil.isEmpty(var3)) {
         var2 = PathUtil.normalizePath(var3);
      } else if (!StringUtil.isEmpty(var1)) {
         var2 = PathUtil.normalizePath(var1);
      }

      String var4 = var0.serviceUri();
      if (!StringUtil.isEmpty(var4)) {
         var2 = var2 + PathUtil.normalizePath(var4);
      }

      if (!var2.startsWith("/")) {
         var2 = "/" + var2;
      }

      return var2;
   }
}
