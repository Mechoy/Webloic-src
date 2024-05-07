package weblogic.wsee.tools.jws.decl.port;

import com.bea.util.jam.JAnnotation;
import com.bea.util.jam.JClass;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.xml.namespace.QName;
import weblogic.jws.WLHttpTransport;
import weblogic.jws.WLHttpsTransport;
import weblogic.jws.WLJmsTransport;
import weblogic.jws.WLLocalTransport;
import weblogic.wsee.WebServiceType;
import weblogic.wsee.callback.CallbackUtils;
import weblogic.wsee.tools.jws.JwsLogEvent;
import weblogic.wsee.tools.jws.build.JwsInfo;
import weblogic.wsee.tools.jws.context.JwsBuildContext;
import weblogic.wsee.tools.jws.decl.WebServiceDecl;
import weblogic.wsee.tools.jws.decl.WebServiceSEIDecl;
import weblogic.wsee.tools.logging.EventLevel;
import weblogic.wsee.tools.logging.LogEvent;
import weblogic.wsee.util.JamUtil;
import weblogic.wsee.util.PathUtil;
import weblogic.wsee.util.StringUtil;
import weblogic.wsee.wsdl.WsdlDefinitions;
import weblogic.wsee.wsdl.WsdlException;
import weblogic.wsee.wsdl.WsdlPort;
import weblogic.wsee.wsdl.WsdlService;
import weblogic.wsee.wsdl.WsdlUtils;
import weblogic.wsee.wsdl.soap11.SoapAddress;

public class PortsBuilder {
   private final JwsBuildContext ctx;
   private final JwsInfo jws;
   private final QName serviceName;
   private String portName = null;
   private String wsdlLocation = null;
   private String contextPath = null;
   private WebServiceDecl parentWebService = null;
   private String protocol = "http";

   public PortsBuilder(JwsBuildContext var1, JwsInfo var2, QName var3, String var4) {
      this.ctx = var1;
      this.jws = var2;
      this.serviceName = var3;
      this.portName = var4;
   }

   public void setContextPath(String var1) {
      this.contextPath = var1;
   }

   public void setParentWebService(WebServiceDecl var1) {
      this.parentWebService = var1;
   }

   public void setWsdlLocation(String var1) {
      this.wsdlLocation = var1;
   }

   public void setProtocol(String var1) {
      this.protocol = var1;
   }

   public Set<PortDecl> buildPorts() {
      Set var1 = this.getPorts();
      this.normalizePorts(var1);
      return var1;
   }

   private void normalizePorts(Set<PortDecl> var1) {
      HashSet var2 = new HashSet();
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         PortDecl var4 = (PortDecl)var3.next();
         this.normalizePort(var4, var2);
      }

