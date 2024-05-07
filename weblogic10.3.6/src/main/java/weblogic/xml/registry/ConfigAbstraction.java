package weblogic.xml.registry;

import java.beans.PropertyChangeListener;
import java.util.Enumeration;
import weblogic.j2ee.descriptor.wl.EntityMappingBean;
import weblogic.j2ee.descriptor.wl.XmlBean;
import weblogic.management.configuration.ConfigurationMBean;
import weblogic.management.configuration.XMLEntitySpecRegistryEntryMBean;
import weblogic.management.configuration.XMLParserSelectRegistryEntryMBean;
import weblogic.management.configuration.XMLRegistryEntryMBean;
import weblogic.management.configuration.XMLRegistryMBean;

class ConfigAbstraction {
   static ConfigAbstraction singularInstance = new ConfigAbstraction();

   static RegistryConfig getRegistryConfig(XMLRegistryMBean var0) {
      return singularInstance.new RegistryMBeanConfigImpl(var0);
   }

   static ParserSelectEntryConfig getParserSelectEntryConfig(XMLParserSelectRegistryEntryMBean var0) {
      return singularInstance.new ParserSelectEntryMBeanConfigImpl(var0);
   }

   static EntitySpecEntryConfig getEntitySpecEntryConfig(XMLEntitySpecRegistryEntryMBean var0) {
      return singularInstance.new EntitySpecEntryMBeanConfigImpl(var0);
   }

   static RegistryConfig getRegistryConfig(XmlBean var0, String var1) {
      return singularInstance.new RegistryXMLDDConfigImpl(var0, var1);
   }

   class EntityIterator extends ArrayIterator {
      EntityIterator(XMLRegistryEntryMBean[] var2) {
         super(var2, true);
      }

      public Object nextElement() {
         return this.isMBean ? ConfigAbstraction.this.new OldEntryMBeanConfigImpl((XMLRegistryEntryMBean)super.nextElement()) : null;
      }
   }

   class EntitySpecEntryIterator extends ArrayIterator {
      EntitySpecEntryIterator(XMLEntitySpecRegistryEntryMBean[] var2) {
         super(var2, true);
      }

      EntitySpecEntryIterator(EntityMappingBean[] var2) {
         super(var2, false);
      }

      public Object nextElement() {
         return this.isMBean ? ConfigAbstraction.this.new EntitySpecEntryMBeanConfigImpl((XMLEntitySpecRegistryEntryMBean)super.nextElement()) : ConfigAbstraction.this.new EntitySpecEntryDescriptorMBeanConfigImpl((EntityMappingBean)super.nextElement());
      }
   }

   class ParserSelectEntryIterator extends ArrayIterator {
      ParserSelectEntryIterator(XMLParserSelectRegistryEntryMBean[] var2) {
         super(var2, true);
      }

      public Object nextElement() {
         return this.isMBean ? ConfigAbstraction.this.new ParserSelectEntryMBeanConfigImpl((XMLParserSelectRegistryEntryMBean)super.nextElement()) : null;
      }
   }

   abstract class ArrayIterator implements Enumeration {
      boolean isMBean = true;
      Object[] array = null;
      int index = -1;

      ArrayIterator(Object[] var2, boolean var3) {
         this.array = var2;
         this.isMBean = var3;
      }

      public boolean hasMoreElements() {
         return this.index + 1 < this.array.length;
      }

      public Object nextElement() {
         ++this.index;
         return this.array[this.index];
      }
   }

   class RegistryXMLDDConfigImpl extends GenericConfigAppBasedImpl implements RegistryConfig {
      XmlBean xmlDD = null;

      RegistryXMLDDConfigImpl(XmlBean var2, String var3) {
         super(var3);
         this.xmlDD = var2;
      }

      public void addPropertyChangeListener(PropertyChangeListener var1) {
      }

      public void removePropertyChangeListener(PropertyChangeListener var1) {
      }

      public String getDocumentBuilderFactory() {
         return this.xmlDD.getParserFactory() == null ? null : this.xmlDD.getParserFactory().getDocumentBuilderFactory();
      }

      public String getTransformerFactory() {
         return this.xmlDD.getParserFactory() == null ? null : this.xmlDD.getParserFactory().getTransformerFactory();
      }

