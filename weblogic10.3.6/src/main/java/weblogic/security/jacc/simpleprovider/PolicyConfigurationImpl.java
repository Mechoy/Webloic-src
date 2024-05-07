package weblogic.security.jacc.simpleprovider;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.security.Permission;
import java.security.PermissionCollection;
import java.security.Permissions;
import java.security.Security;
import java.security.SecurityPermission;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.security.jacc.PolicyConfiguration;
import javax.security.jacc.PolicyContextException;
import sun.security.provider.PolicyFile;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.logging.Loggable;
import weblogic.security.SecurityLogger;
import weblogic.security.jacc.RoleMapperFactory;
import weblogic.utils.AssertionError;

public class PolicyConfigurationImpl implements PolicyConfiguration {
   private PolicyConfiguration nextPC;
   private PolicyConfigurationFactoryImpl pcFactoryImpl;
   private State state;
   private Permissions excludedPermissions = null;
   private Permissions uncheckedPermissions = null;
   private Map rolesToPermissions;
   private String contextId;
   private String contextIdNoNPE;
   private static DebugLogger jaccDebugLogger = DebugLogger.getDebugLogger("DebugSecurityJACCNonPolicy");
   private String appSpecificPolicyDirectory = null;
   private PolicyFile policy = null;
   private boolean policyChanged = false;
   private boolean needRefreshing = false;
   private static String POLICY_URL = "policy.url.";
   private static Object refreshLock = new Object();
   private static Object mapperLock = new Object();
   private String policyFileName;
   private String urlForPolicyFile;
   private static RoleMapperFactory roleMapperFactory;

   public PolicyConfigurationImpl(String var1, PolicyConfigurationFactoryImpl var2) {
      this.pcFactoryImpl = var2;
      this.contextId = var1;
      this.state = State.OPEN;
      this.nextPC = null;
      this.excludedPermissions = new Permissions();
      this.uncheckedPermissions = new Permissions();
      this.rolesToPermissions = new HashMap();
      this.contextIdNoNPE = this.contextId == null ? "null" : this.contextId;
      PolicyWriter.createRepositoryDirectory();
      this.appSpecificPolicyDirectory = PolicyWriter.generateAppDirectoryFileName(this.contextIdNoNPE);
      if (this.appSpecificPolicyDirectory == null) {
         throw new AssertionError(SecurityLogger.getUnexpectedNullVariable("appSpecificPolicyDirectory"));
      } else {
         try {
            this.policyFileName = this.appSpecificPolicyDirectory + File.separator + "granted.policy";
            File var3 = new File(this.policyFileName);
            var3 = var3.getAbsoluteFile();
            URI var4 = var3.toURI();
            URL var5 = var4.toURL();
            this.urlForPolicyFile = var5.toExternalForm();
         } catch (MalformedURLException var9) {
            throw new RuntimeException(SecurityLogger.getUnableToConvertFiletoURL(this.policyFileName, var9), var9);
         }

         synchronized(mapperLock) {
            if (roleMapperFactory == null) {
               try {
                  roleMapperFactory = RoleMapperFactory.getRoleMapperFactory();
               } catch (ClassNotFoundException var7) {
                  throw new RuntimeException(SecurityLogger.logRoleMapperFactoryProblemLoggable().getMessageText(), var7);
               } catch (PolicyContextException var8) {
                  throw new RuntimeException(SecurityLogger.logRoleMapperFactoryProblemLoggable().getMessageText(), var8);
               }
            }
         }

         if (jaccDebugLogger.isDebugEnabled()) {
            jaccDebugLogger.debug("PolicyConfigurationImpl constructor contextId: " + this.contextIdNoNPE + " appSpecificPolicyDirectory of: " + this.appSpecificPolicyDirectory);
         }

      }
   }

