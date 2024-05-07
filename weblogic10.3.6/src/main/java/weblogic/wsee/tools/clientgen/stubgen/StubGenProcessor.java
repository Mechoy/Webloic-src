package weblogic.wsee.tools.clientgen.stubgen;

import com.bea.util.jam.JClass;
import java.io.File;
import java.io.PrintStream;
import java.util.HashSet;
import java.util.Iterator;
import weblogic.jws.wlw.UseWLW81BindingTypes;
import weblogic.wsee.tools.ToolsUtil;
import weblogic.wsee.tools.WsBuildException;
import weblogic.wsee.tools.clientgen.ProcessInfo;
import weblogic.wsee.tools.clientgen.jaxrpc.ClientGenProcessor;
import weblogic.wsee.tools.source.JsClass;
import weblogic.wsee.tools.source.JsPort;
import weblogic.wsee.tools.source.JsService;
import weblogic.wsee.util.jspgen.GenFactory;
import weblogic.wsee.util.jspgen.ScriptException;
import weblogic.wsee.wsdl.WsdlPortType;
import weblogic.wsee.wsdl.WsdlService;

public class StubGenProcessor implements ClientGenProcessor {
   private File destDir;
   private String packageName;
   private String wsdlLocation;
   ProcessInfo.StubInfo stubInfo;
   private boolean initialized = false;
   private boolean generatePolicyMethods;
   private boolean shouldConvertInnerClassString = false;

   public void process(ProcessInfo var1) throws WsBuildException {
      if (!var1.isPartialWsdl()) {
         JsService var2 = var1.getJsService();
         this.packageName = var1.getPackageName();
         this.destDir = var1.getDestDir();
         JClass var3 = var1.getCallbackJClass();
         if (var3 != null) {
            this.shouldConvertInnerClassString = var3.getAnnotation(UseWLW81BindingTypes.class) != null;
         }

         if (!this.shouldConvertInnerClassString) {
            JClass var4 = var1.getWebServiceJClass();
            if (var4 != null) {
               this.shouldConvertInnerClassString = var4.getAnnotation(UseWLW81BindingTypes.class) != null;
            }
         }

         System.out.println("Package name is " + this.packageName);
         System.out.println("DestDir is " + this.destDir.toString());
         this.generatePolicyMethods = var1.getGeneratePolicyMethods();
         this.init(var2.getWsdlService());
         var1.setStubInfo(this.stubInfo);
         String var9 = var1.getPackageName().replace('.', '/');
         this.wsdlLocation = var9 + "/" + var1.getWsdlFileName();
         this.generateServiceInterface(var2.getWsdlService());
         this.generateServiceImpl(var2.getWsdlService());
         HashSet var5 = new HashSet();
         Iterator var6 = var2.getPorts();

         while(var6.hasNext()) {
            JsPort var7 = (JsPort)var6.next();
            WsdlPortType var8 = var7.getWsdlPort().getPortType();
            if (var5.add(var8.getName())) {
               this.generateStubInterface(var8, var7.getEndpoint());
               this.generateStubImpl(var8, var7.getEndpoint(), var1.getCallbackJClass());
            }
         }

         this.generateExceptions(var2);
      }
   }

   private void generateServiceInterface(WsdlService var1) throws WsBuildException {
      PrintStream var2 = ToolsUtil.createJavaSourceStream(this.destDir, this.packageName, this.stubInfo.getServiceName());

      try {
         ServiceBase var3 = (ServiceBase)GenFactory.create("weblogic.wsee.tools.clientgen.stubgen.ServiceInterface");
         var3.setService(var1);
         var3.setWSDLLocation(this.wsdlLocation);
         var3.setPackageName(this.packageName);
         var3.setClassName(this.stubInfo.getServiceName());
         var3.setPortNameMap(this.stubInfo.getPortNameMap());
         var3.setPortTypeNameMap(this.stubInfo.getPortTypeNameMap());
         var3.setOutput(var2);
         var3.setGeneratePolicyMethods(this.generatePolicyMethods);
         var3.setShouldConvertInnerClassString(this.shouldConvertInnerClassString);
         var3.generate();
      } catch (ScriptException var7) {
         throw new WsBuildException("Service interface generation failed", var7);
      } finally {
         var2.close();
      }

   }

