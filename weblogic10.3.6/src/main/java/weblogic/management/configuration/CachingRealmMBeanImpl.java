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
import weblogic.descriptor.internal.ReferenceManager;
import weblogic.descriptor.internal.ResolvedReference;
import weblogic.descriptor.internal.SchemaHelper;
import weblogic.utils.collections.CombinedIterator;

public class CachingRealmMBeanImpl extends ConfigurationMBeanImpl implements CachingRealmMBean, Serializable {
   private boolean _ACLCacheEnable;
   private int _ACLCacheSize;
   private int _ACLCacheTTLNegative;
   private int _ACLCacheTTLPositive;
   private boolean _AuthenticationCacheEnable;
   private int _AuthenticationCacheSize;
   private int _AuthenticationCacheTTLNegative;
   private int _AuthenticationCacheTTLPositive;
   private BasicRealmMBean _BasicRealm;
   private boolean _CacheCaseSensitive;
   private boolean _GroupCacheEnable;
   private int _GroupCacheSize;
   private int _GroupCacheTTLNegative;
   private int _GroupCacheTTLPositive;
   private int _GroupMembershipCacheTTL;
   private boolean _PermissionCacheEnable;
   private int _PermissionCacheSize;
   private int _PermissionCacheTTLNegative;
   private int _PermissionCacheTTLPositive;
   private boolean _UserCacheEnable;
   private int _UserCacheSize;
   private int _UserCacheTTLNegative;
   private int _UserCacheTTLPositive;
   private static SchemaHelper2 _schemaHelper;

   public CachingRealmMBeanImpl() {
      this._initializeProperty(-1);
   }

