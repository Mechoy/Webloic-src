package weblogic.management.configuration;

import java.io.Serializable;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.zip.CRC32;
import javax.management.AttributeNotFoundException;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanException;
import weblogic.cluster.migration.MTCustomValidator;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBeanHelper;
import weblogic.descriptor.internal.Munger;
import weblogic.descriptor.internal.ReferenceManager;
import weblogic.descriptor.internal.ResolvedReference;
import weblogic.descriptor.internal.SchemaHelper;
import weblogic.management.ManagementException;
import weblogic.management.mbeans.custom.JTAMigratableTarget;
import weblogic.transaction.internal.JTAMTCustomValidator;
import weblogic.utils.ArrayUtils;
import weblogic.utils.collections.CombinedIterator;

public class JTAMigratableTargetMBeanImpl extends MigratableTargetMBeanImpl implements JTAMigratableTargetMBean, Serializable {
   private ServerMBean[] _AllCandidateServers;
   private ClusterMBean _Cluster;
   private ServerMBean[] _ConstrainedCandidateServers;
   private ServerMBean _HostingServer;
   private String _MigrationPolicy;
   private String _Name;
   private Set _ServerNames;
   private boolean _StrictOwnershipCheck;
   private ServerMBean _UserPreferredServer;
   private JTAMigratableTarget _customizer;
   private static SchemaHelper2 _schemaHelper;

   public JTAMigratableTargetMBeanImpl() {
      try {
         this._customizer = new JTAMigratableTarget(this);
      } catch (Exception var2) {
         if (var2 instanceof RuntimeException) {
            throw (RuntimeException)var2;
         }

         throw new UndeclaredThrowableException(var2);
      }

      this._initializeProperty(-1);
   }

