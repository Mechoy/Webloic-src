package weblogic.sca.descriptor;

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

public class ComponentBeanImpl extends AbstractDescriptorBean implements ComponentBean, Serializable {
   private String _ImplementationType;
   private String _Name;
   private static SchemaHelper2 _schemaHelper;

   public ComponentBeanImpl() {
      this._initializeProperty(-1);
   }

   public ComponentBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);
      this._initializeProperty(-1);
   }

   public String getName() {
      return this._Name;
   }

   public boolean isNameSet() {
      return this._isSet(0);
   }

   public void setName(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._Name;
      this._Name = var1;
      this._postSet(0, var2, var1);
   }

   public String getImplementationType() {
      return this._ImplementationType;
   }

   public boolean isImplementationTypeSet() {
      return this._isSet(1);
   }

   public void setImplementationType(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._ImplementationType;
      this._ImplementationType = var1;
      this._postSet(1, var2, var1);
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
               this._ImplementationType = null;
               if (var2) {
                  break;
               }
            case 0:
               this._Name = null;
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

   public SchemaHelper _getSchemaHelper2() {
      if (_schemaHelper == null) {
         _schemaHelper = new SchemaHelper2();
      }

      return _schemaHelper;
   }

   public static class SchemaHelper2 extends AbstractSchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 4:
               if (var1.equals("name")) {
                  return 0;
               }
               break;
            case 19:
               if (var1.equals("implementation-type")) {
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
               return "name";
            case 1:
               return "implementation-type";
            default:
               return super.getElementName(var1);
         }
      }
   }

   protected static class Helper extends AbstractDescriptorBeanHelper {
      private ComponentBeanImpl bean;

      protected Helper(ComponentBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 0:
               return "Name";
            case 1:
               return "ImplementationType";
            default:
               return super.getPropertyName(var1);
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("ImplementationType")) {
            return 1;
         } else {
            return var1.equals("Name") ? 0 : super.getPropertyIndex(var1);
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
            if (this.bean.isImplementationTypeSet()) {
               var2.append("ImplementationType");
               var2.append(String.valueOf(this.bean.getImplementationType()));
            }

            if (this.bean.isNameSet()) {
               var2.append("Name");
               var2.append(String.valueOf(this.bean.getName()));
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
            ComponentBeanImpl var2 = (ComponentBeanImpl)var1;
            this.computeDiff("ImplementationType", this.bean.getImplementationType(), var2.getImplementationType(), false);
            this.computeDiff("Name", this.bean.getName(), var2.getName(), false);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            ComponentBeanImpl var3 = (ComponentBeanImpl)var1.getSourceBean();
            ComponentBeanImpl var4 = (ComponentBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("ImplementationType")) {
                  var3.setImplementationType(var4.getImplementationType());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 1);
               } else if (var5.equals("Name")) {
                  var3.setName(var4.getName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 0);
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
            ComponentBeanImpl var5 = (ComponentBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("ImplementationType")) && this.bean.isImplementationTypeSet()) {
               var5.setImplementationType(this.bean.getImplementationType());
            }

            if ((var3 == null || !var3.contains("Name")) && this.bean.isNameSet()) {
               var5.setName(this.bean.getName());
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
