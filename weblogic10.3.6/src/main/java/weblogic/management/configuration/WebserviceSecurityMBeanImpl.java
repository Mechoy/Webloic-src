package weblogic.management.configuration;

import java.io.Serializable;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.zip.CRC32;
import javax.management.InvalidAttributeValueException;
import weblogic.descriptor.BeanRemoveRejectedException;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBeanHelper;
import weblogic.descriptor.internal.Munger;
import weblogic.descriptor.internal.SchemaHelper;
import weblogic.utils.collections.ArrayIterator;
import weblogic.utils.collections.CombinedIterator;

public class WebserviceSecurityMBeanImpl extends ConfigurationMBeanImpl implements WebserviceSecurityMBean, Serializable {
   private String _CompatibilityOrderingPreference;
   private String _CompatibilityPreference;
   private String _DefaultCredentialProviderSTSURI;
   private String _PolicySelectionPreference;
   private WebserviceCredentialProviderMBean[] _WebserviceCredentialProviders;
   private WebserviceSecurityTokenMBean[] _WebserviceSecurityTokens;
   private WebserviceTimestampMBean _WebserviceTimestamp;
   private WebserviceTokenHandlerMBean[] _WebserviceTokenHandlers;
   private static SchemaHelper2 _schemaHelper;

   public WebserviceSecurityMBeanImpl() {
      this._initializeProperty(-1);
   }

