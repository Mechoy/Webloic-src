package weblogic.wsee.server.servlet;

import java.io.IOException;
import java.net.URL;
import java.security.AccessController;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.namespace.QName;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.wsee.WseeCoreLogger;
import weblogic.wsee.connection.transport.jms.JmsTransportInfo;
import weblogic.wsee.deploy.WsdlAddressInfo;
import weblogic.wsee.policy.deployment.PolicyDeployUtils;
import weblogic.wsee.policy.framework.NormalizedExpression;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.server.ServerURLNotFoundException;
import weblogic.wsee.server.ServerUtil;
import weblogic.wsee.util.ToStringWriter;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.wsdl.WsdlDefinitions;
import weblogic.wsee.wsdl.WsdlException;
import weblogic.wsee.wsdl.WsdlFilter;
import weblogic.wsee.wsdl.WsdlPort;
import weblogic.wsee.wsdl.WsdlSchema;
import weblogic.wsee.wsdl.WsdlService;
import weblogic.wsee.wsdl.WsdlUtils;
import weblogic.wsee.wsdl.builder.WsdlDefinitionsBuilder;
import weblogic.wsee.wsdl.builder.WsdlSchemaBuilder;
import weblogic.wsee.wsdl.builder.WsdlServiceBuilder;
import weblogic.wsee.wsdl.internal.WsdlDefinitionsImpl;
import weblogic.wsee.wsdl.soap11.SoapAddress;

public class WsdlRequestProcessor implements Processor {
   private static final String PATH_TRIM_HEADER = "WL-PATH-TRIM";
   private static final String PATH_PREPEND_HEADER = "WL-PATH-PREPEND";
   private static boolean verbose = Verbose.isVerbose(WsdlRequestProcessor.class);
   private boolean embedPolicies = true;
   private String sslPort = null;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public boolean process(HttpServletRequest var1, HttpServletResponse var2, BaseWSServlet var3) throws IOException {
      String var4 = var1.getMethod();
      if (var4.equalsIgnoreCase("GET") || var4.equalsIgnoreCase("HEAD")) {
         WsdlDefinitions var5 = WsdlUtils.findWsdlDefinition(var1.getQueryString(), var3.getPort().getWsdlPort().getService().getDefinitions());
         if (var5 != null) {
            WsdlAddressInfo var8 = this.getAddressInfo(var1, var3);
            this.embedPoliciesInWsdl(var5, var3);
            this.writeWsdl(var2, var8, var5);
            return true;
         }

         WsdlSchema var6 = WsdlUtils.findSchema(var1.getQueryString(), (WsdlDefinitionsImpl)var3.getPort().getWsdlPort().getService().getDefinitions());
         if (var6 != null) {
            WsdlAddressInfo var7 = this.getAddressInfo(var1, var3);
            this.writeSchema(var2, var7, var6, var5 == null ? null : var5.getEncoding());
            return true;
         }
      }

      return false;
   }

   private void embedPoliciesInWsdl(WsdlDefinitions var1, BaseWSServlet var2) {
      if (this.embedPolicies) {
         try {
            PolicyDeployUtils.embedPoliciesInWsdl(var1, var2.getPort().getEndpoint().getService().getPolicyServer());
         } catch (PolicyException var4) {
            var4.printStackTrace();
         }
      }

   }

   private void writeSchema(HttpServletResponse var1, WsdlAddressInfo var2, WsdlSchema var3, String var4) throws IOException {
      var1.setContentType("text/xml");
      ServletOutputStream var5 = var1.getOutputStream();

      try {
         ((WsdlSchemaBuilder)var3).write(var5, var2, var4);
      } catch (WsdlException var7) {
         if (verbose) {
            Verbose.log("Failed to write schema", var7);
         }

         throw new IOException("Failed to write schema: " + var7);
      }

      if (verbose) {
         Verbose.log((Object)("Wrote schema: " + var3.getLocationUrl()));
      }

   }

