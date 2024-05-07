package weblogic.management.configuration;

import com.bea.logging.DateFormatter;
import java.io.OutputStream;
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
import weblogic.management.mbeans.custom.LogFile;
import weblogic.utils.collections.CombinedIterator;

public class LogFileMBeanImpl extends ConfigurationMBeanImpl implements LogFileMBean, Serializable {
   private int _BufferSizeKB;
   private String _DateFormatPattern;
   private int _FileCount;
   private int _FileMinSize;
   private String _FileName;
   private int _FileTimeSpan;
   private long _FileTimeSpanFactor;
   private String _LogFileRotationDir;
   private String _Name;
   private boolean _NumberOfFilesLimited;
   private OutputStream _OutputStream;
   private boolean _RotateLogOnStartup;
   private String _RotationTime;
   private String _RotationType;
   private LogFile _customizer;
   private static SchemaHelper2 _schemaHelper;

   public LogFileMBeanImpl() {
      try {
         this._customizer = new LogFile(this);
      } catch (Exception var2) {
         if (var2 instanceof RuntimeException) {
            throw (RuntimeException)var2;
         }

         throw new UndeclaredThrowableException(var2);
      }

      this._initializeProperty(-1);
   }

   public LogFileMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);

      try {
         this._customizer = new LogFile(this);
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         }

         throw new UndeclaredThrowableException(var4);
      }

      this._initializeProperty(-1);
   }

   public String getDateFormatPattern() {
      if (!this._isSet(7)) {
         try {
            return DateFormatter.getDefaultDateFormatPattern();
         } catch (NullPointerException var2) {
         }
      }

      return this._DateFormatPattern;
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

   public boolean isDateFormatPatternSet() {
      return this._isSet(7);
   }

   public boolean isNameSet() {
      return this._isSet(2);
   }

   public void setDateFormatPattern(String var1) {
      var1 = var1 == null ? null : var1.trim();
      weblogic.descriptor.beangen.LegalChecks.checkNonEmptyString("DateFormatPattern", var1);
      weblogic.descriptor.beangen.LegalChecks.checkNonNull("DateFormatPattern", var1);
      DateFormatter.validateDateFormatPattern(var1);
      String var2 = this._DateFormatPattern;
      this._DateFormatPattern = var1;
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

   public String getFileName() {
      if (!this._isSet(8)) {
         try {
            return "logs/" + this.getName() + ".log";
         } catch (NullPointerException var2) {
         }
      }

      return this._FileName;
   }

   public boolean isFileNameSet() {
      return this._isSet(8);
   }

   public void setFileName(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._FileName;
      this._FileName = var1;
      this._postSet(8, var2, var1);
   }

   public String getRotationType() {
      return this._RotationType;
   }

   public boolean isRotationTypeSet() {
      return this._isSet(9);
   }

   public void setRotationType(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{"bySize", "byTime", "none"};
      var1 = weblogic.descriptor.beangen.LegalChecks.checkInEnum("RotationType", var1, var2);
      String var3 = this._RotationType;
      this._RotationType = var1;
      this._postSet(9, var3, var1);
   }

   public void setNumberOfFilesLimited(boolean var1) throws InvalidAttributeValueException, DistributedManagementException {
      boolean var2 = this._NumberOfFilesLimited;
      this._NumberOfFilesLimited = var1;
      this._postSet(10, var2, var1);
   }

   public boolean isNumberOfFilesLimited() {
      if (!this._isSet(10)) {
         return !this._isProductionModeEnabled();
      } else {
         return this._NumberOfFilesLimited;
      }
   }

   public boolean isNumberOfFilesLimitedSet() {
      return this._isSet(10);
   }

   public int getFileCount() {
      return this._FileCount;
   }

   public boolean isFileCountSet() {
      return this._isSet(11);
   }

   public void setFileCount(int var1) throws InvalidAttributeValueException, DistributedManagementException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("FileCount", (long)var1, 1L, 99999L);
      int var2 = this._FileCount;
      this._FileCount = var1;
      this._postSet(11, var2, var1);
   }

   public int getFileTimeSpan() {
      return this._FileTimeSpan;
   }

   public boolean isFileTimeSpanSet() {
      return this._isSet(12);
   }

   public void touch() throws ConfigurationException {
      this._customizer.touch();
   }

   public void freezeCurrentValue(String var1) throws AttributeNotFoundException, MBeanException {
      this._customizer.freezeCurrentValue(var1);
   }

   public String getRotationTime() {
      return this._RotationTime;
   }

   public boolean isRotationTimeSet() {
      return this._isSet(13);
   }

   public void restoreDefaultValue(String var1) throws AttributeNotFoundException {
      this._customizer.restoreDefaultValue(var1);
   }

   public void setRotationTime(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      LoggingLegalHelper.validateLogTimeString(var1);
      String var2 = this._RotationTime;
      this._RotationTime = var1;
      this._postSet(13, var2, var1);
   }

   public void setFileTimeSpan(int var1) throws InvalidAttributeValueException, DistributedManagementException {
      weblogic.descriptor.beangen.LegalChecks.checkMin("FileTimeSpan", var1, 1);
      int var2 = this._FileTimeSpan;
      this._FileTimeSpan = var1;
      this._postSet(12, var2, var1);
   }

   public long getFileTimeSpanFactor() {
      return this._FileTimeSpanFactor;
   }

   public boolean isFileTimeSpanFactorSet() {
      return this._isSet(14);
   }

   public void setFileTimeSpanFactor(long var1) {
      long var3 = this._FileTimeSpanFactor;
      this._FileTimeSpanFactor = var1;
      this._postSet(14, var3, var1);
   }

   public int getFileMinSize() {
      if (!this._isSet(15)) {
         return this._isProductionModeEnabled() ? 5000 : 500;
      } else {
         return this._FileMinSize;
      }
   }

   public boolean isFileMinSizeSet() {
      return this._isSet(15);
   }

   public void setFileMinSize(int var1) throws InvalidAttributeValueException, DistributedManagementException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("FileMinSize", (long)var1, 1L, 65535L);
      int var2 = this._FileMinSize;
      this._FileMinSize = var1;
      this._postSet(15, var2, var1);
   }

   public boolean getRotateLogOnStartup() {
      if (!this._isSet(16)) {
         return !this._isProductionModeEnabled();
      } else {
         return this._RotateLogOnStartup;
      }
   }

   public boolean isRotateLogOnStartupSet() {
      return this._isSet(16);
   }

   public void setRotateLogOnStartup(boolean var1) {
      boolean var2 = this._RotateLogOnStartup;
      this._RotateLogOnStartup = var1;
      this._postSet(16, var2, var1);
   }

   public String getLogFileRotationDir() {
      return this._LogFileRotationDir;
   }

   public boolean isLogFileRotationDirSet() {
      return this._isSet(17);
   }

   public void setLogFileRotationDir(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._LogFileRotationDir;
      this._LogFileRotationDir = var1;
      this._postSet(17, var2, var1);
   }

   public String computeLogFilePath() {
      return this._customizer.computeLogFilePath();
   }

   public OutputStream getOutputStream() {
      return this._OutputStream;
   }

   public boolean isOutputStreamSet() {
      return this._isSet(18);
   }

   public void setOutputStream(OutputStream var1) {
      this._OutputStream = var1;
   }

   public int getBufferSizeKB() {
      return this._BufferSizeKB;
   }

   public boolean isBufferSizeKBSet() {
      return this._isSet(19);
   }

   public void setBufferSizeKB(int var1) {
      int var2 = this._BufferSizeKB;
      this._BufferSizeKB = var1;
      this._postSet(19, var2, var1);
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
         var1 = 19;
      }

      try {
         switch (var1) {
            case 19:
               this._BufferSizeKB = 8;
               if (var2) {
                  break;
               }
            case 7:
               this._DateFormatPattern = null;
               if (var2) {
                  break;
               }
            case 11:
               this._FileCount = 7;
               if (var2) {
                  break;
               }
            case 15:
               this._FileMinSize = 500;
               if (var2) {
                  break;
               }
            case 8:
               this._FileName = null;
               if (var2) {
                  break;
               }
            case 12:
               this._FileTimeSpan = 24;
               if (var2) {
                  break;
               }
            case 14:
               this._FileTimeSpanFactor = 3600000L;
               if (var2) {
                  break;
               }
            case 17:
               this._LogFileRotationDir = null;
               if (var2) {
                  break;
               }
            case 2:
               this._customizer.setName((String)null);
               if (var2) {
                  break;
               }
            case 18:
               this._OutputStream = null;
               if (var2) {
                  break;
               }
            case 16:
               this._RotateLogOnStartup = true;
               if (var2) {
                  break;
               }
            case 13:
               this._RotationTime = "00:00";
               if (var2) {
                  break;
               }
            case 9:
               this._RotationType = "bySize";
               if (var2) {
                  break;
               }
            case 10:
               this._NumberOfFilesLimited = true;
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
      return "LogFile";
   }

   public void putValue(String var1, Object var2) {
      int var9;
      if (var1.equals("BufferSizeKB")) {
         var9 = this._BufferSizeKB;
         this._BufferSizeKB = (Integer)var2;
         this._postSet(19, var9, this._BufferSizeKB);
      } else {
         String var5;
         if (var1.equals("DateFormatPattern")) {
            var5 = this._DateFormatPattern;
            this._DateFormatPattern = (String)var2;
            this._postSet(7, var5, this._DateFormatPattern);
         } else if (var1.equals("FileCount")) {
            var9 = this._FileCount;
            this._FileCount = (Integer)var2;
            this._postSet(11, var9, this._FileCount);
         } else if (var1.equals("FileMinSize")) {
            var9 = this._FileMinSize;
            this._FileMinSize = (Integer)var2;
            this._postSet(15, var9, this._FileMinSize);
         } else if (var1.equals("FileName")) {
            var5 = this._FileName;
            this._FileName = (String)var2;
            this._postSet(8, var5, this._FileName);
         } else if (var1.equals("FileTimeSpan")) {
            var9 = this._FileTimeSpan;
            this._FileTimeSpan = (Integer)var2;
            this._postSet(12, var9, this._FileTimeSpan);
         } else if (var1.equals("FileTimeSpanFactor")) {
            long var8 = this._FileTimeSpanFactor;
            this._FileTimeSpanFactor = (Long)var2;
            this._postSet(14, var8, this._FileTimeSpanFactor);
         } else if (var1.equals("LogFileRotationDir")) {
            var5 = this._LogFileRotationDir;
            this._LogFileRotationDir = (String)var2;
            this._postSet(17, var5, this._LogFileRotationDir);
         } else if (var1.equals("Name")) {
            var5 = this._Name;
            this._Name = (String)var2;
            this._postSet(2, var5, this._Name);
         } else {
            boolean var6;
            if (var1.equals("NumberOfFilesLimited")) {
               var6 = this._NumberOfFilesLimited;
               this._NumberOfFilesLimited = (Boolean)var2;
               this._postSet(10, var6, this._NumberOfFilesLimited);
            } else if (var1.equals("OutputStream")) {
               OutputStream var7 = this._OutputStream;
               this._OutputStream = (OutputStream)var2;
               this._postSet(18, var7, this._OutputStream);
            } else if (var1.equals("RotateLogOnStartup")) {
               var6 = this._RotateLogOnStartup;
               this._RotateLogOnStartup = (Boolean)var2;
               this._postSet(16, var6, this._RotateLogOnStartup);
            } else if (var1.equals("RotationTime")) {
               var5 = this._RotationTime;
               this._RotationTime = (String)var2;
               this._postSet(13, var5, this._RotationTime);
            } else if (var1.equals("RotationType")) {
               var5 = this._RotationType;
               this._RotationType = (String)var2;
               this._postSet(9, var5, this._RotationType);
            } else if (var1.equals("customizer")) {
               LogFile var3 = this._customizer;
               this._customizer = (LogFile)var2;
            } else {
               super.putValue(var1, var2);
            }
         }
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("BufferSizeKB")) {
         return new Integer(this._BufferSizeKB);
      } else if (var1.equals("DateFormatPattern")) {
         return this._DateFormatPattern;
      } else if (var1.equals("FileCount")) {
         return new Integer(this._FileCount);
      } else if (var1.equals("FileMinSize")) {
         return new Integer(this._FileMinSize);
      } else if (var1.equals("FileName")) {
         return this._FileName;
      } else if (var1.equals("FileTimeSpan")) {
         return new Integer(this._FileTimeSpan);
      } else if (var1.equals("FileTimeSpanFactor")) {
         return new Long(this._FileTimeSpanFactor);
      } else if (var1.equals("LogFileRotationDir")) {
         return this._LogFileRotationDir;
      } else if (var1.equals("Name")) {
         return this._Name;
      } else if (var1.equals("NumberOfFilesLimited")) {
         return new Boolean(this._NumberOfFilesLimited);
      } else if (var1.equals("OutputStream")) {
         return this._OutputStream;
      } else if (var1.equals("RotateLogOnStartup")) {
         return new Boolean(this._RotateLogOnStartup);
      } else if (var1.equals("RotationTime")) {
         return this._RotationTime;
      } else if (var1.equals("RotationType")) {
         return this._RotationType;
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
            case 11:
            case 12:
            case 15:
            case 16:
            case 17:
            case 18:
            case 20:
            case 22:
            default:
               break;
            case 9:
               if (var1.equals("file-name")) {
                  return 8;
               }
               break;
            case 10:
               if (var1.equals("file-count")) {
                  return 11;
               }
               break;
            case 13:
               if (var1.equals("buffer-sizekb")) {
                  return 19;
               }

               if (var1.equals("file-min-size")) {
                  return 15;
               }

               if (var1.equals("output-stream")) {
                  return 18;
               }

               if (var1.equals("rotation-time")) {
                  return 13;
               }

               if (var1.equals("rotation-type")) {
                  return 9;
               }
               break;
            case 14:
               if (var1.equals("file-time-span")) {
                  return 12;
               }
               break;
            case 19:
               if (var1.equals("date-format-pattern")) {
                  return 7;
               }
               break;
            case 21:
               if (var1.equals("file-time-span-factor")) {
                  return 14;
               }

               if (var1.equals("log-file-rotation-dir")) {
                  return 17;
               }

               if (var1.equals("rotate-log-on-startup")) {
                  return 16;
               }
               break;
            case 23:
               if (var1.equals("number-of-files-limited")) {
                  return 10;
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
               return "date-format-pattern";
            case 8:
               return "file-name";
            case 9:
               return "rotation-type";
            case 10:
               return "number-of-files-limited";
            case 11:
               return "file-count";
            case 12:
               return "file-time-span";
            case 13:
               return "rotation-time";
            case 14:
               return "file-time-span-factor";
            case 15:
               return "file-min-size";
            case 16:
               return "rotate-log-on-startup";
            case 17:
               return "log-file-rotation-dir";
            case 18:
               return "output-stream";
            case 19:
               return "buffer-sizekb";
         }
      }

      public boolean isConfigurable(int var1) {
         switch (var1) {
            case 8:
               return true;
            case 13:
               return true;
            case 15:
               return true;
            default:
               return super.isConfigurable(var1);
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
      private LogFileMBeanImpl bean;

      protected Helper(LogFileMBeanImpl var1) {
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
               return "DateFormatPattern";
            case 8:
               return "FileName";
            case 9:
               return "RotationType";
            case 10:
               return "NumberOfFilesLimited";
            case 11:
               return "FileCount";
            case 12:
               return "FileTimeSpan";
            case 13:
               return "RotationTime";
            case 14:
               return "FileTimeSpanFactor";
            case 15:
               return "FileMinSize";
            case 16:
               return "RotateLogOnStartup";
            case 17:
               return "LogFileRotationDir";
            case 18:
               return "OutputStream";
            case 19:
               return "BufferSizeKB";
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("BufferSizeKB")) {
            return 19;
         } else if (var1.equals("DateFormatPattern")) {
            return 7;
         } else if (var1.equals("FileCount")) {
            return 11;
         } else if (var1.equals("FileMinSize")) {
            return 15;
         } else if (var1.equals("FileName")) {
            return 8;
         } else if (var1.equals("FileTimeSpan")) {
            return 12;
         } else if (var1.equals("FileTimeSpanFactor")) {
            return 14;
         } else if (var1.equals("LogFileRotationDir")) {
            return 17;
         } else if (var1.equals("Name")) {
            return 2;
         } else if (var1.equals("OutputStream")) {
            return 18;
         } else if (var1.equals("RotateLogOnStartup")) {
            return 16;
         } else if (var1.equals("RotationTime")) {
            return 13;
         } else if (var1.equals("RotationType")) {
            return 9;
         } else {
            return var1.equals("NumberOfFilesLimited") ? 10 : super.getPropertyIndex(var1);
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
            if (this.bean.isBufferSizeKBSet()) {
               var2.append("BufferSizeKB");
               var2.append(String.valueOf(this.bean.getBufferSizeKB()));
            }

            if (this.bean.isDateFormatPatternSet()) {
               var2.append("DateFormatPattern");
               var2.append(String.valueOf(this.bean.getDateFormatPattern()));
            }

            if (this.bean.isFileCountSet()) {
               var2.append("FileCount");
               var2.append(String.valueOf(this.bean.getFileCount()));
            }

            if (this.bean.isFileMinSizeSet()) {
               var2.append("FileMinSize");
               var2.append(String.valueOf(this.bean.getFileMinSize()));
            }

            if (this.bean.isFileNameSet()) {
               var2.append("FileName");
               var2.append(String.valueOf(this.bean.getFileName()));
            }

            if (this.bean.isFileTimeSpanSet()) {
               var2.append("FileTimeSpan");
               var2.append(String.valueOf(this.bean.getFileTimeSpan()));
            }

            if (this.bean.isFileTimeSpanFactorSet()) {
               var2.append("FileTimeSpanFactor");
               var2.append(String.valueOf(this.bean.getFileTimeSpanFactor()));
            }

            if (this.bean.isLogFileRotationDirSet()) {
               var2.append("LogFileRotationDir");
               var2.append(String.valueOf(this.bean.getLogFileRotationDir()));
            }

            if (this.bean.isNameSet()) {
               var2.append("Name");
               var2.append(String.valueOf(this.bean.getName()));
            }

            if (this.bean.isOutputStreamSet()) {
               var2.append("OutputStream");
               var2.append(String.valueOf(this.bean.getOutputStream()));
            }

            if (this.bean.isRotateLogOnStartupSet()) {
               var2.append("RotateLogOnStartup");
               var2.append(String.valueOf(this.bean.getRotateLogOnStartup()));
            }

            if (this.bean.isRotationTimeSet()) {
               var2.append("RotationTime");
               var2.append(String.valueOf(this.bean.getRotationTime()));
            }

            if (this.bean.isRotationTypeSet()) {
               var2.append("RotationType");
               var2.append(String.valueOf(this.bean.getRotationType()));
            }

            if (this.bean.isNumberOfFilesLimitedSet()) {
               var2.append("NumberOfFilesLimited");
               var2.append(String.valueOf(this.bean.isNumberOfFilesLimited()));
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
            LogFileMBeanImpl var2 = (LogFileMBeanImpl)var1;
            this.computeDiff("BufferSizeKB", this.bean.getBufferSizeKB(), var2.getBufferSizeKB(), false);
            this.computeDiff("DateFormatPattern", this.bean.getDateFormatPattern(), var2.getDateFormatPattern(), false);
            this.computeDiff("FileCount", this.bean.getFileCount(), var2.getFileCount(), true);
            this.computeDiff("FileMinSize", this.bean.getFileMinSize(), var2.getFileMinSize(), true);
            this.computeDiff("FileName", this.bean.getFileName(), var2.getFileName(), false);
            this.computeDiff("FileTimeSpan", this.bean.getFileTimeSpan(), var2.getFileTimeSpan(), true);
            this.computeDiff("FileTimeSpanFactor", this.bean.getFileTimeSpanFactor(), var2.getFileTimeSpanFactor(), false);
            this.computeDiff("LogFileRotationDir", this.bean.getLogFileRotationDir(), var2.getLogFileRotationDir(), false);
            this.computeDiff("Name", this.bean.getName(), var2.getName(), false);
            this.computeDiff("RotateLogOnStartup", this.bean.getRotateLogOnStartup(), var2.getRotateLogOnStartup(), false);
            this.computeDiff("RotationTime", this.bean.getRotationTime(), var2.getRotationTime(), true);
            this.computeDiff("RotationType", this.bean.getRotationType(), var2.getRotationType(), false);
            this.computeDiff("NumberOfFilesLimited", this.bean.isNumberOfFilesLimited(), var2.isNumberOfFilesLimited(), true);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            LogFileMBeanImpl var3 = (LogFileMBeanImpl)var1.getSourceBean();
            LogFileMBeanImpl var4 = (LogFileMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("BufferSizeKB")) {
                  var3.setBufferSizeKB(var4.getBufferSizeKB());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 19);
               } else if (var5.equals("DateFormatPattern")) {
                  var3.setDateFormatPattern(var4.getDateFormatPattern());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 7);
               } else if (var5.equals("FileCount")) {
                  var3.setFileCount(var4.getFileCount());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 11);
               } else if (var5.equals("FileMinSize")) {
                  var3.setFileMinSize(var4.getFileMinSize());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 15);
               } else if (var5.equals("FileName")) {
                  var3.setFileName(var4.getFileName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 8);
               } else if (var5.equals("FileTimeSpan")) {
                  var3.setFileTimeSpan(var4.getFileTimeSpan());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 12);
               } else if (var5.equals("FileTimeSpanFactor")) {
                  var3.setFileTimeSpanFactor(var4.getFileTimeSpanFactor());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 14);
               } else if (var5.equals("LogFileRotationDir")) {
                  var3.setLogFileRotationDir(var4.getLogFileRotationDir());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 17);
               } else if (var5.equals("Name")) {
                  var3.setName(var4.getName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 2);
               } else if (!var5.equals("OutputStream")) {
                  if (var5.equals("RotateLogOnStartup")) {
                     var3.setRotateLogOnStartup(var4.getRotateLogOnStartup());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 16);
                  } else if (var5.equals("RotationTime")) {
                     var3.setRotationTime(var4.getRotationTime());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 13);
                  } else if (var5.equals("RotationType")) {
                     var3.setRotationType(var4.getRotationType());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 9);
                  } else if (var5.equals("NumberOfFilesLimited")) {
                     var3.setNumberOfFilesLimited(var4.isNumberOfFilesLimited());
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
            LogFileMBeanImpl var5 = (LogFileMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("BufferSizeKB")) && this.bean.isBufferSizeKBSet()) {
               var5.setBufferSizeKB(this.bean.getBufferSizeKB());
            }

            if ((var3 == null || !var3.contains("DateFormatPattern")) && this.bean.isDateFormatPatternSet()) {
               var5.setDateFormatPattern(this.bean.getDateFormatPattern());
            }

            if ((var3 == null || !var3.contains("FileCount")) && this.bean.isFileCountSet()) {
               var5.setFileCount(this.bean.getFileCount());
            }

            if ((var3 == null || !var3.contains("FileMinSize")) && this.bean.isFileMinSizeSet()) {
               var5.setFileMinSize(this.bean.getFileMinSize());
            }

            if ((var3 == null || !var3.contains("FileName")) && this.bean.isFileNameSet()) {
               var5.setFileName(this.bean.getFileName());
            }

            if ((var3 == null || !var3.contains("FileTimeSpan")) && this.bean.isFileTimeSpanSet()) {
               var5.setFileTimeSpan(this.bean.getFileTimeSpan());
            }

            if ((var3 == null || !var3.contains("FileTimeSpanFactor")) && this.bean.isFileTimeSpanFactorSet()) {
               var5.setFileTimeSpanFactor(this.bean.getFileTimeSpanFactor());
            }

            if ((var3 == null || !var3.contains("LogFileRotationDir")) && this.bean.isLogFileRotationDirSet()) {
               var5.setLogFileRotationDir(this.bean.getLogFileRotationDir());
            }

            if ((var3 == null || !var3.contains("Name")) && this.bean.isNameSet()) {
               var5.setName(this.bean.getName());
            }

            if ((var3 == null || !var3.contains("RotateLogOnStartup")) && this.bean.isRotateLogOnStartupSet()) {
               var5.setRotateLogOnStartup(this.bean.getRotateLogOnStartup());
            }

            if ((var3 == null || !var3.contains("RotationTime")) && this.bean.isRotationTimeSet()) {
               var5.setRotationTime(this.bean.getRotationTime());
            }

            if ((var3 == null || !var3.contains("RotationType")) && this.bean.isRotationTypeSet()) {
               var5.setRotationType(this.bean.getRotationType());
            }

            if ((var3 == null || !var3.contains("NumberOfFilesLimited")) && this.bean.isNumberOfFilesLimitedSet()) {
               var5.setNumberOfFilesLimited(this.bean.isNumberOfFilesLimited());
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
