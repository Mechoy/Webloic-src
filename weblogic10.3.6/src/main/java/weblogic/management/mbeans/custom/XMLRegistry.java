package weblogic.management.mbeans.custom;

import weblogic.management.configuration.XMLEntitySpecRegistryEntryMBean;
import weblogic.management.configuration.XMLParserSelectRegistryEntryMBean;
import weblogic.management.configuration.XMLRegistryMBean;
import weblogic.management.provider.custom.ConfigurationMBeanCustomized;
import weblogic.management.provider.custom.ConfigurationMBeanCustomizer;

public final class XMLRegistry extends ConfigurationMBeanCustomizer {
   private static final long serialVersionUID = 3074913268437602278L;

   public XMLRegistry(ConfigurationMBeanCustomized var1) {
      super(var1);
   }

   public XMLParserSelectRegistryEntryMBean findParserSelectMBeanByKey(String var1, String var2, String var3) {
      XMLRegistryMBean var4 = (XMLRegistryMBean)((XMLRegistryMBean)this.getMbean());
      XMLParserSelectRegistryEntryMBean[] var5 = var4.getParserSelectRegistryEntries();

      for(int var6 = 0; var6 < var5.length; ++var6) {
         if (var1 != null && var1.equals(var5[var6].getPublicId())) {
            return var5[var6];
         }

         if (var2 != null && var2.equals(var5[var6].getSystemId())) {
            return var5[var6];
         }

         if (var3 != null && var3.equals(var5[var6].getRootElementTag())) {
            return var5[var6];
         }
      }

      return null;
   }

   public XMLEntitySpecRegistryEntryMBean findEntitySpecMBeanByKey(String var1, String var2) {
      XMLRegistryMBean var3 = (XMLRegistryMBean)((XMLRegistryMBean)this.getMbean());
      XMLEntitySpecRegistryEntryMBean[] var4 = var3.getEntitySpecRegistryEntries();

      for(int var5 = 0; var5 < var4.length; ++var5) {
         if (var1 != null && var1.equals(var4[var5].getPublicId())) {
            return var4[var5];
         }

         if (var2 != null && var2.equals(var4[var5].getSystemId())) {
            return var4[var5];
         }
      }

      return null;
   }

   public XMLParserSelectRegistryEntryMBean[] getParserSelectRegistryEntries() {
      return ((XMLRegistryMBean)((XMLRegistryMBean)this.getMbean())).getXMLParserSelectRegistryEntries();
   }

   public XMLEntitySpecRegistryEntryMBean[] getEntitySpecRegistryEntries() {
      return ((XMLRegistryMBean)((XMLRegistryMBean)this.getMbean())).getXMLEntitySpecRegistryEntries();
   }
}
