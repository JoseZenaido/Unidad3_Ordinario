package utng.edu.mx.employee.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import utng.edu.mx.employee.model.Customer;
import utng.edu.mx.employee.model.Employee;
import utng.edu.mx.employee.model.MethodPayment;
import utng.edu.mx.employee.model.OrderDetail;
import utng.edu.mx.employee.model.OrderHeader;

/**
 * Created by ANONYMOUS on 7/02/17.
 */

public final class DBOperations {
    private static EmployeeBD db;
    private static DBOperations operations;

    private static final String JOIN_ORDER_CUSTOMER_METHOD =
            "order_header " +
            "INNER JOIN customer " +
            "ON order_header.id_customer = customer.id " +
            "INNER JOIN method_payment " +
            "ON order_header.id_method_payment = method_payment.id";
    private final String[] orderProj = new String[]{
            EmployeeBD.Tables.ORDER_HEADER + "."
                    + EmployeeContract.OrderHeaders.ID,
            EmployeeContract.OrderHeaders.DATE,
            EmployeeContract.Customers.FIRSTNAME,
            EmployeeContract.Customers.LASTNAME,
            EmployeeContract.MethodsPayment.NAME};

    private DBOperations(){

    }

    public static DBOperations getDBOperations(
            Context context){
        if(operations==null) {
            operations = new DBOperations();
        }
        if(db==null){
            db = new EmployeeBD(context);
        }
        return operations;
    }
    //Operations of  Orders
    public Cursor getOrders(){
        SQLiteDatabase database = db.getReadableDatabase();
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(JOIN_ORDER_CUSTOMER_METHOD);

        return  builder.query(database, orderProj,
                null, null, null, null, null);
    }

    public Cursor getOrderById(String id){
        SQLiteDatabase database = db.getWritableDatabase();
        String selection = String.format("%s=?",
                EmployeeContract.OrderHeaders.ID);
        String[] selectionArgs = {id};
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(JOIN_ORDER_CUSTOMER_METHOD);
        String[] projection = {
                EmployeeBD.Tables.ORDER_HEADER+"."
                        + EmployeeContract.OrderHeaders.ID,
                EmployeeContract.OrderHeaders.DATE,
                EmployeeContract.Customers.FIRSTNAME,
                EmployeeContract.Customers.LASTNAME,
                EmployeeContract.MethodsPayment.NAME
        };
        return builder.query(database, projection, selection,
                selectionArgs, null, null, null);
    }

    public String insertOrderHeader(OrderHeader orderHeader){
        SQLiteDatabase database = db.getWritableDatabase();
        ContentValues values = new ContentValues();
        String idOrderHeader =
                EmployeeContract.OrderHeaders.generateIdOrderHeader();
        values.put(EmployeeContract.OrderHeaders.ID, idOrderHeader);
        values.put(EmployeeContract.OrderHeaders.DATE,
                orderHeader.getDate());
        values.put(EmployeeContract.OrderHeaders.ID_CUSTOMER,
                orderHeader.getIdCustomer());
        values.put(EmployeeContract.OrderHeaders.ID_METHOD_PAYMENT,
                orderHeader.getIdMethodPayment());

        database.insertOrThrow(EmployeeBD.Tables.ORDER_HEADER,
                null, values);
        return idOrderHeader;
    }

    public boolean updateOrderHeader(OrderHeader orderHeader){
        SQLiteDatabase database = db.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(EmployeeContract.OrderHeaders.DATE,
                orderHeader.getDate());
        values.put(EmployeeContract.OrderHeaders.ID_CUSTOMER,
                orderHeader.getIdCustomer());
        values.put(EmployeeContract.OrderHeaders.ID_METHOD_PAYMENT,
                orderHeader.getIdMethodPayment());

        String whereClause = String.format("%s=?", EmployeeContract.OrderHeaders.ID);
        String[] whereArgs = {orderHeader.getIdOrderHeader()};

        int result = database.update(EmployeeBD.Tables.ORDER_HEADER, values, whereClause, whereArgs);
        return result>0;
    }

    public boolean deleteOrderHeader(String idOrderHeader){
        SQLiteDatabase database = db.getWritableDatabase();
        String whereClause =
                EmployeeContract.OrderHeaders.ID + "=?";
        String[] whereArgs = {idOrderHeader};

        int result =  database.delete(
                EmployeeBD.Tables.ORDER_HEADER, whereClause, whereArgs);
        return result > 0;
    }
    //Operations of  OrderDetails
    public Cursor getOrderDetails(){
        SQLiteDatabase database = db.getReadableDatabase();
        String sql = String.format("SELECT * FROM %s",
                EmployeeBD.Tables.ORDER_DETAIL);
        return  database.rawQuery(sql, null);
    }

