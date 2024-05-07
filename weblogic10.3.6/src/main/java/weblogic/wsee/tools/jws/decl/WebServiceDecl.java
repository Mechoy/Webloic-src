package weblogic.wsee.tools.jws.decl;

import com.bea.util.jam.JAnnotation;
import com.bea.util.jam.JAnnotationValue;
import com.bea.util.jam.JClass;
import com.bea.util.jam.JSourcePosition;
import java.io.File;
import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.ejb.SessionBean;
import javax.ejb.Stateless;
import weblogic.ejbgen.Session;
import weblogic.jws.security.UserDataConstraint;
import weblogic.wsee.WebServiceType;
import weblogic.wsee.jws.HandlerChainDeclFactory;
import weblogic.wsee.jws.WebServiceRuntimeDecl;
import weblogic.wsee.tools.jws.build.JwsInfo;
import weblogic.wsee.tools.jws.context.JwsBuildContext;
import weblogic.wsee.tools.jws.decl.port.JmsPort;
import weblogic.wsee.tools.jws.decl.port.PortDecl;
import weblogic.wsee.tools.jws.decl.port.PortsBuilder;
import weblogic.wsee.tools.jws.validation.Validator;
import weblogic.wsee.util.StringUtil;
import weblogic.wsee.util.cow.CowReader;

public abstract class WebServiceDecl extends WebServiceRuntimeDecl implements Validator {
   private String artifactName = null;
   private final Set<PortDecl> ports = new HashSet();
   private final WebServiceSecurityDecl webServiceSecurityDecl;
   final JwsBuildContext ctx;
   private final CowReader cowReader;
   private final boolean isGenerateWsdl;
   private final boolean isGenerateDescriptors;

   WebServiceDecl(JwsBuildContext var1, JwsInfo var2, String var3, WebServiceDecl var4) {
      super(var2.getType(), var2.getJClass(), var4, var2.getCowReader() == null);
      this.ctx = var1;
      this.webServiceSecurityDecl = new WebServiceSecurityDecl(this.sbClass);
      this.initPorts(var2, var3);
      this.cowReader = var2.getCowReader();
      this.isGenerateWsdl = var2.isGenerateWsdl();
      this.isGenerateDescriptors = var2.isGenerateDescriptors();
      if (var2.getCowReader() != null) {
         this.handlerChainDecl = HandlerChainDeclFactory.newInstance(this.eiClass, this.sbClass, this.getType(), this.cowReader.getClassLoader());
      }

   }

   private void initPorts(JwsInfo var1, String var2) {
      PortsBuilder var3 = new PortsBuilder(this.ctx, var1, this.getServiceQName(), this.getPortName());
      var3.setWsdlLocation(this.wsdlLocation);
      var3.setContextPath(var2);
      if (this.parentWebServiceDecl != null) {
         var3.setParentWebService(this.parentWebServiceDecl);
      }

      UserDataConstraint.Transport var4 = this.getWebServiceSecurityDecl().getTransport();
      if (var4 == UserDataConstraint.Transport.INTEGRAL || var4 == UserDataConstraint.Transport.CONFIDENTIAL) {
         var3.setProtocol("https");
      }

      this.ports.addAll(var3.buildPorts());
   }

   public WebServiceDecl getParentWebService() {
      return this.parentWebServiceDecl;
   }

   public Iterator<PortDecl> getPorts() {
      return this.ports.iterator();
   }

   public Iterator<PortDecl> getDDPorts() {
      HashMap var1 = new HashMap();
      Iterator var2 = this.ports.iterator();

      while(var2.hasNext()) {
         PortDecl var3 = (PortDecl)var2.next();
         String var4 = var3.getNormalizedPath();
         if (var1.containsKey(var4)) {
            if (!(var3 instanceof JmsPort) && var1.get(var4) instanceof JmsPort) {
               var1.put(var4, var3);
            }
         } else {
            var1.put(var4, var3);
         }
      }

      return Collections.unmodifiableCollection(var1.values()).iterator();
   }

   public File getSourceFile() {
      JSourcePosition var1 = this.sbClass.getSourcePosition();
      if (var1 != null) {
         URI var2 = var1.getSourceURI();
         if (var2 != null) {
            return new File(var2);
         }
      }

      return null;
   }

   public boolean isEjb() {
      if (this.getType() == WebServiceType.JAXWS) {
         return this.sbClass.getAnnotation(Stateless.class.getName()) != null;
      } else {
         JClass var1 = this.sbClass.getClassLoader().loadClass(SessionBean.class.getName());
         return var1.isAssignableFrom(this.sbClass);
      }
   }

   public CowReader getCowReader() {
      return this.cowReader;
   }

   public String getWsdlFile() {
      return this.getCowReader() == null ? this.getArtifactName() + ".wsdl" : this.getWsdlLocation();
   }

   public String getArtifactName() {
      if (StringUtil.isEmpty(this.artifactName)) {
         this.artifactName = this.serviceName;
      }

      return this.artifactName;
   }

   public void setArtifactName(String var1) {
      this.artifactName = var1;
   }

   public boolean isWlw81UpgradedService() {
      return false;
   }

   public String getDeployedName() {
      String var1 = null;
      if (this.isEjb()) {
         var1 = this.getJClass().getSimpleName();
         JAnnotation var2 = this.getJClass().getAnnotation(Session.class);
         if (var2 != null) {
            JAnnotationValue var3 = var2.getValue("ejbName");
            if (var3 != null) {
               var1 = var3.asString();
            }
         }
      } else {
         var1 = this.getArtifactName() + "Servlet";
      }

      return var1.trim();
   }

   public WebServiceSecurityDecl getWebServiceSecurityDecl() {
      return this.webServiceSecurityDecl;
   }

   public boolean isGenerateWsdl() {
      return this.isGenerateWsdl;
   }

   public boolean isGenerateDescriptors() {
      return this.isGenerateDescriptors;
   }
}
