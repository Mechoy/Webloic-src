package weblogic.uddi.client.structures.request;

import weblogic.uddi.client.structures.datatypes.FindQualifiers;

public abstract class FindRequest extends Request {
   protected int maxRows = 0;
   protected FindQualifiers findQualifiers = null;

   public void setMaxRows(int var1) {
      this.maxRows = var1;
   }

   public int getMaxRows() {
      return this.maxRows;
   }

   public void setFindQualifiers(FindQualifiers var1) {
      this.findQualifiers = var1;
   }

   public FindQualifiers getFindQualifiers() {
      return this.findQualifiers;
   }
}
