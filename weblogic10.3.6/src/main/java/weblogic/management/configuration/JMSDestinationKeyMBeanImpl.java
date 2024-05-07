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
import weblogic.descriptor.BootstrapProperties;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBeanHelper;
import weblogic.descriptor.internal.Munger;
import weblogic.descriptor.internal.SchemaHelper;
import weblogic.j2ee.descriptor.wl.DestinationKeyBean;
import weblogic.management.DistributedManagementException;
import weblogic.management.ManagementException;
import weblogic.management.mbeans.custom.JMSDestinationKey;
import weblogic.utils.collections.CombinedIterator;

public class JMSDestinationKeyMBeanImpl extends ConfigurationMBeanImpl implements JMSDestinationKeyMBean, Serializable {
   private String _Direction;
   private String _KeyType;
   private String _Name;
   private String _Notes;
   private String _Property;
   private JMSDestinationKey _customizer;
   private static SchemaHelper2 _schemaHelper;

   public JMSDestinationKeyMBeanImpl() {
      try {
         this._customizer = new JMSDestinationKey(this);
      } catch (Exception var2) {
         if (var2 instanceof RuntimeException) {
            throw (RuntimeException)var2;
         }

         throw new UndeclaredThrowableException(var2);
      }

      this._initializeProperty(-1);
   }