   public void addToExcludedPolicy(Permission var1) throws PolicyContextException {
      SecurityManager var2 = System.getSecurityManager();
      if (var2 != null) {
         var2.checkPermission(new SecurityPermission("setPolicy"));
      }

      if (State.OPEN != this.state) {
         if (jaccDebugLogger.isDebugEnabled()) {
            jaccDebugLogger.debug("PolicyConfigurationImpl: addToExcludedPolicy called on a non-open PolicyConfiguration.");
         }

         throw new UnsupportedOperationException(SecurityLogger.getPolicyContextNotOpen(this.contextIdNoNPE));
      } else if (null == var1) {
         if (jaccDebugLogger.isDebugEnabled()) {
            jaccDebugLogger.debug("PolicyConfigurationImpl: addToExcludedPolicy was passed a null Permission, ignoring.");
         }

      } else {
         this.excludedPermissions.add(var1);
         this.policyChanged = true;
         if (jaccDebugLogger.isDebugEnabled()) {
            jaccDebugLogger.debug("PolicyConfigurationImpl: addToExcludedPolicy contextId: " + this.contextIdNoNPE + " added " + var1);
         }

      }
   }

   public void addToExcludedPolicy(PermissionCollection var1) throws PolicyContextException {
      SecurityManager var2 = System.getSecurityManager();
      if (var2 != null) {
         var2.checkPermission(new SecurityPermission("setPolicy"));
      }

      if (State.OPEN != this.state) {
         if (jaccDebugLogger.isDebugEnabled()) {
            jaccDebugLogger.debug("PolicyConfigurationImpl: addToExcludedPolicy called on a non-open PolicyConfiguration.");
         }

         throw new UnsupportedOperationException(SecurityLogger.getPolicyContextNotOpen(this.contextIdNoNPE));
      } else if (null != var1 && var1.elements().hasMoreElements()) {
         Enumeration var3 = var1.elements();

         while(var3.hasMoreElements()) {
            Permission var4 = (Permission)var3.nextElement();
            if (var4 != null) {
               this.excludedPermissions.add(var4);
               this.policyChanged = true;
               if (jaccDebugLogger.isDebugEnabled()) {
                  jaccDebugLogger.debug("PolicyConfigurationImpl: addToExcludedPolicy(s) contextId: " + this.contextIdNoNPE + " added " + var4);
               }
            } else if (jaccDebugLogger.isDebugEnabled()) {
               jaccDebugLogger.debug("PolicyConfigurationImpl: addToExcludedPolicy(s) contains a null Permission, ignoring.");
            }
         }

      } else {
         if (jaccDebugLogger.isDebugEnabled()) {
            jaccDebugLogger.debug("PolicyConfigurationImpl: addToExcludedPolicy was passed a null or empty PermissionCollection, ignoring.");
         }

      }
   }

   protected Permissions getExcludedPermissions() {
      return this.state != State.INSERVICE ? null : this.excludedPermissions;
   }

   public void addToRole(String var1, Permission var2) throws PolicyContextException {
      SecurityManager var3 = System.getSecurityManager();
      if (var3 != null) {
         var3.checkPermission(new SecurityPermission("setPolicy"));
      }

      if (State.OPEN != this.state) {
         if (jaccDebugLogger.isDebugEnabled()) {
            jaccDebugLogger.debug("PolicyConfigurationImpl: addToRole called on a non-open PolicyConfiguration.");
         }

         throw new UnsupportedOperationException(SecurityLogger.getPolicyContextNotOpen(this.contextIdNoNPE));
      } else if (null == var1) {
         if (jaccDebugLogger.isDebugEnabled()) {
            jaccDebugLogger.debug("PolicyConfigurationImpl: addToRole was passed a null roleName, ignoring.");
         }

      } else if (null == var2) {
         if (jaccDebugLogger.isDebugEnabled()) {
            jaccDebugLogger.debug("PolicyConfigurationImpl: addToRole was passed a null Permission, ignoring.");
         }

      } else {
         Object var4 = (List)this.rolesToPermissions.get(var1);
         if (null == var4) {
            var4 = new ArrayList();
         }

         ((List)var4).add(var2);
         this.rolesToPermissions.put(var1, var4);
         this.policyChanged = true;
         if (jaccDebugLogger.isDebugEnabled()) {
            jaccDebugLogger.debug("PolicyConfigurationImpl: addToRole contextId: " + this.contextIdNoNPE + " added " + var2 + " for role " + var1);
            StringBuffer var5 = new StringBuffer("PolicyConfigurationImpl: addToRole contextId: " + this.contextIdNoNPE + " role " + var1 + " has the following permissions:\n");
            Iterator var6 = ((List)var4).iterator();

            while(var6.hasNext()) {
               var5.append((Permission)var6.next() + "\n");
            }

            jaccDebugLogger.debug(var5.toString());
         }

      }
   }

