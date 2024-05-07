package weblogic.management.configuration;

import javax.management.InvalidAttributeValueException;

public interface XMLRegistryMBean extends ConfigurationMBean {
   String getDocumentBuilderFactory();

   void setDocumentBuilderFactory(String var1) throws InvalidAttributeValueException;

   String getSAXParserFactory();

   void setSAXParserFactory(String var1) throws InvalidAttributeValueException;

   String getTransformerFactory();

   void setTransformerFactory(String var1) throws InvalidAttributeValueException;

   /** @deprecated */
   boolean addRegistryEntry(XMLRegistryEntryMBean var1);

   /** @deprecated */
   boolean removeRegistryEntry(XMLRegistryEntryMBean var1);

   /** @deprecated */
   XMLRegistryEntryMBean[] getRegistryEntries();

   /** @deprecated */
   void setRegistryEntries(XMLRegistryEntryMBean[] var1) throws InvalidAttributeValueException;

   XMLParserSelectRegistryEntryMBean createXMLParserSelectRegistryEntry(String var1);

   void destroyXMLParserSelectRegistryEntry(XMLParserSelectRegistryEntryMBean var1);

   boolean addParserSelectRegistryEntry(XMLParserSelectRegistryEntryMBean var1);

   boolean removeParserSelectRegistryEntry(XMLParserSelectRegistryEntryMBean var1);

   XMLParserSelectRegistryEntryMBean[] getXMLParserSelectRegistryEntries();

   /** @deprecated */
   XMLParserSelectRegistryEntryMBean[] getParserSelectRegistryEntries();

   void setParserSelectRegistryEntries(XMLParserSelectRegistryEntryMBean[] var1) throws InvalidAttributeValueException;

   boolean addEntitySpecRegistryEntry(XMLEntitySpecRegistryEntryMBean var1);

   boolean removeEntitySpecRegistryEntry(XMLEntitySpecRegistryEntryMBean var1);

   /** @deprecated */
   XMLEntitySpecRegistryEntryMBean[] getEntitySpecRegistryEntries();

   XMLEntitySpecRegistryEntryMBean[] getXMLEntitySpecRegistryEntries();

   XMLEntitySpecRegistryEntryMBean createXMLEntitySpecRegistryEntry(String var1);

   void destroyXMLEntitySpecRegistryEntry(XMLEntitySpecRegistryEntryMBean var1);

   void setEntitySpecRegistryEntries(XMLEntitySpecRegistryEntryMBean[] var1) throws InvalidAttributeValueException;

   String getWhenToCache();

   void setWhenToCache(String var1);

   boolean isHandleEntityInvalidation();

   void setHandleEntityInvalidation(boolean var1);

   XMLParserSelectRegistryEntryMBean findParserSelectMBeanByKey(String var1, String var2, String var3);

   XMLEntitySpecRegistryEntryMBean findEntitySpecMBeanByKey(String var1, String var2);
}
