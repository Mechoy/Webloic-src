package weblogic.wsee.tools.clientgen;

import com.bea.util.jam.JAnnotation;
import com.bea.util.jam.JAnnotationValue;
import com.bea.util.jam.JClass;
import com.bea.util.jam.JMethod;
import com.bea.xml.XmlException;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.jws.WebMethod;
import javax.xml.namespace.QName;
import weblogic.wsee.bind.TypeFamily;
import weblogic.wsee.bind.buildtime.BuildtimeBindings;
import weblogic.wsee.bind.buildtime.S2JBindingsBuilder;
import weblogic.wsee.bind.buildtime.TylarS2JBindingsBuilder;
import weblogic.wsee.callback.CallbackUtils;
import weblogic.wsee.tools.WsBuildException;
import weblogic.wsee.tools.source.EndpointBuilder;
import weblogic.wsee.tools.source.JsClass;
import weblogic.wsee.tools.source.JsMethod;
import weblogic.wsee.tools.source.JsService;
import weblogic.wsee.util.StringUtil;
import weblogic.wsee.wsdl.WsdlDefinitions;
import weblogic.wsee.wsdl.WsdlException;
import weblogic.wsee.wsdl.WsdlFactory;
import weblogic.wsee.wsdl.WsdlPartnerLinkType;
import weblogic.wsee.wsdl.WsdlPortType;
import weblogic.wsee.wsdl.WsdlService;
import weblogic.wsee.wsdl.builder.WsdlDefinitionsBuilder;
import weblogic.wsee.wsdl.builder.WsdlServiceBuilder;

public class ClientGenUtil {
   public static void setupService(String var0, String var1, ProcessInfo var2, boolean var3) throws WsBuildException {
      setupService(var0, var1, var2, var3, false, false, false);
   }

   public static void setupService(String var0, String var1, ProcessInfo var2, boolean var3, boolean var4, boolean var5, boolean var6) throws WsBuildException {
      setupService(var0, var1, var2, var3, var4, (File[])null, var5, var6, false);
   }

   public static void setupService(String var0, String var1, ProcessInfo var2, boolean var3, boolean var4, File[] var5, boolean var6, boolean var7, boolean var8) throws WsBuildException {
      if (var2 == null) {
         throw new WsBuildException("Invalid use of ClientGenUtil.setupService. The ProcessInfo object must be non-null.");
      } else {
         try {
            WsdlDefinitionsBuilder var9 = (WsdlDefinitionsBuilder)WsdlFactory.getInstance().parse(var0);
            var2.setWsdlDefs(var9);
            QName var10 = findServiceName(var9, var1, var0);
            setupBuildtimeBindingsFromScratch(var9, var10, var2, var3, var4, var5, var6, var7);
            setupJsService(var9, var10, var2, var3, var8);
         } catch (WsdlException var11) {
            throw new WsBuildException(var11);
         }
      }
   }

   public static void setupServiceWithExistingTypes(String var0, String var1, ProcessInfo var2, URI var3) throws WsBuildException {
      if (var2 == null) {
         throw new WsBuildException("Invalid use of ClientGenUtil.setupServiceWithExistingTypes. The ProcessInfo object must be non-null.");
      } else {
         try {
            WsdlDefinitions var4 = WsdlFactory.getInstance().parse(var0);
            var2.setWsdlDefs(var4);
            QName var5 = findServiceName(var4, var1, var0);
            BuildtimeBindings var6 = BuildtimeBindings.Loader.reloadBuildtimeBindings(var3, var2.getTypeFamily());
            var2.setBuildtimeBindings(var6);
            setupJsService(var4, var5, var2, false);
         } catch (WsdlException var7) {
            throw new WsBuildException(var7);
         } catch (XmlException var8) {
            throw new WsBuildException(var8);
         } catch (IOException var9) {
            throw new WsBuildException(var9);
         }
      }
   }

   public static BuildtimeBindings createBuildtimeBindings(WsdlDefinitionsBuilder var0, QName var1, File var2, TypeFamily var3, boolean var4, boolean var5, boolean var6) throws WsBuildException {
      return createBuildtimeBindings(var0, var1, var2, var3, var4, var5, var6, false, (File[])null);
   }

