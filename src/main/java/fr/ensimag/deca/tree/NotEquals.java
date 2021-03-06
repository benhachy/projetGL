package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.BNE;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.FLOAT;

/**
 *
 * @author gl03
 * @date 01/01/2022
 */
public class NotEquals extends AbstractOpExactCmp {

    private static int cmpEtiquetes=0; 

    public NotEquals(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected String getOperatorName() {
        return "!=";
    }
    public void  codeGenOpBool(DecacCompiler compiler,GPRegister leftOperand, GPRegister rightOperand,boolean b,Label E,Label EFin,int n) {
        AbstractExpr rOp = getRightOperand();
        AbstractExpr lOp = getLeftOperand();
        rOp.codeGenExpr(compiler, 2);
        lOp.codeGenExpr(compiler, 3);
        if(rOp.getType().isFloat() && lOp.getType().isInt())
        {
            compiler.addInstruction(new FLOAT(Register.getR(3), Register.getR(3)));
        }
        else if(rOp.getType().isInt() && lOp.getType().isFloat())
        {
            compiler.addInstruction(new FLOAT(Register.getR(2), Register.getR(2)));
        }
        compiler.addInstruction(new CMP(Register.getR(2),Register.getR(3)));
        if (b){
            compiler.addInstruction(new BNE(E));
        }
        else{
            compiler.addInstruction(new BEQ(E));
        }
    }
    @Override
    protected void codeGenInst(DecacCompiler compiler){
        this.codeGenExpr(compiler, 2);
    }

    @Override
    public void codeGenExpr(DecacCompiler compiler,int n) {
        getRightOperand().codeGenExpr(compiler, 2);
        getLeftOperand().codeGenExpr(compiler, 3);
        compiler.addInstruction(new CMP(Register.getR(3),Register.getR(2)));
        Label loadFalse = new Label("loadFalseNE."+cmpEtiquetes);
        Label finCmp = new Label("finComparationNE."+cmpEtiquetes);
        compiler.addInstruction(new BEQ(loadFalse));
        new IntLiteral(1).codeGenExpr(compiler,n);
        compiler.addInstruction(new BRA(finCmp));
        compiler.addLabel(loadFalse);
        new IntLiteral(0).codeGenExpr(compiler,n);
        compiler.addLabel(finCmp);
        cmpEtiquetes++;
    }

}
