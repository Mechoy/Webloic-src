package weblogic.management.security.authentication;

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
import weblogic.descriptor.internal.AbstractSchemaHelper2;
import weblogic.descriptor.internal.Munger;
import weblogic.descriptor.internal.SchemaHelper;
import weblogic.management.commo.AbstractCommoConfigurationBean;
import weblogic.utils.collections.CombinedIterator;

public class GroupMembershipHierarchyCacheMBeanImpl extends AbstractCommoConfigurationBean implements GroupMembershipHierarchyCacheMBean, Serializable {
   private Boolean _EnableGroupMembershipLookupHierarchyCaching;
   private Integer _GroupHierarchyCacheTTL;
   private Integer _MaxGroupHierarchiesInCache;
   private static SchemaHelper2 _schemaHelper;

   public GroupMembershipHierarchyCacheMBeanImpl() {
      this._initializeProperty(-1);
   }

   public GroupMembershipHierarchyCacheMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);
      this._initializeProperty(-1);
   }

   public Boolean getEnableGroupMembershipLookupHierarchyCaching() {
      return this._EnableGroupMembershipLookupHierarchyCaching;
   }

   public boolean isEnableGroupMembershipLookupHierarchyCachingSet() {
      return this._isSet(2);
   }

   public void setEnableGroupMembershipLookupHierarchyCaching(Boolean var1) throws InvalidAttributeValueException {
      Boolean var2 = this._EnableGroupMembershipLookupHierarchyCaching;
      this._EnableGroupMembershipLookupHierarchyCaching = var1;
      this._postSet(2, var2, var1);
   }

   public Integer getMaxGroupHierarchiesInCache() {
      return this._MaxGroupHierarchiesInCache;
   }

   public boolean isMaxGroupHierarchiesInCacheSet() {
      return this._isSet(3);
   }

   public void setMaxGroupHierarchiesInCache(Integer var1) throws InvalidAttributeValueException {
      Integer var2 = this._MaxGroupHierarchiesInCache;
      this._MaxGroupHierarchiesInCache = var1;
      this._postSet(3, var2, var1);
   }

   public Integer getGroupHierarchyCacheTTL() {
      return this._GroupHierarchyCacheTTL;
   }

   public boolean isGroupHierarchyCacheTTLSet() {
      return this._isSet(4);
   }

   public void setGroupHierarchyCacheTTL(Integer var1) throws InvalidAttributeValueException {
      Integer var2 = this._GroupHierarchyCacheTTL;
      this._GroupHierarchyCacheTTL = var1;
      this._postSet(4, var2, var1);
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
         var1 = 2;
      }

      try {
         switch (var1) {
            case 2:
               this._EnableGroupMembershipLookupHierarchyCaching = new Boolean(false);
               if (var2) {
                  break;
               }
            case 4:
               this._GroupHierarchyCacheTTL = new Integer(60);
               if (var2) {
                  break;
               }
            case 3:
               this._MaxGroupHierarchiesInCache = new Integer(100);
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
      return "http://xmlns.oracle.com/weblogic/1.0/security.xsd";
   }

   protected String getTargetNamespace() {
      return "http://xmlns.oracle.com/weblogic/security";
   }

   public SchemaHelper _getSchemaHelper2() {
      if (_schemaHelper == null) {
         _schemaHelper = new SchemaHelper2();
      }

      return _schemaHelper;
   }

   public String wls_getInterfaceClassName() {
      return "weblogic.management.security.authentication.GroupMembershipHierarchyCacheMBean";
   }

   public static class SchemaHelper2 extends AbstractSchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 24:
               if (var1.equals("group-hierarchy-cachettl")) {
                  return 4;
               }
               break;
            case 30:
               if (var1.equals("max-group-hierarchies-in-cache")) {
                  return 3;
               }
               break;
            case 48:
               if (var1.equals("enable-group-membership-lookup-hierarchy-caching")) {
                  return 2;
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
               return "enable-group-membership-lookup-hierarchy-caching";
            case 3:
               return "max-group-hierarchies-in-cache";
            case 4:
               return "group-hierarchy-cachettl";
            default:
               return super.getElementName(var1);
         }
      }
   }

   protected static class Helper extends AbstractCommoConfigurationBean.Helper {
      private GroupMembershipHierarchyCacheMBeanImpl bean;

      protected Helper(GroupMembershipHierarchyCacheMBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 2:
               return "EnableGroupMembershipLookupHierarchyCaching";
            case 3:
               return "MaxGroupHierarchiesInCache";
            case 4:
               return "GroupHierarchyCacheTTL";
            default:
               return super.getPropertyName(var1);
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("EnableGroupMembershipLookupHierarchyCaching")) {
            return 2;
         } else if (var1.equals("GroupHierarchyCacheTTL")) {
            return 4;
         } else {
            return var1.equals("MaxGroupHierarchiesInCache") ? 3 : super.getPropertyIndex(var1);
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
            if (this.bean.isEnableGroupMembershipLookupHierarchyCachingSet()) {
               var2.append("EnableGroupMembershipLookupHierarchyCaching");
               var2.append(String.valueOf(this.bean.getEnableGroupMembershipLookupHierarchyCaching()));
            }

            if (this.bean.isGroupHierarchyCacheTTLSet()) {
               var2.append("GroupHierarchyCacheTTL");
               var2.append(String.valueOf(this.bean.getGroupHierarchyCacheTTL()));
            }

            if (this.bean.isMaxGroupHierarchiesInCacheSet()) {
               var2.append("MaxGroupHierarchiesInCache");
               var2.append(String.valueOf(this.bean.getMaxGroupHierarchiesInCache()));
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
            GroupMembershipHierarchyCacheMBeanImpl var2 = (GroupMembershipHierarchyCacheMBeanImpl)var1;
            this.computeDiff("EnableGroupMembershipLookupHierarchyCaching", this.bean.getEnableGroupMembershipLookupHierarchyCaching(), var2.getEnableGroupMembershipLookupHierarchyCaching(), false);
            this.computeDiff("GroupHierarchyCacheTTL", this.bean.getGroupHierarchyCacheTTL(), var2.getGroupHierarchyCacheTTL(), false);
            this.computeDiff("MaxGroupHierarchiesInCache", this.bean.getMaxGroupHierarchiesInCache(), var2.getMaxGroupHierarchiesInCache(), false);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            GroupMembershipHierarchyCacheMBeanImpl var3 = (GroupMembershipHierarchyCacheMBeanImpl)var1.getSourceBean();
            GroupMembershipHierarchyCacheMBeanImpl var4 = (GroupMembershipHierarchyCacheMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("EnableGroupMembershipLookupHierarchyCaching")) {
                  var3.setEnableGroupMembershipLookupHierarchyCaching(var4.getEnableGroupMembershipLookupHierarchyCaching());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 2);
               } else if (var5.equals("GroupHierarchyCacheTTL")) {
                  var3.setGroupHierarchyCacheTTL(var4.getGroupHierarchyCacheTTL());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 4);
               } else if (var5.equals("MaxGroupHierarchiesInCache")) {
                  var3.setMaxGroupHierarchiesInCache(var4.getMaxGroupHierarchiesInCache());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 3);
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
            GroupMembershipHierarchyCacheMBeanImpl var5 = (GroupMembershipHierarchyCacheMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("EnableGroupMembershipLookupHierarchyCaching")) && this.bean.isEnableGroupMembershipLookupHierarchyCachingSet()) {
               var5.setEnableGroupMembershipLookupHierarchyCaching(this.bean.getEnableGroupMembershipLookupHierarchyCaching());
            }

            if ((var3 == null || !var3.contains("GroupHierarchyCacheTTL")) && this.bean.isGroupHierarchyCacheTTLSet()) {
               var5.setGroupHierarchyCacheTTL(this.bean.getGroupHierarchyCacheTTL());
            }

            if ((var3 == null || !var3.contains("MaxGroupHierarchiesInCache")) && this.bean.isMaxGroupHierarchiesInCacheSet()) {
               var5.setMaxGroupHierarchiesInCache(this.bean.getMaxGroupHierarchiesInCache());
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
