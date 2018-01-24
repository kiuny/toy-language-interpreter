package com.andrei.impl.domain.statement;

import com.andrei.impl.domain.ProgramState;
import com.andrei.impl.domain.exceptions.ToyException;
import com.andrei.impl.domain.expression.Expression;
import com.andrei.impl.domain.expression.Variable;
import com.andrei.interfaces.domain.IStatement;

import java.util.Optional;

public class WriteHeap implements IStatement {
    final String variable;
    final Expression valueExpression;

    public WriteHeap(String variable, Expression valueExpression) {
        this.variable = variable;
        this.valueExpression = valueExpression;
    }


    public Optional<ProgramState> execute(ProgramState state) throws ToyException {
        state.getHeap().write(new Variable(variable).evaluate(state), valueExpression.evaluate(state));

        return Optional.empty();
    }


    public String toString() {
        return "wH(" + variable + ", " + valueExpression.toString() + ")";
    }
}
