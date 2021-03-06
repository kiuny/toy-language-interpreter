package com.andrei.impl.domain.expression.artihmetic;

import com.andrei.impl.domain.ProgramState;
import com.andrei.impl.domain.exceptions.ToyException;
import com.andrei.impl.domain.expression.Expression;

public class Division extends ArithmeticExpression {
    protected Division(Expression lhs, Expression rhs) {
        super(lhs, rhs);
    }

    @Override
    public Integer evaluate(ProgramState programState) throws ToyException {
        Integer rhs = this.rhs.evaluate(programState);

        if (rhs == 0) {
            throw new ArithmeticException("Division by 0 is not possible");
        }

        return lhs.evaluate(programState) / rhs;
    }

    @Override
    public String toString() {
        return "(" + lhs.toString() + ") / (" + rhs.toString() + ")";
    }
}
