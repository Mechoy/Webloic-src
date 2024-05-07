package weblogic.webservice.binding;

/** @deprecated */
public abstract class BindingExtension {
   public static final int MAX_NUM_BIND = 2;
   public static int RELIABLE = 0;
   public static int DIME = 1;
   private int key;

   public int getKey() {
      return this.key;
   }

   public void setKey(int var1) {
      this.key = var1;
   }
}
