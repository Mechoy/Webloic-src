package weblogic.management;

import java.io.Serializable;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.zip.CRC32;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanInfo;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBeanHelper;
import weblogic.descriptor.internal.AbstractSchemaHelper2;
import weblogic.descriptor.internal.Munger;
import weblogic.descriptor.internal.SchemaHelper;
import weblogic.management.configuration.ConfigurationException;
import weblogic.management.internal.mbean.AbstractDynamicMBean;
import weblogic.management.mbeans.custom.WebLogic;
import weblogic.utils.collections.CombinedIterator;

public class WebLogicMBeanImpl extends AbstractDynamicMBean implements WebLogicMBean, Serializable {
   private boolean _CachingDisabled;
   private MBeanInfo _MBeanInfo;
   private String _Name;
   private WebLogicObjectName _ObjectName;
   private WebLogicMBean _Parent;
   private boolean _Registered;
   private String _Type;
   private WebLogic _customizer;
   private static SchemaHelper2 _schemaHelper;

   public WebLogicMBeanImpl() {
      try {
         this._customizer = new WebLogic(this);
      } catch (Exception var2) {
         if (var2 instanceof RuntimeException) {
            throw (RuntimeException)var2;
         }

         throw new UndeclaredThrowableException(var2);
      }

      this._initializeProperty(-1);
   }