   public static BuildtimeBindings createBuildtimeBindings(WsdlDefinitionsBuilder var0, QName var1, File var2, TypeFamily var3, boolean var4, boolean var5, boolean var6, boolean var7, File[] var8) throws WsBuildException {
      return createBuildtimeBindings(var0, var1, var2, var3, var4, var5, var6, var7, true, var8, false, false);
   }

   public static BuildtimeBindings createBuildtimeBindings(WsdlDefinitionsBuilder var0, QName var1, File var2, TypeFamily var3, boolean var4, boolean var5, boolean var6, boolean var7, boolean var8, File[] var9, boolean var10, boolean var11) throws WsBuildException {
      try {
         WsdlServiceBuilder[] var12 = null;
         if (var1 != null) {
            WsdlServiceBuilder var13 = (WsdlServiceBuilder)var0.getServices().get(var1);
            WsdlServiceBuilder var14 = null;
            if (var13 == null) {
               throw new WsBuildException("Invalid service name (" + var1 + "). Could not find service in WSDL.");
            }

            QName var15 = getJsCallbackServiceName(var0);
            if (var15 != null) {
               var14 = (WsdlServiceBuilder)var0.getServices().get(var15);
            }

            if (var14 != null) {
               var12 = new WsdlServiceBuilder[]{var13, var14};
            } else {
               var12 = new WsdlServiceBuilder[]{var13};
            }
         }

         return createBindingProvider(var12, var0, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11);
      } catch (XmlException var16) {
         throw new WsBuildException(var16);
      } catch (IOException var17) {
         throw new WsBuildException(var17);
      }
   }

   public static void setupBuildtimeBindingsFromScratch(WsdlDefinitions var0, QName var1, ProcessInfo var2, boolean var3) throws WsBuildException {
      setupBuildtimeBindingsFromScratch(var0, var1, var2, var3);
   }

   public static void setupBuildtimeBindingsFromScratch(WsdlDefinitionsBuilder var0, QName var1, ProcessInfo var2, boolean var3, boolean var4, boolean var5, boolean var6) throws WsBuildException {
      setupBuildtimeBindingsFromScratch(var0, var1, var2, var3, var4, (File[])null, var5, var6);
   }

   public static void setupBuildtimeBindingsFromScratch(WsdlDefinitionsBuilder var0, QName var1, ProcessInfo var2, boolean var3, boolean var4, File[] var5, boolean var6) throws WsBuildException {
      setupBuildtimeBindingsFromScratch(var0, var1, var2, var3, var4, true, var5, var6, false);
   }

   public static void setupBuildtimeBindingsFromScratch(WsdlDefinitionsBuilder var0, QName var1, ProcessInfo var2, boolean var3, boolean var4, File[] var5, boolean var6, boolean var7) throws WsBuildException {
      setupBuildtimeBindingsFromScratch(var0, var1, var2, var3, var4, true, var5, var6, var7);
   }

   public static void setupBuildtimeBindingsFromScratch(WsdlDefinitionsBuilder var0, QName var1, ProcessInfo var2, boolean var3, boolean var4, boolean var5, File[] var6, boolean var7, boolean var8) throws WsBuildException {
      BuildtimeBindings var9 = var2.getBuildtimeBindings();
      if (var9 == null) {
         var9 = createBuildtimeBindings(var0, var1, var2.getDestDir(), var2.getTypeFamily(), var3, var2.isJaxRPCWrappedArrayStyle(), var2.isWriteJavaTypes(), var4, var5, var6, var7, var8);
         var2.setBuildtimeBindings(var9);
      }
   }

   public static void setupJsService(WsdlDefinitions var0, QName var1, ProcessInfo var2, boolean var3) throws WsBuildException {
      setupJsService(var0, var1, var2, var3, false);
   }

