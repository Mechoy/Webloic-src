package weblogic.management.configuration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.zip.CRC32;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBeanHelper;
import weblogic.descriptor.internal.Munger;
import weblogic.descriptor.internal.SchemaHelper;
import weblogic.utils.collections.CombinedIterator;

public class ContextCaseMBeanImpl extends DeploymentMBeanImpl implements ContextCaseMBean, Serializable {
   private String _GroupName;
   private String _RequestClassName;
   private String _UserName;
   private static SchemaHelper2 _schemaHelper;

   public ContextCaseMBeanImpl() {
      this._initializeProperty(-1);
   }

   public ContextCaseMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);
      this._initializeProperty(-1);
   }

   public String getUserName() {
      return this._UserName;
   }

   public boolean isUserNameSet() {
      return this._isSet(9);
   }

   public void setUserName(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._UserName;
      this._UserName = var1;
      this._postSet(9, var2, var1);
   }

   public String getGroupName() {
      return this._GroupName;
   }

   public boolean isGroupNameSet() {
      return this._isSet(10);
   }

   public void setGroupName(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._GroupName;
      this._GroupName = var1;
      this._postSet(10, var2, var1);
   }

   public String getRequestClassName() {
      return this._RequestClassName;
   }

   public boolean isRequestClassNameSet() {
      return this._isSet(11);
   }

   public void setRequestClassName(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._RequestClassName;
      this._RequestClassName = var1;
      this._postSet(11, var2, var1);
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
         var1 = 10;
      }

      try {
         switch (var1) {
            case 10:
               this._GroupName = null;
               if (var2) {
                  break;
               }
            case 11:
               this._RequestClassName = null;
               if (var2) {
                  break;
               }
            case 9:
               this._UserName = null;
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
      return "ContextCase";
   }

   public void putValue(String var1, Object var2) {
      String var3;
      if (var1.equals("GroupName")) {
         var3 = this._GroupName;
         this._GroupName = (String)var2;
         this._postSet(10, var3, this._GroupName);
      } else if (var1.equals("RequestClassName")) {
         var3 = this._RequestClassName;
         this._RequestClassName = (String)var2;
         this._postSet(11, var3, this._RequestClassName);
      } else if (var1.equals("UserName")) {
         var3 = this._UserName;
         this._UserName = (String)var2;
         this._postSet(9, var3, this._UserName);
      } else {
         super.putValue(var1, var2);
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("GroupName")) {
         return this._GroupName;
      } else if (var1.equals("RequestClassName")) {
         return this._RequestClassName;
      } else {
         return var1.equals("UserName") ? this._UserName : super.getValue(var1);
      }
   }

   public static class SchemaHelper2 extends DeploymentMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 9:
               if (var1.equals("user-name")) {
                  return 9;
               }
               break;
            case 10:
               if (var1.equals("group-name")) {
                  return 10;
               }
               break;
            case 18:
               if (var1.equals("request-class-name")) {
                  return 11;
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
            case 9:
               return "user-name";
            case 10:
               return "group-name";
            case 11:
               return "request-class-name";
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

   protected static class Helper extends DeploymentMBeanImpl.Helper {
      private ContextCaseMBeanImpl bean;

      protected Helper(ContextCaseMBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 9:
               return "UserName";
            case 10:
               return "GroupName";
            case 11:
               return "RequestClassName";
            default:
               return super.getPropertyName(var1);
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("GroupName")) {
            return 10;
         } else if (var1.equals("RequestClassName")) {
            return 11;
         } else {
            return var1.equals("UserName") ? 9 : super.getPropertyIndex(var1);
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
            if (this.bean.isGroupNameSet()) {
               var2.append("GroupName");
               var2.append(String.valueOf(this.bean.getGroupName()));
            }

            if (this.bean.isRequestClassNameSet()) {
               var2.append("RequestClassName");
               var2.append(String.valueOf(this.bean.getRequestClassName()));
            }

            if (this.bean.isUserNameSet()) {
               var2.append("UserName");
               var2.append(String.valueOf(this.bean.getUserName()));
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
            ContextCaseMBeanImpl var2 = (ContextCaseMBeanImpl)var1;
            this.computeDiff("GroupName", this.bean.getGroupName(), var2.getGroupName(), false);
            this.computeDiff("RequestClassName", this.bean.getRequestClassName(), var2.getRequestClassName(), false);
            this.computeDiff("UserName", this.bean.getUserName(), var2.getUserName(), false);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            ContextCaseMBeanImpl var3 = (ContextCaseMBeanImpl)var1.getSourceBean();
            ContextCaseMBeanImpl var4 = (ContextCaseMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("GroupName")) {
                  var3.setGroupName(var4.getGroupName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 10);
               } else if (var5.equals("RequestClassName")) {
                  var3.setRequestClassName(var4.getRequestClassName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 11);
               } else if (var5.equals("UserName")) {
                  var3.setUserName(var4.getUserName());
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
            ContextCaseMBeanImpl var5 = (ContextCaseMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("GroupName")) && this.bean.isGroupNameSet()) {
               var5.setGroupName(this.bean.getGroupName());
            }

            if ((var3 == null || !var3.contains("RequestClassName")) && this.bean.isRequestClassNameSet()) {
               var5.setRequestClassName(this.bean.getRequestClassName());
            }

            if ((var3 == null || !var3.contains("UserName")) && this.bean.isUserNameSet()) {
               var5.setUserName(this.bean.getUserName());
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
