package weblogic.management.configuration;

import java.io.Serializable;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.zip.CRC32;
import javax.management.AttributeNotFoundException;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanException;
import weblogic.descriptor.BeanRemoveRejectedException;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBeanHelper;
import weblogic.descriptor.internal.Munger;
import weblogic.descriptor.internal.SchemaHelper;
import weblogic.management.ManagementException;
import weblogic.management.mbeans.custom.XMLRegistry;
import weblogic.utils.ArrayUtils;
import weblogic.utils.collections.ArrayIterator;
import weblogic.utils.collections.CombinedIterator;

public class XMLRegistryMBeanImpl extends ConfigurationMBeanImpl implements XMLRegistryMBean, Serializable {
   private String _DocumentBuilderFactory;
   private XMLEntitySpecRegistryEntryMBean[] _EntitySpecRegistryEntries;
   private boolean _HandleEntityInvalidation;
   private String _Name;
   private XMLParserSelectRegistryEntryMBean[] _ParserSelectRegistryEntries;
   private XMLRegistryEntryMBean[] _RegistryEntries;
   private String _SAXParserFactory;
   private String _TransformerFactory;
   private String _WhenToCache;
   private XMLEntitySpecRegistryEntryMBean[] _XMLEntitySpecRegistryEntries;
   private XMLParserSelectRegistryEntryMBean[] _XMLParserSelectRegistryEntries;
   private XMLRegistry _customizer;
   private static SchemaHelper2 _schemaHelper;

   public XMLRegistryMBeanImpl() {
      try {
         this._customizer = new XMLRegistry(this);
      } catch (Exception var2) {
         if (var2 instanceof RuntimeException) {
            throw (RuntimeException)var2;
         }

         throw new UndeclaredThrowableException(var2);
      }

      this._initializeProperty(-1);
   }