   public JTAMigratableTargetMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);

      try {
         this._customizer = new JTAMigratableTarget(this);
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         }

         throw new UndeclaredThrowableException(var4);
      }

      this._initializeProperty(-1);
   }

   public ServerMBean[] getConstrainedCandidateServers() {
      return this._customizer.getConstrainedCandidateServers();
   }

   public String getConstrainedCandidateServersAsString() {
      return this._getHelper()._serializeKeyList(this.getConstrainedCandidateServers());
   }

   public ServerMBean getHostingServer() {
      return this._customizer.getHostingServer();
   }

   public String getHostingServerAsString() {
      AbstractDescriptorBean var1 = (AbstractDescriptorBean)this.getHostingServer();
      return var1 == null ? null : var1._getKey().toString();
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

   public Set getServerNames() {
      return this._customizer.getServerNames();
   }

   public boolean isConstrainedCandidateServersSet() {
      return this._isSet(12);
   }

   public boolean isHostingServerSet() {
      return this._isSet(7);
   }

   public boolean isNameSet() {
      return this._isSet(2);
   }

   public boolean isServerNamesSet() {
      return this._isSet(11);
   }

   public void setConstrainedCandidateServersAsString(String var1) {
      if (var1 != null && var1.length() != 0) {
         String[] var13 = this._getHelper()._splitKeyList(var1);
         List var3 = this._getHelper()._getKeyList(this._ConstrainedCandidateServers);

         String var5;
         for(int var4 = 0; var4 < var13.length; ++var4) {
            var5 = var13[var4];
            var5 = var5 == null ? null : var5.trim();
            if (var3.contains(var5)) {
               var3.remove(var5);
            } else {
               this._getReferenceManager().registerUnresolvedReference(var5, ServerMBean.class, new ReferenceManager.Resolver(this, 12) {
                  public void resolveReference(Object var1) {
                     try {
                        JTAMigratableTargetMBeanImpl.this.addConstrainedCandidateServer((ServerMBean)var1);
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
               ServerMBean[] var6 = this._ConstrainedCandidateServers;
               int var7 = var6.length;

               for(int var8 = 0; var8 < var7; ++var8) {
                  ServerMBean var9 = var6[var8];
                  if (var5.equals(var9.getName())) {
                     try {
                        this.removeConstrainedCandidateServer(var9);
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
         ServerMBean[] var2 = this._ConstrainedCandidateServers;
         this._initializeProperty(12);
         this._postSet(12, var2, this._ConstrainedCandidateServers);
      }
   }

   public void setHostingServerAsString(String var1) {
      if (var1 != null && var1.length() != 0) {
         var1 = var1 == null ? null : var1.trim();
         this._getReferenceManager().registerUnresolvedReference(var1, ServerMBean.class, new ReferenceManager.Resolver(this, 7) {
            public void resolveReference(Object var1) {
               try {
                  JTAMigratableTargetMBeanImpl.this.setHostingServer((ServerMBean)var1);
               } catch (RuntimeException var3) {
                  throw var3;
               } catch (Exception var4) {
                  throw new AssertionError("Impossible exception: " + var4);
               }
            }
         });
      } else {
         ServerMBean var2 = this._HostingServer;
         this._initializeProperty(7);
         this._postSet(7, var2, this._HostingServer);
      }

   }

   public void setServerNames(Set var1) throws InvalidAttributeValueException {
      this._ServerNames = var1;
   }

   public void setConstrainedCandidateServers(ServerMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new ServerMBeanImpl[0] : var1;
      var1 = (ServerMBean[])((ServerMBean[])this._getHelper()._cleanAndValidateArray(var4, ServerMBean.class));

      for(int var2 = 0; var2 < var1.length; ++var2) {
         if (var1[var2] != null) {
            ResolvedReference var3 = new ResolvedReference(this, 12, (AbstractDescriptorBean)var1[var2]) {
               protected Object getPropertyValue() {
                  return JTAMigratableTargetMBeanImpl.this.getConstrainedCandidateServers();
               }
            };
            this._getReferenceManager().registerResolvedReference((AbstractDescriptorBean)var1[var2], var3);
         }
      }

      ServerMBean[] var5 = this.getConstrainedCandidateServers();
      this._customizer.setConstrainedCandidateServers(var1);
      this._postSet(12, var5, var1);
   }

   public void setHostingServer(ServerMBean var1) {
      if (var1 != null) {
         ResolvedReference var2 = new ResolvedReference(this, 7, (AbstractDescriptorBean)var1) {
            protected Object getPropertyValue() {
               return JTAMigratableTargetMBeanImpl.this.getHostingServer();
            }
         };
         this._getReferenceManager().registerResolvedReference((AbstractDescriptorBean)var1, var2);
      }

      ServerMBean var3 = this._HostingServer;
      this._HostingServer = var1;
      this._postSet(7, var3, var1);
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

   public boolean addConstrainedCandidateServer(ServerMBean var1) throws InvalidAttributeValueException {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 12)) {
         ServerMBean[] var2 = (ServerMBean[])((ServerMBean[])this._getHelper()._extendArray(this.getConstrainedCandidateServers(), ServerMBean.class, var1));

         try {
            this.setConstrainedCandidateServers(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            if (var4 instanceof InvalidAttributeValueException) {
               throw (InvalidAttributeValueException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

      return true;
   }

   public ServerMBean getUserPreferredServer() {
      return this._customizer.getUserPreferredServer();
   }

   public String getUserPreferredServerAsString() {
      AbstractDescriptorBean var1 = (AbstractDescriptorBean)this.getUserPreferredServer();
      return var1 == null ? null : var1._getKey().toString();
   }

   public boolean isUserPreferredServerSet() {
      return this._isSet(8);
   }

   public void setUserPreferredServerAsString(String var1) {
      if (var1 != null && var1.length() != 0) {
         var1 = var1 == null ? null : var1.trim();
         this._getReferenceManager().registerUnresolvedReference(var1, ServerMBean.class, new ReferenceManager.Resolver(this, 8) {
            public void resolveReference(Object var1) {
               try {
                  JTAMigratableTargetMBeanImpl.this.setUserPreferredServer((ServerMBean)var1);
               } catch (RuntimeException var3) {
                  throw var3;
               } catch (Exception var4) {
                  throw new AssertionError("Impossible exception: " + var4);
               }
            }
         });
      } else {
         ServerMBean var2 = this._UserPreferredServer;
         this._initializeProperty(8);
         this._postSet(8, var2, this._UserPreferredServer);
      }

   }

   public boolean isStrictOwnershipCheck() {
      return this._StrictOwnershipCheck;
   }

   public boolean isStrictOwnershipCheckSet() {
      return this._isSet(24);
   }

   public boolean removeConstrainedCandidateServer(ServerMBean var1) throws InvalidAttributeValueException {
      ServerMBean[] var2 = this.getConstrainedCandidateServers();
      ServerMBean[] var3 = (ServerMBean[])((ServerMBean[])this._getHelper()._removeElement(var2, ServerMBean.class, var1));
      if (var3.length != var2.length) {
         try {
            this.setConstrainedCandidateServers(var3);
            return true;
         } catch (Exception var5) {
            if (var5 instanceof RuntimeException) {
               throw (RuntimeException)var5;
            } else if (var5 instanceof InvalidAttributeValueException) {
               throw (InvalidAttributeValueException)var5;
            } else {
               throw new UndeclaredThrowableException(var5);
            }
         }
      } else {
         return false;
      }
   }

   public void setUserPreferredServer(ServerMBean var1) {
      JTAMTCustomValidator.validateUserPreferredServer(this, var1);
      if (var1 != null) {
         ResolvedReference var2 = new ResolvedReference(this, 8, (AbstractDescriptorBean)var1) {
            protected Object getPropertyValue() {
               return JTAMigratableTargetMBeanImpl.this.getUserPreferredServer();
            }
         };
         this._getReferenceManager().registerResolvedReference((AbstractDescriptorBean)var1, var2);
      }

      ServerMBean var3 = this.getUserPreferredServer();
      this._customizer.setUserPreferredServer(var1);
      this._postSet(8, var3, var1);
   }

   public void setStrictOwnershipCheck(boolean var1) {
      boolean var2 = this._StrictOwnershipCheck;
      this._StrictOwnershipCheck = var1;
      this._postSet(24, var2, var1);
   }

   public ClusterMBean getCluster() {
      return this._customizer.getCluster();
   }

   public String getClusterAsString() {
      AbstractDescriptorBean var1 = (AbstractDescriptorBean)this.getCluster();
      return var1 == null ? null : var1._getKey().toString();
   }

   public boolean isClusterSet() {
      return this._isSet(13);
   }

   public void setClusterAsString(String var1) {
      if (var1 != null && var1.length() != 0) {
         var1 = var1 == null ? null : var1.trim();
         this._getReferenceManager().registerUnresolvedReference(var1, ClusterMBean.class, new ReferenceManager.Resolver(this, 13) {
            public void resolveReference(Object var1) {
               try {
                  JTAMigratableTargetMBeanImpl.this.setCluster((ClusterMBean)var1);
               } catch (RuntimeException var3) {
                  throw var3;
               } catch (Exception var4) {
                  throw new AssertionError("Impossible exception: " + var4);
               }
            }
         });
      } else {
         ClusterMBean var2 = this._Cluster;
         this._initializeProperty(13);
         this._postSet(13, var2, this._Cluster);
      }

   }

   public void setCluster(ClusterMBean var1) {
      if (var1 != null) {
         ResolvedReference var2 = new ResolvedReference(this, 13, (AbstractDescriptorBean)var1) {
            protected Object getPropertyValue() {
               return JTAMigratableTargetMBeanImpl.this.getCluster();
            }
         };
         this._getReferenceManager().registerResolvedReference((AbstractDescriptorBean)var1, var2);
      }

      ClusterMBean var3 = this._Cluster;
      this._Cluster = var1;
      this._postSet(13, var3, var1);
   }

   public void addAllCandidateServer(ServerMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 14)) {
         ServerMBean[] var2 = (ServerMBean[])((ServerMBean[])this._getHelper()._extendArray(this.getAllCandidateServers(), ServerMBean.class, var1));

         try {
            this.setAllCandidateServers(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public ServerMBean[] getAllCandidateServers() {
      return this._customizer.getAllCandidateServers();
   }

   public boolean isAllCandidateServersSet() {
      return this._isSet(14);
   }

   public void removeAllCandidateServer(ServerMBean var1) {
      ServerMBean[] var2 = this.getAllCandidateServers();
      ServerMBean[] var3 = (ServerMBean[])((ServerMBean[])this._getHelper()._removeElement(var2, ServerMBean.class, var1));
      if (var3.length != var2.length) {
         try {
            this.setAllCandidateServers(var3);
         } catch (Exception var5) {
            if (var5 instanceof RuntimeException) {
               throw (RuntimeException)var5;
            }

            throw new UndeclaredThrowableException(var5);
         }
      }

   }

   public void setAllCandidateServers(ServerMBean[] var1) {
      Object var2 = var1 == null ? new ServerMBeanImpl[0] : var1;
      this._AllCandidateServers = (ServerMBean[])var2;
   }

   public boolean isManualActiveOn(ServerMBean var1) {
      return this._customizer.isManualActiveOn(var1);
   }

   public boolean isCandidate(ServerMBean var1) {
      return this._customizer.isCandidate(var1);
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

   public String getMigrationPolicy() {
      return this._MigrationPolicy;
   }

   public boolean isMigrationPolicySet() {
      return this._isSet(16);
   }

   public void setMigrationPolicy(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{"manual", "failure-recovery"};
      var1 = weblogic.descriptor.beangen.LegalChecks.checkInEnum("MigrationPolicy", var1, var2);
      MTCustomValidator.validateMigrationPolicy(this, var1);
      String var3 = this._MigrationPolicy;
      this._MigrationPolicy = var1;
      this._postSet(16, var3, var1);
   }

   public Object _getKey() {
      return this.getName();
   }

   public void _validate() throws IllegalArgumentException {
      super._validate();
      MTCustomValidator.validateMigratableTarget(this);
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
         var1 = 14;
      }

      try {
         switch (var1) {
            case 14:
               this._AllCandidateServers = new ServerMBean[0];
               if (var2) {
                  break;
               }
            case 13:
               this._Cluster = null;
               if (var2) {
                  break;
               }
            case 12:
               this._customizer.setConstrainedCandidateServers(new ServerMBean[0]);
               if (var2) {
                  break;
               }
            case 7:
               this._HostingServer = null;
               if (var2) {
                  break;
               }
            case 16:
               this._MigrationPolicy = "manual";
               if (var2) {
                  break;
               }
            case 2:
               this._customizer.setName((String)null);
               if (var2) {
                  break;
               }
            case 11:
               this._ServerNames = null;
               if (var2) {
                  break;
               }
            case 8:
               this._customizer.setUserPreferredServer((ServerMBean)null);
               if (var2) {
                  break;
               }
            case 24:
               this._StrictOwnershipCheck = false;
               if (var2) {
                  break;
               }
            case 3:
            case 4:
            case 5:
            case 6:
            case 9:
            case 10:
            case 15:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
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
      return "JTAMigratableTarget";
   }

   public void putValue(String var1, Object var2) {
      ServerMBean[] var8;
      if (var1.equals("AllCandidateServers")) {
         var8 = this._AllCandidateServers;
         this._AllCandidateServers = (ServerMBean[])((ServerMBean[])var2);
         this._postSet(14, var8, this._AllCandidateServers);
      } else if (var1.equals("Cluster")) {
         ClusterMBean var9 = this._Cluster;
         this._Cluster = (ClusterMBean)var2;
         this._postSet(13, var9, this._Cluster);
      } else if (var1.equals("ConstrainedCandidateServers")) {
         var8 = this._ConstrainedCandidateServers;
         this._ConstrainedCandidateServers = (ServerMBean[])((ServerMBean[])var2);
         this._postSet(12, var8, this._ConstrainedCandidateServers);
      } else {
         ServerMBean var4;
         if (var1.equals("HostingServer")) {
            var4 = this._HostingServer;
            this._HostingServer = (ServerMBean)var2;
            this._postSet(7, var4, this._HostingServer);
         } else {
            String var7;
            if (var1.equals("MigrationPolicy")) {
               var7 = this._MigrationPolicy;
               this._MigrationPolicy = (String)var2;
               this._postSet(16, var7, this._MigrationPolicy);
            } else if (var1.equals("Name")) {
               var7 = this._Name;
               this._Name = (String)var2;
               this._postSet(2, var7, this._Name);
            } else if (var1.equals("ServerNames")) {
               Set var6 = this._ServerNames;
               this._ServerNames = (Set)var2;
               this._postSet(11, var6, this._ServerNames);
            } else if (var1.equals("StrictOwnershipCheck")) {
               boolean var5 = this._StrictOwnershipCheck;
               this._StrictOwnershipCheck = (Boolean)var2;
               this._postSet(24, var5, this._StrictOwnershipCheck);
            } else if (var1.equals("UserPreferredServer")) {
               var4 = this._UserPreferredServer;
               this._UserPreferredServer = (ServerMBean)var2;
               this._postSet(8, var4, this._UserPreferredServer);
            } else if (var1.equals("customizer")) {
               JTAMigratableTarget var3 = this._customizer;
               this._customizer = (JTAMigratableTarget)var2;
            } else {
               super.putValue(var1, var2);
            }
         }
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("AllCandidateServers")) {
         return this._AllCandidateServers;
      } else if (var1.equals("Cluster")) {
         return this._Cluster;
      } else if (var1.equals("ConstrainedCandidateServers")) {
         return this._ConstrainedCandidateServers;
      } else if (var1.equals("HostingServer")) {
         return this._HostingServer;
      } else if (var1.equals("MigrationPolicy")) {
         return this._MigrationPolicy;
      } else if (var1.equals("Name")) {
         return this._Name;
      } else if (var1.equals("ServerNames")) {
         return this._ServerNames;
      } else if (var1.equals("StrictOwnershipCheck")) {
         return new Boolean(this._StrictOwnershipCheck);
      } else if (var1.equals("UserPreferredServer")) {
         return this._UserPreferredServer;
      } else {
         return var1.equals("customizer") ? this._customizer : super.getValue(var1);
      }
   }

   public static class SchemaHelper2 extends MigratableTargetMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 4:
               if (var1.equals("name")) {
                  return 2;
               }
            case 5:
            case 6:
            case 8:
            case 9:
            case 10:
            case 11:
            case 13:
            case 15:
            case 17:
            case 18:
            case 19:
            case 23:
            case 24:
            case 25:
            case 26:
            case 27:
            default:
               break;
            case 7:
               if (var1.equals("cluster")) {
                  return 13;
               }
               break;
            case 12:
               if (var1.equals("server-names")) {
                  return 11;
               }
               break;
            case 14:
               if (var1.equals("hosting-server")) {
                  return 7;
               }
               break;
            case 16:
               if (var1.equals("migration-policy")) {
                  return 16;
               }
               break;
            case 20:
               if (var1.equals("all-candidate-server")) {
                  return 14;
               }
               break;
            case 21:
               if (var1.equals("user-preferred-server")) {
                  return 8;
               }
               break;
            case 22:
               if (var1.equals("strict-ownership-check")) {
                  return 24;
               }
               break;
            case 28:
               if (var1.equals("constrained-candidate-server")) {
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
            case 2:
               return "name";
            case 3:
            case 4:
            case 5:
            case 6:
            case 9:
            case 10:
            case 15:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            default:
               return super.getElementName(var1);
            case 7:
               return "hosting-server";
            case 8:
               return "user-preferred-server";
            case 11:
               return "server-names";
            case 12:
               return "constrained-candidate-server";
            case 13:
               return "cluster";
            case 14:
               return "all-candidate-server";
            case 16:
               return "migration-policy";
            case 24:
               return "strict-ownership-check";
         }
      }

      public boolean isArray(int var1) {
         switch (var1) {
            case 12:
               return true;
            case 14:
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

      public boolean hasKey() {
         return true;
      }

      public String[] getKeyElementNames() {
         ArrayList var1 = new ArrayList();
         var1.add("name");
         return (String[])((String[])var1.toArray(new String[0]));
      }
   }

   protected static class Helper extends MigratableTargetMBeanImpl.Helper {
      private JTAMigratableTargetMBeanImpl bean;

      protected Helper(JTAMigratableTargetMBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 2:
               return "Name";
            case 3:
            case 4:
            case 5:
            case 6:
            case 9:
            case 10:
            case 15:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            default:
               return super.getPropertyName(var1);
            case 7:
               return "HostingServer";
            case 8:
               return "UserPreferredServer";
            case 11:
               return "ServerNames";
            case 12:
               return "ConstrainedCandidateServers";
            case 13:
               return "Cluster";
            case 14:
               return "AllCandidateServers";
            case 16:
               return "MigrationPolicy";
            case 24:
               return "StrictOwnershipCheck";
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("AllCandidateServers")) {
            return 14;
         } else if (var1.equals("Cluster")) {
            return 13;
         } else if (var1.equals("ConstrainedCandidateServers")) {
            return 12;
         } else if (var1.equals("HostingServer")) {
            return 7;
         } else if (var1.equals("MigrationPolicy")) {
            return 16;
         } else if (var1.equals("Name")) {
            return 2;
         } else if (var1.equals("ServerNames")) {
            return 11;
         } else if (var1.equals("UserPreferredServer")) {
            return 8;
         } else {
            return var1.equals("StrictOwnershipCheck") ? 24 : super.getPropertyIndex(var1);
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
            if (this.bean.isAllCandidateServersSet()) {
               var2.append("AllCandidateServers");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getAllCandidateServers())));
            }

            if (this.bean.isClusterSet()) {
               var2.append("Cluster");
               var2.append(String.valueOf(this.bean.getCluster()));
            }

            if (this.bean.isConstrainedCandidateServersSet()) {
               var2.append("ConstrainedCandidateServers");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getConstrainedCandidateServers())));
            }

            if (this.bean.isHostingServerSet()) {
               var2.append("HostingServer");
               var2.append(String.valueOf(this.bean.getHostingServer()));
            }

            if (this.bean.isMigrationPolicySet()) {
               var2.append("MigrationPolicy");
               var2.append(String.valueOf(this.bean.getMigrationPolicy()));
            }

            if (this.bean.isNameSet()) {
               var2.append("Name");
               var2.append(String.valueOf(this.bean.getName()));
            }

            if (this.bean.isServerNamesSet()) {
               var2.append("ServerNames");
               var2.append(String.valueOf(this.bean.getServerNames()));
            }

            if (this.bean.isUserPreferredServerSet()) {
               var2.append("UserPreferredServer");
               var2.append(String.valueOf(this.bean.getUserPreferredServer()));
            }

            if (this.bean.isStrictOwnershipCheckSet()) {
               var2.append("StrictOwnershipCheck");
               var2.append(String.valueOf(this.bean.isStrictOwnershipCheck()));
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
            JTAMigratableTargetMBeanImpl var2 = (JTAMigratableTargetMBeanImpl)var1;
            this.computeDiff("Cluster", this.bean.getCluster(), var2.getCluster(), false);
            this.computeDiff("ConstrainedCandidateServers", this.bean.getConstrainedCandidateServers(), var2.getConstrainedCandidateServers(), false);
            this.computeDiff("HostingServer", this.bean.getHostingServer(), var2.getHostingServer(), true);
            this.computeDiff("MigrationPolicy", this.bean.getMigrationPolicy(), var2.getMigrationPolicy(), false);
            this.computeDiff("Name", this.bean.getName(), var2.getName(), false);
            this.computeDiff("UserPreferredServer", this.bean.getUserPreferredServer(), var2.getUserPreferredServer(), true);
            this.computeDiff("StrictOwnershipCheck", this.bean.isStrictOwnershipCheck(), var2.isStrictOwnershipCheck(), true);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            JTAMigratableTargetMBeanImpl var3 = (JTAMigratableTargetMBeanImpl)var1.getSourceBean();
            JTAMigratableTargetMBeanImpl var4 = (JTAMigratableTargetMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (!var5.equals("AllCandidateServers")) {
                  if (var5.equals("Cluster")) {
                     var3.setClusterAsString(var4.getClusterAsString());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 13);
                  } else if (var5.equals("ConstrainedCandidateServers")) {
                     var3.setConstrainedCandidateServersAsString(var4.getConstrainedCandidateServersAsString());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 12);
                  } else if (var5.equals("HostingServer")) {
                     var3.setHostingServerAsString(var4.getHostingServerAsString());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 7);
                  } else if (var5.equals("MigrationPolicy")) {
                     var3.setMigrationPolicy(var4.getMigrationPolicy());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 16);
                  } else if (var5.equals("Name")) {
                     var3.setName(var4.getName());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 2);
                  } else if (!var5.equals("ServerNames")) {
                     if (var5.equals("UserPreferredServer")) {
                        var3.setUserPreferredServerAsString(var4.getUserPreferredServerAsString());
                        var3._conditionalUnset(var2.isUnsetUpdate(), 8);
                     } else if (var5.equals("StrictOwnershipCheck")) {
                        var3.setStrictOwnershipCheck(var4.isStrictOwnershipCheck());
                        var3._conditionalUnset(var2.isUnsetUpdate(), 24);
                     } else {
                        super.applyPropertyUpdate(var1, var2);
                     }
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
            JTAMigratableTargetMBeanImpl var5 = (JTAMigratableTargetMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("Cluster")) && this.bean.isClusterSet()) {
               var5._unSet(var5, 13);
               var5.setClusterAsString(this.bean.getClusterAsString());
            }

            if ((var3 == null || !var3.contains("ConstrainedCandidateServers")) && this.bean.isConstrainedCandidateServersSet()) {
               var5._unSet(var5, 12);
               var5.setConstrainedCandidateServersAsString(this.bean.getConstrainedCandidateServersAsString());
            }

            if ((var3 == null || !var3.contains("HostingServer")) && this.bean.isHostingServerSet()) {
               var5._unSet(var5, 7);
               var5.setHostingServerAsString(this.bean.getHostingServerAsString());
            }

            if ((var3 == null || !var3.contains("MigrationPolicy")) && this.bean.isMigrationPolicySet()) {
               var5.setMigrationPolicy(this.bean.getMigrationPolicy());
            }

            if ((var3 == null || !var3.contains("Name")) && this.bean.isNameSet()) {
               var5.setName(this.bean.getName());
            }

            if ((var3 == null || !var3.contains("UserPreferredServer")) && this.bean.isUserPreferredServerSet()) {
               var5._unSet(var5, 8);
               var5.setUserPreferredServerAsString(this.bean.getUserPreferredServerAsString());
            }

            if ((var3 == null || !var3.contains("StrictOwnershipCheck")) && this.bean.isStrictOwnershipCheckSet()) {
               var5.setStrictOwnershipCheck(this.bean.isStrictOwnershipCheck());
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
         this.inferSubTree(this.bean.getAllCandidateServers(), var1, var2);
         this.inferSubTree(this.bean.getCluster(), var1, var2);
         this.inferSubTree(this.bean.getConstrainedCandidateServers(), var1, var2);
         this.inferSubTree(this.bean.getHostingServer(), var1, var2);
         this.inferSubTree(this.bean.getUserPreferredServer(), var1, var2);
      }
   }
}
