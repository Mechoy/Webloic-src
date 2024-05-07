package weblogic.wsee.tools.clientgen;

import com.bea.util.jam.JAnnotation;
import com.bea.util.jam.JAnnotationValue;
import com.bea.util.jam.JClass;
import com.bea.util.jam.JMethod;
import com.bea.xbean.xb.xsdschema.SchemaDocument;
import com.bea.xml.XmlException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.jws.WebMethod;
import javax.xml.namespace.QName;
import weblogic.wsee.bind.buildtime.BuildtimeBindings;
import weblogic.wsee.bind.buildtime.internal.WLW81TylarSchemaAndJavaBindingsBuilderImpl;
import weblogic.wsee.tools.WsBuildException;
import weblogic.wsee.tools.source.JsService;
import weblogic.wsee.tools.source.Wlw81EndpointBuilder;
import weblogic.wsee.util.StringUtil;
import weblogic.wsee.wsdl.WsdlDefinitions;
import weblogic.wsee.wsdl.WsdlException;
import weblogic.wsee.wsdl.WsdlFactory;
import weblogic.wsee.wsdl.WsdlService;
import weblogic.wsee.wsdl.WsdlTypes;
import weblogic.wsee.wsdl.builder.WsdlDefinitionsBuilder;

public class ClientGenUtilForWLW81Types {
   public static void setupService(String var0, String var1, JClass var2, ProcessInfo var3, boolean var4) throws WsBuildException {
      if (var3 == null) {
         throw new WsBuildException("Invalid use of ClientGenUtilForWLW81Types.setupService. The ProcessInfo object must be non-null.");
      } else {
         try {
            WsdlDefinitionsBuilder var5 = (WsdlDefinitionsBuilder)WsdlFactory.getInstance().parse(var0);
            var3.setWsdlDefs(var5);
            QName var6 = ClientGenUtil.findServiceName(var5, var1, var0);
            setupBuildtimeBindingsFromScratch(var5, var6, var2, var3, var4);
            setupJsService(var5, var6, var3, var2);
         } catch (WsdlException var7) {
            throw new WsBuildException(var7);
         }
      }
   }

   public static BuildtimeBindings createBuildtimeBindings(WsdlDefinitionsBuilder var0, QName var1, JClass var2, File var3, boolean var4) throws WsBuildException {
      try {
         return createBindingProvider((WsdlService)var0.getServices().get(var1), var2, var3, var4);
      } catch (XmlException var6) {
         throw new WsBuildException(var6);
      } catch (IOException var7) {
         throw new WsBuildException(var7);
      }
   }

   public static void setupBuildtimeBindingsFromScratch(WsdlDefinitionsBuilder var0, QName var1, JClass var2, ProcessInfo var3, boolean var4) throws WsBuildException {
      BuildtimeBindings var5 = var3.getBuildtimeBindings();
      if (var5 == null) {
         var5 = createBuildtimeBindings(var0, var1, var2, var3.getDestDir(), var4);
         var3.setBuildtimeBindings(var5);
      }
   }

   private static BuildtimeBindings createBindingProvider(WsdlService var0, JClass var1, File var2, boolean var3) throws XmlException, IOException {
      WLW81TylarSchemaAndJavaBindingsBuilderImpl var4 = new WLW81TylarSchemaAndJavaBindingsBuilderImpl(var3);
      WsdlDefinitions var5 = var0.getDefinitions();
      WsdlTypes var6 = var5.getTypes();
      if (var6 != null) {
         SchemaDocument[] var7 = var6.getSchemaArray();

         for(int var8 = 0; var8 < var7.length; ++var8) {
            var4.addSchemaDocument(var7[var8]);
         }
      }

      var4.setServiceJClass(var1);
      var4.setWsdlService(var0);
      return var4.createBuildtimeBindings(var2);
   }

   public static void setupJsService(WsdlDefinitions var0, QName var1, ProcessInfo var2, JClass var3) throws WsBuildException {
      BuildtimeBindings var4 = var2.getBuildtimeBindings();
      if (var4 == null) {
         throw new WsBuildException("Invalid use of ClientGenUtil.setupJsService. ProcessInfo.getBuildtimeBindings() must return non-null.");
      } else {
         if (!var0.getServices().values().iterator().hasNext()) {
            var2.setPartialWsdl(true);
         }

         var2.setServiceName(var1.getLocalPart());
         Wlw81EndpointBuilder var5 = new Wlw81EndpointBuilder(var0, var4, var2.getPackageName());
         var5.setAlwaysUseDataHandlerForMimeTypes(true);
         var5.setServiceInterfaceJClass(var3);
         var5.setGenerateAsyncMethods(var2.isGenerateAsyncMethods());

         try {
            JsService var6 = var5.buildJsService(var1);
            if (var2.getCallbackJClass() != null) {
               ClientGenUtil.setCallbackMethodName(var6, var2.getCallbackJClass());
            }

            var2.setJsService(var6);
            QName var7 = ClientGenUtil.getJsCallbackServiceName(var0);
            if (var7 != null) {
               JsService var8 = var5.buildJsService(var7);
               var2.setJsCallbackService(var8);
            }

         } catch (WsdlException var9) {
            throw new WsBuildException("Failed to parse wsdl", var9);
         }
      }
   }

   private static Map buildOperationNameMap(JClass var0) {
      HashMap var1 = new HashMap();
      JMethod[] var2 = var0.getMethods();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         JMethod var4 = var2[var3];
         JAnnotation var5 = var4.getAnnotation(WebMethod.class);
         if (var5 != null) {
            JAnnotationValue var6 = var5.getValue("operationName");
            String var7 = null;
            if (var6 != null) {
               var7 = var6.asString();
            }

            if (StringUtil.isEmpty(var7)) {
               var7 = var4.getSimpleName();
            }

            var1.put(var4.getSimpleName(), var4.getSimpleName());
         }
      }

      return var1;
   }
}
