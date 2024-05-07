package weblogic.wsee.jws;

import com.bea.util.jam.JAnnotation;
import com.bea.util.jam.JClass;
import com.bea.util.jam.JMethod;
import com.bea.util.jam.internal.elements.UnresolvedClassImpl;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingType;
import javax.xml.ws.Provider;
import weblogic.jws.Conversation;
import weblogic.jws.Conversational;
import weblogic.wsee.WebServiceType;
import weblogic.wsee.tools.jws.decl.WebServiceDecl;
import weblogic.wsee.util.ClassUtil;
import weblogic.wsee.util.JamUtil;
import weblogic.wsee.util.StringUtil;

public class WebServiceRuntimeDecl {
   protected final JClass sbClass;
   protected JClass eiClass;
   private final String name;
   protected final String serviceName;
   protected final WebServiceDecl parentWebServiceDecl;
   protected String targetNamespace;
   protected String wsdlLocation;
   private final String endPointInterface;
   private final String className;
   private final String simpleName;
   private final String packageName;
   private final QName portQName;
   private boolean conversational;
   protected String portTypeNamespaceURI;
   private final WebServiceType webServiceType;
   protected HandlerChainDecl handlerChainDecl;
   private String protocolBinding;
   private boolean mtomEnabled;
   private boolean providerBasedEndpoint;

   public WebServiceRuntimeDecl(WebServiceType var1, Class var2) {
      this(var1, JamUtil.loadJClass(var2.getName(), new DelegatingLoader(var2.getClassLoader(), Thread.currentThread().getContextClassLoader())), (WebServiceDecl)null);
   }

   protected WebServiceRuntimeDecl(WebServiceType var1, JClass var2, WebServiceDecl var3) {
      this(var1, var2, var3, true);
   }

   protected WebServiceRuntimeDecl(WebServiceType var1, JClass var2, WebServiceDecl var3, boolean var4) {
      this.targetNamespace = null;
      this.conversational = false;
      this.portTypeNamespaceURI = null;
      this.mtomEnabled = false;
      this.providerBasedEndpoint = false;
      this.webServiceType = var1;
      this.sbClass = var2;
      this.eiClass = this.sbClass;
      this.parentWebServiceDecl = var3;
      this.providerBasedEndpoint = isProviderBasedEndpoint(var2);
      JAnnotation var5 = ClassUtil.getWebServiceAnnotation(this.sbClass);
      this.endPointInterface = JamUtil.getAnnotationStringValue(var5, "endpointInterface");
      if (!StringUtil.isEmpty(this.endPointInterface)) {
         this.eiClass = this.sbClass.getClassLoader().loadClass(this.endPointInterface);
         if (this.eiClass instanceof UnresolvedClassImpl) {
            this.eiClass = this.sbClass;
         }
      }

      JAnnotation var6 = ClassUtil.getWebServiceAnnotation(this.eiClass);
      this.className = this.sbClass.getQualifiedName();
      this.simpleName = StringUtil.getSimpleClassName(this.className);
      this.packageName = StringUtil.getPackage(this.className);
      this.name = this.getPortTypeName(var6);
      this.initNamespaces(var5, var6);
      this.serviceName = this.buildServiceName(this.sbClass, var5);
      this.wsdlLocation = JamUtil.getAnnotationStringValue(var6, "wsdlLocation");
      if (this.wsdlLocation == null) {
         this.wsdlLocation = JamUtil.getAnnotationStringValue(var5, "wsdlLocation");
      }

      this.setConversationalFlag(this.sbClass);
      String var7 = this.buildPortName(var5);
      this.portQName = new QName(this.targetNamespace, var7);
      if (var4) {
         this.handlerChainDecl = HandlerChainDeclFactory.newInstance(this.eiClass, this.sbClass, this.getType());
      }

      JAnnotation var8 = this.sbClass.getAnnotation(BindingType.class);
      this.protocolBinding = JamUtil.getAnnotationStringValue(var8, "value");
      if (StringUtil.isEmpty(this.protocolBinding)) {
         this.protocolBinding = "http://schemas.xmlsoap.org/wsdl/soap/http";
      }

      this.mtomEnabled = "http://schemas.xmlsoap.org/wsdl/soap/http?mtom=true".equals(this.protocolBinding) || "http://www.w3.org/2003/05/soap/bindings/HTTP/?mtom=true".equals(this.protocolBinding);
   }

   private static boolean isProviderBasedEndpoint(JClass var0) {
      JClass var1 = var0.forName(Provider.class.getName());
      return var1.isAssignableFrom(var0);
   }