   public void addToRole(String var1, PermissionCollection var2) throws PolicyContextException {
      SecurityManager var3 = System.getSecurityManager();
      if (var3 != null) {
         var3.checkPermission(new SecurityPermission("setPolicy"));
      }

      if (State.OPEN != this.state) {
         if (jaccDebugLogger.isDebugEnabled()) {
            jaccDebugLogger.debug("PolicyConfigurationImpl: addToRole called on a non-open PolicyConfiguration.");
         }

         throw new UnsupportedOperationException(SecurityLogger.getPolicyContextNotOpen(this.contextIdNoNPE));
      } else if (null == var1) {
         if (jaccDebugLogger.isDebugEnabled()) {
            jaccDebugLogger.debug("PolicyConfigurationImpl: addToRole was passed a null roleName, ignoring.");
         }

      } else if (null != var2 && var2.elements().hasMoreElements()) {
         Object var4 = (List)this.rolesToPermissions.get(var1);
         if (null == var4) {
            var4 = new ArrayList();
         }

         Enumeration var5 = var2.elements();

         while(var5.hasMoreElements()) {
            Permission var6 = (Permission)var5.nextElement();
            if (var6 != null) {
               ((List)var4).add(var6);
               if (jaccDebugLogger.isDebugEnabled()) {
                  jaccDebugLogger.debug("PolicyConfigurationImpl: addToRole(s) contextId: " + this.contextIdNoNPE + " added " + var6 + " for role " + var1);
               }
            } else if (jaccDebugLogger.isDebugEnabled()) {
               jaccDebugLogger.debug("PolicyConfigurationImpl: addToRole(s) contains a null Permission, ignoring.");
            }
         }

         this.rolesToPermissions.put(var1, var4);
         this.policyChanged = true;
         if (jaccDebugLogger.isDebugEnabled()) {
            StringBuffer var7 = new StringBuffer("PolicyConfigurationImpl: addToRole contextId: " + this.contextIdNoNPE + " role " + var1 + " has the following permissions:\n");
            Iterator var8 = ((List)var4).iterator();

            while(var8.hasNext()) {
               var7.append((Permission)var8.next() + "\n");
            }

            jaccDebugLogger.debug(var7.toString());
         }

      } else {
         if (jaccDebugLogger.isDebugEnabled()) {
            jaccDebugLogger.debug("PolicyConfigurationImpl: addToRole was passed a null or empty PermissionCollection, ignoring.");
         }

      }
   }

   public void addToUncheckedPolicy(Permission var1) throws PolicyContextException {
      SecurityManager var2 = System.getSecurityManager();
      if (var2 != null) {
         var2.checkPermission(new SecurityPermission("setPolicy"));
      }

      if (State.OPEN != this.state) {
         if (jaccDebugLogger.isDebugEnabled()) {
            jaccDebugLogger.debug("PolicyConfigurationImpl: addToUncheckedPolicy called on a non-open PolicyConfiguration.");
         }

         throw new UnsupportedOperationException(SecurityLogger.getPolicyContextNotOpen(this.contextIdNoNPE));
      } else if (null == var1) {
         if (jaccDebugLogger.isDebugEnabled()) {
            jaccDebugLogger.debug("PolicyConfigurationImpl: addToUncheckedPolicy was passed a null Permission, ignoring.");
         }

      } else {
         this.uncheckedPermissions.add(var1);
         this.policyChanged = true;
         if (jaccDebugLogger.isDebugEnabled()) {
            jaccDebugLogger.debug("PolicyConfigurationImpl: addToUncheckedPolicy contextId: " + this.contextIdNoNPE + " added " + var1);
         }

      }
   }

