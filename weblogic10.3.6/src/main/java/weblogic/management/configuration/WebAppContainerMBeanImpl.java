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

public class WebAppContainerMBeanImpl extends ConfigurationMBeanImpl implements WebAppContainerMBean, Serializable {
   private boolean _AllowAllRoles;
   private boolean _AuthCookieEnabled;
   private boolean _ChangeSessionIDOnAuthentication;
   private boolean _ClientCertProxyEnabled;
   private boolean _FilterDispatchedRequestsEnabled;
   private boolean _HttpTraceSupportEnabled;
   private boolean _JSPCompilerBackwardsCompatible;
   private int _MaxPostSize;
   private int _MaxPostTimeSecs;
   private String _MimeMappingFile;
   private boolean _OptimisticSerialization;
   private boolean _OverloadProtectionEnabled;
   private String _P3PHeaderValue;
   private int _PostTimeoutSecs;
   private boolean _ReloginEnabled;
   private boolean _RetainOriginalURL;
   private boolean _RtexprvalueJspParamName;
   private boolean _ServletAuthenticationFormURL;
   private int _ServletReloadCheckSecs;
   private boolean _ShowArchivedRealPathEnabled;
   private boolean _WAPEnabled;
   private boolean _WeblogicPluginEnabled;
   private boolean _WorkContextPropagationEnabled;
   private String _XPoweredByHeaderLevel;
   private static SchemaHelper2 _schemaHelper;

   public WebAppContainerMBeanImpl() {
      this._initializeProperty(-1);
   }