   public CachingRealmMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);
      this._initializeProperty(-1);
   }

   public BasicRealmMBean getBasicRealm() {
      return this._BasicRealm;
   }

   public String getBasicRealmAsString() {
      AbstractDescriptorBean var1 = (AbstractDescriptorBean)this.getBasicRealm();
      return var1 == null ? null : var1._getKey().toString();
   }

   public boolean isBasicRealmSet() {
      return this._isSet(7);
   }

   public void setBasicRealmAsString(String var1) {
      if (var1 != null && var1.length() != 0) {
         var1 = var1 == null ? null : var1.trim();
         this._getReferenceManager().registerUnresolvedReference(var1, BasicRealmMBean.class, new ReferenceManager.Resolver(this, 7) {
            public void resolveReference(Object var1) {
               try {
                  CachingRealmMBeanImpl.this.setBasicRealm((BasicRealmMBean)var1);
               } catch (RuntimeException var3) {
                  throw var3;
               } catch (Exception var4) {
                  throw new AssertionError("Impossible exception: " + var4);
               }
            }
         });
      } else {
         BasicRealmMBean var2 = this._BasicRealm;
         this._initializeProperty(7);
         this._postSet(7, var2, this._BasicRealm);
      }

   }

   public void setBasicRealm(BasicRealmMBean var1) throws InvalidAttributeValueException {
      if (var1 != null) {
         ResolvedReference var2 = new ResolvedReference(this, 7, (AbstractDescriptorBean)var1) {
            protected Object getPropertyValue() {
               return CachingRealmMBeanImpl.this.getBasicRealm();
            }
         };
         this._getReferenceManager().registerResolvedReference((AbstractDescriptorBean)var1, var2);
      }

      BasicRealmMBean var3 = this._BasicRealm;
      this._BasicRealm = var1;
      this._postSet(7, var3, var1);
   }

   public boolean getCacheCaseSensitive() {
      return this._CacheCaseSensitive;
   }

   public boolean isCacheCaseSensitiveSet() {
      return this._isSet(8);
   }

   public void setCacheCaseSensitive(boolean var1) {
      boolean var2 = this._CacheCaseSensitive;
      this._CacheCaseSensitive = var1;
      this._postSet(8, var2, var1);
   }

   public boolean getACLCacheEnable() {
      return this._ACLCacheEnable;
   }

   public boolean isACLCacheEnableSet() {
      return this._isSet(9);
   }

   public void setACLCacheEnable(boolean var1) {
      boolean var2 = this._ACLCacheEnable;
      this._ACLCacheEnable = var1;
      this._postSet(9, var2, var1);
   }

   public boolean getAuthenticationCacheEnable() {
      return this._AuthenticationCacheEnable;
   }

   public boolean isAuthenticationCacheEnableSet() {
      return this._isSet(10);
   }

   public void setAuthenticationCacheEnable(boolean var1) {
      boolean var2 = this._AuthenticationCacheEnable;
      this._AuthenticationCacheEnable = var1;
      this._postSet(10, var2, var1);
   }

   public boolean getGroupCacheEnable() {
      return this._GroupCacheEnable;
   }

   public boolean isGroupCacheEnableSet() {
      return this._isSet(11);
   }

   public void setGroupCacheEnable(boolean var1) {
      boolean var2 = this._GroupCacheEnable;
      this._GroupCacheEnable = var1;
      this._postSet(11, var2, var1);
   }

   public boolean getPermissionCacheEnable() {
      return this._PermissionCacheEnable;
   }

   public boolean isPermissionCacheEnableSet() {
      return this._isSet(12);
   }

   public void setPermissionCacheEnable(boolean var1) {
      boolean var2 = this._PermissionCacheEnable;
      this._PermissionCacheEnable = var1;
      this._postSet(12, var2, var1);
   }

   public boolean getUserCacheEnable() {
      return this._UserCacheEnable;
   }

   public boolean isUserCacheEnableSet() {
      return this._isSet(13);
   }

   public void setUserCacheEnable(boolean var1) {
      boolean var2 = this._UserCacheEnable;
      this._UserCacheEnable = var1;
      this._postSet(13, var2, var1);
   }

   public int getACLCacheSize() {
      return this._ACLCacheSize;
   }

   public boolean isACLCacheSizeSet() {
      return this._isSet(14);
   }

   public void setACLCacheSize(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("ACLCacheSize", (long)var1, 17L, 65537L);
      int var2 = this._ACLCacheSize;
      this._ACLCacheSize = var1;
      this._postSet(14, var2, var1);
   }

   public int getAuthenticationCacheSize() {
      return this._AuthenticationCacheSize;
   }

   public boolean isAuthenticationCacheSizeSet() {
      return this._isSet(15);
   }

   public void setAuthenticationCacheSize(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("AuthenticationCacheSize", (long)var1, 17L, 65537L);
      int var2 = this._AuthenticationCacheSize;
      this._AuthenticationCacheSize = var1;
      this._postSet(15, var2, var1);
   }

   public int getGroupCacheSize() {
      return this._GroupCacheSize;
   }

   public boolean isGroupCacheSizeSet() {
      return this._isSet(16);
   }

   public void setGroupCacheSize(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("GroupCacheSize", (long)var1, 17L, 65537L);
      int var2 = this._GroupCacheSize;
      this._GroupCacheSize = var1;
      this._postSet(16, var2, var1);
   }

   public int getPermissionCacheSize() {
      return this._PermissionCacheSize;
   }

   public boolean isPermissionCacheSizeSet() {
      return this._isSet(17);
   }

   public void setPermissionCacheSize(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("PermissionCacheSize", (long)var1, 17L, 65537L);
      int var2 = this._PermissionCacheSize;
      this._PermissionCacheSize = var1;
      this._postSet(17, var2, var1);
   }

   public int getUserCacheSize() {
      return this._UserCacheSize;
   }

   public boolean isUserCacheSizeSet() {
      return this._isSet(18);
   }

   public void setUserCacheSize(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("UserCacheSize", (long)var1, 17L, 65537L);
      int var2 = this._UserCacheSize;
      this._UserCacheSize = var1;
      this._postSet(18, var2, var1);
   }

   public int getACLCacheTTLPositive() {
      return this._ACLCacheTTLPositive;
   }

   public boolean isACLCacheTTLPositiveSet() {
      return this._isSet(19);
   }

   public void setACLCacheTTLPositive(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("ACLCacheTTLPositive", (long)var1, 1L, 2147483647L);
      int var2 = this._ACLCacheTTLPositive;
      this._ACLCacheTTLPositive = var1;
      this._postSet(19, var2, var1);
   }

   public int getGroupCacheTTLPositive() {
      return this._GroupCacheTTLPositive;
   }

   public boolean isGroupCacheTTLPositiveSet() {
      return this._isSet(20);
   }

   public void setGroupCacheTTLPositive(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("GroupCacheTTLPositive", (long)var1, 1L, 2147483647L);
      int var2 = this._GroupCacheTTLPositive;
      this._GroupCacheTTLPositive = var1;
      this._postSet(20, var2, var1);
   }

   public int getAuthenticationCacheTTLPositive() {
      return this._AuthenticationCacheTTLPositive;
   }

   public boolean isAuthenticationCacheTTLPositiveSet() {
      return this._isSet(21);
   }

   public void setAuthenticationCacheTTLPositive(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("AuthenticationCacheTTLPositive", (long)var1, 1L, 2147483647L);
      int var2 = this._AuthenticationCacheTTLPositive;
      this._AuthenticationCacheTTLPositive = var1;
      this._postSet(21, var2, var1);
   }

   public int getPermissionCacheTTLPositive() {
      return this._PermissionCacheTTLPositive;
   }

   public boolean isPermissionCacheTTLPositiveSet() {
      return this._isSet(22);
   }

   public void setPermissionCacheTTLPositive(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("PermissionCacheTTLPositive", (long)var1, 1L, 2147483647L);
      int var2 = this._PermissionCacheTTLPositive;
      this._PermissionCacheTTLPositive = var1;
      this._postSet(22, var2, var1);
   }

   public int getUserCacheTTLPositive() {
      return this._UserCacheTTLPositive;
   }

   public boolean isUserCacheTTLPositiveSet() {
      return this._isSet(23);
   }

   public void setUserCacheTTLPositive(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("UserCacheTTLPositive", (long)var1, 1L, 2147483647L);
      int var2 = this._UserCacheTTLPositive;
      this._UserCacheTTLPositive = var1;
      this._postSet(23, var2, var1);
   }

   public int getACLCacheTTLNegative() {
      return this._ACLCacheTTLNegative;
   }

   public boolean isACLCacheTTLNegativeSet() {
      return this._isSet(24);
   }

   public void setACLCacheTTLNegative(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("ACLCacheTTLNegative", (long)var1, 1L, 2147483647L);
      int var2 = this._ACLCacheTTLNegative;
      this._ACLCacheTTLNegative = var1;
      this._postSet(24, var2, var1);
   }

   public int getGroupCacheTTLNegative() {
      return this._GroupCacheTTLNegative;
   }

   public boolean isGroupCacheTTLNegativeSet() {
      return this._isSet(25);
   }

   public void setGroupCacheTTLNegative(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("GroupCacheTTLNegative", (long)var1, 1L, 2147483647L);
      int var2 = this._GroupCacheTTLNegative;
      this._GroupCacheTTLNegative = var1;
      this._postSet(25, var2, var1);
   }

   public int getAuthenticationCacheTTLNegative() {
      return this._AuthenticationCacheTTLNegative;
   }

   public boolean isAuthenticationCacheTTLNegativeSet() {
      return this._isSet(26);
   }

   public void setAuthenticationCacheTTLNegative(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("AuthenticationCacheTTLNegative", (long)var1, 1L, 2147483647L);
      int var2 = this._AuthenticationCacheTTLNegative;
      this._AuthenticationCacheTTLNegative = var1;
      this._postSet(26, var2, var1);
   }

   public int getPermissionCacheTTLNegative() {
      return this._PermissionCacheTTLNegative;
   }

   public boolean isPermissionCacheTTLNegativeSet() {
      return this._isSet(27);
   }

   public void setPermissionCacheTTLNegative(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("PermissionCacheTTLNegative", (long)var1, 1L, 2147483647L);
      int var2 = this._PermissionCacheTTLNegative;
      this._PermissionCacheTTLNegative = var1;
      this._postSet(27, var2, var1);
   }

   public int getUserCacheTTLNegative() {
      return this._UserCacheTTLNegative;
   }

   public boolean isUserCacheTTLNegativeSet() {
      return this._isSet(28);
   }

   public void setUserCacheTTLNegative(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("UserCacheTTLNegative", (long)var1, 1L, 2147483647L);
      int var2 = this._UserCacheTTLNegative;
      this._UserCacheTTLNegative = var1;
      this._postSet(28, var2, var1);
   }

   public int getGroupMembershipCacheTTL() {
      return this._GroupMembershipCacheTTL;
   }

   public boolean isGroupMembershipCacheTTLSet() {
      return this._isSet(29);
   }

   public void setGroupMembershipCacheTTL(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkMin("GroupMembershipCacheTTL", var1, 0);
      int var2 = this._GroupMembershipCacheTTL;
      this._GroupMembershipCacheTTL = var1;
      this._postSet(29, var2, var1);
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
               this._ACLCacheEnable = true;
               if (var2) {
                  break;
               }
            case 14:
               this._ACLCacheSize = 211;
               if (var2) {
                  break;
               }
            case 24:
               this._ACLCacheTTLNegative = 10;
               if (var2) {
                  break;
               }
            case 19:
               this._ACLCacheTTLPositive = 60;
               if (var2) {
                  break;
               }
            case 10:
               this._AuthenticationCacheEnable = true;
               if (var2) {
                  break;
               }
            case 15:
               this._AuthenticationCacheSize = 211;
               if (var2) {
                  break;
               }
            case 26:
               this._AuthenticationCacheTTLNegative = 10;
               if (var2) {
                  break;
               }
            case 21:
               this._AuthenticationCacheTTLPositive = 60;
               if (var2) {
                  break;
               }
            case 7:
               this._BasicRealm = null;
               if (var2) {
                  break;
               }
            case 8:
               this._CacheCaseSensitive = true;
               if (var2) {
                  break;
               }
            case 11:
               this._GroupCacheEnable = true;
               if (var2) {
                  break;
               }
            case 16:
               this._GroupCacheSize = 211;
               if (var2) {
                  break;
               }
            case 25:
               this._GroupCacheTTLNegative = 10;
               if (var2) {
                  break;
               }
            case 20:
               this._GroupCacheTTLPositive = 60;
               if (var2) {
                  break;
               }
            case 29:
               this._GroupMembershipCacheTTL = 300;
               if (var2) {
                  break;
               }
            case 12:
               this._PermissionCacheEnable = true;
               if (var2) {
                  break;
               }
            case 17:
               this._PermissionCacheSize = 211;
               if (var2) {
                  break;
               }
            case 27:
               this._PermissionCacheTTLNegative = 10;
               if (var2) {
                  break;
               }
            case 22:
               this._PermissionCacheTTLPositive = 60;
               if (var2) {
                  break;
               }
            case 13:
               this._UserCacheEnable = true;
               if (var2) {
                  break;
               }
            case 18:
               this._UserCacheSize = 211;
               if (var2) {
                  break;
               }
            case 28:
               this._UserCacheTTLNegative = 10;
               if (var2) {
                  break;
               }
            case 23:
               this._UserCacheTTLPositive = 60;
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
      return "CachingRealm";
   }

   public void putValue(String var1, Object var2) {
      boolean var4;
      if (var1.equals("ACLCacheEnable")) {
         var4 = this._ACLCacheEnable;
         this._ACLCacheEnable = (Boolean)var2;
         this._postSet(9, var4, this._ACLCacheEnable);
      } else {
         int var3;
         if (var1.equals("ACLCacheSize")) {
            var3 = this._ACLCacheSize;
            this._ACLCacheSize = (Integer)var2;
            this._postSet(14, var3, this._ACLCacheSize);
         } else if (var1.equals("ACLCacheTTLNegative")) {
            var3 = this._ACLCacheTTLNegative;
            this._ACLCacheTTLNegative = (Integer)var2;
            this._postSet(24, var3, this._ACLCacheTTLNegative);
         } else if (var1.equals("ACLCacheTTLPositive")) {
            var3 = this._ACLCacheTTLPositive;
            this._ACLCacheTTLPositive = (Integer)var2;
            this._postSet(19, var3, this._ACLCacheTTLPositive);
         } else if (var1.equals("AuthenticationCacheEnable")) {
            var4 = this._AuthenticationCacheEnable;
            this._AuthenticationCacheEnable = (Boolean)var2;
            this._postSet(10, var4, this._AuthenticationCacheEnable);
         } else if (var1.equals("AuthenticationCacheSize")) {
            var3 = this._AuthenticationCacheSize;
            this._AuthenticationCacheSize = (Integer)var2;
            this._postSet(15, var3, this._AuthenticationCacheSize);
         } else if (var1.equals("AuthenticationCacheTTLNegative")) {
            var3 = this._AuthenticationCacheTTLNegative;
            this._AuthenticationCacheTTLNegative = (Integer)var2;
            this._postSet(26, var3, this._AuthenticationCacheTTLNegative);
         } else if (var1.equals("AuthenticationCacheTTLPositive")) {
            var3 = this._AuthenticationCacheTTLPositive;
            this._AuthenticationCacheTTLPositive = (Integer)var2;
            this._postSet(21, var3, this._AuthenticationCacheTTLPositive);
         } else if (var1.equals("BasicRealm")) {
            BasicRealmMBean var5 = this._BasicRealm;
            this._BasicRealm = (BasicRealmMBean)var2;
            this._postSet(7, var5, this._BasicRealm);
         } else if (var1.equals("CacheCaseSensitive")) {
            var4 = this._CacheCaseSensitive;
            this._CacheCaseSensitive = (Boolean)var2;
            this._postSet(8, var4, this._CacheCaseSensitive);
         } else if (var1.equals("GroupCacheEnable")) {
            var4 = this._GroupCacheEnable;
            this._GroupCacheEnable = (Boolean)var2;
            this._postSet(11, var4, this._GroupCacheEnable);
         } else if (var1.equals("GroupCacheSize")) {
            var3 = this._GroupCacheSize;
            this._GroupCacheSize = (Integer)var2;
            this._postSet(16, var3, this._GroupCacheSize);
         } else if (var1.equals("GroupCacheTTLNegative")) {
            var3 = this._GroupCacheTTLNegative;
            this._GroupCacheTTLNegative = (Integer)var2;
            this._postSet(25, var3, this._GroupCacheTTLNegative);
         } else if (var1.equals("GroupCacheTTLPositive")) {
            var3 = this._GroupCacheTTLPositive;
            this._GroupCacheTTLPositive = (Integer)var2;
            this._postSet(20, var3, this._GroupCacheTTLPositive);
         } else if (var1.equals("GroupMembershipCacheTTL")) {
            var3 = this._GroupMembershipCacheTTL;
            this._GroupMembershipCacheTTL = (Integer)var2;
            this._postSet(29, var3, this._GroupMembershipCacheTTL);
         } else if (var1.equals("PermissionCacheEnable")) {
            var4 = this._PermissionCacheEnable;
            this._PermissionCacheEnable = (Boolean)var2;
            this._postSet(12, var4, this._PermissionCacheEnable);
         } else if (var1.equals("PermissionCacheSize")) {
            var3 = this._PermissionCacheSize;
            this._PermissionCacheSize = (Integer)var2;
            this._postSet(17, var3, this._PermissionCacheSize);
         } else if (var1.equals("PermissionCacheTTLNegative")) {
            var3 = this._PermissionCacheTTLNegative;
            this._PermissionCacheTTLNegative = (Integer)var2;
            this._postSet(27, var3, this._PermissionCacheTTLNegative);
         } else if (var1.equals("PermissionCacheTTLPositive")) {
            var3 = this._PermissionCacheTTLPositive;
            this._PermissionCacheTTLPositive = (Integer)var2;
            this._postSet(22, var3, this._PermissionCacheTTLPositive);
         } else if (var1.equals("UserCacheEnable")) {
            var4 = this._UserCacheEnable;
            this._UserCacheEnable = (Boolean)var2;
            this._postSet(13, var4, this._UserCacheEnable);
         } else if (var1.equals("UserCacheSize")) {
            var3 = this._UserCacheSize;
            this._UserCacheSize = (Integer)var2;
            this._postSet(18, var3, this._UserCacheSize);
         } else if (var1.equals("UserCacheTTLNegative")) {
            var3 = this._UserCacheTTLNegative;
            this._UserCacheTTLNegative = (Integer)var2;
            this._postSet(28, var3, this._UserCacheTTLNegative);
         } else if (var1.equals("UserCacheTTLPositive")) {
            var3 = this._UserCacheTTLPositive;
            this._UserCacheTTLPositive = (Integer)var2;
            this._postSet(23, var3, this._UserCacheTTLPositive);
         } else {
            super.putValue(var1, var2);
         }
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("ACLCacheEnable")) {
         return new Boolean(this._ACLCacheEnable);
      } else if (var1.equals("ACLCacheSize")) {
         return new Integer(this._ACLCacheSize);
      } else if (var1.equals("ACLCacheTTLNegative")) {
         return new Integer(this._ACLCacheTTLNegative);
      } else if (var1.equals("ACLCacheTTLPositive")) {
         return new Integer(this._ACLCacheTTLPositive);
      } else if (var1.equals("AuthenticationCacheEnable")) {
         return new Boolean(this._AuthenticationCacheEnable);
      } else if (var1.equals("AuthenticationCacheSize")) {
         return new Integer(this._AuthenticationCacheSize);
      } else if (var1.equals("AuthenticationCacheTTLNegative")) {
         return new Integer(this._AuthenticationCacheTTLNegative);
      } else if (var1.equals("AuthenticationCacheTTLPositive")) {
         return new Integer(this._AuthenticationCacheTTLPositive);
      } else if (var1.equals("BasicRealm")) {
         return this._BasicRealm;
      } else if (var1.equals("CacheCaseSensitive")) {
         return new Boolean(this._CacheCaseSensitive);
      } else if (var1.equals("GroupCacheEnable")) {
         return new Boolean(this._GroupCacheEnable);
      } else if (var1.equals("GroupCacheSize")) {
         return new Integer(this._GroupCacheSize);
      } else if (var1.equals("GroupCacheTTLNegative")) {
         return new Integer(this._GroupCacheTTLNegative);
      } else if (var1.equals("GroupCacheTTLPositive")) {
         return new Integer(this._GroupCacheTTLPositive);
      } else if (var1.equals("GroupMembershipCacheTTL")) {
         return new Integer(this._GroupMembershipCacheTTL);
      } else if (var1.equals("PermissionCacheEnable")) {
         return new Boolean(this._PermissionCacheEnable);
      } else if (var1.equals("PermissionCacheSize")) {
         return new Integer(this._PermissionCacheSize);
      } else if (var1.equals("PermissionCacheTTLNegative")) {
         return new Integer(this._PermissionCacheTTLNegative);
      } else if (var1.equals("PermissionCacheTTLPositive")) {
         return new Integer(this._PermissionCacheTTLPositive);
      } else if (var1.equals("UserCacheEnable")) {
         return new Boolean(this._UserCacheEnable);
      } else if (var1.equals("UserCacheSize")) {
         return new Integer(this._UserCacheSize);
      } else if (var1.equals("UserCacheTTLNegative")) {
         return new Integer(this._UserCacheTTLNegative);
      } else {
         return var1.equals("UserCacheTTLPositive") ? new Integer(this._UserCacheTTLPositive) : super.getValue(var1);
      }
   }

   public static class SchemaHelper2 extends ConfigurationMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 11:
               if (var1.equals("basic-realm")) {
                  return 7;
               }
            case 12:
            case 13:
            case 19:
            case 24:
            case 26:
            case 29:
            case 30:
            case 31:
            default:
               break;
            case 14:
               if (var1.equals("acl-cache-size")) {
                  return 14;
               }
               break;
            case 15:
               if (var1.equals("user-cache-size")) {
                  return 18;
               }
               break;
            case 16:
               if (var1.equals("acl-cache-enable")) {
                  return 9;
               }

               if (var1.equals("group-cache-size")) {
                  return 16;
               }
               break;
            case 17:
               if (var1.equals("user-cache-enable")) {
                  return 13;
               }
               break;
            case 18:
               if (var1.equals("group-cache-enable")) {
                  return 11;
               }
               break;
            case 20:
               if (var1.equals("cache-case-sensitive")) {
                  return 8;
               }
               break;
            case 21:
               if (var1.equals("acl-cachettl-negative")) {
                  return 24;
               }

               if (var1.equals("acl-cachettl-positive")) {
                  return 19;
               }

               if (var1.equals("permission-cache-size")) {
                  return 17;
               }
               break;
            case 22:
               if (var1.equals("user-cachettl-negative")) {
                  return 28;
               }

               if (var1.equals("user-cachettl-positive")) {
                  return 23;
               }
               break;
            case 23:
               if (var1.equals("group-cachettl-negative")) {
                  return 25;
               }

               if (var1.equals("group-cachettl-positive")) {
                  return 20;
               }

               if (var1.equals("permission-cache-enable")) {
                  return 12;
               }
               break;
            case 25:
               if (var1.equals("authentication-cache-size")) {
                  return 15;
               }

               if (var1.equals("group-membership-cachettl")) {
                  return 29;
               }
               break;
            case 27:
               if (var1.equals("authentication-cache-enable")) {
                  return 10;
               }
               break;
            case 28:
               if (var1.equals("permission-cachettl-negative")) {
                  return 27;
               }

               if (var1.equals("permission-cachettl-positive")) {
                  return 22;
               }
               break;
            case 32:
               if (var1.equals("authentication-cachettl-negative")) {
                  return 26;
               }

               if (var1.equals("authentication-cachettl-positive")) {
                  return 21;
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
               return "basic-realm";
            case 8:
               return "cache-case-sensitive";
            case 9:
               return "acl-cache-enable";
            case 10:
               return "authentication-cache-enable";
            case 11:
               return "group-cache-enable";
            case 12:
               return "permission-cache-enable";
            case 13:
               return "user-cache-enable";
            case 14:
               return "acl-cache-size";
            case 15:
               return "authentication-cache-size";
            case 16:
               return "group-cache-size";
            case 17:
               return "permission-cache-size";
            case 18:
               return "user-cache-size";
            case 19:
               return "acl-cachettl-positive";
            case 20:
               return "group-cachettl-positive";
            case 21:
               return "authentication-cachettl-positive";
            case 22:
               return "permission-cachettl-positive";
            case 23:
               return "user-cachettl-positive";
            case 24:
               return "acl-cachettl-negative";
            case 25:
               return "group-cachettl-negative";
            case 26:
               return "authentication-cachettl-negative";
            case 27:
               return "permission-cachettl-negative";
            case 28:
               return "user-cachettl-negative";
            case 29:
               return "group-membership-cachettl";
            default:
               return super.getElementName(var1);
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

   protected static class Helper extends ConfigurationMBeanImpl.Helper {
      private CachingRealmMBeanImpl bean;

      protected Helper(CachingRealmMBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 7:
               return "BasicRealm";
            case 8:
               return "CacheCaseSensitive";
            case 9:
               return "ACLCacheEnable";
            case 10:
               return "AuthenticationCacheEnable";
            case 11:
               return "GroupCacheEnable";
            case 12:
               return "PermissionCacheEnable";
            case 13:
               return "UserCacheEnable";
            case 14:
               return "ACLCacheSize";
            case 15:
               return "AuthenticationCacheSize";
            case 16:
               return "GroupCacheSize";
            case 17:
               return "PermissionCacheSize";
            case 18:
               return "UserCacheSize";
            case 19:
               return "ACLCacheTTLPositive";
            case 20:
               return "GroupCacheTTLPositive";
            case 21:
               return "AuthenticationCacheTTLPositive";
            case 22:
               return "PermissionCacheTTLPositive";
            case 23:
               return "UserCacheTTLPositive";
            case 24:
               return "ACLCacheTTLNegative";
            case 25:
               return "GroupCacheTTLNegative";
            case 26:
               return "AuthenticationCacheTTLNegative";
            case 27:
               return "PermissionCacheTTLNegative";
            case 28:
               return "UserCacheTTLNegative";
            case 29:
               return "GroupMembershipCacheTTL";
            default:
               return super.getPropertyName(var1);
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("ACLCacheEnable")) {
            return 9;
         } else if (var1.equals("ACLCacheSize")) {
            return 14;
         } else if (var1.equals("ACLCacheTTLNegative")) {
            return 24;
         } else if (var1.equals("ACLCacheTTLPositive")) {
            return 19;
         } else if (var1.equals("AuthenticationCacheEnable")) {
            return 10;
         } else if (var1.equals("AuthenticationCacheSize")) {
            return 15;
         } else if (var1.equals("AuthenticationCacheTTLNegative")) {
            return 26;
         } else if (var1.equals("AuthenticationCacheTTLPositive")) {
            return 21;
         } else if (var1.equals("BasicRealm")) {
            return 7;
         } else if (var1.equals("CacheCaseSensitive")) {
            return 8;
         } else if (var1.equals("GroupCacheEnable")) {
            return 11;
         } else if (var1.equals("GroupCacheSize")) {
            return 16;
         } else if (var1.equals("GroupCacheTTLNegative")) {
            return 25;
         } else if (var1.equals("GroupCacheTTLPositive")) {
            return 20;
         } else if (var1.equals("GroupMembershipCacheTTL")) {
            return 29;
         } else if (var1.equals("PermissionCacheEnable")) {
            return 12;
         } else if (var1.equals("PermissionCacheSize")) {
            return 17;
         } else if (var1.equals("PermissionCacheTTLNegative")) {
            return 27;
         } else if (var1.equals("PermissionCacheTTLPositive")) {
            return 22;
         } else if (var1.equals("UserCacheEnable")) {
            return 13;
         } else if (var1.equals("UserCacheSize")) {
            return 18;
         } else if (var1.equals("UserCacheTTLNegative")) {
            return 28;
         } else {
            return var1.equals("UserCacheTTLPositive") ? 23 : super.getPropertyIndex(var1);
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
            if (this.bean.isACLCacheEnableSet()) {
               var2.append("ACLCacheEnable");
               var2.append(String.valueOf(this.bean.getACLCacheEnable()));
            }

            if (this.bean.isACLCacheSizeSet()) {
               var2.append("ACLCacheSize");
               var2.append(String.valueOf(this.bean.getACLCacheSize()));
            }

            if (this.bean.isACLCacheTTLNegativeSet()) {
               var2.append("ACLCacheTTLNegative");
               var2.append(String.valueOf(this.bean.getACLCacheTTLNegative()));
            }

            if (this.bean.isACLCacheTTLPositiveSet()) {
               var2.append("ACLCacheTTLPositive");
               var2.append(String.valueOf(this.bean.getACLCacheTTLPositive()));
            }

            if (this.bean.isAuthenticationCacheEnableSet()) {
               var2.append("AuthenticationCacheEnable");
               var2.append(String.valueOf(this.bean.getAuthenticationCacheEnable()));
            }

            if (this.bean.isAuthenticationCacheSizeSet()) {
               var2.append("AuthenticationCacheSize");
               var2.append(String.valueOf(this.bean.getAuthenticationCacheSize()));
            }

            if (this.bean.isAuthenticationCacheTTLNegativeSet()) {
               var2.append("AuthenticationCacheTTLNegative");
               var2.append(String.valueOf(this.bean.getAuthenticationCacheTTLNegative()));
            }

            if (this.bean.isAuthenticationCacheTTLPositiveSet()) {
               var2.append("AuthenticationCacheTTLPositive");
               var2.append(String.valueOf(this.bean.getAuthenticationCacheTTLPositive()));
            }

            if (this.bean.isBasicRealmSet()) {
               var2.append("BasicRealm");
               var2.append(String.valueOf(this.bean.getBasicRealm()));
            }

            if (this.bean.isCacheCaseSensitiveSet()) {
               var2.append("CacheCaseSensitive");
               var2.append(String.valueOf(this.bean.getCacheCaseSensitive()));
            }

            if (this.bean.isGroupCacheEnableSet()) {
               var2.append("GroupCacheEnable");
               var2.append(String.valueOf(this.bean.getGroupCacheEnable()));
            }

            if (this.bean.isGroupCacheSizeSet()) {
               var2.append("GroupCacheSize");
               var2.append(String.valueOf(this.bean.getGroupCacheSize()));
            }

            if (this.bean.isGroupCacheTTLNegativeSet()) {
               var2.append("GroupCacheTTLNegative");
               var2.append(String.valueOf(this.bean.getGroupCacheTTLNegative()));
            }

            if (this.bean.isGroupCacheTTLPositiveSet()) {
               var2.append("GroupCacheTTLPositive");
               var2.append(String.valueOf(this.bean.getGroupCacheTTLPositive()));
            }

            if (this.bean.isGroupMembershipCacheTTLSet()) {
               var2.append("GroupMembershipCacheTTL");
               var2.append(String.valueOf(this.bean.getGroupMembershipCacheTTL()));
            }

            if (this.bean.isPermissionCacheEnableSet()) {
               var2.append("PermissionCacheEnable");
               var2.append(String.valueOf(this.bean.getPermissionCacheEnable()));
            }

            if (this.bean.isPermissionCacheSizeSet()) {
               var2.append("PermissionCacheSize");
               var2.append(String.valueOf(this.bean.getPermissionCacheSize()));
            }

            if (this.bean.isPermissionCacheTTLNegativeSet()) {
               var2.append("PermissionCacheTTLNegative");
               var2.append(String.valueOf(this.bean.getPermissionCacheTTLNegative()));
            }

            if (this.bean.isPermissionCacheTTLPositiveSet()) {
               var2.append("PermissionCacheTTLPositive");
               var2.append(String.valueOf(this.bean.getPermissionCacheTTLPositive()));
            }

            if (this.bean.isUserCacheEnableSet()) {
               var2.append("UserCacheEnable");
               var2.append(String.valueOf(this.bean.getUserCacheEnable()));
            }

            if (this.bean.isUserCacheSizeSet()) {
               var2.append("UserCacheSize");
               var2.append(String.valueOf(this.bean.getUserCacheSize()));
            }

            if (this.bean.isUserCacheTTLNegativeSet()) {
               var2.append("UserCacheTTLNegative");
               var2.append(String.valueOf(this.bean.getUserCacheTTLNegative()));
            }

            if (this.bean.isUserCacheTTLPositiveSet()) {
               var2.append("UserCacheTTLPositive");
               var2.append(String.valueOf(this.bean.getUserCacheTTLPositive()));
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
            CachingRealmMBeanImpl var2 = (CachingRealmMBeanImpl)var1;
            this.computeDiff("ACLCacheEnable", this.bean.getACLCacheEnable(), var2.getACLCacheEnable(), false);
            this.computeDiff("ACLCacheSize", this.bean.getACLCacheSize(), var2.getACLCacheSize(), false);
            this.computeDiff("ACLCacheTTLNegative", this.bean.getACLCacheTTLNegative(), var2.getACLCacheTTLNegative(), false);
            this.computeDiff("ACLCacheTTLPositive", this.bean.getACLCacheTTLPositive(), var2.getACLCacheTTLPositive(), false);
            this.computeDiff("AuthenticationCacheEnable", this.bean.getAuthenticationCacheEnable(), var2.getAuthenticationCacheEnable(), false);
            this.computeDiff("AuthenticationCacheSize", this.bean.getAuthenticationCacheSize(), var2.getAuthenticationCacheSize(), false);
            this.computeDiff("AuthenticationCacheTTLNegative", this.bean.getAuthenticationCacheTTLNegative(), var2.getAuthenticationCacheTTLNegative(), false);
            this.computeDiff("AuthenticationCacheTTLPositive", this.bean.getAuthenticationCacheTTLPositive(), var2.getAuthenticationCacheTTLPositive(), false);
            this.computeDiff("BasicRealm", this.bean.getBasicRealm(), var2.getBasicRealm(), false);
            this.computeDiff("CacheCaseSensitive", this.bean.getCacheCaseSensitive(), var2.getCacheCaseSensitive(), false);
            this.computeDiff("GroupCacheEnable", this.bean.getGroupCacheEnable(), var2.getGroupCacheEnable(), false);
            this.computeDiff("GroupCacheSize", this.bean.getGroupCacheSize(), var2.getGroupCacheSize(), false);
            this.computeDiff("GroupCacheTTLNegative", this.bean.getGroupCacheTTLNegative(), var2.getGroupCacheTTLNegative(), false);
            this.computeDiff("GroupCacheTTLPositive", this.bean.getGroupCacheTTLPositive(), var2.getGroupCacheTTLPositive(), false);
            this.computeDiff("GroupMembershipCacheTTL", this.bean.getGroupMembershipCacheTTL(), var2.getGroupMembershipCacheTTL(), false);
            this.computeDiff("PermissionCacheEnable", this.bean.getPermissionCacheEnable(), var2.getPermissionCacheEnable(), false);
            this.computeDiff("PermissionCacheSize", this.bean.getPermissionCacheSize(), var2.getPermissionCacheSize(), false);
            this.computeDiff("PermissionCacheTTLNegative", this.bean.getPermissionCacheTTLNegative(), var2.getPermissionCacheTTLNegative(), false);
            this.computeDiff("PermissionCacheTTLPositive", this.bean.getPermissionCacheTTLPositive(), var2.getPermissionCacheTTLPositive(), false);
            this.computeDiff("UserCacheEnable", this.bean.getUserCacheEnable(), var2.getUserCacheEnable(), false);
            this.computeDiff("UserCacheSize", this.bean.getUserCacheSize(), var2.getUserCacheSize(), false);
            this.computeDiff("UserCacheTTLNegative", this.bean.getUserCacheTTLNegative(), var2.getUserCacheTTLNegative(), false);
            this.computeDiff("UserCacheTTLPositive", this.bean.getUserCacheTTLPositive(), var2.getUserCacheTTLPositive(), false);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            CachingRealmMBeanImpl var3 = (CachingRealmMBeanImpl)var1.getSourceBean();
            CachingRealmMBeanImpl var4 = (CachingRealmMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("ACLCacheEnable")) {
                  var3.setACLCacheEnable(var4.getACLCacheEnable());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 9);
               } else if (var5.equals("ACLCacheSize")) {
                  var3.setACLCacheSize(var4.getACLCacheSize());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 14);
               } else if (var5.equals("ACLCacheTTLNegative")) {
                  var3.setACLCacheTTLNegative(var4.getACLCacheTTLNegative());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 24);
               } else if (var5.equals("ACLCacheTTLPositive")) {
                  var3.setACLCacheTTLPositive(var4.getACLCacheTTLPositive());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 19);
               } else if (var5.equals("AuthenticationCacheEnable")) {
                  var3.setAuthenticationCacheEnable(var4.getAuthenticationCacheEnable());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 10);
               } else if (var5.equals("AuthenticationCacheSize")) {
                  var3.setAuthenticationCacheSize(var4.getAuthenticationCacheSize());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 15);
               } else if (var5.equals("AuthenticationCacheTTLNegative")) {
                  var3.setAuthenticationCacheTTLNegative(var4.getAuthenticationCacheTTLNegative());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 26);
               } else if (var5.equals("AuthenticationCacheTTLPositive")) {
                  var3.setAuthenticationCacheTTLPositive(var4.getAuthenticationCacheTTLPositive());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 21);
               } else if (var5.equals("BasicRealm")) {
                  var3.setBasicRealmAsString(var4.getBasicRealmAsString());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 7);
               } else if (var5.equals("CacheCaseSensitive")) {
                  var3.setCacheCaseSensitive(var4.getCacheCaseSensitive());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 8);
               } else if (var5.equals("GroupCacheEnable")) {
                  var3.setGroupCacheEnable(var4.getGroupCacheEnable());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 11);
               } else if (var5.equals("GroupCacheSize")) {
                  var3.setGroupCacheSize(var4.getGroupCacheSize());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 16);
               } else if (var5.equals("GroupCacheTTLNegative")) {
                  var3.setGroupCacheTTLNegative(var4.getGroupCacheTTLNegative());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 25);
               } else if (var5.equals("GroupCacheTTLPositive")) {
                  var3.setGroupCacheTTLPositive(var4.getGroupCacheTTLPositive());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 20);
               } else if (var5.equals("GroupMembershipCacheTTL")) {
                  var3.setGroupMembershipCacheTTL(var4.getGroupMembershipCacheTTL());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 29);
               } else if (var5.equals("PermissionCacheEnable")) {
                  var3.setPermissionCacheEnable(var4.getPermissionCacheEnable());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 12);
               } else if (var5.equals("PermissionCacheSize")) {
                  var3.setPermissionCacheSize(var4.getPermissionCacheSize());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 17);
               } else if (var5.equals("PermissionCacheTTLNegative")) {
                  var3.setPermissionCacheTTLNegative(var4.getPermissionCacheTTLNegative());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 27);
               } else if (var5.equals("PermissionCacheTTLPositive")) {
                  var3.setPermissionCacheTTLPositive(var4.getPermissionCacheTTLPositive());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 22);
               } else if (var5.equals("UserCacheEnable")) {
                  var3.setUserCacheEnable(var4.getUserCacheEnable());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 13);
               } else if (var5.equals("UserCacheSize")) {
                  var3.setUserCacheSize(var4.getUserCacheSize());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 18);
               } else if (var5.equals("UserCacheTTLNegative")) {
                  var3.setUserCacheTTLNegative(var4.getUserCacheTTLNegative());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 28);
               } else if (var5.equals("UserCacheTTLPositive")) {
                  var3.setUserCacheTTLPositive(var4.getUserCacheTTLPositive());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 23);
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
            CachingRealmMBeanImpl var5 = (CachingRealmMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("ACLCacheEnable")) && this.bean.isACLCacheEnableSet()) {
               var5.setACLCacheEnable(this.bean.getACLCacheEnable());
            }

            if ((var3 == null || !var3.contains("ACLCacheSize")) && this.bean.isACLCacheSizeSet()) {
               var5.setACLCacheSize(this.bean.getACLCacheSize());
            }

            if ((var3 == null || !var3.contains("ACLCacheTTLNegative")) && this.bean.isACLCacheTTLNegativeSet()) {
               var5.setACLCacheTTLNegative(this.bean.getACLCacheTTLNegative());
            }

            if ((var3 == null || !var3.contains("ACLCacheTTLPositive")) && this.bean.isACLCacheTTLPositiveSet()) {
               var5.setACLCacheTTLPositive(this.bean.getACLCacheTTLPositive());
            }

            if ((var3 == null || !var3.contains("AuthenticationCacheEnable")) && this.bean.isAuthenticationCacheEnableSet()) {
               var5.setAuthenticationCacheEnable(this.bean.getAuthenticationCacheEnable());
            }

            if ((var3 == null || !var3.contains("AuthenticationCacheSize")) && this.bean.isAuthenticationCacheSizeSet()) {
               var5.setAuthenticationCacheSize(this.bean.getAuthenticationCacheSize());
            }

            if ((var3 == null || !var3.contains("AuthenticationCacheTTLNegative")) && this.bean.isAuthenticationCacheTTLNegativeSet()) {
               var5.setAuthenticationCacheTTLNegative(this.bean.getAuthenticationCacheTTLNegative());
            }

            if ((var3 == null || !var3.contains("AuthenticationCacheTTLPositive")) && this.bean.isAuthenticationCacheTTLPositiveSet()) {
               var5.setAuthenticationCacheTTLPositive(this.bean.getAuthenticationCacheTTLPositive());
            }

            if ((var3 == null || !var3.contains("BasicRealm")) && this.bean.isBasicRealmSet()) {
               var5._unSet(var5, 7);
               var5.setBasicRealmAsString(this.bean.getBasicRealmAsString());
            }

            if ((var3 == null || !var3.contains("CacheCaseSensitive")) && this.bean.isCacheCaseSensitiveSet()) {
               var5.setCacheCaseSensitive(this.bean.getCacheCaseSensitive());
            }

            if ((var3 == null || !var3.contains("GroupCacheEnable")) && this.bean.isGroupCacheEnableSet()) {
               var5.setGroupCacheEnable(this.bean.getGroupCacheEnable());
            }

            if ((var3 == null || !var3.contains("GroupCacheSize")) && this.bean.isGroupCacheSizeSet()) {
               var5.setGroupCacheSize(this.bean.getGroupCacheSize());
            }

            if ((var3 == null || !var3.contains("GroupCacheTTLNegative")) && this.bean.isGroupCacheTTLNegativeSet()) {
               var5.setGroupCacheTTLNegative(this.bean.getGroupCacheTTLNegative());
            }

            if ((var3 == null || !var3.contains("GroupCacheTTLPositive")) && this.bean.isGroupCacheTTLPositiveSet()) {
               var5.setGroupCacheTTLPositive(this.bean.getGroupCacheTTLPositive());
            }

            if ((var3 == null || !var3.contains("GroupMembershipCacheTTL")) && this.bean.isGroupMembershipCacheTTLSet()) {
               var5.setGroupMembershipCacheTTL(this.bean.getGroupMembershipCacheTTL());
            }

            if ((var3 == null || !var3.contains("PermissionCacheEnable")) && this.bean.isPermissionCacheEnableSet()) {
               var5.setPermissionCacheEnable(this.bean.getPermissionCacheEnable());
            }

            if ((var3 == null || !var3.contains("PermissionCacheSize")) && this.bean.isPermissionCacheSizeSet()) {
               var5.setPermissionCacheSize(this.bean.getPermissionCacheSize());
            }

            if ((var3 == null || !var3.contains("PermissionCacheTTLNegative")) && this.bean.isPermissionCacheTTLNegativeSet()) {
               var5.setPermissionCacheTTLNegative(this.bean.getPermissionCacheTTLNegative());
            }

            if ((var3 == null || !var3.contains("PermissionCacheTTLPositive")) && this.bean.isPermissionCacheTTLPositiveSet()) {
               var5.setPermissionCacheTTLPositive(this.bean.getPermissionCacheTTLPositive());
            }

            if ((var3 == null || !var3.contains("UserCacheEnable")) && this.bean.isUserCacheEnableSet()) {
               var5.setUserCacheEnable(this.bean.getUserCacheEnable());
            }

            if ((var3 == null || !var3.contains("UserCacheSize")) && this.bean.isUserCacheSizeSet()) {
               var5.setUserCacheSize(this.bean.getUserCacheSize());
            }

            if ((var3 == null || !var3.contains("UserCacheTTLNegative")) && this.bean.isUserCacheTTLNegativeSet()) {
               var5.setUserCacheTTLNegative(this.bean.getUserCacheTTLNegative());
            }

            if ((var3 == null || !var3.contains("UserCacheTTLPositive")) && this.bean.isUserCacheTTLPositiveSet()) {
               var5.setUserCacheTTLPositive(this.bean.getUserCacheTTLPositive());
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
         this.inferSubTree(this.bean.getBasicRealm(), var1, var2);
      }
   }
}