   public void addToUncheckedPolicy(PermissionCollection var1) throws PolicyContextException {
      SecurityManager var2 = System.getSecurityManager();
      if (var2 != null) {
         var2.checkPermission(new SecurityPermission("setPolicy"));
      }

      if (State.OPEN != this.state) {
         if (jaccDebugLogger.isDebugEnabled()) {
            jaccDebugLogger.debug("PolicyConfigurationImpl: addToUncheckedPolicy called on a non-open PolicyConfiguration.");
         }

         throw new UnsupportedOperationException(SecurityLogger.getPolicyContextNotOpen(this.contextIdNoNPE));
      } else if (null != var1 && var1.elements().hasMoreElements()) {
         Enumeration var3 = var1.elements();

         while(var3.hasMoreElements()) {
            Permission var4 = (Permission)var3.nextElement();
            if (var4 != null) {
               this.uncheckedPermissions.add(var4);
               this.policyChanged = true;
               if (jaccDebugLogger.isDebugEnabled()) {
                  jaccDebugLogger.debug("PolicyConfigurationImpl: addToUncheckedPolicy(s) contextId: " + this.contextIdNoNPE + " added " + var4);
               }
            } else if (jaccDebugLogger.isDebugEnabled()) {
               jaccDebugLogger.debug("PolicyConfigurationImpl: addToUncheckedPolicy(s) contains a null Permission, ignoring.");
            }
         }

      } else {
         if (jaccDebugLogger.isDebugEnabled()) {
            jaccDebugLogger.debug("PolicyConfigurationImpl: addToUncheckedPolicy was passed a null or empty PermissionCollection, ignoring.");
         }

      }
   }

   public void commit() throws PolicyContextException {
      SecurityManager var1 = System.getSecurityManager();
      if (var1 != null) {
         var1.checkPermission(new SecurityPermission("setPolicy"));
      }

      synchronized(refreshLock) {
         if (jaccDebugLogger.isDebugEnabled()) {
            jaccDebugLogger.debug("PolicyConfigurationImpl:commit contextId: " + this.contextIdNoNPE);
         }

         if (State.DELETED == this.state) {
            if (jaccDebugLogger.isDebugEnabled()) {
               jaccDebugLogger.debug("PolicyConfigurationImpl:commit policy context in deleted state, unable to commit. contextId: " + this.contextIdNoNPE);
            }

            Loggable var8 = SecurityLogger.getPolicyContextNotOpenLoggable(this.contextIdNoNPE);
            throw new UnsupportedOperationException(var8.getMessageText());
         } else {
            RoleMapperImpl var3 = (RoleMapperImpl)roleMapperFactory.getRoleMapperForContextID(this.contextId);
            if (var3 == null) {
               if (jaccDebugLogger.isDebugEnabled()) {
                  jaccDebugLogger.debug("PolicyConfigurationImpl:commit no rolemapper exists for contextId: " + this.contextIdNoNPE + ". This may be OK.");
               }
            } else {
               HashMap var4 = (HashMap)var3.getRolesToPrincipalNames();
               if (jaccDebugLogger.isDebugEnabled() && (var4 == null || var4.size() == 0)) {
                  jaccDebugLogger.debug("PolicyConfigurationImpl:commit no role to principal mappings have been defined for contextId: " + this.contextIdNoNPE + ". This may be OK.");
               }
            }

            String var9 = this.excludedToString();
            String var5 = this.uncheckedGrantedToString();
            if (this.policyChanged) {
               if (jaccDebugLogger.isDebugEnabled()) {
                  jaccDebugLogger.debug("PolicyConfigurationImpl:commit policy changed writing out statements");
               }

               PolicyWriter.createAppDirectory(this.appSpecificPolicyDirectory);
               PolicyWriter.writeGrantStatements(this.appSpecificPolicyDirectory, "excluded", var9);
               PolicyWriter.writeGrantStatements(this.appSpecificPolicyDirectory, "granted", var5);
               this.policyChanged = false;
            } else if (jaccDebugLogger.isDebugEnabled()) {
               jaccDebugLogger.debug("PolicyConfigurationImpl:commit policy did not change. No statements will be written.");
            }

            if (jaccDebugLogger.isDebugEnabled()) {
               jaccDebugLogger.debug("PolicyConfigurationImpl:commit contextId: " + this.contextIdNoNPE + " transitioned from " + this.state + " to " + State.INSERVICE);
            }

            this.state = State.INSERVICE;
            this.needRefreshing = true;
         }
      }
   }