      this.namePorts(var2);
   }

   private void normalizePort(PortDecl var1, Set<Port> var2) {
      if (var1 instanceof Port) {
         Port var3 = (Port)var1;
         var3.setContextPath(this.normalizeContextPath(var3.getPortName(), var3.getContextPath()));
         var3.setServiceUri(this.normalizeServiceUri(var3.getServiceUri()));
         var3.setProtocol(this.protocol);
         if (var3 instanceof JmsPort) {
            ((JmsPort)var3).setQueue(((JmsPort)var3).getQueue());
            ((JmsPort)var3).setFactory(((JmsPort)var3).getFactory());
         }

         if (StringUtil.isEmpty(var3.getPortName())) {
            var2.add(var3);
         } else {
            var3.setPortName(var3.getPortName().trim());
         }
      }

   }

   private void namePorts(Set<Port> var1) {
      if (var1.size() == 1) {
         Port var2 = (Port)var1.iterator().next();
         var2.setPortName(this.portName);
      } else {
         Iterator var5 = var1.iterator();

         while(var5.hasNext()) {
            Port var3 = (Port)var5.next();
            Port var4 = (Port)var3;
            var4.setPortName(this.portName + var4.getProtocol());
         }
      }

   }

   private Set<PortDecl> getPorts() {
      HashSet var1 = new HashSet();
      if (!this.jws.getPorts().hasNext()) {
         var1.addAll(this.findTransportAnnotations());
      } else {
         Iterator var2 = this.jws.getPorts();

         while(var2.hasNext()) {
            var1.add(var2.next());
         }
      }

      if (var1.size() == 0 && !StringUtil.isEmpty(this.wsdlLocation) && this.jws.getCowReader() != null) {
         this.loadWsdlPorts(var1);
      }

      if (var1.size() == 0) {
         this.addDefaultPorts(var1);
      }

      return var1;
   }

   private void addDefaultPorts(Set<PortDecl> var1) {
      if (this.parentWebService == null) {
         var1.add(new HttpPort());
      } else if (this.parentWebService instanceof WebServiceSEIDecl) {
         WebServiceSEIDecl var2 = (WebServiceSEIDecl)this.parentWebService;
         Iterator var3 = var2.getPorts();

         while(var3.hasNext()) {
            PortDecl var4 = (PortDecl)var3.next();
            Port var5 = PortFactory.newPort(var4.getProtocol());
            if (var5 == null) {
               this.ctx.getLogger().log(EventLevel.WARNING, (LogEvent)(new JwsLogEvent(this.jws.getJClass(), "port.protocol.unhandled", new Object[]{var4.getProtocol(), var4.getPortName(), this.serviceName})));
            } else {
               var5.setPortName(CallbackUtils.getCallbackPortName(var4.getPortName()));
               var1.add(var5);
            }
         }
      }

   }

   private void loadWsdlPorts(Set<PortDecl> var1) {
      WsdlDefinitions var2 = null;

      try {
         var2 = this.jws.getCowReader().getWsdl(this.wsdlLocation);
      } catch (WsdlException var11) {
         this.ctx.getLogger().log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(this.jws.getJClass(), "cow.wsdl.invalid", new Object[]{this.wsdlLocation, var11.getMessage()})));
      }

      WsdlService var3 = (WsdlService)var2.getServices().get(this.serviceName);
      if (var3 != null) {
         Iterator var4 = var3.getPorts().values().iterator();

         while(var4.hasNext()) {
            WsdlPort var5 = (WsdlPort)var4.next();
            SoapAddress var6 = WsdlUtils.getSoapAddress(var5);
            if (var6 != null) {
               String var7 = var6.getLocation();
               if ("REPLACE_WITH_ACTUAL_URL".equals(var7)) {
                  var7 = this.protocol + "://localhost/" + (this.contextPath != null ? this.contextPath + "/" : "") + this.serviceName.getLocalPart() + "/" + this.portName;
               }

               try {
                  URL var8 = new URL(var7);
                  Port var9 = PortFactory.newPort(var8, var5);
                  if (var9 != null) {
                     var1.add(var9);
                  } else {
                     this.ctx.getLogger().log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(this.jws.getJClass(), "port.cow.invalidAddressLocation", new Object[]{var6.getLocation(), "Unsupported protocol."})));
                  }
               } catch (MalformedURLException var10) {
                  this.ctx.getLogger().log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(this.jws.getJClass(), "port.cow.invalidAddressLocation", new Object[]{var6.getLocation(), var10.getMessage()})));
               }
            }
         }
      }

   }

   private Set<PortDecl> findTransportAnnotations() {
      HashSet var1 = new HashSet();
      JClass var2 = this.jws.getJClass();
      JAnnotation var3 = var2.getAnnotation(WLHttpTransport.class);
      if (var3 != null) {
         HttpPort var4 = new HttpPort();
         setCommonAttributes(var4, var3);
         var1.add(var4);
      }

      var3 = var2.getAnnotation(WLHttpsTransport.class);
      if (var3 != null) {
         HttpsPort var7 = new HttpsPort();
         setCommonAttributes(var7, var3);
         var1.add(var7);
      }

      var3 = var2.getAnnotation(WLLocalTransport.class);
      if (var3 != null) {
         LocalPort var8 = new LocalPort();
         setCommonAttributes(var8, var3);
         var1.add(var8);
      }

      var3 = var2.getAnnotation(WLJmsTransport.class);
      if (var3 != null) {
         JmsPort var9 = new JmsPort();
         setCommonAttributes(var9, var3);
         String var5 = JamUtil.getAnnotationStringValue(var3, "queue");
         String var6 = JamUtil.getAnnotationStringValue(var3, "connectionFactory");
         var9.setQueue(var5);
         var9.setFactory(var6);
         var1.add(var9);
      }

      return var1;
   }

   private static void setCommonAttributes(Port var0, JAnnotation var1) {
      var0.setContextPath(JamUtil.getAnnotationStringValue(var1, "contextPath"));
      var0.setServiceUri(JamUtil.getAnnotationStringValue(var1, "serviceUri"));
      var0.setPortName(JamUtil.getAnnotationStringValue(var1, "portName"));
   }

   private String normalizeServiceUri(String var1) {
      String var2 = null;
      if (var1 == null) {
         if (this.ctx.getTask() == JwsBuildContext.Task.APT) {
            var2 = this.jws.getJClass().getQualifiedName().replace('.', '/') + ".jws";
         } else if (this.jws.getType() == WebServiceType.JAXWS) {
            var2 = this.serviceName.getLocalPart();
         } else {
            var2 = this.jws.getJClass().getSimpleName();
         }
      } else {
         var2 = var1;
      }

      return PathUtil.normalizePath(var2);
   }

   private String normalizeContextPath(String var1, String var2) {
      String var3 = null;
      if (this.contextPath != null) {
         var3 = this.contextPath;
         if (var2 != null) {
            this.ctx.getLogger().log(EventLevel.WARNING, (LogEvent)(new JwsLogEvent(this.jws.getJClass(), "port.overrideContextPath", new Object[]{this.serviceName, var1, var2, this.contextPath})));
         }
      } else if (var2 != null) {
         var3 = var2;
      } else {
         var3 = this.jws.getJClass().getSimpleName();
      }

      var3 = var3.replace('\\', '/');
      if (var3.endsWith("/")) {
         var3 = var3.substring(0, var3.length() - 1);
      }

      return var3;
   }
}
