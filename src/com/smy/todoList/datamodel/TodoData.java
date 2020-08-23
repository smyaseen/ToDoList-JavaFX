package com.smy.todoList.datamodel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TodoData {

    private static final TodoData instance = new TodoData();
    private final String fileName = "TodoList.txt";

    private ObservableList<TodoItem> todoItemList;
    private final DateTimeFormatter dateTimeFormatter;

    private TodoData() {
        dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    }

    public static TodoData getInstance() {
        return instance;
    }

    public ObservableList<TodoItem> getTodoItemList() {
        return  todoItemList;
    }

    public void setTodoItemList(ObservableList<TodoItem> todoItemList) {
        this.todoItemList = todoItemList;
    }

    public void addTodoItem(TodoItem todoItem) {
        todoItemList.add(todoItem);
    }

    public void loadTodoItems() throws IOException {

        todoItemList = FXCollections.observableArrayList();
        Path path = Paths.get(fileName);
        BufferedReader br = Files.newBufferedReader(path);

        String input;

        try {
                while ((input = br.readLine()) != null) {

                    String[] itemPieces = input.split("\t");

                    String shortDescription = itemPieces[0];
                    String longDescription = itemPieces[1];
                    String dueDate = itemPieces[2];

                    LocalDate date = LocalDate.parse(dueDate,dateTimeFormatter);

                    TodoItem todoItem = new TodoItem(shortDescription,longDescription,date);

                    todoItemList.add(todoItem);
                }

        } finally {
            if (br != null) br.close();
        }
    }

    public void saveTodoItems() throws IOException {

        Path path = Paths.get(fileName);
        BufferedWriter bw = Files.newBufferedWriter(path);

        try {

            for (TodoItem todoItem : todoItemList) {

                bw.write(String.format("%s\t%s\t%s",
                        todoItem.getShortDescription(),
                        todoItem.getLongDescription(),
                        todoItem.getDueDate().format(dateTimeFormatter)));

                bw.newLine();
            }

        } finally {
            if (bw != null) bw.close();
        }

    }

    public void deleteItem(TodoItem item) {

        todoItemList.remove(item);
    }


}
