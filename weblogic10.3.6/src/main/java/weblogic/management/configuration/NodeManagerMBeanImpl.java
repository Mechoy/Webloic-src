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
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBeanHelper;
import weblogic.descriptor.internal.Munger;
import weblogic.descriptor.internal.SchemaHelper;
import weblogic.management.ManagementException;
import weblogic.management.mbeans.custom.NodeManager;
import weblogic.utils.ArrayUtils;
import weblogic.utils.collections.CombinedIterator;

public class NodeManagerMBeanImpl extends ConfigurationMBeanImpl implements NodeManagerMBean, Serializable {
   private String _Adapter;
   private String _AdapterName;
   private String _AdapterVersion;
   private boolean _DebugEnabled;
   private String[] _InstalledVMMAdapters;
   private String _ListenAddress;
   private int _ListenPort;
   private String _NMType;
   private String _Name;
   private String _NodeManagerHome;
   private String _Password;
   private byte[] _PasswordEncrypted;
   private String _ShellCommand;
   private String _UserName;
   private NodeManager _customizer;
   private static SchemaHelper2 _schemaHelper;

   public NodeManagerMBeanImpl() {
      try {
         this._customizer = new NodeManager(this);
      } catch (Exception var2) {
         if (var2 instanceof RuntimeException) {
            throw (RuntimeException)var2;
         }

         throw new UndeclaredThrowableException(var2);
      }

      this._initializeProperty(-1);
   }

