package com.smy.todoList;

import com.smy.todoList.datamodel.TodoData;
import com.smy.todoList.datamodel.TodoItem;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.util.Callback;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Optional;
import java.util.function.Predicate;


public class Controller {

    @FXML
    private ListView<TodoItem> listView;

    @FXML
    private TextArea detailView;

    @FXML
    private Label dueDate;

    @FXML
    private BorderPane mainBorderPane;

    @FXML
    private ContextMenu listContextMenu;

    @FXML
    ToggleButton toggleButton;

    private FilteredList<TodoItem> filteredList;

    private Predicate<TodoItem> unfilteredItems;
    private Predicate<TodoItem> todayItems;



    public void initialize() {

        listContextMenu = new ContextMenu();

        MenuItem deleteMenuItem = new MenuItem("Delete");
        deleteMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                TodoItem item = listView.getSelectionModel().getSelectedItem();

                deleteItem(item);

            }
        });

        MenuItem editMenuItem = new MenuItem("Edit");

        editMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

            // DO SOMETHING TO EDIT

                showEditItemDialog();


            }
        });

        listContextMenu.getItems().addAll(editMenuItem);
        listContextMenu.getItems().addAll(deleteMenuItem);

        listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TodoItem>() {
            @Override
            public void changed(ObservableValue<? extends TodoItem> observable, TodoItem oldValue, TodoItem newValue) {
                if (newValue != null) {
                    TodoItem item = listView.getSelectionModel().getSelectedItem();
                    detailView.setText(item.getLongDescription());
                    DateTimeFormatter df = DateTimeFormatter.ofPattern("MMM d, yy");
                    dueDate.setText(df.format(item.getDueDate()));

                }
            }
        });

        unfilteredItems = new Predicate<TodoItem>() {
          @Override
            public boolean test(TodoItem todoItem) {
              return true;
          }
        };

        todayItems = new Predicate<TodoItem>() {
            @Override
            public boolean test(TodoItem todoItem) {
                return todoItem.getDueDate().equals(LocalDate.now());
            }
        };

        filteredList = new FilteredList<>(TodoData.getInstance().getTodoItemList(),unfilteredItems);

        SortedList<TodoItem> sortedList = new SortedList<>(filteredList, new Comparator<TodoItem>() {

            @Override
            public int compare(TodoItem o1, TodoItem o2) {
                return o1.getDueDate().compareTo(o2.getDueDate());
            }
        });



        listView.setItems(sortedList);
        listView.getSelectionModel().selectFirst();
        listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        detailView.editableProperty().setValue(false);

        listView.setCellFactory(new Callback<ListView<TodoItem>, ListCell<TodoItem>>() {
            @Override
            public ListCell<TodoItem> call(ListView<TodoItem> param) {

                ListCell<TodoItem> cell = new ListCell<TodoItem>() {
                    @Override
                    protected void updateItem(TodoItem item, boolean empty) {
                        super.updateItem(item, empty);

                        if (empty) setText(null);

                        else {
                            setText(item.getShortDescription());

                            if (item.getDueDate().isBefore(LocalDate.now().plusDays(1)))

                                setTextFill(Color.RED);

                            else if (item.getDueDate().equals(LocalDate.now().plusDays(1))) {
                                setTextFill(Color.DARKORANGE);
                            }

                        }


                    }
                };

                cell.emptyProperty().addListener((obs,wasEmpty,isNowEmpty) ->
                {
                    if (isNowEmpty) cell.setContextMenu(null);

                    else cell.setContextMenu(listContextMenu);

                });



                return cell;
            }
        });

        }

        @FXML
        public void showNewItemDialog() {

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.initOwner(mainBorderPane.getScene().getWindow());
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("todoItemDialog.fxml"));

            dialog.setTitle("Add New Item");
            dialog.setHeaderText("This Adds new Item");

            try {
                dialog.getDialogPane().setContent(fxmlLoader.load());

            }   catch (IOException e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
                return;
            }

            dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
            dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

            Optional<ButtonType> result = dialog.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {

                System.out.println("OK Pressed!");
                DialogController dialogController = fxmlLoader.getController();

             TodoItem newTodoItem = dialogController.processResult();

                listView.getSelectionModel().select(newTodoItem);

            }
            else
                System.out.println("Cancel Pressed");

        }

    public void showEditItemDialog() {

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBorderPane.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("editItemDialog.fxml"));

        dialog.setTitle("Edit New Item");
        dialog.setHeaderText("This Edits Item");

        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());

        }   catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return;
        }

        DialogController dialogController = fxmlLoader.getController();

        TodoItem selectedItem = listView.getSelectionModel().getSelectedItem();

         dialogController.preEditItem(selectedItem);


        dialog.getDialogPane().getButtonTypes().add(ButtonType.APPLY);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.APPLY) {

            System.out.println("Apply Pressed!");


            TodoItem newTodoItem = dialogController.processResult();

            TodoData.getInstance().getTodoItemList().remove(selectedItem);

            listView.getSelectionModel().select(newTodoItem);


        }
        else
            System.out.println("Cancel Pressed");

    }

        private void  deleteItem(TodoItem item) {

        Alert deleteAlert = new Alert(Alert.AlertType.CONFIRMATION);

        deleteAlert.setHeaderText("Delete Item: " + item.getShortDescription());
        deleteAlert.setTitle("Delete Item");
        deleteAlert.setContentText("Are you sure? press Ok to confirm, cancel to back out.");

        Optional<ButtonType> result = deleteAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            TodoData.getInstance().deleteItem(item);
        }

        }

        @FXML
        private void handleDeleteKeyPress(KeyEvent keyEvent) {

            TodoItem item = listView.getSelectionModel().getSelectedItem();

            if (item != null)

                if (keyEvent.getCode() == KeyCode.DELETE)

                          deleteItem(item);

        }

        public void handleFilterButton() {

        TodoItem todoItem = listView.getSelectionModel().getSelectedItem();

            if (toggleButton.isSelected()) {
                filteredList.setPredicate(todayItems);

                if (filteredList.isEmpty()) {
                    detailView.setText("");
                    dueDate.setText("");
                }

                else if (filteredList.contains(todoItem)) listView.getSelectionModel().select(todoItem);

                else listView.getSelectionModel().selectFirst();
            }
            else {

                filteredList.setPredicate(unfilteredItems);
                listView.getSelectionModel().select(todoItem);
            }


        }

    @FXML
    public void exitProgram() {
        Platform.exit();
    }

}
