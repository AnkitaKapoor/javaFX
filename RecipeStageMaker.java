/*
 * Class Name:   RecipeStageMaker
 * Name:         Ankita Kapoor
 * Student ID:   18358877
 * User name:    18358877
 * Subject Code: CSE4OAD
 */

import javafx.application.Application;
import javafx.stage.*;
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

public class RecipeStageMaker 
{
   private RecipeDSC recipeDSC = new RecipeDSC();;
   private ObservableList<Recipe> tableData = FXCollections.observableArrayList();
   private TableView<Recipe> tableView= new TableView<Recipe>() ;
   private Stage primaryStage=new Stage();
   private Stage secondaryStage=new Stage();

   // id
   private Label idLB = new Label("Id: ");
   private TextField idTF = new TextField();
   private HBox idHBox = new HBox(idLB, idTF);

   // name
   private Label nameLB = new Label("Recipe Name: ");;
   private TextField nameTF = new TextField();
   private HBox nameHBox = new HBox(nameLB, nameTF);

   // serves
   private Label servesLB = new Label("Serves: ");
   private TextField servesTF = new TextField();
   private HBox servesHBox = new HBox(servesLB, servesTF);

   // ingredients
   private Label ingredientsLB = new Label("Ingredients: ");
   private TextField ingredientsTF = new TextField();
   private HBox ingredientsHBox = new HBox(ingredientsLB, ingredientsTF);

   // steps
   private TextArea stepsTA = new TextArea();
   private HBox stepsHBox = new HBox(stepsTA);

   // remarks
   private Label remarksLB = new Label("Remarks: ");
   private TextField remarksTF = new TextField();
   private HBox remarksHBox = new HBox(remarksLB, remarksTF);

   // action buttons
   private Button addBT = new Button("ADD Recipe");
   private Button updateBT = new Button("UPDATE Recipe");
   private Button deleteBT = new Button("DELETE Recipe");
   private Button cancelBT = new Button("EXIT/CANCEL");
   private HBox up_actionHBox = new HBox(updateBT,cancelBT );

   private VBox root = new VBox();
   private Scene scene = new Scene(root);
   private Stage stage = new Stage();


   java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
   private double width = screenSize.getWidth();
   private double height = screenSize.getHeight();

   // To Initilize the RecipeStageMaker
   public RecipeStageMaker(RecipeDSC recipeDSC, ObservableList<Recipe> tableData, TableView<Recipe> tableView, Stage primaryStage )
   {
      this.recipeDSC=recipeDSC;
      this. tableData= tableData;
      this.tableView=tableView;
      this.primaryStage=primaryStage;
   }

   // To present a stage to view and/or update the recipe selected in the table view
   public void showViewRecipeStage() 
   {
      Recipe selectedRecipe = tableView.getSelectionModel().getSelectedItem();

      idTF.setText(new String() + selectedRecipe.getId());		
      nameTF.setText(selectedRecipe.getName());
      servesTF.setText(new String() + selectedRecipe.getServes());
      ingredientsTF.setText(selectedRecipe.getIngredients());
      stepsTA.setText(selectedRecipe.getSteps());
      remarksTF.setText(selectedRecipe.getRemarks());
      idTF.setDisable(true);
      ingredientsTF.setPrefWidth(300);
      remarksTF.setPrefWidth(300);
      nameTF.setDisable(false);
      servesTF.setDisable(false);
      ingredientsTF.setDisable(false);
      remarksTF.setDisable(false);
      stepsTA.setDisable(false);


      HBox up_actionHBox = new HBox(updateBT,cancelBT );
      up_actionHBox.setAlignment(Pos.CENTER);
      up_actionHBox.setSpacing(10);
      up_actionHBox.setStyle("-fx-border-style: solid; -fx-border-width: 1 1 1 1;"
            + "-fx-border-color: black");
      up_actionHBox.setPadding(new Insets(5, 2, 5, 2));
      root = new VBox();
      root.getChildren().addAll(idHBox,nameHBox,servesHBox,ingredientsHBox,stepsHBox,remarksHBox, up_actionHBox);       
      scene = new Scene(root,380,350);
      root.setSpacing(5);
      root.setStyle(" -fx-font-size: 12");
      root.setAlignment(Pos.CENTER);
      root.setPadding(new Insets(10, 5, 10, 5));
      secondaryStage.setScene(scene);
      secondaryStage.setTitle("View/Update Recipe");
      secondaryStage.setX(width/1.5);
      secondaryStage.setY(height/1.5);  
      secondaryStage.show();

      cancelBT.setOnAction(e ->
            {
            secondaryStage.close();
            });

      updateBT.setOnAction(e ->
            {
            try
            {
            int u=selectedRecipe.getId();
            String name=nameTF.getText().trim();
            int serves=Integer.parseInt(servesTF.getText());
            String ingredients=ingredientsTF.getText().trim();
            String steps=stepsTA.getText().trim();
            String remarks=remarksTF.getText().trim();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Are you sure you want to update this recipe?");
            Optional answer = alert.showAndWait(); 

            if(answer.isPresent() & answer.get() == ButtonType.OK)
            {
            recipeDSC.update(new Recipe(u, name, serves, ingredients,steps,remarks));
            for(Recipe r: tableData)
            {
            if(r.getId()==u)
            {
               r.setName(name);
               r.setServes(serves);
               r.setIngredients(ingredients);
               r.setSteps(steps);
               r.setRemarks(remarks);
               tableView.getColumns().get(0).setVisible(false);
               tableView.getColumns().get(0).setVisible(true);
               break;
            }
            }
            secondaryStage.close();
            }
            }
            catch(Exception exception)
            {
               throw new RuntimeException(exception.getMessage());
            }
            });
   }

