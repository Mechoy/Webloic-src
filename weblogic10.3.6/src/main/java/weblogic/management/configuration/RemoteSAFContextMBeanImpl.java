package weblogic.management.configuration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.zip.CRC32;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBeanHelper;
import weblogic.descriptor.internal.Munger;
import weblogic.descriptor.internal.SchemaHelper;
import weblogic.utils.collections.CombinedIterator;

public class RemoteSAFContextMBeanImpl extends ConfigurationMBeanImpl implements RemoteSAFContextMBean, Serializable {
   private String _JndiInitialContextFactory;
   private String _JndiProperty;
   private String _Password;
   private String _Protocol;
   private String _Url;
   private String _Username;
   private static SchemaHelper2 _schemaHelper;

   public RemoteSAFContextMBeanImpl() {
      this._initializeProperty(-1);
   }

   public RemoteSAFContextMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);
      this._initializeProperty(-1);
   }

   public String getUrl() {
      return this._Url;
   }

   public boolean isUrlSet() {
      return this._isSet(7);
   }

   public void setUrl(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._Url;
      this._Url = var1;
      this._postSet(7, var2, var1);
   }

   public String getProtocol() {
      return this._Protocol;
   }

   public boolean isProtocolSet() {
      return this._isSet(8);
   }

   public void setProtocol(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._Protocol;
      this._Protocol = var1;
      this._postSet(8, var2, var1);
   }

   public String getUsername() {
      return this._Username;
   }

   public boolean isUsernameSet() {
      return this._isSet(9);
   }

   public void setUsername(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._Username;
      this._Username = var1;
      this._postSet(9, var2, var1);
   }

   public String getPassword() {
      return this._Password;
   }

   public boolean isPasswordSet() {
      return this._isSet(10);
   }

   public void setPassword(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._Password;
      this._Password = var1;
      this._postSet(10, var2, var1);
   }

   public String getJndiProperty() {
      return this._JndiProperty;
   }

   public boolean isJndiPropertySet() {
      return this._isSet(11);
   }

   public void setJndiProperty(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._JndiProperty;
      this._JndiProperty = var1;
      this._postSet(11, var2, var1);
   }

   public String getJndiInitialContextFactory() {
      return this._JndiInitialContextFactory;
   }

   public boolean isJndiInitialContextFactorySet() {
      return this._isSet(12);
   }

   public void setJndiInitialContextFactory(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._JndiInitialContextFactory;
      this._JndiInitialContextFactory = var1;
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
               this._JndiInitialContextFactory = null;
               if (var2) {
                  break;
               }
            case 11:
               this._JndiProperty = null;
               if (var2) {
                  break;
               }
            case 10:
               this._Password = null;
               if (var2) {
                  break;
               }
            case 8:
               this._Protocol = null;
               if (var2) {
                  break;
               }
            case 7:
               this._Url = null;
               if (var2) {
                  break;
               }
            case 9:
               this._Username = null;
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
      return "RemoteSAFContext";
   }

   public void putValue(String var1, Object var2) {
      String var3;
      if (var1.equals("JndiInitialContextFactory")) {
         var3 = this._JndiInitialContextFactory;
         this._JndiInitialContextFactory = (String)var2;
         this._postSet(12, var3, this._JndiInitialContextFactory);
      } else if (var1.equals("JndiProperty")) {
         var3 = this._JndiProperty;
         this._JndiProperty = (String)var2;
         this._postSet(11, var3, this._JndiProperty);
      } else if (var1.equals("Password")) {
         var3 = this._Password;
         this._Password = (String)var2;
         this._postSet(10, var3, this._Password);
      } else if (var1.equals("Protocol")) {
         var3 = this._Protocol;
         this._Protocol = (String)var2;
         this._postSet(8, var3, this._Protocol);
      } else if (var1.equals("Url")) {
         var3 = this._Url;
         this._Url = (String)var2;
         this._postSet(7, var3, this._Url);
      } else if (var1.equals("Username")) {
         var3 = this._Username;
         this._Username = (String)var2;
         this._postSet(9, var3, this._Username);
      } else {
         super.putValue(var1, var2);
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("JndiInitialContextFactory")) {
         return this._JndiInitialContextFactory;
      } else if (var1.equals("JndiProperty")) {
         return this._JndiProperty;
      } else if (var1.equals("Password")) {
         return this._Password;
      } else if (var1.equals("Protocol")) {
         return this._Protocol;
      } else if (var1.equals("Url")) {
         return this._Url;
      } else {
         return var1.equals("Username") ? this._Username : super.getValue(var1);
      }
   }

   public static class SchemaHelper2 extends ConfigurationMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 3:
               if (var1.equals("url")) {
                  return 7;
               }
               break;
            case 8:
               if (var1.equals("password")) {
                  return 10;
               }

               if (var1.equals("protocol")) {
                  return 8;
               }

               if (var1.equals("username")) {
                  return 9;
               }
               break;
            case 13:
               if (var1.equals("jndi-property")) {
                  return 11;
               }
               break;
            case 28:
               if (var1.equals("jndi-initial-context-factory")) {
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
            case 7:
               return "url";
            case 8:
               return "protocol";
            case 9:
               return "username";
            case 10:
               return "password";
            case 11:
               return "jndi-property";
            case 12:
               return "jndi-initial-context-factory";
            default:
               return super.getElementName(var1);
         }
      }

      public boolean isConfigurable(int var1) {
         switch (var1) {
            case 7:
               return true;
            case 8:
               return true;
            case 9:
               return true;
            case 10:
               return true;
            case 11:
               return true;
            case 12:
               return true;
            default:
               return super.isConfigurable(var1);
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
      private RemoteSAFContextMBeanImpl bean;

      protected Helper(RemoteSAFContextMBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 7:
               return "Url";
            case 8:
               return "Protocol";
            case 9:
               return "Username";
            case 10:
               return "Password";
            case 11:
               return "JndiProperty";
            case 12:
               return "JndiInitialContextFactory";
            default:
               return super.getPropertyName(var1);
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("JndiInitialContextFactory")) {
            return 12;
         } else if (var1.equals("JndiProperty")) {
            return 11;
         } else if (var1.equals("Password")) {
            return 10;
         } else if (var1.equals("Protocol")) {
            return 8;
         } else if (var1.equals("Url")) {
            return 7;
         } else {
            return var1.equals("Username") ? 9 : super.getPropertyIndex(var1);
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
            if (this.bean.isJndiInitialContextFactorySet()) {
               var2.append("JndiInitialContextFactory");
               var2.append(String.valueOf(this.bean.getJndiInitialContextFactory()));
            }

            if (this.bean.isJndiPropertySet()) {
               var2.append("JndiProperty");
               var2.append(String.valueOf(this.bean.getJndiProperty()));
            }

            if (this.bean.isPasswordSet()) {
               var2.append("Password");
               var2.append(String.valueOf(this.bean.getPassword()));
            }

            if (this.bean.isProtocolSet()) {
               var2.append("Protocol");
               var2.append(String.valueOf(this.bean.getProtocol()));
            }

            if (this.bean.isUrlSet()) {
               var2.append("Url");
               var2.append(String.valueOf(this.bean.getUrl()));
            }

            if (this.bean.isUsernameSet()) {
               var2.append("Username");
               var2.append(String.valueOf(this.bean.getUsername()));
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
            RemoteSAFContextMBeanImpl var2 = (RemoteSAFContextMBeanImpl)var1;
            this.computeDiff("JndiInitialContextFactory", this.bean.getJndiInitialContextFactory(), var2.getJndiInitialContextFactory(), false);
            this.computeDiff("JndiProperty", this.bean.getJndiProperty(), var2.getJndiProperty(), false);
            this.computeDiff("Password", this.bean.getPassword(), var2.getPassword(), false);
            this.computeDiff("Protocol", this.bean.getProtocol(), var2.getProtocol(), false);
            this.computeDiff("Url", this.bean.getUrl(), var2.getUrl(), false);
            this.computeDiff("Username", this.bean.getUsername(), var2.getUsername(), false);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            RemoteSAFContextMBeanImpl var3 = (RemoteSAFContextMBeanImpl)var1.getSourceBean();
            RemoteSAFContextMBeanImpl var4 = (RemoteSAFContextMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("JndiInitialContextFactory")) {
                  var3.setJndiInitialContextFactory(var4.getJndiInitialContextFactory());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 12);
               } else if (var5.equals("JndiProperty")) {
                  var3.setJndiProperty(var4.getJndiProperty());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 11);
               } else if (var5.equals("Password")) {
                  var3.setPassword(var4.getPassword());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 10);
               } else if (var5.equals("Protocol")) {
                  var3.setProtocol(var4.getProtocol());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 8);
               } else if (var5.equals("Url")) {
                  var3.setUrl(var4.getUrl());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 7);
               } else if (var5.equals("Username")) {
                  var3.setUsername(var4.getUsername());
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
            RemoteSAFContextMBeanImpl var5 = (RemoteSAFContextMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("JndiInitialContextFactory")) && this.bean.isJndiInitialContextFactorySet()) {
               var5.setJndiInitialContextFactory(this.bean.getJndiInitialContextFactory());
            }

            if ((var3 == null || !var3.contains("JndiProperty")) && this.bean.isJndiPropertySet()) {
               var5.setJndiProperty(this.bean.getJndiProperty());
            }

            if ((var3 == null || !var3.contains("Password")) && this.bean.isPasswordSet()) {
               var5.setPassword(this.bean.getPassword());
            }

            if ((var3 == null || !var3.contains("Protocol")) && this.bean.isProtocolSet()) {
               var5.setProtocol(this.bean.getProtocol());
            }

            if ((var3 == null || !var3.contains("Url")) && this.bean.isUrlSet()) {
               var5.setUrl(this.bean.getUrl());
            }

            if ((var3 == null || !var3.contains("Username")) && this.bean.isUsernameSet()) {
               var5.setUsername(this.bean.getUsername());
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