   public XMLRegistryMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);

      try {
         this._customizer = new XMLRegistry(this);
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         }

         throw new UndeclaredThrowableException(var4);
      }

      this._initializeProperty(-1);
   }

   public String getDocumentBuilderFactory() {
      return this._DocumentBuilderFactory;
   }

   public String getName() {
      if (!this._isSet(2)) {
         try {
            return ((ConfigurationMBean)this.getParent()).getName();
         } catch (NullPointerException var2) {
         }
      }

      return this._customizer.getName();
   }

   public boolean isDocumentBuilderFactorySet() {
      return this._isSet(7);
   }

   public boolean isNameSet() {
      return this._isSet(2);
   }

   public void setDocumentBuilderFactory(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._DocumentBuilderFactory;
      this._DocumentBuilderFactory = var1;
      this._postSet(7, var2, var1);
   }

   public void setName(String var1) throws InvalidAttributeValueException, ManagementException {
      var1 = var1 == null ? null : var1.trim();
      weblogic.descriptor.beangen.LegalChecks.checkNonEmptyString("Name", var1);
      weblogic.descriptor.beangen.LegalChecks.checkNonNull("Name", var1);
      ConfigurationValidator.validateName(var1);
      String var2 = this.getName();
      this._customizer.setName(var1);
      this._postSet(2, var2, var1);
   }

   public String getSAXParserFactory() {
      return this._SAXParserFactory;
   }

   public boolean isSAXParserFactorySet() {
      return this._isSet(8);
   }

   public void setSAXParserFactory(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._SAXParserFactory;
      this._SAXParserFactory = var1;
      this._postSet(8, var2, var1);
   }

   public String getTransformerFactory() {
      return this._TransformerFactory;
   }

   public boolean isTransformerFactorySet() {
      return this._isSet(9);
   }

   public void setTransformerFactory(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._TransformerFactory;
      this._TransformerFactory = var1;
      this._postSet(9, var2, var1);
   }

   public boolean addRegistryEntry(XMLRegistryEntryMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 10)) {
         XMLRegistryEntryMBean[] var2 = (XMLRegistryEntryMBean[])((XMLRegistryEntryMBean[])this._getHelper()._extendArray(this.getRegistryEntries(), XMLRegistryEntryMBean.class, var1));

         try {
            this.setRegistryEntries(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

      return true;
   }

   public boolean removeRegistryEntry(XMLRegistryEntryMBean var1) {
      XMLRegistryEntryMBean[] var2 = this.getRegistryEntries();
      XMLRegistryEntryMBean[] var3 = (XMLRegistryEntryMBean[])((XMLRegistryEntryMBean[])this._getHelper()._removeElement(var2, XMLRegistryEntryMBean.class, var1));
      if (var3.length != var2.length) {
         try {
            this.setRegistryEntries(var3);
            return true;
         } catch (Exception var5) {
            if (var5 instanceof RuntimeException) {
               throw (RuntimeException)var5;
            } else {
               throw new UndeclaredThrowableException(var5);
            }
         }
      } else {
         return false;
      }
   }

   public XMLRegistryEntryMBean[] getRegistryEntries() {
      return this._RegistryEntries;
   }

   public boolean isRegistryEntriesSet() {
      return this._isSet(10);
   }

   public void setRegistryEntries(XMLRegistryEntryMBean[] var1) throws InvalidAttributeValueException {
      Object var2 = var1 == null ? new XMLRegistryEntryMBeanImpl[0] : var1;
      this._RegistryEntries = (XMLRegistryEntryMBean[])var2;
   }

   public XMLParserSelectRegistryEntryMBean createXMLParserSelectRegistryEntry(String var1) {
      XMLParserSelectRegistryEntryMBeanImpl var2 = new XMLParserSelectRegistryEntryMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addXMLParserSelectRegistryEntry(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public void touch() throws ConfigurationException {
      this._customizer.touch();
   }

   public void destroyXMLParserSelectRegistryEntry(XMLParserSelectRegistryEntryMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 11);
         XMLParserSelectRegistryEntryMBean[] var2 = this.getXMLParserSelectRegistryEntries();
         XMLParserSelectRegistryEntryMBean[] var3 = (XMLParserSelectRegistryEntryMBean[])((XMLParserSelectRegistryEntryMBean[])this._getHelper()._removeElement(var2, XMLParserSelectRegistryEntryMBean.class, var1));
         if (var2.length != var3.length) {
            this._preDestroy((AbstractDescriptorBean)var1);

            try {
               AbstractDescriptorBean var4 = (AbstractDescriptorBean)var1;
               if (var4 == null) {
                  return;
               }

               List var5 = this._getReferenceManager().getResolvedReferences(var4);
               if (var5 != null && var5.size() > 0) {
                  throw new BeanRemoveRejectedException(var4, var5);
               }

               this._getReferenceManager().unregisterBean(var4);
               this._markDestroyed(var4);
               this.setXMLParserSelectRegistryEntries(var3);
            } catch (Exception var6) {
               if (var6 instanceof RuntimeException) {
                  throw (RuntimeException)var6;
               }

               throw new UndeclaredThrowableException(var6);
            }
         }

      } catch (Exception var7) {
         if (var7 instanceof RuntimeException) {
            throw (RuntimeException)var7;
         } else {
            throw new UndeclaredThrowableException(var7);
         }
      }
   }

   public void freezeCurrentValue(String var1) throws AttributeNotFoundException, MBeanException {
      this._customizer.freezeCurrentValue(var1);
   }

   public boolean addParserSelectRegistryEntry(XMLParserSelectRegistryEntryMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 12)) {
         XMLParserSelectRegistryEntryMBean[] var2 = (XMLParserSelectRegistryEntryMBean[])((XMLParserSelectRegistryEntryMBean[])this._getHelper()._extendArray(this.getParserSelectRegistryEntries(), XMLParserSelectRegistryEntryMBean.class, var1));

         try {
            this.setParserSelectRegistryEntries(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

      return true;
   }

   public void restoreDefaultValue(String var1) throws AttributeNotFoundException {
      this._customizer.restoreDefaultValue(var1);
   }

   public boolean removeParserSelectRegistryEntry(XMLParserSelectRegistryEntryMBean var1) {
      XMLParserSelectRegistryEntryMBean[] var2 = this.getParserSelectRegistryEntries();
      XMLParserSelectRegistryEntryMBean[] var3 = (XMLParserSelectRegistryEntryMBean[])((XMLParserSelectRegistryEntryMBean[])this._getHelper()._removeElement(var2, XMLParserSelectRegistryEntryMBean.class, var1));
      if (var3.length != var2.length) {
         try {
            this.setParserSelectRegistryEntries(var3);
            return true;
         } catch (Exception var5) {
            if (var5 instanceof RuntimeException) {
               throw (RuntimeException)var5;
            } else {
               throw new UndeclaredThrowableException(var5);
            }
         }
      } else {
         return false;
      }
   }

   public void addXMLParserSelectRegistryEntry(XMLParserSelectRegistryEntryMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 11)) {
         XMLParserSelectRegistryEntryMBean[] var2;
         if (this._isSet(11)) {
            var2 = (XMLParserSelectRegistryEntryMBean[])((XMLParserSelectRegistryEntryMBean[])this._getHelper()._extendArray(this.getXMLParserSelectRegistryEntries(), XMLParserSelectRegistryEntryMBean.class, var1));
         } else {
            var2 = new XMLParserSelectRegistryEntryMBean[]{var1};
         }

         try {
            this.setXMLParserSelectRegistryEntries(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public XMLParserSelectRegistryEntryMBean[] getXMLParserSelectRegistryEntries() {
      return this._XMLParserSelectRegistryEntries;
   }

   public boolean isXMLParserSelectRegistryEntriesSet() {
      return this._isSet(11);
   }

   public void removeXMLParserSelectRegistryEntry(XMLParserSelectRegistryEntryMBean var1) {
      this.destroyXMLParserSelectRegistryEntry(var1);
   }

   public void setXMLParserSelectRegistryEntries(XMLParserSelectRegistryEntryMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new XMLParserSelectRegistryEntryMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 11)) {
            this._getReferenceManager().registerBean(var3, false);
            this._postCreate(var3);
         }
      }

      XMLParserSelectRegistryEntryMBean[] var5 = this._XMLParserSelectRegistryEntries;
      this._XMLParserSelectRegistryEntries = (XMLParserSelectRegistryEntryMBean[])var4;
      this._postSet(11, var5, var4);
   }

   public XMLParserSelectRegistryEntryMBean[] getParserSelectRegistryEntries() {
      return this._customizer.getParserSelectRegistryEntries();
   }

   public boolean isParserSelectRegistryEntriesSet() {
      return this._isSet(12);
   }

   public void setParserSelectRegistryEntries(XMLParserSelectRegistryEntryMBean[] var1) throws InvalidAttributeValueException {
      Object var2 = var1 == null ? new XMLParserSelectRegistryEntryMBeanImpl[0] : var1;
      this._ParserSelectRegistryEntries = (XMLParserSelectRegistryEntryMBean[])var2;
   }

   public boolean addEntitySpecRegistryEntry(XMLEntitySpecRegistryEntryMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 13)) {
         XMLEntitySpecRegistryEntryMBean[] var2 = (XMLEntitySpecRegistryEntryMBean[])((XMLEntitySpecRegistryEntryMBean[])this._getHelper()._extendArray(this.getEntitySpecRegistryEntries(), XMLEntitySpecRegistryEntryMBean.class, var1));

         try {
            this.setEntitySpecRegistryEntries(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

      return true;
   }

   public boolean removeEntitySpecRegistryEntry(XMLEntitySpecRegistryEntryMBean var1) {
      XMLEntitySpecRegistryEntryMBean[] var2 = this.getEntitySpecRegistryEntries();
      XMLEntitySpecRegistryEntryMBean[] var3 = (XMLEntitySpecRegistryEntryMBean[])((XMLEntitySpecRegistryEntryMBean[])this._getHelper()._removeElement(var2, XMLEntitySpecRegistryEntryMBean.class, var1));
      if (var3.length != var2.length) {
         try {
            this.setEntitySpecRegistryEntries(var3);
            return true;
         } catch (Exception var5) {
            if (var5 instanceof RuntimeException) {
               throw (RuntimeException)var5;
            } else {
               throw new UndeclaredThrowableException(var5);
            }
         }
      } else {
         return false;
      }
   }

   public XMLEntitySpecRegistryEntryMBean[] getEntitySpecRegistryEntries() {
      return this._customizer.getEntitySpecRegistryEntries();
   }

   public boolean isEntitySpecRegistryEntriesSet() {
      return this._isSet(13);
   }

   public void addXMLEntitySpecRegistryEntry(XMLEntitySpecRegistryEntryMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 14)) {
         XMLEntitySpecRegistryEntryMBean[] var2;
         if (this._isSet(14)) {
            var2 = (XMLEntitySpecRegistryEntryMBean[])((XMLEntitySpecRegistryEntryMBean[])this._getHelper()._extendArray(this.getXMLEntitySpecRegistryEntries(), XMLEntitySpecRegistryEntryMBean.class, var1));
         } else {
            var2 = new XMLEntitySpecRegistryEntryMBean[]{var1};
         }

         try {
            this.setXMLEntitySpecRegistryEntries(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public XMLEntitySpecRegistryEntryMBean[] getXMLEntitySpecRegistryEntries() {
      return this._XMLEntitySpecRegistryEntries;
   }

   public boolean isXMLEntitySpecRegistryEntriesSet() {
      return this._isSet(14);
   }

   public void removeXMLEntitySpecRegistryEntry(XMLEntitySpecRegistryEntryMBean var1) {
      this.destroyXMLEntitySpecRegistryEntry(var1);
   }

   public void setXMLEntitySpecRegistryEntries(XMLEntitySpecRegistryEntryMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new XMLEntitySpecRegistryEntryMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 14)) {
            this._getReferenceManager().registerBean(var3, false);
            this._postCreate(var3);
         }
      }

      XMLEntitySpecRegistryEntryMBean[] var5 = this._XMLEntitySpecRegistryEntries;
      this._XMLEntitySpecRegistryEntries = (XMLEntitySpecRegistryEntryMBean[])var4;
      this._postSet(14, var5, var4);
   }

   public XMLEntitySpecRegistryEntryMBean createXMLEntitySpecRegistryEntry(String var1) {
      XMLEntitySpecRegistryEntryMBeanImpl var2 = new XMLEntitySpecRegistryEntryMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addXMLEntitySpecRegistryEntry(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public void destroyXMLEntitySpecRegistryEntry(XMLEntitySpecRegistryEntryMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 14);
         XMLEntitySpecRegistryEntryMBean[] var2 = this.getXMLEntitySpecRegistryEntries();
         XMLEntitySpecRegistryEntryMBean[] var3 = (XMLEntitySpecRegistryEntryMBean[])((XMLEntitySpecRegistryEntryMBean[])this._getHelper()._removeElement(var2, XMLEntitySpecRegistryEntryMBean.class, var1));
         if (var2.length != var3.length) {
            this._preDestroy((AbstractDescriptorBean)var1);

            try {
               AbstractDescriptorBean var4 = (AbstractDescriptorBean)var1;
               if (var4 == null) {
                  return;
               }

               List var5 = this._getReferenceManager().getResolvedReferences(var4);
               if (var5 != null && var5.size() > 0) {
                  throw new BeanRemoveRejectedException(var4, var5);
               }

               this._getReferenceManager().unregisterBean(var4);
               this._markDestroyed(var4);
               this.setXMLEntitySpecRegistryEntries(var3);
            } catch (Exception var6) {
               if (var6 instanceof RuntimeException) {
                  throw (RuntimeException)var6;
               }

               throw new UndeclaredThrowableException(var6);
            }
         }

      } catch (Exception var7) {
         if (var7 instanceof RuntimeException) {
            throw (RuntimeException)var7;
         } else {
            throw new UndeclaredThrowableException(var7);
         }
      }
   }

   public void setEntitySpecRegistryEntries(XMLEntitySpecRegistryEntryMBean[] var1) throws InvalidAttributeValueException {
      Object var2 = var1 == null ? new XMLEntitySpecRegistryEntryMBeanImpl[0] : var1;
      this._EntitySpecRegistryEntries = (XMLEntitySpecRegistryEntryMBean[])var2;
   }

   public String getWhenToCache() {
      return this._WhenToCache;
   }

   public boolean isWhenToCacheSet() {
      return this._isSet(15);
   }

   public void setWhenToCache(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{"cache-on-reference", "cache-at-initialization", "cache-never"};
      var1 = weblogic.descriptor.beangen.LegalChecks.checkInEnum("WhenToCache", var1, var2);
      String var3 = this._WhenToCache;
      this._WhenToCache = var1;
      this._postSet(15, var3, var1);
   }

   public boolean isHandleEntityInvalidation() {
      return this._HandleEntityInvalidation;
   }

   public boolean isHandleEntityInvalidationSet() {
      return this._isSet(16);
   }

   public void setHandleEntityInvalidation(boolean var1) {
      boolean var2 = this._HandleEntityInvalidation;
      this._HandleEntityInvalidation = var1;
      this._postSet(16, var2, var1);
   }

   public XMLParserSelectRegistryEntryMBean findParserSelectMBeanByKey(String var1, String var2, String var3) {
      return this._customizer.findParserSelectMBeanByKey(var1, var2, var3);
   }

   public XMLEntitySpecRegistryEntryMBean findEntitySpecMBeanByKey(String var1, String var2) {
      return this._customizer.findEntitySpecMBeanByKey(var1, var2);
   }

   public Object _getKey() {
      return this.getName();
   }

   public void _validate() throws IllegalArgumentException {
      super._validate();
   }

   public boolean _hasKey() {
      return true;
   }

   public boolean _isPropertyAKey(Munger.ReaderEventInfo var1) {
      String var2 = var1.getElementName();
      switch (var2.length()) {
         case 4:
            if (var2.equals("name")) {
               return var1.compareXpaths(this._getPropertyXpath("name"));
            }

            return super._isPropertyAKey(var1);
         default:
            return super._isPropertyAKey(var1);
      }
   }

   protected void _unSet(int var1) {
      if (!this._initializeProperty(var1)) {
         super._unSet(var1);
      } else {
         this._markSet(var1, false);
      }

   }

   protected AbstractDescriptorBeanHelper _createHelper() {
      return new Helper(this);
   }

   public boolean _isAnythingSet() {
      return super._isAnythingSet();
   }

   private boolean _initializeProperty(int var1) {
      boolean var2 = var1 > -1;
      if (!var2) {
         var1 = 7;
      }

      try {
         switch (var1) {
            case 7:
               this._DocumentBuilderFactory = "weblogic.apache.xerces.jaxp.DocumentBuilderFactoryImpl";
               if (var2) {
                  break;
               }
            case 13:
               this._EntitySpecRegistryEntries = new XMLEntitySpecRegistryEntryMBean[0];
               if (var2) {
                  break;
               }
            case 2:
               this._customizer.setName((String)null);
               if (var2) {
                  break;
               }
            case 12:
               this._ParserSelectRegistryEntries = new XMLParserSelectRegistryEntryMBean[0];
               if (var2) {
                  break;
               }
            case 10:
               this._RegistryEntries = new XMLRegistryEntryMBean[0];
               if (var2) {
                  break;
               }
            case 8:
               this._SAXParserFactory = "weblogic.apache.xerces.jaxp.SAXParserFactoryImpl";
               if (var2) {
                  break;
               }
            case 9:
               this._TransformerFactory = "weblogic.xml.jaxp.WebLogicTransformerFactory";
               if (var2) {
                  break;
               }
            case 15:
               this._WhenToCache = "cache-on-reference";
               if (var2) {
                  break;
               }
            case 14:
               this._XMLEntitySpecRegistryEntries = new XMLEntitySpecRegistryEntryMBean[0];
               if (var2) {
                  break;
               }
            case 11:
               this._XMLParserSelectRegistryEntries = new XMLParserSelectRegistryEntryMBean[0];
               if (var2) {
                  break;
               }
            case 16:
               this._HandleEntityInvalidation = false;
               if (var2) {
                  break;
               }
            case 3:
            case 4:
            case 5:
            case 6:
            default:
               if (var2) {
                  return false;
               }
         }

         return true;
      } catch (RuntimeException var4) {
         throw var4;
      } catch (Exception var5) {
         throw (Error)(new AssertionError("Impossible Exception")).initCause(var5);
      }
   }

   public Munger.SchemaHelper _getSchemaHelper() {
      return null;
   }

   public String _getElementName(int var1) {
      return this._getSchemaHelper2().getElementName(var1);
   }

   protected String getSchemaLocation() {
      return "http://xmlns.oracle.com/weblogic/1.0/domain.xsd";
   }

   protected String getTargetNamespace() {
      return "http://xmlns.oracle.com/weblogic/domain";
   }

   public SchemaHelper _getSchemaHelper2() {
      if (_schemaHelper == null) {
         _schemaHelper = new SchemaHelper2();
      }

      return _schemaHelper;
   }

   public String getType() {
      return "XMLRegistry";
   }

   public void putValue(String var1, Object var2) {
      String var6;
      if (var1.equals("DocumentBuilderFactory")) {
         var6 = this._DocumentBuilderFactory;
         this._DocumentBuilderFactory = (String)var2;
         this._postSet(7, var6, this._DocumentBuilderFactory);
      } else {
         XMLEntitySpecRegistryEntryMBean[] var5;
         if (var1.equals("EntitySpecRegistryEntries")) {
            var5 = this._EntitySpecRegistryEntries;
            this._EntitySpecRegistryEntries = (XMLEntitySpecRegistryEntryMBean[])((XMLEntitySpecRegistryEntryMBean[])var2);
            this._postSet(13, var5, this._EntitySpecRegistryEntries);
         } else if (var1.equals("HandleEntityInvalidation")) {
            boolean var8 = this._HandleEntityInvalidation;
            this._HandleEntityInvalidation = (Boolean)var2;
            this._postSet(16, var8, this._HandleEntityInvalidation);
         } else if (var1.equals("Name")) {
            var6 = this._Name;
            this._Name = (String)var2;
            this._postSet(2, var6, this._Name);
         } else {
            XMLParserSelectRegistryEntryMBean[] var4;
            if (var1.equals("ParserSelectRegistryEntries")) {
               var4 = this._ParserSelectRegistryEntries;
               this._ParserSelectRegistryEntries = (XMLParserSelectRegistryEntryMBean[])((XMLParserSelectRegistryEntryMBean[])var2);
               this._postSet(12, var4, this._ParserSelectRegistryEntries);
            } else if (var1.equals("RegistryEntries")) {
               XMLRegistryEntryMBean[] var7 = this._RegistryEntries;
               this._RegistryEntries = (XMLRegistryEntryMBean[])((XMLRegistryEntryMBean[])var2);
               this._postSet(10, var7, this._RegistryEntries);
            } else if (var1.equals("SAXParserFactory")) {
               var6 = this._SAXParserFactory;
               this._SAXParserFactory = (String)var2;
               this._postSet(8, var6, this._SAXParserFactory);
            } else if (var1.equals("TransformerFactory")) {
               var6 = this._TransformerFactory;
               this._TransformerFactory = (String)var2;
               this._postSet(9, var6, this._TransformerFactory);
            } else if (var1.equals("WhenToCache")) {
               var6 = this._WhenToCache;
               this._WhenToCache = (String)var2;
               this._postSet(15, var6, this._WhenToCache);
            } else if (var1.equals("XMLEntitySpecRegistryEntries")) {
               var5 = this._XMLEntitySpecRegistryEntries;
               this._XMLEntitySpecRegistryEntries = (XMLEntitySpecRegistryEntryMBean[])((XMLEntitySpecRegistryEntryMBean[])var2);
               this._postSet(14, var5, this._XMLEntitySpecRegistryEntries);
            } else if (var1.equals("XMLParserSelectRegistryEntries")) {
               var4 = this._XMLParserSelectRegistryEntries;
               this._XMLParserSelectRegistryEntries = (XMLParserSelectRegistryEntryMBean[])((XMLParserSelectRegistryEntryMBean[])var2);
               this._postSet(11, var4, this._XMLParserSelectRegistryEntries);
            } else if (var1.equals("customizer")) {
               XMLRegistry var3 = this._customizer;
               this._customizer = (XMLRegistry)var2;
            } else {
               super.putValue(var1, var2);
            }
         }
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("DocumentBuilderFactory")) {
         return this._DocumentBuilderFactory;
      } else if (var1.equals("EntitySpecRegistryEntries")) {
         return this._EntitySpecRegistryEntries;
      } else if (var1.equals("HandleEntityInvalidation")) {
         return new Boolean(this._HandleEntityInvalidation);
      } else if (var1.equals("Name")) {
         return this._Name;
      } else if (var1.equals("ParserSelectRegistryEntries")) {
         return this._ParserSelectRegistryEntries;
      } else if (var1.equals("RegistryEntries")) {
         return this._RegistryEntries;
      } else if (var1.equals("SAXParserFactory")) {
         return this._SAXParserFactory;
      } else if (var1.equals("TransformerFactory")) {
         return this._TransformerFactory;
      } else if (var1.equals("WhenToCache")) {
         return this._WhenToCache;
      } else if (var1.equals("XMLEntitySpecRegistryEntries")) {
         return this._XMLEntitySpecRegistryEntries;
      } else if (var1.equals("XMLParserSelectRegistryEntries")) {
         return this._XMLParserSelectRegistryEntries;
      } else {
         return var1.equals("customizer") ? this._customizer : super.getValue(var1);
      }
   }

   public static class SchemaHelper2 extends ConfigurationMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 4:
               if (var1.equals("name")) {
                  return 2;
               }
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 15:
            case 16:
            case 17:
            case 20:
            case 21:
            case 22:
            case 23:
            case 25:
            case 27:
            case 29:
            case 31:
            default:
               break;
            case 13:
               if (var1.equals("when-to-cache")) {
                  return 15;
               }
               break;
            case 14:
               if (var1.equals("registry-entry")) {
                  return 10;
               }
               break;
            case 18:
               if (var1.equals("sax-parser-factory")) {
                  return 8;
               }
               break;
            case 19:
               if (var1.equals("transformer-factory")) {
                  return 9;
               }
               break;
            case 24:
               if (var1.equals("document-builder-factory")) {
                  return 7;
               }
               break;
            case 26:
               if (var1.equals("entity-spec-registry-entry")) {
                  return 13;
               }

               if (var1.equals("handle-entity-invalidation")) {
                  return 16;
               }
               break;
            case 28:
               if (var1.equals("parser-select-registry-entry")) {
                  return 12;
               }
               break;
            case 30:
               if (var1.equals("xml-entity-spec-registry-entry")) {
                  return 14;
               }
               break;
            case 32:
               if (var1.equals("xml-parser-select-registry-entry")) {
                  return 11;
               }
         }

         return super.getPropertyIndex(var1);
      }

      public SchemaHelper getSchemaHelper(int var1) {
         switch (var1) {
            case 11:
               return new XMLParserSelectRegistryEntryMBeanImpl.SchemaHelper2();
            case 14:
               return new XMLEntitySpecRegistryEntryMBeanImpl.SchemaHelper2();
            default:
               return super.getSchemaHelper(var1);
         }
      }

      public String getElementName(int var1) {
         switch (var1) {
            case 2:
               return "name";
            case 3:
            case 4:
            case 5:
            case 6:
            default:
               return super.getElementName(var1);
            case 7:
               return "document-builder-factory";
            case 8:
               return "sax-parser-factory";
            case 9:
               return "transformer-factory";
            case 10:
               return "registry-entry";
            case 11:
               return "xml-parser-select-registry-entry";
            case 12:
               return "parser-select-registry-entry";
            case 13:
               return "entity-spec-registry-entry";
            case 14:
               return "xml-entity-spec-registry-entry";
            case 15:
               return "when-to-cache";
            case 16:
               return "handle-entity-invalidation";
         }
      }

      public boolean isArray(int var1) {
         switch (var1) {
            case 10:
               return true;
            case 11:
               return true;
            case 12:
               return true;
            case 13:
               return true;
            case 14:
               return true;
            default:
               return super.isArray(var1);
         }
      }

      public boolean isBean(int var1) {
         switch (var1) {
            case 11:
               return true;
            case 14:
               return true;
            default:
               return super.isBean(var1);
         }
      }

      public boolean isKey(int var1) {
         switch (var1) {
            case 2:
               return true;
            default:
               return super.isKey(var1);
         }
      }

      public boolean hasKey() {
         return true;
      }

      public String[] getKeyElementNames() {
         ArrayList var1 = new ArrayList();
         var1.add("name");
         return (String[])((String[])var1.toArray(new String[0]));
      }
   }

   protected static class Helper extends ConfigurationMBeanImpl.Helper {
      private XMLRegistryMBeanImpl bean;

      protected Helper(XMLRegistryMBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 2:
               return "Name";
            case 3:
            case 4:
            case 5:
            case 6:
            default:
               return super.getPropertyName(var1);
            case 7:
               return "DocumentBuilderFactory";
            case 8:
               return "SAXParserFactory";
            case 9:
               return "TransformerFactory";
            case 10:
               return "RegistryEntries";
            case 11:
               return "XMLParserSelectRegistryEntries";
            case 12:
               return "ParserSelectRegistryEntries";
            case 13:
               return "EntitySpecRegistryEntries";
            case 14:
               return "XMLEntitySpecRegistryEntries";
            case 15:
               return "WhenToCache";
            case 16:
               return "HandleEntityInvalidation";
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("DocumentBuilderFactory")) {
            return 7;
         } else if (var1.equals("EntitySpecRegistryEntries")) {
            return 13;
         } else if (var1.equals("Name")) {
            return 2;
         } else if (var1.equals("ParserSelectRegistryEntries")) {
            return 12;
         } else if (var1.equals("RegistryEntries")) {
            return 10;
         } else if (var1.equals("SAXParserFactory")) {
            return 8;
         } else if (var1.equals("TransformerFactory")) {
            return 9;
         } else if (var1.equals("WhenToCache")) {
            return 15;
         } else if (var1.equals("XMLEntitySpecRegistryEntries")) {
            return 14;
         } else if (var1.equals("XMLParserSelectRegistryEntries")) {
            return 11;
         } else {
            return var1.equals("HandleEntityInvalidation") ? 16 : super.getPropertyIndex(var1);
         }
      }

      public Iterator getChildren() {
         ArrayList var1 = new ArrayList();
         var1.add(new ArrayIterator(this.bean.getXMLEntitySpecRegistryEntries()));
         var1.add(new ArrayIterator(this.bean.getXMLParserSelectRegistryEntries()));
         return new CombinedIterator(var1);
      }

      protected long computeHashValue(CRC32 var1) {
         try {
            StringBuffer var2 = new StringBuffer();
            long var3 = super.computeHashValue(var1);
            if (var3 != 0L) {
               var2.append(String.valueOf(var3));
            }

            long var5 = 0L;
            if (this.bean.isDocumentBuilderFactorySet()) {
               var2.append("DocumentBuilderFactory");
               var2.append(String.valueOf(this.bean.getDocumentBuilderFactory()));
            }

            if (this.bean.isEntitySpecRegistryEntriesSet()) {
               var2.append("EntitySpecRegistryEntries");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getEntitySpecRegistryEntries())));
            }

            if (this.bean.isNameSet()) {
               var2.append("Name");
               var2.append(String.valueOf(this.bean.getName()));
            }

            if (this.bean.isParserSelectRegistryEntriesSet()) {
               var2.append("ParserSelectRegistryEntries");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getParserSelectRegistryEntries())));
            }

            if (this.bean.isRegistryEntriesSet()) {
               var2.append("RegistryEntries");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getRegistryEntries())));
            }

            if (this.bean.isSAXParserFactorySet()) {
               var2.append("SAXParserFactory");
               var2.append(String.valueOf(this.bean.getSAXParserFactory()));
            }

            if (this.bean.isTransformerFactorySet()) {
               var2.append("TransformerFactory");
               var2.append(String.valueOf(this.bean.getTransformerFactory()));
            }

            if (this.bean.isWhenToCacheSet()) {
               var2.append("WhenToCache");
               var2.append(String.valueOf(this.bean.getWhenToCache()));
            }

            var5 = 0L;

            int var7;
            for(var7 = 0; var7 < this.bean.getXMLEntitySpecRegistryEntries().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getXMLEntitySpecRegistryEntries()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = 0L;

            for(var7 = 0; var7 < this.bean.getXMLParserSelectRegistryEntries().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getXMLParserSelectRegistryEntries()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            if (this.bean.isHandleEntityInvalidationSet()) {
               var2.append("HandleEntityInvalidation");
               var2.append(String.valueOf(this.bean.isHandleEntityInvalidation()));
            }

            var1.update(var2.toString().getBytes());
            return var1.getValue();
         } catch (Exception var8) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var8);
         }
      }

      protected void computeDiff(AbstractDescriptorBean var1) {
         try {
            super.computeDiff(var1);
            XMLRegistryMBeanImpl var2 = (XMLRegistryMBeanImpl)var1;
            this.computeDiff("DocumentBuilderFactory", this.bean.getDocumentBuilderFactory(), var2.getDocumentBuilderFactory(), true);
            this.computeDiff("Name", this.bean.getName(), var2.getName(), false);
            this.computeDiff("SAXParserFactory", this.bean.getSAXParserFactory(), var2.getSAXParserFactory(), true);
            this.computeDiff("TransformerFactory", this.bean.getTransformerFactory(), var2.getTransformerFactory(), true);
            this.computeDiff("WhenToCache", this.bean.getWhenToCache(), var2.getWhenToCache(), true);
            this.computeChildDiff("XMLEntitySpecRegistryEntries", this.bean.getXMLEntitySpecRegistryEntries(), var2.getXMLEntitySpecRegistryEntries(), false);
            this.computeChildDiff("XMLParserSelectRegistryEntries", this.bean.getXMLParserSelectRegistryEntries(), var2.getXMLParserSelectRegistryEntries(), false);
            this.computeDiff("HandleEntityInvalidation", this.bean.isHandleEntityInvalidation(), var2.isHandleEntityInvalidation(), true);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            XMLRegistryMBeanImpl var3 = (XMLRegistryMBeanImpl)var1.getSourceBean();
            XMLRegistryMBeanImpl var4 = (XMLRegistryMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("DocumentBuilderFactory")) {
                  var3.setDocumentBuilderFactory(var4.getDocumentBuilderFactory());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 7);
               } else if (!var5.equals("EntitySpecRegistryEntries")) {
                  if (var5.equals("Name")) {
                     var3.setName(var4.getName());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 2);
                  } else if (!var5.equals("ParserSelectRegistryEntries") && !var5.equals("RegistryEntries")) {
                     if (var5.equals("SAXParserFactory")) {
                        var3.setSAXParserFactory(var4.getSAXParserFactory());
                        var3._conditionalUnset(var2.isUnsetUpdate(), 8);
                     } else if (var5.equals("TransformerFactory")) {
                        var3.setTransformerFactory(var4.getTransformerFactory());
                        var3._conditionalUnset(var2.isUnsetUpdate(), 9);
                     } else if (var5.equals("WhenToCache")) {
                        var3.setWhenToCache(var4.getWhenToCache());
                        var3._conditionalUnset(var2.isUnsetUpdate(), 15);
                     } else if (var5.equals("XMLEntitySpecRegistryEntries")) {
                        if (var6 == 2) {
                           var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                           var3.addXMLEntitySpecRegistryEntry((XMLEntitySpecRegistryEntryMBean)var2.getAddedObject());
                        } else {
                           if (var6 != 3) {
                              throw new AssertionError("Invalid type: " + var6);
                           }

                           var3.removeXMLEntitySpecRegistryEntry((XMLEntitySpecRegistryEntryMBean)var2.getRemovedObject());
                        }

                        if (var3.getXMLEntitySpecRegistryEntries() == null || var3.getXMLEntitySpecRegistryEntries().length == 0) {
                           var3._conditionalUnset(var2.isUnsetUpdate(), 14);
                        }
                     } else if (var5.equals("XMLParserSelectRegistryEntries")) {
                        if (var6 == 2) {
                           var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                           var3.addXMLParserSelectRegistryEntry((XMLParserSelectRegistryEntryMBean)var2.getAddedObject());
                        } else {
                           if (var6 != 3) {
                              throw new AssertionError("Invalid type: " + var6);
                           }

                           var3.removeXMLParserSelectRegistryEntry((XMLParserSelectRegistryEntryMBean)var2.getRemovedObject());
                        }

                        if (var3.getXMLParserSelectRegistryEntries() == null || var3.getXMLParserSelectRegistryEntries().length == 0) {
                           var3._conditionalUnset(var2.isUnsetUpdate(), 11);
                        }
                     } else if (var5.equals("HandleEntityInvalidation")) {
                        var3.setHandleEntityInvalidation(var4.isHandleEntityInvalidation());
                        var3._conditionalUnset(var2.isUnsetUpdate(), 16);
                     } else {
                        super.applyPropertyUpdate(var1, var2);
                     }
                  }
               }

            }
         } catch (RuntimeException var7) {
            throw var7;
         } catch (Exception var8) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var8);
         }
      }

      protected AbstractDescriptorBean finishCopy(AbstractDescriptorBean var1, boolean var2, List var3) {
         try {
            XMLRegistryMBeanImpl var5 = (XMLRegistryMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("DocumentBuilderFactory")) && this.bean.isDocumentBuilderFactorySet()) {
               var5.setDocumentBuilderFactory(this.bean.getDocumentBuilderFactory());
            }

            if ((var3 == null || !var3.contains("Name")) && this.bean.isNameSet()) {
               var5.setName(this.bean.getName());
            }

            if ((var3 == null || !var3.contains("SAXParserFactory")) && this.bean.isSAXParserFactorySet()) {
               var5.setSAXParserFactory(this.bean.getSAXParserFactory());
            }

            if ((var3 == null || !var3.contains("TransformerFactory")) && this.bean.isTransformerFactorySet()) {
               var5.setTransformerFactory(this.bean.getTransformerFactory());
            }

            if ((var3 == null || !var3.contains("WhenToCache")) && this.bean.isWhenToCacheSet()) {
               var5.setWhenToCache(this.bean.getWhenToCache());
            }

            int var8;
            if ((var3 == null || !var3.contains("XMLEntitySpecRegistryEntries")) && this.bean.isXMLEntitySpecRegistryEntriesSet() && !var5._isSet(14)) {
               XMLEntitySpecRegistryEntryMBean[] var6 = this.bean.getXMLEntitySpecRegistryEntries();
               XMLEntitySpecRegistryEntryMBean[] var7 = new XMLEntitySpecRegistryEntryMBean[var6.length];

               for(var8 = 0; var8 < var7.length; ++var8) {
                  var7[var8] = (XMLEntitySpecRegistryEntryMBean)((XMLEntitySpecRegistryEntryMBean)this.createCopy((AbstractDescriptorBean)var6[var8], var2));
               }

               var5.setXMLEntitySpecRegistryEntries(var7);
            }

            if ((var3 == null || !var3.contains("XMLParserSelectRegistryEntries")) && this.bean.isXMLParserSelectRegistryEntriesSet() && !var5._isSet(11)) {
               XMLParserSelectRegistryEntryMBean[] var11 = this.bean.getXMLParserSelectRegistryEntries();
               XMLParserSelectRegistryEntryMBean[] var12 = new XMLParserSelectRegistryEntryMBean[var11.length];

               for(var8 = 0; var8 < var12.length; ++var8) {
                  var12[var8] = (XMLParserSelectRegistryEntryMBean)((XMLParserSelectRegistryEntryMBean)this.createCopy((AbstractDescriptorBean)var11[var8], var2));
               }

               var5.setXMLParserSelectRegistryEntries(var12);
            }

            if ((var3 == null || !var3.contains("HandleEntityInvalidation")) && this.bean.isHandleEntityInvalidationSet()) {
               var5.setHandleEntityInvalidation(this.bean.isHandleEntityInvalidation());
            }

            return var5;
         } catch (RuntimeException var9) {
            throw var9;
         } catch (Exception var10) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var10);
         }
      }

      protected void inferSubTree(Class var1, Object var2) {
         super.inferSubTree(var1, var2);
         Object var3 = null;
         this.inferSubTree(this.bean.getEntitySpecRegistryEntries(), var1, var2);
         this.inferSubTree(this.bean.getParserSelectRegistryEntries(), var1, var2);
         this.inferSubTree(this.bean.getRegistryEntries(), var1, var2);
         this.inferSubTree(this.bean.getXMLEntitySpecRegistryEntries(), var1, var2);
         this.inferSubTree(this.bean.getXMLParserSelectRegistryEntries(), var1, var2);
      }
   }
}
