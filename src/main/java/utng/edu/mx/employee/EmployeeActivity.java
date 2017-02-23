package utng.edu.mx.employee;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


import utng.edu.mx.employee.adapters.EmployeeAdapter;
import utng.edu.mx.employee.model.Employee;
import utng.edu.mx.employee.sqlite.DBOperations;

public class EmployeeActivity extends AppCompatActivity {
    private EditText etBithDate;
    private EditText etLastName;
    private EditText etFistName;
    private Button btSaveEmployee;
private DBOperations operations;
    private Employee employee = new Employee();
    private RecyclerView rvEmployee;
   private List<Employee> employees=new ArrayList<Employee>();
private EmployeeAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);
       //iniciacion de la isntancia
        operations=DBOperations.getDBOperations(getApplicationContext());
        employee.setEmpNo("");


        initComponents();
    }
    private void initComponents(){
        rvEmployee=(RecyclerView)findViewById(R.id.rv_product_list);
        rvEmployee.setHasFixedSize(true);
        LinearLayoutManager layoutManeger=new LinearLayoutManager(this);
        rvEmployee.setLayoutManager(layoutManeger);
        //
       getEmployee();
        adapter=new EmployeeAdapter(employees);

        adapter.setListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.bt_delete_employee:
                        operations.deleteEmployee(employees.get(rvEmployee.getChildPosition((View)v.getParent().getParent())).getEmpNo());
                        getEmployee();
                        adapter.notifyDataSetChanged();
                        break;
                    case R.id.bt_edit_employee:

                        Toast.makeText(getApplicationContext(),"Editar", Toast.LENGTH_SHORT).show();
                        Cursor c = operations.getEmployeeById(employees.get(
                                rvEmployee.getChildPosition(
                                        (View)v.getParent().getParent())).getEmpNo());
                            if (c!=null){
                                if (c.moveToFirst()){
                                    employee = new Employee(c.getString(1),c.getString(2),c.getString(3),c.getString(4));
                                }
                                etBithDate.setText(employee.getBirthDate());
                                etFistName.setText(String.valueOf(employee.getFirstName()));
                                etLastName.setText(String.valueOf(employee.getLastName()));
                            }else{
                                Toast.makeText(getApplicationContext(),"Registro no encontrado", Toast.LENGTH_SHORT).show();
                            }
                        break;
                }

            }
        });
        rvEmployee.setAdapter(adapter);

        etBithDate=(EditText)findViewById(R.id.et_bith_date);
        etLastName=(EditText)findViewById(R.id.et_last_name);
        etFistName=(EditText)findViewById(R.id.et_first_name);

        btSaveEmployee=(Button)findViewById(R.id.bt_save_product);

        btSaveEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!employee.getEmpNo().equals("")){
                    employee.setBirthDate(etBithDate.getText().toString());
                    employee.setFirstName(etLastName.getText().toString());
                    employee.setLastName(etFistName.getText().toString());
                    operations.updateEmployee(employee);

                }else {
                    employee = new Employee("", etBithDate.getText().toString(),   etFistName.getText().toString(),  etLastName.getText().toString());
                    operations.insertEmployee(employee);
                }
                getEmployee();
                clearData();
                adapter.notifyDataSetChanged();
            }
        });

    }
    private void getEmployee(){
        Cursor c =operations.getEmployee();
        employees.clear();
        if(c!=null){
            while (c.moveToNext()){
                employees.add(new Employee(c.getString(1),c.getString(2), c.getString(3),c.getString(4)));
            }
        }

    }

    private void clearData(){
        etBithDate.setText("");
        etFistName.setText("");
        etLastName.setText("");
        employee=null;
        employee= new Employee();
        employee.setEmpNo("");
    }
}
