package p.hh.jsontemplate.jtm;

import java.util.Map;

public class TypeDefVisitor {

    Map<String, PropertyDeclaration> getTypeMap(PropertyDeclaration propertyDeclaration, Map<String, PropertyDeclaration> typeMap) {
        propertyDeclaration.visitTypeDeclaration(typeMap);
        return typeMap;
    }
}
