package weblogic.diagnostics.descriptor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.zip.CRC32;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.DescriptorValidateException;
import weblogic.descriptor.beangen.LegalChecks;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBeanHelper;
import weblogic.descriptor.internal.Munger;
import weblogic.descriptor.internal.SchemaHelper;
import weblogic.utils.collections.CombinedIterator;

public class WLDFJMSNotificationBeanImpl extends WLDFNotificationBeanImpl implements WLDFJMSNotificationBean, Serializable {
   private String _ConnectionFactoryJNDIName;
   private String _DestinationJNDIName;
   private static SchemaHelper2 _schemaHelper;

   public WLDFJMSNotificationBeanImpl() {
      this._initializeProperty(-1);
   }

   public WLDFJMSNotificationBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);
      this._initializeProperty(-1);
   }

   public String getDestinationJNDIName() {
      return this._DestinationJNDIName;
   }

   public boolean isDestinationJNDINameSet() {
      return this._isSet(2);
   }

   public void setDestinationJNDIName(String var1) {
      var1 = var1 == null ? null : var1.trim();
      LegalChecks.checkNonEmptyString("DestinationJNDIName", var1);
      LegalChecks.checkNonNull("DestinationJNDIName", var1);
      String var2 = this._DestinationJNDIName;
      this._DestinationJNDIName = var1;
      this._postSet(2, var2, var1);
   }

   public String getConnectionFactoryJNDIName() {
      return this._ConnectionFactoryJNDIName;
   }

   public boolean isConnectionFactoryJNDINameSet() {
      return this._isSet(3);
   }

   public void setConnectionFactoryJNDIName(String var1) {
      var1 = var1 == null ? null : var1.trim();
      LegalChecks.checkNonEmptyString("ConnectionFactoryJNDIName", var1);
      LegalChecks.checkNonNull("ConnectionFactoryJNDIName", var1);
      String var2 = this._ConnectionFactoryJNDIName;
      this._ConnectionFactoryJNDIName = var1;
      this._postSet(3, var2, var1);
   }

   public Object _getKey() {
      return super._getKey();
   }

   public void _validate() throws IllegalArgumentException {
      super._validate();
      LegalChecks.checkIsSet("DestinationJNDIName", this.isDestinationJNDINameSet());
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
               this._ConnectionFactoryJNDIName = "weblogic.jms.ConnectionFactory";
               if (var2) {
                  break;
               }
            case 2:
               this._DestinationJNDIName = null;
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

   public static void validateGeneration() {
      try {
         LegalChecks.checkNonEmptyString("ConnectionFactoryJNDIName", "weblogic.jms.ConnectionFactory");
      } catch (IllegalArgumentException var2) {
         throw new DescriptorValidateException("The default value for the property  is zero-length. Properties annotated with false value on @legalZeroLength or @legalNull  should either have @required/@derivedDefault annotations or have a non-zero-length value on @default annotation. Refer annotation legalZeroLength on property ConnectionFactoryJNDIName in WLDFJMSNotificationBean" + var2.getMessage());
      }

      try {
         LegalChecks.checkNonNull("ConnectionFactoryJNDIName", "weblogic.jms.ConnectionFactory");
      } catch (IllegalArgumentException var1) {
         throw new DescriptorValidateException("The default value for the property  is null. Properties annotated with false value on @legalZeroLength or @legalNull  should either have @required/@derivedDefault annotations or have a non-null value on @default annotation. Refer annotation legalNull on property ConnectionFactoryJNDIName in WLDFJMSNotificationBean" + var1.getMessage());
      }
   }

   public static class SchemaHelper2 extends WLDFNotificationBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 21:
               if (var1.equals("destination-jndi-name")) {
                  return 2;
               }
               break;
            case 28:
               if (var1.equals("connection-factory-jndi-name")) {
                  return 3;
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
               return "destination-jndi-name";
            case 3:
               return "connection-factory-jndi-name";
            default:
               return super.getElementName(var1);
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

   protected static class Helper extends WLDFNotificationBeanImpl.Helper {
      private WLDFJMSNotificationBeanImpl bean;

      protected Helper(WLDFJMSNotificationBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 2:
               return "DestinationJNDIName";
            case 3:
               return "ConnectionFactoryJNDIName";
            default:
               return super.getPropertyName(var1);
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("ConnectionFactoryJNDIName")) {
            return 3;
         } else {
            return var1.equals("DestinationJNDIName") ? 2 : super.getPropertyIndex(var1);
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
            if (this.bean.isConnectionFactoryJNDINameSet()) {
               var2.append("ConnectionFactoryJNDIName");
               var2.append(String.valueOf(this.bean.getConnectionFactoryJNDIName()));
            }

            if (this.bean.isDestinationJNDINameSet()) {
               var2.append("DestinationJNDIName");
               var2.append(String.valueOf(this.bean.getDestinationJNDIName()));
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
            WLDFJMSNotificationBeanImpl var2 = (WLDFJMSNotificationBeanImpl)var1;
            this.computeDiff("ConnectionFactoryJNDIName", this.bean.getConnectionFactoryJNDIName(), var2.getConnectionFactoryJNDIName(), true);
            this.computeDiff("DestinationJNDIName", this.bean.getDestinationJNDIName(), var2.getDestinationJNDIName(), true);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            WLDFJMSNotificationBeanImpl var3 = (WLDFJMSNotificationBeanImpl)var1.getSourceBean();
            WLDFJMSNotificationBeanImpl var4 = (WLDFJMSNotificationBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("ConnectionFactoryJNDIName")) {
                  var3.setConnectionFactoryJNDIName(var4.getConnectionFactoryJNDIName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 3);
               } else if (var5.equals("DestinationJNDIName")) {
                  var3.setDestinationJNDIName(var4.getDestinationJNDIName());
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
            WLDFJMSNotificationBeanImpl var5 = (WLDFJMSNotificationBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("ConnectionFactoryJNDIName")) && this.bean.isConnectionFactoryJNDINameSet()) {
               var5.setConnectionFactoryJNDIName(this.bean.getConnectionFactoryJNDIName());
            }

            if ((var3 == null || !var3.contains("DestinationJNDIName")) && this.bean.isDestinationJNDINameSet()) {
               var5.setDestinationJNDIName(this.bean.getDestinationJNDIName());
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
