package weblogic.management.configuration;

import java.io.Serializable;

public final class ListResults implements Serializable {
   private static final long serialVersionUID = 2046804399823914593L;
   private Object[] firstBatch;
   private RemoteEnumeration rest;

   public ListResults(Object[] var1, RemoteEnumeration var2) {
      this.firstBatch = var1;
      this.rest = var2;
   }

   public Object[] getFirstBatch() {
      return this.firstBatch;
   }

   public RemoteEnumeration getRest() {
      return this.rest;
   }
}
