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
import weblogic.utils.collections.ArrayIterator;
import weblogic.utils.collections.CombinedIterator;

public class CacheMBeanImpl extends DeploymentMBeanImpl implements CacheMBean, Serializable {
   private CacheAsyncListenersMBean _AsyncListeners;
   private String _EvictionPolicy;
   private CacheExpirationMBean _Expiration;
   private String _JNDIName;
   private CacheLoaderMBean _Loader;
   private int _MaxCacheUnits;
   private CacheStoreMBean _Store;
   private CacheTransactionMBean _Transactional;
   private String _WorkManager;
   private static SchemaHelper2 _schemaHelper;

   public CacheMBeanImpl() {
      this._initializeProperty(-1);
   }

   public CacheMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);
      this._initializeProperty(-1);
   }

   public String getJNDIName() {
      if (!this._isSet(9)) {
         try {
            return "com/bea/cache/" + this.getName();
         } catch (NullPointerException var2) {
         }
      }

      return this._JNDIName;
   }

   public boolean isJNDINameSet() {
      return this._isSet(9);
   }

   public void setJNDIName(String var1) {
      var1 = var1 == null ? null : var1.trim();
      weblogic.descriptor.beangen.LegalChecks.checkNonNull("JNDIName", var1);
      String var2 = this._JNDIName;
      this._JNDIName = var1;
      this._postSet(9, var2, var1);
   }

   public int getMaxCacheUnits() {
      return this._MaxCacheUnits;
   }

   public boolean isMaxCacheUnitsSet() {
      return this._isSet(10);
   }

   public void setMaxCacheUnits(int var1) {
      weblogic.descriptor.beangen.LegalChecks.checkMin("MaxCacheUnits", var1, 1);
      int var2 = this._MaxCacheUnits;
      this._MaxCacheUnits = var1;
      this._postSet(10, var2, var1);
   }

   public CacheExpirationMBean getExpiration() {
      return this._Expiration;
   }

   public boolean isExpirationSet() {
      return this._isSet(11) || this._isAnythingSet((AbstractDescriptorBean)this.getExpiration());
   }

   public void setExpiration(CacheExpirationMBean var1) throws InvalidAttributeValueException {
      AbstractDescriptorBean var2 = (AbstractDescriptorBean)var1;
      if (this._setParent(var2, this, 11)) {
         this._postCreate(var2);
      }

      CacheExpirationMBean var3 = this._Expiration;
      this._Expiration = var1;
      this._postSet(11, var3, var1);
   }

   public String getEvictionPolicy() {
      return this._EvictionPolicy;
   }

   public boolean isEvictionPolicySet() {
      return this._isSet(12);
   }

   public void setEvictionPolicy(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{"LRU", "NRU", "FIFO", "LFU"};
      var1 = weblogic.descriptor.beangen.LegalChecks.checkInEnum("EvictionPolicy", var1, var2);
      String var3 = this._EvictionPolicy;
      this._EvictionPolicy = var1;
      this._postSet(12, var3, var1);
   }

   public String getWorkManager() {
      return this._WorkManager;
   }

   public boolean isWorkManagerSet() {
      return this._isSet(13);
   }

   public void setWorkManager(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._WorkManager;
      this._WorkManager = var1;
      this._postSet(13, var2, var1);
   }

   public CacheLoaderMBean getLoader() {
      return this._Loader;
   }

   public boolean isLoaderSet() {
      return this._isSet(14) || this._isAnythingSet((AbstractDescriptorBean)this.getLoader());
   }

   public void setLoader(CacheLoaderMBean var1) throws InvalidAttributeValueException {
      AbstractDescriptorBean var2 = (AbstractDescriptorBean)var1;
      if (this._setParent(var2, this, 14)) {
         this._postCreate(var2);
      }

      CacheLoaderMBean var3 = this._Loader;
      this._Loader = var1;
      this._postSet(14, var3, var1);
   }

   public CacheStoreMBean getStore() {
      return this._Store;
   }

   public boolean isStoreSet() {
      return this._isSet(15) || this._isAnythingSet((AbstractDescriptorBean)this.getStore());
   }

   public void setStore(CacheStoreMBean var1) throws InvalidAttributeValueException {
      AbstractDescriptorBean var2 = (AbstractDescriptorBean)var1;
      if (this._setParent(var2, this, 15)) {
         this._postCreate(var2);
      }

      CacheStoreMBean var3 = this._Store;
      this._Store = var1;
      this._postSet(15, var3, var1);
   }

   public CacheTransactionMBean getTransactional() {
      return this._Transactional;
   }

   public boolean isTransactionalSet() {
      return this._isSet(16) || this._isAnythingSet((AbstractDescriptorBean)this.getTransactional());
   }

   public void setTransactional(CacheTransactionMBean var1) throws InvalidAttributeValueException {
      AbstractDescriptorBean var2 = (AbstractDescriptorBean)var1;
      if (this._setParent(var2, this, 16)) {
         this._postCreate(var2);
      }

      CacheTransactionMBean var3 = this._Transactional;
      this._Transactional = var1;
      this._postSet(16, var3, var1);
   }

   public CacheAsyncListenersMBean getAsyncListeners() {
      return this._AsyncListeners;
   }

   public boolean isAsyncListenersSet() {
      return this._isSet(17) || this._isAnythingSet((AbstractDescriptorBean)this.getAsyncListeners());
   }

   public void setAsyncListeners(CacheAsyncListenersMBean var1) throws InvalidAttributeValueException {
      AbstractDescriptorBean var2 = (AbstractDescriptorBean)var1;
      if (this._setParent(var2, this, 17)) {
         this._postCreate(var2);
      }

      CacheAsyncListenersMBean var3 = this._AsyncListeners;
      this._AsyncListeners = var1;
      this._postSet(17, var3, var1);
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
      return super._isAnythingSet() || this.isAsyncListenersSet() || this.isExpirationSet() || this.isLoaderSet() || this.isStoreSet() || this.isTransactionalSet();
   }

   private boolean _initializeProperty(int var1) {
      boolean var2 = var1 > -1;
      if (!var2) {
         var1 = 17;
      }

      try {
         switch (var1) {
            case 17:
               this._AsyncListeners = new CacheAsyncListenersMBeanImpl(this, 17);
               this._postCreate((AbstractDescriptorBean)this._AsyncListeners);
               if (var2) {
                  break;
               }
            case 12:
               this._EvictionPolicy = "LFU";
               if (var2) {
                  break;
               }
            case 11:
               this._Expiration = new CacheExpirationMBeanImpl(this, 11);
               this._postCreate((AbstractDescriptorBean)this._Expiration);
               if (var2) {
                  break;
               }
            case 9:
               this._JNDIName = null;
               if (var2) {
                  break;
               }
            case 14:
               this._Loader = new CacheLoaderMBeanImpl(this, 14);
               this._postCreate((AbstractDescriptorBean)this._Loader);
               if (var2) {
                  break;
               }
            case 10:
               this._MaxCacheUnits = 64;
               if (var2) {
                  break;
               }
            case 15:
               this._Store = new CacheStoreMBeanImpl(this, 15);
               this._postCreate((AbstractDescriptorBean)this._Store);
               if (var2) {
                  break;
               }
            case 16:
               this._Transactional = new CacheTransactionMBeanImpl(this, 16);
               this._postCreate((AbstractDescriptorBean)this._Transactional);
               if (var2) {
                  break;
               }
            case 13:
               this._WorkManager = null;
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
      return "Cache";
   }

   public void putValue(String var1, Object var2) {
      if (var1.equals("AsyncListeners")) {
         CacheAsyncListenersMBean var9 = this._AsyncListeners;
         this._AsyncListeners = (CacheAsyncListenersMBean)var2;
         this._postSet(17, var9, this._AsyncListeners);
      } else {
         String var3;
         if (var1.equals("EvictionPolicy")) {
            var3 = this._EvictionPolicy;
            this._EvictionPolicy = (String)var2;
            this._postSet(12, var3, this._EvictionPolicy);
         } else if (var1.equals("Expiration")) {
            CacheExpirationMBean var8 = this._Expiration;
            this._Expiration = (CacheExpirationMBean)var2;
            this._postSet(11, var8, this._Expiration);
         } else if (var1.equals("JNDIName")) {
            var3 = this._JNDIName;
            this._JNDIName = (String)var2;
            this._postSet(9, var3, this._JNDIName);
         } else if (var1.equals("Loader")) {
            CacheLoaderMBean var7 = this._Loader;
            this._Loader = (CacheLoaderMBean)var2;
            this._postSet(14, var7, this._Loader);
         } else if (var1.equals("MaxCacheUnits")) {
            int var6 = this._MaxCacheUnits;
            this._MaxCacheUnits = (Integer)var2;
            this._postSet(10, var6, this._MaxCacheUnits);
         } else if (var1.equals("Store")) {
            CacheStoreMBean var5 = this._Store;
            this._Store = (CacheStoreMBean)var2;
            this._postSet(15, var5, this._Store);
         } else if (var1.equals("Transactional")) {
            CacheTransactionMBean var4 = this._Transactional;
            this._Transactional = (CacheTransactionMBean)var2;
            this._postSet(16, var4, this._Transactional);
         } else if (var1.equals("WorkManager")) {
            var3 = this._WorkManager;
            this._WorkManager = (String)var2;
            this._postSet(13, var3, this._WorkManager);
         } else {
            super.putValue(var1, var2);
         }
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("AsyncListeners")) {
         return this._AsyncListeners;
      } else if (var1.equals("EvictionPolicy")) {
         return this._EvictionPolicy;
      } else if (var1.equals("Expiration")) {
         return this._Expiration;
      } else if (var1.equals("JNDIName")) {
         return this._JNDIName;
      } else if (var1.equals("Loader")) {
         return this._Loader;
      } else if (var1.equals("MaxCacheUnits")) {
         return new Integer(this._MaxCacheUnits);
      } else if (var1.equals("Store")) {
         return this._Store;
      } else if (var1.equals("Transactional")) {
         return this._Transactional;
      } else {
         return var1.equals("WorkManager") ? this._WorkManager : super.getValue(var1);
      }
   }

   public static class SchemaHelper2 extends DeploymentMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 5:
               if (var1.equals("store")) {
                  return 15;
               }
               break;
            case 6:
               if (var1.equals("loader")) {
                  return 14;
               }
            case 7:
            case 8:
            case 11:
            case 14:
            default:
               break;
            case 9:
               if (var1.equals("jndi-name")) {
                  return 9;
               }
               break;
            case 10:
               if (var1.equals("expiration")) {
                  return 11;
               }
               break;
            case 12:
               if (var1.equals("work-manager")) {
                  return 13;
               }
               break;
            case 13:
               if (var1.equals("transactional")) {
                  return 16;
               }
               break;
            case 15:
               if (var1.equals("async-listeners")) {
                  return 17;
               }

               if (var1.equals("eviction-policy")) {
                  return 12;
               }

               if (var1.equals("max-cache-units")) {
                  return 10;
               }
         }

         return super.getPropertyIndex(var1);
      }

      public SchemaHelper getSchemaHelper(int var1) {
         switch (var1) {
            case 11:
               return new CacheExpirationMBeanImpl.SchemaHelper2();
            case 12:
            case 13:
            default:
               return super.getSchemaHelper(var1);
            case 14:
               return new CacheLoaderMBeanImpl.SchemaHelper2();
            case 15:
               return new CacheStoreMBeanImpl.SchemaHelper2();
            case 16:
               return new CacheTransactionMBeanImpl.SchemaHelper2();
            case 17:
               return new CacheAsyncListenersMBeanImpl.SchemaHelper2();
         }
      }

      public String getElementName(int var1) {
         switch (var1) {
            case 9:
               return "jndi-name";
            case 10:
               return "max-cache-units";
            case 11:
               return "expiration";
            case 12:
               return "eviction-policy";
            case 13:
               return "work-manager";
            case 14:
               return "loader";
            case 15:
               return "store";
            case 16:
               return "transactional";
            case 17:
               return "async-listeners";
            default:
               return super.getElementName(var1);
         }
      }

      public boolean isArray(int var1) {
         switch (var1) {
            case 7:
               return true;
            default:
               return super.isArray(var1);
         }
      }

      public boolean isBean(int var1) {
         switch (var1) {
            case 11:
               return true;
            case 12:
            case 13:
            default:
               return super.isBean(var1);
            case 14:
               return true;
            case 15:
               return true;
            case 16:
               return true;
            case 17:
               return true;
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

   protected static class Helper extends DeploymentMBeanImpl.Helper {
      private CacheMBeanImpl bean;

      protected Helper(CacheMBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 9:
               return "JNDIName";
            case 10:
               return "MaxCacheUnits";
            case 11:
               return "Expiration";
            case 12:
               return "EvictionPolicy";
            case 13:
               return "WorkManager";
            case 14:
               return "Loader";
            case 15:
               return "Store";
            case 16:
               return "Transactional";
            case 17:
               return "AsyncListeners";
            default:
               return super.getPropertyName(var1);
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("AsyncListeners")) {
            return 17;
         } else if (var1.equals("EvictionPolicy")) {
            return 12;
         } else if (var1.equals("Expiration")) {
            return 11;
         } else if (var1.equals("JNDIName")) {
            return 9;
         } else if (var1.equals("Loader")) {
            return 14;
         } else if (var1.equals("MaxCacheUnits")) {
            return 10;
         } else if (var1.equals("Store")) {
            return 15;
         } else if (var1.equals("Transactional")) {
            return 16;
         } else {
            return var1.equals("WorkManager") ? 13 : super.getPropertyIndex(var1);
         }
      }

      public Iterator getChildren() {
         ArrayList var1 = new ArrayList();
         if (this.bean.getAsyncListeners() != null) {
            var1.add(new ArrayIterator(new CacheAsyncListenersMBean[]{this.bean.getAsyncListeners()}));
         }

         if (this.bean.getExpiration() != null) {
            var1.add(new ArrayIterator(new CacheExpirationMBean[]{this.bean.getExpiration()}));
         }

         if (this.bean.getLoader() != null) {
            var1.add(new ArrayIterator(new CacheLoaderMBean[]{this.bean.getLoader()}));
         }

         if (this.bean.getStore() != null) {
            var1.add(new ArrayIterator(new CacheStoreMBean[]{this.bean.getStore()}));
         }

         if (this.bean.getTransactional() != null) {
            var1.add(new ArrayIterator(new CacheTransactionMBean[]{this.bean.getTransactional()}));
         }

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
            var5 = this.computeChildHashValue(this.bean.getAsyncListeners());
            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            if (this.bean.isEvictionPolicySet()) {
               var2.append("EvictionPolicy");
               var2.append(String.valueOf(this.bean.getEvictionPolicy()));
            }

            var5 = this.computeChildHashValue(this.bean.getExpiration());
            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            if (this.bean.isJNDINameSet()) {
               var2.append("JNDIName");
               var2.append(String.valueOf(this.bean.getJNDIName()));
            }

            var5 = this.computeChildHashValue(this.bean.getLoader());
            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            if (this.bean.isMaxCacheUnitsSet()) {
               var2.append("MaxCacheUnits");
               var2.append(String.valueOf(this.bean.getMaxCacheUnits()));
            }

            var5 = this.computeChildHashValue(this.bean.getStore());
            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = this.computeChildHashValue(this.bean.getTransactional());
            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            if (this.bean.isWorkManagerSet()) {
               var2.append("WorkManager");
               var2.append(String.valueOf(this.bean.getWorkManager()));
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
            CacheMBeanImpl var2 = (CacheMBeanImpl)var1;
            this.computeSubDiff("AsyncListeners", this.bean.getAsyncListeners(), var2.getAsyncListeners());
            this.computeDiff("EvictionPolicy", this.bean.getEvictionPolicy(), var2.getEvictionPolicy(), false);
            this.computeSubDiff("Expiration", this.bean.getExpiration(), var2.getExpiration());
            this.computeDiff("JNDIName", this.bean.getJNDIName(), var2.getJNDIName(), false);
            this.computeSubDiff("Loader", this.bean.getLoader(), var2.getLoader());
            this.computeDiff("MaxCacheUnits", this.bean.getMaxCacheUnits(), var2.getMaxCacheUnits(), true);
            this.computeSubDiff("Store", this.bean.getStore(), var2.getStore());
            this.computeSubDiff("Transactional", this.bean.getTransactional(), var2.getTransactional());
            this.computeDiff("WorkManager", this.bean.getWorkManager(), var2.getWorkManager(), false);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            CacheMBeanImpl var3 = (CacheMBeanImpl)var1.getSourceBean();
            CacheMBeanImpl var4 = (CacheMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("AsyncListeners")) {
                  if (var6 == 2) {
                     var3.setAsyncListeners((CacheAsyncListenersMBean)this.createCopy((AbstractDescriptorBean)var4.getAsyncListeners()));
                  } else {
                     if (var6 != 3) {
                        throw new AssertionError("Invalid type: " + var6);
                     }

                     var3._destroySingleton("AsyncListeners", var3.getAsyncListeners());
                  }

                  var3._conditionalUnset(var2.isUnsetUpdate(), 17);
               } else if (var5.equals("EvictionPolicy")) {
                  var3.setEvictionPolicy(var4.getEvictionPolicy());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 12);
               } else if (var5.equals("Expiration")) {
                  if (var6 == 2) {
                     var3.setExpiration((CacheExpirationMBean)this.createCopy((AbstractDescriptorBean)var4.getExpiration()));
                  } else {
                     if (var6 != 3) {
                        throw new AssertionError("Invalid type: " + var6);
                     }

                     var3._destroySingleton("Expiration", var3.getExpiration());
                  }

                  var3._conditionalUnset(var2.isUnsetUpdate(), 11);
               } else if (var5.equals("JNDIName")) {
                  var3.setJNDIName(var4.getJNDIName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 9);
               } else if (var5.equals("Loader")) {
                  if (var6 == 2) {
                     var3.setLoader((CacheLoaderMBean)this.createCopy((AbstractDescriptorBean)var4.getLoader()));
                  } else {
                     if (var6 != 3) {
                        throw new AssertionError("Invalid type: " + var6);
                     }

                     var3._destroySingleton("Loader", var3.getLoader());
                  }

                  var3._conditionalUnset(var2.isUnsetUpdate(), 14);
               } else if (var5.equals("MaxCacheUnits")) {
                  var3.setMaxCacheUnits(var4.getMaxCacheUnits());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 10);
               } else if (var5.equals("Store")) {
                  if (var6 == 2) {
                     var3.setStore((CacheStoreMBean)this.createCopy((AbstractDescriptorBean)var4.getStore()));
                  } else {
                     if (var6 != 3) {
                        throw new AssertionError("Invalid type: " + var6);
                     }

                     var3._destroySingleton("Store", var3.getStore());
                  }

                  var3._conditionalUnset(var2.isUnsetUpdate(), 15);
               } else if (var5.equals("Transactional")) {
                  if (var6 == 2) {
                     var3.setTransactional((CacheTransactionMBean)this.createCopy((AbstractDescriptorBean)var4.getTransactional()));
                  } else {
                     if (var6 != 3) {
                        throw new AssertionError("Invalid type: " + var6);
                     }

                     var3._destroySingleton("Transactional", var3.getTransactional());
                  }

                  var3._conditionalUnset(var2.isUnsetUpdate(), 16);
               } else if (var5.equals("WorkManager")) {
                  var3.setWorkManager(var4.getWorkManager());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 13);
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
            CacheMBeanImpl var5 = (CacheMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("AsyncListeners")) && this.bean.isAsyncListenersSet() && !var5._isSet(17)) {
               CacheAsyncListenersMBean var4 = this.bean.getAsyncListeners();
               var5.setAsyncListeners((CacheAsyncListenersMBean)null);
               var5.setAsyncListeners(var4 == null ? null : (CacheAsyncListenersMBean)this.createCopy((AbstractDescriptorBean)var4, var2));
            }

            if ((var3 == null || !var3.contains("EvictionPolicy")) && this.bean.isEvictionPolicySet()) {
               var5.setEvictionPolicy(this.bean.getEvictionPolicy());
            }

            if ((var3 == null || !var3.contains("Expiration")) && this.bean.isExpirationSet() && !var5._isSet(11)) {
               CacheExpirationMBean var8 = this.bean.getExpiration();
               var5.setExpiration((CacheExpirationMBean)null);
               var5.setExpiration(var8 == null ? null : (CacheExpirationMBean)this.createCopy((AbstractDescriptorBean)var8, var2));
            }

            if ((var3 == null || !var3.contains("JNDIName")) && this.bean.isJNDINameSet()) {
               var5.setJNDIName(this.bean.getJNDIName());
            }

            if ((var3 == null || !var3.contains("Loader")) && this.bean.isLoaderSet() && !var5._isSet(14)) {
               CacheLoaderMBean var9 = this.bean.getLoader();
               var5.setLoader((CacheLoaderMBean)null);
               var5.setLoader(var9 == null ? null : (CacheLoaderMBean)this.createCopy((AbstractDescriptorBean)var9, var2));
            }

            if ((var3 == null || !var3.contains("MaxCacheUnits")) && this.bean.isMaxCacheUnitsSet()) {
               var5.setMaxCacheUnits(this.bean.getMaxCacheUnits());
            }

            if ((var3 == null || !var3.contains("Store")) && this.bean.isStoreSet() && !var5._isSet(15)) {
               CacheStoreMBean var10 = this.bean.getStore();
               var5.setStore((CacheStoreMBean)null);
               var5.setStore(var10 == null ? null : (CacheStoreMBean)this.createCopy((AbstractDescriptorBean)var10, var2));
            }

            if ((var3 == null || !var3.contains("Transactional")) && this.bean.isTransactionalSet() && !var5._isSet(16)) {
               CacheTransactionMBean var11 = this.bean.getTransactional();
               var5.setTransactional((CacheTransactionMBean)null);
               var5.setTransactional(var11 == null ? null : (CacheTransactionMBean)this.createCopy((AbstractDescriptorBean)var11, var2));
            }

            if ((var3 == null || !var3.contains("WorkManager")) && this.bean.isWorkManagerSet()) {
               var5.setWorkManager(this.bean.getWorkManager());
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
         this.inferSubTree(this.bean.getAsyncListeners(), var1, var2);
         this.inferSubTree(this.bean.getExpiration(), var1, var2);
         this.inferSubTree(this.bean.getLoader(), var1, var2);
         this.inferSubTree(this.bean.getStore(), var1, var2);
         this.inferSubTree(this.bean.getTransactional(), var1, var2);
      }
   }
}