    public Cursor getOrderDetailById(String id){
        SQLiteDatabase database = db.getReadableDatabase();
        String sql = String.format("SELECT * FROM %s" +
                " WHERE %s=?", EmployeeBD.Tables.ORDER_DETAIL,
                EmployeeContract.OrderHeaders.ID);
        String[] whereArgs = {id};
        return database.rawQuery(sql, whereArgs);
    }

    public String insertOrderDetail(OrderDetail orderDetail){
        SQLiteDatabase database = db.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(EmployeeContract.OrderDetails.ID,
                orderDetail.getIdOrderHeader());
        values.put(EmployeeContract.OrderDetails.SEQUENCE,
                orderDetail.getSequence());
        values.put(EmployeeContract.OrderDetails.ID_PRODUCT,
                orderDetail.getIdEmployee());
        values.put(EmployeeContract.OrderDetails.QUANTITY,
                orderDetail.getQuantity());
        values.put(EmployeeContract.OrderDetails.PRICE,
                orderDetail.getPrice());

        database.insertOrThrow(EmployeeBD.Tables.ORDER_DETAIL,
                null, values);
        return String.format("%s#%d",
                orderDetail.getIdOrderHeader(),
                orderDetail.getSequence());
    }

    public boolean updateOrderDetail(OrderDetail orderDetail){
        SQLiteDatabase database = db.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(EmployeeContract.OrderDetails.SEQUENCE,
                orderDetail.getSequence());
        values.put(EmployeeContract.OrderDetails.QUANTITY,
                orderDetail.getQuantity());
        values.put(EmployeeContract.OrderDetails.PRICE,
                orderDetail.getPrice());

        String whereClause = String.format("%s=? AND %s=?",
                EmployeeContract.OrderDetails.ID,
                EmployeeContract.OrderDetails.SEQUENCE);
        String[] whereArgs = {orderDetail.getIdOrderHeader(),
        String.valueOf(orderDetail.getSequence())};

        int result = database.update(EmployeeBD.Tables.ORDER_DETAIL,
                values, whereClause, whereArgs);
        return result>0;
    }

    public boolean deleteOrderDetail(String idOrderDetail){
        SQLiteDatabase database = db.getWritableDatabase();
        String whereClause =
                EmployeeContract.OrderHeaders.ID + "=?";
        String[] whereArgs = {idOrderDetail};

        int result =  database.delete(
                EmployeeBD.Tables.ORDER_DETAIL, whereClause, whereArgs);
        return result > 0;
    }
    //Operations of  Employee
    public Cursor getEmployee(){
        SQLiteDatabase database = db.getReadableDatabase();
        String sql = String.format("SELECT * FROM %s",
                EmployeeBD.Tables.EMPLOYEE);
        return  database.rawQuery(sql, null);
    }

    public Cursor getEmployeeById(String id){
        SQLiteDatabase database = db.getReadableDatabase();
        String sql = String.format("SELECT * FROM %s" +
                        " WHERE %s=?",
                EmployeeBD.Tables.EMPLOYEE,
                EmployeeContract.Employee.EMP_NO);
        String[] whereArgs = {id};
        return database.rawQuery(sql, whereArgs);
    }

    public String insertEmployee (Employee employee){
        SQLiteDatabase database = db.getWritableDatabase();
        String idEmployee = EmployeeContract.Employee.generateIdProduct();
        ContentValues values = new ContentValues();
        values.put(EmployeeContract.Employee.EMP_NO, idEmployee);
        values.put(EmployeeContract.Employee.BITH_DATE, employee.getBirthDate());
        values.put(EmployeeContract.Employee.FIST_NAME, employee.getFirstName());
        values.put(EmployeeContract.Employee.lAST_NAME, employee.getLastName());
        database.insertOrThrow(EmployeeBD.Tables.EMPLOYEE, null, values);
        return idEmployee;
    }

    public boolean updateEmployee(Employee employee){
        SQLiteDatabase database = db.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(EmployeeContract.Employee.BITH_DATE, employee.getBirthDate());
        values.put(EmployeeContract.Employee.FIST_NAME, employee.getFirstName());
        values.put(EmployeeContract.Employee.lAST_NAME, employee.getLastName());
        String whereClause = String.format("%s=?", EmployeeContract.Employee.EMP_NO);
        String[] whereArgs = {employee.getEmpNo()};
        int result = database.update(EmployeeBD.Tables.EMPLOYEE,
                values, whereClause, whereArgs);
        return result>0;
    }