      public String getSAXParserFactory() {
         return this.xmlDD.getParserFactory() == null ? null : this.xmlDD.getParserFactory().getSaxparserFactory();
      }

      public String getWhenToCache() {
         return null;
      }

      public String getHandleEntityInvalidation() {
         return null;
      }

      public Enumeration getParserSelectRegistryEntries() {
         return ConfigAbstraction.this.new ParserSelectEntryIterator((XMLParserSelectRegistryEntryMBean[])null);
      }

      public Enumeration getEntitySpecRegistryEntries() {
         return this.xmlDD.getEntityMappings() != null ? ConfigAbstraction.this.new EntitySpecEntryIterator(this.xmlDD.getEntityMappings()) : null;
      }

      public Enumeration getRegistryEntries() {
         return ConfigAbstraction.this.new EntityIterator((XMLRegistryEntryMBean[])null);
      }
   }

   abstract class GenericConfigAppBasedImpl {
      String applicationName;

      GenericConfigAppBasedImpl(String var2) {
         this.applicationName = var2;
      }

      public String toString() {
         return "ConfigAbstraction:" + this.applicationName;
      }

      public String getName() {
         return this.applicationName + ".XMLRegistry";
      }

      public void addPropertyChangeListener(PropertyChangeListener var1) {
      }

      public void removePropertyChangeListener(PropertyChangeListener var1) {
      }
   }

   class OldEntryMBeanConfigImpl extends GenericMBeanConfigImpl implements OldEntryConfig {
      XMLRegistryEntryMBean mBean = null;

      OldEntryMBeanConfigImpl(XMLRegistryEntryMBean var2) {
         super(var2);
         this.mBean = var2;
      }

      public String getPublicId() {
         return this.mBean.getPublicId();
      }

      public String getSystemId() {
         return this.mBean.getSystemId();
      }

      public String getRootElementTag() {
         return this.mBean.getRootElementTag();
      }

      public String getDocumentBuilderFactory() {
         return this.mBean.getDocumentBuilderFactory();
      }

      public String getTransformerFactory() {
         return null;
      }

      public String getSAXParserFactory() {
         return this.mBean.getSAXParserFactory();
      }

      public String getParserClassName() {
         return this.mBean.getParserClassName();
      }

      public String getEntityPath() {
         return this.mBean.getEntityPath();
      }
   }

   class EntitySpecEntryDescriptorMBeanConfigImpl implements EntitySpecEntryConfig {
      EntityMappingBean descMBean = null;

      EntitySpecEntryDescriptorMBeanConfigImpl(EntityMappingBean var2) {
         this.descMBean = var2;
      }

      public String getPublicId() {
         return this.descMBean.getPublicId();
      }

      public String getSystemId() {
         return this.descMBean.getSystemId();
      }

      public String getWhenToCache() {
         return this.descMBean.getWhenToCache();
      }

      public String getEntityURI() {
         return this.descMBean.getEntityUri();
      }

      public int getCacheTimeoutInterval() {
         return this.descMBean.getCacheTimeoutInterval();
      }

      public String getHandleEntityInvalidation() {
         return null;
      }

      public void addPropertyChangeListener(PropertyChangeListener var1) {
      }

      public void removePropertyChangeListener(PropertyChangeListener var1) {
      }
   }

   class EntitySpecEntryMBeanConfigImpl extends GenericMBeanConfigImpl implements EntitySpecEntryConfig {
      XMLEntitySpecRegistryEntryMBean mBean = null;

      EntitySpecEntryMBeanConfigImpl(XMLEntitySpecRegistryEntryMBean var2) {
         super(var2);
         this.mBean = var2;
      }

      public String getPublicId() {
         return this.mBean.getPublicId();
      }

      public String getSystemId() {
         return this.mBean.getSystemId();
      }

      public String getWhenToCache() {
         return this.mBean.getWhenToCache();
      }

      public String getEntityURI() {
         return this.mBean.getEntityURI();
      }

      public int getCacheTimeoutInterval() {
         return this.mBean.getCacheTimeoutInterval();
      }

      public String getHandleEntityInvalidation() {
         return this.mBean.getHandleEntityInvalidation();
      }
   }

