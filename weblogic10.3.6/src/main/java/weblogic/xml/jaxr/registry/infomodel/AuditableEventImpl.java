package weblogic.xml.jaxr.registry.infomodel;

import java.sql.Timestamp;
import javax.xml.registry.JAXRException;
import javax.xml.registry.infomodel.AuditableEvent;
import javax.xml.registry.infomodel.RegistryObject;
import javax.xml.registry.infomodel.User;
import weblogic.xml.jaxr.registry.RegistryServiceImpl;

public class AuditableEventImpl extends RegistryObjectImpl implements AuditableEvent {
   private static final long serialVersionUID = -1L;
   private int m_eventType;
   private RegistryObject m_registryObject;
   private Timestamp m_timestamp;
   private User m_user;

   public AuditableEventImpl(RegistryServiceImpl var1) throws JAXRException {
      super(var1);
   }

   public AuditableEventImpl(AuditableEvent var1, RegistryServiceImpl var2) throws JAXRException {
      super((RegistryObject)var1, var2);
      if (var1 != null) {
         this.m_eventType = var1.getEventType();
         this.m_registryObject = new RegistryObjectImpl(var1.getRegistryObject(), var2);
         this.m_timestamp = var1.getTimestamp();
         this.m_user = new UserImpl(var1.getUser(), var2);
      }

   }

   public User getUser() throws JAXRException {
      this.checkCapability(1);
      return null;
   }

   public Timestamp getTimestamp() throws JAXRException {
      this.checkCapability(1);
      return null;
   }

   public int getEventType() throws JAXRException {
      this.checkCapability(1);
      return 0;
   }

   public RegistryObject getRegistryObject() throws JAXRException {
      this.checkCapability(1);
      return null;
   }

   protected Object[] getDefiningElements() {
      Object[] var1 = new Object[0];
      Object[] var2 = mergeObjectArrays(super.getDefiningElements(), var1);
      return var2;
   }

   protected String[] getVariableNames() {
      String[] var1 = new String[0];
      String[] var2 = mergeStringArrays(super.getVariableNames(), var1);
      return var2;
   }
}
