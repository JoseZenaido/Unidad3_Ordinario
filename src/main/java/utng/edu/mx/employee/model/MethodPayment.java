package utng.edu.mx.employee.model;

/**
 * Created by ANONYMOUS on 2/7/2017.
 */

public class MethodPayment {
    private String idMethodPayment;
    private String name;

    public MethodPayment(String idMethodPayment, String name) {
        this.idMethodPayment = idMethodPayment;
        this.name = name;
    }
    public MethodPayment(){
        this("","");
    }

    public String getIdMethodPayment() {
        return idMethodPayment;
    }

    public void setIdMethodPayment(String idMethodPayment) {
        this.idMethodPayment = idMethodPayment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "MethodPayment{" +
                "idMethodPayment='" + idMethodPayment + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
