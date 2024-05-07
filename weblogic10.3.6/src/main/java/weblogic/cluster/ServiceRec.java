package weblogic.cluster;

class ServiceRec {
   public int id;
   public Object service;

   public ServiceRec(int var1, Object var2) {
      this.id = var1;
      this.service = var2;
   }

   public boolean equals(Object var1) {
      try {
         ServiceRec var2 = (ServiceRec)var1;
         if (this.id != -1 && var2.id != -1) {
            return this.service == null || this.id == var2.id && this.service.equals(var2.service);
         } else {
            return this.service == null || this.service.equals(var2.service);
         }
      } catch (ClassCastException var3) {
         return false;
      }
   }
}
