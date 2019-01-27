//package p.hh.jsontemplate.jsoncomposer;
//
//public class Main {
//
//    public static void main(String[] args) {
//        JsonBuilder jsonBuilder = new JsonBuilder();
//        String prettyPrint = jsonBuilder.createObject()
//                .putInteger("intField", 1)
//                .putBoolean("boolField", true)
//                .putFloat("floatField", 1.2f)
//                .putString("strField", "hello")
//                .putObject("objField")
//                .putString("strField2", "kjl;jdl")
//                .putNull("nullField")
//                .putObject("anotherObjField")
//                .putBoolean("mail", true)
//                .end()
//                .end()
//                .putArray("someElements")
//                .addNull()
//                .addString("blah")
//                .addInteger(123)
//                .addFloat(2.3f)
//                .addBoolean(false)
//                .addObject()
//                .putString("key", "value")
//                .end()
//                .end()
//                .end()
//                .build()
//                .prettyPrint(0);
//        System.out.println(prettyPrint);
//    }
//}
