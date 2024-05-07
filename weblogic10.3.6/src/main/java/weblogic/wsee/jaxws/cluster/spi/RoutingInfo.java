package weblogic.wsee.jaxws.cluster.spi;

import java.io.Serializable;
import weblogic.wsee.persistence.AbstractStorable;

public class RoutingInfo extends AbstractStorable {
   private static final long serialVersionUID = -1474902173471396038L;
   private String _name;
   private Type _type;

   public RoutingInfo(String var1, Type var2) {
      super((Serializable)null);
      this._name = var1;
      this._type = var2;
   }

   public String getName() {
      return this._name;
   }

   public Type getType() {
      return this._type;
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append(this._type);
      var1.append("/");
      var1.append(this._name);
      return var1.toString();
   }

   public int hashCode() {
      return this.toString().hashCode();
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof RoutingInfo)) {
         return false;
      } else {
         RoutingInfo var2 = (RoutingInfo)var1;
         return var2.toString().equals(this.toString());
      }
   }

   public static enum Type {
      PHYSICAL_STORE_NAME,
      SERVER_NAME,
      HOST_AND_PORT,
      NEED_BODY,
      ABSTAIN;
   }
}
