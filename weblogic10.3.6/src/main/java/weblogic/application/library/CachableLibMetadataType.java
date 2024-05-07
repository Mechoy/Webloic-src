package weblogic.application.library;

import java.io.Serializable;

public final class CachableLibMetadataType implements Serializable {
   private static final long serialVersionUID = -7070404227431048765L;
   public static final CachableLibMetadataType ANNOTATED_CLASSES = new CachableLibMetadataType(".annotations");
   public static final CachableLibMetadataType TLD = new CachableLibMetadataType(".tlds");
   public static final CachableLibMetadataType FACE_BEANS = new CachableLibMetadataType(".faces");
   public static final CachableLibMetadataType TAG_HANDLERS = new CachableLibMetadataType(".taghandlers");
   public static final CachableLibMetadataType TAG_LISTENERS = new CachableLibMetadataType(".taglisteners");
   private final String name;

   private CachableLibMetadataType(String var1) {
      this.name = var1;
   }

   public String getName() {
      return this.name;
   }

   public String toString() {
      return this.name;
   }
}
