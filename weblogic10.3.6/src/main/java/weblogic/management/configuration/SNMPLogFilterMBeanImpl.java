package weblogic.management.configuration;

import java.io.Serializable;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.Arrays;
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
import weblogic.utils.ArrayUtils;
import weblogic.utils.collections.CombinedIterator;

public class SNMPLogFilterMBeanImpl extends SNMPTrapSourceMBeanImpl implements SNMPLogFilterMBean, Serializable {
   private String[] _MessageIds;
   private String _MessageSubstring;
   private String _SeverityLevel;
   private String[] _SubsystemNames;
   private String[] _UserIds;
   private static SchemaHelper2 _schemaHelper;

   public SNMPLogFilterMBeanImpl() {
      this._initializeProperty(-1);
   }

   public SNMPLogFilterMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);
      this._initializeProperty(-1);
   }

   public String getSeverityLevel() {
      return this._SeverityLevel;
   }

   public boolean isSeverityLevelSet() {
      return this._isSet(8);
   }

   public void setSeverityLevel(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._SeverityLevel;
      this._SeverityLevel = var1;
      this._postSet(8, var2, var1);
   }

   public String[] getSubsystemNames() {
      return this._SubsystemNames;
   }

   public boolean isSubsystemNamesSet() {
      return this._isSet(9);
   }

   public void setSubsystemNames(String[] var1) {
      var1 = var1 == null ? new String[0] : var1;
      var1 = this._getHelper()._trimElements(var1);
      String[] var2 = this._SubsystemNames;
      this._SubsystemNames = var1;
      this._postSet(9, var2, var1);
   }

   public boolean addSubsystemName(String var1) throws InvalidAttributeValueException, ConfigurationException {
      this._getHelper()._ensureNonNull(var1);
      String[] var2;
      if (this._isSet(9)) {
         var2 = (String[])((String[])this._getHelper()._extendArray(this.getSubsystemNames(), String.class, var1));
      } else {
         var2 = new String[]{var1};
      }

      try {
         this.setSubsystemNames(var2);
         return true;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else if (var4 instanceof InvalidAttributeValueException) {
            throw (InvalidAttributeValueException)var4;
         } else if (var4 instanceof ConfigurationException) {
            throw (ConfigurationException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public boolean removeSubsystemName(String var1) throws InvalidAttributeValueException, ConfigurationException {
      String[] var2 = this.getSubsystemNames();
      String[] var3 = (String[])((String[])this._getHelper()._removeElement(var2, String.class, var1));
      if (var3.length != var2.length) {
         try {
            this.setSubsystemNames(var3);
            return true;
         } catch (Exception var5) {
            if (var5 instanceof RuntimeException) {
               throw (RuntimeException)var5;
            } else if (var5 instanceof InvalidAttributeValueException) {
               throw (InvalidAttributeValueException)var5;
            } else if (var5 instanceof ConfigurationException) {
               throw (ConfigurationException)var5;
            } else {
               throw new UndeclaredThrowableException(var5);
            }
         }
      } else {
         return false;
      }
   }

   public String[] getUserIds() {
      return this._UserIds;
   }

   public boolean isUserIdsSet() {
      return this._isSet(10);
   }

   public void setUserIds(String[] var1) {
      var1 = var1 == null ? new String[0] : var1;
      var1 = this._getHelper()._trimElements(var1);
      String[] var2 = this._UserIds;
      this._UserIds = var1;
      this._postSet(10, var2, var1);
   }

   public boolean addUserId(String var1) throws InvalidAttributeValueException, ConfigurationException {
      this._getHelper()._ensureNonNull(var1);
      String[] var2;
      if (this._isSet(10)) {
         var2 = (String[])((String[])this._getHelper()._extendArray(this.getUserIds(), String.class, var1));
      } else {
         var2 = new String[]{var1};
      }

      try {
         this.setUserIds(var2);
         return true;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else if (var4 instanceof InvalidAttributeValueException) {
            throw (InvalidAttributeValueException)var4;
         } else if (var4 instanceof ConfigurationException) {
            throw (ConfigurationException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public boolean removeUserId(String var1) throws InvalidAttributeValueException, ConfigurationException {
      String[] var2 = this.getUserIds();
      String[] var3 = (String[])((String[])this._getHelper()._removeElement(var2, String.class, var1));
      if (var3.length != var2.length) {
         try {
            this.setUserIds(var3);
            return true;
         } catch (Exception var5) {
            if (var5 instanceof RuntimeException) {
               throw (RuntimeException)var5;
            } else if (var5 instanceof InvalidAttributeValueException) {
               throw (InvalidAttributeValueException)var5;
            } else if (var5 instanceof ConfigurationException) {
               throw (ConfigurationException)var5;
            } else {
               throw new UndeclaredThrowableException(var5);
            }
         }
      } else {
         return false;
      }
   }

   public String[] getMessageIds() {
      return this._MessageIds;
   }

   public boolean isMessageIdsSet() {
      return this._isSet(11);
   }

   public void setMessageIds(String[] var1) {
      var1 = var1 == null ? new String[0] : var1;
      var1 = this._getHelper()._trimElements(var1);
      String[] var2 = this._MessageIds;
      this._MessageIds = var1;
      this._postSet(11, var2, var1);
   }

   public boolean addMessageId(String var1) throws InvalidAttributeValueException, ConfigurationException {
      this._getHelper()._ensureNonNull(var1);
      String[] var2;
      if (this._isSet(11)) {
         var2 = (String[])((String[])this._getHelper()._extendArray(this.getMessageIds(), String.class, var1));
      } else {
         var2 = new String[]{var1};
      }

      try {
         this.setMessageIds(var2);
         return true;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else if (var4 instanceof InvalidAttributeValueException) {
            throw (InvalidAttributeValueException)var4;
         } else if (var4 instanceof ConfigurationException) {
            throw (ConfigurationException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public boolean removeMessageId(String var1) throws InvalidAttributeValueException, ConfigurationException {
      String[] var2 = this.getMessageIds();
      String[] var3 = (String[])((String[])this._getHelper()._removeElement(var2, String.class, var1));
      if (var3.length != var2.length) {
         try {
            this.setMessageIds(var3);
            return true;
         } catch (Exception var5) {
            if (var5 instanceof RuntimeException) {
               throw (RuntimeException)var5;
            } else if (var5 instanceof InvalidAttributeValueException) {
               throw (InvalidAttributeValueException)var5;
            } else if (var5 instanceof ConfigurationException) {
               throw (ConfigurationException)var5;
            } else {
               throw new UndeclaredThrowableException(var5);
            }
         }
      } else {
         return false;
      }
   }

   public String getMessageSubstring() {
      return this._MessageSubstring;
   }

   public boolean isMessageSubstringSet() {
      return this._isSet(12);
   }

   public void setMessageSubstring(String var1) throws InvalidAttributeValueException, ConfigurationException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._MessageSubstring;
      this._MessageSubstring = var1;
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
         var1 = 11;
      }

      try {
         switch (var1) {
            case 11:
               this._MessageIds = new String[0];
               if (var2) {
                  break;
               }
            case 12:
               this._MessageSubstring = null;
               if (var2) {
                  break;
               }
            case 8:
               this._SeverityLevel = "Notice";
               if (var2) {
                  break;
               }
            case 9:
               this._SubsystemNames = new String[0];
               if (var2) {
                  break;
               }
            case 10:
               this._UserIds = new String[0];
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
      return "SNMPLogFilter";
   }

   public void putValue(String var1, Object var2) {
      String[] var3;
      if (var1.equals("MessageIds")) {
         var3 = this._MessageIds;
         this._MessageIds = (String[])((String[])var2);
         this._postSet(11, var3, this._MessageIds);
      } else {
         String var4;
         if (var1.equals("MessageSubstring")) {
            var4 = this._MessageSubstring;
            this._MessageSubstring = (String)var2;
            this._postSet(12, var4, this._MessageSubstring);
         } else if (var1.equals("SeverityLevel")) {
            var4 = this._SeverityLevel;
            this._SeverityLevel = (String)var2;
            this._postSet(8, var4, this._SeverityLevel);
         } else if (var1.equals("SubsystemNames")) {
            var3 = this._SubsystemNames;
            this._SubsystemNames = (String[])((String[])var2);
            this._postSet(9, var3, this._SubsystemNames);
         } else if (var1.equals("UserIds")) {
            var3 = this._UserIds;
            this._UserIds = (String[])((String[])var2);
            this._postSet(10, var3, this._UserIds);
         } else {
            super.putValue(var1, var2);
         }
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("MessageIds")) {
         return this._MessageIds;
      } else if (var1.equals("MessageSubstring")) {
         return this._MessageSubstring;
      } else if (var1.equals("SeverityLevel")) {
         return this._SeverityLevel;
      } else if (var1.equals("SubsystemNames")) {
         return this._SubsystemNames;
      } else {
         return var1.equals("UserIds") ? this._UserIds : super.getValue(var1);
      }
   }

   public static class SchemaHelper2 extends SNMPTrapSourceMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 7:
               if (var1.equals("user-id")) {
                  return 10;
               }
               break;
            case 10:
               if (var1.equals("message-id")) {
                  return 11;
               }
               break;
            case 14:
               if (var1.equals("severity-level")) {
                  return 8;
               }

               if (var1.equals("subsystem-name")) {
                  return 9;
               }
               break;
            case 17:
               if (var1.equals("message-substring")) {
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
            case 8:
               return "severity-level";
            case 9:
               return "subsystem-name";
            case 10:
               return "user-id";
            case 11:
               return "message-id";
            case 12:
               return "message-substring";
            default:
               return super.getElementName(var1);
         }
      }

      public boolean isArray(int var1) {
         switch (var1) {
            case 7:
               return true;
            case 8:
            default:
               return super.isArray(var1);
            case 9:
               return true;
            case 10:
               return true;
            case 11:
               return true;
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

      public String[] getKeyElementNames() {
         ArrayList var1 = new ArrayList();
         var1.add("name");
         return (String[])((String[])var1.toArray(new String[0]));
      }
   }

   protected static class Helper extends SNMPTrapSourceMBeanImpl.Helper {
      private SNMPLogFilterMBeanImpl bean;

      protected Helper(SNMPLogFilterMBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 8:
               return "SeverityLevel";
            case 9:
               return "SubsystemNames";
            case 10:
               return "UserIds";
            case 11:
               return "MessageIds";
            case 12:
               return "MessageSubstring";
            default:
               return super.getPropertyName(var1);
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("MessageIds")) {
            return 11;
         } else if (var1.equals("MessageSubstring")) {
            return 12;
         } else if (var1.equals("SeverityLevel")) {
            return 8;
         } else if (var1.equals("SubsystemNames")) {
            return 9;
         } else {
            return var1.equals("UserIds") ? 10 : super.getPropertyIndex(var1);
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
            if (this.bean.isMessageIdsSet()) {
               var2.append("MessageIds");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getMessageIds())));
            }

            if (this.bean.isMessageSubstringSet()) {
               var2.append("MessageSubstring");
               var2.append(String.valueOf(this.bean.getMessageSubstring()));
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
            SNMPLogFilterMBeanImpl var2 = (SNMPLogFilterMBeanImpl)var1;
            this.computeDiff("MessageIds", this.bean.getMessageIds(), var2.getMessageIds(), true);
            this.computeDiff("MessageSubstring", this.bean.getMessageSubstring(), var2.getMessageSubstring(), true);
            this.computeDiff("SeverityLevel", this.bean.getSeverityLevel(), var2.getSeverityLevel(), true);
            this.computeDiff("SubsystemNames", this.bean.getSubsystemNames(), var2.getSubsystemNames(), true);
            this.computeDiff("UserIds", this.bean.getUserIds(), var2.getUserIds(), true);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            SNMPLogFilterMBeanImpl var3 = (SNMPLogFilterMBeanImpl)var1.getSourceBean();
            SNMPLogFilterMBeanImpl var4 = (SNMPLogFilterMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("MessageIds")) {
                  if (var6 == 2) {
                     var2.resetAddedObject(var2.getAddedObject());
                     var3.addMessageId((String)var2.getAddedObject());
                  } else {
                     if (var6 != 3) {
                        throw new AssertionError("Invalid type: " + var6);
                     }

                     var3.removeMessageId((String)var2.getRemovedObject());
                  }

                  if (var3.getMessageIds() == null || var3.getMessageIds().length == 0) {
                     var3._conditionalUnset(var2.isUnsetUpdate(), 11);
                  }
               } else if (var5.equals("MessageSubstring")) {
                  var3.setMessageSubstring(var4.getMessageSubstring());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 12);
               } else if (var5.equals("SeverityLevel")) {
                  var3.setSeverityLevel(var4.getSeverityLevel());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 8);
               } else if (var5.equals("SubsystemNames")) {
                  if (var6 == 2) {
                     var2.resetAddedObject(var2.getAddedObject());
                     var3.addSubsystemName((String)var2.getAddedObject());
                  } else {
                     if (var6 != 3) {
                        throw new AssertionError("Invalid type: " + var6);
                     }

                     var3.removeSubsystemName((String)var2.getRemovedObject());
                  }

                  if (var3.getSubsystemNames() == null || var3.getSubsystemNames().length == 0) {
                     var3._conditionalUnset(var2.isUnsetUpdate(), 9);
                  }
               } else if (var5.equals("UserIds")) {
                  if (var6 == 2) {
                     var2.resetAddedObject(var2.getAddedObject());
                     var3.addUserId((String)var2.getAddedObject());
                  } else {
                     if (var6 != 3) {
                        throw new AssertionError("Invalid type: " + var6);
                     }

                     var3.removeUserId((String)var2.getRemovedObject());
                  }

                  if (var3.getUserIds() == null || var3.getUserIds().length == 0) {
                     var3._conditionalUnset(var2.isUnsetUpdate(), 10);
                  }
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
            SNMPLogFilterMBeanImpl var5 = (SNMPLogFilterMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            String[] var4;
            if ((var3 == null || !var3.contains("MessageIds")) && this.bean.isMessageIdsSet()) {
               var4 = this.bean.getMessageIds();
               var5.setMessageIds(var4 == null ? null : (String[])((String[])((String[])((String[])var4)).clone()));
            }

            if ((var3 == null || !var3.contains("MessageSubstring")) && this.bean.isMessageSubstringSet()) {
               var5.setMessageSubstring(this.bean.getMessageSubstring());
            }

            if ((var3 == null || !var3.contains("SeverityLevel")) && this.bean.isSeverityLevelSet()) {
               var5.setSeverityLevel(this.bean.getSeverityLevel());
            }

            if ((var3 == null || !var3.contains("SubsystemNames")) && this.bean.isSubsystemNamesSet()) {
               var4 = this.bean.getSubsystemNames();
               var5.setSubsystemNames(var4 == null ? null : (String[])((String[])((String[])((String[])var4)).clone()));
            }

            if ((var3 == null || !var3.contains("UserIds")) && this.bean.isUserIdsSet()) {
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
      }
   }
}