   private void writeWsdl(HttpServletResponse var1, WsdlAddressInfo var2, WsdlDefinitions var3) throws IOException {
      var1.setContentType("text/xml");
      ServletOutputStream var4 = var1.getOutputStream();
      if (var3.getEncoding() != null) {
         var1.setCharacterEncoding(var3.getEncoding());
      }

      try {
         ((WsdlDefinitionsBuilder)var3).write(var4, var2);
      } catch (WsdlException var6) {
         if (verbose) {
            Verbose.log("Failed to write wsdl", var6);
         }

         throw new IOException("Failed to write WSDL: " + var6);
      }

      if (verbose) {
         Verbose.log((Object)("Wrote WSDL: " + var3.getName()));
      }

   }

   private WsdlAddressInfo getAddressInfo(HttpServletRequest var1, BaseWSServlet var2) throws IOException {
      WsdlAddressInfo var3 = new WsdlAddressInfo();
      UnsupportedPortsWsdlFilter var4 = null;
      WsdlFilter var5 = var2.getPort().getWsdlPort().getService().getWsdlFilter();
      if (var5 instanceof UnsupportedPortsWsdlFilter) {
         var4 = (UnsupportedPortsWsdlFilter)var5;
         var4.reset();
      } else {
         var4 = new UnsupportedPortsWsdlFilter(var5);
         ((WsdlServiceBuilder)var2.getPort().getWsdlPort().getService()).setWsdlFilter(var4);
      }

      var3.setWsdlFilter(var4);
      boolean var6 = var1.isSecure();
      URL var7 = new URL(ServerUtil.getHTTPServerURL(var6, var1));
      String var8 = var7.getHost();
      var3.setHost(var8);
      int var9 = var7.getPort();
      var3.setPort("" + var9);
      String var10 = var1.getRequestURI();
      String var11 = var1.getHeader("WL-PATH-TRIM");
      String var12 = var1.getHeader("WL-PATH-PREPEND");
      var10 = rewriteUriForProxyHeaders(var10, var12, var11);
      String var13 = var7.toString() + var10 + "?WSDL";
      var3.setImportPrefix(var13);
      WsdlDefinitions var14 = var2.getPort().getWsdlPort().getDefinitions();
      String var15 = var10;
      Iterator var16 = var14.getServices().values().iterator();

      label90:
      while(var16.hasNext()) {
         WsdlService var17 = (WsdlService)var16.next();
         Iterator var18 = var17.getPorts().values().iterator();

         while(true) {
            WsdlPort var19;
            WsdlAddressInfo.PortAddress var20;
            URL var22;
            while(true) {
               if (!var18.hasNext()) {
                  continue label90;
               }

               var19 = (WsdlPort)var18.next();
               if (verbose) {
                  Verbose.log((Object)(" process WsdlPort '" + var19 + "'"));
               }

               var3.addWsdlPort(var19.getName(), var19.getPortAddress());
               var20 = var19.getPortAddress();
               if (var20 == null) {
                  var20 = var3.addWsdlPort(var19.getName());
               }

               String var21 = this.getProtocol(var19);
               var22 = var7;
               boolean var23 = var21.equalsIgnoreCase("jms");
               if (var23) {
                  var22 = new URL(ServerUtil.swapProtocol(ServerUtil.getJMSServerURL(), "http"));
                  var20.setProtocol("jms");
                  break;
               }

               if (SecurityHelper.isHttpsRequiredByWssp(getEndpointPolicy(var2, var19))) {
                  var22 = new URL(ServerUtil.getHTTPServerURL(true));
               } else if ((!var6 || !var21.equalsIgnoreCase("http")) && !var6 && var21.equalsIgnoreCase("https")) {
                  try {
                     var22 = new URL(ServerUtil.getHTTPServerURL(true));
                  } catch (ServerURLNotFoundException var29) {
                     var4.addUnsupportedPort(var19.getName());
                     WseeCoreLogger.logServicePortNotAvailableInWSDL(var17.getName().getLocalPart(), var19.getName().getLocalPart(), var7.toString() + var10 + "?WSDL");
                     continue;
                  }
               }

               var20.setProtocol(var22.getProtocol());
               break;
            }

            var20.setHost(var22.getHost());
            var20.setListenPort(String.valueOf(var22.getPort()));
            String var24 = WsdlUtils.getQueryString(var19);
            if (verbose) {
               Verbose.log((Object)("Query string:" + var24));
            }

            SoapAddress var25 = WsdlUtils.getSoapAddress(var19);
            if (var25 != null) {
               try {
                  String var26 = var25.getLocation();
                  if (var26.toLowerCase(Locale.ENGLISH).startsWith("jms:")) {
                     JmsTransportInfo var27 = new JmsTransportInfo(var26);
                     var15 = var27.getServiceUri();
                  } else {
                     URL var30 = new URL(var26);
                     var15 = rewriteUriForProxyHeaders(var30.getPath(), var12, var11);
                  }
               } catch (Throwable var28) {
                  throw new IOException(" Error.  Could not determine uri from 'location' for WsdlPort " + var19 + "   " + var28.getMessage());
               }
            } else {
               if (verbose) {
                  Verbose.log((Object)("wsdlPort " + var19 + "  has no wsdl extensions (therefore no Soap address) "));
               }

               if (var20.getServiceuri() != null) {
                  var15 = var20.getServiceuri();
               }
            }

            if (var24 != null) {
               var15 = var15 + "?" + var24;
            }

            if (verbose) {
               Verbose.log((Object)(" setting Address ServiceUri to '" + var15 + "'"));
            }

            var20.setServiceuri(var15);
         }
      }

      return var3;
   }

