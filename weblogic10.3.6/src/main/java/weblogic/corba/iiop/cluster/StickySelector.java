package weblogic.corba.iiop.cluster;

class StickySelector implements Selector {
   public static final StickySelector SELECTOR = new StickySelector();
   public static final String ALGORITHM = "sticky";

   public int select(int var1, int var2) {
      return var1;
   }
}
