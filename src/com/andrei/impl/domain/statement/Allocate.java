package com.andrei.impl.domain.statement;

import com.andrei.impl.domain.ProgramState;
import com.andrei.impl.domain.exceptions.ToyException;
import com.andrei.impl.domain.expression.Expression;
import com.andrei.interfaces.domain.IStatement;

import java.util.Optional;

public class Allocate implements IStatement{

    String variable;
    Expression valueExpression;

    public Allocate(String variable, Expression valueExpression) {
        this.variable = variable;
        this.valueExpression = valueExpression;
    }

    @Override
    public Optional<ProgramState> execute(ProgramState state) throws ToyException {
        Integer allocatedMemoryAddress = state.getHeap().allocate();
        state.getSymbolTable().put(variable, allocatedMemoryAddress);
        state.getHeap().write(allocatedMemoryAddress, valueExpression.evaluate(state));

        return Optional.empty();
    }

    @Override
    public String toString() {
        return "new(" + variable + ", " + valueExpression.toString() + ")";
    }
}