   public static void setupJsService(WsdlDefinitions var0, QName var1, ProcessInfo var2, boolean var3, boolean var4) throws WsBuildException {
      BuildtimeBindings var5 = var2.getBuildtimeBindings();
      if (var5 == null) {
         throw new WsBuildException("Invalid use of ClientGenUtil.setupJsService. ProcessInfo.getBuildtimeBindings() must return non-null.");
      } else {
         if (!var0.getServices().values().iterator().hasNext()) {
            var2.setPartialWsdl(true);
         }

         var2.setServiceName(var1.getLocalPart());
         EndpointBuilder var6 = new EndpointBuilder(var0, var5, var2.getPackageName());
         var6.setAutoDetectWrapped(var3);
         var6.setGenerateAsyncMethods(var2.isGenerateAsyncMethods());
         var6.setFillIncompleteParameterOrderList(var4);

         try {
            JsService var7 = var6.buildJsService(var1);
            if (var2.getCallbackJClass() != null) {
               setCallbackMethodName(var7, var2.getCallbackJClass());
            }

            var2.setPackageName(var6.getPackageName());
            var2.setJsService(var7);
            QName var8 = getJsCallbackServiceName(var0);
            if (var8 != null) {
               JsService var9 = var6.buildJsService(var8);
               var2.setJsCallbackService(var9);
            }

         } catch (WsdlException var10) {
            throw new WsBuildException("Failed to parse wsdl", var10);
         }
      }
   }

   private static void p(String var0) {
      System.out.println("[ClientGenUtil] " + var0);
   }

   public static QName getJsCallbackServiceName(WsdlDefinitions var0) throws WsBuildException {
      WsdlPartnerLinkType var1 = (WsdlPartnerLinkType)var0.getExtension("PartnerLinkType");
      if (var1 != null) {
         Iterator var2 = var0.getServices().values().iterator();

         while(var2.hasNext()) {
            WsdlService var3 = (WsdlService)var2.next();
            Iterator var4 = var3.getPortTypes().iterator();

            while(var4.hasNext()) {
               WsdlPortType var5 = (WsdlPortType)var4.next();
               QName var6 = null;

               try {
                  var6 = var1.getPortTypeName("Callback");
                  if (var6 != null && var5.getName().equals(var6)) {
                     return var3.getName();
                  }
               } catch (WsdlException var8) {
               }
            }
         }
      }

      return null;
   }

   protected static void setCallbackMethodName(JsService var0, JClass var1) throws WsBuildException {
      Map var2 = buildOperationNameMap(var1);
      Iterator var3 = var0.getEndpoints();
      if (!var3.hasNext()) {
         throw new WsBuildException("Callback service doesn't have an Endpoint.");
      } else {
         String var4 = null;

         while(var3.hasNext()) {
            JsClass var5 = (JsClass)var3.next();
            if (var4 == null) {
               var4 = var5.getQualifiedName();
            } else if (!var4.equals(var5.getQualifiedName())) {
               throw new WsBuildException("Callback service has endpoints with different names");
            }

            JsMethod[] var6 = var5.getMethods();
            int var7 = var6.length;

            for(int var8 = 0; var8 < var7; ++var8) {
               JsMethod var9 = var6[var8];
               String var10 = (String)var2.get(var9.getOperationName().getLocalPart());
               if (var10 == null) {
                  throw new WsBuildException("operation name " + var9.getOperationName() + " is not found in callback interface " + var1.getQualifiedName());
               }

               var9.setMethodName(var10);
            }
         }

      }
   }

   private static Map buildOperationNameMap(JClass var0) {
      HashMap var1 = new HashMap();
      JMethod[] var2 = var0.getMethods();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         JMethod var5 = var2[var4];
         String var6 = var5.getSimpleName();
         JAnnotation var7 = var5.getAnnotation(WebMethod.class);
         if (var7 != null) {
            var6 = getOperationName(var7, var5);
         }

         var1.put(var5.getSimpleName(), var6);
      }

