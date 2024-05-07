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

public class COMMBeanImpl extends ConfigurationMBeanImpl implements COMMBean, Serializable {
   private boolean _ApartmentThreaded;
   private boolean _MemoryLoggingEnabled;
   private String _NTAuthHost;
   private boolean _NativeModeEnabled;
   private boolean _PrefetchEnums;
   private boolean _VerboseLoggingEnabled;
   private static SchemaHelper2 _schemaHelper;

   public COMMBeanImpl() {
      this._initializeProperty(-1);
   }

   public COMMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);
      this._initializeProperty(-1);
   }

   public String getNTAuthHost() {
      return this._NTAuthHost;
   }

   public boolean isNTAuthHostSet() {
      return this._isSet(7);
   }

   public void setNTAuthHost(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._NTAuthHost;
      this._NTAuthHost = var1;
      this._postSet(7, var2, var1);
   }

   public boolean isNativeModeEnabled() {
      return this._NativeModeEnabled;
   }

   public boolean isNativeModeEnabledSet() {
      return this._isSet(8);
   }

   public void setNativeModeEnabled(boolean var1) {
      boolean var2 = this._NativeModeEnabled;
      this._NativeModeEnabled = var1;
      this._postSet(8, var2, var1);
   }

   public boolean isVerboseLoggingEnabled() {
      return this._VerboseLoggingEnabled;
   }

   public boolean isVerboseLoggingEnabledSet() {
      return this._isSet(9);
   }

   public void setVerboseLoggingEnabled(boolean var1) {
      boolean var2 = this._VerboseLoggingEnabled;
      this._VerboseLoggingEnabled = var1;
      this._postSet(9, var2, var1);
   }

   public boolean isMemoryLoggingEnabled() {
      return this._MemoryLoggingEnabled;
   }

   public boolean isMemoryLoggingEnabledSet() {
      return this._isSet(10);
   }

   public void setMemoryLoggingEnabled(boolean var1) {
      boolean var2 = this._MemoryLoggingEnabled;
      this._MemoryLoggingEnabled = var1;
      this._postSet(10, var2, var1);
   }

   public boolean isPrefetchEnums() {
      return this._PrefetchEnums;
   }

   public boolean isPrefetchEnumsSet() {
      return this._isSet(11);
   }

   public void setPrefetchEnums(boolean var1) {
      boolean var2 = this._PrefetchEnums;
      this._PrefetchEnums = var1;
      this._postSet(11, var2, var1);
   }

   public boolean isApartmentThreaded() {
      return this._ApartmentThreaded;
   }

   public boolean isApartmentThreadedSet() {
      return this._isSet(12);
   }

   public void setApartmentThreaded(boolean var1) {
      boolean var2 = this._ApartmentThreaded;
      this._ApartmentThreaded = var1;
      this._postSet(12, var2, var1);
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
               this._NTAuthHost = null;
               if (var2) {
                  break;
               }
            case 12:
               this._ApartmentThreaded = false;
               if (var2) {
                  break;
               }
            case 10:
               this._MemoryLoggingEnabled = false;
               if (var2) {
                  break;
               }
            case 8:
               this._NativeModeEnabled = false;
               if (var2) {
                  break;
               }
            case 11:
               this._PrefetchEnums = false;
               if (var2) {
                  break;
               }
            case 9:
               this._VerboseLoggingEnabled = false;
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
      return "COM";
   }

   public void putValue(String var1, Object var2) {
      boolean var3;
      if (var1.equals("ApartmentThreaded")) {
         var3 = this._ApartmentThreaded;
         this._ApartmentThreaded = (Boolean)var2;
         this._postSet(12, var3, this._ApartmentThreaded);
      } else if (var1.equals("MemoryLoggingEnabled")) {
         var3 = this._MemoryLoggingEnabled;
         this._MemoryLoggingEnabled = (Boolean)var2;
         this._postSet(10, var3, this._MemoryLoggingEnabled);
      } else if (var1.equals("NTAuthHost")) {
         String var4 = this._NTAuthHost;
         this._NTAuthHost = (String)var2;
         this._postSet(7, var4, this._NTAuthHost);
      } else if (var1.equals("NativeModeEnabled")) {
         var3 = this._NativeModeEnabled;
         this._NativeModeEnabled = (Boolean)var2;
         this._postSet(8, var3, this._NativeModeEnabled);
      } else if (var1.equals("PrefetchEnums")) {
         var3 = this._PrefetchEnums;
         this._PrefetchEnums = (Boolean)var2;
         this._postSet(11, var3, this._PrefetchEnums);
      } else if (var1.equals("VerboseLoggingEnabled")) {
         var3 = this._VerboseLoggingEnabled;
         this._VerboseLoggingEnabled = (Boolean)var2;
         this._postSet(9, var3, this._VerboseLoggingEnabled);
      } else {
         super.putValue(var1, var2);
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("ApartmentThreaded")) {
         return new Boolean(this._ApartmentThreaded);
      } else if (var1.equals("MemoryLoggingEnabled")) {
         return new Boolean(this._MemoryLoggingEnabled);
      } else if (var1.equals("NTAuthHost")) {
         return this._NTAuthHost;
      } else if (var1.equals("NativeModeEnabled")) {
         return new Boolean(this._NativeModeEnabled);
      } else if (var1.equals("PrefetchEnums")) {
         return new Boolean(this._PrefetchEnums);
      } else {
         return var1.equals("VerboseLoggingEnabled") ? new Boolean(this._VerboseLoggingEnabled) : super.getValue(var1);
      }
   }

   public static class SchemaHelper2 extends ConfigurationMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 12:
               if (var1.equals("nt-auth-host")) {
                  return 7;
               }
            case 13:
            case 15:
            case 16:
            case 17:
            case 20:
            case 21:
            default:
               break;
            case 14:
               if (var1.equals("prefetch-enums")) {
                  return 11;
               }
               break;
            case 18:
               if (var1.equals("apartment-threaded")) {
                  return 12;
               }
               break;
            case 19:
               if (var1.equals("native-mode-enabled")) {
                  return 8;
               }
               break;
            case 22:
               if (var1.equals("memory-logging-enabled")) {
                  return 10;
               }
               break;
            case 23:
               if (var1.equals("verbose-logging-enabled")) {
                  return 9;
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
               return "nt-auth-host";
            case 8:
               return "native-mode-enabled";
            case 9:
               return "verbose-logging-enabled";
            case 10:
               return "memory-logging-enabled";
            case 11:
               return "prefetch-enums";
            case 12:
               return "apartment-threaded";
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
      private COMMBeanImpl bean;

      protected Helper(COMMBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 7:
               return "NTAuthHost";
            case 8:
               return "NativeModeEnabled";
            case 9:
               return "VerboseLoggingEnabled";
            case 10:
               return "MemoryLoggingEnabled";
            case 11:
               return "PrefetchEnums";
            case 12:
               return "ApartmentThreaded";
            default:
               return super.getPropertyName(var1);
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("NTAuthHost")) {
            return 7;
         } else if (var1.equals("ApartmentThreaded")) {
            return 12;
         } else if (var1.equals("MemoryLoggingEnabled")) {
            return 10;
         } else if (var1.equals("NativeModeEnabled")) {
            return 8;
         } else if (var1.equals("PrefetchEnums")) {
            return 11;
         } else {
            return var1.equals("VerboseLoggingEnabled") ? 9 : super.getPropertyIndex(var1);
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
            if (this.bean.isNTAuthHostSet()) {
               var2.append("NTAuthHost");
               var2.append(String.valueOf(this.bean.getNTAuthHost()));
            }

            if (this.bean.isApartmentThreadedSet()) {
               var2.append("ApartmentThreaded");
               var2.append(String.valueOf(this.bean.isApartmentThreaded()));
            }

            if (this.bean.isMemoryLoggingEnabledSet()) {
               var2.append("MemoryLoggingEnabled");
               var2.append(String.valueOf(this.bean.isMemoryLoggingEnabled()));
            }

            if (this.bean.isNativeModeEnabledSet()) {
               var2.append("NativeModeEnabled");
               var2.append(String.valueOf(this.bean.isNativeModeEnabled()));
            }

            if (this.bean.isPrefetchEnumsSet()) {
               var2.append("PrefetchEnums");
               var2.append(String.valueOf(this.bean.isPrefetchEnums()));
            }

            if (this.bean.isVerboseLoggingEnabledSet()) {
               var2.append("VerboseLoggingEnabled");
               var2.append(String.valueOf(this.bean.isVerboseLoggingEnabled()));
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
            COMMBeanImpl var2 = (COMMBeanImpl)var1;
            this.computeDiff("NTAuthHost", this.bean.getNTAuthHost(), var2.getNTAuthHost(), false);
            this.computeDiff("ApartmentThreaded", this.bean.isApartmentThreaded(), var2.isApartmentThreaded(), false);
            this.computeDiff("MemoryLoggingEnabled", this.bean.isMemoryLoggingEnabled(), var2.isMemoryLoggingEnabled(), false);
            this.computeDiff("NativeModeEnabled", this.bean.isNativeModeEnabled(), var2.isNativeModeEnabled(), false);
            this.computeDiff("PrefetchEnums", this.bean.isPrefetchEnums(), var2.isPrefetchEnums(), false);
            this.computeDiff("VerboseLoggingEnabled", this.bean.isVerboseLoggingEnabled(), var2.isVerboseLoggingEnabled(), false);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            COMMBeanImpl var3 = (COMMBeanImpl)var1.getSourceBean();
            COMMBeanImpl var4 = (COMMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("NTAuthHost")) {
                  var3.setNTAuthHost(var4.getNTAuthHost());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 7);
               } else if (var5.equals("ApartmentThreaded")) {
                  var3.setApartmentThreaded(var4.isApartmentThreaded());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 12);
               } else if (var5.equals("MemoryLoggingEnabled")) {
                  var3.setMemoryLoggingEnabled(var4.isMemoryLoggingEnabled());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 10);
               } else if (var5.equals("NativeModeEnabled")) {
                  var3.setNativeModeEnabled(var4.isNativeModeEnabled());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 8);
               } else if (var5.equals("PrefetchEnums")) {
                  var3.setPrefetchEnums(var4.isPrefetchEnums());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 11);
               } else if (var5.equals("VerboseLoggingEnabled")) {
                  var3.setVerboseLoggingEnabled(var4.isVerboseLoggingEnabled());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 9);
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
            COMMBeanImpl var5 = (COMMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("NTAuthHost")) && this.bean.isNTAuthHostSet()) {
               var5.setNTAuthHost(this.bean.getNTAuthHost());
            }

            if ((var3 == null || !var3.contains("ApartmentThreaded")) && this.bean.isApartmentThreadedSet()) {
               var5.setApartmentThreaded(this.bean.isApartmentThreaded());
            }

            if ((var3 == null || !var3.contains("MemoryLoggingEnabled")) && this.bean.isMemoryLoggingEnabledSet()) {
               var5.setMemoryLoggingEnabled(this.bean.isMemoryLoggingEnabled());
            }

            if ((var3 == null || !var3.contains("NativeModeEnabled")) && this.bean.isNativeModeEnabledSet()) {
               var5.setNativeModeEnabled(this.bean.isNativeModeEnabled());
            }

            if ((var3 == null || !var3.contains("PrefetchEnums")) && this.bean.isPrefetchEnumsSet()) {
               var5.setPrefetchEnums(this.bean.isPrefetchEnums());
            }

            if ((var3 == null || !var3.contains("VerboseLoggingEnabled")) && this.bean.isVerboseLoggingEnabledSet()) {
               var5.setVerboseLoggingEnabled(this.bean.isVerboseLoggingEnabled());
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
