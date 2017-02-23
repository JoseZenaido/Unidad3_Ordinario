package utng.edu.mx.employee;

import android.database.DatabaseUtils;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import java.util.Calendar;

import utng.edu.mx.employee.model.Customer;
import utng.edu.mx.employee.model.Employee;
import utng.edu.mx.employee.model.MethodPayment;
import utng.edu.mx.employee.model.OrderDetail;
import utng.edu.mx.employee.model.OrderHeader;
import utng.edu.mx.employee.sqlite.DBOperations;
import utng.edu.mx.employee.sqlite.EmployeeBD;


public class MainActivity extends AppCompatActivity {
    DBOperations data;
    public class DataTaskTest extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... params) {
            //inserciones
            String currentDate= Calendar.getInstance().getTime().toString();
            try{
                data.getDb().beginTransaction();
                //InsertCustomer
                String customer1=data.insertCustomer(new Customer(null,"Veronica","Del Topo","402558"));
                String customer2=data.insertCustomer(new Customer(null,"Carlos","Villagran","458852"));
                //Method
                String methodPayment1=data.insertMethodPayment(new MethodPayment(null,"Efectivo"));
                String methodPayment2=data.insertMethodPayment(new MethodPayment(null,"Cerdito"));
                //Products
                String employee1=data.insertEmployee(new Employee(null,"eded","Leche","dddd"));
                String employee2=data.insertEmployee(new Employee(null,"Galletas","sdsd","sdsd"));
                String employee3=data.insertEmployee(new Employee(null,"Queso","sdddd","dsds"));
                String employee4=data.insertEmployee(new Employee(null,"Papitas","ddd","ddd"));
                //Orders Header
                String order1=data.insertOrderHeader(new OrderHeader(null,customer1,methodPayment1,currentDate));
                String order2=data.insertOrderHeader(new OrderHeader(null,customer2,methodPayment2,currentDate));

                //Ordert dETAIL
                data.insertOrderDetail(new OrderDetail(order1,1,employee1,5,2));
                data.insertOrderDetail(new OrderDetail(order1,2,employee1,15,5));
                data.insertOrderDetail(new OrderDetail(order2,1,employee1,35,26));
                data.insertOrderDetail(new OrderDetail(order2,2,employee1,45,12));
                //ELIMINAR PEDIDO
                data.deleteOrderHeader(order1);
                //Actualizacioon
                data.updateCustomer(new Customer(customer2,"Jasmin ","Santana","418100256"));
                data.getDb().setTransactionSuccessful();

            }finally {
                data.getDb().endTransaction();
            }
            //Querys
            Log.d("Customers","Customers");
            DatabaseUtils.dumpCursor(data.getCustomers());
            Log.d("Method of Payment","Method of Payments");
            DatabaseUtils.dumpCursor(data.getMethodsPayment());
            Log.d("Employees","Employees");
            DatabaseUtils.dumpCursor(data.getEmployee());
            Log.d("Order Headers","Order HeaderS");
            DatabaseUtils.dumpCursor(data.getOrderDetails());
            Log.d("Order Details","Order Details");
            DatabaseUtils.dumpCursor(data.getOrderDetails());

            return null;
        }
    }
            @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
                Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
                setSupportActionBar(toolbar);
                getApplicationContext().deleteDatabase(EmployeeBD.DATABASE_NAME);
                data=DBOperations.getDBOperations(getApplicationContext());
                new DataTaskTest().execute();

    }
}