   public void delete() throws PolicyContextException {
      SecurityManager var1 = System.getSecurityManager();
      if (var1 != null) {
         var1.checkPermission(new SecurityPermission("setPolicy"));
      }

      synchronized(refreshLock) {
         if (jaccDebugLogger.isDebugEnabled()) {
            jaccDebugLogger.debug("PolicyConfigurationImpl: delete contextId: " + this.contextIdNoNPE);
         }

         this.internalRemoveExcludedPolicy();
         this.internalRemoveUncheckedPolicy();
         this.rolesToPermissions.clear();
         PolicyConfigurationFactoryImpl.removePolicyConfiguration(this.contextId);
         this.nextPC = null;

         try {
            PolicyWriter.deletePolicyFiles(this.appSpecificPolicyDirectory);
         } catch (IOException var5) {
            if (jaccDebugLogger.isDebugEnabled()) {
               jaccDebugLogger.debug(" Caught an IOException while trying to delete " + this.appSpecificPolicyDirectory + " and the .policy files in it");
            }

            throw new PolicyContextException(SecurityLogger.getUnableToDeletePolicyDirectory(var5, this.appSpecificPolicyDirectory));
         }

         this.policy = null;
         State var3 = this.state;
         this.state = State.DELETED;
         this.policyChanged = false;
         if (jaccDebugLogger.isDebugEnabled()) {
            jaccDebugLogger.debug("PolicyConfigurationImpl: delete contextId: " + this.contextIdNoNPE + " transitioned from " + var3 + " to " + this.state);
         }

      }
   }

   public String getContextID() throws PolicyContextException {
      return this.contextId;
   }

   public boolean inService() throws PolicyContextException {
      SecurityManager var1 = System.getSecurityManager();
      if (var1 != null) {
         var1.checkPermission(new SecurityPermission("setPolicy"));
      }

      return this.state == State.INSERVICE;
   }

   public void linkConfiguration(PolicyConfiguration var1) throws PolicyContextException {
      SecurityManager var2 = System.getSecurityManager();
      if (var2 != null) {
         var2.checkPermission(new SecurityPermission("setPolicy"));
      }

      if (jaccDebugLogger.isDebugEnabled()) {
         jaccDebugLogger.debug("PolicyConfigurationImpl: linkConfiguration for PC with contextId: " + this.contextIdNoNPE);
      }

      if (State.OPEN != this.state) {
         if (jaccDebugLogger.isDebugEnabled()) {
            jaccDebugLogger.debug("PolicyConfigurationImpl: addToUncheckedPolicy called on a non-open PolicyConfiguration.");
         }

         throw new UnsupportedOperationException(SecurityLogger.getPolicyContextNotOpen(this.contextIdNoNPE));
      } else if (var1 == null) {
         if (jaccDebugLogger.isDebugEnabled()) {
            jaccDebugLogger.debug("PolicyConfigurationImpl: linkConfiguration received a null link, ignoring.");
         }

      } else if (var1 == this) {
         if (jaccDebugLogger.isDebugEnabled()) {
            jaccDebugLogger.debug("PolicyConfigurationImpl: linkConfiguration Cannot link to self");
         }

         throw new IllegalArgumentException(SecurityLogger.getCannotLinkPolicyConfigurationToSelf());
      } else {
         if (var1 instanceof PolicyConfigurationImpl) {
            PolicyConfiguration var3 = ((PolicyConfigurationImpl)var1).getLink();

            while(var3 != null) {
               if (jaccDebugLogger.isDebugEnabled()) {
                  jaccDebugLogger.debug("PolicyConfigurationImpl: linkConfiguration following link to: " + var1.getContextID());
               }

               if (var3 == this || var3 == var1) {
                  throw new PolicyContextException(SecurityLogger.getCannotHaveCircularPolicyConfigurationLinks());
               }

               if (var3 instanceof PolicyConfigurationImpl) {
                  var3 = ((PolicyConfigurationImpl)var3).getLink();
               }
            }
         }

         this.nextPC = var1;
         this.policyChanged = true;
         if (jaccDebugLogger.isDebugEnabled()) {
            jaccDebugLogger.debug("PolicyConfigurationImpl: linkConfiguration for PC with contextId: " + this.contextIdNoNPE + " linked to contextId " + (var1.getContextID() == null ? "null" : var1.getContextID()));
         }

      }
   }

   private void internalRemoveExcludedPolicy() throws PolicyContextException {
      this.excludedPermissions = new Permissions();
      this.policyChanged = true;
      if (jaccDebugLogger.isDebugEnabled()) {
         jaccDebugLogger.debug("PolicyConfigurationImpl: internalRemoveExcludedPolicy contextId: " + this.contextIdNoNPE);
      }

   }