   // To present a stage to add a recipe
   public void showAddRecipeStage()
   {
      nameTF.clear();
      servesTF.clear();
      ingredientsTF.clear();
      remarksTF.clear();
      stepsTA.clear();                                 

      root = new VBox();
      HBox add_actionHBox = new HBox(addBT ,cancelBT);
      add_actionHBox.setAlignment(Pos.CENTER);
      add_actionHBox.setSpacing(10);
      add_actionHBox.setStyle("-fx-border-style: solid; -fx-border-width: 1 1 1 1;"
            + "-fx-border-color: black");
      add_actionHBox.setPadding(new Insets(5, 2, 5, 2));


      ingredientsTF.setPrefWidth(250);
      remarksTF.setPrefWidth(250);
      root.getChildren().addAll(nameHBox,servesHBox,ingredientsHBox,stepsHBox,remarksHBox,add_actionHBox);
      scene = new Scene(root,380,300);
      root.setSpacing(5);
      root.setStyle(" -fx-font-size: 12");
      root.setAlignment(Pos.CENTER);
      root.setPadding(new Insets(10, 5, 10, 5));
      secondaryStage.setScene(scene);
      secondaryStage.setTitle("Add Recipe");
      secondaryStage.setX(width/1.5);
      secondaryStage.setY(height/1.5);

      secondaryStage.show();

      cancelBT.setOnAction(e ->
            {
            secondaryStage.close();
            });

      addBT.setOnAction(e ->
            {
            try
            {

            if((nameTF.getText().trim().isEmpty()) || (ingredientsTF.getText().trim().isEmpty()) || (stepsTA.getText().trim().isEmpty()) || (remarksTF.getText().trim().isEmpty()))
            {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Pleasee fill all the deatils to add recipe!");
            Optional answer = alert.showAndWait();
            }
            else
            {
            String name=nameTF.getText().trim();
            int serves=Integer.parseInt(servesTF.getText());
            String ingredients=ingredientsTF.getText().trim();
            String steps=stepsTA.getText().trim();
            String remarks=remarksTF.getText().trim();

            ///////////////////////////////////
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Are you sure you want to add this recipe?");
            Optional answer = alert.showAndWait();  

            if(answer.isPresent() & answer.get() == ButtonType.OK)
            {
               int id = RecipeDSC.add(name, serves, ingredients,steps,remarks);
               tableData.add(new Recipe(id, name, serves, ingredients,steps,remarks));
               secondaryStage.close();
            } 
            ///////////////////////////////////////////////////
            } 
            }
            catch(Exception exception)
            {
               throw new RuntimeException(exception.getMessage());
            }
            });	
   }

   // To present a stage to delete the recipe selected in the table view
   public void showDeleteRecipeStage()
   {
      Recipe selectedRecipe = tableView.getSelectionModel().getSelectedItem();

      idTF.setText(new String() + selectedRecipe.getId());
      nameTF.setText(selectedRecipe.getName());
      servesTF.setText(new String() + selectedRecipe.getServes());
      ingredientsTF.setText(selectedRecipe.getIngredients());
      ingredientsTF.setPrefWidth(275);
      remarksTF.setText(selectedRecipe.getRemarks());
      remarksTF.setPrefWidth(275);

      idTF.setDisable(true);
      nameTF.setDisable(true);
      servesTF.setDisable(true);
      ingredientsTF.setDisable(true);
      remarksTF.setDisable(true);

      HBox del_actionHBox = new HBox(deleteBT ,cancelBT);
      del_actionHBox.setAlignment(Pos.CENTER);
      del_actionHBox.setSpacing(10);
      del_actionHBox.setStyle("-fx-border-style: solid; -fx-border-width: 1 1 1 1;"
            + "-fx-border-color: black");
      del_actionHBox.setPadding(new Insets(5, 2, 5, 2));

      int i=selectedRecipe.getId();
      root = new VBox();
      root.getChildren().addAll(idHBox,nameHBox,servesHBox,ingredientsHBox,remarksHBox,del_actionHBox);		
      scene = new Scene(root,380,180);
      root.setSpacing(5);
      root.setStyle(" -fx-font-size: 12");
      root.setPadding(new Insets(10, 5, 10, 5));
      root.setAlignment(Pos.CENTER);
      secondaryStage.setScene(scene);
      secondaryStage.setTitle("Delete Recipe");
      secondaryStage.setX(width/1.5);
      secondaryStage.setY(height/1.5);

      secondaryStage.show();

      cancelBT.setOnAction(e ->
            {
            secondaryStage.close();
            });	    

      deleteBT.setOnAction((e) ->
            {
            try
            {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Are you sure you want to delete this recipe?");
            Optional answer = alert.showAndWait();  
            if(answer.isPresent() & answer.get() == ButtonType.OK)
            {
            RecipeDSC.delete(i);
            tableData.remove(selectedRecipe);
            secondaryStage.close();
            }}
            catch(Exception exception)
            {
            throw new RuntimeException(exception.getMessage());
            }
            });	 
   }
}
