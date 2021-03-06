package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.BooleanType;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.LEA;
import fr.ensimag.ima.pseudocode.instructions.BNE;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.CMP;
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
import fr.ensimag.deca.context.BooleanType;



/**
 *
 * @author gl03
 * @date 01/01/2022
 */
public class InstanceOf extends AbstractExpr {
    public AbstractIdentifier type;
    public AbstractExpr expr;
    public InstanceOf(AbstractIdentifier type,AbstractExpr expr) {
        this.type = type;
        this.expr=expr;
    }
    static int numberofIstanceOf;
    @Override
    public  Type verifyExpr(DecacCompiler compiler,
            EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError{
            Type returnType = type.verifyType(compiler);
            expr.verifyExpr(compiler, localEnv, currentClass);
            if(!expr.getType().isClassOrNull()){
                throw new ContextualError(expr.getType().getName()+" n'est pas un Objet", expr.getLocation());
            }
            if(!returnType.isClass()){
                throw new ContextualError(returnType.getName()+" n'est pas une classe", type.getLocation());
            }
            SymbolTable tab = new SymbolTable();
            SymbolTable.Symbol symbol = tab.create("instanceof");
            BooleanType chaine = new BooleanType(symbol);
            setType(chaine);
            return chaine;
    }
    @Override
    protected void iterChildren(TreeFunction f) {
        type.iter(f);
        expr.iter(f);
    }
    public void  codeGenOpBool(DecacCompiler compiler,GPRegister leftOperand, GPRegister rightOperand,boolean b,Label E,Label EFin,int n) {

        Label beginningInstanceOf= new Label("instanceOf.Begin"+numberofIstanceOf);
        Label succesInstanceOf= new Label("instanceOf.succes"+numberofIstanceOf);
        Label endInstanceOf= new Label("instanceOf.end"+numberofIstanceOf);
        numberofIstanceOf++;
        compiler.addInstruction(new LEA(Identifier.getVariableAddress(type.getName()),Register.getR(2)));

        expr.codeGenExpr(compiler,3);
        compiler.addInstruction(new LOAD(new RegisterOffset(0,Register.getR(3)),Register.getR(3)));
        compiler.addLabel(beginningInstanceOf);
        compiler.addInstruction(new CMP(Register.getR(3),Register.getR(2)));
        compiler.addInstruction(new BEQ(E));
        compiler.addInstruction(new LOAD(new RegisterOffset(0,Register.getR(3)),Register.getR(3)));
        compiler.addInstruction(new CMP(new NullOperand(),Register.getR(3)));
        compiler.addInstruction(new BEQ(EFin));
        compiler.addInstruction(new BNE(beginningInstanceOf));
    }
    @Override
    public void codeGenExpr(DecacCompiler compiler, int n){
        Label beginningInstanceOf= new Label("instanceOf.Begin"+numberofIstanceOf);
        Label succesInstanceOf= new Label("instanceOf.succes"+numberofIstanceOf);
        Label endInstanceOf= new Label("instanceOf.end"+numberofIstanceOf);
        numberofIstanceOf++;
        compiler.addInstruction(new LEA(Identifier.getVariableAddress(type.getName()),Register.getR(2)));

        expr.codeGenExpr(compiler,3);
        compiler.addInstruction(new LOAD(new RegisterOffset(0,Register.getR(3)),Register.getR(3)));
        compiler.addLabel(beginningInstanceOf);
        compiler.addInstruction(new CMP(Register.getR(3),Register.getR(2)));
        compiler.addInstruction(new BEQ(succesInstanceOf));
        compiler.addInstruction(new LOAD(new RegisterOffset(0,Register.getR(3)),Register.getR(3)));
        compiler.addInstruction(new CMP(new NullOperand(),Register.getR(3)));
        compiler.addInstruction(new BNE(beginningInstanceOf));
        compiler.addInstruction(new LOAD(new ImmediateInteger(0),Register.getR(n)));
        compiler.addInstruction(new BRA(endInstanceOf));
        compiler.addLabel(succesInstanceOf);
        compiler.addInstruction(new LOAD(new ImmediateInteger(1),Register.getR(n)));
        compiler.addLabel(endInstanceOf);



    }
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // leaf node => nothing to do
    }
    @Override
    public void decompile(IndentPrintStream s) {
        
    }
}