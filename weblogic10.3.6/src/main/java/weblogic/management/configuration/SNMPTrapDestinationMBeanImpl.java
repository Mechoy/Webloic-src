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

public class SNMPTrapDestinationMBeanImpl extends ConfigurationMBeanImpl implements SNMPTrapDestinationMBean, Serializable {
   private String _Community;
   private String _Host;
   private int _Port;
   private String _SecurityLevel;
   private String _SecurityName;
   private static SchemaHelper2 _schemaHelper;

   public SNMPTrapDestinationMBeanImpl() {
      this._initializeProperty(-1);
   }

   public SNMPTrapDestinationMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);
      this._initializeProperty(-1);
   }

   public String getHost() {
      return this._Host;
   }

   public boolean isHostSet() {
      return this._isSet(7);
   }

   public void setHost(String var1) throws InvalidAttributeValueException, ConfigurationException {
      var1 = var1 == null ? null : var1.trim();
      weblogic.descriptor.beangen.LegalChecks.checkNonEmptyString("Host", var1);
      String var2 = this._Host;
      this._Host = var1;
      this._postSet(7, var2, var1);
   }

   public int getPort() {
      return this._Port;
   }

   public boolean isPortSet() {
      return this._isSet(8);
   }

   public void setPort(int var1) throws InvalidAttributeValueException, ConfigurationException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("Port", (long)var1, 1L, 65535L);
      int var2 = this._Port;
      this._Port = var1;
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

   public String getSecurityName() {
      return this._SecurityName;
   }

   public boolean isSecurityNameSet() {
      return this._isSet(10);
   }

   public void setSecurityName(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._SecurityName;
      this._SecurityName = var1;
      this._postSet(10, var2, var1);
   }

   public String getSecurityLevel() {
      if (!this._isSet(11)) {
         return this._isProductionModeEnabled() ? "authNoPriv" : "noAuthNoPriv";
      } else {
         return this._SecurityLevel;
      }
   }

   public boolean isSecurityLevelSet() {
      return this._isSet(11);
   }

   public void setSecurityLevel(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{"noAuthNoPriv", "authNoPriv", "authPriv"};
      var1 = weblogic.descriptor.beangen.LegalChecks.checkInEnum("SecurityLevel", var1, var2);
      weblogic.descriptor.beangen.LegalChecks.checkNonNull("SecurityLevel", var1);
      String var3 = this._SecurityLevel;
      this._SecurityLevel = var1;
      this._postSet(11, var3, var1);
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
               this._Community = "public";
               if (var2) {
                  break;
               }
            case 7:
               this._Host = "localhost";
               if (var2) {
                  break;
               }
            case 8:
               this._Port = 162;
               if (var2) {
                  break;
               }
            case 11:
               this._SecurityLevel = "noAuthNoPriv";
               if (var2) {
                  break;
               }
            case 10:
               this._SecurityName = null;
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
      return "SNMPTrapDestination";
   }

   public void putValue(String var1, Object var2) {
      String var3;
      if (var1.equals("Community")) {
         var3 = this._Community;
         this._Community = (String)var2;
         this._postSet(9, var3, this._Community);
      } else if (var1.equals("Host")) {
         var3 = this._Host;
         this._Host = (String)var2;
         this._postSet(7, var3, this._Host);
      } else if (var1.equals("Port")) {
         int var4 = this._Port;
         this._Port = (Integer)var2;
         this._postSet(8, var4, this._Port);
      } else if (var1.equals("SecurityLevel")) {
         var3 = this._SecurityLevel;
         this._SecurityLevel = (String)var2;
         this._postSet(11, var3, this._SecurityLevel);
      } else if (var1.equals("SecurityName")) {
         var3 = this._SecurityName;
         this._SecurityName = (String)var2;
         this._postSet(10, var3, this._SecurityName);
      } else {
         super.putValue(var1, var2);
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("Community")) {
         return this._Community;
      } else if (var1.equals("Host")) {
         return this._Host;
      } else if (var1.equals("Port")) {
         return new Integer(this._Port);
      } else if (var1.equals("SecurityLevel")) {
         return this._SecurityLevel;
      } else {
         return var1.equals("SecurityName") ? this._SecurityName : super.getValue(var1);
      }
   }

   public static void validateGeneration() {
      try {
         weblogic.descriptor.beangen.LegalChecks.checkNonEmptyString("Community", "public");
      } catch (IllegalArgumentException var3) {
         throw new DescriptorValidateException("The default value for the property  is zero-length. Properties annotated with false value on @legalZeroLength or @legalNull  should either have @required/@derivedDefault annotations or have a non-zero-length value on @default annotation. Refer annotation legalZeroLength on property Community in SNMPTrapDestinationMBean" + var3.getMessage());
      }

      try {
         weblogic.descriptor.beangen.LegalChecks.checkNonEmptyString("Host", "localhost");
      } catch (IllegalArgumentException var2) {
         throw new DescriptorValidateException("The default value for the property  is zero-length. Properties annotated with false value on @legalZeroLength or @legalNull  should either have @required/@derivedDefault annotations or have a non-zero-length value on @default annotation. Refer annotation legalZeroLength on property Host in SNMPTrapDestinationMBean" + var2.getMessage());
      }

      try {
         weblogic.descriptor.beangen.LegalChecks.checkNonNull("SecurityLevel", "noAuthNoPriv");
      } catch (IllegalArgumentException var1) {
         throw new DescriptorValidateException("The default value for the property  is null. Properties annotated with false value on @legalZeroLength or @legalNull  should either have @required/@derivedDefault annotations or have a non-null value on @default annotation. Refer annotation legalNull on property SecurityLevel in SNMPTrapDestinationMBean" + var1.getMessage());
      }
   }

   public static class SchemaHelper2 extends ConfigurationMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 4:
               if (var1.equals("host")) {
                  return 7;
               }

               if (var1.equals("port")) {
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
                  return 10;
               }
               break;
            case 14:
               if (var1.equals("security-level")) {
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
            case 7:
               return "host";
            case 8:
               return "port";
            case 9:
               return "community";
            case 10:
               return "security-name";
            case 11:
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
      private SNMPTrapDestinationMBeanImpl bean;

      protected Helper(SNMPTrapDestinationMBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 7:
               return "Host";
            case 8:
               return "Port";
            case 9:
               return "Community";
            case 10:
               return "SecurityName";
            case 11:
               return "SecurityLevel";
            default:
               return super.getPropertyName(var1);
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("Community")) {
            return 9;
         } else if (var1.equals("Host")) {
            return 7;
         } else if (var1.equals("Port")) {
            return 8;
         } else if (var1.equals("SecurityLevel")) {
            return 11;
         } else {
            return var1.equals("SecurityName") ? 10 : super.getPropertyIndex(var1);
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

            if (this.bean.isHostSet()) {
               var2.append("Host");
               var2.append(String.valueOf(this.bean.getHost()));
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

            var1.update(var2.toString().getBytes());
            return var1.getValue();
         } catch (Exception var7) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var7);
         }
      }

      protected void computeDiff(AbstractDescriptorBean var1) {
         try {
            super.computeDiff(var1);
            SNMPTrapDestinationMBeanImpl var2 = (SNMPTrapDestinationMBeanImpl)var1;
            this.computeDiff("Community", this.bean.getCommunity(), var2.getCommunity(), true);
            this.computeDiff("Host", this.bean.getHost(), var2.getHost(), true);
            this.computeDiff("Port", this.bean.getPort(), var2.getPort(), true);
            this.computeDiff("SecurityLevel", this.bean.getSecurityLevel(), var2.getSecurityLevel(), true);
            this.computeDiff("SecurityName", this.bean.getSecurityName(), var2.getSecurityName(), true);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            SNMPTrapDestinationMBeanImpl var3 = (SNMPTrapDestinationMBeanImpl)var1.getSourceBean();
            SNMPTrapDestinationMBeanImpl var4 = (SNMPTrapDestinationMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("Community")) {
                  var3.setCommunity(var4.getCommunity());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 9);
               } else if (var5.equals("Host")) {
                  var3.setHost(var4.getHost());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 7);
               } else if (var5.equals("Port")) {
                  var3.setPort(var4.getPort());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 8);
               } else if (var5.equals("SecurityLevel")) {
                  var3.setSecurityLevel(var4.getSecurityLevel());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 11);
               } else if (var5.equals("SecurityName")) {
                  var3.setSecurityName(var4.getSecurityName());
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
            SNMPTrapDestinationMBeanImpl var5 = (SNMPTrapDestinationMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("Community")) && this.bean.isCommunitySet()) {
               var5.setCommunity(this.bean.getCommunity());
            }

            if ((var3 == null || !var3.contains("Host")) && this.bean.isHostSet()) {
               var5.setHost(this.bean.getHost());
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
