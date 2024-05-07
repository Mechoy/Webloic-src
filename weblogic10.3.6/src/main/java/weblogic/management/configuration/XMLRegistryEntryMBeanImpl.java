package weblogic.management.configuration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.zip.CRC32;
import javax.management.InvalidAttributeValueException;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBeanHelper;
import weblogic.descriptor.internal.Munger;
import weblogic.descriptor.internal.SchemaHelper;
import weblogic.utils.collections.CombinedIterator;

public class XMLRegistryEntryMBeanImpl extends ConfigurationMBeanImpl implements XMLRegistryEntryMBean, Serializable {
   private String _DocumentBuilderFactory;
   private String _EntityPath;
   private String _ParserClassName;
   private String _PublicId;
   private String _RootElementTag;
   private String _SAXParserFactory;
   private String _SystemId;
   private static SchemaHelper2 _schemaHelper;

   public XMLRegistryEntryMBeanImpl() {
      this._initializeProperty(-1);
   }

   public XMLRegistryEntryMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);
      this._initializeProperty(-1);
   }

   public String getPublicId() {
      return this._PublicId;
   }

   public boolean isPublicIdSet() {
      return this._isSet(7);
   }

   public void setPublicId(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._PublicId;
      this._PublicId = var1;
      this._postSet(7, var2, var1);
   }

   public String getSystemId() {
      return this._SystemId;
   }

   public boolean isSystemIdSet() {
      return this._isSet(8);
   }

   public void setSystemId(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._SystemId;
      this._SystemId = var1;
      this._postSet(8, var2, var1);
   }

   public String getRootElementTag() {
      return this._RootElementTag;
   }

   public boolean isRootElementTagSet() {
      return this._isSet(9);
   }

   public void setRootElementTag(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._RootElementTag;
      this._RootElementTag = var1;
      this._postSet(9, var2, var1);
   }

   public String getEntityPath() {
      return this._EntityPath;
   }

   public boolean isEntityPathSet() {
      return this._isSet(10);
   }

   public void setEntityPath(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._EntityPath;
      this._EntityPath = var1;
      this._postSet(10, var2, var1);
   }

   public String getParserClassName() {
      return this._ParserClassName;
   }

   public boolean isParserClassNameSet() {
      return this._isSet(11);
   }

   public void setParserClassName(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._ParserClassName;
      this._ParserClassName = var1;
      this._postSet(11, var2, var1);
   }

   public String getDocumentBuilderFactory() {
      return this._DocumentBuilderFactory;
   }

   public boolean isDocumentBuilderFactorySet() {
      return this._isSet(12);
   }

   public void setDocumentBuilderFactory(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._DocumentBuilderFactory;
      this._DocumentBuilderFactory = var1;
      this._postSet(12, var2, var1);
   }

   public String getSAXParserFactory() {
      return this._SAXParserFactory;
   }

   public boolean isSAXParserFactorySet() {
      return this._isSet(13);
   }

   public void setSAXParserFactory(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._SAXParserFactory;
      this._SAXParserFactory = var1;
      this._postSet(13, var2, var1);
   }

   public Object _getKey() {
      return super._getKey();
   }

   public void _validate() throws IllegalArgumentException {
      super._validate();
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
         var1 = 12;
      }

      try {
         switch (var1) {
            case 12:
               this._DocumentBuilderFactory = null;
               if (var2) {
                  break;
               }
            case 10:
               this._EntityPath = null;
               if (var2) {
                  break;
               }
            case 11:
               this._ParserClassName = null;
               if (var2) {
                  break;
               }
            case 7:
               this._PublicId = null;
               if (var2) {
                  break;
               }
            case 9:
               this._RootElementTag = null;
               if (var2) {
                  break;
               }
            case 13:
               this._SAXParserFactory = null;
               if (var2) {
                  break;
               }
            case 8:
               this._SystemId = null;
               if (var2) {
                  break;
               }
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
      return "XMLRegistryEntry";
   }

   public void putValue(String var1, Object var2) {
      String var3;
      if (var1.equals("DocumentBuilderFactory")) {
         var3 = this._DocumentBuilderFactory;
         this._DocumentBuilderFactory = (String)var2;
         this._postSet(12, var3, this._DocumentBuilderFactory);
      } else if (var1.equals("EntityPath")) {
         var3 = this._EntityPath;
         this._EntityPath = (String)var2;
         this._postSet(10, var3, this._EntityPath);
      } else if (var1.equals("ParserClassName")) {
         var3 = this._ParserClassName;
         this._ParserClassName = (String)var2;
         this._postSet(11, var3, this._ParserClassName);
      } else if (var1.equals("PublicId")) {
         var3 = this._PublicId;
         this._PublicId = (String)var2;
         this._postSet(7, var3, this._PublicId);
      } else if (var1.equals("RootElementTag")) {
         var3 = this._RootElementTag;
         this._RootElementTag = (String)var2;
         this._postSet(9, var3, this._RootElementTag);
      } else if (var1.equals("SAXParserFactory")) {
         var3 = this._SAXParserFactory;
         this._SAXParserFactory = (String)var2;
         this._postSet(13, var3, this._SAXParserFactory);
      } else if (var1.equals("SystemId")) {
         var3 = this._SystemId;
         this._SystemId = (String)var2;
         this._postSet(8, var3, this._SystemId);
      } else {
         super.putValue(var1, var2);
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("DocumentBuilderFactory")) {
         return this._DocumentBuilderFactory;
      } else if (var1.equals("EntityPath")) {
         return this._EntityPath;
      } else if (var1.equals("ParserClassName")) {
         return this._ParserClassName;
      } else if (var1.equals("PublicId")) {
         return this._PublicId;
      } else if (var1.equals("RootElementTag")) {
         return this._RootElementTag;
      } else if (var1.equals("SAXParserFactory")) {
         return this._SAXParserFactory;
      } else {
         return var1.equals("SystemId") ? this._SystemId : super.getValue(var1);
      }
   }

   public static class SchemaHelper2 extends ConfigurationMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 9:
               if (var1.equals("public-id")) {
                  return 7;
               }

               if (var1.equals("system-id")) {
                  return 8;
               }
            case 10:
            case 12:
            case 13:
            case 14:
            case 15:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            default:
               break;
            case 11:
               if (var1.equals("entity-path")) {
                  return 10;
               }
               break;
            case 16:
               if (var1.equals("root-element-tag")) {
                  return 9;
               }
               break;
            case 17:
               if (var1.equals("parser-class-name")) {
                  return 11;
               }
               break;
            case 18:
               if (var1.equals("sax-parser-factory")) {
                  return 13;
               }
               break;
            case 24:
               if (var1.equals("document-builder-factory")) {
                  return 12;
               }
         }

         return super.getPropertyIndex(var1);
      }

      public SchemaHelper getSchemaHelper(int var1) {
         switch (var1) {
            default:
               return super.getSchemaHelper(var1);
         }
      }

      public String getElementName(int var1) {
         switch (var1) {
            case 7:
               return "public-id";
            case 8:
               return "system-id";
            case 9:
               return "root-element-tag";
            case 10:
               return "entity-path";
            case 11:
               return "parser-class-name";
            case 12:
               return "document-builder-factory";
            case 13:
               return "sax-parser-factory";
            default:
               return super.getElementName(var1);
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

      public String[] getKeyElementNames() {
         ArrayList var1 = new ArrayList();
         var1.add("name");
         return (String[])((String[])var1.toArray(new String[0]));
      }
   }

   protected static class Helper extends ConfigurationMBeanImpl.Helper {
      private XMLRegistryEntryMBeanImpl bean;

      protected Helper(XMLRegistryEntryMBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 7:
               return "PublicId";
            case 8:
               return "SystemId";
            case 9:
               return "RootElementTag";
            case 10:
               return "EntityPath";
            case 11:
               return "ParserClassName";
            case 12:
               return "DocumentBuilderFactory";
            case 13:
               return "SAXParserFactory";
            default:
               return super.getPropertyName(var1);
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("DocumentBuilderFactory")) {
            return 12;
         } else if (var1.equals("EntityPath")) {
            return 10;
         } else if (var1.equals("ParserClassName")) {
            return 11;
         } else if (var1.equals("PublicId")) {
            return 7;
         } else if (var1.equals("RootElementTag")) {
            return 9;
         } else if (var1.equals("SAXParserFactory")) {
            return 13;
         } else {
            return var1.equals("SystemId") ? 8 : super.getPropertyIndex(var1);
         }
      }

      public Iterator getChildren() {
         ArrayList var1 = new ArrayList();
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

            if (this.bean.isEntityPathSet()) {
               var2.append("EntityPath");
               var2.append(String.valueOf(this.bean.getEntityPath()));
            }

            if (this.bean.isParserClassNameSet()) {
               var2.append("ParserClassName");
               var2.append(String.valueOf(this.bean.getParserClassName()));
            }

            if (this.bean.isPublicIdSet()) {
               var2.append("PublicId");
               var2.append(String.valueOf(this.bean.getPublicId()));
            }

            if (this.bean.isRootElementTagSet()) {
               var2.append("RootElementTag");
               var2.append(String.valueOf(this.bean.getRootElementTag()));
            }

            if (this.bean.isSAXParserFactorySet()) {
               var2.append("SAXParserFactory");
               var2.append(String.valueOf(this.bean.getSAXParserFactory()));
            }

            if (this.bean.isSystemIdSet()) {
               var2.append("SystemId");
               var2.append(String.valueOf(this.bean.getSystemId()));
            }

            var1.update(var2.toString().getBytes());
            return var1.getValue();
         } catch (Exception var7) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var7);
         }
      }

      protected void computeDiff(AbstractDescriptorBean var1) {
         try {
            super.computeDiff(var1);
            XMLRegistryEntryMBeanImpl var2 = (XMLRegistryEntryMBeanImpl)var1;
            this.computeDiff("DocumentBuilderFactory", this.bean.getDocumentBuilderFactory(), var2.getDocumentBuilderFactory(), true);
            this.computeDiff("EntityPath", this.bean.getEntityPath(), var2.getEntityPath(), true);
            this.computeDiff("ParserClassName", this.bean.getParserClassName(), var2.getParserClassName(), true);
            this.computeDiff("PublicId", this.bean.getPublicId(), var2.getPublicId(), true);
            this.computeDiff("RootElementTag", this.bean.getRootElementTag(), var2.getRootElementTag(), true);
            this.computeDiff("SAXParserFactory", this.bean.getSAXParserFactory(), var2.getSAXParserFactory(), true);
            this.computeDiff("SystemId", this.bean.getSystemId(), var2.getSystemId(), true);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            XMLRegistryEntryMBeanImpl var3 = (XMLRegistryEntryMBeanImpl)var1.getSourceBean();
            XMLRegistryEntryMBeanImpl var4 = (XMLRegistryEntryMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("DocumentBuilderFactory")) {
                  var3.setDocumentBuilderFactory(var4.getDocumentBuilderFactory());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 12);
               } else if (var5.equals("EntityPath")) {
                  var3.setEntityPath(var4.getEntityPath());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 10);
               } else if (var5.equals("ParserClassName")) {
                  var3.setParserClassName(var4.getParserClassName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 11);
               } else if (var5.equals("PublicId")) {
                  var3.setPublicId(var4.getPublicId());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 7);
               } else if (var5.equals("RootElementTag")) {
                  var3.setRootElementTag(var4.getRootElementTag());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 9);
               } else if (var5.equals("SAXParserFactory")) {
                  var3.setSAXParserFactory(var4.getSAXParserFactory());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 13);
               } else if (var5.equals("SystemId")) {
                  var3.setSystemId(var4.getSystemId());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 8);
               } else {
                  super.applyPropertyUpdate(var1, var2);
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
            XMLRegistryEntryMBeanImpl var5 = (XMLRegistryEntryMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("DocumentBuilderFactory")) && this.bean.isDocumentBuilderFactorySet()) {
               var5.setDocumentBuilderFactory(this.bean.getDocumentBuilderFactory());
            }

            if ((var3 == null || !var3.contains("EntityPath")) && this.bean.isEntityPathSet()) {
               var5.setEntityPath(this.bean.getEntityPath());
            }

            if ((var3 == null || !var3.contains("ParserClassName")) && this.bean.isParserClassNameSet()) {
               var5.setParserClassName(this.bean.getParserClassName());
            }

            if ((var3 == null || !var3.contains("PublicId")) && this.bean.isPublicIdSet()) {
               var5.setPublicId(this.bean.getPublicId());
            }

            if ((var3 == null || !var3.contains("RootElementTag")) && this.bean.isRootElementTagSet()) {
               var5.setRootElementTag(this.bean.getRootElementTag());
            }

            if ((var3 == null || !var3.contains("SAXParserFactory")) && this.bean.isSAXParserFactorySet()) {
               var5.setSAXParserFactory(this.bean.getSAXParserFactory());
            }

            if ((var3 == null || !var3.contains("SystemId")) && this.bean.isSystemIdSet()) {
               var5.setSystemId(this.bean.getSystemId());
            }

            return var5;
         } catch (RuntimeException var6) {
            throw var6;
         } catch (Exception var7) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var7);
         }
      }

      protected void inferSubTree(Class var1, Object var2) {
         super.inferSubTree(var1, var2);
         Object var3 = null;
      }
   }
}
