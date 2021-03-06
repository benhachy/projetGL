package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.BooleanType;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.NullType;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import java.io.PrintStream;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.FLOAT;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Definition;
import fr.ensimag.deca.context.EnvironmentExp;



/**
 *
 * @author gl03
 * @date 01/01/2022
 */
public class Null extends AbstractExpr {
    public  Type verifyExpr(DecacCompiler compiler,
    EnvironmentExp localEnv, ClassDefinition currentClass)
    throws ContextualError{
         SymbolTable tab = new SymbolTable();
         SymbolTable.Symbol symbol = tab.create("null");
         NullType chaine = new NullType(symbol);
         setType(chaine);
         return chaine;
    }
    @Override
    protected void iterChildren(TreeFunction f) {
        // leaf nodes => nothing to do 
    }
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // leaf node => nothing to do
    }
    @Override
    public void decompile(IndentPrintStream s) {
        s.print(" null ");
    }
    @Override
    public void codeGenExpr(DecacCompiler compiler,int n) {
            compiler.addInstruction(new LOAD(new NullOperand(),Register.getR(n)));        
    }

}