   public void removeExcludedPolicy() throws PolicyContextException {
      SecurityManager var1 = System.getSecurityManager();
      if (var1 != null) {
         var1.checkPermission(new SecurityPermission("setPolicy"));
      }

      if (State.OPEN != this.state) {
         if (jaccDebugLogger.isDebugEnabled()) {
            jaccDebugLogger.debug("PolicyConfigurationImpl: removeExcludedPolicy called on a non-open PolicyConfiguration.");
         }

         throw new UnsupportedOperationException(SecurityLogger.getPolicyContextNotOpen(this.contextIdNoNPE));
      } else {
         this.excludedPermissions = new Permissions();
         this.policyChanged = true;
         if (jaccDebugLogger.isDebugEnabled()) {
            jaccDebugLogger.debug("PolicyConfigurationImpl: removeExcludedPolicy contextId: " + this.contextIdNoNPE);
         }

      }
   }

   public void removeRole(String var1) throws PolicyContextException {
      SecurityManager var2 = System.getSecurityManager();
      if (var2 != null) {
         var2.checkPermission(new SecurityPermission("setPolicy"));
      }

      if (State.OPEN != this.state) {
         if (jaccDebugLogger.isDebugEnabled()) {
            jaccDebugLogger.debug("PolicyConfigurationImpl: removeRole called on a non-open PolicyConfiguration.");
         }

         throw new UnsupportedOperationException(SecurityLogger.getPolicyContextNotOpen(this.contextIdNoNPE));
      } else if (var1 == null) {
         if (jaccDebugLogger.isDebugEnabled()) {
            jaccDebugLogger.debug("PolicyConfigurationImpl: removeRole received a null roleName, ignoring.");
         }

      } else {
         if (this.rolesToPermissions.containsKey(var1)) {
            this.rolesToPermissions.remove(var1);
            this.policyChanged = true;
         }

         if (jaccDebugLogger.isDebugEnabled()) {
            jaccDebugLogger.debug("PolicyConfigurationImpl: removeRole completed contextId: " + this.contextIdNoNPE + " roleName: " + var1);
         }

      }
   }

   private void internalRemoveUncheckedPolicy() throws PolicyContextException {
      this.uncheckedPermissions = new Permissions();
      this.policyChanged = true;
      if (jaccDebugLogger.isDebugEnabled()) {
         jaccDebugLogger.debug("PolicyConfigurationImpl: internalRemoveUncheckedPolicy contextId: " + this.contextIdNoNPE);
      }

   }

   public void removeUncheckedPolicy() throws PolicyContextException {
      SecurityManager var1 = System.getSecurityManager();
      if (var1 != null) {
         var1.checkPermission(new SecurityPermission("setPolicy"));
      }

      if (State.OPEN != this.state) {
         if (jaccDebugLogger.isDebugEnabled()) {
            jaccDebugLogger.debug("PolicyConfigurationImpl: removeUncheckedPolicy called on a non-open PolicyConfiguration.");
         }

         throw new UnsupportedOperationException(SecurityLogger.getPolicyContextNotOpen(this.contextIdNoNPE));
      } else {
         this.uncheckedPermissions = new Permissions();
         this.policyChanged = true;
         if (jaccDebugLogger.isDebugEnabled()) {
            jaccDebugLogger.debug("PolicyConfigurationImpl: removeUncheckedPolicy contextId: " + this.contextIdNoNPE);
         }

      }
   }

   private String uncheckedGrantedToString() {
      String var1 = null;
      if (this.uncheckedPermissions != null && this.uncheckedPermissions.elements().hasMoreElements() || this.rolesToPermissions != null && !this.rolesToPermissions.isEmpty()) {
         StringBuffer var2 = new StringBuffer(GrantGenerator.generateHeader());
         var2.append(GrantGenerator.generateUncheckedGrants(this.contextId, this.uncheckedPermissions));
         Map var3 = this.getRolesToPrincipalMapping();
         if (var3 != null && !var3.isEmpty()) {
            HashMap var4 = new HashMap(var3);
            Set var5 = var3.keySet();
            Iterator var6 = var5.iterator();

            while(var6.hasNext()) {
               String var7 = (String)var6.next();
               if (!this.rolesToPermissions.containsKey(var7)) {
                  if (jaccDebugLogger.isDebugEnabled()) {
                     jaccDebugLogger.debug("PolicyConfigurationImpl: uncheckedGrantedToString contextId: " + this.contextIdNoNPE + " role: " + var7 + " isn't in rolesToPermissions, removing.");
                  }

                  var4.remove(var7);
               }
            }

            var2.append(GrantGenerator.generateRoleGrants(this.contextId, var4, this.rolesToPermissions));
         }

         var1 = var2.toString();
      }

      return var1;
   }

