package weblogic.wsee.wstx.wsat.v11.types;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

@XmlRegistry
public class ObjectFactory {
   private static final QName _Aborted_QNAME = new QName("http://docs.oasis-open.org/ws-tx/wsat/2006/06", "Aborted");
   private static final QName _Commit_QNAME = new QName("http://docs.oasis-open.org/ws-tx/wsat/2006/06", "Commit");
   private static final QName _ReadOnly_QNAME = new QName("http://docs.oasis-open.org/ws-tx/wsat/2006/06", "ReadOnly");
   private static final QName _Committed_QNAME = new QName("http://docs.oasis-open.org/ws-tx/wsat/2006/06", "Committed");
   private static final QName _Rollback_QNAME = new QName("http://docs.oasis-open.org/ws-tx/wsat/2006/06", "Rollback");
   private static final QName _Prepare_QNAME = new QName("http://docs.oasis-open.org/ws-tx/wsat/2006/06", "Prepare");
   private static final QName _Prepared_QNAME = new QName("http://docs.oasis-open.org/ws-tx/wsat/2006/06", "Prepared");

   public Notification createNotification() {
      return new Notification();
   }

   public ATAssertion createATAssertion() {
      return new ATAssertion();
   }

   @XmlElementDecl(
      namespace = "http://docs.oasis-open.org/ws-tx/wsat/2006/06",
      name = "Aborted"
   )
   public JAXBElement<Notification> createAborted(Notification var1) {
      return new JAXBElement(_Aborted_QNAME, Notification.class, (Class)null, var1);
   }

   @XmlElementDecl(
      namespace = "http://docs.oasis-open.org/ws-tx/wsat/2006/06",
      name = "Commit"
   )
   public JAXBElement<Notification> createCommit(Notification var1) {
      return new JAXBElement(_Commit_QNAME, Notification.class, (Class)null, var1);
   }

   @XmlElementDecl(
      namespace = "http://docs.oasis-open.org/ws-tx/wsat/2006/06",
      name = "ReadOnly"
   )
   public JAXBElement<Notification> createReadOnly(Notification var1) {
      return new JAXBElement(_ReadOnly_QNAME, Notification.class, (Class)null, var1);
   }

   @XmlElementDecl(
      namespace = "http://docs.oasis-open.org/ws-tx/wsat/2006/06",
      name = "Committed"
   )
   public JAXBElement<Notification> createCommitted(Notification var1) {
      return new JAXBElement(_Committed_QNAME, Notification.class, (Class)null, var1);
   }

   @XmlElementDecl(
      namespace = "http://docs.oasis-open.org/ws-tx/wsat/2006/06",
      name = "Rollback"
   )
   public JAXBElement<Notification> createRollback(Notification var1) {
      return new JAXBElement(_Rollback_QNAME, Notification.class, (Class)null, var1);
   }

   @XmlElementDecl(
      namespace = "http://docs.oasis-open.org/ws-tx/wsat/2006/06",
      name = "Prepare"
   )
   public JAXBElement<Notification> createPrepare(Notification var1) {
      return new JAXBElement(_Prepare_QNAME, Notification.class, (Class)null, var1);
   }

   @XmlElementDecl(
      namespace = "http://docs.oasis-open.org/ws-tx/wsat/2006/06",
      name = "Prepared"
   )
   public JAXBElement<Notification> createPrepared(Notification var1) {
      return new JAXBElement(_Prepared_QNAME, Notification.class, (Class)null, var1);
   }
}
