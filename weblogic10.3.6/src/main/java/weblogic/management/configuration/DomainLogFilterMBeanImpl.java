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
import weblogic.management.DistributedManagementException;
import weblogic.management.ManagementException;
import weblogic.management.mbeans.custom.DomainLogFilter;
import weblogic.utils.ArrayUtils;
import weblogic.utils.collections.CombinedIterator;

public class DomainLogFilterMBeanImpl extends ConfigurationMBeanImpl implements DomainLogFilterMBean, Serializable {
   private LogFilterMBean _Delegate;
   private String _Name;
   private int _SeverityLevel;
   private String[] _SubsystemNames;
   private String[] _UserIds;
   private DomainLogFilter _customizer;
   private static SchemaHelper2 _schemaHelper;

   public DomainLogFilterMBeanImpl() {
      try {
         this._customizer = new DomainLogFilter(this);
      } catch (Exception var2) {
         if (var2 instanceof RuntimeException) {
            throw (RuntimeException)var2;
         }

         throw new UndeclaredThrowableException(var2);
      }

      this._initializeProperty(-1);
   }

   public DomainLogFilterMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);

      try {
         this._customizer = new DomainLogFilter(this);
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

   public int getSeverityLevel() {
      return this._customizer.getSeverityLevel();
   }

   public boolean isNameSet() {
      return this._isSet(2);
   }

   public boolean isSeverityLevelSet() {
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

   public void setSeverityLevel(int var1) throws InvalidAttributeValueException, DistributedManagementException {
      int[] var2 = new int[]{64, 16, 8, 32, 4, 2, 1};
      var1 = weblogic.descriptor.beangen.LegalChecks.checkInEnum("SeverityLevel", var1, var2);
      int var3 = this.getSeverityLevel();
      this._customizer.setSeverityLevel(var1);
      this._postSet(7, var3, var1);
   }

   public String[] getSubsystemNames() {
      return this._customizer.getSubsystemNames();
   }

   public boolean isSubsystemNamesSet() {
      return this._isSet(8);
   }

   public void setSubsystemNames(String[] var1) throws InvalidAttributeValueException, DistributedManagementException {
      var1 = var1 == null ? new String[0] : var1;
      var1 = this._getHelper()._trimElements(var1);
      String[] var2 = this.getSubsystemNames();
      this._customizer.setSubsystemNames(var1);
      this._postSet(8, var2, var1);
   }

   public String[] getUserIds() {
      return this._customizer.getUserIds();
   }

   public boolean isUserIdsSet() {
      return this._isSet(9);
   }

   public void setUserIds(String[] var1) throws InvalidAttributeValueException, DistributedManagementException {
      var1 = var1 == null ? new String[0] : var1;
      var1 = this._getHelper()._trimElements(var1);
      String[] var2 = this.getUserIds();
      this._customizer.setUserIds(var1);
      this._postSet(9, var2, var1);
   }

   public LogFilterMBean getDelegate() {
      return this._customizer.getDelegate();
   }

   public boolean isDelegateSet() {
      return this._isSet(10);
   }

   public void setDelegate(LogFilterMBean var1) {
      this._customizer.setDelegate(var1);
   }

   public void touch() throws ConfigurationException {
      this._customizer.touch();
   }

   public void freezeCurrentValue(String var1) throws AttributeNotFoundException, MBeanException {
      this._customizer.freezeCurrentValue(var1);
   }

   public void restoreDefaultValue(String var1) throws AttributeNotFoundException {
      this._customizer.restoreDefaultValue(var1);
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
         var1 = 10;
      }

      try {
         switch (var1) {
            case 10:
               this._customizer.setDelegate((LogFilterMBean)null);
               if (var2) {
                  break;
               }
            case 2:
               this._customizer.setName((String)null);
               if (var2) {
                  break;
               }
            case 7:
               this._customizer.setSeverityLevel(16);
               if (var2) {
                  break;
               }
            case 8:
               this._customizer.setSubsystemNames(new String[0]);
               if (var2) {
                  break;
               }
            case 9:
               this._customizer.setUserIds(new String[0]);
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
      return "DomainLogFilter";
   }

   public void putValue(String var1, Object var2) {
      if (var1.equals("Delegate")) {
         LogFilterMBean var7 = this._Delegate;
         this._Delegate = (LogFilterMBean)var2;
         this._postSet(10, var7, this._Delegate);
      } else if (var1.equals("Name")) {
         String var6 = this._Name;
         this._Name = (String)var2;
         this._postSet(2, var6, this._Name);
      } else if (var1.equals("SeverityLevel")) {
         int var5 = this._SeverityLevel;
         this._SeverityLevel = (Integer)var2;
         this._postSet(7, var5, this._SeverityLevel);
      } else {
         String[] var4;
         if (var1.equals("SubsystemNames")) {
            var4 = this._SubsystemNames;
            this._SubsystemNames = (String[])((String[])var2);
            this._postSet(8, var4, this._SubsystemNames);
         } else if (var1.equals("UserIds")) {
            var4 = this._UserIds;
            this._UserIds = (String[])((String[])var2);
            this._postSet(9, var4, this._UserIds);
         } else if (var1.equals("customizer")) {
            DomainLogFilter var3 = this._customizer;
            this._customizer = (DomainLogFilter)var2;
         } else {
            super.putValue(var1, var2);
         }
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("Delegate")) {
         return this._Delegate;
      } else if (var1.equals("Name")) {
         return this._Name;
      } else if (var1.equals("SeverityLevel")) {
         return new Integer(this._SeverityLevel);
      } else if (var1.equals("SubsystemNames")) {
         return this._SubsystemNames;
      } else if (var1.equals("UserIds")) {
         return this._UserIds;
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
               break;
            case 7:
               if (var1.equals("user-id")) {
                  return 9;
               }
               break;
            case 8:
               if (var1.equals("delegate")) {
                  return 10;
               }
               break;
            case 14:
               if (var1.equals("severity-level")) {
                  return 7;
               }

               if (var1.equals("subsystem-name")) {
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
            case 2:
               return "name";
            case 3:
            case 4:
            case 5:
            case 6:
            default:
               return super.getElementName(var1);
            case 7:
               return "severity-level";
            case 8:
               return "subsystem-name";
            case 9:
               return "user-id";
            case 10:
               return "delegate";
         }
      }

      public boolean isArray(int var1) {
         switch (var1) {
            case 8:
               return true;
            case 9:
               return true;
            default:
               return super.isArray(var1);
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

   protected static class Helper extends ConfigurationMBeanImpl.Helper {
      private DomainLogFilterMBeanImpl bean;

      protected Helper(DomainLogFilterMBeanImpl var1) {
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
               return "SeverityLevel";
            case 8:
               return "SubsystemNames";
            case 9:
               return "UserIds";
            case 10:
               return "Delegate";
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("Delegate")) {
            return 10;
         } else if (var1.equals("Name")) {
            return 2;
         } else if (var1.equals("SeverityLevel")) {
            return 7;
         } else if (var1.equals("SubsystemNames")) {
            return 8;
         } else {
            return var1.equals("UserIds") ? 9 : super.getPropertyIndex(var1);
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
            if (this.bean.isDelegateSet()) {
               var2.append("Delegate");
               var2.append(String.valueOf(this.bean.getDelegate()));
            }

            if (this.bean.isNameSet()) {
               var2.append("Name");
               var2.append(String.valueOf(this.bean.getName()));
            }

            if (this.bean.isSeverityLevelSet()) {
               var2.append("SeverityLevel");
               var2.append(String.valueOf(this.bean.getSeverityLevel()));
            }

            if (this.bean.isSubsystemNamesSet()) {
               var2.append("SubsystemNames");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getSubsystemNames())));
            }

            if (this.bean.isUserIdsSet()) {
               var2.append("UserIds");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getUserIds())));
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
            DomainLogFilterMBeanImpl var2 = (DomainLogFilterMBeanImpl)var1;
            this.computeDiff("Name", this.bean.getName(), var2.getName(), false);
            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("SeverityLevel", this.bean.getSeverityLevel(), var2.getSeverityLevel(), true);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("SubsystemNames", this.bean.getSubsystemNames(), var2.getSubsystemNames(), true);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("UserIds", this.bean.getUserIds(), var2.getUserIds(), true);
            }

         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            DomainLogFilterMBeanImpl var3 = (DomainLogFilterMBeanImpl)var1.getSourceBean();
            DomainLogFilterMBeanImpl var4 = (DomainLogFilterMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (!var5.equals("Delegate")) {
                  if (var5.equals("Name")) {
                     var3.setName(var4.getName());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 2);
                  } else if (var5.equals("SeverityLevel")) {
                     var3.setSeverityLevel(var4.getSeverityLevel());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 7);
                  } else if (var5.equals("SubsystemNames")) {
                     var3.setSubsystemNames(var4.getSubsystemNames());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 8);
                  } else if (var5.equals("UserIds")) {
                     var3.setUserIds(var4.getUserIds());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 9);
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
            DomainLogFilterMBeanImpl var5 = (DomainLogFilterMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("Name")) && this.bean.isNameSet()) {
               var5.setName(this.bean.getName());
            }

            if (var2 && (var3 == null || !var3.contains("SeverityLevel")) && this.bean.isSeverityLevelSet()) {
               var5.setSeverityLevel(this.bean.getSeverityLevel());
            }

            String[] var4;
            if (var2 && (var3 == null || !var3.contains("SubsystemNames")) && this.bean.isSubsystemNamesSet()) {
               var4 = this.bean.getSubsystemNames();
               var5.setSubsystemNames(var4 == null ? null : (String[])((String[])((String[])((String[])var4)).clone()));
            }

            if (var2 && (var3 == null || !var3.contains("UserIds")) && this.bean.isUserIdsSet()) {
               var4 = this.bean.getUserIds();
               var5.setUserIds(var4 == null ? null : (String[])((String[])((String[])((String[])var4)).clone()));
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
         this.inferSubTree(this.bean.getDelegate(), var1, var2);
      }
   }
}
