package weblogic.management.j2ee.mejb;

import java.rmi.RemoteException;
import java.util.Set;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.IntrospectionException;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.ObjectName;
import javax.management.QueryExp;
import javax.management.ReflectionException;
import javax.management.j2ee.ListenerRegistration;
import weblogic.management.j2ee.WLSListenerRegistry;
import weblogic.management.j2ee.internal.InvalidObjectNameException;
import weblogic.management.j2ee.internal.JMOService;

public class MejbBean implements SessionBean {
   private SessionContext ctx = null;
   private WLSListenerRegistry registry = null;
   private static final JMOService service = JMOService.getJMOService();

   public void ejbCreate() {
      this.registry = new WLSListenerRegistry(service);
   }

   public void ejbActivate() {
   }

   public void ejbPassivate() {
   }

   public void ejbRemove() {
   }

   public void setSessionContext(SessionContext var1) {
      this.ctx = var1;
   }

   public Set queryNames(ObjectName var1, QueryExp var2) throws RemoteException {
      return service.queryNames(var1, var2);
   }

   public boolean isRegistered(ObjectName var1) throws RemoteException {
      return service.isRegistered(var1);
   }

   public Integer getMBeanCount() throws RemoteException {
      return service.getMBeanCount();
   }

   public MBeanInfo getMBeanInfo(ObjectName var1) throws IntrospectionException, InstanceNotFoundException, ReflectionException, RemoteException {
      return service.getMBeanInfo(var1);
   }

   public Object getAttribute(ObjectName var1, String var2) throws MBeanException, AttributeNotFoundException, InstanceNotFoundException, ReflectionException, RemoteException {
      try {
         return service.getAttribute(var1, var2);
      } catch (InvalidObjectNameException var4) {
         throw new MBeanException(var4);
      }
   }

   public AttributeList getAttributes(ObjectName var1, String[] var2) throws InstanceNotFoundException, ReflectionException, RemoteException {
      return service.getAttributes(var1, var2);
   }

   public void setAttribute(ObjectName var1, Attribute var2) throws InstanceNotFoundException, AttributeNotFoundException, InvalidAttributeValueException, MBeanException, ReflectionException, RemoteException {
      try {
         service.setAttribute(var1, var2);
      } catch (InvalidObjectNameException var4) {
         throw new MBeanException(var4);
      }
   }

   public AttributeList setAttributes(ObjectName var1, AttributeList var2) throws InstanceNotFoundException, ReflectionException, RemoteException {
      return service.setAttributes(var1, var2);
   }

   public Object invoke(ObjectName var1, String var2, Object[] var3, String[] var4) throws InstanceNotFoundException, MBeanException, ReflectionException, RemoteException {
      try {
         return service.invoke(var1, var2, var3, var4);
      } catch (InvalidObjectNameException var6) {
         throw new MBeanException(var6);
      }
   }

   public String getDefaultDomain() throws RemoteException {
      return service.getDefaultDomain();
   }

   public ListenerRegistration getListenerRegistry() throws RemoteException {
      return this.registry;
   }
}
