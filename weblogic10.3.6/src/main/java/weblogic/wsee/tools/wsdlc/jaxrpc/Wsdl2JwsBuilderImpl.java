package weblogic.wsee.tools.wsdlc.jaxrpc;

import com.bea.xml.XmlException;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import javax.xml.namespace.QName;
import weblogic.wsee.tools.ToolsUtil;
import weblogic.wsee.tools.WsBuildException;
import weblogic.wsee.tools.source.EndpointBuilder;
import weblogic.wsee.tools.source.JsClass;
import weblogic.wsee.tools.wsdlc.BaseWsdl2JwsBuilder;
import weblogic.wsee.tools.wsdlc.WsdlcUtils;
import weblogic.wsee.wsdl.WsdlBinding;
import weblogic.wsee.wsdl.WsdlException;
import weblogic.wsee.wsdl.WsdlPort;
import weblogic.wsee.wsdl.WsdlPortType;
import weblogic.wsee.wsdl.WsdlService;
import weblogic.xml.schema.binding.internal.NameUtil;

public class Wsdl2JwsBuilderImpl extends BaseWsdl2JwsBuilder<Options> {
   private static final String USAGE = "One of 'bindingName', 'portName' or 'serviceName' must be specified.";
   private Options options = new Options();

   public void setOptions(Options var1) {
      if (var1 == null) {
         this.options = new Options();
      } else {
         this.options = var1;
      }

   }

   protected void validate() throws WsBuildException {
      int var1 = 0;
      if (this.options.getBindingName() != null) {
         ++var1;
      }

      if (this.options.getServiceName() != null) {
         ++var1;
      }

      if (this.portName != null) {
         ++var1;
      }

      if (var1 > 1) {
         ToolsUtil.throwException("Only one of 'bindingName', 'portName' and 'serviceName' can be specified. Please specify only one of them", this.logger);
      }

      if (this.options.getImplTemplate() != null && this.destImplDir == null) {
         ToolsUtil.throwException("destImplDir must be specified when implTemplate is provided.", this.logger);
      }

   }

   protected WsdlService getService(WsdlPort var1) throws WsBuildException {
      WsdlService var2 = null;
      if (var1 != null) {
         var2 = var1.getService();
      } else {
         if (this.options.getServiceName() != null) {
            var2 = this.findService();
         } else {
            Iterator var3 = this.options.getDefinitions().getServices().values().iterator();
            if (!var3.hasNext()) {
               throw new WsBuildException("Wsdl has no services");
            }

            var2 = (WsdlService)var3.next();
            if (var3.hasNext()) {
               throw new WsBuildException("Wsdl has more than one service.  One of 'bindingName', 'portName' or 'serviceName' must be specified.");
            }
         }

         this.checkPortTypesSame(var2);
      }

      if (!var2.getPorts().values().iterator().hasNext()) {
         throw new WsBuildException("Service " + var2.getName() + " has no ports");
      } else {
         return var2;
      }
   }

   protected WsdlPort getRequestedPort() throws WsBuildException {
      return this.portName == null && this.options.getBindingName() == null ? null : this.findPort();
   }

   protected void executeImpl() throws WsBuildException {
      this.validate();
      JwsGenInfo var1 = this.buildJwsGenInfo();
      JwsGenerator var2 = new JwsGenerator(var1, this.logger, this.options.getCodeGenBaseData());
      var2.generate(this.destDir, var1.getServiceClassName(), this.options.getInterfaceTemplate(), (ClassLoader)null, "interface");
      if (this.destImplDir != null) {
         var2.generate(this.destImplDir, var1.getImplClassName(), this.options.getImplTemplate(), this.classLoader, "implementation");
      }

   }

   private void checkPortTypesSame(WsdlService var1) throws WsBuildException {
      QName var2 = null;
      Iterator var3 = var1.getPortTypes().iterator();

      while(var3.hasNext()) {
         WsdlPortType var4 = (WsdlPortType)var3.next();
         if (var2 == null) {
            var2 = var4.getName();
         } else if (!var2.equals(var4.getName())) {
            throw new WsBuildException("Service " + var1.getName() + " uses more than 1 port type.  " + "One of 'bindingName', 'portName' or 'serviceName' must be specified.");
         }
      }

   }

   private WsdlService findService() throws WsBuildException {
      Iterator var1 = this.options.getDefinitions().getServices().values().iterator();

      WsdlService var2;
      do {
         if (!var1.hasNext()) {
            throw new WsBuildException("Service " + this.options.getServiceName() + " not found in wsdl");
         }

         var2 = (WsdlService)var1.next();
      } while(!WsdlcUtils.equalsNSOptional(this.options.getServiceName(), var2.getName()));

      return var2;
   }

