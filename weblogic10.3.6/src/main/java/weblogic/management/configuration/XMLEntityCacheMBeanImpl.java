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
import weblogic.management.runtime.EntityCacheCumulativeRuntimeMBean;
import weblogic.management.runtime.EntityCacheCurrentStateRuntimeMBean;
import weblogic.utils.collections.CombinedIterator;

public class XMLEntityCacheMBeanImpl extends ConfigurationMBeanImpl implements XMLEntityCacheMBean, Serializable {
   private int _CacheDiskSize;
   private String _CacheLocation;
   private int _CacheMemorySize;
   private int _CacheTimeoutInterval;
   private EntityCacheCurrentStateRuntimeMBean _EntityCacheCurrentRuntime;
   private EntityCacheCumulativeRuntimeMBean _EntityCacheHistoricalRuntime;
   private EntityCacheCumulativeRuntimeMBean _EntityCacheSessionRuntime;
   private int _MaxSize;
   private static SchemaHelper2 _schemaHelper;

   public XMLEntityCacheMBeanImpl() {
      this._initializeProperty(-1);
   }

   public XMLEntityCacheMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);
      this._initializeProperty(-1);
   }

   public String getCacheLocation() {
      return this._CacheLocation;
   }

   public boolean isCacheLocationSet() {
      return this._isSet(7);
   }

   public void setCacheLocation(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._CacheLocation;
      this._CacheLocation = var1;
      this._postSet(7, var2, var1);
   }

   public int getCacheMemorySize() {
      return this._CacheMemorySize;
   }

   public boolean isCacheMemorySizeSet() {
      return this._isSet(8);
   }

   public void setCacheMemorySize(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkMin("CacheMemorySize", var1, 0);
      int var2 = this._CacheMemorySize;
      this._CacheMemorySize = var1;
      this._postSet(8, var2, var1);
   }

   public int getCacheDiskSize() {
      return this._CacheDiskSize;
   }

   public boolean isCacheDiskSizeSet() {
      return this._isSet(9);
   }

   public void setCacheDiskSize(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkMin("CacheDiskSize", var1, 0);
      int var2 = this._CacheDiskSize;
      this._CacheDiskSize = var1;
      this._postSet(9, var2, var1);
   }

   public int getCacheTimeoutInterval() {
      return this._CacheTimeoutInterval;
   }

   public boolean isCacheTimeoutIntervalSet() {
      return this._isSet(10);
   }

   public void setCacheTimeoutInterval(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkMin("CacheTimeoutInterval", var1, 0);
      int var2 = this._CacheTimeoutInterval;
      this._CacheTimeoutInterval = var1;
      this._postSet(10, var2, var1);
   }

   public EntityCacheCurrentStateRuntimeMBean getEntityCacheCurrentRuntime() {
      return this._EntityCacheCurrentRuntime;
   }

   public boolean isEntityCacheCurrentRuntimeSet() {
      return this._isSet(11);
   }

   public void setEntityCacheCurrentRuntime(EntityCacheCurrentStateRuntimeMBean var1) {
      this._EntityCacheCurrentRuntime = var1;
   }

   public EntityCacheCumulativeRuntimeMBean getEntityCacheSessionRuntime() {
      return this._EntityCacheSessionRuntime;
   }

   public boolean isEntityCacheSessionRuntimeSet() {
      return this._isSet(12);
   }

   public void setEntityCacheSessionRuntime(EntityCacheCumulativeRuntimeMBean var1) {
      this._EntityCacheSessionRuntime = var1;
   }

   public EntityCacheCumulativeRuntimeMBean getEntityCacheHistoricalRuntime() {
      return this._EntityCacheHistoricalRuntime;
   }

   public boolean isEntityCacheHistoricalRuntimeSet() {
      return this._isSet(13);
   }

   public void setEntityCacheHistoricalRuntime(EntityCacheCumulativeRuntimeMBean var1) {
      this._EntityCacheHistoricalRuntime = var1;
   }

   public int getMaxSize() {
      return this._MaxSize;
   }

   public boolean isMaxSizeSet() {
      return this._isSet(14);
   }

   public void setMaxSize(int var1) {
      int var2 = this._MaxSize;
      this._MaxSize = var1;
      this._postSet(14, var2, var1);
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
         var1 = 9;
      }

      try {
         switch (var1) {
            case 9:
               this._CacheDiskSize = 5;
               if (var2) {
                  break;
               }
            case 7:
               this._CacheLocation = "xmlcache";
               if (var2) {
                  break;
               }
            case 8:
               this._CacheMemorySize = 500;
               if (var2) {
                  break;
               }
            case 10:
               this._CacheTimeoutInterval = 120;
               if (var2) {
                  break;
               }
            case 11:
               this._EntityCacheCurrentRuntime = null;
               if (var2) {
                  break;
               }
            case 13:
               this._EntityCacheHistoricalRuntime = null;
               if (var2) {
                  break;
               }
            case 12:
               this._EntityCacheSessionRuntime = null;
               if (var2) {
                  break;
               }
            case 14:
               this._MaxSize = 0;
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
      return "XMLEntityCache";
   }

   public void putValue(String var1, Object var2) {
      int var3;
      if (var1.equals("CacheDiskSize")) {
         var3 = this._CacheDiskSize;
         this._CacheDiskSize = (Integer)var2;
         this._postSet(9, var3, this._CacheDiskSize);
      } else if (var1.equals("CacheLocation")) {
         String var6 = this._CacheLocation;
         this._CacheLocation = (String)var2;
         this._postSet(7, var6, this._CacheLocation);
      } else if (var1.equals("CacheMemorySize")) {
         var3 = this._CacheMemorySize;
         this._CacheMemorySize = (Integer)var2;
         this._postSet(8, var3, this._CacheMemorySize);
      } else if (var1.equals("CacheTimeoutInterval")) {
         var3 = this._CacheTimeoutInterval;
         this._CacheTimeoutInterval = (Integer)var2;
         this._postSet(10, var3, this._CacheTimeoutInterval);
      } else if (var1.equals("EntityCacheCurrentRuntime")) {
         EntityCacheCurrentStateRuntimeMBean var5 = this._EntityCacheCurrentRuntime;
         this._EntityCacheCurrentRuntime = (EntityCacheCurrentStateRuntimeMBean)var2;
         this._postSet(11, var5, this._EntityCacheCurrentRuntime);
      } else {
         EntityCacheCumulativeRuntimeMBean var4;
         if (var1.equals("EntityCacheHistoricalRuntime")) {
            var4 = this._EntityCacheHistoricalRuntime;
            this._EntityCacheHistoricalRuntime = (EntityCacheCumulativeRuntimeMBean)var2;
            this._postSet(13, var4, this._EntityCacheHistoricalRuntime);
         } else if (var1.equals("EntityCacheSessionRuntime")) {
            var4 = this._EntityCacheSessionRuntime;
            this._EntityCacheSessionRuntime = (EntityCacheCumulativeRuntimeMBean)var2;
            this._postSet(12, var4, this._EntityCacheSessionRuntime);
         } else if (var1.equals("MaxSize")) {
            var3 = this._MaxSize;
            this._MaxSize = (Integer)var2;
            this._postSet(14, var3, this._MaxSize);
         } else {
            super.putValue(var1, var2);
         }
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("CacheDiskSize")) {
         return new Integer(this._CacheDiskSize);
      } else if (var1.equals("CacheLocation")) {
         return this._CacheLocation;
      } else if (var1.equals("CacheMemorySize")) {
         return new Integer(this._CacheMemorySize);
      } else if (var1.equals("CacheTimeoutInterval")) {
         return new Integer(this._CacheTimeoutInterval);
      } else if (var1.equals("EntityCacheCurrentRuntime")) {
         return this._EntityCacheCurrentRuntime;
      } else if (var1.equals("EntityCacheHistoricalRuntime")) {
         return this._EntityCacheHistoricalRuntime;
      } else if (var1.equals("EntityCacheSessionRuntime")) {
         return this._EntityCacheSessionRuntime;
      } else {
         return var1.equals("MaxSize") ? new Integer(this._MaxSize) : super.getValue(var1);
      }
   }

   public static class SchemaHelper2 extends ConfigurationMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 8:
               if (var1.equals("max-size")) {
                  return 14;
               }
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 16:
            case 18:
            case 19:
            case 20:
            case 21:
            case 23:
            case 24:
            case 25:
            case 26:
            case 27:
            case 29:
            case 30:
            default:
               break;
            case 14:
               if (var1.equals("cache-location")) {
                  return 7;
               }
               break;
            case 15:
               if (var1.equals("cache-disk-size")) {
                  return 9;
               }
               break;
            case 17:
               if (var1.equals("cache-memory-size")) {
                  return 8;
               }
               break;
            case 22:
               if (var1.equals("cache-timeout-interval")) {
                  return 10;
               }
               break;
            case 28:
               if (var1.equals("entity-cache-current-runtime")) {
                  return 11;
               }

               if (var1.equals("entity-cache-session-runtime")) {
                  return 12;
               }
               break;
            case 31:
               if (var1.equals("entity-cache-historical-runtime")) {
                  return 13;
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
               return "cache-location";
            case 8:
               return "cache-memory-size";
            case 9:
               return "cache-disk-size";
            case 10:
               return "cache-timeout-interval";
            case 11:
               return "entity-cache-current-runtime";
            case 12:
               return "entity-cache-session-runtime";
            case 13:
               return "entity-cache-historical-runtime";
            case 14:
               return "max-size";
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
      private XMLEntityCacheMBeanImpl bean;

      protected Helper(XMLEntityCacheMBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 7:
               return "CacheLocation";
            case 8:
               return "CacheMemorySize";
            case 9:
               return "CacheDiskSize";
            case 10:
               return "CacheTimeoutInterval";
            case 11:
               return "EntityCacheCurrentRuntime";
            case 12:
               return "EntityCacheSessionRuntime";
            case 13:
               return "EntityCacheHistoricalRuntime";
            case 14:
               return "MaxSize";
            default:
               return super.getPropertyName(var1);
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("CacheDiskSize")) {
            return 9;
         } else if (var1.equals("CacheLocation")) {
            return 7;
         } else if (var1.equals("CacheMemorySize")) {
            return 8;
         } else if (var1.equals("CacheTimeoutInterval")) {
            return 10;
         } else if (var1.equals("EntityCacheCurrentRuntime")) {
            return 11;
         } else if (var1.equals("EntityCacheHistoricalRuntime")) {
            return 13;
         } else if (var1.equals("EntityCacheSessionRuntime")) {
            return 12;
         } else {
            return var1.equals("MaxSize") ? 14 : super.getPropertyIndex(var1);
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
            if (this.bean.isCacheDiskSizeSet()) {
               var2.append("CacheDiskSize");
               var2.append(String.valueOf(this.bean.getCacheDiskSize()));
            }

            if (this.bean.isCacheLocationSet()) {
               var2.append("CacheLocation");
               var2.append(String.valueOf(this.bean.getCacheLocation()));
            }

            if (this.bean.isCacheMemorySizeSet()) {
               var2.append("CacheMemorySize");
               var2.append(String.valueOf(this.bean.getCacheMemorySize()));
            }

            if (this.bean.isCacheTimeoutIntervalSet()) {
               var2.append("CacheTimeoutInterval");
               var2.append(String.valueOf(this.bean.getCacheTimeoutInterval()));
            }

            if (this.bean.isEntityCacheCurrentRuntimeSet()) {
               var2.append("EntityCacheCurrentRuntime");
               var2.append(String.valueOf(this.bean.getEntityCacheCurrentRuntime()));
            }

            if (this.bean.isEntityCacheHistoricalRuntimeSet()) {
               var2.append("EntityCacheHistoricalRuntime");
               var2.append(String.valueOf(this.bean.getEntityCacheHistoricalRuntime()));
            }

            if (this.bean.isEntityCacheSessionRuntimeSet()) {
               var2.append("EntityCacheSessionRuntime");
               var2.append(String.valueOf(this.bean.getEntityCacheSessionRuntime()));
            }

            if (this.bean.isMaxSizeSet()) {
               var2.append("MaxSize");
               var2.append(String.valueOf(this.bean.getMaxSize()));
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
            XMLEntityCacheMBeanImpl var2 = (XMLEntityCacheMBeanImpl)var1;
            this.computeDiff("CacheDiskSize", this.bean.getCacheDiskSize(), var2.getCacheDiskSize(), true);
            this.computeDiff("CacheLocation", this.bean.getCacheLocation(), var2.getCacheLocation(), true);
            this.computeDiff("CacheMemorySize", this.bean.getCacheMemorySize(), var2.getCacheMemorySize(), true);
            this.computeDiff("CacheTimeoutInterval", this.bean.getCacheTimeoutInterval(), var2.getCacheTimeoutInterval(), true);
            this.computeDiff("MaxSize", this.bean.getMaxSize(), var2.getMaxSize(), true);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            XMLEntityCacheMBeanImpl var3 = (XMLEntityCacheMBeanImpl)var1.getSourceBean();
            XMLEntityCacheMBeanImpl var4 = (XMLEntityCacheMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("CacheDiskSize")) {
                  var3.setCacheDiskSize(var4.getCacheDiskSize());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 9);
               } else if (var5.equals("CacheLocation")) {
                  var3.setCacheLocation(var4.getCacheLocation());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 7);
               } else if (var5.equals("CacheMemorySize")) {
                  var3.setCacheMemorySize(var4.getCacheMemorySize());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 8);
               } else if (var5.equals("CacheTimeoutInterval")) {
                  var3.setCacheTimeoutInterval(var4.getCacheTimeoutInterval());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 10);
               } else if (!var5.equals("EntityCacheCurrentRuntime") && !var5.equals("EntityCacheHistoricalRuntime") && !var5.equals("EntityCacheSessionRuntime")) {
                  if (var5.equals("MaxSize")) {
                     var3.setMaxSize(var4.getMaxSize());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 14);
                  } else {
                     super.applyPropertyUpdate(var1, var2);
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
            XMLEntityCacheMBeanImpl var5 = (XMLEntityCacheMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("CacheDiskSize")) && this.bean.isCacheDiskSizeSet()) {
               var5.setCacheDiskSize(this.bean.getCacheDiskSize());
            }

            if ((var3 == null || !var3.contains("CacheLocation")) && this.bean.isCacheLocationSet()) {
               var5.setCacheLocation(this.bean.getCacheLocation());
            }

            if ((var3 == null || !var3.contains("CacheMemorySize")) && this.bean.isCacheMemorySizeSet()) {
               var5.setCacheMemorySize(this.bean.getCacheMemorySize());
            }

            if ((var3 == null || !var3.contains("CacheTimeoutInterval")) && this.bean.isCacheTimeoutIntervalSet()) {
               var5.setCacheTimeoutInterval(this.bean.getCacheTimeoutInterval());
            }

            if ((var3 == null || !var3.contains("MaxSize")) && this.bean.isMaxSizeSet()) {
               var5.setMaxSize(this.bean.getMaxSize());
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
