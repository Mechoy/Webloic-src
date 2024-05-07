package weblogic.diagnostics.snmp.mib;

import java.beans.BeanInfo;
import java.beans.PropertyDescriptor;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.diagnostics.snmp.agent.SNMPAgent;
import weblogic.diagnostics.snmp.agent.SNMPAgentToolkit;
import weblogic.diagnostics.snmp.agent.SNMPAgentToolkitException;
import weblogic.diagnostics.snmp.agent.SNMPUtil;
import weblogic.management.provider.ManagementServiceClient;
import weblogic.management.provider.beaninfo.BeanInfoAccess;

public class MibGenerator implements MibConstants {
   private static final boolean DEBUG = true;
   private static Set supportedPackages = new HashSet();
   private static Set excludedAttributes = new HashSet();
   private static Set ignoredAttributes = new HashSet();
   private static Set supportedClasses = new HashSet();
   private static final DebugLogger logger;
   private static final String ROOT_OID = ".1.3.6.1.4.1.140.625";
   private static final int INCREMENT = 5;
   private static final int DEFAULT_MIB_GEN_PORT = 1160;

   public static void main(String[] var0) throws Exception {
      if (var0.length < 2) {
         printUsage();
         System.exit(1);
      }

      String var1 = var0[0];
      int var2 = Integer.parseInt(var0[1]);
      int var3 = 1160;
      if (var0.length > 2) {
         var3 = Integer.parseInt(var0[2]);
      }

      SNMPUtil.initializeTypeNamePrefixes(SUBSYSTEM_PREFIXES);
      WLSMibMetadata var4 = WLSMibMetadata.loadResource();
      SNMPAgent var5 = new SNMPAgent();
      var5.setUdpListenPort(var3);
      var5.initialize();
      SNMPAgentToolkit var6 = var5.getSNMPAgentToolkit();
      Calendar var7 = Calendar.getInstance();
      var7.setTime(new Date());
      String var8 = new String("" + var7.get(1));
      if (var7.get(2) + 1 < 10) {
         var8 = var8 + "0";
      }

      var8 = var8 + (var7.get(2) + 1);
      if (var7.get(5) < 10) {
         var8 = var8 + "0";
      }

      var8 = var8 + var7.get(5);
      if (var7.get(11) < 10) {
         var8 = var8 + "0";
      }

      var8 = var8 + var7.get(11);
      if (var7.get(12) < 10) {
         var8 = var8 + "0";
      }

      var8 = var8 + var7.get(12);
      var8 = var8 + "Z";
      var6.addModuleIdentityInfo(var8, "BEA Systems Inc.", "www.bea.com", "BEA", "BEA-WEBLOGIC-MIB", ".1.3.6.1.4.1.140.625", "wls");
      var6.createSNMPMibTables("BEA-WEBLOGIC-MIB");
      BeanInfoAccess var9 = ManagementServiceClient.getBeanInfoAccess();
      String[] var10 = var9.getSubtypes((String)null);

      for(int var11 = 0; var11 < var10.length; ++var11) {
         String var12 = var10[var11];
         if (!var12.endsWith("MBean")) {
            logger.debug("Not supported " + var12);
         } else {
            try {
               Class var13 = Class.forName(var12);
               String var14 = var13.getPackage().getName();
               if (!supportedPackages.contains(var14)) {
                  continue;
               }
            } catch (Exception var24) {
               logger.debug("Type not found", var24);
               continue;
            }

            String[] var27 = var9.getSubtypes(var12);
            boolean var29 = false;
            if (var27.length > 1) {
               ArrayList var15 = new ArrayList();
               Collections.addAll(var15, var27);
               logger.debug("Found subtypes " + var15 + " for type " + var12);
               var29 = true;
            }

            logger.debug("Found type " + var12);
            BeanInfo var31 = var9.getBeanInfoForInterface(var10[var11], false, (String)null);
            Object var16 = var31.getBeanDescriptor().getValue("deprecated");
            if (var16 != null) {
               logger.debug("Deprecated type " + var12 + " " + var16);
            } else {
               Boolean var17 = (Boolean)var31.getBeanDescriptor().getValue("abstract");
               if (var17 != null && var17) {
                  logger.debug("Abstract type " + var12);
               } else {
                  Boolean var18 = (Boolean)var31.getBeanDescriptor().getValue("exclude");
                  if (var18 != null && var18) {
                     logger.debug("Excluded type " + var12);
                  } else {
                     String var19 = (String)var31.getBeanDescriptor().getValue("obsolete");
                     if (var19 != null && var19.length() > 0) {
                        logger.debug("Obsolete type " + var12 + " " + var19);
                     } else {
                        List var20 = getPropertyDescriptorsForSNMP(var12, var31);
                        String var21 = var4.getSNMPTableName(var12);
                        if (var21 == null && !var29) {
                           logger.debug("Missing type from mib " + var12);
                           var21 = getTableNameForType(var12);
                           logger.debug("Defining table " + var21);
                           String var22 = ".1.3.6.1.4.1.140.625." + var2;
                           var2 += 5;
                           String var23 = var31.getBeanDescriptor().getShortDescription();
                           var23 = SNMPUtil.stripHtmlTags(var23);
                           logger.debug("Table desc for " + var21 + " = " + var23);
                           var6.createSNMPTable("BEA-WEBLOGIC-MIB", var21, var23, var22, var20);
                        } else {
                           updateAttributesForType(var6, var4, var21, var20);
                        }
                     }
                  }
               }
            }
         }
      }

      String var25 = var6.outputMIBModule("BEA-WEBLOGIC-MIB");
      FileOutputStream var26 = new FileOutputStream(var1);
      OutputStreamWriter var28 = new OutputStreamWriter(var26);
      BufferedWriter var30 = new BufferedWriter(var28);
      var30.write(var25);
      var30.flush();
      var30.close();
   }

