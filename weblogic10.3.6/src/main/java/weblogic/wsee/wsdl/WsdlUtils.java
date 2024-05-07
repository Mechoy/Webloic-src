package weblogic.wsee.wsdl;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.AccessController;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.wsee.addressing.wsdl.EndpointReferenceWsdlExtension;
import weblogic.wsee.deploy.WsdlAddressInfo;
import weblogic.wsee.policy.framework.PolicyStatement;
import weblogic.wsee.util.StringUtil;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.ws.WsPort;
import weblogic.wsee.wsdl.builder.WsdlOperationBuilder;
import weblogic.wsee.wsdl.internal.WsdlDefinitionsImpl;
import weblogic.wsee.wsdl.internal.WsdlTypesImpl;
import weblogic.wsee.wsdl.mime.MimeContent;
import weblogic.wsee.wsdl.mime.MimeMultipartRelated;
import weblogic.wsee.wsdl.mime.MimePart;
import weblogic.wsee.wsdl.soap11.SoapAddress;

public class WsdlUtils {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public static WsdlDefinitions findWsdlDefinition(String var0, WsdlDefinitions var1) {
      if (var0.length() < 4) {
         return null;
      } else if (!"WSDL".equalsIgnoreCase(var0.substring(0, 4))) {
         return null;
      } else if (var0.length() == 4) {
         return var1;
      } else {
         String var2 = var0.substring(5);
         return ((WsdlDefinitionsImpl)var1).findImport(var2);
      }
   }

   public static WsdlSchema findSchema(String var0, WsdlDefinitionsImpl var1) {
      if (var0.length() < 4) {
         return null;
      } else if (!"WSDL".equalsIgnoreCase(var0.substring(0, 4))) {
         return null;
      } else if (var0.length() == 4) {
         throw new AssertionError("This is WSDL request. Should never get here with query.length==4 where query='" + var0 + "'");
      } else {
         String var2 = var0.substring(5);
         WsdlTypesImpl var3 = (WsdlTypesImpl)var1.getTypes();
         return var3 == null ? null : var3.findImport(var2);
      }
   }

   public static void updateAddress(WsdlDefinitions var0, WsdlAddressInfo var1) {
      ServerMBean var2 = ManagementService.getRuntimeAccess(kernelId).getServer();
      String var3 = var2.getListenAddress();
      if (var3 == null) {
         var3 = "localhost";
      }

      var1.setHost(var3);
      var1.setPort("" + var2.getListenPort());
      Iterator var4 = var0.getServices().values().iterator();

      while(var4.hasNext()) {
         WsdlService var5 = (WsdlService)var4.next();
         Iterator var6 = var5.getPorts().values().iterator();

         while(var6.hasNext()) {
            WsdlPort var7 = (WsdlPort)var6.next();
            SoapAddress var8 = getSoapAddress(var7);
            String var9 = var1.getServiceUrl(var7.getName());
            if (var8 != null && var9 != null) {
               String var10 = getQueryString(var7);
               if (var10 != null) {
                  var9 = var9 + "?" + var10;
               }

               var8.setLocation(var9);
            }

            EndpointReferenceWsdlExtension var11 = getEndpointReferencelExtension(var7);
            if (var11 != null && var9 != null) {
               var11.setAddressLocation(var9);
            }
         }
      }

   }

   public static boolean policyExists(Map var0, PolicyStatement var1) {
      String var2 = var1.getId();
      if (var2 == null) {
         return var0.get(var1) != null;
      } else {
         return var0.get(var2) != null;
      }
   }

   public static void addPolicyToMap(Map var0, PolicyStatement var1) {
      String var2 = var1.getId();
      if (var2 != null) {
         var0.put(var2, var1);
      } else {
         var0.put(var1, var1);
      }
   }

