package weblogic.management.configuration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.zip.CRC32;
import javax.management.InvalidAttributeValueException;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBeanHelper;
import weblogic.descriptor.internal.Munger;
import weblogic.descriptor.internal.SchemaHelper;
import weblogic.j2ee.descriptor.wl.validators.WseeConfigBeanValidator;
import weblogic.management.ManagementException;
import weblogic.utils.collections.CombinedIterator;

public class WebServiceBufferingQueueMBeanImpl extends ConfigurationMBeanImpl implements WebServiceBufferingQueueMBean, Serializable {
   private String _ConnectionFactoryJndiName;
   private Boolean _Enabled;
   private String _Name;
   private Boolean _TransactionEnabled;
   private static SchemaHelper2 _schemaHelper;

   public WebServiceBufferingQueueMBeanImpl() {
      this._initializeProperty(-1);
   }

   public WebServiceBufferingQueueMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);
      this._initializeProperty(-1);
   }

   public String getName() {
      return this._Name;
   }

   public boolean isNameSet() {
      return this._isSet(2);
   }

   public void setName(String var1) throws InvalidAttributeValueException, ManagementException {
      var1 = var1 == null ? null : var1.trim();
      WseeConfigBeanValidator.validateBufferQueueJndiName(var1);
      String var2 = this._Name;
      this._Name = var1;
      this._postSet(2, var2, var1);
   }

   public Boolean isEnabled() {
      return this._Enabled;
   }

   public boolean isEnabledSet() {
      return this._isSet(7);
   }

   public void setEnabled(Boolean var1) {
      Boolean var2 = this._Enabled;
      this._Enabled = var1;
      this._postSet(7, var2, var1);
   }

   public String getConnectionFactoryJndiName() {
      return this._ConnectionFactoryJndiName;
   }

   public boolean isConnectionFactoryJndiNameSet() {
      return this._isSet(8);
   }

   public void setConnectionFactoryJndiName(String var1) {
      var1 = var1 == null ? null : var1.trim();
      WseeConfigBeanValidator.validateConnectionFactoryJndiName(var1);
      String var2 = this._ConnectionFactoryJndiName;
      this._ConnectionFactoryJndiName = var1;
      this._postSet(8, var2, var1);
   }

   public Boolean isTransactionEnabled() {
      return this._TransactionEnabled;
   }

   public boolean isTransactionEnabledSet() {
      return this._isSet(9);
   }

   public void setTransactionEnabled(Boolean var1) {
      Boolean var2 = this._TransactionEnabled;
      this._TransactionEnabled = var1;
      this._postSet(9, var2, var1);
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
         var1 = 8;
      }

      try {
         switch (var1) {
            case 8:
               this._ConnectionFactoryJndiName = null;
               if (var2) {
                  break;
               }
            case 2:
               this._Name = null;
               if (var2) {
                  break;
               }
            case 7:
               this._Enabled = false;
               if (var2) {
                  break;
               }
            case 9:
               this._TransactionEnabled = false;
               if (var2) {
                  break;
               }
            case 3:
            case 4:
            case 5:
            case 6:
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
      return "WebServiceBufferingQueue";
   }

   public void putValue(String var1, Object var2) {
      String var4;
      if (var1.equals("ConnectionFactoryJndiName")) {
         var4 = this._ConnectionFactoryJndiName;
         this._ConnectionFactoryJndiName = (String)var2;
         this._postSet(8, var4, this._ConnectionFactoryJndiName);
      } else {
         Boolean var3;
         if (var1.equals("Enabled")) {
            var3 = this._Enabled;
            this._Enabled = (Boolean)var2;
            this._postSet(7, var3, this._Enabled);
         } else if (var1.equals("Name")) {
            var4 = this._Name;
            this._Name = (String)var2;
            this._postSet(2, var4, this._Name);
         } else if (var1.equals("TransactionEnabled")) {
            var3 = this._TransactionEnabled;
            this._TransactionEnabled = (Boolean)var2;
            this._postSet(9, var3, this._TransactionEnabled);
         } else {
            super.putValue(var1, var2);
         }
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("ConnectionFactoryJndiName")) {
         return this._ConnectionFactoryJndiName;
      } else if (var1.equals("Enabled")) {
         return this._Enabled;
      } else if (var1.equals("Name")) {
         return this._Name;
      } else {
         return var1.equals("TransactionEnabled") ? this._TransactionEnabled : super.getValue(var1);
      }
   }

   public static class SchemaHelper2 extends ConfigurationMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 4:
               if (var1.equals("name")) {
                  return 2;
               }
               break;
            case 7:
               if (var1.equals("enabled")) {
                  return 7;
               }
               break;
            case 19:
               if (var1.equals("transaction-enabled")) {
                  return 9;
               }
               break;
            case 28:
               if (var1.equals("connection-factory-jndi-name")) {
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
            case 2:
               return "name";
            case 3:
            case 4:
            case 5:
            case 6:
            default:
               return super.getElementName(var1);
            case 7:
               return "enabled";
            case 8:
               return "connection-factory-jndi-name";
            case 9:
               return "transaction-enabled";
         }
      }
   }

   protected static class Helper extends ConfigurationMBeanImpl.Helper {
      private WebServiceBufferingQueueMBeanImpl bean;

      protected Helper(WebServiceBufferingQueueMBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 2:
               return "Name";
            case 3:
            case 4:
            case 5:
            case 6:
            default:
               return super.getPropertyName(var1);
            case 7:
               return "Enabled";
            case 8:
               return "ConnectionFactoryJndiName";
            case 9:
               return "TransactionEnabled";
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("ConnectionFactoryJndiName")) {
            return 8;
         } else if (var1.equals("Name")) {
            return 2;
         } else if (var1.equals("Enabled")) {
            return 7;
         } else {
            return var1.equals("TransactionEnabled") ? 9 : super.getPropertyIndex(var1);
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
            if (this.bean.isConnectionFactoryJndiNameSet()) {
               var2.append("ConnectionFactoryJndiName");
               var2.append(String.valueOf(this.bean.getConnectionFactoryJndiName()));
            }

            if (this.bean.isNameSet()) {
               var2.append("Name");
               var2.append(String.valueOf(this.bean.getName()));
            }

            if (this.bean.isEnabledSet()) {
               var2.append("Enabled");
               var2.append(String.valueOf(this.bean.isEnabled()));
            }

            if (this.bean.isTransactionEnabledSet()) {
               var2.append("TransactionEnabled");
               var2.append(String.valueOf(this.bean.isTransactionEnabled()));
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
            WebServiceBufferingQueueMBeanImpl var2 = (WebServiceBufferingQueueMBeanImpl)var1;
            this.computeDiff("ConnectionFactoryJndiName", this.bean.getConnectionFactoryJndiName(), var2.getConnectionFactoryJndiName(), true);
            this.computeDiff("Name", this.bean.getName(), var2.getName(), false);
            this.computeDiff("Enabled", this.bean.isEnabled(), var2.isEnabled(), true);
            this.computeDiff("TransactionEnabled", this.bean.isTransactionEnabled(), var2.isTransactionEnabled(), true);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            WebServiceBufferingQueueMBeanImpl var3 = (WebServiceBufferingQueueMBeanImpl)var1.getSourceBean();
            WebServiceBufferingQueueMBeanImpl var4 = (WebServiceBufferingQueueMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("ConnectionFactoryJndiName")) {
                  var3.setConnectionFactoryJndiName(var4.getConnectionFactoryJndiName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 8);
               } else if (var5.equals("Name")) {
                  var3.setName(var4.getName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 2);
               } else if (var5.equals("Enabled")) {
                  var3.setEnabled(var4.isEnabled());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 7);
               } else if (var5.equals("TransactionEnabled")) {
                  var3.setTransactionEnabled(var4.isTransactionEnabled());
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
            WebServiceBufferingQueueMBeanImpl var5 = (WebServiceBufferingQueueMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("ConnectionFactoryJndiName")) && this.bean.isConnectionFactoryJndiNameSet()) {
               var5.setConnectionFactoryJndiName(this.bean.getConnectionFactoryJndiName());
            }

            if ((var3 == null || !var3.contains("Name")) && this.bean.isNameSet()) {
               var5.setName(this.bean.getName());
            }

            if ((var3 == null || !var3.contains("Enabled")) && this.bean.isEnabledSet()) {
               var5.setEnabled(this.bean.isEnabled());
            }

            if ((var3 == null || !var3.contains("TransactionEnabled")) && this.bean.isTransactionEnabledSet()) {
               var5.setTransactionEnabled(this.bean.isTransactionEnabled());
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
