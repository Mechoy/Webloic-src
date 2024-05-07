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
import weblogic.descriptor.internal.ReferenceManager;
import weblogic.descriptor.internal.ResolvedReference;
import weblogic.descriptor.internal.SchemaHelper;
import weblogic.management.ManagementException;
import weblogic.management.mbeans.custom.Log;
import weblogic.utils.collections.CombinedIterator;

public class LogMBeanImpl extends CommonLogMBeanImpl implements LogMBean, Serializable {
   private LogFilterMBean _DomainLogBroadcastFilter;
   private String _DomainLogBroadcastSeverity;
   private int _DomainLogBroadcasterBufferSize;
   private boolean _Log4jLoggingEnabled;
   private LogFilterMBean _LogFileFilter;
   private LogFilterMBean _MemoryBufferFilter;
   private String _MemoryBufferSeverity;
   private int _MemoryBufferSize;
   private String _Name;
   private boolean _RedirectStderrToServerLogEnabled;
   private boolean _RedirectStdoutToServerLogEnabled;
   private boolean _ServerLoggingBridgeUseParentLoggersEnabled;
   private LogFilterMBean _StdoutFilter;
   private Log _customizer;
   private static SchemaHelper2 _schemaHelper;

   public LogMBeanImpl() {
      try {
         this._customizer = new Log(this);
      } catch (Exception var2) {
         if (var2 instanceof RuntimeException) {
            throw (RuntimeException)var2;
         }

         throw new UndeclaredThrowableException(var2);
      }

      this._initializeProperty(-1);
   }