   public static String findReturnPart(WsdlOperation var0) {
      String var1 = ((WsdlOperationBuilder)var0).getParameterOrder();
      HashSet var2 = new HashSet();
      if (var1 != null) {
         StringTokenizer var3 = new StringTokenizer(var1, " ");

         while(var3.hasMoreTokens()) {
            var2.add(var3.nextToken());
         }
      }

      WsdlMessage var4;
      WsdlMessage var9;
      if (((WsdlOperationBuilder)var0).isWLW81CallbackOperation()) {
         var4 = var0.getOutput();
         var9 = var0.getInput();
      } else {
         var4 = var0.getInput();
         var9 = var0.getOutput();
      }

      if (var9 == null) {
         return null;
      } else {
         String var5 = null;
         Iterator var6 = var9.getParts().values().iterator();

         while(true) {
            WsdlPart var7;
            WsdlPart var8;
            do {
               if (!var6.hasNext()) {
                  return var5;
               }

               var7 = (WsdlPart)var6.next();
               var8 = var4 == null ? null : (WsdlPart)var4.getParts().get(var7.getName());
            } while(var8 == null && var2.contains(var7.getName()));

            if (!var7.equals(var8)) {
               if (var5 != null) {
                  if (StringUtil.isEmpty(var1)) {
                     return null;
                  }

                  throw new IllegalStateException("While processing WSDL '" + var0.getName().toString() + "' More than one return outPart found in " + "operation: " + var0 + ". Only one outPart name can be " + "missing in the parameterOrder");
               }

               var5 = var7.getName();
            }
         }
      }
   }

   public static String getQueryString(WsdlPort var0) {
      SoapAddress var1 = getSoapAddress(var0);
      String var2 = null;
      if (var1 != null) {
         String var3 = var1.getLocation();

         try {
            URI var4 = new URI(var3);
            var2 = var4.getQuery();
         } catch (URISyntaxException var5) {
            Verbose.log("Failed to create URI from WSDL location, where location='" + (var3 == null ? "NULL" : var3) + "'", var5);
         }
      }

      return var2;
   }

   public static String constructRelativeLocation(String var0, String var1) throws WsdlException {
      return constructRelativeLocation(var0, var1, (RelativeResourceResolver)null);
   }

   public static String constructRelativeLocation(String var0, String var1, RelativeResourceResolver var2) throws WsdlException {
      URL var3;
      if (var2 != null) {
         var3 = var2.getResource(var0);
         if (var3 != null) {
            return var3.toString();
         }
      }

      var3 = null;

      try {
         var3 = new URL(var1);
      } catch (MalformedURLException var9) {
         String var5 = "file:" + var1;

         try {
            var3 = new URL(var5);
         } catch (MalformedURLException var8) {
            throw new WsdlException("Failed to construct WSDL relative URL.Tried the following locations: '" + var0 + "', '" + var1 + "', " + var5 + "'.  Unable to get a URL from any of those locations. " + var9.getMessage() + ", " + var8.getMessage());
         }
      }

      try {
         return (new URL(var3, var0)).toExternalForm();
      } catch (MalformedURLException var7) {
         throw new WsdlException("Failed to construct WSDL relative URL using rootURL='" + var3 + "', location='" + var0 + "'. " + var7.getMessage());
      }
   }

   public static SoapAddress getSoapAddress(WsdlPort var0) {
      SoapAddress var1 = null;
      var1 = (SoapAddress)var0.getExtension("SOAP11-address");
      if (var1 == null) {
         var1 = (SoapAddress)var0.getExtension("SOAP12-address");
      }

      return var1;
   }

   public static boolean isSoap12(WsPort var0) {
      if (var0 != null) {
         String var1 = var0.getWsdlPort().getBinding().getBindingType();
         return "SOAP12".equals(var1);
      } else {
         return false;
      }
   }

   public static EndpointReferenceWsdlExtension getEndpointReferencelExtension(WsdlPort var0) {
      return (EndpointReferenceWsdlExtension)var0.getExtension("EndPonit-Reference");
   }

   public static String getMimeType(String var0, WsdlBindingMessage var1) {
      if (var1 == null) {
         return null;
      } else {
         String var2 = null;
         MimeMultipartRelated var3 = MimeMultipartRelated.narrow(var1);
         if (var3 != null) {
            List var4 = var3.getParts();
            Iterator var5 = var4.iterator();

            while(var5.hasNext()) {
               MimePart var6 = (MimePart)var5.next();
               List var7 = MimeContent.narrow(var6);
               Iterator var8 = var7.iterator();

               while(var8.hasNext()) {
                  MimeContent var9 = (MimeContent)var8.next();
                  if (var0.equals(var9.getPart())) {
                     var2 = var9.getType();
                     return var2;
                  }
               }
            }
         }

         return var2;
      }
   }
}
