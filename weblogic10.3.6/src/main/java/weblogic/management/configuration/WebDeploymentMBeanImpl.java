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
import weblogic.descriptor.internal.ReferenceManager;
import weblogic.descriptor.internal.ResolvedReference;
import weblogic.descriptor.internal.SchemaHelper;
import weblogic.management.DistributedManagementException;
import weblogic.utils.ArrayUtils;
import weblogic.utils.collections.CombinedIterator;

public class WebDeploymentMBeanImpl extends DeploymentMBeanImpl implements WebDeploymentMBean, Serializable {
   private VirtualHostMBean[] _DeployedVirtualHosts;
   private VirtualHostMBean[] _VirtualHosts;
   private WebServerMBean[] _WebServers;
   private static SchemaHelper2 _schemaHelper;

   public WebDeploymentMBeanImpl() {
      this._initializeProperty(-1);
   }

   public WebDeploymentMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);
      this._initializeProperty(-1);
   }

   public WebServerMBean[] getWebServers() {
      return this._WebServers;
   }

   public boolean isWebServersSet() {
      return this._isSet(9);
   }

   public void setWebServers(WebServerMBean[] var1) throws InvalidAttributeValueException, DistributedManagementException {
      Object var2 = var1 == null ? new WebServerMBeanImpl[0] : var1;
      this._WebServers = (WebServerMBean[])var2;
   }

   public boolean addWebServer(WebServerMBean var1) throws InvalidAttributeValueException, DistributedManagementException {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 9)) {
         WebServerMBean[] var2 = (WebServerMBean[])((WebServerMBean[])this._getHelper()._extendArray(this.getWebServers(), WebServerMBean.class, var1));

         try {
            this.setWebServers(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            if (var4 instanceof InvalidAttributeValueException) {
               throw (InvalidAttributeValueException)var4;
            }

            if (var4 instanceof DistributedManagementException) {
               throw (DistributedManagementException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

      return true;
   }

   public boolean removeWebServer(WebServerMBean var1) throws DistributedManagementException {
      WebServerMBean[] var2 = this.getWebServers();
      WebServerMBean[] var3 = (WebServerMBean[])((WebServerMBean[])this._getHelper()._removeElement(var2, WebServerMBean.class, var1));
      if (var3.length != var2.length) {
         try {
            this.setWebServers(var3);
            return true;
         } catch (Exception var5) {
            if (var5 instanceof RuntimeException) {
               throw (RuntimeException)var5;
            } else if (var5 instanceof DistributedManagementException) {
               throw (DistributedManagementException)var5;
            } else {
               throw new UndeclaredThrowableException(var5);
            }
         }
      } else {
         return false;
      }
   }

   public VirtualHostMBean[] getVirtualHosts() {
      return this._VirtualHosts;
   }

   public String getVirtualHostsAsString() {
      return this._getHelper()._serializeKeyList(this.getVirtualHosts());
   }

   public boolean isVirtualHostsSet() {
      return this._isSet(10);
   }

   public void setVirtualHostsAsString(String var1) {
      if (var1 != null && var1.length() != 0) {
         String[] var13 = this._getHelper()._splitKeyList(var1);
         List var3 = this._getHelper()._getKeyList(this._VirtualHosts);

         String var5;
         for(int var4 = 0; var4 < var13.length; ++var4) {
            var5 = var13[var4];
            var5 = var5 == null ? null : var5.trim();
            if (var3.contains(var5)) {
               var3.remove(var5);
            } else {
               this._getReferenceManager().registerUnresolvedReference(var5, VirtualHostMBean.class, new ReferenceManager.Resolver(this, 10) {
                  public void resolveReference(Object var1) {
                     try {
                        WebDeploymentMBeanImpl.this.addVirtualHost((VirtualHostMBean)var1);
                     } catch (RuntimeException var3) {
                        throw var3;
                     } catch (Exception var4) {
                        throw new AssertionError("Impossible exception: " + var4);
                     }
                  }
               });
            }
         }

         Iterator var14 = var3.iterator();

         while(true) {
            while(var14.hasNext()) {
               var5 = (String)var14.next();
               VirtualHostMBean[] var6 = this._VirtualHosts;
               int var7 = var6.length;

               for(int var8 = 0; var8 < var7; ++var8) {
                  VirtualHostMBean var9 = var6[var8];
                  if (var5.equals(var9.getName())) {
                     try {
                        this.removeVirtualHost(var9);
                        break;
                     } catch (RuntimeException var11) {
                        throw var11;
                     } catch (Exception var12) {
                        throw new AssertionError("Impossible exception: " + var12);
                     }
                  }
               }
            }

            return;
         }
      } else {
         VirtualHostMBean[] var2 = this._VirtualHosts;
         this._initializeProperty(10);
         this._postSet(10, var2, this._VirtualHosts);
      }
   }

   public void setVirtualHosts(VirtualHostMBean[] var1) throws InvalidAttributeValueException, DistributedManagementException {
      Object var4 = var1 == null ? new VirtualHostMBeanImpl[0] : var1;
      var1 = (VirtualHostMBean[])((VirtualHostMBean[])this._getHelper()._cleanAndValidateArray(var4, VirtualHostMBean.class));

      for(int var2 = 0; var2 < var1.length; ++var2) {
         if (var1[var2] != null) {
            ResolvedReference var3 = new ResolvedReference(this, 10, (AbstractDescriptorBean)var1[var2]) {
               protected Object getPropertyValue() {
                  return WebDeploymentMBeanImpl.this.getVirtualHosts();
               }
            };
            this._getReferenceManager().registerResolvedReference((AbstractDescriptorBean)var1[var2], var3);
         }
      }

      VirtualHostMBean[] var5 = this._VirtualHosts;
      this._VirtualHosts = var1;
      this._postSet(10, var5, var1);
   }

   public boolean addVirtualHost(VirtualHostMBean var1) throws InvalidAttributeValueException, DistributedManagementException {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 10)) {
         VirtualHostMBean[] var2;
         if (this._isSet(10)) {
            var2 = (VirtualHostMBean[])((VirtualHostMBean[])this._getHelper()._extendArray(this.getVirtualHosts(), VirtualHostMBean.class, var1));
         } else {
            var2 = new VirtualHostMBean[]{var1};
         }

         try {
            this.setVirtualHosts(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            if (var4 instanceof InvalidAttributeValueException) {
               throw (InvalidAttributeValueException)var4;
            }

            if (var4 instanceof DistributedManagementException) {
               throw (DistributedManagementException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

      return true;
   }

   public boolean removeVirtualHost(VirtualHostMBean var1) throws DistributedManagementException {
      VirtualHostMBean[] var2 = this.getVirtualHosts();
      VirtualHostMBean[] var3 = (VirtualHostMBean[])((VirtualHostMBean[])this._getHelper()._removeElement(var2, VirtualHostMBean.class, var1));
      if (var3.length != var2.length) {
         try {
            this.setVirtualHosts(var3);
            return true;
         } catch (Exception var5) {
            if (var5 instanceof RuntimeException) {
               throw (RuntimeException)var5;
            } else if (var5 instanceof DistributedManagementException) {
               throw (DistributedManagementException)var5;
            } else {
               throw new UndeclaredThrowableException(var5);
            }
         }
      } else {
         return false;
      }
   }

   public void addDeployedVirtualHost(VirtualHostMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 11)) {
         VirtualHostMBean[] var2 = (VirtualHostMBean[])((VirtualHostMBean[])this._getHelper()._extendArray(this.getDeployedVirtualHosts(), VirtualHostMBean.class, var1));

         try {
            this.setDeployedVirtualHosts(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public VirtualHostMBean[] getDeployedVirtualHosts() {
      return this._DeployedVirtualHosts;
   }

   public boolean isDeployedVirtualHostsSet() {
      return this._isSet(11);
   }

   public void removeDeployedVirtualHost(VirtualHostMBean var1) {
      VirtualHostMBean[] var2 = this.getDeployedVirtualHosts();
      VirtualHostMBean[] var3 = (VirtualHostMBean[])((VirtualHostMBean[])this._getHelper()._removeElement(var2, VirtualHostMBean.class, var1));
      if (var3.length != var2.length) {
         try {
            this.setDeployedVirtualHosts(var3);
         } catch (Exception var5) {
            if (var5 instanceof RuntimeException) {
               throw (RuntimeException)var5;
            }

            throw new UndeclaredThrowableException(var5);
         }
      }

   }

   public void setDeployedVirtualHosts(VirtualHostMBean[] var1) {
      Object var2 = var1 == null ? new VirtualHostMBeanImpl[0] : var1;
      this._DeployedVirtualHosts = (VirtualHostMBean[])var2;
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
               this._DeployedVirtualHosts = new VirtualHostMBean[0];
               if (var2) {
                  break;
               }
            case 10:
               this._VirtualHosts = new VirtualHostMBean[0];
               if (var2) {
                  break;
               }
            case 9:
               this._WebServers = new WebServerMBean[0];
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
      return "WebDeployment";
   }

   public void putValue(String var1, Object var2) {
      VirtualHostMBean[] var4;
      if (var1.equals("DeployedVirtualHosts")) {
         var4 = this._DeployedVirtualHosts;
         this._DeployedVirtualHosts = (VirtualHostMBean[])((VirtualHostMBean[])var2);
         this._postSet(11, var4, this._DeployedVirtualHosts);
      } else if (var1.equals("VirtualHosts")) {
         var4 = this._VirtualHosts;
         this._VirtualHosts = (VirtualHostMBean[])((VirtualHostMBean[])var2);
         this._postSet(10, var4, this._VirtualHosts);
      } else if (var1.equals("WebServers")) {
         WebServerMBean[] var3 = this._WebServers;
         this._WebServers = (WebServerMBean[])((WebServerMBean[])var2);
         this._postSet(9, var3, this._WebServers);
      } else {
         super.putValue(var1, var2);
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("DeployedVirtualHosts")) {
         return this._DeployedVirtualHosts;
      } else if (var1.equals("VirtualHosts")) {
         return this._VirtualHosts;
      } else {
         return var1.equals("WebServers") ? this._WebServers : super.getValue(var1);
      }
   }

   public static class SchemaHelper2 extends DeploymentMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 10:
               if (var1.equals("web-server")) {
                  return 9;
               }
               break;
            case 12:
               if (var1.equals("virtual-host")) {
                  return 10;
               }
               break;
            case 21:
               if (var1.equals("deployed-virtual-host")) {
                  return 11;
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
            case 9:
               return "web-server";
            case 10:
               return "virtual-host";
            case 11:
               return "deployed-virtual-host";
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

   protected static class Helper extends DeploymentMBeanImpl.Helper {
      private WebDeploymentMBeanImpl bean;

      protected Helper(WebDeploymentMBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 9:
               return "WebServers";
            case 10:
               return "VirtualHosts";
            case 11:
               return "DeployedVirtualHosts";
            default:
               return super.getPropertyName(var1);
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("DeployedVirtualHosts")) {
            return 11;
         } else if (var1.equals("VirtualHosts")) {
            return 10;
         } else {
            return var1.equals("WebServers") ? 9 : super.getPropertyIndex(var1);
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
            if (this.bean.isDeployedVirtualHostsSet()) {
               var2.append("DeployedVirtualHosts");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getDeployedVirtualHosts())));
            }

            if (this.bean.isVirtualHostsSet()) {
               var2.append("VirtualHosts");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getVirtualHosts())));
            }

            if (this.bean.isWebServersSet()) {
               var2.append("WebServers");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getWebServers())));
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
            WebDeploymentMBeanImpl var2 = (WebDeploymentMBeanImpl)var1;
            this.computeDiff("VirtualHosts", this.bean.getVirtualHosts(), var2.getVirtualHosts(), true);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            WebDeploymentMBeanImpl var3 = (WebDeploymentMBeanImpl)var1.getSourceBean();
            WebDeploymentMBeanImpl var4 = (WebDeploymentMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (!var5.equals("DeployedVirtualHosts")) {
                  if (var5.equals("VirtualHosts")) {
                     var3.setVirtualHostsAsString(var4.getVirtualHostsAsString());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 10);
                  } else if (!var5.equals("WebServers")) {
                     super.applyPropertyUpdate(var1, var2);
                  }
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
            WebDeploymentMBeanImpl var5 = (WebDeploymentMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("VirtualHosts")) && this.bean.isVirtualHostsSet()) {
               var5._unSet(var5, 10);
               var5.setVirtualHostsAsString(this.bean.getVirtualHostsAsString());
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
         this.inferSubTree(this.bean.getDeployedVirtualHosts(), var1, var2);
         this.inferSubTree(this.bean.getVirtualHosts(), var1, var2);
         this.inferSubTree(this.bean.getWebServers(), var1, var2);
      }
   }
}
