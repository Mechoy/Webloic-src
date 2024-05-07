package weblogic.diagnostics.snmp.mib;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class ConfigHelper {
   private static final String ATTR_CHANGE_RUNTIME_MBEANS = "DeploymentTaskRuntime,JTARuntime,JVMRuntime,ServerLifeCycleRuntime,ServerRuntime";
   /** @deprecated */
   private static final String ATTRCHANGERUNTIMEMBEANS = "DeploymentTaskRuntime,JTARuntime,JVMRuntime,ServerLifeCycleRuntime,ServerRuntime";
   private static Set allMBeanTypes = null;
   private static Set attrChangeMBeanTypes = null;
   private static boolean initialized = false;

   public static Set getMonitorMBeanTypes() throws WLSMibMetadataException {
      ensureInitialized();
      return allMBeanTypes;
   }

   public static Set getAttributeChangeMBeanTypes() throws WLSMibMetadataException {
      ensureInitialized();
      return attrChangeMBeanTypes;
   }

   private static void ensureInitialized() throws WLSMibMetadataException {
      if (!initialized) {
         WLSMibMetadata var0 = WLSMibMetadata.loadResource();
         Set var1 = var0.wlsTypeNameToSNMPTableName.keySet();
         allMBeanTypes = new HashSet();
         attrChangeMBeanTypes = new HashSet();

         String var4;
         for(Iterator var2 = var1.iterator(); var2.hasNext(); allMBeanTypes.add(var4)) {
            Object var3 = var2.next();
            var4 = var3.toString();
            var4 = var4.replaceAll("[a-z]*\\.", "");
            var4 = var4.substring(0, var4.lastIndexOf("MBean"));
            if (!var4.endsWith("Runtime")) {
               attrChangeMBeanTypes.add(var4);
            }
         }

         String[] var5 = "DeploymentTaskRuntime,JTARuntime,JVMRuntime,ServerLifeCycleRuntime,ServerRuntime".split(",");
         attrChangeMBeanTypes.addAll(Arrays.asList(var5));
         initialized = true;
      }

   }

   public static void validateMonitorMBeanType(String var0) throws IllegalArgumentException {
   }

   public static boolean isValidRuntimeMBeanType(String var0) {
      return false;
   }

   public static boolean isValidConfigMBeanType(String var0) {
      return false;
   }

   public static void validateAttributeChangeMBeanType(String var0) throws IllegalArgumentException {
   }

   public static void main(String[] var0) throws WLSMibMetadataException {
      System.out.println("AttributeChangeMBeanTypes=" + getAttributeChangeMBeanTypes());
      System.out.println("#####");
      System.out.println("MonitorMBeanTypes=" + getMonitorMBeanTypes());
   }
}
