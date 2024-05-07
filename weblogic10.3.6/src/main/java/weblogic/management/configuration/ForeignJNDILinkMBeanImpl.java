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

public class ForeignJNDILinkMBeanImpl extends ConfigurationMBeanImpl implements ForeignJNDILinkMBean, Serializable {
   private String _LocalJNDIName;
   private String _RemoteJNDIName;
   private static SchemaHelper2 _schemaHelper;

   public ForeignJNDILinkMBeanImpl() {
      this._initializeProperty(-1);
   }

   public ForeignJNDILinkMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);
      this._initializeProperty(-1);
   }

   public String getLocalJNDIName() {
      return this._LocalJNDIName;
   }

   public boolean isLocalJNDINameSet() {
      return this._isSet(7);
   }

   public void setLocalJNDIName(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      weblogic.descriptor.beangen.LegalChecks.checkNonNull("LocalJNDIName", var1);
      String var2 = this._LocalJNDIName;
      this._LocalJNDIName = var1;
      this._postSet(7, var2, var1);
   }

   public String getRemoteJNDIName() {
      return this._RemoteJNDIName;
   }

   public boolean isRemoteJNDINameSet() {
      return this._isSet(8);
   }

   public void setRemoteJNDIName(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      weblogic.descriptor.beangen.LegalChecks.checkNonNull("RemoteJNDIName", var1);
      String var2 = this._RemoteJNDIName;
      this._RemoteJNDIName = var1;
      this._postSet(8, var2, var1);
   }

   public Object _getKey() {
      return super._getKey();
   }

   public void _validate() throws IllegalArgumentException {
      super._validate();
      weblogic.descriptor.beangen.LegalChecks.checkIsSet("LocalJNDIName", this.isLocalJNDINameSet());
      weblogic.descriptor.beangen.LegalChecks.checkIsSet("RemoteJNDIName", this.isRemoteJNDINameSet());
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
               this._LocalJNDIName = null;
               if (var2) {
                  break;
               }
            case 8:
               this._RemoteJNDIName = null;
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
      return "ForeignJNDILink";
   }

   public void putValue(String var1, Object var2) {
      String var3;
      if (var1.equals("LocalJNDIName")) {
         var3 = this._LocalJNDIName;
         this._LocalJNDIName = (String)var2;
         this._postSet(7, var3, this._LocalJNDIName);
      } else if (var1.equals("RemoteJNDIName")) {
         var3 = this._RemoteJNDIName;
         this._RemoteJNDIName = (String)var2;
         this._postSet(8, var3, this._RemoteJNDIName);
      } else {
         super.putValue(var1, var2);
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("LocalJNDIName")) {
         return this._LocalJNDIName;
      } else {
         return var1.equals("RemoteJNDIName") ? this._RemoteJNDIName : super.getValue(var1);
      }
   }

   public static class SchemaHelper2 extends ConfigurationMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 15:
               if (var1.equals("local-jndi-name")) {
                  return 7;
               }
               break;
            case 16:
               if (var1.equals("remote-jndi-name")) {
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
               return "local-jndi-name";
            case 8:
               return "remote-jndi-name";
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
      private ForeignJNDILinkMBeanImpl bean;

      protected Helper(ForeignJNDILinkMBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 7:
               return "LocalJNDIName";
            case 8:
               return "RemoteJNDIName";
            default:
               return super.getPropertyName(var1);
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("LocalJNDIName")) {
            return 7;
         } else {
            return var1.equals("RemoteJNDIName") ? 8 : super.getPropertyIndex(var1);
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
            if (this.bean.isLocalJNDINameSet()) {
               var2.append("LocalJNDIName");
               var2.append(String.valueOf(this.bean.getLocalJNDIName()));
            }

            if (this.bean.isRemoteJNDINameSet()) {
               var2.append("RemoteJNDIName");
               var2.append(String.valueOf(this.bean.getRemoteJNDIName()));
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
            ForeignJNDILinkMBeanImpl var2 = (ForeignJNDILinkMBeanImpl)var1;
            this.computeDiff("LocalJNDIName", this.bean.getLocalJNDIName(), var2.getLocalJNDIName(), true);
            this.computeDiff("RemoteJNDIName", this.bean.getRemoteJNDIName(), var2.getRemoteJNDIName(), true);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            ForeignJNDILinkMBeanImpl var3 = (ForeignJNDILinkMBeanImpl)var1.getSourceBean();
            ForeignJNDILinkMBeanImpl var4 = (ForeignJNDILinkMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("LocalJNDIName")) {
                  var3.setLocalJNDIName(var4.getLocalJNDIName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 7);
               } else if (var5.equals("RemoteJNDIName")) {
                  var3.setRemoteJNDIName(var4.getRemoteJNDIName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 8);
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
            ForeignJNDILinkMBeanImpl var5 = (ForeignJNDILinkMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("LocalJNDIName")) && this.bean.isLocalJNDINameSet()) {
               var5.setLocalJNDIName(this.bean.getLocalJNDIName());
            }

            if ((var3 == null || !var3.contains("RemoteJNDIName")) && this.bean.isRemoteJNDINameSet()) {
               var5.setRemoteJNDIName(this.bean.getRemoteJNDIName());
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
