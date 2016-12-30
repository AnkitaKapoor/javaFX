/*
 * Class Name:   RecipeDSC
 * Name:         Ankita Kapoor
 * Student ID:   18358877
 * User name:    18358877
 * Subject Code: CSE4OAD
 */

import java.sql.*;
import java.util.*;

public class RecipeDSC
{
   private static Connection connection;
   private  static Statement statement;
   private static PreparedStatement preparedStatement;
   private static String password;
   private  List<Recipe> recipes = new ArrayList<>();

   public  void getPassword()
   {
      char [] pwd = System.console().readPassword("Password: ");
      password = new String(pwd);
   }

   public static void connect() throws SQLException
   {
      String url = "jdbc:mysql://latcs7.cs.latrobe.edu.au:3306/18358877";
      String user = "18358877";
      password="Karankita_6100";
      connection = DriverManager.getConnection(url, user, password);
      statement = connection.createStatement();
   }

   public static void disconnect() throws SQLException
   {
      if (preparedStatement != null) preparedStatement.close();
      if (statement != null) statement.close();
      if (connection != null) connection.close();
   }

   // To find a Recipe with the given id.
   public static Recipe find(int id) throws SQLException
   {
      connect();

      String queryString = "select * from recipe where id=?";
      preparedStatement = connection.prepareStatement(queryString);
      preparedStatement.setInt(1, id);
      ResultSet rs = preparedStatement.executeQuery();
      Recipe recipe = null;

      if(rs.next()) 
      {
         String name = rs.getString(2);
         Integer serves = rs.getInt(3);
         String ingredients = rs.getString(4);
         String steps = rs.getString(5);
         String remarks = rs.getString(6);
         recipe=new Recipe(id, name, serves,ingredients,steps,remarks);
      }

      disconnect();

      return recipe;
   }

   // To count the total number of Recipes in the database
   public static int count() throws SQLException
   {
      connect();

      String queryString="select count(*) from recipe";
      ResultSet rs = statement.executeQuery(queryString);
      rs.next();
      int count = rs.getInt(1);

      disconnect();

      return count;
   }

   // To obtain and return  the list of all Recipes
   public static List<Recipe> findAll() throws SQLException
   {
      connect();

      String queryString="select * from recipe";
      ResultSet rs = statement.executeQuery(queryString);
      ArrayList<Recipe> recipes = new ArrayList<Recipe>();

      while (rs.next())
      {
         Integer id = rs.getInt(1);
         String name = rs.getString(2);
         Integer serves = rs.getInt(3);
         String ingredients = rs.getString(4);
         String steps = rs.getString(5);
         String remarks = rs.getString(6);
         recipes.add(new Recipe(id, name, serves, ingredients, steps, remarks));
      }

      disconnect();

      return recipes;
   }

   // To add a new Recipe to the database.
   public static int add(String name, int serves, String ingredients, String steps, String remarks) throws SQLException
   {
      connect();

      int last_inserted_id=0;
      String insertString = "insert into recipe (name,serves,ingredients,steps,remarks)values(?, ?, ?, ?,?)";
      preparedStatement = connection.prepareStatement(insertString,Statement.RETURN_GENERATED_KEYS);
      preparedStatement.setString(1, name);
      preparedStatement.setInt(2, serves);
      preparedStatement.setString(3, ingredients);
      preparedStatement.setString(4, steps);
      preparedStatement.setString(5, remarks);
      preparedStatement.executeUpdate();
      ResultSet rs= preparedStatement.getGeneratedKeys();

      if(rs.next())
      {
         last_inserted_id=rs.getInt(1);
      }	

      disconnect();

      return last_inserted_id;
   }

   // To add the new Recipe details.
   public void add(Recipe recipe) throws Exception
   {
      Recipe tmp = find(recipe.getId());
      boolean pre = (tmp == null);

      if (!pre)
      {
         String msg = "Recipe id " + recipe.getId() + " is not new!";
         System.out.println("\nERROR: " + msg);
         throw new Exception(msg);
      }

      recipes.add(recipe);
   }

   // To update an existing Recipe
   public static void update(Recipe recipe) throws Exception
   {
      int id= recipe.getId();
      Recipe tmp = find(recipe.getId());
      boolean pre = (tmp != null);

      if(! pre)
      {
         String msg = "Recipe does not exist!";
         System.out.println("\nERROR: " + msg);
         throw new Exception(msg);
      }

      connect();

      String updateString = "update recipe set "
         +  " name = ?"
         +  ", serves = ?"
         +  ", ingredients = ?"
         +  ", steps = ?"
         + ", remarks = ?"
         +  " where id = ?";

      preparedStatement = connection.prepareStatement(updateString);
      preparedStatement.setString(1,  recipe.getName());
      preparedStatement.setInt(2, recipe.getServes());
      preparedStatement.setString(3, recipe.getIngredients());
      preparedStatement.setString(4, recipe.getSteps());
      preparedStatement.setString(5, recipe.getRemarks());
      preparedStatement.setInt(6, id);
      preparedStatement.executeUpdate();

      disconnect();
   }

   // To delete a Recipe record from the database
   public static void delete(int id) throws Exception
   {
      Recipe recipe = find(id);
      boolean pre = (recipe != null);

      if(! pre)
      {
         String msg = "Recipe does not exist!";
         System.out.println("\nERROR: " + msg);
         throw new Exception(msg);
      }

      connect();

      String deleteStatement =
         "delete from recipe " +
         "where id = ? ";

      preparedStatement = connection.prepareStatement(deleteStatement);
      preparedStatement.setInt(1, id);
      preparedStatement.executeUpdate();

      disconnect();
   }

   // To delete a Recipe record
   public void delete(Recipe recipe) throws Exception
   {
      Recipe tmp = find(recipe.getId());
      boolean pre = (tmp != null);

      if (!pre)
      {
         String msg = "Recipe id " + recipe.getId() + " does not exist!";
         System.out.println("\nERROR: " + msg);
         throw new Exception(msg);
      }   

      recipes.remove(recipe);
   }

   public static void main(String [] args) throws Exception
   { 
   }

}
