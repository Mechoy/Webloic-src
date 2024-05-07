package weblogic.wsee.tools.clientgen.mapping;

import com.bea.staxb.buildtime.internal.bts.XmlTypeName;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.EditableDescriptorManager;
import weblogic.j2ee.descriptor.ConstructorParameterOrderBean;
import weblogic.j2ee.descriptor.ExceptionMappingBean;
import weblogic.j2ee.descriptor.JavaWsdlMappingBean;
import weblogic.j2ee.descriptor.MethodParamPartsMappingBean;
import weblogic.j2ee.descriptor.PackageMappingBean;
import weblogic.j2ee.descriptor.PortMappingBean;
import weblogic.j2ee.descriptor.ServiceEndpointInterfaceMappingBean;
import weblogic.j2ee.descriptor.ServiceEndpointMethodMappingBean;
import weblogic.j2ee.descriptor.ServiceInterfaceMappingBean;
import weblogic.j2ee.descriptor.WsdlMessageMappingBean;
import weblogic.j2ee.descriptor.WsdlReturnValueMappingBean;
import weblogic.wsee.bind.buildtime.BuildtimeBindings;
import weblogic.wsee.tools.WsBuildException;
import weblogic.wsee.tools.clientgen.ProcessInfo;
import weblogic.wsee.tools.clientgen.jaxrpc.ClientGenProcessor;
import weblogic.wsee.tools.source.JsFault;
import weblogic.wsee.tools.source.JsMethod;
import weblogic.wsee.tools.source.JsParameterType;
import weblogic.wsee.tools.source.JsPort;
import weblogic.wsee.util.NamespaceSpecifyingDescriptorManager;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.wsdl.WsdlMessage;
import weblogic.wsee.wsdl.WsdlOperation;
import weblogic.wsee.wsdl.WsdlPort;
import weblogic.wsee.wsdl.WsdlService;

public class JaxrpcMappingProcessor implements ClientGenProcessor {
   private static final boolean VERBOSE = Verbose.isVerbose(JaxrpcMappingProcessor.class);
   private ProcessInfo info;
   private JavaWsdlMappingBean jwmb;
   private String tns;

   public void process(ProcessInfo var1) throws WsBuildException {
      this.info = var1;
      this.tns = var1.getJsService().getWsdlService().getDefinitions().getTargetNamespace();
      EditableDescriptorManager var2 = new EditableDescriptorManager();
      this.jwmb = (JavaWsdlMappingBean)var2.createDescriptorRoot(JavaWsdlMappingBean.class).getRootBean();
      this.createPackageMapping();
      this.createBindingMapping();
      this.createServiceInterfaceMapping();
      Iterator var3 = var1.getJsService().getPorts();

      while(var3.hasNext()) {
         JsPort var4 = (JsPort)var3.next();
         this.createServiceEndpointInterfaceMapping(var4);
         this.createExceptionMapping(var4);
      }

      this.writeout();
   }

   private void createBindingMapping() {
      BuildtimeBindings var1 = this.info.getBuildtimeBindings();
      var1.generate109Descriptor(this.jwmb);
   }

   private void createPackageMapping() {
      PackageMappingBean var1 = this.jwmb.createPackageMapping();
      var1.setNamespaceURI(this.tns);
      var1.setPackageType(this.info.getPackageName());
   }

   private void createServiceInterfaceMapping() {
      if (!this.info.isPartialWsdl()) {
         ServiceInterfaceMappingBean var1 = this.jwmb.createServiceInterfaceMapping();
         var1.setServiceInterface(this.info.getPackageName() + "." + this.info.getStubInfo().getServiceName());
         WsdlService var2 = this.info.getJsService().getWsdlService();
         var1.setWsdlServiceName(var2.getName());
         Iterator var3 = this.info.getJsService().getPorts();

         while(var3.hasNext()) {
            JsPort var4 = (JsPort)var3.next();
            PortMappingBean var5 = var1.createPortMapping();
            String var6 = var4.getWsdlPort().getName().getLocalPart();
            var5.setPortName(var6);
            var5.setJavaPortName((String)this.info.getStubInfo().getPortNameMap().get(var6));
         }

      }
   }

   private void createServiceEndpointInterfaceMapping(JsPort var1) {
      ServiceEndpointInterfaceMappingBean var2 = this.jwmb.createServiceEndpointInterfaceMapping();
      WsdlPort var3 = var1.getWsdlPort();
      var2.setWsdlBinding(var3.getBinding().getName());
      var2.setWsdlPortType(var3.getPortType().getName());
      Map var4 = this.info.getStubInfo().getPortTypeNameMap();
      var2.setServiceEndpointInterface(this.info.getPackageName() + "." + var4.get(var3.getPortType().getName().getLocalPart()));
      JsMethod[] var5 = var1.getEndpoint().getMethods();

      for(int var6 = 0; var6 < var5.length; ++var6) {
         JsMethod var7 = var5[var6];
         ServiceEndpointMethodMappingBean var8 = var2.createServiceEndpointMethodMapping();
         WsdlOperation var9 = (WsdlOperation)var3.getPortType().getOperations().get(var7.getOperationName());
         this.fillMethodMapping(var8, var7, var9);
      }

   }

