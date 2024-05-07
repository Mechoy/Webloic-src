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
import weblogic.utils.collections.CombinedIterator;

public class SNMPAttributeChangeMBeanImpl extends SNMPTrapSourceMBeanImpl implements SNMPAttributeChangeMBean, Serializable {
   private String _AttributeMBeanName;
   private String _AttributeMBeanType;
   private String _AttributeName;
   private static SchemaHelper2 _schemaHelper;

   public SNMPAttributeChangeMBeanImpl() {
      this._initializeProperty(-1);
   }

   public SNMPAttributeChangeMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);
      this._initializeProperty(-1);
   }

   public String getAttributeMBeanType() {
      return this._AttributeMBeanType;
   }

   public boolean isAttributeMBeanTypeSet() {
      return this._isSet(8);
   }

   public void setAttributeMBeanType(String var1) throws InvalidAttributeValueException, ConfigurationException {
      var1 = var1 == null ? null : var1.trim();
      weblogic.descriptor.beangen.LegalChecks.checkNonEmptyString("AttributeMBeanType", var1);
      String var2 = this._AttributeMBeanType;
      this._AttributeMBeanType = var1;
      this._postSet(8, var2, var1);
   }

   public String getAttributeMBeanName() {
      return this._AttributeMBeanName;
   }

   public boolean isAttributeMBeanNameSet() {
      return this._isSet(9);
   }

   public void setAttributeMBeanName(String var1) throws InvalidAttributeValueException, ConfigurationException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._AttributeMBeanName;
      this._AttributeMBeanName = var1;
      this._postSet(9, var2, var1);
   }

   public String getAttributeName() {
      return this._AttributeName;
   }

   public boolean isAttributeNameSet() {
      return this._isSet(10);
   }

   public void setAttributeName(String var1) throws InvalidAttributeValueException, ConfigurationException {
      var1 = var1 == null ? null : var1.trim();
      weblogic.descriptor.beangen.LegalChecks.checkNonEmptyString("AttributeName", var1);
      String var2 = this._AttributeName;
      this._AttributeName = var1;
      this._postSet(10, var2, var1);
   }

   public Object _getKey() {
      return super._getKey();
   }

   public void _validate() throws IllegalArgumentException {
      super._validate();
      SNMPValidator.validateAttributeChangeMBean(this);
      weblogic.descriptor.beangen.LegalChecks.checkIsSet("AttributeMBeanType", this.isAttributeMBeanTypeSet());
      weblogic.descriptor.beangen.LegalChecks.checkIsSet("AttributeName", this.isAttributeNameSet());
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
         var1 = 9;
      }

      try {
         switch (var1) {
            case 9:
               this._AttributeMBeanName = null;
               if (var2) {
                  break;
               }
            case 8:
               this._AttributeMBeanType = null;
               if (var2) {
                  break;
               }
            case 10:
               this._AttributeName = null;
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
      return "SNMPAttributeChange";
   }

   public void putValue(String var1, Object var2) {
      String var3;
      if (var1.equals("AttributeMBeanName")) {
         var3 = this._AttributeMBeanName;
         this._AttributeMBeanName = (String)var2;
         this._postSet(9, var3, this._AttributeMBeanName);
      } else if (var1.equals("AttributeMBeanType")) {
         var3 = this._AttributeMBeanType;
         this._AttributeMBeanType = (String)var2;
         this._postSet(8, var3, this._AttributeMBeanType);
      } else if (var1.equals("AttributeName")) {
         var3 = this._AttributeName;
         this._AttributeName = (String)var2;
         this._postSet(10, var3, this._AttributeName);
      } else {
         super.putValue(var1, var2);
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("AttributeMBeanName")) {
         return this._AttributeMBeanName;
      } else if (var1.equals("AttributeMBeanType")) {
         return this._AttributeMBeanType;
      } else {
         return var1.equals("AttributeName") ? this._AttributeName : super.getValue(var1);
      }
   }

   public static class SchemaHelper2 extends SNMPTrapSourceMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 14:
               if (var1.equals("attribute-name")) {
                  return 10;
               }
               break;
            case 20:
               if (var1.equals("attributem-bean-name")) {
                  return 9;
               }

               if (var1.equals("attributem-bean-type")) {
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
            case 8:
               return "attributem-bean-type";
            case 9:
               return "attributem-bean-name";
            case 10:
               return "attribute-name";
            default:
               return super.getElementName(var1);
         }
      }

      public boolean isArray(int var1) {
         switch (var1) {
            case 7:
               return true;
            default:
               return super.isArray(var1);
         }
      }

      public boolean isBean(int var1) {
         switch (var1) {
            default:
               return super.isBean(var1);
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

   protected static class Helper extends SNMPTrapSourceMBeanImpl.Helper {
      private SNMPAttributeChangeMBeanImpl bean;

      protected Helper(SNMPAttributeChangeMBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 8:
               return "AttributeMBeanType";
            case 9:
               return "AttributeMBeanName";
            case 10:
               return "AttributeName";
            default:
               return super.getPropertyName(var1);
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("AttributeMBeanName")) {
            return 9;
         } else if (var1.equals("AttributeMBeanType")) {
            return 8;
         } else {
            return var1.equals("AttributeName") ? 10 : super.getPropertyIndex(var1);
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
            if (this.bean.isAttributeMBeanNameSet()) {
               var2.append("AttributeMBeanName");
               var2.append(String.valueOf(this.bean.getAttributeMBeanName()));
            }

            if (this.bean.isAttributeMBeanTypeSet()) {
               var2.append("AttributeMBeanType");
               var2.append(String.valueOf(this.bean.getAttributeMBeanType()));
            }

            if (this.bean.isAttributeNameSet()) {
               var2.append("AttributeName");
               var2.append(String.valueOf(this.bean.getAttributeName()));
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
            SNMPAttributeChangeMBeanImpl var2 = (SNMPAttributeChangeMBeanImpl)var1;
            this.computeDiff("AttributeMBeanName", this.bean.getAttributeMBeanName(), var2.getAttributeMBeanName(), true);
            this.computeDiff("AttributeMBeanType", this.bean.getAttributeMBeanType(), var2.getAttributeMBeanType(), true);
            this.computeDiff("AttributeName", this.bean.getAttributeName(), var2.getAttributeName(), true);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            SNMPAttributeChangeMBeanImpl var3 = (SNMPAttributeChangeMBeanImpl)var1.getSourceBean();
            SNMPAttributeChangeMBeanImpl var4 = (SNMPAttributeChangeMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("AttributeMBeanName")) {
                  var3.setAttributeMBeanName(var4.getAttributeMBeanName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 9);
               } else if (var5.equals("AttributeMBeanType")) {
                  var3.setAttributeMBeanType(var4.getAttributeMBeanType());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 8);
               } else if (var5.equals("AttributeName")) {
                  var3.setAttributeName(var4.getAttributeName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 10);
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
            SNMPAttributeChangeMBeanImpl var5 = (SNMPAttributeChangeMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("AttributeMBeanName")) && this.bean.isAttributeMBeanNameSet()) {
               var5.setAttributeMBeanName(this.bean.getAttributeMBeanName());
            }

            if ((var3 == null || !var3.contains("AttributeMBeanType")) && this.bean.isAttributeMBeanTypeSet()) {
               var5.setAttributeMBeanType(this.bean.getAttributeMBeanType());
            }

            if ((var3 == null || !var3.contains("AttributeName")) && this.bean.isAttributeNameSet()) {
               var5.setAttributeName(this.bean.getAttributeName());
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
