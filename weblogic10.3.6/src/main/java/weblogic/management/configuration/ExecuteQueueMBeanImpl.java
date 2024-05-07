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
import weblogic.utils.collections.CombinedIterator;

public class ExecuteQueueMBeanImpl extends ConfigurationMBeanImpl implements ExecuteQueueMBean, Serializable {
   private int _QueueLength;
   private int _QueueLengthThresholdPercent;
   private int _ThreadCount;
   private int _ThreadPriority;
   private int _ThreadsIncrease;
   private int _ThreadsMaximum;
   private int _ThreadsMinimum;
   private static SchemaHelper2 _schemaHelper;

   public ExecuteQueueMBeanImpl() {
      this._initializeProperty(-1);
   }

   public ExecuteQueueMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);
      this._initializeProperty(-1);
   }

   public int getQueueLength() {
      return this._QueueLength;
   }

   public boolean isQueueLengthSet() {
      return this._isSet(7);
   }

   public void setQueueLength(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("QueueLength", (long)var1, 256L, 1073741824L);
      int var2 = this._QueueLength;
      this._QueueLength = var1;
      this._postSet(7, var2, var1);
   }

   public int getThreadPriority() {
      return this._ThreadPriority;
   }

   public boolean isThreadPrioritySet() {
      return this._isSet(8);
   }

   public void setThreadPriority(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("ThreadPriority", (long)var1, 1L, 10L);
      int var2 = this._ThreadPriority;
      this._ThreadPriority = var1;
      this._postSet(8, var2, var1);
   }

   public int getThreadCount() {
      if (!this._isSet(9)) {
         return this._isProductionModeEnabled() ? 25 : 15;
      } else {
         return this._ThreadCount;
      }
   }

   public boolean isThreadCountSet() {
      return this._isSet(9);
   }

   public void setThreadCount(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("ThreadCount", (long)var1, 0L, 65536L);
      int var2 = this._ThreadCount;
      this._ThreadCount = var1;
      this._postSet(9, var2, var1);
   }

   public int getQueueLengthThresholdPercent() {
      return this._QueueLengthThresholdPercent;
   }

   public boolean isQueueLengthThresholdPercentSet() {
      return this._isSet(10);
   }

   public void setQueueLengthThresholdPercent(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("QueueLengthThresholdPercent", (long)var1, 1L, 99L);
      int var2 = this._QueueLengthThresholdPercent;
      this._QueueLengthThresholdPercent = var1;
      this._postSet(10, var2, var1);
   }

   public int getThreadsIncrease() {
      return this._ThreadsIncrease;
   }

   public boolean isThreadsIncreaseSet() {
      return this._isSet(11);
   }

   public void setThreadsIncrease(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("ThreadsIncrease", (long)var1, 0L, 65536L);
      int var2 = this._ThreadsIncrease;
      this._ThreadsIncrease = var1;
      this._postSet(11, var2, var1);
   }

   public int getThreadsMaximum() {
      return this._ThreadsMaximum;
   }

   public boolean isThreadsMaximumSet() {
      return this._isSet(12);
   }

   public void setThreadsMaximum(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("ThreadsMaximum", (long)var1, 1L, 65536L);
      int var2 = this._ThreadsMaximum;
      this._ThreadsMaximum = var1;
      this._postSet(12, var2, var1);
   }

   public int getThreadsMinimum() {
      return this._ThreadsMinimum;
   }

   public boolean isThreadsMinimumSet() {
      return this._isSet(13);
   }

   public void setThreadsMinimum(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("ThreadsMinimum", (long)var1, 0L, 65536L);
      int var2 = this._ThreadsMinimum;
      this._ThreadsMinimum = var1;
      this._postSet(13, var2, var1);
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
         var1 = 7;
      }

      try {
         switch (var1) {
            case 7:
               this._QueueLength = 65536;
               if (var2) {
                  break;
               }
            case 10:
               this._QueueLengthThresholdPercent = 90;
               if (var2) {
                  break;
               }
            case 9:
               this._ThreadCount = 15;
               if (var2) {
                  break;
               }
            case 8:
               this._ThreadPriority = 5;
               if (var2) {
                  break;
               }
            case 11:
               this._ThreadsIncrease = 0;
               if (var2) {
                  break;
               }
            case 12:
               this._ThreadsMaximum = 400;
               if (var2) {
                  break;
               }
            case 13:
               this._ThreadsMinimum = 5;
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
      return "ExecuteQueue";
   }

   public void putValue(String var1, Object var2) {
      int var3;
      if (var1.equals("QueueLength")) {
         var3 = this._QueueLength;
         this._QueueLength = (Integer)var2;
         this._postSet(7, var3, this._QueueLength);
      } else if (var1.equals("QueueLengthThresholdPercent")) {
         var3 = this._QueueLengthThresholdPercent;
         this._QueueLengthThresholdPercent = (Integer)var2;
         this._postSet(10, var3, this._QueueLengthThresholdPercent);
      } else if (var1.equals("ThreadCount")) {
         var3 = this._ThreadCount;
         this._ThreadCount = (Integer)var2;
         this._postSet(9, var3, this._ThreadCount);
      } else if (var1.equals("ThreadPriority")) {
         var3 = this._ThreadPriority;
         this._ThreadPriority = (Integer)var2;
         this._postSet(8, var3, this._ThreadPriority);
      } else if (var1.equals("ThreadsIncrease")) {
         var3 = this._ThreadsIncrease;
         this._ThreadsIncrease = (Integer)var2;
         this._postSet(11, var3, this._ThreadsIncrease);
      } else if (var1.equals("ThreadsMaximum")) {
         var3 = this._ThreadsMaximum;
         this._ThreadsMaximum = (Integer)var2;
         this._postSet(12, var3, this._ThreadsMaximum);
      } else if (var1.equals("ThreadsMinimum")) {
         var3 = this._ThreadsMinimum;
         this._ThreadsMinimum = (Integer)var2;
         this._postSet(13, var3, this._ThreadsMinimum);
      } else {
         super.putValue(var1, var2);
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("QueueLength")) {
         return new Integer(this._QueueLength);
      } else if (var1.equals("QueueLengthThresholdPercent")) {
         return new Integer(this._QueueLengthThresholdPercent);
      } else if (var1.equals("ThreadCount")) {
         return new Integer(this._ThreadCount);
      } else if (var1.equals("ThreadPriority")) {
         return new Integer(this._ThreadPriority);
      } else if (var1.equals("ThreadsIncrease")) {
         return new Integer(this._ThreadsIncrease);
      } else if (var1.equals("ThreadsMaximum")) {
         return new Integer(this._ThreadsMaximum);
      } else {
         return var1.equals("ThreadsMinimum") ? new Integer(this._ThreadsMinimum) : super.getValue(var1);
      }
   }

   public static class SchemaHelper2 extends ConfigurationMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 12:
               if (var1.equals("queue-length")) {
                  return 7;
               }

               if (var1.equals("thread-count")) {
                  return 9;
               }
               break;
            case 15:
               if (var1.equals("thread-priority")) {
                  return 8;
               }

               if (var1.equals("threads-maximum")) {
                  return 12;
               }

               if (var1.equals("threads-minimum")) {
                  return 13;
               }
               break;
            case 16:
               if (var1.equals("threads-increase")) {
                  return 11;
               }
               break;
            case 30:
               if (var1.equals("queue-length-threshold-percent")) {
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
            case 7:
               return "queue-length";
            case 8:
               return "thread-priority";
            case 9:
               return "thread-count";
            case 10:
               return "queue-length-threshold-percent";
            case 11:
               return "threads-increase";
            case 12:
               return "threads-maximum";
            case 13:
               return "threads-minimum";
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
      private ExecuteQueueMBeanImpl bean;

      protected Helper(ExecuteQueueMBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 7:
               return "QueueLength";
            case 8:
               return "ThreadPriority";
            case 9:
               return "ThreadCount";
            case 10:
               return "QueueLengthThresholdPercent";
            case 11:
               return "ThreadsIncrease";
            case 12:
               return "ThreadsMaximum";
            case 13:
               return "ThreadsMinimum";
            default:
               return super.getPropertyName(var1);
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("QueueLength")) {
            return 7;
         } else if (var1.equals("QueueLengthThresholdPercent")) {
            return 10;
         } else if (var1.equals("ThreadCount")) {
            return 9;
         } else if (var1.equals("ThreadPriority")) {
            return 8;
         } else if (var1.equals("ThreadsIncrease")) {
            return 11;
         } else if (var1.equals("ThreadsMaximum")) {
            return 12;
         } else {
            return var1.equals("ThreadsMinimum") ? 13 : super.getPropertyIndex(var1);
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
            if (this.bean.isQueueLengthSet()) {
               var2.append("QueueLength");
               var2.append(String.valueOf(this.bean.getQueueLength()));
            }

            if (this.bean.isQueueLengthThresholdPercentSet()) {
               var2.append("QueueLengthThresholdPercent");
               var2.append(String.valueOf(this.bean.getQueueLengthThresholdPercent()));
            }

            if (this.bean.isThreadCountSet()) {
               var2.append("ThreadCount");
               var2.append(String.valueOf(this.bean.getThreadCount()));
            }

            if (this.bean.isThreadPrioritySet()) {
               var2.append("ThreadPriority");
               var2.append(String.valueOf(this.bean.getThreadPriority()));
            }

            if (this.bean.isThreadsIncreaseSet()) {
               var2.append("ThreadsIncrease");
               var2.append(String.valueOf(this.bean.getThreadsIncrease()));
            }

            if (this.bean.isThreadsMaximumSet()) {
               var2.append("ThreadsMaximum");
               var2.append(String.valueOf(this.bean.getThreadsMaximum()));
            }

            if (this.bean.isThreadsMinimumSet()) {
               var2.append("ThreadsMinimum");
               var2.append(String.valueOf(this.bean.getThreadsMinimum()));
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
            ExecuteQueueMBeanImpl var2 = (ExecuteQueueMBeanImpl)var1;
            this.computeDiff("QueueLength", this.bean.getQueueLength(), var2.getQueueLength(), false);
            this.computeDiff("QueueLengthThresholdPercent", this.bean.getQueueLengthThresholdPercent(), var2.getQueueLengthThresholdPercent(), true);
            this.computeDiff("ThreadCount", this.bean.getThreadCount(), var2.getThreadCount(), false);
            this.computeDiff("ThreadPriority", this.bean.getThreadPriority(), var2.getThreadPriority(), false);
            this.computeDiff("ThreadsIncrease", this.bean.getThreadsIncrease(), var2.getThreadsIncrease(), true);
            this.computeDiff("ThreadsMaximum", this.bean.getThreadsMaximum(), var2.getThreadsMaximum(), true);
            this.computeDiff("ThreadsMinimum", this.bean.getThreadsMinimum(), var2.getThreadsMinimum(), false);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            ExecuteQueueMBeanImpl var3 = (ExecuteQueueMBeanImpl)var1.getSourceBean();
            ExecuteQueueMBeanImpl var4 = (ExecuteQueueMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("QueueLength")) {
                  var3.setQueueLength(var4.getQueueLength());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 7);
               } else if (var5.equals("QueueLengthThresholdPercent")) {
                  var3.setQueueLengthThresholdPercent(var4.getQueueLengthThresholdPercent());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 10);
               } else if (var5.equals("ThreadCount")) {
                  var3.setThreadCount(var4.getThreadCount());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 9);
               } else if (var5.equals("ThreadPriority")) {
                  var3.setThreadPriority(var4.getThreadPriority());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 8);
               } else if (var5.equals("ThreadsIncrease")) {
                  var3.setThreadsIncrease(var4.getThreadsIncrease());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 11);
               } else if (var5.equals("ThreadsMaximum")) {
                  var3.setThreadsMaximum(var4.getThreadsMaximum());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 12);
               } else if (var5.equals("ThreadsMinimum")) {
                  var3.setThreadsMinimum(var4.getThreadsMinimum());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 13);
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
            ExecuteQueueMBeanImpl var5 = (ExecuteQueueMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("QueueLength")) && this.bean.isQueueLengthSet()) {
               var5.setQueueLength(this.bean.getQueueLength());
            }

            if ((var3 == null || !var3.contains("QueueLengthThresholdPercent")) && this.bean.isQueueLengthThresholdPercentSet()) {
               var5.setQueueLengthThresholdPercent(this.bean.getQueueLengthThresholdPercent());
            }

            if ((var3 == null || !var3.contains("ThreadCount")) && this.bean.isThreadCountSet()) {
               var5.setThreadCount(this.bean.getThreadCount());
            }

            if ((var3 == null || !var3.contains("ThreadPriority")) && this.bean.isThreadPrioritySet()) {
               var5.setThreadPriority(this.bean.getThreadPriority());
            }

            if ((var3 == null || !var3.contains("ThreadsIncrease")) && this.bean.isThreadsIncreaseSet()) {
               var5.setThreadsIncrease(this.bean.getThreadsIncrease());
            }

            if ((var3 == null || !var3.contains("ThreadsMaximum")) && this.bean.isThreadsMaximumSet()) {
               var5.setThreadsMaximum(this.bean.getThreadsMaximum());
            }

            if ((var3 == null || !var3.contains("ThreadsMinimum")) && this.bean.isThreadsMinimumSet()) {
               var5.setThreadsMinimum(this.bean.getThreadsMinimum());
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
