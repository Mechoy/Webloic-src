package weblogic.wsee.jaxws.injection;

public interface ObjectFactory {
   Object newInstance(String var1) throws IllegalAccessException, InstantiationException, ClassNotFoundException;

   <T> T newInstance(Class<T> var1) throws IllegalAccessException, InstantiationException;
}