   public JMSDestinationKeyMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);

      try {
         this._customizer = new JMSDestinationKey(this);
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

   public String getProperty() {
      return this._customizer.getProperty();
   }

   public boolean isNameSet() {
      return this._isSet(2);
   }

   public boolean isPropertySet() {
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

   public void setProperty(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      weblogic.descriptor.beangen.LegalChecks.checkNonEmptyString("Property", var1);
      weblogic.descriptor.beangen.LegalChecks.checkNonNull("Property", var1);
      String var2 = this.getProperty();
      this._customizer.setProperty(var1);
      this._postSet(7, var2, var1);
   }

   public String getKeyType() {
      return this._customizer.getKeyType();
   }

   public String getNotes() {
      return this._customizer.getNotes();
   }

   public boolean isKeyTypeSet() {
      return this._isSet(8);
   }

   public boolean isNotesSet() {
      return this._isSet(3);
   }

   public void setKeyType(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{"Boolean", "Byte", "Short", "Int", "Long", "Float", "Double", "String"};
      var1 = weblogic.descriptor.beangen.LegalChecks.checkInEnum("KeyType", var1, var2);
      String var3 = this.getKeyType();
      this._customizer.setKeyType(var1);
      this._postSet(8, var3, var1);
   }

   public void setNotes(String var1) throws InvalidAttributeValueException, DistributedManagementException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this.getNotes();
      this._customizer.setNotes(var1);
      this._postSet(3, var2, var1);
   }

   public String getDirection() {
      return this._customizer.getDirection();
   }

   public boolean isDirectionSet() {
      return this._isSet(9);
   }

   public void setDirection(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{"Ascending", "Descending"};
      var1 = weblogic.descriptor.beangen.LegalChecks.checkInEnum("Direction", var1, var2);
      String var3 = this.getDirection();
      this._customizer.setDirection(var1);
      this._postSet(9, var3, var1);
   }

   public void useDelegates(DestinationKeyBean var1) {
      this._customizer.useDelegates(var1);
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
      weblogic.descriptor.beangen.LegalChecks.checkIsSet("Property", this.isPropertySet());
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
         var1 = 9;
      }

      try {
         switch (var1) {
            case 9:
               this._customizer.setDirection("Ascending");
               if (var2) {
                  break;
               }
            case 8:
               this._customizer.setKeyType("String");
               if (var2) {
                  break;
               }
            case 2:
               this._customizer.setName((String)null);
               if (var2) {
                  break;
               }
            case 3:
               this._customizer.setNotes((String)null);
               if (var2) {
                  break;
               }
            case 7:
               this._customizer.setProperty((String)null);
               if (var2) {
                  break;
               }
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
      return "JMSDestinationKey";
   }

   public void putValue(String var1, Object var2) {
      String var4;
      if (var1.equals("Direction")) {
         var4 = this._Direction;
         this._Direction = (String)var2;
         this._postSet(9, var4, this._Direction);
      } else if (var1.equals("KeyType")) {
         var4 = this._KeyType;
         this._KeyType = (String)var2;
         this._postSet(8, var4, this._KeyType);
      } else if (var1.equals("Name")) {
         var4 = this._Name;
         this._Name = (String)var2;
         this._postSet(2, var4, this._Name);
      } else if (var1.equals("Notes")) {
         var4 = this._Notes;
         this._Notes = (String)var2;
         this._postSet(3, var4, this._Notes);
      } else if (var1.equals("Property")) {
         var4 = this._Property;
         this._Property = (String)var2;
         this._postSet(7, var4, this._Property);
      } else if (var1.equals("customizer")) {
         JMSDestinationKey var3 = this._customizer;
         this._customizer = (JMSDestinationKey)var2;
      } else {
         super.putValue(var1, var2);
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("Direction")) {
         return this._Direction;
      } else if (var1.equals("KeyType")) {
         return this._KeyType;
      } else if (var1.equals("Name")) {
         return this._Name;
      } else if (var1.equals("Notes")) {
         return this._Notes;
      } else if (var1.equals("Property")) {
         return this._Property;
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
            case 5:
               if (var1.equals("notes")) {
                  return 3;
               }
            case 6:
            case 7:
            default:
               break;
            case 8:
               if (var1.equals("key-type")) {
                  return 8;
               }

               if (var1.equals("property")) {
                  return 7;
               }
               break;
            case 9:
               if (var1.equals("direction")) {
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
               return "notes";
            case 4:
            case 5:
            case 6:
            default:
               return super.getElementName(var1);
            case 7:
               return "property";
            case 8:
               return "key-type";
            case 9:
               return "direction";
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
      private JMSDestinationKeyMBeanImpl bean;

      protected Helper(JMSDestinationKeyMBeanImpl var1) {
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
            case 5:
            case 6:
            default:
               return super.getPropertyName(var1);
            case 7:
               return "Property";
            case 8:
               return "KeyType";
            case 9:
               return "Direction";
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("Direction")) {
            return 9;
         } else if (var1.equals("KeyType")) {
            return 8;
         } else if (var1.equals("Name")) {
            return 2;
         } else if (var1.equals("Notes")) {
            return 3;
         } else {
            return var1.equals("Property") ? 7 : super.getPropertyIndex(var1);
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
            if (this.bean.isDirectionSet()) {
               var2.append("Direction");
               var2.append(String.valueOf(this.bean.getDirection()));
            }

            if (this.bean.isKeyTypeSet()) {
               var2.append("KeyType");
               var2.append(String.valueOf(this.bean.getKeyType()));
            }

            if (this.bean.isNameSet()) {
               var2.append("Name");
               var2.append(String.valueOf(this.bean.getName()));
            }

            if (this.bean.isNotesSet()) {
               var2.append("Notes");
               var2.append(String.valueOf(this.bean.getNotes()));
            }

            if (this.bean.isPropertySet()) {
               var2.append("Property");
               var2.append(String.valueOf(this.bean.getProperty()));
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
            JMSDestinationKeyMBeanImpl var2 = (JMSDestinationKeyMBeanImpl)var1;
            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("Direction", this.bean.getDirection(), var2.getDirection(), false);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("KeyType", this.bean.getKeyType(), var2.getKeyType(), false);
            }

            this.computeDiff("Name", this.bean.getName(), var2.getName(), false);
            this.computeDiff("Notes", this.bean.getNotes(), var2.getNotes(), true);
            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("Property", this.bean.getProperty(), var2.getProperty(), false);
            }

         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            JMSDestinationKeyMBeanImpl var3 = (JMSDestinationKeyMBeanImpl)var1.getSourceBean();
            JMSDestinationKeyMBeanImpl var4 = (JMSDestinationKeyMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("Direction")) {
                  var3.setDirection(var4.getDirection());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 9);
               } else if (var5.equals("KeyType")) {
                  var3.setKeyType(var4.getKeyType());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 8);
               } else if (var5.equals("Name")) {
                  var3.setName(var4.getName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 2);
               } else if (var5.equals("Notes")) {
                  var3.setNotes(var4.getNotes());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 3);
               } else if (var5.equals("Property")) {
                  var3.setProperty(var4.getProperty());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 7);
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
            JMSDestinationKeyMBeanImpl var5 = (JMSDestinationKeyMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if (var2 && (var3 == null || !var3.contains("Direction")) && this.bean.isDirectionSet()) {
               var5.setDirection(this.bean.getDirection());
            }

            if (var2 && (var3 == null || !var3.contains("KeyType")) && this.bean.isKeyTypeSet()) {
               var5.setKeyType(this.bean.getKeyType());
            }

            if ((var3 == null || !var3.contains("Name")) && this.bean.isNameSet()) {
               var5.setName(this.bean.getName());
            }

            if ((var3 == null || !var3.contains("Notes")) && this.bean.isNotesSet()) {
               var5.setNotes(this.bean.getNotes());
            }

            if (var2 && (var3 == null || !var3.contains("Property")) && this.bean.isPropertySet()) {
               var5.setProperty(this.bean.getProperty());
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
