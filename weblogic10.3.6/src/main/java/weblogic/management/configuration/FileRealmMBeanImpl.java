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

public class FileRealmMBeanImpl extends ConfigurationMBeanImpl implements FileRealmMBean, Serializable {
   private int _MaxACLs;
   private int _MaxGroups;
   private int _MaxUsers;
   private static SchemaHelper2 _schemaHelper;

   public FileRealmMBeanImpl() {
      this._initializeProperty(-1);
   }

   public FileRealmMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);
      this._initializeProperty(-1);
   }

   public int getMaxUsers() {
      return this._MaxUsers;
   }

   public boolean isMaxUsersSet() {
      return this._isSet(7);
   }

   public void setMaxUsers(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("MaxUsers", (long)var1, 1L, 10000L);
      int var2 = this._MaxUsers;
      this._MaxUsers = var1;
      this._postSet(7, var2, var1);
   }

   public int getMaxGroups() {
      return this._MaxGroups;
   }

   public boolean isMaxGroupsSet() {
      return this._isSet(8);
   }

   public void setMaxGroups(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("MaxGroups", (long)var1, 1L, 10000L);
      int var2 = this._MaxGroups;
      this._MaxGroups = var1;
      this._postSet(8, var2, var1);
   }

   public int getMaxACLs() {
      return this._MaxACLs;
   }

   public boolean isMaxACLsSet() {
      return this._isSet(9);
   }

   public void setMaxACLs(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("MaxACLs", (long)var1, 1L, 10000L);
      int var2 = this._MaxACLs;
      this._MaxACLs = var1;
      this._postSet(9, var2, var1);
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
         var1 = 9;
      }

      try {
         switch (var1) {
            case 9:
               this._MaxACLs = 1000;
               if (var2) {
                  break;
               }
            case 8:
               this._MaxGroups = 1000;
               if (var2) {
                  break;
               }
            case 7:
               this._MaxUsers = 1000;
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
      return "FileRealm";
   }

   public void putValue(String var1, Object var2) {
      int var3;
      if (var1.equals("MaxACLs")) {
         var3 = this._MaxACLs;
         this._MaxACLs = (Integer)var2;
         this._postSet(9, var3, this._MaxACLs);
      } else if (var1.equals("MaxGroups")) {
         var3 = this._MaxGroups;
         this._MaxGroups = (Integer)var2;
         this._postSet(8, var3, this._MaxGroups);
      } else if (var1.equals("MaxUsers")) {
         var3 = this._MaxUsers;
         this._MaxUsers = (Integer)var2;
         this._postSet(7, var3, this._MaxUsers);
      } else {
         super.putValue(var1, var2);
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("MaxACLs")) {
         return new Integer(this._MaxACLs);
      } else if (var1.equals("MaxGroups")) {
         return new Integer(this._MaxGroups);
      } else {
         return var1.equals("MaxUsers") ? new Integer(this._MaxUsers) : super.getValue(var1);
      }
   }

   public static class SchemaHelper2 extends ConfigurationMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 8:
               if (var1.equals("maxac-ls")) {
                  return 9;
               }
               break;
            case 9:
               if (var1.equals("max-users")) {
                  return 7;
               }
               break;
            case 10:
               if (var1.equals("max-groups")) {
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
            case 7:
               return "max-users";
            case 8:
               return "max-groups";
            case 9:
               return "maxac-ls";
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
      private FileRealmMBeanImpl bean;

      protected Helper(FileRealmMBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 7:
               return "MaxUsers";
            case 8:
               return "MaxGroups";
            case 9:
               return "MaxACLs";
            default:
               return super.getPropertyName(var1);
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("MaxACLs")) {
            return 9;
         } else if (var1.equals("MaxGroups")) {
            return 8;
         } else {
            return var1.equals("MaxUsers") ? 7 : super.getPropertyIndex(var1);
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
            if (this.bean.isMaxACLsSet()) {
               var2.append("MaxACLs");
               var2.append(String.valueOf(this.bean.getMaxACLs()));
            }

            if (this.bean.isMaxGroupsSet()) {
               var2.append("MaxGroups");
               var2.append(String.valueOf(this.bean.getMaxGroups()));
            }

            if (this.bean.isMaxUsersSet()) {
               var2.append("MaxUsers");
               var2.append(String.valueOf(this.bean.getMaxUsers()));
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
            FileRealmMBeanImpl var2 = (FileRealmMBeanImpl)var1;
            this.computeDiff("MaxACLs", this.bean.getMaxACLs(), var2.getMaxACLs(), false);
            this.computeDiff("MaxGroups", this.bean.getMaxGroups(), var2.getMaxGroups(), false);
            this.computeDiff("MaxUsers", this.bean.getMaxUsers(), var2.getMaxUsers(), false);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            FileRealmMBeanImpl var3 = (FileRealmMBeanImpl)var1.getSourceBean();
            FileRealmMBeanImpl var4 = (FileRealmMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("MaxACLs")) {
                  var3.setMaxACLs(var4.getMaxACLs());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 9);
               } else if (var5.equals("MaxGroups")) {
                  var3.setMaxGroups(var4.getMaxGroups());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 8);
               } else if (var5.equals("MaxUsers")) {
                  var3.setMaxUsers(var4.getMaxUsers());
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
            FileRealmMBeanImpl var5 = (FileRealmMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("MaxACLs")) && this.bean.isMaxACLsSet()) {
               var5.setMaxACLs(this.bean.getMaxACLs());
            }

            if ((var3 == null || !var3.contains("MaxGroups")) && this.bean.isMaxGroupsSet()) {
               var5.setMaxGroups(this.bean.getMaxGroups());
            }

            if ((var3 == null || !var3.contains("MaxUsers")) && this.bean.isMaxUsersSet()) {
               var5.setMaxUsers(this.bean.getMaxUsers());
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
