# training-course-tp-ministore

Training course brief involving creating a small scale ERP with Java linked to a Postgresql DB.
Project using Maven to import required dependencies.


## Environment

This project is intended to be used with a **pre-generated postgresql database** and as such is intended to accomodate the content of this database.

**Database information** is provided by a **.env file** that is then loaded by the DatabaseService class, please refer to **.env.example** for the expected content.

## Features 

Features are handled through dedicated view that are generated or recalled when requested by clicking on one of the views listed in the menu.

Views are divided in 3 categories:

* **Customers**

    * **Index** : list all registered customers.

    * **Add** : display a form to add a new customer and on submit will called a **pre-generated stored procedure** in the database to attempt to insert the new user. Form validation itself is handled through dedicated **FormGroup** and **Validator** classes and selection of cities relies on selected country.

* **Products**
    * **Index** : list all registered products with an optional category selector.

    * **Survey stocks** : list all products with inventory stocks below the given threshold.

* **Orders**
    * **Place order** : display a customer selector and a list of products in a table with the possibility to add a quantity to order through the "Order amount" column. Will create a new order for the selected customer that will contain all items with positive order amount inputed through a **transaction** with full **order rollback** in case of **invalid input or quantity** ordered above the quantity available.

    * **Order history** : display all orders and their content for a user selected customer.