package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;

/**
 * Left-hand side value of an assignment.
 * 
 * @author gl03
 * @date 01/01/2022
 */
public abstract class AbstractLValue extends AbstractExpr {

    public abstract void codeGenAssign(DecacCompiler compiler, int n);
}

