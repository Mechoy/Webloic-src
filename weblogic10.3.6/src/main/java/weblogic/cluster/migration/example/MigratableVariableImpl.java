package weblogic.cluster.migration.example;

import java.io.Serializable;
import java.security.AccessController;
import weblogic.cluster.migration.MigratableRemote;
import weblogic.cluster.migration.MigrationException;
import weblogic.cluster.migration.rmiservice.Initialization;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public class MigratableVariableImpl implements MigratableVariable, MigratableRemote, Initialization {
   private Serializable value = null;
   private String name = null;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public synchronized void set(Serializable var1) {
      this.writeToStore(var1);
      this.value = var1;
   }

   public synchronized Serializable get() {
      return this.value;
   }

   public void migratableInitialize() {
      System.err.println("initialization called on " + this);
   }

   public void migratableActivate() throws MigrationException {
      System.err.println("activating ... " + this);
      this.value = this.readFromStore();
      System.err.println(" done.");
   }

   public void migratableDeactivate() throws MigrationException {
      System.err.println("deactivating ... " + this);
      System.err.println(" done.");
   }

   private void writeToStore(Serializable var1) {
   }

   private Serializable readFromStore() {
      return null;
   }

   public String whereAmI() {
      return ManagementService.getRuntimeAccess(kernelId).getServerName();
   }

   public String toString() {
      return this.name + " (" + this.getClass() + ")";
   }

   public void initialize(String var1) {
      this.name = var1;
   }

   public void destroy(String var1) {
   }

   public int hashCode() {
      return this.name.hashCode();
   }

   public boolean equals(Object var1) {
      return var1 instanceof MigratableVariableImpl ? ((MigratableVariableImpl)var1).getName().equals(this.name) : false;
   }

   public String getName() {
      return this.name;
   }

   public int getOrder() {
      return 100;
   }
}
