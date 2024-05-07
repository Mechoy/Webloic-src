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
import weblogic.j2ee.descriptor.wl.DistributedDestinationMemberBean;
import weblogic.management.DistributedManagementException;
import weblogic.management.ManagementException;
import weblogic.management.mbeans.custom.JMSDistributedTopicMember;
import weblogic.utils.collections.CombinedIterator;

public class JMSDistributedTopicMemberMBeanImpl extends JMSDistributedDestinationMemberMBeanImpl implements JMSDistributedTopicMemberMBean, Serializable {
   private JMSTopicMBean _JMSTopic;
   private String _Name;
   private String _Notes;
   private int _Weight;
   private JMSDistributedTopicMember _customizer;
   private static SchemaHelper2 _schemaHelper;

   public JMSDistributedTopicMemberMBeanImpl() {
      try {
         this._customizer = new JMSDistributedTopicMember(this);
      } catch (Exception var2) {
         if (var2 instanceof RuntimeException) {
            throw (RuntimeException)var2;
         }

         throw new UndeclaredThrowableException(var2);
      }

      this._initializeProperty(-1);
   }

   public JMSDistributedTopicMemberMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);

      try {
         this._customizer = new JMSDistributedTopicMember(this);
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         }

         throw new UndeclaredThrowableException(var4);
      }

      this._initializeProperty(-1);
   }

   public JMSTopicMBean getJMSTopic() {
      return this._customizer.getJMSTopic();
   }

   public String getJMSTopicAsString() {
      AbstractDescriptorBean var1 = (AbstractDescriptorBean)this.getJMSTopic();
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

   public int getWeight() {
      return this._customizer.getWeight();
   }

   public boolean isJMSTopicSet() {
      return this._isSet(8);
   }

   public boolean isNameSet() {
      return this._isSet(2);
   }

   public boolean isWeightSet() {
      return this._isSet(7);
   }

   public void setJMSTopicAsString(String var1) {
      if (var1 != null && var1.length() != 0) {
         var1 = var1 == null ? null : var1.trim();
         this._getReferenceManager().registerUnresolvedReference(var1, JMSTopicMBean.class, new ReferenceManager.Resolver(this, 8) {
            public void resolveReference(Object var1) {
               try {
                  JMSDistributedTopicMemberMBeanImpl.this.setJMSTopic((JMSTopicMBean)var1);
               } catch (RuntimeException var3) {
                  throw var3;
               } catch (Exception var4) {
                  throw new AssertionError("Impossible exception: " + var4);
               }
            }
         });
      } else {
         JMSTopicMBean var2 = this._JMSTopic;
         this._initializeProperty(8);
         this._postSet(8, var2, this._JMSTopic);
      }

   }

   public void setJMSTopic(JMSTopicMBean var1) throws InvalidAttributeValueException {
      if (var1 != null) {
         ResolvedReference var2 = new ResolvedReference(this, 8, (AbstractDescriptorBean)var1) {
            protected Object getPropertyValue() {
               return JMSDistributedTopicMemberMBeanImpl.this.getJMSTopic();
            }
         };
         this._getReferenceManager().registerResolvedReference((AbstractDescriptorBean)var1, var2);
      }

      JMSTopicMBean var3 = this.getJMSTopic();
      this._customizer.setJMSTopic(var1);
      this._postSet(8, var3, var1);
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

   public void setWeight(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkMin("Weight", var1, 1);
      int var2 = this.getWeight();
      this._customizer.setWeight(var1);
      this._postSet(7, var2, var1);
   }

   public String getNotes() {
      return this._customizer.getNotes();
   }

   public boolean isNotesSet() {
      return this._isSet(3);
   }

   public void useDelegates(DomainMBean var1, DistributedDestinationMemberBean var2) {
      this._customizer.useDelegates(var1, var2);
   }

   public void setNotes(String var1) throws InvalidAttributeValueException, DistributedManagementException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this.getNotes();
      this._customizer.setNotes(var1);
      this._postSet(3, var2, var1);
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
         var1 = 8;
      }

      try {
         switch (var1) {
            case 8:
               this._customizer.setJMSTopic((JMSTopicMBean)null);
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
               this._customizer.setWeight(1);
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
      return "JMSDistributedTopicMember";
   }

   public void putValue(String var1, Object var2) {
      if (var1.equals("JMSTopic")) {
         JMSTopicMBean var6 = this._JMSTopic;
         this._JMSTopic = (JMSTopicMBean)var2;
         this._postSet(8, var6, this._JMSTopic);
      } else {
         String var5;
         if (var1.equals("Name")) {
            var5 = this._Name;
            this._Name = (String)var2;
            this._postSet(2, var5, this._Name);
         } else if (var1.equals("Notes")) {
            var5 = this._Notes;
            this._Notes = (String)var2;
            this._postSet(3, var5, this._Notes);
         } else if (var1.equals("Weight")) {
            int var4 = this._Weight;
            this._Weight = (Integer)var2;
            this._postSet(7, var4, this._Weight);
         } else if (var1.equals("customizer")) {
            JMSDistributedTopicMember var3 = this._customizer;
            this._customizer = (JMSDistributedTopicMember)var2;
         } else {
            super.putValue(var1, var2);
         }
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("JMSTopic")) {
         return this._JMSTopic;
      } else if (var1.equals("Name")) {
         return this._Name;
      } else if (var1.equals("Notes")) {
         return this._Notes;
      } else if (var1.equals("Weight")) {
         return new Integer(this._Weight);
      } else {
         return var1.equals("customizer") ? this._customizer : super.getValue(var1);
      }
   }

   public static class SchemaHelper2 extends JMSDistributedDestinationMemberMBeanImpl.SchemaHelper2 implements SchemaHelper {
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
            case 6:
               if (var1.equals("weight")) {
                  return 7;
               }
            case 7:
            case 8:
            default:
               break;
            case 9:
               if (var1.equals("jms-topic")) {
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
               return "notes";
            case 4:
            case 5:
            case 6:
            default:
               return super.getElementName(var1);
            case 7:
               return "weight";
            case 8:
               return "jms-topic";
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

   protected static class Helper extends JMSDistributedDestinationMemberMBeanImpl.Helper {
      private JMSDistributedTopicMemberMBeanImpl bean;

      protected Helper(JMSDistributedTopicMemberMBeanImpl var1) {
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
               return "Weight";
            case 8:
               return "JMSTopic";
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("JMSTopic")) {
            return 8;
         } else if (var1.equals("Name")) {
            return 2;
         } else if (var1.equals("Notes")) {
            return 3;
         } else {
            return var1.equals("Weight") ? 7 : super.getPropertyIndex(var1);
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
            if (this.bean.isJMSTopicSet()) {
               var2.append("JMSTopic");
               var2.append(String.valueOf(this.bean.getJMSTopic()));
            }

            if (this.bean.isNameSet()) {
               var2.append("Name");
               var2.append(String.valueOf(this.bean.getName()));
            }

            if (this.bean.isNotesSet()) {
               var2.append("Notes");
               var2.append(String.valueOf(this.bean.getNotes()));
            }

            if (this.bean.isWeightSet()) {
               var2.append("Weight");
               var2.append(String.valueOf(this.bean.getWeight()));
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
            JMSDistributedTopicMemberMBeanImpl var2 = (JMSDistributedTopicMemberMBeanImpl)var1;
            this.computeDiff("JMSTopic", this.bean.getJMSTopic(), var2.getJMSTopic(), true);
            this.computeDiff("Name", this.bean.getName(), var2.getName(), false);
            this.computeDiff("Notes", this.bean.getNotes(), var2.getNotes(), true);
            this.computeDiff("Weight", this.bean.getWeight(), var2.getWeight(), true);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            JMSDistributedTopicMemberMBeanImpl var3 = (JMSDistributedTopicMemberMBeanImpl)var1.getSourceBean();
            JMSDistributedTopicMemberMBeanImpl var4 = (JMSDistributedTopicMemberMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("JMSTopic")) {
                  var3.setJMSTopicAsString(var4.getJMSTopicAsString());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 8);
               } else if (var5.equals("Name")) {
                  var3.setName(var4.getName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 2);
               } else if (var5.equals("Notes")) {
                  var3.setNotes(var4.getNotes());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 3);
               } else if (var5.equals("Weight")) {
                  var3.setWeight(var4.getWeight());
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
            JMSDistributedTopicMemberMBeanImpl var5 = (JMSDistributedTopicMemberMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("JMSTopic")) && this.bean.isJMSTopicSet()) {
               var5._unSet(var5, 8);
               var5.setJMSTopicAsString(this.bean.getJMSTopicAsString());
            }

            if ((var3 == null || !var3.contains("Name")) && this.bean.isNameSet()) {
               var5.setName(this.bean.getName());
            }

            if ((var3 == null || !var3.contains("Notes")) && this.bean.isNotesSet()) {
               var5.setNotes(this.bean.getNotes());
            }

            if ((var3 == null || !var3.contains("Weight")) && this.bean.isWeightSet()) {
               var5.setWeight(this.bean.getWeight());
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
         this.inferSubTree(this.bean.getJMSTopic(), var1, var2);
      }
   }
}