   class ParserSelectEntryMBeanConfigImpl extends GenericMBeanConfigImpl implements ParserSelectEntryConfig {
      XMLParserSelectRegistryEntryMBean mBean = null;

      ParserSelectEntryMBeanConfigImpl(XMLParserSelectRegistryEntryMBean var2) {
         super(var2);
         this.mBean = var2;
      }

      public String getPublicId() {
         return this.mBean.getPublicId();
      }

      public String getSystemId() {
         return this.mBean.getSystemId();
      }

      public String getRootElementTag() {
         return this.mBean.getRootElementTag();
      }

      public String getDocumentBuilderFactory() {
         return this.mBean.getDocumentBuilderFactory();
      }

      public String getTransformerFactory() {
         return null;
      }

      public String getSAXParserFactory() {
         return this.mBean.getSAXParserFactory();
      }

      public String getParserClassName() {
         return this.mBean.getParserClassName();
      }
   }

   class RegistryMBeanConfigImpl extends GenericMBeanConfigImpl implements RegistryConfig {
      XMLRegistryMBean mBean = null;

      RegistryMBeanConfigImpl(XMLRegistryMBean var2) {
         super(var2);
         this.mBean = var2;
      }

      public String getDocumentBuilderFactory() {
         return this.mBean.getDocumentBuilderFactory();
      }

      public String getTransformerFactory() {
         return this.mBean.getTransformerFactory();
      }

      public String getSAXParserFactory() {
         return this.mBean.getSAXParserFactory();
      }

      public String getWhenToCache() {
         return this.mBean.getWhenToCache();
      }

      public String getHandleEntityInvalidation() {
         return this.mBean.isHandleEntityInvalidation() ? "true" : "false";
      }

      public Enumeration getParserSelectRegistryEntries() {
         return ConfigAbstraction.this.new ParserSelectEntryIterator(this.mBean.getParserSelectRegistryEntries());
      }

      public Enumeration getEntitySpecRegistryEntries() {
         return ConfigAbstraction.this.new EntitySpecEntryIterator(this.mBean.getEntitySpecRegistryEntries());
      }

      public Enumeration getRegistryEntries() {
         return ConfigAbstraction.this.new EntityIterator(this.mBean.getRegistryEntries());
      }

      public String getName() {
         return this.mBean.getName();
      }
   }

   abstract class GenericMBeanConfigImpl {
      ConfigurationMBean configurationMBean = null;

      GenericMBeanConfigImpl(ConfigurationMBean var2) {
         this.configurationMBean = var2;
      }

      public void addPropertyChangeListener(PropertyChangeListener var1) {
         this.configurationMBean.addPropertyChangeListener(var1);
      }

      public void removePropertyChangeListener(PropertyChangeListener var1) {
         try {
            this.configurationMBean.removePropertyChangeListener(var1);
         } catch (Exception var3) {
         }

      }

      public String toString() {
         return this.configurationMBean.toString();
      }

      public ConfigurationMBean getMBean() {
         return this.configurationMBean;
      }
   }

   interface RegistryConfig extends JAXPConfig, CacheConfig {
      String getName();

      Enumeration getParserSelectRegistryEntries();

      Enumeration getEntitySpecRegistryEntries();

      Enumeration getRegistryEntries();
   }

   interface OldEntryConfig extends EntryConfig, JAXPConfig {
      String getRootElementTag();

      String getParserClassName();

      String getEntityPath();

      ConfigurationMBean getMBean();
   }

   interface EntitySpecEntryConfig extends EntryConfig, CacheConfig {
      String getEntityURI();

      int getCacheTimeoutInterval();
   }

   interface ParserSelectEntryConfig extends EntryConfig, JAXPConfig {
      String getRootElementTag();

      String getParserClassName();
   }

   interface CacheConfig extends GenericConfig {
      String getWhenToCache();

      String getHandleEntityInvalidation();
   }

   interface EntryConfig extends GenericConfig {
      String getPublicId();

      String getSystemId();
   }

   interface JAXPConfig extends GenericConfig {
      String getDocumentBuilderFactory();

      String getTransformerFactory();

      String getSAXParserFactory();
   }

   interface GenericConfig {
      void addPropertyChangeListener(PropertyChangeListener var1);

      void removePropertyChangeListener(PropertyChangeListener var1);
   }
}
