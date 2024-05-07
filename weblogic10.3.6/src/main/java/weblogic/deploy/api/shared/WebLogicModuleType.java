package weblogic.deploy.api.shared;

import java.io.File;
import java.io.IOException;
import javax.enterprise.deploy.shared.ModuleType;
import weblogic.application.ApplicationFileManager;
import weblogic.deploy.api.internal.SPIDeployerLogger;
import weblogic.deploy.api.internal.utils.ConfigHelper;
import weblogic.deploy.api.spi.config.DescriptorSupport;
import weblogic.deploy.api.spi.config.DescriptorSupportManager;
import weblogic.ejb.spi.EJBJarUtils;
import weblogic.utils.application.WarDetector;
import weblogic.utils.jars.VirtualJarFile;

public class WebLogicModuleType extends ModuleType {
   private int value;
   private static int base = findNextSlot();
   private static int numMods;
   private static final int WLS_OFFSET = 323;
   public static final WebLogicModuleType UNKNOWN;
   public static final WebLogicModuleType JMS;
   public static final WebLogicModuleType JDBC;
   public static final WebLogicModuleType INTERCEPT;
   public static final WebLogicModuleType CONFIG;
   public static final WebLogicModuleType SUBMODULE;
   public static final WebLogicModuleType WLDF;
   public static final WebLogicModuleType WSEE;
   public static final WebLogicModuleType SCA_COMPOSITE;
   public static final WebLogicModuleType SCA_JAVA;
   public static final WebLogicModuleType SCA_SPRING;
   public static final WebLogicModuleType SCA_BPEL;
   public static final WebLogicModuleType SCA_EXTENSION;
   public static final WebLogicModuleType COHERENCE_CLUSTER;
   private static final String[] WLStringTable = new String[]{"unknown", "jms", "jdbc", "intercept", "config", "submodule", "wldf", "webservice", "sca composite", "sca java", "sca spring", "sca bpel", "sca extension", "coherence cluster"};
   private static String[] stringTable = null;
   private static final WebLogicModuleType[] WLEnumValueTable;
   private static ModuleType[] enumValueTable = null;
   private static final String[] WLModuleExtension = new String[]{"", ".xml", ".xml", ".xml", ".xml", "", ".xml", ".xml", "", "", "", "", ""};
   protected static String[] standardUris;
   protected static String[] wlsUris;
   public static final String MODULETYPE_EAR;
   public static final String MODULETYPE_WAR;
   public static final String MODULETYPE_EJB;
   public static final String MODULETYPE_RAR;
   public static final String MODULETYPE_CAR;
   public static final String MODULETYPE_UNKNOWN;
   public static final String MODULETYPE_JMS;
   public static final String MODULETYPE_JDBC;
   public static final String MODULETYPE_INTERCEPT;
   public static final String MODULETYPE_CONFIG;
   public static final String MODULETYPE_SUBMODULE;
   public static final String MODULETYPE_WLDF;
   public static final String MODULETYPE_WSEE;
   public static final String MODULETYPE_COMPOSITE;
   public static final String MODULETYPE_JAVA;
   public static final String MODULETYPE_SPRING;
   public static final String MODULETYPE_BPEL;
   public static final String MODULETYPE_SCA_EXTENSION;
   public static final String MODULETYPE_COHERENCE_CLUSTER;

   public static ModuleType getTypeFromString(String var0) {
      if (var0 == null) {
         return UNKNOWN;
      } else {
         Object var1;
         if (var0.equalsIgnoreCase(MODULETYPE_EAR)) {
            var1 = ModuleType.EAR;
         } else if (var0.equalsIgnoreCase(MODULETYPE_EJB)) {
            var1 = ModuleType.EJB;
         } else if (var0.equalsIgnoreCase(MODULETYPE_WAR)) {
            var1 = ModuleType.WAR;
         } else if (var0.equalsIgnoreCase(MODULETYPE_RAR)) {
            var1 = ModuleType.RAR;
         } else if (var0.equalsIgnoreCase(MODULETYPE_CAR)) {
            var1 = ModuleType.CAR;
         } else if (var0.equalsIgnoreCase(MODULETYPE_JMS)) {
            var1 = JMS;
         } else if (var0.equalsIgnoreCase(MODULETYPE_JDBC)) {
            var1 = JDBC;
         } else if (var0.equalsIgnoreCase(MODULETYPE_INTERCEPT)) {
            var1 = INTERCEPT;
         } else if (var0.equalsIgnoreCase(MODULETYPE_CONFIG)) {
            var1 = CONFIG;
         } else if (var0.equalsIgnoreCase(MODULETYPE_SUBMODULE)) {
            var1 = SUBMODULE;
         } else if (var0.equalsIgnoreCase(MODULETYPE_WLDF)) {
            var1 = WLDF;
         } else if (var0.equalsIgnoreCase(MODULETYPE_WSEE)) {
            var1 = WSEE;
         } else if (var0.equalsIgnoreCase(MODULETYPE_COMPOSITE)) {
            var1 = SCA_COMPOSITE;
         } else if (var0.equalsIgnoreCase(MODULETYPE_JAVA)) {
            var1 = SCA_JAVA;
         } else if (var0.equalsIgnoreCase(MODULETYPE_SPRING)) {
            var1 = SCA_SPRING;
         } else if (var0.equalsIgnoreCase(MODULETYPE_BPEL)) {
            var1 = SCA_BPEL;
         } else if (var0.equalsIgnoreCase(MODULETYPE_SCA_EXTENSION)) {
            var1 = SCA_EXTENSION;
         } else if (var0.equalsIgnoreCase(MODULETYPE_COHERENCE_CLUSTER)) {
            var1 = COHERENCE_CLUSTER;
         } else {
            var1 = UNKNOWN;
         }

         return (ModuleType)var1;
      }
   }

