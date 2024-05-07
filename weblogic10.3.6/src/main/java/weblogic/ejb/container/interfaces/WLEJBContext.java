package weblogic.ejb.container.interfaces;

import javax.ejb.EJBLocalObject;
import javax.ejb.EJBObject;
import javax.ejb.EnterpriseBean;

public interface WLEJBContext extends weblogic.ejb.spi.WLEJBContext {
   void setBean(EnterpriseBean var1);

   void setEJBObject(EJBObject var1);

   void setEJBLocalObject(EJBLocalObject var1);

   String getApplicationName();
}
