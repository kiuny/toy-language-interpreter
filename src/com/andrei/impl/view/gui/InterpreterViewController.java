package com.andrei.impl.view.gui;

import com.andrei.impl.controller.Controller;
import com.andrei.impl.domain.ProgramState;
import com.andrei.impl.domain.ToyFile;
import com.andrei.impl.repository.Repository;
import com.andrei.interfaces.domain.IStatement;
import com.andrei.interfaces.repository.IRepository;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ListBinding;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class InterpreterViewController {

    public TextField numberProgramStates;
    public ListView<String> programListsIdentifiers;
    public ListView<String> outList;
    public ListView<String> executionStack;
    public TableView<Map.Entry<Integer, Integer>> heapTable;
    public TableColumn<Map.Entry<Integer, Integer>, Integer> addressColumn;
    public TableColumn<Map.Entry<Integer, Integer>, Integer> heapValueColumn;
    public TableView<ToyFile> fileTable;
    public TableColumn<ToyFile, Integer> fileDescriptorColumn;
    public TableColumn<ToyFile, String> fileNameColumn;
    public TableView<Map.Entry<String, Integer>> symbolTable;
    public TableColumn<Map.Entry<String, Integer>, String> symbolNameColumn;
    public TableColumn<Map.Entry<String, Integer>, Integer> symbolValueColumn;

    private Controller controller;
    private ProgramState activeProgramState;
    private IRepository repository;

    public void initialize(IStatement statement) {
        activeProgramState = new ProgramState(statement);
        repository = new Repository(activeProgramState, "log" + String.valueOf(statement.toString().hashCode()));
        controller = new Controller(repository);
        initializeBindings();
        render();
    }

    private void renderProgramStates() {
//        numberProgramStates.setText(String.valueOf(repository.getProgramStates().size()));
//        programListsIdentifiers.getItems().setAll(repository.getProgramStates().stream().map(ProgramState::getThreadId).map(Object::toString).collect(Collectors.toList()));
//        programListsIdentifiers.getSelectionModel().select(String.valueOf(activeProgramState.getThreadId()));
    }

    private void initializeBindings() {
        numberProgramStates.textProperty().bind(Bindings.size(repository.getProgramStates()).asString());
        programListsIdentifiers.setItems(new ListBinding<String>() {
            {
                super.bind(repository.getProgramStates());
            }

            @Override
            protected ObservableList<String> computeValue() {
                return FXCollections.observableList(repository.getProgramStates().stream().map(ProgramState::getThreadId).map(String::valueOf).collect(Collectors.toList()));
            }
        });

        addressColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getKey()).asObject());
        heapValueColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getValue()).asObject());

        fileDescriptorColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getFileDescriptor()).asObject());
        fileNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFilename()));

        symbolNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getKey()));
        symbolValueColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getValue()).asObject());
    }

    public void renderSymbolTable() {
        symbolTable.getItems().setAll(FXCollections.observableArrayList(activeProgramState.getSymbolTable().entrySet()));
    }

    public void renderOutList() {
        outList.getItems().setAll(FXCollections.observableArrayList(activeProgramState.getOutput()));
    }

    public void renderHeapTable() {
        heapTable.getItems().setAll(FXCollections.observableArrayList(activeProgramState.getHeap().entrySet()));
    }

    public void renderFileTable() {
        fileTable.getItems().setAll(FXCollections.observableArrayList(activeProgramState.getFileTable().getAllFiles()));
    }

    public void renderExecutionStack() {
        executionStack.getItems().setAll(
                FXCollections.observableArrayList(
                        activeProgramState.getExecutionStack()
                                .stream()
                                .map(Object::toString)
                                .collect(Collectors.toList()))
        );
        Collections.reverse(executionStack.getItems());
    }

    public void runOneStep(ActionEvent actionEvent) {
        try {
            controller.oneStep();
            render();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText(e.getClass().getSimpleName());
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    private void render() {
        renderProgramStates();
        renderOutList();
        renderSymbolTable();
        renderHeapTable();
        renderExecutionStack();
        renderFileTable();
    }
}
