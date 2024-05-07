package weblogic.ejb.container.interfaces;

public interface Ejb3SessionHome {
   void prepare();

   Object getBindableImpl(Class var1);
}
