package weblogic.management.configuration;

import java.io.Serializable;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
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
import weblogic.management.DistributedManagementException;
import weblogic.management.ManagementException;
import weblogic.management.mbeans.custom.JMSFileStore;
import weblogic.utils.collections.CombinedIterator;

public class JMSFileStoreMBeanImpl extends JMSStoreMBeanImpl implements JMSFileStoreMBean, Serializable {
   private int _BlockSize;
   private String _CacheDirectory;
   private FileStoreMBean _DelegatedBean;
   private JMSServerMBean _DelegatedJMSServer;
   private String _Directory;
   private boolean _FileLockingEnabled;
   private long _InitialSize;
   private int _IoBufferSize;
   private long _MaxFileSize;
   private int _MaxWindowBufferSize;
   private int _MinWindowBufferSize;
   private String _Name;
   private String _SynchronousWritePolicy;
   private JMSFileStore _customizer;
   private static SchemaHelper2 _schemaHelper;

   public JMSFileStoreMBeanImpl() {
      try {
         this._customizer = new JMSFileStore(this);
      } catch (Exception var2) {
         if (var2 instanceof RuntimeException) {
            throw (RuntimeException)var2;
         }

         throw new UndeclaredThrowableException(var2);
      }

      this._initializeProperty(-1);
   }