   protected WebLogicModuleType(int var1) {
      super(var1);
      this.value = var1;
   }

   public int getValue() {
      return this.value;
   }

   public String[] getStringTable() {
      if (stringTable == null) {
         stringTable = new String[numMods];
         String[] var1 = super.getStringTable();

         int var2;
         for(var2 = 0; var2 < base; ++var2) {
            stringTable[var2] = var1[var2];
         }

         for(var2 = 323; var2 < numMods; ++var2) {
            stringTable[var2] = WLStringTable[var2 - 323];
         }
      }

      return stringTable;
   }

   public ModuleType[] getEnumValueTable() {
      if (enumValueTable == null) {
         enumValueTable = new ModuleType[numMods];
         ModuleType[] var1 = super.getEnumValueTable();

         int var2;
         for(var2 = 0; var2 < base; ++var2) {
            enumValueTable[var2] = var1[var2];
         }

         for(var2 = 323; var2 < numMods; ++var2) {
            enumValueTable[var2] = WLEnumValueTable[var2 - 323];
         }
      }

      return enumValueTable;
   }

   public String getModuleExtension() {
      return WLModuleExtension[this.getValue() - this.getOffset()];
   }

   public static ModuleType getModuleType(int var0) {
      if (var0 < base) {
         return ModuleType.getModuleType(var0);
      } else {
         return var0 >= 323 ? WLEnumValueTable[var0 - 323] : null;
      }
   }

   protected int getOffset() {
      return 323;
   }

   private static int findNextSlot() {
      int var1 = 0;

      while(true) {
         try {
            ModuleType var0 = ModuleType.getModuleType(var1);
         } catch (ArrayIndexOutOfBoundsException var3) {
            return var1;
         }

         ++var1;
      }
   }

   public static String getDDUri(int var0) {
      ModuleType var1 = getModuleType(var0);
      DescriptorSupport[] var2 = DescriptorSupportManager.getForModuleType(var1);
      return var1 != null && var2.length > 0 ? var2[0].getBaseURI() : null;
   }

   public static String[] getWLSDDUri(int var0) {
      ModuleType var1 = getModuleType(var0);
      if (var1 == null) {
         return new String[0];
      } else {
         DescriptorSupport[] var2 = DescriptorSupportManager.getForModuleType(var1);
         String[] var3 = new String[var2.length];

         for(int var4 = 0; var4 < var2.length; ++var4) {
            var3[var4] = var2[var4].getConfigURI();
         }

         return var3;
      }
   }

   public boolean equals(ModuleType var1) {
      return this.getValue() == var1.getValue();
   }

   public static int getModuleTypes() {
      return numMods;
   }

   public String toString() {
      return this.getStringTable()[this.getValue()];
   }

   public static ModuleType getFileModuleType(File var0) {
      if (var0 == null) {
         return null;
      } else if (var0.getName().endsWith(".xml")) {
         return getWebLogicModuleType(var0.getPath());
      } else {
         VirtualJarFile var1 = null;

         ModuleType var3;
         try {
            ApplicationFileManager var2 = ApplicationFileManager.newInstance(var0);
            if (!var2.isSplitDirectory()) {
               var1 = var2.getVirtualJarFile();
               var3 = getStandardModuleType(var0.toString(), var1);
               return var3;
            }

            var3 = ModuleType.EAR;
         } catch (IOException var14) {
            SPIDeployerLogger.logAppReadError(var0.toString(), var14.toString(), var14);
            var3 = null;
            return var3;
         } finally {
            if (var1 != null) {
               try {
                  var1.close();
               } catch (IOException var13) {
               }
            }

         }

         return var3;
      }
   }

