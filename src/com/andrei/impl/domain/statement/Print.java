package com.andrei.impl.domain.statement;

import com.andrei.impl.domain.exceptions.ToyException;
import com.andrei.impl.domain.expression.Expression;
import com.andrei.impl.domain.ProgramState;
import com.andrei.interfaces.domain.IStatement;

public class Print implements IStatement{

    final Expression expression;

    public Print(Expression expression) {
        this.expression = expression;
    }

    @Override
    public void execute(ProgramState state) throws ToyException {
        state.getOutput().add(expression.evaluate(state).toString());

    }

    @Override
    public String toString() {
        return "print(" + expression.toString() + ")";
    }
}