   private void generateServiceImpl(WsdlService var1) throws WsBuildException {
      PrintStream var2 = ToolsUtil.createJavaSourceStream(this.destDir, this.packageName, this.stubInfo.getServiceImplName());

      try {
         ServiceBase var3 = (ServiceBase)GenFactory.create("weblogic.wsee.tools.clientgen.stubgen.ServiceImpl");
         var3.setService(var1);
         var3.setWSDLLocation(this.wsdlLocation);
         var3.setPackageName(this.packageName);
         var3.setClassName(this.stubInfo.getServiceImplName());
         var3.setServiceName(this.stubInfo.getServiceName());
         var3.setPortNameMap(this.stubInfo.getPortNameMap());
         var3.setPortTypeNameMap(this.stubInfo.getPortTypeNameMap());
         var3.setStubNameMap(this.stubInfo.getStubNameMap());
         var3.setOutput(var2);
         var3.setGeneratePolicyMethods(this.generatePolicyMethods);
         var3.setShouldConvertInnerClassString(this.shouldConvertInnerClassString);
         var3.generate();
      } catch (ScriptException var7) {
         throw new WsBuildException("Service interface generation failed", var7);
      } finally {
         var2.close();
      }

   }

   private void generateStubInterface(WsdlPortType var1, JsClass var2) throws WsBuildException {
      String var3 = (String)this.stubInfo.getPortTypeNameMap().get(var1.getName().getLocalPart());
      PrintStream var4 = ToolsUtil.createJavaSourceStream(this.destDir, this.packageName, var3);

      try {
         StubBase var5 = (StubBase)GenFactory.create("weblogic.wsee.tools.clientgen.stubgen.StubInterface");
         var5.setPackageName(this.packageName);
         var5.setClassName(var3);
         var5.setEndpoint(var2);
         var5.setOutput(var4);
         var5.setShouldConvertInnerClassString(this.shouldConvertInnerClassString);
         var5.generate();
      } catch (ScriptException var9) {
         throw new WsBuildException("Service interface generation failed", var9);
      } finally {
         var4.close();
      }

   }

   private void generateStubImpl(WsdlPortType var1, JsClass var2, JClass var3) throws WsBuildException {
      String var4 = (String)this.stubInfo.getStubNameMap().get(var1.getName().getLocalPart());
      String var5 = (String)this.stubInfo.getPortTypeNameMap().get(var1.getName().getLocalPart());
      PrintStream var6 = ToolsUtil.createJavaSourceStream(this.destDir, this.packageName, var4);

      try {
         StubBase var7 = (StubBase)GenFactory.create("weblogic.wsee.tools.clientgen.stubgen.StubImpl");
         System.out.println("class name is " + var4);
         System.out.println("service class name is " + this.stubInfo.getServiceName());
         System.out.println("Porttype name is " + var5);
         System.out.println("service impl name is " + this.stubInfo.getServiceImplName());
         var7.setPackageName(this.packageName);
         var7.setClassName(var4);
         var7.setEndpoint(var2);
         var7.setPortTypeName(var5);
         var7.setOutput(var6);
         var7.setServiceClassName(this.stubInfo.getServiceName());
         var7.setPortNameMap(this.stubInfo.getPortNameMap());
         var7.setShouldConvertInnerClassString(this.shouldConvertInnerClassString);
         if (var3 != null) {
            var7.setCallbackInterface(var3.getQualifiedName());
            var7.setCallback(var3);
         }

         var7.generate();
      } catch (ScriptException var11) {
         throw new WsBuildException("Service interface generation failed", var11);
      } finally {
         var6.close();
      }

   }

   public void generateExceptions(JsService var1) throws WsBuildException {
   }

   private void init(WsdlService var1) throws WsBuildException {
      if (!this.initialized) {
         if (this.destDir == null) {
            throw new WsBuildException("Destination directory is not set.");
         } else if (this.packageName == null) {
            throw new WsBuildException("Package name is not set.");
         } else {
            this.stubInfo = new ProcessInfo.StubInfo(var1);
            this.initialized = true;
         }
      }
   }
}
