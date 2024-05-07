package weblogic.diagnostics.image.descriptor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.zip.CRC32;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBeanHelper;
import weblogic.descriptor.internal.AbstractSchemaHelper2;
import weblogic.descriptor.internal.Munger;
import weblogic.descriptor.internal.SchemaHelper;
import weblogic.utils.collections.CombinedIterator;

public class LogEntryBeanImpl extends AbstractDescriptorBean implements LogEntryBean, Serializable {
   private String _DiagnosticContextId;
   private String _FormattedDate;
   private String _LogMessage;
   private String _MachineName;
   private String _MessageId;
   private String _ServerName;
   private int _Severity;
   private String _StackTrace;
   private String _Subsystem;
   private String _ThreadName;
   private long _Timestamp;
   private String _TransactionId;
   private String _UserId;
   private static SchemaHelper2 _schemaHelper;

   public LogEntryBeanImpl() {
      this._initializeProperty(-1);
   }

   public LogEntryBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);
      this._initializeProperty(-1);
   }

   public String getFormattedDate() {
      return this._FormattedDate;
   }

   public boolean isFormattedDateSet() {
      return this._isSet(0);
   }

   public void setFormattedDate(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._FormattedDate;
      this._FormattedDate = var1;
      this._postSet(0, var2, var1);
   }

   public String getMessageId() {
      return this._MessageId;
   }

   public boolean isMessageIdSet() {
      return this._isSet(1);
   }

   public void setMessageId(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._MessageId;
      this._MessageId = var1;
      this._postSet(1, var2, var1);
   }

   public String getMachineName() {
      return this._MachineName;
   }

   public boolean isMachineNameSet() {
      return this._isSet(2);
   }

   public void setMachineName(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._MachineName;
      this._MachineName = var1;
      this._postSet(2, var2, var1);
   }

   public String getServerName() {
      return this._ServerName;
   }

   public boolean isServerNameSet() {
      return this._isSet(3);
   }

   public void setServerName(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._ServerName;
      this._ServerName = var1;
      this._postSet(3, var2, var1);
   }

   public String getThreadName() {
      return this._ThreadName;
   }

   public boolean isThreadNameSet() {
      return this._isSet(4);
   }

   public void setThreadName(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._ThreadName;
      this._ThreadName = var1;
      this._postSet(4, var2, var1);
   }

   public String getUserId() {
      return this._UserId;
   }

   public boolean isUserIdSet() {
      return this._isSet(5);
   }

   public void setUserId(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._UserId;
      this._UserId = var1;
      this._postSet(5, var2, var1);
   }

   public String getTransactionId() {
      return this._TransactionId;
   }

   public boolean isTransactionIdSet() {
      return this._isSet(6);
   }

   public void setTransactionId(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._TransactionId;
      this._TransactionId = var1;
      this._postSet(6, var2, var1);
   }

   public int getSeverity() {
      return this._Severity;
   }

   public boolean isSeveritySet() {
      return this._isSet(7);
   }

   public void setSeverity(int var1) {
      int var2 = this._Severity;
      this._Severity = var1;
      this._postSet(7, var2, var1);
   }

   public String getSubsystem() {
      return this._Subsystem;
   }

   public boolean isSubsystemSet() {
      return this._isSet(8);
   }

   public void setSubsystem(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._Subsystem;
      this._Subsystem = var1;
      this._postSet(8, var2, var1);
   }

   public long getTimestamp() {
      return this._Timestamp;
   }

   public boolean isTimestampSet() {
      return this._isSet(9);
   }

   public void setTimestamp(long var1) {
      long var3 = this._Timestamp;
      this._Timestamp = var1;
      this._postSet(9, var3, var1);
   }

   public String getLogMessage() {
      return this._LogMessage;
   }

   public boolean isLogMessageSet() {
      return this._isSet(10);
   }

   public void setLogMessage(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._LogMessage;
      this._LogMessage = var1;
      this._postSet(10, var2, var1);
   }

   public String getStackTrace() {
      return this._StackTrace;
   }

   public boolean isStackTraceSet() {
      return this._isSet(11);
   }

   public void setStackTrace(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._StackTrace;
      this._StackTrace = var1;
      this._postSet(11, var2, var1);
   }

   public String getDiagnosticContextId() {
      return this._DiagnosticContextId;
   }

   public boolean isDiagnosticContextIdSet() {
      return this._isSet(12);
   }

   public void setDiagnosticContextId(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._DiagnosticContextId;
      this._DiagnosticContextId = var1;
      this._postSet(12, var2, var1);
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
         var1 = 12;
      }

      try {
         switch (var1) {
            case 12:
               this._DiagnosticContextId = null;
               if (var2) {
                  break;
               }
            case 0:
               this._FormattedDate = null;
               if (var2) {
                  break;
               }
            case 10:
               this._LogMessage = null;
               if (var2) {
                  break;
               }
            case 2:
               this._MachineName = null;
               if (var2) {
                  break;
               }
            case 1:
               this._MessageId = null;
               if (var2) {
                  break;
               }
            case 3:
               this._ServerName = null;
               if (var2) {
                  break;
               }
            case 7:
               this._Severity = 0;
               if (var2) {
                  break;
               }
            case 11:
               this._StackTrace = null;
               if (var2) {
                  break;
               }
            case 8:
               this._Subsystem = null;
               if (var2) {
                  break;
               }
            case 4:
               this._ThreadName = null;
               if (var2) {
                  break;
               }
            case 9:
               this._Timestamp = 0L;
               if (var2) {
                  break;
               }
            case 6:
               this._TransactionId = null;
               if (var2) {
                  break;
               }
            case 5:
               this._UserId = null;
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
      return "http://xmlns.oracle.com/weblogic/weblogic-diagnostics-image/1.0/weblogic-diagnostics-image.xsd";
   }

   protected String getTargetNamespace() {
      return "http://xmlns.oracle.com/weblogic/weblogic-diagnostics-image";
   }

   public SchemaHelper _getSchemaHelper2() {
      if (_schemaHelper == null) {
         _schemaHelper = new SchemaHelper2();
      }

      return _schemaHelper;
   }

   public static class SchemaHelper2 extends AbstractSchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 7:
               if (var1.equals("user-id")) {
                  return 5;
               }
               break;
            case 8:
               if (var1.equals("severity")) {
                  return 7;
               }
               break;
            case 9:
               if (var1.equals("subsystem")) {
                  return 8;
               }

               if (var1.equals("timestamp")) {
                  return 9;
               }
               break;
            case 10:
               if (var1.equals("message-id")) {
                  return 1;
               }
               break;
            case 11:
               if (var1.equals("log-message")) {
                  return 10;
               }

               if (var1.equals("server-name")) {
                  return 3;
               }

               if (var1.equals("stack-trace")) {
                  return 11;
               }

               if (var1.equals("thread-name")) {
                  return 4;
               }
               break;
            case 12:
               if (var1.equals("machine-name")) {
                  return 2;
               }
            case 13:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            default:
               break;
            case 14:
               if (var1.equals("formatted-date")) {
                  return 0;
               }

               if (var1.equals("transaction-id")) {
                  return 6;
               }
               break;
            case 21:
               if (var1.equals("diagnostic-context-id")) {
                  return 12;
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
            case 0:
               return "formatted-date";
            case 1:
               return "message-id";
            case 2:
               return "machine-name";
            case 3:
               return "server-name";
            case 4:
               return "thread-name";
            case 5:
               return "user-id";
            case 6:
               return "transaction-id";
            case 7:
               return "severity";
            case 8:
               return "subsystem";
            case 9:
               return "timestamp";
            case 10:
               return "log-message";
            case 11:
               return "stack-trace";
            case 12:
               return "diagnostic-context-id";
            default:
               return super.getElementName(var1);
         }
      }
   }

   protected static class Helper extends AbstractDescriptorBeanHelper {
      private LogEntryBeanImpl bean;

      protected Helper(LogEntryBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 0:
               return "FormattedDate";
            case 1:
               return "MessageId";
            case 2:
               return "MachineName";
            case 3:
               return "ServerName";
            case 4:
               return "ThreadName";
            case 5:
               return "UserId";
            case 6:
               return "TransactionId";
            case 7:
               return "Severity";
            case 8:
               return "Subsystem";
            case 9:
               return "Timestamp";
            case 10:
               return "LogMessage";
            case 11:
               return "StackTrace";
            case 12:
               return "DiagnosticContextId";
            default:
               return super.getPropertyName(var1);
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("DiagnosticContextId")) {
            return 12;
         } else if (var1.equals("FormattedDate")) {
            return 0;
         } else if (var1.equals("LogMessage")) {
            return 10;
         } else if (var1.equals("MachineName")) {
            return 2;
         } else if (var1.equals("MessageId")) {
            return 1;
         } else if (var1.equals("ServerName")) {
            return 3;
         } else if (var1.equals("Severity")) {
            return 7;
         } else if (var1.equals("StackTrace")) {
            return 11;
         } else if (var1.equals("Subsystem")) {
            return 8;
         } else if (var1.equals("ThreadName")) {
            return 4;
         } else if (var1.equals("Timestamp")) {
            return 9;
         } else if (var1.equals("TransactionId")) {
            return 6;
         } else {
            return var1.equals("UserId") ? 5 : super.getPropertyIndex(var1);
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
            if (this.bean.isDiagnosticContextIdSet()) {
               var2.append("DiagnosticContextId");
               var2.append(String.valueOf(this.bean.getDiagnosticContextId()));
            }

            if (this.bean.isFormattedDateSet()) {
               var2.append("FormattedDate");
               var2.append(String.valueOf(this.bean.getFormattedDate()));
            }

            if (this.bean.isLogMessageSet()) {
               var2.append("LogMessage");
               var2.append(String.valueOf(this.bean.getLogMessage()));
            }

            if (this.bean.isMachineNameSet()) {
               var2.append("MachineName");
               var2.append(String.valueOf(this.bean.getMachineName()));
            }

            if (this.bean.isMessageIdSet()) {
               var2.append("MessageId");
               var2.append(String.valueOf(this.bean.getMessageId()));
            }

            if (this.bean.isServerNameSet()) {
               var2.append("ServerName");
               var2.append(String.valueOf(this.bean.getServerName()));
            }

            if (this.bean.isSeveritySet()) {
               var2.append("Severity");
               var2.append(String.valueOf(this.bean.getSeverity()));
            }

            if (this.bean.isStackTraceSet()) {
               var2.append("StackTrace");
               var2.append(String.valueOf(this.bean.getStackTrace()));
            }

            if (this.bean.isSubsystemSet()) {
               var2.append("Subsystem");
               var2.append(String.valueOf(this.bean.getSubsystem()));
            }

            if (this.bean.isThreadNameSet()) {
               var2.append("ThreadName");
               var2.append(String.valueOf(this.bean.getThreadName()));
            }

            if (this.bean.isTimestampSet()) {
               var2.append("Timestamp");
               var2.append(String.valueOf(this.bean.getTimestamp()));
            }

            if (this.bean.isTransactionIdSet()) {
               var2.append("TransactionId");
               var2.append(String.valueOf(this.bean.getTransactionId()));
            }

            if (this.bean.isUserIdSet()) {
               var2.append("UserId");
               var2.append(String.valueOf(this.bean.getUserId()));
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
            LogEntryBeanImpl var2 = (LogEntryBeanImpl)var1;
            this.computeDiff("DiagnosticContextId", this.bean.getDiagnosticContextId(), var2.getDiagnosticContextId(), false);
            this.computeDiff("FormattedDate", this.bean.getFormattedDate(), var2.getFormattedDate(), false);
            this.computeDiff("LogMessage", this.bean.getLogMessage(), var2.getLogMessage(), false);
            this.computeDiff("MachineName", this.bean.getMachineName(), var2.getMachineName(), false);
            this.computeDiff("MessageId", this.bean.getMessageId(), var2.getMessageId(), false);
            this.computeDiff("ServerName", this.bean.getServerName(), var2.getServerName(), false);
            this.computeDiff("Severity", this.bean.getSeverity(), var2.getSeverity(), false);
            this.computeDiff("StackTrace", this.bean.getStackTrace(), var2.getStackTrace(), false);
            this.computeDiff("Subsystem", this.bean.getSubsystem(), var2.getSubsystem(), false);
            this.computeDiff("ThreadName", this.bean.getThreadName(), var2.getThreadName(), false);
            this.computeDiff("Timestamp", this.bean.getTimestamp(), var2.getTimestamp(), false);
            this.computeDiff("TransactionId", this.bean.getTransactionId(), var2.getTransactionId(), false);
            this.computeDiff("UserId", this.bean.getUserId(), var2.getUserId(), false);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            LogEntryBeanImpl var3 = (LogEntryBeanImpl)var1.getSourceBean();
            LogEntryBeanImpl var4 = (LogEntryBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("DiagnosticContextId")) {
                  var3.setDiagnosticContextId(var4.getDiagnosticContextId());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 12);
               } else if (var5.equals("FormattedDate")) {
                  var3.setFormattedDate(var4.getFormattedDate());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 0);
               } else if (var5.equals("LogMessage")) {
                  var3.setLogMessage(var4.getLogMessage());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 10);
               } else if (var5.equals("MachineName")) {
                  var3.setMachineName(var4.getMachineName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 2);
               } else if (var5.equals("MessageId")) {
                  var3.setMessageId(var4.getMessageId());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 1);
               } else if (var5.equals("ServerName")) {
                  var3.setServerName(var4.getServerName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 3);
               } else if (var5.equals("Severity")) {
                  var3.setSeverity(var4.getSeverity());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 7);
               } else if (var5.equals("StackTrace")) {
                  var3.setStackTrace(var4.getStackTrace());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 11);
               } else if (var5.equals("Subsystem")) {
                  var3.setSubsystem(var4.getSubsystem());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 8);
               } else if (var5.equals("ThreadName")) {
                  var3.setThreadName(var4.getThreadName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 4);
               } else if (var5.equals("Timestamp")) {
                  var3.setTimestamp(var4.getTimestamp());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 9);
               } else if (var5.equals("TransactionId")) {
                  var3.setTransactionId(var4.getTransactionId());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 6);
               } else if (var5.equals("UserId")) {
                  var3.setUserId(var4.getUserId());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 5);
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
            LogEntryBeanImpl var5 = (LogEntryBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("DiagnosticContextId")) && this.bean.isDiagnosticContextIdSet()) {
               var5.setDiagnosticContextId(this.bean.getDiagnosticContextId());
            }

            if ((var3 == null || !var3.contains("FormattedDate")) && this.bean.isFormattedDateSet()) {
               var5.setFormattedDate(this.bean.getFormattedDate());
            }

            if ((var3 == null || !var3.contains("LogMessage")) && this.bean.isLogMessageSet()) {
               var5.setLogMessage(this.bean.getLogMessage());
            }

            if ((var3 == null || !var3.contains("MachineName")) && this.bean.isMachineNameSet()) {
               var5.setMachineName(this.bean.getMachineName());
            }

            if ((var3 == null || !var3.contains("MessageId")) && this.bean.isMessageIdSet()) {
               var5.setMessageId(this.bean.getMessageId());
            }

            if ((var3 == null || !var3.contains("ServerName")) && this.bean.isServerNameSet()) {
               var5.setServerName(this.bean.getServerName());
            }

            if ((var3 == null || !var3.contains("Severity")) && this.bean.isSeveritySet()) {
               var5.setSeverity(this.bean.getSeverity());
            }

            if ((var3 == null || !var3.contains("StackTrace")) && this.bean.isStackTraceSet()) {
               var5.setStackTrace(this.bean.getStackTrace());
            }

            if ((var3 == null || !var3.contains("Subsystem")) && this.bean.isSubsystemSet()) {
               var5.setSubsystem(this.bean.getSubsystem());
            }

            if ((var3 == null || !var3.contains("ThreadName")) && this.bean.isThreadNameSet()) {
               var5.setThreadName(this.bean.getThreadName());
            }

            if ((var3 == null || !var3.contains("Timestamp")) && this.bean.isTimestampSet()) {
               var5.setTimestamp(this.bean.getTimestamp());
            }

            if ((var3 == null || !var3.contains("TransactionId")) && this.bean.isTransactionIdSet()) {
               var5.setTransactionId(this.bean.getTransactionId());
            }

            if ((var3 == null || !var3.contains("UserId")) && this.bean.isUserIdSet()) {
               var5.setUserId(this.bean.getUserId());
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
