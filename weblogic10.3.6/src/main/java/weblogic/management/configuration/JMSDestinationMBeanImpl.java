package weblogic.management.configuration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.zip.CRC32;
import javax.management.InvalidAttributeValueException;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.BootstrapProperties;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBeanHelper;
import weblogic.descriptor.internal.Munger;
import weblogic.descriptor.internal.ReferenceManager;
import weblogic.descriptor.internal.SchemaHelper;
import weblogic.utils.collections.CombinedIterator;

public class JMSDestinationMBeanImpl extends JMSDestCommonMBeanImpl implements JMSDestinationMBean, Serializable {
   private String _BytesPagingEnabled;
   private String _JNDIName;
   private boolean _JNDINameReplicated;
   private String _MessagesPagingEnabled;
   private String _StoreEnabled;
   private JMSTemplateMBean _Template;
   private static SchemaHelper2 _schemaHelper;

   public JMSDestinationMBeanImpl() {
      this._initializeProperty(-1);
   }

   public JMSDestinationMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);
      this._initializeProperty(-1);
   }

   public JMSTemplateMBean getTemplate() {
      return this._Template;
   }

   public String getTemplateAsString() {
      AbstractDescriptorBean var1 = (AbstractDescriptorBean)this.getTemplate();
      return var1 == null ? null : var1._getKey().toString();
   }

   public boolean isTemplateSet() {
      return this._isSet(25);
   }

   public void setTemplateAsString(String var1) {
      if (var1 != null && var1.length() != 0) {
         var1 = var1 == null ? null : var1.trim();
         this._getReferenceManager().registerUnresolvedReference(var1, JMSTemplateMBean.class, new ReferenceManager.Resolver(this, 25) {
            public void resolveReference(Object var1) {
               try {
                  JMSDestinationMBeanImpl.this.setTemplate((JMSTemplateMBean)var1);
               } catch (RuntimeException var3) {
                  throw var3;
               } catch (Exception var4) {
                  throw new AssertionError("Impossible exception: " + var4);
               }
            }
         });
      } else {
         JMSTemplateMBean var2 = this._Template;
         this._initializeProperty(25);
         this._postSet(25, var2, this._Template);
      }

   }

   public void setTemplate(JMSTemplateMBean var1) throws InvalidAttributeValueException {
      JMSTemplateMBean var2 = this._Template;
      this._Template = var1;
      this._postSet(25, var2, var1);
   }

   public String getJNDIName() {
      return this._JNDIName;
   }

   public boolean isJNDINameSet() {
      return this._isSet(26);
   }

   public void setJNDIName(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._JNDIName;
      this._JNDIName = var1;
      this._postSet(26, var2, var1);
   }

   public boolean isJNDINameReplicated() {
      return this._JNDINameReplicated;
   }

   public boolean isJNDINameReplicatedSet() {
      return this._isSet(27);
   }

   public void setJNDINameReplicated(boolean var1) throws InvalidAttributeValueException {
      boolean var2 = this._JNDINameReplicated;
      this._JNDINameReplicated = var1;
      this._postSet(27, var2, var1);
   }

   public String getStoreEnabled() {
      return this._StoreEnabled;
   }

   public boolean isStoreEnabledSet() {
      return this._isSet(28);
   }

   public void setStoreEnabled(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{"default", "false", "true"};
      var1 = weblogic.descriptor.beangen.LegalChecks.checkInEnum("StoreEnabled", var1, var2);
      String var3 = this._StoreEnabled;
      this._StoreEnabled = var1;
      this._postSet(28, var3, var1);
   }

   public String getMessagesPagingEnabled() {
      return this._MessagesPagingEnabled;
   }

   public boolean isMessagesPagingEnabledSet() {
      return this._isSet(29);
   }

   public void setMessagesPagingEnabled(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{"default", "false", "true"};
      var1 = weblogic.descriptor.beangen.LegalChecks.checkInEnum("MessagesPagingEnabled", var1, var2);
      String var3 = this._MessagesPagingEnabled;
      this._MessagesPagingEnabled = var1;
      this._postSet(29, var3, var1);
   }

   public String getBytesPagingEnabled() {
      return this._BytesPagingEnabled;
   }

   public boolean isBytesPagingEnabledSet() {
      return this._isSet(30);
   }

   public void setBytesPagingEnabled(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{"default", "false", "true"};
      var1 = weblogic.descriptor.beangen.LegalChecks.checkInEnum("BytesPagingEnabled", var1, var2);
      String var3 = this._BytesPagingEnabled;
      this._BytesPagingEnabled = var1;
      this._postSet(30, var3, var1);
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
         var1 = 30;
      }

      try {
         switch (var1) {
            case 30:
               this._BytesPagingEnabled = "default";
               if (var2) {
                  break;
               }
            case 26:
               this._JNDIName = null;
               if (var2) {
                  break;
               }
            case 29:
               this._MessagesPagingEnabled = "default";
               if (var2) {
                  break;
               }
            case 28:
               this._StoreEnabled = "default";
               if (var2) {
                  break;
               }
            case 25:
               this._Template = null;
               if (var2) {
                  break;
               }
            case 27:
               this._JNDINameReplicated = true;
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
      return "JMSDestination";
   }

   public void putValue(String var1, Object var2) {
      String var4;
      if (var1.equals("BytesPagingEnabled")) {
         var4 = this._BytesPagingEnabled;
         this._BytesPagingEnabled = (String)var2;
         this._postSet(30, var4, this._BytesPagingEnabled);
      } else if (var1.equals("JNDIName")) {
         var4 = this._JNDIName;
         this._JNDIName = (String)var2;
         this._postSet(26, var4, this._JNDIName);
      } else if (var1.equals("JNDINameReplicated")) {
         boolean var5 = this._JNDINameReplicated;
         this._JNDINameReplicated = (Boolean)var2;
         this._postSet(27, var5, this._JNDINameReplicated);
      } else if (var1.equals("MessagesPagingEnabled")) {
         var4 = this._MessagesPagingEnabled;
         this._MessagesPagingEnabled = (String)var2;
         this._postSet(29, var4, this._MessagesPagingEnabled);
      } else if (var1.equals("StoreEnabled")) {
         var4 = this._StoreEnabled;
         this._StoreEnabled = (String)var2;
         this._postSet(28, var4, this._StoreEnabled);
      } else if (var1.equals("Template")) {
         JMSTemplateMBean var3 = this._Template;
         this._Template = (JMSTemplateMBean)var2;
         this._postSet(25, var3, this._Template);
      } else {
         super.putValue(var1, var2);
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("BytesPagingEnabled")) {
         return this._BytesPagingEnabled;
      } else if (var1.equals("JNDIName")) {
         return this._JNDIName;
      } else if (var1.equals("JNDINameReplicated")) {
         return new Boolean(this._JNDINameReplicated);
      } else if (var1.equals("MessagesPagingEnabled")) {
         return this._MessagesPagingEnabled;
      } else if (var1.equals("StoreEnabled")) {
         return this._StoreEnabled;
      } else {
         return var1.equals("Template") ? this._Template : super.getValue(var1);
      }
   }

   public static class SchemaHelper2 extends JMSDestCommonMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 8:
               if (var1.equals("template")) {
                  return 25;
               }
               break;
            case 9:
               if (var1.equals("jndi-name")) {
                  return 26;
               }
               break;
            case 13:
               if (var1.equals("store-enabled")) {
                  return 28;
               }
               break;
            case 20:
               if (var1.equals("bytes-paging-enabled")) {
                  return 30;
               }

               if (var1.equals("jndi-name-replicated")) {
                  return 27;
               }
               break;
            case 23:
               if (var1.equals("messages-paging-enabled")) {
                  return 29;
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
            case 25:
               return "template";
            case 26:
               return "jndi-name";
            case 27:
               return "jndi-name-replicated";
            case 28:
               return "store-enabled";
            case 29:
               return "messages-paging-enabled";
            case 30:
               return "bytes-paging-enabled";
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

   protected static class Helper extends JMSDestCommonMBeanImpl.Helper {
      private JMSDestinationMBeanImpl bean;

      protected Helper(JMSDestinationMBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 25:
               return "Template";
            case 26:
               return "JNDIName";
            case 27:
               return "JNDINameReplicated";
            case 28:
               return "StoreEnabled";
            case 29:
               return "MessagesPagingEnabled";
            case 30:
               return "BytesPagingEnabled";
            default:
               return super.getPropertyName(var1);
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("BytesPagingEnabled")) {
            return 30;
         } else if (var1.equals("JNDIName")) {
            return 26;
         } else if (var1.equals("MessagesPagingEnabled")) {
            return 29;
         } else if (var1.equals("StoreEnabled")) {
            return 28;
         } else if (var1.equals("Template")) {
            return 25;
         } else {
            return var1.equals("JNDINameReplicated") ? 27 : super.getPropertyIndex(var1);
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
            if (this.bean.isBytesPagingEnabledSet()) {
               var2.append("BytesPagingEnabled");
               var2.append(String.valueOf(this.bean.getBytesPagingEnabled()));
            }

            if (this.bean.isJNDINameSet()) {
               var2.append("JNDIName");
               var2.append(String.valueOf(this.bean.getJNDIName()));
            }

            if (this.bean.isMessagesPagingEnabledSet()) {
               var2.append("MessagesPagingEnabled");
               var2.append(String.valueOf(this.bean.getMessagesPagingEnabled()));
            }

            if (this.bean.isStoreEnabledSet()) {
               var2.append("StoreEnabled");
               var2.append(String.valueOf(this.bean.getStoreEnabled()));
            }

            if (this.bean.isTemplateSet()) {
               var2.append("Template");
               var2.append(String.valueOf(this.bean.getTemplate()));
            }

            if (this.bean.isJNDINameReplicatedSet()) {
               var2.append("JNDINameReplicated");
               var2.append(String.valueOf(this.bean.isJNDINameReplicated()));
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
            JMSDestinationMBeanImpl var2 = (JMSDestinationMBeanImpl)var1;
            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("BytesPagingEnabled", this.bean.getBytesPagingEnabled(), var2.getBytesPagingEnabled(), false);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("JNDIName", this.bean.getJNDIName(), var2.getJNDIName(), false);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("MessagesPagingEnabled", this.bean.getMessagesPagingEnabled(), var2.getMessagesPagingEnabled(), false);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("StoreEnabled", this.bean.getStoreEnabled(), var2.getStoreEnabled(), false);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("Template", this.bean.getTemplate(), var2.getTemplate(), false);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("JNDINameReplicated", this.bean.isJNDINameReplicated(), var2.isJNDINameReplicated(), false);
            }

         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            JMSDestinationMBeanImpl var3 = (JMSDestinationMBeanImpl)var1.getSourceBean();
            JMSDestinationMBeanImpl var4 = (JMSDestinationMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("BytesPagingEnabled")) {
                  var3.setBytesPagingEnabled(var4.getBytesPagingEnabled());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 30);
               } else if (var5.equals("JNDIName")) {
                  var3.setJNDIName(var4.getJNDIName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 26);
               } else if (var5.equals("MessagesPagingEnabled")) {
                  var3.setMessagesPagingEnabled(var4.getMessagesPagingEnabled());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 29);
               } else if (var5.equals("StoreEnabled")) {
                  var3.setStoreEnabled(var4.getStoreEnabled());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 28);
               } else if (var5.equals("Template")) {
                  var3.setTemplateAsString(var4.getTemplateAsString());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 25);
               } else if (var5.equals("JNDINameReplicated")) {
                  var3.setJNDINameReplicated(var4.isJNDINameReplicated());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 27);
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
            JMSDestinationMBeanImpl var5 = (JMSDestinationMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if (var2 && (var3 == null || !var3.contains("BytesPagingEnabled")) && this.bean.isBytesPagingEnabledSet()) {
               var5.setBytesPagingEnabled(this.bean.getBytesPagingEnabled());
            }

            if (var2 && (var3 == null || !var3.contains("JNDIName")) && this.bean.isJNDINameSet()) {
               var5.setJNDIName(this.bean.getJNDIName());
            }

            if (var2 && (var3 == null || !var3.contains("MessagesPagingEnabled")) && this.bean.isMessagesPagingEnabledSet()) {
               var5.setMessagesPagingEnabled(this.bean.getMessagesPagingEnabled());
            }

            if (var2 && (var3 == null || !var3.contains("StoreEnabled")) && this.bean.isStoreEnabledSet()) {
               var5.setStoreEnabled(this.bean.getStoreEnabled());
            }

            if (var2 && (var3 == null || !var3.contains("Template")) && this.bean.isTemplateSet()) {
               var5._unSet(var5, 25);
               var5.setTemplateAsString(this.bean.getTemplateAsString());
            }

            if (var2 && (var3 == null || !var3.contains("JNDINameReplicated")) && this.bean.isJNDINameReplicatedSet()) {
               var5.setJNDINameReplicated(this.bean.isJNDINameReplicated());
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
         this.inferSubTree(this.bean.getTemplate(), var1, var2);
      }
   }
}
