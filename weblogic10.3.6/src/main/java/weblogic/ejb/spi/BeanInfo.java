package weblogic.ejb.spi;

public interface BeanInfo {
   String getEJBName();

   Class getBeanClass();

   boolean getIsResourceRef();
}
