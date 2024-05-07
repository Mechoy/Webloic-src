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

public class CacheAsyncListenersMBeanImpl extends ConfigurationMBeanImpl implements CacheAsyncListenersMBean, Serializable {
   private boolean _Enabled;
   private String _WorkManager;
   private static SchemaHelper2 _schemaHelper;

   public CacheAsyncListenersMBeanImpl() {
      this._initializeProperty(-1);
   }

   public CacheAsyncListenersMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);
      this._initializeProperty(-1);
   }

   public boolean getEnabled() {
      return this._Enabled;
   }

   public boolean isEnabledSet() {
      return this._isSet(7);
   }

   public void setEnabled(boolean var1) {
      boolean var2 = this._Enabled;
      this._Enabled = var1;
      this._postSet(7, var2, var1);
   }

   public String getWorkManager() {
      if (!this._isSet(8)) {
         try {
            return ((CacheMBean)this.getParent()).getWorkManager();
         } catch (NullPointerException var2) {
         }
      }

      return this._WorkManager;
   }

   public boolean isWorkManagerSet() {
      return this._isSet(8);
   }

   public void setWorkManager(String var1) {
      var1 = var1 == null ? null : var1.trim();
      weblogic.descriptor.beangen.LegalChecks.checkNonNull("WorkManager", var1);
      String var2 = this._WorkManager;
      this._WorkManager = var1;
      this._postSet(8, var2, var1);
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
         var1 = 7;
      }

      try {
         switch (var1) {
            case 7:
               this._Enabled = false;
               if (var2) {
                  break;
               }
            case 8:
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
      return "CacheAsyncListeners";
   }

   public void putValue(String var1, Object var2) {
      if (var1.equals("Enabled")) {
         boolean var4 = this._Enabled;
         this._Enabled = (Boolean)var2;
         this._postSet(7, var4, this._Enabled);
      } else if (var1.equals("WorkManager")) {
         String var3 = this._WorkManager;
         this._WorkManager = (String)var2;
         this._postSet(8, var3, this._WorkManager);
      } else {
         super.putValue(var1, var2);
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("Enabled")) {
         return new Boolean(this._Enabled);
      } else {
         return var1.equals("WorkManager") ? this._WorkManager : super.getValue(var1);
      }
   }

   public static class SchemaHelper2 extends ConfigurationMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 7:
               if (var1.equals("enabled")) {
                  return 7;
               }
               break;
            case 12:
               if (var1.equals("work-manager")) {
                  return 8;
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
               return "enabled";
            case 8:
               return "work-manager";
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
      private CacheAsyncListenersMBeanImpl bean;

      protected Helper(CacheAsyncListenersMBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 7:
               return "Enabled";
            case 8:
               return "WorkManager";
            default:
               return super.getPropertyName(var1);
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("Enabled")) {
            return 7;
         } else {
            return var1.equals("WorkManager") ? 8 : super.getPropertyIndex(var1);
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
            if (this.bean.isEnabledSet()) {
               var2.append("Enabled");
               var2.append(String.valueOf(this.bean.getEnabled()));
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
            CacheAsyncListenersMBeanImpl var2 = (CacheAsyncListenersMBeanImpl)var1;
            this.computeDiff("Enabled", this.bean.getEnabled(), var2.getEnabled(), true);
            this.computeDiff("WorkManager", this.bean.getWorkManager(), var2.getWorkManager(), true);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            CacheAsyncListenersMBeanImpl var3 = (CacheAsyncListenersMBeanImpl)var1.getSourceBean();
            CacheAsyncListenersMBeanImpl var4 = (CacheAsyncListenersMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("Enabled")) {
                  var3.setEnabled(var4.getEnabled());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 7);
               } else if (var5.equals("WorkManager")) {
                  var3.setWorkManager(var4.getWorkManager());
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
            CacheAsyncListenersMBeanImpl var5 = (CacheAsyncListenersMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("Enabled")) && this.bean.isEnabledSet()) {
               var5.setEnabled(this.bean.getEnabled());
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
      }
   }
}
