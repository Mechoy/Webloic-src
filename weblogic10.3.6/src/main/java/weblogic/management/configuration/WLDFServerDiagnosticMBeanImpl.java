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
import weblogic.descriptor.BeanRemoveRejectedException;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBeanHelper;
import weblogic.descriptor.internal.Munger;
import weblogic.descriptor.internal.ReferenceManager;
import weblogic.descriptor.internal.ResolvedReference;
import weblogic.descriptor.internal.SchemaHelper;
import weblogic.management.ManagementException;
import weblogic.management.mbeans.custom.WLDFServerDiagnostic;
import weblogic.utils.ArrayUtils;
import weblogic.utils.PlatformConstants;
import weblogic.utils.collections.ArrayIterator;
import weblogic.utils.collections.CombinedIterator;

public class WLDFServerDiagnosticMBeanImpl extends ConfigurationMBeanImpl implements WLDFServerDiagnosticMBean, Serializable {
   private boolean _DataRetirementEnabled;
   private boolean _DataRetirementTestModeEnabled;
   private boolean _DiagnosticContextEnabled;
   private String _DiagnosticDataArchiveType;
   private JDBCSystemResourceMBean _DiagnosticJDBCResource;
   private int _DiagnosticStoreBlockSize;
   private String _DiagnosticStoreDir;
   private boolean _DiagnosticStoreFileLockingEnabled;
   private int _DiagnosticStoreIoBufferSize;
   private long _DiagnosticStoreMaxFileSize;
   private int _DiagnosticStoreMaxWindowBufferSize;
   private int _DiagnosticStoreMinWindowBufferSize;
   private long _EventPersistenceInterval;
   private long _EventsImageCaptureInterval;
   private String _ImageDir;
   private int _ImageTimeout;
   private String _Name;
   private int _PreferredStoreSizeLimit;
   private int _StoreSizeCheckPeriod;
   private boolean _SynchronousEventPersistenceEnabled;
   private WLDFDataRetirementByAgeMBean[] _WLDFDataRetirementByAges;
   private WLDFDataRetirementMBean[] _WLDFDataRetirements;
   private String _WLDFDiagnosticVolume;
   private WLDFServerDiagnostic _customizer;
   private static SchemaHelper2 _schemaHelper;

   public WLDFServerDiagnosticMBeanImpl() {
      try {
         this._customizer = new WLDFServerDiagnostic(this);
      } catch (Exception var2) {
         if (var2 instanceof RuntimeException) {
            throw (RuntimeException)var2;
         }

         throw new UndeclaredThrowableException(var2);
      }

      this._initializeProperty(-1);
   }

   public WLDFServerDiagnosticMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);

      try {
         this._customizer = new WLDFServerDiagnostic(this);
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         }

         throw new UndeclaredThrowableException(var4);
      }

      this._initializeProperty(-1);
   }

   public String getImageDir() {
      if (!this._isSet(7)) {
         try {
            return "logs" + PlatformConstants.FILE_SEP + "diagnostic_images";
         } catch (NullPointerException var2) {
         }
      }

      return this._ImageDir;
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

   public boolean isImageDirSet() {
      return this._isSet(7);
   }

   public boolean isNameSet() {
      return this._isSet(2);
   }

   public void setImageDir(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._ImageDir;
      this._ImageDir = var1;
      this._postSet(7, var2, var1);
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

   public int getImageTimeout() {
      return this._ImageTimeout;
   }

   public boolean isImageTimeoutSet() {
      return this._isSet(8);
   }

   public void setImageTimeout(int var1) {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("ImageTimeout", (long)var1, 0L, 1440L);
      int var2 = this._ImageTimeout;
      this._ImageTimeout = var1;
      this._postSet(8, var2, var1);
   }

   public long getEventsImageCaptureInterval() {
      return this._EventsImageCaptureInterval;
   }

   public boolean isEventsImageCaptureIntervalSet() {
      return this._isSet(9);
   }

   public void setEventsImageCaptureInterval(long var1) {
      long var3 = this._EventsImageCaptureInterval;
      this._EventsImageCaptureInterval = var1;
      this._postSet(9, var3, var1);
   }

   public String getDiagnosticStoreDir() {
      return this._DiagnosticStoreDir;
   }

   public boolean isDiagnosticStoreDirSet() {
      return this._isSet(10);
   }

   public void setDiagnosticStoreDir(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._DiagnosticStoreDir;
      this._DiagnosticStoreDir = var1;
      this._postSet(10, var2, var1);
   }

   public boolean isDiagnosticStoreFileLockingEnabled() {
      return this._DiagnosticStoreFileLockingEnabled;
   }

   public boolean isDiagnosticStoreFileLockingEnabledSet() {
      return this._isSet(11);
   }

   public void setDiagnosticStoreFileLockingEnabled(boolean var1) {
      boolean var2 = this._DiagnosticStoreFileLockingEnabled;
      this._DiagnosticStoreFileLockingEnabled = var1;
      this._postSet(11, var2, var1);
   }

   public int getDiagnosticStoreMinWindowBufferSize() {
      return this._DiagnosticStoreMinWindowBufferSize;
   }

   public boolean isDiagnosticStoreMinWindowBufferSizeSet() {
      return this._isSet(12);
   }

   public void touch() throws ConfigurationException {
      this._customizer.touch();
   }

   public void freezeCurrentValue(String var1) throws AttributeNotFoundException, MBeanException {
      this._customizer.freezeCurrentValue(var1);
   }

   public void setDiagnosticStoreMinWindowBufferSize(int var1) {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("DiagnosticStoreMinWindowBufferSize", (long)var1, -1L, 1073741824L);
      int var2 = this._DiagnosticStoreMinWindowBufferSize;
      this._DiagnosticStoreMinWindowBufferSize = var1;
      this._postSet(12, var2, var1);
   }

   public int getDiagnosticStoreMaxWindowBufferSize() {
      return this._DiagnosticStoreMaxWindowBufferSize;
   }

   public boolean isDiagnosticStoreMaxWindowBufferSizeSet() {
      return this._isSet(13);
   }

   public void restoreDefaultValue(String var1) throws AttributeNotFoundException {
      this._customizer.restoreDefaultValue(var1);
   }

   public void setDiagnosticStoreMaxWindowBufferSize(int var1) {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("DiagnosticStoreMaxWindowBufferSize", (long)var1, -1L, 1073741824L);
      int var2 = this._DiagnosticStoreMaxWindowBufferSize;
      this._DiagnosticStoreMaxWindowBufferSize = var1;
      this._postSet(13, var2, var1);
   }

   public int getDiagnosticStoreIoBufferSize() {
      return this._DiagnosticStoreIoBufferSize;
   }

   public boolean isDiagnosticStoreIoBufferSizeSet() {
      return this._isSet(14);
   }

   public void setDiagnosticStoreIoBufferSize(int var1) {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("DiagnosticStoreIoBufferSize", (long)var1, -1L, 67108864L);
      int var2 = this._DiagnosticStoreIoBufferSize;
      this._DiagnosticStoreIoBufferSize = var1;
      this._postSet(14, var2, var1);
   }

   public long getDiagnosticStoreMaxFileSize() {
      return this._DiagnosticStoreMaxFileSize;
   }

   public boolean isDiagnosticStoreMaxFileSizeSet() {
      return this._isSet(15);
   }

   public void setDiagnosticStoreMaxFileSize(long var1) {
      weblogic.descriptor.beangen.LegalChecks.checkMin("DiagnosticStoreMaxFileSize", var1, 10485760L);
      long var3 = this._DiagnosticStoreMaxFileSize;
      this._DiagnosticStoreMaxFileSize = var1;
      this._postSet(15, var3, var1);
   }

   public int getDiagnosticStoreBlockSize() {
      return this._DiagnosticStoreBlockSize;
   }

   public boolean isDiagnosticStoreBlockSizeSet() {
      return this._isSet(16);
   }

   public void setDiagnosticStoreBlockSize(int var1) {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("DiagnosticStoreBlockSize", (long)var1, -1L, 8192L);
      int var2 = this._DiagnosticStoreBlockSize;
      this._DiagnosticStoreBlockSize = var1;
      this._postSet(16, var2, var1);
   }

   public String getDiagnosticDataArchiveType() {
      return this._DiagnosticDataArchiveType;
   }

   public boolean isDiagnosticDataArchiveTypeSet() {
      return this._isSet(17);
   }

   public void setDiagnosticDataArchiveType(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{"FileStoreArchive", "JDBCArchive"};
      var1 = weblogic.descriptor.beangen.LegalChecks.checkInEnum("DiagnosticDataArchiveType", var1, var2);
      String var3 = this._DiagnosticDataArchiveType;
      this._DiagnosticDataArchiveType = var1;
      this._postSet(17, var3, var1);
   }

   public JDBCSystemResourceMBean getDiagnosticJDBCResource() {
      return this._DiagnosticJDBCResource;
   }

   public String getDiagnosticJDBCResourceAsString() {
      AbstractDescriptorBean var1 = (AbstractDescriptorBean)this.getDiagnosticJDBCResource();
      return var1 == null ? null : var1._getKey().toString();
   }

   public boolean isDiagnosticJDBCResourceSet() {
      return this._isSet(18);
   }

   public void setDiagnosticJDBCResourceAsString(String var1) {
      if (var1 != null && var1.length() != 0) {
         var1 = var1 == null ? null : var1.trim();
         this._getReferenceManager().registerUnresolvedReference(var1, JDBCSystemResourceMBean.class, new ReferenceManager.Resolver(this, 18) {
            public void resolveReference(Object var1) {
               try {
                  WLDFServerDiagnosticMBeanImpl.this.setDiagnosticJDBCResource((JDBCSystemResourceMBean)var1);
               } catch (RuntimeException var3) {
                  throw var3;
               } catch (Exception var4) {
                  throw new AssertionError("Impossible exception: " + var4);
               }
            }
         });
      } else {
         JDBCSystemResourceMBean var2 = this._DiagnosticJDBCResource;
         this._initializeProperty(18);
         this._postSet(18, var2, this._DiagnosticJDBCResource);
      }

   }

   public void setDiagnosticJDBCResource(JDBCSystemResourceMBean var1) {
      if (var1 != null) {
         ResolvedReference var2 = new ResolvedReference(this, 18, (AbstractDescriptorBean)var1) {
            protected Object getPropertyValue() {
               return WLDFServerDiagnosticMBeanImpl.this.getDiagnosticJDBCResource();
            }
         };
         this._getReferenceManager().registerResolvedReference((AbstractDescriptorBean)var1, var2);
      }

      JDBCSystemResourceMBean var3 = this._DiagnosticJDBCResource;
      this._DiagnosticJDBCResource = var1;
      this._postSet(18, var3, var1);
   }

   public boolean isSynchronousEventPersistenceEnabled() {
      return this._SynchronousEventPersistenceEnabled;
   }

   public boolean isSynchronousEventPersistenceEnabledSet() {
      return this._isSet(19);
   }

   public void setSynchronousEventPersistenceEnabled(boolean var1) {
      boolean var2 = this._SynchronousEventPersistenceEnabled;
      this._SynchronousEventPersistenceEnabled = var1;
      this._postSet(19, var2, var1);
   }

   public long getEventPersistenceInterval() {
      return this._EventPersistenceInterval;
   }

   public boolean isEventPersistenceIntervalSet() {
      return this._isSet(20);
   }

   public void setEventPersistenceInterval(long var1) {
      long var3 = this._EventPersistenceInterval;
      this._EventPersistenceInterval = var1;
      this._postSet(20, var3, var1);
   }

   public boolean isDiagnosticContextEnabled() {
      return this._DiagnosticContextEnabled;
   }

   public boolean isDiagnosticContextEnabledSet() {
      return this._isSet(21);
   }

   public void setDiagnosticContextEnabled(boolean var1) {
      boolean var2 = this._DiagnosticContextEnabled;
      this._DiagnosticContextEnabled = var1;
      this._postSet(21, var2, var1);
   }

   public boolean isDataRetirementTestModeEnabled() {
      return this._DataRetirementTestModeEnabled;
   }

   public boolean isDataRetirementTestModeEnabledSet() {
      return this._isSet(22);
   }

   public void setDataRetirementTestModeEnabled(boolean var1) {
      boolean var2 = this._DataRetirementTestModeEnabled;
      this._DataRetirementTestModeEnabled = var1;
      this._postSet(22, var2, var1);
   }

   public boolean isDataRetirementEnabled() {
      return this._DataRetirementEnabled;
   }

   public boolean isDataRetirementEnabledSet() {
      return this._isSet(23);
   }

   public void setDataRetirementEnabled(boolean var1) {
      boolean var2 = this._DataRetirementEnabled;
      this._DataRetirementEnabled = var1;
      this._postSet(23, var2, var1);
   }

   public int getPreferredStoreSizeLimit() {
      return this._PreferredStoreSizeLimit;
   }

   public boolean isPreferredStoreSizeLimitSet() {
      return this._isSet(24);
   }

   public void setPreferredStoreSizeLimit(int var1) {
      weblogic.descriptor.beangen.LegalChecks.checkMin("PreferredStoreSizeLimit", var1, 10);
      int var2 = this._PreferredStoreSizeLimit;
      this._PreferredStoreSizeLimit = var1;
      this._postSet(24, var2, var1);
   }

   public int getStoreSizeCheckPeriod() {
      return this._StoreSizeCheckPeriod;
   }

   public boolean isStoreSizeCheckPeriodSet() {
      return this._isSet(25);
   }

   public void setStoreSizeCheckPeriod(int var1) {
      weblogic.descriptor.beangen.LegalChecks.checkMin("StoreSizeCheckPeriod", var1, 1);
      int var2 = this._StoreSizeCheckPeriod;
      this._StoreSizeCheckPeriod = var1;
      this._postSet(25, var2, var1);
   }

   public void addWLDFDataRetirement(WLDFDataRetirementMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 26)) {
         WLDFDataRetirementMBean[] var2 = (WLDFDataRetirementMBean[])((WLDFDataRetirementMBean[])this._getHelper()._extendArray(this.getWLDFDataRetirements(), WLDFDataRetirementMBean.class, var1));

         try {
            this.setWLDFDataRetirements(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public WLDFDataRetirementMBean[] getWLDFDataRetirements() {
      return this._customizer.getWLDFDataRetirements();
   }

   public boolean isWLDFDataRetirementsSet() {
      return this._isSet(26);
   }

   public void removeWLDFDataRetirement(WLDFDataRetirementMBean var1) {
      WLDFDataRetirementMBean[] var2 = this.getWLDFDataRetirements();
      WLDFDataRetirementMBean[] var3 = (WLDFDataRetirementMBean[])((WLDFDataRetirementMBean[])this._getHelper()._removeElement(var2, WLDFDataRetirementMBean.class, var1));
      if (var3.length != var2.length) {
         try {
            this.setWLDFDataRetirements(var3);
         } catch (Exception var5) {
            if (var5 instanceof RuntimeException) {
               throw (RuntimeException)var5;
            }

            throw new UndeclaredThrowableException(var5);
         }
      }

   }

   public void setWLDFDataRetirements(WLDFDataRetirementMBean[] var1) throws InvalidAttributeValueException {
      Object var2 = var1 == null ? new WLDFDataRetirementMBeanImpl[0] : var1;
      this._WLDFDataRetirements = (WLDFDataRetirementMBean[])var2;
   }

   public WLDFDataRetirementMBean lookupWLDFDataRetirement(String var1) {
      return this._customizer.lookupWLDFDataRetirement(var1);
   }

   public void addWLDFDataRetirementByAge(WLDFDataRetirementByAgeMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 27)) {
         WLDFDataRetirementByAgeMBean[] var2;
         if (this._isSet(27)) {
            var2 = (WLDFDataRetirementByAgeMBean[])((WLDFDataRetirementByAgeMBean[])this._getHelper()._extendArray(this.getWLDFDataRetirementByAges(), WLDFDataRetirementByAgeMBean.class, var1));
         } else {
            var2 = new WLDFDataRetirementByAgeMBean[]{var1};
         }

         try {
            this.setWLDFDataRetirementByAges(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public WLDFDataRetirementByAgeMBean[] getWLDFDataRetirementByAges() {
      return this._WLDFDataRetirementByAges;
   }

   public boolean isWLDFDataRetirementByAgesSet() {
      return this._isSet(27);
   }

   public void removeWLDFDataRetirementByAge(WLDFDataRetirementByAgeMBean var1) {
      this.destroyWLDFDataRetirementByAge(var1);
   }

   public void setWLDFDataRetirementByAges(WLDFDataRetirementByAgeMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new WLDFDataRetirementByAgeMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 27)) {
            this._getReferenceManager().registerBean(var3, false);
            this._postCreate(var3);
         }
      }

      WLDFDataRetirementByAgeMBean[] var5 = this._WLDFDataRetirementByAges;
      this._WLDFDataRetirementByAges = (WLDFDataRetirementByAgeMBean[])var4;
      this._postSet(27, var5, var4);
   }

   public WLDFDataRetirementByAgeMBean createWLDFDataRetirementByAge(String var1) {
      WLDFDataRetirementByAgeMBeanImpl var2 = new WLDFDataRetirementByAgeMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addWLDFDataRetirementByAge(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public void destroyWLDFDataRetirementByAge(WLDFDataRetirementByAgeMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 27);
         WLDFDataRetirementByAgeMBean[] var2 = this.getWLDFDataRetirementByAges();
         WLDFDataRetirementByAgeMBean[] var3 = (WLDFDataRetirementByAgeMBean[])((WLDFDataRetirementByAgeMBean[])this._getHelper()._removeElement(var2, WLDFDataRetirementByAgeMBean.class, var1));
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
               this.setWLDFDataRetirementByAges(var3);
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

   public WLDFDataRetirementByAgeMBean lookupWLDFDataRetirementByAge(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._WLDFDataRetirementByAges).iterator();

      WLDFDataRetirementByAgeMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (WLDFDataRetirementByAgeMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public String getWLDFDiagnosticVolume() {
      return this._WLDFDiagnosticVolume;
   }

   public boolean isWLDFDiagnosticVolumeSet() {
      return this._isSet(28);
   }

   public void setWLDFDiagnosticVolume(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{"Off", "Low", "Medium", "High"};
      var1 = weblogic.descriptor.beangen.LegalChecks.checkInEnum("WLDFDiagnosticVolume", var1, var2);
      String var3 = this._WLDFDiagnosticVolume;
      this._WLDFDiagnosticVolume = var1;
      this._postSet(28, var3, var1);
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
         var1 = 17;
      }

      try {
         switch (var1) {
            case 17:
               this._DiagnosticDataArchiveType = "FileStoreArchive";
               if (var2) {
                  break;
               }
            case 18:
               this._DiagnosticJDBCResource = null;
               if (var2) {
                  break;
               }
            case 16:
               this._DiagnosticStoreBlockSize = -1;
               if (var2) {
                  break;
               }
            case 10:
               this._DiagnosticStoreDir = "data/store/diagnostics";
               if (var2) {
                  break;
               }
            case 14:
               this._DiagnosticStoreIoBufferSize = -1;
               if (var2) {
                  break;
               }
            case 15:
               this._DiagnosticStoreMaxFileSize = 1342177280L;
               if (var2) {
                  break;
               }
            case 13:
               this._DiagnosticStoreMaxWindowBufferSize = -1;
               if (var2) {
                  break;
               }
            case 12:
               this._DiagnosticStoreMinWindowBufferSize = -1;
               if (var2) {
                  break;
               }
            case 20:
               this._EventPersistenceInterval = 5000L;
               if (var2) {
                  break;
               }
            case 9:
               this._EventsImageCaptureInterval = 60000L;
               if (var2) {
                  break;
               }
            case 7:
               this._ImageDir = null;
               if (var2) {
                  break;
               }
            case 8:
               this._ImageTimeout = 1;
               if (var2) {
                  break;
               }
            case 2:
               this._customizer.setName((String)null);
               if (var2) {
                  break;
               }
            case 24:
               this._PreferredStoreSizeLimit = 100;
               if (var2) {
                  break;
               }
            case 25:
               this._StoreSizeCheckPeriod = 1;
               if (var2) {
                  break;
               }
            case 27:
               this._WLDFDataRetirementByAges = new WLDFDataRetirementByAgeMBean[0];
               if (var2) {
                  break;
               }
            case 26:
               this._WLDFDataRetirements = new WLDFDataRetirementMBean[0];
               if (var2) {
                  break;
               }
            case 28:
               this._WLDFDiagnosticVolume = "Low";
               if (var2) {
                  break;
               }
            case 23:
               this._DataRetirementEnabled = true;
               if (var2) {
                  break;
               }
            case 22:
               this._DataRetirementTestModeEnabled = false;
               if (var2) {
                  break;
               }
            case 21:
               this._DiagnosticContextEnabled = false;
               if (var2) {
                  break;
               }
            case 11:
               this._DiagnosticStoreFileLockingEnabled = true;
               if (var2) {
                  break;
               }
            case 19:
               this._SynchronousEventPersistenceEnabled = false;
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
      return "WLDFServerDiagnostic";
   }

   public void putValue(String var1, Object var2) {
      boolean var8;
      if (var1.equals("DataRetirementEnabled")) {
         var8 = this._DataRetirementEnabled;
         this._DataRetirementEnabled = (Boolean)var2;
         this._postSet(23, var8, this._DataRetirementEnabled);
      } else if (var1.equals("DataRetirementTestModeEnabled")) {
         var8 = this._DataRetirementTestModeEnabled;
         this._DataRetirementTestModeEnabled = (Boolean)var2;
         this._postSet(22, var8, this._DataRetirementTestModeEnabled);
      } else if (var1.equals("DiagnosticContextEnabled")) {
         var8 = this._DiagnosticContextEnabled;
         this._DiagnosticContextEnabled = (Boolean)var2;
         this._postSet(21, var8, this._DiagnosticContextEnabled);
      } else {
         String var5;
         if (var1.equals("DiagnosticDataArchiveType")) {
            var5 = this._DiagnosticDataArchiveType;
            this._DiagnosticDataArchiveType = (String)var2;
            this._postSet(17, var5, this._DiagnosticDataArchiveType);
         } else if (var1.equals("DiagnosticJDBCResource")) {
            JDBCSystemResourceMBean var11 = this._DiagnosticJDBCResource;
            this._DiagnosticJDBCResource = (JDBCSystemResourceMBean)var2;
            this._postSet(18, var11, this._DiagnosticJDBCResource);
         } else {
            int var9;
            if (var1.equals("DiagnosticStoreBlockSize")) {
               var9 = this._DiagnosticStoreBlockSize;
               this._DiagnosticStoreBlockSize = (Integer)var2;
               this._postSet(16, var9, this._DiagnosticStoreBlockSize);
            } else if (var1.equals("DiagnosticStoreDir")) {
               var5 = this._DiagnosticStoreDir;
               this._DiagnosticStoreDir = (String)var2;
               this._postSet(10, var5, this._DiagnosticStoreDir);
            } else if (var1.equals("DiagnosticStoreFileLockingEnabled")) {
               var8 = this._DiagnosticStoreFileLockingEnabled;
               this._DiagnosticStoreFileLockingEnabled = (Boolean)var2;
               this._postSet(11, var8, this._DiagnosticStoreFileLockingEnabled);
            } else if (var1.equals("DiagnosticStoreIoBufferSize")) {
               var9 = this._DiagnosticStoreIoBufferSize;
               this._DiagnosticStoreIoBufferSize = (Integer)var2;
               this._postSet(14, var9, this._DiagnosticStoreIoBufferSize);
            } else {
               long var10;
               if (var1.equals("DiagnosticStoreMaxFileSize")) {
                  var10 = this._DiagnosticStoreMaxFileSize;
                  this._DiagnosticStoreMaxFileSize = (Long)var2;
                  this._postSet(15, var10, this._DiagnosticStoreMaxFileSize);
               } else if (var1.equals("DiagnosticStoreMaxWindowBufferSize")) {
                  var9 = this._DiagnosticStoreMaxWindowBufferSize;
                  this._DiagnosticStoreMaxWindowBufferSize = (Integer)var2;
                  this._postSet(13, var9, this._DiagnosticStoreMaxWindowBufferSize);
               } else if (var1.equals("DiagnosticStoreMinWindowBufferSize")) {
                  var9 = this._DiagnosticStoreMinWindowBufferSize;
                  this._DiagnosticStoreMinWindowBufferSize = (Integer)var2;
                  this._postSet(12, var9, this._DiagnosticStoreMinWindowBufferSize);
               } else if (var1.equals("EventPersistenceInterval")) {
                  var10 = this._EventPersistenceInterval;
                  this._EventPersistenceInterval = (Long)var2;
                  this._postSet(20, var10, this._EventPersistenceInterval);
               } else if (var1.equals("EventsImageCaptureInterval")) {
                  var10 = this._EventsImageCaptureInterval;
                  this._EventsImageCaptureInterval = (Long)var2;
                  this._postSet(9, var10, this._EventsImageCaptureInterval);
               } else if (var1.equals("ImageDir")) {
                  var5 = this._ImageDir;
                  this._ImageDir = (String)var2;
                  this._postSet(7, var5, this._ImageDir);
               } else if (var1.equals("ImageTimeout")) {
                  var9 = this._ImageTimeout;
                  this._ImageTimeout = (Integer)var2;
                  this._postSet(8, var9, this._ImageTimeout);
               } else if (var1.equals("Name")) {
                  var5 = this._Name;
                  this._Name = (String)var2;
                  this._postSet(2, var5, this._Name);
               } else if (var1.equals("PreferredStoreSizeLimit")) {
                  var9 = this._PreferredStoreSizeLimit;
                  this._PreferredStoreSizeLimit = (Integer)var2;
                  this._postSet(24, var9, this._PreferredStoreSizeLimit);
               } else if (var1.equals("StoreSizeCheckPeriod")) {
                  var9 = this._StoreSizeCheckPeriod;
                  this._StoreSizeCheckPeriod = (Integer)var2;
                  this._postSet(25, var9, this._StoreSizeCheckPeriod);
               } else if (var1.equals("SynchronousEventPersistenceEnabled")) {
                  var8 = this._SynchronousEventPersistenceEnabled;
                  this._SynchronousEventPersistenceEnabled = (Boolean)var2;
                  this._postSet(19, var8, this._SynchronousEventPersistenceEnabled);
               } else if (var1.equals("WLDFDataRetirementByAges")) {
                  WLDFDataRetirementByAgeMBean[] var7 = this._WLDFDataRetirementByAges;
                  this._WLDFDataRetirementByAges = (WLDFDataRetirementByAgeMBean[])((WLDFDataRetirementByAgeMBean[])var2);
                  this._postSet(27, var7, this._WLDFDataRetirementByAges);
               } else if (var1.equals("WLDFDataRetirements")) {
                  WLDFDataRetirementMBean[] var6 = this._WLDFDataRetirements;
                  this._WLDFDataRetirements = (WLDFDataRetirementMBean[])((WLDFDataRetirementMBean[])var2);
                  this._postSet(26, var6, this._WLDFDataRetirements);
               } else if (var1.equals("WLDFDiagnosticVolume")) {
                  var5 = this._WLDFDiagnosticVolume;
                  this._WLDFDiagnosticVolume = (String)var2;
                  this._postSet(28, var5, this._WLDFDiagnosticVolume);
               } else if (var1.equals("customizer")) {
                  WLDFServerDiagnostic var3 = this._customizer;
                  this._customizer = (WLDFServerDiagnostic)var2;
               } else {
                  super.putValue(var1, var2);
               }
            }
         }
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("DataRetirementEnabled")) {
         return new Boolean(this._DataRetirementEnabled);
      } else if (var1.equals("DataRetirementTestModeEnabled")) {
         return new Boolean(this._DataRetirementTestModeEnabled);
      } else if (var1.equals("DiagnosticContextEnabled")) {
         return new Boolean(this._DiagnosticContextEnabled);
      } else if (var1.equals("DiagnosticDataArchiveType")) {
         return this._DiagnosticDataArchiveType;
      } else if (var1.equals("DiagnosticJDBCResource")) {
         return this._DiagnosticJDBCResource;
      } else if (var1.equals("DiagnosticStoreBlockSize")) {
         return new Integer(this._DiagnosticStoreBlockSize);
      } else if (var1.equals("DiagnosticStoreDir")) {
         return this._DiagnosticStoreDir;
      } else if (var1.equals("DiagnosticStoreFileLockingEnabled")) {
         return new Boolean(this._DiagnosticStoreFileLockingEnabled);
      } else if (var1.equals("DiagnosticStoreIoBufferSize")) {
         return new Integer(this._DiagnosticStoreIoBufferSize);
      } else if (var1.equals("DiagnosticStoreMaxFileSize")) {
         return new Long(this._DiagnosticStoreMaxFileSize);
      } else if (var1.equals("DiagnosticStoreMaxWindowBufferSize")) {
         return new Integer(this._DiagnosticStoreMaxWindowBufferSize);
      } else if (var1.equals("DiagnosticStoreMinWindowBufferSize")) {
         return new Integer(this._DiagnosticStoreMinWindowBufferSize);
      } else if (var1.equals("EventPersistenceInterval")) {
         return new Long(this._EventPersistenceInterval);
      } else if (var1.equals("EventsImageCaptureInterval")) {
         return new Long(this._EventsImageCaptureInterval);
      } else if (var1.equals("ImageDir")) {
         return this._ImageDir;
      } else if (var1.equals("ImageTimeout")) {
         return new Integer(this._ImageTimeout);
      } else if (var1.equals("Name")) {
         return this._Name;
      } else if (var1.equals("PreferredStoreSizeLimit")) {
         return new Integer(this._PreferredStoreSizeLimit);
      } else if (var1.equals("StoreSizeCheckPeriod")) {
         return new Integer(this._StoreSizeCheckPeriod);
      } else if (var1.equals("SynchronousEventPersistenceEnabled")) {
         return new Boolean(this._SynchronousEventPersistenceEnabled);
      } else if (var1.equals("WLDFDataRetirementByAges")) {
         return this._WLDFDataRetirementByAges;
      } else if (var1.equals("WLDFDataRetirements")) {
         return this._WLDFDataRetirements;
      } else if (var1.equals("WLDFDiagnosticVolume")) {
         return this._WLDFDiagnosticVolume;
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
            case 7:
            case 8:
            case 10:
            case 11:
            case 12:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 21:
            case 25:
            case 32:
            case 34:
            case 35:
            case 36:
            case 38:
            default:
               break;
            case 9:
               if (var1.equals("image-dir")) {
                  return 7;
               }
               break;
            case 13:
               if (var1.equals("image-timeout")) {
                  return 8;
               }
               break;
            case 20:
               if (var1.equals("diagnostic-store-dir")) {
                  return 10;
               }

               if (var1.equals("wldf-data-retirement")) {
                  return 26;
               }
               break;
            case 22:
               if (var1.equals("wldf-diagnostic-volume")) {
                  return 28;
               }
               break;
            case 23:
               if (var1.equals("store-size-check-period")) {
                  return 25;
               }

               if (var1.equals("data-retirement-enabled")) {
                  return 23;
               }
               break;
            case 24:
               if (var1.equals("diagnostic-jdbc-resource")) {
                  return 18;
               }
               break;
            case 26:
               if (var1.equals("event-persistence-interval")) {
                  return 20;
               }

               if (var1.equals("preferred-store-size-limit")) {
                  return 24;
               }

               if (var1.equals("diagnostic-context-enabled")) {
                  return 21;
               }
               break;
            case 27:
               if (var1.equals("diagnostic-store-block-size")) {
                  return 16;
               }

               if (var1.equals("wldf-data-retirement-by-age")) {
                  return 27;
               }
               break;
            case 28:
               if (var1.equals("diagnostic-data-archive-type")) {
                  return 17;
               }
               break;
            case 29:
               if (var1.equals("events-image-capture-interval")) {
                  return 9;
               }
               break;
            case 30:
               if (var1.equals("diagnostic-store-max-file-size")) {
                  return 15;
               }
               break;
            case 31:
               if (var1.equals("diagnostic-store-io-buffer-size")) {
                  return 14;
               }
               break;
            case 33:
               if (var1.equals("data-retirement-test-mode-enabled")) {
                  return 22;
               }
               break;
            case 37:
               if (var1.equals("diagnostic-store-file-locking-enabled")) {
                  return 11;
               }

               if (var1.equals("synchronous-event-persistence-enabled")) {
                  return 19;
               }
               break;
            case 39:
               if (var1.equals("diagnostic-store-max-window-buffer-size")) {
                  return 13;
               }

               if (var1.equals("diagnostic-store-min-window-buffer-size")) {
                  return 12;
               }
         }

         return super.getPropertyIndex(var1);
      }

      public SchemaHelper getSchemaHelper(int var1) {
         switch (var1) {
            case 27:
               return new WLDFDataRetirementByAgeMBeanImpl.SchemaHelper2();
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
               return "image-dir";
            case 8:
               return "image-timeout";
            case 9:
               return "events-image-capture-interval";
            case 10:
               return "diagnostic-store-dir";
            case 11:
               return "diagnostic-store-file-locking-enabled";
            case 12:
               return "diagnostic-store-min-window-buffer-size";
            case 13:
               return "diagnostic-store-max-window-buffer-size";
            case 14:
               return "diagnostic-store-io-buffer-size";
            case 15:
               return "diagnostic-store-max-file-size";
            case 16:
               return "diagnostic-store-block-size";
            case 17:
               return "diagnostic-data-archive-type";
            case 18:
               return "diagnostic-jdbc-resource";
            case 19:
               return "synchronous-event-persistence-enabled";
            case 20:
               return "event-persistence-interval";
            case 21:
               return "diagnostic-context-enabled";
            case 22:
               return "data-retirement-test-mode-enabled";
            case 23:
               return "data-retirement-enabled";
            case 24:
               return "preferred-store-size-limit";
            case 25:
               return "store-size-check-period";
            case 26:
               return "wldf-data-retirement";
            case 27:
               return "wldf-data-retirement-by-age";
            case 28:
               return "wldf-diagnostic-volume";
         }
      }

      public boolean isArray(int var1) {
         switch (var1) {
            case 26:
               return true;
            case 27:
               return true;
            default:
               return super.isArray(var1);
         }
      }

      public boolean isBean(int var1) {
         switch (var1) {
            case 27:
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
      private WLDFServerDiagnosticMBeanImpl bean;

      protected Helper(WLDFServerDiagnosticMBeanImpl var1) {
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
               return "ImageDir";
            case 8:
               return "ImageTimeout";
            case 9:
               return "EventsImageCaptureInterval";
            case 10:
               return "DiagnosticStoreDir";
            case 11:
               return "DiagnosticStoreFileLockingEnabled";
            case 12:
               return "DiagnosticStoreMinWindowBufferSize";
            case 13:
               return "DiagnosticStoreMaxWindowBufferSize";
            case 14:
               return "DiagnosticStoreIoBufferSize";
            case 15:
               return "DiagnosticStoreMaxFileSize";
            case 16:
               return "DiagnosticStoreBlockSize";
            case 17:
               return "DiagnosticDataArchiveType";
            case 18:
               return "DiagnosticJDBCResource";
            case 19:
               return "SynchronousEventPersistenceEnabled";
            case 20:
               return "EventPersistenceInterval";
            case 21:
               return "DiagnosticContextEnabled";
            case 22:
               return "DataRetirementTestModeEnabled";
            case 23:
               return "DataRetirementEnabled";
            case 24:
               return "PreferredStoreSizeLimit";
            case 25:
               return "StoreSizeCheckPeriod";
            case 26:
               return "WLDFDataRetirements";
            case 27:
               return "WLDFDataRetirementByAges";
            case 28:
               return "WLDFDiagnosticVolume";
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("DiagnosticDataArchiveType")) {
            return 17;
         } else if (var1.equals("DiagnosticJDBCResource")) {
            return 18;
         } else if (var1.equals("DiagnosticStoreBlockSize")) {
            return 16;
         } else if (var1.equals("DiagnosticStoreDir")) {
            return 10;
         } else if (var1.equals("DiagnosticStoreIoBufferSize")) {
            return 14;
         } else if (var1.equals("DiagnosticStoreMaxFileSize")) {
            return 15;
         } else if (var1.equals("DiagnosticStoreMaxWindowBufferSize")) {
            return 13;
         } else if (var1.equals("DiagnosticStoreMinWindowBufferSize")) {
            return 12;
         } else if (var1.equals("EventPersistenceInterval")) {
            return 20;
         } else if (var1.equals("EventsImageCaptureInterval")) {
            return 9;
         } else if (var1.equals("ImageDir")) {
            return 7;
         } else if (var1.equals("ImageTimeout")) {
            return 8;
         } else if (var1.equals("Name")) {
            return 2;
         } else if (var1.equals("PreferredStoreSizeLimit")) {
            return 24;
         } else if (var1.equals("StoreSizeCheckPeriod")) {
            return 25;
         } else if (var1.equals("WLDFDataRetirementByAges")) {
            return 27;
         } else if (var1.equals("WLDFDataRetirements")) {
            return 26;
         } else if (var1.equals("WLDFDiagnosticVolume")) {
            return 28;
         } else if (var1.equals("DataRetirementEnabled")) {
            return 23;
         } else if (var1.equals("DataRetirementTestModeEnabled")) {
            return 22;
         } else if (var1.equals("DiagnosticContextEnabled")) {
            return 21;
         } else if (var1.equals("DiagnosticStoreFileLockingEnabled")) {
            return 11;
         } else {
            return var1.equals("SynchronousEventPersistenceEnabled") ? 19 : super.getPropertyIndex(var1);
         }
      }

      public Iterator getChildren() {
         ArrayList var1 = new ArrayList();
         var1.add(new ArrayIterator(this.bean.getWLDFDataRetirementByAges()));
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
            if (this.bean.isDiagnosticDataArchiveTypeSet()) {
               var2.append("DiagnosticDataArchiveType");
               var2.append(String.valueOf(this.bean.getDiagnosticDataArchiveType()));
            }

            if (this.bean.isDiagnosticJDBCResourceSet()) {
               var2.append("DiagnosticJDBCResource");
               var2.append(String.valueOf(this.bean.getDiagnosticJDBCResource()));
            }

            if (this.bean.isDiagnosticStoreBlockSizeSet()) {
               var2.append("DiagnosticStoreBlockSize");
               var2.append(String.valueOf(this.bean.getDiagnosticStoreBlockSize()));
            }

            if (this.bean.isDiagnosticStoreDirSet()) {
               var2.append("DiagnosticStoreDir");
               var2.append(String.valueOf(this.bean.getDiagnosticStoreDir()));
            }

            if (this.bean.isDiagnosticStoreIoBufferSizeSet()) {
               var2.append("DiagnosticStoreIoBufferSize");
               var2.append(String.valueOf(this.bean.getDiagnosticStoreIoBufferSize()));
            }

            if (this.bean.isDiagnosticStoreMaxFileSizeSet()) {
               var2.append("DiagnosticStoreMaxFileSize");
               var2.append(String.valueOf(this.bean.getDiagnosticStoreMaxFileSize()));
            }

            if (this.bean.isDiagnosticStoreMaxWindowBufferSizeSet()) {
               var2.append("DiagnosticStoreMaxWindowBufferSize");
               var2.append(String.valueOf(this.bean.getDiagnosticStoreMaxWindowBufferSize()));
            }

            if (this.bean.isDiagnosticStoreMinWindowBufferSizeSet()) {
               var2.append("DiagnosticStoreMinWindowBufferSize");
               var2.append(String.valueOf(this.bean.getDiagnosticStoreMinWindowBufferSize()));
            }

            if (this.bean.isEventPersistenceIntervalSet()) {
               var2.append("EventPersistenceInterval");
               var2.append(String.valueOf(this.bean.getEventPersistenceInterval()));
            }

            if (this.bean.isEventsImageCaptureIntervalSet()) {
               var2.append("EventsImageCaptureInterval");
               var2.append(String.valueOf(this.bean.getEventsImageCaptureInterval()));
            }

            if (this.bean.isImageDirSet()) {
               var2.append("ImageDir");
               var2.append(String.valueOf(this.bean.getImageDir()));
            }

            if (this.bean.isImageTimeoutSet()) {
               var2.append("ImageTimeout");
               var2.append(String.valueOf(this.bean.getImageTimeout()));
            }

            if (this.bean.isNameSet()) {
               var2.append("Name");
               var2.append(String.valueOf(this.bean.getName()));
            }

            if (this.bean.isPreferredStoreSizeLimitSet()) {
               var2.append("PreferredStoreSizeLimit");
               var2.append(String.valueOf(this.bean.getPreferredStoreSizeLimit()));
            }

            if (this.bean.isStoreSizeCheckPeriodSet()) {
               var2.append("StoreSizeCheckPeriod");
               var2.append(String.valueOf(this.bean.getStoreSizeCheckPeriod()));
            }

            var5 = 0L;

            for(int var7 = 0; var7 < this.bean.getWLDFDataRetirementByAges().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getWLDFDataRetirementByAges()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            if (this.bean.isWLDFDataRetirementsSet()) {
               var2.append("WLDFDataRetirements");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getWLDFDataRetirements())));
            }

            if (this.bean.isWLDFDiagnosticVolumeSet()) {
               var2.append("WLDFDiagnosticVolume");
               var2.append(String.valueOf(this.bean.getWLDFDiagnosticVolume()));
            }

            if (this.bean.isDataRetirementEnabledSet()) {
               var2.append("DataRetirementEnabled");
               var2.append(String.valueOf(this.bean.isDataRetirementEnabled()));
            }

            if (this.bean.isDataRetirementTestModeEnabledSet()) {
               var2.append("DataRetirementTestModeEnabled");
               var2.append(String.valueOf(this.bean.isDataRetirementTestModeEnabled()));
            }

            if (this.bean.isDiagnosticContextEnabledSet()) {
               var2.append("DiagnosticContextEnabled");
               var2.append(String.valueOf(this.bean.isDiagnosticContextEnabled()));
            }

            if (this.bean.isDiagnosticStoreFileLockingEnabledSet()) {
               var2.append("DiagnosticStoreFileLockingEnabled");
               var2.append(String.valueOf(this.bean.isDiagnosticStoreFileLockingEnabled()));
            }

            if (this.bean.isSynchronousEventPersistenceEnabledSet()) {
               var2.append("SynchronousEventPersistenceEnabled");
               var2.append(String.valueOf(this.bean.isSynchronousEventPersistenceEnabled()));
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
            WLDFServerDiagnosticMBeanImpl var2 = (WLDFServerDiagnosticMBeanImpl)var1;
            this.computeDiff("DiagnosticDataArchiveType", this.bean.getDiagnosticDataArchiveType(), var2.getDiagnosticDataArchiveType(), false);
            this.computeDiff("DiagnosticJDBCResource", this.bean.getDiagnosticJDBCResource(), var2.getDiagnosticJDBCResource(), false);
            this.computeDiff("DiagnosticStoreBlockSize", this.bean.getDiagnosticStoreBlockSize(), var2.getDiagnosticStoreBlockSize(), false);
            this.computeDiff("DiagnosticStoreDir", this.bean.getDiagnosticStoreDir(), var2.getDiagnosticStoreDir(), false);
            this.computeDiff("DiagnosticStoreIoBufferSize", this.bean.getDiagnosticStoreIoBufferSize(), var2.getDiagnosticStoreIoBufferSize(), false);
            this.computeDiff("DiagnosticStoreMaxFileSize", this.bean.getDiagnosticStoreMaxFileSize(), var2.getDiagnosticStoreMaxFileSize(), false);
            this.computeDiff("DiagnosticStoreMaxWindowBufferSize", this.bean.getDiagnosticStoreMaxWindowBufferSize(), var2.getDiagnosticStoreMaxWindowBufferSize(), false);
            this.computeDiff("DiagnosticStoreMinWindowBufferSize", this.bean.getDiagnosticStoreMinWindowBufferSize(), var2.getDiagnosticStoreMinWindowBufferSize(), false);
            this.computeDiff("EventPersistenceInterval", this.bean.getEventPersistenceInterval(), var2.getEventPersistenceInterval(), true);
            this.computeDiff("EventsImageCaptureInterval", this.bean.getEventsImageCaptureInterval(), var2.getEventsImageCaptureInterval(), true);
            this.computeDiff("ImageDir", this.bean.getImageDir(), var2.getImageDir(), true);
            this.computeDiff("ImageTimeout", this.bean.getImageTimeout(), var2.getImageTimeout(), true);
            this.computeDiff("Name", this.bean.getName(), var2.getName(), false);
            this.computeDiff("PreferredStoreSizeLimit", this.bean.getPreferredStoreSizeLimit(), var2.getPreferredStoreSizeLimit(), true);
            this.computeDiff("StoreSizeCheckPeriod", this.bean.getStoreSizeCheckPeriod(), var2.getStoreSizeCheckPeriod(), true);
            this.computeChildDiff("WLDFDataRetirementByAges", this.bean.getWLDFDataRetirementByAges(), var2.getWLDFDataRetirementByAges(), true);
            this.computeDiff("WLDFDiagnosticVolume", this.bean.getWLDFDiagnosticVolume(), var2.getWLDFDiagnosticVolume(), true);
            this.computeDiff("DataRetirementEnabled", this.bean.isDataRetirementEnabled(), var2.isDataRetirementEnabled(), true);
            this.computeDiff("DataRetirementTestModeEnabled", this.bean.isDataRetirementTestModeEnabled(), var2.isDataRetirementTestModeEnabled(), false);
            this.computeDiff("DiagnosticContextEnabled", this.bean.isDiagnosticContextEnabled(), var2.isDiagnosticContextEnabled(), true);
            this.computeDiff("DiagnosticStoreFileLockingEnabled", this.bean.isDiagnosticStoreFileLockingEnabled(), var2.isDiagnosticStoreFileLockingEnabled(), false);
            this.computeDiff("SynchronousEventPersistenceEnabled", this.bean.isSynchronousEventPersistenceEnabled(), var2.isSynchronousEventPersistenceEnabled(), false);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            WLDFServerDiagnosticMBeanImpl var3 = (WLDFServerDiagnosticMBeanImpl)var1.getSourceBean();
            WLDFServerDiagnosticMBeanImpl var4 = (WLDFServerDiagnosticMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("DiagnosticDataArchiveType")) {
                  var3.setDiagnosticDataArchiveType(var4.getDiagnosticDataArchiveType());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 17);
               } else if (var5.equals("DiagnosticJDBCResource")) {
                  var3.setDiagnosticJDBCResourceAsString(var4.getDiagnosticJDBCResourceAsString());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 18);
               } else if (var5.equals("DiagnosticStoreBlockSize")) {
                  var3.setDiagnosticStoreBlockSize(var4.getDiagnosticStoreBlockSize());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 16);
               } else if (var5.equals("DiagnosticStoreDir")) {
                  var3.setDiagnosticStoreDir(var4.getDiagnosticStoreDir());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 10);
               } else if (var5.equals("DiagnosticStoreIoBufferSize")) {
                  var3.setDiagnosticStoreIoBufferSize(var4.getDiagnosticStoreIoBufferSize());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 14);
               } else if (var5.equals("DiagnosticStoreMaxFileSize")) {
                  var3.setDiagnosticStoreMaxFileSize(var4.getDiagnosticStoreMaxFileSize());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 15);
               } else if (var5.equals("DiagnosticStoreMaxWindowBufferSize")) {
                  var3.setDiagnosticStoreMaxWindowBufferSize(var4.getDiagnosticStoreMaxWindowBufferSize());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 13);
               } else if (var5.equals("DiagnosticStoreMinWindowBufferSize")) {
                  var3.setDiagnosticStoreMinWindowBufferSize(var4.getDiagnosticStoreMinWindowBufferSize());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 12);
               } else if (var5.equals("EventPersistenceInterval")) {
                  var3.setEventPersistenceInterval(var4.getEventPersistenceInterval());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 20);
               } else if (var5.equals("EventsImageCaptureInterval")) {
                  var3.setEventsImageCaptureInterval(var4.getEventsImageCaptureInterval());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 9);
               } else if (var5.equals("ImageDir")) {
                  var3.setImageDir(var4.getImageDir());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 7);
               } else if (var5.equals("ImageTimeout")) {
                  var3.setImageTimeout(var4.getImageTimeout());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 8);
               } else if (var5.equals("Name")) {
                  var3.setName(var4.getName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 2);
               } else if (var5.equals("PreferredStoreSizeLimit")) {
                  var3.setPreferredStoreSizeLimit(var4.getPreferredStoreSizeLimit());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 24);
               } else if (var5.equals("StoreSizeCheckPeriod")) {
                  var3.setStoreSizeCheckPeriod(var4.getStoreSizeCheckPeriod());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 25);
               } else if (var5.equals("WLDFDataRetirementByAges")) {
                  if (var6 == 2) {
                     var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                     var3.addWLDFDataRetirementByAge((WLDFDataRetirementByAgeMBean)var2.getAddedObject());
                  } else {
                     if (var6 != 3) {
                        throw new AssertionError("Invalid type: " + var6);
                     }

                     var3.removeWLDFDataRetirementByAge((WLDFDataRetirementByAgeMBean)var2.getRemovedObject());
                  }

                  if (var3.getWLDFDataRetirementByAges() == null || var3.getWLDFDataRetirementByAges().length == 0) {
                     var3._conditionalUnset(var2.isUnsetUpdate(), 27);
                  }
               } else if (!var5.equals("WLDFDataRetirements")) {
                  if (var5.equals("WLDFDiagnosticVolume")) {
                     var3.setWLDFDiagnosticVolume(var4.getWLDFDiagnosticVolume());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 28);
                  } else if (var5.equals("DataRetirementEnabled")) {
                     var3.setDataRetirementEnabled(var4.isDataRetirementEnabled());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 23);
                  } else if (var5.equals("DataRetirementTestModeEnabled")) {
                     var3.setDataRetirementTestModeEnabled(var4.isDataRetirementTestModeEnabled());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 22);
                  } else if (var5.equals("DiagnosticContextEnabled")) {
                     var3.setDiagnosticContextEnabled(var4.isDiagnosticContextEnabled());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 21);
                  } else if (var5.equals("DiagnosticStoreFileLockingEnabled")) {
                     var3.setDiagnosticStoreFileLockingEnabled(var4.isDiagnosticStoreFileLockingEnabled());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 11);
                  } else if (var5.equals("SynchronousEventPersistenceEnabled")) {
                     var3.setSynchronousEventPersistenceEnabled(var4.isSynchronousEventPersistenceEnabled());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 19);
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
            WLDFServerDiagnosticMBeanImpl var5 = (WLDFServerDiagnosticMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("DiagnosticDataArchiveType")) && this.bean.isDiagnosticDataArchiveTypeSet()) {
               var5.setDiagnosticDataArchiveType(this.bean.getDiagnosticDataArchiveType());
            }

            if ((var3 == null || !var3.contains("DiagnosticJDBCResource")) && this.bean.isDiagnosticJDBCResourceSet()) {
               var5._unSet(var5, 18);
               var5.setDiagnosticJDBCResourceAsString(this.bean.getDiagnosticJDBCResourceAsString());
            }

            if ((var3 == null || !var3.contains("DiagnosticStoreBlockSize")) && this.bean.isDiagnosticStoreBlockSizeSet()) {
               var5.setDiagnosticStoreBlockSize(this.bean.getDiagnosticStoreBlockSize());
            }

            if ((var3 == null || !var3.contains("DiagnosticStoreDir")) && this.bean.isDiagnosticStoreDirSet()) {
               var5.setDiagnosticStoreDir(this.bean.getDiagnosticStoreDir());
            }

            if ((var3 == null || !var3.contains("DiagnosticStoreIoBufferSize")) && this.bean.isDiagnosticStoreIoBufferSizeSet()) {
               var5.setDiagnosticStoreIoBufferSize(this.bean.getDiagnosticStoreIoBufferSize());
            }

            if ((var3 == null || !var3.contains("DiagnosticStoreMaxFileSize")) && this.bean.isDiagnosticStoreMaxFileSizeSet()) {
               var5.setDiagnosticStoreMaxFileSize(this.bean.getDiagnosticStoreMaxFileSize());
            }

            if ((var3 == null || !var3.contains("DiagnosticStoreMaxWindowBufferSize")) && this.bean.isDiagnosticStoreMaxWindowBufferSizeSet()) {
               var5.setDiagnosticStoreMaxWindowBufferSize(this.bean.getDiagnosticStoreMaxWindowBufferSize());
            }

            if ((var3 == null || !var3.contains("DiagnosticStoreMinWindowBufferSize")) && this.bean.isDiagnosticStoreMinWindowBufferSizeSet()) {
               var5.setDiagnosticStoreMinWindowBufferSize(this.bean.getDiagnosticStoreMinWindowBufferSize());
            }

            if ((var3 == null || !var3.contains("EventPersistenceInterval")) && this.bean.isEventPersistenceIntervalSet()) {
               var5.setEventPersistenceInterval(this.bean.getEventPersistenceInterval());
            }

            if ((var3 == null || !var3.contains("EventsImageCaptureInterval")) && this.bean.isEventsImageCaptureIntervalSet()) {
               var5.setEventsImageCaptureInterval(this.bean.getEventsImageCaptureInterval());
            }

            if ((var3 == null || !var3.contains("ImageDir")) && this.bean.isImageDirSet()) {
               var5.setImageDir(this.bean.getImageDir());
            }

            if ((var3 == null || !var3.contains("ImageTimeout")) && this.bean.isImageTimeoutSet()) {
               var5.setImageTimeout(this.bean.getImageTimeout());
            }

            if ((var3 == null || !var3.contains("Name")) && this.bean.isNameSet()) {
               var5.setName(this.bean.getName());
            }

            if ((var3 == null || !var3.contains("PreferredStoreSizeLimit")) && this.bean.isPreferredStoreSizeLimitSet()) {
               var5.setPreferredStoreSizeLimit(this.bean.getPreferredStoreSizeLimit());
            }

            if ((var3 == null || !var3.contains("StoreSizeCheckPeriod")) && this.bean.isStoreSizeCheckPeriodSet()) {
               var5.setStoreSizeCheckPeriod(this.bean.getStoreSizeCheckPeriod());
            }

            if ((var3 == null || !var3.contains("WLDFDataRetirementByAges")) && this.bean.isWLDFDataRetirementByAgesSet() && !var5._isSet(27)) {
               WLDFDataRetirementByAgeMBean[] var6 = this.bean.getWLDFDataRetirementByAges();
               WLDFDataRetirementByAgeMBean[] var7 = new WLDFDataRetirementByAgeMBean[var6.length];

               for(int var8 = 0; var8 < var7.length; ++var8) {
                  var7[var8] = (WLDFDataRetirementByAgeMBean)((WLDFDataRetirementByAgeMBean)this.createCopy((AbstractDescriptorBean)var6[var8], var2));
               }

               var5.setWLDFDataRetirementByAges(var7);
            }

            if ((var3 == null || !var3.contains("WLDFDiagnosticVolume")) && this.bean.isWLDFDiagnosticVolumeSet()) {
               var5.setWLDFDiagnosticVolume(this.bean.getWLDFDiagnosticVolume());
            }

            if ((var3 == null || !var3.contains("DataRetirementEnabled")) && this.bean.isDataRetirementEnabledSet()) {
               var5.setDataRetirementEnabled(this.bean.isDataRetirementEnabled());
            }

            if ((var3 == null || !var3.contains("DataRetirementTestModeEnabled")) && this.bean.isDataRetirementTestModeEnabledSet()) {
               var5.setDataRetirementTestModeEnabled(this.bean.isDataRetirementTestModeEnabled());
            }

            if ((var3 == null || !var3.contains("DiagnosticContextEnabled")) && this.bean.isDiagnosticContextEnabledSet()) {
               var5.setDiagnosticContextEnabled(this.bean.isDiagnosticContextEnabled());
            }

            if ((var3 == null || !var3.contains("DiagnosticStoreFileLockingEnabled")) && this.bean.isDiagnosticStoreFileLockingEnabledSet()) {
               var5.setDiagnosticStoreFileLockingEnabled(this.bean.isDiagnosticStoreFileLockingEnabled());
            }

            if ((var3 == null || !var3.contains("SynchronousEventPersistenceEnabled")) && this.bean.isSynchronousEventPersistenceEnabledSet()) {
               var5.setSynchronousEventPersistenceEnabled(this.bean.isSynchronousEventPersistenceEnabled());
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
         this.inferSubTree(this.bean.getDiagnosticJDBCResource(), var1, var2);
         this.inferSubTree(this.bean.getWLDFDataRetirementByAges(), var1, var2);
         this.inferSubTree(this.bean.getWLDFDataRetirements(), var1, var2);
      }
   }
}
