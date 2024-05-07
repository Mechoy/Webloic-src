package weblogic.management.configuration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.zip.CRC32;
import javax.management.InvalidAttributeValueException;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.DescriptorValidateException;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBeanHelper;
import weblogic.descriptor.internal.Munger;
import weblogic.descriptor.internal.SchemaHelper;
import weblogic.utils.collections.CombinedIterator;

public class WTCPasswordMBeanImpl extends ConfigurationMBeanImpl implements WTCPasswordMBean, Serializable {
   private String _LocalAccessPoint;
   private String _LocalPassword;
   private String _LocalPasswordIV;
   private String _RemoteAccessPoint;
   private String _RemotePassword;
   private String _RemotePasswordIV;
   private static SchemaHelper2 _schemaHelper;

   public WTCPasswordMBeanImpl() {
      this._initializeProperty(-1);
   }

   public WTCPasswordMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);
      this._initializeProperty(-1);
   }

   public void setLocalAccessPoint(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      weblogic.descriptor.beangen.LegalChecks.checkNonNull("LocalAccessPoint", var1);
      String var2 = this._LocalAccessPoint;
      this._LocalAccessPoint = var1;
      this._postSet(7, var2, var1);
   }

   public String getLocalAccessPoint() {
      return this._LocalAccessPoint;
   }

   public boolean isLocalAccessPointSet() {
      return this._isSet(7);
   }

   public void setRemoteAccessPoint(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      weblogic.descriptor.beangen.LegalChecks.checkNonNull("RemoteAccessPoint", var1);
      String var2 = this._RemoteAccessPoint;
      this._RemoteAccessPoint = var1;
      this._postSet(8, var2, var1);
   }

   public String getRemoteAccessPoint() {
      return this._RemoteAccessPoint;
   }

   public boolean isRemoteAccessPointSet() {
      return this._isSet(8);
   }

   public void setLocalPasswordIV(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      weblogic.descriptor.beangen.LegalChecks.checkNonNull("LocalPasswordIV", var1);
      String var2 = this._LocalPasswordIV;
      this._LocalPasswordIV = var1;
      this._postSet(9, var2, var1);
   }

   public String getLocalPasswordIV() {
      return this._LocalPasswordIV;
   }

   public boolean isLocalPasswordIVSet() {
      return this._isSet(9);
   }

   public void setLocalPassword(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      weblogic.descriptor.beangen.LegalChecks.checkNonNull("LocalPassword", var1);
      String var2 = this._LocalPassword;
      this._LocalPassword = var1;
      this._postSet(10, var2, var1);
   }

   public String getLocalPassword() {
      return this._LocalPassword;
   }

   public boolean isLocalPasswordSet() {
      return this._isSet(10);
   }

   public void setRemotePasswordIV(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      weblogic.descriptor.beangen.LegalChecks.checkNonNull("RemotePasswordIV", var1);
      String var2 = this._RemotePasswordIV;
      this._RemotePasswordIV = var1;
      this._postSet(11, var2, var1);
   }

   public String getRemotePasswordIV() {
      return this._RemotePasswordIV;
   }

   public boolean isRemotePasswordIVSet() {
      return this._isSet(11);
   }

   public void setRemotePassword(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      weblogic.descriptor.beangen.LegalChecks.checkNonNull("RemotePassword", var1);
      String var2 = this._RemotePassword;
      this._RemotePassword = var1;
      this._postSet(12, var2, var1);
   }

   public String getRemotePassword() {
      return this._RemotePassword;
   }

   public boolean isRemotePasswordSet() {
      return this._isSet(12);
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
         var1 = 7;
      }

      try {
         switch (var1) {
            case 7:
               this._LocalAccessPoint = "myLAP";
               if (var2) {
                  break;
               }
            case 10:
               this._LocalPassword = "myLPWD";
               if (var2) {
                  break;
               }
            case 9:
               this._LocalPasswordIV = "myLPWDIV";
               if (var2) {
                  break;
               }
            case 8:
               this._RemoteAccessPoint = "myRAP";
               if (var2) {
                  break;
               }
            case 12:
               this._RemotePassword = "myRPWD";
               if (var2) {
                  break;
               }
            case 11:
               this._RemotePasswordIV = "myRPWDIV";
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
      return "WTCPassword";
   }

   public void putValue(String var1, Object var2) {
      String var3;
      if (var1.equals("LocalAccessPoint")) {
         var3 = this._LocalAccessPoint;
         this._LocalAccessPoint = (String)var2;
         this._postSet(7, var3, this._LocalAccessPoint);
      } else if (var1.equals("LocalPassword")) {
         var3 = this._LocalPassword;
         this._LocalPassword = (String)var2;
         this._postSet(10, var3, this._LocalPassword);
      } else if (var1.equals("LocalPasswordIV")) {
         var3 = this._LocalPasswordIV;
         this._LocalPasswordIV = (String)var2;
         this._postSet(9, var3, this._LocalPasswordIV);
      } else if (var1.equals("RemoteAccessPoint")) {
         var3 = this._RemoteAccessPoint;
         this._RemoteAccessPoint = (String)var2;
         this._postSet(8, var3, this._RemoteAccessPoint);
      } else if (var1.equals("RemotePassword")) {
         var3 = this._RemotePassword;
         this._RemotePassword = (String)var2;
         this._postSet(12, var3, this._RemotePassword);
      } else if (var1.equals("RemotePasswordIV")) {
         var3 = this._RemotePasswordIV;
         this._RemotePasswordIV = (String)var2;
         this._postSet(11, var3, this._RemotePasswordIV);
      } else {
         super.putValue(var1, var2);
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("LocalAccessPoint")) {
         return this._LocalAccessPoint;
      } else if (var1.equals("LocalPassword")) {
         return this._LocalPassword;
      } else if (var1.equals("LocalPasswordIV")) {
         return this._LocalPasswordIV;
      } else if (var1.equals("RemoteAccessPoint")) {
         return this._RemoteAccessPoint;
      } else if (var1.equals("RemotePassword")) {
         return this._RemotePassword;
      } else {
         return var1.equals("RemotePasswordIV") ? this._RemotePasswordIV : super.getValue(var1);
      }
   }

   public static void validateGeneration() {
      try {
         weblogic.descriptor.beangen.LegalChecks.checkNonNull("LocalAccessPoint", "myLAP");
      } catch (IllegalArgumentException var6) {
         throw new DescriptorValidateException("The default value for the property  is null. Properties annotated with false value on @legalZeroLength or @legalNull  should either have @required/@derivedDefault annotations or have a non-null value on @default annotation. Refer annotation legalNull on property LocalAccessPoint in WTCPasswordMBean" + var6.getMessage());
      }

      try {
         weblogic.descriptor.beangen.LegalChecks.checkNonNull("LocalPassword", "myLPWD");
      } catch (IllegalArgumentException var5) {
         throw new DescriptorValidateException("The default value for the property  is null. Properties annotated with false value on @legalZeroLength or @legalNull  should either have @required/@derivedDefault annotations or have a non-null value on @default annotation. Refer annotation legalNull on property LocalPassword in WTCPasswordMBean" + var5.getMessage());
      }

      try {
         weblogic.descriptor.beangen.LegalChecks.checkNonNull("LocalPasswordIV", "myLPWDIV");
      } catch (IllegalArgumentException var4) {
         throw new DescriptorValidateException("The default value for the property  is null. Properties annotated with false value on @legalZeroLength or @legalNull  should either have @required/@derivedDefault annotations or have a non-null value on @default annotation. Refer annotation legalNull on property LocalPasswordIV in WTCPasswordMBean" + var4.getMessage());
      }

      try {
         weblogic.descriptor.beangen.LegalChecks.checkNonNull("RemoteAccessPoint", "myRAP");
      } catch (IllegalArgumentException var3) {
         throw new DescriptorValidateException("The default value for the property  is null. Properties annotated with false value on @legalZeroLength or @legalNull  should either have @required/@derivedDefault annotations or have a non-null value on @default annotation. Refer annotation legalNull on property RemoteAccessPoint in WTCPasswordMBean" + var3.getMessage());
      }

      try {
         weblogic.descriptor.beangen.LegalChecks.checkNonNull("RemotePassword", "myRPWD");
      } catch (IllegalArgumentException var2) {
         throw new DescriptorValidateException("The default value for the property  is null. Properties annotated with false value on @legalZeroLength or @legalNull  should either have @required/@derivedDefault annotations or have a non-null value on @default annotation. Refer annotation legalNull on property RemotePassword in WTCPasswordMBean" + var2.getMessage());
      }

      try {
         weblogic.descriptor.beangen.LegalChecks.checkNonNull("RemotePasswordIV", "myRPWDIV");
      } catch (IllegalArgumentException var1) {
         throw new DescriptorValidateException("The default value for the property  is null. Properties annotated with false value on @legalZeroLength or @legalNull  should either have @required/@derivedDefault annotations or have a non-null value on @default annotation. Refer annotation legalNull on property RemotePasswordIV in WTCPasswordMBean" + var1.getMessage());
      }
   }

   public static class SchemaHelper2 extends ConfigurationMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 14:
               if (var1.equals("local-password")) {
                  return 10;
               }
               break;
            case 15:
               if (var1.equals("remote-password")) {
                  return 12;
               }
               break;
            case 16:
               if (var1.equals("local-passwordiv")) {
                  return 9;
               }
               break;
            case 17:
               if (var1.equals("remote-passwordiv")) {
                  return 11;
               }
               break;
            case 18:
               if (var1.equals("local-access-point")) {
                  return 7;
               }
               break;
            case 19:
               if (var1.equals("remote-access-point")) {
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
            case 7:
               return "local-access-point";
            case 8:
               return "remote-access-point";
            case 9:
               return "local-passwordiv";
            case 10:
               return "local-password";
            case 11:
               return "remote-passwordiv";
            case 12:
               return "remote-password";
            default:
               return super.getElementName(var1);
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

   protected static class Helper extends ConfigurationMBeanImpl.Helper {
      private WTCPasswordMBeanImpl bean;

      protected Helper(WTCPasswordMBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 7:
               return "LocalAccessPoint";
            case 8:
               return "RemoteAccessPoint";
            case 9:
               return "LocalPasswordIV";
            case 10:
               return "LocalPassword";
            case 11:
               return "RemotePasswordIV";
            case 12:
               return "RemotePassword";
            default:
               return super.getPropertyName(var1);
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("LocalAccessPoint")) {
            return 7;
         } else if (var1.equals("LocalPassword")) {
            return 10;
         } else if (var1.equals("LocalPasswordIV")) {
            return 9;
         } else if (var1.equals("RemoteAccessPoint")) {
            return 8;
         } else if (var1.equals("RemotePassword")) {
            return 12;
         } else {
            return var1.equals("RemotePasswordIV") ? 11 : super.getPropertyIndex(var1);
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
            if (this.bean.isLocalAccessPointSet()) {
               var2.append("LocalAccessPoint");
               var2.append(String.valueOf(this.bean.getLocalAccessPoint()));
            }

            if (this.bean.isLocalPasswordSet()) {
               var2.append("LocalPassword");
               var2.append(String.valueOf(this.bean.getLocalPassword()));
            }

            if (this.bean.isLocalPasswordIVSet()) {
               var2.append("LocalPasswordIV");
               var2.append(String.valueOf(this.bean.getLocalPasswordIV()));
            }

            if (this.bean.isRemoteAccessPointSet()) {
               var2.append("RemoteAccessPoint");
               var2.append(String.valueOf(this.bean.getRemoteAccessPoint()));
            }

            if (this.bean.isRemotePasswordSet()) {
               var2.append("RemotePassword");
               var2.append(String.valueOf(this.bean.getRemotePassword()));
            }

            if (this.bean.isRemotePasswordIVSet()) {
               var2.append("RemotePasswordIV");
               var2.append(String.valueOf(this.bean.getRemotePasswordIV()));
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
            WTCPasswordMBeanImpl var2 = (WTCPasswordMBeanImpl)var1;
            this.computeDiff("LocalAccessPoint", this.bean.getLocalAccessPoint(), var2.getLocalAccessPoint(), true);
            this.computeDiff("LocalPassword", this.bean.getLocalPassword(), var2.getLocalPassword(), true);
            this.computeDiff("LocalPasswordIV", this.bean.getLocalPasswordIV(), var2.getLocalPasswordIV(), true);
            this.computeDiff("RemoteAccessPoint", this.bean.getRemoteAccessPoint(), var2.getRemoteAccessPoint(), true);
            this.computeDiff("RemotePassword", this.bean.getRemotePassword(), var2.getRemotePassword(), true);
            this.computeDiff("RemotePasswordIV", this.bean.getRemotePasswordIV(), var2.getRemotePasswordIV(), true);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            WTCPasswordMBeanImpl var3 = (WTCPasswordMBeanImpl)var1.getSourceBean();
            WTCPasswordMBeanImpl var4 = (WTCPasswordMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("LocalAccessPoint")) {
                  var3.setLocalAccessPoint(var4.getLocalAccessPoint());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 7);
               } else if (var5.equals("LocalPassword")) {
                  var3.setLocalPassword(var4.getLocalPassword());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 10);
               } else if (var5.equals("LocalPasswordIV")) {
                  var3.setLocalPasswordIV(var4.getLocalPasswordIV());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 9);
               } else if (var5.equals("RemoteAccessPoint")) {
                  var3.setRemoteAccessPoint(var4.getRemoteAccessPoint());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 8);
               } else if (var5.equals("RemotePassword")) {
                  var3.setRemotePassword(var4.getRemotePassword());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 12);
               } else if (var5.equals("RemotePasswordIV")) {
                  var3.setRemotePasswordIV(var4.getRemotePasswordIV());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 11);
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
            WTCPasswordMBeanImpl var5 = (WTCPasswordMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("LocalAccessPoint")) && this.bean.isLocalAccessPointSet()) {
               var5.setLocalAccessPoint(this.bean.getLocalAccessPoint());
            }

            if ((var3 == null || !var3.contains("LocalPassword")) && this.bean.isLocalPasswordSet()) {
               var5.setLocalPassword(this.bean.getLocalPassword());
            }

            if ((var3 == null || !var3.contains("LocalPasswordIV")) && this.bean.isLocalPasswordIVSet()) {
               var5.setLocalPasswordIV(this.bean.getLocalPasswordIV());
            }

            if ((var3 == null || !var3.contains("RemoteAccessPoint")) && this.bean.isRemoteAccessPointSet()) {
               var5.setRemoteAccessPoint(this.bean.getRemoteAccessPoint());
            }

            if ((var3 == null || !var3.contains("RemotePassword")) && this.bean.isRemotePasswordSet()) {
               var5.setRemotePassword(this.bean.getRemotePassword());
            }

            if ((var3 == null || !var3.contains("RemotePasswordIV")) && this.bean.isRemotePasswordIVSet()) {
               var5.setRemotePasswordIV(this.bean.getRemotePasswordIV());
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
