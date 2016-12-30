/*
 * Class Name:   RecipeMain
 * Name:         Ankita Kapoor
 * Student ID:   18358877
 * User name:    18358877
 * Subject Code: CSE4OAD
 */

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.geometry.*;
import java.util.*;
import java.io.*;
import javafx.collections.*;
import javafx.collections.transformation.*;
import javafx.scene.control.cell.*;
import javafx.beans.property.*;

public class RecipeMain extends Application
{
   private ObservableList<Recipe> tableData;
   private TableView<Recipe> tableView;
   private RecipeDSC recipeDSC;
   private RecipeStageMaker recipeSM;

   public void start(Stage primaryStage) throws Exception
   {
      build(primaryStage);
      primaryStage.setTitle(getClass().getName());
      primaryStage.show();
   }

   public void build(Stage primaryStage) throws Exception
   {
      loadData();

      Node searchArea = makeSearchArea();
      Node tableViewArea = makeTableView();
      Node addViewDeleteArea = makeViewAddDeleteArea(primaryStage);

      VBox root = new VBox();
      root.getChildren().addAll(searchArea, tableViewArea, addViewDeleteArea);
      Scene scene = new Scene(root,800,480);
      root.setStyle("-fx-alignment: center; -fx-font-size: 10; -fx-spacing: 10");
      root.setPadding(new Insets(10, 5, 10, 5));

      root.setAlignment(Pos.CENTER);
      primaryStage.setScene(scene);
      recipeSM = new RecipeStageMaker(recipeDSC,tableData,tableView,primaryStage);
   }

   // To load data from the database into tableData and then to tableView
   private void loadData() throws Exception
   {
      recipeDSC = new RecipeDSC();
      List<Recipe> recipes = recipeDSC.findAll();

      tableData = FXCollections.observableArrayList();
      tableData.clear();
      tableData.addAll(recipes);
      tableView = new TableView<Recipe>();
      tableView.setItems(tableData);
   }

   // To allow the user to search the table view
   private Node makeSearchArea()
   {
      Label filterLB = new Label("Enter Search Text: ");
      TextField filterTF = new TextField();
      filterTF.setPrefWidth(200);
      HBox filterHBox = new HBox(filterLB, filterTF);
      filterHBox.setAlignment(Pos.CENTER);

      Label searchBy = new Label("Search by : ");
      RadioButton anyField = new RadioButton(" Any Field ");
      RadioButton ingr = new RadioButton(" Ingredients ");
      RadioButton rName = new RadioButton(" Recipe Name ");
      ToggleGroup fontTG = new ToggleGroup();
      anyField.setToggleGroup(fontTG);
      ingr.setToggleGroup(fontTG);
      rName.setToggleGroup(fontTG);
      anyField.setSelected(true);

      FilteredList<Recipe> filteredList =new FilteredList<Recipe>(tableView.getItems(),recipes -> true);
      SortedList<Recipe> sortedList = new SortedList<Recipe>(filteredList);
      sortedList.comparatorProperty().bind(tableView.comparatorProperty());
      tableView.setItems(sortedList);
      RadioButton selectedRB = (RadioButton) fontTG.getSelectedToggle();

      ////////////////////////////// TableData is filtered on selection of radio buttons 
      fontTG.selectedToggleProperty().addListener( (observable, oldValue, newValue) ->
            {
            filteredList.setPredicate((recipe)->
                  {
                  String filterString = filterTF.getText().trim();
                  filterString=filterString.toUpperCase();

                  if (filterString == null )
                  {
                  return true;
                  }

                  else
                  {
                  if(newValue ==ingr )
                  {
                  return recipe.getIngredients().toUpperCase().contains(filterString);
                  }
                  else if(newValue ==rName)
                  {
                  return recipe.getName().toUpperCase().contains(filterString);
                  }
                  else if(newValue ==anyField)
                  {
                     return recipe.getName().toUpperCase().contains(filterString)||
                        recipe.getIngredients().toUpperCase().contains(filterString)||
                        recipe.getRemarks().toUpperCase().contains(filterString);
                  }
                  else
                  {
                     return true;
                  }
                  }
                  }
            );
            });
      /////////////////////////////////TableData is filtered when text is entered in the search field
      filterTF.textProperty().addListener((observable, oldValue, newValue)->
            {
            filteredList.setPredicate((recipe)->
                  {
                  RadioButton sRB = (RadioButton) fontTG.getSelectedToggle();
                  if (newValue == null || newValue.isEmpty())
                  {
                  return true;
                  }

                  else
                  {
                  String filterString = newValue.toUpperCase();

                  if (sRB== ingr) 
                  {
                  return recipe.getIngredients().toUpperCase().contains(filterString);
                  }
                  else if(sRB== rName)
                  {
                  return recipe.getName().toUpperCase().contains(filterString);
                  }
                  else if(sRB==anyField)
                  {
                     return recipe.getName().toUpperCase().contains(filterString)||
                        recipe.getIngredients().toUpperCase().contains(filterString)||
                        recipe.getRemarks().toUpperCase().contains(filterString);
                  }
                  else
                  {
                     return true;
                  }
                  }
                  });
            });
      ///////////////////////////////////////////////////////////////////////////////////////////////////

      HBox fontHB = new HBox(searchBy,anyField, ingr, rName);
      fontHB.setAlignment(Pos.CENTER);
      fontHB.setSpacing(10);
      VBox root = new VBox();
      root.getChildren().addAll(filterHBox,fontHB);
      root.setSpacing(5);
      return root;
   }

