package weblogic.diagnostics.descriptor;

import java.io.Serializable;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.zip.CRC32;
import javax.management.InvalidAttributeValueException;
import weblogic.descriptor.BeanRemoveRejectedException;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBeanHelper;
import weblogic.descriptor.internal.Munger;
import weblogic.descriptor.internal.SchemaHelper;
import weblogic.utils.ArrayUtils;
import weblogic.utils.collections.ArrayIterator;
import weblogic.utils.collections.CombinedIterator;

public class WLDFInstrumentationBeanImpl extends WLDFBeanImpl implements WLDFInstrumentationBean, Serializable {
   private boolean _Enabled;
   private String[] _Excludes;
   private String[] _Includes;
   private WLDFInstrumentationMonitorBean[] _WLDFInstrumentationMonitors;
   private static SchemaHelper2 _schemaHelper;

   public WLDFInstrumentationBeanImpl() {
      this._initializeProperty(-1);
   }

   public WLDFInstrumentationBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);
      this._initializeProperty(-1);
   }

   public boolean isEnabled() {
      return this._Enabled;
   }

   public boolean isEnabledSet() {
      return this._isSet(1);
   }

   public void setEnabled(boolean var1) {
      boolean var2 = this._Enabled;
      this._Enabled = var1;
      this._postSet(1, var2, var1);
   }

   public String[] getIncludes() {
      return this._Includes;
   }

   public boolean isIncludesSet() {
      return this._isSet(2);
   }

   public void setIncludes(String[] var1) {
      var1 = var1 == null ? new String[0] : var1;
      var1 = this._getHelper()._trimElements(var1);
      String[] var2 = this._Includes;
      this._Includes = var1;
      this._postSet(2, var2, var1);
   }

   public String[] getExcludes() {
      return this._Excludes;
   }

   public boolean isExcludesSet() {
      return this._isSet(3);
   }

   public void setExcludes(String[] var1) {
      var1 = var1 == null ? new String[0] : var1;
      var1 = this._getHelper()._trimElements(var1);
      String[] var2 = this._Excludes;
      this._Excludes = var1;
      this._postSet(3, var2, var1);
   }

   public void addWLDFInstrumentationMonitor(WLDFInstrumentationMonitorBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 4)) {
         WLDFInstrumentationMonitorBean[] var2;
         if (this._isSet(4)) {
            var2 = (WLDFInstrumentationMonitorBean[])((WLDFInstrumentationMonitorBean[])this._getHelper()._extendArray(this.getWLDFInstrumentationMonitors(), WLDFInstrumentationMonitorBean.class, var1));
         } else {
            var2 = new WLDFInstrumentationMonitorBean[]{var1};
         }

         try {
            this.setWLDFInstrumentationMonitors(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public WLDFInstrumentationMonitorBean[] getWLDFInstrumentationMonitors() {
      return this._WLDFInstrumentationMonitors;
   }

   public boolean isWLDFInstrumentationMonitorsSet() {
      return this._isSet(4);
   }

   public void removeWLDFInstrumentationMonitor(WLDFInstrumentationMonitorBean var1) {
      this.destroyWLDFInstrumentationMonitor(var1);
   }

   public void setWLDFInstrumentationMonitors(WLDFInstrumentationMonitorBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new WLDFInstrumentationMonitorBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 4)) {
            this._getReferenceManager().registerBean(var3, false);
            this._postCreate(var3);
         }
      }

      WLDFInstrumentationMonitorBean[] var5 = this._WLDFInstrumentationMonitors;
      this._WLDFInstrumentationMonitors = (WLDFInstrumentationMonitorBean[])var4;
      this._postSet(4, var5, var4);
   }

   public WLDFInstrumentationMonitorBean createWLDFInstrumentationMonitor(String var1) {
      WLDFInstrumentationMonitorBeanImpl var2 = new WLDFInstrumentationMonitorBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addWLDFInstrumentationMonitor(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public void destroyWLDFInstrumentationMonitor(WLDFInstrumentationMonitorBean var1) {
      try {
         this._checkIsPotentialChild(var1, 4);
         WLDFInstrumentationMonitorBean[] var2 = this.getWLDFInstrumentationMonitors();
         WLDFInstrumentationMonitorBean[] var3 = (WLDFInstrumentationMonitorBean[])((WLDFInstrumentationMonitorBean[])this._getHelper()._removeElement(var2, WLDFInstrumentationMonitorBean.class, var1));
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
               this.setWLDFInstrumentationMonitors(var3);
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
         var1 = 3;
      }

      try {
         switch (var1) {
            case 3:
               this._Excludes = new String[0];
               if (var2) {
                  break;
               }
            case 2:
               this._Includes = new String[0];
               if (var2) {
                  break;
               }
            case 4:
               this._WLDFInstrumentationMonitors = new WLDFInstrumentationMonitorBean[0];
               if (var2) {
                  break;
               }
            case 1:
               this._Enabled = false;
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
      return "http://xmlns.oracle.com/weblogic/weblogic-diagnostics/1.0/weblogic-diagnostics.xsd";
   }

   protected String getTargetNamespace() {
      return "http://xmlns.oracle.com/weblogic/weblogic-diagnostics";
   }

   public SchemaHelper _getSchemaHelper2() {
      if (_schemaHelper == null) {
         _schemaHelper = new SchemaHelper2();
      }

      return _schemaHelper;
   }

   public static class SchemaHelper2 extends WLDFBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 7:
               if (var1.equals("exclude")) {
                  return 3;
               }

               if (var1.equals("include")) {
                  return 2;
               }

               if (var1.equals("enabled")) {
                  return 1;
               }
               break;
            case 28:
               if (var1.equals("wldf-instrumentation-monitor")) {
                  return 4;
               }
         }

         return super.getPropertyIndex(var1);
      }

      public SchemaHelper getSchemaHelper(int var1) {
         switch (var1) {
            case 4:
               return new WLDFInstrumentationMonitorBeanImpl.SchemaHelper2();
            default:
               return super.getSchemaHelper(var1);
         }
      }

      public String getElementName(int var1) {
         switch (var1) {
            case 1:
               return "enabled";
            case 2:
               return "include";
            case 3:
               return "exclude";
            case 4:
               return "wldf-instrumentation-monitor";
            default:
               return super.getElementName(var1);
         }
      }

      public boolean isArray(int var1) {
         switch (var1) {
            case 2:
               return true;
            case 3:
               return true;
            case 4:
               return true;
            default:
               return super.isArray(var1);
         }
      }

      public boolean isBean(int var1) {
         switch (var1) {
            case 4:
               return true;
            default:
               return super.isBean(var1);
         }
      }

      public boolean isConfigurable(int var1) {
         switch (var1) {
            case 1:
               return true;
            case 2:
               return true;
            case 3:
               return true;
            default:
               return super.isConfigurable(var1);
         }
      }

      public boolean isKey(int var1) {
         switch (var1) {
            case 0:
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

   protected static class Helper extends WLDFBeanImpl.Helper {
      private WLDFInstrumentationBeanImpl bean;

      protected Helper(WLDFInstrumentationBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 1:
               return "Enabled";
            case 2:
               return "Includes";
            case 3:
               return "Excludes";
            case 4:
               return "WLDFInstrumentationMonitors";
            default:
               return super.getPropertyName(var1);
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("Excludes")) {
            return 3;
         } else if (var1.equals("Includes")) {
            return 2;
         } else if (var1.equals("WLDFInstrumentationMonitors")) {
            return 4;
         } else {
            return var1.equals("Enabled") ? 1 : super.getPropertyIndex(var1);
         }
      }

      public Iterator getChildren() {
         ArrayList var1 = new ArrayList();
         var1.add(new ArrayIterator(this.bean.getWLDFInstrumentationMonitors()));
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
            if (this.bean.isExcludesSet()) {
               var2.append("Excludes");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getExcludes())));
            }

            if (this.bean.isIncludesSet()) {
               var2.append("Includes");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getIncludes())));
            }

            var5 = 0L;

            for(int var7 = 0; var7 < this.bean.getWLDFInstrumentationMonitors().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getWLDFInstrumentationMonitors()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            if (this.bean.isEnabledSet()) {
               var2.append("Enabled");
               var2.append(String.valueOf(this.bean.isEnabled()));
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
            WLDFInstrumentationBeanImpl var2 = (WLDFInstrumentationBeanImpl)var1;
            this.computeDiff("Excludes", this.bean.getExcludes(), var2.getExcludes(), true);
            this.computeDiff("Includes", this.bean.getIncludes(), var2.getIncludes(), true);
            this.computeChildDiff("WLDFInstrumentationMonitors", this.bean.getWLDFInstrumentationMonitors(), var2.getWLDFInstrumentationMonitors(), true);
            this.computeDiff("Enabled", this.bean.isEnabled(), var2.isEnabled(), true);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            WLDFInstrumentationBeanImpl var3 = (WLDFInstrumentationBeanImpl)var1.getSourceBean();
            WLDFInstrumentationBeanImpl var4 = (WLDFInstrumentationBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("Excludes")) {
                  var3.setExcludes(var4.getExcludes());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 3);
               } else if (var5.equals("Includes")) {
                  var3.setIncludes(var4.getIncludes());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 2);
               } else if (var5.equals("WLDFInstrumentationMonitors")) {
                  if (var6 == 2) {
                     var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                     var3.addWLDFInstrumentationMonitor((WLDFInstrumentationMonitorBean)var2.getAddedObject());
                  } else {
                     if (var6 != 3) {
                        throw new AssertionError("Invalid type: " + var6);
                     }

                     var3.removeWLDFInstrumentationMonitor((WLDFInstrumentationMonitorBean)var2.getRemovedObject());
                  }

                  if (var3.getWLDFInstrumentationMonitors() == null || var3.getWLDFInstrumentationMonitors().length == 0) {
                     var3._conditionalUnset(var2.isUnsetUpdate(), 4);
                  }
               } else if (var5.equals("Enabled")) {
                  var3.setEnabled(var4.isEnabled());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 1);
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
            WLDFInstrumentationBeanImpl var5 = (WLDFInstrumentationBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            String[] var4;
            if ((var3 == null || !var3.contains("Excludes")) && this.bean.isExcludesSet()) {
               var4 = this.bean.getExcludes();
               var5.setExcludes(var4 == null ? null : (String[])((String[])((String[])((String[])var4)).clone()));
            }

            if ((var3 == null || !var3.contains("Includes")) && this.bean.isIncludesSet()) {
               var4 = this.bean.getIncludes();
               var5.setIncludes(var4 == null ? null : (String[])((String[])((String[])((String[])var4)).clone()));
            }

            if ((var3 == null || !var3.contains("WLDFInstrumentationMonitors")) && this.bean.isWLDFInstrumentationMonitorsSet() && !var5._isSet(4)) {
               WLDFInstrumentationMonitorBean[] var6 = this.bean.getWLDFInstrumentationMonitors();
               WLDFInstrumentationMonitorBean[] var7 = new WLDFInstrumentationMonitorBean[var6.length];

               for(int var8 = 0; var8 < var7.length; ++var8) {
                  var7[var8] = (WLDFInstrumentationMonitorBean)((WLDFInstrumentationMonitorBean)this.createCopy((AbstractDescriptorBean)var6[var8], var2));
               }

               var5.setWLDFInstrumentationMonitors(var7);
            }

            if ((var3 == null || !var3.contains("Enabled")) && this.bean.isEnabledSet()) {
               var5.setEnabled(this.bean.isEnabled());
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
         this.inferSubTree(this.bean.getWLDFInstrumentationMonitors(), var1, var2);
      }
   }
}
