package weblogic.diagnostics.image.descriptor;

import java.io.Serializable;
import java.util.ArrayList;
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
import weblogic.utils.collections.CombinedIterator;

public class MetricBeanImpl extends AbstractDescriptorBean implements MetricBean, Serializable {
   private String _AttributeName;
   private String _AttributeType;
   private String _AttributeValue;
   private static SchemaHelper2 _schemaHelper;

   public MetricBeanImpl() {
      this._initializeProperty(-1);
   }

   public MetricBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);
      this._initializeProperty(-1);
   }

   public String getAttributeName() {
      return this._AttributeName;
   }

   public boolean isAttributeNameSet() {
      return this._isSet(0);
   }

   public void setAttributeName(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._AttributeName;
      this._AttributeName = var1;
      this._postSet(0, var2, var1);
   }

   public String getAttributeValue() {
      return this._AttributeValue;
   }

   public boolean isAttributeValueSet() {
      return this._isSet(1);
   }

   public void setAttributeValue(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._AttributeValue;
      this._AttributeValue = var1;
      this._postSet(1, var2, var1);
   }

   public String getAttributeType() {
      return this._AttributeType;
   }

   public boolean isAttributeTypeSet() {
      return this._isSet(2);
   }

   public void setAttributeType(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._AttributeType;
      this._AttributeType = var1;
      this._postSet(2, var2, var1);
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
               this._AttributeName = null;
               if (var2) {
                  break;
               }
            case 2:
               this._AttributeType = null;
               if (var2) {
                  break;
               }
            case 1:
               this._AttributeValue = null;
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
            case 14:
               if (var1.equals("attribute-name")) {
                  return 0;
               }

               if (var1.equals("attribute-type")) {
                  return 2;
               }
               break;
            case 15:
               if (var1.equals("attribute-value")) {
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

      public String getElementName(int var1) {
         switch (var1) {
            case 0:
               return "attribute-name";
            case 1:
               return "attribute-value";
            case 2:
               return "attribute-type";
            default:
               return super.getElementName(var1);
         }
      }
   }

   protected static class Helper extends AbstractDescriptorBeanHelper {
      private MetricBeanImpl bean;

      protected Helper(MetricBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 0:
               return "AttributeName";
            case 1:
               return "AttributeValue";
            case 2:
               return "AttributeType";
            default:
               return super.getPropertyName(var1);
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("AttributeName")) {
            return 0;
         } else if (var1.equals("AttributeType")) {
            return 2;
         } else {
            return var1.equals("AttributeValue") ? 1 : super.getPropertyIndex(var1);
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
            if (this.bean.isAttributeNameSet()) {
               var2.append("AttributeName");
               var2.append(String.valueOf(this.bean.getAttributeName()));
            }

            if (this.bean.isAttributeTypeSet()) {
               var2.append("AttributeType");
               var2.append(String.valueOf(this.bean.getAttributeType()));
            }

            if (this.bean.isAttributeValueSet()) {
               var2.append("AttributeValue");
               var2.append(String.valueOf(this.bean.getAttributeValue()));
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
            MetricBeanImpl var2 = (MetricBeanImpl)var1;
            this.computeDiff("AttributeName", this.bean.getAttributeName(), var2.getAttributeName(), false);
            this.computeDiff("AttributeType", this.bean.getAttributeType(), var2.getAttributeType(), false);
            this.computeDiff("AttributeValue", this.bean.getAttributeValue(), var2.getAttributeValue(), false);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            MetricBeanImpl var3 = (MetricBeanImpl)var1.getSourceBean();
            MetricBeanImpl var4 = (MetricBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("AttributeName")) {
                  var3.setAttributeName(var4.getAttributeName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 0);
               } else if (var5.equals("AttributeType")) {
                  var3.setAttributeType(var4.getAttributeType());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 2);
               } else if (var5.equals("AttributeValue")) {
                  var3.setAttributeValue(var4.getAttributeValue());
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
            MetricBeanImpl var5 = (MetricBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("AttributeName")) && this.bean.isAttributeNameSet()) {
               var5.setAttributeName(this.bean.getAttributeName());
            }

            if ((var3 == null || !var3.contains("AttributeType")) && this.bean.isAttributeTypeSet()) {
               var5.setAttributeType(this.bean.getAttributeType());
            }

            if ((var3 == null || !var3.contains("AttributeValue")) && this.bean.isAttributeValueSet()) {
               var5.setAttributeValue(this.bean.getAttributeValue());
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