   public WebLogicMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);

      try {
         this._customizer = new WebLogic(this);
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         }

         throw new UndeclaredThrowableException(var4);
      }

      this._initializeProperty(-1);
   }

   public String getName() {
      return this._customizer.getName();
   }

   public boolean isNameSet() {
      return this._isSet(1);
   }

   public void setName(String var1) throws InvalidAttributeValueException, ManagementException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this.getName();
      this._customizer.setName(var1);
      this._postSet(1, var2, var1);
   }

   public String getType() {
      return this._Type;
   }

   public boolean isTypeSet() {
      return this._isSet(2);
   }

   public void setType(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      this._Type = var1;
   }

   public WebLogicObjectName getObjectName() {
      return this._customizer.getObjectName();
   }

   public boolean isObjectNameSet() {
      return this._isSet(3);
   }

   public void setObjectName(WebLogicObjectName var1) throws InvalidAttributeValueException {
      this._ObjectName = var1;
   }

   public MBeanInfo getMBeanInfo() {
      return this._MBeanInfo;
   }

   public boolean isCachingDisabled() {
      return this._CachingDisabled;
   }

   public boolean isCachingDisabledSet() {
      return this._isSet(4);
   }

   public boolean isMBeanInfoSet() {
      return this._isSet(0);
   }

   public void setCachingDisabled(boolean var1) throws InvalidAttributeValueException {
      this._CachingDisabled = var1;
   }

   public void setMBeanInfo(MBeanInfo var1) throws InvalidAttributeValueException {
      this._MBeanInfo = var1;
   }

   public WebLogicMBean getParent() {
      return this._customizer.getParent();
   }

   public boolean isParentSet() {
      return this._isSet(5);
   }

   public void setParent(WebLogicMBean var1) throws ConfigurationException {
      this._customizer.setParent(var1);
   }

   public boolean isRegistered() {
      return this._customizer.isRegistered();
   }

   public boolean isRegisteredSet() {
      return this._isSet(6);
   }

   public void setRegistered(boolean var1) throws InvalidAttributeValueException {
      this._Registered = var1;
   }

   public Object _getKey() {
      return this.getName();
   }

   public void _validate() throws IllegalArgumentException {
      super._validate();
   }

   public boolean _hasKey() {
      return true;
   }

   public boolean _isPropertyAKey(Munger.ReaderEventInfo var1) {
      String var2 = var1.getElementName();
      switch (var2.length()) {
         case 4:
            if (var2.equals("name")) {
               return var1.compareXpaths(this._getPropertyXpath("name"));
            }

            return super._isPropertyAKey(var1);
         default:
            return super._isPropertyAKey(var1);
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
         var1 = 0;
      }

      try {
         switch (var1) {
            case 0:
               this._MBeanInfo = null;
               if (var2) {
                  break;
               }
            case 1:
               this._customizer.setName((String)null);
               if (var2) {
                  break;
               }
            case 3:
               this._ObjectName = null;
               if (var2) {
                  break;
               }
            case 5:
               this._customizer.setParent((WebLogicMBean)null);
               if (var2) {
                  break;
               }
            case 2:
               this._Type = null;
               if (var2) {
                  break;
               }
            case 4:
               this._CachingDisabled = false;
               if (var2) {
                  break;
               }
            case 6:
               this._Registered = false;
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

   public void putValue(String var1, Object var2) {
      boolean var5;
      if (var1.equals("CachingDisabled")) {
         var5 = this._CachingDisabled;
         this._CachingDisabled = (Boolean)var2;
         this._postSet(4, var5, this._CachingDisabled);
      } else if (var1.equals("MBeanInfo")) {
         MBeanInfo var8 = this._MBeanInfo;
         this._MBeanInfo = (MBeanInfo)var2;
         this._postSet(0, var8, this._MBeanInfo);
      } else {
         String var4;
         if (var1.equals("Name")) {
            var4 = this._Name;
            this._Name = (String)var2;
            this._postSet(1, var4, this._Name);
         } else if (var1.equals("ObjectName")) {
            WebLogicObjectName var7 = this._ObjectName;
            this._ObjectName = (WebLogicObjectName)var2;
            this._postSet(3, var7, this._ObjectName);
         } else if (var1.equals("Parent")) {
            WebLogicMBean var6 = this._Parent;
            this._Parent = (WebLogicMBean)var2;
            this._postSet(5, var6, this._Parent);
         } else if (var1.equals("Registered")) {
            var5 = this._Registered;
            this._Registered = (Boolean)var2;
            this._postSet(6, var5, this._Registered);
         } else if (var1.equals("Type")) {
            var4 = this._Type;
            this._Type = (String)var2;
            this._postSet(2, var4, this._Type);
         } else if (var1.equals("customizer")) {
            WebLogic var3 = this._customizer;
            this._customizer = (WebLogic)var2;
         } else {
            super.putValue(var1, var2);
         }
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("CachingDisabled")) {
         return new Boolean(this._CachingDisabled);
      } else if (var1.equals("MBeanInfo")) {
         return this._MBeanInfo;
      } else if (var1.equals("Name")) {
         return this._Name;
      } else if (var1.equals("ObjectName")) {
         return this._ObjectName;
      } else if (var1.equals("Parent")) {
         return this._Parent;
      } else if (var1.equals("Registered")) {
         return new Boolean(this._Registered);
      } else if (var1.equals("Type")) {
         return this._Type;
      } else {
         return var1.equals("customizer") ? this._customizer : super.getValue(var1);
      }
   }

   public static class SchemaHelper2 extends AbstractSchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 4:
               if (var1.equals("name")) {
                  return 1;
               }

               if (var1.equals("type")) {
                  return 2;
               }
            case 5:
            case 7:
            case 8:
            case 9:
            case 12:
            case 13:
            case 14:
            case 15:
            default:
               break;
            case 6:
               if (var1.equals("parent")) {
                  return 5;
               }
               break;
            case 10:
               if (var1.equals("registered")) {
                  return 6;
               }
               break;
            case 11:
               if (var1.equals("m-bean-info")) {
                  return 0;
               }

               if (var1.equals("object-name")) {
                  return 3;
               }
               break;
            case 16:
               if (var1.equals("caching-disabled")) {
                  return 4;
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
               return "m-bean-info";
            case 1:
               return "name";
            case 2:
               return "type";
            case 3:
               return "object-name";
            case 4:
               return "caching-disabled";
            case 5:
               return "parent";
            case 6:
               return "registered";
            default:
               return super.getElementName(var1);
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
            case 1:
               return true;
            default:
               return super.isKey(var1);
         }
      }

      public boolean hasKey() {
         return true;
      }

      public String[] getKeyElementNames() {
         ArrayList var1 = new ArrayList();
         var1.add("name");
         return (String[])((String[])var1.toArray(new String[0]));
      }
   }

   protected static class Helper extends AbstractDynamicMBean.Helper {
      private WebLogicMBeanImpl bean;

      protected Helper(WebLogicMBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 0:
               return "MBeanInfo";
            case 1:
               return "Name";
            case 2:
               return "Type";
            case 3:
               return "ObjectName";
            case 4:
               return "CachingDisabled";
            case 5:
               return "Parent";
            case 6:
               return "Registered";
            default:
               return super.getPropertyName(var1);
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("MBeanInfo")) {
            return 0;
         } else if (var1.equals("Name")) {
            return 1;
         } else if (var1.equals("ObjectName")) {
            return 3;
         } else if (var1.equals("Parent")) {
            return 5;
         } else if (var1.equals("Type")) {
            return 2;
         } else if (var1.equals("CachingDisabled")) {
            return 4;
         } else {
            return var1.equals("Registered") ? 6 : super.getPropertyIndex(var1);
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
            if (this.bean.isMBeanInfoSet()) {
               var2.append("MBeanInfo");
               var2.append(String.valueOf(this.bean.getMBeanInfo()));
            }

            if (this.bean.isNameSet()) {
               var2.append("Name");
               var2.append(String.valueOf(this.bean.getName()));
            }

            if (this.bean.isObjectNameSet()) {
               var2.append("ObjectName");
               var2.append(String.valueOf(this.bean.getObjectName()));
            }

            if (this.bean.isParentSet()) {
               var2.append("Parent");
               var2.append(String.valueOf(this.bean.getParent()));
            }

            if (this.bean.isTypeSet()) {
               var2.append("Type");
               var2.append(String.valueOf(this.bean.getType()));
            }

            if (this.bean.isCachingDisabledSet()) {
               var2.append("CachingDisabled");
               var2.append(String.valueOf(this.bean.isCachingDisabled()));
            }

            if (this.bean.isRegisteredSet()) {
               var2.append("Registered");
               var2.append(String.valueOf(this.bean.isRegistered()));
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
            WebLogicMBeanImpl var2 = (WebLogicMBeanImpl)var1;
            this.computeDiff("Name", this.bean.getName(), var2.getName(), false);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            WebLogicMBeanImpl var3 = (WebLogicMBeanImpl)var1.getSourceBean();
            WebLogicMBeanImpl var4 = (WebLogicMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (!var5.equals("MBeanInfo")) {
                  if (var5.equals("Name")) {
                     var3.setName(var4.getName());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 1);
                  } else if (!var5.equals("ObjectName") && !var5.equals("Parent") && !var5.equals("Type") && !var5.equals("CachingDisabled") && !var5.equals("Registered")) {
                     super.applyPropertyUpdate(var1, var2);
                  }
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
            WebLogicMBeanImpl var5 = (WebLogicMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
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
         this.inferSubTree(this.bean.getParent(), var1, var2);
      }
   }
}
