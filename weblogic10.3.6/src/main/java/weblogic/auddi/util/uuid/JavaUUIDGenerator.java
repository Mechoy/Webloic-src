package weblogic.auddi.util.uuid;

public class JavaUUIDGenerator implements UUIDGenerator {
   private UUIDGen m_generator = new UUIDGen();

   public String uuidGen() throws UUIDException {
      return this.m_generator.generateRandomBasedUUID();
   }

   public static void main(String[] var0) throws Exception {
      JavaUUIDGenerator var1 = new JavaUUIDGenerator();
      int var2 = Integer.parseInt(var0[0]);

      for(int var3 = 0; var3 < var2; ++var3) {
         System.out.println(var1.uuidGen());
      }

   }
}
