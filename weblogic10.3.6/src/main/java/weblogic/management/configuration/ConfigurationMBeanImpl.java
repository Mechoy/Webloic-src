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
import weblogic.descriptor.internal.AbstractSchemaHelper2;
import weblogic.descriptor.internal.Munger;
import weblogic.descriptor.internal.SchemaHelper;
import weblogic.management.DistributedManagementException;
import weblogic.management.ManagementException;
import weblogic.management.WebLogicMBeanImpl;
import weblogic.management.provider.custom.ConfigurationMBeanCustomizer;
import weblogic.utils.collections.CombinedIterator;

public class ConfigurationMBeanImpl extends WebLogicMBeanImpl implements ConfigurationMBean, Serializable {
   private String _Comments;
   private boolean _DefaultedMBean;
   private String _Name;
   private String _Notes;
   private boolean _PersistenceEnabled;
   private ConfigurationMBeanCustomizer _customizer;
   private static SchemaHelper2 _schemaHelper;

   public ConfigurationMBeanImpl() {
      try {
         this._customizer = new ConfigurationMBeanCustomizer(this);
      } catch (Exception var2) {
         if (var2 instanceof RuntimeException) {
            throw (RuntimeException)var2;
         }

         throw new UndeclaredThrowableException(var2);
      }

      this._initializeProperty(-1);
   }