   private static void printUsage() {
      StringBuilder var0 = new StringBuilder();
      var0.append("java weblogic.diagnostics.snmp.mib.MibGenerator <MIB filename> <Starting index for new tables> [port]");
      System.out.println(var0.toString());
   }

   private static void updateAttributesForType(SNMPAgentToolkit var0, WLSMibMetadata var1, String var2, List var3) throws SNMPAgentToolkitException {
      WLSMibTableColumnsMetadata var4 = var1.getColumnsMetadataForSNMPTable(var2);
      if (var4 == null) {
         logger.debug("No metadata found for table " + var2);
      } else {
         Map var5 = var4.getAttributeColumnMap();
         Map var6 = var4.getAttributeColumnMap();
         Iterator var7 = var3.iterator();

         String var9;
         String var10;
         while(var7.hasNext()) {
            PropertyDescriptor var8 = (PropertyDescriptor)var7.next();
            var9 = var8.getName();
            if (!var5.containsKey(var9)) {
               var10 = SNMPUtil.getColumnNameForAttribute(var2, var9);
               logger.debug("Missing attribute " + var9 + " snmp col name = " + var10);
               if (var10.length() > 64) {
                  logger.debug("SNMP Column name " + var10 + " length is larger than 64 characters, looking for an OVERRIDEN name");
                  if (WLSMibMetadataBuilder.isColumnNameOverridden(var10)) {
                     var10 = WLSMibMetadataBuilder.getColumnNameOverride(var10);
                  } else {
                     System.out.println("Length of SNMP attribute " + var10 + " is greater than 64 characters in violation of SNMP standard. Please add shorter name override to the WLSMibMetadata.OVERRIDES map");
                     System.exit(1);
                  }
               }

               String var11 = SNMPUtil.stripHtmlTags((String)var8.getValue("description"));
               var0.createSNMPColumn(var2, var10, var8.getPropertyType(), var11);
            }
         }

         HashSet var12 = new HashSet();
         Iterator var13 = var3.iterator();

         while(var13.hasNext()) {
            PropertyDescriptor var14 = (PropertyDescriptor)var13.next();
            var10 = var14.getName();
            var12.add(var10);
         }

         var13 = var5.keySet().iterator();

         while(var13.hasNext()) {
            var9 = (String)((String)var13.next());
            if (logger.isDebugEnabled()) {
               logger.debug("Evaluating removal of " + var9 + " from " + var2);
            }

            if (!var12.contains(var9) && !ignoredAttributes.contains(var9)) {
               if (logger.isDebugEnabled()) {
                  logger.debug("Removing column " + var5.get(var9) + " from " + var2);
               }

               var10 = (String)var5.get(var9);
               if (WLSMibMetadataBuilder.isColumnNameOverridden(var10)) {
                  var10 = WLSMibMetadataBuilder.getColumnNameOverride(var10);
               }

               var0.removeSNMPColumn(var2, var10);
            }
         }

         var0.completeTableEdit(var2);
      }
   }

