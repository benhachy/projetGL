package fr.ensimag.deca.context;

import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.deca.tree.Location;
import org.apache.commons.lang.Validate;

/**
 * Type defined by a class.
 *
 * @author gl03
 * @date 01/01/2022
 */
public class ClassType extends Type {
    
    protected ClassDefinition definition;
    
    public ClassDefinition getDefinition() {
        return this.definition;
    }
            
    @Override
    public ClassType asClassType(String errorMessage, Location l) {
        return this;
    }

    @Override
    public boolean isClass() {
        return true;
    }

    @Override
    public boolean isClassOrNull() {
        return true;
    }

    /**
     * Standard creation of a type class.
     */
    public ClassType(Symbol className, Location location, ClassDefinition superClass) {
        super(className);
        this.definition = new ClassDefinition(this, location, superClass);
    }

    /**
     * Creates a type representing a class className.
     * (To be used by subclasses only)
     */
    protected ClassType(Symbol className) {
        super(className);
    }
    

    @Override
    public boolean sameType(Type otherType) {
        return otherType.isClassOrNull();
    }

    /**
     * Return true if potentialSuperClass is a superclass of this class.
     */
    public boolean isSubClassOf(ClassType potentialSuperClass) {
        if(potentialSuperClass == null|| this == null|| getName().equals(SymbolTable.creerSymbol("0"))){
            return false;
        }
        if(potentialSuperClass.getName().equals(getDefinition().getSuperClass().getType().getName())){
            return true;
        }
        return getDefinition().getSuperClass().getType().isSubClassOf(potentialSuperClass);
    }


}
