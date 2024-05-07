package weblogic.jms.module;

import java.util.HashMap;
import java.util.LinkedList;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.TargetMBean;

public class UpdateInformation {
   private int maxType;
   private LinkedList[] addedEntities;
   private HashMap[] addedEntitiesHash;
   private LinkedList[] deletedEntities;
   private HashMap[] deletedEntitiesHash;
   private LinkedList[] changedEntities;
   private TargetMBean[] defaultTargets;
   private boolean defaultTargetsChanged = false;
   private HashMap addedGroups;
   private HashMap removedGroups;
   private HashMap changedGroups;
   private HashMap addedLocalGroups;
   private HashMap removedLocalGroups;
   private HashMap changedLocalGroups;
   private DomainMBean proposedDomain;

   public UpdateInformation(int var1) {
      this.maxType = var1;
      this.addedEntities = new LinkedList[this.maxType];
      this.addedEntitiesHash = new HashMap[this.maxType];
      this.deletedEntities = new LinkedList[this.maxType];
      this.deletedEntitiesHash = new HashMap[this.maxType];
      this.changedEntities = new LinkedList[this.maxType];

      for(int var2 = 0; var2 < this.maxType; ++var2) {
         this.addedEntities[var2] = new LinkedList();
         this.addedEntitiesHash[var2] = new HashMap();
         this.deletedEntities[var2] = new LinkedList();
         this.deletedEntitiesHash[var2] = new HashMap();
         this.changedEntities[var2] = new LinkedList();
      }

   }

   synchronized LinkedList[] getAddedEntities() {
      return this.addedEntities;
   }

   HashMap[] getAddedEntitiesHash() {
      return this.addedEntitiesHash;
   }

   LinkedList[] getDeletedEntities() {
      return this.deletedEntities;
   }

   HashMap[] getDeletedEntitiesHash() {
      return this.deletedEntitiesHash;
   }

   LinkedList[] getChangedEntities() {
      return this.changedEntities;
   }

   void setAddedLocalGroups(HashMap var1) {
      this.addedLocalGroups = var1;
   }

   HashMap getAddedLocalGroups() {
      return this.addedLocalGroups;
   }

   void setRemovedLocalGroups(HashMap var1) {
      this.removedLocalGroups = var1;
   }

   HashMap getRemovedLocalGroups() {
      return this.removedLocalGroups;
   }

   void setChangedLocalGroups(HashMap var1) {
      this.changedLocalGroups = var1;
   }

   HashMap getChangedLocalGroups() {
      return this.changedLocalGroups;
   }

   void setAddedGroups(HashMap var1) {
      this.addedGroups = var1;
   }

   HashMap getAddedGroups() {
      return this.addedGroups;
   }

   void setRemovedGroups(HashMap var1) {
      this.removedGroups = var1;
   }

   HashMap getRemovedGroups() {
      return this.removedGroups;
   }

   void setChangedGroups(HashMap var1) {
      this.changedGroups = var1;
   }

   HashMap getChangedGroups() {
      return this.changedGroups;
   }

   DomainMBean getProposedDomain() {
      return this.proposedDomain;
   }

   void setProposedDomain(DomainMBean var1) {
      this.proposedDomain = var1;
   }

   void setDefaultTargets(TargetMBean[] var1) {
      if (this.defaultTargets != null && var1 != null) {
         if (this.defaultTargets.length != var1.length) {
            this.defaultTargetsChanged = true;
         } else {
            LinkedList var2 = new LinkedList();

            int var3;
            for(var3 = 0; var3 < this.defaultTargets.length; ++var3) {
               var2.add(this.defaultTargets[var3]);
            }

            for(var3 = 0; var3 < var1.length; ++var3) {
               if (!var2.contains(var1[var3])) {
                  this.defaultTargetsChanged = true;
               }
            }
         }
      }

      if (this.defaultTargets != null && var1 == null || this.defaultTargets == null && var1 != null) {
         this.defaultTargetsChanged = true;
      }

      if (this.defaultTargetsChanged) {
         this.defaultTargets = var1;
      }

   }

   TargetMBean[] getDefaultTargets() {
      return this.defaultTargets;
   }

   boolean hasDefaultTargetsChanged() {
      return this.defaultTargetsChanged;
   }

   void clearTargetUpdates() {
      this.getAddedGroups().clear();
      this.setAddedGroups((HashMap)null);
      this.getAddedLocalGroups().clear();
      this.setAddedLocalGroups((HashMap)null);
      this.getRemovedGroups().clear();
      this.setRemovedGroups((HashMap)null);
      this.getRemovedLocalGroups().clear();
      this.setRemovedLocalGroups((HashMap)null);
      this.getChangedGroups().clear();
      this.setChangedGroups((HashMap)null);
      this.getChangedLocalGroups().clear();
      this.setChangedLocalGroups((HashMap)null);
   }

   void close() {
      for(int var1 = 0; var1 < this.maxType; ++var1) {
         this.addedEntities[var1].clear();
         this.addedEntities[var1] = null;
         this.addedEntitiesHash[var1].clear();
         this.addedEntitiesHash[var1] = null;
         this.deletedEntities[var1].clear();
         this.deletedEntities[var1] = null;
         this.deletedEntitiesHash[var1].clear();
         this.deletedEntitiesHash[var1] = null;
         this.changedEntities[var1].clear();
         this.changedEntities[var1] = null;
      }

      this.addedEntities = null;
      this.addedEntitiesHash = null;
      this.deletedEntities = null;
      this.deletedEntitiesHash = null;
      this.changedEntities = null;
      this.proposedDomain = null;
      this.defaultTargets = null;
      this.defaultTargetsChanged = false;
   }
}