   public ConfigurationMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);

      try {
         this._customizer = new ConfigurationMBeanCustomizer(this);
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

   public void setName(String var1) throws InvalidAttributeValueException, ManagementException {
      var1 = var1 == null ? null : var1.trim();
      weblogic.descriptor.beangen.LegalChecks.checkNonEmptyString("Name", var1);
      weblogic.descriptor.beangen.LegalChecks.checkNonNull("Name", var1);
      ConfigurationValidator.validateName(var1);
      String var2 = this.getName();
      this._customizer.setName(var1);
      this._postSet(2, var2, var1);
   }

   public String getNotes() {
      return this._Notes;
   }

   public boolean isNotesSet() {
      return this._isSet(3);
   }

   public void setNotes(String var1) throws InvalidAttributeValueException, DistributedManagementException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._Notes;
      this._Notes = var1;
      this._postSet(3, var2, var1);
   }

   public boolean isPersistenceEnabled() {
      return this._PersistenceEnabled;
   }

   public boolean isPersistenceEnabledSet() {
      return this._isSet(4);
   }

   public void setPersistenceEnabled(boolean var1) {
      this._PersistenceEnabled = var1;
   }

   public boolean isDefaultedMBean() {
      return this._DefaultedMBean;
   }

   public boolean isDefaultedMBeanSet() {
      return this._isSet(5);
   }

   public void setDefaultedMBean(boolean var1) {
      this._DefaultedMBean = var1;
   }

   public String getComments() {
      return this._Comments;
   }

   public boolean isCommentsSet() {
      return this._isSet(6);
   }

   public void setComments(String var1) {
      var1 = var1 == null ? null : var1.trim();
      this._Comments = var1;
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
         var1 = 6;
      }

      try {
         switch (var1) {
            case 6:
               this._Comments = null;
               if (var2) {
                  break;
               }
            case 2:
               this._customizer.setName((String)null);
               if (var2) {
                  break;
               }
            case 3:
               this._Notes = null;
               if (var2) {
                  break;
               }
            case 5:
               this._DefaultedMBean = false;
               if (var2) {
                  break;
               }
            case 4:
               this._PersistenceEnabled = true;
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
      return "Configuration";
   }

   public void putValue(String var1, Object var2) {
      String var5;
      if (var1.equals("Comments")) {
         var5 = this._Comments;
         this._Comments = (String)var2;
         this._postSet(6, var5, this._Comments);
      } else {
         boolean var4;
         if (var1.equals("DefaultedMBean")) {
            var4 = this._DefaultedMBean;
            this._DefaultedMBean = (Boolean)var2;
            this._postSet(5, var4, this._DefaultedMBean);
         } else if (var1.equals("Name")) {
            var5 = this._Name;
            this._Name = (String)var2;
            this._postSet(2, var5, this._Name);
         } else if (var1.equals("Notes")) {
            var5 = this._Notes;
            this._Notes = (String)var2;
            this._postSet(3, var5, this._Notes);
         } else if (var1.equals("PersistenceEnabled")) {
            var4 = this._PersistenceEnabled;
            this._PersistenceEnabled = (Boolean)var2;
            this._postSet(4, var4, this._PersistenceEnabled);
         } else if (var1.equals("customizer")) {
            ConfigurationMBeanCustomizer var3 = this._customizer;
            this._customizer = (ConfigurationMBeanCustomizer)var2;
         } else {
            super.putValue(var1, var2);
         }
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("Comments")) {
         return this._Comments;
      } else if (var1.equals("DefaultedMBean")) {
         return new Boolean(this._DefaultedMBean);
      } else if (var1.equals("Name")) {
         return this._Name;
      } else if (var1.equals("Notes")) {
         return this._Notes;
      } else if (var1.equals("PersistenceEnabled")) {
         return new Boolean(this._PersistenceEnabled);
      } else {
         return var1.equals("customizer") ? this._customizer : super.getValue(var1);
      }
   }

   public static class SchemaHelper2 extends AbstractSchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 4:
               if (var1.equals("name")) {
                  return 2;
               }
               break;
            case 5:
               if (var1.equals("notes")) {
                  return 3;
               }
               break;
            case 8:
               if (var1.equals("comments")) {
                  return 6;
               }
               break;
            case 15:
               if (var1.equals("defaultedm-bean")) {
                  return 5;
               }
               break;
            case 19:
               if (var1.equals("persistence-enabled")) {
                  return 4;
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
               return "notes";
            case 4:
               return "persistence-enabled";
            case 5:
               return "defaultedm-bean";
            case 6:
               return "comments";
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

      public boolean hasKey() {
         return true;
      }

      public String[] getKeyElementNames() {
         ArrayList var1 = new ArrayList();
         var1.add("name");
         return (String[])((String[])var1.toArray(new String[0]));
      }
   }

   protected static class Helper extends WebLogicMBeanImpl.Helper {
      private ConfigurationMBeanImpl bean;

      protected Helper(ConfigurationMBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 2:
               return "Name";
            case 3:
               return "Notes";
            case 4:
               return "PersistenceEnabled";
            case 5:
               return "DefaultedMBean";
            case 6:
               return "Comments";
            default:
               return super.getPropertyName(var1);
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("Comments")) {
            return 6;
         } else if (var1.equals("Name")) {
            return 2;
         } else if (var1.equals("Notes")) {
            return 3;
         } else if (var1.equals("DefaultedMBean")) {
            return 5;
         } else {
            return var1.equals("PersistenceEnabled") ? 4 : super.getPropertyIndex(var1);
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
            if (this.bean.isCommentsSet()) {
               var2.append("Comments");
               var2.append(String.valueOf(this.bean.getComments()));
            }

            if (this.bean.isNameSet()) {
               var2.append("Name");
               var2.append(String.valueOf(this.bean.getName()));
            }

            if (this.bean.isNotesSet()) {
               var2.append("Notes");
               var2.append(String.valueOf(this.bean.getNotes()));
            }

            if (this.bean.isDefaultedMBeanSet()) {
               var2.append("DefaultedMBean");
               var2.append(String.valueOf(this.bean.isDefaultedMBean()));
            }

            if (this.bean.isPersistenceEnabledSet()) {
               var2.append("PersistenceEnabled");
               var2.append(String.valueOf(this.bean.isPersistenceEnabled()));
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
            ConfigurationMBeanImpl var2 = (ConfigurationMBeanImpl)var1;
            this.computeDiff("Name", this.bean.getName(), var2.getName(), false);
            this.computeDiff("Notes", this.bean.getNotes(), var2.getNotes(), true);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            ConfigurationMBeanImpl var3 = (ConfigurationMBeanImpl)var1.getSourceBean();
            ConfigurationMBeanImpl var4 = (ConfigurationMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (!var5.equals("Comments")) {
                  if (var5.equals("Name")) {
                     var3.setName(var4.getName());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 2);
                  } else if (var5.equals("Notes")) {
                     var3.setNotes(var4.getNotes());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 3);
                  } else if (!var5.equals("DefaultedMBean") && !var5.equals("PersistenceEnabled")) {
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
            ConfigurationMBeanImpl var5 = (ConfigurationMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("Name")) && this.bean.isNameSet()) {
               var5.setName(this.bean.getName());
            }

            if ((var3 == null || !var3.contains("Notes")) && this.bean.isNotesSet()) {
               var5.setNotes(this.bean.getNotes());
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
