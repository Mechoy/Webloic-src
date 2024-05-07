package weblogic.wsee.jaxws.injection;

import com.oracle.pitchfork.interfaces.WSEEComponentContributorBroker;
import com.oracle.pitchfork.interfaces.inject.DeploymentUnitMetadataI;
import com.oracle.pitchfork.interfaces.inject.Jsr250MetadataI;
import weblogic.j2ee.descriptor.J2eeEnvironmentBean;
import weblogic.j2ee.injection.J2eeComponentContributor;
import weblogic.j2ee.injection.PitchforkContext;
import weblogic.wsee.util.Verbose;

public abstract class WSEEComponentContributor extends J2eeComponentContributor implements ObjectFactory {
   private static boolean verbose = Verbose.isVerbose(WSEEComponentContributor.class);
   private WSEEComponentContributorBroker ccBroker;

   WSEEComponentContributor(PitchforkContext var1) {
      super(var1);
      this.ccBroker = var1.getPitchforkUtils().createWSEEComponentContributorBroker();
   }

   public void init() throws Throwable {
      try {
         this.ccBroker.init(this);
         if (verbose) {
            Verbose.log((Object)"Created WSEE Contributor");
         }

      } catch (Throwable var2) {
         Verbose.log("Exception when creating spring bean factory", var2);
         throw var2;
      }
   }

   public <T> T newInstance(Class<T> var1) throws IllegalAccessException, InstantiationException {
      return this.newInstance(var1, true);
   }

   private <T> T newInstance(Class<T> var1, boolean var2) throws IllegalAccessException, InstantiationException {
      if (var2) {
         Object var3 = this.loadUsingSpring(var1.getName());
         if (var3 != null) {
            return var3;
         }
      }

      return var1.newInstance();
   }

   public Object newInstance(String var1) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
      Object var2 = this.loadUsingSpring(var1);
      return var2 != null ? var2 : this.newInstance(Class.forName(var1), false);
   }

   public Jsr250MetadataI getMetadata(String var1) {
      throw new AssertionError("This operation is not supported in WSEEComponentContributor");
   }

   private Object loadUsingSpring(String var1) {
      try {
         Object var2 = this.ccBroker.getBean(var1);
         if (verbose) {
            Verbose.log((Object)("WSEE Contributor newInstance for: " + var1));
         }

         return var2;
      } catch (Exception var3) {
         Verbose.log((Object)var3);
         return null;
      }
   }

   protected void contribute(Jsr250MetadataI var1, J2eeEnvironmentBean var2) {
   }

   public Jsr250MetadataI newJsr250Metadata(String var1, Class<?> var2, DeploymentUnitMetadataI var3) {
      return this.ccBroker.newJsr250Metadata(var1, var2, var3);
   }
}
