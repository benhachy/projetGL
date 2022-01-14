package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.BGE;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.CMP;

/**
 * Operator "x >= y"
 * 
 * @author gl03
 * @date 01/01/2022
 */
public class GreaterOrEqual extends AbstractOpIneq {

    private static int cmpEtiquetes=0; 

    public GreaterOrEqual(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected String getOperatorName() {
        return ">=";
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler){
        getRightOperand().codeGenExpr(compiler, 2);
        getLeftOperand().codeGenExpr(compiler, 3);
        compiler.addInstruction(new CMP(Register.getR(3),Register.getR(2)));
        Label loadTrue = new Label("loadTrueGE."+cmpEtiquetes);
        Label finCmp = new Label("finComparationGE."+cmpEtiquetes);
        compiler.addInstruction(new BGE(loadTrue));
        new IntLiteral(0).codeGenExpr(compiler,1);
        compiler.addInstruction(new BRA(finCmp));
        compiler.addLabel(loadTrue);
        new IntLiteral(1).codeGenExpr(compiler,1);
        compiler.addLabel(finCmp);
        cmpEtiquetes++;
    }

}
