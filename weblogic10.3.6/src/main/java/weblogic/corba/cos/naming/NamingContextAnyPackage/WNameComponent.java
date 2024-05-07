package weblogic.corba.cos.naming.NamingContextAnyPackage;

import org.omg.CORBA.portable.IDLEntity;

public final class WNameComponent implements IDLEntity {
   public String id = null;
   public String kind = null;

   public WNameComponent() {
   }

   public WNameComponent(String var1, String var2) {
      this.id = var1;
      this.kind = var2;
   }
}