   public LogMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);

      try {
         this._customizer = new Log(this);
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         }

         throw new UndeclaredThrowableException(var4);
      }

      this._initializeProperty(-1);
   }

   public LogFilterMBean getLogFileFilter() {
      return this._LogFileFilter;
   }

   public String getLogFileFilterAsString() {
      AbstractDescriptorBean var1 = (AbstractDescriptorBean)this.getLogFileFilter();
      return var1 == null ? null : var1._getKey().toString();
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

   public boolean isLogFileFilterSet() {
      return this._isSet(27);
   }

   public boolean isNameSet() {
      return this._isSet(2);
   }

   public void setLogFileFilterAsString(String var1) {
      if (var1 != null && var1.length() != 0) {
         var1 = var1 == null ? null : var1.trim();
         this._getReferenceManager().registerUnresolvedReference(var1, LogFilterMBean.class, new ReferenceManager.Resolver(this, 27) {
            public void resolveReference(Object var1) {
               try {
                  LogMBeanImpl.this.setLogFileFilter((LogFilterMBean)var1);
               } catch (RuntimeException var3) {
                  throw var3;
               } catch (Exception var4) {
                  throw new AssertionError("Impossible exception: " + var4);
               }
            }
         });
      } else {
         LogFilterMBean var2 = this._LogFileFilter;
         this._initializeProperty(27);
         this._postSet(27, var2, this._LogFileFilter);
      }

   }

   public void setLogFileFilter(LogFilterMBean var1) {
      if (var1 != null) {
         ResolvedReference var2 = new ResolvedReference(this, 27, (AbstractDescriptorBean)var1) {
            protected Object getPropertyValue() {
               return LogMBeanImpl.this.getLogFileFilter();
            }
         };
         this._getReferenceManager().registerResolvedReference((AbstractDescriptorBean)var1, var2);
      }

      LogFilterMBean var3 = this._LogFileFilter;
      this._LogFileFilter = var1;
      this._postSet(27, var3, var1);
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

   public LogFilterMBean getStdoutFilter() {
      return this._StdoutFilter;
   }

   public String getStdoutFilterAsString() {
      AbstractDescriptorBean var1 = (AbstractDescriptorBean)this.getStdoutFilter();
      return var1 == null ? null : var1._getKey().toString();
   }

   public boolean isStdoutFilterSet() {
      return this._isSet(28);
   }

   public void setStdoutFilterAsString(String var1) {
      if (var1 != null && var1.length() != 0) {
         var1 = var1 == null ? null : var1.trim();
         this._getReferenceManager().registerUnresolvedReference(var1, LogFilterMBean.class, new ReferenceManager.Resolver(this, 28) {
            public void resolveReference(Object var1) {
               try {
                  LogMBeanImpl.this.setStdoutFilter((LogFilterMBean)var1);
               } catch (RuntimeException var3) {
                  throw var3;
               } catch (Exception var4) {
                  throw new AssertionError("Impossible exception: " + var4);
               }
            }
         });
      } else {
         LogFilterMBean var2 = this._StdoutFilter;
         this._initializeProperty(28);
         this._postSet(28, var2, this._StdoutFilter);
      }

   }

   public void setStdoutFilter(LogFilterMBean var1) {
      if (var1 != null) {
         ResolvedReference var2 = new ResolvedReference(this, 28, (AbstractDescriptorBean)var1) {
            protected Object getPropertyValue() {
               return LogMBeanImpl.this.getStdoutFilter();
            }
         };
         this._getReferenceManager().registerResolvedReference((AbstractDescriptorBean)var1, var2);
      }

      LogFilterMBean var3 = this._StdoutFilter;
      this._StdoutFilter = var1;
      this._postSet(28, var3, var1);
   }

   public String getDomainLogBroadcastSeverity() {
      return this._DomainLogBroadcastSeverity;
   }

   public boolean isDomainLogBroadcastSeveritySet() {
      return this._isSet(29);
   }

   public void setDomainLogBroadcastSeverity(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{"Debug", "Info", "Warning", "Error", "Notice", "Critical", "Alert", "Emergency", "Off"};
      var1 = weblogic.descriptor.beangen.LegalChecks.checkInEnum("DomainLogBroadcastSeverity", var1, var2);
      String var3 = this._DomainLogBroadcastSeverity;
      this._DomainLogBroadcastSeverity = var1;
      this._postSet(29, var3, var1);
   }

   public LogFilterMBean getDomainLogBroadcastFilter() {
      return this._DomainLogBroadcastFilter;
   }

   public String getDomainLogBroadcastFilterAsString() {
      AbstractDescriptorBean var1 = (AbstractDescriptorBean)this.getDomainLogBroadcastFilter();
      return var1 == null ? null : var1._getKey().toString();
   }

   public boolean isDomainLogBroadcastFilterSet() {
      return this._isSet(30);
   }

   public void setDomainLogBroadcastFilterAsString(String var1) {
      if (var1 != null && var1.length() != 0) {
         var1 = var1 == null ? null : var1.trim();
         this._getReferenceManager().registerUnresolvedReference(var1, LogFilterMBean.class, new ReferenceManager.Resolver(this, 30) {
            public void resolveReference(Object var1) {
               try {
                  LogMBeanImpl.this.setDomainLogBroadcastFilter((LogFilterMBean)var1);
               } catch (RuntimeException var3) {
                  throw var3;
               } catch (Exception var4) {
                  throw new AssertionError("Impossible exception: " + var4);
               }
            }
         });
      } else {
         LogFilterMBean var2 = this._DomainLogBroadcastFilter;
         this._initializeProperty(30);
         this._postSet(30, var2, this._DomainLogBroadcastFilter);
      }

   }

   public void setDomainLogBroadcastFilter(LogFilterMBean var1) {
      if (var1 != null) {
         ResolvedReference var2 = new ResolvedReference(this, 30, (AbstractDescriptorBean)var1) {
            protected Object getPropertyValue() {
               return LogMBeanImpl.this.getDomainLogBroadcastFilter();
            }
         };
         this._getReferenceManager().registerResolvedReference((AbstractDescriptorBean)var1, var2);
      }

      LogFilterMBean var3 = this._DomainLogBroadcastFilter;
      this._DomainLogBroadcastFilter = var1;
      this._postSet(30, var3, var1);
   }

   public String getMemoryBufferSeverity() {
      return this._MemoryBufferSeverity;
   }

   public boolean isMemoryBufferSeveritySet() {
      return this._isSet(31);
   }

   public void setMemoryBufferSeverity(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{"Trace", "Debug", "Info", "Warning", "Error", "Notice", "Critical", "Alert", "Emergency", "Off"};
      var1 = weblogic.descriptor.beangen.LegalChecks.checkInEnum("MemoryBufferSeverity", var1, var2);
      String var3 = this._MemoryBufferSeverity;
      this._MemoryBufferSeverity = var1;
      this._postSet(31, var3, var1);
   }

   public LogFilterMBean getMemoryBufferFilter() {
      return this._MemoryBufferFilter;
   }

   public String getMemoryBufferFilterAsString() {
      AbstractDescriptorBean var1 = (AbstractDescriptorBean)this.getMemoryBufferFilter();
      return var1 == null ? null : var1._getKey().toString();
   }

   public boolean isMemoryBufferFilterSet() {
      return this._isSet(32);
   }

   public void setMemoryBufferFilterAsString(String var1) {
      if (var1 != null && var1.length() != 0) {
         var1 = var1 == null ? null : var1.trim();
         this._getReferenceManager().registerUnresolvedReference(var1, LogFilterMBean.class, new ReferenceManager.Resolver(this, 32) {
            public void resolveReference(Object var1) {
               try {
                  LogMBeanImpl.this.setMemoryBufferFilter((LogFilterMBean)var1);
               } catch (RuntimeException var3) {
                  throw var3;
               } catch (Exception var4) {
                  throw new AssertionError("Impossible exception: " + var4);
               }
            }
         });
      } else {
         LogFilterMBean var2 = this._MemoryBufferFilter;
         this._initializeProperty(32);
         this._postSet(32, var2, this._MemoryBufferFilter);
      }

   }

   public void touch() throws ConfigurationException {
      this._customizer.touch();
   }

   public void freezeCurrentValue(String var1) throws AttributeNotFoundException, MBeanException {
      this._customizer.freezeCurrentValue(var1);
   }

   public void setMemoryBufferFilter(LogFilterMBean var1) {
      if (var1 != null) {
         ResolvedReference var2 = new ResolvedReference(this, 32, (AbstractDescriptorBean)var1) {
            protected Object getPropertyValue() {
               return LogMBeanImpl.this.getMemoryBufferFilter();
            }
         };
         this._getReferenceManager().registerResolvedReference((AbstractDescriptorBean)var1, var2);
      }

      LogFilterMBean var3 = this._MemoryBufferFilter;
      this._MemoryBufferFilter = var1;
      this._postSet(32, var3, var1);
   }

   public int getMemoryBufferSize() {
      if (!this._isSet(33)) {
         return this._isProductionModeEnabled() ? 500 : 10;
      } else {
         return this._MemoryBufferSize;
      }
   }

   public boolean isMemoryBufferSizeSet() {
      return this._isSet(33);
   }

   public void restoreDefaultValue(String var1) throws AttributeNotFoundException {
      this._customizer.restoreDefaultValue(var1);
   }

   public void setMemoryBufferSize(int var1) {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("MemoryBufferSize", (long)var1, 10L, 5000L);
      int var2 = this._MemoryBufferSize;
      this._MemoryBufferSize = var1;
      this._postSet(33, var2, var1);
   }

   public boolean isLog4jLoggingEnabled() {
      return this._Log4jLoggingEnabled;
   }

   public boolean isLog4jLoggingEnabledSet() {
      return this._isSet(34);
   }

   public void setLog4jLoggingEnabled(boolean var1) {
      boolean var2 = this._Log4jLoggingEnabled;
      this._Log4jLoggingEnabled = var1;
      this._postSet(34, var2, var1);
   }

   public boolean isRedirectStdoutToServerLogEnabled() {
      return this._RedirectStdoutToServerLogEnabled;
   }

   public boolean isRedirectStdoutToServerLogEnabledSet() {
      return this._isSet(35);
   }

   public void setRedirectStdoutToServerLogEnabled(boolean var1) {
      boolean var2 = this._RedirectStdoutToServerLogEnabled;
      this._RedirectStdoutToServerLogEnabled = var1;
      this._postSet(35, var2, var1);
   }

   public boolean isRedirectStderrToServerLogEnabled() {
      return this._RedirectStderrToServerLogEnabled;
   }

   public boolean isRedirectStderrToServerLogEnabledSet() {
      return this._isSet(36);
   }

   public void setRedirectStderrToServerLogEnabled(boolean var1) {
      boolean var2 = this._RedirectStderrToServerLogEnabled;
      this._RedirectStderrToServerLogEnabled = var1;
      this._postSet(36, var2, var1);
   }

   public int getDomainLogBroadcasterBufferSize() {
      if (!this._isSet(37)) {
         return this._isProductionModeEnabled() ? 10 : 1;
      } else {
         return this._DomainLogBroadcasterBufferSize;
      }
   }

   public boolean isDomainLogBroadcasterBufferSizeSet() {
      return this._isSet(37);
   }

   public void setDomainLogBroadcasterBufferSize(int var1) {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("DomainLogBroadcasterBufferSize", (long)var1, 1L, 100L);
      int var2 = this._DomainLogBroadcasterBufferSize;
      this._DomainLogBroadcasterBufferSize = var1;
      this._postSet(37, var2, var1);
   }

   public String computeLogFilePath() {
      return this._customizer.computeLogFilePath();
   }

   public boolean isServerLoggingBridgeUseParentLoggersEnabled() {
      return this._ServerLoggingBridgeUseParentLoggersEnabled;
   }

   public boolean isServerLoggingBridgeUseParentLoggersEnabledSet() {
      return this._isSet(38);
   }

   public void setServerLoggingBridgeUseParentLoggersEnabled(boolean var1) {
      boolean var2 = this._ServerLoggingBridgeUseParentLoggersEnabled;
      this._ServerLoggingBridgeUseParentLoggersEnabled = var1;
      this._postSet(38, var2, var1);
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
         var1 = 30;
      }

      try {
         switch (var1) {
            case 30:
               this._DomainLogBroadcastFilter = null;
               if (var2) {
                  break;
               }
            case 29:
               this._DomainLogBroadcastSeverity = "Notice";
               if (var2) {
                  break;
               }
            case 37:
               this._DomainLogBroadcasterBufferSize = 1;
               if (var2) {
                  break;
               }
            case 27:
               this._LogFileFilter = null;
               if (var2) {
                  break;
               }
            case 32:
               this._MemoryBufferFilter = null;
               if (var2) {
                  break;
               }
            case 31:
               this._MemoryBufferSeverity = "Trace";
               if (var2) {
                  break;
               }
            case 33:
               this._MemoryBufferSize = 10;
               if (var2) {
                  break;
               }
            case 2:
               this._customizer.setName((String)null);
               if (var2) {
                  break;
               }
            case 28:
               this._StdoutFilter = null;
               if (var2) {
                  break;
               }
            case 34:
               this._Log4jLoggingEnabled = false;
               if (var2) {
                  break;
               }
            case 36:
               this._RedirectStderrToServerLogEnabled = false;
               if (var2) {
                  break;
               }
            case 35:
               this._RedirectStdoutToServerLogEnabled = false;
               if (var2) {
                  break;
               }
            case 38:
               this._ServerLoggingBridgeUseParentLoggersEnabled = false;
               if (var2) {
                  break;
               }
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
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
      return "Log";
   }

   public void putValue(String var1, Object var2) {
      LogFilterMBean var4;
      if (var1.equals("DomainLogBroadcastFilter")) {
         var4 = this._DomainLogBroadcastFilter;
         this._DomainLogBroadcastFilter = (LogFilterMBean)var2;
         this._postSet(30, var4, this._DomainLogBroadcastFilter);
      } else {
         String var6;
         if (var1.equals("DomainLogBroadcastSeverity")) {
            var6 = this._DomainLogBroadcastSeverity;
            this._DomainLogBroadcastSeverity = (String)var2;
            this._postSet(29, var6, this._DomainLogBroadcastSeverity);
         } else {
            int var7;
            if (var1.equals("DomainLogBroadcasterBufferSize")) {
               var7 = this._DomainLogBroadcasterBufferSize;
               this._DomainLogBroadcasterBufferSize = (Integer)var2;
               this._postSet(37, var7, this._DomainLogBroadcasterBufferSize);
            } else {
               boolean var5;
               if (var1.equals("Log4jLoggingEnabled")) {
                  var5 = this._Log4jLoggingEnabled;
                  this._Log4jLoggingEnabled = (Boolean)var2;
                  this._postSet(34, var5, this._Log4jLoggingEnabled);
               } else if (var1.equals("LogFileFilter")) {
                  var4 = this._LogFileFilter;
                  this._LogFileFilter = (LogFilterMBean)var2;
                  this._postSet(27, var4, this._LogFileFilter);
               } else if (var1.equals("MemoryBufferFilter")) {
                  var4 = this._MemoryBufferFilter;
                  this._MemoryBufferFilter = (LogFilterMBean)var2;
                  this._postSet(32, var4, this._MemoryBufferFilter);
               } else if (var1.equals("MemoryBufferSeverity")) {
                  var6 = this._MemoryBufferSeverity;
                  this._MemoryBufferSeverity = (String)var2;
                  this._postSet(31, var6, this._MemoryBufferSeverity);
               } else if (var1.equals("MemoryBufferSize")) {
                  var7 = this._MemoryBufferSize;
                  this._MemoryBufferSize = (Integer)var2;
                  this._postSet(33, var7, this._MemoryBufferSize);
               } else if (var1.equals("Name")) {
                  var6 = this._Name;
                  this._Name = (String)var2;
                  this._postSet(2, var6, this._Name);
               } else if (var1.equals("RedirectStderrToServerLogEnabled")) {
                  var5 = this._RedirectStderrToServerLogEnabled;
                  this._RedirectStderrToServerLogEnabled = (Boolean)var2;
                  this._postSet(36, var5, this._RedirectStderrToServerLogEnabled);
               } else if (var1.equals("RedirectStdoutToServerLogEnabled")) {
                  var5 = this._RedirectStdoutToServerLogEnabled;
                  this._RedirectStdoutToServerLogEnabled = (Boolean)var2;
                  this._postSet(35, var5, this._RedirectStdoutToServerLogEnabled);
               } else if (var1.equals("ServerLoggingBridgeUseParentLoggersEnabled")) {
                  var5 = this._ServerLoggingBridgeUseParentLoggersEnabled;
                  this._ServerLoggingBridgeUseParentLoggersEnabled = (Boolean)var2;
                  this._postSet(38, var5, this._ServerLoggingBridgeUseParentLoggersEnabled);
               } else if (var1.equals("StdoutFilter")) {
                  var4 = this._StdoutFilter;
                  this._StdoutFilter = (LogFilterMBean)var2;
                  this._postSet(28, var4, this._StdoutFilter);
               } else if (var1.equals("customizer")) {
                  Log var3 = this._customizer;
                  this._customizer = (Log)var2;
               } else {
                  super.putValue(var1, var2);
               }
            }
         }
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("DomainLogBroadcastFilter")) {
         return this._DomainLogBroadcastFilter;
      } else if (var1.equals("DomainLogBroadcastSeverity")) {
         return this._DomainLogBroadcastSeverity;
      } else if (var1.equals("DomainLogBroadcasterBufferSize")) {
         return new Integer(this._DomainLogBroadcasterBufferSize);
      } else if (var1.equals("Log4jLoggingEnabled")) {
         return new Boolean(this._Log4jLoggingEnabled);
      } else if (var1.equals("LogFileFilter")) {
         return this._LogFileFilter;
      } else if (var1.equals("MemoryBufferFilter")) {
         return this._MemoryBufferFilter;
      } else if (var1.equals("MemoryBufferSeverity")) {
         return this._MemoryBufferSeverity;
      } else if (var1.equals("MemoryBufferSize")) {
         return new Integer(this._MemoryBufferSize);
      } else if (var1.equals("Name")) {
         return this._Name;
      } else if (var1.equals("RedirectStderrToServerLogEnabled")) {
         return new Boolean(this._RedirectStderrToServerLogEnabled);
      } else if (var1.equals("RedirectStdoutToServerLogEnabled")) {
         return new Boolean(this._RedirectStdoutToServerLogEnabled);
      } else if (var1.equals("ServerLoggingBridgeUseParentLoggersEnabled")) {
         return new Boolean(this._ServerLoggingBridgeUseParentLoggersEnabled);
      } else if (var1.equals("StdoutFilter")) {
         return this._StdoutFilter;
      } else {
         return var1.equals("customizer") ? this._customizer : super.getValue(var1);
      }
   }

   public static class SchemaHelper2 extends CommonLogMBeanImpl.SchemaHelper2 implements SchemaHelper {
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
            case 9:
            case 10:
            case 11:
            case 12:
            case 14:
            case 16:
            case 17:
            case 19:
            case 23:
            case 24:
            case 25:
            case 26:
            case 28:
            case 30:
            case 31:
            case 32:
            case 33:
            case 35:
            case 36:
            case 38:
            case 39:
            case 40:
            case 41:
            case 42:
            case 43:
            case 44:
            case 45:
            case 46:
            case 47:
            default:
               break;
            case 13:
               if (var1.equals("stdout-filter")) {
                  return 28;
               }
               break;
            case 15:
               if (var1.equals("log-file-filter")) {
                  return 27;
               }
               break;
            case 18:
               if (var1.equals("memory-buffer-size")) {
                  return 33;
               }
               break;
            case 20:
               if (var1.equals("memory-buffer-filter")) {
                  return 32;
               }
               break;
            case 21:
               if (var1.equals("log4j-logging-enabled")) {
                  return 34;
               }
               break;
            case 22:
               if (var1.equals("memory-buffer-severity")) {
                  return 31;
               }
               break;
            case 27:
               if (var1.equals("domain-log-broadcast-filter")) {
                  return 30;
               }
               break;
            case 29:
               if (var1.equals("domain-log-broadcast-severity")) {
                  return 29;
               }
               break;
            case 34:
               if (var1.equals("domain-log-broadcaster-buffer-size")) {
                  return 37;
               }
               break;
            case 37:
               if (var1.equals("redirect-stderr-to-server-log-enabled")) {
                  return 36;
               }

               if (var1.equals("redirect-stdout-to-server-log-enabled")) {
                  return 35;
               }
               break;
            case 48:
               if (var1.equals("server-logging-bridge-use-parent-loggers-enabled")) {
                  return 38;
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
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
            default:
               return super.getElementName(var1);
            case 27:
               return "log-file-filter";
            case 28:
               return "stdout-filter";
            case 29:
               return "domain-log-broadcast-severity";
            case 30:
               return "domain-log-broadcast-filter";
            case 31:
               return "memory-buffer-severity";
            case 32:
               return "memory-buffer-filter";
            case 33:
               return "memory-buffer-size";
            case 34:
               return "log4j-logging-enabled";
            case 35:
               return "redirect-stdout-to-server-log-enabled";
            case 36:
               return "redirect-stderr-to-server-log-enabled";
            case 37:
               return "domain-log-broadcaster-buffer-size";
            case 38:
               return "server-logging-bridge-use-parent-loggers-enabled";
         }
      }

      public boolean isBean(int var1) {
         switch (var1) {
            default:
               return super.isBean(var1);
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

   protected static class Helper extends CommonLogMBeanImpl.Helper {
      private LogMBeanImpl bean;

      protected Helper(LogMBeanImpl var1) {
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
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
            default:
               return super.getPropertyName(var1);
            case 27:
               return "LogFileFilter";
            case 28:
               return "StdoutFilter";
            case 29:
               return "DomainLogBroadcastSeverity";
            case 30:
               return "DomainLogBroadcastFilter";
            case 31:
               return "MemoryBufferSeverity";
            case 32:
               return "MemoryBufferFilter";
            case 33:
               return "MemoryBufferSize";
            case 34:
               return "Log4jLoggingEnabled";
            case 35:
               return "RedirectStdoutToServerLogEnabled";
            case 36:
               return "RedirectStderrToServerLogEnabled";
            case 37:
               return "DomainLogBroadcasterBufferSize";
            case 38:
               return "ServerLoggingBridgeUseParentLoggersEnabled";
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("DomainLogBroadcastFilter")) {
            return 30;
         } else if (var1.equals("DomainLogBroadcastSeverity")) {
            return 29;
         } else if (var1.equals("DomainLogBroadcasterBufferSize")) {
            return 37;
         } else if (var1.equals("LogFileFilter")) {
            return 27;
         } else if (var1.equals("MemoryBufferFilter")) {
            return 32;
         } else if (var1.equals("MemoryBufferSeverity")) {
            return 31;
         } else if (var1.equals("MemoryBufferSize")) {
            return 33;
         } else if (var1.equals("Name")) {
            return 2;
         } else if (var1.equals("StdoutFilter")) {
            return 28;
         } else if (var1.equals("Log4jLoggingEnabled")) {
            return 34;
         } else if (var1.equals("RedirectStderrToServerLogEnabled")) {
            return 36;
         } else if (var1.equals("RedirectStdoutToServerLogEnabled")) {
            return 35;
         } else {
            return var1.equals("ServerLoggingBridgeUseParentLoggersEnabled") ? 38 : super.getPropertyIndex(var1);
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
            if (this.bean.isDomainLogBroadcastFilterSet()) {
               var2.append("DomainLogBroadcastFilter");
               var2.append(String.valueOf(this.bean.getDomainLogBroadcastFilter()));
            }

            if (this.bean.isDomainLogBroadcastSeveritySet()) {
               var2.append("DomainLogBroadcastSeverity");
               var2.append(String.valueOf(this.bean.getDomainLogBroadcastSeverity()));
            }

            if (this.bean.isDomainLogBroadcasterBufferSizeSet()) {
               var2.append("DomainLogBroadcasterBufferSize");
               var2.append(String.valueOf(this.bean.getDomainLogBroadcasterBufferSize()));
            }

            if (this.bean.isLogFileFilterSet()) {
               var2.append("LogFileFilter");
               var2.append(String.valueOf(this.bean.getLogFileFilter()));
            }

            if (this.bean.isMemoryBufferFilterSet()) {
               var2.append("MemoryBufferFilter");
               var2.append(String.valueOf(this.bean.getMemoryBufferFilter()));
            }

            if (this.bean.isMemoryBufferSeveritySet()) {
               var2.append("MemoryBufferSeverity");
               var2.append(String.valueOf(this.bean.getMemoryBufferSeverity()));
            }

            if (this.bean.isMemoryBufferSizeSet()) {
               var2.append("MemoryBufferSize");
               var2.append(String.valueOf(this.bean.getMemoryBufferSize()));
            }

            if (this.bean.isNameSet()) {
               var2.append("Name");
               var2.append(String.valueOf(this.bean.getName()));
            }

            if (this.bean.isStdoutFilterSet()) {
               var2.append("StdoutFilter");
               var2.append(String.valueOf(this.bean.getStdoutFilter()));
            }

            if (this.bean.isLog4jLoggingEnabledSet()) {
               var2.append("Log4jLoggingEnabled");
               var2.append(String.valueOf(this.bean.isLog4jLoggingEnabled()));
            }

            if (this.bean.isRedirectStderrToServerLogEnabledSet()) {
               var2.append("RedirectStderrToServerLogEnabled");
               var2.append(String.valueOf(this.bean.isRedirectStderrToServerLogEnabled()));
            }

            if (this.bean.isRedirectStdoutToServerLogEnabledSet()) {
               var2.append("RedirectStdoutToServerLogEnabled");
               var2.append(String.valueOf(this.bean.isRedirectStdoutToServerLogEnabled()));
            }

            if (this.bean.isServerLoggingBridgeUseParentLoggersEnabledSet()) {
               var2.append("ServerLoggingBridgeUseParentLoggersEnabled");
               var2.append(String.valueOf(this.bean.isServerLoggingBridgeUseParentLoggersEnabled()));
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
            LogMBeanImpl var2 = (LogMBeanImpl)var1;
            this.computeDiff("DomainLogBroadcastFilter", this.bean.getDomainLogBroadcastFilter(), var2.getDomainLogBroadcastFilter(), true);
            this.computeDiff("DomainLogBroadcastSeverity", this.bean.getDomainLogBroadcastSeverity(), var2.getDomainLogBroadcastSeverity(), true);
            this.computeDiff("DomainLogBroadcasterBufferSize", this.bean.getDomainLogBroadcasterBufferSize(), var2.getDomainLogBroadcasterBufferSize(), true);
            this.computeDiff("LogFileFilter", this.bean.getLogFileFilter(), var2.getLogFileFilter(), true);
            this.computeDiff("MemoryBufferFilter", this.bean.getMemoryBufferFilter(), var2.getMemoryBufferFilter(), true);
            this.computeDiff("MemoryBufferSeverity", this.bean.getMemoryBufferSeverity(), var2.getMemoryBufferSeverity(), true);
            this.computeDiff("MemoryBufferSize", this.bean.getMemoryBufferSize(), var2.getMemoryBufferSize(), true);
            this.computeDiff("Name", this.bean.getName(), var2.getName(), false);
            this.computeDiff("StdoutFilter", this.bean.getStdoutFilter(), var2.getStdoutFilter(), true);
            this.computeDiff("Log4jLoggingEnabled", this.bean.isLog4jLoggingEnabled(), var2.isLog4jLoggingEnabled(), false);
            this.computeDiff("RedirectStderrToServerLogEnabled", this.bean.isRedirectStderrToServerLogEnabled(), var2.isRedirectStderrToServerLogEnabled(), false);
            this.computeDiff("RedirectStdoutToServerLogEnabled", this.bean.isRedirectStdoutToServerLogEnabled(), var2.isRedirectStdoutToServerLogEnabled(), false);
            this.computeDiff("ServerLoggingBridgeUseParentLoggersEnabled", this.bean.isServerLoggingBridgeUseParentLoggersEnabled(), var2.isServerLoggingBridgeUseParentLoggersEnabled(), false);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            LogMBeanImpl var3 = (LogMBeanImpl)var1.getSourceBean();
            LogMBeanImpl var4 = (LogMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("DomainLogBroadcastFilter")) {
                  var3.setDomainLogBroadcastFilterAsString(var4.getDomainLogBroadcastFilterAsString());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 30);
               } else if (var5.equals("DomainLogBroadcastSeverity")) {
                  var3.setDomainLogBroadcastSeverity(var4.getDomainLogBroadcastSeverity());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 29);
               } else if (var5.equals("DomainLogBroadcasterBufferSize")) {
                  var3.setDomainLogBroadcasterBufferSize(var4.getDomainLogBroadcasterBufferSize());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 37);
               } else if (var5.equals("LogFileFilter")) {
                  var3.setLogFileFilterAsString(var4.getLogFileFilterAsString());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 27);
               } else if (var5.equals("MemoryBufferFilter")) {
                  var3.setMemoryBufferFilterAsString(var4.getMemoryBufferFilterAsString());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 32);
               } else if (var5.equals("MemoryBufferSeverity")) {
                  var3.setMemoryBufferSeverity(var4.getMemoryBufferSeverity());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 31);
               } else if (var5.equals("MemoryBufferSize")) {
                  var3.setMemoryBufferSize(var4.getMemoryBufferSize());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 33);
               } else if (var5.equals("Name")) {
                  var3.setName(var4.getName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 2);
               } else if (var5.equals("StdoutFilter")) {
                  var3.setStdoutFilterAsString(var4.getStdoutFilterAsString());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 28);
               } else if (var5.equals("Log4jLoggingEnabled")) {
                  var3.setLog4jLoggingEnabled(var4.isLog4jLoggingEnabled());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 34);
               } else if (var5.equals("RedirectStderrToServerLogEnabled")) {
                  var3.setRedirectStderrToServerLogEnabled(var4.isRedirectStderrToServerLogEnabled());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 36);
               } else if (var5.equals("RedirectStdoutToServerLogEnabled")) {
                  var3.setRedirectStdoutToServerLogEnabled(var4.isRedirectStdoutToServerLogEnabled());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 35);
               } else if (var5.equals("ServerLoggingBridgeUseParentLoggersEnabled")) {
                  var3.setServerLoggingBridgeUseParentLoggersEnabled(var4.isServerLoggingBridgeUseParentLoggersEnabled());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 38);
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
            LogMBeanImpl var5 = (LogMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("DomainLogBroadcastFilter")) && this.bean.isDomainLogBroadcastFilterSet()) {
               var5._unSet(var5, 30);
               var5.setDomainLogBroadcastFilterAsString(this.bean.getDomainLogBroadcastFilterAsString());
            }

            if ((var3 == null || !var3.contains("DomainLogBroadcastSeverity")) && this.bean.isDomainLogBroadcastSeveritySet()) {
               var5.setDomainLogBroadcastSeverity(this.bean.getDomainLogBroadcastSeverity());
            }

            if ((var3 == null || !var3.contains("DomainLogBroadcasterBufferSize")) && this.bean.isDomainLogBroadcasterBufferSizeSet()) {
               var5.setDomainLogBroadcasterBufferSize(this.bean.getDomainLogBroadcasterBufferSize());
            }

            if ((var3 == null || !var3.contains("LogFileFilter")) && this.bean.isLogFileFilterSet()) {
               var5._unSet(var5, 27);
               var5.setLogFileFilterAsString(this.bean.getLogFileFilterAsString());
            }

            if ((var3 == null || !var3.contains("MemoryBufferFilter")) && this.bean.isMemoryBufferFilterSet()) {
               var5._unSet(var5, 32);
               var5.setMemoryBufferFilterAsString(this.bean.getMemoryBufferFilterAsString());
            }

            if ((var3 == null || !var3.contains("MemoryBufferSeverity")) && this.bean.isMemoryBufferSeveritySet()) {
               var5.setMemoryBufferSeverity(this.bean.getMemoryBufferSeverity());
            }

            if ((var3 == null || !var3.contains("MemoryBufferSize")) && this.bean.isMemoryBufferSizeSet()) {
               var5.setMemoryBufferSize(this.bean.getMemoryBufferSize());
            }

            if ((var3 == null || !var3.contains("Name")) && this.bean.isNameSet()) {
               var5.setName(this.bean.getName());
            }

            if ((var3 == null || !var3.contains("StdoutFilter")) && this.bean.isStdoutFilterSet()) {
               var5._unSet(var5, 28);
               var5.setStdoutFilterAsString(this.bean.getStdoutFilterAsString());
            }

            if ((var3 == null || !var3.contains("Log4jLoggingEnabled")) && this.bean.isLog4jLoggingEnabledSet()) {
               var5.setLog4jLoggingEnabled(this.bean.isLog4jLoggingEnabled());
            }

            if ((var3 == null || !var3.contains("RedirectStderrToServerLogEnabled")) && this.bean.isRedirectStderrToServerLogEnabledSet()) {
               var5.setRedirectStderrToServerLogEnabled(this.bean.isRedirectStderrToServerLogEnabled());
            }

            if ((var3 == null || !var3.contains("RedirectStdoutToServerLogEnabled")) && this.bean.isRedirectStdoutToServerLogEnabledSet()) {
               var5.setRedirectStdoutToServerLogEnabled(this.bean.isRedirectStdoutToServerLogEnabled());
            }

            if ((var3 == null || !var3.contains("ServerLoggingBridgeUseParentLoggersEnabled")) && this.bean.isServerLoggingBridgeUseParentLoggersEnabledSet()) {
               var5.setServerLoggingBridgeUseParentLoggersEnabled(this.bean.isServerLoggingBridgeUseParentLoggersEnabled());
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
         this.inferSubTree(this.bean.getDomainLogBroadcastFilter(), var1, var2);
         this.inferSubTree(this.bean.getLogFileFilter(), var1, var2);
         this.inferSubTree(this.bean.getMemoryBufferFilter(), var1, var2);
         this.inferSubTree(this.bean.getStdoutFilter(), var1, var2);
      }
   }
}
