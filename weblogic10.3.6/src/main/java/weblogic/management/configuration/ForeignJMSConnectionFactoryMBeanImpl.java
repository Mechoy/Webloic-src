package weblogic.management.configuration;

import java.io.Serializable;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.zip.CRC32;
import javax.management.AttributeNotFoundException;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanException;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.BootstrapProperties;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBeanHelper;
import weblogic.descriptor.internal.Munger;
import weblogic.descriptor.internal.SchemaHelper;
import weblogic.j2ee.descriptor.wl.ForeignConnectionFactoryBean;
import weblogic.management.ManagementException;
import weblogic.management.mbeans.custom.ForeignJMSConnectionFactory;
import weblogic.utils.ArrayUtils;
import weblogic.utils.collections.CombinedIterator;

public class ForeignJMSConnectionFactoryMBeanImpl extends ForeignJNDIObjectMBeanImpl implements ForeignJMSConnectionFactoryMBean, Serializable {
   private String _ConnectionHealthChecking;
   private String _LocalJNDIName;
   private String _Name;
   private String _Password;
   private byte[] _PasswordEncrypted;
   private String _RemoteJNDIName;
   private String _Username;
   private ForeignJMSConnectionFactory _customizer;
   private static SchemaHelper2 _schemaHelper;

   public ForeignJMSConnectionFactoryMBeanImpl() {
      try {
         this._customizer = new ForeignJMSConnectionFactory(this);
      } catch (Exception var2) {
         if (var2 instanceof RuntimeException) {
            throw (RuntimeException)var2;
         }

         throw new UndeclaredThrowableException(var2);
      }

      this._initializeProperty(-1);
   }

   public ForeignJMSConnectionFactoryMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);

      try {
         this._customizer = new ForeignJMSConnectionFactory(this);
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         }

         throw new UndeclaredThrowableException(var4);
      }

      this._initializeProperty(-1);
   }

   public String getName() {
      if (!this._isSet(2)) {
         try {
            return ((ConfigurationMBean)this.getParent()).getName();
         } catch (NullPointerException var2) {
         }
      }

      return this._customizer.getName();
   }

   public boolean isNameSet() {
      return this._isSet(2);
   }

   public void setLocalJNDIName(String var1) {
      var1 = var1 == null ? null : var1.trim();
      weblogic.descriptor.beangen.LegalChecks.checkNonNull("LocalJNDIName", var1);
      String var2 = this.getLocalJNDIName();

      try {
         this._customizer.setLocalJNDIName(var1);
      } catch (InvalidAttributeValueException var4) {
         throw new UndeclaredThrowableException(var4);
      }

      this._postSet(7, var2, var1);
   }

   public String getLocalJNDIName() {
      return this._customizer.getLocalJNDIName();
   }

   public boolean isLocalJNDINameSet() {
      return this._isSet(7);
   }

   public void setName(String var1) throws InvalidAttributeValueException, ManagementException {
      var1 = var1 == null ? null : var1.trim();
      weblogic.descriptor.beangen.LegalChecks.checkNonEmptyString("Name", var1);
      weblogic.descriptor.beangen.LegalChecks.checkNonNull("Name", var1);
      ConfigurationValidator.validateName(var1);
      String var2 = this.getName();
      this._customizer.setName(var1);
      this._postSet(2, var2, var1);
   }

   public void setRemoteJNDIName(String var1) {
      var1 = var1 == null ? null : var1.trim();
      weblogic.descriptor.beangen.LegalChecks.checkNonNull("RemoteJNDIName", var1);
      String var2 = this.getRemoteJNDIName();

      try {
         this._customizer.setRemoteJNDIName(var1);
      } catch (InvalidAttributeValueException var4) {
         throw new UndeclaredThrowableException(var4);
      }

      this._postSet(8, var2, var1);
   }

   public String getRemoteJNDIName() {
      return this._customizer.getRemoteJNDIName();
   }

   public boolean isRemoteJNDINameSet() {
      return this._isSet(8);
   }

   public void setUsername(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this.getUsername();

      try {
         this._customizer.setUsername(var1);
      } catch (InvalidAttributeValueException var4) {
         throw new UndeclaredThrowableException(var4);
      }

      this._postSet(9, var2, var1);
   }

   public String getUsername() {
      return this._customizer.getUsername();
   }

   public boolean isUsernameSet() {
      return this._isSet(9);
   }

   public void setPassword(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this.getPassword();

      try {
         this._customizer.setPassword(var1);
      } catch (InvalidAttributeValueException var4) {
         throw new UndeclaredThrowableException(var4);
      }

      this._postSet(10, var2, var1);
   }

   public String getPassword() {
      return this._customizer.getPassword();
   }

   public boolean isPasswordSet() {
      return this._isSet(10);
   }

   public void setConnectionHealthChecking(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._ConnectionHealthChecking;
      this._ConnectionHealthChecking = var1;
      this._postSet(11, var2, var1);
   }

   public String getConnectionHealthChecking() {
      return this._ConnectionHealthChecking;
   }

   public boolean isConnectionHealthCheckingSet() {
      return this._isSet(11);
   }

   public byte[] getPasswordEncrypted() {
      return this._PasswordEncrypted;
   }

   public boolean isPasswordEncryptedSet() {
      return this._isSet(12);
   }

   public void touch() throws ConfigurationException {
      this._customizer.touch();
   }

   public void freezeCurrentValue(String var1) throws AttributeNotFoundException, MBeanException {
      this._customizer.freezeCurrentValue(var1);
   }

   public void setPasswordEncrypted(byte[] var1) {
      byte[] var2 = this._PasswordEncrypted;
      this._PasswordEncrypted = var1;
      this._postSet(12, var2, var1);
   }

   public void restoreDefaultValue(String var1) throws AttributeNotFoundException {
      this._customizer.restoreDefaultValue(var1);
   }

   public void useDelegates(ForeignConnectionFactoryBean var1) {
      this._customizer.useDelegates(var1);
   }

   public Object _getKey() {
      return this.getName();
   }

   public void _validate() throws IllegalArgumentException {
      super._validate();
      weblogic.descriptor.beangen.LegalChecks.checkIsSet("LocalJNDIName", this.isLocalJNDINameSet());
      weblogic.descriptor.beangen.LegalChecks.checkIsSet("RemoteJNDIName", this.isRemoteJNDINameSet());
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
         var1 = 11;
      }

      try {
         switch (var1) {
            case 11:
               this._ConnectionHealthChecking = null;
               if (var2) {
                  break;
               }
            case 7:
               this._customizer.setLocalJNDIName((String)null);
               if (var2) {
                  break;
               }
            case 2:
               this._customizer.setName((String)null);
               if (var2) {
                  break;
               }
            case 10:
               this._customizer.setPassword((String)null);
               if (var2) {
                  break;
               }
            case 12:
               this._PasswordEncrypted = new byte[0];
               if (var2) {
                  break;
               }
            case 8:
               this._customizer.setRemoteJNDIName((String)null);
               if (var2) {
                  break;
               }
            case 9:
               this._customizer.setUsername((String)null);
               if (var2) {
                  break;
               }
            case 3:
            case 4:
            case 5:
            case 6:
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
      return "ForeignJMSConnectionFactory";
   }

   public void putValue(String var1, Object var2) {
      String var4;
      if (var1.equals("ConnectionHealthChecking")) {
         var4 = this._ConnectionHealthChecking;
         this._ConnectionHealthChecking = (String)var2;
         this._postSet(11, var4, this._ConnectionHealthChecking);
      } else if (var1.equals("LocalJNDIName")) {
         var4 = this._LocalJNDIName;
         this._LocalJNDIName = (String)var2;
         this._postSet(7, var4, this._LocalJNDIName);
      } else if (var1.equals("Name")) {
         var4 = this._Name;
         this._Name = (String)var2;
         this._postSet(2, var4, this._Name);
      } else if (var1.equals("Password")) {
         var4 = this._Password;
         this._Password = (String)var2;
         this._postSet(10, var4, this._Password);
      } else if (var1.equals("PasswordEncrypted")) {
         byte[] var5 = this._PasswordEncrypted;
         this._PasswordEncrypted = (byte[])((byte[])var2);
         this._postSet(12, var5, this._PasswordEncrypted);
      } else if (var1.equals("RemoteJNDIName")) {
         var4 = this._RemoteJNDIName;
         this._RemoteJNDIName = (String)var2;
         this._postSet(8, var4, this._RemoteJNDIName);
      } else if (var1.equals("Username")) {
         var4 = this._Username;
         this._Username = (String)var2;
         this._postSet(9, var4, this._Username);
      } else if (var1.equals("customizer")) {
         ForeignJMSConnectionFactory var3 = this._customizer;
         this._customizer = (ForeignJMSConnectionFactory)var2;
      } else {
         super.putValue(var1, var2);
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("ConnectionHealthChecking")) {
         return this._ConnectionHealthChecking;
      } else if (var1.equals("LocalJNDIName")) {
         return this._LocalJNDIName;
      } else if (var1.equals("Name")) {
         return this._Name;
      } else if (var1.equals("Password")) {
         return this._Password;
      } else if (var1.equals("PasswordEncrypted")) {
         return this._PasswordEncrypted;
      } else if (var1.equals("RemoteJNDIName")) {
         return this._RemoteJNDIName;
      } else if (var1.equals("Username")) {
         return this._Username;
      } else {
         return var1.equals("customizer") ? this._customizer : super.getValue(var1);
      }
   }

   public static class SchemaHelper2 extends ForeignJNDIObjectMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 4:
               if (var1.equals("name")) {
                  return 2;
               }
               break;
            case 8:
               if (var1.equals("password")) {
                  return 10;
               }

               if (var1.equals("username")) {
                  return 9;
               }
               break;
            case 15:
               if (var1.equals("local-jndi-name")) {
                  return 7;
               }
               break;
            case 16:
               if (var1.equals("remote-jndi-name")) {
                  return 8;
               }
               break;
            case 18:
               if (var1.equals("password-encrypted")) {
                  return 12;
               }
               break;
            case 26:
               if (var1.equals("connection-health-checking")) {
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
            case 2:
               return "name";
            case 3:
            case 4:
            case 5:
            case 6:
            default:
               return super.getElementName(var1);
            case 7:
               return "local-jndi-name";
            case 8:
               return "remote-jndi-name";
            case 9:
               return "username";
            case 10:
               return "password";
            case 11:
               return "connection-health-checking";
            case 12:
               return "password-encrypted";
         }
      }

      public boolean isArray(int var1) {
         switch (var1) {
            case 12:
               return true;
            default:
               return super.isArray(var1);
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

      public boolean hasKey() {
         return true;
      }

      public String[] getKeyElementNames() {
         ArrayList var1 = new ArrayList();
         var1.add("name");
         return (String[])((String[])var1.toArray(new String[0]));
      }
   }

   protected static class Helper extends ForeignJNDIObjectMBeanImpl.Helper {
      private ForeignJMSConnectionFactoryMBeanImpl bean;

      protected Helper(ForeignJMSConnectionFactoryMBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 2:
               return "Name";
            case 3:
            case 4:
            case 5:
            case 6:
            default:
               return super.getPropertyName(var1);
            case 7:
               return "LocalJNDIName";
            case 8:
               return "RemoteJNDIName";
            case 9:
               return "Username";
            case 10:
               return "Password";
            case 11:
               return "ConnectionHealthChecking";
            case 12:
               return "PasswordEncrypted";
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("ConnectionHealthChecking")) {
            return 11;
         } else if (var1.equals("LocalJNDIName")) {
            return 7;
         } else if (var1.equals("Name")) {
            return 2;
         } else if (var1.equals("Password")) {
            return 10;
         } else if (var1.equals("PasswordEncrypted")) {
            return 12;
         } else if (var1.equals("RemoteJNDIName")) {
            return 8;
         } else {
            return var1.equals("Username") ? 9 : super.getPropertyIndex(var1);
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
            if (this.bean.isConnectionHealthCheckingSet()) {
               var2.append("ConnectionHealthChecking");
               var2.append(String.valueOf(this.bean.getConnectionHealthChecking()));
            }

            if (this.bean.isLocalJNDINameSet()) {
               var2.append("LocalJNDIName");
               var2.append(String.valueOf(this.bean.getLocalJNDIName()));
            }

            if (this.bean.isNameSet()) {
               var2.append("Name");
               var2.append(String.valueOf(this.bean.getName()));
            }

            if (this.bean.isPasswordSet()) {
               var2.append("Password");
               var2.append(String.valueOf(this.bean.getPassword()));
            }

            if (this.bean.isPasswordEncryptedSet()) {
               var2.append("PasswordEncrypted");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getPasswordEncrypted())));
            }

            if (this.bean.isRemoteJNDINameSet()) {
               var2.append("RemoteJNDIName");
               var2.append(String.valueOf(this.bean.getRemoteJNDIName()));
            }

            if (this.bean.isUsernameSet()) {
               var2.append("Username");
               var2.append(String.valueOf(this.bean.getUsername()));
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
            ForeignJMSConnectionFactoryMBeanImpl var2 = (ForeignJMSConnectionFactoryMBeanImpl)var1;
            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("ConnectionHealthChecking", this.bean.getConnectionHealthChecking(), var2.getConnectionHealthChecking(), true);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("LocalJNDIName", this.bean.getLocalJNDIName(), var2.getLocalJNDIName(), true);
            }

            this.computeDiff("Name", this.bean.getName(), var2.getName(), false);
            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("Password", this.bean.getPassword(), var2.getPassword(), true);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("PasswordEncrypted", this.bean.getPasswordEncrypted(), var2.getPasswordEncrypted(), true);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("RemoteJNDIName", this.bean.getRemoteJNDIName(), var2.getRemoteJNDIName(), true);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("Username", this.bean.getUsername(), var2.getUsername(), true);
            }

         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            ForeignJMSConnectionFactoryMBeanImpl var3 = (ForeignJMSConnectionFactoryMBeanImpl)var1.getSourceBean();
            ForeignJMSConnectionFactoryMBeanImpl var4 = (ForeignJMSConnectionFactoryMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("ConnectionHealthChecking")) {
                  var3.setConnectionHealthChecking(var4.getConnectionHealthChecking());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 11);
               } else if (var5.equals("LocalJNDIName")) {
                  var3.setLocalJNDIName(var4.getLocalJNDIName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 7);
               } else if (var5.equals("Name")) {
                  var3.setName(var4.getName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 2);
               } else if (var5.equals("Password")) {
                  var3.setPassword(var4.getPassword());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 10);
               } else if (var5.equals("PasswordEncrypted")) {
                  var3.setPasswordEncrypted(var4.getPasswordEncrypted());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 12);
               } else if (var5.equals("RemoteJNDIName")) {
                  var3.setRemoteJNDIName(var4.getRemoteJNDIName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 8);
               } else if (var5.equals("Username")) {
                  var3.setUsername(var4.getUsername());
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
            ForeignJMSConnectionFactoryMBeanImpl var5 = (ForeignJMSConnectionFactoryMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if (var2 && (var3 == null || !var3.contains("ConnectionHealthChecking")) && this.bean.isConnectionHealthCheckingSet()) {
               var5.setConnectionHealthChecking(this.bean.getConnectionHealthChecking());
            }

            if (var2 && (var3 == null || !var3.contains("LocalJNDIName")) && this.bean.isLocalJNDINameSet()) {
               var5.setLocalJNDIName(this.bean.getLocalJNDIName());
            }

            if ((var3 == null || !var3.contains("Name")) && this.bean.isNameSet()) {
               var5.setName(this.bean.getName());
            }

            if (var2 && (var3 == null || !var3.contains("Password")) && this.bean.isPasswordSet()) {
               var5.setPassword(this.bean.getPassword());
            }

            if (var2 && (var3 == null || !var3.contains("PasswordEncrypted")) && this.bean.isPasswordEncryptedSet()) {
               byte[] var4 = this.bean.getPasswordEncrypted();
               var5.setPasswordEncrypted(var4 == null ? null : (byte[])((byte[])((byte[])((byte[])var4)).clone()));
            }

            if (var2 && (var3 == null || !var3.contains("RemoteJNDIName")) && this.bean.isRemoteJNDINameSet()) {
               var5.setRemoteJNDIName(this.bean.getRemoteJNDIName());
            }

            if (var2 && (var3 == null || !var3.contains("Username")) && this.bean.isUsernameSet()) {
               var5.setUsername(this.bean.getUsername());
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
