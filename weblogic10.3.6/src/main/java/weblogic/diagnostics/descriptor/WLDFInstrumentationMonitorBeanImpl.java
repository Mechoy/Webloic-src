package weblogic.diagnostics.descriptor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.zip.CRC32;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.beangen.LegalChecks;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBeanHelper;
import weblogic.descriptor.internal.Munger;
import weblogic.descriptor.internal.SchemaHelper;
import weblogic.utils.ArrayUtils;
import weblogic.utils.collections.CombinedIterator;

public class WLDFInstrumentationMonitorBeanImpl extends WLDFBeanImpl implements WLDFInstrumentationMonitorBean, Serializable {
   private String[] _Actions;
   private String _Description;
   private boolean _DyeFilteringEnabled;
   private String _DyeMask;
   private boolean _Enabled;
   private String[] _Excludes;
   private String[] _Includes;
   private String _LocationType;
   private String _Pointcut;
   private String _Properties;
   private static SchemaHelper2 _schemaHelper;

   public WLDFInstrumentationMonitorBeanImpl() {
      this._initializeProperty(-1);
   }

   public WLDFInstrumentationMonitorBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);
      this._initializeProperty(-1);
   }

   public String getDescription() {
      return this._Description;
   }

   public boolean isDescriptionSet() {
      return this._isSet(1);
   }

   public void setDescription(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._Description;
      this._Description = var1;
      this._postSet(1, var2, var1);
   }

   public boolean isEnabled() {
      return this._Enabled;
   }

   public boolean isEnabledSet() {
      return this._isSet(2);
   }

   public void setEnabled(boolean var1) {
      boolean var2 = this._Enabled;
      this._Enabled = var1;
      this._postSet(2, var2, var1);
   }

   public String getDyeMask() {
      return this._DyeMask;
   }

   public boolean isDyeMaskSet() {
      return this._isSet(3);
   }

   public void setDyeMask(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._DyeMask;
      this._DyeMask = var1;
      this._postSet(3, var2, var1);
   }

   public boolean isDyeFilteringEnabled() {
      return this._DyeFilteringEnabled;
   }

   public boolean isDyeFilteringEnabledSet() {
      return this._isSet(4);
   }

   public void setDyeFilteringEnabled(boolean var1) {
      boolean var2 = this._DyeFilteringEnabled;
      this._DyeFilteringEnabled = var1;
      this._postSet(4, var2, var1);
   }

   public String getProperties() {
      return this._Properties;
   }

   public boolean isPropertiesSet() {
      return this._isSet(5);
   }

   public void setProperties(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._Properties;
      this._Properties = var1;
      this._postSet(5, var2, var1);
   }

   public String[] getActions() {
      return this._Actions;
   }

   public boolean isActionsSet() {
      return this._isSet(6);
   }

   public void setActions(String[] var1) {
      var1 = var1 == null ? new String[0] : var1;
      var1 = this._getHelper()._trimElements(var1);
      String[] var2 = this._Actions;
      this._Actions = var1;
      this._postSet(6, var2, var1);
   }

   public String getLocationType() {
      return this._LocationType;
   }

   public boolean isLocationTypeSet() {
      return this._isSet(7);
   }

   public void setLocationType(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{"before", "after", "around"};
      var1 = LegalChecks.checkInEnum("LocationType", var1, var2);
      String var3 = this._LocationType;
      this._LocationType = var1;
      this._postSet(7, var3, var1);
   }

   public String getPointcut() {
      return this._Pointcut;
   }

   public boolean isPointcutSet() {
      return this._isSet(8);
   }

   public void setPointcut(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._Pointcut;
      this._Pointcut = var1;
      this._postSet(8, var2, var1);
   }

   public String[] getIncludes() {
      return this._Includes;
   }

   public boolean isIncludesSet() {
      return this._isSet(9);
   }

   public void setIncludes(String[] var1) {
      var1 = var1 == null ? new String[0] : var1;
      var1 = this._getHelper()._trimElements(var1);
      String[] var2 = this._Includes;
      this._Includes = var1;
      this._postSet(9, var2, var1);
   }

   public String[] getExcludes() {
      return this._Excludes;
   }

   public boolean isExcludesSet() {
      return this._isSet(10);
   }

   public void setExcludes(String[] var1) {
      var1 = var1 == null ? new String[0] : var1;
      var1 = this._getHelper()._trimElements(var1);
      String[] var2 = this._Excludes;
      this._Excludes = var1;
      this._postSet(10, var2, var1);
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
         var1 = 6;
      }

      try {
         switch (var1) {
            case 6:
               this._Actions = new String[0];
               if (var2) {
                  break;
               }
            case 1:
               this._Description = null;
               if (var2) {
                  break;
               }
            case 3:
               this._DyeMask = null;
               if (var2) {
                  break;
               }
            case 10:
               this._Excludes = new String[0];
               if (var2) {
                  break;
               }
            case 9:
               this._Includes = new String[0];
               if (var2) {
                  break;
               }
            case 7:
               this._LocationType = "before";
               if (var2) {
                  break;
               }
            case 8:
               this._Pointcut = null;
               if (var2) {
                  break;
               }
            case 5:
               this._Properties = null;
               if (var2) {
                  break;
               }
            case 4:
               this._DyeFilteringEnabled = false;
               if (var2) {
                  break;
               }
            case 2:
               this._Enabled = true;
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
            case 6:
               if (var1.equals("action")) {
                  return 6;
               }
               break;
            case 7:
               if (var1.equals("exclude")) {
                  return 10;
               }

               if (var1.equals("include")) {
                  return 9;
               }

               if (var1.equals("enabled")) {
                  return 2;
               }
               break;
            case 8:
               if (var1.equals("dye-mask")) {
                  return 3;
               }

               if (var1.equals("pointcut")) {
                  return 8;
               }
            case 9:
            case 12:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            default:
               break;
            case 10:
               if (var1.equals("properties")) {
                  return 5;
               }
               break;
            case 11:
               if (var1.equals("description")) {
                  return 1;
               }
               break;
            case 13:
               if (var1.equals("location-type")) {
                  return 7;
               }
               break;
            case 21:
               if (var1.equals("dye-filtering-enabled")) {
                  return 4;
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
            case 1:
               return "description";
            case 2:
               return "enabled";
            case 3:
               return "dye-mask";
            case 4:
               return "dye-filtering-enabled";
            case 5:
               return "properties";
            case 6:
               return "action";
            case 7:
               return "location-type";
            case 8:
               return "pointcut";
            case 9:
               return "include";
            case 10:
               return "exclude";
            default:
               return super.getElementName(var1);
         }
      }

      public boolean isArray(int var1) {
         switch (var1) {
            case 6:
               return true;
            case 7:
            case 8:
            default:
               return super.isArray(var1);
            case 9:
               return true;
            case 10:
               return true;
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
            case 4:
               return true;
            case 5:
               return true;
            case 6:
               return true;
            case 7:
               return true;
            case 8:
               return true;
            case 9:
               return true;
            case 10:
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
      private WLDFInstrumentationMonitorBeanImpl bean;

      protected Helper(WLDFInstrumentationMonitorBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 1:
               return "Description";
            case 2:
               return "Enabled";
            case 3:
               return "DyeMask";
            case 4:
               return "DyeFilteringEnabled";
            case 5:
               return "Properties";
            case 6:
               return "Actions";
            case 7:
               return "LocationType";
            case 8:
               return "Pointcut";
            case 9:
               return "Includes";
            case 10:
               return "Excludes";
            default:
               return super.getPropertyName(var1);
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("Actions")) {
            return 6;
         } else if (var1.equals("Description")) {
            return 1;
         } else if (var1.equals("DyeMask")) {
            return 3;
         } else if (var1.equals("Excludes")) {
            return 10;
         } else if (var1.equals("Includes")) {
            return 9;
         } else if (var1.equals("LocationType")) {
            return 7;
         } else if (var1.equals("Pointcut")) {
            return 8;
         } else if (var1.equals("Properties")) {
            return 5;
         } else if (var1.equals("DyeFilteringEnabled")) {
            return 4;
         } else {
            return var1.equals("Enabled") ? 2 : super.getPropertyIndex(var1);
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
            if (this.bean.isActionsSet()) {
               var2.append("Actions");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getActions())));
            }

            if (this.bean.isDescriptionSet()) {
               var2.append("Description");
               var2.append(String.valueOf(this.bean.getDescription()));
            }

            if (this.bean.isDyeMaskSet()) {
               var2.append("DyeMask");
               var2.append(String.valueOf(this.bean.getDyeMask()));
            }

            if (this.bean.isExcludesSet()) {
               var2.append("Excludes");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getExcludes())));
            }

            if (this.bean.isIncludesSet()) {
               var2.append("Includes");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getIncludes())));
            }

            if (this.bean.isLocationTypeSet()) {
               var2.append("LocationType");
               var2.append(String.valueOf(this.bean.getLocationType()));
            }

            if (this.bean.isPointcutSet()) {
               var2.append("Pointcut");
               var2.append(String.valueOf(this.bean.getPointcut()));
            }

            if (this.bean.isPropertiesSet()) {
               var2.append("Properties");
               var2.append(String.valueOf(this.bean.getProperties()));
            }

            if (this.bean.isDyeFilteringEnabledSet()) {
               var2.append("DyeFilteringEnabled");
               var2.append(String.valueOf(this.bean.isDyeFilteringEnabled()));
            }

            if (this.bean.isEnabledSet()) {
               var2.append("Enabled");
               var2.append(String.valueOf(this.bean.isEnabled()));
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
            WLDFInstrumentationMonitorBeanImpl var2 = (WLDFInstrumentationMonitorBeanImpl)var1;
            this.computeDiff("Actions", this.bean.getActions(), var2.getActions(), true);
            this.computeDiff("Description", this.bean.getDescription(), var2.getDescription(), true);
            this.computeDiff("DyeMask", this.bean.getDyeMask(), var2.getDyeMask(), true);
            this.computeDiff("Excludes", this.bean.getExcludes(), var2.getExcludes(), true);
            this.computeDiff("Includes", this.bean.getIncludes(), var2.getIncludes(), true);
            this.computeDiff("LocationType", this.bean.getLocationType(), var2.getLocationType(), false);
            this.computeDiff("Pointcut", this.bean.getPointcut(), var2.getPointcut(), false);
            this.computeDiff("Properties", this.bean.getProperties(), var2.getProperties(), true);
            this.computeDiff("DyeFilteringEnabled", this.bean.isDyeFilteringEnabled(), var2.isDyeFilteringEnabled(), true);
            this.computeDiff("Enabled", this.bean.isEnabled(), var2.isEnabled(), true);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            WLDFInstrumentationMonitorBeanImpl var3 = (WLDFInstrumentationMonitorBeanImpl)var1.getSourceBean();
            WLDFInstrumentationMonitorBeanImpl var4 = (WLDFInstrumentationMonitorBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("Actions")) {
                  var3.setActions(var4.getActions());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 6);
               } else if (var5.equals("Description")) {
                  var3.setDescription(var4.getDescription());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 1);
               } else if (var5.equals("DyeMask")) {
                  var3.setDyeMask(var4.getDyeMask());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 3);
               } else if (var5.equals("Excludes")) {
                  var3.setExcludes(var4.getExcludes());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 10);
               } else if (var5.equals("Includes")) {
                  var3.setIncludes(var4.getIncludes());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 9);
               } else if (var5.equals("LocationType")) {
                  var3.setLocationType(var4.getLocationType());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 7);
               } else if (var5.equals("Pointcut")) {
                  var3.setPointcut(var4.getPointcut());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 8);
               } else if (var5.equals("Properties")) {
                  var3.setProperties(var4.getProperties());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 5);
               } else if (var5.equals("DyeFilteringEnabled")) {
                  var3.setDyeFilteringEnabled(var4.isDyeFilteringEnabled());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 4);
               } else if (var5.equals("Enabled")) {
                  var3.setEnabled(var4.isEnabled());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 2);
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
            WLDFInstrumentationMonitorBeanImpl var5 = (WLDFInstrumentationMonitorBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            String[] var4;
            if ((var3 == null || !var3.contains("Actions")) && this.bean.isActionsSet()) {
               var4 = this.bean.getActions();
               var5.setActions(var4 == null ? null : (String[])((String[])((String[])((String[])var4)).clone()));
            }

            if ((var3 == null || !var3.contains("Description")) && this.bean.isDescriptionSet()) {
               var5.setDescription(this.bean.getDescription());
            }

            if ((var3 == null || !var3.contains("DyeMask")) && this.bean.isDyeMaskSet()) {
               var5.setDyeMask(this.bean.getDyeMask());
            }

            if ((var3 == null || !var3.contains("Excludes")) && this.bean.isExcludesSet()) {
               var4 = this.bean.getExcludes();
               var5.setExcludes(var4 == null ? null : (String[])((String[])((String[])((String[])var4)).clone()));
            }

            if ((var3 == null || !var3.contains("Includes")) && this.bean.isIncludesSet()) {
               var4 = this.bean.getIncludes();
               var5.setIncludes(var4 == null ? null : (String[])((String[])((String[])((String[])var4)).clone()));
            }

            if ((var3 == null || !var3.contains("LocationType")) && this.bean.isLocationTypeSet()) {
               var5.setLocationType(this.bean.getLocationType());
            }

            if ((var3 == null || !var3.contains("Pointcut")) && this.bean.isPointcutSet()) {
               var5.setPointcut(this.bean.getPointcut());
            }

            if ((var3 == null || !var3.contains("Properties")) && this.bean.isPropertiesSet()) {
               var5.setProperties(this.bean.getProperties());
            }

            if ((var3 == null || !var3.contains("DyeFilteringEnabled")) && this.bean.isDyeFilteringEnabledSet()) {
               var5.setDyeFilteringEnabled(this.bean.isDyeFilteringEnabled());
            }

            if ((var3 == null || !var3.contains("Enabled")) && this.bean.isEnabledSet()) {
               var5.setEnabled(this.bean.isEnabled());
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
