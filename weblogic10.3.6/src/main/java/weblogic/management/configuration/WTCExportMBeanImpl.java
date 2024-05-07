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

public class WTCExportMBeanImpl extends ConfigurationMBeanImpl implements WTCExportMBean, Serializable {
   private String _EJBName;
   private String _LocalAccessPoint;
   private String _RemoteName;
   private String _ResourceName;
   private String _TargetClass;
   private String _TargetJar;
   private static SchemaHelper2 _schemaHelper;

   public WTCExportMBeanImpl() {
      this._initializeProperty(-1);
   }

   public WTCExportMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);
      this._initializeProperty(-1);
   }

   public void setResourceName(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      weblogic.descriptor.beangen.LegalChecks.checkNonNull("ResourceName", var1);
      String var2 = this._ResourceName;
      this._ResourceName = var1;
      this._postSet(7, var2, var1);
   }

   public String getResourceName() {
      return this._ResourceName;
   }

   public boolean isResourceNameSet() {
      return this._isSet(7);
   }

   public void setLocalAccessPoint(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      weblogic.descriptor.beangen.LegalChecks.checkNonNull("LocalAccessPoint", var1);
      String var2 = this._LocalAccessPoint;
      this._LocalAccessPoint = var1;
      this._postSet(8, var2, var1);
   }

   public String getLocalAccessPoint() {
      return this._LocalAccessPoint;
   }

   public boolean isLocalAccessPointSet() {
      return this._isSet(8);
   }

   public void setEJBName(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._EJBName;
      this._EJBName = var1;
      this._postSet(9, var2, var1);
   }

   public String getEJBName() {
      return this._EJBName;
   }

   public boolean isEJBNameSet() {
      return this._isSet(9);
   }

   public void setTargetClass(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._TargetClass;
      this._TargetClass = var1;
      this._postSet(10, var2, var1);
   }

   public String getTargetClass() {
      return this._TargetClass;
   }

   public boolean isTargetClassSet() {
      return this._isSet(10);
   }

   public void setTargetJar(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._TargetJar;
      this._TargetJar = var1;
      this._postSet(11, var2, var1);
   }

   public String getTargetJar() {
      return this._TargetJar;
   }

   public boolean isTargetJarSet() {
      return this._isSet(11);
   }

   public void setRemoteName(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._RemoteName;
      this._RemoteName = var1;
      this._postSet(12, var2, var1);
   }

   public String getRemoteName() {
      return this._RemoteName;
   }

   public boolean isRemoteNameSet() {
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
         var1 = 9;
      }

      try {
         switch (var1) {
            case 9:
               this._EJBName = null;
               if (var2) {
                  break;
               }
            case 8:
               this._LocalAccessPoint = "myLAP";
               if (var2) {
                  break;
               }
            case 12:
               this._RemoteName = null;
               if (var2) {
                  break;
               }
            case 7:
               this._ResourceName = "myExport";
               if (var2) {
                  break;
               }
            case 10:
               this._TargetClass = null;
               if (var2) {
                  break;
               }
            case 11:
               this._TargetJar = null;
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
      return "WTCExport";
   }

   public void putValue(String var1, Object var2) {
      String var3;
      if (var1.equals("EJBName")) {
         var3 = this._EJBName;
         this._EJBName = (String)var2;
         this._postSet(9, var3, this._EJBName);
      } else if (var1.equals("LocalAccessPoint")) {
         var3 = this._LocalAccessPoint;
         this._LocalAccessPoint = (String)var2;
         this._postSet(8, var3, this._LocalAccessPoint);
      } else if (var1.equals("RemoteName")) {
         var3 = this._RemoteName;
         this._RemoteName = (String)var2;
         this._postSet(12, var3, this._RemoteName);
      } else if (var1.equals("ResourceName")) {
         var3 = this._ResourceName;
         this._ResourceName = (String)var2;
         this._postSet(7, var3, this._ResourceName);
      } else if (var1.equals("TargetClass")) {
         var3 = this._TargetClass;
         this._TargetClass = (String)var2;
         this._postSet(10, var3, this._TargetClass);
      } else if (var1.equals("TargetJar")) {
         var3 = this._TargetJar;
         this._TargetJar = (String)var2;
         this._postSet(11, var3, this._TargetJar);
      } else {
         super.putValue(var1, var2);
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("EJBName")) {
         return this._EJBName;
      } else if (var1.equals("LocalAccessPoint")) {
         return this._LocalAccessPoint;
      } else if (var1.equals("RemoteName")) {
         return this._RemoteName;
      } else if (var1.equals("ResourceName")) {
         return this._ResourceName;
      } else if (var1.equals("TargetClass")) {
         return this._TargetClass;
      } else {
         return var1.equals("TargetJar") ? this._TargetJar : super.getValue(var1);
      }
   }

   public static void validateGeneration() {
      try {
         weblogic.descriptor.beangen.LegalChecks.checkNonNull("LocalAccessPoint", "myLAP");
      } catch (IllegalArgumentException var2) {
         throw new DescriptorValidateException("The default value for the property  is null. Properties annotated with false value on @legalZeroLength or @legalNull  should either have @required/@derivedDefault annotations or have a non-null value on @default annotation. Refer annotation legalNull on property LocalAccessPoint in WTCExportMBean" + var2.getMessage());
      }

      try {
         weblogic.descriptor.beangen.LegalChecks.checkNonNull("ResourceName", "myExport");
      } catch (IllegalArgumentException var1) {
         throw new DescriptorValidateException("The default value for the property  is null. Properties annotated with false value on @legalZeroLength or @legalNull  should either have @required/@derivedDefault annotations or have a non-null value on @default annotation. Refer annotation legalNull on property ResourceName in WTCExportMBean" + var1.getMessage());
      }
   }

   public static class SchemaHelper2 extends ConfigurationMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 8:
               if (var1.equals("ejb-name")) {
                  return 9;
               }
            case 9:
            case 14:
            case 15:
            case 16:
            case 17:
            default:
               break;
            case 10:
               if (var1.equals("target-jar")) {
                  return 11;
               }
               break;
            case 11:
               if (var1.equals("remote-name")) {
                  return 12;
               }
               break;
            case 12:
               if (var1.equals("target-class")) {
                  return 10;
               }
               break;
            case 13:
               if (var1.equals("resource-name")) {
                  return 7;
               }
               break;
            case 18:
               if (var1.equals("local-access-point")) {
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
               return "resource-name";
            case 8:
               return "local-access-point";
            case 9:
               return "ejb-name";
            case 10:
               return "target-class";
            case 11:
               return "target-jar";
            case 12:
               return "remote-name";
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
      private WTCExportMBeanImpl bean;

      protected Helper(WTCExportMBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 7:
               return "ResourceName";
            case 8:
               return "LocalAccessPoint";
            case 9:
               return "EJBName";
            case 10:
               return "TargetClass";
            case 11:
               return "TargetJar";
            case 12:
               return "RemoteName";
            default:
               return super.getPropertyName(var1);
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("EJBName")) {
            return 9;
         } else if (var1.equals("LocalAccessPoint")) {
            return 8;
         } else if (var1.equals("RemoteName")) {
            return 12;
         } else if (var1.equals("ResourceName")) {
            return 7;
         } else if (var1.equals("TargetClass")) {
            return 10;
         } else {
            return var1.equals("TargetJar") ? 11 : super.getPropertyIndex(var1);
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
            if (this.bean.isEJBNameSet()) {
               var2.append("EJBName");
               var2.append(String.valueOf(this.bean.getEJBName()));
            }

            if (this.bean.isLocalAccessPointSet()) {
               var2.append("LocalAccessPoint");
               var2.append(String.valueOf(this.bean.getLocalAccessPoint()));
            }

            if (this.bean.isRemoteNameSet()) {
               var2.append("RemoteName");
               var2.append(String.valueOf(this.bean.getRemoteName()));
            }

            if (this.bean.isResourceNameSet()) {
               var2.append("ResourceName");
               var2.append(String.valueOf(this.bean.getResourceName()));
            }

            if (this.bean.isTargetClassSet()) {
               var2.append("TargetClass");
               var2.append(String.valueOf(this.bean.getTargetClass()));
            }

            if (this.bean.isTargetJarSet()) {
               var2.append("TargetJar");
               var2.append(String.valueOf(this.bean.getTargetJar()));
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
            WTCExportMBeanImpl var2 = (WTCExportMBeanImpl)var1;
            this.computeDiff("EJBName", this.bean.getEJBName(), var2.getEJBName(), true);
            this.computeDiff("LocalAccessPoint", this.bean.getLocalAccessPoint(), var2.getLocalAccessPoint(), true);
            this.computeDiff("RemoteName", this.bean.getRemoteName(), var2.getRemoteName(), true);
            this.computeDiff("ResourceName", this.bean.getResourceName(), var2.getResourceName(), true);
            this.computeDiff("TargetClass", this.bean.getTargetClass(), var2.getTargetClass(), true);
            this.computeDiff("TargetJar", this.bean.getTargetJar(), var2.getTargetJar(), true);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            WTCExportMBeanImpl var3 = (WTCExportMBeanImpl)var1.getSourceBean();
            WTCExportMBeanImpl var4 = (WTCExportMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("EJBName")) {
                  var3.setEJBName(var4.getEJBName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 9);
               } else if (var5.equals("LocalAccessPoint")) {
                  var3.setLocalAccessPoint(var4.getLocalAccessPoint());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 8);
               } else if (var5.equals("RemoteName")) {
                  var3.setRemoteName(var4.getRemoteName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 12);
               } else if (var5.equals("ResourceName")) {
                  var3.setResourceName(var4.getResourceName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 7);
               } else if (var5.equals("TargetClass")) {
                  var3.setTargetClass(var4.getTargetClass());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 10);
               } else if (var5.equals("TargetJar")) {
                  var3.setTargetJar(var4.getTargetJar());
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
            WTCExportMBeanImpl var5 = (WTCExportMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("EJBName")) && this.bean.isEJBNameSet()) {
               var5.setEJBName(this.bean.getEJBName());
            }

            if ((var3 == null || !var3.contains("LocalAccessPoint")) && this.bean.isLocalAccessPointSet()) {
               var5.setLocalAccessPoint(this.bean.getLocalAccessPoint());
            }

            if ((var3 == null || !var3.contains("RemoteName")) && this.bean.isRemoteNameSet()) {
               var5.setRemoteName(this.bean.getRemoteName());
            }

            if ((var3 == null || !var3.contains("ResourceName")) && this.bean.isResourceNameSet()) {
               var5.setResourceName(this.bean.getResourceName());
            }

            if ((var3 == null || !var3.contains("TargetClass")) && this.bean.isTargetClassSet()) {
               var5.setTargetClass(this.bean.getTargetClass());
            }

            if ((var3 == null || !var3.contains("TargetJar")) && this.bean.isTargetJarSet()) {
               var5.setTargetJar(this.bean.getTargetJar());
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