   // To Define the table view and put it in a HBox. Return the HBox
   private Node makeTableView()
   {
      TableColumn<Recipe, Integer> idColumn =new TableColumn<Recipe,Integer>("Id");
      idColumn.setCellValueFactory(new PropertyValueFactory<Recipe, Integer>("id"));

      TableColumn<Recipe, String> nameColumn =new TableColumn<Recipe, String>("Recipe Name");
      nameColumn.setCellValueFactory(new PropertyValueFactory<Recipe, String>("name"));

      TableColumn<Recipe, Integer> servesColumn = new TableColumn<Recipe, Integer>("Serves");
      servesColumn.setCellValueFactory(new PropertyValueFactory<Recipe, Integer>("serves"));

      TableColumn<Recipe, String> ingColumn =new TableColumn<Recipe, String>("Ingredients");
      ingColumn.setCellValueFactory(new PropertyValueFactory<Recipe, String>("ingredients"));

      TableColumn<Recipe, String> remarksColumn =new TableColumn<Recipe, String>("Remarks");
      remarksColumn.setCellValueFactory(new PropertyValueFactory<Recipe, String>("remarks"));

      tableView.getColumns().add(idColumn);
      tableView.getColumns().add(nameColumn);
      tableView.getColumns().add(servesColumn);
      tableView.getColumns().add(ingColumn);
      tableView.getColumns().add(remarksColumn);

      tableView.setMinWidth(700);
      tableView.setMaxWidth(750);

      idColumn.setMinWidth(25);
      nameColumn.setMinWidth(150);
      servesColumn.setMinWidth(25);
      ingColumn.setMinWidth(350);
      remarksColumn.setMinWidth(150);

      HBox h = new HBox();
      h.getChildren().add(tableView);
      h.setAlignment(Pos.CENTER);

      // To enable text wrap around.
      ingColumn.setCellFactory(
            (TableColumn<Recipe, String> param) ->
            {
            return new TableCell<Recipe, String>()
            {
            @Override
            public void updateItem(String item, boolean empty)
            {
            super.updateItem(item, empty);
            Text text = new Text(item);
            text.wrappingWidthProperty().bind(ingColumn.widthProperty());
            this.setWrapText(true);
            setGraphic(text);
            }
            };
            });
      return h;
   }

   // To Create the area with the buttons to view, add and delete recipes
   private Node makeViewAddDeleteArea(Stage primaryStage)
   {
      Button updateBT = new Button("View/Update Selected Recipe");
      Button addBT = new Button("Add Recipe");
      Button deleteBT = new Button("Delete Selected Recipe");
      recipeSM = new RecipeStageMaker(recipeDSC,tableData,tableView,primaryStage);
      //////////////////////////////////
      updateBT.setOnAction((e) ->
            {
            Recipe selectedRecipe = tableView.getSelectionModel().getSelectedItem();
            if(selectedRecipe== null)
            {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Please select specific Recipe to Update!");
            alert.show();
            }
            else
            {
            recipeSM.showViewRecipeStage();
            }     
            });	
      //////////////////////////////////////
      addBT.setOnAction((e) ->
            {
            recipeSM.showAddRecipeStage();
            });	
      //////////////////////////////////////////
      deleteBT.setOnAction((e) ->
            {
            Recipe selectedRecipe = tableView.getSelectionModel().getSelectedItem();
            if(selectedRecipe== null)
            {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Please select specific Recipe to Delete!");
            alert.show();
            }
            else
            {
            recipeSM.showDeleteRecipeStage();
            }  
            });	
      ///////////////////////////////////////////

      HBox bt = new HBox(updateBT,addBT,deleteBT);

      bt.setStyle("-fx-border-style: solid; -fx-border-width: 1 1 1 1;"
            + "-fx-border-color: black");
      bt.setPadding(new Insets(10, 5, 10, 5));
      bt.setAlignment(Pos.CENTER);
      bt.setSpacing(10);
      return bt;
   }
}