   private static String getTableNameForType(String var0) {
      return SNMPUtil.convertTypeNameToSNMPTableName(var0);
   }

   private static List getPropertyDescriptorsForSNMP(String var0, BeanInfo var1) {
      ArrayList var2 = new ArrayList();
      PropertyDescriptor[] var3 = var1.getPropertyDescriptors();
      logger.debug("Discovering properties for " + var0);

      for(int var4 = 0; var4 < var3.length; ++var4) {
         PropertyDescriptor var5 = var3[var4];
         Class var6 = var5.getPropertyType();
         if (isClassSupported(var6)) {
            logger.debug("Found property " + var5.getName());
            Boolean var7 = (Boolean)var5.getValue("encrypted");
            if (var7 != null && var7.equals(Boolean.TRUE)) {
               logger.debug("Found encrypted property " + var5.getName());
            } else {
               var7 = (Boolean)var5.getValue("exclude");
               if (var7 != null && var7.equals(Boolean.TRUE)) {
                  logger.debug("Found excluded property " + var5.getName());
               } else {
                  Object var8 = var5.getValue("obsolete");
                  if (var8 != null) {
                     logger.debug("Found obsolete property " + var5.getName() + " for " + var0);
                  } else {
                     var7 = (Boolean)var5.getValue("internal");
                     if (var7 != null && var7.equals(Boolean.TRUE)) {
                        logger.debug("Found internal property " + var5.getName());
                     } else {
                        String var9 = var5.getName();
                        if (!excludedAttributes.contains(var9)) {
                           var2.add(var5);
                        }
                     }
                  }
               }
            }
         } else {
            logger.debug("Ignoring property " + var5.getName() + " of type " + var6);
         }
      }

      return var2;
   }

   private static boolean isClassSupported(Class var0) {
      if (var0.isPrimitive()) {
         return true;
      } else if (var0.isArray()) {
         return var0.getComponentType() == String.class;
      } else {
         return supportedClasses.contains(var0);
      }
   }

   static {
      supportedPackages.add("weblogic.management.configuration");
      supportedPackages.add("weblogic.management.runtime");
      excludedAttributes.add("Notes");
      ignoredAttributes.add("Index");
      ignoredAttributes.add("Parent");
      ignoredAttributes.add("ObjectName");
      supportedClasses.add(Character.class);
      supportedClasses.add(String.class);
      supportedClasses.add(Boolean.class);
      supportedClasses.add(Byte.class);
      supportedClasses.add(Short.class);
      supportedClasses.add(Integer.class);
      supportedClasses.add(Long.class);
      supportedClasses.add(Float.class);
      supportedClasses.add(Double.class);
      supportedClasses.add(Date.class);
      logger = DebugLogger.getDebugLogger("DebugSNMPMib");
   }
}
