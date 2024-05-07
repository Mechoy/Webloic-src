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

public class SNMPProxyMBeanImpl extends ConfigurationMBeanImpl implements SNMPProxyMBean, Serializable {
   private String _Community;
   private String _OidRoot;
   private int _Port;
   private String _SecurityLevel;
   private String _SecurityName;
   private long _Timeout;
   private static SchemaHelper2 _schemaHelper;

   public SNMPProxyMBeanImpl() {
      this._initializeProperty(-1);
   }

   public SNMPProxyMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);
      this._initializeProperty(-1);
   }

   public int getPort() {
      return this._Port;
   }

   public boolean isPortSet() {
      return this._isSet(7);
   }

   public void setPort(int var1) throws InvalidAttributeValueException, ConfigurationException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("Port", (long)var1, 0L, 65535L);
      int var2 = this._Port;
      this._Port = var1;
      this._postSet(7, var2, var1);
   }

   public String getOidRoot() {
      return this._OidRoot;
   }

   public boolean isOidRootSet() {
      return this._isSet(8);
   }

   public void setOidRoot(String var1) throws InvalidAttributeValueException, ConfigurationException {
      var1 = var1 == null ? null : var1.trim();
      weblogic.descriptor.beangen.LegalChecks.checkNonEmptyString("OidRoot", var1);
      String var2 = this._OidRoot;
      this._OidRoot = var1;
      this._postSet(8, var2, var1);
   }

   public String getCommunity() {
      return this._Community;
   }

   public boolean isCommunitySet() {
      return this._isSet(9);
   }

   public void setCommunity(String var1) throws InvalidAttributeValueException, ConfigurationException {
      var1 = var1 == null ? null : var1.trim();
      weblogic.descriptor.beangen.LegalChecks.checkNonEmptyString("Community", var1);
      String var2 = this._Community;
      this._Community = var1;
      this._postSet(9, var2, var1);
   }

   public long getTimeout() {
      return this._Timeout;
   }

   public boolean isTimeoutSet() {
      return this._isSet(10);
   }

   public void setTimeout(long var1) throws InvalidAttributeValueException, ConfigurationException {
      weblogic.descriptor.beangen.LegalChecks.checkMin("Timeout", var1, 0L);
      long var3 = this._Timeout;
      this._Timeout = var1;
      this._postSet(10, var3, var1);
   }

   public String getSecurityName() {
      return this._SecurityName;
   }

   public boolean isSecurityNameSet() {
      return this._isSet(11);
   }

   public void setSecurityName(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._SecurityName;
      this._SecurityName = var1;
      this._postSet(11, var2, var1);
   }

   public String getSecurityLevel() {
      if (!this._isSet(12)) {
         return this._isProductionModeEnabled() ? "authNoPriv" : "noAuthNoPriv";
      } else {
         return this._SecurityLevel;
      }
   }

   public boolean isSecurityLevelSet() {
      return this._isSet(12);
   }

   public void setSecurityLevel(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{"noAuthNoPriv", "authNoPriv", "authPriv"};
      var1 = weblogic.descriptor.beangen.LegalChecks.checkInEnum("SecurityLevel", var1, var2);
      weblogic.descriptor.beangen.LegalChecks.checkNonNull("SecurityLevel", var1);
      String var3 = this._SecurityLevel;
      this._SecurityLevel = var1;
      this._postSet(12, var3, var1);
   }

   public Object _getKey() {
      return super._getKey();
   }

   public void _validate() throws IllegalArgumentException {
      super._validate();
      SNMPValidator.validateSNMPProxy(this);
      weblogic.descriptor.beangen.LegalChecks.checkIsSet("OidRoot", this.isOidRootSet());
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
               this._Community = "public";
               if (var2) {
                  break;
               }
            case 8:
               this._OidRoot = null;
               if (var2) {
                  break;
               }
            case 7:
               this._Port = 0;
               if (var2) {
                  break;
               }
            case 12:
               this._SecurityLevel = "noAuthNoPriv";
               if (var2) {
                  break;
               }
            case 11:
               this._SecurityName = null;
               if (var2) {
                  break;
               }
            case 10:
               this._Timeout = 5000L;
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
      return "SNMPProxy";
   }

   public void putValue(String var1, Object var2) {
      String var5;
      if (var1.equals("Community")) {
         var5 = this._Community;
         this._Community = (String)var2;
         this._postSet(9, var5, this._Community);
      } else if (var1.equals("OidRoot")) {
         var5 = this._OidRoot;
         this._OidRoot = (String)var2;
         this._postSet(8, var5, this._OidRoot);
      } else if (var1.equals("Port")) {
         int var6 = this._Port;
         this._Port = (Integer)var2;
         this._postSet(7, var6, this._Port);
      } else if (var1.equals("SecurityLevel")) {
         var5 = this._SecurityLevel;
         this._SecurityLevel = (String)var2;
         this._postSet(12, var5, this._SecurityLevel);
      } else if (var1.equals("SecurityName")) {
         var5 = this._SecurityName;
         this._SecurityName = (String)var2;
         this._postSet(11, var5, this._SecurityName);
      } else if (var1.equals("Timeout")) {
         long var3 = this._Timeout;
         this._Timeout = (Long)var2;
         this._postSet(10, var3, this._Timeout);
      } else {
         super.putValue(var1, var2);
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("Community")) {
         return this._Community;
      } else if (var1.equals("OidRoot")) {
         return this._OidRoot;
      } else if (var1.equals("Port")) {
         return new Integer(this._Port);
      } else if (var1.equals("SecurityLevel")) {
         return this._SecurityLevel;
      } else if (var1.equals("SecurityName")) {
         return this._SecurityName;
      } else {
         return var1.equals("Timeout") ? new Long(this._Timeout) : super.getValue(var1);
      }
   }

   public static void validateGeneration() {
      try {
         weblogic.descriptor.beangen.LegalChecks.checkNonEmptyString("Community", "public");
      } catch (IllegalArgumentException var2) {
         throw new DescriptorValidateException("The default value for the property  is zero-length. Properties annotated with false value on @legalZeroLength or @legalNull  should either have @required/@derivedDefault annotations or have a non-zero-length value on @default annotation. Refer annotation legalZeroLength on property Community in SNMPProxyMBean" + var2.getMessage());
      }

      try {
         weblogic.descriptor.beangen.LegalChecks.checkNonNull("SecurityLevel", "noAuthNoPriv");
      } catch (IllegalArgumentException var1) {
         throw new DescriptorValidateException("The default value for the property  is null. Properties annotated with false value on @legalZeroLength or @legalNull  should either have @required/@derivedDefault annotations or have a non-null value on @default annotation. Refer annotation legalNull on property SecurityLevel in SNMPProxyMBean" + var1.getMessage());
      }
   }

   public static class SchemaHelper2 extends ConfigurationMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 4:
               if (var1.equals("port")) {
                  return 7;
               }
            case 5:
            case 6:
            case 10:
            case 11:
            case 12:
            default:
               break;
            case 7:
               if (var1.equals("timeout")) {
                  return 10;
               }
               break;
            case 8:
               if (var1.equals("oid-root")) {
                  return 8;
               }
               break;
            case 9:
               if (var1.equals("community")) {
                  return 9;
               }
               break;
            case 13:
               if (var1.equals("security-name")) {
                  return 11;
               }
               break;
            case 14:
               if (var1.equals("security-level")) {
                  return 12;
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
               return "port";
            case 8:
               return "oid-root";
            case 9:
               return "community";
            case 10:
               return "timeout";
            case 11:
               return "security-name";
            case 12:
               return "security-level";
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
      private SNMPProxyMBeanImpl bean;

      protected Helper(SNMPProxyMBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 7:
               return "Port";
            case 8:
               return "OidRoot";
            case 9:
               return "Community";
            case 10:
               return "Timeout";
            case 11:
               return "SecurityName";
            case 12:
               return "SecurityLevel";
            default:
               return super.getPropertyName(var1);
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("Community")) {
            return 9;
         } else if (var1.equals("OidRoot")) {
            return 8;
         } else if (var1.equals("Port")) {
            return 7;
         } else if (var1.equals("SecurityLevel")) {
            return 12;
         } else if (var1.equals("SecurityName")) {
            return 11;
         } else {
            return var1.equals("Timeout") ? 10 : super.getPropertyIndex(var1);
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
            if (this.bean.isCommunitySet()) {
               var2.append("Community");
               var2.append(String.valueOf(this.bean.getCommunity()));
            }

            if (this.bean.isOidRootSet()) {
               var2.append("OidRoot");
               var2.append(String.valueOf(this.bean.getOidRoot()));
            }

            if (this.bean.isPortSet()) {
               var2.append("Port");
               var2.append(String.valueOf(this.bean.getPort()));
            }

            if (this.bean.isSecurityLevelSet()) {
               var2.append("SecurityLevel");
               var2.append(String.valueOf(this.bean.getSecurityLevel()));
            }

            if (this.bean.isSecurityNameSet()) {
               var2.append("SecurityName");
               var2.append(String.valueOf(this.bean.getSecurityName()));
            }

            if (this.bean.isTimeoutSet()) {
               var2.append("Timeout");
               var2.append(String.valueOf(this.bean.getTimeout()));
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
            SNMPProxyMBeanImpl var2 = (SNMPProxyMBeanImpl)var1;
            this.computeDiff("Community", this.bean.getCommunity(), var2.getCommunity(), true);
            this.computeDiff("OidRoot", this.bean.getOidRoot(), var2.getOidRoot(), true);
            this.computeDiff("Port", this.bean.getPort(), var2.getPort(), true);
            this.computeDiff("SecurityLevel", this.bean.getSecurityLevel(), var2.getSecurityLevel(), true);
            this.computeDiff("SecurityName", this.bean.getSecurityName(), var2.getSecurityName(), true);
            this.computeDiff("Timeout", this.bean.getTimeout(), var2.getTimeout(), true);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            SNMPProxyMBeanImpl var3 = (SNMPProxyMBeanImpl)var1.getSourceBean();
            SNMPProxyMBeanImpl var4 = (SNMPProxyMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("Community")) {
                  var3.setCommunity(var4.getCommunity());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 9);
               } else if (var5.equals("OidRoot")) {
                  var3.setOidRoot(var4.getOidRoot());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 8);
               } else if (var5.equals("Port")) {
                  var3.setPort(var4.getPort());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 7);
               } else if (var5.equals("SecurityLevel")) {
                  var3.setSecurityLevel(var4.getSecurityLevel());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 12);
               } else if (var5.equals("SecurityName")) {
                  var3.setSecurityName(var4.getSecurityName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 11);
               } else if (var5.equals("Timeout")) {
                  var3.setTimeout(var4.getTimeout());
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
            SNMPProxyMBeanImpl var5 = (SNMPProxyMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("Community")) && this.bean.isCommunitySet()) {
               var5.setCommunity(this.bean.getCommunity());
            }

            if ((var3 == null || !var3.contains("OidRoot")) && this.bean.isOidRootSet()) {
               var5.setOidRoot(this.bean.getOidRoot());
            }

            if ((var3 == null || !var3.contains("Port")) && this.bean.isPortSet()) {
               var5.setPort(this.bean.getPort());
            }

            if ((var3 == null || !var3.contains("SecurityLevel")) && this.bean.isSecurityLevelSet()) {
               var5.setSecurityLevel(this.bean.getSecurityLevel());
            }

            if ((var3 == null || !var3.contains("SecurityName")) && this.bean.isSecurityNameSet()) {
               var5.setSecurityName(this.bean.getSecurityName());
            }

            if ((var3 == null || !var3.contains("Timeout")) && this.bean.isTimeoutSet()) {
               var5.setTimeout(this.bean.getTimeout());
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
