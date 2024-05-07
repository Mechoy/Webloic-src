package weblogic.management.configuration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.zip.CRC32;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBeanHelper;
import weblogic.descriptor.internal.Munger;
import weblogic.descriptor.internal.SchemaHelper;
import weblogic.utils.collections.CombinedIterator;

public class CacheStoreMBeanImpl extends ConfigurationMBeanImpl implements CacheStoreMBean, Serializable {
   private int _BufferMaxSize;
   private int _BufferWriteAttempts;
   private long _BufferWriteTimeout;
   private String _CustomStore;
   private int _StoreBatchSize;
   private String _WorkManager;
   private String _WritePolicy;
   private static SchemaHelper2 _schemaHelper;

   public CacheStoreMBeanImpl() {
      this._initializeProperty(-1);
   }

   public CacheStoreMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);
      this._initializeProperty(-1);
   }

   public String getCustomStore() {
      return this._CustomStore;
   }

   public boolean isCustomStoreSet() {
      return this._isSet(7);
   }

   public void setCustomStore(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._CustomStore;
      this._CustomStore = var1;
      this._postSet(7, var2, var1);
   }

   public String getWritePolicy() {
      if (!this._isSet(8)) {
         try {
            return this.getCustomStore() != null ? "WriteThrough" : "None";
         } catch (NullPointerException var2) {
         }
      }

      return this._WritePolicy;
   }

   public boolean isWritePolicySet() {
      return this._isSet(8);
   }

   public void setWritePolicy(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{"None", "WriteThrough", "WriteBehind"};
      var1 = weblogic.descriptor.beangen.LegalChecks.checkInEnum("WritePolicy", var1, var2);
      String var3 = this._WritePolicy;
      this._WritePolicy = var1;
      this._postSet(8, var3, var1);
   }

   public String getWorkManager() {
      if (!this._isSet(9)) {
         try {
            return ((CacheMBean)this.getParent()).getWorkManager();
         } catch (NullPointerException var2) {
         }
      }

      return this._WorkManager;
   }

   public boolean isWorkManagerSet() {
      return this._isSet(9);
   }

   public void setWorkManager(String var1) {
      var1 = var1 == null ? null : var1.trim();
      weblogic.descriptor.beangen.LegalChecks.checkNonNull("WorkManager", var1);
      String var2 = this._WorkManager;
      this._WorkManager = var1;
      this._postSet(9, var2, var1);
   }

   public int getBufferMaxSize() {
      return this._BufferMaxSize;
   }

   public boolean isBufferMaxSizeSet() {
      return this._isSet(10);
   }

   public void setBufferMaxSize(int var1) {
      weblogic.descriptor.beangen.LegalChecks.checkMin("BufferMaxSize", var1, 1);
      int var2 = this._BufferMaxSize;
      this._BufferMaxSize = var1;
      this._postSet(10, var2, var1);
   }

   public long getBufferWriteTimeout() {
      return this._BufferWriteTimeout;
   }

   public boolean isBufferWriteTimeoutSet() {
      return this._isSet(11);
   }

   public void setBufferWriteTimeout(long var1) {
      weblogic.descriptor.beangen.LegalChecks.checkMin("BufferWriteTimeout", var1, 1L);
      long var3 = this._BufferWriteTimeout;
      this._BufferWriteTimeout = var1;
      this._postSet(11, var3, var1);
   }

   public int getBufferWriteAttempts() {
      return this._BufferWriteAttempts;
   }

   public boolean isBufferWriteAttemptsSet() {
      return this._isSet(12);
   }

   public void setBufferWriteAttempts(int var1) {
      weblogic.descriptor.beangen.LegalChecks.checkMin("BufferWriteAttempts", var1, 1);
      int var2 = this._BufferWriteAttempts;
      this._BufferWriteAttempts = var1;
      this._postSet(12, var2, var1);
   }

   public int getStoreBatchSize() {
      return this._StoreBatchSize;
   }

   public boolean isStoreBatchSizeSet() {
      return this._isSet(13);
   }

   public void setStoreBatchSize(int var1) {
      weblogic.descriptor.beangen.LegalChecks.checkMin("StoreBatchSize", var1, 1);
      int var2 = this._StoreBatchSize;
      this._StoreBatchSize = var1;
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
         var1 = 10;
      }

      try {
         switch (var1) {
            case 10:
               this._BufferMaxSize = 100;
               if (var2) {
                  break;
               }
            case 12:
               this._BufferWriteAttempts = 1;
               if (var2) {
                  break;
               }
            case 11:
               this._BufferWriteTimeout = 100L;
               if (var2) {
                  break;
               }
            case 7:
               this._CustomStore = null;
               if (var2) {
                  break;
               }
            case 13:
               this._StoreBatchSize = 1;
               if (var2) {
                  break;
               }
            case 9:
               this._WorkManager = null;
               if (var2) {
                  break;
               }
            case 8:
               this._WritePolicy = null;
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
      return "CacheStore";
   }

   public void putValue(String var1, Object var2) {
      int var5;
      if (var1.equals("BufferMaxSize")) {
         var5 = this._BufferMaxSize;
         this._BufferMaxSize = (Integer)var2;
         this._postSet(10, var5, this._BufferMaxSize);
      } else if (var1.equals("BufferWriteAttempts")) {
         var5 = this._BufferWriteAttempts;
         this._BufferWriteAttempts = (Integer)var2;
         this._postSet(12, var5, this._BufferWriteAttempts);
      } else if (var1.equals("BufferWriteTimeout")) {
         long var6 = this._BufferWriteTimeout;
         this._BufferWriteTimeout = (Long)var2;
         this._postSet(11, var6, this._BufferWriteTimeout);
      } else {
         String var3;
         if (var1.equals("CustomStore")) {
            var3 = this._CustomStore;
            this._CustomStore = (String)var2;
            this._postSet(7, var3, this._CustomStore);
         } else if (var1.equals("StoreBatchSize")) {
            var5 = this._StoreBatchSize;
            this._StoreBatchSize = (Integer)var2;
            this._postSet(13, var5, this._StoreBatchSize);
         } else if (var1.equals("WorkManager")) {
            var3 = this._WorkManager;
            this._WorkManager = (String)var2;
            this._postSet(9, var3, this._WorkManager);
         } else if (var1.equals("WritePolicy")) {
            var3 = this._WritePolicy;
            this._WritePolicy = (String)var2;
            this._postSet(8, var3, this._WritePolicy);
         } else {
            super.putValue(var1, var2);
         }
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("BufferMaxSize")) {
         return new Integer(this._BufferMaxSize);
      } else if (var1.equals("BufferWriteAttempts")) {
         return new Integer(this._BufferWriteAttempts);
      } else if (var1.equals("BufferWriteTimeout")) {
         return new Long(this._BufferWriteTimeout);
      } else if (var1.equals("CustomStore")) {
         return this._CustomStore;
      } else if (var1.equals("StoreBatchSize")) {
         return new Integer(this._StoreBatchSize);
      } else if (var1.equals("WorkManager")) {
         return this._WorkManager;
      } else {
         return var1.equals("WritePolicy") ? this._WritePolicy : super.getValue(var1);
      }
   }

   public static class SchemaHelper2 extends ConfigurationMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 12:
               if (var1.equals("custom-store")) {
                  return 7;
               }

               if (var1.equals("work-manager")) {
                  return 9;
               }

               if (var1.equals("write-policy")) {
                  return 8;
               }
            case 13:
            case 14:
            case 17:
            case 18:
            case 19:
            default:
               break;
            case 15:
               if (var1.equals("buffer-max-size")) {
                  return 10;
               }
               break;
            case 16:
               if (var1.equals("store-batch-size")) {
                  return 13;
               }
               break;
            case 20:
               if (var1.equals("buffer-write-timeout")) {
                  return 11;
               }
               break;
            case 21:
               if (var1.equals("buffer-write-attempts")) {
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
               return "custom-store";
            case 8:
               return "write-policy";
            case 9:
               return "work-manager";
            case 10:
               return "buffer-max-size";
            case 11:
               return "buffer-write-timeout";
            case 12:
               return "buffer-write-attempts";
            case 13:
               return "store-batch-size";
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
      private CacheStoreMBeanImpl bean;

      protected Helper(CacheStoreMBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 7:
               return "CustomStore";
            case 8:
               return "WritePolicy";
            case 9:
               return "WorkManager";
            case 10:
               return "BufferMaxSize";
            case 11:
               return "BufferWriteTimeout";
            case 12:
               return "BufferWriteAttempts";
            case 13:
               return "StoreBatchSize";
            default:
               return super.getPropertyName(var1);
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("BufferMaxSize")) {
            return 10;
         } else if (var1.equals("BufferWriteAttempts")) {
            return 12;
         } else if (var1.equals("BufferWriteTimeout")) {
            return 11;
         } else if (var1.equals("CustomStore")) {
            return 7;
         } else if (var1.equals("StoreBatchSize")) {
            return 13;
         } else if (var1.equals("WorkManager")) {
            return 9;
         } else {
            return var1.equals("WritePolicy") ? 8 : super.getPropertyIndex(var1);
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
            if (this.bean.isBufferMaxSizeSet()) {
               var2.append("BufferMaxSize");
               var2.append(String.valueOf(this.bean.getBufferMaxSize()));
            }

            if (this.bean.isBufferWriteAttemptsSet()) {
               var2.append("BufferWriteAttempts");
               var2.append(String.valueOf(this.bean.getBufferWriteAttempts()));
            }

            if (this.bean.isBufferWriteTimeoutSet()) {
               var2.append("BufferWriteTimeout");
               var2.append(String.valueOf(this.bean.getBufferWriteTimeout()));
            }

            if (this.bean.isCustomStoreSet()) {
               var2.append("CustomStore");
               var2.append(String.valueOf(this.bean.getCustomStore()));
            }

            if (this.bean.isStoreBatchSizeSet()) {
               var2.append("StoreBatchSize");
               var2.append(String.valueOf(this.bean.getStoreBatchSize()));
            }

            if (this.bean.isWorkManagerSet()) {
               var2.append("WorkManager");
               var2.append(String.valueOf(this.bean.getWorkManager()));
            }

            if (this.bean.isWritePolicySet()) {
               var2.append("WritePolicy");
               var2.append(String.valueOf(this.bean.getWritePolicy()));
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
            CacheStoreMBeanImpl var2 = (CacheStoreMBeanImpl)var1;
            this.computeDiff("BufferMaxSize", this.bean.getBufferMaxSize(), var2.getBufferMaxSize(), false);
            this.computeDiff("BufferWriteAttempts", this.bean.getBufferWriteAttempts(), var2.getBufferWriteAttempts(), true);
            this.computeDiff("BufferWriteTimeout", this.bean.getBufferWriteTimeout(), var2.getBufferWriteTimeout(), true);
            this.computeDiff("CustomStore", this.bean.getCustomStore(), var2.getCustomStore(), true);
            this.computeDiff("StoreBatchSize", this.bean.getStoreBatchSize(), var2.getStoreBatchSize(), true);
            this.computeDiff("WorkManager", this.bean.getWorkManager(), var2.getWorkManager(), true);
            this.computeDiff("WritePolicy", this.bean.getWritePolicy(), var2.getWritePolicy(), true);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            CacheStoreMBeanImpl var3 = (CacheStoreMBeanImpl)var1.getSourceBean();
            CacheStoreMBeanImpl var4 = (CacheStoreMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("BufferMaxSize")) {
                  var3.setBufferMaxSize(var4.getBufferMaxSize());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 10);
               } else if (var5.equals("BufferWriteAttempts")) {
                  var3.setBufferWriteAttempts(var4.getBufferWriteAttempts());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 12);
               } else if (var5.equals("BufferWriteTimeout")) {
                  var3.setBufferWriteTimeout(var4.getBufferWriteTimeout());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 11);
               } else if (var5.equals("CustomStore")) {
                  var3.setCustomStore(var4.getCustomStore());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 7);
               } else if (var5.equals("StoreBatchSize")) {
                  var3.setStoreBatchSize(var4.getStoreBatchSize());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 13);
               } else if (var5.equals("WorkManager")) {
                  var3.setWorkManager(var4.getWorkManager());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 9);
               } else if (var5.equals("WritePolicy")) {
                  var3.setWritePolicy(var4.getWritePolicy());
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
            CacheStoreMBeanImpl var5 = (CacheStoreMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("BufferMaxSize")) && this.bean.isBufferMaxSizeSet()) {
               var5.setBufferMaxSize(this.bean.getBufferMaxSize());
            }

            if ((var3 == null || !var3.contains("BufferWriteAttempts")) && this.bean.isBufferWriteAttemptsSet()) {
               var5.setBufferWriteAttempts(this.bean.getBufferWriteAttempts());
            }

            if ((var3 == null || !var3.contains("BufferWriteTimeout")) && this.bean.isBufferWriteTimeoutSet()) {
               var5.setBufferWriteTimeout(this.bean.getBufferWriteTimeout());
            }

            if ((var3 == null || !var3.contains("CustomStore")) && this.bean.isCustomStoreSet()) {
               var5.setCustomStore(this.bean.getCustomStore());
            }

            if ((var3 == null || !var3.contains("StoreBatchSize")) && this.bean.isStoreBatchSizeSet()) {
               var5.setStoreBatchSize(this.bean.getStoreBatchSize());
            }

            if ((var3 == null || !var3.contains("WorkManager")) && this.bean.isWorkManagerSet()) {
               var5.setWorkManager(this.bean.getWorkManager());
            }

            if ((var3 == null || !var3.contains("WritePolicy")) && this.bean.isWritePolicySet()) {
               var5.setWritePolicy(this.bean.getWritePolicy());
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
