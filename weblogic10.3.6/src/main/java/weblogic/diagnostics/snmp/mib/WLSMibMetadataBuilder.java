package weblogic.diagnostics.snmp.mib;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import weblogic.diagnostics.snmp.agent.SNMPAgent;
import weblogic.diagnostics.snmp.agent.SNMPAgentToolkitException;
import weblogic.i18n.logging.NonCatalogLogger;

public class WLSMibMetadataBuilder implements MibConstants {
   private static final NonCatalogLogger LOGGER = new NonCatalogLogger("WLSMibMetadataBuilder");
   private static final int DEFAULT_MIB_METADATA_BUILDER_PORT = 1260;

   public static void main(String[] var0) throws SNMPAgentToolkitException, WLSMibMetadataException {
      SNMPAgent var1 = new SNMPAgent();
      int var2 = var0.length;
      if (var2 > 0) {
         var1.setMibBasePath(var0[0]);
      }

      if (var2 > 1) {
         var1.setMibModules(var0[1]);
      }

      if (var2 > 2) {
         var1.setRootOidNode(var0[2]);
      }

      SNMPExtensionProviderBuildPlugin var3 = null;
      String var4;
      if (var2 > 3) {
         var4 = var0[3];

         try {
            Class var5 = Class.forName(var4);
            var3 = (SNMPExtensionProviderBuildPlugin)var5.newInstance();
         } catch (Exception var12) {
            LOGGER.error("Invalid build plugin class: " + var4, var12);
         }
      }

      var4 = "WLSMibMetadata.dat";
      if (var2 > 4) {
         var4 = var0[4];
      }

      int var13 = 1260;
      if (var2 > 5) {
         var13 = Integer.parseInt(var0[5]);
      }

      if (var13 > 0) {
         var1.setUdpListenPort(var13);
      }

      var1.initialize();
      Map var6 = var1.getSNMPTablesMetadata();
      WLSMibMetadata var7 = new WLSMibMetadata();
      Iterator var8 = var6.keySet().iterator();

      while(var8.hasNext()) {
         String var9 = (String)var8.next();
         String[] var10 = (String[])((String[])var6.get(var9));
         buildTypeInfo(var3, var7, var9, var10);
      }

      try {
         saveToDisk(var7, var4);
         if (DEBUG_LOGGER.isDebugEnabled()) {
            DEBUG_LOGGER.debug("Writing to disk " + var7);
         }

      } catch (Throwable var11) {
         throw new WLSMibMetadataException(var11);
      }
   }

   private static void buildTypeInfo(SNMPExtensionProviderBuildPlugin var0, WLSMibMetadata var1, String var2, String[] var3) throws WLSMibMetadataException {
      int var4 = var2.lastIndexOf("Table");
      String var5 = null;
      if (DEBUG_LOGGER.isDebugEnabled()) {
         DEBUG_LOGGER.debug("Processing snmp table " + var2);
      }

      String var6 = var2.substring(0, var4);
      if (WLSMibMetadataBuilder.Initializer.OUTLIERS.containsKey(var6)) {
         var5 = (String)WLSMibMetadataBuilder.Initializer.OUTLIERS.get(var6);
         if (DEBUG_LOGGER.isDebugEnabled()) {
            DEBUG_LOGGER.debug("Found WLS type " + var5 + " for " + var2);
         }
      } else {
         ArrayList var7 = new ArrayList();
         Collections.addAll(var7, (Object[])SUBSYSTEM_PREFIXES);
         if (var6.startsWith("wlssnmp")) {
            var6 = var6.substring(3);
         }

         String var8 = findSubsystemPrefix(var7, var6);
         int var9 = var8.length();
         String var10 = var8;
         String var11 = var8.toUpperCase();
         if (var6.length() > var9) {
            char var12 = var6.charAt(var9);
            if (Character.isLowerCase(var12)) {
               var10 = var8 + var12;
               char var13 = Character.toUpperCase(var12);
               var11 = var11 + var13;
            }
         }

         String var15 = var6.replaceFirst(var10, var11);
         if (DEBUG_LOGGER.isDebugEnabled()) {
            DEBUG_LOGGER.debug("Processed cheese = " + var15);
         }

         var5 = getFullyQualifiedClassName(var15 + "MBean");
         if (var5 == null) {
            if (DEBUG_LOGGER.isDebugEnabled()) {
               DEBUG_LOGGER.debug("Using build plugin to fine type for " + var2);
            }

            var5 = var0.getFullyQualifiedClassName(var15);
         }

         if (var5 == null) {
            throw new WLSMibMetadataException("Did not find type for " + var2);
         }

         if (DEBUG_LOGGER.isDebugEnabled()) {
            DEBUG_LOGGER.debug("Found type " + var5 + " for " + var2);
         }
      }

      WLSMibTableColumnsMetadata var14 = getColumnsMetadata(var6, var3);
      validateAttributes(var5, var14.getAttributeNames());
      if (DEBUG_LOGGER.isDebugEnabled()) {
         DEBUG_LOGGER.debug("Validated WLS type " + var5 + " for " + var2);
      }

      var1.snmpTableNameToWLSTypeName.put(var2, var5);
      var1.wlsTypeNameToSNMPTableName.put(var5, var2);
      var1.snmpTableNameToColumns.put(var2, var14);
      var1.wlsTypeNameToColumns.put(var5, var14);
   }