   public JMSFileStoreMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);

      try {
         this._customizer = new JMSFileStore(this);
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         }

         throw new UndeclaredThrowableException(var4);
      }

      this._initializeProperty(-1);
   }

   public String getDirectory() {
      return this._customizer.getDirectory();
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

   public boolean isDirectorySet() {
      return this._isSet(8);
   }

   public boolean isNameSet() {
      return this._isSet(2);
   }

   public void setDelegatedBean(FileStoreMBean var1) {
      this._customizer.setDelegatedBean(var1);
   }

   public FileStoreMBean getDelegatedBean() {
      return this._customizer.getDelegatedBean();
   }

   public boolean isDelegatedBeanSet() {
      return this._isSet(18);
   }

   public void setDirectory(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this.getDirectory();
      this._customizer.setDirectory(var1);
      this._postSet(8, var2, var1);
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

   public String getSynchronousWritePolicy() {
      return this._customizer.getSynchronousWritePolicy();
   }

   public boolean isSynchronousWritePolicySet() {
      return this._isSet(9);
   }

   public void setDelegatedJMSServer(JMSServerMBean var1) {
      this._customizer.setDelegatedJMSServer(var1);
   }

   public JMSServerMBean getDelegatedJMSServer() {
      return this._customizer.getDelegatedJMSServer();
   }

   public boolean isDelegatedJMSServerSet() {
      return this._isSet(19);
   }

   public void setSynchronousWritePolicy(String var1) throws InvalidAttributeValueException, DistributedManagementException {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{"Disabled", "Cache-Flush", "Direct-Write", "Direct-Write-With-Cache"};
      var1 = weblogic.descriptor.beangen.LegalChecks.checkInEnum("SynchronousWritePolicy", var1, var2);
      String var3 = this.getSynchronousWritePolicy();
      this._customizer.setSynchronousWritePolicy(var1);
      this._postSet(9, var3, var1);
   }

   public String getCacheDirectory() {
      return this._CacheDirectory;
   }

   public boolean isCacheDirectorySet() {
      return this._isSet(10);
   }

   public void setCacheDirectory(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._CacheDirectory;
      this._CacheDirectory = var1;
      this._postSet(10, var2, var1);
   }

   public int getMinWindowBufferSize() {
      return this._MinWindowBufferSize;
   }

   public boolean isMinWindowBufferSizeSet() {
      return this._isSet(11);
   }

   public void setMinWindowBufferSize(int var1) {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("MinWindowBufferSize", (long)var1, -1L, 1073741824L);
      int var2 = this._MinWindowBufferSize;
      this._MinWindowBufferSize = var1;
      this._postSet(11, var2, var1);
   }

   public int getMaxWindowBufferSize() {
      return this._MaxWindowBufferSize;
   }

   public boolean isMaxWindowBufferSizeSet() {
      return this._isSet(12);
   }

   public void setMaxWindowBufferSize(int var1) {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("MaxWindowBufferSize", (long)var1, -1L, 1073741824L);
      int var2 = this._MaxWindowBufferSize;
      this._MaxWindowBufferSize = var1;
      this._postSet(12, var2, var1);
   }

   public int getIoBufferSize() {
      return this._IoBufferSize;
   }

   public boolean isIoBufferSizeSet() {
      return this._isSet(13);
   }

   public void touch() throws ConfigurationException {
      this._customizer.touch();
   }

   public void freezeCurrentValue(String var1) throws AttributeNotFoundException, MBeanException {
      this._customizer.freezeCurrentValue(var1);
   }

   public void setIoBufferSize(int var1) {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("IoBufferSize", (long)var1, -1L, 67108864L);
      int var2 = this._IoBufferSize;
      this._IoBufferSize = var1;
      this._postSet(13, var2, var1);
   }

   public long getMaxFileSize() {
      return this._MaxFileSize;
   }

   public boolean isMaxFileSizeSet() {
      return this._isSet(14);
   }

   public void restoreDefaultValue(String var1) throws AttributeNotFoundException {
      this._customizer.restoreDefaultValue(var1);
   }

   public void setMaxFileSize(long var1) {
      weblogic.descriptor.beangen.LegalChecks.checkMin("MaxFileSize", var1, 1048576L);
      long var3 = this._MaxFileSize;
      this._MaxFileSize = var1;
      this._postSet(14, var3, var1);
   }

   public int getBlockSize() {
      return this._BlockSize;
   }

   public boolean isBlockSizeSet() {
      return this._isSet(15);
   }

   public void setBlockSize(int var1) {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("BlockSize", (long)var1, -1L, 8192L);
      int var2 = this._BlockSize;
      this._BlockSize = var1;
      this._postSet(15, var2, var1);
   }

   public long getInitialSize() {
      return this._InitialSize;
   }

   public boolean isInitialSizeSet() {
      return this._isSet(16);
   }

   public void setInitialSize(long var1) {
      weblogic.descriptor.beangen.LegalChecks.checkMin("InitialSize", var1, 0L);
      long var3 = this._InitialSize;
      this._InitialSize = var1;
      this._postSet(16, var3, var1);
   }

   public boolean isFileLockingEnabled() {
      return this._FileLockingEnabled;
   }

   public boolean isFileLockingEnabledSet() {
      return this._isSet(17);
   }

   public void setFileLockingEnabled(boolean var1) {
      boolean var2 = this._FileLockingEnabled;
      this._FileLockingEnabled = var1;
      this._postSet(17, var2, var1);
   }

   public Object _getKey() {
      return this.getName();
   }

   public void _validate() throws IllegalArgumentException {
      super._validate();
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
         var1 = 15;
      }

      try {
         switch (var1) {
            case 15:
               this._BlockSize = -1;
               if (var2) {
                  break;
               }
            case 10:
               this._CacheDirectory = null;
               if (var2) {
                  break;
               }
            case 18:
               this._customizer.setDelegatedBean((FileStoreMBean)null);
               if (var2) {
                  break;
               }
            case 19:
               this._customizer.setDelegatedJMSServer((JMSServerMBean)null);
               if (var2) {
                  break;
               }
            case 8:
               this._customizer.setDirectory((String)null);
               if (var2) {
                  break;
               }
            case 16:
               this._InitialSize = 0L;
               if (var2) {
                  break;
               }
            case 13:
               this._IoBufferSize = -1;
               if (var2) {
                  break;
               }
            case 14:
               this._MaxFileSize = 1342177280L;
               if (var2) {
                  break;
               }
            case 12:
               this._MaxWindowBufferSize = -1;
               if (var2) {
                  break;
               }
            case 11:
               this._MinWindowBufferSize = -1;
               if (var2) {
                  break;
               }
            case 2:
               this._customizer.setName((String)null);
               if (var2) {
                  break;
               }
            case 9:
               this._customizer.setSynchronousWritePolicy("Direct-Write");
               if (var2) {
                  break;
               }
            case 17:
               this._FileLockingEnabled = true;
               if (var2) {
                  break;
               }
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
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
      return "JMSFileStore";
   }

   public void putValue(String var1, Object var2) {
      int var6;
      if (var1.equals("BlockSize")) {
         var6 = this._BlockSize;
         this._BlockSize = (Integer)var2;
         this._postSet(15, var6, this._BlockSize);
      } else {
         String var5;
         if (var1.equals("CacheDirectory")) {
            var5 = this._CacheDirectory;
            this._CacheDirectory = (String)var2;
            this._postSet(10, var5, this._CacheDirectory);
         } else if (var1.equals("DelegatedBean")) {
            FileStoreMBean var10 = this._DelegatedBean;
            this._DelegatedBean = (FileStoreMBean)var2;
            this._postSet(18, var10, this._DelegatedBean);
         } else if (var1.equals("DelegatedJMSServer")) {
            JMSServerMBean var9 = this._DelegatedJMSServer;
            this._DelegatedJMSServer = (JMSServerMBean)var2;
            this._postSet(19, var9, this._DelegatedJMSServer);
         } else if (var1.equals("Directory")) {
            var5 = this._Directory;
            this._Directory = (String)var2;
            this._postSet(8, var5, this._Directory);
         } else if (var1.equals("FileLockingEnabled")) {
            boolean var8 = this._FileLockingEnabled;
            this._FileLockingEnabled = (Boolean)var2;
            this._postSet(17, var8, this._FileLockingEnabled);
         } else {
            long var7;
            if (var1.equals("InitialSize")) {
               var7 = this._InitialSize;
               this._InitialSize = (Long)var2;
               this._postSet(16, var7, this._InitialSize);
            } else if (var1.equals("IoBufferSize")) {
               var6 = this._IoBufferSize;
               this._IoBufferSize = (Integer)var2;
               this._postSet(13, var6, this._IoBufferSize);
            } else if (var1.equals("MaxFileSize")) {
               var7 = this._MaxFileSize;
               this._MaxFileSize = (Long)var2;
               this._postSet(14, var7, this._MaxFileSize);
            } else if (var1.equals("MaxWindowBufferSize")) {
               var6 = this._MaxWindowBufferSize;
               this._MaxWindowBufferSize = (Integer)var2;
               this._postSet(12, var6, this._MaxWindowBufferSize);
            } else if (var1.equals("MinWindowBufferSize")) {
               var6 = this._MinWindowBufferSize;
               this._MinWindowBufferSize = (Integer)var2;
               this._postSet(11, var6, this._MinWindowBufferSize);
            } else if (var1.equals("Name")) {
               var5 = this._Name;
               this._Name = (String)var2;
               this._postSet(2, var5, this._Name);
            } else if (var1.equals("SynchronousWritePolicy")) {
               var5 = this._SynchronousWritePolicy;
               this._SynchronousWritePolicy = (String)var2;
               this._postSet(9, var5, this._SynchronousWritePolicy);
            } else if (var1.equals("customizer")) {
               JMSFileStore var3 = this._customizer;
               this._customizer = (JMSFileStore)var2;
            } else {
               super.putValue(var1, var2);
            }
         }
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("BlockSize")) {
         return new Integer(this._BlockSize);
      } else if (var1.equals("CacheDirectory")) {
         return this._CacheDirectory;
      } else if (var1.equals("DelegatedBean")) {
         return this._DelegatedBean;
      } else if (var1.equals("DelegatedJMSServer")) {
         return this._DelegatedJMSServer;
      } else if (var1.equals("Directory")) {
         return this._Directory;
      } else if (var1.equals("FileLockingEnabled")) {
         return new Boolean(this._FileLockingEnabled);
      } else if (var1.equals("InitialSize")) {
         return new Long(this._InitialSize);
      } else if (var1.equals("IoBufferSize")) {
         return new Integer(this._IoBufferSize);
      } else if (var1.equals("MaxFileSize")) {
         return new Long(this._MaxFileSize);
      } else if (var1.equals("MaxWindowBufferSize")) {
         return new Integer(this._MaxWindowBufferSize);
      } else if (var1.equals("MinWindowBufferSize")) {
         return new Integer(this._MinWindowBufferSize);
      } else if (var1.equals("Name")) {
         return this._Name;
      } else if (var1.equals("SynchronousWritePolicy")) {
         return this._SynchronousWritePolicy;
      } else {
         return var1.equals("customizer") ? this._customizer : super.getValue(var1);
      }
   }

   public static class SchemaHelper2 extends JMSStoreMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 4:
               if (var1.equals("name")) {
                  return 2;
               }
            case 5:
            case 6:
            case 7:
            case 8:
            case 11:
            case 16:
            case 17:
            case 18:
            case 19:
            case 21:
            case 23:
            default:
               break;
            case 9:
               if (var1.equals("directory")) {
                  return 8;
               }
               break;
            case 10:
               if (var1.equals("block-size")) {
                  return 15;
               }
               break;
            case 12:
               if (var1.equals("initial-size")) {
                  return 16;
               }
               break;
            case 13:
               if (var1.equals("max-file-size")) {
                  return 14;
               }
               break;
            case 14:
               if (var1.equals("delegated-bean")) {
                  return 18;
               }

               if (var1.equals("io-buffer-size")) {
                  return 13;
               }
               break;
            case 15:
               if (var1.equals("cache-directory")) {
                  return 10;
               }
               break;
            case 20:
               if (var1.equals("delegated-jms-server")) {
                  return 19;
               }

               if (var1.equals("file-locking-enabled")) {
                  return 17;
               }
               break;
            case 22:
               if (var1.equals("max-window-buffer-size")) {
                  return 12;
               }

               if (var1.equals("min-window-buffer-size")) {
                  return 11;
               }
               break;
            case 24:
               if (var1.equals("synchronous-write-policy")) {
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
            case 2:
               return "name";
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            default:
               return super.getElementName(var1);
            case 8:
               return "directory";
            case 9:
               return "synchronous-write-policy";
            case 10:
               return "cache-directory";
            case 11:
               return "min-window-buffer-size";
            case 12:
               return "max-window-buffer-size";
            case 13:
               return "io-buffer-size";
            case 14:
               return "max-file-size";
            case 15:
               return "block-size";
            case 16:
               return "initial-size";
            case 17:
               return "file-locking-enabled";
            case 18:
               return "delegated-bean";
            case 19:
               return "delegated-jms-server";
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

      public boolean hasKey() {
         return true;
      }

      public String[] getKeyElementNames() {
         ArrayList var1 = new ArrayList();
         var1.add("name");
         return (String[])((String[])var1.toArray(new String[0]));
      }
   }

   protected static class Helper extends JMSStoreMBeanImpl.Helper {
      private JMSFileStoreMBeanImpl bean;

      protected Helper(JMSFileStoreMBeanImpl var1) {
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
            case 7:
            default:
               return super.getPropertyName(var1);
            case 8:
               return "Directory";
            case 9:
               return "SynchronousWritePolicy";
            case 10:
               return "CacheDirectory";
            case 11:
               return "MinWindowBufferSize";
            case 12:
               return "MaxWindowBufferSize";
            case 13:
               return "IoBufferSize";
            case 14:
               return "MaxFileSize";
            case 15:
               return "BlockSize";
            case 16:
               return "InitialSize";
            case 17:
               return "FileLockingEnabled";
            case 18:
               return "DelegatedBean";
            case 19:
               return "DelegatedJMSServer";
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("BlockSize")) {
            return 15;
         } else if (var1.equals("CacheDirectory")) {
            return 10;
         } else if (var1.equals("DelegatedBean")) {
            return 18;
         } else if (var1.equals("DelegatedJMSServer")) {
            return 19;
         } else if (var1.equals("Directory")) {
            return 8;
         } else if (var1.equals("InitialSize")) {
            return 16;
         } else if (var1.equals("IoBufferSize")) {
            return 13;
         } else if (var1.equals("MaxFileSize")) {
            return 14;
         } else if (var1.equals("MaxWindowBufferSize")) {
            return 12;
         } else if (var1.equals("MinWindowBufferSize")) {
            return 11;
         } else if (var1.equals("Name")) {
            return 2;
         } else if (var1.equals("SynchronousWritePolicy")) {
            return 9;
         } else {
            return var1.equals("FileLockingEnabled") ? 17 : super.getPropertyIndex(var1);
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
            if (this.bean.isBlockSizeSet()) {
               var2.append("BlockSize");
               var2.append(String.valueOf(this.bean.getBlockSize()));
            }

            if (this.bean.isCacheDirectorySet()) {
               var2.append("CacheDirectory");
               var2.append(String.valueOf(this.bean.getCacheDirectory()));
            }

            if (this.bean.isDelegatedBeanSet()) {
               var2.append("DelegatedBean");
               var2.append(String.valueOf(this.bean.getDelegatedBean()));
            }

            if (this.bean.isDelegatedJMSServerSet()) {
               var2.append("DelegatedJMSServer");
               var2.append(String.valueOf(this.bean.getDelegatedJMSServer()));
            }

            if (this.bean.isDirectorySet()) {
               var2.append("Directory");
               var2.append(String.valueOf(this.bean.getDirectory()));
            }

            if (this.bean.isInitialSizeSet()) {
               var2.append("InitialSize");
               var2.append(String.valueOf(this.bean.getInitialSize()));
            }

            if (this.bean.isIoBufferSizeSet()) {
               var2.append("IoBufferSize");
               var2.append(String.valueOf(this.bean.getIoBufferSize()));
            }

            if (this.bean.isMaxFileSizeSet()) {
               var2.append("MaxFileSize");
               var2.append(String.valueOf(this.bean.getMaxFileSize()));
            }

            if (this.bean.isMaxWindowBufferSizeSet()) {
               var2.append("MaxWindowBufferSize");
               var2.append(String.valueOf(this.bean.getMaxWindowBufferSize()));
            }

            if (this.bean.isMinWindowBufferSizeSet()) {
               var2.append("MinWindowBufferSize");
               var2.append(String.valueOf(this.bean.getMinWindowBufferSize()));
            }

            if (this.bean.isNameSet()) {
               var2.append("Name");
               var2.append(String.valueOf(this.bean.getName()));
            }

            if (this.bean.isSynchronousWritePolicySet()) {
               var2.append("SynchronousWritePolicy");
               var2.append(String.valueOf(this.bean.getSynchronousWritePolicy()));
            }

            if (this.bean.isFileLockingEnabledSet()) {
               var2.append("FileLockingEnabled");
               var2.append(String.valueOf(this.bean.isFileLockingEnabled()));
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
            JMSFileStoreMBeanImpl var2 = (JMSFileStoreMBeanImpl)var1;
            this.computeDiff("BlockSize", this.bean.getBlockSize(), var2.getBlockSize(), true);
            this.computeDiff("CacheDirectory", this.bean.getCacheDirectory(), var2.getCacheDirectory(), true);
            this.computeDiff("Directory", this.bean.getDirectory(), var2.getDirectory(), false);
            this.computeDiff("InitialSize", this.bean.getInitialSize(), var2.getInitialSize(), true);
            this.computeDiff("IoBufferSize", this.bean.getIoBufferSize(), var2.getIoBufferSize(), true);
            this.computeDiff("MaxFileSize", this.bean.getMaxFileSize(), var2.getMaxFileSize(), true);
            this.computeDiff("MaxWindowBufferSize", this.bean.getMaxWindowBufferSize(), var2.getMaxWindowBufferSize(), true);
            this.computeDiff("MinWindowBufferSize", this.bean.getMinWindowBufferSize(), var2.getMinWindowBufferSize(), true);
            this.computeDiff("Name", this.bean.getName(), var2.getName(), false);
            this.computeDiff("SynchronousWritePolicy", this.bean.getSynchronousWritePolicy(), var2.getSynchronousWritePolicy(), true);
            this.computeDiff("FileLockingEnabled", this.bean.isFileLockingEnabled(), var2.isFileLockingEnabled(), true);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            JMSFileStoreMBeanImpl var3 = (JMSFileStoreMBeanImpl)var1.getSourceBean();
            JMSFileStoreMBeanImpl var4 = (JMSFileStoreMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("BlockSize")) {
                  var3.setBlockSize(var4.getBlockSize());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 15);
               } else if (var5.equals("CacheDirectory")) {
                  var3.setCacheDirectory(var4.getCacheDirectory());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 10);
               } else if (!var5.equals("DelegatedBean") && !var5.equals("DelegatedJMSServer")) {
                  if (var5.equals("Directory")) {
                     var3.setDirectory(var4.getDirectory());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 8);
                  } else if (var5.equals("InitialSize")) {
                     var3.setInitialSize(var4.getInitialSize());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 16);
                  } else if (var5.equals("IoBufferSize")) {
                     var3.setIoBufferSize(var4.getIoBufferSize());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 13);
                  } else if (var5.equals("MaxFileSize")) {
                     var3.setMaxFileSize(var4.getMaxFileSize());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 14);
                  } else if (var5.equals("MaxWindowBufferSize")) {
                     var3.setMaxWindowBufferSize(var4.getMaxWindowBufferSize());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 12);
                  } else if (var5.equals("MinWindowBufferSize")) {
                     var3.setMinWindowBufferSize(var4.getMinWindowBufferSize());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 11);
                  } else if (var5.equals("Name")) {
                     var3.setName(var4.getName());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 2);
                  } else if (var5.equals("SynchronousWritePolicy")) {
                     var3.setSynchronousWritePolicy(var4.getSynchronousWritePolicy());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 9);
                  } else if (var5.equals("FileLockingEnabled")) {
                     var3.setFileLockingEnabled(var4.isFileLockingEnabled());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 17);
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
            JMSFileStoreMBeanImpl var5 = (JMSFileStoreMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("BlockSize")) && this.bean.isBlockSizeSet()) {
               var5.setBlockSize(this.bean.getBlockSize());
            }

            if ((var3 == null || !var3.contains("CacheDirectory")) && this.bean.isCacheDirectorySet()) {
               var5.setCacheDirectory(this.bean.getCacheDirectory());
            }

            if ((var3 == null || !var3.contains("Directory")) && this.bean.isDirectorySet()) {
               var5.setDirectory(this.bean.getDirectory());
            }

            if ((var3 == null || !var3.contains("InitialSize")) && this.bean.isInitialSizeSet()) {
               var5.setInitialSize(this.bean.getInitialSize());
            }

            if ((var3 == null || !var3.contains("IoBufferSize")) && this.bean.isIoBufferSizeSet()) {
               var5.setIoBufferSize(this.bean.getIoBufferSize());
            }

            if ((var3 == null || !var3.contains("MaxFileSize")) && this.bean.isMaxFileSizeSet()) {
               var5.setMaxFileSize(this.bean.getMaxFileSize());
            }

            if ((var3 == null || !var3.contains("MaxWindowBufferSize")) && this.bean.isMaxWindowBufferSizeSet()) {
               var5.setMaxWindowBufferSize(this.bean.getMaxWindowBufferSize());
            }

            if ((var3 == null || !var3.contains("MinWindowBufferSize")) && this.bean.isMinWindowBufferSizeSet()) {
               var5.setMinWindowBufferSize(this.bean.getMinWindowBufferSize());
            }

            if ((var3 == null || !var3.contains("Name")) && this.bean.isNameSet()) {
               var5.setName(this.bean.getName());
            }

            if ((var3 == null || !var3.contains("SynchronousWritePolicy")) && this.bean.isSynchronousWritePolicySet()) {
               var5.setSynchronousWritePolicy(this.bean.getSynchronousWritePolicy());
            }

            if ((var3 == null || !var3.contains("FileLockingEnabled")) && this.bean.isFileLockingEnabledSet()) {
               var5.setFileLockingEnabled(this.bean.isFileLockingEnabled());
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
         this.inferSubTree(this.bean.getDelegatedBean(), var1, var2);
         this.inferSubTree(this.bean.getDelegatedJMSServer(), var1, var2);
      }
   }
}