   public NodeManagerMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);

      try {
         this._customizer = new NodeManager(this);
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

   public void setNMType(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{"SSH", "RSH", "Plain", "SSL", "ssh", "rsh", "ssl", "plain", "VMM", "vmm", "VMMS", "vmms"};
      var1 = weblogic.descriptor.beangen.LegalChecks.checkInEnum("NMType", var1, var2);
      String var3 = this._NMType;
      this._NMType = var1;
      this._postSet(7, var3, var1);
   }

   public String getNMType() {
      return this._NMType;
   }

   public boolean isNMTypeSet() {
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

   public String getListenAddress() {
      return this._ListenAddress;
   }

   public boolean isListenAddressSet() {
      return this._isSet(8);
   }

   public void setListenAddress(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._ListenAddress;
      this._ListenAddress = var1;
      this._postSet(8, var2, var1);
   }

   public int getListenPort() {
      return this._ListenPort;
   }

   public boolean isListenPortSet() {
      return this._isSet(9);
   }

   public void setListenPort(int var1) {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("ListenPort", (long)var1, 1L, 65534L);
      int var2 = this._ListenPort;
      this._ListenPort = var1;
      this._postSet(9, var2, var1);
   }

   public boolean isDebugEnabled() {
      return this._DebugEnabled;
   }

   public boolean isDebugEnabledSet() {
      return this._isSet(10);
   }

   public void setDebugEnabled(boolean var1) {
      boolean var2 = this._DebugEnabled;
      this._DebugEnabled = var1;
      this._postSet(10, var2, var1);
   }

   public void setShellCommand(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._ShellCommand;
      this._ShellCommand = var1;
      this._postSet(11, var2, var1);
   }

   public String getShellCommand() {
      return this._ShellCommand;
   }

   public boolean isShellCommandSet() {
      return this._isSet(11);
   }

   public void setNodeManagerHome(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._NodeManagerHome;
      this._NodeManagerHome = var1;
      this._postSet(12, var2, var1);
   }

   public void touch() throws ConfigurationException {
      this._customizer.touch();
   }

   public void freezeCurrentValue(String var1) throws AttributeNotFoundException, MBeanException {
      this._customizer.freezeCurrentValue(var1);
   }

   public String getNodeManagerHome() {
      return this._NodeManagerHome;
   }

   public boolean isNodeManagerHomeSet() {
      return this._isSet(12);
   }

   public void restoreDefaultValue(String var1) throws AttributeNotFoundException {
      this._customizer.restoreDefaultValue(var1);
   }

   public void setAdapter(String var1) {
      var1 = var1 == null ? null : var1.trim();
      VMMAdapterValidator.validateAdapter(var1);
      String var2 = this.getAdapter();
      this._customizer.setAdapter(var1);
      this._postSet(13, var2, var1);
   }

   public String getAdapter() {
      return this._customizer.getAdapter();
   }

   public boolean isAdapterSet() {
      return this._isSet(13);
   }

   public void setAdapterName(String var1) {
      var1 = var1 == null ? null : var1.trim();
      VMMAdapterValidator.validateAdapterName(var1);
      String var2 = this._AdapterName;
      this._AdapterName = var1;
      this._postSet(14, var2, var1);
   }

   public String getAdapterName() {
      return this._AdapterName;
   }

   public boolean isAdapterNameSet() {
      return this._isSet(14);
   }

   public void setAdapterVersion(String var1) {
      var1 = var1 == null ? null : var1.trim();
      VMMAdapterValidator.validateAdapterVersion(var1);
      String var2 = this._AdapterVersion;
      this._AdapterVersion = var1;
      this._postSet(15, var2, var1);
   }

   public String getAdapterVersion() {
      return this._AdapterVersion;
   }

   public boolean isAdapterVersionSet() {
      return this._isSet(15);
   }

   public void setUserName(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._UserName;
      this._UserName = var1;
      this._postSet(16, var2, var1);
   }

   public String getUserName() {
      return this._UserName;
   }

   public boolean isUserNameSet() {
      return this._isSet(16);
   }

   public void setPassword(String var1) {
      var1 = var1 == null ? null : var1.trim();
      this.setPasswordEncrypted(var1 == null ? null : this._encrypt("Password", var1));
   }

   public String getPassword() {
      byte[] var1 = this.getPasswordEncrypted();
      return var1 == null ? null : this._decrypt("Password", var1);
   }

   public boolean isPasswordSet() {
      return this.isPasswordEncryptedSet();
   }

   public byte[] getPasswordEncrypted() {
      return this._getHelper()._cloneArray(this._PasswordEncrypted);
   }

   public String getPasswordEncryptedAsString() {
      byte[] var1 = this.getPasswordEncrypted();
      return var1 == null ? null : new String(var1);
   }

   public boolean isPasswordEncryptedSet() {
      return this._isSet(18);
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

   public String[] getInstalledVMMAdapters() {
      return this._customizer.getInstalledVMMAdapters();
   }

   public boolean isInstalledVMMAdaptersSet() {
      return this._isSet(19);
   }

   public void setInstalledVMMAdapters(String[] var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? new String[0] : var1;
      var1 = this._getHelper()._trimElements(var1);
      String[] var2 = this._InstalledVMMAdapters;
      this._InstalledVMMAdapters = var1;
      this._postSet(19, var2, var1);
   }

   public Object _getKey() {
      return this.getName();
   }

   public void _validate() throws IllegalArgumentException {
      super._validate();
   }

   public void setPasswordEncrypted(byte[] var1) {
      byte[] var2 = this._PasswordEncrypted;
      if (this._isProductionModeEnabled() && var1 != null && !this._isEncrypted(var1)) {
         throw new IllegalArgumentException("In production mode, it's not allowed to set a clear text value to the property: PasswordEncrypted of NodeManagerMBean");
      } else {
         this._getHelper()._clearArray(this._PasswordEncrypted);
         this._PasswordEncrypted = this._getHelper()._cloneArray(var1);
         this._postSet(18, var2, var1);
      }
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
         if (var1 == 17) {
            this._markSet(18, false);
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
         var1 = 13;
      }

      try {
         switch (var1) {
            case 13:
               this._customizer.setAdapter((String)null);
               if (var2) {
                  break;
               }
            case 14:
               this._AdapterName = null;
               if (var2) {
                  break;
               }
            case 15:
               this._AdapterVersion = null;
               if (var2) {
                  break;
               }
            case 19:
               this._InstalledVMMAdapters = new String[0];
               if (var2) {
                  break;
               }
            case 8:
               this._ListenAddress = "localhost";
               if (var2) {
                  break;
               }
            case 9:
               this._ListenPort = 5556;
               if (var2) {
                  break;
               }
            case 7:
               this._NMType = "SSL";
               if (var2) {
                  break;
               }
            case 2:
               this._customizer.setName((String)null);
               if (var2) {
                  break;
               }
            case 12:
               this._NodeManagerHome = null;
               if (var2) {
                  break;
               }
            case 17:
               this._PasswordEncrypted = null;
               if (var2) {
                  break;
               }
            case 18:
               this._PasswordEncrypted = null;
               if (var2) {
                  break;
               }
            case 11:
               this._ShellCommand = null;
               if (var2) {
                  break;
               }
            case 16:
               this._UserName = null;
               if (var2) {
                  break;
               }
            case 10:
               this._DebugEnabled = false;
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
      return "NodeManager";
   }

   public void putValue(String var1, Object var2) {
      String var4;
      if (var1.equals("Adapter")) {
         var4 = this._Adapter;
         this._Adapter = (String)var2;
         this._postSet(13, var4, this._Adapter);
      } else if (var1.equals("AdapterName")) {
         var4 = this._AdapterName;
         this._AdapterName = (String)var2;
         this._postSet(14, var4, this._AdapterName);
      } else if (var1.equals("AdapterVersion")) {
         var4 = this._AdapterVersion;
         this._AdapterVersion = (String)var2;
         this._postSet(15, var4, this._AdapterVersion);
      } else if (var1.equals("DebugEnabled")) {
         boolean var8 = this._DebugEnabled;
         this._DebugEnabled = (Boolean)var2;
         this._postSet(10, var8, this._DebugEnabled);
      } else if (var1.equals("InstalledVMMAdapters")) {
         String[] var7 = this._InstalledVMMAdapters;
         this._InstalledVMMAdapters = (String[])((String[])var2);
         this._postSet(19, var7, this._InstalledVMMAdapters);
      } else if (var1.equals("ListenAddress")) {
         var4 = this._ListenAddress;
         this._ListenAddress = (String)var2;
         this._postSet(8, var4, this._ListenAddress);
      } else if (var1.equals("ListenPort")) {
         int var6 = this._ListenPort;
         this._ListenPort = (Integer)var2;
         this._postSet(9, var6, this._ListenPort);
      } else if (var1.equals("NMType")) {
         var4 = this._NMType;
         this._NMType = (String)var2;
         this._postSet(7, var4, this._NMType);
      } else if (var1.equals("Name")) {
         var4 = this._Name;
         this._Name = (String)var2;
         this._postSet(2, var4, this._Name);
      } else if (var1.equals("NodeManagerHome")) {
         var4 = this._NodeManagerHome;
         this._NodeManagerHome = (String)var2;
         this._postSet(12, var4, this._NodeManagerHome);
      } else if (var1.equals("Password")) {
         var4 = this._Password;
         this._Password = (String)var2;
         this._postSet(17, var4, this._Password);
      } else if (var1.equals("PasswordEncrypted")) {
         byte[] var5 = this._PasswordEncrypted;
         this._PasswordEncrypted = (byte[])((byte[])var2);
         this._postSet(18, var5, this._PasswordEncrypted);
      } else if (var1.equals("ShellCommand")) {
         var4 = this._ShellCommand;
         this._ShellCommand = (String)var2;
         this._postSet(11, var4, this._ShellCommand);
      } else if (var1.equals("UserName")) {
         var4 = this._UserName;
         this._UserName = (String)var2;
         this._postSet(16, var4, this._UserName);
      } else if (var1.equals("customizer")) {
         NodeManager var3 = this._customizer;
         this._customizer = (NodeManager)var2;
      } else {
         super.putValue(var1, var2);
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("Adapter")) {
         return this._Adapter;
      } else if (var1.equals("AdapterName")) {
         return this._AdapterName;
      } else if (var1.equals("AdapterVersion")) {
         return this._AdapterVersion;
      } else if (var1.equals("DebugEnabled")) {
         return new Boolean(this._DebugEnabled);
      } else if (var1.equals("InstalledVMMAdapters")) {
         return this._InstalledVMMAdapters;
      } else if (var1.equals("ListenAddress")) {
         return this._ListenAddress;
      } else if (var1.equals("ListenPort")) {
         return new Integer(this._ListenPort);
      } else if (var1.equals("NMType")) {
         return this._NMType;
      } else if (var1.equals("Name")) {
         return this._Name;
      } else if (var1.equals("NodeManagerHome")) {
         return this._NodeManagerHome;
      } else if (var1.equals("Password")) {
         return this._Password;
      } else if (var1.equals("PasswordEncrypted")) {
         return this._PasswordEncrypted;
      } else if (var1.equals("ShellCommand")) {
         return this._ShellCommand;
      } else if (var1.equals("UserName")) {
         return this._UserName;
      } else {
         return var1.equals("customizer") ? this._customizer : super.getValue(var1);
      }
   }

   public static class SchemaHelper2 extends ConfigurationMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 4:
               if (var1.equals("name")) {
                  return 2;
               }
            case 5:
            case 6:
            case 10:
            case 16:
            case 19:
            default:
               break;
            case 7:
               if (var1.equals("adapter")) {
                  return 13;
               }

               if (var1.equals("nm-type")) {
                  return 7;
               }
               break;
            case 8:
               if (var1.equals("password")) {
                  return 17;
               }
               break;
            case 9:
               if (var1.equals("user-name")) {
                  return 16;
               }
               break;
            case 11:
               if (var1.equals("listen-port")) {
                  return 9;
               }
               break;
            case 12:
               if (var1.equals("adapter-name")) {
                  return 14;
               }
               break;
            case 13:
               if (var1.equals("shell-command")) {
                  return 11;
               }

               if (var1.equals("debug-enabled")) {
                  return 10;
               }
               break;
            case 14:
               if (var1.equals("listen-address")) {
                  return 8;
               }
               break;
            case 15:
               if (var1.equals("adapter-version")) {
                  return 15;
               }
               break;
            case 17:
               if (var1.equals("node-manager-home")) {
                  return 12;
               }
               break;
            case 18:
               if (var1.equals("password-encrypted")) {
                  return 18;
               }
               break;
            case 20:
               if (var1.equals("installedvmm-adapter")) {
                  return 19;
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
               return "nm-type";
            case 8:
               return "listen-address";
            case 9:
               return "listen-port";
            case 10:
               return "debug-enabled";
            case 11:
               return "shell-command";
            case 12:
               return "node-manager-home";
            case 13:
               return "adapter";
            case 14:
               return "adapter-name";
            case 15:
               return "adapter-version";
            case 16:
               return "user-name";
            case 17:
               return "password";
            case 18:
               return "password-encrypted";
            case 19:
               return "installedvmm-adapter";
         }
      }

      public boolean isArray(int var1) {
         switch (var1) {
            case 19:
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

   protected static class Helper extends ConfigurationMBeanImpl.Helper {
      private NodeManagerMBeanImpl bean;

      protected Helper(NodeManagerMBeanImpl var1) {
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
               return "NMType";
            case 8:
               return "ListenAddress";
            case 9:
               return "ListenPort";
            case 10:
               return "DebugEnabled";
            case 11:
               return "ShellCommand";
            case 12:
               return "NodeManagerHome";
            case 13:
               return "Adapter";
            case 14:
               return "AdapterName";
            case 15:
               return "AdapterVersion";
            case 16:
               return "UserName";
            case 17:
               return "Password";
            case 18:
               return "PasswordEncrypted";
            case 19:
               return "InstalledVMMAdapters";
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("Adapter")) {
            return 13;
         } else if (var1.equals("AdapterName")) {
            return 14;
         } else if (var1.equals("AdapterVersion")) {
            return 15;
         } else if (var1.equals("InstalledVMMAdapters")) {
            return 19;
         } else if (var1.equals("ListenAddress")) {
            return 8;
         } else if (var1.equals("ListenPort")) {
            return 9;
         } else if (var1.equals("NMType")) {
            return 7;
         } else if (var1.equals("Name")) {
            return 2;
         } else if (var1.equals("NodeManagerHome")) {
            return 12;
         } else if (var1.equals("Password")) {
            return 17;
         } else if (var1.equals("PasswordEncrypted")) {
            return 18;
         } else if (var1.equals("ShellCommand")) {
            return 11;
         } else if (var1.equals("UserName")) {
            return 16;
         } else {
            return var1.equals("DebugEnabled") ? 10 : super.getPropertyIndex(var1);
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
            if (this.bean.isAdapterSet()) {
               var2.append("Adapter");
               var2.append(String.valueOf(this.bean.getAdapter()));
            }

            if (this.bean.isAdapterNameSet()) {
               var2.append("AdapterName");
               var2.append(String.valueOf(this.bean.getAdapterName()));
            }

            if (this.bean.isAdapterVersionSet()) {
               var2.append("AdapterVersion");
               var2.append(String.valueOf(this.bean.getAdapterVersion()));
            }

            if (this.bean.isInstalledVMMAdaptersSet()) {
               var2.append("InstalledVMMAdapters");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getInstalledVMMAdapters())));
            }

            if (this.bean.isListenAddressSet()) {
               var2.append("ListenAddress");
               var2.append(String.valueOf(this.bean.getListenAddress()));
            }

            if (this.bean.isListenPortSet()) {
               var2.append("ListenPort");
               var2.append(String.valueOf(this.bean.getListenPort()));
            }

            if (this.bean.isNMTypeSet()) {
               var2.append("NMType");
               var2.append(String.valueOf(this.bean.getNMType()));
            }

            if (this.bean.isNameSet()) {
               var2.append("Name");
               var2.append(String.valueOf(this.bean.getName()));
            }

            if (this.bean.isNodeManagerHomeSet()) {
               var2.append("NodeManagerHome");
               var2.append(String.valueOf(this.bean.getNodeManagerHome()));
            }

            if (this.bean.isPasswordSet()) {
               var2.append("Password");
               var2.append(String.valueOf(this.bean.getPassword()));
            }

            if (this.bean.isPasswordEncryptedSet()) {
               var2.append("PasswordEncrypted");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getPasswordEncrypted())));
            }

            if (this.bean.isShellCommandSet()) {
               var2.append("ShellCommand");
               var2.append(String.valueOf(this.bean.getShellCommand()));
            }

            if (this.bean.isUserNameSet()) {
               var2.append("UserName");
               var2.append(String.valueOf(this.bean.getUserName()));
            }

            if (this.bean.isDebugEnabledSet()) {
               var2.append("DebugEnabled");
               var2.append(String.valueOf(this.bean.isDebugEnabled()));
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
            NodeManagerMBeanImpl var2 = (NodeManagerMBeanImpl)var1;
            this.computeDiff("Adapter", this.bean.getAdapter(), var2.getAdapter(), false);
            this.computeDiff("AdapterName", this.bean.getAdapterName(), var2.getAdapterName(), false);
            this.computeDiff("AdapterVersion", this.bean.getAdapterVersion(), var2.getAdapterVersion(), false);
            this.computeDiff("InstalledVMMAdapters", this.bean.getInstalledVMMAdapters(), var2.getInstalledVMMAdapters(), false);
            this.computeDiff("ListenAddress", this.bean.getListenAddress(), var2.getListenAddress(), true);
            this.computeDiff("ListenPort", this.bean.getListenPort(), var2.getListenPort(), true);
            this.computeDiff("NMType", this.bean.getNMType(), var2.getNMType(), false);
            this.computeDiff("Name", this.bean.getName(), var2.getName(), false);
            this.computeDiff("NodeManagerHome", this.bean.getNodeManagerHome(), var2.getNodeManagerHome(), false);
            this.computeDiff("PasswordEncrypted", this.bean.getPasswordEncrypted(), var2.getPasswordEncrypted(), false);
            this.computeDiff("ShellCommand", this.bean.getShellCommand(), var2.getShellCommand(), false);
            this.computeDiff("UserName", this.bean.getUserName(), var2.getUserName(), true);
            this.computeDiff("DebugEnabled", this.bean.isDebugEnabled(), var2.isDebugEnabled(), true);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            NodeManagerMBeanImpl var3 = (NodeManagerMBeanImpl)var1.getSourceBean();
            NodeManagerMBeanImpl var4 = (NodeManagerMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("Adapter")) {
                  var3.setAdapter(var4.getAdapter());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 13);
               } else if (var5.equals("AdapterName")) {
                  var3.setAdapterName(var4.getAdapterName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 14);
               } else if (var5.equals("AdapterVersion")) {
                  var3.setAdapterVersion(var4.getAdapterVersion());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 15);
               } else if (var5.equals("InstalledVMMAdapters")) {
                  var3._conditionalUnset(var2.isUnsetUpdate(), 19);
               } else if (var5.equals("ListenAddress")) {
                  var3.setListenAddress(var4.getListenAddress());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 8);
               } else if (var5.equals("ListenPort")) {
                  var3.setListenPort(var4.getListenPort());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 9);
               } else if (var5.equals("NMType")) {
                  var3.setNMType(var4.getNMType());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 7);
               } else if (var5.equals("Name")) {
                  var3.setName(var4.getName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 2);
               } else if (var5.equals("NodeManagerHome")) {
                  var3.setNodeManagerHome(var4.getNodeManagerHome());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 12);
               } else if (!var5.equals("Password")) {
                  if (var5.equals("PasswordEncrypted")) {
                     var3.setPasswordEncrypted(var4.getPasswordEncrypted());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 18);
                  } else if (var5.equals("ShellCommand")) {
                     var3.setShellCommand(var4.getShellCommand());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 11);
                  } else if (var5.equals("UserName")) {
                     var3.setUserName(var4.getUserName());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 16);
                  } else if (var5.equals("DebugEnabled")) {
                     var3.setDebugEnabled(var4.isDebugEnabled());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 10);
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
            NodeManagerMBeanImpl var5 = (NodeManagerMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("Adapter")) && this.bean.isAdapterSet()) {
               var5.setAdapter(this.bean.getAdapter());
            }

            if ((var3 == null || !var3.contains("AdapterName")) && this.bean.isAdapterNameSet()) {
               var5.setAdapterName(this.bean.getAdapterName());
            }

            if ((var3 == null || !var3.contains("AdapterVersion")) && this.bean.isAdapterVersionSet()) {
               var5.setAdapterVersion(this.bean.getAdapterVersion());
            }

            if ((var3 == null || !var3.contains("InstalledVMMAdapters")) && this.bean.isInstalledVMMAdaptersSet()) {
               String[] var4 = this.bean.getInstalledVMMAdapters();
               var5.setInstalledVMMAdapters(var4 == null ? null : (String[])((String[])((String[])((String[])var4)).clone()));
            }

            if ((var3 == null || !var3.contains("ListenAddress")) && this.bean.isListenAddressSet()) {
               var5.setListenAddress(this.bean.getListenAddress());
            }

            if ((var3 == null || !var3.contains("ListenPort")) && this.bean.isListenPortSet()) {
               var5.setListenPort(this.bean.getListenPort());
            }

            if ((var3 == null || !var3.contains("NMType")) && this.bean.isNMTypeSet()) {
               var5.setNMType(this.bean.getNMType());
            }

            if ((var3 == null || !var3.contains("Name")) && this.bean.isNameSet()) {
               var5.setName(this.bean.getName());
            }

            if ((var3 == null || !var3.contains("NodeManagerHome")) && this.bean.isNodeManagerHomeSet()) {
               var5.setNodeManagerHome(this.bean.getNodeManagerHome());
            }

            if ((var3 == null || !var3.contains("PasswordEncrypted")) && this.bean.isPasswordEncryptedSet()) {
               byte[] var8 = this.bean.getPasswordEncrypted();
               var5.setPasswordEncrypted(var8 == null ? null : (byte[])((byte[])((byte[])((byte[])var8)).clone()));
            }

            if ((var3 == null || !var3.contains("ShellCommand")) && this.bean.isShellCommandSet()) {
               var5.setShellCommand(this.bean.getShellCommand());
            }

            if ((var3 == null || !var3.contains("UserName")) && this.bean.isUserNameSet()) {
               var5.setUserName(this.bean.getUserName());
            }

            if ((var3 == null || !var3.contains("DebugEnabled")) && this.bean.isDebugEnabledSet()) {
               var5.setDebugEnabled(this.bean.isDebugEnabled());
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