   private static void saveToDisk(WLSMibMetadata var0, String var1) throws IOException {
      FileOutputStream var2 = new FileOutputStream(var1);
      ObjectOutputStream var3 = new ObjectOutputStream(var2);
      var3.writeObject(var0);
      LOGGER.notice("Generated MIB metadata to " + var1);
   }

   private static void validateAttributes(String var0, Iterator var1) throws IllegalArgumentException {
      if (DEBUG_LOGGER.isDebugEnabled()) {
         DEBUG_LOGGER.debug("Validating type " + var0);
      }

      Class var2 = null;

      try {
         var2 = Class.forName(var0);
      } catch (Exception var8) {
         throw new IllegalArgumentException(var8);
      }

      while(var1.hasNext()) {
         String var3 = (String)var1.next();
         if (DEBUG_LOGGER.isDebugEnabled()) {
            DEBUG_LOGGER.debug("Validating attribute " + var0 + "." + var3);
         }

         Method var4 = null;

         try {
            var4 = var2.getMethod("get" + var3);
            if (DEBUG_LOGGER.isDebugEnabled()) {
               DEBUG_LOGGER.debug("Found getter " + var4.getName() + " on type " + var0);
            }

            if (var4 != null) {
               Class var5 = var4.getReturnType();
               if (DEBUG_LOGGER.isDebugEnabled()) {
                  DEBUG_LOGGER.debug("Attribute" + var0 + "." + var3 + " return type = " + var5);
               }
            }
         } catch (Exception var7) {
            try {
               var4 = var2.getMethod("is" + var3);
               if (DEBUG_LOGGER.isDebugEnabled()) {
                  DEBUG_LOGGER.debug("Found getter " + var4.getName() + " on type " + var0);
               }
            } catch (Exception var6) {
               System.err.println("No attribute " + var3 + " defined on " + var0);
            }
         }
      }

   }

   private static WLSMibTableColumnsMetadata getColumnsMetadata(String var0, String[] var1) {
      ArrayList var2 = new ArrayList();

      for(int var3 = 0; var3 < var1.length; ++var3) {
         String var4 = var1[var3];
         if (WLSMibMetadataBuilder.Initializer.OVERRIDES_REVERSE.containsKey(var4)) {
            var4 = (String)WLSMibMetadataBuilder.Initializer.OVERRIDES_REVERSE.get(var1[var3]);
         }

         int var5 = var0.length();
         if (var0.startsWith("snmp") && var1[var3].startsWith("wlssnmp")) {
            var5 += 3;
         }

         String var6 = var4.substring(var5);
         var2.add(new String[]{var1[var3], var6});
         if (DEBUG_LOGGER.isDebugEnabled()) {
            DEBUG_LOGGER.debug("Discovered attribute " + var6 + " for type " + var0 + " , snmp column name = " + var1[var3]);
         }
      }

      return new WLSMibTableColumnsMetadata(var2);
   }

