package weblogic.wsee.wsdl;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.xml.namespace.QName;
import weblogic.wsee.tools.WsBuildException;
import weblogic.wsee.tools.wsdlc.WsdlcUtils;
import weblogic.wsee.wsdl.builder.WsdlBindingOperationBuilder;
import weblogic.wsee.wsdl.builder.WsdlMessageBuilder;
import weblogic.wsee.wsdl.builder.WsdlOperationBuilder;
import weblogic.wsee.wsdl.builder.WsdlServiceBuilder;
import weblogic.wsee.wsdl.internal.WsdlDefinitionsImpl;

public final class Callback81WsdlExtracter {
   private static void saveUpdatedWsdl(WsdlDefinitionsImpl var0, File var1) throws WsBuildException, WsdlException {
      var0.specialModeForCallback81Wsdl = true;

      try {
         var0.writeToFile(var1);
      } catch (IOException var3) {
         throw new WsBuildException(var3);
      }
   }

   public static File extract(String var0, String var1, File var2) throws WsBuildException {
      try {
         WsdlDefinitions var3 = WsdlFactory.getInstance().parse(var0);
         WsdlPort var4 = null;
         var4 = extractWsdlPort(var3, var1);

         assert var4 != null;

         extractBinding(var3, var4);
         extractPortType(var3, var4);
         Set var5 = extractOperations(var4);
         extractBindingOperation(var4);
         extractWsdlMessages(var3, var5);
         updateServiceNameAndAddressInfo(var3, var4);
         File var6 = new File(var2, var1 + ".wsdl");
         saveUpdatedWsdl((WsdlDefinitionsImpl)var3, var6);
         return var6;
      } catch (WsdlException var7) {
         throw new WsBuildException(var7);
      }
   }

   private static void updateServiceNameAndAddressInfo(WsdlDefinitions var0, WsdlPort var1) {
      WsdlServiceBuilder var2 = (WsdlServiceBuilder)var0.getServices().values().iterator().next();
      var2.setName(var1.getName());
      if ("jms".equalsIgnoreCase(var1.getBinding().getTransportProtocol())) {
         var1.getExtensions().remove("SOAP11-address");
         var1.getExtensions().remove("SOAP12-address");
      }

   }

   private static void extractWsdlMessages(WsdlDefinitions var0, Set<WsdlMessage> var1) {
      Iterator var2 = var0.getMessages().values().iterator();

      while(var2.hasNext()) {
         WsdlMessage var3 = (WsdlMessage)var2.next();
         if (!var1.contains(var3)) {
            var2.remove();
         }
      }

   }

   private static void extractBindingOperation(WsdlPort var0) {
      Iterator var1 = var0.getBinding().getOperations().values().iterator();

      while(var1.hasNext()) {
         WsdlBindingOperationBuilder var2 = (WsdlBindingOperationBuilder)var1.next();
         if (var0.getBinding().getPortType().getOperations().get(var2.getName()) == null) {
            var1.remove();
         } else {
            var2.flipCallbackInputAndOutput();
            var2.getExtensions().remove("cwPhase");
         }
      }

   }

   private static Set<WsdlMessage> extractOperations(WsdlPort var0) {
      Iterator var1 = var0.getBinding().getPortType().getOperations().values().iterator();
      HashSet var2 = new HashSet();

      while(var1.hasNext()) {
         WsdlOperationBuilder var3 = (WsdlOperationBuilder)var1.next();
         if (!var3.isWLW81CallbackOperation()) {
            var1.remove();
         } else {
            WsdlMessageBuilder var4 = var3.getInput();
            WsdlMessageBuilder var5 = var3.getOutput();
            if (var5 != null) {
               var2.add(var5);
            }

            if (var4 != null) {
               var2.add(var4);
            }

            var2.addAll(var3.getFaults().values());
            var3.flipCallbackInputAndOutputParts();
         }
      }

      return var2;
   }

   private static void extractPortType(WsdlDefinitions var0, WsdlPort var1) {
      Iterator var2 = var0.getPortTypes().values().iterator();

      while(var2.hasNext()) {
         WsdlPortType var3 = (WsdlPortType)var2.next();
         if (!var3.equals(var1.getBinding().getPortType())) {
            var2.remove();
         }
      }

   }

   private static void extractBinding(WsdlDefinitions var0, WsdlPort var1) {
      Iterator var2 = var0.getBindings().values().iterator();

      while(var2.hasNext()) {
         WsdlBinding var3 = (WsdlBinding)var2.next();
         if (!var3.equals(var1.getBinding())) {
            var2.remove();
         }
      }

   }

   private static WsdlPort extractWsdlPort(WsdlDefinitions var0, String var1) {
      WsdlPort var2 = null;
      Iterator var3 = var0.getServices().values().iterator();

      while(var3.hasNext()) {
         WsdlService var4 = (WsdlService)var3.next();
         if (var2 != null) {
            var3.remove();
            break;
         }

         Iterator var5 = var4.getPorts().values().iterator();

         while(var5.hasNext()) {
            WsdlPort var6 = (WsdlPort)var5.next();
            if (WsdlcUtils.equalsNSOptional(QName.valueOf(var1), var6.getName())) {
               var2 = var6;
            } else {
               var5.remove();
            }
         }
      }

      return var2;
   }

   public static void main(String[] var0) throws Exception {
      System.out.println(extract(var0[0], var0[1], new File(var0[2])).getCanonicalPath());
   }
}