   private WsdlPort findPort() throws WsBuildException {
      Iterator var1 = this.options.getDefinitions().getServices().values().iterator();

      while(var1.hasNext()) {
         WsdlService var2 = (WsdlService)var1.next();
         Iterator var3 = var2.getPorts().values().iterator();

         while(var3.hasNext()) {
            WsdlPort var4 = (WsdlPort)var3.next();
            if (this.portName != null) {
               if (WsdlcUtils.equalsNSOptional(this.portName, var4.getName())) {
                  return var4;
               }
            } else if (this.options.getBindingName() != null && WsdlcUtils.equalsNSOptional(this.options.getBindingName(), var4.getBinding().getName())) {
               return var4;
            }
         }
      }

      if (this.options.getBindingName() != null) {
         throw new WsBuildException("No port using binding " + this.options.getBindingName() + " found in wsdl");
      } else {
         throw new WsBuildException("No port " + this.portName + " found in wsdl");
      }
   }

   private JsClass buildJsClass(boolean var1, WsdlService var2, WsdlPort var3) throws WsBuildException {
      Map var4 = null;

      EndpointBuilder var5;
      try {
         var5 = new EndpointBuilder(this.options.getDefinitions(), this.destDir, this.packageName, this.options.isJaxRPCWrappedArrayStyle(), var1, this.options.getTypeFamily(), (ClassLoader)null, this.bindingFiles, this.options.isAllowWrappedArrayForDocLiteral(), this.options.isSortSchemaTypes(), this.options.isFillIncompleteParameterOrderList(), this.options.isIncludeGlobalTypes());
         var5.setWlw81CallbackGen(this.options.isWlw81CallbackGen() && !this.options.isUpgraded81Jws());
         var4 = var5.build();

         assert var4.size() > 0 : "No endpoints build for " + this.wsdl;
      } catch (IOException var6) {
         throw new WsBuildException(var6);
      } catch (XmlException var7) {
         throw new WsBuildException(var7);
      } catch (WsdlException var8) {
         throw new WsBuildException(var8);
      }

      var5 = null;
      JsClass var9;
      if (var3 != null) {
         var9 = (JsClass)var4.get(var3.getBinding().getName());
      } else {
         var9 = this.getEndpointForService(var2, var4);
      }

      if (var9 == null) {
         throw new WsBuildException("No valid SOAP endpoint defined found. Please check if there is SOAP binding defined.");
      } else {
         return var1 && var9.isStyleMixed() ? this.buildJsClass(false, var2, var3) : var9;
      }
   }

   private JsClass getEndpointForService(WsdlService var1, Map<QName, JsClass> var2) throws WsBuildException {
      JsClass var3 = null;
      Iterator var4 = var1.getPorts().values().iterator();

      while(var4.hasNext()) {
         WsdlPort var5 = (WsdlPort)var4.next();
         JsClass var6 = (JsClass)var2.get(var5.getBinding().getName());
         if (var3 == null) {
            var3 = var6;
         } else if (!var3.isBindingEqual(var6)) {
            throw new WsBuildException("Port " + var5.getName() + " uses different binding that other ports in the service.  " + "One of 'bindingName', 'portName' or 'serviceName' must be specified.");
         }
      }

      return var3;
   }

   private JwsGenInfo buildJwsGenInfo() throws WsBuildException {
      WsdlPort var1 = this.getRequestedPort();
      WsdlService var2 = this.getService(var1);
      JsClass var3 = this.buildJsClass(this.options.isAutoDetectWrapped(), var2, var1);
      JwsGenInfo var4 = new JwsGenInfo();
      var4.setUpgraded81Jws(this.options.isUpgraded81Jws());
      var4.setCallback81(this.options.isWlw81CallbackGen());
      var4.setServiceClassName(this.getServiceClassName(var3) + (this.options.isWlw81CallbackGen() ? "Callback" : ""));
      if (var1 == null) {
         var4.setService(var2);
      } else {
         var4.setPort(var1);
      }

      var4.setBinding(var3);
      var4.setSOAPBindingInfo(new SOAPBindingInfo(var3, (WsdlBinding)this.options.getDefinitions().getBindings().get(var3.getBindingName())));
      var4.setWsdlLocation(this.wsdlLocation);
      var4.validate();
      return var4;
   }

   private String getServiceClassName(JsClass var1) {
      String var2 = null;
      if (this.portName != null) {
         var2 = this.portName.getLocalPart();
      } else {
         var2 = var1.getName();
      }

      return NameUtil.getJAXRPCClassName(var2);
   }
}