   private String buildServiceName(JClass var1, JAnnotation var2) {
      String var3 = JamUtil.getAnnotationStringValue(var2, "serviceName");
      if (!StringUtil.isEmpty(var3)) {
         return var3;
      } else {
         return this.getType() == WebServiceType.JAXWS ? this.simpleName + "Service" : ClassUtil.getServiceName(var1, this.getParentServiceName());
      }
   }

   private String buildPortName(JAnnotation var1) {
      String var2 = JamUtil.getAnnotationStringValue(var1, "portName");
      if (!StringUtil.isEmpty(var2)) {
         return var2;
      } else if (this.getType() == WebServiceType.JAXWS) {
         String var3 = JamUtil.getAnnotationStringValue(var1, "name");
         if (!StringUtil.isEmpty(var3)) {
            return var3 + "Port";
         } else {
            return StringUtil.isEmpty(this.endPointInterface) ? this.getPortType().trim() + "Port" : this.simpleName + "Port";
         }
      } else {
         return this.getPortType().trim() + "SoapPort";
      }
   }

   public HandlerChainDecl getHandlerChainDecl() {
      return this.handlerChainDecl;
   }

   public String getName() {
      return this.name;
   }

   public String getServiceName() {
      return this.serviceName;
   }

   public QName getServiceQName() {
      return new QName(this.targetNamespace, this.serviceName);
   }

   public String getEndPointInterface() {
      return this.endPointInterface;
   }

   public String getPortComponentName() {
      if (this.providerBasedEndpoint) {
         return this.getClassName();
      } else {
         return StringUtil.isEmpty(this.endPointInterface) ? this.getPortType() : this.getSimpleName();
      }
   }

   private void setConversationalFlag(JClass var1) {
      this.conversational = var1.getAnnotation(Conversational.class) != null;
      JMethod[] var2 = var1.getMethods();

      for(int var3 = 0; !this.conversational && var3 < var2.length; ++var3) {
         this.conversational = var2[var3].getAnnotation(Conversation.class) != null;
      }

   }

   public String getPortType() {
      return this.name;
   }

   public String getPortTypeNamespaceURI() {
      return this.portTypeNamespaceURI;
   }

   public String getTargetNamespace() {
      return this.targetNamespace;
   }

   public String getWsdlLocation() {
      return this.wsdlLocation;
   }

   public String getClassName() {
      return this.className;
   }

   public String getSimpleName() {
      return this.simpleName;
   }

   public String getPackageName() {
      return this.packageName;
   }

   public QName getPortTypeQName() {
      return new QName(this.getPortTypeNamespaceURI(), this.getPortType());
   }

   public JClass getJClass() {
      return this.sbClass;
   }

   public JClass getEIClass() {
      return this.eiClass;
   }

   private String getParentServiceName() {
      return this.parentWebServiceDecl == null ? null : this.parentWebServiceDecl.getServiceQName().getLocalPart();
   }

   protected void initNamespaces(JAnnotation var1, JAnnotation var2) {
      this.portTypeNamespaceURI = JamUtil.getAnnotationStringValue(var2, "targetNamespace");
      this.targetNamespace = JamUtil.getAnnotationStringValue(var1, "targetNamespace");
      if (StringUtil.isEmpty(this.targetNamespace)) {
         if (!StringUtil.isEmpty(this.portTypeNamespaceURI) && this.getType() != WebServiceType.JAXWS) {
            this.targetNamespace = this.portTypeNamespaceURI;
         } else {
            this.targetNamespace = ClassUtil.getNamespaceFromClass(this.webServiceType, this.eiClass);
         }
      }

      if (StringUtil.isEmpty(this.portTypeNamespaceURI)) {
         this.portTypeNamespaceURI = this.targetNamespace;
      }

   }

   public boolean isConversational() {
      return this.conversational;
   }

   public QName getPortQName() {
      return this.portQName;
   }

   public String getPortName() {
      return this.portQName.getLocalPart();
   }

   public String getProtocolBinding() {
      return this.protocolBinding;
   }

   public boolean isMtomEnabled() {
      return this.mtomEnabled;
   }

   private String getPortTypeName(JAnnotation var1) {
      String var2 = JamUtil.getAnnotationStringValue(var1, "name");
      if (StringUtil.isEmpty(var2)) {
         var2 = ClassUtil.getDefaultName(this.eiClass, this.getParentServiceName());
      }

      return ClassUtil.normalizeClassName(var2);
   }

   public WebServiceType getType() {
      return this.webServiceType;
   }

   private static final class DelegatingLoader extends ClassLoader {
      private final ClassLoader loader;

      DelegatingLoader(ClassLoader var1, ClassLoader var2) {
         super(var2);
         this.loader = var1;
      }

      protected Class findClass(String var1) throws ClassNotFoundException {
         return this.loader.loadClass(var1);
      }

      protected URL findResource(String var1) {
         return this.loader.getResource(var1);
      }
   }
}
