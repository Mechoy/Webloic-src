package weblogic.cluster.singleton;

import java.io.Serializable;

public class SingletonServicesState implements Serializable {
   private int state;
   private Serializable data;

   public SingletonServicesState(int var1) {
      this.state = var1;
   }

   public void setStateData(Serializable var1) {
      this.data = var1;
   }

   public Serializable getStateData() {
      return this.data;
   }

   public int getState() {
      return this.state;
   }

   public String toString() {
      String var1 = SingletonServicesStateManager.STRINGIFIED_STATE[this.state];
      if (this.data != null) {
         var1 = var1 + " Data:" + this.data;
      }

      return var1;
   }

   public int hashCode() {
      return this.toString().hashCode();
   }

   public boolean equals(Object var1) {
      boolean var2 = false;
      if (var1 instanceof SingletonServicesState) {
         SingletonServicesState var3 = (SingletonServicesState)var1;
         if (var3.toString().equals(this.toString())) {
            var2 = true;
         }
      }

      return var2;
   }
}