   private String excludedToString() {
      String var1 = null;
      if (this.excludedPermissions != null && this.excludedPermissions.elements().hasMoreElements()) {
         StringBuffer var2 = new StringBuffer(GrantGenerator.generateHeader());
         var2.append(GrantGenerator.generateExcludedGrants(this.contextId, this.excludedPermissions));
         var1 = var2.toString();
      }

      return var1;
   }

   private Map getRolesToPrincipalMapping() {
      SecurityManager var1 = System.getSecurityManager();
      if (var1 != null) {
         var1.checkPermission(new SecurityPermission("setPolicy"));
      }

      RoleMapperImpl var2 = null;
      Object var3 = new HashMap();
      var2 = (RoleMapperImpl)roleMapperFactory.getRoleMapperForContextID(this.contextId);
      if (var2 != null) {
         if (jaccDebugLogger.isDebugEnabled()) {
            jaccDebugLogger.debug("PolicyConfigurationImpl:getRolesToPrincipalMapping found a mapping for contextID: " + this.contextIdNoNPE);
         }

         var3 = var2.getRolesToPrincipalNames();
      } else if (jaccDebugLogger.isDebugEnabled()) {
         jaccDebugLogger.debug("PolicyConfigurationImpl:getRolesToPrincipalMapping finds no mappings for contextID: " + this.contextIdNoNPE);
      }

      return (Map)var3;
   }

   PolicyConfiguration getLink() {
      return this.nextPC;
   }

   public State getState() {
      return this.state;
   }

   void setStateOpen() {
      this.state = State.OPEN;
   }

   public String getAppSpecificPolicyDirectoryName() {
      return new String(this.appSpecificPolicyDirectory);
   }

   public void refresh() {
      synchronized(refreshLock) {
         if (this.needRefreshing) {
            State var10001 = this.state;
            if (this.state == State.INSERVICE) {
               int var2 = 0;
               String var3 = null;
               String var4 = null;

               do {
                  StringBuilder var10000 = (new StringBuilder()).append(POLICY_URL);
                  ++var2;
                  var4 = var10000.append(var2).toString();
                  var3 = Security.getProperty(var4);
               } while(var3 != null && !var3.equals(""));

               try {
                  Security.setProperty(var4, this.urlForPolicyFile);
                  if (jaccDebugLogger.isDebugEnabled()) {
                     jaccDebugLogger.debug("PolicyConfigurationImpl:refresh add url: " + var4 + ": " + this.urlForPolicyFile);
                  }

                  if (this.policy == null) {
                     this.policy = new PolicyFile();
                  } else {
                     this.policy.refresh();
                     if (jaccDebugLogger.isDebugEnabled()) {
                        jaccDebugLogger.debug("PolicyConfigurationImpl:refresh for ContextID: " + this.contextIdNoNPE);
                     }
                  }

                  this.needRefreshing = false;
               } finally {
                  Security.setProperty(var4, "");
                  if (jaccDebugLogger.isDebugEnabled()) {
                     jaccDebugLogger.debug("PolicyConfigurationImpl:refresh url: " + var4 + " reset to \"\" ");
                  }

               }
            }
         }

      }
   }

   protected PolicyFile getPolicy() {
      synchronized(refreshLock) {
         return this.state == State.INSERVICE ? this.policy : null;
      }
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append("\nPolicyConfiguration for context: " + this.contextIdNoNPE);
      var1.append("\nState: " + this.state);
      var1.append("\nAppSpecificPolicyDirectory: " + this.appSpecificPolicyDirectory);

      try {
         var1.append("\nLinked to PolicyConfiguration with contextId: " + (this.nextPC == null ? "null" : this.nextPC.getContextID()));
      } catch (PolicyContextException var4) {
         var1.append("\nLinked to PolicyConfiguration with contextId: Got an exception attempting to access!");
      }

      String var2 = this.excludedToString();
      if (var2 != null) {
         var1.append("\n" + var2);
      }

      String var3 = this.uncheckedGrantedToString();
      if (var3 != null) {
         var1.append("\n" + var3);
      }

      var1.append("\n\n");
      return var1.toString();
   }
}