   private static String getFullyQualifiedClassName(String var0) {
      String var1;
      try {
         var1 = "weblogic.management.runtime." + var0;
         Class.forName(var1);
         return var1;
      } catch (Exception var3) {
         try {
            var1 = "weblogic.management.configuration." + var0;
            Class.forName(var1);
            return var1;
         } catch (Exception var2) {
            return null;
         }
      }
   }

   private static String findSubsystemPrefix(List var0, String var1) {
      String var2 = "";
      Iterator var3 = var0.iterator();

      String var4;
      do {
         if (!var3.hasNext()) {
            return var2;
         }

         var4 = (String)var3.next();
      } while(!var1.startsWith(var4));

      return var4;
   }

   public static boolean isColumnNameOverridden(String var0) {
      boolean var1 = WLSMibMetadataBuilder.Initializer.OVERRIDES.containsKey(var0);
      if (DEBUG_LOGGER.isDebugEnabled()) {
         DEBUG_LOGGER.debug("isColumnNameOverriden for column " + var0 + "::returning " + var1);
      }

      return var1;
   }

   public static String getColumnNameOverride(String var0) {
      String var1 = (String)WLSMibMetadataBuilder.Initializer.OVERRIDES.get(var0);
      if (DEBUG_LOGGER.isDebugEnabled()) {
         DEBUG_LOGGER.debug("getColumnNameOverride() for column " + var0 + "::returning " + var1);
      }

      return var1;
   }

   private static final class Initializer {
      private static final Map OUTLIERS = new HashMap() {
         {
            this.put("snmpAgent", "weblogic.management.configuration.SNMPAgentMBean");
            this.put("wsReliableDeliveryPolicy", "weblogic.management.configuration.WSReliableDeliveryPolicyMBean");
            this.put("wtctBridgeGlobal", "weblogic.management.configuration.WTCtBridgeGlobalMBean");
            this.put("wtctBridgeRedirect", "weblogic.management.configuration.WTCtBridgeRedirectMBean");
            this.put("jrockitRuntime", "weblogic.management.runtime.JRockitRuntimeMBean");
            this.put("xmlEntitySpecRegistry", "weblogic.management.configuration.XMLEntitySpecRegistryEntryMBean");
            this.put("xmlParserSelectRegistry", "weblogic.management.configuration.XMLParserSelectRegistryEntryMBean");
            this.put("componentRuntime", "weblogic.management.runtime.ComponentRuntimeMBean");
            this.put("commonLog", "weblogic.management.configuration.CommonLogMBean");
         }
      };
      private static final Map OVERRIDES = new HashMap() {
         {
            this.put("connectorConnectionPoolRuntimeConnectionsDestroyedByErrorTotalCount", "connectorConnectionPoolRuntimeConnectionsDestroyedByErrorTotalCt");
            this.put("connectorConnectionPoolRuntimeConnectionsDestroyedByShrinkingTotalCount", "connectorConnectionPoolRuntimeConsDestroyedByShrinkingTotalCount");
            this.put("singleSignOnServicesWantTransportLayerSecurityClientAuthentication", "singleSignOnServicesWantTransportLayerSecurityClientAtn");
            this.put("springTransactionManagerRuntimeGlobalRollbackOnParticipationFailure", "springTransactionManagerRuntimeGlobalRollbkOnParticipationFailure");
            this.put("transactionNameRuntimeTransactionReadOnlyOnePhaseCommittedTotalCount", "txNameRuntimeTxReadOnlyOnePhaseCommittedTotalCount");
            this.put("transactionNameRuntimeTransactionOneResourceOnePhaseCommittedTotalCount", "txNameRuntimeTxOneResourceOnePhaseCommittedTotalCount");
         }
      };
      private static final Map OVERRIDES_REVERSE = new HashMap() {
         {
            Iterator var1 = WLSMibMetadataBuilder.Initializer.OVERRIDES.entrySet().iterator();

            while(var1.hasNext()) {
               Map.Entry var2 = (Map.Entry)var1.next();
               this.put(var2.getValue(), var2.getKey());
            }

         }
      };
   }
}
