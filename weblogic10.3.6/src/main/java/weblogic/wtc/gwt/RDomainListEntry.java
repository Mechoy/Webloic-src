package weblogic.wtc.gwt;

public final class RDomainListEntry {
   private TDMRemoteTDomain c;
   private RDomainListEntry n;

   public RDomainListEntry(TDMRemoteTDomain var1) {
      this.c = var1;
      this.n = null;
   }

   public void setNext(RDomainListEntry var1) {
      this.n = var1;
   }

   public RDomainListEntry getNext() {
      return this.n;
   }

   public TDMRemoteTDomain getRDom() {
      return this.c;
   }
}