    public boolean deleteEmployee(String idEmployee){
        SQLiteDatabase database = db.getWritableDatabase();
        String whereClause = EmployeeContract.Employee.EMP_NO + "=?";
        String[] whereArgs = {idEmployee};
        int result =  database.delete(EmployeeBD.Tables.EMPLOYEE,
                whereClause, whereArgs);
        return result > 0;
    }
    // Operations Customers
    public Cursor getCustomers() {
        SQLiteDatabase database = db.getReadableDatabase();
        String sql = String.format("SELECT * FROM %s",
                EmployeeBD.Tables.CUSTOMER);
        return database.rawQuery(sql, null);
    }
    public Cursor getCustomersById(String idCustomer) {
        SQLiteDatabase database = db.getReadableDatabase();
        String sql = String.format("SELECT * FROM %s WHERE %s=?",
                EmployeeBD.Tables.CUSTOMER,
                EmployeeContract.Customers.ID);
        String[] whereArgs ={idCustomer};
        return database.rawQuery(sql, whereArgs);
    }

    public String insertCustomer(Customer customer) {
        SQLiteDatabase database = db.getWritableDatabase();
        String idCustomer = EmployeeContract.Customers.generateIdCustomer();
        ContentValues values = new ContentValues();
        values.put(EmployeeContract.Customers.ID, idCustomer);
        values.put(EmployeeContract.Customers.FIRSTNAME, customer.getFirstname());
        values.put(EmployeeContract.Customers.LASTNAME, customer.getLastname());
        values.put(EmployeeContract.Customers.PHONE, customer.getPhone());

        return database.insertOrThrow(EmployeeBD.Tables.CUSTOMER,
                null, values) > 0 ? idCustomer : null;
    }

    public boolean updateCustomer(Customer customer) {
        SQLiteDatabase database = db.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(EmployeeContract.Customers.FIRSTNAME,
                customer.getFirstname());
        values.put(EmployeeContract.Customers.LASTNAME,
                customer.getLastname());
        values.put(EmployeeContract.Customers.PHONE,
                customer.getPhone());
        String whereClause = String.format("%s=?",
                EmployeeContract.Customers.ID);
        final String[] whereArgs = {customer.getIdCustomer()};
        int result = database.update(EmployeeBD.Tables.CUSTOMER,
                values, whereClause, whereArgs);
        return result > 0;
    }
    public boolean deleteCustomer(String idCustomer) {
        SQLiteDatabase database = db.getWritableDatabase();
        String whereClause = String.format("%s=?",
                EmployeeContract.Customers.ID);
        final String[] whereArgs = {idCustomer};
        int result = database.delete(EmployeeBD.Tables.CUSTOMER, whereClause, whereArgs);
        return result > 0;
    }

    // Operation Method of payment
    public Cursor getMethodsPayment() {
        SQLiteDatabase database = db.getReadableDatabase();
        String sql = String.format("SELECT * FROM %s",
                EmployeeBD.Tables.METHOD_PAYMENT);
        return database.rawQuery(sql, null);
    }

    public Cursor getMethodsPaymentById(String idMethodPayment) {
        SQLiteDatabase database = db.getReadableDatabase();
        String sql = String.format("SELECT * FROM %s WHERE %s=?",
                EmployeeBD.Tables.METHOD_PAYMENT,
                EmployeeContract.MethodsPayment.ID);
        String[] whereArgs = {idMethodPayment};
        return database.rawQuery(sql, null);
    }
    public String insertMethodPayment(MethodPayment methodPayment) {
        SQLiteDatabase database = db.getWritableDatabase();

        String idMethodPayment = EmployeeContract.MethodsPayment.generateIdMethodPayment();

        ContentValues values = new ContentValues();
        values.put(EmployeeContract.MethodsPayment.ID, idMethodPayment);
        values.put(EmployeeContract.MethodsPayment.NAME, methodPayment.getName());

        return database.insertOrThrow(
                EmployeeBD.Tables.METHOD_PAYMENT, null,
                values) > 0 ? idMethodPayment : null;
    }

    public boolean updateMethodPayment(MethodPayment methodPayment) {
        SQLiteDatabase database = db.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(EmployeeContract.MethodsPayment.NAME,
                methodPayment.getName());
        String whereClause = String.format("%s=?",
                EmployeeContract.MethodsPayment.ID);
        String[] whereArgs = {methodPayment.getIdMethodPayment()};
        int result = database.update(
                EmployeeBD.Tables.METHOD_PAYMENT, values,
                whereClause, whereArgs);
        return result > 0;
    }

    public boolean deleteMethodPayment(String idMethodPayment) {
        SQLiteDatabase database = db.getWritableDatabase();
        String whereClause = String.format("%s=?", EmployeeContract.MethodsPayment.ID);
        String[] whereArgs = {idMethodPayment};
        int result = database.delete(EmployeeBD.Tables.METHOD_PAYMENT, whereClause, whereArgs);
        return result > 0;
    }

    public SQLiteDatabase getDb() {
        return db.getWritableDatabase();
    }


}