   private void createExceptionMapping(JsPort var1) {
      JsMethod[] var2 = var1.getEndpoint().getMethods();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         JsMethod var4 = var2[var3];
         if (VERBOSE) {
            Verbose.log((Object)(" createExceptionMapping for JsMethod " + var4.getMethodName()));
         }

         JsFault[] var5 = var4.getFaults();

         for(int var6 = 0; var6 < var5.length; ++var6) {
            ExceptionMappingBean var7 = this.jwmb.createExceptionMapping();
            QName var8 = var5[var6].getFaultMessage();
            String var9 = var5[var6].getPartName();
            String var10 = var5[var6].getJsr109MappingFileExceptionClass();
            var7.setExceptionType(var10);
            var7.setWsdlMessage(var8);
            var7.setWsdlMessagePartName(var9);
            List var11 = var5[var6].getConstructorElementNames();
            if (VERBOSE) {
               Verbose.log((Object)(" fault type " + var10 + ", message " + var8 + ", partName " + var9 + ", has " + var11.size() + " element names in its constructor "));
            }

            if (var11.size() > 0) {
               ConstructorParameterOrderBean var12 = var7.createConstructorParameterOrder();

               QName var14;
               for(Iterator var13 = var11.iterator(); var13.hasNext(); var12.addElementName(var14.getLocalPart())) {
                  var14 = (QName)var13.next();
                  if (VERBOSE) {
                     Verbose.log((Object)("  element name " + var14.getLocalPart() + " add to ConstructorParameterOrderBean"));
                  }
               }
            }
         }
      }

   }

   private void fillMethodMapping(ServiceEndpointMethodMappingBean var1, JsMethod var2, WsdlOperation var3) {
      var1.setJavaMethodName(var2.getMethodName());
      var1.setWsdlOperation(var2.getOperationName().getLocalPart());
      if (var2.isWrapped()) {
         var1.createWrappedElement();
      }

      int var4 = 0;
      JsParameterType[] var5 = var2.getArguments();

      for(int var6 = 0; var6 < var5.length; ++var6) {
         JsParameterType var7 = var5[var6];
         MethodParamPartsMappingBean var8 = var1.createMethodParamPartsMapping();
         var8.setParamPosition(var4++);
         if (var7.getNonHolderType() != null) {
            var8.setParamType(var7.getNonHolderType());
         } else {
            var8.setParamType(var7.getType());
         }

         this.fillParamMapping(var8, var3, var7);
      }

      this.createReturnMapping(var1, var3, var2);
   }

   private void fillParamMapping(MethodParamPartsMappingBean var1, WsdlOperation var2, JsParameterType var3) {
      WsdlMessageMappingBean var4 = var1.createWsdlMessageMapping();
      var4.setParameterMode(var3.getModeAsString());
      if (var3.isSoapHeader()) {
         var4.createSoapHeader();
      }

      WsdlMessage var5 = null;
      if (var3.getMode() == 1) {
         var5 = var2.getOutput();
      } else {
         var5 = var2.getInput();
      }

      var4.setWsdlMessage(var5.getName());
      if (!var3.getPartName().equals(XmlTypeName.ANY_ELEMENT_WILDCARD_ELEMENT_NAME.getLocalPart()) && !var3.getPartName().equals(XmlTypeName.ANY_ELEMENT_WILDCARD_ARRAY_ELEMENT_NAME.getLocalPart())) {
         var4.setWsdlMessagePartName(var3.getPartName());
      } else {
         var4.setWsdlMessagePartName("");
      }

   }

   private void createReturnMapping(ServiceEndpointMethodMappingBean var1, WsdlOperation var2, JsMethod var3) {
      if (var3.getReturnType() != null) {
         if (!"void".equals(var3.getReturnType().getType())) {
            WsdlReturnValueMappingBean var4 = var1.createWsdlReturnValueMapping();
            var4.setMethodReturnValue(var3.getReturnType().getType());
            var4.setWsdlMessage(var2.getOutput().getName());
            if (!var3.getReturnType().getPartName().equals(XmlTypeName.ANY_ELEMENT_WILDCARD_ELEMENT_NAME.getLocalPart()) && !var3.getReturnType().getPartName().equals(XmlTypeName.ANY_ELEMENT_WILDCARD_ARRAY_ELEMENT_NAME.getLocalPart())) {
               var4.setWsdlMessagePartName(var3.getReturnType().getPartName());
            } else {
               var4.setWsdlMessagePartName("");
            }

         }
      }
   }

   private void writeout() throws WsBuildException {
      File var1 = null;
      if (this.info.getMappingFileUri() == null) {
         String var2 = this.info.getPackageName().replace('.', '/');
         var2 = var2 + "/" + this.info.getMappingFileName();
         this.info.setMappingFileUri(var2);
         var1 = new File(this.info.getDestDir(), var2);
      } else {
         var1 = new File(this.info.getDestDir(), this.info.getMappingFileUri());
      }

      FileOutputStream var13 = null;

      try {
         var1.getParentFile().mkdirs();
         var13 = new FileOutputStream(var1);
         DescriptorBean var3 = (DescriptorBean)this.jwmb;
         (new NamespaceSpecifyingDescriptorManager()).writeDescriptorAsXML(var3.getDescriptor(), var13, "UTF-8");
      } catch (IOException var11) {
         throw new WsBuildException(var11);
      } finally {
         try {
            var13.close();
         } catch (Throwable var10) {
         }

      }

   }
}
