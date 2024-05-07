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
import weblogic.utils.collections.ArrayIterator;
import weblogic.utils.collections.CombinedIterator;

public class UnixMachineMBeanImpl extends MachineMBeanImpl implements UnixMachineMBean, Serializable {
   private String _PostBindGID;
   private boolean _PostBindGIDEnabled;
   private String _PostBindUID;
   private boolean _PostBindUIDEnabled;
   private static SchemaHelper2 _schemaHelper;

   public UnixMachineMBeanImpl() {
      this._initializeProperty(-1);
   }

   public UnixMachineMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);
      this._initializeProperty(-1);
   }

   public boolean isPostBindUIDEnabled() {
      return this._PostBindUIDEnabled;
   }

   public boolean isPostBindUIDEnabledSet() {
      return this._isSet(9);
   }

   public void setPostBindUIDEnabled(boolean var1) {
      boolean var2 = this._PostBindUIDEnabled;
      this._PostBindUIDEnabled = var1;
      this._postSet(9, var2, var1);
   }

   public String getPostBindUID() {
      return this._PostBindUID;
   }

   public boolean isPostBindUIDSet() {
      return this._isSet(10);
   }

   public void setPostBindUID(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._PostBindUID;
      this._PostBindUID = var1;
      this._postSet(10, var2, var1);
   }

   public boolean isPostBindGIDEnabled() {
      return this._PostBindGIDEnabled;
   }

   public boolean isPostBindGIDEnabledSet() {
      return this._isSet(11);
   }

   public void setPostBindGIDEnabled(boolean var1) {
      boolean var2 = this._PostBindGIDEnabled;
      this._PostBindGIDEnabled = var1;
      this._postSet(11, var2, var1);
   }

   public String getPostBindGID() {
      return this._PostBindGID;
   }

   public boolean isPostBindGIDSet() {
      return this._isSet(12);
   }

   public void setPostBindGID(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._PostBindGID;
      this._PostBindGID = var1;
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
               this._PostBindGID = "nobody";
               if (var2) {
                  break;
               }
            case 10:
               this._PostBindUID = "nobody";
               if (var2) {
                  break;
               }
            case 11:
               this._PostBindGIDEnabled = false;
               if (var2) {
                  break;
               }
            case 9:
               this._PostBindUIDEnabled = false;
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
      return "UnixMachine";
   }

   public void putValue(String var1, Object var2) {
      String var4;
      if (var1.equals("PostBindGID")) {
         var4 = this._PostBindGID;
         this._PostBindGID = (String)var2;
         this._postSet(12, var4, this._PostBindGID);
      } else {
         boolean var3;
         if (var1.equals("PostBindGIDEnabled")) {
            var3 = this._PostBindGIDEnabled;
            this._PostBindGIDEnabled = (Boolean)var2;
            this._postSet(11, var3, this._PostBindGIDEnabled);
         } else if (var1.equals("PostBindUID")) {
            var4 = this._PostBindUID;
            this._PostBindUID = (String)var2;
            this._postSet(10, var4, this._PostBindUID);
         } else if (var1.equals("PostBindUIDEnabled")) {
            var3 = this._PostBindUIDEnabled;
            this._PostBindUIDEnabled = (Boolean)var2;
            this._postSet(9, var3, this._PostBindUIDEnabled);
         } else {
            super.putValue(var1, var2);
         }
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("PostBindGID")) {
         return this._PostBindGID;
      } else if (var1.equals("PostBindGIDEnabled")) {
         return new Boolean(this._PostBindGIDEnabled);
      } else if (var1.equals("PostBindUID")) {
         return this._PostBindUID;
      } else {
         return var1.equals("PostBindUIDEnabled") ? new Boolean(this._PostBindUIDEnabled) : super.getValue(var1);
      }
   }

   public static class SchemaHelper2 extends MachineMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 12:
               if (var1.equals("post-bindgid")) {
                  return 12;
               }

               if (var1.equals("post-binduid")) {
                  return 10;
               }
               break;
            case 20:
               if (var1.equals("post-bindgid-enabled")) {
                  return 11;
               }

               if (var1.equals("post-binduid-enabled")) {
                  return 9;
               }
         }

         return super.getPropertyIndex(var1);
      }

      public SchemaHelper getSchemaHelper(int var1) {
         switch (var1) {
            case 8:
               return new NodeManagerMBeanImpl.SchemaHelper2();
            default:
               return super.getSchemaHelper(var1);
         }
      }

      public String getElementName(int var1) {
         switch (var1) {
            case 9:
               return "post-binduid-enabled";
            case 10:
               return "post-binduid";
            case 11:
               return "post-bindgid-enabled";
            case 12:
               return "post-bindgid";
            default:
               return super.getElementName(var1);
         }
      }

      public boolean isArray(int var1) {
         switch (var1) {
            case 7:
               return true;
            default:
               return super.isArray(var1);
         }
      }

      public boolean isBean(int var1) {
         switch (var1) {
            case 8:
               return true;
            default:
               return super.isBean(var1);
         }
      }

      public boolean isConfigurable(int var1) {
         switch (var1) {
            case 7:
               return true;
            case 8:
            default:
               return super.isConfigurable(var1);
            case 9:
               return true;
            case 10:
               return true;
            case 11:
               return true;
            case 12:
               return true;
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

   protected static class Helper extends MachineMBeanImpl.Helper {
      private UnixMachineMBeanImpl bean;

      protected Helper(UnixMachineMBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 9:
               return "PostBindUIDEnabled";
            case 10:
               return "PostBindUID";
            case 11:
               return "PostBindGIDEnabled";
            case 12:
               return "PostBindGID";
            default:
               return super.getPropertyName(var1);
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("PostBindGID")) {
            return 12;
         } else if (var1.equals("PostBindUID")) {
            return 10;
         } else if (var1.equals("PostBindGIDEnabled")) {
            return 11;
         } else {
            return var1.equals("PostBindUIDEnabled") ? 9 : super.getPropertyIndex(var1);
         }
      }

      public Iterator getChildren() {
         ArrayList var1 = new ArrayList();
         if (this.bean.getNodeManager() != null) {
            var1.add(new ArrayIterator(new NodeManagerMBean[]{this.bean.getNodeManager()}));
         }

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
            if (this.bean.isPostBindGIDSet()) {
               var2.append("PostBindGID");
               var2.append(String.valueOf(this.bean.getPostBindGID()));
            }

            if (this.bean.isPostBindUIDSet()) {
               var2.append("PostBindUID");
               var2.append(String.valueOf(this.bean.getPostBindUID()));
            }

            if (this.bean.isPostBindGIDEnabledSet()) {
               var2.append("PostBindGIDEnabled");
               var2.append(String.valueOf(this.bean.isPostBindGIDEnabled()));
            }

            if (this.bean.isPostBindUIDEnabledSet()) {
               var2.append("PostBindUIDEnabled");
               var2.append(String.valueOf(this.bean.isPostBindUIDEnabled()));
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
            UnixMachineMBeanImpl var2 = (UnixMachineMBeanImpl)var1;
            this.computeDiff("PostBindGID", this.bean.getPostBindGID(), var2.getPostBindGID(), false);
            this.computeDiff("PostBindUID", this.bean.getPostBindUID(), var2.getPostBindUID(), false);
            this.computeDiff("PostBindGIDEnabled", this.bean.isPostBindGIDEnabled(), var2.isPostBindGIDEnabled(), false);
            this.computeDiff("PostBindUIDEnabled", this.bean.isPostBindUIDEnabled(), var2.isPostBindUIDEnabled(), false);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            UnixMachineMBeanImpl var3 = (UnixMachineMBeanImpl)var1.getSourceBean();
            UnixMachineMBeanImpl var4 = (UnixMachineMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("PostBindGID")) {
                  var3.setPostBindGID(var4.getPostBindGID());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 12);
               } else if (var5.equals("PostBindUID")) {
                  var3.setPostBindUID(var4.getPostBindUID());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 10);
               } else if (var5.equals("PostBindGIDEnabled")) {
                  var3.setPostBindGIDEnabled(var4.isPostBindGIDEnabled());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 11);
               } else if (var5.equals("PostBindUIDEnabled")) {
                  var3.setPostBindUIDEnabled(var4.isPostBindUIDEnabled());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 9);
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
            UnixMachineMBeanImpl var5 = (UnixMachineMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("PostBindGID")) && this.bean.isPostBindGIDSet()) {
               var5.setPostBindGID(this.bean.getPostBindGID());
            }

            if ((var3 == null || !var3.contains("PostBindUID")) && this.bean.isPostBindUIDSet()) {
               var5.setPostBindUID(this.bean.getPostBindUID());
            }

            if ((var3 == null || !var3.contains("PostBindGIDEnabled")) && this.bean.isPostBindGIDEnabledSet()) {
               var5.setPostBindGIDEnabled(this.bean.isPostBindGIDEnabled());
            }

            if ((var3 == null || !var3.contains("PostBindUIDEnabled")) && this.bean.isPostBindUIDEnabledSet()) {
               var5.setPostBindUIDEnabled(this.bean.isPostBindUIDEnabled());
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
