package weblogic.ejb.spi;

public interface SessionBeanInfo extends BeanInfo {
   boolean isStateful();

   WSObjectFactory getWSObjectFactory();
}
