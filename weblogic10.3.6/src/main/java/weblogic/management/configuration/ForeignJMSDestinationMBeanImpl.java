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
import weblogic.j2ee.descriptor.wl.ForeignDestinationBean;
import weblogic.management.ManagementException;
import weblogic.management.mbeans.custom.ForeignJMSDestination;
import weblogic.utils.collections.CombinedIterator;

public class ForeignJMSDestinationMBeanImpl extends ForeignJNDIObjectMBeanImpl implements ForeignJMSDestinationMBean, Serializable {
   private String _LocalJNDIName;
   private String _Name;
   private String _RemoteJNDIName;
   private ForeignJMSDestination _customizer;
   private static SchemaHelper2 _schemaHelper;

   public ForeignJMSDestinationMBeanImpl() {
      try {
         this._customizer = new ForeignJMSDestination(this);
      } catch (Exception var2) {
         if (var2 instanceof RuntimeException) {
            throw (RuntimeException)var2;
         }

         throw new UndeclaredThrowableException(var2);
      }

      this._initializeProperty(-1);
   }

   public ForeignJMSDestinationMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);

      try {
         this._customizer = new ForeignJMSDestination(this);
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

   public void setLocalJNDIName(String var1) {
      var1 = var1 == null ? null : var1.trim();
      weblogic.descriptor.beangen.LegalChecks.checkNonNull("LocalJNDIName", var1);
      String var2 = this.getLocalJNDIName();

      try {
         this._customizer.setLocalJNDIName(var1);
      } catch (InvalidAttributeValueException var4) {
         throw new UndeclaredThrowableException(var4);
      }

      this._postSet(7, var2, var1);
   }

   public String getLocalJNDIName() {
      return this._customizer.getLocalJNDIName();
   }

   public boolean isLocalJNDINameSet() {
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

   public void setRemoteJNDIName(String var1) {
      var1 = var1 == null ? null : var1.trim();
      weblogic.descriptor.beangen.LegalChecks.checkNonNull("RemoteJNDIName", var1);
      String var2 = this.getRemoteJNDIName();

      try {
         this._customizer.setRemoteJNDIName(var1);
      } catch (InvalidAttributeValueException var4) {
         throw new UndeclaredThrowableException(var4);
      }

      this._postSet(8, var2, var1);
   }

   public String getRemoteJNDIName() {
      return this._customizer.getRemoteJNDIName();
   }

   public boolean isRemoteJNDINameSet() {
      return this._isSet(8);
   }

   public void useDelegates(ForeignDestinationBean var1) {
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
      weblogic.descriptor.beangen.LegalChecks.checkIsSet("LocalJNDIName", this.isLocalJNDINameSet());
      weblogic.descriptor.beangen.LegalChecks.checkIsSet("RemoteJNDIName", this.isRemoteJNDINameSet());
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
         var1 = 7;
      }

      try {
         switch (var1) {
            case 7:
               this._customizer.setLocalJNDIName((String)null);
               if (var2) {
                  break;
               }
            case 2:
               this._customizer.setName((String)null);
               if (var2) {
                  break;
               }
            case 8:
               this._customizer.setRemoteJNDIName((String)null);
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
      return "ForeignJMSDestination";
   }

   public void putValue(String var1, Object var2) {
      String var4;
      if (var1.equals("LocalJNDIName")) {
         var4 = this._LocalJNDIName;
         this._LocalJNDIName = (String)var2;
         this._postSet(7, var4, this._LocalJNDIName);
      } else if (var1.equals("Name")) {
         var4 = this._Name;
         this._Name = (String)var2;
         this._postSet(2, var4, this._Name);
      } else if (var1.equals("RemoteJNDIName")) {
         var4 = this._RemoteJNDIName;
         this._RemoteJNDIName = (String)var2;
         this._postSet(8, var4, this._RemoteJNDIName);
      } else if (var1.equals("customizer")) {
         ForeignJMSDestination var3 = this._customizer;
         this._customizer = (ForeignJMSDestination)var2;
      } else {
         super.putValue(var1, var2);
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("LocalJNDIName")) {
         return this._LocalJNDIName;
      } else if (var1.equals("Name")) {
         return this._Name;
      } else if (var1.equals("RemoteJNDIName")) {
         return this._RemoteJNDIName;
      } else {
         return var1.equals("customizer") ? this._customizer : super.getValue(var1);
      }
   }

   public static class SchemaHelper2 extends ForeignJNDIObjectMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 4:
               if (var1.equals("name")) {
                  return 2;
               }
               break;
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
            case 2:
               return "name";
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

      public boolean hasKey() {
         return true;
      }

      public String[] getKeyElementNames() {
         ArrayList var1 = new ArrayList();
         var1.add("name");
         return (String[])((String[])var1.toArray(new String[0]));
      }
   }

   protected static class Helper extends ForeignJNDIObjectMBeanImpl.Helper {
      private ForeignJMSDestinationMBeanImpl bean;

      protected Helper(ForeignJMSDestinationMBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 2:
               return "Name";
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
         } else if (var1.equals("Name")) {
            return 2;
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

            if (this.bean.isNameSet()) {
               var2.append("Name");
               var2.append(String.valueOf(this.bean.getName()));
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
            ForeignJMSDestinationMBeanImpl var2 = (ForeignJMSDestinationMBeanImpl)var1;
            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("LocalJNDIName", this.bean.getLocalJNDIName(), var2.getLocalJNDIName(), true);
            }

            this.computeDiff("Name", this.bean.getName(), var2.getName(), false);
            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("RemoteJNDIName", this.bean.getRemoteJNDIName(), var2.getRemoteJNDIName(), true);
            }

         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            ForeignJMSDestinationMBeanImpl var3 = (ForeignJMSDestinationMBeanImpl)var1.getSourceBean();
            ForeignJMSDestinationMBeanImpl var4 = (ForeignJMSDestinationMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("LocalJNDIName")) {
                  var3.setLocalJNDIName(var4.getLocalJNDIName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 7);
               } else if (var5.equals("Name")) {
                  var3.setName(var4.getName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 2);
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
            ForeignJMSDestinationMBeanImpl var5 = (ForeignJMSDestinationMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if (var2 && (var3 == null || !var3.contains("LocalJNDIName")) && this.bean.isLocalJNDINameSet()) {
               var5.setLocalJNDIName(this.bean.getLocalJNDIName());
            }

            if ((var3 == null || !var3.contains("Name")) && this.bean.isNameSet()) {
               var5.setName(this.bean.getName());
            }

            if (var2 && (var3 == null || !var3.contains("RemoteJNDIName")) && this.bean.isRemoteJNDINameSet()) {
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
