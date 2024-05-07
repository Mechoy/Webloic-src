package weblogic.wsee.wstx.wsat.v10.types;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

@XmlRegistry
public class ObjectFactory {
   private static final QName _Replay_QNAME = new QName("http://schemas.xmlsoap.org/ws/2004/10/wsat", "Replay");
   private static final QName _Committed_QNAME = new QName("http://schemas.xmlsoap.org/ws/2004/10/wsat", "Committed");
   private static final QName _ReadOnly_QNAME = new QName("http://schemas.xmlsoap.org/ws/2004/10/wsat", "ReadOnly");
   private static final QName _Aborted_QNAME = new QName("http://schemas.xmlsoap.org/ws/2004/10/wsat", "Aborted");
   private static final QName _Commit_QNAME = new QName("http://schemas.xmlsoap.org/ws/2004/10/wsat", "Commit");
   private static final QName _Prepare_QNAME = new QName("http://schemas.xmlsoap.org/ws/2004/10/wsat", "Prepare");
   private static final QName _Prepared_QNAME = new QName("http://schemas.xmlsoap.org/ws/2004/10/wsat", "Prepared");
   private static final QName _Rollback_QNAME = new QName("http://schemas.xmlsoap.org/ws/2004/10/wsat", "Rollback");

   public Notification createNotification() {
      return new Notification();
   }

   public ATAlwaysCapability createATAlwaysCapability() {
      return new ATAlwaysCapability();
   }

   public ATAssertion createATAssertion() {
      return new ATAssertion();
   }

   public PrepareResponse createPrepareResponse() {
      return new PrepareResponse();
   }

   public ReplayResponse createReplayResponse() {
      return new ReplayResponse();
   }

   @XmlElementDecl(
      namespace = "http://schemas.xmlsoap.org/ws/2004/10/wsat",
      name = "Replay"
   )
   public JAXBElement<Notification> createReplay(Notification var1) {
      return new JAXBElement(_Replay_QNAME, Notification.class, (Class)null, var1);
   }

   @XmlElementDecl(
      namespace = "http://schemas.xmlsoap.org/ws/2004/10/wsat",
      name = "Committed"
   )
   public JAXBElement<Notification> createCommitted(Notification var1) {
      return new JAXBElement(_Committed_QNAME, Notification.class, (Class)null, var1);
   }

   @XmlElementDecl(
      namespace = "http://schemas.xmlsoap.org/ws/2004/10/wsat",
      name = "ReadOnly"
   )
   public JAXBElement<Notification> createReadOnly(Notification var1) {
      return new JAXBElement(_ReadOnly_QNAME, Notification.class, (Class)null, var1);
   }

   @XmlElementDecl(
      namespace = "http://schemas.xmlsoap.org/ws/2004/10/wsat",
      name = "Aborted"
   )
   public JAXBElement<Notification> createAborted(Notification var1) {
      return new JAXBElement(_Aborted_QNAME, Notification.class, (Class)null, var1);
   }

   @XmlElementDecl(
      namespace = "http://schemas.xmlsoap.org/ws/2004/10/wsat",
      name = "Commit"
   )
   public JAXBElement<Notification> createCommit(Notification var1) {
      return new JAXBElement(_Commit_QNAME, Notification.class, (Class)null, var1);
   }

   @XmlElementDecl(
      namespace = "http://schemas.xmlsoap.org/ws/2004/10/wsat",
      name = "Prepare"
   )
   public JAXBElement<Notification> createPrepare(Notification var1) {
      return new JAXBElement(_Prepare_QNAME, Notification.class, (Class)null, var1);
   }

   @XmlElementDecl(
      namespace = "http://schemas.xmlsoap.org/ws/2004/10/wsat",
      name = "Prepared"
   )
   public JAXBElement<Notification> createPrepared(Notification var1) {
      return new JAXBElement(_Prepared_QNAME, Notification.class, (Class)null, var1);
   }

   @XmlElementDecl(
      namespace = "http://schemas.xmlsoap.org/ws/2004/10/wsat",
      name = "Rollback"
   )
   public JAXBElement<Notification> createRollback(Notification var1) {
      return new JAXBElement(_Rollback_QNAME, Notification.class, (Class)null, var1);
   }
}