   public WebserviceSecurityMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);
      this._initializeProperty(-1);
   }

   public void addWebserviceTokenHandler(WebserviceTokenHandlerMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 7)) {
         WebserviceTokenHandlerMBean[] var2;
         if (this._isSet(7)) {
            var2 = (WebserviceTokenHandlerMBean[])((WebserviceTokenHandlerMBean[])this._getHelper()._extendArray(this.getWebserviceTokenHandlers(), WebserviceTokenHandlerMBean.class, var1));
         } else {
            var2 = new WebserviceTokenHandlerMBean[]{var1};
         }

         try {
            this.setWebserviceTokenHandlers(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public WebserviceTokenHandlerMBean[] getWebserviceTokenHandlers() {
      return this._WebserviceTokenHandlers;
   }

   public boolean isWebserviceTokenHandlersSet() {
      return this._isSet(7);
   }

   public void removeWebserviceTokenHandler(WebserviceTokenHandlerMBean var1) {
      this.destroyWebserviceTokenHandler(var1);
   }

   public void setWebserviceTokenHandlers(WebserviceTokenHandlerMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new WebserviceTokenHandlerMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 7)) {
            this._getReferenceManager().registerBean(var3, true);
            this._postCreate(var3);
         }
      }

      WebserviceTokenHandlerMBean[] var5 = this._WebserviceTokenHandlers;
      this._WebserviceTokenHandlers = (WebserviceTokenHandlerMBean[])var4;
      this._postSet(7, var5, var4);
   }

   public WebserviceTokenHandlerMBean lookupWebserviceTokenHandler(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._WebserviceTokenHandlers).iterator();

      WebserviceTokenHandlerMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (WebserviceTokenHandlerMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public WebserviceTokenHandlerMBean createWebserviceTokenHandler(String var1) {
      WebserviceTokenHandlerMBeanImpl var2 = new WebserviceTokenHandlerMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addWebserviceTokenHandler(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public void destroyWebserviceTokenHandler(WebserviceTokenHandlerMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 7);
         WebserviceTokenHandlerMBean[] var2 = this.getWebserviceTokenHandlers();
         WebserviceTokenHandlerMBean[] var3 = (WebserviceTokenHandlerMBean[])((WebserviceTokenHandlerMBean[])this._getHelper()._removeElement(var2, WebserviceTokenHandlerMBean.class, var1));
         if (var2.length != var3.length) {
            this._preDestroy((AbstractDescriptorBean)var1);

            try {
               AbstractDescriptorBean var4 = (AbstractDescriptorBean)var1;
               if (var4 == null) {
                  return;
               }

               List var5 = this._getReferenceManager().getResolvedReferences(var4);
               if (var5 != null && var5.size() > 0) {
                  throw new BeanRemoveRejectedException(var4, var5);
               }

               this._getReferenceManager().unregisterBean(var4);
               this._markDestroyed(var4);
               this.setWebserviceTokenHandlers(var3);
            } catch (Exception var6) {
               if (var6 instanceof RuntimeException) {
                  throw (RuntimeException)var6;
               }

               throw new UndeclaredThrowableException(var6);
            }
         }

      } catch (Exception var7) {
         if (var7 instanceof RuntimeException) {
            throw (RuntimeException)var7;
         } else {
            throw new UndeclaredThrowableException(var7);
         }
      }
   }

   public void addWebserviceCredentialProvider(WebserviceCredentialProviderMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 8)) {
         WebserviceCredentialProviderMBean[] var2;
         if (this._isSet(8)) {
            var2 = (WebserviceCredentialProviderMBean[])((WebserviceCredentialProviderMBean[])this._getHelper()._extendArray(this.getWebserviceCredentialProviders(), WebserviceCredentialProviderMBean.class, var1));
         } else {
            var2 = new WebserviceCredentialProviderMBean[]{var1};
         }

         try {
            this.setWebserviceCredentialProviders(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public WebserviceCredentialProviderMBean[] getWebserviceCredentialProviders() {
      return this._WebserviceCredentialProviders;
   }

   public boolean isWebserviceCredentialProvidersSet() {
      return this._isSet(8);
   }

   public void removeWebserviceCredentialProvider(WebserviceCredentialProviderMBean var1) {
      this.destroyWebserviceCredentialProvider(var1);
   }

   public void setWebserviceCredentialProviders(WebserviceCredentialProviderMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new WebserviceCredentialProviderMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 8)) {
            this._getReferenceManager().registerBean(var3, true);
            this._postCreate(var3);
         }
      }

      WebserviceCredentialProviderMBean[] var5 = this._WebserviceCredentialProviders;
      this._WebserviceCredentialProviders = (WebserviceCredentialProviderMBean[])var4;
      this._postSet(8, var5, var4);
   }

   public WebserviceCredentialProviderMBean lookupWebserviceCredentialProvider(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._WebserviceCredentialProviders).iterator();

      WebserviceCredentialProviderMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (WebserviceCredentialProviderMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public WebserviceCredentialProviderMBean createWebserviceCredentialProvider(String var1) {
      WebserviceCredentialProviderMBeanImpl var2 = new WebserviceCredentialProviderMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addWebserviceCredentialProvider(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public void destroyWebserviceCredentialProvider(WebserviceCredentialProviderMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 8);
         WebserviceCredentialProviderMBean[] var2 = this.getWebserviceCredentialProviders();
         WebserviceCredentialProviderMBean[] var3 = (WebserviceCredentialProviderMBean[])((WebserviceCredentialProviderMBean[])this._getHelper()._removeElement(var2, WebserviceCredentialProviderMBean.class, var1));
         if (var2.length != var3.length) {
            this._preDestroy((AbstractDescriptorBean)var1);

            try {
               AbstractDescriptorBean var4 = (AbstractDescriptorBean)var1;
               if (var4 == null) {
                  return;
               }

               List var5 = this._getReferenceManager().getResolvedReferences(var4);
               if (var5 != null && var5.size() > 0) {
                  throw new BeanRemoveRejectedException(var4, var5);
               }

               this._getReferenceManager().unregisterBean(var4);
               this._markDestroyed(var4);
               this.setWebserviceCredentialProviders(var3);
            } catch (Exception var6) {
               if (var6 instanceof RuntimeException) {
                  throw (RuntimeException)var6;
               }

               throw new UndeclaredThrowableException(var6);
            }
         }

      } catch (Exception var7) {
         if (var7 instanceof RuntimeException) {
            throw (RuntimeException)var7;
         } else {
            throw new UndeclaredThrowableException(var7);
         }
      }
   }

   public void addWebserviceSecurityToken(WebserviceSecurityTokenMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 9)) {
         WebserviceSecurityTokenMBean[] var2;
         if (this._isSet(9)) {
            var2 = (WebserviceSecurityTokenMBean[])((WebserviceSecurityTokenMBean[])this._getHelper()._extendArray(this.getWebserviceSecurityTokens(), WebserviceSecurityTokenMBean.class, var1));
         } else {
            var2 = new WebserviceSecurityTokenMBean[]{var1};
         }

         try {
            this.setWebserviceSecurityTokens(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public WebserviceSecurityTokenMBean[] getWebserviceSecurityTokens() {
      return this._WebserviceSecurityTokens;
   }

   public boolean isWebserviceSecurityTokensSet() {
      return this._isSet(9);
   }

   public void removeWebserviceSecurityToken(WebserviceSecurityTokenMBean var1) {
      this.destroyWebserviceSecurityToken(var1);
   }

   public void setWebserviceSecurityTokens(WebserviceSecurityTokenMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new WebserviceSecurityTokenMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 9)) {
            this._getReferenceManager().registerBean(var3, true);
            this._postCreate(var3);
         }
      }

      WebserviceSecurityTokenMBean[] var5 = this._WebserviceSecurityTokens;
      this._WebserviceSecurityTokens = (WebserviceSecurityTokenMBean[])var4;
      this._postSet(9, var5, var4);
   }

   public WebserviceSecurityTokenMBean lookupWebserviceSecurityToken(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._WebserviceSecurityTokens).iterator();

      WebserviceSecurityTokenMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (WebserviceSecurityTokenMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public WebserviceSecurityTokenMBean createWebserviceSecurityToken(String var1) {
      WebserviceSecurityTokenMBeanImpl var2 = new WebserviceSecurityTokenMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addWebserviceSecurityToken(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public void destroyWebserviceSecurityToken(WebserviceSecurityTokenMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 9);
         WebserviceSecurityTokenMBean[] var2 = this.getWebserviceSecurityTokens();
         WebserviceSecurityTokenMBean[] var3 = (WebserviceSecurityTokenMBean[])((WebserviceSecurityTokenMBean[])this._getHelper()._removeElement(var2, WebserviceSecurityTokenMBean.class, var1));
         if (var2.length != var3.length) {
            this._preDestroy((AbstractDescriptorBean)var1);

            try {
               AbstractDescriptorBean var4 = (AbstractDescriptorBean)var1;
               if (var4 == null) {
                  return;
               }

               List var5 = this._getReferenceManager().getResolvedReferences(var4);
               if (var5 != null && var5.size() > 0) {
                  throw new BeanRemoveRejectedException(var4, var5);
               }

               this._getReferenceManager().unregisterBean(var4);
               this._markDestroyed(var4);
               this.setWebserviceSecurityTokens(var3);
            } catch (Exception var6) {
               if (var6 instanceof RuntimeException) {
                  throw (RuntimeException)var6;
               }

               throw new UndeclaredThrowableException(var6);
            }
         }

      } catch (Exception var7) {
         if (var7 instanceof RuntimeException) {
            throw (RuntimeException)var7;
         } else {
            throw new UndeclaredThrowableException(var7);
         }
      }
   }

   public WebserviceTimestampMBean getWebserviceTimestamp() {
      return this._WebserviceTimestamp;
   }

   public boolean isWebserviceTimestampSet() {
      return this._isSet(10) || this._isAnythingSet((AbstractDescriptorBean)this.getWebserviceTimestamp());
   }

   public void setWebserviceTimestamp(WebserviceTimestampMBean var1) throws InvalidAttributeValueException {
      AbstractDescriptorBean var2 = (AbstractDescriptorBean)var1;
      if (this._setParent(var2, this, 10)) {
         this._postCreate(var2);
      }

      WebserviceTimestampMBean var3 = this._WebserviceTimestamp;
      this._WebserviceTimestamp = var1;
      this._postSet(10, var3, var1);
   }

   public String getDefaultCredentialProviderSTSURI() {
      return this._DefaultCredentialProviderSTSURI;
   }

   public boolean isDefaultCredentialProviderSTSURISet() {
      return this._isSet(11);
   }

   public void setDefaultCredentialProviderSTSURI(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._DefaultCredentialProviderSTSURI;
      this._DefaultCredentialProviderSTSURI = var1;
      this._postSet(11, var2, var1);
   }

   public String getPolicySelectionPreference() {
      return this._PolicySelectionPreference;
   }

   public boolean isPolicySelectionPreferenceSet() {
      return this._isSet(12);
   }

   public void setPolicySelectionPreference(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{"NONE", "SCP", "SPC", "CSP", "CPS", "PCS", "PSC"};
      var1 = weblogic.descriptor.beangen.LegalChecks.checkInEnum("PolicySelectionPreference", var1, var2);
      String var3 = this._PolicySelectionPreference;
      this._PolicySelectionPreference = var1;
      this._postSet(12, var3, var1);
   }

   public void setCompatibilityPreference(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._CompatibilityPreference;
      this._CompatibilityPreference = var1;
      this._postSet(13, var2, var1);
   }

   public String getCompatibilityPreference() {
      return this._CompatibilityPreference;
   }

   public boolean isCompatibilityPreferenceSet() {
      return this._isSet(13);
   }

   public void setCompatibilityOrderingPreference(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._CompatibilityOrderingPreference;
      this._CompatibilityOrderingPreference = var1;
      this._postSet(14, var2, var1);
   }

   public String getCompatibilityOrderingPreference() {
      return this._CompatibilityOrderingPreference;
   }

   public boolean isCompatibilityOrderingPreferenceSet() {
      return this._isSet(14);
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
      return super._isAnythingSet() || this.isWebserviceTimestampSet();
   }

   private boolean _initializeProperty(int var1) {
      boolean var2 = var1 > -1;
      if (!var2) {
         var1 = 14;
      }

      try {
         switch (var1) {
            case 14:
               this._CompatibilityOrderingPreference = null;
               if (var2) {
                  break;
               }
            case 13:
               this._CompatibilityPreference = null;
               if (var2) {
                  break;
               }
            case 11:
               this._DefaultCredentialProviderSTSURI = null;
               if (var2) {
                  break;
               }
            case 12:
               this._PolicySelectionPreference = "NONE";
               if (var2) {
                  break;
               }
            case 8:
               this._WebserviceCredentialProviders = new WebserviceCredentialProviderMBean[0];
               if (var2) {
                  break;
               }
            case 9:
               this._WebserviceSecurityTokens = new WebserviceSecurityTokenMBean[0];
               if (var2) {
                  break;
               }
            case 10:
               this._WebserviceTimestamp = new WebserviceTimestampMBeanImpl(this, 10);
               this._postCreate((AbstractDescriptorBean)this._WebserviceTimestamp);
               if (var2) {
                  break;
               }
            case 7:
               this._WebserviceTokenHandlers = new WebserviceTokenHandlerMBean[0];
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
      return "WebserviceSecurity";
   }

   public void putValue(String var1, Object var2) {
      String var7;
      if (var1.equals("CompatibilityOrderingPreference")) {
         var7 = this._CompatibilityOrderingPreference;
         this._CompatibilityOrderingPreference = (String)var2;
         this._postSet(14, var7, this._CompatibilityOrderingPreference);
      } else if (var1.equals("CompatibilityPreference")) {
         var7 = this._CompatibilityPreference;
         this._CompatibilityPreference = (String)var2;
         this._postSet(13, var7, this._CompatibilityPreference);
      } else if (var1.equals("DefaultCredentialProviderSTSURI")) {
         var7 = this._DefaultCredentialProviderSTSURI;
         this._DefaultCredentialProviderSTSURI = (String)var2;
         this._postSet(11, var7, this._DefaultCredentialProviderSTSURI);
      } else if (var1.equals("PolicySelectionPreference")) {
         var7 = this._PolicySelectionPreference;
         this._PolicySelectionPreference = (String)var2;
         this._postSet(12, var7, this._PolicySelectionPreference);
      } else if (var1.equals("WebserviceCredentialProviders")) {
         WebserviceCredentialProviderMBean[] var6 = this._WebserviceCredentialProviders;
         this._WebserviceCredentialProviders = (WebserviceCredentialProviderMBean[])((WebserviceCredentialProviderMBean[])var2);
         this._postSet(8, var6, this._WebserviceCredentialProviders);
      } else if (var1.equals("WebserviceSecurityTokens")) {
         WebserviceSecurityTokenMBean[] var5 = this._WebserviceSecurityTokens;
         this._WebserviceSecurityTokens = (WebserviceSecurityTokenMBean[])((WebserviceSecurityTokenMBean[])var2);
         this._postSet(9, var5, this._WebserviceSecurityTokens);
      } else if (var1.equals("WebserviceTimestamp")) {
         WebserviceTimestampMBean var4 = this._WebserviceTimestamp;
         this._WebserviceTimestamp = (WebserviceTimestampMBean)var2;
         this._postSet(10, var4, this._WebserviceTimestamp);
      } else if (var1.equals("WebserviceTokenHandlers")) {
         WebserviceTokenHandlerMBean[] var3 = this._WebserviceTokenHandlers;
         this._WebserviceTokenHandlers = (WebserviceTokenHandlerMBean[])((WebserviceTokenHandlerMBean[])var2);
         this._postSet(7, var3, this._WebserviceTokenHandlers);
      } else {
         super.putValue(var1, var2);
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("CompatibilityOrderingPreference")) {
         return this._CompatibilityOrderingPreference;
      } else if (var1.equals("CompatibilityPreference")) {
         return this._CompatibilityPreference;
      } else if (var1.equals("DefaultCredentialProviderSTSURI")) {
         return this._DefaultCredentialProviderSTSURI;
      } else if (var1.equals("PolicySelectionPreference")) {
         return this._PolicySelectionPreference;
      } else if (var1.equals("WebserviceCredentialProviders")) {
         return this._WebserviceCredentialProviders;
      } else if (var1.equals("WebserviceSecurityTokens")) {
         return this._WebserviceSecurityTokens;
      } else if (var1.equals("WebserviceTimestamp")) {
         return this._WebserviceTimestamp;
      } else {
         return var1.equals("WebserviceTokenHandlers") ? this._WebserviceTokenHandlers : super.getValue(var1);
      }
   }

   public static class SchemaHelper2 extends ConfigurationMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 20:
               if (var1.equals("webservice-timestamp")) {
                  return 10;
               }
            case 21:
            case 22:
            case 23:
            case 26:
            case 28:
            case 29:
            case 31:
            case 32:
            default:
               break;
            case 24:
               if (var1.equals("compatibility-preference")) {
                  return 13;
               }

               if (var1.equals("webservice-token-handler")) {
                  return 7;
               }
               break;
            case 25:
               if (var1.equals("webservice-security-token")) {
                  return 9;
               }
               break;
            case 27:
               if (var1.equals("policy-selection-preference")) {
                  return 12;
               }
               break;
            case 30:
               if (var1.equals("webservice-credential-provider")) {
                  return 8;
               }
               break;
            case 33:
               if (var1.equals("compatibility-ordering-preference")) {
                  return 14;
               }
               break;
            case 34:
               if (var1.equals("default-credential-providersts-uri")) {
                  return 11;
               }
         }

         return super.getPropertyIndex(var1);
      }

      public SchemaHelper getSchemaHelper(int var1) {
         switch (var1) {
            case 7:
               return new WebserviceTokenHandlerMBeanImpl.SchemaHelper2();
            case 8:
               return new WebserviceCredentialProviderMBeanImpl.SchemaHelper2();
            case 9:
               return new WebserviceSecurityTokenMBeanImpl.SchemaHelper2();
            case 10:
               return new WebserviceTimestampMBeanImpl.SchemaHelper2();
            default:
               return super.getSchemaHelper(var1);
         }
      }

      public String getElementName(int var1) {
         switch (var1) {
            case 7:
               return "webservice-token-handler";
            case 8:
               return "webservice-credential-provider";
            case 9:
               return "webservice-security-token";
            case 10:
               return "webservice-timestamp";
            case 11:
               return "default-credential-providersts-uri";
            case 12:
               return "policy-selection-preference";
            case 13:
               return "compatibility-preference";
            case 14:
               return "compatibility-ordering-preference";
            default:
               return super.getElementName(var1);
         }
      }

      public boolean isArray(int var1) {
         switch (var1) {
            case 7:
               return true;
            case 8:
               return true;
            case 9:
               return true;
            default:
               return super.isArray(var1);
         }
      }

      public boolean isBean(int var1) {
         switch (var1) {
            case 7:
               return true;
            case 8:
               return true;
            case 9:
               return true;
            case 10:
               return true;
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

   protected static class Helper extends ConfigurationMBeanImpl.Helper {
      private WebserviceSecurityMBeanImpl bean;

      protected Helper(WebserviceSecurityMBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 7:
               return "WebserviceTokenHandlers";
            case 8:
               return "WebserviceCredentialProviders";
            case 9:
               return "WebserviceSecurityTokens";
            case 10:
               return "WebserviceTimestamp";
            case 11:
               return "DefaultCredentialProviderSTSURI";
            case 12:
               return "PolicySelectionPreference";
            case 13:
               return "CompatibilityPreference";
            case 14:
               return "CompatibilityOrderingPreference";
            default:
               return super.getPropertyName(var1);
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("CompatibilityOrderingPreference")) {
            return 14;
         } else if (var1.equals("CompatibilityPreference")) {
            return 13;
         } else if (var1.equals("DefaultCredentialProviderSTSURI")) {
            return 11;
         } else if (var1.equals("PolicySelectionPreference")) {
            return 12;
         } else if (var1.equals("WebserviceCredentialProviders")) {
            return 8;
         } else if (var1.equals("WebserviceSecurityTokens")) {
            return 9;
         } else if (var1.equals("WebserviceTimestamp")) {
            return 10;
         } else {
            return var1.equals("WebserviceTokenHandlers") ? 7 : super.getPropertyIndex(var1);
         }
      }

      public Iterator getChildren() {
         ArrayList var1 = new ArrayList();
         var1.add(new ArrayIterator(this.bean.getWebserviceCredentialProviders()));
         var1.add(new ArrayIterator(this.bean.getWebserviceSecurityTokens()));
         if (this.bean.getWebserviceTimestamp() != null) {
            var1.add(new ArrayIterator(new WebserviceTimestampMBean[]{this.bean.getWebserviceTimestamp()}));
         }

         var1.add(new ArrayIterator(this.bean.getWebserviceTokenHandlers()));
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
            if (this.bean.isCompatibilityOrderingPreferenceSet()) {
               var2.append("CompatibilityOrderingPreference");
               var2.append(String.valueOf(this.bean.getCompatibilityOrderingPreference()));
            }

            if (this.bean.isCompatibilityPreferenceSet()) {
               var2.append("CompatibilityPreference");
               var2.append(String.valueOf(this.bean.getCompatibilityPreference()));
            }

            if (this.bean.isDefaultCredentialProviderSTSURISet()) {
               var2.append("DefaultCredentialProviderSTSURI");
               var2.append(String.valueOf(this.bean.getDefaultCredentialProviderSTSURI()));
            }

            if (this.bean.isPolicySelectionPreferenceSet()) {
               var2.append("PolicySelectionPreference");
               var2.append(String.valueOf(this.bean.getPolicySelectionPreference()));
            }

            var5 = 0L;

            int var7;
            for(var7 = 0; var7 < this.bean.getWebserviceCredentialProviders().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getWebserviceCredentialProviders()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = 0L;

            for(var7 = 0; var7 < this.bean.getWebserviceSecurityTokens().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getWebserviceSecurityTokens()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = this.computeChildHashValue(this.bean.getWebserviceTimestamp());
            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = 0L;

            for(var7 = 0; var7 < this.bean.getWebserviceTokenHandlers().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getWebserviceTokenHandlers()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var1.update(var2.toString().getBytes());
            return var1.getValue();
         } catch (Exception var8) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var8);
         }
      }

      protected void computeDiff(AbstractDescriptorBean var1) {
         try {
            super.computeDiff(var1);
            WebserviceSecurityMBeanImpl var2 = (WebserviceSecurityMBeanImpl)var1;
            this.computeDiff("CompatibilityOrderingPreference", this.bean.getCompatibilityOrderingPreference(), var2.getCompatibilityOrderingPreference(), false);
            this.computeDiff("CompatibilityPreference", this.bean.getCompatibilityPreference(), var2.getCompatibilityPreference(), false);
            this.computeDiff("DefaultCredentialProviderSTSURI", this.bean.getDefaultCredentialProviderSTSURI(), var2.getDefaultCredentialProviderSTSURI(), false);
            this.computeDiff("PolicySelectionPreference", this.bean.getPolicySelectionPreference(), var2.getPolicySelectionPreference(), true);
            this.computeChildDiff("WebserviceCredentialProviders", this.bean.getWebserviceCredentialProviders(), var2.getWebserviceCredentialProviders(), true);
            this.computeChildDiff("WebserviceSecurityTokens", this.bean.getWebserviceSecurityTokens(), var2.getWebserviceSecurityTokens(), true);
            this.computeSubDiff("WebserviceTimestamp", this.bean.getWebserviceTimestamp(), var2.getWebserviceTimestamp());
            this.computeChildDiff("WebserviceTokenHandlers", this.bean.getWebserviceTokenHandlers(), var2.getWebserviceTokenHandlers(), true);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            WebserviceSecurityMBeanImpl var3 = (WebserviceSecurityMBeanImpl)var1.getSourceBean();
            WebserviceSecurityMBeanImpl var4 = (WebserviceSecurityMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("CompatibilityOrderingPreference")) {
                  var3.setCompatibilityOrderingPreference(var4.getCompatibilityOrderingPreference());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 14);
               } else if (var5.equals("CompatibilityPreference")) {
                  var3.setCompatibilityPreference(var4.getCompatibilityPreference());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 13);
               } else if (var5.equals("DefaultCredentialProviderSTSURI")) {
                  var3.setDefaultCredentialProviderSTSURI(var4.getDefaultCredentialProviderSTSURI());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 11);
               } else if (var5.equals("PolicySelectionPreference")) {
                  var3.setPolicySelectionPreference(var4.getPolicySelectionPreference());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 12);
               } else if (var5.equals("WebserviceCredentialProviders")) {
                  if (var6 == 2) {
                     var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                     var3.addWebserviceCredentialProvider((WebserviceCredentialProviderMBean)var2.getAddedObject());
                  } else {
                     if (var6 != 3) {
                        throw new AssertionError("Invalid type: " + var6);
                     }

                     var3.removeWebserviceCredentialProvider((WebserviceCredentialProviderMBean)var2.getRemovedObject());
                  }

                  if (var3.getWebserviceCredentialProviders() == null || var3.getWebserviceCredentialProviders().length == 0) {
                     var3._conditionalUnset(var2.isUnsetUpdate(), 8);
                  }
               } else if (var5.equals("WebserviceSecurityTokens")) {
                  if (var6 == 2) {
                     var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                     var3.addWebserviceSecurityToken((WebserviceSecurityTokenMBean)var2.getAddedObject());
                  } else {
                     if (var6 != 3) {
                        throw new AssertionError("Invalid type: " + var6);
                     }

                     var3.removeWebserviceSecurityToken((WebserviceSecurityTokenMBean)var2.getRemovedObject());
                  }

                  if (var3.getWebserviceSecurityTokens() == null || var3.getWebserviceSecurityTokens().length == 0) {
                     var3._conditionalUnset(var2.isUnsetUpdate(), 9);
                  }
               } else if (var5.equals("WebserviceTimestamp")) {
                  if (var6 == 2) {
                     var3.setWebserviceTimestamp((WebserviceTimestampMBean)this.createCopy((AbstractDescriptorBean)var4.getWebserviceTimestamp()));
                  } else {
                     if (var6 != 3) {
                        throw new AssertionError("Invalid type: " + var6);
                     }

                     var3._destroySingleton("WebserviceTimestamp", var3.getWebserviceTimestamp());
                  }

                  var3._conditionalUnset(var2.isUnsetUpdate(), 10);
               } else if (var5.equals("WebserviceTokenHandlers")) {
                  if (var6 == 2) {
                     var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                     var3.addWebserviceTokenHandler((WebserviceTokenHandlerMBean)var2.getAddedObject());
                  } else {
                     if (var6 != 3) {
                        throw new AssertionError("Invalid type: " + var6);
                     }

                     var3.removeWebserviceTokenHandler((WebserviceTokenHandlerMBean)var2.getRemovedObject());
                  }

                  if (var3.getWebserviceTokenHandlers() == null || var3.getWebserviceTokenHandlers().length == 0) {
                     var3._conditionalUnset(var2.isUnsetUpdate(), 7);
                  }
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
            WebserviceSecurityMBeanImpl var5 = (WebserviceSecurityMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("CompatibilityOrderingPreference")) && this.bean.isCompatibilityOrderingPreferenceSet()) {
               var5.setCompatibilityOrderingPreference(this.bean.getCompatibilityOrderingPreference());
            }

            if ((var3 == null || !var3.contains("CompatibilityPreference")) && this.bean.isCompatibilityPreferenceSet()) {
               var5.setCompatibilityPreference(this.bean.getCompatibilityPreference());
            }

            if ((var3 == null || !var3.contains("DefaultCredentialProviderSTSURI")) && this.bean.isDefaultCredentialProviderSTSURISet()) {
               var5.setDefaultCredentialProviderSTSURI(this.bean.getDefaultCredentialProviderSTSURI());
            }

            if ((var3 == null || !var3.contains("PolicySelectionPreference")) && this.bean.isPolicySelectionPreferenceSet()) {
               var5.setPolicySelectionPreference(this.bean.getPolicySelectionPreference());
            }

            int var8;
            if ((var3 == null || !var3.contains("WebserviceCredentialProviders")) && this.bean.isWebserviceCredentialProvidersSet() && !var5._isSet(8)) {
               WebserviceCredentialProviderMBean[] var6 = this.bean.getWebserviceCredentialProviders();
               WebserviceCredentialProviderMBean[] var7 = new WebserviceCredentialProviderMBean[var6.length];

               for(var8 = 0; var8 < var7.length; ++var8) {
                  var7[var8] = (WebserviceCredentialProviderMBean)((WebserviceCredentialProviderMBean)this.createCopy((AbstractDescriptorBean)var6[var8], var2));
               }

               var5.setWebserviceCredentialProviders(var7);
            }

            if ((var3 == null || !var3.contains("WebserviceSecurityTokens")) && this.bean.isWebserviceSecurityTokensSet() && !var5._isSet(9)) {
               WebserviceSecurityTokenMBean[] var11 = this.bean.getWebserviceSecurityTokens();
               WebserviceSecurityTokenMBean[] var13 = new WebserviceSecurityTokenMBean[var11.length];

               for(var8 = 0; var8 < var13.length; ++var8) {
                  var13[var8] = (WebserviceSecurityTokenMBean)((WebserviceSecurityTokenMBean)this.createCopy((AbstractDescriptorBean)var11[var8], var2));
               }

               var5.setWebserviceSecurityTokens(var13);
            }

            if ((var3 == null || !var3.contains("WebserviceTimestamp")) && this.bean.isWebserviceTimestampSet() && !var5._isSet(10)) {
               WebserviceTimestampMBean var4 = this.bean.getWebserviceTimestamp();
               var5.setWebserviceTimestamp((WebserviceTimestampMBean)null);
               var5.setWebserviceTimestamp(var4 == null ? null : (WebserviceTimestampMBean)this.createCopy((AbstractDescriptorBean)var4, var2));
            }

            if ((var3 == null || !var3.contains("WebserviceTokenHandlers")) && this.bean.isWebserviceTokenHandlersSet() && !var5._isSet(7)) {
               WebserviceTokenHandlerMBean[] var12 = this.bean.getWebserviceTokenHandlers();
               WebserviceTokenHandlerMBean[] var14 = new WebserviceTokenHandlerMBean[var12.length];

               for(var8 = 0; var8 < var14.length; ++var8) {
                  var14[var8] = (WebserviceTokenHandlerMBean)((WebserviceTokenHandlerMBean)this.createCopy((AbstractDescriptorBean)var12[var8], var2));
               }

               var5.setWebserviceTokenHandlers(var14);
            }

            return var5;
         } catch (RuntimeException var9) {
            throw var9;
         } catch (Exception var10) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var10);
         }
      }

      protected void inferSubTree(Class var1, Object var2) {
         super.inferSubTree(var1, var2);
         Object var3 = null;
         this.inferSubTree(this.bean.getWebserviceCredentialProviders(), var1, var2);
         this.inferSubTree(this.bean.getWebserviceSecurityTokens(), var1, var2);
         this.inferSubTree(this.bean.getWebserviceTimestamp(), var1, var2);
         this.inferSubTree(this.bean.getWebserviceTokenHandlers(), var1, var2);
      }
   }
}
