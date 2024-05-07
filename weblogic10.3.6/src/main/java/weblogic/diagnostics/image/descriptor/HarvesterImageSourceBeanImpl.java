package weblogic.diagnostics.image.descriptor;

import java.io.Serializable;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.zip.CRC32;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBeanHelper;
import weblogic.descriptor.internal.AbstractSchemaHelper2;
import weblogic.descriptor.internal.Munger;
import weblogic.descriptor.internal.SchemaHelper;
import weblogic.utils.ArrayUtils;
import weblogic.utils.collections.CombinedIterator;

public class HarvesterImageSourceBeanImpl extends AbstractDescriptorBean implements HarvesterImageSourceBean, Serializable {
   private long _HarvesterCycleDurationNanos;
   private String _HarvesterCycleStartTime;
   private String[] _HarvesterSamples;
   private static SchemaHelper2 _schemaHelper;

   public HarvesterImageSourceBeanImpl() {
      this._initializeRootBean(this.getDescriptor());
      this._initializeProperty(-1);
   }

   public HarvesterImageSourceBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);
      this._initializeRootBean(this.getDescriptor());
      this._initializeProperty(-1);
   }

   public String getHarvesterCycleStartTime() {
      return this._HarvesterCycleStartTime;
   }

   public boolean isHarvesterCycleStartTimeSet() {
      return this._isSet(0);
   }

   public void setHarvesterCycleStartTime(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._HarvesterCycleStartTime;
      this._HarvesterCycleStartTime = var1;
      this._postSet(0, var2, var1);
   }

   public void setHarvesterCycleDurationNanos(long var1) {
      long var3 = this._HarvesterCycleDurationNanos;
      this._HarvesterCycleDurationNanos = var1;
      this._postSet(1, var3, var1);
   }

   public long getHarvesterCycleDurationNanos() {
      return this._HarvesterCycleDurationNanos;
   }

   public boolean isHarvesterCycleDurationNanosSet() {
      return this._isSet(1);
   }

   public void setHarvesterSamples(String[] var1) {
      var1 = var1 == null ? new String[0] : var1;
      var1 = this._getHelper()._trimElements(var1);
      String[] var2 = this._HarvesterSamples;
      this._HarvesterSamples = var1;
      this._postSet(2, var2, var1);
   }

   public boolean addHarvesterSample(String var1) {
      this._getHelper()._ensureNonNull(var1);
      String[] var2;
      if (this._isSet(2)) {
         var2 = (String[])((String[])this._getHelper()._extendArray(this.getHarvesterSamples(), String.class, var1));
      } else {
         var2 = new String[]{var1};
      }

      try {
         this.setHarvesterSamples(var2);
         return true;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public boolean removeHarvesterSample(String var1) {
      String[] var2 = this.getHarvesterSamples();
      String[] var3 = (String[])((String[])this._getHelper()._removeElement(var2, String.class, var1));
      if (var3.length != var2.length) {
         try {
            this.setHarvesterSamples(var3);
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

   public String[] getHarvesterSamples() {
      return this._HarvesterSamples;
   }

   public boolean isHarvesterSamplesSet() {
      return this._isSet(2);
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
         var1 = 1;
      }

      try {
         switch (var1) {
            case 1:
               this._HarvesterCycleDurationNanos = 0L;
               if (var2) {
                  break;
               }
            case 0:
               this._HarvesterCycleStartTime = null;
               if (var2) {
                  break;
               }
            case 2:
               this._HarvesterSamples = new String[0];
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
      return "http://xmlns.oracle.com/weblogic/weblogic-diagnostics-image/1.0/weblogic-diagnostics-image.xsd";
   }

   protected String getTargetNamespace() {
      return "http://xmlns.oracle.com/weblogic/weblogic-diagnostics-image";
   }

   public SchemaHelper _getSchemaHelper2() {
      if (_schemaHelper == null) {
         _schemaHelper = new SchemaHelper2();
      }

      return _schemaHelper;
   }

   public static class SchemaHelper2 extends AbstractSchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 16:
               if (var1.equals("harvester-sample")) {
                  return 2;
               }
               break;
            case 26:
               if (var1.equals("harvester-cycle-start-time")) {
                  return 0;
               }
               break;
            case 30:
               if (var1.equals("harvester-cycle-duration-nanos")) {
                  return 1;
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

      public String getRootElementName() {
         return "harvester-image-source";
      }

      public String getElementName(int var1) {
         switch (var1) {
            case 0:
               return "harvester-cycle-start-time";
            case 1:
               return "harvester-cycle-duration-nanos";
            case 2:
               return "harvester-sample";
            default:
               return super.getElementName(var1);
         }
      }

      public boolean isArray(int var1) {
         switch (var1) {
            case 2:
               return true;
            default:
               return super.isArray(var1);
         }
      }
   }

   protected static class Helper extends AbstractDescriptorBeanHelper {
      private HarvesterImageSourceBeanImpl bean;

      protected Helper(HarvesterImageSourceBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 0:
               return "HarvesterCycleStartTime";
            case 1:
               return "HarvesterCycleDurationNanos";
            case 2:
               return "HarvesterSamples";
            default:
               return super.getPropertyName(var1);
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("HarvesterCycleDurationNanos")) {
            return 1;
         } else if (var1.equals("HarvesterCycleStartTime")) {
            return 0;
         } else {
            return var1.equals("HarvesterSamples") ? 2 : super.getPropertyIndex(var1);
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
            if (this.bean.isHarvesterCycleDurationNanosSet()) {
               var2.append("HarvesterCycleDurationNanos");
               var2.append(String.valueOf(this.bean.getHarvesterCycleDurationNanos()));
            }

            if (this.bean.isHarvesterCycleStartTimeSet()) {
               var2.append("HarvesterCycleStartTime");
               var2.append(String.valueOf(this.bean.getHarvesterCycleStartTime()));
            }

            if (this.bean.isHarvesterSamplesSet()) {
               var2.append("HarvesterSamples");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getHarvesterSamples())));
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
            HarvesterImageSourceBeanImpl var2 = (HarvesterImageSourceBeanImpl)var1;
            this.computeDiff("HarvesterCycleDurationNanos", this.bean.getHarvesterCycleDurationNanos(), var2.getHarvesterCycleDurationNanos(), false);
            this.computeDiff("HarvesterCycleStartTime", this.bean.getHarvesterCycleStartTime(), var2.getHarvesterCycleStartTime(), false);
            this.computeDiff("HarvesterSamples", this.bean.getHarvesterSamples(), var2.getHarvesterSamples(), false);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            HarvesterImageSourceBeanImpl var3 = (HarvesterImageSourceBeanImpl)var1.getSourceBean();
            HarvesterImageSourceBeanImpl var4 = (HarvesterImageSourceBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("HarvesterCycleDurationNanos")) {
                  var3.setHarvesterCycleDurationNanos(var4.getHarvesterCycleDurationNanos());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 1);
               } else if (var5.equals("HarvesterCycleStartTime")) {
                  var3.setHarvesterCycleStartTime(var4.getHarvesterCycleStartTime());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 0);
               } else if (var5.equals("HarvesterSamples")) {
                  if (var6 == 2) {
                     var2.resetAddedObject(var2.getAddedObject());
                     var3.addHarvesterSample((String)var2.getAddedObject());
                  } else {
                     if (var6 != 3) {
                        throw new AssertionError("Invalid type: " + var6);
                     }

                     var3.removeHarvesterSample((String)var2.getRemovedObject());
                  }

                  if (var3.getHarvesterSamples() == null || var3.getHarvesterSamples().length == 0) {
                     var3._conditionalUnset(var2.isUnsetUpdate(), 2);
                  }
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
            HarvesterImageSourceBeanImpl var5 = (HarvesterImageSourceBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("HarvesterCycleDurationNanos")) && this.bean.isHarvesterCycleDurationNanosSet()) {
               var5.setHarvesterCycleDurationNanos(this.bean.getHarvesterCycleDurationNanos());
            }

            if ((var3 == null || !var3.contains("HarvesterCycleStartTime")) && this.bean.isHarvesterCycleStartTimeSet()) {
               var5.setHarvesterCycleStartTime(this.bean.getHarvesterCycleStartTime());
            }

            if ((var3 == null || !var3.contains("HarvesterSamples")) && this.bean.isHarvesterSamplesSet()) {
               String[] var4 = this.bean.getHarvesterSamples();
               var5.setHarvesterSamples(var4 == null ? null : (String[])((String[])((String[])((String[])var4)).clone()));
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
