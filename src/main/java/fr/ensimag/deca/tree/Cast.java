package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;



/**
 *
 * @author gl03
 * @date 01/01/2022
 */
public class Cast extends AbstractExpr {
    public AbstractIdentifier type;
    public AbstractExpr expr;
    public Cast(AbstractIdentifier type,AbstractExpr expr) {
        this.type = type;
        this.expr=expr;
    }
    @Override
    public  Type verifyExpr(DecacCompiler compiler,
    EnvironmentExp localEnv, ClassDefinition currentClass)
    throws ContextualError{
         throw new UnsupportedOperationException("New is Not yet implemented");
    }
    @Override
    protected void iterChildren(TreeFunction f) {
    throw new UnsupportedOperationException("Not yet supported");
    }
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
    // leaf node => nothing to do
    }
    @Override
    public void decompile(IndentPrintStream s) {

    }
}