package weblogic.security.principal;

import com.bea.common.security.ApiLogger;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.security.InvalidParameterException;

public class PrincipalFactory {
   private static PrincipalFactory instance = null;
   private static PrincipalConfigurationDelegate delegate = null;

   public static synchronized PrincipalFactory getInstance() {
      if (instance != null) {
         return instance;
      } else {
         instance = new PrincipalFactory();
         return instance;
      }
   }

   private PrincipalFactory() {
      delegate = PrincipalConfigurationDelegate.getInstance();
   }

   public WLSPrincipal createWLSUser(String userName) {
      return this.createWLSUser(userName, (String)null, (String)null);
   }

   public WLSPrincipal createWLSUser(String userName, String guid, String dn) {
      return this.create(WLSUserImpl.class, userName, guid, dn);
   }

   public WLSPrincipal createWLSGroup(String groupName) {
      return this.createWLSGroup(groupName, (String)null, (String)null);
   }

   public WLSPrincipal createWLSGroup(String groupName, String guid, String dn) {
      return this.create(WLSGroupImpl.class, groupName, guid, dn);
   }

   public WLSPrincipal create(Class klass, String name, String guid, String dn) throws InvalidParameterException {
      if (WLSPrincipal.class.isAssignableFrom(klass)) {
         Constructor ctor = null;
         Class[] parameterTypes = null;
         Object[] argList = null;
         WLSPrincipal principal = null;
         WLSAbstractPrincipal abstractPrincipal = null;

         try {
            if (WLSAbstractPrincipal.class.isAssignableFrom(klass)) {
               parameterTypes = new Class[]{String.class};
               argList = new String[]{name};
               ctor = klass.getConstructor(parameterTypes);
               abstractPrincipal = (WLSAbstractPrincipal)ctor.newInstance(argList);
               abstractPrincipal.setGuid(guid);
               abstractPrincipal.setDn(dn);
               Class var10 = PrincipalConfigurationDelegate.class;
               synchronized(PrincipalConfigurationDelegate.class) {
                  abstractPrincipal.setEqualsCaseInsensitive(delegate.isEqualsCaseInsensitive());
                  abstractPrincipal.setEqualsCompareDnAndGuid(delegate.isEqualsCompareDnAndGuid());
               }

               abstractPrincipal.principalFactoryCreated = true;
               return abstractPrincipal;
            } else {
               parameterTypes = new Class[]{String.class, String.class, String.class};
               argList = new String[]{name, guid, dn};
               ctor = klass.getConstructor(parameterTypes);
               principal = (WLSPrincipal)ctor.newInstance(argList);
               return principal;
            }
         } catch (IllegalAccessException var13) {
            throw new IllegalStateException(ApiLogger.getUnableToInstantiatePrincipal(klass.getName(), var13));
         } catch (NoSuchMethodException var14) {
            throw new IllegalStateException(ApiLogger.getUnableToInstantiatePrincipal(klass.getName(), var14));
         } catch (InvocationTargetException var15) {
            throw new IllegalStateException(ApiLogger.getUnableToInstantiatePrincipal(klass.getName(), var15));
         } catch (InstantiationException var16) {
            throw new IllegalStateException(ApiLogger.getUnableToInstantiatePrincipal(klass.getName(), var16));
         } catch (IllegalArgumentException var17) {
            throw new IllegalStateException(ApiLogger.getUnableToInstantiatePrincipal(klass.getName(), var17));
         }
      } else {
         throw new InvalidParameterException(ApiLogger.getInvalidPrincipalClassName(klass.getName()));
      }
   }
}
