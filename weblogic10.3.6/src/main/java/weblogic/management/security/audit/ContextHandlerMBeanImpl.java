package weblogic.management.security.audit;

import java.io.Serializable;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.Arrays;
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
import weblogic.management.commo.AbstractCommoConfigurationBean;
import weblogic.management.commo.RequiredModelMBeanWrapper;
import weblogic.utils.ArrayUtils;
import weblogic.utils.collections.CombinedIterator;

public class ContextHandlerMBeanImpl extends AbstractCommoConfigurationBean implements ContextHandlerMBean, Serializable {
   private String[] _ActiveContextHandlerEntries;
   private String[] _SupportedContextHandlerEntries;
   private ContextHandlerImpl _customizer;
   private static SchemaHelper2 _schemaHelper;

   public ContextHandlerMBeanImpl() {
      try {
         this._customizer = new ContextHandlerImpl(new RequiredModelMBeanWrapper(this));
      } catch (Exception var2) {
         if (var2 instanceof RuntimeException) {
            throw (RuntimeException)var2;
         }

         throw new UndeclaredThrowableException(var2);
      }

      this._initializeProperty(-1);
   }

   public ContextHandlerMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);

      try {
         this._customizer = new ContextHandlerImpl(new RequiredModelMBeanWrapper(this));
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         }

         throw new UndeclaredThrowableException(var4);
      }

      this._initializeProperty(-1);
   }

   public String[] getSupportedContextHandlerEntries() {
      return this._SupportedContextHandlerEntries;
   }

   public boolean isSupportedContextHandlerEntriesSet() {
      return this._isSet(2);
   }

   public void setSupportedContextHandlerEntries(String[] var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? new String[0] : var1;
      var1 = this._getHelper()._trimElements(var1);
      this._SupportedContextHandlerEntries = var1;
   }

   public String[] getActiveContextHandlerEntries() {
      return this._ActiveContextHandlerEntries;
   }

   public boolean isActiveContextHandlerEntriesSet() {
      return this._isSet(3);
   }

   public void setActiveContextHandlerEntries(String[] var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? new String[0] : var1;
      var1 = this._getHelper()._trimElements(var1);
      String[] var2 = this._ActiveContextHandlerEntries;
      this._ActiveContextHandlerEntries = var1;
      this._postSet(3, var2, var1);
   }

   public Object _getKey() {
      return super._getKey();
   }

   public void _validate() throws IllegalArgumentException {
      super._validate();

      try {
         if (!this._customizer.validateActiveContextHandlerEntries(this.getActiveContextHandlerEntries())) {
            throw new IllegalArgumentException("The ContextHandler ActiveContextHandlerEntries attribute was set to an illegal value.");
         }
      } catch (InvalidAttributeValueException var2) {
         throw new IllegalArgumentException(var2.toString());
      }
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
               this._ActiveContextHandlerEntries = new String[0];
               if (var2) {
                  break;
               }
            case 2:
               this._SupportedContextHandlerEntries = new String[0];
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
      return "http://xmlns.oracle.com/weblogic/1.0/security.xsd";
   }

   protected String getTargetNamespace() {
      return "http://xmlns.oracle.com/weblogic/security";
   }

   public SchemaHelper _getSchemaHelper2() {
      if (_schemaHelper == null) {
         _schemaHelper = new SchemaHelper2();
      }

      return _schemaHelper;
   }

   public String wls_getInterfaceClassName() {
      return "weblogic.management.security.audit.ContextHandlerMBean";
   }

   public static class SchemaHelper2 extends AbstractSchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 28:
               if (var1.equals("active-context-handler-entry")) {
                  return 3;
               }
               break;
            case 31:
               if (var1.equals("supported-context-handler-entry")) {
                  return 2;
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
            case 2:
               return "supported-context-handler-entry";
            case 3:
               return "active-context-handler-entry";
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
            default:
               return super.isArray(var1);
         }
      }
   }

   protected static class Helper extends AbstractCommoConfigurationBean.Helper {
      private ContextHandlerMBeanImpl bean;

      protected Helper(ContextHandlerMBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 2:
               return "SupportedContextHandlerEntries";
            case 3:
               return "ActiveContextHandlerEntries";
            default:
               return super.getPropertyName(var1);
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("ActiveContextHandlerEntries")) {
            return 3;
         } else {
            return var1.equals("SupportedContextHandlerEntries") ? 2 : super.getPropertyIndex(var1);
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
            if (this.bean.isActiveContextHandlerEntriesSet()) {
               var2.append("ActiveContextHandlerEntries");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getActiveContextHandlerEntries())));
            }

            if (this.bean.isSupportedContextHandlerEntriesSet()) {
               var2.append("SupportedContextHandlerEntries");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getSupportedContextHandlerEntries())));
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
            ContextHandlerMBeanImpl var2 = (ContextHandlerMBeanImpl)var1;
            this.computeDiff("ActiveContextHandlerEntries", this.bean.getActiveContextHandlerEntries(), var2.getActiveContextHandlerEntries(), false);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            ContextHandlerMBeanImpl var3 = (ContextHandlerMBeanImpl)var1.getSourceBean();
            ContextHandlerMBeanImpl var4 = (ContextHandlerMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("ActiveContextHandlerEntries")) {
                  var3.setActiveContextHandlerEntries(var4.getActiveContextHandlerEntries());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 3);
               } else if (!var5.equals("SupportedContextHandlerEntries")) {
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
            ContextHandlerMBeanImpl var5 = (ContextHandlerMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("ActiveContextHandlerEntries")) && this.bean.isActiveContextHandlerEntriesSet()) {
               String[] var4 = this.bean.getActiveContextHandlerEntries();
               var5.setActiveContextHandlerEntries(var4 == null ? null : (String[])((String[])((String[])((String[])var4)).clone()));
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
