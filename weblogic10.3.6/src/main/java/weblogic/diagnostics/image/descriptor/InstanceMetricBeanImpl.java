package weblogic.diagnostics.image.descriptor;

import java.io.Serializable;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.zip.CRC32;
import javax.management.InvalidAttributeValueException;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBeanHelper;
import weblogic.descriptor.internal.AbstractSchemaHelper2;
import weblogic.descriptor.internal.Munger;
import weblogic.descriptor.internal.SchemaHelper;
import weblogic.utils.collections.ArrayIterator;
import weblogic.utils.collections.CombinedIterator;

public class InstanceMetricBeanImpl extends AbstractDescriptorBean implements InstanceMetricBean, Serializable {
   private String _InstanceName;
   private MetricBean[] _MBeanMetrics;
   private static SchemaHelper2 _schemaHelper;

   public InstanceMetricBeanImpl() {
      this._initializeProperty(-1);
   }

   public InstanceMetricBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);
      this._initializeProperty(-1);
   }

   public String getInstanceName() {
      return this._InstanceName;
   }

   public boolean isInstanceNameSet() {
      return this._isSet(0);
   }

   public void setInstanceName(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._InstanceName;
      this._InstanceName = var1;
      this._postSet(0, var2, var1);
   }

   public void addMBeanMetric(MetricBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 1)) {
         MetricBean[] var2;
         if (this._isSet(1)) {
            var2 = (MetricBean[])((MetricBean[])this._getHelper()._extendArray(this.getMBeanMetrics(), MetricBean.class, var1));
         } else {
            var2 = new MetricBean[]{var1};
         }

         try {
            this.setMBeanMetrics(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public MetricBean[] getMBeanMetrics() {
      return this._MBeanMetrics;
   }

   public boolean isMBeanMetricsSet() {
      return this._isSet(1);
   }

   public void removeMBeanMetric(MetricBean var1) {
      MetricBean[] var2 = this.getMBeanMetrics();
      MetricBean[] var3 = (MetricBean[])((MetricBean[])this._getHelper()._removeElement(var2, MetricBean.class, var1));
      if (var3.length != var2.length) {
         this._preDestroy((AbstractDescriptorBean)var1);

         try {
            this._getReferenceManager().unregisterBean((AbstractDescriptorBean)var1);
            this.setMBeanMetrics(var3);
         } catch (Exception var5) {
            if (var5 instanceof RuntimeException) {
               throw (RuntimeException)var5;
            }

            throw new UndeclaredThrowableException(var5);
         }
      }

   }

   public void setMBeanMetrics(MetricBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new MetricBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 1)) {
            this._getReferenceManager().registerBean(var3, false);
            this._postCreate(var3);
         }
      }

      MetricBean[] var5 = this._MBeanMetrics;
      this._MBeanMetrics = (MetricBean[])var4;
      this._postSet(1, var5, var4);
   }

   public MetricBean createMBeanMetric() {
      MetricBeanImpl var1 = new MetricBeanImpl(this, -1);

      try {
         this.addMBeanMetric(var1);
         return var1;
      } catch (Exception var3) {
         if (var3 instanceof RuntimeException) {
            throw (RuntimeException)var3;
         } else {
            throw new UndeclaredThrowableException(var3);
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
         var1 = 0;
      }

      try {
         switch (var1) {
            case 0:
               this._InstanceName = null;
               if (var2) {
                  break;
               }
            case 1:
               this._MBeanMetrics = new MetricBean[0];
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
            case 13:
               if (var1.equals("instance-name")) {
                  return 0;
               } else if (var1.equals("m-bean-metric")) {
                  return 1;
               }
            default:
               return super.getPropertyIndex(var1);
         }
      }

      public SchemaHelper getSchemaHelper(int var1) {
         switch (var1) {
            case 1:
               return new MetricBeanImpl.SchemaHelper2();
            default:
               return super.getSchemaHelper(var1);
         }
      }

      public String getElementName(int var1) {
         switch (var1) {
            case 0:
               return "instance-name";
            case 1:
               return "m-bean-metric";
            default:
               return super.getElementName(var1);
         }
      }

      public boolean isArray(int var1) {
         switch (var1) {
            case 1:
               return true;
            default:
               return super.isArray(var1);
         }
      }

      public boolean isBean(int var1) {
         switch (var1) {
            case 1:
               return true;
            default:
               return super.isBean(var1);
         }
      }
   }

   protected static class Helper extends AbstractDescriptorBeanHelper {
      private InstanceMetricBeanImpl bean;

      protected Helper(InstanceMetricBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 0:
               return "InstanceName";
            case 1:
               return "MBeanMetrics";
            default:
               return super.getPropertyName(var1);
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("InstanceName")) {
            return 0;
         } else {
            return var1.equals("MBeanMetrics") ? 1 : super.getPropertyIndex(var1);
         }
      }

      public Iterator getChildren() {
         ArrayList var1 = new ArrayList();
         var1.add(new ArrayIterator(this.bean.getMBeanMetrics()));
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
            if (this.bean.isInstanceNameSet()) {
               var2.append("InstanceName");
               var2.append(String.valueOf(this.bean.getInstanceName()));
            }

            var5 = 0L;

            for(int var7 = 0; var7 < this.bean.getMBeanMetrics().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getMBeanMetrics()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
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
            InstanceMetricBeanImpl var2 = (InstanceMetricBeanImpl)var1;
            this.computeDiff("InstanceName", this.bean.getInstanceName(), var2.getInstanceName(), false);
            this.computeChildDiff("MBeanMetrics", this.bean.getMBeanMetrics(), var2.getMBeanMetrics(), false);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            InstanceMetricBeanImpl var3 = (InstanceMetricBeanImpl)var1.getSourceBean();
            InstanceMetricBeanImpl var4 = (InstanceMetricBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("InstanceName")) {
                  var3.setInstanceName(var4.getInstanceName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 0);
               } else if (var5.equals("MBeanMetrics")) {
                  if (var6 == 2) {
                     var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                     var3.addMBeanMetric((MetricBean)var2.getAddedObject());
                  } else {
                     if (var6 != 3) {
                        throw new AssertionError("Invalid type: " + var6);
                     }

                     var3.removeMBeanMetric((MetricBean)var2.getRemovedObject());
                  }

                  if (var3.getMBeanMetrics() == null || var3.getMBeanMetrics().length == 0) {
                     var3._conditionalUnset(var2.isUnsetUpdate(), 1);
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
            InstanceMetricBeanImpl var5 = (InstanceMetricBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("InstanceName")) && this.bean.isInstanceNameSet()) {
               var5.setInstanceName(this.bean.getInstanceName());
            }

            if ((var3 == null || !var3.contains("MBeanMetrics")) && this.bean.isMBeanMetricsSet() && !var5._isSet(1)) {
               MetricBean[] var6 = this.bean.getMBeanMetrics();
               MetricBean[] var7 = new MetricBean[var6.length];

               for(int var8 = 0; var8 < var7.length; ++var8) {
                  var7[var8] = (MetricBean)((MetricBean)this.createCopy((AbstractDescriptorBean)var6[var8], var2));
               }

               var5.setMBeanMetrics(var7);
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
         this.inferSubTree(this.bean.getMBeanMetrics(), var1, var2);
      }
   }
}
