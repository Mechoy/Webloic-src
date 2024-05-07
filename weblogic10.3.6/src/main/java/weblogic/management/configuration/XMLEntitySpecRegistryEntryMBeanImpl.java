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

public class XMLEntitySpecRegistryEntryMBeanImpl extends ConfigurationMBeanImpl implements XMLEntitySpecRegistryEntryMBean, Serializable {
   private int _CacheTimeoutInterval;
   private String _EntityURI;
   private String _HandleEntityInvalidation;
   private String _PublicId;
   private String _SystemId;
   private String _WhenToCache;
   private static SchemaHelper2 _schemaHelper;

   public XMLEntitySpecRegistryEntryMBeanImpl() {
      this._initializeProperty(-1);
   }

   public XMLEntitySpecRegistryEntryMBeanImpl(DescriptorBean var1, int var2) {
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

   public String getEntityURI() {
      return this._EntityURI;
   }

   public boolean isEntityURISet() {
      return this._isSet(9);
   }

   public void setEntityURI(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._EntityURI;
      this._EntityURI = var1;
      this._postSet(9, var2, var1);
   }

   public String getWhenToCache() {
      return this._WhenToCache;
   }

   public boolean isWhenToCacheSet() {
      return this._isSet(10);
   }

   public void setWhenToCache(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{"cache-on-reference", "cache-at-initialization", "cache-never", "defer-to-registry-setting"};
      var1 = weblogic.descriptor.beangen.LegalChecks.checkInEnum("WhenToCache", var1, var2);
      String var3 = this._WhenToCache;
      this._WhenToCache = var1;
      this._postSet(10, var3, var1);
   }

   public int getCacheTimeoutInterval() {
      return this._CacheTimeoutInterval;
   }

   public boolean isCacheTimeoutIntervalSet() {
      return this._isSet(11);
   }

   public void setCacheTimeoutInterval(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkMin("CacheTimeoutInterval", var1, -1);
      int var2 = this._CacheTimeoutInterval;
      this._CacheTimeoutInterval = var1;
      this._postSet(11, var2, var1);
   }

   public String getHandleEntityInvalidation() {
      return this._HandleEntityInvalidation;
   }

   public boolean isHandleEntityInvalidationSet() {
      return this._isSet(12);
   }

   public void setHandleEntityInvalidation(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{"true", "false", "defer-to-registry-setting"};
      var1 = weblogic.descriptor.beangen.LegalChecks.checkInEnum("HandleEntityInvalidation", var1, var2);
      String var3 = this._HandleEntityInvalidation;
      this._HandleEntityInvalidation = var1;
      this._postSet(12, var3, var1);
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
         var1 = 11;
      }

      try {
         switch (var1) {
            case 11:
               this._CacheTimeoutInterval = -1;
               if (var2) {
                  break;
               }
            case 9:
               this._EntityURI = null;
               if (var2) {
                  break;
               }
            case 12:
               this._HandleEntityInvalidation = "defer-to-registry-setting";
               if (var2) {
                  break;
               }
            case 7:
               this._PublicId = null;
               if (var2) {
                  break;
               }
            case 8:
               this._SystemId = null;
               if (var2) {
                  break;
               }
            case 10:
               this._WhenToCache = "defer-to-registry-setting";
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
      return "XMLEntitySpecRegistryEntry";
   }

   public void putValue(String var1, Object var2) {
      if (var1.equals("CacheTimeoutInterval")) {
         int var4 = this._CacheTimeoutInterval;
         this._CacheTimeoutInterval = (Integer)var2;
         this._postSet(11, var4, this._CacheTimeoutInterval);
      } else {
         String var3;
         if (var1.equals("EntityURI")) {
            var3 = this._EntityURI;
            this._EntityURI = (String)var2;
            this._postSet(9, var3, this._EntityURI);
         } else if (var1.equals("HandleEntityInvalidation")) {
            var3 = this._HandleEntityInvalidation;
            this._HandleEntityInvalidation = (String)var2;
            this._postSet(12, var3, this._HandleEntityInvalidation);
         } else if (var1.equals("PublicId")) {
            var3 = this._PublicId;
            this._PublicId = (String)var2;
            this._postSet(7, var3, this._PublicId);
         } else if (var1.equals("SystemId")) {
            var3 = this._SystemId;
            this._SystemId = (String)var2;
            this._postSet(8, var3, this._SystemId);
         } else if (var1.equals("WhenToCache")) {
            var3 = this._WhenToCache;
            this._WhenToCache = (String)var2;
            this._postSet(10, var3, this._WhenToCache);
         } else {
            super.putValue(var1, var2);
         }
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("CacheTimeoutInterval")) {
         return new Integer(this._CacheTimeoutInterval);
      } else if (var1.equals("EntityURI")) {
         return this._EntityURI;
      } else if (var1.equals("HandleEntityInvalidation")) {
         return this._HandleEntityInvalidation;
      } else if (var1.equals("PublicId")) {
         return this._PublicId;
      } else if (var1.equals("SystemId")) {
         return this._SystemId;
      } else {
         return var1.equals("WhenToCache") ? this._WhenToCache : super.getValue(var1);
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
               break;
            case 10:
               if (var1.equals("entity-uri")) {
                  return 9;
               }
               break;
            case 13:
               if (var1.equals("when-to-cache")) {
                  return 10;
               }
               break;
            case 22:
               if (var1.equals("cache-timeout-interval")) {
                  return 11;
               }
               break;
            case 26:
               if (var1.equals("handle-entity-invalidation")) {
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
               return "entity-uri";
            case 10:
               return "when-to-cache";
            case 11:
               return "cache-timeout-interval";
            case 12:
               return "handle-entity-invalidation";
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
      private XMLEntitySpecRegistryEntryMBeanImpl bean;

      protected Helper(XMLEntitySpecRegistryEntryMBeanImpl var1) {
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
               return "EntityURI";
            case 10:
               return "WhenToCache";
            case 11:
               return "CacheTimeoutInterval";
            case 12:
               return "HandleEntityInvalidation";
            default:
               return super.getPropertyName(var1);
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("CacheTimeoutInterval")) {
            return 11;
         } else if (var1.equals("EntityURI")) {
            return 9;
         } else if (var1.equals("HandleEntityInvalidation")) {
            return 12;
         } else if (var1.equals("PublicId")) {
            return 7;
         } else if (var1.equals("SystemId")) {
            return 8;
         } else {
            return var1.equals("WhenToCache") ? 10 : super.getPropertyIndex(var1);
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
            if (this.bean.isCacheTimeoutIntervalSet()) {
               var2.append("CacheTimeoutInterval");
               var2.append(String.valueOf(this.bean.getCacheTimeoutInterval()));
            }

            if (this.bean.isEntityURISet()) {
               var2.append("EntityURI");
               var2.append(String.valueOf(this.bean.getEntityURI()));
            }

            if (this.bean.isHandleEntityInvalidationSet()) {
               var2.append("HandleEntityInvalidation");
               var2.append(String.valueOf(this.bean.getHandleEntityInvalidation()));
            }

            if (this.bean.isPublicIdSet()) {
               var2.append("PublicId");
               var2.append(String.valueOf(this.bean.getPublicId()));
            }

            if (this.bean.isSystemIdSet()) {
               var2.append("SystemId");
               var2.append(String.valueOf(this.bean.getSystemId()));
            }

            if (this.bean.isWhenToCacheSet()) {
               var2.append("WhenToCache");
               var2.append(String.valueOf(this.bean.getWhenToCache()));
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
            XMLEntitySpecRegistryEntryMBeanImpl var2 = (XMLEntitySpecRegistryEntryMBeanImpl)var1;
            this.computeDiff("CacheTimeoutInterval", this.bean.getCacheTimeoutInterval(), var2.getCacheTimeoutInterval(), true);
            this.computeDiff("EntityURI", this.bean.getEntityURI(), var2.getEntityURI(), true);
            this.computeDiff("HandleEntityInvalidation", this.bean.getHandleEntityInvalidation(), var2.getHandleEntityInvalidation(), true);
            this.computeDiff("PublicId", this.bean.getPublicId(), var2.getPublicId(), true);
            this.computeDiff("SystemId", this.bean.getSystemId(), var2.getSystemId(), true);
            this.computeDiff("WhenToCache", this.bean.getWhenToCache(), var2.getWhenToCache(), true);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            XMLEntitySpecRegistryEntryMBeanImpl var3 = (XMLEntitySpecRegistryEntryMBeanImpl)var1.getSourceBean();
            XMLEntitySpecRegistryEntryMBeanImpl var4 = (XMLEntitySpecRegistryEntryMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("CacheTimeoutInterval")) {
                  var3.setCacheTimeoutInterval(var4.getCacheTimeoutInterval());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 11);
               } else if (var5.equals("EntityURI")) {
                  var3.setEntityURI(var4.getEntityURI());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 9);
               } else if (var5.equals("HandleEntityInvalidation")) {
                  var3.setHandleEntityInvalidation(var4.getHandleEntityInvalidation());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 12);
               } else if (var5.equals("PublicId")) {
                  var3.setPublicId(var4.getPublicId());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 7);
               } else if (var5.equals("SystemId")) {
                  var3.setSystemId(var4.getSystemId());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 8);
               } else if (var5.equals("WhenToCache")) {
                  var3.setWhenToCache(var4.getWhenToCache());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 10);
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
            XMLEntitySpecRegistryEntryMBeanImpl var5 = (XMLEntitySpecRegistryEntryMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("CacheTimeoutInterval")) && this.bean.isCacheTimeoutIntervalSet()) {
               var5.setCacheTimeoutInterval(this.bean.getCacheTimeoutInterval());
            }

            if ((var3 == null || !var3.contains("EntityURI")) && this.bean.isEntityURISet()) {
               var5.setEntityURI(this.bean.getEntityURI());
            }

            if ((var3 == null || !var3.contains("HandleEntityInvalidation")) && this.bean.isHandleEntityInvalidationSet()) {
               var5.setHandleEntityInvalidation(this.bean.getHandleEntityInvalidation());
            }

            if ((var3 == null || !var3.contains("PublicId")) && this.bean.isPublicIdSet()) {
               var5.setPublicId(this.bean.getPublicId());
            }

            if ((var3 == null || !var3.contains("SystemId")) && this.bean.isSystemIdSet()) {
               var5.setSystemId(this.bean.getSystemId());
            }

            if ((var3 == null || !var3.contains("WhenToCache")) && this.bean.isWhenToCacheSet()) {
               var5.setWhenToCache(this.bean.getWhenToCache());
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