      return var1;
   }

   private static String getOperationName(JAnnotation var0, JMethod var1) {
      JAnnotationValue var2 = var0.getValue("operationName");
      String var3 = null;
      if (var2 != null) {
         var3 = var2.asString();
      }

      if (StringUtil.isEmpty(var3)) {
         var3 = var1.getSimpleName();
      }

      return var3;
   }

   private static BuildtimeBindings createBindingProvider(WsdlServiceBuilder[] var0, WsdlDefinitionsBuilder var1, File var2, TypeFamily var3, boolean var4, boolean var5, boolean var6, boolean var7, File[] var8, boolean var9) throws XmlException, IOException {
      return createBindingProvider(var0, var1, var2, var3, var4, var5, var6, var7, true, var8, var9, false);
   }

   private static BuildtimeBindings createBindingProvider(WsdlServiceBuilder[] var0, WsdlDefinitionsBuilder var1, File var2, TypeFamily var3, boolean var4, boolean var5, boolean var6, boolean var7, boolean var8, File[] var9, boolean var10, boolean var11) throws XmlException, IOException {
      Object var12 = null;
      if (TypeFamily.TYLAR.equals(var3)) {
         TylarS2JBindingsBuilder var13 = S2JBindingsBuilder.Factory.createTylarBindingsBuilder();
         var13.setJaxRPCWrappedArrayStyle(var5);
         var13.setWriteJavaTypes(var6);
         var13.setXsdConfig(var9);
         var13.setUseJaxRpcRules(var8);
         var12 = var13;
      } else if (TypeFamily.XMLBEANS_APACHE.equals(var3)) {
         var12 = S2JBindingsBuilder.Factory.createXmlBeansApacheBindingsBuilder();
         ((S2JBindingsBuilder)var12).setXsdConfig(var9);
      } else if (TypeFamily.XMLBEANS.equals(var3)) {
         var12 = S2JBindingsBuilder.Factory.createXmlBeansBindingsBuilder();
         ((S2JBindingsBuilder)var12).setXsdConfig(var9);
      }

      return EndpointBuilder.setupBindingProviderWithServices((S2JBindingsBuilder)var12, var1, var2, var0, var4, var7, var10, var11);
   }

   public static QName findServiceName(WsdlDefinitions var0, String var1, String var2) throws WsBuildException {
      QName var3 = null;
      String var4 = var2 == null ? var0.getName() : var2;
      Iterator var5;
      if (var1 != null) {
         var3 = new QName(var0.getTargetNamespace(), var1);
         if (var0.getServices().get(var3) == null) {
            var5 = var0.getImportedWsdlDefinitions().iterator();

            do {
               if (!var5.hasNext()) {
                  throw new WsBuildException("Service \"" + var1 + "\" is not found in wsdl " + var4 + " or the wsdls imported by " + var4);
               }

               WsdlDefinitions var6 = (WsdlDefinitions)var5.next();
               var3 = new QName(var6.getTargetNamespace(), var1);
            } while(var0.getServices().get(var3) == null);

            return var3;
         }
      } else {
         var5 = var0.getServices().values().iterator();
         if (!var5.hasNext()) {
            throw new WsBuildException("No service found in wsdl " + var4);
         }

         WsdlService var7 = (WsdlService)var5.next();
         var3 = var7.getName();
         if (var5.hasNext()) {
            throw new WsBuildException("WSDL at \"" + var4 + "\" has more than one service definition, please specify a serviceName.");
         }
      }

      return var3;
   }

   public static void addJsCallbackServiceToProcessInfoIfWlw81CallbackPresent(String var0, String var1, ProcessInfo var2, boolean var3) throws WsBuildException {
      try {
         if (var2.getJsCallbackService() == null && CallbackUtils.has81StyleCallback(var2.getJsService())) {
            QName var4 = findServiceName(var2.getWsdlDefs(), var1, var0);
            EndpointBuilder var5 = new EndpointBuilder(var2.getWsdlDefs(), var2.getBuildtimeBindings(), var2.getPackageName());
            var5.setAutoDetectWrapped(var3);
            var5.setGenerateAsyncMethods(var2.isGenerateAsyncMethods());
            var5.setWlw81CallbackGen(true);
            JsService var6 = var5.buildJsService(var4);
            var2.setJsCallbackService(var6);
         }

      } catch (WsdlException var7) {
         throw new WsBuildException(var7);
      }
   }
}
