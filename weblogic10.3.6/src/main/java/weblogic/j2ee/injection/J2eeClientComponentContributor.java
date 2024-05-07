package weblogic.j2ee.injection;

import com.oracle.pitchfork.interfaces.inject.DeploymentUnitMetadataI;
import com.oracle.pitchfork.interfaces.inject.EnricherI;
import com.oracle.pitchfork.interfaces.inject.Jsr250MetadataI;
import weblogic.j2ee.descriptor.ApplicationClientBean;

public class J2eeClientComponentContributor extends BaseComponentContributor {
   private final Class mainClass;
   private final ApplicationClientBean clientBean;

   public J2eeClientComponentContributor(Class var1, ApplicationClientBean var2, PitchforkContext var3) {
      super(var3);
      this.mainClass = var1;
      this.clientBean = var2;
   }

   public void contribute(EnricherI var1) {
      Jsr250MetadataI var2 = this.buildJsr250MetaData(var1, "main", this.mainClass.getName());
      this.buildInjectionMetadata(var2, this.clientBean);
      var1.attach(var2);
      if (var2 != null) {
         var2.inject((Object)null);
      }

   }

   public Jsr250MetadataI newJsr250Metadata(String var1, Class<?> var2, DeploymentUnitMetadataI var3) {
      return this.pitchforkContext.getPitchforkUtils().createJ2eeClientInjectionMetadata(var1, var2, var3);
   }

   public Jsr250MetadataI getMetadata(String var1) {
      return null;
   }
}
