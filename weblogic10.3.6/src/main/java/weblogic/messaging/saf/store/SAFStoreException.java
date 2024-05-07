package weblogic.messaging.saf.store;

import weblogic.messaging.saf.SAFException;

public final class SAFStoreException extends SAFException {
   static final long serialVersionUID = -1658399020464780884L;
   private final SAFStore store;

   public SAFStoreException(SAFStore var1, Throwable var2) {
      super(var2);
      this.store = var1;
   }

   public String toString() {
      String var1 = super.toString();
      return "<SAFStoreException> : " + this.store + var1;
   }
}
