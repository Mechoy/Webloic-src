package weblogic.corba.cos.naming.NamingContextAnyPackage;

import org.omg.CORBA.UserException;
import org.omg.CosNaming.NamingContext;

public final class CannotProceed extends UserException {
   public NamingContext cxt = null;
   public WNameComponent[] rest_of_name = null;

   public CannotProceed() {
      super(CannotProceedHelper.id());
   }

   public CannotProceed(NamingContext var1, WNameComponent[] var2) {
      super(CannotProceedHelper.id());
      this.cxt = var1;
      this.rest_of_name = var2;
   }

   public CannotProceed(String var1, NamingContext var2, WNameComponent[] var3) {
      super(CannotProceedHelper.id() + "  " + var1);
      this.cxt = var2;
      this.rest_of_name = var3;
   }
}
