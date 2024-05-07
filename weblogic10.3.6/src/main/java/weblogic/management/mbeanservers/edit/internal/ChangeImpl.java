package weblogic.management.mbeanservers.edit.internal;

import weblogic.management.mbeanservers.edit.Change;

public class ChangeImpl implements Change {
   private final Object bean;
   private final String attributeName;
   private final String operation;
   private final Object oldValue;
   private final Object newValue;
   private final boolean restartRequired;

   public ChangeImpl(Object var1, String var2, String var3, Object var4, Object var5, boolean var6) {
      this.bean = var1;
      this.attributeName = var2;
      this.operation = var3;
      this.oldValue = var4;
      this.newValue = var5;
      this.restartRequired = var6;
   }

   public Object getBean() {
      return this.bean;
   }

   public String getAttributeName() {
      return this.attributeName;
   }

   public String getOperation() {
      return this.operation;
   }

   public Object getOldValue() {
      return this.oldValue;
   }

   public Object getNewValue() {
      return this.newValue;
   }

   public boolean isRestartRequired() {
      return this.restartRequired;
   }
}