   private static final NormalizedExpression getEndpointPolicy(BaseWSServlet var0, WsdlPort var1) throws IOException {
      try {
         return var0.getDeployInfo().getWssPolicyContext().getPolicyServer().getEndpointPolicy(var1);
      } catch (PolicyException var3) {
         throw new IOException(var3.getMessage());
      }
   }

   private static String rewriteUriForProxyHeaders(String var0, String var1, String var2) {
      if (var1 != null && var0.startsWith(var1)) {
         var0 = var0.substring(var1.length());
      }

      if (var2 != null) {
         var0 = var2 + var0;
      }

      return var0;
   }

   private String getProtocol(WsdlPort var1) {
      return var1.getBinding().getTransportProtocol();
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

   private class UnsupportedPortsWsdlFilter implements WsdlFilter {
      private WsdlFilter delegate = null;
      private Set<QName> unsupportedPorts = new HashSet();

      public UnsupportedPortsWsdlFilter(WsdlFilter var2) {
         this.setDelegate(var2);
      }

      public void setDelegate(WsdlFilter var1) {
         this.delegate = var1;
      }

      public WsdlFilter getDelegate() {
         return this.delegate;
      }

      public void addUnsupportedPort(QName var1) {
         this.unsupportedPorts.add(var1);
      }

      public void removeUnsupportedPort(QName var1) {
         this.unsupportedPorts.remove(var1);
      }

      public void reset() {
         this.unsupportedPorts.clear();
      }

      public boolean isPortSupported(QName var1) {
         if (this.unsupportedPorts.contains(var1)) {
            return false;
         } else {
            return this.delegate != null ? this.delegate.isPortSupported(var1) : true;
         }
      }

      public boolean isMessagePartSupported(QName var1, String var2) {
         return this.delegate != null ? this.delegate.isMessagePartSupported(var1, var2) : true;
      }

      public String rewritePortUrl(QName var1, String var2, String var3, String var4, String var5, String var6) {
         return this.delegate != null ? this.delegate.rewritePortUrl(var1, var2, var3, var4, var5, var6) : var2 + "://" + var3 + ":" + var4 + var5;
      }
   }
}
