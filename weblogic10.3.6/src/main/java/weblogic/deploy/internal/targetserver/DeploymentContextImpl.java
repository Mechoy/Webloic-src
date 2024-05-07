package weblogic.deploy.internal.targetserver;

import java.io.InputStream;
import weblogic.application.Deployment;
import weblogic.deploy.container.DeploymentContext;
import weblogic.management.configuration.DomainMBean;
import weblogic.security.acl.internal.AuthenticatedSubject;

public class DeploymentContextImpl implements DeploymentContext {
   private DomainMBean proposedDomain = null;
   private String[] updatedResources = null;
   private String[] stoppedModuleIds = null;
   private final AuthenticatedSubject initiator;
   private Deployment.AdminModeCallback adminModeCallback = null;
   private boolean isAdminModeTransition;
   private boolean isIgnoreSessionsEnabled;
   private int rmiGracePeriodSecs;
   private String[] userSuppliedTargets = null;
   private boolean requiresRestart = false;
   private int deploymentOperation;
   private boolean isStatic = false;
   private boolean isStaged = false;

   public DeploymentContextImpl(AuthenticatedSubject var1) {
      this.initiator = var1;
   }

   public final DomainMBean getProposedDomain() {
      return this.proposedDomain;
   }

   public final void setProposedDomain(DomainMBean var1) {
      if (this.proposedDomain == null) {
         this.proposedDomain = var1;
      }

   }

   public final String[] getUpdatedResourceURIs() {
      return this.updatedResources == null ? new String[0] : this.updatedResources;
   }

   public final void setUpdatedResourceURIs(String[] var1) {
      if (this.updatedResources == null) {
         this.updatedResources = var1;
      }

   }

   public String[] getStoppedModules() {
      return this.stoppedModuleIds;
   }

   public void setStoppedModules(String[] var1) {
      this.stoppedModuleIds = var1;
   }

   public final AuthenticatedSubject getInitiator() {
      return this.initiator;
   }

   public final Deployment.AdminModeCallback getAdminModeCallback() {
      return this.adminModeCallback;
   }

   public final void setAdminModeCallback(Deployment.AdminModeCallback var1) {
      if (this.adminModeCallback == null) {
         this.adminModeCallback = var1;
      }

   }

   public final boolean isAdminModeTransition() {
      return this.isAdminModeTransition;
   }

   public final void setAdminModeTransition(boolean var1) {
      this.isAdminModeTransition = var1;
   }

   public final boolean isIgnoreSessionsEnabled() {
      return this.isIgnoreSessionsEnabled;
   }

   public final void setRMIGracePeriodSecs(int var1) {
      this.rmiGracePeriodSecs = var1;
   }

   public final int getRMIGracePeriodSecs() {
      return this.rmiGracePeriodSecs;
   }

   public final String[] getUserSuppliedTargets() {
      return this.userSuppliedTargets;
   }

   public final void setIgnoreSessions(boolean var1) {
      this.isIgnoreSessionsEnabled = var1;
   }

   public final void setUserSuppliedTargets(String[] var1) {
      this.userSuppliedTargets = var1;
   }

   public final void setRequiresRestart(boolean var1) {
      this.requiresRestart = var1;
   }

   public final boolean requiresRestart() {
      return this.requiresRestart;
   }

   public final void setDeploymentOperation(int var1) {
      this.deploymentOperation = var1;
   }

   public final int getDeploymentOperation() {
      return this.deploymentOperation;
   }

   public final void setStaticDeploymentOperation(boolean var1) {
      this.isStatic = var1;
   }

   public final boolean isStaticDeploymentOperation() {
      return this.isStatic;
   }

   public boolean isAppStaged() {
      return this.isStaged;
   }

   public void setAppStaged(boolean var1) {
      this.isStaged = var1;
   }

   public final InputStream getApplicationDescriptor() {
      return null;
   }

   public final InputStream getWLApplicationDescriptor() {
      return null;
   }

   public final InputStream getAltDD() {
      return null;
   }

   public final InputStream getAltWLDD() {
      return null;
   }
}
