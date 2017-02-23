package utng.edu.mx.employee.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import utng.edu.mx.employee.R;
import utng.edu.mx.employee.model.Employee;
import utng.edu.mx.employee.sqlite.EmployeeContract;

/**
 * Created by ANONYMOUS on 2/15/2017.
 */

public class EmployeeAdapter extends RecyclerView.Adapter <EmployeeAdapter.EmployeeViewHolder>
        implements View.OnClickListener {
    List<Employee> employees;
    View.OnClickListener listener;
    //Constructor
    public EmployeeAdapter(List<Employee> employees){
        this.employees=employees;
    }
    //getter and setter de listener
    public View.OnClickListener getListener() {
        return listener;
    }

    public void setListener(View.OnClickListener listener) {
        this.listener = listener;
    }




    @Override
    public EmployeeAdapter.EmployeeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) { //se inflael cardview al reciclerview
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout,parent,false);
        EmployeeViewHolder holder=new EmployeeViewHolder(view);
        view.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(EmployeeAdapter.EmployeeViewHolder holder, int position) {
        holder.tvEmployeeBirthDate.setText(employees.get(position).getBirthDate());
        holder.tvEmployeeFisrtName.setText(String.valueOf(employees.get(position).getFirstName()));
        holder.tvEmployeeLastName.setText(String.valueOf(employees.get(position).getLastName()));
        holder.iv_Employee.setImageResource(R.mipmap.ic_launcher);
        holder.setListener(this);

    }

    @Override
    public int getItemCount() {
        return employees.size();
    }

    @Override
    public void onClick(View v) {
        if(listener!=null){
            listener.onClick(v);
        }
    }
    public static class EmployeeViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener{
        CardView cvEmployee;
        TextView tvEmployeeBirthDate;
        TextView tvEmployeeFisrtName;
        TextView tvEmployeeLastName;
        ImageView iv_Employee;
        ImageButton btEditEmployee;
        ImageButton btDeleteEmployee;
        View.OnClickListener listener;

        public EmployeeViewHolder(View itemView) {
            super(itemView);
            cvEmployee=(CardView)itemView.findViewById(R.id.cv_employee);
            iv_Employee=(ImageView)itemView.findViewById(R.id.iv_employee);
            tvEmployeeBirthDate=(TextView)itemView.findViewById(R.id.tv_employee_birth_date);
            tvEmployeeFisrtName=(TextView)itemView.findViewById(R.id.tv_employee_first_name);
            tvEmployeeLastName=(TextView)itemView.findViewById(R.id.tv_employee_last_name);

            btEditEmployee=(ImageButton) itemView.findViewById(R.id.bt_edit_employee);
            btDeleteEmployee=(ImageButton) itemView.findViewById(R.id.bt_delete_employee);
            btEditEmployee.setOnClickListener(this);
            btDeleteEmployee.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {
            if (listener!=null){
                listener.onClick(v);
            }
        }

        public void setListener(View.OnClickListener listener){
            this.listener=listener;

        }
    }
}
