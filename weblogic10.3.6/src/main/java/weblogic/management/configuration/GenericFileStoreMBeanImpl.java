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

public class GenericFileStoreMBeanImpl extends ConfigurationMBeanImpl implements GenericFileStoreMBean, Serializable {
   private int _BlockSize;
   private String _CacheDirectory;
   private String _Directory;
   private boolean _FileLockingEnabled;
   private long _InitialSize;
   private int _IoBufferSize;
   private long _MaxFileSize;
   private int _MaxWindowBufferSize;
   private int _MinWindowBufferSize;
   private String _SynchronousWritePolicy;
   private static SchemaHelper2 _schemaHelper;

   public GenericFileStoreMBeanImpl() {
      this._initializeProperty(-1);
   }

   public GenericFileStoreMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);
      this._initializeProperty(-1);
   }

   public String getDirectory() {
      return this._Directory;
   }

   public boolean isDirectorySet() {
      return this._isSet(7);
   }

   public void setDirectory(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._Directory;
      this._Directory = var1;
      this._postSet(7, var2, var1);
   }

   public String getSynchronousWritePolicy() {
      return this._SynchronousWritePolicy;
   }

   public boolean isSynchronousWritePolicySet() {
      return this._isSet(8);
   }

   public void setSynchronousWritePolicy(String var1) throws InvalidAttributeValueException, DistributedManagementException {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{"Disabled", "Cache-Flush", "Direct-Write", "Direct-Write-With-Cache"};
      var1 = weblogic.descriptor.beangen.LegalChecks.checkInEnum("SynchronousWritePolicy", var1, var2);
      String var3 = this._SynchronousWritePolicy;
      this._SynchronousWritePolicy = var1;
      this._postSet(8, var3, var1);
   }

   public String getCacheDirectory() {
      return this._CacheDirectory;
   }

   public boolean isCacheDirectorySet() {
      return this._isSet(9);
   }

   public void setCacheDirectory(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._CacheDirectory;
      this._CacheDirectory = var1;
      this._postSet(9, var2, var1);
   }

   public int getMinWindowBufferSize() {
      return this._MinWindowBufferSize;
   }

   public boolean isMinWindowBufferSizeSet() {
      return this._isSet(10);
   }

   public void setMinWindowBufferSize(int var1) {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("MinWindowBufferSize", (long)var1, -1L, 1073741824L);
      int var2 = this._MinWindowBufferSize;
      this._MinWindowBufferSize = var1;
      this._postSet(10, var2, var1);
   }

   public int getMaxWindowBufferSize() {
      return this._MaxWindowBufferSize;
   }

   public boolean isMaxWindowBufferSizeSet() {
      return this._isSet(11);
   }

   public void setMaxWindowBufferSize(int var1) {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("MaxWindowBufferSize", (long)var1, -1L, 1073741824L);
      int var2 = this._MaxWindowBufferSize;
      this._MaxWindowBufferSize = var1;
      this._postSet(11, var2, var1);
   }

   public int getIoBufferSize() {
      return this._IoBufferSize;
   }

   public boolean isIoBufferSizeSet() {
      return this._isSet(12);
   }

   public void setIoBufferSize(int var1) {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("IoBufferSize", (long)var1, -1L, 67108864L);
      int var2 = this._IoBufferSize;
      this._IoBufferSize = var1;
      this._postSet(12, var2, var1);
   }

   public long getMaxFileSize() {
      return this._MaxFileSize;
   }

   public boolean isMaxFileSizeSet() {
      return this._isSet(13);
   }

   public void setMaxFileSize(long var1) {
      weblogic.descriptor.beangen.LegalChecks.checkMin("MaxFileSize", var1, 1048576L);
      long var3 = this._MaxFileSize;
      this._MaxFileSize = var1;
      this._postSet(13, var3, var1);
   }

   public int getBlockSize() {
      return this._BlockSize;
   }

   public boolean isBlockSizeSet() {
      return this._isSet(14);
   }

   public void setBlockSize(int var1) {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("BlockSize", (long)var1, -1L, 8192L);
      int var2 = this._BlockSize;
      this._BlockSize = var1;
      this._postSet(14, var2, var1);
   }

   public long getInitialSize() {
      return this._InitialSize;
   }

   public boolean isInitialSizeSet() {
      return this._isSet(15);
   }

   public void setInitialSize(long var1) {
      weblogic.descriptor.beangen.LegalChecks.checkMin("InitialSize", var1, 0L);
      long var3 = this._InitialSize;
      this._InitialSize = var1;
      this._postSet(15, var3, var1);
   }

   public boolean isFileLockingEnabled() {
      return this._FileLockingEnabled;
   }

   public boolean isFileLockingEnabledSet() {
      return this._isSet(16);
   }

   public void setFileLockingEnabled(boolean var1) {
      boolean var2 = this._FileLockingEnabled;
      this._FileLockingEnabled = var1;
      this._postSet(16, var2, var1);
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
         var1 = 14;
      }

      try {
         switch (var1) {
            case 14:
               this._BlockSize = -1;
               if (var2) {
                  break;
               }
            case 9:
               this._CacheDirectory = null;
               if (var2) {
                  break;
               }
            case 7:
               this._Directory = null;
               if (var2) {
                  break;
               }
            case 15:
               this._InitialSize = 0L;
               if (var2) {
                  break;
               }
            case 12:
               this._IoBufferSize = -1;
               if (var2) {
                  break;
               }
            case 13:
               this._MaxFileSize = 1342177280L;
               if (var2) {
                  break;
               }
            case 11:
               this._MaxWindowBufferSize = -1;
               if (var2) {
                  break;
               }
            case 10:
               this._MinWindowBufferSize = -1;
               if (var2) {
                  break;
               }
            case 8:
               this._SynchronousWritePolicy = "Direct-Write";
               if (var2) {
                  break;
               }
            case 16:
               this._FileLockingEnabled = true;
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
      return "GenericFileStore";
   }

   public void putValue(String var1, Object var2) {
      int var5;
      if (var1.equals("BlockSize")) {
         var5 = this._BlockSize;
         this._BlockSize = (Integer)var2;
         this._postSet(14, var5, this._BlockSize);
      } else {
         String var3;
         if (var1.equals("CacheDirectory")) {
            var3 = this._CacheDirectory;
            this._CacheDirectory = (String)var2;
            this._postSet(9, var3, this._CacheDirectory);
         } else if (var1.equals("Directory")) {
            var3 = this._Directory;
            this._Directory = (String)var2;
            this._postSet(7, var3, this._Directory);
         } else if (var1.equals("FileLockingEnabled")) {
            boolean var7 = this._FileLockingEnabled;
            this._FileLockingEnabled = (Boolean)var2;
            this._postSet(16, var7, this._FileLockingEnabled);
         } else {
            long var6;
            if (var1.equals("InitialSize")) {
               var6 = this._InitialSize;
               this._InitialSize = (Long)var2;
               this._postSet(15, var6, this._InitialSize);
            } else if (var1.equals("IoBufferSize")) {
               var5 = this._IoBufferSize;
               this._IoBufferSize = (Integer)var2;
               this._postSet(12, var5, this._IoBufferSize);
            } else if (var1.equals("MaxFileSize")) {
               var6 = this._MaxFileSize;
               this._MaxFileSize = (Long)var2;
               this._postSet(13, var6, this._MaxFileSize);
            } else if (var1.equals("MaxWindowBufferSize")) {
               var5 = this._MaxWindowBufferSize;
               this._MaxWindowBufferSize = (Integer)var2;
               this._postSet(11, var5, this._MaxWindowBufferSize);
            } else if (var1.equals("MinWindowBufferSize")) {
               var5 = this._MinWindowBufferSize;
               this._MinWindowBufferSize = (Integer)var2;
               this._postSet(10, var5, this._MinWindowBufferSize);
            } else if (var1.equals("SynchronousWritePolicy")) {
               var3 = this._SynchronousWritePolicy;
               this._SynchronousWritePolicy = (String)var2;
               this._postSet(8, var3, this._SynchronousWritePolicy);
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
      } else {
         return var1.equals("SynchronousWritePolicy") ? this._SynchronousWritePolicy : super.getValue(var1);
      }
   }

   public static class SchemaHelper2 extends ConfigurationMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 9:
               if (var1.equals("directory")) {
                  return 7;
               }
               break;
            case 10:
               if (var1.equals("block-size")) {
                  return 14;
               }
            case 11:
            case 16:
            case 17:
            case 18:
            case 19:
            case 21:
            case 23:
            default:
               break;
            case 12:
               if (var1.equals("initial-size")) {
                  return 15;
               }
               break;
            case 13:
               if (var1.equals("max-file-size")) {
                  return 13;
               }
               break;
            case 14:
               if (var1.equals("io-buffer-size")) {
                  return 12;
               }
               break;
            case 15:
               if (var1.equals("cache-directory")) {
                  return 9;
               }
               break;
            case 20:
               if (var1.equals("file-locking-enabled")) {
                  return 16;
               }
               break;
            case 22:
               if (var1.equals("max-window-buffer-size")) {
                  return 11;
               }

               if (var1.equals("min-window-buffer-size")) {
                  return 10;
               }
               break;
            case 24:
               if (var1.equals("synchronous-write-policy")) {
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
               return "directory";
            case 8:
               return "synchronous-write-policy";
            case 9:
               return "cache-directory";
            case 10:
               return "min-window-buffer-size";
            case 11:
               return "max-window-buffer-size";
            case 12:
               return "io-buffer-size";
            case 13:
               return "max-file-size";
            case 14:
               return "block-size";
            case 15:
               return "initial-size";
            case 16:
               return "file-locking-enabled";
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
      private GenericFileStoreMBeanImpl bean;

      protected Helper(GenericFileStoreMBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 7:
               return "Directory";
            case 8:
               return "SynchronousWritePolicy";
            case 9:
               return "CacheDirectory";
            case 10:
               return "MinWindowBufferSize";
            case 11:
               return "MaxWindowBufferSize";
            case 12:
               return "IoBufferSize";
            case 13:
               return "MaxFileSize";
            case 14:
               return "BlockSize";
            case 15:
               return "InitialSize";
            case 16:
               return "FileLockingEnabled";
            default:
               return super.getPropertyName(var1);
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("BlockSize")) {
            return 14;
         } else if (var1.equals("CacheDirectory")) {
            return 9;
         } else if (var1.equals("Directory")) {
            return 7;
         } else if (var1.equals("InitialSize")) {
            return 15;
         } else if (var1.equals("IoBufferSize")) {
            return 12;
         } else if (var1.equals("MaxFileSize")) {
            return 13;
         } else if (var1.equals("MaxWindowBufferSize")) {
            return 11;
         } else if (var1.equals("MinWindowBufferSize")) {
            return 10;
         } else if (var1.equals("SynchronousWritePolicy")) {
            return 8;
         } else {
            return var1.equals("FileLockingEnabled") ? 16 : super.getPropertyIndex(var1);
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
            GenericFileStoreMBeanImpl var2 = (GenericFileStoreMBeanImpl)var1;
            this.computeDiff("BlockSize", this.bean.getBlockSize(), var2.getBlockSize(), true);
            this.computeDiff("CacheDirectory", this.bean.getCacheDirectory(), var2.getCacheDirectory(), true);
            this.computeDiff("Directory", this.bean.getDirectory(), var2.getDirectory(), false);
            this.computeDiff("InitialSize", this.bean.getInitialSize(), var2.getInitialSize(), true);
            this.computeDiff("IoBufferSize", this.bean.getIoBufferSize(), var2.getIoBufferSize(), true);
            this.computeDiff("MaxFileSize", this.bean.getMaxFileSize(), var2.getMaxFileSize(), true);
            this.computeDiff("MaxWindowBufferSize", this.bean.getMaxWindowBufferSize(), var2.getMaxWindowBufferSize(), true);
            this.computeDiff("MinWindowBufferSize", this.bean.getMinWindowBufferSize(), var2.getMinWindowBufferSize(), true);
            this.computeDiff("SynchronousWritePolicy", this.bean.getSynchronousWritePolicy(), var2.getSynchronousWritePolicy(), true);
            this.computeDiff("FileLockingEnabled", this.bean.isFileLockingEnabled(), var2.isFileLockingEnabled(), true);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            GenericFileStoreMBeanImpl var3 = (GenericFileStoreMBeanImpl)var1.getSourceBean();
            GenericFileStoreMBeanImpl var4 = (GenericFileStoreMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("BlockSize")) {
                  var3.setBlockSize(var4.getBlockSize());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 14);
               } else if (var5.equals("CacheDirectory")) {
                  var3.setCacheDirectory(var4.getCacheDirectory());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 9);
               } else if (var5.equals("Directory")) {
                  var3.setDirectory(var4.getDirectory());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 7);
               } else if (var5.equals("InitialSize")) {
                  var3.setInitialSize(var4.getInitialSize());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 15);
               } else if (var5.equals("IoBufferSize")) {
                  var3.setIoBufferSize(var4.getIoBufferSize());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 12);
               } else if (var5.equals("MaxFileSize")) {
                  var3.setMaxFileSize(var4.getMaxFileSize());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 13);
               } else if (var5.equals("MaxWindowBufferSize")) {
                  var3.setMaxWindowBufferSize(var4.getMaxWindowBufferSize());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 11);
               } else if (var5.equals("MinWindowBufferSize")) {
                  var3.setMinWindowBufferSize(var4.getMinWindowBufferSize());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 10);
               } else if (var5.equals("SynchronousWritePolicy")) {
                  var3.setSynchronousWritePolicy(var4.getSynchronousWritePolicy());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 8);
               } else if (var5.equals("FileLockingEnabled")) {
                  var3.setFileLockingEnabled(var4.isFileLockingEnabled());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 16);
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
            GenericFileStoreMBeanImpl var5 = (GenericFileStoreMBeanImpl)var1;
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
      }
   }
}
