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
import weblogic.management.DistributedManagementException;
import weblogic.utils.collections.CombinedIterator;

public class IIOPMBeanImpl extends ConfigurationMBeanImpl implements IIOPMBean, Serializable {
   private int _CompleteMessageTimeout;
   private String _DefaultCharCodeset;
   private int _DefaultMinorVersion;
   private String _DefaultWideCharCodeset;
   private boolean _EnableIORServlet;
   private int _IdleConnectionTimeout;
   private String _LocationForwardPolicy;
   private int _MaxMessageSize;
   private String _SystemSecurity;
   private String _TxMechanism;
   private boolean _UseFullRepositoryIdList;
   private boolean _UseJavaSerialization;
   private boolean _UseLocateRequest;
   private boolean _UseSerialFormatVersion2;
   private boolean _UseStatefulAuthentication;
   private static SchemaHelper2 _schemaHelper;

   public IIOPMBeanImpl() {
      this._initializeProperty(-1);
   }

   public IIOPMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);
      this._initializeProperty(-1);
   }

   public int getIdleConnectionTimeout() {
      return this._IdleConnectionTimeout;
   }

   public boolean isIdleConnectionTimeoutSet() {
      return this._isSet(7);
   }

   public void setIdleConnectionTimeout(int var1) throws InvalidAttributeValueException, DistributedManagementException {
      weblogic.descriptor.beangen.LegalChecks.checkMin("IdleConnectionTimeout", var1, -1);
      int var2 = this._IdleConnectionTimeout;
      this._IdleConnectionTimeout = var1;
      this._postSet(7, var2, var1);
   }

   public int getCompleteMessageTimeout() {
      return this._CompleteMessageTimeout;
   }

   public boolean isCompleteMessageTimeoutSet() {
      return this._isSet(8);
   }

   public void setCompleteMessageTimeout(int var1) throws InvalidAttributeValueException, DistributedManagementException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("CompleteMessageTimeout", (long)var1, -1L, 480L);
      int var2 = this._CompleteMessageTimeout;
      this._CompleteMessageTimeout = var1;
      this._postSet(8, var2, var1);
   }

   public int getMaxMessageSize() {
      return this._MaxMessageSize;
   }

   public boolean isMaxMessageSizeSet() {
      return this._isSet(9);
   }

   public void setMaxMessageSize(int var1) throws InvalidAttributeValueException, DistributedManagementException {
      IIOPValidator.validateMaxMessageSize(var1);
      int var2 = this._MaxMessageSize;
      this._MaxMessageSize = var1;
      this._postSet(9, var2, var1);
   }

   public int getDefaultMinorVersion() {
      return this._DefaultMinorVersion;
   }

   public boolean isDefaultMinorVersionSet() {
      return this._isSet(10);
   }

   public void setDefaultMinorVersion(int var1) throws InvalidAttributeValueException, DistributedManagementException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("DefaultMinorVersion", (long)var1, 0L, 2L);
      int var2 = this._DefaultMinorVersion;
      this._DefaultMinorVersion = var1;
      this._postSet(10, var2, var1);
   }

   public boolean getUseLocateRequest() {
      return this._UseLocateRequest;
   }

   public boolean isUseLocateRequestSet() {
      return this._isSet(11);
   }

   public void setUseLocateRequest(boolean var1) throws InvalidAttributeValueException, DistributedManagementException {
      boolean var2 = this._UseLocateRequest;
      this._UseLocateRequest = var1;
      this._postSet(11, var2, var1);
   }

   public String getTxMechanism() {
      return this._TxMechanism;
   }

   public boolean isTxMechanismSet() {
      return this._isSet(12);
   }

   public void setTxMechanism(String var1) throws InvalidAttributeValueException, DistributedManagementException {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{"OTS", "JTA", "OTSv11", "none"};
      var1 = weblogic.descriptor.beangen.LegalChecks.checkInEnum("TxMechanism", var1, var2);
      String var3 = this._TxMechanism;
      this._TxMechanism = var1;
      this._postSet(12, var3, var1);
   }

   public String getLocationForwardPolicy() {
      return this._LocationForwardPolicy;
   }

   public boolean isLocationForwardPolicySet() {
      return this._isSet(13);
   }

   public void setLocationForwardPolicy(String var1) throws InvalidAttributeValueException, DistributedManagementException {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{"off", "failover", "round-robin", "random"};
      var1 = weblogic.descriptor.beangen.LegalChecks.checkInEnum("LocationForwardPolicy", var1, var2);
      String var3 = this._LocationForwardPolicy;
      this._LocationForwardPolicy = var1;
      this._postSet(13, var3, var1);
   }

   public String getDefaultWideCharCodeset() {
      return this._DefaultWideCharCodeset;
   }

   public boolean isDefaultWideCharCodesetSet() {
      return this._isSet(14);
   }

   public void setDefaultWideCharCodeset(String var1) throws InvalidAttributeValueException, DistributedManagementException {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{"UCS-2", "UTF-16", "UTF-8", "UTF-16BE", "UTF-16LE"};
      var1 = weblogic.descriptor.beangen.LegalChecks.checkInEnum("DefaultWideCharCodeset", var1, var2);
      String var3 = this._DefaultWideCharCodeset;
      this._DefaultWideCharCodeset = var1;
      this._postSet(14, var3, var1);
   }

   public String getDefaultCharCodeset() {
      return this._DefaultCharCodeset;
   }

   public boolean isDefaultCharCodesetSet() {
      return this._isSet(15);
   }

   public void setDefaultCharCodeset(String var1) throws InvalidAttributeValueException, DistributedManagementException {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{"US-ASCII", "UTF-8", "ISO-8859-1"};
      var1 = weblogic.descriptor.beangen.LegalChecks.checkInEnum("DefaultCharCodeset", var1, var2);
      String var3 = this._DefaultCharCodeset;
      this._DefaultCharCodeset = var1;
      this._postSet(15, var3, var1);
   }

   public boolean getUseFullRepositoryIdList() {
      return this._UseFullRepositoryIdList;
   }

   public boolean isUseFullRepositoryIdListSet() {
      return this._isSet(16);
   }

   public void setUseFullRepositoryIdList(boolean var1) throws InvalidAttributeValueException, DistributedManagementException {
      boolean var2 = this._UseFullRepositoryIdList;
      this._UseFullRepositoryIdList = var1;
      this._postSet(16, var2, var1);
   }

   public boolean getUseStatefulAuthentication() {
      return this._UseStatefulAuthentication;
   }

   public boolean isUseStatefulAuthenticationSet() {
      return this._isSet(17);
   }

   public void setUseStatefulAuthentication(boolean var1) throws InvalidAttributeValueException, DistributedManagementException {
      boolean var2 = this._UseStatefulAuthentication;
      this._UseStatefulAuthentication = var1;
      this._postSet(17, var2, var1);
   }

   public boolean getUseSerialFormatVersion2() {
      return this._UseSerialFormatVersion2;
   }

   public boolean isUseSerialFormatVersion2Set() {
      return this._isSet(18);
   }

   public void setUseSerialFormatVersion2(boolean var1) throws InvalidAttributeValueException, DistributedManagementException {
      boolean var2 = this._UseSerialFormatVersion2;
      this._UseSerialFormatVersion2 = var1;
      this._postSet(18, var2, var1);
   }

   public boolean getEnableIORServlet() {
      return this._EnableIORServlet;
   }

   public boolean isEnableIORServletSet() {
      return this._isSet(19);
   }

   public void setEnableIORServlet(boolean var1) {
      boolean var2 = this._EnableIORServlet;
      this._EnableIORServlet = var1;
      this._postSet(19, var2, var1);
   }

   public boolean getUseJavaSerialization() {
      return this._UseJavaSerialization;
   }

   public boolean isUseJavaSerializationSet() {
      return this._isSet(20);
   }

   public void setUseJavaSerialization(boolean var1) throws InvalidAttributeValueException, DistributedManagementException {
      boolean var2 = this._UseJavaSerialization;
      this._UseJavaSerialization = var1;
      this._postSet(20, var2, var1);
   }

   public String getSystemSecurity() {
      return this._SystemSecurity;
   }

   public boolean isSystemSecuritySet() {
      return this._isSet(21);
   }

   public void setSystemSecurity(String var1) throws InvalidAttributeValueException, DistributedManagementException {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{"none", "supported", "required"};
      var1 = weblogic.descriptor.beangen.LegalChecks.checkInEnum("SystemSecurity", var1, var2);
      String var3 = this._SystemSecurity;
      this._SystemSecurity = var1;
      this._postSet(21, var3, var1);
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
               this._CompleteMessageTimeout = -1;
               if (var2) {
                  break;
               }
            case 15:
               this._DefaultCharCodeset = "US-ASCII";
               if (var2) {
                  break;
               }
            case 10:
               this._DefaultMinorVersion = 2;
               if (var2) {
                  break;
               }
            case 14:
               this._DefaultWideCharCodeset = "UCS-2";
               if (var2) {
                  break;
               }
            case 19:
               this._EnableIORServlet = false;
               if (var2) {
                  break;
               }
            case 7:
               this._IdleConnectionTimeout = -1;
               if (var2) {
                  break;
               }
            case 13:
               this._LocationForwardPolicy = "off";
               if (var2) {
                  break;
               }
            case 9:
               this._MaxMessageSize = -1;
               if (var2) {
                  break;
               }
            case 21:
               this._SystemSecurity = "supported";
               if (var2) {
                  break;
               }
            case 12:
               this._TxMechanism = "OTS";
               if (var2) {
                  break;
               }
            case 16:
               this._UseFullRepositoryIdList = false;
               if (var2) {
                  break;
               }
            case 20:
               this._UseJavaSerialization = false;
               if (var2) {
                  break;
               }
            case 11:
               this._UseLocateRequest = false;
               if (var2) {
                  break;
               }
            case 18:
               this._UseSerialFormatVersion2 = false;
               if (var2) {
                  break;
               }
            case 17:
               this._UseStatefulAuthentication = true;
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
      return "IIOP";
   }

   public void putValue(String var1, Object var2) {
      int var5;
      if (var1.equals("CompleteMessageTimeout")) {
         var5 = this._CompleteMessageTimeout;
         this._CompleteMessageTimeout = (Integer)var2;
         this._postSet(8, var5, this._CompleteMessageTimeout);
      } else {
         String var4;
         if (var1.equals("DefaultCharCodeset")) {
            var4 = this._DefaultCharCodeset;
            this._DefaultCharCodeset = (String)var2;
            this._postSet(15, var4, this._DefaultCharCodeset);
         } else if (var1.equals("DefaultMinorVersion")) {
            var5 = this._DefaultMinorVersion;
            this._DefaultMinorVersion = (Integer)var2;
            this._postSet(10, var5, this._DefaultMinorVersion);
         } else if (var1.equals("DefaultWideCharCodeset")) {
            var4 = this._DefaultWideCharCodeset;
            this._DefaultWideCharCodeset = (String)var2;
            this._postSet(14, var4, this._DefaultWideCharCodeset);
         } else {
            boolean var3;
            if (var1.equals("EnableIORServlet")) {
               var3 = this._EnableIORServlet;
               this._EnableIORServlet = (Boolean)var2;
               this._postSet(19, var3, this._EnableIORServlet);
            } else if (var1.equals("IdleConnectionTimeout")) {
               var5 = this._IdleConnectionTimeout;
               this._IdleConnectionTimeout = (Integer)var2;
               this._postSet(7, var5, this._IdleConnectionTimeout);
            } else if (var1.equals("LocationForwardPolicy")) {
               var4 = this._LocationForwardPolicy;
               this._LocationForwardPolicy = (String)var2;
               this._postSet(13, var4, this._LocationForwardPolicy);
            } else if (var1.equals("MaxMessageSize")) {
               var5 = this._MaxMessageSize;
               this._MaxMessageSize = (Integer)var2;
               this._postSet(9, var5, this._MaxMessageSize);
            } else if (var1.equals("SystemSecurity")) {
               var4 = this._SystemSecurity;
               this._SystemSecurity = (String)var2;
               this._postSet(21, var4, this._SystemSecurity);
            } else if (var1.equals("TxMechanism")) {
               var4 = this._TxMechanism;
               this._TxMechanism = (String)var2;
               this._postSet(12, var4, this._TxMechanism);
            } else if (var1.equals("UseFullRepositoryIdList")) {
               var3 = this._UseFullRepositoryIdList;
               this._UseFullRepositoryIdList = (Boolean)var2;
               this._postSet(16, var3, this._UseFullRepositoryIdList);
            } else if (var1.equals("UseJavaSerialization")) {
               var3 = this._UseJavaSerialization;
               this._UseJavaSerialization = (Boolean)var2;
               this._postSet(20, var3, this._UseJavaSerialization);
            } else if (var1.equals("UseLocateRequest")) {
               var3 = this._UseLocateRequest;
               this._UseLocateRequest = (Boolean)var2;
               this._postSet(11, var3, this._UseLocateRequest);
            } else if (var1.equals("UseSerialFormatVersion2")) {
               var3 = this._UseSerialFormatVersion2;
               this._UseSerialFormatVersion2 = (Boolean)var2;
               this._postSet(18, var3, this._UseSerialFormatVersion2);
            } else if (var1.equals("UseStatefulAuthentication")) {
               var3 = this._UseStatefulAuthentication;
               this._UseStatefulAuthentication = (Boolean)var2;
               this._postSet(17, var3, this._UseStatefulAuthentication);
            } else {
               super.putValue(var1, var2);
            }
         }
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("CompleteMessageTimeout")) {
         return new Integer(this._CompleteMessageTimeout);
      } else if (var1.equals("DefaultCharCodeset")) {
         return this._DefaultCharCodeset;
      } else if (var1.equals("DefaultMinorVersion")) {
         return new Integer(this._DefaultMinorVersion);
      } else if (var1.equals("DefaultWideCharCodeset")) {
         return this._DefaultWideCharCodeset;
      } else if (var1.equals("EnableIORServlet")) {
         return new Boolean(this._EnableIORServlet);
      } else if (var1.equals("IdleConnectionTimeout")) {
         return new Integer(this._IdleConnectionTimeout);
      } else if (var1.equals("LocationForwardPolicy")) {
         return this._LocationForwardPolicy;
      } else if (var1.equals("MaxMessageSize")) {
         return new Integer(this._MaxMessageSize);
      } else if (var1.equals("SystemSecurity")) {
         return this._SystemSecurity;
      } else if (var1.equals("TxMechanism")) {
         return this._TxMechanism;
      } else if (var1.equals("UseFullRepositoryIdList")) {
         return new Boolean(this._UseFullRepositoryIdList);
      } else if (var1.equals("UseJavaSerialization")) {
         return new Boolean(this._UseJavaSerialization);
      } else if (var1.equals("UseLocateRequest")) {
         return new Boolean(this._UseLocateRequest);
      } else if (var1.equals("UseSerialFormatVersion2")) {
         return new Boolean(this._UseSerialFormatVersion2);
      } else {
         return var1.equals("UseStatefulAuthentication") ? new Boolean(this._UseStatefulAuthentication) : super.getValue(var1);
      }
   }

   public static class SchemaHelper2 extends ConfigurationMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 12:
               if (var1.equals("tx-mechanism")) {
                  return 12;
               }
            case 13:
            case 14:
            case 19:
            default:
               break;
            case 15:
               if (var1.equals("system-security")) {
                  return 21;
               }
               break;
            case 16:
               if (var1.equals("max-message-size")) {
                  return 9;
               }
               break;
            case 17:
               if (var1.equals("enableior-servlet")) {
                  return 19;
               }
               break;
            case 18:
               if (var1.equals("use-locate-request")) {
                  return 11;
               }
               break;
            case 20:
               if (var1.equals("default-char-codeset")) {
                  return 15;
               }
               break;
            case 21:
               if (var1.equals("default-minor-version")) {
                  return 10;
               }
               break;
            case 22:
               if (var1.equals("use-java-serialization")) {
                  return 20;
               }
               break;
            case 23:
               if (var1.equals("idle-connection-timeout")) {
                  return 7;
               }

               if (var1.equals("location-forward-policy")) {
                  return 13;
               }
               break;
            case 24:
               if (var1.equals("complete-message-timeout")) {
                  return 8;
               }
               break;
            case 25:
               if (var1.equals("default-wide-char-codeset")) {
                  return 14;
               }
               break;
            case 26:
               if (var1.equals("use-serial-format-version2")) {
                  return 18;
               }
               break;
            case 27:
               if (var1.equals("use-full-repository-id-list")) {
                  return 16;
               }

               if (var1.equals("use-stateful-authentication")) {
                  return 17;
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
               return "idle-connection-timeout";
            case 8:
               return "complete-message-timeout";
            case 9:
               return "max-message-size";
            case 10:
               return "default-minor-version";
            case 11:
               return "use-locate-request";
            case 12:
               return "tx-mechanism";
            case 13:
               return "location-forward-policy";
            case 14:
               return "default-wide-char-codeset";
            case 15:
               return "default-char-codeset";
            case 16:
               return "use-full-repository-id-list";
            case 17:
               return "use-stateful-authentication";
            case 18:
               return "use-serial-format-version2";
            case 19:
               return "enableior-servlet";
            case 20:
               return "use-java-serialization";
            case 21:
               return "system-security";
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
            case 9:
               return true;
            case 10:
               return true;
            case 11:
               return true;
            case 12:
               return true;
            case 13:
               return true;
            case 14:
            default:
               return super.isConfigurable(var1);
            case 15:
               return true;
            case 16:
               return true;
            case 17:
               return true;
            case 18:
               return true;
            case 19:
               return true;
            case 20:
               return true;
            case 21:
               return true;
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
      private IIOPMBeanImpl bean;

      protected Helper(IIOPMBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 7:
               return "IdleConnectionTimeout";
            case 8:
               return "CompleteMessageTimeout";
            case 9:
               return "MaxMessageSize";
            case 10:
               return "DefaultMinorVersion";
            case 11:
               return "UseLocateRequest";
            case 12:
               return "TxMechanism";
            case 13:
               return "LocationForwardPolicy";
            case 14:
               return "DefaultWideCharCodeset";
            case 15:
               return "DefaultCharCodeset";
            case 16:
               return "UseFullRepositoryIdList";
            case 17:
               return "UseStatefulAuthentication";
            case 18:
               return "UseSerialFormatVersion2";
            case 19:
               return "EnableIORServlet";
            case 20:
               return "UseJavaSerialization";
            case 21:
               return "SystemSecurity";
            default:
               return super.getPropertyName(var1);
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("CompleteMessageTimeout")) {
            return 8;
         } else if (var1.equals("DefaultCharCodeset")) {
            return 15;
         } else if (var1.equals("DefaultMinorVersion")) {
            return 10;
         } else if (var1.equals("DefaultWideCharCodeset")) {
            return 14;
         } else if (var1.equals("EnableIORServlet")) {
            return 19;
         } else if (var1.equals("IdleConnectionTimeout")) {
            return 7;
         } else if (var1.equals("LocationForwardPolicy")) {
            return 13;
         } else if (var1.equals("MaxMessageSize")) {
            return 9;
         } else if (var1.equals("SystemSecurity")) {
            return 21;
         } else if (var1.equals("TxMechanism")) {
            return 12;
         } else if (var1.equals("UseFullRepositoryIdList")) {
            return 16;
         } else if (var1.equals("UseJavaSerialization")) {
            return 20;
         } else if (var1.equals("UseLocateRequest")) {
            return 11;
         } else if (var1.equals("UseSerialFormatVersion2")) {
            return 18;
         } else {
            return var1.equals("UseStatefulAuthentication") ? 17 : super.getPropertyIndex(var1);
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
            if (this.bean.isCompleteMessageTimeoutSet()) {
               var2.append("CompleteMessageTimeout");
               var2.append(String.valueOf(this.bean.getCompleteMessageTimeout()));
            }

            if (this.bean.isDefaultCharCodesetSet()) {
               var2.append("DefaultCharCodeset");
               var2.append(String.valueOf(this.bean.getDefaultCharCodeset()));
            }

            if (this.bean.isDefaultMinorVersionSet()) {
               var2.append("DefaultMinorVersion");
               var2.append(String.valueOf(this.bean.getDefaultMinorVersion()));
            }

            if (this.bean.isDefaultWideCharCodesetSet()) {
               var2.append("DefaultWideCharCodeset");
               var2.append(String.valueOf(this.bean.getDefaultWideCharCodeset()));
            }

            if (this.bean.isEnableIORServletSet()) {
               var2.append("EnableIORServlet");
               var2.append(String.valueOf(this.bean.getEnableIORServlet()));
            }

            if (this.bean.isIdleConnectionTimeoutSet()) {
               var2.append("IdleConnectionTimeout");
               var2.append(String.valueOf(this.bean.getIdleConnectionTimeout()));
            }

            if (this.bean.isLocationForwardPolicySet()) {
               var2.append("LocationForwardPolicy");
               var2.append(String.valueOf(this.bean.getLocationForwardPolicy()));
            }

            if (this.bean.isMaxMessageSizeSet()) {
               var2.append("MaxMessageSize");
               var2.append(String.valueOf(this.bean.getMaxMessageSize()));
            }

            if (this.bean.isSystemSecuritySet()) {
               var2.append("SystemSecurity");
               var2.append(String.valueOf(this.bean.getSystemSecurity()));
            }

            if (this.bean.isTxMechanismSet()) {
               var2.append("TxMechanism");
               var2.append(String.valueOf(this.bean.getTxMechanism()));
            }

            if (this.bean.isUseFullRepositoryIdListSet()) {
               var2.append("UseFullRepositoryIdList");
               var2.append(String.valueOf(this.bean.getUseFullRepositoryIdList()));
            }

            if (this.bean.isUseJavaSerializationSet()) {
               var2.append("UseJavaSerialization");
               var2.append(String.valueOf(this.bean.getUseJavaSerialization()));
            }

            if (this.bean.isUseLocateRequestSet()) {
               var2.append("UseLocateRequest");
               var2.append(String.valueOf(this.bean.getUseLocateRequest()));
            }

            if (this.bean.isUseSerialFormatVersion2Set()) {
               var2.append("UseSerialFormatVersion2");
               var2.append(String.valueOf(this.bean.getUseSerialFormatVersion2()));
            }

            if (this.bean.isUseStatefulAuthenticationSet()) {
               var2.append("UseStatefulAuthentication");
               var2.append(String.valueOf(this.bean.getUseStatefulAuthentication()));
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
            IIOPMBeanImpl var2 = (IIOPMBeanImpl)var1;
            this.computeDiff("CompleteMessageTimeout", this.bean.getCompleteMessageTimeout(), var2.getCompleteMessageTimeout(), true);
            this.computeDiff("DefaultCharCodeset", this.bean.getDefaultCharCodeset(), var2.getDefaultCharCodeset(), false);
            this.computeDiff("DefaultMinorVersion", this.bean.getDefaultMinorVersion(), var2.getDefaultMinorVersion(), false);
            this.computeDiff("DefaultWideCharCodeset", this.bean.getDefaultWideCharCodeset(), var2.getDefaultWideCharCodeset(), false);
            this.computeDiff("EnableIORServlet", this.bean.getEnableIORServlet(), var2.getEnableIORServlet(), false);
            this.computeDiff("IdleConnectionTimeout", this.bean.getIdleConnectionTimeout(), var2.getIdleConnectionTimeout(), true);
            this.computeDiff("LocationForwardPolicy", this.bean.getLocationForwardPolicy(), var2.getLocationForwardPolicy(), false);
            this.computeDiff("MaxMessageSize", this.bean.getMaxMessageSize(), var2.getMaxMessageSize(), false);
            this.computeDiff("SystemSecurity", this.bean.getSystemSecurity(), var2.getSystemSecurity(), false);
            this.computeDiff("TxMechanism", this.bean.getTxMechanism(), var2.getTxMechanism(), false);
            this.computeDiff("UseFullRepositoryIdList", this.bean.getUseFullRepositoryIdList(), var2.getUseFullRepositoryIdList(), false);
            this.computeDiff("UseJavaSerialization", this.bean.getUseJavaSerialization(), var2.getUseJavaSerialization(), true);
            this.computeDiff("UseLocateRequest", this.bean.getUseLocateRequest(), var2.getUseLocateRequest(), false);
            this.computeDiff("UseSerialFormatVersion2", this.bean.getUseSerialFormatVersion2(), var2.getUseSerialFormatVersion2(), false);
            this.computeDiff("UseStatefulAuthentication", this.bean.getUseStatefulAuthentication(), var2.getUseStatefulAuthentication(), true);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            IIOPMBeanImpl var3 = (IIOPMBeanImpl)var1.getSourceBean();
            IIOPMBeanImpl var4 = (IIOPMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("CompleteMessageTimeout")) {
                  var3.setCompleteMessageTimeout(var4.getCompleteMessageTimeout());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 8);
               } else if (var5.equals("DefaultCharCodeset")) {
                  var3.setDefaultCharCodeset(var4.getDefaultCharCodeset());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 15);
               } else if (var5.equals("DefaultMinorVersion")) {
                  var3.setDefaultMinorVersion(var4.getDefaultMinorVersion());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 10);
               } else if (var5.equals("DefaultWideCharCodeset")) {
                  var3.setDefaultWideCharCodeset(var4.getDefaultWideCharCodeset());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 14);
               } else if (var5.equals("EnableIORServlet")) {
                  var3.setEnableIORServlet(var4.getEnableIORServlet());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 19);
               } else if (var5.equals("IdleConnectionTimeout")) {
                  var3.setIdleConnectionTimeout(var4.getIdleConnectionTimeout());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 7);
               } else if (var5.equals("LocationForwardPolicy")) {
                  var3.setLocationForwardPolicy(var4.getLocationForwardPolicy());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 13);
               } else if (var5.equals("MaxMessageSize")) {
                  var3.setMaxMessageSize(var4.getMaxMessageSize());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 9);
               } else if (var5.equals("SystemSecurity")) {
                  var3.setSystemSecurity(var4.getSystemSecurity());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 21);
               } else if (var5.equals("TxMechanism")) {
                  var3.setTxMechanism(var4.getTxMechanism());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 12);
               } else if (var5.equals("UseFullRepositoryIdList")) {
                  var3.setUseFullRepositoryIdList(var4.getUseFullRepositoryIdList());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 16);
               } else if (var5.equals("UseJavaSerialization")) {
                  var3.setUseJavaSerialization(var4.getUseJavaSerialization());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 20);
               } else if (var5.equals("UseLocateRequest")) {
                  var3.setUseLocateRequest(var4.getUseLocateRequest());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 11);
               } else if (var5.equals("UseSerialFormatVersion2")) {
                  var3.setUseSerialFormatVersion2(var4.getUseSerialFormatVersion2());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 18);
               } else if (var5.equals("UseStatefulAuthentication")) {
                  var3.setUseStatefulAuthentication(var4.getUseStatefulAuthentication());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 17);
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
            IIOPMBeanImpl var5 = (IIOPMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("CompleteMessageTimeout")) && this.bean.isCompleteMessageTimeoutSet()) {
               var5.setCompleteMessageTimeout(this.bean.getCompleteMessageTimeout());
            }

            if ((var3 == null || !var3.contains("DefaultCharCodeset")) && this.bean.isDefaultCharCodesetSet()) {
               var5.setDefaultCharCodeset(this.bean.getDefaultCharCodeset());
            }

            if ((var3 == null || !var3.contains("DefaultMinorVersion")) && this.bean.isDefaultMinorVersionSet()) {
               var5.setDefaultMinorVersion(this.bean.getDefaultMinorVersion());
            }

            if ((var3 == null || !var3.contains("DefaultWideCharCodeset")) && this.bean.isDefaultWideCharCodesetSet()) {
               var5.setDefaultWideCharCodeset(this.bean.getDefaultWideCharCodeset());
            }

            if ((var3 == null || !var3.contains("EnableIORServlet")) && this.bean.isEnableIORServletSet()) {
               var5.setEnableIORServlet(this.bean.getEnableIORServlet());
            }

            if ((var3 == null || !var3.contains("IdleConnectionTimeout")) && this.bean.isIdleConnectionTimeoutSet()) {
               var5.setIdleConnectionTimeout(this.bean.getIdleConnectionTimeout());
            }

            if ((var3 == null || !var3.contains("LocationForwardPolicy")) && this.bean.isLocationForwardPolicySet()) {
               var5.setLocationForwardPolicy(this.bean.getLocationForwardPolicy());
            }

            if ((var3 == null || !var3.contains("MaxMessageSize")) && this.bean.isMaxMessageSizeSet()) {
               var5.setMaxMessageSize(this.bean.getMaxMessageSize());
            }

            if ((var3 == null || !var3.contains("SystemSecurity")) && this.bean.isSystemSecuritySet()) {
               var5.setSystemSecurity(this.bean.getSystemSecurity());
            }

            if ((var3 == null || !var3.contains("TxMechanism")) && this.bean.isTxMechanismSet()) {
               var5.setTxMechanism(this.bean.getTxMechanism());
            }

            if ((var3 == null || !var3.contains("UseFullRepositoryIdList")) && this.bean.isUseFullRepositoryIdListSet()) {
               var5.setUseFullRepositoryIdList(this.bean.getUseFullRepositoryIdList());
            }

            if ((var3 == null || !var3.contains("UseJavaSerialization")) && this.bean.isUseJavaSerializationSet()) {
               var5.setUseJavaSerialization(this.bean.getUseJavaSerialization());
            }

            if ((var3 == null || !var3.contains("UseLocateRequest")) && this.bean.isUseLocateRequestSet()) {
               var5.setUseLocateRequest(this.bean.getUseLocateRequest());
            }

            if ((var3 == null || !var3.contains("UseSerialFormatVersion2")) && this.bean.isUseSerialFormatVersion2Set()) {
               var5.setUseSerialFormatVersion2(this.bean.getUseSerialFormatVersion2());
            }

            if ((var3 == null || !var3.contains("UseStatefulAuthentication")) && this.bean.isUseStatefulAuthenticationSet()) {
               var5.setUseStatefulAuthentication(this.bean.getUseStatefulAuthentication());
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
