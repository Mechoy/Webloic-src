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

public class AdminConsoleMBeanImpl extends ConfigurationMBeanImpl implements AdminConsoleMBean, Serializable {
   private String _CookieName;
   private int _SessionTimeout;
   private static SchemaHelper2 _schemaHelper;

   public AdminConsoleMBeanImpl() {
      this._initializeProperty(-1);
   }

   public AdminConsoleMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);
      this._initializeProperty(-1);
   }

   public String getCookieName() {
      return this._CookieName;
   }

   public boolean isCookieNameSet() {
      return this._isSet(7);
   }

   public void setCookieName(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._CookieName;
      this._CookieName = var1;
      this._postSet(7, var2, var1);
   }

   public int getSessionTimeout() {
      return this._SessionTimeout;
   }

   public boolean isSessionTimeoutSet() {
      return this._isSet(8);
   }

   public void setSessionTimeout(int var1) {
      int var2 = this._SessionTimeout;
      this._SessionTimeout = var1;
      this._postSet(8, var2, var1);
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
               this._CookieName = "ADMINCONSOLESESSION";
               if (var2) {
                  break;
               }
            case 8:
               this._SessionTimeout = 3600;
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
      return "AdminConsole";
   }

   public void putValue(String var1, Object var2) {
      if (var1.equals("CookieName")) {
         String var4 = this._CookieName;
         this._CookieName = (String)var2;
         this._postSet(7, var4, this._CookieName);
      } else if (var1.equals("SessionTimeout")) {
         int var3 = this._SessionTimeout;
         this._SessionTimeout = (Integer)var2;
         this._postSet(8, var3, this._SessionTimeout);
      } else {
         super.putValue(var1, var2);
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("CookieName")) {
         return this._CookieName;
      } else {
         return var1.equals("SessionTimeout") ? new Integer(this._SessionTimeout) : super.getValue(var1);
      }
   }

   public static class SchemaHelper2 extends ConfigurationMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 11:
               if (var1.equals("cookie-name")) {
                  return 7;
               }
               break;
            case 15:
               if (var1.equals("session-timeout")) {
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
               return "cookie-name";
            case 8:
               return "session-timeout";
            default:
               return super.getElementName(var1);
         }
      }

      public boolean isConfigurable(int var1) {
         switch (var1) {
            case 7:
               return true;
            case 8:
               return true;
            default:
               return super.isConfigurable(var1);
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
      private AdminConsoleMBeanImpl bean;

      protected Helper(AdminConsoleMBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 7:
               return "CookieName";
            case 8:
               return "SessionTimeout";
            default:
               return super.getPropertyName(var1);
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("CookieName")) {
            return 7;
         } else {
            return var1.equals("SessionTimeout") ? 8 : super.getPropertyIndex(var1);
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
            if (this.bean.isCookieNameSet()) {
               var2.append("CookieName");
               var2.append(String.valueOf(this.bean.getCookieName()));
            }

            if (this.bean.isSessionTimeoutSet()) {
               var2.append("SessionTimeout");
               var2.append(String.valueOf(this.bean.getSessionTimeout()));
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
            AdminConsoleMBeanImpl var2 = (AdminConsoleMBeanImpl)var1;
            this.computeDiff("CookieName", this.bean.getCookieName(), var2.getCookieName(), false);
            this.computeDiff("SessionTimeout", this.bean.getSessionTimeout(), var2.getSessionTimeout(), false);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            AdminConsoleMBeanImpl var3 = (AdminConsoleMBeanImpl)var1.getSourceBean();
            AdminConsoleMBeanImpl var4 = (AdminConsoleMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("CookieName")) {
                  var3.setCookieName(var4.getCookieName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 7);
               } else if (var5.equals("SessionTimeout")) {
                  var3.setSessionTimeout(var4.getSessionTimeout());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 8);
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
            AdminConsoleMBeanImpl var5 = (AdminConsoleMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("CookieName")) && this.bean.isCookieNameSet()) {
               var5.setCookieName(this.bean.getCookieName());
            }

            if ((var3 == null || !var3.contains("SessionTimeout")) && this.bean.isSessionTimeoutSet()) {
               var5.setSessionTimeout(this.bean.getSessionTimeout());
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
