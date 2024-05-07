package weblogic.management.configuration;

import java.io.Serializable;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.zip.CRC32;
import javax.management.InvalidAttributeValueException;
import weblogic.descriptor.BeanRemoveRejectedException;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.beangen.StringHelper;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBeanHelper;
import weblogic.descriptor.internal.Munger;
import weblogic.descriptor.internal.SchemaHelper;
import weblogic.utils.ArrayUtils;
import weblogic.utils.collections.ArrayIterator;
import weblogic.utils.collections.CombinedIterator;

public class ForeignJNDIProviderMBeanImpl extends DeploymentMBeanImpl implements ForeignJNDIProviderMBean, Serializable {
   private ForeignJNDILinkMBean[] _ForeignJNDILinks;
   private String _InitialContextFactory;
   private String _Password;
   private byte[] _PasswordEncrypted;
   private Properties _Properties;
   private String _ProviderURL;
   private String _User;
   private static SchemaHelper2 _schemaHelper;

   public ForeignJNDIProviderMBeanImpl() {
      this._initializeProperty(-1);
   }

   public ForeignJNDIProviderMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);
      this._initializeProperty(-1);
   }

   public String getInitialContextFactory() {
      return this._InitialContextFactory;
   }

   public boolean isInitialContextFactorySet() {
      return this._isSet(9);
   }

   public void setInitialContextFactory(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._InitialContextFactory;
      this._InitialContextFactory = var1;
      this._postSet(9, var2, var1);
   }

   public String getProviderURL() {
      return this._ProviderURL;
   }

   public boolean isProviderURLSet() {
      return this._isSet(10);
   }

   public void setProviderURL(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._ProviderURL;
      this._ProviderURL = var1;
      this._postSet(10, var2, var1);
   }

   public String getPassword() {
      byte[] var1 = this.getPasswordEncrypted();
      return var1 == null ? null : this._decrypt("Password", var1);
   }

   public boolean isPasswordSet() {
      return this.isPasswordEncryptedSet();
   }

   public void setPassword(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      this.setPasswordEncrypted(var1 == null ? null : this._encrypt("Password", var1));
   }

   public byte[] getPasswordEncrypted() {
      return this._getHelper()._cloneArray(this._PasswordEncrypted);
   }

   public String getPasswordEncryptedAsString() {
      byte[] var1 = this.getPasswordEncrypted();
      return var1 == null ? null : new String(var1);
   }

   public boolean isPasswordEncryptedSet() {
      return this._isSet(12);
   }

   public void setPasswordEncryptedAsString(String var1) {
      try {
         byte[] var2 = var1 == null ? null : var1.getBytes();
         this.setPasswordEncrypted(var2);
      } catch (Exception var3) {
         if (var3 instanceof RuntimeException) {
            throw (RuntimeException)var3;
         } else {
            throw new UndeclaredThrowableException(var3);
         }
      }
   }

   public String getUser() {
      return this._User;
   }

   public boolean isUserSet() {
      return this._isSet(13);
   }

   public void setUser(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._User;
      this._User = var1;
      this._postSet(13, var2, var1);
   }

   public void addForeignJNDILink(ForeignJNDILinkMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 14)) {
         ForeignJNDILinkMBean[] var2;
         if (this._isSet(14)) {
            var2 = (ForeignJNDILinkMBean[])((ForeignJNDILinkMBean[])this._getHelper()._extendArray(this.getForeignJNDILinks(), ForeignJNDILinkMBean.class, var1));
         } else {
            var2 = new ForeignJNDILinkMBean[]{var1};
         }

         try {
            this.setForeignJNDILinks(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public ForeignJNDILinkMBean[] getForeignJNDILinks() {
      return this._ForeignJNDILinks;
   }

   public boolean isForeignJNDILinksSet() {
      return this._isSet(14);
   }

   public void removeForeignJNDILink(ForeignJNDILinkMBean var1) {
      this.destroyForeignJNDILink(var1);
   }

   public void setForeignJNDILinks(ForeignJNDILinkMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new ForeignJNDILinkMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 14)) {
            this._getReferenceManager().registerBean(var3, false);
            this._postCreate(var3);
         }
      }

      ForeignJNDILinkMBean[] var5 = this._ForeignJNDILinks;
      this._ForeignJNDILinks = (ForeignJNDILinkMBean[])var4;
      this._postSet(14, var5, var4);
   }

   public ForeignJNDILinkMBean lookupForeignJNDILink(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._ForeignJNDILinks).iterator();

      ForeignJNDILinkMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (ForeignJNDILinkMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public ForeignJNDILinkMBean createForeignJNDILink(String var1) {
      ForeignJNDILinkMBeanImpl var2 = new ForeignJNDILinkMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addForeignJNDILink(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public void destroyForeignJNDILink(ForeignJNDILinkMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 14);
         ForeignJNDILinkMBean[] var2 = this.getForeignJNDILinks();
         ForeignJNDILinkMBean[] var3 = (ForeignJNDILinkMBean[])((ForeignJNDILinkMBean[])this._getHelper()._removeElement(var2, ForeignJNDILinkMBean.class, var1));
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
               this.setForeignJNDILinks(var3);
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

   public void setProperties(Properties var1) throws InvalidAttributeValueException {
      Properties var2 = this._Properties;
      this._Properties = var1;
      this._postSet(15, var2, var1);
   }

   public Properties getProperties() {
      return this._Properties;
   }

   public String getPropertiesAsString() {
      return StringHelper.objectToString(this.getProperties());
   }

   public boolean isPropertiesSet() {
      return this._isSet(15);
   }

   public void setPropertiesAsString(String var1) {
      try {
         this.setProperties(StringHelper.stringToProperties(var1));
      } catch (Exception var3) {
         if (var3 instanceof RuntimeException) {
            throw (RuntimeException)var3;
         } else {
            throw new UndeclaredThrowableException(var3);
         }
      }
   }

   public Object _getKey() {
      return super._getKey();
   }

   public void _validate() throws IllegalArgumentException {
      super._validate();
   }

   public void setPasswordEncrypted(byte[] var1) {
      byte[] var2 = this._PasswordEncrypted;
      if (this._isProductionModeEnabled() && var1 != null && !this._isEncrypted(var1)) {
         throw new IllegalArgumentException("In production mode, it's not allowed to set a clear text value to the property: PasswordEncrypted of ForeignJNDIProviderMBean");
      } else {
         this._getHelper()._clearArray(this._PasswordEncrypted);
         this._PasswordEncrypted = this._getHelper()._cloneArray(var1);
         this._postSet(12, var2, var1);
      }
   }

   protected void _unSet(int var1) {
      if (!this._initializeProperty(var1)) {
         super._unSet(var1);
      } else {
         this._markSet(var1, false);
         if (var1 == 11) {
            this._markSet(12, false);
         }
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
         var1 = 14;
      }

      try {
         switch (var1) {
            case 14:
               this._ForeignJNDILinks = new ForeignJNDILinkMBean[0];
               if (var2) {
                  break;
               }
            case 9:
               this._InitialContextFactory = null;
               if (var2) {
                  break;
               }
            case 11:
               this._PasswordEncrypted = null;
               if (var2) {
                  break;
               }
            case 12:
               this._PasswordEncrypted = null;
               if (var2) {
                  break;
               }
            case 15:
               this._Properties = null;
               if (var2) {
                  break;
               }
            case 10:
               this._ProviderURL = null;
               if (var2) {
                  break;
               }
            case 13:
               this._User = null;
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
      return "ForeignJNDIProvider";
   }

   public void putValue(String var1, Object var2) {
      if (var1.equals("ForeignJNDILinks")) {
         ForeignJNDILinkMBean[] var6 = this._ForeignJNDILinks;
         this._ForeignJNDILinks = (ForeignJNDILinkMBean[])((ForeignJNDILinkMBean[])var2);
         this._postSet(14, var6, this._ForeignJNDILinks);
      } else {
         String var3;
         if (var1.equals("InitialContextFactory")) {
            var3 = this._InitialContextFactory;
            this._InitialContextFactory = (String)var2;
            this._postSet(9, var3, this._InitialContextFactory);
         } else if (var1.equals("Password")) {
            var3 = this._Password;
            this._Password = (String)var2;
            this._postSet(11, var3, this._Password);
         } else if (var1.equals("PasswordEncrypted")) {
            byte[] var5 = this._PasswordEncrypted;
            this._PasswordEncrypted = (byte[])((byte[])var2);
            this._postSet(12, var5, this._PasswordEncrypted);
         } else if (var1.equals("Properties")) {
            Properties var4 = this._Properties;
            this._Properties = (Properties)var2;
            this._postSet(15, var4, this._Properties);
         } else if (var1.equals("ProviderURL")) {
            var3 = this._ProviderURL;
            this._ProviderURL = (String)var2;
            this._postSet(10, var3, this._ProviderURL);
         } else if (var1.equals("User")) {
            var3 = this._User;
            this._User = (String)var2;
            this._postSet(13, var3, this._User);
         } else {
            super.putValue(var1, var2);
         }
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("ForeignJNDILinks")) {
         return this._ForeignJNDILinks;
      } else if (var1.equals("InitialContextFactory")) {
         return this._InitialContextFactory;
      } else if (var1.equals("Password")) {
         return this._Password;
      } else if (var1.equals("PasswordEncrypted")) {
         return this._PasswordEncrypted;
      } else if (var1.equals("Properties")) {
         return this._Properties;
      } else if (var1.equals("ProviderURL")) {
         return this._ProviderURL;
      } else {
         return var1.equals("User") ? this._User : super.getValue(var1);
      }
   }

   public static class SchemaHelper2 extends DeploymentMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 4:
               if (var1.equals("user")) {
                  return 13;
               }
            case 5:
            case 6:
            case 7:
            case 9:
            case 11:
            case 13:
            case 14:
            case 15:
            case 16:
            case 19:
            case 20:
            case 21:
            case 22:
            default:
               break;
            case 8:
               if (var1.equals("password")) {
                  return 11;
               }
               break;
            case 10:
               if (var1.equals("properties")) {
                  return 15;
               }
               break;
            case 12:
               if (var1.equals("provider-url")) {
                  return 10;
               }
               break;
            case 17:
               if (var1.equals("foreign-jndi-link")) {
                  return 14;
               }
               break;
            case 18:
               if (var1.equals("password-encrypted")) {
                  return 12;
               }
               break;
            case 23:
               if (var1.equals("initial-context-factory")) {
                  return 9;
               }
         }

         return super.getPropertyIndex(var1);
      }

      public SchemaHelper getSchemaHelper(int var1) {
         switch (var1) {
            case 14:
               return new ForeignJNDILinkMBeanImpl.SchemaHelper2();
            default:
               return super.getSchemaHelper(var1);
         }
      }

      public String getElementName(int var1) {
         switch (var1) {
            case 9:
               return "initial-context-factory";
            case 10:
               return "provider-url";
            case 11:
               return "password";
            case 12:
               return "password-encrypted";
            case 13:
               return "user";
            case 14:
               return "foreign-jndi-link";
            case 15:
               return "properties";
            default:
               return super.getElementName(var1);
         }
      }

      public boolean isArray(int var1) {
         switch (var1) {
            case 7:
               return true;
            case 14:
               return true;
            default:
               return super.isArray(var1);
         }
      }

      public boolean isBean(int var1) {
         switch (var1) {
            case 14:
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

   protected static class Helper extends DeploymentMBeanImpl.Helper {
      private ForeignJNDIProviderMBeanImpl bean;

      protected Helper(ForeignJNDIProviderMBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 9:
               return "InitialContextFactory";
            case 10:
               return "ProviderURL";
            case 11:
               return "Password";
            case 12:
               return "PasswordEncrypted";
            case 13:
               return "User";
            case 14:
               return "ForeignJNDILinks";
            case 15:
               return "Properties";
            default:
               return super.getPropertyName(var1);
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("ForeignJNDILinks")) {
            return 14;
         } else if (var1.equals("InitialContextFactory")) {
            return 9;
         } else if (var1.equals("Password")) {
            return 11;
         } else if (var1.equals("PasswordEncrypted")) {
            return 12;
         } else if (var1.equals("Properties")) {
            return 15;
         } else if (var1.equals("ProviderURL")) {
            return 10;
         } else {
            return var1.equals("User") ? 13 : super.getPropertyIndex(var1);
         }
      }

      public Iterator getChildren() {
         ArrayList var1 = new ArrayList();
         var1.add(new ArrayIterator(this.bean.getForeignJNDILinks()));
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
            var5 = 0L;

            for(int var7 = 0; var7 < this.bean.getForeignJNDILinks().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getForeignJNDILinks()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            if (this.bean.isInitialContextFactorySet()) {
               var2.append("InitialContextFactory");
               var2.append(String.valueOf(this.bean.getInitialContextFactory()));
            }

            if (this.bean.isPasswordSet()) {
               var2.append("Password");
               var2.append(String.valueOf(this.bean.getPassword()));
            }

            if (this.bean.isPasswordEncryptedSet()) {
               var2.append("PasswordEncrypted");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getPasswordEncrypted())));
            }

            if (this.bean.isPropertiesSet()) {
               var2.append("Properties");
               var2.append(String.valueOf(this.bean.getProperties()));
            }

            if (this.bean.isProviderURLSet()) {
               var2.append("ProviderURL");
               var2.append(String.valueOf(this.bean.getProviderURL()));
            }

            if (this.bean.isUserSet()) {
               var2.append("User");
               var2.append(String.valueOf(this.bean.getUser()));
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
            ForeignJNDIProviderMBeanImpl var2 = (ForeignJNDIProviderMBeanImpl)var1;
            this.computeChildDiff("ForeignJNDILinks", this.bean.getForeignJNDILinks(), var2.getForeignJNDILinks(), true);
            this.computeDiff("InitialContextFactory", this.bean.getInitialContextFactory(), var2.getInitialContextFactory(), true);
            this.computeDiff("PasswordEncrypted", this.bean.getPasswordEncrypted(), var2.getPasswordEncrypted(), true);
            this.computeDiff("Properties", this.bean.getProperties(), var2.getProperties(), true);
            this.computeDiff("ProviderURL", this.bean.getProviderURL(), var2.getProviderURL(), true);
            this.computeDiff("User", this.bean.getUser(), var2.getUser(), true);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            ForeignJNDIProviderMBeanImpl var3 = (ForeignJNDIProviderMBeanImpl)var1.getSourceBean();
            ForeignJNDIProviderMBeanImpl var4 = (ForeignJNDIProviderMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("ForeignJNDILinks")) {
                  if (var6 == 2) {
                     var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                     var3.addForeignJNDILink((ForeignJNDILinkMBean)var2.getAddedObject());
                  } else {
                     if (var6 != 3) {
                        throw new AssertionError("Invalid type: " + var6);
                     }

                     var3.removeForeignJNDILink((ForeignJNDILinkMBean)var2.getRemovedObject());
                  }

                  if (var3.getForeignJNDILinks() == null || var3.getForeignJNDILinks().length == 0) {
                     var3._conditionalUnset(var2.isUnsetUpdate(), 14);
                  }
               } else if (var5.equals("InitialContextFactory")) {
                  var3.setInitialContextFactory(var4.getInitialContextFactory());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 9);
               } else if (!var5.equals("Password")) {
                  if (var5.equals("PasswordEncrypted")) {
                     var3.setPasswordEncrypted(var4.getPasswordEncrypted());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 12);
                  } else if (var5.equals("Properties")) {
                     var3.setProperties(var4.getProperties() == null ? null : (Properties)var4.getProperties().clone());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 15);
                  } else if (var5.equals("ProviderURL")) {
                     var3.setProviderURL(var4.getProviderURL());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 10);
                  } else if (var5.equals("User")) {
                     var3.setUser(var4.getUser());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 13);
                  } else {
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
            ForeignJNDIProviderMBeanImpl var5 = (ForeignJNDIProviderMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("ForeignJNDILinks")) && this.bean.isForeignJNDILinksSet() && !var5._isSet(14)) {
               ForeignJNDILinkMBean[] var6 = this.bean.getForeignJNDILinks();
               ForeignJNDILinkMBean[] var7 = new ForeignJNDILinkMBean[var6.length];

               for(int var8 = 0; var8 < var7.length; ++var8) {
                  var7[var8] = (ForeignJNDILinkMBean)((ForeignJNDILinkMBean)this.createCopy((AbstractDescriptorBean)var6[var8], var2));
               }

               var5.setForeignJNDILinks(var7);
            }

            if ((var3 == null || !var3.contains("InitialContextFactory")) && this.bean.isInitialContextFactorySet()) {
               var5.setInitialContextFactory(this.bean.getInitialContextFactory());
            }

            if ((var3 == null || !var3.contains("PasswordEncrypted")) && this.bean.isPasswordEncryptedSet()) {
               byte[] var4 = this.bean.getPasswordEncrypted();
               var5.setPasswordEncrypted(var4 == null ? null : (byte[])((byte[])((byte[])((byte[])var4)).clone()));
            }

            if ((var3 == null || !var3.contains("Properties")) && this.bean.isPropertiesSet()) {
               var5.setProperties(this.bean.getProperties());
            }

            if ((var3 == null || !var3.contains("ProviderURL")) && this.bean.isProviderURLSet()) {
               var5.setProviderURL(this.bean.getProviderURL());
            }

            if ((var3 == null || !var3.contains("User")) && this.bean.isUserSet()) {
               var5.setUser(this.bean.getUser());
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
         this.inferSubTree(this.bean.getForeignJNDILinks(), var1, var2);
      }
   }
}