   public WebAppContainerMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);
      this._initializeProperty(-1);
   }

   public boolean isReloginEnabled() {
      return this._ReloginEnabled;
   }

   public boolean isReloginEnabledSet() {
      return this._isSet(7);
   }

   public void setReloginEnabled(boolean var1) {
      boolean var2 = this._ReloginEnabled;
      this._ReloginEnabled = var1;
      this._postSet(7, var2, var1);
   }

   public boolean isAllowAllRoles() {
      return this._AllowAllRoles;
   }

   public boolean isAllowAllRolesSet() {
      return this._isSet(8);
   }

   public void setAllowAllRoles(boolean var1) throws InvalidAttributeValueException {
      boolean var2 = this._AllowAllRoles;
      this._AllowAllRoles = var1;
      this._postSet(8, var2, var1);
   }

   public boolean isFilterDispatchedRequestsEnabled() {
      return this._FilterDispatchedRequestsEnabled;
   }

   public boolean isFilterDispatchedRequestsEnabledSet() {
      return this._isSet(9);
   }

   public void setFilterDispatchedRequestsEnabled(boolean var1) throws InvalidAttributeValueException {
      boolean var2 = this._FilterDispatchedRequestsEnabled;
      this._FilterDispatchedRequestsEnabled = var1;
      this._postSet(9, var2, var1);
   }

   public boolean isOverloadProtectionEnabled() {
      return this._OverloadProtectionEnabled;
   }

   public boolean isOverloadProtectionEnabledSet() {
      return this._isSet(10);
   }

   public void setOverloadProtectionEnabled(boolean var1) {
      boolean var2 = this._OverloadProtectionEnabled;
      this._OverloadProtectionEnabled = var1;
      this._postSet(10, var2, var1);
   }

   public String getXPoweredByHeaderLevel() {
      return this._XPoweredByHeaderLevel;
   }

   public boolean isXPoweredByHeaderLevelSet() {
      return this._isSet(11);
   }

   public void setXPoweredByHeaderLevel(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{"NONE", "SHORT", "MEDIUM", "FULL"};
      var1 = weblogic.descriptor.beangen.LegalChecks.checkInEnum("XPoweredByHeaderLevel", var1, var2);
      String var3 = this._XPoweredByHeaderLevel;
      this._XPoweredByHeaderLevel = var1;
      this._postSet(11, var3, var1);
   }

   public String getMimeMappingFile() {
      return this._MimeMappingFile;
   }

   public boolean isMimeMappingFileSet() {
      return this._isSet(12);
   }

   public void setMimeMappingFile(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._MimeMappingFile;
      this._MimeMappingFile = var1;
      this._postSet(12, var2, var1);
   }

   public boolean isOptimisticSerialization() {
      return this._OptimisticSerialization;
   }

   public boolean isOptimisticSerializationSet() {
      return this._isSet(13);
   }

   public void setOptimisticSerialization(boolean var1) {
      boolean var2 = this._OptimisticSerialization;
      this._OptimisticSerialization = var1;
      this._postSet(13, var2, var1);
   }

   public boolean isRetainOriginalURL() {
      return this._RetainOriginalURL;
   }

   public boolean isRetainOriginalURLSet() {
      return this._isSet(14);
   }

   public void setRetainOriginalURL(boolean var1) {
      boolean var2 = this._RetainOriginalURL;
      this._RetainOriginalURL = var1;
      this._postSet(14, var2, var1);
   }

   public boolean isServletAuthenticationFormURL() {
      return this._ServletAuthenticationFormURL;
   }

   public boolean isServletAuthenticationFormURLSet() {
      return this._isSet(15);
   }

   public void setServletAuthenticationFormURL(boolean var1) {
      boolean var2 = this._ServletAuthenticationFormURL;
      this._ServletAuthenticationFormURL = var1;
      this._postSet(15, var2, var1);
   }

   public boolean isRtexprvalueJspParamName() {
      return this._RtexprvalueJspParamName;
   }

   public boolean isRtexprvalueJspParamNameSet() {
      return this._isSet(16);
   }

   public void setRtexprvalueJspParamName(boolean var1) {
      boolean var2 = this._RtexprvalueJspParamName;
      this._RtexprvalueJspParamName = var1;
      this._postSet(16, var2, var1);
   }

   public void setClientCertProxyEnabled(boolean var1) {
      boolean var2 = this._ClientCertProxyEnabled;
      this._ClientCertProxyEnabled = var1;
      this._postSet(17, var2, var1);
   }

   public boolean isClientCertProxyEnabled() {
      return this._ClientCertProxyEnabled;
   }

   public boolean isClientCertProxyEnabledSet() {
      return this._isSet(17);
   }

   public void setHttpTraceSupportEnabled(boolean var1) {
      boolean var2 = this._HttpTraceSupportEnabled;
      this._HttpTraceSupportEnabled = var1;
      this._postSet(18, var2, var1);
   }

   public boolean isHttpTraceSupportEnabled() {
      return this._HttpTraceSupportEnabled;
   }

   public boolean isHttpTraceSupportEnabledSet() {
      return this._isSet(18);
   }

   public void setWeblogicPluginEnabled(boolean var1) {
      boolean var2 = this._WeblogicPluginEnabled;
      this._WeblogicPluginEnabled = var1;
      this._postSet(19, var2, var1);
   }

   public boolean isWeblogicPluginEnabled() {
      return this._WeblogicPluginEnabled;
   }

   public boolean isWeblogicPluginEnabledSet() {
      return this._isSet(19);
   }

   public void setAuthCookieEnabled(boolean var1) {
      boolean var2 = this._AuthCookieEnabled;
      this._AuthCookieEnabled = var1;
      this._postSet(20, var2, var1);
   }

   public boolean isAuthCookieEnabled() {
      return this._AuthCookieEnabled;
   }

   public boolean isAuthCookieEnabledSet() {
      return this._isSet(20);
   }

   public void setWAPEnabled(boolean var1) {
      boolean var2 = this._WAPEnabled;
      this._WAPEnabled = var1;
      this._postSet(21, var2, var1);
   }

   public boolean isWAPEnabled() {
      return this._WAPEnabled;
   }

   public boolean isWAPEnabledSet() {
      return this._isSet(21);
   }

   public void setPostTimeoutSecs(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("PostTimeoutSecs", (long)var1, 0L, 120L);
      int var2 = this._PostTimeoutSecs;
      this._PostTimeoutSecs = var1;
      this._postSet(22, var2, var1);
   }

   public int getPostTimeoutSecs() {
      return this._PostTimeoutSecs;
   }

   public boolean isPostTimeoutSecsSet() {
      return this._isSet(22);
   }

   public void setMaxPostTimeSecs(int var1) throws InvalidAttributeValueException {
      int var2 = this._MaxPostTimeSecs;
      this._MaxPostTimeSecs = var1;
      this._postSet(23, var2, var1);
   }

   public int getMaxPostTimeSecs() {
      return this._MaxPostTimeSecs;
   }

   public boolean isMaxPostTimeSecsSet() {
      return this._isSet(23);
   }

   public void setMaxPostSize(int var1) throws InvalidAttributeValueException {
      int var2 = this._MaxPostSize;
      this._MaxPostSize = var1;
      this._postSet(24, var2, var1);
   }

   public int getMaxPostSize() {
      return this._MaxPostSize;
   }

   public boolean isMaxPostSizeSet() {
      return this._isSet(24);
   }

   public boolean isWorkContextPropagationEnabled() {
      return this._WorkContextPropagationEnabled;
   }

   public boolean isWorkContextPropagationEnabledSet() {
      return this._isSet(25);
   }

   public void setWorkContextPropagationEnabled(boolean var1) {
      boolean var2 = this._WorkContextPropagationEnabled;
      this._WorkContextPropagationEnabled = var1;
      this._postSet(25, var2, var1);
   }

   public void setP3PHeaderValue(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._P3PHeaderValue;
      this._P3PHeaderValue = var1;
      this._postSet(26, var2, var1);
   }

   public String getP3PHeaderValue() {
      return this._P3PHeaderValue;
   }

   public boolean isP3PHeaderValueSet() {
      return this._isSet(26);
   }

   public boolean isJSPCompilerBackwardsCompatible() {
      return this._JSPCompilerBackwardsCompatible;
   }

   public boolean isJSPCompilerBackwardsCompatibleSet() {
      return this._isSet(27);
   }

   public void setJSPCompilerBackwardsCompatible(boolean var1) {
      boolean var2 = this._JSPCompilerBackwardsCompatible;
      this._JSPCompilerBackwardsCompatible = var1;
      this._postSet(27, var2, var1);
   }

   public int getServletReloadCheckSecs() {
      if (!this._isSet(28)) {
         return this._isProductionModeEnabled() ? -1 : 1;
      } else {
         return this._ServletReloadCheckSecs;
      }
   }

   public boolean isServletReloadCheckSecsSet() {
      return this._isSet(28);
   }

   public void setServletReloadCheckSecs(int var1) {
      int var2 = this._ServletReloadCheckSecs;
      this._ServletReloadCheckSecs = var1;
      this._postSet(28, var2, var1);
   }

   public boolean isShowArchivedRealPathEnabled() {
      return this._ShowArchivedRealPathEnabled;
   }

   public boolean isShowArchivedRealPathEnabledSet() {
      return this._isSet(29);
   }

   public void setShowArchivedRealPathEnabled(boolean var1) {
      boolean var2 = this._ShowArchivedRealPathEnabled;
      this._ShowArchivedRealPathEnabled = var1;
      this._postSet(29, var2, var1);
   }

   public boolean isChangeSessionIDOnAuthentication() {
      return this._ChangeSessionIDOnAuthentication;
   }

   public boolean isChangeSessionIDOnAuthenticationSet() {
      return this._isSet(30);
   }

   public void setChangeSessionIDOnAuthentication(boolean var1) {
      boolean var2 = this._ChangeSessionIDOnAuthentication;
      this._ChangeSessionIDOnAuthentication = var1;
      this._postSet(30, var2, var1);
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
         var1 = 24;
      }

      try {
         switch (var1) {
            case 24:
               this._MaxPostSize = -1;
               if (var2) {
                  break;
               }
            case 23:
               this._MaxPostTimeSecs = -1;
               if (var2) {
                  break;
               }
            case 12:
               this._MimeMappingFile = "./config/mimemappings.properties";
               if (var2) {
                  break;
               }
            case 26:
               this._P3PHeaderValue = null;
               if (var2) {
                  break;
               }
            case 22:
               this._PostTimeoutSecs = 30;
               if (var2) {
                  break;
               }
            case 28:
               this._ServletReloadCheckSecs = 1;
               if (var2) {
                  break;
               }
            case 11:
               this._XPoweredByHeaderLevel = "SHORT";
               if (var2) {
                  break;
               }
            case 8:
               this._AllowAllRoles = false;
               if (var2) {
                  break;
               }
            case 20:
               this._AuthCookieEnabled = true;
               if (var2) {
                  break;
               }
            case 30:
               this._ChangeSessionIDOnAuthentication = true;
               if (var2) {
                  break;
               }
            case 17:
               this._ClientCertProxyEnabled = false;
               if (var2) {
                  break;
               }
            case 9:
               this._FilterDispatchedRequestsEnabled = false;
               if (var2) {
                  break;
               }
            case 18:
               this._HttpTraceSupportEnabled = false;
               if (var2) {
                  break;
               }
            case 27:
               this._JSPCompilerBackwardsCompatible = false;
               if (var2) {
                  break;
               }
            case 13:
               this._OptimisticSerialization = false;
               if (var2) {
                  break;
               }
            case 10:
               this._OverloadProtectionEnabled = false;
               if (var2) {
                  break;
               }
            case 7:
               this._ReloginEnabled = false;
               if (var2) {
                  break;
               }
            case 14:
               this._RetainOriginalURL = false;
               if (var2) {
                  break;
               }
            case 16:
               this._RtexprvalueJspParamName = false;
               if (var2) {
                  break;
               }
            case 15:
               this._ServletAuthenticationFormURL = true;
               if (var2) {
                  break;
               }
            case 29:
               this._ShowArchivedRealPathEnabled = false;
               if (var2) {
                  break;
               }
            case 21:
               this._WAPEnabled = false;
               if (var2) {
                  break;
               }
            case 19:
               this._WeblogicPluginEnabled = false;
               if (var2) {
                  break;
               }
            case 25:
               this._WorkContextPropagationEnabled = true;
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
      return "WebAppContainer";
   }

   public void putValue(String var1, Object var2) {
      boolean var4;
      if (var1.equals("AllowAllRoles")) {
         var4 = this._AllowAllRoles;
         this._AllowAllRoles = (Boolean)var2;
         this._postSet(8, var4, this._AllowAllRoles);
      } else if (var1.equals("AuthCookieEnabled")) {
         var4 = this._AuthCookieEnabled;
         this._AuthCookieEnabled = (Boolean)var2;
         this._postSet(20, var4, this._AuthCookieEnabled);
      } else if (var1.equals("ChangeSessionIDOnAuthentication")) {
         var4 = this._ChangeSessionIDOnAuthentication;
         this._ChangeSessionIDOnAuthentication = (Boolean)var2;
         this._postSet(30, var4, this._ChangeSessionIDOnAuthentication);
      } else if (var1.equals("ClientCertProxyEnabled")) {
         var4 = this._ClientCertProxyEnabled;
         this._ClientCertProxyEnabled = (Boolean)var2;
         this._postSet(17, var4, this._ClientCertProxyEnabled);
      } else if (var1.equals("FilterDispatchedRequestsEnabled")) {
         var4 = this._FilterDispatchedRequestsEnabled;
         this._FilterDispatchedRequestsEnabled = (Boolean)var2;
         this._postSet(9, var4, this._FilterDispatchedRequestsEnabled);
      } else if (var1.equals("HttpTraceSupportEnabled")) {
         var4 = this._HttpTraceSupportEnabled;
         this._HttpTraceSupportEnabled = (Boolean)var2;
         this._postSet(18, var4, this._HttpTraceSupportEnabled);
      } else if (var1.equals("JSPCompilerBackwardsCompatible")) {
         var4 = this._JSPCompilerBackwardsCompatible;
         this._JSPCompilerBackwardsCompatible = (Boolean)var2;
         this._postSet(27, var4, this._JSPCompilerBackwardsCompatible);
      } else {
         int var5;
         if (var1.equals("MaxPostSize")) {
            var5 = this._MaxPostSize;
            this._MaxPostSize = (Integer)var2;
            this._postSet(24, var5, this._MaxPostSize);
         } else if (var1.equals("MaxPostTimeSecs")) {
            var5 = this._MaxPostTimeSecs;
            this._MaxPostTimeSecs = (Integer)var2;
            this._postSet(23, var5, this._MaxPostTimeSecs);
         } else {
            String var3;
            if (var1.equals("MimeMappingFile")) {
               var3 = this._MimeMappingFile;
               this._MimeMappingFile = (String)var2;
               this._postSet(12, var3, this._MimeMappingFile);
            } else if (var1.equals("OptimisticSerialization")) {
               var4 = this._OptimisticSerialization;
               this._OptimisticSerialization = (Boolean)var2;
               this._postSet(13, var4, this._OptimisticSerialization);
            } else if (var1.equals("OverloadProtectionEnabled")) {
               var4 = this._OverloadProtectionEnabled;
               this._OverloadProtectionEnabled = (Boolean)var2;
               this._postSet(10, var4, this._OverloadProtectionEnabled);
            } else if (var1.equals("P3PHeaderValue")) {
               var3 = this._P3PHeaderValue;
               this._P3PHeaderValue = (String)var2;
               this._postSet(26, var3, this._P3PHeaderValue);
            } else if (var1.equals("PostTimeoutSecs")) {
               var5 = this._PostTimeoutSecs;
               this._PostTimeoutSecs = (Integer)var2;
               this._postSet(22, var5, this._PostTimeoutSecs);
            } else if (var1.equals("ReloginEnabled")) {
               var4 = this._ReloginEnabled;
               this._ReloginEnabled = (Boolean)var2;
               this._postSet(7, var4, this._ReloginEnabled);
            } else if (var1.equals("RetainOriginalURL")) {
               var4 = this._RetainOriginalURL;
               this._RetainOriginalURL = (Boolean)var2;
               this._postSet(14, var4, this._RetainOriginalURL);
            } else if (var1.equals("RtexprvalueJspParamName")) {
               var4 = this._RtexprvalueJspParamName;
               this._RtexprvalueJspParamName = (Boolean)var2;
               this._postSet(16, var4, this._RtexprvalueJspParamName);
            } else if (var1.equals("ServletAuthenticationFormURL")) {
               var4 = this._ServletAuthenticationFormURL;
               this._ServletAuthenticationFormURL = (Boolean)var2;
               this._postSet(15, var4, this._ServletAuthenticationFormURL);
            } else if (var1.equals("ServletReloadCheckSecs")) {
               var5 = this._ServletReloadCheckSecs;
               this._ServletReloadCheckSecs = (Integer)var2;
               this._postSet(28, var5, this._ServletReloadCheckSecs);
            } else if (var1.equals("ShowArchivedRealPathEnabled")) {
               var4 = this._ShowArchivedRealPathEnabled;
               this._ShowArchivedRealPathEnabled = (Boolean)var2;
               this._postSet(29, var4, this._ShowArchivedRealPathEnabled);
            } else if (var1.equals("WAPEnabled")) {
               var4 = this._WAPEnabled;
               this._WAPEnabled = (Boolean)var2;
               this._postSet(21, var4, this._WAPEnabled);
            } else if (var1.equals("WeblogicPluginEnabled")) {
               var4 = this._WeblogicPluginEnabled;
               this._WeblogicPluginEnabled = (Boolean)var2;
               this._postSet(19, var4, this._WeblogicPluginEnabled);
            } else if (var1.equals("WorkContextPropagationEnabled")) {
               var4 = this._WorkContextPropagationEnabled;
               this._WorkContextPropagationEnabled = (Boolean)var2;
               this._postSet(25, var4, this._WorkContextPropagationEnabled);
            } else if (var1.equals("XPoweredByHeaderLevel")) {
               var3 = this._XPoweredByHeaderLevel;
               this._XPoweredByHeaderLevel = (String)var2;
               this._postSet(11, var3, this._XPoweredByHeaderLevel);
            } else {
               super.putValue(var1, var2);
            }
         }
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("AllowAllRoles")) {
         return new Boolean(this._AllowAllRoles);
      } else if (var1.equals("AuthCookieEnabled")) {
         return new Boolean(this._AuthCookieEnabled);
      } else if (var1.equals("ChangeSessionIDOnAuthentication")) {
         return new Boolean(this._ChangeSessionIDOnAuthentication);
      } else if (var1.equals("ClientCertProxyEnabled")) {
         return new Boolean(this._ClientCertProxyEnabled);
      } else if (var1.equals("FilterDispatchedRequestsEnabled")) {
         return new Boolean(this._FilterDispatchedRequestsEnabled);
      } else if (var1.equals("HttpTraceSupportEnabled")) {
         return new Boolean(this._HttpTraceSupportEnabled);
      } else if (var1.equals("JSPCompilerBackwardsCompatible")) {
         return new Boolean(this._JSPCompilerBackwardsCompatible);
      } else if (var1.equals("MaxPostSize")) {
         return new Integer(this._MaxPostSize);
      } else if (var1.equals("MaxPostTimeSecs")) {
         return new Integer(this._MaxPostTimeSecs);
      } else if (var1.equals("MimeMappingFile")) {
         return this._MimeMappingFile;
      } else if (var1.equals("OptimisticSerialization")) {
         return new Boolean(this._OptimisticSerialization);
      } else if (var1.equals("OverloadProtectionEnabled")) {
         return new Boolean(this._OverloadProtectionEnabled);
      } else if (var1.equals("P3PHeaderValue")) {
         return this._P3PHeaderValue;
      } else if (var1.equals("PostTimeoutSecs")) {
         return new Integer(this._PostTimeoutSecs);
      } else if (var1.equals("ReloginEnabled")) {
         return new Boolean(this._ReloginEnabled);
      } else if (var1.equals("RetainOriginalURL")) {
         return new Boolean(this._RetainOriginalURL);
      } else if (var1.equals("RtexprvalueJspParamName")) {
         return new Boolean(this._RtexprvalueJspParamName);
      } else if (var1.equals("ServletAuthenticationFormURL")) {
         return new Boolean(this._ServletAuthenticationFormURL);
      } else if (var1.equals("ServletReloadCheckSecs")) {
         return new Integer(this._ServletReloadCheckSecs);
      } else if (var1.equals("ShowArchivedRealPathEnabled")) {
         return new Boolean(this._ShowArchivedRealPathEnabled);
      } else if (var1.equals("WAPEnabled")) {
         return new Boolean(this._WAPEnabled);
      } else if (var1.equals("WeblogicPluginEnabled")) {
         return new Boolean(this._WeblogicPluginEnabled);
      } else if (var1.equals("WorkContextPropagationEnabled")) {
         return new Boolean(this._WorkContextPropagationEnabled);
      } else {
         return var1.equals("XPoweredByHeaderLevel") ? this._XPoweredByHeaderLevel : super.getValue(var1);
      }
   }

   public static class SchemaHelper2 extends ConfigurationMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 11:
               if (var1.equals("wap-enabled")) {
                  return 21;
               }
            case 12:
            case 14:
            case 20:
            case 21:
            case 22:
            case 28:
            case 29:
            case 30:
            default:
               break;
            case 13:
               if (var1.equals("max-post-size")) {
                  return 24;
               }
               break;
            case 15:
               if (var1.equals("allow-all-roles")) {
                  return 8;
               }

               if (var1.equals("relogin-enabled")) {
                  return 7;
               }
               break;
            case 16:
               if (var1.equals("p3p-header-value")) {
                  return 26;
               }
               break;
            case 17:
               if (var1.equals("mime-mapping-file")) {
                  return 12;
               }

               if (var1.equals("post-timeout-secs")) {
                  return 22;
               }
               break;
            case 18:
               if (var1.equals("max-post-time-secs")) {
                  return 23;
               }
               break;
            case 19:
               if (var1.equals("auth-cookie-enabled")) {
                  return 20;
               }

               if (var1.equals("retain-original-url")) {
                  return 14;
               }
               break;
            case 23:
               if (var1.equals("weblogic-plugin-enabled")) {
                  return 19;
               }
               break;
            case 24:
               if (var1.equals("optimistic-serialization")) {
                  return 13;
               }
               break;
            case 25:
               if (var1.equals("servlet-reload-check-secs")) {
                  return 28;
               }

               if (var1.equals("x-powered-by-header-level")) {
                  return 11;
               }

               if (var1.equals("client-cert-proxy-enabled")) {
                  return 17;
               }
               break;
            case 26:
               if (var1.equals("http-trace-support-enabled")) {
                  return 18;
               }

               if (var1.equals("rtexprvalue-jsp-param-name")) {
                  return 16;
               }
               break;
            case 27:
               if (var1.equals("overload-protection-enabled")) {
                  return 10;
               }
               break;
            case 31:
               if (var1.equals("servlet-authentication-form-url")) {
                  return 15;
               }

               if (var1.equals("show-archived-real-path-enabled")) {
                  return 29;
               }
               break;
            case 32:
               if (var1.equals("work-context-propagation-enabled")) {
                  return 25;
               }
               break;
            case 33:
               if (var1.equals("jsp-compiler-backwards-compatible")) {
                  return 27;
               }
               break;
            case 34:
               if (var1.equals("change-sessionid-on-authentication")) {
                  return 30;
               }

               if (var1.equals("filter-dispatched-requests-enabled")) {
                  return 9;
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
               return "relogin-enabled";
            case 8:
               return "allow-all-roles";
            case 9:
               return "filter-dispatched-requests-enabled";
            case 10:
               return "overload-protection-enabled";
            case 11:
               return "x-powered-by-header-level";
            case 12:
               return "mime-mapping-file";
            case 13:
               return "optimistic-serialization";
            case 14:
               return "retain-original-url";
            case 15:
               return "servlet-authentication-form-url";
            case 16:
               return "rtexprvalue-jsp-param-name";
            case 17:
               return "client-cert-proxy-enabled";
            case 18:
               return "http-trace-support-enabled";
            case 19:
               return "weblogic-plugin-enabled";
            case 20:
               return "auth-cookie-enabled";
            case 21:
               return "wap-enabled";
            case 22:
               return "post-timeout-secs";
            case 23:
               return "max-post-time-secs";
            case 24:
               return "max-post-size";
            case 25:
               return "work-context-propagation-enabled";
            case 26:
               return "p3p-header-value";
            case 27:
               return "jsp-compiler-backwards-compatible";
            case 28:
               return "servlet-reload-check-secs";
            case 29:
               return "show-archived-real-path-enabled";
            case 30:
               return "change-sessionid-on-authentication";
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
               return true;
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
            case 22:
               return true;
            case 23:
               return true;
            case 24:
               return true;
            case 25:
               return true;
            case 26:
               return true;
            case 27:
            default:
               return super.isConfigurable(var1);
            case 28:
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
      private WebAppContainerMBeanImpl bean;

      protected Helper(WebAppContainerMBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 7:
               return "ReloginEnabled";
            case 8:
               return "AllowAllRoles";
            case 9:
               return "FilterDispatchedRequestsEnabled";
            case 10:
               return "OverloadProtectionEnabled";
            case 11:
               return "XPoweredByHeaderLevel";
            case 12:
               return "MimeMappingFile";
            case 13:
               return "OptimisticSerialization";
            case 14:
               return "RetainOriginalURL";
            case 15:
               return "ServletAuthenticationFormURL";
            case 16:
               return "RtexprvalueJspParamName";
            case 17:
               return "ClientCertProxyEnabled";
            case 18:
               return "HttpTraceSupportEnabled";
            case 19:
               return "WeblogicPluginEnabled";
            case 20:
               return "AuthCookieEnabled";
            case 21:
               return "WAPEnabled";
            case 22:
               return "PostTimeoutSecs";
            case 23:
               return "MaxPostTimeSecs";
            case 24:
               return "MaxPostSize";
            case 25:
               return "WorkContextPropagationEnabled";
            case 26:
               return "P3PHeaderValue";
            case 27:
               return "JSPCompilerBackwardsCompatible";
            case 28:
               return "ServletReloadCheckSecs";
            case 29:
               return "ShowArchivedRealPathEnabled";
            case 30:
               return "ChangeSessionIDOnAuthentication";
            default:
               return super.getPropertyName(var1);
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("MaxPostSize")) {
            return 24;
         } else if (var1.equals("MaxPostTimeSecs")) {
            return 23;
         } else if (var1.equals("MimeMappingFile")) {
            return 12;
         } else if (var1.equals("P3PHeaderValue")) {
            return 26;
         } else if (var1.equals("PostTimeoutSecs")) {
            return 22;
         } else if (var1.equals("ServletReloadCheckSecs")) {
            return 28;
         } else if (var1.equals("XPoweredByHeaderLevel")) {
            return 11;
         } else if (var1.equals("AllowAllRoles")) {
            return 8;
         } else if (var1.equals("AuthCookieEnabled")) {
            return 20;
         } else if (var1.equals("ChangeSessionIDOnAuthentication")) {
            return 30;
         } else if (var1.equals("ClientCertProxyEnabled")) {
            return 17;
         } else if (var1.equals("FilterDispatchedRequestsEnabled")) {
            return 9;
         } else if (var1.equals("HttpTraceSupportEnabled")) {
            return 18;
         } else if (var1.equals("JSPCompilerBackwardsCompatible")) {
            return 27;
         } else if (var1.equals("OptimisticSerialization")) {
            return 13;
         } else if (var1.equals("OverloadProtectionEnabled")) {
            return 10;
         } else if (var1.equals("ReloginEnabled")) {
            return 7;
         } else if (var1.equals("RetainOriginalURL")) {
            return 14;
         } else if (var1.equals("RtexprvalueJspParamName")) {
            return 16;
         } else if (var1.equals("ServletAuthenticationFormURL")) {
            return 15;
         } else if (var1.equals("ShowArchivedRealPathEnabled")) {
            return 29;
         } else if (var1.equals("WAPEnabled")) {
            return 21;
         } else if (var1.equals("WeblogicPluginEnabled")) {
            return 19;
         } else {
            return var1.equals("WorkContextPropagationEnabled") ? 25 : super.getPropertyIndex(var1);
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
            if (this.bean.isMaxPostSizeSet()) {
               var2.append("MaxPostSize");
               var2.append(String.valueOf(this.bean.getMaxPostSize()));
            }

            if (this.bean.isMaxPostTimeSecsSet()) {
               var2.append("MaxPostTimeSecs");
               var2.append(String.valueOf(this.bean.getMaxPostTimeSecs()));
            }

            if (this.bean.isMimeMappingFileSet()) {
               var2.append("MimeMappingFile");
               var2.append(String.valueOf(this.bean.getMimeMappingFile()));
            }

            if (this.bean.isP3PHeaderValueSet()) {
               var2.append("P3PHeaderValue");
               var2.append(String.valueOf(this.bean.getP3PHeaderValue()));
            }

            if (this.bean.isPostTimeoutSecsSet()) {
               var2.append("PostTimeoutSecs");
               var2.append(String.valueOf(this.bean.getPostTimeoutSecs()));
            }

            if (this.bean.isServletReloadCheckSecsSet()) {
               var2.append("ServletReloadCheckSecs");
               var2.append(String.valueOf(this.bean.getServletReloadCheckSecs()));
            }

            if (this.bean.isXPoweredByHeaderLevelSet()) {
               var2.append("XPoweredByHeaderLevel");
               var2.append(String.valueOf(this.bean.getXPoweredByHeaderLevel()));
            }

            if (this.bean.isAllowAllRolesSet()) {
               var2.append("AllowAllRoles");
               var2.append(String.valueOf(this.bean.isAllowAllRoles()));
            }

            if (this.bean.isAuthCookieEnabledSet()) {
               var2.append("AuthCookieEnabled");
               var2.append(String.valueOf(this.bean.isAuthCookieEnabled()));
            }

            if (this.bean.isChangeSessionIDOnAuthenticationSet()) {
               var2.append("ChangeSessionIDOnAuthentication");
               var2.append(String.valueOf(this.bean.isChangeSessionIDOnAuthentication()));
            }

            if (this.bean.isClientCertProxyEnabledSet()) {
               var2.append("ClientCertProxyEnabled");
               var2.append(String.valueOf(this.bean.isClientCertProxyEnabled()));
            }

            if (this.bean.isFilterDispatchedRequestsEnabledSet()) {
               var2.append("FilterDispatchedRequestsEnabled");
               var2.append(String.valueOf(this.bean.isFilterDispatchedRequestsEnabled()));
            }

            if (this.bean.isHttpTraceSupportEnabledSet()) {
               var2.append("HttpTraceSupportEnabled");
               var2.append(String.valueOf(this.bean.isHttpTraceSupportEnabled()));
            }

            if (this.bean.isJSPCompilerBackwardsCompatibleSet()) {
               var2.append("JSPCompilerBackwardsCompatible");
               var2.append(String.valueOf(this.bean.isJSPCompilerBackwardsCompatible()));
            }

            if (this.bean.isOptimisticSerializationSet()) {
               var2.append("OptimisticSerialization");
               var2.append(String.valueOf(this.bean.isOptimisticSerialization()));
            }

            if (this.bean.isOverloadProtectionEnabledSet()) {
               var2.append("OverloadProtectionEnabled");
               var2.append(String.valueOf(this.bean.isOverloadProtectionEnabled()));
            }

            if (this.bean.isReloginEnabledSet()) {
               var2.append("ReloginEnabled");
               var2.append(String.valueOf(this.bean.isReloginEnabled()));
            }

            if (this.bean.isRetainOriginalURLSet()) {
               var2.append("RetainOriginalURL");
               var2.append(String.valueOf(this.bean.isRetainOriginalURL()));
            }

            if (this.bean.isRtexprvalueJspParamNameSet()) {
               var2.append("RtexprvalueJspParamName");
               var2.append(String.valueOf(this.bean.isRtexprvalueJspParamName()));
            }

            if (this.bean.isServletAuthenticationFormURLSet()) {
               var2.append("ServletAuthenticationFormURL");
               var2.append(String.valueOf(this.bean.isServletAuthenticationFormURL()));
            }

            if (this.bean.isShowArchivedRealPathEnabledSet()) {
               var2.append("ShowArchivedRealPathEnabled");
               var2.append(String.valueOf(this.bean.isShowArchivedRealPathEnabled()));
            }

            if (this.bean.isWAPEnabledSet()) {
               var2.append("WAPEnabled");
               var2.append(String.valueOf(this.bean.isWAPEnabled()));
            }

            if (this.bean.isWeblogicPluginEnabledSet()) {
               var2.append("WeblogicPluginEnabled");
               var2.append(String.valueOf(this.bean.isWeblogicPluginEnabled()));
            }

            if (this.bean.isWorkContextPropagationEnabledSet()) {
               var2.append("WorkContextPropagationEnabled");
               var2.append(String.valueOf(this.bean.isWorkContextPropagationEnabled()));
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
            WebAppContainerMBeanImpl var2 = (WebAppContainerMBeanImpl)var1;
            this.computeDiff("MaxPostSize", this.bean.getMaxPostSize(), var2.getMaxPostSize(), true);
            this.computeDiff("MaxPostTimeSecs", this.bean.getMaxPostTimeSecs(), var2.getMaxPostTimeSecs(), true);
            this.computeDiff("MimeMappingFile", this.bean.getMimeMappingFile(), var2.getMimeMappingFile(), false);
            this.computeDiff("P3PHeaderValue", this.bean.getP3PHeaderValue(), var2.getP3PHeaderValue(), false);
            this.computeDiff("PostTimeoutSecs", this.bean.getPostTimeoutSecs(), var2.getPostTimeoutSecs(), true);
            this.computeDiff("ServletReloadCheckSecs", this.bean.getServletReloadCheckSecs(), var2.getServletReloadCheckSecs(), true);
            this.computeDiff("XPoweredByHeaderLevel", this.bean.getXPoweredByHeaderLevel(), var2.getXPoweredByHeaderLevel(), true);
            this.computeDiff("AllowAllRoles", this.bean.isAllowAllRoles(), var2.isAllowAllRoles(), false);
            this.computeDiff("AuthCookieEnabled", this.bean.isAuthCookieEnabled(), var2.isAuthCookieEnabled(), true);
            this.computeDiff("ChangeSessionIDOnAuthentication", this.bean.isChangeSessionIDOnAuthentication(), var2.isChangeSessionIDOnAuthentication(), false);
            this.computeDiff("ClientCertProxyEnabled", this.bean.isClientCertProxyEnabled(), var2.isClientCertProxyEnabled(), false);
            this.computeDiff("FilterDispatchedRequestsEnabled", this.bean.isFilterDispatchedRequestsEnabled(), var2.isFilterDispatchedRequestsEnabled(), false);
            this.computeDiff("HttpTraceSupportEnabled", this.bean.isHttpTraceSupportEnabled(), var2.isHttpTraceSupportEnabled(), true);
            this.computeDiff("JSPCompilerBackwardsCompatible", this.bean.isJSPCompilerBackwardsCompatible(), var2.isJSPCompilerBackwardsCompatible(), false);
            this.computeDiff("OptimisticSerialization", this.bean.isOptimisticSerialization(), var2.isOptimisticSerialization(), false);
            this.computeDiff("OverloadProtectionEnabled", this.bean.isOverloadProtectionEnabled(), var2.isOverloadProtectionEnabled(), true);
            this.computeDiff("ReloginEnabled", this.bean.isReloginEnabled(), var2.isReloginEnabled(), false);
            this.computeDiff("RetainOriginalURL", this.bean.isRetainOriginalURL(), var2.isRetainOriginalURL(), false);
            this.computeDiff("RtexprvalueJspParamName", this.bean.isRtexprvalueJspParamName(), var2.isRtexprvalueJspParamName(), false);
            this.computeDiff("ServletAuthenticationFormURL", this.bean.isServletAuthenticationFormURL(), var2.isServletAuthenticationFormURL(), false);
            this.computeDiff("ShowArchivedRealPathEnabled", this.bean.isShowArchivedRealPathEnabled(), var2.isShowArchivedRealPathEnabled(), false);
            this.computeDiff("WAPEnabled", this.bean.isWAPEnabled(), var2.isWAPEnabled(), true);
            this.computeDiff("WeblogicPluginEnabled", this.bean.isWeblogicPluginEnabled(), var2.isWeblogicPluginEnabled(), true);
            this.computeDiff("WorkContextPropagationEnabled", this.bean.isWorkContextPropagationEnabled(), var2.isWorkContextPropagationEnabled(), true);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            WebAppContainerMBeanImpl var3 = (WebAppContainerMBeanImpl)var1.getSourceBean();
            WebAppContainerMBeanImpl var4 = (WebAppContainerMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("MaxPostSize")) {
                  var3.setMaxPostSize(var4.getMaxPostSize());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 24);
               } else if (var5.equals("MaxPostTimeSecs")) {
                  var3.setMaxPostTimeSecs(var4.getMaxPostTimeSecs());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 23);
               } else if (var5.equals("MimeMappingFile")) {
                  var3.setMimeMappingFile(var4.getMimeMappingFile());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 12);
               } else if (var5.equals("P3PHeaderValue")) {
                  var3.setP3PHeaderValue(var4.getP3PHeaderValue());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 26);
               } else if (var5.equals("PostTimeoutSecs")) {
                  var3.setPostTimeoutSecs(var4.getPostTimeoutSecs());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 22);
               } else if (var5.equals("ServletReloadCheckSecs")) {
                  var3.setServletReloadCheckSecs(var4.getServletReloadCheckSecs());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 28);
               } else if (var5.equals("XPoweredByHeaderLevel")) {
                  var3.setXPoweredByHeaderLevel(var4.getXPoweredByHeaderLevel());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 11);
               } else if (var5.equals("AllowAllRoles")) {
                  var3.setAllowAllRoles(var4.isAllowAllRoles());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 8);
               } else if (var5.equals("AuthCookieEnabled")) {
                  var3.setAuthCookieEnabled(var4.isAuthCookieEnabled());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 20);
               } else if (var5.equals("ChangeSessionIDOnAuthentication")) {
                  var3.setChangeSessionIDOnAuthentication(var4.isChangeSessionIDOnAuthentication());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 30);
               } else if (var5.equals("ClientCertProxyEnabled")) {
                  var3.setClientCertProxyEnabled(var4.isClientCertProxyEnabled());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 17);
               } else if (var5.equals("FilterDispatchedRequestsEnabled")) {
                  var3.setFilterDispatchedRequestsEnabled(var4.isFilterDispatchedRequestsEnabled());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 9);
               } else if (var5.equals("HttpTraceSupportEnabled")) {
                  var3.setHttpTraceSupportEnabled(var4.isHttpTraceSupportEnabled());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 18);
               } else if (var5.equals("JSPCompilerBackwardsCompatible")) {
                  var3.setJSPCompilerBackwardsCompatible(var4.isJSPCompilerBackwardsCompatible());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 27);
               } else if (var5.equals("OptimisticSerialization")) {
                  var3.setOptimisticSerialization(var4.isOptimisticSerialization());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 13);
               } else if (var5.equals("OverloadProtectionEnabled")) {
                  var3.setOverloadProtectionEnabled(var4.isOverloadProtectionEnabled());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 10);
               } else if (var5.equals("ReloginEnabled")) {
                  var3.setReloginEnabled(var4.isReloginEnabled());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 7);
               } else if (var5.equals("RetainOriginalURL")) {
                  var3.setRetainOriginalURL(var4.isRetainOriginalURL());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 14);
               } else if (var5.equals("RtexprvalueJspParamName")) {
                  var3.setRtexprvalueJspParamName(var4.isRtexprvalueJspParamName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 16);
               } else if (var5.equals("ServletAuthenticationFormURL")) {
                  var3.setServletAuthenticationFormURL(var4.isServletAuthenticationFormURL());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 15);
               } else if (var5.equals("ShowArchivedRealPathEnabled")) {
                  var3.setShowArchivedRealPathEnabled(var4.isShowArchivedRealPathEnabled());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 29);
               } else if (var5.equals("WAPEnabled")) {
                  var3.setWAPEnabled(var4.isWAPEnabled());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 21);
               } else if (var5.equals("WeblogicPluginEnabled")) {
                  var3.setWeblogicPluginEnabled(var4.isWeblogicPluginEnabled());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 19);
               } else if (var5.equals("WorkContextPropagationEnabled")) {
                  var3.setWorkContextPropagationEnabled(var4.isWorkContextPropagationEnabled());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 25);
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
            WebAppContainerMBeanImpl var5 = (WebAppContainerMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("MaxPostSize")) && this.bean.isMaxPostSizeSet()) {
               var5.setMaxPostSize(this.bean.getMaxPostSize());
            }

            if ((var3 == null || !var3.contains("MaxPostTimeSecs")) && this.bean.isMaxPostTimeSecsSet()) {
               var5.setMaxPostTimeSecs(this.bean.getMaxPostTimeSecs());
            }

            if ((var3 == null || !var3.contains("MimeMappingFile")) && this.bean.isMimeMappingFileSet()) {
               var5.setMimeMappingFile(this.bean.getMimeMappingFile());
            }

            if ((var3 == null || !var3.contains("P3PHeaderValue")) && this.bean.isP3PHeaderValueSet()) {
               var5.setP3PHeaderValue(this.bean.getP3PHeaderValue());
            }

            if ((var3 == null || !var3.contains("PostTimeoutSecs")) && this.bean.isPostTimeoutSecsSet()) {
               var5.setPostTimeoutSecs(this.bean.getPostTimeoutSecs());
            }

            if ((var3 == null || !var3.contains("ServletReloadCheckSecs")) && this.bean.isServletReloadCheckSecsSet()) {
               var5.setServletReloadCheckSecs(this.bean.getServletReloadCheckSecs());
            }

            if ((var3 == null || !var3.contains("XPoweredByHeaderLevel")) && this.bean.isXPoweredByHeaderLevelSet()) {
               var5.setXPoweredByHeaderLevel(this.bean.getXPoweredByHeaderLevel());
            }

            if ((var3 == null || !var3.contains("AllowAllRoles")) && this.bean.isAllowAllRolesSet()) {
               var5.setAllowAllRoles(this.bean.isAllowAllRoles());
            }

            if ((var3 == null || !var3.contains("AuthCookieEnabled")) && this.bean.isAuthCookieEnabledSet()) {
               var5.setAuthCookieEnabled(this.bean.isAuthCookieEnabled());
            }

            if ((var3 == null || !var3.contains("ChangeSessionIDOnAuthentication")) && this.bean.isChangeSessionIDOnAuthenticationSet()) {
               var5.setChangeSessionIDOnAuthentication(this.bean.isChangeSessionIDOnAuthentication());
            }

            if ((var3 == null || !var3.contains("ClientCertProxyEnabled")) && this.bean.isClientCertProxyEnabledSet()) {
               var5.setClientCertProxyEnabled(this.bean.isClientCertProxyEnabled());
            }

            if ((var3 == null || !var3.contains("FilterDispatchedRequestsEnabled")) && this.bean.isFilterDispatchedRequestsEnabledSet()) {
               var5.setFilterDispatchedRequestsEnabled(this.bean.isFilterDispatchedRequestsEnabled());
            }

            if ((var3 == null || !var3.contains("HttpTraceSupportEnabled")) && this.bean.isHttpTraceSupportEnabledSet()) {
               var5.setHttpTraceSupportEnabled(this.bean.isHttpTraceSupportEnabled());
            }

            if ((var3 == null || !var3.contains("JSPCompilerBackwardsCompatible")) && this.bean.isJSPCompilerBackwardsCompatibleSet()) {
               var5.setJSPCompilerBackwardsCompatible(this.bean.isJSPCompilerBackwardsCompatible());
            }

            if ((var3 == null || !var3.contains("OptimisticSerialization")) && this.bean.isOptimisticSerializationSet()) {
               var5.setOptimisticSerialization(this.bean.isOptimisticSerialization());
            }

            if ((var3 == null || !var3.contains("OverloadProtectionEnabled")) && this.bean.isOverloadProtectionEnabledSet()) {
               var5.setOverloadProtectionEnabled(this.bean.isOverloadProtectionEnabled());
            }

            if ((var3 == null || !var3.contains("ReloginEnabled")) && this.bean.isReloginEnabledSet()) {
               var5.setReloginEnabled(this.bean.isReloginEnabled());
            }

            if ((var3 == null || !var3.contains("RetainOriginalURL")) && this.bean.isRetainOriginalURLSet()) {
               var5.setRetainOriginalURL(this.bean.isRetainOriginalURL());
            }

            if ((var3 == null || !var3.contains("RtexprvalueJspParamName")) && this.bean.isRtexprvalueJspParamNameSet()) {
               var5.setRtexprvalueJspParamName(this.bean.isRtexprvalueJspParamName());
            }

            if ((var3 == null || !var3.contains("ServletAuthenticationFormURL")) && this.bean.isServletAuthenticationFormURLSet()) {
               var5.setServletAuthenticationFormURL(this.bean.isServletAuthenticationFormURL());
            }

            if ((var3 == null || !var3.contains("ShowArchivedRealPathEnabled")) && this.bean.isShowArchivedRealPathEnabledSet()) {
               var5.setShowArchivedRealPathEnabled(this.bean.isShowArchivedRealPathEnabled());
            }

            if ((var3 == null || !var3.contains("WAPEnabled")) && this.bean.isWAPEnabledSet()) {
               var5.setWAPEnabled(this.bean.isWAPEnabled());
            }

            if ((var3 == null || !var3.contains("WeblogicPluginEnabled")) && this.bean.isWeblogicPluginEnabledSet()) {
               var5.setWeblogicPluginEnabled(this.bean.isWeblogicPluginEnabled());
            }

            if ((var3 == null || !var3.contains("WorkContextPropagationEnabled")) && this.bean.isWorkContextPropagationEnabledSet()) {
               var5.setWorkContextPropagationEnabled(this.bean.isWorkContextPropagationEnabled());
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
