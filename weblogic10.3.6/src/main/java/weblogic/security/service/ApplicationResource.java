package weblogic.security.service;

import weblogic.security.spi.Resource;

public final class ApplicationResource extends ResourceBase {
   private static final String[] KEYS = new String[]{"application"};

   public ApplicationResource(String application) {
      this.initialize(application);
   }

   /** @deprecated */
   public void initialize(String application) {
      this.parent = NO_PARENT;
      this.init(new String[]{application}, 0L);
   }

   /** @deprecated */
   public ApplicationResource() {
      this((String)null, (Resource)null);
   }

   public ApplicationResource(String application, Resource parent) {
      this.parent = parent == null ? NO_PARENT : parent;
      long seed = this.parent == NO_PARENT ? 0L : parent.getID();
      this.init(new String[]{application}, seed);
   }

   public String[] getKeys() {
      return KEYS;
   }

   protected Resource makeParent() {
      return this.parent == NO_PARENT ? null : this.parent;
   }

   public boolean equals(Object obj) {
      if (!super.equals(obj)) {
         return false;
      } else {
         Resource p = ((ApplicationResource)obj).parent;
         return this.parent == p || this.parent != NO_PARENT && p != NO_PARENT && this.parent.equals(p);
      }
   }

   public String getType() {
      return "<app>";
   }

   /** @deprecated */
   public void reset() {
      this.init((String[])null, 0, 0L);
      this.parent = null;
   }

   public String getApplicationName() {
      return this.length > 0 ? this.values[0] : null;
   }
}
