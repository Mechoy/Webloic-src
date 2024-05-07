package weblogic.wsee.util;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.AccessController;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.xml.rpc.JAXRPCException;
import javax.xml.rpc.Stub;
import weblogic.j2ee.descriptor.wl.DeliveryFailureParamsBean;
import weblogic.j2ee.descriptor.wl.DestinationBean;
import weblogic.j2ee.descriptor.wl.JMSBean;
import weblogic.jms.common.DestinationImpl;
import weblogic.jms.extensions.JMSModuleHelper;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.JMSSystemResourceMBean;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.wsee.handler.InvocationException;

public final class JmsUtil {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   public static final String URI = "URI";
   public static final String WSEE_JMS_SUBJECT = "WSEE_JMS_SUBJECT";
   public static final String JMS_TEXTMESSAGE = "TextMessage";
   public static final String JMS_BYTESMESSAGE = "BytesMessage";
   public static final String ISWLW81MESSAGE = "IsWLW81Message";

   public static String getNonJmsTransportSelector(String var0) {
      String var1 = new String(var0);
      if (var0.indexOf("/") == 0) {
         var1 = var0.substring(1);
      }

      return "(URI = '" + var1 + "' OR " + "URI" + " = '/" + var1 + "')";
   }

   public static String getJmsTransportSelector(String var0) {
      String var1 = new String(var0);
      if (var0.indexOf("/") == 0) {
         var1 = var0.substring(1);
      }

      return "(URI = '" + var1 + "' OR " + "URI" + " = '/" + var1 + "')";
   }

   public static String getWLW81Selector(String var0) {
      String var1 = new String(var0);
      if (var0.indexOf("/") == 0) {
         var1 = var0.substring(1);
      }

      return "URI = '" + var1 + ".jws'" + " OR " + "URI" + " = '/" + var1 + ".jws'";
   }

   public static void setJmsTransportTextMessage(Stub var0) {
      if (var0 == null) {
         throw new JAXRPCException("Stub is null");
      } else {
         var0._setProperty("weblogic.wsee.transport.jms.messagetype", "TextMessage");
      }
   }

   public static void setJmsTransportBytesMessage(Stub var0) {
      if (var0 == null) {
         throw new JAXRPCException("Stub is null");
      } else {
         var0._setProperty("weblogic.wsee.transport.jms.messagetype", "BytesMessage");
      }
   }

   public static String getErrorDestinationJNDI(String var0) {
      DomainMBean var1 = ManagementService.getRuntimeAccess(kernelId).getDomain();
      if (var1 == null) {
         throw new InvocationException("Cannot find DomainMBean");
      } else {
         DestinationImpl var2 = null;

         try {
            InitialContext var3 = new InitialContext();
            var2 = (DestinationImpl)var3.lookup(var0);
         } catch (NamingException var12) {
            throw new InvocationException("Error looking up queue at " + var0, var12);
         }

         if (var2 == null) {
            throw new InvocationException("Cannot find DestinationImpl for " + var0);
         } else {
            String var13 = var2.getQueueName();
            if (var13 == null) {
               throw new InvocationException("Queue name is not set for " + var0);
            } else {
               int var4 = var13.indexOf(33);
               String var5 = null;
               String var6 = null;
               if (var4 != 0 && var4 != var13.length() - 1) {
                  if (var4 < 0) {
                     var5 = "interop-jms";
                     var6 = var13;
                  } else {
                     var5 = var13.substring(0, var4);
                     var6 = var13.substring(var4 + 1, var13.length());
                  }

                  Object var7 = var1.lookupJMSInteropModule(var5);
                  if (var7 == null) {
                     var7 = var1.lookupJMSSystemResource(var5);
                     if (var7 == null) {
                        throw new InvocationException("Could not find JMSSystemResource " + var5 + " in the domain " + var1.getName());
                     }
                  }

                  JMSBean var8 = ((JMSSystemResourceMBean)var7).getJMSResource();
                  if (var8 == null) {
                     throw new InvocationException("Could not find JMSBean for " + var5 + " in the domain " + var1.getName());
                  } else {
                     DestinationBean var9 = JMSModuleHelper.findDestinationBean(var6, var8);
                     if (var9 == null) {
                        throw new InvocationException("Could not find DestinationBean " + var6 + " in the domain " + var1.getName());
                     } else {
                        DeliveryFailureParamsBean var10 = var9.getDeliveryFailureParams();
                        if (var10 == null) {
                           throw new InvocationException("Could not find DeliveryFailureParamsBean for " + var6 + " in the domain " + var1.getName());
                        } else {
                           DestinationBean var11 = var10.getErrorDestination();
                           return var11 == null ? null : var11.getJNDIName();
                        }
                     }
                  }
               } else {
                  throw new InvocationException("Incorrect format for the queue name " + var13);
               }
            }
         }
      }
   }

   public static Map asMap(String var0) {
      StringTokenizer var1 = new StringTokenizer(var0, "&");

      HashMap var2;
      String var5;
      for(var2 = new HashMap(); var1.hasMoreTokens(); var5 = null) {
         String var3 = var1.nextToken();
         String var4 = null;
         var5 = null;
         int var6 = var3.indexOf("=");
         if (var6 != -1) {
            var4 = var3.substring(0, var6);
            var5 = var3.substring(var6 + 1);
         }

         if (var4 != null && var5 != null) {
            var2.put(var4, var5);
         }

         var4 = null;
      }

      return var2;
   }

   public static String wlw81UriToWls9(String var0) {
      return null;
   }

   public static String wls9UriToWlw81(String var0) {
      try {
         URI var1 = new URI(var0);
         String var2 = var1.getHost();
         int var3 = var1.getPort();
         String var4 = var1.getPath();
         if (var4 == null) {
            throw new JAXRPCException("Wrong WLS9 JMS URI format: no path specified");
         } else {
            String var5 = var1.getQuery();
            if (var5 == null) {
               throw new JAXRPCException("Wrong WLS9 JMS URI format: no query specified");
            } else {
               Map var6 = asMap(var5);
               String var7 = (String)var6.get("URI");
               if (StringUtil.isEmpty(var7)) {
                  throw new JAXRPCException("Wrong WLS9 JMS URI format: no URI specified");
               } else {
                  String var8 = (String)var6.get("FACTORY");
                  if (StringUtil.isEmpty(var8)) {
                     var8 = "weblogic.jms.ConnectionFactory";
                  }

                  StringBuffer var9 = new StringBuffer();
                  var9.append(var1.getScheme());
                  var9.append("://");
                  var9.append(var2);
                  if (var3 != -1) {
                     var9.append(":" + var3);
                  }

                  var9.append("/" + var8);
                  var9.append("/" + var7);
                  var9.append("?URI=" + var4);
                  return var9.toString();
               }
            }
         }
      } catch (URISyntaxException var10) {
         throw new JAXRPCException(var10);
      }
   }

   public static boolean isWlw81JmsUri(String var0) {
      if (var0 == null) {
         return false;
      } else {
         try {
            URI var1 = new URI(var0);
            String var2 = var1.getScheme();
            if (!"jms".equals(var2)) {
               return false;
            } else {
               String var3 = var1.getQuery();
               if (var3 == null) {
                  return false;
               } else {
                  Map var4 = asMap(var3);
                  String var5 = (String)var4.get("URI");
                  return StringUtil.isEmpty(var5) ? false : isWlw81JmsUriProperty(var5);
               }
            }
         } catch (URISyntaxException var6) {
            return false;
         }
      }
   }

   public static boolean isWlw81JmsUriProperty(String var0) {
      return var0 == null ? false : var0.startsWith("/");
   }
}