   private static ModuleType getWebLogicModuleType(String var0) {
      if (var0.endsWith("-jms.xml")) {
         return JMS;
      } else if (var0.endsWith("-jdbc.xml")) {
         return JDBC;
      } else if (var0.endsWith("-interception.xml")) {
         return INTERCEPT;
      } else if (var0.endsWith("-coherence.xml")) {
         return COHERENCE_CLUSTER;
      } else {
         String var1 = var0.replace('\\', '/');
         DescriptorSupport[] var2 = DescriptorSupportManager.getForBaseURI(var1);
         if (var2.length > 0) {
            return var2[0].getModuleType();
         } else if (var0.endsWith("weblogic-diagnostics.xml")) {
            return WLDF;
         } else if (var0.endsWith("webservices.xml")) {
            return WSEE;
         } else {
            return var0.endsWith("web-services.xml") ? WSEE : CONFIG;
         }
      }
   }

   private static ModuleType getStandardModuleType(String var0, VirtualJarFile var1) {
      if (var1.getEntry("META-INF/application.xml") == null && !var1.getName().endsWith(".ear")) {
         if (var1.getEntry("META-INF/ejb-jar.xml") != null) {
            return ModuleType.EJB;
         } else if (var1.getEntry("WEB-INF/web.xml") != null) {
            return ModuleType.WAR;
         } else if (var1.getEntry("META-INF/ra.xml") != null) {
            return ModuleType.RAR;
         } else if (var1.getEntry("META-INF/weblogic-ra.xml") != null) {
            return ModuleType.RAR;
         } else if (var1.getEntry("META-INF/application-client.xml") != null) {
            return ModuleType.CAR;
         } else if (var1.getEntry("META-INF/weblogic-application-client.xml") != null) {
            return ModuleType.CAR;
         } else if (WarDetector.instance.suffixed(var0)) {
            return ModuleType.WAR;
         } else {
            try {
               if (EJBJarUtils.isEJB(var1)) {
                  return ModuleType.EJB;
               }
            } catch (IOException var3) {
            }

            return var1.getEntry("META-INF/sca-contribution.xml") != null ? SCA_COMPOSITE : null;
         }
      } else {
         return ModuleType.EAR;
      }
   }

   public static ModuleType getFileModuleType(String var0, VirtualJarFile var1) {
      if (var0 == null && var1 == null) {
         return null;
      } else if (var0 != null && var0.endsWith(".xml")) {
         return getWebLogicModuleType(var0);
      } else {
         return var1 != null ? getStandardModuleType(var0, var1) : null;
      }
   }

   public static String getFileModuleTypeAsString(File var0) {
      ConfigHelper.checkParam("File", var0);
      ModuleType var1 = getFileModuleType(var0);
      return var1 == null ? null : var1.toString();
   }

   static {
      int var0 = 323;
      UNKNOWN = new WebLogicModuleType(var0++);
      JMS = new WebLogicModuleType(var0++);
      JDBC = new WebLogicModuleType(var0++);
      INTERCEPT = new WebLogicModuleType(var0++);
      CONFIG = new WebLogicModuleType(var0++);
      SUBMODULE = new WebLogicModuleType(var0++);
      WLDF = new WebLogicModuleType(var0++);
      WSEE = new WebLogicModuleType(var0++);
      SCA_COMPOSITE = new WebLogicModuleType(var0++);
      SCA_JAVA = new WebLogicModuleType(var0++);
      SCA_SPRING = new WebLogicModuleType(var0++);
      SCA_BPEL = new WebLogicModuleType(var0++);
      SCA_EXTENSION = new WebLogicModuleType(var0++);
      COHERENCE_CLUSTER = new WebLogicModuleType(var0++);
      WLEnumValueTable = new WebLogicModuleType[]{UNKNOWN, JMS, JDBC, INTERCEPT, CONFIG, SUBMODULE, WLDF, WSEE, SCA_COMPOSITE, SCA_JAVA, SCA_SPRING, SCA_BPEL, SCA_EXTENSION, COHERENCE_CLUSTER};
      numMods = var0;
      MODULETYPE_EAR = ModuleType.EAR.toString();
      MODULETYPE_WAR = ModuleType.WAR.toString();
      MODULETYPE_EJB = ModuleType.EJB.toString();
      MODULETYPE_RAR = ModuleType.RAR.toString();
      MODULETYPE_CAR = ModuleType.CAR.toString();
      MODULETYPE_UNKNOWN = UNKNOWN.toString();
      MODULETYPE_JMS = JMS.toString();
      MODULETYPE_JDBC = JDBC.toString();
      MODULETYPE_INTERCEPT = INTERCEPT.toString();
      MODULETYPE_CONFIG = CONFIG.toString();
      MODULETYPE_SUBMODULE = SUBMODULE.toString();
      MODULETYPE_WLDF = WLDF.toString();
      MODULETYPE_WSEE = WSEE.toString();
      MODULETYPE_COMPOSITE = SCA_COMPOSITE.toString();
      MODULETYPE_JAVA = SCA_JAVA.toString();
      MODULETYPE_SPRING = SCA_SPRING.toString();
      MODULETYPE_BPEL = SCA_BPEL.toString();
      MODULETYPE_SCA_EXTENSION = SCA_EXTENSION.toString();
      MODULETYPE_COHERENCE_CLUSTER = COHERENCE_CLUSTER.toString();
   }
}
