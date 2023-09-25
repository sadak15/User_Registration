//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example.register;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;


public class HelloController implements Initializable {
    @FXML
    private TableView<Register> TableView;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnUpdate;
    @FXML
    private TableColumn<Register, String> coPassword;
    @FXML
    private TableColumn<Register, Integer> coPhone;
    @FXML
    private TableColumn<Register, String> coUsername;
    @FXML
    private TableColumn<Register, String> colFullname;
    @FXML
    private TableColumn<Register, Integer> coId;
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtPassword;
    @FXML
    private TextField txtPhone;
    @FXML
    private TextField txtSearch;
    @FXML
    private TextField txtUsername;
    private ObservableList<Register> list;
    private FilteredList<Register> filteredData;
    String dburl = "jdbc:mysql://localhost:3306/ahmed";
    String user = "root";
    String pass = "";
    Connection con;
    Statement st;
    PreparedStatement pst;
    private Register selectedRegister;
    private boolean isFiltering = false;

    public HelloController() {
    }

    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.list = FXCollections.observableArrayList();
        this.coId.setCellValueFactory(new PropertyValueFactory("Id"));
        this.colFullname.setCellValueFactory(new PropertyValueFactory("Name"));
        this.coUsername.setCellValueFactory(new PropertyValueFactory("Username"));
        this.coPassword.setCellValueFactory(new PropertyValueFactory("Password"));
        this.coPhone.setCellValueFactory(new PropertyValueFactory("Phone"));
        this.populate();
        this.filteredData = new FilteredList(this.list, (p) -> {
            return true;
        });
        this.TableView.setItems(this.filteredData);
        this.txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            this.filter(newValue);
        });
    }

    public void populate() {
        try {
            this.con = DriverManager.getConnection(this.dburl, this.user, this.pass);
            System.out.println("Connected...");
            String sql = "SELECT * FROM tbahmed";
            this.pst = this.con.prepareStatement(sql);
            ResultSet rs = this.pst.executeQuery();
            this.list.clear();

            while(rs.next()) {
                int id = rs.getInt(1);
                String name = rs.getString(2);
                String username = rs.getString(3);
                String password = rs.getString(4);
                int phone = rs.getInt(5);
                this.list.add(new Register(id, name, username, password, phone));
            }

            this.TableView.setItems(this.list);
        } catch (SQLException var8) {
            System.out.println(var8.getMessage());
        }

    }

    public void insertData() {
        String name = this.txtName.getText().trim();
        String username = this.txtUsername.getText().trim();
        String password = this.txtPassword.getText().trim();
        String phoneText = this.txtPhone.getText().trim();
        Alert alert;
        if (username.isEmpty()) {
            alert = new Alert(AlertType.ERROR);
            alert.setHeaderText((String)null);
            alert.setContentText("Username is Empty. Please try again.!");
            alert.showAndWait();
        } else if (password.isEmpty()) {
            alert = new Alert(AlertType.ERROR);
            alert.setHeaderText((String)null);
            alert.setContentText("Password is Empty. Please try again.!");
            alert.showAndWait();
        } else if (phoneText.isEmpty()) {
            alert = new Alert(AlertType.ERROR);
            alert.setHeaderText((String)null);
            alert.setContentText("Phone number is Empty. Please try again.");
            alert.showAndWait();
        } else {
            int phone;
            Alert alerts;
            try {
                phone = Integer.parseInt(phoneText);
            } catch (NumberFormatException var10) {
                alert = new Alert(AlertType.ERROR);
                alert.setHeaderText((String)null);
                alert.setContentText("Invalid phone number");
                alert.showAndWait();
                return;
            }

            String sql = "INSERT INTO tbahmed (Name, Username, Password, Phone) VALUES (?, ?, ?, ?)";

            try {
                this.pst = this.con.prepareStatement(sql);
                this.pst.setString(1, name);
                this.pst.setString(2, username);
                this.pst.setString(3, password);
                this.pst.setInt(4, phone);
                this.pst.executeUpdate();
                System.out.println("Data inserted...");
                this.populate();
                alert = new Alert(AlertType.INFORMATION);
                alert.setHeaderText((String)null);
                alert.setContentText("Successfully Registered");
                alert.showAndWait();
            } catch (SQLException var9) {
                System.out.println(var9.getMessage());
                Alert alerts1 = new Alert(AlertType.ERROR);
                alerts1.setHeaderText((String)null);
                alerts1.setContentText("Error occurred");
                alerts1.showAndWait();
            }

        }
    }

    public void updateData() {
        if (this.selectedRegister == null) {
            System.out.println("No register selected.");
        } else {
            String sql = "UPDATE tbahmed SET Name=?, Username=?, Password=?, Phone=? WHERE id=?";
            String Name = this.txtName.getText();
            String Username = this.txtUsername.getText();
            String Password = this.txtPassword.getText();
            int Phone = Integer.parseInt(this.txtPhone.getText());

            try {
                this.pst = this.con.prepareStatement(sql);
                this.pst.setString(1, Name);
                this.pst.setString(2, Username);
                this.pst.setString(3, Password);
                this.pst.setInt(4, Phone);
                this.pst.setInt(5, this.selectedRegister.getId());
                this.pst.executeUpdate();
                System.out.println("Data Updated...");
                this.selectRow();
                this.populate();
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText((String)null);
                alert.setContentText("Successfully Updated");
                alert.showAndWait();
            } catch (SQLException var8) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText((String)null);
                alert.setContentText("Error occurred while Updating data.");
                alert.showAndWait();
            }

        }
    }

    public void deleteData() {
        String sql = "DELETE FROM tbahmed WHERE  Name=?";
        String Name = this.txtName.getText();
        try {
            this.pst = this.con.prepareStatement(sql);
            this.pst.setString(1, Name);
            this.pst.executeUpdate();
            System.out.println("Data deleted...");
            this.populate();
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText((String)null);
            alert.setContentText("Successfully Deleted");
            alert.showAndWait();
        } catch (SQLException var5) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText((String)null);
            alert.setContentText("Error occurred while Deleting data.");
            alert.showAndWait();
        }
       //delete
    }

    public void selectRow() {
        this.selectedRegister = (Register)this.TableView.getSelectionModel().getSelectedItem();
        if (this.selectedRegister != null) {
            this.txtName.setText(this.selectedRegister.getName());
            this.txtUsername.setText(this.selectedRegister.getUsername());
            this.txtPassword.setText(this.selectedRegister.getPassword());
            this.txtPhone.setText(String.valueOf(this.selectedRegister.getPhone()));
        } else {
            this.txtName.setText("");
            this.txtUsername.setText("");
            this.txtPassword.setText("");
            this.txtPhone.setText("");
        }

    }

    public void filter(String searchText) {
        if (this.isFiltering) {
            ObservableList<Register> tableViewData = this.TableView.getItems();
            this.filteredData.setPredicate((register) -> {
                if (searchText != null && !searchText.isEmpty()) {
                    try {
                        int searchId = Integer.parseInt(searchText);
                        if (register.getId() == searchId) {
                            return true;
                        }
                    } catch (NumberFormatException var3) {
                    }

                    return false;
                } else {
                    return true;
                }
            });
            this.TableView.setItems(this.filteredData);
        }
    }

    public void btnSearchAction(ActionEvent event) {
        String searchText = this.txtSearch.getText().trim();
        this.isFiltering = true;
        this.filter(searchText);
        this.isFiltering = false;
    }
}
